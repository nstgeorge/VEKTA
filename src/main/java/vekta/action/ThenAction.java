package vekta.action;

import vekta.action.runner.Runner;

public class ThenAction implements Action {

	private final Action complete;
	private final Action cancel;

	public ThenAction(Action next, boolean complete, boolean cancel) {
		this(complete ? next : null, cancel ? next : null);
	}

	public ThenAction(Action complete, Action cancel) {
		this.complete = complete;
		this.cancel = cancel;
	}

	@Override
	public void onComplete(Runner runner) {
		if(complete != null) {
			runner.async(complete);
		}
	}

	@Override
	public void onCancel(Runner runner) {
		if(cancel != null) {
			runner.async(cancel);
		}
	}
}
