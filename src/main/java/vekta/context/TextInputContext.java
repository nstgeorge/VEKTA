package vekta.context;

import processing.event.KeyEvent;
import vekta.KeyBinding;

import java.util.function.Consumer;

import static processing.core.PConstants.ESC;
import static vekta.Vekta.*;

/**
 * Context for receiving a user input string.
 */
public class TextInputContext implements Context {
	private static final int BLINK_RATE = 30;

	private final Context back;
	private final String prompt;
	private final Consumer<String> callback;

	private String text;
	private boolean startedTyping;

	public TextInputContext(Context back, String prompt, String text, Consumer<String> callback) {
		this(back, prompt, callback);

		setText(text);
	}

	public TextInputContext(Context back, String prompt, Consumer<String> callback) {
		this.prompt = prompt;
		this.back = back;
		this.callback = callback;
	}

	public String getPrompt() {
		return prompt;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text != null ? text : "";
	}

	@Override
	public void focus() {
	}

	@Override
	public void render() {
		v.clear();

		v.textSize(32);
		v.fill(v.color(100));
		v.textAlign(CENTER, CENTER);
		v.rectMode(CENTER);
		v.text(getPrompt(), v.width / 2F, v.height / 2F - 100);

		String text = " " + getText() + (v.frameCount / BLINK_RATE % 2 == 0 ? "_" : " ");

		// Draw outline
		v.stroke(UI_COLOR);
		v.noFill();
		v.rect(v.width / 2F, v.height / 2F, max(200, v.textWidth(text) + 40), 60);

		// Draw input text
		v.fill(startedTyping ? 255 : UI_COLOR);
		v.text(text, v.width / 2F, v.height / 2F - 3);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		switch(event.getKey()) {
		case ESC:
			close(null);
			break;
		case ENTER:
		case RETURN:
			closeIfValid();
			break;
		case BACKSPACE:
			if(!getText().isEmpty()) {
				setText(startedTyping ? getText().substring(0, getText().length() - 1) : "");
			}
			break;
		}
	}

	@Override
	public void keyTyped(char key) {
		startedTyping = true;
		setText(getText() + key);
	}

	@Override
	public void keyPressed(KeyBinding key) {
		if(key == KeyBinding.MENU_SELECT) {
			closeIfValid();
		}
	}

	public void closeIfValid() {
		if(!getText().isEmpty()) {
			close(getText());
		}
	}

	public void close(String value) {
		setContext(back);
		if(value != null) {
			callback.accept(value.trim());
		}
	}
}
