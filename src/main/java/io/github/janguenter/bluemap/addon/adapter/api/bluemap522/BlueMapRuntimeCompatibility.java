/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.addon.adapter.api.bluemap522;

import de.bluecolored.bluemap.core.BlueMap;

import java.util.Objects;

/** Exact BlueMap identities audited for the BlueMap 5.22 adapter ABI. */
public final class BlueMapRuntimeCompatibility {

    /** Upstream BlueMap 5.22. */
    public static final RuntimeIdentity UPSTREAM_5_22 = new RuntimeIdentity(
            "5.22",
            "fe5115d5548a30d34175b8e0449aaca280af199f"
    );

    /** Released ATMons Java 21 backport of BlueMap 5.22. */
    public static final RuntimeIdentity ATMONS_5_22_BACKPORT = new RuntimeIdentity(
            "5.22-agent.backport-5.22-mc1.21.1-2",
            "9be321df995a1103808621d529eb72773e719d4d"
    );

    /** Exact tested BlueMap 5.23 feature-backport runtime. */
    public static final RuntimeIdentity ATMONS_5_23_FEATURE_BACKPORT =
            new RuntimeIdentity(
                    "5.22-feature.backport-5.23-stateless-java-web-server-46",
                    "7e07f4e74ec1e92a6ead9aa1e66054af3e133aac"
            );

    private BlueMapRuntimeCompatibility() {
    }

    /**
     * Checks the loaded BlueMap runtime against identities selected by the consumer.
     *
     * <p>A match proves only the audited adapter ABI. Consumers retain narrower
     * mod-specific compatibility conditions.</p>
     */
    public static boolean matchesCurrent(RuntimeIdentity... acceptedIdentities) {
        return matches(BlueMap.VERSION, BlueMap.GIT_HASH, acceptedIdentities);
    }

    /** Checks two values against identities selected by the consumer. */
    public static boolean matches(
            String version,
            String gitHash,
            RuntimeIdentity... acceptedIdentities
    ) {
        Objects.requireNonNull(acceptedIdentities, "acceptedIdentities");
        for (RuntimeIdentity identity : acceptedIdentities) {
            if (Objects.requireNonNull(identity, "accepted identity")
                    .matches(version, gitHash)) {
                return true;
            }
        }
        return false;
    }
}
