package com.kryeit.fabric;

import com.kryeit.Main;
import com.kryeit.MissionHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import static com.kryeit.Main.REGISTRATE;

public class MainImpl implements ModInitializer {

    @Override
    public void onInitialize() {
        Main.init();
        MissionHandler.registerEvents();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> Main.handlePlayerLogin(handler.getPlayer()));

        ServerLifecycleEvents.SERVER_STARTED.register(server ->
            Main.readConfig()
        );
    }

    public static void finalizeRegistrate() {
        REGISTRATE.register();
    }

}
