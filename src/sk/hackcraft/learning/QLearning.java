package sk.hackcraft.learning;

import java.util.HashMap;

public class QLearning implements Learning {

	// learning rates
	private double alpha = 0.1;
	private double gamma = 0.9;
	
	private int rewardLimit = 1;

	private State[] states;
	private Action[] actions;

	private double[][] qMatrix;
	
	private double[] rewardArray;

	private HashMap<State, Integer> stateIndices = new HashMap<>();
	private HashMap<Action, Integer> actionIndices = new HashMap<>();

	public QLearning(State[] states, Action[] actions) {
		this.states = states;
		this.actions = actions;
		buildIndices();
		buildMatrix();
	}
	
	private void buildMatrix() {
		qMatrix = new double [states.length][actions.length];
	}

	private void buildIndices() {
		for (int i = 0; i < states.length; i++) {
			stateIndices.put(states[i], i);
		}
		for (int i = 0; i < actions.length; i++) {
			actionIndices.put(actions[i], i);
		}
	}

	// TODO Optimize, maybe not needed every time, use sime kind of caching and invalidation for states.
	protected double maxQ(int stateIndex) {
		double maxValue = Double.MIN_VALUE;

		for (int actionIndex = 0; actionIndex < actions.length; actionIndex++) {
			maxValue = Math.max(qMatrix[stateIndex][actionIndex], maxValue);
		}
		return maxValue;
	}

	@Override
	public Action estimateBestActionIn(State state) {
		int stateIndex = stateIndices.get(state);
		int bestActionIndex = -1;
		double maxValue = Double.MIN_VALUE;

		for (int actionIndex = 0; actionIndex < actions.length; actionIndex++) {
			double value = qMatrix[stateIndex][actionIndex];

			if (value > maxValue) {
				maxValue = value;
				bestActionIndex = actionIndex;
			}
		}
		return actions[bestActionIndex];
	}

	public void experience(State currentState, Action action, State nextState, double reward) {
		int currentStateIndex = stateIndices.get(currentState);
		int nextStateIndex = stateIndices.get(nextState);
		int actionIndex = actionIndices.get(action);
		
		// Using this possible action, consider to go to the next state
		double q = qMatrix[currentStateIndex][actionIndex];
		double r = getReward(currentStateIndex);

		double maxQ = maxQ(nextStateIndex);

		double value = q + alpha * (r + gamma * maxQ - q);
		qMatrix[currentStateIndex][actionIndex] = value;
	}
	
	public double getReward(int state) {
		return rewardArray[state];
	}
	
	public void setBaseReward (int initialState, int okState, int badState) {
		rewardArray[badState] = rewardLimit *(-1);
		rewardArray[initialState] = 0;
		rewardArray[okState] = rewardLimit;
	}
	
	public void spreadReward(int currentState, double rewardValue) {
		rewardArray[currentState] = rewardValue * gamma;
	}
}
