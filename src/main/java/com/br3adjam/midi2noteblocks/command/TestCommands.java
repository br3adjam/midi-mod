package com.br3adjam.midi2noteblocks.command;

import com.br3adjam.midi2noteblocks.world.NoteblockControl;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.network.chat.SignableCommand;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;


public class TestCommands {
    private final CommandDispatcher<CommandSourceStack> dispatcher;
    private final CommandBuildContext registryAccess;
    private final Commands.CommandSelection environment;

    public TestCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        this.dispatcher = dispatcher;
        this.registryAccess = registryAccess;
        this.environment = environment;
    }

    static public void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("place")
                    .then(Commands.argument("pos", BlockPosArgument.blockPos())
                            .executes(TestCommands::place)));
        });    }

    private static int place(CommandContext<CommandSourceStack> context) {
        BlockPos pos = BlockPosArgument.getBlockPos(context, "pos");
//        NoteblockControl::placeNoteblock(pos, context.getSource().getLevel());  // FIXME)) wtf is this vro
        context.getSource().sendSuccess(() -> Component.literal("Placed Noteblock at %s".formatted(pos)), false);
        return 1;
    }


}
