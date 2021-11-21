package org.featurehouse.mcmod.brightore;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;

import java.util.function.BiConsumer;

public enum OreTagMap {
    INSTANCE;

    private final Object2IntMap<Block> map = new Object2IntOpenHashMap<>();

    @Deprecated(forRemoval = true)
    public Object2IntMap<Block> get() {
        return map;
    }

    void clear() {
        map.clear();
    }

    public boolean containsKey(Block block) {
        return map.containsKey(block);
    }

    public int getValue(Block block) {
        return map.getOrDefault(block, 0);
    }

    public int put(Block block, int light) {
        return map.put(block, light);
    }

    @SuppressWarnings("unused")
    public int put(Block block) {
        return this.put(block, BrightOreConfig.INSTANCE.defaultLight());
    }

    @Deprecated // If you want to disable rendering a kind of
    // ore, you must set its luminance to zero.
    public boolean available(Block block) {
        if (!map.containsKey(block)) return false;
        return this.getValue(block) > 0;
    }

    @SuppressWarnings("unused")
    public void forEach(BiConsumer<Block, Integer> consumer) {
        map.forEach(consumer);
    }
}
