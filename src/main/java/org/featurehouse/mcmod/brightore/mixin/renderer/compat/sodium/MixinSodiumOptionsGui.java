package org.featurehouse.mcmod.brightore.mixin.renderer.compat.sodium;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import org.featurehouse.mcmod.brightore.BrightOreModMenuImpl;
import org.featurehouse.mcmod.brightore.compat.sodium.BrightOreOption;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * @deprecated please use {@linkplain BrightOreModMenuImpl cloth
 * config screen} instead. This screen will not be supported and will
 * be removed by Minecraft 1.19.
 */
@Mixin(SodiumOptionsGUI.class)
@Environment(EnvType.CLIENT)
@SuppressWarnings("all")
public abstract class MixinSodiumOptionsGui {
    @Shadow @Final private List<OptionPage> pages;

    @Inject(method = "<init>(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("RETURN"))
    private void addPage(Screen prevScreen, CallbackInfo ci) {
        this.pages.add(BrightOreOption.brightOre());
    }
}
