package org.featurehouse.mcmod.brightore.mixin.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.featurehouse.mcmod.brightore.BrightOreModMenuImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @deprecated please use {@linkplain BrightOreModMenuImpl cloth
 * config screen} instead. This screen will not be supported and will
 * be removed by Minecraft 1.19.
 */
@Mixin(VideoOptionsScreen.class)
abstract class MixinVideoOptionsMenu extends GameOptionsScreen {
    @Shadow private ButtonListWidget list;

    private MixinVideoOptionsMenu(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    /*@Inject(method = "init()V",
            at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/client/gui/widget/ButtonListWidget;addAll([Lnet/minecraft/client/option/Option;)V",
            ordinal = 0
    ))*/
    @Inject(method = "init", at = @At("RETURN"))
    @SuppressWarnings("all")
    private void onInit(CallbackInfo ci) {
        VideoOptionsScreen it = (VideoOptionsScreen) (Object) this;
        this.list.addSingleOptionEntry(org.featurehouse.mcmod.brightore.BrightOreConfigScreen.asOption(it, client));
    }
}
