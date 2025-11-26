package common.struts.stv;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

// TODO: STV not yet implement
@Getter
public class ActionMapping {
    private final Map<String, ActionForward> forwards = new HashMap<>();
    @Setter
    private String name;

    public void addForward(String name, String path) {
        forwards.put(name, new ActionForward(path));
    }

    public ActionForward findForward(String name) {
        return forwards.get(name);
    }
}
