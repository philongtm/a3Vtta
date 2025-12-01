/******************************************************************************
 著作権情報				:
 プロジェクト			: SAMPLE
 使用JDK バージョン		: 1.4.2.05
 更新履歴
 No		日付			修正者			修正内容
 001		2008/01/13		SSC			1.5次版に修正を施し流用
 ******************************************************************************/
package common;

import app.SessionData;
import common.global.GL;
import common.global.GS;
import common.struts.AppLocale;
import common.util.Function;
import common.util.JspMessage;
import common.util.Log;
import common.util.Profile;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * フィルタークラス
 * <pre>
 * リソース(Servlet または静的なコンテンツ)へのリクエストもしくは
 * リソースからのレスポンス、あるいはその両方をフィルタリングするタスクを処理する。
 * </pre>
 *
 * @author NDC
 */
public class AppFilter implements Filter {

    private static final String POST = "post";
    private static final String GET = "get";
    private static final String LOGIN_JSP = "login.jsp";
    private static final String LOGIN_ACTION = "login.do";
    private static final String CLASSNAME = "common.AppFilter";
    private boolean useFilter = false;
    private String encoding = null;
    private final Log log = new Log();

    /**
     * Web コンテナは、Filter をサービス状態にするために init メソッドを呼び出します。 Servlet コンテナは、Filter をインスタンス化したあと、 一度だけ init メソッドを呼び出します。Filter がフィルタリングの仕事を依頼される前に、init メソッドは正常に完了してなければいけません。
     * <pre>
     * init メソッドが以下のような状況になると、Web コンテナは Filter をサービス状態にできません。
     * 1. ServletException をスローした
     * 2. Web コネクタで定義した時間内に戻らない
     * </pre>
     *
     * @param filterConfig Filter 設定のためのオブジェクト
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        String param = filterConfig.getInitParameter("useFilter");
        this.encoding = filterConfig.getInitParameter("encoding");
        if (param.equals("true")) {
            this.useFilter = true;
        }
    }

    /**
     * Filter クラスの doFilter メソッドはコンテナにより呼び出され、
     * 最後のチェーンにおけるリソースへのクライアントリクエストのために、
     * 毎回リクエスト・レスポンスのペアが、チェーンを通して渡されます。
     * このメソッドに渡される FilterChain を利用して、Filter がリクエストやレスポンスを
     * チェーン内の次のエンティティ(Filter)にリクエストとレスポンスを渡す事ができます。
     * <pre>
     * このメソッドの実装。
     * 1. クライアントの文字エンコーディング指定。
     * 2. リクエストの検査。
     * 　(1)要求されたリソースの呼び出しを許可する場合。
     * 　　・リクエストのメソッドがget,post以外
     * 　　・ファイルの拡張子がjsp,do以外
     * 　　　※ファイルの拡張子がxlsの場合
     * 　　　　ファイル名がtmp*.xlsの場合はエクセルのダウンロードアクションを起動する。
     * 　　・エラー画面(ファイル名がerrorで始まる)
     * 　　・ログイン画面(login.jsp,login.do)
     * 　(2)その他はセッションの有効性をチェックし、リソースの呼び出し許可を判定する。
     * </pre>
     *
     * @param request  クライアントのリクエストに含まれている情報を Servlet に提供するオブジェクト。
     * @param response Servlet がクライアントに送り返すレスポンスをラップするオブジェクト。
     * @param chain    Servlet コンテナによって提供され、リソースへのフィルタリングされたリクエストのチェーンに対するビュー。
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        log.write(GS.LOG_INF, CLASSNAME, "start");

        //エンコーディングの設定
        if (useFilter) {
            if (encoding != null) {
                request.setCharacterEncoding(encoding);
            }
        }

        //セッションチェックを行いOKの時にchain.doFilter()を実行する。
        if (sessionCheck(
                (HttpServletRequest) request,
                (HttpServletResponse) response,
                //2022/06/21 Fix bug 16,18 START
                ((HttpServletRequest) request).getSession(true))) {
            //2022/06/21 Fix bug 16,18 END
            chain.doFilter(request, response);
        }

        log.write(GS.LOG_INF, CLASSNAME, "end");
    }

    /* (非 Javadoc)
     * @see jakarta.servlet.Filter#destroy()
     */
    public void destroy() {
        // TODO 自動生成されたメソッド・スタブ
    }

