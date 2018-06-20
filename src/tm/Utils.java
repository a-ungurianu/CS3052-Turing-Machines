package tm;

import tm.description.Move;
import tm.description.State;
import tm.description.Transition;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {
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
}
