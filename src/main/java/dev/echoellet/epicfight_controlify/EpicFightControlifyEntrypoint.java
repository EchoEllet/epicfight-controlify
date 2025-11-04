package dev.echoellet.epicfight_controlify;

import dev.echoellet.epicfight_controlify.compat.ICompatModule;
import dev.echoellet.epicfight_controlify.compat.epicfight.EpicFightCompat;
import dev.echoellet.epicfight_controlify.compat.epicskills.EpicSkillsCompat;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.api.entrypoint.InitContext;
import dev.isxander.controlify.api.entrypoint.PreInitContext;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

// Important for maintainers: Be careful when using Epic Fight classes here,
// as the Epic Fight mod might not be loaded yet. For example, avoid referencing
// EpicFightItems.UCHIGATANA.get() in onControlifyPreInit.
@ApiStatus.Internal
public class EpicFightControlifyEntrypoint implements ControlifyEntrypoint {
    private static List<ICompatModule> modules = null;

    private static List<ICompatModule> getModules() {
        if (modules == null) {
            modules = new ArrayList<>();
            modules.add(new EpicFightCompat());
            EpicFightControlify.LOGGER.info("Registering compatibility module for 'Epic Fight' mod");

            if (ModList.get().isLoaded("epicskills")) {
                modules.add(new EpicSkillsCompat());
                EpicFightControlify.LOGGER.info("Registering compatibility module for 'Epic Fight: Skill Tree' addon");
            }
        }
        return modules;
    }

    @Override
    public void onControllersDiscovered(ControlifyApi controlify) {
        for (ICompatModule module : getModules()) {
            module.onControllersDiscovered(controlify);
        }
    }

    @Override
    public void onControlifyInit(InitContext context) {
        for (ICompatModule module : getModules()) {
            module.onControlifyInit(context);
        }
    }

    @Override
    public void onControlifyPreInit(PreInitContext context) {
        for (ICompatModule module : getModules()) {
            module.onControlifyPreInit(context);
        }
    }
}
