package org.featurehouse.mcmod.brightore;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import static org.featurehouse.mcmod.brightore.BrightOreConfig.INSTANCE;

@Environment(EnvType.CLIENT)
public class BrightOreModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        //return BrightOreConfigScreen::new;
        return hasClothConfig2() ? Conf::conf :
                ModMenuApi.super.getModConfigScreenFactory();
    }

    private static Boolean hasClothConfig2;
    private static boolean hasClothConfig2() {
        if (hasClothConfig2 == null)
            hasClothConfig2 = FabricLoader.getInstance().isModLoaded("cloth-config2");
        return hasClothConfig2;
    }

    interface Conf {
        private static Screen conf(Screen parent) {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(new TranslatableText("options.bright_ore"))
                    .setSavingRunnable(() -> {
                        INSTANCE.save();
                        MinecraftClient.getInstance().worldRenderer.reload();
                    });
            var entryBuilder = builder.entryBuilder();
            builder.getOrCreateCategory(new TranslatableText("options.bright_ore"))
                    .addEntry(entryBuilder.startBooleanToggle(new TranslatableText("options.bright_ore.render"),
                            INSTANCE.render())
                            .setDefaultValue(true)
                            .setSaveConsumer(INSTANCE::setRender)
                            .build()
                    ).addEntry(entryBuilder.startIntSlider(new TranslatableText("options.bright_ore.default_light"),
                            INSTANCE.defaultLight(), 0, 30)
                            .setDefaultValue(BrightOreConfig.DEFAULT_LIGHT_PRESET)
                            .setSaveConsumer(INSTANCE::setDefaultLight)
                            .setTooltip(new TranslatableText("options.bright_ore.default_light.exp"))
                            .build()
                    );
            return builder.setDefaultBackgroundTexture(new Identifier("textures/block/stone.png"))
                    .build();
        }
    }
}
