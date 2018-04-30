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

            String nextStateSym;
            if(descriptionScanner.hasNext(stateRegex)) {
                nextStateSym  = descriptionScanner.next(stateRegex);
            }
            else {
                throw new IOException("Unknown state: " + descriptionScanner.next());
            }
            String rewriteSymbol = descriptionScanner.next(alphabetRegex);
            String moveSym = descriptionScanner.next(moveRegex);

            State currentState = states.get(currentStateSym);

            State nextState = states.get(nextStateSym);

            Move move = Move.fromSymbol(moveSym);

            currentState.addTransition(currentSymbol.charAt(0), new Transition(nextState, rewriteSymbol.charAt(0), move));
        }

        return startState;
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
