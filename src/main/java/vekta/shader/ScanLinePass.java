package vekta.shader;

import ch.bildspur.postfx.Supervisor;
import ch.bildspur.postfx.pass.Pass;
import jdk.internal.util.xml.impl.Input;
import processing.core.PGraphics;
import processing.opengl.PShader;
import sun.misc.IOUtils;
import vekta.Settings;

import java.io.*;
import java.nio.Buffer;
import java.util.Objects;

import static vekta.Vekta.v;

public class ScanLinePass implements Pass {
	private final PShader shader;

	public ScanLinePass() {
		shader = v.loadShader("shader/ScanLineFrag.glsl");
	}

	@Override
	public void prepare(Supervisor supervisor) {
		shader.set("count", 50.0f);
		shader.set("resolution", (float)v.width, (float)v.height);
		shader.set("brightnessBoost", 0.001f * Settings.getFloat("scanLineIntensity"));
		shader.set("time", (float)v.millis() / 1000.0f);
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
