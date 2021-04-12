package vekta.action;

import vekta.action.runner.Runner;
import vekta.world.World;

public class BasicAction implements Action {

	private final World.Callback handler;

	public BasicAction(World.Callback handler) {
		this.handler = handler;
	}

	@Override
	public void onStart(Runner runner) {
		handler.callback();
		runner.complete();
	}
}
