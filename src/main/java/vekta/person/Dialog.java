package vekta.person;

import vekta.Player;
import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.menu.option.BasicOption;
import vekta.menu.option.DialogOption;
import vekta.menu.option.MenuOption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static vekta.Vekta.*;

public class Dialog implements Serializable {
	private final String type;
	private final Person person;
	private final String message;

	private final List<String> responses = new ArrayList<>();
	private final List<MenuOption> options = new ArrayList<>();
	private Dialog next;

	private boolean visited;

	public Dialog(String type, Person person, String message) {
		this.type = type;
		this.person = person;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public Person getPerson() {
		return person;
	}

	public String getMessage() {
		return message;
	}

	public boolean isVisited() {
		return visited;
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

	public Dialog getNext() {
		return next;
	}

	public boolean hasNext() {
		return next != null;
	}

	public Dialog then(String next) {
		return then(getPerson().createDialog(next));
	}

	public Dialog then(String next, float chance) {
		return v.chance(chance) ? then(next) : this;
	}

	public Dialog then(Dialog dialog) {
		if(next == null) {
			next = dialog;
		}
		else {
			next.then(dialog);
		}
		return this;
	}

	public void parseResponse(String text) {
		if(text.startsWith(":")) {
			String[] args = text.split(" ", 2);
			Dialog next = getPerson().createDialog(args[0].substring(1).trim());
			add(new DialogOption(args[1].trim(), next));
			next.then(this);
		}
		else {
			addResponse(text);
		}
	}

	public void add(MenuOption option) {
		options.add(option);
	}

	public void add(String response, Dialog dialog) {
		add(new DialogOption(response, dialog));
	}

	//	public void openMenu(Menu menu) {
	//		openMenu(menu.getPlayer(), new BackOption(menu));
	//	}

	public void openMenu(Player player, MenuOption def) {
		if(visited && hasNext()) {
			getNext().openMenu(player, def);
			return;
		}
		visited = true;

		Menu menu = new Menu(player, new DialogMenuHandle(def, this));
		boolean hasResponses = !getResponses().isEmpty();
		List<String> responses = new ArrayList<>(hasResponses ? getResponses() : Collections.singletonList("Next"));
		Collections.shuffle(responses);
		for(String response : responses) {
			if(hasNext()) {
				menu.add(new DialogOption(response, getNext()));
			}
			else if(hasResponses) {
				// Use custom responses for the default option text
				menu.add(new BasicOption(response, menu.getDefault()));
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
