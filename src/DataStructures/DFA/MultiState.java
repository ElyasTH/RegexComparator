package DataStructures.DFA;

import DataStructures.State;
import DataStructures.TransitionFunction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class MultiState {

    private HashSet<State> states = new HashSet<>();
    private HashSet<TransitionFunction> transitions = new HashSet<>();

    public void addState(State state){
        states.add(state);
    }

    public void addStates(HashSet<State> states){
        this.states.addAll(states);
    }

    public void addTransition(TransitionFunction transition){
        transitions.add(transition);
    }

    public static HashSet<HashSet<TransitionFunction>> getSameConditionTransitions(MultiState state){

        HashSet<HashSet<TransitionFunction>> result = new HashSet<>();

        ArrayList<TransitionFunction> allTransitions = new ArrayList<>();
        for (State subState: state.getStates()){
            allTransitions.addAll(subState.getTransitions());
        }

        HashSet<TransitionFunction> visitedTransitions = new HashSet<>();
        for (int i = 0; i < allTransitions.size(); i++){
            TransitionFunction current = allTransitions.get(i);
            if (visitedTransitions.contains(current)) continue;
            visitedTransitions.add(current);
            HashSet<TransitionFunction> currentSet = new HashSet<>();
            currentSet.add(current);

            for (int j = i + 1; j < allTransitions.size(); j++){
                TransitionFunction current2 = allTransitions.get(j);
                if (visitedTransitions.contains(current2)) continue;
                if (current.getCondition().equals(current2.getCondition())){
                    visitedTransitions.add(current2);
                    currentSet.add(current2);
                }
            }

            result.add(currentSet);
        }

        return result;
    }

    public HashSet<State> getStates() {
        return states;
    }

    public void setStates(HashSet<State> states) {
        this.states = states;
    }

    public HashSet<TransitionFunction> getTransitions() {
        return transitions;
    }

    public void setTransitions(HashSet<TransitionFunction> transitions) {
        this.transitions = transitions;
    }

    public TransitionFunction getTransitionByCondition(String condition){
        for (TransitionFunction transition: transitions){
            if (transition.getCondition().equals(condition)) return transition;
        }
        return null;
    }

    public boolean equals(MultiState other){
        return this.getStates().equals(other.getStates());
    }
}
