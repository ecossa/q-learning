package sk.hackcraft.learning.bot;

import sk.hackcraft.bwu.Game;
import sk.hackcraft.learning.Learning;
import jnibwapi.Unit;

public class UnitController {

	private UnitState [] states;
	
	private Learning learning;
	
	private Unit unit;
	
	private UnitState lastState = null;

	private UnitAction executingAction = null;
	
	private int possibleStateChangeFrame = 0;
	
	public UnitController(UnitState[] states, Learning learning, Unit unit) {
		this.unit = unit;
		this.states = states;
	}
	
	public void update(Game game) {
		if(shouldDecideAction(game)) {
			UnitState currentState = detectState();
			
			if(lastState != null) {
				double reward = currentState.getValue(game, unit) - lastState.getValue(game, unit);
				learning.experience(lastState, executingAction, currentState, reward);
			}
			
			executingAction = (UnitAction)learning.estimateBestActionIn(currentState);
			lastState = currentState;
			
			executingAction.executeOn(game, unit);
			possibleStateChangeFrame += 15;
		}
	}
	
	private boolean shouldDecideAction(Game game) {
		return lastState == null || game.getFrameCount() >= possibleStateChangeFrame;
	}
	
	private UnitState detectState() {
		for(UnitState state : states) {
			if(state.isUnitInIt(unit)) {
				return state;
			}
		}
		throw new IllegalStateException("Unit has no detected state");
	}
}
