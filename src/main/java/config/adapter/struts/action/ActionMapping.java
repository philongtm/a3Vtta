package config.adapter.struts.action;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class ActionMapping {
    private String path;
    private String type;   // Action class
    private String name;   // Form bean name
    private String scope;  // request/session
    private Map<String, ActionForward> forwards = new HashMap<>();

    public void addForward(ActionForward forward) {
        forwards.put(forward.getName(), forward);
    }

    public ActionForward findForward(String name) {
        return forwards.get(name);
    }
}
