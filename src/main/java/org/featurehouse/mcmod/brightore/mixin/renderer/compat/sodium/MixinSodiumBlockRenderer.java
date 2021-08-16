package org.featurehouse.mcmod.brightore.mixin.renderer.compat.sodium;

import me.jellysquid.mods.sodium.client.render.pipeline.BlockRenderer;
import net.minecraft.block.BlockState;
import org.featurehouse.mcmod.brightore.OreTagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockRenderer.class)
public class MixinSodiumBlockRenderer {
    @Redirect(method = "getLightingMode(Lnet/minecraft/block/BlockState;" +
            "Lnet/minecraft/client/render/model/BakedModel;)" +
            "Lme/jellysquid/mods/sodium/client/model/light/LightMode;",
    at = @At(value = "INVOKE", target = "net.minecraft.block.AbstractBlock.AbstractBlockState.getLuminance()I"))
    private int redirectLuminance(BlockState it) {
        return OreTagLoader.redirectLuminance(it);
    }
}
