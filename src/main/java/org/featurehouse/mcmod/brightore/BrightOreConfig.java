package org.featurehouse.mcmod.brightore;

import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

@Environment(EnvType.CLIENT)
public enum BrightOreConfig {
    INSTANCE;
    private static final File CONFIG_FILE;
    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = LogManager.getLogger("Bright Ore Config");

    private boolean render;

    public static void onInitialize() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new OreTagLoader());
        INSTANCE.reload();
    }

    public boolean render() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    public void save() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE));
            String s = GSON.toJson(this.toJson());
            bw.write(s);
            bw.close();
        } catch (IOException e) {
            LOGGER.fatal("Cannot save", e);
        }
    }

    public void reload() {
        if (!CONFIG_FILE.exists()) {
            LOGGER.info("Creating config file: {}", CONFIG_FILE.getAbsolutePath());
            try {
                CONFIG_FILE.getParentFile().mkdirs();
                CONFIG_FILE.createNewFile();

                BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE));
                bw.write("{\n  \"render\": true\n}");
                bw.close();
            } catch (IOException e) {
                LOGGER.fatal("Cannot load config", e);
            } finally {
                LOGGER.info("Setting up default values");
                this.setToDefault();
            }
        } else {
            try {
                BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE));
                JsonElement je = new JsonParser().parse(br);
                JsonObject root = JsonHelper.asObject(je, "root");
                this.fromJson(root);
                br.close();
            } catch (IOException | JsonParseException e) {
                LOGGER.fatal("Cannot load config", e);
                this.setToDefault();
            }
        }
    }

    protected void setToDefault() {
        this.render = true;
    }

    protected JsonObject toJson() {
        JsonObject root = new JsonObject();
        root.add("render", new JsonPrimitive(this.render));
        return root;
    }

    protected void fromJson(JsonObject root) throws JsonSyntaxException {
        this.render = JsonHelper.getBoolean(root, "render");
    }

    static {
        CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("org.featurehouse.mcmod.bright-ore/config.json").toFile();
    }
}
