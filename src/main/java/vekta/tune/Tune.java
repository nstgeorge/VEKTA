package vekta.tune;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static vekta.Vekta.v;

public class Tune {
	private final Instrument instrument;
	private final List<Note> notes = new ArrayList<>();
	private final int noteOffset;
	private final float noteInterval;

	private float intervalProgress;
	private int currentInterval;

	public Tune(Instrument instrument, int noteOffset, float noteInterval) {
		this.instrument = instrument;
		this.noteOffset = noteOffset;
		this.noteInterval = noteInterval;
	}

	public void add(int time, int note) {
		notes.add(new Note(time, note));
		notes.sort(Comparator.comparingInt(a -> a.time));
	}

	public void reset() {
		intervalProgress = 0;
		currentInterval = 0;
	}

	public void update() {
		if(intervalProgress >= 0) {
			intervalProgress -= noteInterval;
			for(Note note : notes) {
				if(note.time == currentInterval) {
					instrument.play(note.note + noteOffset);
				}
			}
			currentInterval++;
		}
		intervalProgress += 1 / v.frameRate;
	}

	private static class Note {
		private final int time;
		private final int note;

		private Note(int time, int note) {
			this.time = time;
			this.note = note;
		}
	}
}
