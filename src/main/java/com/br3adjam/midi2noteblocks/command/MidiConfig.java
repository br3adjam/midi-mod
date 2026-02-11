package com.br3adjam.midi2noteblocks.command;

import com.br3adjam.midi2noteblocks.world.BlockControl;
import com.br3adjam.midi2noteblocks.world.RedstoneGeneratorParams;
import com.br3adjam.midi2noteblocks.world.TickHandler;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import net.minecraft.text.*;

import java.util.function.Supplier;

public class MidiConfig {
    private final CommandDispatcher<ServerCommandSource> dispatcher;
    private final CommandExecutionContext registryAccess;
    private final CommandManager.RegistrationEnvironment environment;

    public MidiConfig(CommandDispatcher<ServerCommandSource> dispatcher, CommandExecutionContext registryAccess, CommandManager.RegistrationEnvironment environment) {
        this.dispatcher = dispatcher;
        this.registryAccess = registryAccess;
        this.environment = environment;
    }

    static public void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("sendMessage")
                .executes(MidiConfig::sendMesssage)
            );
        });
    }

    private static int sendMesssage(CommandContext<ServerCommandSource> context) {
        textClickEvent clickEvent = new textClickEvent(textClickEvent.Action.RUN_COMMAND, "/toggle");
        textHoverEvent hoverEvent = new textHoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("toggle"));
        Text msg = Text.literal("Click me")
                .setStyle(Style.EMPTY
                        .withColor(Formatting.AQUA)
                        .withUnderline(true)
                        .withClickEvent(clickEvent)
                        .withHoverEvent(hoverEvent)
                );

        ServerPlayerEntity player = context.getSource().getPlayer();
        player.sendMessage(Text.literal("test!"), false);
        return 1;
    }



}
