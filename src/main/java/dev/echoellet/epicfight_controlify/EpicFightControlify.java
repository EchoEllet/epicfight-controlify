package dev.echoellet.epicfight_controlify;

import com.yesman.epicskills.EpicSkills;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModLoadingException;
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
        crash();
    }

    public void crash() {
        throw new RuntimeException(
                """
                        ❌ 'Epic Fight: Controlify' is no longer needed.
                        
                        For Minecraft 1.20.1: Install Epic Fight 20.13.5 or newer and Controlify: Forgified (Unofficial backport).
                        For Minecraft 1.21.1 or newer: Install the latest Epic Fight and the official Controlify (not the backport).
                        
                        Please remove this mod to avoid crashes or undefined behavior."""
        );
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
