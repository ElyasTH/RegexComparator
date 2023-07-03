package DataStructures.NFA;

import java.util.ArrayList;
import java.util.HashSet;

class TransitionFunction {

    private HashSet<Character> conditions;
    private State nextState;

    public TransitionFunction(HashSet<Character> conditions, State nextState) {
        this.conditions = conditions;
        this.nextState = nextState;
    }

    public HashSet<Character> getConditions() {
        return conditions;
    }

    public void setConditions(HashSet<Character> conditions) {
        this.conditions = conditions;
    }

    public State getNextState() {
        return nextState;
    }

    public void setNextState(State nextState) {
        this.nextState = nextState;
    }

    @Override
    public String toString() {
        return conditions + "->" + nextState;
    }
}
