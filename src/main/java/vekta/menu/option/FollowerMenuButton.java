package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.FollowerMenuHandle;
import vekta.person.Dialog;
import vekta.person.Person;
import vekta.player.Player;

import java.util.List;
import java.util.stream.Collectors;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.setContext;

public class FollowerMenuButton extends ButtonOption {
	private final List<Person> followers;

	public FollowerMenuButton(Player player) {
		followers = getWorld().findObjects(Person.class).stream()
				.filter(person -> person.getFaction() == player.getFaction())
				.collect(Collectors.toList());
	}

	@Override
	public String getName() {
		return "Followers";
	}

	@Override
	public boolean isEnabled() {
		return !followers.isEmpty();
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new FollowerMenuHandle());

		for(Person person : followers) {
			Dialog dialog = person.createDialog("follower");
			sub.add(new DialogButton(person.getName(), dialog));
		}

		sub.addDefault();
		setContext(sub);
	}
}