    /**
     * セッションの有効チェック
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param session  HttpSession
     * @return true/有効 false/無効
     */
    private boolean sessionCheck(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException, ServletException {

        String path;
        String method;

        method = request.getMethod().toLowerCase();
        path = request.getRequestURI();

        log.write(GS.LOG_INF, CLASSNAME, method + " " + path);

        if ((!method.equals(GET)) && (!method.equals(POST))) {
            log.write(GS.LOG_DBG, CLASSNAME, "1");
            return true;
        }

        String[] part = Function.StrSplitToken(path, "/");

        if (part.length <= 1) {
            log.write(GS.LOG_DBG, CLASSNAME, "2");
            return true;
        } else {
            path = part[part.length - 1].toLowerCase();
        }

        //EXCELのダウンロード
        //tmpxxxxx.xlsは一時ファイルなので、ダウンロード用アクションクラスを起動する。
        //ダウンロード後に一時ファイルが削除される。
        if (path.endsWith(GS.DOTXLS)) {
            if (path.startsWith(GS.TMP)) {
                //一時ファイル名をURLに追加
                request.getRequestDispatcher("/excel.do?" + GS.NAME + "=" + path).forward(request, response);
                log.write(GS.LOG_DBG, CLASSNAME, "3");
                return false;
            } else {
                //通常
                log.write(GS.LOG_DBG, CLASSNAME, "4");
                return true;
            }
        }

        if (path.endsWith(".jsp") || path.endsWith(".do")) {
            //JSP,ACTION
            if (path.equals("close.jsp")) {
                //クローズ
                return true;
            }
        } else {
            //JSP,ACTION以外
            return true;
        }

        //エラー画面
        if (path.startsWith("error")) {
            log.write(GS.LOG_DBG, CLASSNAME, "6");
            return true;
        }

        //LOGIN画面
        if (path.equals(LOGIN_JSP) || path.equals(LOGIN_ACTION)) {
            //セッション初期化
            if (session == null) {
                log.write(GS.LOG_DBG, CLASSNAME, "7");
                InitSession(request.getSession(true), request);
            } else {
                log.write(GS.LOG_DBG, CLASSNAME, "8");
                if (!ValidSession1(session)) {
                    log.write(GS.LOG_DBG, CLASSNAME, "9");
                    session.invalidate();
                    InitSession(request.getSession(true), request);
                }
            }
            return true;
        }

        //セッションＩＤ：なし
        if ((!request.isRequestedSessionIdFromCookie()) && (!request.isRequestedSessionIdFromURL())) {
            //無効セッション：４０４エラー
            log.write(GS.LOG_INF, CLASSNAME, "無効セッション：" + request.getRequestURI());
            timeout(path, request, response);

            log.write(GS.LOG_DBG, CLASSNAME, "10");
            return false;

            //セッションＩＤ：あり
        } else {
            if (ValidSession2(session)) {
                log.write(GS.LOG_DBG, CLASSNAME, "11");
                return true;
            } else {
                log.write(GS.LOG_DBG, CLASSNAME, "12");
                timeout(path, request, response);
                return false;
            }
        }
    }

    /**
     * セッションの有効チェック１
     * <p>
     * 共通セッションが取得可能の時に有効とする。
     *
     * @param session HttpSession
     * @return true/有効 false/無効
     */
    private boolean ValidSession1(HttpSession session) {
        if (session == null) {
            log.write(GS.LOG_DBG, CLASSNAME, "100");
            return false;
        }
        SessionData appData = (SessionData) session.getAttribute(SessionData.class.getName());
        if (appData == null) {
            log.write(GS.LOG_DBG, CLASSNAME, "101");
            return false;
        } else {
            log.write(GS.LOG_DBG, CLASSNAME, "102");
            return true;
        }
    }

    /**
     * セッションの有効チェック２
     * <p>
     * 共通セッションのユーザＩＤが取得可能の時に有効とする。
     *
     * @param session HttpSession
     * @return true/有効 false/無効
     */
    private boolean ValidSession2(HttpSession session) {
        if (session == null) {
            log.write(GS.LOG_DBG, CLASSNAME, "201");
            return false;
        }
        SessionData appData = (SessionData) session.getAttribute(SessionData.class.getName());
        if (appData == null) {
            log.write(GS.LOG_DBG, CLASSNAME, "202");
            return false;
        }
        String userId = appData.getUser_bean().getComUserId();
        if ((userId == null) || (userId.isEmpty())) {
            log.write(GS.LOG_DBG, CLASSNAME, "203");
            return false;
        } else {
            log.write(GS.LOG_DBG, CLASSNAME, "204");
            return true;
        }
    }

    /**
     * セッションタイムアウト時のエラー画面表示
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    private void timeout(String path, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession ses;
        InitSession(ses = request.getSession(true), request);
        JspMessage i18n = new JspMessage(ses);
        request.getSession(true).setAttribute(GS.MESSAGECONTEXT, i18n.get(GL.SYSTEM_TIMEOUT));

        if (path.indexOf("popupframe.jsp") > 0) {
            //モーダルのフレーム
            response.sendRedirect(GS.WEB_INCLUDE + "error32.jsp");
        } else {
            //モーダルのフレーム以外
            response.sendRedirect(GS.WEB_INCLUDE + "error31.jsp");
        }
    }

    /**
     * セッションの初期化
     *
     * @param session HttpSession
     * @param request HttpServletRequest
     */
    private void InitSession(HttpSession session, HttpServletRequest request) {

        //デフォルトの言語を設定
        AppLocale.setDefault(request);

        //セッションのタイムアウト設定
        session.setMaxInactiveInterval(Profile.getInt(GS.PROFILE_SESSION_TIMEOUT, 86400));

        //セッションデータ作成
        {
            SessionData sesData;
            //ＡＰＰ共通
            sesData = new SessionData();
            sesData.initialize();
            session.setAttribute(sesData.getClass().getName(), sesData);
        }
    }
}