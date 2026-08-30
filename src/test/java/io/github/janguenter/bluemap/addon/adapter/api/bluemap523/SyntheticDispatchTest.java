/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.addon.adapter.api.bluemap523;

import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variant;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.VariantSet;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variants;
import de.bluecolored.bluemap.core.util.Key;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class SyntheticDispatchTest {

    @Test
    void acceptsTheExactSyntheticState() {
        BlockRendererType renderer = renderer("test:renderer");
        Variant variant = new Variant(ResourcePack.MISSING_BLOCK_MODEL);
        variant.setRenderer(renderer);
        BlockState state = new BlockState(new Variants(
                new VariantSet[0],
                new VariantSet(variant)
        ));

        assertTrue(SyntheticDispatch.matches(state, renderer));
    }

    @Test
    void rejectsCompetingOrMalformedDispatch() {
        BlockRendererType renderer = renderer("test:renderer");
        Variant expected = new Variant(ResourcePack.MISSING_BLOCK_MODEL);
        expected.setRenderer(renderer);
        Variant wrongModel = new Variant(ResourcePack.MISSING_ENTITY_MODEL);
        wrongModel.setRenderer(renderer);

        assertFalse(SyntheticDispatch.matches(
                new BlockState(new Variants(
                        new VariantSet[0],
                        new VariantSet(wrongModel)
                )),
                renderer
        ));
        assertFalse(SyntheticDispatch.matches(
                new BlockState(new Variants(
                        new VariantSet[0],
                        new VariantSet(expected)
                )),
                null
        ));
        assertFalse(SyntheticDispatch.matches(
                new BlockState(new Variants(
                        new VariantSet[]{new VariantSet(expected)},
                        new VariantSet(expected)
                )),
                renderer
        ));
    }

    private static BlockRendererType renderer(String key) {
        BlockRendererType renderer = mock(BlockRendererType.class);
        org.mockito.Mockito.when(renderer.getKey()).thenReturn(Key.parse(key));
        return renderer;
    }
}
