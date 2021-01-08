package vekta.menu.option.input;

import java.util.Arrays;
import java.util.List;

public class YesNoInputController extends ChoicesInputController<Boolean> {
	private static final List<Boolean> CHOICES = Arrays.asList(true, false);

	public YesNoInputController() {
		super(CHOICES, b -> b ? "Yes" : "No");
	}
}
