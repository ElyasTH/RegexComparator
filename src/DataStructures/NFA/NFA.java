package DataStructures.NFA;

import java.util.*;

public class NFA {

    private State initialState;
    private State finalState;
    private final HashSet<State> states = new HashSet<>();

    public void addState(State newState){
        this.states.add(newState);
    }

    public void removeState(State stateToRemove){
        states.remove(stateToRemove);
        if (initialState == stateToRemove){
            initialState = null;
        }
        if (finalState == stateToRemove){
            finalState = null;
        }
    }

    public static NFA fromRegex(String regex){
        Stack<Character> operatorStack = new Stack<>();
        Stack<NFA> operandStack = new Stack<>();
        Stack<NFA> concatStack = new Stack<>();

        boolean concatFlag = false;
        char op;
        int parenthesisCount = 0;
        NFA nfa1, nfa2;

        for (char c: regex.toCharArray()){

            if (Character.isLetter(c) || Character.isDigit(c)){

                NFA newNFA = new NFA();

                State initialState = new State(), finalState = new State();
                newNFA.addState(initialState);
                newNFA.addState(finalState);
                newNFA.setInitialState(initialState);
                newNFA.setFinalState(finalState);

                initialState.addTransition(c, finalState);

                operandStack.push(newNFA);
                if (concatFlag){ // concat this w/ previous
                    operatorStack.push('.'); // '.' used to represent concat.
                }
                else
                    concatFlag = true;
            }
            else{
                if (c == ')'){
                    concatFlag = false;
                    if (parenthesisCount == 0){
                        System.out.println("Error: More end parenthesis "+
                                "than beginning parenthesis");
                        System.exit(1);
                    }
                    else{ parenthesisCount--;}
                    // process stuff on stack till '('
                    while (!operatorStack.empty() && operatorStack.peek() != '('){
                        op = operatorStack.pop();
                        if (op == '.'){
                            nfa2 = operandStack.pop();
                            nfa1 = operandStack.pop();
                            operandStack.push(concat(nfa1, nfa2));
                        }
                        else if (op == '+'){
                            nfa2 = operandStack.pop();

                            if(!operatorStack.empty() &&
                                    operatorStack.peek() == '.'){

                                concatStack.push(operandStack.pop());
                                while (!operatorStack.empty() &&
                                        operatorStack.peek() == '.'){

                                    concatStack.push(operandStack.pop());
                                    operatorStack.pop();
                                }
                                nfa1 = concat(concatStack.pop(),
                                        concatStack.pop());
                                while (concatStack.size() > 0){
                                    nfa1 = concat(nfa1, concatStack.pop());
                                }
                            }
                            else{
                                nfa1 = operandStack.pop();
                            }
                            operandStack.push(union(nfa1, nfa2));
                        }
                    }
                }
                else if (c == '*'){
                    operandStack.push(kleene(operandStack.pop()));
                    concatFlag = true;
                }
                else if (c == '('){ // if any other operator: push
                    operatorStack.push(c);
                    parenthesisCount++;
                }
                else if (c == '+'){
                    operatorStack.push(c);
                    concatFlag = false;
                }
            }
        }
        while (operatorStack.size() > 0){
            if (operandStack.empty()){
                System.out.println("Error: imbalance in operandStack and "
                        + "operatorStack");
                System.exit(1);
            }
            op = operatorStack.pop();
            if (op == '.'){
                nfa2 = operandStack.pop();
                nfa1 = operandStack.pop();
                operandStack.push(concat(nfa1, nfa2));
            }
            else if (op == '+'){
                nfa2 = operandStack.pop();
                if( !operatorStack.empty() && operatorStack.peek() == '.'){
                    concatStack.push(operandStack.pop());
                    while (!operatorStack.empty() && operatorStack.peek() == '.'){
                        concatStack.push(operandStack.pop());
                        operatorStack.pop();
                    }
                    nfa1 = concat(concatStack.pop(),
                            concatStack.pop());
                    while (concatStack.size() > 0){
                        nfa1 =  concat(nfa1, concatStack.pop());
                    }
                }
                else{
                    nfa1 = operandStack.pop();
                }
                operandStack.push(union(nfa1, nfa2));
            }
        }

        return operandStack.pop();
    }

    public static NFA concat(NFA nfa1, NFA nfa2) {

        for (TransitionFunction transition: nfa2.getInitialState().getTransitions()){
            nfa1.getFinalState().addTransition(transition);
        }

        nfa2.removeState(nfa2.getInitialState());

        for (State state: nfa2.getStates()){
            nfa1.addState(state);
        }

        nfa1.setFinalState(nfa2.getFinalState());

        return nfa1;
    }

    public static NFA union(NFA nfa1, NFA nfa2){

        NFA result = new NFA();

        State initialState = new State(), finalState = new State();
        result.addState(initialState);
        result.addState(finalState);
        result.setFinalState(finalState);
        result.setInitialState(initialState);

        initialState.addTransition('λ', nfa1.initialState);
        initialState.addTransition('λ', nfa2.initialState);
        nfa1.getFinalState().addTransition('λ', finalState);
        nfa2.getFinalState().addTransition('λ', finalState);

        for (State state: nfa1.states){
            result.addState(state);
        }
        for (State state: nfa2.states){
            result.addState(state);
        }

        return result;
    }

    public static NFA kleene(NFA nfa){

        NFA result = new NFA();

        State initialState = new State(), finalState = new State();
        result.addState(initialState);
        result.addState(finalState);
        result.setInitialState(initialState);
        result.setFinalState(finalState);

        result.getInitialState().addTransition('λ', result.getFinalState());
        result.getFinalState().addTransition('λ', result.getInitialState());

        result.getInitialState().addTransition('λ', nfa.getInitialState());
        nfa.getFinalState().addTransition('λ', result.getFinalState());

        for (State state: nfa.getStates()){
            result.addState(state);
        }

        return result;
    }

    public static void print(NFA nfa){

        ArrayList<State> statesArray = new ArrayList<>(nfa.getStates());

        if (statesArray.indexOf(nfa.getInitialState()) != 0){
            statesArray.remove(nfa.getInitialState());
            statesArray.add(statesArray.get(0));
            statesArray.set(0, nfa.getInitialState());
        }

        if (statesArray.indexOf(nfa.getFinalState()) != statesArray.size()-1){
            statesArray.remove(nfa.getFinalState());
            statesArray.add(nfa.getFinalState());
        }

        for (int i = 0; i < statesArray.size(); i++){

            for (TransitionFunction transition: statesArray.get(i).getTransitions()){

                System.out.println("(" + statesArray.indexOf(transition.getCurrentState()) +
                        "," + transition.getCondition() +
                        "," + statesArray.indexOf(transition.getNextState()) + ")");
            }
        }

        System.out.println(statesArray.indexOf(nfa.getFinalState()));
        System.out.println(statesArray.indexOf(nfa.getInitialState()));
    }

    public State getInitialState() {
        return initialState;
    }

    public void setInitialState(State initialState) {
        this.initialState = initialState;
    }

    public void setFinalState(State finalState){
        this.finalState = finalState;
    }

    public State getFinalState() {
        return finalState;
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
