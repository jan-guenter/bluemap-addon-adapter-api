/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.addon.adapter.api.bluemap523;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlueMapRuntimeCompatibilityTest {

    @Test
    void acceptsOnlyTheExactFeatureBackportPair() {
        assertTrue(BlueMapRuntimeCompatibility.matches(
                "5.22-feature.backport-5.23-stateless-java-web-server-46",
                "7e07f4e74ec1e92a6ead9aa1e66054af3e133aac"
        ));
        assertFalse(BlueMapRuntimeCompatibility.matches(
                "5.23", "7e07f4e74ec1e92a6ead9aa1e66054af3e133aac"
        ));
        assertFalse(BlueMapRuntimeCompatibility.matches(
                "5.22-feature.backport-5.23-stateless-java-web-server-46",
                "0000000000000000000000000000000000000000"
        ));
        assertFalse(BlueMapRuntimeCompatibility.matches(null, null));
    }
}
