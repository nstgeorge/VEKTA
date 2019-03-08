package vekta.person;

import vekta.menu.option.DialogOption;
import vekta.menu.option.MenuOption;

import java.util.ArrayList;
import java.util.List;

public class Dialog {
	private final String type;
	private final String message;
	private final Person person;

	private final List<String> responses = new ArrayList<>();
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

	public void add(Dialog dialog) {
		for(String response : getResponses()) {
			add(response, dialog);
		}
	}

	public void add(MenuOption option) {
		options.add(option);
	}

	public void add(String response, Dialog dialog) {
		add(new DialogOption(response, dialog));
	}
}
