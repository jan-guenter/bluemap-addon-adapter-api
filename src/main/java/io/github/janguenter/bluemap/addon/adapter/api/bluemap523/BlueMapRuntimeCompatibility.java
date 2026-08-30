/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.addon.adapter.api.bluemap523;

import de.bluecolored.bluemap.core.BlueMap;

/** Exact identity check for the tested BlueMap 5.23 feature backport. */
public final class BlueMapRuntimeCompatibility {

    private static final String VERSION =
            "5.22-feature.backport-5.23-stateless-java-web-server-46";
    private static final String COMMIT =
            "7e07f4e74ec1e92a6ead9aa1e66054af3e133aac";

    private BlueMapRuntimeCompatibility() {
    }

    /** Checks the loaded BlueMap runtime against the sole audited target. */
    public static boolean matchesCurrent() {
        return matches(BlueMap.VERSION, BlueMap.GIT_HASH);
    }

    /** Checks two values against the sole audited target. */
    public static boolean matches(String version, String gitHash) {
        return VERSION.equals(version) && COMMIT.equals(gitHash);
    }
}
