package com.br3adjam.midi2noteblocks.command;

import com.br3adjam.midi2noteblocks.world.BlockControl;
import com.br3adjam.midi2noteblocks.world.RedstoneGeneratorParams;
import com.br3adjam.midi2noteblocks.world.TickHandler;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.sun.jdi.connect.Connector;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.impl.util.Arguments;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.ArgumentGetter;
import net.minecraft.command.*;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;

import net.minecraft.component.Component;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import net.minecraft.network.*;
import net.minecraft.network.listener.ClientPacketListener.*;
//import net.minecraft.server.network.ServerPlayerEntity.*;
//import net.minecraft.server.network.ServerPlayNetworkHandler.*;
//import net.minecraft.client.network.ClientPlayNetworkHandler.*; // for chatting??

import net.minecraft.server.command.CommandManager;
import net.minecraft.world.World;
import net.minecraft.world.level.*;
import net.minecraft.world.block.*;
import net.minecraft.server.command.ServerCommandSource;

import java.awt.*;

public class TestCommands {
//    private final CommandDispatcher<ServerCommandSource> dispatcher;
//    private final CommandBuildContext registryAccess;
//    private final Command.CommandSelection environment;
    private final CommandDispatcher<ServerCommandSource> dispatcher;
    private final CommandExecutionContext registryAccess;
    private final CommandManager.RegistrationEnvironment environment;


    public TestCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandExecutionContext registryAccess, CommandManager.RegistrationEnvironment environment) {
        this.dispatcher = dispatcher;
        this.registryAccess = registryAccess;
        this.environment = environment;
    }

    static public void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("place")
                    .then(CommandManager.argument("block", BlockStateArgumentType.blockState(registryAccess))
                        .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos()) // i luv javadocs
                                .executes(TestCommands::place)
                        )
                    )
            );
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("place_noteblock")
                    .then(CommandManager.argument("pitch", IntegerArgumentType.integer(0, 24))
                        .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                .executes(TestCommands::placeNoteblock)
                        )
                    )
            );
        });

        // starts redstone generator
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("start")
                    .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                        .then(CommandManager.argument("direction", StringArgumentType.word())
                                .executes(TestCommands::start)
                        )
                    )
            );
        });

        // stops redstone generator
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("stop")
                .executes(TestCommands::stop)
            );
        });
    }



    private static int placeNoteblock(CommandContext<ServerCommandSource> context) {
        BlockControl blockControl = new BlockControl(context.getSource().getWorld());
        int pitch = IntegerArgumentType.getInteger(context, "pitch");
        BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
        blockControl.placeNoteblock(pos, pitch);  // FIXME)) wtf is this vro
        context.getSource().sendFeedback(() -> Text.literal("Placed Noteblock at %s".formatted(pos)), false);
        return 1;
    }

    private static int place(CommandContext<ServerCommandSource> context) {
        BlockControl blockControl = new BlockControl(context.getSource().getWorld());
        BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
        BlockStateArgument block = BlockStateArgumentType.getBlockState(context, "block");
        context.getSource().getWorld().setBlockState(pos, block.getBlockState(), 3);
        context.getSource().sendFeedback(() -> Text.literal("Placed %s at %s".formatted(block,pos)), false);
        return 1;
    }

    private static int start(CommandContext<ServerCommandSource> context) {
        BlockControl blockControl = new BlockControl(context.getSource().getWorld());
        World level = context.getSource().getWorld();

        RedstoneGeneratorParams redstoneGeneratorParams = new RedstoneGeneratorParams(
                BlockPosArgumentType.getBlockPos(context, "pos"),
                Direction.valueOf(StringArgumentType.getString(context, "direction"))
        );


        level.setBlockState(redstoneGeneratorParams.startPos, Blocks.REDSTONE_TORCH.getDefaultState(), 3);

        //place repeaters and redstone in chain each tick ( todo)) can tkae in midi + placement of noteblocks)

        System.out.println("start command ran");

        TickHandler.redstoneGeneratorParams = redstoneGeneratorParams;

        return 1;
    }

    private static int stop(CommandContext<net.minecraft.server.command.ServerCommandSource> context) {
        TickHandler.redstoneGeneratorParams = null;
        System.out.println("run stop");

        return 1;
    }


}
