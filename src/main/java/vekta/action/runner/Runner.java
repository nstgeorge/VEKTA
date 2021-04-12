package vekta.action.runner;

import vekta.action.Action;
import vekta.action.BasicAction;
import vekta.object.SpaceObject;
import vekta.world.World;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static vekta.Vekta.v;

public class Runner implements Serializable {
	private static final Logger LOG = Logger.getLogger(Runner.class.getName());

	private final SpaceObject object;
	private final Action action;
	private final List<Runner> completeRunners = new ArrayList<>();
	private final List<Runner> cancelRunners = new ArrayList<>();

	private RunnerState state = RunnerState.READY;
	private int startMillis = 0;

	public Runner(SpaceObject object, Action action) {
		this.object = object;
		this.action = action;
	}

	public final SpaceObject getObject() {
		return object;
	}

	public Action getAction() {
		return action;
	}

	/**
	 * Get the current state of the action (ready, started, completed, cancelled, etc.)
	 */
	public final RunnerState getState() {
		return state;
	}

	/**
	 * Get the number of seconds since the action started, or else 0.
	 */
	public final float getRunTimeSeconds() {
		if(getState() != RunnerState.STARTED) {
			return 0;
		}
		return (v.millis() - startMillis) / 1000f;
	}

	/**
	 * Start the action (cannot be restarted by default)
	 */
	public final void start() {
		if(action.isRestartable() || checkValid("start", RunnerState.READY)) {
			state = RunnerState.STARTED;
			startMillis = v.millis();
			//			register(this); // Add to the current world
			getObject().notifyRunner(this);
			action.onStart(this);
		}
	}

	/**
	 * Complete the running action (mutually exclusive with `cancel()`)
	 */
	public final void complete() {
		if(checkValid("complete", RunnerState.STARTED)) {
			state = RunnerState.COMPLETED;
			getObject().notifyRunner(this);
			action.onComplete(this);
			action.onEnd(this);
			for(Runner runner : completeRunners) {
				runner.start();
			}
		}
	}

	/**
	 * Cancel the running action (mutually exclusive with `complete()`)
	 */
	public final void cancel() {
		if(checkValid("cancel", RunnerState.STARTED)) {
			state = RunnerState.CANCELLED;
			getObject().notifyRunner(this);
			action.onCancel(this);
			action.onEnd(this);
			for(Runner runner : cancelRunners) {
				runner.start();
			}
		}
	}

	private boolean checkValid(String transitionName, RunnerState expected) {
		RunnerState state = getState();
		if(state != expected) {
			LOG.warning("Tried to " + transitionName + " action in state " + getState().name() + " (" + getAction().getClass().getSimpleName() + " on " + getObject().getName() + ")");
			return false;
		}
		return true;
	}

	/**
	 * Run another action after completion and/or cancellation.
	 *
	 * @param action   The action to run
	 * @param complete Run when completed?
	 * @param cancel   Run when cancelled?
	 */
	public Runner then(Action action, boolean complete, boolean cancel) {
		Runner next = new Runner(getObject(), action);
		boolean started = false;
		if(complete) {
			completeRunners.add(next);
			if(getState() == RunnerState.COMPLETED) {
				next.start();
				started = true;
			}
		}
		if(cancel) {
			cancelRunners.add(next);
			if(getState() == RunnerState.CANCELLED && !started) {
				next.start();
			}
		}
		return next;
	}

	/**
	 * Run another action after completion. Similar to `Promise::then()` in JavaScript
	 *
	 * @param complete The action to run
	 */
	public Runner then(Action complete) {
		return then(complete, true, false);
	}

	/**
	 * Run a callback function after completion. Similar to `Promise::then()` in JavaScript
	 *
	 * @param complete The function to run
	 */
	public Runner then(World.Callback complete) {
		return then(new BasicAction(complete));
	}

	/**
	 * Run another action in parallel (equivalent to `getObject().async(action)`)
	 *
	 * @param action The action to run
	 */
	public Runner async(Action action) {
		return getObject().async(action);
	}
}
