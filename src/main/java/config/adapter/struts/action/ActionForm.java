package config.adapter.struts.action;

import jakarta.servlet.ServletRequest;

public abstract class ActionForm {
    public void reset() {
    }

    public void reset(ActionMapping mapping, ServletRequest request) {
    }

    public void validate() throws Exception {
    }
}
