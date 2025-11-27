package config.adapter.struts.action;

import java.util.ArrayList;
import java.util.List;

// TODO: STV not yet implement
public class ActionErrors {
    private final List<ActionMessage> messages = new ArrayList<>();

    public void add(String property, ActionMessage msg) {
        messages.add(msg);
    }

    public List<ActionMessage> getAll() {
        return messages;
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public void clear() {
        messages.clear();
    }
}
