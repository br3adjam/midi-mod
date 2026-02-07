package com.br3adjam.midi2noteblocks.world;

import com.br3adjam.midi2noteblocks.command.TestCommands;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.level.*;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;

public class BlockControl {
    static ServerWorld level;
    public BlockControl(ServerWorld level) {
        this.level = level;
    }

    public void placeNoteblock(BlockPos pos, int pitch) {
        level.setBlockState(pos, Blocks.NOTE_BLOCK.getDefaultState()
                .with(NoteBlock.NOTE, Math.max(Math.min(pitch, 24), 0) ),
            3); // TODO)) make sure this works + make pitch changeable
//        level.getBlockState(pos).getBlock().triggerEvent(level.getBlockState(pos), level, pos, 0, 0);
        level.addSyncedBlockEvent(pos, Blocks.NOTE_BLOCK, 0, 0);

    }

    public static void placeNext(RedstoneGeneratorParams redstoneGeneratorParams) {
        BlockPos repeaterPos = redstoneGeneratorParams.startPos.offset(redstoneGeneratorParams.direction, 2*redstoneGeneratorParams.step + 1);
        BlockPos redstonePos = repeaterPos.offset(redstoneGeneratorParams.direction, 1);

        // todo)) make the redstone actually go in the direction it's supposed to

        // wool
        level.setBlockState(repeaterPos.down(), Blocks.WHITE_WOOL.getDefaultState(), 3);
        // repeater
        level.setBlockState(repeaterPos, Blocks.REPEATER.getDefaultState()
                .with(RepeaterBlock.FACING, redstoneGeneratorParams.direction.getOpposite()), 3);

        // wool
        level.setBlockState(redstonePos.down(), Blocks.WHITE_WOOL.getDefaultState(), 3);
        // redstone
        level.setBlockState(redstonePos, Blocks.REDSTONE_WIRE.getDefaultState(), 3);


        redstoneGeneratorParams.step++;
        System.out.println("next step");
    }
}
