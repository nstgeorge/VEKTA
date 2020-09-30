package vekta.menu.option.input;

import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;

import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.setContext;

public class ChoicesInputController<T> implements InputController<T> {
	private final List<T> choices = new ArrayList<>();
	private final InputNamer<T> namer;
	private final boolean wrapEdges;

	public ChoicesInputController(Iterable<T> choices) {
		this(choices, null);
	}

	public ChoicesInputController(Iterable<T> choices, InputNamer<T> namer) {
		this(choices, namer, false);
	}

	public ChoicesInputController(Iterable<T> choices, InputNamer<T> namer, boolean wrapEdges) {
		this.namer = namer;
		this.wrapEdges = wrapEdges;

		for(T item : choices) {
			if(!this.choices.contains(item)) {
				this.choices.add(item);
			}
		}
	}

	public List<T> getChoices() {
		return choices;
	}

	@Override
	public String getName(T value) {
		return namer != null ? namer.getName(value) : String.valueOf(value);
	}

	@Override
	public boolean hasLeft(T value) {
		return wrapEdges ? InputController.super.hasLeft(value) : getChoices().size() > 1;
	}

	@Override
	public boolean hasRight(T value) {
		return wrapEdges ? InputController.super.hasRight(value) : getChoices().size() > 1;
	}

	@Override
	public T getLeft(T value) {
		return getChoices().get((getChoices().indexOf(value) - 1 + getChoices().size()) % getChoices().size());
	}

	@Override
	public T getRight(T value) {
		return getChoices().get((getChoices().indexOf(value) + 1) % getChoices().size());
	}

	@Override
	public String getSelectVerb() {
		return choices.size() == 2 ? "toggle" : "select";
	}

	@Override
	public void select(Menu menu, InputWatcher<T> watcher) {
		if(getChoices().size() == 2) {
			watcher.setValue(getRight(watcher.getValue()));
		}
		else if(getChoices().size() > 2) {
			Menu sub = new Menu(menu, new MenuHandle());
			for(T choice : getChoices()) {
				sub.add(new ChoiceButton<>(watcher, this, choice));
			}
			sub.addDefault();
			setContext(sub);
		}
	}
}
