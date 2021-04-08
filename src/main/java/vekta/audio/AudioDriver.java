package vekta.audio;

import org.fmod.FMODLoader;
import org.fmod.studio.FmodStudioSystem;
import org.fmod.studio.EventDescription;
import org.fmod.studio.EventInstance;
import vekta.Resources;

import java.util.*;
import java.util.logging.Logger;

import static org.fmod.jni.FMODConstants.*;

/**
 * For now, a pretty barebones audio system that loads in FMOD. May be expanded for further usage later.
 * Have fun, Ryan!
 */
public final class AudioDriver {

	private static final Logger LOG = Logger.getLogger(AudioDriver.class.getSimpleName());

	// This is a very, very temporary fix until we have a better method for accessing this data.
	private static final String RESOURCES_PATH = "src/main/resources/";

	//	private static final List<Bank> BANKS = new ArrayList<>();

	private static FmodStudioSystem studio;

	/**
	 * Load the FMOD natives and initialize the studio system with banks
	 */
	public static void init() {
		if(FMODLoader.loadNatives()) {
			LOG.info("FMOD natives loaded.");
		}
		else {
			LOG.warning("Failed to load FMOD natives.");
		}

		studio = FmodStudioSystem.create();
		studio.initialize(32, FMOD_STUDIO_INIT_NORMAL, FMOD_INIT_NORMAL, null);

		String[] paths = Resources.getLocatedAudioBanks()
				.stream()
				.map(e -> RESOURCES_PATH + e)
				.toArray(String[]::new);

		LOG.info("Found banks: " + Arrays.asList(paths));
		for(String path : paths) {
			loadBank(path);
		}
	}

	/**
	 * Get the FModStudioSystem currently in use. This can be used to access more advanced audio features outside of the AudioDriver.
	 *
	 * @return FModStudioSystem
	 */
	public static FmodStudioSystem getStudio() {
		return studio;
	}

	/**
	 * Load an audio bank into the studio.
	 *
	 * @param path Path to load. `Resources.getLocatedAudioBanks()` can be helpful here.
	 */
	private static void loadBank(String path) {
		LOG.info("Loading " + path + " into studio...");
		/*BANKS.add(*/
		studio.loadBankFile(path, FMOD_STUDIO_LOAD_BANK_NORMAL);
		System.out.println("Loaded " + path + " into studio.");
	}

	/**
	 * Play an event once.
	 *
	 * @param path Path to the event to play
	 */
	public static void playOneShot(String path) {
		EventInstance inst = studio.getEvent(path).createInstance();
		inst.start();
		inst.release();
	}

	/**
	 * Get a sound from the referenced path.
	 *
	 * @param path Path to event to get. This is not a directory, it is an FMod event path.
	 * @return EventInstance that references the sound.
	 */
	public static EventInstance getSound(String path) {
		EventDescription desc = studio.getEvent(path);
		desc.loadSampleData();
		return desc.createInstance();
	}
}
