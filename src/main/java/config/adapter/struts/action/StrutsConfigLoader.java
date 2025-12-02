package config.adapter.struts.action;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class StrutsConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(StrutsConfigLoader.class);

    @Value("${struts.config.path:WEB-INF/struts-config}")
    private String configPath;

    @Value("${struts.extensionMapping:.do}")
    private String extensionMapping;

    private final Map<String, ActionMapping> actionMappings = new HashMap<>();
    private final Map<String, ActionForward> globalForwards = new HashMap<>();
    private final Map<String, FormBean> formBeans = new HashMap<>();

    private final ServletContext servletContext;

    public StrutsConfigLoader(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @PostConstruct
    public void load() throws Exception {
// TODO: xem l?i c?u h?nh config path
        if (configPath.startsWith("classpath:")) {
            String path = configPath.substring("classpath:".length());
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:" + path + "/*.xml");

            for (Resource r : resources) {
                try (InputStream in = r.getInputStream()) {
                    loadConfig(in);
                }
            }
        } else {
            String realPath = servletContext.getRealPath(configPath);
            File folder = new File(realPath);
            if (!folder.exists() || !folder.isDirectory()) {
                throw new RuntimeException("Struts config path not found: " + realPath);
            }
            File[] xmlFiles = folder.listFiles((dir, name) -> name.endsWith(".xml"));
            if (xmlFiles != null) {
                for (File f : xmlFiles) {
                    loadConfig(f);
                }
            }
        }

        globalForwards.values().forEach(forward ->
                actionMappings.values().forEach(mapping -> mapping.addForward(forward))
        );

        logger.info("Loaded Struts configs from {}, total mappings: {}", configPath, actionMappings.size());
    }

    public String getActionPath(String path) {
        return Optional.ofNullable(path)
                .filter(p -> extensionMapping != null && p.endsWith(extensionMapping))
                .map(p -> p.substring(0, p.length() - extensionMapping.length()))
                .orElse(path);
    }

    public String getActionPath(HttpServletRequest request) {
        String uri = request.getRequestURI();        // /app/user/list.do
        String ctx = request.getContextPath();       // /app
        return getActionPath(uri.substring(ctx.length())); // /user/list
    }

    public ActionMapping getActionMapping(HttpServletRequest request) {
        return actionMappings.get(getActionPath(request));
    }

    public FormBean getFormBean(String beanName) {
        return formBeans.get(beanName);
    }

    private void loadConfig(File xmlFile) throws Exception {
        try (InputStream in = new java.io.FileInputStream(xmlFile)) {
            loadConfig(in);
        }
    }

    private void loadConfig(InputStream in) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        org.w3c.dom.Document doc = db.parse(in);

        // Load global-forwards
        var globalForwardsNodes = doc.getElementsByTagName("global-forwards");
        for (int i = 0; i < globalForwardsNodes.getLength(); i++) {
            org.w3c.dom.Element gfContainer = (org.w3c.dom.Element) globalForwardsNodes.item(i);
            var forwardNodes = gfContainer.getElementsByTagName("forward");
            for (int j = 0; j < forwardNodes.getLength(); j++) {
                org.w3c.dom.Element f = (org.w3c.dom.Element) forwardNodes.item(j);
                String name = f.getAttribute("name");
                String path = f.getAttribute("path");
                globalForwards.put(name, new ActionForward(name, path));
            }
        }

        // Load actions
        var actionNodes = doc.getElementsByTagName("action");
        for (int i = 0; i < actionNodes.getLength(); i++) {
            org.w3c.dom.Element a = (org.w3c.dom.Element) actionNodes.item(i);
            ActionMapping mapping = new ActionMapping();
            mapping.setPath(a.getAttribute("path"));
            mapping.setType(a.getAttribute("type"));
            mapping.setName(a.getAttribute("name"));
            mapping.setScope(a.getAttribute("scope"));

            var forwardNodes = a.getElementsByTagName("forward");
            for (int j = 0; j < forwardNodes.getLength(); j++) {
                org.w3c.dom.Element f = (org.w3c.dom.Element) forwardNodes.item(j);
                mapping.addForward(new ActionForward(f.getAttribute("name"), f.getAttribute("path")));
            }

            actionMappings.put(mapping.getPath(), mapping);
        }

        // Load form-beans
        var formBeansNodes = doc.getElementsByTagName("form-beans");
        for (int i = 0; i < formBeansNodes.getLength(); i++) {
            org.w3c.dom.Element fbContainer = (org.w3c.dom.Element) formBeansNodes.item(i);
            var fbNodes = fbContainer.getElementsByTagName("form-bean");
            for (int j = 0; j < fbNodes.getLength(); j++) {
                org.w3c.dom.Element f = (org.w3c.dom.Element) fbNodes.item(j);
                String name = f.getAttribute("name");
                String type = f.getAttribute("type");

                FormBean bean = new FormBean();
                bean.setName(name);
                bean.setType(type);

                formBeans.put(name, bean);
            }
        }

    }
}

