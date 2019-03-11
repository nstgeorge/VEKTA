package vekta.person;

import vekta.Player;
import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.menu.option.DialogOption;
import vekta.menu.option.MenuOption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static vekta.Vekta.*;

public class Dialog implements Serializable {
	private final String type;
	private final String message;
	private final Person person;

	private final List<String> responses = new ArrayList<>();
	private final List<Dialog> continuations = new ArrayList<>();
	private final List<MenuOption> options = new ArrayList<>();

	public Dialog(String type, String message, Person person) {
		this.type = type;
		this.message = message;
		this.person = person;
	}

	public String getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public Person getPerson() {
		return person;
	}

	public List<String> getResponses() {
		return responses;
	}

	public void addResponse(String response) {
		responses.add(response);
	}

	public List<MenuOption> getOptions() {
		return options;
	}

	public void addContinuation(Dialog dialog) {
		continuations.add(dialog);
	}

	public void add(MenuOption option) {
		options.add(option);
	}

	public void add(String response, Dialog dialog) {
		add(new DialogOption(response, dialog));
	}

	public void openMenu(Menu menu) {
		openMenu(menu.getPlayer(), menu.getDefault());
	}

	public void openMenu(Player player, MenuOption def) {
		Menu menu = new Menu(player, new DialogMenuHandle(def, this));
		if(!continuations.isEmpty()) {
			Dialog next = v.random(continuations);
			List<String> responses = getResponses().isEmpty() ? Collections.singletonList("Next") : getResponses();
			for(String response : responses) {
				menu.add(new DialogOption(response, next));
			}
		}

		if(!getOptions().isEmpty()) {
			for(MenuOption option : getOptions()) {
				menu.add(option);
			}
		}
		else {
			menu.addDefault();
		}
		setContext(menu);
		applyContext();
	}
}
