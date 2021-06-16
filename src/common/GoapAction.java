package common;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;

public abstract class GoapAction {
	private HashSet<SimpleEntry<String, Object>> preconditions;
	private HashSet<SimpleEntry<String, Object>> effects;

	private boolean inRange = false;

	/*
	 * The cost of performing the action. Figure out a weight that suits the action.
	 * Changing it will affect what actions are chosen during planning.
	 */
	public float cost = 1f;

	/**
	 * An action often has to perform on an object. This is that object. Can be
	 * null.
	 */
	public IGameObject target;

	public GoapAction() {
		preconditions = new HashSet<SimpleEntry<String, Object>>();
		effects = new HashSet<SimpleEntry<String, Object>>();
	}

	public void doReset() {
		inRange = false;
		target = null;
		reset();
	}

	/**
	 * Reset any variables that need to be reset before planning happens again.
	 */
	public abstract void reset();

	/**
	 * Is the action done?
	 */
	public abstract boolean isDone();

	/**
	 * Procedurally check if this action can run. Not all actions will need this,
	 * but some might.
	 */
	public abstract boolean checkProceduralPrecondition(IGameObject agent);

	/**
	 * Run the action. Returns True if the action performed successfully or false if
	 * something happened and it can no longer perform. In this case the action
	 * queue should clear out and the goal cannot be reached.
	 */
	public abstract boolean perform(IGameObject agent);

	/**
	 * Does this action need to be within range of a target game object? If not then
	 * the moveTo state will not need to run for this action.
	 */
	public abstract boolean requiresInRange();

	/**
	 * Are we in range of the target? The MoveTo state will set this and it gets
	 * reset each time this action is performed.
	 */
	public boolean isInRange() {
		return inRange;
	}

	public void setInRange(boolean inRange) {
		this.inRange = inRange;
	}

	public void addPrecondition(String key, Object value) {
		preconditions.add(new SimpleEntry<String, Object>(key, value));
	}

	SimpleEntry<String, Object> getEntry(HashSet<SimpleEntry<String, Object>> set, String key) {
		return set.stream().filter(kvp -> kvp.getKey().equals(key)).findFirst().orElseThrow();
	}

	public boolean removePrecondition(String key) {
		var remove = getEntry(preconditions, key);

		return preconditions.remove(remove);
	}

	public void addEffect(String key, Object value) {
		effects.add(new SimpleEntry<String, Object>(key, value));
	}

	public boolean removeEffect(String key) {
		var remove = getEntry(effects, key);

		return effects.remove(remove);
	}

}
