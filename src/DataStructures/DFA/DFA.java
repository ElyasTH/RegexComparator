package DataStructures.DFA;

import DataStructures.NFA.NFA;
import DataStructures.State;
import DataStructures.TransitionFunction;

import java.util.*;

public class DFA {

    private HashSet<MultiState> states = new HashSet<>();
    private MultiState initialState;
    private HashSet<MultiState> finalStates = new HashSet<>();
    private HashSet<State> minimizedStates = new HashSet<>();
    private State minimizedInitialState;
    private HashSet<State> minimizedFinalStates = new HashSet<>();

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

    public void minimize() {

        HashSet<HashSet<MultiState>> minSets = new HashSet<>();
        minSets.add(finalStates);
        HashSet<MultiState> nonFinalStates = new HashSet<>(states);
        nonFinalStates.removeAll(finalStates);
        minSets.add(nonFinalStates);

        boolean repeat = true;
        while(repeat) {
            HashSet<HashSet<MultiState>> newMinSets = new HashSet<>();

            for (HashSet<MultiState> minSet : minSets) {
                stateLoop: for (MultiState state : minSet) {

                    for (HashSet<MultiState> newMinSet: newMinSets){
                        if (newMinSet.contains(state)) continue stateLoop;
                    }

                    HashSet<MultiState> stateIndistSet = new HashSet<>();
                    stateIndistSet.add(state);
                    for (MultiState state2: minSet){
                        if (state == state2) continue;
                        if (checkIndistinguishability(minSets, state, state2))
                            stateIndistSet.add(state2);
                    }
                    newMinSets.add(stateIndistSet);

                }
            }

            if (newMinSets.size() == minSets.size()) {
                repeat = false;
                Loop1:
                for (HashSet<MultiState> newMinSet : newMinSets) {
                    for (HashSet<MultiState> minSet : minSets) {
                        if (newMinSet.containsAll(minSet)) {
                            continue Loop1;
                        }
                    }
                    repeat = true;
                }
            }
            minSets = newMinSets;
        }

        ArrayList<State> newStates = new ArrayList<>();
        ArrayList<HashSet<MultiState>> minSetsArray = new ArrayList<>(minSets);

        HashSet<MultiState> newInitialState = null;
        for (HashSet<MultiState> multiStates: minSetsArray){
            if (multiStates.contains(initialState) && minSetsArray.indexOf(multiStates) != 0){
                newInitialState = multiStates;
            }
        }
        if (newInitialState != null){
            minSetsArray.remove(newInitialState);
            minSetsArray.add(minSetsArray.get(0));
            minSetsArray.set(0, newInitialState);
        }

        for (int i = 0; i < minSetsArray.size(); i++){
            newStates.add(new State(i));
        }

        for (HashSet<MultiState> multiStates: minSetsArray){
            int index = minSetsArray.indexOf(multiStates);
            for (MultiState multiState: multiStates){
                for (TransitionFunction transition: multiState.getTransitions()){
                    for (HashSet<MultiState> multiStates1: minSetsArray){
                        if (multiStates1.contains(transition.getNextMultiState())){
                            newStates.get(index).addTransition(transition.getCondition(), newStates.get(minSetsArray.indexOf(multiStates1)));
                        }
                    }
                }
                break;
            }
        }

        for (HashSet<MultiState> multiStates: minSetsArray){
            for (MultiState multiState: multiStates){
                if (finalStates.contains(multiState)){
                    minimizedFinalStates.add(newStates.get(minSetsArray.indexOf(multiStates)));
                    break;
                }
            }
        }

        outerLoop: for (HashSet<MultiState> multiStates: minSetsArray){
            for (MultiState multiState: multiStates){
                if (initialState == multiState){
                    minimizedInitialState = newStates.get(minSetsArray.indexOf(multiStates));
                    break outerLoop;
                }
            }
        }

        minimizedStates = new HashSet<>();
        minimizedStates.addAll(newStates);

    }

    private boolean checkIndistinguishability(HashSet<HashSet<MultiState>> totalSet, MultiState state1, MultiState state2){

        for (TransitionFunction transition1: state1.getTransitions()){

            HashSet<MultiState> checkSet = null;
            for (HashSet<MultiState> set: totalSet){
                if (set.contains(transition1.getNextMultiState())){
                    checkSet = set;
                    break;
                }
            }

            if (checkSet == null){
                System.out.println("WTF?!");
                return false;
            }
            TransitionFunction transition2 = state2.getTransitionByCondition(transition1.getCondition());
            if (!checkSet.contains(transition2.getNextMultiState())) return false;

        }

        return true;

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

        System.out.println("Final State(s): ");
        for (MultiState finalState: getFinalStates()){
            System.out.print(statesArray.indexOf(finalState) + ",");
        }
        System.out.println();
    }
    public void printMinimized(){

        for (State state: minimizedStates){
            for (TransitionFunction transition: state.getTransitions()){
                System.out.println("(" + transition.getCurrentState().getStateId() +
                        "," + transition.getCondition() +
                        "," + transition.getNextState().getStateId() + ")");
            }
        }
        System.out.print("Final State(s): ");
        for (State state: minimizedFinalStates){
            System.out.print(state.getStateId() + ",");
        }
        System.out.println();

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
