package dev.echoellet.epicfight_controlify;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import yesman.epicfight.main.EpicFightMod;

@Mod(EpicFightControlify.MODID)
public class EpicFightControlify {
    public static final String MODID = "epicfight_controlify";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EpicFightControlify(IEventBus modEventBus, ModContainer modContainer) {

    }

    @ApiStatus.Internal
    public static @NotNull ResourceLocation rl(@NotNull final String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    @ApiStatus.Internal
    public static @NotNull ResourceLocation epicFightRl(@NotNull final String path) {
        return ResourceLocation.fromNamespaceAndPath(EpicFightMod.MODID, path);
    }
}
