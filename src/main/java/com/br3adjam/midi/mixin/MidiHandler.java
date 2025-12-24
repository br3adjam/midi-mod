package com.br3adjam.midi.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
@Mixin(MinecraftServer.class)
public class MidiHandler {
    public MidiHandler() throws MidiUnavailableException {
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
    }
    @Inject(at = @At("HEAD"), method = "loadWorld")
    private void init(CallbackInfo info) {

    }
}
