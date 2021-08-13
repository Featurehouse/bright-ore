package org.featurehouse.mcmod.brightore.mixin.renderer.compat.indigo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import net.minecraft.block.BlockState;
import org.featurehouse.mcmod.brightore.OreTagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @see org.featurehouse.mcmod.brightore.mixin.renderer.MixinBlockModelRenderer
 */
@Mixin(BlockRenderInfo.class)
@Environment(EnvType.CLIENT)
@SuppressWarnings("mixin_config")
public class MixinBlockRenderInfo {
    @Redirect(
            method = "prepareForBlock(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getLuminance()I")
    )
    private int aoLuminanceRedirection(BlockState it) {
        return OreTagLoader.redirectLuminance(it);
    }
}
