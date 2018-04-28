import info.leadinglight.jdot.Edge;
import info.leadinglight.jdot.Graph;
import info.leadinglight.jdot.Node;
import info.leadinglight.jdot.enums.Color;
import info.leadinglight.jdot.enums.Rankdir;
import info.leadinglight.jdot.enums.Style;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void showHelp() {
        System.out.println("Usage: ");
        System.out.println("runtm <desription-file> <input>");
    }


    private static Node createStateNode(String name, boolean start, boolean accepting) {
        Node node = new Node(name);
        if(start) {
            node.setPeripheries(2);
        }
        if(accepting) {
            node.setStyle(Style.Node.filled).setFillColor(Color.SVG.lightgray);
        }
        return node;
    }

    public static State readMachine(String path) throws IOException {

        Scanner descriptionScanner = new Scanner(new FileInputStream(path));

        descriptionScanner.next("states");
        int noStates = descriptionScanner.nextInt();
        Map<String,State> states = new HashMap<>();
        State startState = null;

        for(int i = 0; i < noStates; ++i) {
            String stateName = descriptionScanner.next();
            boolean accepting = false;
            if(descriptionScanner.hasNext("\\+")) {
                descriptionScanner.next("\\+");
                accepting = true;
            }
            State state = new State(stateName, accepting);
            states.put(stateName, state);

            if(startState == null) {
                startState = state;
            }
        }

        descriptionScanner.next("alphabet");
        int alphabetSize = descriptionScanner.nextInt();

        List<String> alphabet = new LinkedList<>();

        for(int i = 0; i < alphabetSize; ++i) {
            String letter = descriptionScanner.next("[^\\s]");
            assert letter.length() == 1;
            alphabet.add(letter);
        }

        String stateRegex = states.values().stream().map(State::getName).map(Pattern::quote).collect(Collectors.joining("|"));
        String alphabetRegex = alphabet.stream().map(Pattern::quote).collect(Collectors.joining());
        alphabetRegex = "[" + alphabetRegex + "_]";
        stateRegex = "(" +stateRegex + ")";

        String moveRegex = "[RSL]";

        while(descriptionScanner.hasNext()) {
            String currentStateSym;
            if(descriptionScanner.hasNext(stateRegex)) {
                currentStateSym = descriptionScanner.next(stateRegex);
            }
            else {
                throw new IOException("Unknown state: " + descriptionScanner.next());
            }
            String currentSymbol = descriptionScanner.next(alphabetRegex);
            String nextStateSym = descriptionScanner.next(stateRegex);
            String rewriteSymbol = descriptionScanner.next(alphabetRegex);
            String moveSym = descriptionScanner.next(moveRegex);

            State currentState = states.get(currentStateSym);
            if (currentState == null) {
                throw new IOException("Unknown state: " + currentStateSym);
            }

            State nextState = states.get(nextStateSym);
            if (nextState == null) {
                throw new IOException("Unknown state: " + nextStateSym);
            }

            Move move = Move.fromSymbol(moveSym);

            currentState.addTransition(currentSymbol.charAt(0), new Transition(nextState, rewriteSymbol.charAt(0), move));
        }

        return startState;
    }

    private static Edge createStateTransitionEdge(State currentState, State nextState, Character on, Character rewrite, String move) {
        Edge edge = new Edge();
        edge.addNode(currentState.getName());
        edge.addNode(nextState.getName());
        edge.setLabel("" + on +"->" + rewrite +"\\n" + move);
        return edge;
    }

    public static void main(String[] args) throws IOException {
        if(args.length != 2) {
            showHelp();
            return;
        }

        String descriptionPath = args[0];
        String inputPath = args[1];

        State startState = readMachine(descriptionPath);

        Scanner inputScanner = new Scanner(new FileInputStream(inputPath));
        String input = inputScanner.nextLine();

        MachineRunner runner = new MachineRunner(startState, input, true);

        System.out.println(runner.run());
        System.out.println(runner.getTransitionCount());
    }
}
