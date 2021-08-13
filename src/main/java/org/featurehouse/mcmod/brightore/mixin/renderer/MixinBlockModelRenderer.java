package org.featurehouse.mcmod.brightore.mixin.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.featurehouse.mcmod.brightore.OreTagLoader;
import org.featurehouse.mcmod.brightore.mixin.renderer.compat.indigo.MixinBlockRenderInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

/**
 * @see MixinBlockRenderInfo
 */
@Mixin(value = BlockModelRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class MixinBlockModelRenderer {
    @Shadow public abstract boolean renderFlat(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack buffer, VertexConsumer vertexConsumer, boolean cull, Random random, long l, int i);

    /**
     * If Client-side light is greater than zero, will be redirected
     * to {@link BlockModelRenderer#renderFlat(BlockRenderView, BakedModel, BlockState, BlockPos,
     * MatrixStack, VertexConsumer, boolean, Random, long, int)} BEFORE indigo. <br />
     *
     * <br />
     * Indigo is incompatible with Bright Ore. However, we do not want to shake it
     * away unfriendly. Indigo uses a
     * {@link org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin} to start/stop indigo
     * mixin. So just take a custom value on {@code fabric.mod.json}.<br />
     * <br />
     *
     * See Also: {@code net.minecraft.client.render.chunk.ChunkBuilder.RebuildTask#render}
     *
     * @see net.fabricmc.fabric.mixin.client.indigo.renderer.MixinBlockModelRenderer
     * @see net.fabricmc.fabric.mixin.client.indigo.renderer.MixinChunkRebuildTask
     * @see BlockModelRenderer#renderFlat(BlockRenderView, BakedModel, BlockState, BlockPos,
     * MatrixStack, VertexConsumer, boolean, Random, long, int)
     */
    @Inject(
            at = @At("HEAD"),
            cancellable = true,
            method = "render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZLjava/util/Random;JI)Z",
            allow = 1
    )
    private void blockLight(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay, CallbackInfoReturnable<Boolean> cir) {
        int light = OreTagLoader.redirectLuminance(state);

        if (light > 0) {
            //System.out.printf("Force Render Flat at %s with light ", pos);
            cir.setReturnValue(this.renderFlat(world, model, state, pos, matrix, vertexConsumer, cull, random, seed, overlay));
        }
    }
}