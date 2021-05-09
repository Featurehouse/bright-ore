package org.featurehouse.mcmod.brightore.mixin;

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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = BlockModelRenderer.class, priority = 888)
@Environment(EnvType.CLIENT)
abstract class MixinSmoothBlockModelRenderer {
    @Shadow public abstract boolean renderFlat(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack buffer, VertexConsumer vertexConsumer, boolean cull, Random random, long l, int i);

    /**
     * If Client-side light is greater than zero, will be redirected
     * to {@link BlockModelRenderer#renderFlat(BlockRenderView, BakedModel, BlockState, BlockPos,
     * MatrixStack, VertexConsumer, boolean, Random, long, int)} BEFORE indigo. <br />
     *
     * See Also: {@code net.fabricmc.mixin.client.indigo.renderer.MixinBlockModelRenderer}
     *
     * @see BlockModelRenderer#renderFlat(BlockRenderView, BakedModel, BlockState, BlockPos,
     * MatrixStack, VertexConsumer, boolean, Random, long, int)
     */
    @Inject(at = @At("HEAD"), cancellable = true, method = "render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZLjava/util/Random;JI)Z")
    private void blockLight(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay, CallbackInfoReturnable<Boolean> cir) {
        int light = OreTagLoader.redirectLuminance(state);
        if (light > 0)
            cir.setReturnValue(this.renderFlat(world, model, state, pos, matrix, vertexConsumer, cull, random, seed, overlay));
    }
}