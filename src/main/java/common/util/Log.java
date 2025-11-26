/******************************************************************************
 著作権情報				:
 使用JDK バージョン		: 1.4.2.05
 更新履歴
 No		日付			修正者			修正内容
 ******************************************************************************/
package common.util;

import common.AppContext;
import common.global.GS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

/**
 * ログ出力するクラス
 *
 */
public class Log {

    // ログディレクトリ
    private static String logDir = null;

    // ログレベル
    private static final int logLevel = Profile.getInt(GS.PROFILE_LOGLEVEL, 0);

    // 出力用クラス
    private static PrintWriter pw = null;

    //
    private static boolean openError = false;

    // 時間表示用クラス
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    // 日替わり判定用
    private static final SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat sdfDD = new SimpleDateFormat("dd");
    private static String currentYMD = "";
    private static String currentDD = "";

    private AppContext appContext = null;

    static final String[] space = {
            "        ",
            "       ",
            "      ",
            "     ",
            "    ",
            "   ",
            "  ",
            " "
    };

    /**
     * コンストラクタ
     */
    public Log() {
        this.appContext = null;
    }

    /**
     * コンストラクタ
     *
     * @param appContext ＡＰＰコンテキスト
     */
    public Log(AppContext appContext) {
        this.appContext = appContext;
    }

    /**
     * ログファイルのオープンを行う。
     * <pre>
     * 使用ディレクトリ：/WEB-INF/log
     * </pre>
     */
    public static void open(String dir) {
        logDir = dir;
        File file = new File(logDir);
        if (!file.exists()) {
            file.mkdir();
        }
        dayChange();
        write(null, GS.LOG_INF, "", ">>>> " + sdf.format(new Date()) + " : アプリケーションを開始します <<<<");

        try {
            // ログファイルの一覧を作成
            File fdir = new File(logDir);
            String[] dirList = fdir.list();

            // 今日以外のログファイル圧縮
            for (int i = 0; i < dirList.length; i++) {
                if ((!dirList[i].startsWith(currentDD)) && (dirList[i].endsWith(".log"))) {
                    compress(dirList[i]);
                }
            }
        } catch (Exception e) {
            write(null, GS.LOG_INF, "", ">>>> " + e.getMessage());
        }
    }

    /**
     * ログファイルのクローズを行う。
     *
     */
    public static void close() {
        write(null, GS.LOG_INF, "", ">>>> " + sdf.format(new Date()) + " : アプリケーションを終了します <<<<");
        if (pw != null) {
            pw.close();
            pw = null;
        }
    }

    /**
     * ログの出力を行う。
     * 設定ファイルのログレベル以下のメッセージを出力する。
     *
     * @param level     ログレベル LOG_ERR:エラー LOG_WAR:警告 LOG_INF:情報 LOG_DBG:デバッグ
     * @param className クラス名
     * @param message   メッセージ
     */
    public void write(int level, String className, String message) {
        if (level <= logLevel) {
            write(appContext, level, className, message);
        }
    }

