package org.featurehouse.mcmod.brightore.compat.optifine;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

@Deprecated(forRemoval = true)
public class OFRefs {
    private static final Logger LOGGER = LogManager.getLogger("Bright Ore OptiFine Compat");

    protected static final Class<?> net_optifine_Config;
    static {
        Class<?> clazz;
        try {
            clazz = Class.forName("net.optifine.Config");
        } catch (ClassNotFoundException e) {
            clazz = null;
        } net_optifine_Config = clazz;
    }
    static {
        if (!FabricLoader.getInstance().isModLoaded("optifabric")) {
            if (net_optifine_Config == null)
                throw new IllegalStateException("OptiFine is not loaded",
                        new ClassNotFoundException("net.optifine.Config"));
            else { // Using other ways to load OptiFine
                LOGGER.warn("Using other ways to load OptiFine instead of OptiFabric. Use it at your own risk.");
            }
        } else if (net_optifine_Config == null) throw new InternalError("OptiFine is loaded illegally");
        // else: everything runs okay
    }

    public static boolean isDynamicLights() {
        MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        try {
            MethodHandle mh = lookup.findStatic(    // Config.isDynamicLights () => boolean
                    net_optifine_Config, "isDynamicLights", MethodType.methodType(boolean.class));
            return (Boolean) mh.invoke();
        } catch (Throwable t) {
            throw new IllegalStateException(t);
        }
    }
}
