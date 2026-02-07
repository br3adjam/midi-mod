package com.br3adjam.midi2noteblocks.midi;

import javax.sound.midi.*;

import static javax.sound.midi.MidiSystem.*;

public class MidiKeyboard {
    MidiDevice midiDevice;
    
    public void run() throws MidiUnavailableException {
        Sequencer seq;
        Transmitter seqTrans;
        Synthesizer synth;
        Receiver synthRcvr;
        try {
            seq = MidiSystem.getSequencer();
            seqTrans = seq.getTransmitter();
            synth = MidiSystem.getSynthesizer();
            synthRcvr = synth.getReceiver();
            seqTrans.setReceiver(synthRcvr);
        } catch (MidiUnavailableException e) {
            throw e;
            // handle or throw exception
        }
    }

/*
*         midiDevice = MidiSystem.getMidiDevice(midiDeviceInfo);
        midiDevice.open();
        transmitter = midiDevice.getTransmitter();
        receiver = midiDevice.getReceiver();
        transmitter.setReceiver(receiver);
*/

}
