package DataStructures.NFA;

class TransitionFunction {

    private char condition;
    private State currentState;
    private State nextState;

    public TransitionFunction(State currentState, char condition, State nextState) {
        this.currentState = currentState;
        this.condition = condition;
        this.nextState = nextState;
    }

    public char getCondition() {
        return condition;
    }

    public void setCondition(char condition) {
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

    @Override
    public String toString() {
        return condition + "->" + nextState;
    }
}
