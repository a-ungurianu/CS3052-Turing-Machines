import java.util.*;

public class State {

    private final String name;
    private final boolean accepting;
    private final Map<Character, List<Transition>> transitions = new HashMap<>();

    State(String name, boolean accepting) {
        this.name = name;
        this.accepting = accepting;
    }

    public void addTransition(Character symbol, Transition transition) {
        transitions.putIfAbsent(symbol, new LinkedList<>());
        transitions.get(symbol).add(transition);
    }

    public String getName() {
        return name;
    }

    public List<Transition> getTransitionsFor(Character curr) {
        return transitions.getOrDefault(curr, Collections.emptyList());
    }

    public boolean isAccepting() {
        return this.accepting;
    }
}
