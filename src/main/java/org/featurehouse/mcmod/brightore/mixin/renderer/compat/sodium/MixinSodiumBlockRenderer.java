package org.featurehouse.mcmod.brightore.mixin.renderer.compat.sodium;

import me.jellysquid.mods.sodium.client.render.pipeline.BlockRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import org.featurehouse.mcmod.brightore.OreTagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(BlockRenderer.class)
public class MixinSodiumBlockRenderer {
    @Redirect(method = "getLightingMode",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getLuminance()I"))
    private int redirectLuminance(BlockState it) {
        return OreTagLoader.redirectLuminance(it);
    }
}
