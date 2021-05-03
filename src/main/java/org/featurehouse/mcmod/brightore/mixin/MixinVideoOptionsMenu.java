package org.featurehouse.mcmod.brightore.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.featurehouse.mcmod.brightore.BrightOreConfigScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
abstract class MixinVideoOptionsMenu extends GameOptionsScreen {
    @Shadow private ButtonListWidget list;

    private MixinVideoOptionsMenu(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "init()V",
            at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/client/gui/widget/ButtonListWidget;addAll([Lnet/minecraft/client/option/Option;)V"
    ))
    private void onInit(CallbackInfo ci) {
        VideoOptionsScreen it = (VideoOptionsScreen) (Object) this;
        this.list.addSingleOptionEntry(BrightOreConfigScreen.asOption(it, client));
    }
}
