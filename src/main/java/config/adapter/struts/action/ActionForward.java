package config.adapter.struts.action;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ActionForward {
    private final String name;
    private final String path;
    private final boolean redirect;

    public ActionForward(String path) {
        this(null, path, false);
    }

    public ActionForward(String name, String path) {
        this(name, path, false);
    }

    public ActionForward(String name, String path, boolean redirect) {
        this.name = name;
        this.path = path;
        this.redirect = redirect;
    }

    public String view() {
        return redirect ? "redirect:" + path : path;
    }
}
