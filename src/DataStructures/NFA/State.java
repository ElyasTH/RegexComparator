package DataStructures.NFA;

import java.util.ArrayList;
import java.util.HashSet;

class State {

    private int stateId;
    private HashSet<TransitionFunction> transitions;
    private boolean isInitial;
    private boolean isFinal;

    public State(int stateId) {
        this.stateId = stateId;
    }

    public TransitionFunction addTransition(HashSet<Character> conditions, State nextState){
        TransitionFunction newTransition = new TransitionFunction(conditions, nextState);
        transitions.add(newTransition);
        return newTransition;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public HashSet<TransitionFunction> getTransitions() {
        return transitions;
    }

    public void setTransitions(HashSet<TransitionFunction> transitions) {
        this.transitions = transitions;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public void setInitial(boolean initial) {
        isInitial = initial;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    @Override
    public String toString() {
        return "q" + stateId + "isInitial: " + isInitial + "isFinal: " + isFinal;
    }
}
