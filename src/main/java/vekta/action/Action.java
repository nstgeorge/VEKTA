package vekta.action;

import vekta.action.runner.Runner;
import vekta.world.RenderLevel;

import java.io.Serializable;

public interface Action extends Serializable {
	/**
	 * Can the action be restarted?
	 *
	 * @return `false` by default
	 */
	default boolean isRestartable() {
		return false;
	}

	/**
	 * Called when the action is started (and restarted)
	 */
	default void onStart(Runner runner) {
	}

	/**
	 * Called when the action is either completed or cancelled
	 */
	default void onEnd(Runner runner) {
	}

	/**
	 * Called when the action is completed
	 */
	default void onComplete(Runner runner) {
	}

	/**
	 * Called when the action is cancelled
	 */
	default void onCancel(Runner runner) {
	}

	/**
	 * Called just after the object is updated
	 */
	default void onUpdate(Runner runner) {
	}

	/**
	 * Called just before the object is drawn
	 */
	default void onDraw(Runner runner, RenderLevel level, float r) {
	}
}