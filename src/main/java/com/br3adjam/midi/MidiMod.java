package com.br3adjam.midi;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.NoteBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class MidiMod implements ModInitializer {
	public static final String MOD_ID = "midi-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public RecordingSession session = null;

	//less than ideal map
    public record McNote(int pitch, int octave) {}
    Map<Integer, McNote> midiToMc = new HashMap<>();

    //holy naming
	private Map<Integer, Block> octaveNoteblock = Map.of(
		1, Blocks.OAK_PLANKS,
		2, Blocks.WHITE_WOOL,
		3, Blocks.DIRT,
		4, Blocks.DIRT,
		5, Blocks.GLOWSTONE,
		6, Blocks.CLAY,
		7, Blocks.GOLD_BLOCK
	);

	@Override
	public void onInitialize() {


        for (int midi = 0; midi <= 127; midi++) {
            int noteBlock = midi - 54;

            if (noteBlock < 0 || noteBlock > 24) continue;

            int pitch = noteBlock % 12;
            int octave = noteBlock / 12;

            midiToMc.put(midi, new McNote(pitch, octave));
        }

		// setting up midi device

		// runs every game tick
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			if (session != null) {
                ServerWorld world = server.getWorld(World.OVERWORLD);
                update(world);
				session.currentTick++;
			}
		});

		// inputs of all the commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			// "/record"
			dispatcher.register(literal("record")
					.then(argument("x", IntegerArgumentType.integer())//x input
					.then(argument("y", IntegerArgumentType.integer())//y input
					.then(argument("z", IntegerArgumentType.integer())//z input

					.executes(ctx -> {
						int x = IntegerArgumentType.getInteger(ctx, "x");
						int y = IntegerArgumentType.getInteger(ctx, "y");
						int z = IntegerArgumentType.getInteger(ctx, "z");
						ctx.getSource().sendFeedback(() -> Text.literal("Recording MIDI input at "+x+" "+y+" "+z), false);
						BlockPos anchor = new BlockPos(x, y, z);
						session = new RecordingSession(anchor);
						return 1;
					})))));
		});
	}

	private void update(ServerWorld world) {
		if(session==null)return;

		//place blocks at current tick
		BlockPos notePos=session.anchor.add(session.currentTick,0,0); // places along the x axis
		world.setBlockState(notePos,Blocks.NOTE_BLOCK.getDefaultState()); // note blocks

		//repeater
		BlockPos repeaterPos = session.anchor.add(session.currentTick - 1, 0, 0);
		if (session.currentTick > 0) {
			world.setBlockState(repeaterPos, Blocks.REPEATER.getDefaultState()
					.with(net.minecraft.block.RepeaterBlock.DELAY, 1)); //1 tick delay
		}
	}

	private void setTone(ServerWorld world, BlockPos pos, String note){
		//BlockPos notePos, int pitch
		int octave = notes.get(note);

		world.setBlockState(pos, Blocks.NOTE_BLOCK.getDefaultState()
				.with(NoteBlock.NOTE, ????);
		world.setBlockState(pos.down(), octaveNoteblock.get(octave).getDefaultState());
		//default noteblock sound used due to my want for compatibility with vanilla
	}



}