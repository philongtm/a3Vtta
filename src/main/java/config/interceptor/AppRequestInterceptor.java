package config.interceptor;

import common.AppContext;
import common.db.PooledConnection;
import common.global.GS;
import common.util.Function;
import common.util.Log;
import config.adapter.struts.action.ActionForm;
import config.adapter.struts.action.ActionMapping;
import config.adapter.struts.action.FormBean;
import config.adapter.struts.action.StrutsConfigLoader;
import config.adapter.struts.upload.FormFile;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Convert from file AppRequestProcessor.
 * ref:
 * 拡張RequestProcessorクラス
 * サーブレットリクエストに対するstrutsコントローラが実行するロジック。
 * SessionBean関係の処理をここで行う。
 */
@Component
public class AppRequestInterceptor implements HandlerInterceptor {

    private final String PASSWORD = "password";
    private final String CLASSNAME = this.getClass().getName();
    private final Log log = new Log();
    private final StrutsConfigLoader configLoader;

    /**
     * Constructor
     *
     * @param configLoader StrutsConfigLoader
     */
    public AppRequestInterceptor(StrutsConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    /**
     * preHandleはAction.execute()の前処理に相当
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        preProcessingRequest(request, response, handler);

        synchronized (request.getSession()) {
            AppContext appContext = (AppContext) request.getAttribute(GS.APPCONTEXT);
            if (appContext == null) {
                appContext = new AppContext(null, request, response); // ServletContext null or inject if needed
                request.setAttribute(GS.APPCONTEXT, appContext);
            }

            appContext.getLog().write(GS.LOG_INF, this.getClass().getName(),
                    "CharacterEncoding=" + request.getCharacterEncoding() +
                            " ContentLength=" + request.getContentLength() +
                            " ContentType=" + request.getContentType()
            );

            // doGet / doPost logging
            String method = request.getMethod();
            if ("GET".equalsIgnoreCase(method)) {
                appContext.getLog().write(GS.LOG_INF, this.getClass().getName(), "<<doGet>>" + request.getRequestURI());
            } else if ("POST".equalsIgnoreCase(method)) {
                appContext.getLog().write(GS.LOG_INF, this.getClass().getName(), "<<doPost>>" + request.getRequestURI());
            }

            // Request completion cleanup (after completion)
        }

        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String param = params.nextElement();
            if (PASSWORD.equals(param)) {
                continue; // パスワードは出力しない
            }
            String value = request.getParameter(param);
            log.write(GS.LOG_DBG, CLASSNAME, "param : " + param + " = \"" + value + "\"");
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                System.out.println("cookie : " + c.getName() + " = " + c.getValue());
            }
        }

        return true; // true=処理続行, false=処理中止
    }

    @PostConstruct
    public void init() {
        // init logic Struts style
        String[] part = Function.StrSplitToken(System.getProperty("user.dir"), File.separator);
        PooledConnection.setRootDir(part[part.length - 1]);
        Log.open(System.getProperty("user.dir") + File.separator + GS.LOGDIR);
    }

    @PreDestroy
    public void destroy() {
        Log.close();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AppContext appContext = (AppContext) request.getAttribute(GS.APPCONTEXT);
        if (appContext != null) {
            appContext.destroy();
            request.removeAttribute(GS.APPCONTEXT);
        }
    }

    /**
     * Handles pre-processing of requests: prepares form, populates data, sets locale, validates, and triggers business logic.
     */
    private void preProcessingRequest(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // 1. get config action mapping from struts config
        ActionMapping actionMapping = configLoader.getActionMapping(request);
        if (actionMapping != null) {
            request.setAttribute(GS.REQUEST_ATTRIBUTE_STRUST_ACTION_MAPPING, actionMapping);

            // 2. prepare form bean
            boolean isSessionScope = "session".equalsIgnoreCase(actionMapping.getScope());
            FormBean formBean = configLoader.getFormBean(actionMapping.getName());
            ActionForm actionForm = (ActionForm) (isSessionScope
                    ? request.getSession().getAttribute(actionMapping.getName())
                    : null);
            if (isSessionScope && actionForm == null) {
                actionForm = (ActionForm) createInstance(formBean);
                request.getSession().setAttribute(actionMapping.getName(), actionForm);
            } else if (!isSessionScope) {
                actionForm = (ActionForm) createInstance(formBean);
            }

            if (actionForm != null) {
                actionForm.reset();

                boolean isMultipart = request.getContentType() != null &&
                        request.getContentType().startsWith("multipart/form-data");
                Map<String, Object> multipartParameters = new HashMap<>();
                Map<String, Object> properties = new HashMap<>();
                Enumeration<String> names;

                if (isMultipart) {
                    MultipartHttpServletRequest multiReq = (MultipartHttpServletRequest) request;

                    // get file upload
                    for (Iterator<String> it = multiReq.getFileNames(); it.hasNext(); ) {
                        String name = it.next();
                        MultipartFile file = multiReq.getFile(name);
                        if (file != null && !file.isEmpty()) {
                            multipartParameters.put(name, new FormFile(file));
                        }
                    }

                    // get text field
                    multipartParameters.putAll(multiReq.getParameterMap());
                    names = Collections.enumeration(multipartParameters.keySet());
                } else {
                    names = request.getParameterNames();
                }

                while (names.hasMoreElements()) {
                    String name = names.nextElement();
                    Object parameterValue = isMultipart ? multipartParameters.get(name) : request.getParameterValues(name);

                    // Ignore system variables
                    if (name.startsWith("org.springframework.")) {
                        continue;
                    }

                    properties.put(name, parameterValue);
                }

                // Set the corresponding properties of our bean
                BeanUtils.populate(actionForm, properties);

                try {
                    processValidate(request, response, actionForm);
                } catch (IOException e) {
                    throw new ServletException(e);
                }

                request.setAttribute(GS.REQUEST_ATTRIBUTE_STRUST_FORM_BEAN, actionForm);
            }
        }
    }

    private Object createInstance(FormBean info) throws Exception {
        if (info == null || info.getType() == null) {
            return null;
        }

        Class<?> clazz = Class.forName(info.getType());

        Constructor<?> ctor = clazz.getDeclaredConstructor();
        ctor.setAccessible(true);

        return ctor.newInstance();
    }

    /**
     * 単項目チェックはformをvalueに展開する前（populate前に行う）
     */
    private boolean processValidate(HttpServletRequest request, HttpServletResponse response, ActionForm form)
            throws IOException {
        return true;
    }
}
