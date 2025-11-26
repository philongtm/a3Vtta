package common.struts.adapter.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// TODO: STV not yet implement
public abstract class Action {
    public abstract ActionForward execute(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception;
}
