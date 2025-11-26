package config.interceptor;

import common.AppContext;
import common.db.PooledConnection;
import common.global.GS;
import common.util.Function;
import common.util.Log;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.File;
import java.util.Enumeration;

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

    /**
     * preHandleはAction.execute()の前処理に相当
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
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
}
