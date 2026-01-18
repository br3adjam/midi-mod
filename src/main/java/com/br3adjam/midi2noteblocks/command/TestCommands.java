package com.br3adjam.midi2noteblocks.command;

import com.br3adjam.midi2noteblocks.world.NoteblockControl;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.mixin.command.CommandSourceMixin;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.network.chat.SignableCommand;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;


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
                    .then(Commands.argument("block", BlockStateArgument.block(registryAccess))
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes(TestCommands::place)
                        )
                    )
            );
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("place_noteblock")
                    .then(Commands.argument("pitch", IntegerArgumentType.integer(0, 24))
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes(TestCommands::placeNoteblock)
                        )
                    )
            );
        });
    }

    private static int placeNoteblock(CommandContext<CommandSourceStack> context) {
        NoteblockControl noteblockControl = new NoteblockControl(context.getSource().getLevel());
        int pitch = IntegerArgumentType.getInteger(context, "pitch");
        BlockPos pos = BlockPosArgument.getBlockPos(context, "pos");
        noteblockControl.placeNoteblock(pos, pitch);  // FIXME)) wtf is this vro
        context.getSource().sendSuccess(() -> Component.literal("Placed Noteblock at %s".formatted(pos)), false);
        return 1;
    }

    private static int place(CommandContext<CommandSourceStack> context) {
        NoteblockControl noteblockControl = new NoteblockControl(context.getSource().getLevel());
        BlockPos pos = BlockPosArgument.getBlockPos(context, "pos");
        BlockInput block = BlockStateArgument.getBlock(context, "block");
        context.getSource().getLevel().setBlock(pos, block.getState(), 3);
        context.getSource().sendSuccess(() -> Component.literal("Placed %s at %s".formatted(block,pos)), false);
        return 1;
    }


}
