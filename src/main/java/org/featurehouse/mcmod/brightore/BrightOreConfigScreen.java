package org.featurehouse.mcmod.brightore;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.VideoOptionsScreen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
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

    /**
     * @see VideoOptionsScreen#init()
     * @see Option#AO
     * @see Option#AUTO_JUMP
     */
    @Override
    protected void init() {
        assert client != null : "???";
        this.addButton(RENDER.createButton(client.options, this.width / 2 - 100, 27, 200));
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

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    protected static final BooleanOption RENDER = new BooleanOption("options.bright_ore.render",
            vanilla -> BrightOreConfig.INSTANCE.render(),
            (vanilla, ifRender) -> BrightOreConfig.INSTANCE.setRender(ifRender));

}
