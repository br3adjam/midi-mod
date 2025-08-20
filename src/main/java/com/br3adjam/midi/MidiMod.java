package com.br3adjam.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;


import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class MidiMod implements ModInitializer {
	public static final String MOD_ID = "midi-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private RecordingSession session = null;

	@Override

//	public void setNoteblock(int x,int y,int z){
//		ServerWorld world = ctx.getSource().getWorld();
//		BlockPos pos = new BlockPos(x, y, z);
//		world.setBlockState(pos, Blocks.NOTE_BLOCK.getDefaultState());
//	}
	public void onInitialize() {
		// setting up midi device
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (MidiDevice.Info info : infos) {
            MidiDevice device = null;
            try {
                device = MidiSystem.getMidiDevice(info);
            } catch (MidiUnavailableException e) {
                throw new RuntimeException(e);
            }
            try {
                device.open();
            } catch (MidiUnavailableException e) {
                throw new RuntimeException(e);
            }
            try {
                device.getTransmitter().setReceiver(new Receiver() {
                    @Override
                    public void send(MidiMessage message, long timeStamp) {
                        if (message instanceof ShortMessage sm) {
                            if (sm.getCommand() == ShortMessage.NOTE_ON) {
                                int midiNote = sm.getData1();
                                int velocity = sm.getData2();
                                // enqueue for Minecraft tick builder
                            }
                        }
                    }

                    @Override
                    public void close() {}
                });
            } catch (MidiUnavailableException e) {
                throw new RuntimeException(e);
            }
        }

		// runs every game tick
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			if (session != null) {
				placeNextTickBlocks(server.getOverworld());
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

	private void placeNextTickBlocks(ServerWorld world) {
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

	private void setTone(ServerWorld world, BlockPos notePos, int pitch){//12=middle c
		//MAXIMUM OF 30 NOTES(15 ON LEFT OF MIDDLE C AND 15 ON AND TO THE RIGHT OF MIDDLE C)
		world.setBlockState(notePos, Blocks.NOTE_BLOCK.getDefaultState()
				.with(net.minecraft.block.NoteBlock.PITCH, pitch));
	}

	public class RecordingSession {
		public final BlockPos anchor;
		public int currentTick = 0;

		public RecordingSession(BlockPos anchor) {
			this.anchor = anchor;
		}
	}

}