package codigo_intermedio;

import java.util.HashMap;
import java.util.Map;

public class VariableContext {
    private final Map<String, Double> variables = new HashMap<>();

    public void set(String name, Double value) {
        variables.put(name, value);
    }

    public Double get(String name) {
        return variables.get(name);
    }

    public boolean contains(String name) {
        return variables.containsKey(name);
    }

    public Map<String, Double> getAll() {
        return variables;
    }
}