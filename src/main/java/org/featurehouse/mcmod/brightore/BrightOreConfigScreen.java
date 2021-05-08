package org.featurehouse.mcmod.brightore;

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
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

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

    @Override
    protected void init() {
        this.addButton(RENDER.createButton(null, this.width / 2 - 100, 27, 200));
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> {
            BrightOreConfig.INSTANCE.save();
            assert this.client != null : "???";
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

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    protected static final CyclingOption<Boolean> RENDER = CyclingOption.create("options.bright_ore.render", vanilla -> BrightOreConfig.INSTANCE.render(),
            (vanilla, option, ifRender) -> BrightOreConfig.INSTANCE.setRender(ifRender));
}