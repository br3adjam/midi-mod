package com.br3adjam.midi2noteblocks.world;

import com.br3adjam.midi2noteblocks.command.TestCommands;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

public class TickHandler {
    public static boolean nextTick = false;
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(TickHandler::onServerTick);
    }

    private static int onServerTick(MinecraftServer server) {
        if (TestCommands.run) {
            nextTick = true; //todo)) fix/make work
            System.out.println("next tick");
        }
        return 1;
    }
}
