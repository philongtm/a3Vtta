package config.filter;

import common.AppContext;
import common.db.PooledConnection;
import common.global.GS;
import common.util.Function;
import common.util.Log;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;

/**
 * Convert from file AppActionServlet.
 * ref:
 * 拡張アクションサーブレットクラスをFilterで代替
 * 全てのSpring MVCアプリケーションのバックボーン
 */
public class AppActionFilter implements Filter {

    /**
     * アプリケーションの最初の処理
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String[] part = Function.StrSplitToken(System.getProperty("user.dir"), File.separator);
        PooledConnection.setRootDir(part[part.length - 1]);
        Log.open(new File(System.getProperty("user.dir"), GS.LOGDIR).getAbsolutePath());
    }

    /**
     * GET / POSTリクエストの最初の処理
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // セッション毎の排他処理
        synchronized (request.getSession()) {
            // ＡＰＰコンテキスト作成
            AppContext appContext = (AppContext) request.getAttribute(GS.APPCONTEXT);
            if (appContext == null) {
                appContext = new AppContext(request.getServletContext(), request, response);
                request.setAttribute(GS.APPCONTEXT, appContext);
            }

            // ログ出力
            appContext.getLog().write(GS.LOG_INF, getClass().getName(),
                    "CharacterEncoding=" + request.getCharacterEncoding() +
                            " ContentLength=" + request.getContentLength() +
                            " ContentType=" + request.getContentType()
            );

            String method = request.getMethod();
            if ("GET".equalsIgnoreCase(method)) {
                appContext.getLog().write(GS.LOG_INF, getClass().getName(), "<<doGet 1>>" + request.getRequestURI());
                chain.doFilter(req, res); // delegate to next filter / controller
                appContext.getLog().write(GS.LOG_INF, getClass().getName(), "<<doGet 2>>" + request.getRequestURI());
            } else if ("POST".equalsIgnoreCase(method)) {
                appContext.getLog().write(GS.LOG_INF, getClass().getName(), "<<doPost 1>>" + request.getRequestURI());
                chain.doFilter(req, res); // delegate to next filter / controller
                appContext.getLog().write(GS.LOG_INF, getClass().getName(), "<<doPost 2>>" + request.getRequestURI());
            } else {
                chain.doFilter(req, res); // その他のHTTPメソッド
            }

            // ＡＰＰコンテキスト破棄
            appContext.destroy();
            request.removeAttribute(GS.APPCONTEXT);
            appContext = null;
        }
    }

    /**
     * アプリケーションの最後の処理
     */
    @Override
    public void destroy() {
        Log.close();
    }
}
