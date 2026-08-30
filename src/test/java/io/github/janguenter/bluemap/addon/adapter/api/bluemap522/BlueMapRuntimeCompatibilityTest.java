/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.addon.adapter.api.bluemap522;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlueMapRuntimeCompatibilityTest {

    @Test
    void acceptsOnlyExactConsumerSelectedPairs() {
        RuntimeIdentity upstream = BlueMapRuntimeCompatibility.UPSTREAM_5_22;
        RuntimeIdentity backport = BlueMapRuntimeCompatibility.ATMONS_5_22_BACKPORT;
        RuntimeIdentity feature =
                BlueMapRuntimeCompatibility.ATMONS_5_23_FEATURE_BACKPORT;

        assertTrue(BlueMapRuntimeCompatibility.matches(
                upstream.version(), upstream.gitHash(), upstream, backport, feature
        ));
        assertTrue(BlueMapRuntimeCompatibility.matches(
                backport.version(), backport.gitHash(), upstream, backport, feature
        ));
        assertTrue(BlueMapRuntimeCompatibility.matches(
                feature.version(), feature.gitHash(), upstream, backport, feature
        ));
        assertFalse(BlueMapRuntimeCompatibility.matches(
                "5.23", feature.gitHash(), upstream, backport, feature
        ));
        assertFalse(BlueMapRuntimeCompatibility.matches(
                upstream.version(), backport.gitHash(), upstream, backport, feature
        ));
        assertFalse(BlueMapRuntimeCompatibility.matches(null, null, upstream));
    }

    @Test
    void leavesTheAcceptedSubsetToTheConsumer() {
        RuntimeIdentity onlyBackport = BlueMapRuntimeCompatibility.ATMONS_5_22_BACKPORT;
        assertTrue(BlueMapRuntimeCompatibility.matches(
                onlyBackport.version(), onlyBackport.gitHash(), onlyBackport
        ));
        assertFalse(BlueMapRuntimeCompatibility.matches(
                BlueMapRuntimeCompatibility.UPSTREAM_5_22.version(),
                BlueMapRuntimeCompatibility.UPSTREAM_5_22.gitHash(),
                onlyBackport
        ));
        assertThrows(NullPointerException.class, () ->
                BlueMapRuntimeCompatibility.matches(
                        "5.22", "commit", (RuntimeIdentity[]) null
                )
        );
    }
}
