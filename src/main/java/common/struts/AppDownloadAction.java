/******************************************************************************
 著作権情報				:
 使用JDK バージョン		: 1.4.2.05
 更新履歴
 No		日付			修正者			修正内容
 001		09/05/15		SSC				1.5次版機能組込
 002		09/10/20		SSC				課題No.52 HTMLファイルオープン対応
 003		14/03/17		SSC				案件No.D13493 改善対応
 004		14/05/19		SSC				案件No.D13493 改善対応（ファイル名文字化け対応）
 ******************************************************************************/
package common.struts;

import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;
import common.util.SplitPath;
import common.util.TempFile;
import config.adapter.struts.action.ActionForm;
import config.adapter.struts.action.ActionForward;
import config.adapter.struts.action.ActionMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 拡張アクションクラス
 *
 */
// TODO: how use new instance in db access layer?
public class AppDownloadAction {

    private String CLASSNAME = getClass().getName(); // クラス名

    private HashMap extMap = null;

    private Log log = new Log();

    /**
     * コンストラクタ
     */
    public AppDownloadAction() {
        super();
        if (extMap == null) {
            extMap = new HashMap();
            extMap.put("jpg", "image/jpeg");
            extMap.put("gif", "image/gif");
            //extMap.put("txt", "text/plain");
            // 課題No.52
            // 追加開始
            extMap.put("html", "text/html");
            extMap.put("htm", "text/html");
            extMap.put("xml", "text/xml");
            // 追加完了
            extMap.put("xls", "application/vnd.ms-excel");
            extMap.put("csv", "application/vnd.ms-excel");
        }
    }

    /**
     * カスタムexecute()メソッド
     * 共通の前処理を行い、アプリケーション用のexecute()メソッドを呼び出す。
     *
     * @param mapping  アクションマッピングオブジェクト
     * @param form     アクションフォームのインスタンス
     * @param request  HTTPリクエストオブジェクト
     * @param response HTTPレスポンスオブジェクト
     * @return ActionForwardオブジェクト
     */
    @RequestMapping
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        TempFile tmp = null;
        SplitPath sp = null;
        File file = null;
        FileInputStream is = null;
        byte[] buf = new byte[1024];
        int length;

        try {


            tmp = (TempFile) request.getAttribute(GS.DOWNLOADCONTEXT);

            sp = new SplitPath(tmp.getDisplayFilename());

            String contentType = (String) extMap.get(sp.getExt().toLowerCase());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            file = new File(tmp.getPath());
            is = new FileInputStream(file);

            log.write(GS.LOG_INF, CLASSNAME, "start - " + tmp.getPath() + " " + tmp.getDisplayFilename());

            response.setContentType(contentType);

            InputCheck chk = new InputCheck();
            String filename = tmp.getDisplayFilename().replace(GS.SPACE_CHARCTER, GS.SPACE_CHARCTER_ENCODE);
            filename = filename.replace(GS.SLASH, GS.SPACE_CHARCTER_ENCODE);
            filename = filename.replace(GS.SEMICOLON, GS.SPACE_CHARCTER_ENCODE);

            if (!chk.isHankaku(filename)) {
                SplitPath path = new SplitPath(filename);
                filename = path.getFname();
//				if( path.getFname().length() != filename.length() ) {
//					filename += "...";
//				}
                //filename = java.net.URLEncoder.encode(filename,"UTF-8") + "." + path.getExt();
                // No353, 2008/05/23, SJA渡辺, ダウンロード時開くを押した際のファイル名の文字化けを修正。
                // 2022/06/10 Fix bug No.7,No.13 START
                //filename = new String((filename).getBytes("SJIS"),"ISO-8859-1") + "." + path.getExt();
                filename = URLEncoder.encode(path.getFname(), "UTF-8") + "." + path.getExt();
                // 2022/06/10 Fix bug No.7,No.13 END
            }
            // 課題No.52
            // 修正開始
            // response.setHeader("Content-Disposition", "attachment;filename=\"" + filename +"\"");
            String conDis = Function.trim(sp.getExt().toLowerCase());
            if (conDis.equals("html") || conDis.equals("htm") || conDis.equals("xml")) {
                response.setHeader("Content-Disposition", "inline;filename=\"" + filename + "\"");
            } else {
                response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
            }
            response.setContentLength(tmp.getLength());
            // 修正完了

            //no-cache指定は、ダイアログの「開く」でファイルを参照できない
//			response.setHeader("Pragma","no-cache");
//			response.setHeader("Cache-Control","no-cache");

            OutputStream os = response.getOutputStream();
            while ((length = is.read(buf)) != -1) {
                os.write(buf, 0, length);
            }

            os.flush();
            os.close();

            log.write(GS.LOG_INF, CLASSNAME, "end - " + tmp.getPath() + " " + tmp.getDisplayFilename());

        } catch (IOException e) {
            // クライアントのダイアログでキャンセルされた時の例外を無視
            if (e.getMessage() != null) {
                log.write(GS.LOG_INF, CLASSNAME, "end - " + e.getMessage());
                throw e;
            } else {
                log.write(GS.LOG_INF, CLASSNAME, "end - キャンセルボタンが押下されました。");
            }
        } finally {
            sp = null;
            file = null;
            buf = null;
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    log.write(GS.LOG_INF, CLASSNAME, "end - " + e.getMessage());
                    throw e;
                }
            }
            if (tmp != null) {
                try {
                    tmp.delete();
                } catch (Exception e) {
                    log.write(GS.LOG_INF, CLASSNAME, "end - " + e.getMessage());
                    throw e;
                }
            }
            tmp = null;
            is = null;
        }
        return null;
    }
}
