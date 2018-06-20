package tm.runner;

import tm.description.State;

import java.util.*;
import java.util.stream.Collectors;

public class MachineRunner {

    private int transitionCount = 0;

    private List<MachineState> quantumStates = new LinkedList<>();

    private final boolean printTransitions;

    public MachineRunner(State startState, String tapeInput) {
        this(startState, tapeInput, false);
    }

    public MachineRunner(State startState, String tapeInput, boolean printTransitions) {
        this.printTransitions = printTransitions;

        quantumStates.add(new MachineState(startState, tapeInput));
    }

    private List<MachineState> step() {
        System.out.println("Current machines: ");
        if(this.printTransitions) {
            int idx = 1;
            for (MachineState state : quantumStates) {
                System.out.println("Machine " + idx);
                state.printState();
                idx++;
            }
            System.out.println();
        }

        return quantumStates.stream()
                .map(MachineState::step)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private boolean hasAcceptingState() {
        for(MachineState state:quantumStates) {
            if(state.isAccepting()) return true;
        }
        return false;
    }

     public MachineState run() {
        this.transitionCount = 0;
        while(!hasAcceptingState()) {
            this.quantumStates = step();
            this.transitionCount += 1;
            if(this.quantumStates.isEmpty()) {
                return null;
            }
        }
        return getAcceptingMachine();
    }

    private MachineState getAcceptingMachine() {
        for(MachineState state:quantumStates) {
            if(state.isAccepting()) return state;
        }
        return null;
    }

    public int getTransitionCount() {
        return transitionCount;
    }

}
