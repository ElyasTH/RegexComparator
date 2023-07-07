package Utils;

import DataStructures.State;

import java.util.Objects;

public class StatePair {

    private State state1, state2;

    public StatePair(State state1, State state2) {
        this.state1 = state1;
        this.state2 = state2;
    }

    public State getMatch(State state){
        if (state == state1) return state2;
        else if (state == state2) return state1;
        else return null;
    }

    public boolean contains(State state){
        return state1 == state || state2 == state;
    }

    public State getState1() {
        return state1;
    }

    public void setState1(State state1) {
        this.state1 = state1;
    }

    public State getState2() {
        return state2;
    }

    public void setState2(State state2) {
        this.state2 = state2;
    }

    public boolean equals(StatePair other){
        return this.contains(other.state1) && this.contains(other.state2);
    }
}
