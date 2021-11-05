package org.featurehouse.mcmod.brightore.mixin.renderer;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.light.LightingProvider;
import org.apache.logging.log4j.Logger;
import org.featurehouse.mcmod.brightore.OreTagMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
abstract class SetBlockStateHacks {

    @Shadow public abstract LightingProvider getLightingProvider();

    @Shadow @Final protected static Logger LOGGER;

    /*
     * [13:32:30] [Render thread/INFO] (SetBlockStateHacks) old=Block{minecraft:air} oldlight=OptionalInt.empty
     * [13:32:30] [Render thread/INFO] (SetBlockStateHacks) new=Block{minecraft:deepslate_emerald_ore} newlight=OptionalInt.empty
     * [13:32:30] [Render thread/INFO] (SetBlockStateHacks) newOLight.isPresent: false
     * [13:32:30] [Render thread/INFO] (SetBlockStateHacks) oldOLight!=newOLight: false
     */
    @Inject(method = "onBlockChanged", at = @At("HEAD"))
    private void afterChange(BlockPos pos,
                             BlockState oldBlock,
                             BlockState newBlock,
                             CallbackInfo ci) {
        if (!((World)(Object) this).isClient()) return;   // only in client
        if (!oldBlock.equals(newBlock)) {
            var oldOLight = OreTagMap.INSTANCE.getOptional(oldBlock);
            var newOLight = OreTagMap.INSTANCE.getOptional(newBlock);
            LOGGER.debug("old={} oldlight={}", oldBlock, oldOLight);
            LOGGER.debug("new={} newlight={}", newBlock, newOLight);

            LOGGER.debug("newOLight.isPresent: {}", newOLight.isPresent());
            LOGGER.debug("oldOLight!=newOLight: {}", !oldOLight.equals(newOLight));

            if (newOLight.isPresent() && !oldOLight.equals(newOLight)) {
                LOGGER.debug("Apply at {} from {} to {}\n", pos, oldBlock, newBlock);

                getLightingProvider().addLightSource(pos, newOLight.getAsInt());
            } /*else {
                this.getLightingProvider().checkBlock(pos);
            }*/
            LOGGER.debug("");
        }
    }
}
