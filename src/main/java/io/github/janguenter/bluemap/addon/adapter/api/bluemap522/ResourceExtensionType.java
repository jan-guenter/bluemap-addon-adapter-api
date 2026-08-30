/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.addon.adapter.api.bluemap522;

import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePackExtension;
import de.bluecolored.bluemap.core.util.Key;

import java.util.Objects;
import java.util.function.Function;

/** A keyed resource-pack extension type backed by one consumer-owned factory. */
public final class ResourceExtensionType<E extends ResourcePackExtension>
        implements ResourcePack.Extension<E> {

    private final Key key;
    private final Function<ResourcePack, E> factory;

    /** Creates an extension type without registering it. */
    public ResourceExtensionType(Key key, Function<ResourcePack, E> factory) {
        this.key = Objects.requireNonNull(key, "key");
        this.factory = Objects.requireNonNull(factory, "factory");
    }

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public E create(ResourcePack pack) {
        return factory.apply(pack);
    }
}
