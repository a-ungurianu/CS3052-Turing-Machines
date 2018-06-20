package tm.description;

import tm.description.Move;
import tm.description.State;

public class Transition {
    private final State nextState;
    private final Character rewriteSymbol;
    private final Move move;

    public Transition(State nextState, Character rewriteSymbol, Move move) {
        this.nextState = nextState;
        this.rewriteSymbol = rewriteSymbol;
        this.move = move;
    }

    public Character getRewriteSymbol() {
        return this.rewriteSymbol;
    }

    public Move getMove() {
        return this.move;
    }

    public State getNextState() {
        return this.nextState;
    }
}
