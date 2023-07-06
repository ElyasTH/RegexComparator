package DataStructures;

import java.util.HashSet;

public class State {

    private int stateId;
    private HashSet<TransitionFunction> transitions = new HashSet<>();

    public TransitionFunction addTransition(String condition, State nextState){
        TransitionFunction newTransition = new TransitionFunction(this, condition, nextState);
        this.transitions.add(newTransition);
        return newTransition;
    }

    public void addTransition(TransitionFunction transition){
        this.transitions.add(transition);
        transition.setCurrentState(this);
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
    @Override
    public String toString() {
        return "q" + stateId;
    }
}
