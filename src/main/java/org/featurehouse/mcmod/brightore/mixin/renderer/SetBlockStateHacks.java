package org.featurehouse.mcmod.brightore.mixin.renderer;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.light.LightingProvider;
import org.apache.logging.log4j.Logger;
import org.featurehouse.mcmod.brightore.OreTagMap;
import org.featurehouse.mcmod.brightore.mixin.uninline.TUninline;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
abstract class SetBlockStateHacks {
    @Shadow public abstract boolean isClient();

    @Shadow public abstract LightingProvider getLightingProvider();

    @Shadow @Final protected static Logger LOGGER;

    @Inject(method = "onBlockChanged", at = @At("HEAD"))
    private void afterChange(BlockPos pos,
                             BlockState oldBlock,
                             BlockState newBlock,
                             CallbackInfo ci) {
        TUninline.afterChange((World)(Object)this, pos, oldBlock, newBlock);
        /*if (!this.isClient()) return;   // only in client
        if (!oldBlock.equals(newBlock)) {
            var oldOLight = OreTagMap.INSTANCE.getOptional(oldBlock);
            var newOLight = OreTagMap.INSTANCE.getOptional(newBlock);
            if (newOLight.isPresent() && !oldOLight.equals(newOLight)) {
                LOGGER.info("Apply at {} from {} to {}", pos, oldBlock, newBlock);
                this.getLightingProvider().addLightSource(pos, newOLight.getAsInt());
            }
            //else {
            //    this.getLightingProvider().checkBlock(pos);
            //}
        }*/
    }
}
