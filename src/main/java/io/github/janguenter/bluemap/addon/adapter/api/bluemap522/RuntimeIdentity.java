/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.addon.adapter.api.bluemap522;

import java.util.Objects;

/** An exact BlueMap runtime version and source-commit pair. */
public record RuntimeIdentity(String version, String gitHash) {

    /** Creates an exact, non-null runtime identity. */
    public RuntimeIdentity {
        Objects.requireNonNull(version, "version");
        Objects.requireNonNull(gitHash, "gitHash");
    }

    boolean matches(String candidateVersion, String candidateGitHash) {
        return version.equals(candidateVersion) && gitHash.equals(candidateGitHash);
    }
}
