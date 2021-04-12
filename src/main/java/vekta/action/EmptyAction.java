package vekta.action;

import vekta.action.runner.Runner;

public class EmptyAction implements Action {
	public static final EmptyAction INSTANCE = new EmptyAction();

	private EmptyAction() {
	}

	@Override
	public void onStart(Runner runner) {
		runner.complete();
	}
}
