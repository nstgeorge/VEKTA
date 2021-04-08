package vekta.audio;

import org.fmod.FMODLoader;
import org.fmod.jni.FMOD_STUDIO_STOP_MODE;
import org.fmod.studio.Bank;
import org.fmod.studio.FmodStudioSystem;
import org.fmod.studio.EventDescription;
import org.fmod.studio.EventInstance;
import vekta.Resources;

import java.util.*;
import java.util.stream.Collectors;

import static org.fmod.jni.FMODConstants.*;

/**
 * For now, a pretty barebones audio system that loads in FMOD. May be expanded for further usage later.
 * Have fun, Ryan!
 */
public final class AudioDriver {

	private static FmodStudioSystem studio;
	private static ArrayList<Bank> banks = new ArrayList<>();

	// This is likely a temporary fix until we have a better method for accessing this data.
	private static final String RESOURCES_PATH = "src/main/resources/";

	/**
	 * Load the FMOD natives and initialize the studio system with banks
	 */
	public static void init() {
		if(FMODLoader.loadNatives()) {
			System.out.println("FMOD natives loaded.");
		} else {
			System.out.println("Failed to load FMOD natives.");
		}

		List<String> banks = Resources.getLocatedAudioBanks()
				.stream()
				.map((String e) -> RESOURCES_PATH + e)
				.collect(Collectors.toList());

		studio = FmodStudioSystem.create();
		studio.initialize(32, FMOD_STUDIO_INIT_NORMAL, FMOD_INIT_NORMAL, null);

		System.out.println("Found banks: " + banks);
		loadBanks(studio, banks.toArray(new String[] {}));
	}

	/**
	 * Get the FModStudioSystem currently in use. This can be used to access more advanced audio features outside of the AudioDriver.
	 * @return FModStudioSystem
	 */
	public static FmodStudioSystem getStudio() {
		return studio;
	}

	/**
	 * Load one or more audio banks into the studio.
	 * @param studio FmodStudioSystem to load into
	 * @param paths Paths to each bank to load. Resources.getLocatedAudioBanks() can be helpful here.
	 */
	private static void loadBanks(FmodStudioSystem studio, String... paths) {
		Objects.requireNonNull(paths, "AudioDriver.loadBanks() was provided no banks to load.");
		for(String path : paths) {
			System.out.println("Loading " + path + " into studio...");
			banks.add(studio.loadBankFile(path, FMOD_STUDIO_LOAD_BANK_NORMAL));
			System.out.println("Loaded " + path + " into studio.");
		}
	}

	/**
	 * Play an event once.
	 * @param path Path to the event to play
	 */
	public static void playOneShot(String path) {
		EventInstance inst = studio.getEvent(path).createInstance();
		inst.start();
		inst.release();
	}

	/**
	 * Get a sound from the referenced path.
	 * @param path Path to event to get. This is not a directory, it is an FMod event path.
	 * @return EventInstance that references the sound.
	 */
	public static EventInstance getSound(String path) {
		EventDescription desc = studio.getEvent(path);
		desc.loadSampleData();
		return desc.createInstance();
	}

	/**
	 * Stop the sound referenced and allow it to fade out naturally if that behavior is defined.
	 * @param inst EventInstance to stop
	 */
	public static void stopSound(EventInstance inst) {
		inst.stop(FMOD_STUDIO_STOP_MODE.FMOD_STUDIO_STOP_ALLOWFADEOUT);
	}
}
