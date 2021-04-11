package org.featurehouse.mcmod.brightore.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static org.featurehouse.mcmod.brightore.OreTagLoader.OreTagMap.INSTANCE;

@Mixin(WorldRenderer.class)
@Environment(EnvType.CLIENT)
class MixinBlockRenderer {
    @Redirect(
            method = "getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;getLuminance()I"
            )
    )
    private static int blockLight(BlockState it) {
        Block block1 = it.getBlock();
        if (INSTANCE.containsKey(block1))
            return INSTANCE.getValue(block1);
        else if (block1 instanceof OreBlock || block1 instanceof RedstoneOreBlock) {
            return 30;
        } else return it.getLuminance();
    }
}
