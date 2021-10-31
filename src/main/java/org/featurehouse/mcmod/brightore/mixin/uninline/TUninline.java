package org.featurehouse.mcmod.brightore.mixin.uninline;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.featurehouse.mcmod.brightore.OreTagMap;

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
            if (newOLight.isPresent() && !oldOLight.equals(newOLight)) {
                LOGGER.info("Apply at {} from {} to {}", pos, oldBlock, newBlock);
                it.getLightingProvider().addLightSource(pos, newOLight.getAsInt());
            } /*else {
                this.getLightingProvider().checkBlock(pos);
            }*/
        }
    }
}
