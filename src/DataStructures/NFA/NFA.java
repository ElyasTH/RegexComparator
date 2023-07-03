package DataStructures.NFA;

import java.util.*;

public class NFA {

    private State initialState;
    private HashSet<State> finalStates;
    private HashSet<State> states;
    private int stateCount = 0;

    public State addState(boolean isFinal, boolean isInitial){
        State newState = new State(stateCount++);
        states.add(newState);
        if (isInitial){
            initialState = newState;
            newState.setInitial(true);
        }
        if (isFinal){
            finalStates.add(newState);
            newState.setFinal(true);
        }
        return newState;
    }

    public void removeState(State stateToRemove){
        states.remove(stateToRemove);
        finalStates.remove(stateToRemove);
        if (initialState == stateToRemove){
            initialState = null;
            System.out.println("Initial state is null now.");
        }
    }

    public State getInitialState() {
        return initialState;
    }

    public void setInitialState(State initialState) {
        this.initialState = initialState;
    }

    public HashSet<State> getFinalStates() {
        return finalStates;
    }

    public HashSet<State> getStates() {
        return states;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (State state: states){
            out.append(state).append("; transitions: ");
            for (TransitionFunction transition: state.getTransitions()){
                out.append(transition).append(',');
            }
            out.deleteCharAt(out.length()-1);
            out.append("\n");
        }
        return out.toString();
    }
}
