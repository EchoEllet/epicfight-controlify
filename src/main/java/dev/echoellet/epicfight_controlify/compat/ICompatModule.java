package dev.echoellet.epicfight_controlify.compat;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.entrypoint.InitContext;
import dev.isxander.controlify.api.entrypoint.PreInitContext;

public interface ICompatModule {
    /**
     * Called once Controlify has been fully initialised. And all controllers have
     * been discovered and loaded.
     * Due to the nature of the resource-pack system, this is called
     * very late in the game's lifecycle (once the resources have been reloaded).
     */
    void onControllersDiscovered(ControlifyApi controlify);

    /**
     * Called once Controlify has initialised some systems but controllers
     * have not yet been discovered and constructed. This is the ideal
     * time to register events in preparation for controller discovery.
     */
    void onControlifyInit(InitContext context);


    /**
     * Called at the end of Controlify's client-side entrypoint.
     * You can register guides and input bindings here.
     */
    void onControlifyPreInit(PreInitContext context);
}
