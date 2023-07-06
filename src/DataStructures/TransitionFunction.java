package DataStructures;

import DataStructures.DFA.MultiState;

public class TransitionFunction {

    private String condition;
    private State currentState;
    private MultiState currentMultiState;
    private State nextState;
    private MultiState nextMultiState;

    public TransitionFunction(State currentState, String condition, State nextState) {
        this.currentState = currentState;
        this.condition = condition;
        this.nextState = nextState;
    }

    public TransitionFunction(MultiState currentState, String condition, MultiState nextState) {
        this.currentMultiState = currentState;
        this.condition = condition;
        this.nextMultiState = nextState;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getNextState() {
        return nextState;
    }

    public void setNextState(State nextState) {
        this.nextState = nextState;
    }

    public MultiState getCurrentMultiState() {
        return currentMultiState;
    }

    public MultiState getNextMultiState() {
        return nextMultiState;
    }

    @Override
    public String toString() {
        return currentState + "," + condition + "->" + nextState;
    }
}
