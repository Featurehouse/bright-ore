package org.featurehouse.mcmod.brightore.mixin.uninline;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.featurehouse.mcmod.brightore.OreTagMap;

/*
 * [13:32:30] [Render thread/INFO] (SetBlockStateHacks) old=Block{minecraft:air} oldlight=OptionalInt.empty
 * [13:32:30] [Render thread/INFO] (SetBlockStateHacks) new=Block{minecraft:deepslate_emerald_ore} newlight=OptionalInt.empty
 * [13:32:30] [Render thread/INFO] (SetBlockStateHacks) newOLight.isPresent: false
 * [13:32:30] [Render thread/INFO] (SetBlockStateHacks) oldOLight!=newOLight: false
 */
public class TUninline {
    private static final Logger LOGGER = LogManager.getLogger("SetBlockStateHacks");

    static public
    /*private*/ void afterChange(World it,
                             BlockPos pos,
                             BlockState oldBlock,
                             BlockState newBlock) {
        if (!it.isClient()) return;   // only in client
        if (!oldBlock.equals(newBlock)) {
            var oldOLight = OreTagMap.INSTANCE.getOptional(oldBlock);
            var newOLight = OreTagMap.INSTANCE.getOptional(newBlock);
            LOGGER.debug("old={} oldlight={}", oldBlock, oldOLight);
            LOGGER.debug("new={} newlight={}", newBlock, newOLight);

            LOGGER.debug("newOLight.isPresent: {}", newOLight.isPresent());
            LOGGER.debug("oldOLight!=newOLight: {}", !oldOLight.equals(newOLight));

            if (newOLight.isPresent() && !oldOLight.equals(newOLight)) {
                LOGGER.debug("Apply at {} from {} to {}\n", pos, oldBlock, newBlock);

                it.getLightingProvider().addLightSource(pos, newOLight.getAsInt());
            } /*else {
                this.getLightingProvider().checkBlock(pos);
            }*/
            LOGGER.debug("");
        }
    }
}
