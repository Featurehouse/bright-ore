package org.featurehouse.mcmod.brightore;

import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.block.*;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static org.featurehouse.mcmod.brightore.OreTagMap.INSTANCE;

@Environment(EnvType.CLIENT)
public class OreTagLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {

    protected static final Identifier FABRIC_ID = new Identifier("bright_ore", "ore_tags");
    private static final Gson GSON = new GsonBuilder().create();
    private static final Logger LOGGER = LogManager.getLogger("Ore Tag Loader");

    public OreTagLoader() {
        super(GSON, "ore_tags");
    }

    @Override
    public Identifier getFabricId() {
        return FABRIC_ID;
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        prepared.forEach((fileId, json) -> {
            JsonArray root = JsonHelper.asArray(json, fileId.toString());
            for (JsonElement je : root) {
                if (je.isJsonObject()) {
                    JsonObject obj = je.getAsJsonObject();
                    try {
                        @Nullable JsonElement je2 = obj.get("light");
                        int light = je2 == null ? BrightOreConfig.INSTANCE.defaultLight() : je2.getAsInt();

                        String string = obj.get("value").getAsString();
                        put(string, light);
                    } catch (RuntimeException e) {
                        if (!obj.get("required").getAsBoolean()) {
                            LOGGER.debug("Ignored error", e);
                        } else {
                            throw e;
                        }
                    }
                } else {
                    String string = je.getAsString();
                    put(string, BrightOreConfig.INSTANCE.defaultLight());
                }
            }
        });
    }

    private static void put(String string, int light) throws RuntimeException {
        if (string.startsWith("#")) {
            Identifier tagId = new Identifier(string.substring(1));
            Tag<Block> tag = BlockTags.getTagGroup().getTag(tagId);
            if (tag == null) throw new NullPointerException("Invalid tag: " + string);
            for (Block block: tag.values()) {
                if (notAir(block))
                    OreTagMap.INSTANCE.put(block, light);
            }
        } else {
            Identifier blockId = new Identifier(string);
            @NotNull Block block = Registry.BLOCK.get(blockId);
            if (notAir(block))
                OreTagMap.INSTANCE.put(block, light);
        }
    }

    private static boolean notAir(Block block) {
        return block != Blocks.AIR && block != Blocks.CAVE_AIR && block != Blocks.VOID_AIR;
    }

    public static int redirectLuminance(BlockState it) {
        if (BrightOreConfig.INSTANCE.render()) {
            Block block1 = it.getBlock();
            if (INSTANCE.containsKey(block1))
                return INSTANCE.getValue(block1);
            else if (block1 instanceof OreBlock || block1 instanceof RedstoneOreBlock) {
                return BrightOreConfig.INSTANCE.defaultLight();
            }
        } return it.getLuminance();
    }
}
