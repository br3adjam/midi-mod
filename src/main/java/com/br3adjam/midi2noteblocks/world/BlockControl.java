package com.br3adjam.midi2noteblocks.world;

import com.br3adjam.midi2noteblocks.command.TestCommands;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;

public class BlockControl {
    static Level level;
    public BlockControl(Level level) {
        this.level = level;
    }

    public void placeNoteblock(BlockPos pos, int pitch) {
        level.setBlock(pos, Blocks.NOTE_BLOCK.defaultBlockState()
                .setValue(NoteBlock.NOTE, Math.max(Math.min(pitch, 24), 0) ),
            3); // TODO)) make sure this works + make pitch changeable
//        level.getBlockState(pos).getBlock().triggerEvent(level.getBlockState(pos), level, pos, 0, 0);
        level.blockEvent(pos, Blocks.NOTE_BLOCK, 0, 0);

    }

    public static void placeNext(CommandContext<CommandSourceStack> context) {
        BlockPos startPos = BlockPosArgument.getBlockPos(context, "pos");
        Direction direction = Direction.valueOf(StringArgumentType.getString(context, "direction"));

        BlockPos repeaterPos = startPos.relative(direction.getAxis(), 2* TestCommands.step + 1);
        BlockPos redstonePos = repeaterPos.relative(direction.getAxis(), 2* TestCommands.step + 2);

        level.setBlock(repeaterPos, Blocks.REPEATER.defaultBlockState(), 3);
        level.setBlock(redstonePos, Blocks.REDSTONE_WIRE.defaultBlockState(), 3);

        TestCommands.step++;
        System.out.println("next step");
    }
}
