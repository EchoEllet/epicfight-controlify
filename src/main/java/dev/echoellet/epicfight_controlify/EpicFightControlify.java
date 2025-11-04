package dev.echoellet.epicfight_controlify;

import com.yesman.epicskills.EpicSkills;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import yesman.epicfight.main.EpicFightMod;

@Mod(EpicFightControlify.MODID)
public class EpicFightControlify {
    public static final String MODID = "epicfight_controlify";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public EpicFightControlify(FMLJavaModLoadingContext context) {

    }

    @ApiStatus.Internal
    public static @NotNull ResourceLocation rl(@NotNull final String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    @ApiStatus.Internal
    public static @NotNull ResourceLocation epicFightRl(@NotNull final String path) {
        return ResourceLocation.fromNamespaceAndPath(EpicFightMod.MODID, path);
    }

    @ApiStatus.Internal
    public static @NotNull ResourceLocation epicSkillsRl(@NotNull final String path) {
        return ResourceLocation.fromNamespaceAndPath(EpicSkills.MODID, path);
    }
}
