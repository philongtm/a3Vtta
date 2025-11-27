package config;

import common.AppContext;
import common.global.GS;
import common.util.Log;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

/**
 * Convert from file AppExceptionHandler.
 * ref:
 * 拡張RequestProcessorクラス
 * サーブレットリクエストに対するstrutsコントローラが実行するロジック。
 * SessionBean関係の処理をここで行う。
 */
@ControllerAdvice
public class AppExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AppContext app = AppContext.getInstance(request);
        Log appLog = app.getLog();

        String errJsp;
        logger.error(ex.getMessage(), ex);

        if (ex instanceof SQLException) {
            // DB access error
            errJsp = "error2.jsp";
            request.setAttribute("ERRJSP", errJsp);
            appLog.write(GS.LOG_ERR, "", "■ＤＢアクセスエラー発生");
        } else {
            if (app.getSqlExecuter().isError()) {
                // DB access error from SQLExecuter
                errJsp = "error2.jsp";
                request.setAttribute("ERRJSP", errJsp);
                appLog.write(GS.LOG_ERR, "", "■ＤＢアクセスエラー発生");
                Exception dbEx = app.getSqlExecuter().getConnectException();
                appLog.write(GS.LOG_ERR, "", dbEx);

                // forward to error JSP
                ModelAndView mav = new ModelAndView("/WEB-INF/views/" + errJsp);
                mav.addObject("exception", ex);
                return mav;
            }
            // System error
            errJsp = "error1.jsp";
            request.setAttribute("ERRJSP", errJsp);
            appLog.write(GS.LOG_ERR, "", "■サーブレット例外発生");
        }

        // log original exception
        appLog.write(GS.LOG_ERR, "", ex);

        // forward to error JSP
        ModelAndView mav = new ModelAndView("/WEB-INF/views/" + errJsp);
        mav.addObject("exception", ex);
        return mav;
    }
}
