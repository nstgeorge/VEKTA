package vekta.audio;

import com.google.common.io.ByteSource;
import org.fmod.FMODLoader;
import org.fmod.jni.*;
import org.fmod.lowlevel.FMODResultTracker;
import org.fmod.studio.Bank;
import org.fmod.studio.EventDescription;
import org.fmod.studio.EventInstance;
import org.fmod.studio.FmodStudioSystem;
import vekta.Resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

import static org.fmod.jni.FMODConstants.*;
import static vekta.Vekta.createGameDirectory;

/**
 * For now, a pretty barebones audio system that loads in FMOD. May be expanded for further usage later.
 */
public final class AudioDriver {
	private static final File CACHE_DIRECTORY = createGameDirectory("cache/FMOD");

	private static final Logger LOG = Logger.getLogger(AudioDriver.class.getName());

	private static final int MAX_CHANNELS = 32;

	//	private static final List<Bank> BANKS = new ArrayList<>();

	private static FmodStudioSystem studio;

	/**
	 * Load the FMOD natives and initialize the studio system with banks
	 */
	public static void init() {
		if(!FMODLoader.loadNatives()) {
			LOG.warning("Failed to load FMOD natives.");
		}

		studio = FmodStudioSystem.create();
		studio.initialize(MAX_CHANNELS, FMOD_STUDIO_INIT_NORMAL, FMOD_INIT_NORMAL, null);

		// Remove previous cache files
		for(File file : Objects.requireNonNull(CACHE_DIRECTORY.listFiles())) {
			if(!file.isDirectory() && !file.delete()) {
				LOG.warning("Unable to delete previously cached audio bank: " + file.getPath());
			}
		}

		for(String path : Resources.getAudioBankPaths()) {
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
	 * Load an audio bank from the classpath into the studio.
	 *
	 * @param path Classpath entry to load
	 */
	private static void loadBank(String path) {
		LOG.info("Loading audio bank: " + path);

		try {
			ByteSource source = new ByteSource() {
				@Override
				public InputStream openStream() {
					return AudioDriver.class.getResourceAsStream("/" + path);
				}
			};
			//			_loadBankMemory(source.read(), FMOD_STUDIO_LOAD_BANK_NORMAL);
			//			studio.loadBankFile("src/main/resources/" + path, FMOD_STUDIO_LOAD_BANK_NORMAL);
			File file = new File(CACHE_DIRECTORY, path.replace("/", "_").replace("\\", "_").replace(" ", "_"));
			try(OutputStream stream = new FileOutputStream(file)) {
				source.copyTo(stream);
			}
			studio.loadBankFile(file.getPath(), FMOD_STUDIO_LOAD_BANK_NORMAL);
		}
		catch(Exception e) {
			LOG.warning("Unable to load audio bank: " + path);
			e.printStackTrace();
		}
	}

	/**
	 * Binding implementation for `StudioSystem::loadBankMemory(String buffer, int length, FMOD_STUDIO_LOAD_MEMORY_MODE mode, long flags, Bank bank)`
	 */
	@SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
	private static Bank _loadBankMemory(byte[] buffer, long studioLoadBankFlags) throws ReflectiveOperationException {
		// This is almost working; we need to figure out how to pass a `String` so that JNI properly converts it to a `char*` buffer

		//		String bufferString = StandardCharsets.US_ASCII.decode(ByteBuffer.wrap(buffer)).toString();
		String bufferString = new String(buffer);
		System.out.println("::::::::::::::  " + bufferString.length() + " / " + buffer.length);//////////////////////

		SWIGTYPE_p_p_FMOD_STUDIO_BANK pp = FMOD.new_FMOD_STUDIO_BANK_p_p();
		Method method = FMODResultTracker.class.getDeclaredMethod("processApiResult", FMOD_RESULT.class, String.class);
		method.setAccessible(true);
		method.invoke(studio, FMOD.FMOD_Studio_System_LoadBankMemory(
				studio.getPointer(),
				bufferString,
				buffer.length,
				FMOD_STUDIO_LOAD_MEMORY_MODE.FMOD_STUDIO_LOAD_MEMORY,
				studioLoadBankFlags,
				pp
		), "StudioSystem.loadBankMemory");
		SWIGTYPE_p_FMOD_STUDIO_BANK p = FMOD.FMOD_STUDIO_BANK_p_p_value(pp);
		FMOD.delete_FMOD_STUDIO_BANK_p_p(pp);
		Constructor<Bank> constructor = Bank.class.getDeclaredConstructor(SWIGTYPE_p_FMOD_STUDIO_BANK.class);
		constructor.setAccessible(true);
		return constructor.newInstance(p);
	}

	/**
	 * Create an `EventInstance` from the referenced path.
	 *
	 * @param path Path to event to get. This is not a directory, it is an FMod event path.
	 * @return EventInstance that references the sound.
	 */
	public static EventInstance createEventInstance(String path) {
		EventDescription desc = studio.getEvent(path);
		desc.loadSampleData();
		return desc.createInstance();
	}
}
