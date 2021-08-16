package org.featurehouse.mcmod.brightore.mixin.renderer.compat.sodium;

import me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess;
import net.minecraft.block.BlockState;
import org.featurehouse.mcmod.brightore.OreTagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightDataAccess.class)
public class MixinLightDataAccess {
    @Redirect(method = "compute(III)J", at = @At(
            value = "INVOKE",
            target = "net.minecraft.block.BlockState.getLuminance()I"
    ))
    private int redirectLuminance(BlockState it) {
        return OreTagLoader.redirectLuminance(it);
    }
}
