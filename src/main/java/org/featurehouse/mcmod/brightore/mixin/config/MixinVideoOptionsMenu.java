package org.featurehouse.mcmod.brightore.mixin.config;

import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import org.featurehouse.mcmod.brightore.BrightOreConfigScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @deprecated use {@linkplain org.featurehouse.mcmod.brightore.BrightOreModMenuImpl
 * ModMenu} with Cloth Config. This will be removed in 1.19.
 */
@SuppressWarnings(value="all")
@Mixin(VideoOptionsScreen.class)
abstract class MixinVideoOptionsMenu extends GameOptionsScreen {
    @Shadow private ButtonListWidget list;

    @Deprecated
    private MixinVideoOptionsMenu() {
        super(null, null, null);
    }

    /*@Inject(method = "init()V",
            at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/client/gui/widget/ButtonListWidget;addAll([Lnet/minecraft/client/option/Option;)V",
            ordinal = 0
    ))*/
    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        this.list.addSingleOptionEntry(
                BrightOreConfigScreen.asOption((VideoOptionsScreen) (Object) this, client));
    }
}
