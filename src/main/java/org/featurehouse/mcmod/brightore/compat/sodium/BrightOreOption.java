package org.featurehouse.mcmod.brightore.compat.sodium;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.Control;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.featurehouse.mcmod.brightore.BrightOreConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @see OptionImpl
 */
@Environment(EnvType.CLIENT)
public class BrightOreOption<H>
        implements Option<H>, OptionStorage<BrightOreConfig> {

    static {
        if (!FabricLoader.getInstance().isModLoaded("sodium")) {
            throw new IllegalAccessError("Sodium mod is not loaded");
        }
    }

    public static OptionPage brightOre() {
        List<OptionGroup> groups = new ArrayList<>();
        groups.add(OptionGroup.createBuilder()
                .add(new BrightOreOption<>(
                        new TranslatableText("options.bright_ore.render"),
                        null,
                        TickBoxControl::new,
                        Boolean.TRUE,
                        BrightOreConfig.INSTANCE::setRender,
                        BrightOreConfig.INSTANCE::render
                ))
                .add(new BrightOreOption<>(
                        new TranslatableText("options.bright_ore.default_light"),
                        new TranslatableText("options.bright_ore.default_light.exp"),
                        option -> new SliderControl(option, 0, 30, 1, ControlValueFormatter.number()),
                        BrightOreConfig.DEFAULT_LIGHT_PRESET,
                        BrightOreConfig.INSTANCE::setDefaultLight,
                        BrightOreConfig.INSTANCE::defaultLight
                ))
                .build()
        );
        return new OptionPage(new TranslatableText("options.bright_ore")
                , ImmutableList.copyOf(groups));
    }

    final Text name;
    final @Nullable Text tooltip;
    final Function<Option<H>, Control<H>> control;
    final @NotNull H defaultValue;
    final Consumer<H> setter;
    final Supplier<H> getter;

    Control<H> controlLazy;

    H currentValue;

    BrightOreOption(Text name,
                    @Nullable Text tooltip,
                    Function<Option<H>, Control<H>> controlInitializer,
                    @NotNull H defaultValue,
                    Consumer<H> setter,
                    Supplier<H> getter) {
        this.name = name;
        this.tooltip = tooltip;
        this.control = controlInitializer;
        this.defaultValue = defaultValue;
        this.setter = setter;
        this.getter = getter;

        currentValue = getValue();
    }

    @Override
    public Text getName() {
        return name;
    }

    @Override
    public @NotNull Text getTooltip() {
        return tooltip != null ? tooltip : Text.of("");
    }

    @Override
    public OptionImpact getImpact() {
        return OptionImpact.VARIES;
    }

    @Override
    public Control<H> getControl() {
        if (controlLazy == null) {
            controlLazy = control.apply(this);
        }
        return controlLazy;
    }

    @Override
    public H getValue() {
        return getter.get();
    }

    @Override
    public void setValue(H h) {
        setter.accept(h);
    }

    @Override
    public void reset() {
        this.setValue(currentValue);
    }

    @Override
    public OptionStorage<?> getStorage() {
        return this;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean hasChanged() {
        return !currentValue.equals(getValue());
    }

    @Override
    public void applyChanges() {
        currentValue = getValue();
    }

    @Override
    public Collection<OptionFlag> getFlags() {
        return Collections.singleton(OptionFlag.REQUIRES_RENDERER_RELOAD);
    }

    @Override
    public BrightOreConfig getData() {
        return BrightOreConfig.INSTANCE;
    }

    @Override
    public void save() {
        BrightOreConfig.INSTANCE.save();
    }
}
