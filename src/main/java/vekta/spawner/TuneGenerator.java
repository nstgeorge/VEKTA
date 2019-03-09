package vekta.spawner;

import vekta.Resources;
import vekta.sound.SoundGroup;
import vekta.sound.Tune;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static processing.core.PApplet.round;
import static vekta.Vekta.v;

public class TuneGenerator {
	private static final SoundGroup[] INSTRUMENTS = Arrays.stream(Resources.getStrings("instrument"))
			.map(SoundGroup::new)
			.toArray(SoundGroup[]::new);

	private static final int[][] RHYTHMS = Arrays.stream(Resources.getStrings("rhythm"))
			.map(s -> s.replace(" ", "")) // Remove whitespace
			.map(s -> IntStream.range(0, s.length())
					.filter(i -> s.charAt(i) == '.') // Count the '.' character as a note in the rhythm
					.toArray())
			.toArray(int[][]::new);

	public static Tune randomTune() {
		SoundGroup instrument = v.random(INSTRUMENTS);

		int offset = (int)v.random(instrument.size());
		Tune tune = new Tune(instrument, offset, v.random(.2F, .25F));

		// Create a list of available notes
		List<Integer> available = IntStream.range(0, instrument.size()).boxed().collect(Collectors.toList());

		int prev = 0;
		for(int i : randomRhythm()) {
			int slide = round(v.random(-3, 3)); // Up/down movement
			int note = slide == 0 ? v.random(available) : prev + slide; // If no movement, pick a random note
			if(v.chance(.9F)) {
				// Usually prevent notes from repeating
				available.remove((Integer)note);
			}
			tune.add(i, note);
		}

		return tune;
	}

	public static int[] randomRhythm() {
		return v.random(RHYTHMS);
	}
}
