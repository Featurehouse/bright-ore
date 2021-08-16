package org.featurehouse.mcmod.brightore.compat;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.*;

@Environment(EnvType.CLIENT)
public class BrightOreMixinConfig implements IMixinConfigPlugin {
    static final Logger LOGGER = LogManager.getLogger("Bright Ore Mixin Config");
    private static final boolean APPLY_INDIGO = FabricLoader.getInstance().isModLoaded("fabric-renderer-indigo");
    private static final boolean APPLY_OF = FabricLoader.getInstance().isModLoaded("optifabric");
    private static final boolean APPLY_SODIUM = FabricLoader.getInstance().isModLoaded("sodium");

    /**
     * Called after the plugin is instantiated, do any setup here.
     *
     * @param mixinPackage The mixin root package from the config
     */
    @Override
    public void onLoad(String mixinPackage) {
        LOGGER.info("[Bright Ore] Loading Mixin Config at {}", mixinPackage);
        if (APPLY_INDIGO) LOGGER.info("[Bright Ore] Indigo compat should be loaded");
        if (APPLY_OF) LOGGER.info("[Bright Ore] OptiFine compat should be loaded");
        if (APPLY_SODIUM) LOGGER.info("[Bright Ore] Sodium compat should be loaded");
    }

    /**
     * Called only if the "referenceMap" key in the config is <b>not</b> set.
     * This allows the refmap file name to be supplied by the plugin
     * programatically if desired. Returning <code>null</code> will revert to
     * the default behaviour of using the default refmap json file.
     *
     * @return Path to the refmap resource or null to revert to the default
     */
    @Override @Nullable
    public String getRefMapperConfig() {
        return null;
    }

    /* @Nullable */
    private Set<String> incompatibleMixins = new HashSet<>();
    private boolean loaded = false;

    private void load() {
        if (loaded) return;
        LOGGER.info("[Bright Ore] Checking mod custom values");
        modLoop: for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
            final ModMetadata meta = modContainer.getMetadata();

            if (meta.containsCustomValue("bright-ore:incompatible-mixins")) {
                CustomValue cv = meta.getCustomValue("bright-ore:incompatible-mixins");
                switch (cv.getType()) {
                    case STRING:
                        if (cv.getAsString().equals("*")) {
                            incompatibleMixins = null;
                            break modLoop;
                        } else {
                            incompatibleMixins.add(cv.getAsString());
                            break;
                        }
                    case ARRAY:
                        cv.getAsArray().forEach(each -> incompatibleMixins.add(each.getAsString()));
                        break;
                    default:
                        LOGGER.warn("Invalid Class Name: {}. Skipped.", cv.getAsString());
                }
            }
        } loaded = true;
    }

    /**
     * Called during mixin intialisation, allows this plugin to control whether
     * a specific will be applied to the specified target. Returning false will
     * remove the target from the mixin's target set, and if all targets are
     * removed then the mixin will not be applied at all.
     *
     * @param targetClassName Fully qualified class name of the target class
     * @param mixinClassName  Fully qualified class name of the mixin
     * @return True to allow the mixin to be applied, or false to remove it from
     * target's mixin set
     */
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        load();

        // If OptiFine loaded, MixinWorldRenderer redirection will crash
        if (APPLY_OF && "org.featurehouse.mcmod.brightore.mixin.renderer.MixinWorldRenderer".equals(mixinClassName)) {
            LOGGER.info("[Bright Ore] Skipping vanilla MixinWorldRenderer for OptiFine loaded");
            return false;
        }

        if (incompatibleMixins == null) return false;
        return !incompatibleMixins.contains(mixinClassName);
    }

    /**
     * Called after all configurations are initialised, this allows this plugin
     * to observe classes targetted by other mixin configs and optionally remove
     * targets from its own set. The set myTargets is a direct view of the
     * targets collection in this companion config and keys may be removed from
     * this set to suppress mixins in this config which target the specified
     * class. Adding keys to the set will have no effect.
     *
     * @param myTargets    Target class set from the companion config
     * @param otherTargets Target class set incorporating targets from all other
     */
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    /**
     * After mixins specified in the configuration have been processed, this
     * method is called to allow the plugin to add any additional mixins to
     * load. It should return a list of mixin class names or return null if the
     * plugin does not wish to append any mixins of its own.
     *
     * @return additional mixins to apply
     */
    @Override
    public List<String> getMixins() {
        // Additional ones
        ImmutableList.Builder<String> builder = new ImmutableList.Builder<>();
        if (APPLY_INDIGO) {
            LOGGER.info("[Bright Ore] Should apply indigo mixins");
            builder.add("compat.indigo.MixinBlockRenderInfo");
        } if (APPLY_OF) {
            LOGGER.info("[Bright Ore] Should apply OptiFine mixins");
            builder.add("compat.optifine.MixinOptiFineWorldRenderer");
        } if (APPLY_SODIUM) {
            LOGGER.info("[Bright Ore] Should apply Sodium mixins");
            builder.add(
                    "compat.sodium.MixinSodiumOptionsGui",
                    "compat.sodium.MixinSodiumBlockRenderer",
                    "compat.sodium.MixinLightDataAccess"
            );
        }
        return builder.build();
    }

    /**
     * Called immediately <b>before</b> a mixin is applied to a target class,
     * allows any pre-application transformations to be applied.
     *
     * @param targetClassName Transformed name of the target class
     * @param targetClass     Target class tree
     * @param mixinClassName  Name of the mixin class
     * @param mixinInfo       Information about this mixin
     */
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    /**
     * Called immediately <b>after</b> a mixin is applied to a target class,
     * allows any post-application transformations to be applied.
     *
     * @param targetClassName Transformed name of the target class
     * @param targetClass     Target class tree
     * @param mixinClassName  Name of the mixin class
     * @param mixinInfo       Information about this mixin
     */
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
