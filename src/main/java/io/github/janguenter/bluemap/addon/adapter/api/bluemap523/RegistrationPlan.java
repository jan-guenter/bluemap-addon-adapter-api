/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.addon.adapter.api.bluemap523;

import de.bluecolored.bluemap.core.util.Keyed;
import de.bluecolored.bluemap.core.util.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** An immutable, ordered set of identity-safe BlueMap registry operations. */
public final class RegistrationPlan {

    private static final RegistrationPlan EMPTY = new RegistrationPlan(List.of());

    private final List<Registration> registrations;

    private RegistrationPlan(List<Registration> registrations) {
        this.registrations = registrations;
    }

    /** Returns an empty plan. */
    public static RegistrationPlan empty() {
        return EMPTY;
    }

    /** Returns a new plan with one registry candidate appended. */
    public <T extends Keyed> RegistrationPlan add(Registry<T> registry, T candidate) {
        Objects.requireNonNull(registry, "registry");
        Objects.requireNonNull(candidate, "candidate");
        List<Registration> additions = new ArrayList<>(registrations.size() + 1);
        additions.addAll(registrations);
        additions.add(new TypedRegistration<>(registry, candidate));
        return new RegistrationPlan(List.copyOf(additions));
    }

    /** Checks every candidate without mutating a registry. */
    public boolean canApply() {
        return registrations.stream().allMatch(Registration::canApply);
    }

    /**
     * Preflights the complete plan, then applies candidates in insertion order.
     * A failed identity read-back stops the plan without rolling back earlier entries.
     */
    public boolean apply() {
        if (!canApply()) {
            return false;
        }
        for (Registration registration : registrations) {
            if (!registration.apply()) {
                return false;
            }
        }
        return true;
    }

    private interface Registration {

        boolean canApply();

        boolean apply();
    }

    private record TypedRegistration<T extends Keyed>(Registry<T> registry, T candidate)
            implements Registration {

        @Override
        public boolean canApply() {
            return RegistryGuard.canRegister(registry, candidate);
        }

        @Override
        public boolean apply() {
            return RegistryGuard.register(registry, candidate);
        }
    }
}
