/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.addon.adapter.api.bluemap522;

import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variant;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.VariantSet;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variants;

/** Exact validation for the add-ons' synthetic missing-model dispatch states. */
public final class SyntheticDispatch {

    private SyntheticDispatch() {
    }

    /** Checks a complete synthetic block state and its only default variant. */
    public static boolean matches(
            BlockState blockState,
            BlockRendererType expectedRenderer
    ) {
        if (blockState == null || blockState.getMultipart() != null) {
            return false;
        }
        Variants variants = blockState.getVariants();
        if (variants == null
                || variants.getVariants().length != 0
                || variants.getDefaultVariant() == null) {
            return false;
        }
        VariantSet defaultVariant = variants.getDefaultVariant();
        return defaultVariant.getVariants().length == 1
                && matchesVariant(defaultVariant.getVariants()[0], expectedRenderer);
    }

    /** Checks the exact renderer, missing-model, transform, UV and weight tuple. */
    private static boolean matchesVariant(
            Variant variant,
            BlockRendererType expectedRenderer
    ) {
        return variant != null
                && expectedRenderer != null
                && variant.getRenderer() == expectedRenderer
                && ResourcePack.MISSING_BLOCK_MODEL.equals(variant.getModel())
                && !variant.isTransformed()
                && !variant.isUvlock()
                && Double.compare(variant.getWeight(), 1D) == 0;
    }
}
