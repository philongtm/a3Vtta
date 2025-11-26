package common.struts.stv;

// TODO: STV not yet implement
public class ActionMessage {
    private final String key;
    private final Object[] args;

    public ActionMessage(String key, Object... args) {
        this.key = key;
        this.args = args;
    }

    public String getKey() {
        return key;
    }

    public Object[] getArgs() {
        return args;
    }
}
