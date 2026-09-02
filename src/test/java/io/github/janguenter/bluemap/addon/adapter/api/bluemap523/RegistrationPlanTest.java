/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.addon.adapter.api.bluemap523;

import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.util.Keyed;
import de.bluecolored.bluemap.core.util.Registry;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistrationPlanTest {

    @Test
    void emptyPlanIsReusableAndSuccessful() {
        RegistrationPlan plan = RegistrationPlan.empty();

        assertTrue(plan.canApply());
        assertTrue(plan.apply());
        assertTrue(plan.apply());
    }

    @Test
    void appliesHeterogeneousRegistriesInInsertionOrder() {
        List<String> order = new ArrayList<>();
        RecordingRegistry<FirstEntry> firstRegistry = new RecordingRegistry<>(order, "first");
        RecordingRegistry<SecondEntry> secondRegistry = new RecordingRegistry<>(order, "second");
        FirstEntry first = new FirstEntry(Key.parse("test:first"));
        SecondEntry second = new SecondEntry(Key.parse("test:second"));
        RegistrationPlan plan = RegistrationPlan.empty()
                .add(firstRegistry, first)
                .add(secondRegistry, second);

        assertTrue(plan.canApply());
        assertTrue(plan.apply());
        assertSame(first, firstRegistry.get(first.getKey()));
        assertSame(second, secondRegistry.get(second.getKey()));
        assertEquals(List.of("first", "second"), order);
    }

    @Test
    void acceptsAnAlreadyOwnedCandidateByObjectIdentity() {
        Registry<FirstEntry> registry = new Registry<>();
        FirstEntry candidate = new FirstEntry(Key.parse("test:owned"));
        registry.register(candidate);
        RegistrationPlan plan = RegistrationPlan.empty().add(registry, candidate);

        assertTrue(plan.canApply());
        assertTrue(plan.apply());
        assertSame(candidate, registry.get(candidate.getKey()));
    }

    @Test
    void collisionPreflightLeavesEveryRegistryUntouched() {
        Registry<FirstEntry> firstRegistry = new Registry<>();
        Registry<SecondEntry> secondRegistry = new Registry<>();
        FirstEntry existing = new FirstEntry(Key.parse("test:collision"));
        FirstEntry collision = new FirstEntry(existing.getKey());
        SecondEntry later = new SecondEntry(Key.parse("test:later"));
        firstRegistry.register(existing);
        RegistrationPlan plan = RegistrationPlan.empty()
                .add(firstRegistry, collision)
                .add(secondRegistry, later);

        assertFalse(plan.canApply());
        assertFalse(plan.apply());
        assertSame(existing, firstRegistry.get(existing.getKey()));
        assertNull(secondRegistry.get(later.getKey()));
    }

    @Test
    void failedReadBackStopsWithoutRollingBackEarlierEntries() {
        Registry<FirstEntry> firstRegistry = new Registry<>();
        Registry<SecondEntry> hostileRegistry = new DroppingRegistry<>();
        FirstEntry first = new FirstEntry(Key.parse("test:first"));
        SecondEntry dropped = new SecondEntry(Key.parse("test:dropped"));
        RegistrationPlan plan = RegistrationPlan.empty()
                .add(firstRegistry, first)
                .add(hostileRegistry, dropped);

        assertTrue(plan.canApply());
        assertFalse(plan.apply());
        assertSame(first, firstRegistry.get(first.getKey()));
        assertNull(hostileRegistry.get(dropped.getKey()));
    }

    @Test
    void addingAnEntryDoesNotMutateTheOriginalPlan() {
        Registry<FirstEntry> registry = new Registry<>();
        FirstEntry candidate = new FirstEntry(Key.parse("test:immutable"));
        RegistrationPlan base = RegistrationPlan.empty();
        RegistrationPlan extended = base.add(registry, candidate);

        assertTrue(base.apply());
        assertNull(registry.get(candidate.getKey()));
        assertTrue(extended.apply());
        assertSame(candidate, registry.get(candidate.getKey()));
    }

    @Test
    void rejectsNullRegistryAndCandidateAtPlanConstruction() {
        Registry<FirstEntry> registry = new Registry<>();
        FirstEntry candidate = new FirstEntry(Key.parse("test:candidate"));

        assertThrows(NullPointerException.class, () -> RegistrationPlan.empty().add(null, candidate));
        assertThrows(NullPointerException.class, () -> RegistrationPlan.empty().add(registry, null));
        assertDoesNotThrow(() -> RegistrationPlan.empty().add(registry, candidate));
    }

    private record FirstEntry(Key key) implements Keyed {

        @Override
        public Key getKey() {
            return key;
        }
    }

    private record SecondEntry(Key key) implements Keyed {

        @Override
        public Key getKey() {
            return key;
        }
    }

    private static final class RecordingRegistry<T extends Keyed> extends Registry<T> {

        private final List<String> order;
        private final String label;

        private RecordingRegistry(List<String> order, String label) {
            this.order = order;
            this.label = label;
        }

        @Override
        public boolean register(T entry) {
            order.add(label);
            return super.register(entry);
        }
    }

    private static final class DroppingRegistry<T extends Keyed> extends Registry<T> {

        @Override
        public boolean register(T entry) {
            return false;
        }
    }
}
