package org.featurehouse.mcmod.brightore.mixin.renderer.compat.optifine;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import org.featurehouse.mcmod.brightore.OreTagLoader;
import org.featurehouse.mcmod.brightore.OreTagMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    /*@Inject(at = @At("RETURN"), method = "getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;" +
            "Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I",
            cancellable = true
    )
    private static void processDynamicLights(BlockRenderView world, BlockState state, BlockPos pos,
                                             CallbackInfoReturnable<Integer> cir) {
        if (OFRefs.isDynamicLights() && OreTagMap.INSTANCE.containsKey(state.getBlock())) {

        }
    }*/
    /*@Redirect(method = "getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;" +
            "Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I",
            at = @At(value = "INVOKE", target = "net.optifine.DynamicLights.getCombinedLight" +
                    "(Lnet/minecraft/util/math/BlockPos;I)I")
    )
    private static int redirectGetCombinedLight(BlockPos pos, int light) {

    }*/
    @Inject(method = "getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft" +
            "/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I",
            at = @At("RETURN"), cancellable = true
    )
    private static void modifyBlockLightValue(BlockRenderView world, BlockState state, BlockPos pos,
                                              CallbackInfoReturnable<Integer> cir) {
        Block block = state.getBlock();
        if (!OreTagMap.INSTANCE.containsKey(block)) return;

        int val = cir.getReturnValueI();
        int oldLight = val & 0x0000_00FF;
        int brightOreLight = Math.min(15, OreTagMap.INSTANCE.getValue(block));
        if (brightOreLight > oldLight) {
            val &= 0xFFFF_FF00; // -256
            val |= brightOreLight;
            cir.setReturnValue(val);
        }
    }
}
