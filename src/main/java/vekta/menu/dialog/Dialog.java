package vekta.menu.dialog;

import vekta.menu.option.MenuOption;
import vekta.person.Person;

import java.util.ArrayList;
import java.util.List;

public class Dialog {
	private final String message;
	private final Person person;

	private final List<MenuOption> options = new ArrayList<>();

	public Dialog(String message, Person person) {
		this.message = message;
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	public String getMessage() {
		return message;
	}

	public List<MenuOption> getOptions() {
		return options;
	}
}