    /**
     * Exceptionスタックトレースの出力を行う。
     * 設定ファイルのログレベル以下のメッセージを出力する。
     *
     * @param level     ログレベル LOG_ERR:エラー LOG_WAR:警告 LOG_INF:情報 LOG_DBG:デバッグ
     * @param className クラス名
     */
    public void write(int level, String className, Throwable exception) {
        if (level <= logLevel) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            if (exception != null) {
                exception.printStackTrace(pw);
            }
            StringBuffer sb = sw.getBuffer();
            write(appContext, level, className, sb.toString());
        }
    }

    /**
     * ログの出力を行う。
     * 設定ファイルのログレベル以下のメッセージを出力する。
     *
     * @param level     ログレベル LOG_ERR:エラー LOG_WAR:警告 LOG_INF:情報 LOG_DBG:デバッグ
     * @param className クラス名
     * @param message   メッセージ
     */
    private synchronized static void write(AppContext appContext,
                                           int level,
                                           String className,
                                           String message) {

        String userId = "";

        if (appContext != null) {
            try {
                app.SessionData as = appContext.getCMN();
                if ((as != null) && (as.getUser_bean().getComUserId() != null)) {
                    userId = as.getUser_bean().getComUserId();
                }
            } catch (Exception e) {
                appContext = null;
            }
        }
        int n = userId.length();
        if (n < 8) {
            userId += space[n];
        }

        String levelTitle;
        String printMessage;
        dayChange();
        if (level == GS.LOG_DBG) {
            levelTitle = "D";
        } else if (level == GS.LOG_INF) {
            levelTitle = "I";
        } else if (level == GS.LOG_WAR) {
            levelTitle = "W";
        } else if (level == GS.LOG_ERR) {
            levelTitle = "E";
        } else {
            levelTitle = "?";
        }
        printMessage = sdf.format(new Date()) + " |" + levelTitle + "|" + userId + "|" + className + "| " + message;
        if ((pw != null) && (!openError)) {
            pw.println(printMessage);
            pw.flush();
        }
        System.out.println(printMessage);
    }

    /**
     * 日替わりの処理を行う。
     * 日が変わったら出力先ディレクトリを変更し、旧ファイルを削除する。
     */
    private static void dayChange() {

        String ymd = sdfYMD.format(new Date());
        String logFile;

        if (currentYMD.equals(ymd)) {
            return;

        } else {

            // ファイルをクローズ
            if (pw != null) {
                pw.close();
                pw = null;
            }

            // 前日のログファイルを圧縮する
            if (currentDD.length() > 0) {
                compress(currentDD + ".log");
            }

            // 今日のログファイル名を編集
            Date date = new Date();
            currentYMD = sdfYMD.format(date);
            currentDD = sdfDD.format(date);
            logFile = logDir + File.separator + currentDD + ".log";

            // 前月のファイルを削除
            fileDelete(logFile);
        }

        try {
            // 今日のログファイルオープン
            pw = new PrintWriter(new FileWriter(logFile, true));
        } catch (IOException e) {
            System.out.println(logFile + ":" + e.getMessage());
            pw = null;
            openError = true;
        }
    }

    /**
     * 出力先ディレクトリのファイル削除を行う。
     * 現在日より２日以前のものを削除する。
     *
     * @param logDir 出力先ディレクトリ
     */
    private static synchronized void fileDelete(String logFile) {
        long timeOverVal = 86400 * 1000 * 2; //24時間（秒）* ミリ秒 * 2
        long curentUtc = System.currentTimeMillis();

        File wkFile = new File(logFile);
        if (wkFile.exists()) {
            long lastAccs = wkFile.lastModified();
            if ((curentUtc - lastAccs) > timeOverVal) {
                wkFile.delete();
            }
        }
    }

    /**
     * ログファイルの圧縮
     *
     * @param logFile
     */
    private static void compress(String logFile) {
        FileInputStream is = null;
        GZIPOutputStream os = null;
        try {
            String path = logDir + File.separator + logFile;
            if ((pw != null) && (!openError)) pw.println(path);
            System.out.println(path);

            // 読み込み用オブジェクト生成
            File file = new File(path);
            is = new FileInputStream(file);

            // ZIPへの書き込み用オブジェクト生成
            File zipFile = new File(path + ".gz");
            os = new GZIPOutputStream(new FileOutputStream(zipFile));

            // ZIPへの書き込み
            int length;
            byte[] buf = new byte[1024];
            while ((length = is.read(buf)) != -1) {
                os.write(buf, 0, length);
            }

            // クローズ
            os.finish();
            os.close();
            os = null;
            is.close();
            is = null;

            buf = null;

            // ログファイルを削除
            file.delete();
        } catch (Exception e) {
            if ((pw != null) && (!openError)) pw.println(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                    os = null;
                }
            } catch (Exception e) {
                if ((pw != null) && (!openError)) pw.println(e.getMessage());
                System.out.println(e.getMessage());
            }
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (Exception e) {
                if ((pw != null) && (!openError)) pw.println(e.getMessage());
                System.out.println(e.getMessage());
            }
        }
        if ((pw != null) && (!openError)) pw.flush();
    }

//	/**
//	 * ログファイルの圧縮
//	 * 
//	 * @param logFile
//	 */
//	private static void compress(String logFile){
//		try {
//			String cmd = "/usr/bin/gzip -f9 " + logDir + File.separator + logFile;
//			if( (pw!=null) && (!openError) ) pw.println(cmd);
//			System.out.println(cmd);
//
//			Runtime rt = Runtime.getRuntime();
//	        Process p  = rt.exec(cmd);
//	        InputStreamReader in  = new InputStreamReader(p.getInputStream());
//	        BufferedReader reader = new BufferedReader(in);
//	        String line;
//	        while ((line = reader.readLine()) != null) {
//				if( (pw!=null) && (!openError) ) pw.println(line);
//				System.out.println(line);
//	        }
//	        reader.close();
//	        p.getInputStream().close();
//	        p.getOutputStream().close();
//	        p.getErrorStream().close();
//	        p.waitFor();
//		}
//		catch(Exception e){		
//			if( (pw!=null) && (!openError) ) pw.println(e.getMessage()); 
//			System.out.println(e.getMessage());
//		}
//		if( (pw!=null) && (!openError) ) pw.flush();
//	}
}