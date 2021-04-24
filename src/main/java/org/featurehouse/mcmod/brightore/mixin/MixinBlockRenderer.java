package org.featurehouse.mcmod.brightore.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.client.render.WorldRenderer;
import org.featurehouse.mcmod.brightore.OreTagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static org.featurehouse.mcmod.brightore.OreTagLoader.OreTagMap.INSTANCE;

@Mixin(WorldRenderer.class)
@Environment(EnvType.CLIENT)
abstract class MixinBlockRenderer {
    @Redirect(
            method = "getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;getLuminance()I"
            )
    )
    private static int blockLight(BlockState it) {
        return OreTagLoader.redirectLuminance(it);
    }
}
