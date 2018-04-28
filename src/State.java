import java.util.HashMap;
import java.util.Map;

public class State {

    private final String name;
    private final boolean accepting;
    private final Map<Character, Transition> transitions = new HashMap<>();

    State(String name, boolean accepting) {
        this.name = name;
        this.accepting = accepting;
    }

    public void addTransition(Character symbol, Transition transition) {
        transitions.put(symbol, transition);
    }

    public String getName() {
        return name;
    }

    public Transition getTransition(Character curr) {
        return transitions.get(curr);
    }

    public boolean isAccepting() {
        return this.accepting;
    }
}
