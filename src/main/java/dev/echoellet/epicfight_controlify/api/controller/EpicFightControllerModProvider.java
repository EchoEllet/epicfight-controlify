package dev.echoellet.epicfight_controlify.api.controller;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.main.EpicFightMod;

/**
 * Provides access to the active {@link IEpicFightControllerMod} implementation.
 * <p>
 * Only one mod can register an implementation at a time. If multiple mods register, a warning is logged
 * and the last one takes effect.
 *
 * @see IEpicFightControllerMod
 */
@ApiStatus.Experimental
public final class EpicFightControllerModProvider {
    private EpicFightControllerModProvider() {
    }

    @Nullable
    @javax.annotation.Nullable
    private static IEpicFightControllerMod instance = null;

    /**
     * True if a mod other than Epic Fight has already registered an implementation
     */
    private static boolean overriddenByOtherMod;

    /**
     * Registers a controller mod implementation.
     * Logs a warning if another mod has already registered.
     */
    public static void set(@NotNull String registrantModId, @NotNull IEpicFightControllerMod modInstance) {
        if (overriddenByOtherMod) {
            EpicFightMod.LOGGER.warn(
                    "Mod '{}' is overriding the Epic Fight controller implementation, which was already set by another mod. "
                            + "Only the last registered implementation will be used. "
                            + "This may occur if multiple controller mods are installed.",
                    registrantModId
            );
        }
        EpicFightControllerModProvider.instance = modInstance;

        if (registrantModId.equals(EpicFightMod.MODID)) {
            EpicFightMod.LOGGER.info(
                    "Epic Fight detected and registered supported controller mod: '{}'.",
                    modInstance.getModName()
            );
        } else {
            overriddenByOtherMod = true;
        }
    }

    /**
     * Returns the current controller implementation, or null if none
     */
    @Nullable
    public static IEpicFightControllerMod get() {
        return EpicFightControllerModProvider.instance;
    }
}
