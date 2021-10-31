package org.featurehouse.mcmod.brightore;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public enum OreTagMap {
    INSTANCE;

    private final Object2IntMap<Block> map =
            Object2IntMaps.synchronize(new Object2IntOpenHashMap<>() {
                @Override
                public int put(Block block, int v) {
                    return super.put(Objects.requireNonNull(block), v);
                }

                @Override
                public boolean containsKey(Object k) {
                    if (!BrightOreConfig.INSTANCE.render())
                        return false;
                    return super.containsKey(k);
                }
            });//new Object2IntOpenHashMap<>();

    @Deprecated
    public Object2IntMap<Block> get() {
        return map;
    }

    @Contract("null->false")
    public boolean containsKey(Block block) {
        return map.containsKey(block);
    }

    public boolean hasRedefined(@NotNull BlockState state) {
        return map.containsKey(state.getBlock());
    }

    @Deprecated
    public int getValue(Block block) {
        return map.getOrDefault(block, 0);
    }

    public OptionalInt getOptional(@NotNull BlockState state) {
        return getOptional(state.getBlock());
    }

    public OptionalInt getOptional(@NotNull Block block) {
        if (!map.containsKey(block)) return OptionalInt.empty();
        return OptionalInt.of(map.getInt(block));
    }

    /**
     * Returns the redirected luminance, or real luminance if
     * not defined.
     */
    public int getValue(@NotNull BlockState state) {
        return map.getOrDefault(state.getBlock(), state.getLuminance());
    }

    public int getValueStrict(@NotNull BlockState state)
            throws IllegalArgumentException {
        return getValueStrict(state.getBlock());
    }

    public int getValueStrict(@NotNull Block block) throws IllegalArgumentException {
        if (!map.containsKey(block)) {
            throw new IllegalArgumentException("block " + block + " is not in the map.");
        } return map.getInt(block);
    }

    public int put(Block block, int light) {
        return map.put(block, light);
    }

    public int put(Block block) {
        return this.put(block, BrightOreConfig.INSTANCE.defaultLight());
    }

    @Deprecated // If you want to disable rendering a kind of
    // ore, you must set its luminance to zero.
    public boolean available(Block block) {
        if (!map.containsKey(block)) return false;
        return this.getValue(block) > 0;
    }

    public void forEach(BiConsumer<Block, Integer> consumer) {
        map.forEach(consumer);
    }
}
