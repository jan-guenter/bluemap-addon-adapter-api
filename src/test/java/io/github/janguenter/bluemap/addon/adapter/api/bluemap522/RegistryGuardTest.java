/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.addon.adapter.api.bluemap522;

import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.util.Keyed;
import de.bluecolored.bluemap.core.util.Registry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistryGuardTest {

    @Test
    void registersAnEmptyKeyDespiteBlueMapsInvertedReturnValue() {
        Registry<Entry> registry = new Registry<>();
        Entry candidate = new Entry(Key.parse("test:candidate"));

        assertTrue(RegistryGuard.canRegister(registry, candidate));
        assertTrue(RegistryGuard.register(registry, candidate));
        assertSame(candidate, registry.get(candidate.getKey()));
        assertTrue(RegistryGuard.register(registry, candidate));
    }

    @Test
    void rejectsAnotherObjectAtTheSameKey() {
        Registry<Entry> registry = new Registry<>();
        Entry first = new Entry(Key.parse("test:collision"));
        Entry second = new Entry(Key.parse("test:collision"));
        registry.register(first);

        assertFalse(RegistryGuard.canRegister(registry, second));
        assertFalse(RegistryGuard.register(registry, second));
        assertSame(first, registry.get(first.getKey()));
    }

    private record Entry(Key key) implements Keyed {

        @Override
        public Key getKey() {
            return key;
        }
    }
}
