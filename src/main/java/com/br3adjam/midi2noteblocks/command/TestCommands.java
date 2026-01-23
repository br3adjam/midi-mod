package com.br3adjam.midi2noteblocks.command;

import com.br3adjam.midi2noteblocks.world.BlockControl;
import com.br3adjam.midi2noteblocks.world.RedstoneGeneratorParams;
import com.br3adjam.midi2noteblocks.world.TickHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

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

        // starts redstone generator
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("start")
                    .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .then(Commands.argument("direction", StringArgumentType.word())
                                .executes(TestCommands::start)
                        )
                    )
            );
        });

        // stops redstone generator
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("stop")
                .executes(TestCommands::stop)
            );
        });
    }

    private static int placeNoteblock(CommandContext<CommandSourceStack> context) {
        BlockControl blockControl = new BlockControl(context.getSource().getLevel());
        int pitch = IntegerArgumentType.getInteger(context, "pitch");
        BlockPos pos = BlockPosArgument.getBlockPos(context, "pos");
        blockControl.placeNoteblock(pos, pitch);  // FIXME)) wtf is this vro
        context.getSource().sendSuccess(() -> Component.literal("Placed Noteblock at %s".formatted(pos)), false);
        return 1;
    }

    private static int place(CommandContext<CommandSourceStack> context) {
        BlockControl blockControl = new BlockControl(context.getSource().getLevel());
        BlockPos pos = BlockPosArgument.getBlockPos(context, "pos");
        BlockInput block = BlockStateArgument.getBlock(context, "block");
        context.getSource().getLevel().setBlock(pos, block.getState(), 3);
        context.getSource().sendSuccess(() -> Component.literal("Placed %s at %s".formatted(block,pos)), false);
        return 1;
    }

    private static int start(CommandContext<CommandSourceStack> context) {
        BlockControl blockControl = new BlockControl(context.getSource().getLevel());
        Level level = context.getSource().getLevel();

        RedstoneGeneratorParams redstoneGeneratorParams = new RedstoneGeneratorParams(
                BlockPosArgument.getBlockPos(context, "pos"),
                Direction.valueOf(StringArgumentType.getString(context, "direction"))
        );


        level.setBlock(redstoneGeneratorParams.startPos, Blocks.REDSTONE_TORCH.defaultBlockState(), 3);

        //place repeaters and redstone in chain each tick ( todo)) can tkae in midi + placement of noteblocks)

        System.out.println("start command ran");

        TickHandler.redstoneGeneratorParams = redstoneGeneratorParams;

        return 1;
    }

    private static int stop(CommandContext<CommandSourceStack> context) {
        TickHandler.redstoneGeneratorParams = null;
        System.out.println("run stop");

        return 1;
    }


}
