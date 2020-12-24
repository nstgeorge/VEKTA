package vekta.shader;

import ch.bildspur.postfx.Supervisor;
import ch.bildspur.postfx.pass.Pass;
import processing.core.PGraphics;
import processing.opengl.PShader;
import vekta.Settings;

import static vekta.Vekta.v;

public class ScanLinePass implements Pass {
	private final PShader shader;

	public ScanLinePass() {
		String name = "ScanLineFrag";

		//		try {
		//			File file = File.createTempFile(name, ".glsl");
		//
		//			IOUtil.copyStream2File(getClass().getResourceAsStream("/shader/ScanLineFrag.glsl"), file, -1);
		//
		//			shader = v.loadShader(file.getAbsolutePath());
		//		}
		//		catch(IOException e) {
		//			throw new RuntimeException("Failed to load shader: " + name, e);
		//		}

		shader = v.loadShader("shader/ScanLineFrag.glsl");
	}

	@Override
	public void prepare(Supervisor supervisor) {
		// Scan-line uniforms
		shader.set("count", 50.0f);
		shader.set("resolution", (float)v.width, (float)v.height);
		shader.set("brightnessBoost", 0.001f * Settings.getFloat("scanLineIntensity"));
		shader.set("time", (float)v.millis() / 1000.0f);

		// Vignette uniforms
		shader.set("vigInnerRad", 0.25f);
		shader.set("vigOuterRad", 1.35f);
		shader.set("vigOpacity", 0.12f);

		// Dithering uniforms
		shader.set("dithMixScale", 0.4f);
		shader.set("dithNoiseScale", 5.0f);

		// Fisheye uniforms
		shader.set("fishLensPower", 0.98f);
	}

	@Override
	public void apply(Supervisor supervisor) {
		PGraphics pass = supervisor.getNextPass();
		supervisor.clearPass(pass);
		shader.set("time", (float)v.millis() / 1000.0f);
		pass.beginDraw();
		pass.shader(shader);
		pass.image(supervisor.getCurrentPass(), 0, 0);
		pass.endDraw();
	}
}
