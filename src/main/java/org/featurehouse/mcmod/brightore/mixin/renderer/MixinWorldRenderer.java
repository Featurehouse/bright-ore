package org.featurehouse.mcmod.brightore.mixin.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.WorldRenderer;
import org.featurehouse.mcmod.brightore.OreTagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @see org.featurehouse.mcmod.brightore.mixin.renderer.compat.optifine.MixinOptiFineWorldRenderer
 */
@Mixin(value = WorldRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class MixinWorldRenderer {
    @Redirect(
            method = "getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;getLuminance()I"
            ),
            allow = 1
    )
    private static int blockLight(BlockState it) {
        return OreTagLoader.redirectLuminance(it);
    }
}
