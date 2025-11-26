/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.tairyu.bss;

import app.MeisaisyosaiBean;
import app.SessionData;
import app.tairyu.dbAcc.SyosaiDbAcc;
import app.tairyu.form.SyosaiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.struts.AppDownload;
import common.util.Ftp;
import common.util.InputCheck;
import common.util.Log;
import common.util.TempFile;

import java.util.Map;

/**
 * OB1103_実質滞留債権判定_明細詳細 ビジネスロジッククラス <br>
 */
public class SyosaiBss {

    private AppContext appContext = null;		// ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;		// ＤＢアクセス
    private Log log = null;					// LOG
    private SessionData cmnData;				// 共通セッション
    private MeisaisyosaiBean syosai_bean;		// 明細情報
    
    private static final String TAIRYU_HANTEI_OVER6		= "1";                  // 滞留判定 「6ヶ月超滞留」
    private static final int MAX_LEN_HANTEI_JIYU			= 1000;					// 判定事由の最大バイト数
    private static final String FILE_NM 				= "file_nm"; 				// ファイル名
	private static final String JITU_FILE_NM 			= "jitu_file_nm"; 			// 実ファイル名

    /**
     * コンストラクタ <br>
     * 
     * @param appContext AppContext
     * @throws Exception Exception
     */
    public SyosaiBss(AppContext appContext) throws Exception {
        this.appContext = appContext;
        this.log = appContext.getLog();
        this.cmnData = appContext.getCMN();
        this.syosai_bean = cmnData.getSyosai_bean();
    }

    /**
     * 画面初期表示値取得(メニューリンクから遷移時) <br>
     * 
     * @throws Exception Exception
     */
    public void executeInit() throws Exception {

        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SyosaiDbAcc dbacc = new SyosaiDbAcc(sqlExec, log, appContext);
        
        // 汎用項目タイトル名取得
        dbacc.getKomokuTitles();
        
        // 滞留判定セレクトボックス値取得
        dbacc.getTairyuJdg();

        // 添付ファイル情報の取得
        dbacc.getTenpu();
    }

    /**
     * 画面初期表示値取得(明細一覧以外から遷移時) <br>
     * 
     * @return forward
     * @throws Exception Exception
     */
    public String execute() throws Exception {

        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SyosaiDbAcc dbacc = new SyosaiDbAcc(sqlExec, log, appContext);

        // 添付ファイル情報の取得
        dbacc.getTenpu();

        return GS.OB1103;
    }

    /**
     * 保存処理 <br>
     * 
     * @return 成功フラグ
     * @throws Exception Exception
     */
    public boolean save() throws Exception {

        // 登録チェック
        if (!this.doCheck()) {
        	return false;
        }
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SyosaiDbAcc dbacc = new SyosaiDbAcc(sqlExec, log, appContext);
        // T10_滞留判定の更新
        dbacc.updT10TairyuHantei();
        // T11_文書添付の削除
        dbacc.delT11BunsyoTenpu();
        dbacc.commit();
        return true;
    }
    
    /**
     * 登録チェック <br>
     * 
     * @return forward
     * @throws Exception Exception
     */
    private boolean doCheck() throws Exception {
    	// チェッククラス
    	InputCheck check = new InputCheck();
        // 滞留判定
        String tairyuHantei = syosai_bean.getTairyu_hantei();
        // 判定事由
        String hanteiJiyu = syosai_bean.getHantei_jiyu();
        
    	// 滞留判定が選択されていない場合
        if (check.isNullBlank(tairyuHantei)) {
            // エラーダイアログ（err.select、ラベル名キー(滞留判定)）を表示する。
            appContext.setMsgCode(GL.ERR_SELECT, GL.OB1103_TAIRYUHANTEI);
            return false;
        }
    	// 滞留判定が「6ヶ月超滞留」以外の場合
        if (!TAIRYU_HANTEI_OVER6.equals(tairyuHantei)) {
        	// 判定事由が入力されていない場合
        	if (check.isNullBlank(hanteiJiyu)) {
                // エラーダイアログ（err.Input）を表示する。
                appContext.setMsgCode(GL.ERR_INPUT,GL.REPLACE_HANTEIJIYU);
                return false;
        	}
        }
    	// 判定事由が1000バイトを超える場合
        if (check.lenB(hanteiJiyu) > MAX_LEN_HANTEI_JIYU) {
			// エラーダイアログ（err.length、ラベル名キー(判定事由)）を表示する。
            appContext.setMsgCode(GL.ERR_LENGTH, GL.OB1103_MSGHANTEIJIYU);
            return false;
		}
    	// 判定事由に入力禁止文字が含まれている場合
		if (check.haveKinshiMoji(hanteiJiyu)) {
			// エラーダイアログ（err.prohibitted）を表示する。
			for (int i = 0; i < hanteiJiyu.length(); i++) {
				String kinshiChar = hanteiJiyu.substring(i, i + 1);
				if (check.haveKinshiMoji(kinshiChar)) {
					// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
					appContext.setMsgCd(GL.ERR_PROHIBITTED, kinshiChar);
					return false;
				}
			}
		}
    	return true;
    }

    /**
     * ダウンロード処理 <br>
     * 
     * @throws Exception Exception
     */
    public void download() throws Exception {
    	// アクションフォーム
    	SyosaiForm form = (SyosaiForm)appContext.getActionForm();
    	// 選択されたインデックス
    	int index = form.getId();
    	// 添付情報
    	Map tempMap = form.getAr_tenpu().get(index);
    	// ファイル名（パース含む）
    	String remoteFname = (String)tempMap.get(JITU_FILE_NM);
    	String localFname = (String)tempMap.get(FILE_NM);
    	// ダウンロードを行う
    	Ftp ftp = new Ftp();
    	TempFile tempFile = ftp.Get(remoteFname, localFname);
    	AppDownload appDownload = new AppDownload();
    	appDownload.execute(tempFile, appContext.getResponse());
    }
}