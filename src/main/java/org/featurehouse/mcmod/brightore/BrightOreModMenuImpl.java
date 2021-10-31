package org.featurehouse.mcmod.brightore;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BrightOreModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        //return BrightOreConfigScreen::new;
        return !loadedClothConfig() ? ModMenuApi.super.getModConfigScreenFactory() :
                parent-> {
                    var builder = ConfigBuilder.create().setParentScreen(parent)
                            .setTitle(new TranslatableText("options.bright_ore"))
                            .setSavingRunnable(BrightOreConfig.INSTANCE::save)
                            .setDefaultBackgroundTexture(new Identifier("textures/block/stone.png"));
                    var mainCategory = builder.getOrCreateCategory(new TranslatableText("options.bright_ore"));
                    var entryBuilder = builder.entryBuilder();
                    mainCategory.addEntry(entryBuilder.startBooleanToggle(
                            new TranslatableText("options.bright_ore.render"), BrightOreConfig.INSTANCE.render())
                                    .setSaveConsumer(BrightOreConfig.INSTANCE::setRender).build()
                            ).addEntry(entryBuilder.startIntSlider(
                                    new TranslatableText("options.bright_ore.default_light"), BrightOreConfig.INSTANCE.defaultLight(),
                                    0, 30)
                            .setSaveConsumer(BrightOreConfig.INSTANCE::setDefaultLight)
                            .setTooltip(new TranslatableText("options.bright_ore.default_light.exp"))
                            .build()
                    );
                    return builder.setDoesConfirmSave(true).build();
                };
    }

    private static boolean loadedClothConfig() {
        if (loadedClothConfig == null)
            loadedClothConfig = FabricLoader.getInstance().isModLoaded("cloth-config2");
        return loadedClothConfig;
    } private static Boolean loadedClothConfig;
}
