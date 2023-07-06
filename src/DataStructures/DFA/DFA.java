package DataStructures.DFA;

import DataStructures.NFA.NFA;
import DataStructures.State;
import DataStructures.TransitionFunction;

import java.util.*;

public class DFA {

    private HashSet<MultiState> states = new HashSet<>();
    private MultiState initialState;
    private HashSet<MultiState> finalStates = new HashSet<>();

    public static DFA fromNFA(NFA nfa){
        DFA result = new DFA();
        HashSet<Character> alphabet = NFA.getAlphabet(nfa);

        MultiState initial = new MultiState();
        initial.addStates(getLambdaConnectedStates(nfa.getInitialState()));
        result.setInitialState(initial);
        result.addState(initial);

        HashSet<MultiState> visitedStates = new HashSet<>();

        MultiState trapState = new MultiState();
        for (char c1 : alphabet) {
            trapState.addTransition(new TransitionFunction(trapState, String.valueOf(c1), trapState));
        }
        boolean hasTrap = false;

        while(visitedStates.size() != result.getStates().size()) {
            HashSet<MultiState> statesToAdd = new HashSet<>();

            for (MultiState state : result.getStates()) {
                HashSet<Character> stateAlphabet = new HashSet<>();

                if (visitedStates.contains(state)) continue;
                visitedStates.add(state);
                HashSet<HashSet<TransitionFunction>> sameConditionTransitions = MultiState.getSameConditionTransitions(state);

                Loop1:
                for (HashSet<TransitionFunction> transitionSet : sameConditionTransitions) {
                    MultiState nextState = new MultiState();
                    String condition = null;
                    for (TransitionFunction transition : transitionSet) {
                        condition = transition.getCondition();
                        if (transition.getCondition().equals("lambda")) continue Loop1;
                        nextState.addStates(getLambdaConnectedStates(transition.getNextState()));
                    }

                    for (MultiState mState : result.getStates()) {
                        if (mState.equals(nextState)) {
                            nextState = mState;
                            break;
                        }
                    }
                    if (!result.getStates().contains(nextState)) {
                        statesToAdd.add(nextState);
                    }

                    state.addTransition(new TransitionFunction(state, condition, nextState));
                    if (condition != null) stateAlphabet.add(condition.charAt(0));
                }

                for (char c : alphabet) {
                    if (!stateAlphabet.contains(c)) {
                        hasTrap = true;
                        state.addTransition(new TransitionFunction(state, String.valueOf(c), trapState));
                    }
                }
            }
            result.getStates().addAll(statesToAdd);
        }

        for (MultiState multiState: result.getStates()){
            if (multiState.getStates().contains(nfa.getFinalState())) result.finalStates.add(multiState);
        }

        if (hasTrap) result.addState(trapState);

        return result;
    }
    public static HashSet<State> getLambdaConnectedStates(State state){

        HashSet<State> visitedStates = new HashSet<>();
        return new HashSet<>(getLambdaConnectedStates(state, visitedStates));

    }
    private static HashSet<State> getLambdaConnectedStates(State current, HashSet<State> visitedStates){

        HashSet<State> result = new HashSet<>();
        result.add(current);
        visitedStates.add(current);
        for(TransitionFunction transition: current.getTransitions()){
            if (transition.getCondition().equals("lambda") && !visitedStates.contains(transition.getNextState())){
                result.addAll(getLambdaConnectedStates(transition.getNextState(), visitedStates));
            }
        }
        return result;

    }
    public void addState(MultiState state){
        states.add(state);
    }

    public void print(){
        ArrayList<MultiState> statesArray = new ArrayList<>(getStates());

        if (statesArray.indexOf(getInitialState()) != 0){
            statesArray.remove(getInitialState());
            statesArray.add(statesArray.get(0));
            statesArray.set(0, getInitialState());
        }

        for (int i = 0; i < statesArray.size(); i++){

            for (TransitionFunction transition: statesArray.get(i).getTransitions()){

                System.out.println("(" + statesArray.indexOf(transition.getCurrentMultiState()) +
                        "," + transition.getCondition() +
                        "," + statesArray.indexOf(transition.getNextMultiState()) + ")");
            }
        }

        System.out.println(statesArray.indexOf(getInitialState()));
        for (MultiState finalState: getFinalStates()){
            System.out.print(statesArray.indexOf(finalState) + ",");
        }
    }

    public void addFinalState(MultiState state){
        finalStates.add(state);
    }

    public HashSet<MultiState> getStates() {
        return states;
    }

    public void setInitialState(MultiState initial){
        initialState = initial;
    }

    public MultiState getInitialState() {
        return initialState;
    }

    public HashSet<MultiState> getFinalStates() {
        return finalStates;
    }
}
