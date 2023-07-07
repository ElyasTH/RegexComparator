package DataStructures;

import java.util.HashSet;

public class State {

    private int stateId;
    private HashSet<TransitionFunction> transitions = new HashSet<>();

    public State(){}

    public State(int stateId){
        this.stateId = stateId;
    }
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

    public TransitionFunction getTransitionByCondition(String condition){
        for (TransitionFunction transition: transitions){
            if (transition.getCondition().equals(condition)) return transition;
        }
        return null;
    }

    public void setTransitions(HashSet<TransitionFunction> transitions) {
        this.transitions = transitions;
    }

    public boolean equals(State otherState){
        return this.stateId == otherState.stateId;
    }
    @Override
    public String toString() {
        return "q" + stateId;
    }
}
