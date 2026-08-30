/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.addon.adapter.api.bluemap523;

import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePackExtension;
import de.bluecolored.bluemap.core.util.Key;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class ResourceExtensionTypeTest {

    @Test
    void delegatesCreationToTheExactFactory() {
        Key key = Key.parse("test:extension");
        ResourcePack pack = mock(ResourcePack.class);
        TestExtension extension = new TestExtension();
        AtomicReference<ResourcePack> observed = new AtomicReference<>();
        ResourceExtensionType<TestExtension> type = new ResourceExtensionType<>(
                key,
                supplied -> {
                    observed.set(supplied);
                    return extension;
                }
        );

        assertSame(key, type.getKey());
        assertSame(extension, type.create(pack));
        assertSame(pack, observed.get());
    }

    private static final class TestExtension implements ResourcePackExtension {

        @Override
        public void loadResources(Iterable<Path> roots) throws IOException {
            // No resources are required for the factory contract test.
        }
    }
}
