package org.featurehouse.mcmod.brightore.mixin.renderer.compat.sodium;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import org.featurehouse.mcmod.brightore.compat.sodium.BrightOreOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SodiumOptionsGUI.class)
@Environment(EnvType.CLIENT)
@SuppressWarnings("mixin")
public abstract class MixinSodiumOptionsGui {
    @Accessor("pages")
    abstract List<OptionPage> getPages();

    @Inject(method = "<init>(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("RETURN"))
    private void addPage(Screen prevScreen, CallbackInfo ci) {
        this.getPages().add(BrightOreOption.brightOre());
    }
}
