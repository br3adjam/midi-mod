package com.br3adjam.midi2noteblocks.world;

import com.br3adjam.midi2noteblocks.command.TestCommands;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;

public class TickHandler {
    public static boolean nextTick = false;
    public static RedstoneGeneratorParams redstoneGeneratorParams;
    public static int tickCount = 0;
    public static final int DELAY_TICKS = 20; // 20 for testing


    public static void register() {
        ServerTickEvents.START_WORLD_TICK.register(TickHandler::onWorldTick);
    }

    private static int onWorldTick(ServerLevel level) {
        if (redstoneGeneratorParams == null) {
            System.out.println("redstoneGeneratorParams is null");
            return 1;
        }

        tickCount=(tickCount + 1) % DELAY_TICKS;
        System.out.println("tick processed, tick count: " + tickCount);

        if (tickCount == 0) {
            BlockControl.placeNext(redstoneGeneratorParams);
            System.out.println("next placed");
        }

        return 1;
    }
}
