package common.struts.adapter.action;

// TODO: STV not yet implement
public class ActionForward {
    private final String path;
    private final boolean redirect;

    public ActionForward(String path) {
        this(path, false);
    }

    public ActionForward(String path, boolean redirect) {
        this.path = path;
        this.redirect = redirect;
    }

    public String view() {
        return redirect ? "redirect:" + path : path;
    }

    public String getPath() {
        return path;
    }

    public boolean isRedirect() {
        return redirect;
    }
}
