package org.featurehouse.mcmod.brightore.mixin.renderer.compat.optifine;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.featurehouse.mcmod.brightore.OreTagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @see org.featurehouse.mcmod.brightore.mixin.renderer.MixinWorldRenderer
 */
@Mixin(WorldRenderer.class)
@Environment(EnvType.CLIENT)
@SuppressWarnings("optifine-modification")
public class MixinOptiFineWorldRenderer {
    // See: class_2680 /* BlockState */ .getLightValue(BlockView, BlockPos)
    @Redirect(
            method = "getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;" +
                    "Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I",
            at = @At(value = "INVOKE", target = "net.minecraft.block.BlockState.getLightValue" +
                    "(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)I")
    )
    private static int redirectGetLightValue(BlockState it, BlockView world, BlockPos pos) {
        return OreTagLoader.redirectLuminance(it);
    }
}
