import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.max;

public class MachineRunner {


    private int maxTransitionCount = 0;

    private class MachineState {
        State currentState;
        int position;
        int transitionCount;
        Map<Integer, Character> tape = new HashMap<>();

        MachineState(State startState, String tape) {
            this.currentState = startState;
            this.position = 0;
            this.transitionCount = 0;

            for(int i = 0; i < tape.length(); ++i) {
                this.tape.put(i, tape.charAt(i));
            }
        }

        MachineState(MachineState state) {
            this.currentState = state.currentState;
            this.position = state.position;
            this.transitionCount = state.transitionCount;
            for(Map.Entry<Integer, Character> entry: state.tape.entrySet()) {
                tape.put(entry.getKey(), entry.getValue());
            }
        }

        private void setCurrentSymbol(Character rewriteSymbol) {
            tape.put(position, rewriteSymbol);
        }

        private Character getCurrentSymbol() {
            return tape.getOrDefault(position, '_');
        }

        private void apply(Transition trans) {
            setCurrentSymbol(trans.getRewriteSymbol());
            moveTape(trans.getMove());
            currentState = trans.getNextState();
            this.transitionCount++;
        }

        private List<MachineState> step() {
            Character curr = getCurrentSymbol();

            List<MachineState> nextStates = new LinkedList<>();

            List<Transition> transs = currentState.getTransitionsFor(curr);

            if(transs.isEmpty()) {
                maxTransitionCount = max(this.transitionCount, maxTransitionCount);
            }

            if(transs.size() == 1) {
                this.apply(transs.get(0));
                nextStates.add(this);
            }
            else {
                for (Transition trans : transs) {
                    MachineState fork = new MachineState(this);
                    fork.apply(trans);
                    nextStates.add(fork);
                }
            }

            return nextStates;
        }

        private void moveTape(Move move) {
            switch(move) {
                case Left: position -= 1; break;
                case Right: position += 1; break;
                case Stay: break;
            }
        }

        public boolean isAccepting() {
            return this.currentState.isAccepting();
        }

        private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
        private static final String BLACK_BOLD = "\033[1;30m";
        private static final String ANSI_RESET = "\u001B[0m";

        private void printState() {
            System.out.println("State: " + currentState.getName());
            if(currentState.getName().equals("start")) {
                System.out.println("Trans count:" + getTransitionCount());
            }

            LinkedList<Integer> positions = new LinkedList<>(tape.keySet());
            positions.sort(Integer::compareTo);

            int startPos = Math.min(position, positions.getFirst());
            int endPos = Math.max(positions.getLast(), position);

            System.out.print("╔═");
            for(int i = startPos; i < endPos; ++i) {
                System.out.print("╦═");
            }
            System.out.println("╗");

            for(int i = startPos; i <= endPos; ++i) {
                System.out.print("║");
                if(i == position) {
                    System.out.print(ANSI_PURPLE_BACKGROUND + BLACK_BOLD);
                    System.out.print(tape.getOrDefault(i,'_'));
                    System.out.print(ANSI_RESET);
                }
                else {
                    System.out.print(tape.getOrDefault(i,'_'));
                }
            }
            System.out.println("║");

            System.out.print("╚═");
            for(int i = startPos; i < endPos; ++i) {
                System.out.print("╩═");
            }
            System.out.println("╝");
            System.out.println();
        }

        public int getTransitionCount() {
            return transitionCount;
        }
    }

    List<MachineState> quantumStates = new LinkedList<>();

    private final boolean printTransitions;

    MachineRunner(State startState, String tapeInput) {
        this(startState, tapeInput, false);
    }

    MachineRunner(State startState, String tapeInput, boolean printTransitions) {
        this.printTransitions = printTransitions;

        quantumStates.add(new MachineState(startState, tapeInput));

    }

    private List<MachineState> step() {

        return quantumStates.stream()
                .map(MachineState::step)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    boolean hasAcceptingState() {
        for(MachineState state:quantumStates) {
            if(state.isAccepting()) return true;
        }
        return false;
    }

     MachineState run() {
        while(!hasAcceptingState()) {
            this.quantumStates = step();
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

    int getTransitionCount() {
        return quantumStates.stream().map(MachineState::getTransitionCount).max(Integer::compareTo).orElse(maxTransitionCount);
    }


}
