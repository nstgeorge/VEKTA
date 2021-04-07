package vekta.audio;

import org.fmod.FMODLoader;
import org.fmod.studio.FmodStudioSystem;

import java.io.File;
import java.util.Objects;

import static org.fmod.jni.FMODConstants.*;

public final class Driver {
	private static final String BANK_DIR = "../resources/audio/banks";

	private static FmodStudioSystem studio;

	/**
	 * Load the FMOD natives and initialize the studio system
	 */
	public static void init() {
		if(FMODLoader.loadNatives()) {
			System.out.println("FMOD natives loaded.");
		} else {
			System.out.println("Failed to load FMOD natives.");
		}

		studio = FmodStudioSystem.create();
		loadBanks(studio, new File(BANK_DIR).list());

	}

	private static void loadBanks(FmodStudioSystem studio, String... paths) {
		Objects.requireNonNull(paths, "Driver.loadBanks was provided no banks to load.");
		for(String path : paths) {
			studio.loadBankFile(path, FMOD_STUDIO_LOAD_BANK_NORMAL);
		}
	}
}
