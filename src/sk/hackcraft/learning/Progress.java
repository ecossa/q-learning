package sk.hackcraft.learning;

public class Progress {
	
	// TODO: toString for states and actions
	
	public static void showRewardArray (int[] rewardArray, State[] states) {
		for (int state = 0; state < states.length; state++) {
			System.out.println ("Reward for state: " + rewardArray[state]);
		}
	}
	
	public static void showQmatrixForState (int[][] qMatrix, Action[] actions, State currentState, int stateIndex) {
		System.out.println("For state: " + currentState.toString());
		for (int action = 0; action < actions.length; action++) {
			System.out.print("Act: " + actions[action].toString() 
					+ " with qValue" + qMatrix[stateIndex][action]);
		}
	}
	
	public static void showStep (State currentState, Action action, int qValue, int rewardValue) {
		System.out.println("From state " + currentState 
				+ " do action "+ action 
				+ " with qValue " + qValue 
				+ "  and rewardValue " + rewardValue);
	}
}
