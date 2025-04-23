package vekta.menu.option.input;

import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;

import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.setContext;

public class resolutionChoicesController<T> implements InputController<T> {

	private AspectRatioWatcher aspectRatio;

	private final List<T> choices16x9 = new ArrayList<>();
	private final List<T> choices21x9 = new ArrayList<>();
	private final List<T> choices3x4 = new ArrayList<>();

	public resolutionChoicesController(Iterable<T> choices16x9, Iterable<T> choices21x9, Iterable<T> choices3x4, AspectRatioWatcher a) {
		aspectRatio = a;

		for(T item : choices16x9) {
			if(!this.choices16x9.contains(item)) {
				this.choices16x9.add(item);
			}
		}

		for(T item : choices21x9) {
			if(!this.choices21x9.contains(item)) {
				this.choices21x9.add(item);
			}
		}

		for(T item : choices3x4) {
			if(!this.choices3x4.contains(item)) {
				this.choices3x4.add(item);
			}
		}
	}

	public List<T> getChoices() {
		switch(aspectRatio.findSetting()) {
			case "16x9":
				return choices16x9;
			case "21x9":
				return choices21x9;
			default:
				return choices3x4;
		}
	}

	@Override
	public String getName(T value) {
		return String.valueOf(value);
	}

	@Override
	public String getSelectVerb() {
		return "select";
	}

	@Override
	public void select(Menu menu, InputWatcher<T> watcher) {
		Menu sub = new Menu(menu, new MenuHandle());
		for(T choice : getChoices()) {
			sub.add(new ChoiceButton<>(watcher, this, choice));
		}
		sub.addDefault();
		setContext(sub);
	}
}
