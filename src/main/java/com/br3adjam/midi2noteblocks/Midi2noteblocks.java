package com.br3adjam.midi2noteblocks;

import com.br3adjam.midi2noteblocks.command.TestCommands;
import com.br3adjam.midi2noteblocks.midi.MidiKeyboard;
import com.br3adjam.midi2noteblocks.world.TickHandler;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.loader.impl.game.GameProviderHelper.getSource;

public class Midi2noteblocks implements ModInitializer {

	public static final String MOD_ID = "midi2noteblocks";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing midi2noteblocks");

        TestCommands.register();
        TickHandler.register();

        // todo)) add midi

        // todo)) make proper functions in proper classes and packages + use interfaces
	}
}
