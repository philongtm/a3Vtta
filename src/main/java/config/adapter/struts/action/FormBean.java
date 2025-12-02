package config.adapter.struts.action;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FormBean {
    private String name;
    private String type;
    private String dynamic = "false";
}
