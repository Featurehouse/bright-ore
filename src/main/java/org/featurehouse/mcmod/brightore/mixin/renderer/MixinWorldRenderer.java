package org.featurehouse.mcmod.brightore.mixin.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.featurehouse.mcmod.brightore.OreTagLoader;
import org.featurehouse.mcmod.brightore.OreTagMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @see org.featurehouse.mcmod.brightore.mixin.renderer.compat.optifine.MixinOptiFineWorldRenderer
 */
@Mixin(value = WorldRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class MixinWorldRenderer {
    @Inject(method = "getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I",
    at = @At("RETURN"), allow = 1, cancellable = true)
    private static void blockLight(BlockRenderView world, BlockState state, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        int value = cir.getReturnValueI();
        if (OreTagMap.INSTANCE.containsKey(state.getBlock())) {
            int newLight = OreTagMap.INSTANCE.getValue(state.getBlock());
            value &= 0xFFFF0000; // 15728880 is 0x00F000F0
            value |= (newLight << 4);
            cir.setReturnValue(value);
        }
    }
}
