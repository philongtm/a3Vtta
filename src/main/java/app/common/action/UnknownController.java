package app.common.action;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class UnknownController {

    @RequestMapping("/include/*.jsp")
    public String unknown(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
