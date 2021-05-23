package org.featurehouse.mcmod.brightore;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;

import java.util.List;

/**
 * @see OptionsScreen
 * @see VideoOptionsScreen
 */
@Environment(EnvType.CLIENT)
public class BrightOreConfigScreen extends Screen {
    protected Screen parent;

    protected BrightOreConfigScreen(Screen parent) {
        super(new TranslatableText("options.bright_ore"));
        this.parent = parent;
    }

    /**
     * @see VideoOptionsScreen#init()
     * @see Option#AO
     * @see Option#GAMMA
     */
    @Override
    protected void init() {
        assert client != null : "???";
        this.addButton(RENDER.createButton(client.options, this.width / 2 - 100, 27, 200));
        this.addButton(DEFAULT_LIGHT.createButton(client.options, this.width / 2 - 100, 54, 200));
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> {
            BrightOreConfig.INSTANCE.save();
            client.worldRenderer.reload();
            this.client.openScreen(this.parent);
        }));
    }

    public static Option asOption(Screen parent, MinecraftClient client) {
        return new Option("options.bright_ore") {
            @Override
            public AbstractButtonWidget createButton(GameOptions options, int x, int y, int width) {
                return new ButtonWidget(x, y, width, 20, new TranslatableText("options.bright_ore"), button -> client.openScreen(new BrightOreConfigScreen(parent)));
            }
        };
    }

    protected static final List<OrderedText> DEFAULT_LIGHT_TEXT = ImmutableList.of(new TranslatableText("options.bright_ore.default_light.exp").asOrderedText());
    private static final TranslatableText DEFAULT_LIGHT_MT = new TranslatableText("options.bright_ore.default_light");

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    protected static final CyclingOption<Boolean> RENDER = CyclingOption.create("options.bright_ore.render",
            vanilla -> BrightOreConfig.INSTANCE.render(),
            (vanilla, option, ifRender) -> BrightOreConfig.INSTANCE.setRender(ifRender));
    protected static final DoubleOption DEFAULT_LIGHT = new DoubleOption("options.bright_ore.default_light",
            0.0D, 30.0D, 1.0F,
            vanilla -> ((double) BrightOreConfig.INSTANCE.defaultLight()),
            (GameOptions vanilla, Double value) -> BrightOreConfig.INSTANCE.setDefaultLight(value.intValue()),
            (vanilla, option) -> new TranslatableText("options.generic_value", DEFAULT_LIGHT_MT, BrightOreConfig.INSTANCE.defaultLight()),
            client -> DEFAULT_LIGHT_TEXT);
}
