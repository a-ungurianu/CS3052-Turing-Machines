import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MachineRunner {

    private int position;
    private int transitionCount = 0;
    private State state;
    Map<Integer, Character> tape = new HashMap<>();

    MachineRunner(State startState, String tapeInput) {
        this.state = startState;
        this.position = 0;

        for(int i = 0; i < tapeInput.length(); ++i) {
            tape.put(i, tapeInput.charAt(i));
        }
    }

    private void moveTape(Move move) {
        switch(move) {
            case Left: position -= 1; break;
            case Right: position += 1; break;
            case Stay: break;
        }
    }

    private void setCurrentSymbol(Character rewriteSymbol) {
        tape.put(position, rewriteSymbol);
    }

    private Character getCurrentSymbol() {
        return tape.getOrDefault(position, '_');
    }

    private boolean step() {
        Character curr = getCurrentSymbol();
        Transition trans = state.getTransition(curr);

        if(trans == null) return false;

        setCurrentSymbol(trans.getRewriteSymbol());
        moveTape(trans.getMove());
        state = trans.getNextState();
        this.transitionCount++;

        return true;
    }

     boolean run() {
        while(!state.isAccepting()) {
            if(!step()) {
                return false;
            }
//            printState();
        }
        return true;
    }


    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    private static final String BLACK_BOLD = "\033[1;30m";
    private static final String ANSI_RESET = "\u001B[0m";

    private void printState() {
        System.out.println("State: " + state.getName());

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
