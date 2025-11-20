package ca.uwaterloo.watform.dashtotlaplus;

import java.util.ArrayList;
import java.util.List;

public class Temp {
    static class State {
        boolean isDefault = false;
        String name;

        public State(String name) {
            this.name = name;
        }
    }

    static class OR_State extends State {
        List<State> child_states;

        public OR_State(String name, List<State> child_states) {
            super(name);
            this.child_states = child_states;
        }
    }

    static class AND_State extends State {
        List<State> child_states;

        public AND_State(String name, List<State> child_states) {
            super(name);
            this.child_states = child_states;
        }
    }

    static class Transition {
        String name;
        State from;
        State goTo;

        public Transition(String name, State from, State goTo) {
            this.name = name;
            this.from = from;
            this.goTo = goTo;
        }
    }

    static class Event {
        String name;
        boolean isEnv;

        public Event(String name, boolean isEnv) {
            this.name = name;
            this.isEnv = isEnv;
        }
    }

    List<State> leafStates;
    List<AND_State> ANDStates;
    List<OR_State> ORStates;
    List<Transition> transitions;
    List<Event> events;

    public Temp(
            List<State> leafStates,
            List<OR_State> ORStates,
            List<AND_State> ANDStates,
            List<Transition> transitions,
            List<Event> events) {
        this.leafStates = leafStates;
        this.ANDStates = ANDStates;
        this.ORStates = ORStates;
        this.transitions = transitions;
        this.events = events;
    }

    public Temp() {
        this.leafStates = new ArrayList<>();
        this.ANDStates = new ArrayList<>();
        this.ORStates = new ArrayList<>();
        this.transitions = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public static Temp testOne() {
        Temp model = new Temp();

        State root = new State("root");
        State s1 = new State("s1");
        State s2 = new State("s2");
        Transition t1 = new Transition("t1", s1, s2);

        model.leafStates.add(s1);
        model.leafStates.add(s2);
        model.transitions.add(t1);

        return model;
    }
}
