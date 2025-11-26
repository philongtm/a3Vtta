/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/27		SSC				課題No.169 滞留判定取得 
******************************************************************************/
package app.system.bss;


import app.MeisaisyosaiBean;
import app.SessionData;
import app.system.dbAcc.KureemuSyosaiDbAcc;
import app.system.form.KureemuSyosaiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.util.InputCheck;
import common.util.Log;

/**
 * OS3103_クレーム債権_明細詳細 ビジネスロジッククラス <br>
 */
public class KureemuSyosaiBss {

    private AppContext appContext = null;		// ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;		// ＤＢアクセス
    private Log log = null;					// LOG
    private SessionData cmnData;				// 共通セッション
    private MeisaisyosaiBean syosai_bean;		// 明細情報
    private KureemuSyosaiForm form;         	// アクションフォーム
    
    private static final int MAX_LEN_HANTEI_JIYU			= 1000;					// 判定事由の最大バイト数
    //課題No.169
    //修正開始
    //private static final String KUREMU			     	= "クレーム債権";       // クレーム債権
    private static final String KUREMU			     	= "4";       // クレーム債権
    //修正完了
    
    /**
     * コンストラクタ <br>
     * 
     * @param appContext AppContext
     * @throws Exception Exception
     */
    public KureemuSyosaiBss(AppContext appContext) throws Exception {
        this.appContext = appContext;
        this.log = appContext.getLog();
        this.cmnData = appContext.getCMN();
        this.form = (KureemuSyosaiForm) appContext.getActionForm();
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
        KureemuSyosaiDbAcc dbacc = new KureemuSyosaiDbAcc(sqlExec, log, appContext);
        
        // 汎用項目タイトル名取得
        dbacc.getKomokuTitles();
        
        // '4'以外の場合はチェックオフで表示
        form.setKureemu_saiken(false);
        // 共)明細詳細情報.滞留判定が '4'：クレーム債権の場合、チェックオンで表示        
        //課題No.169
        //修正開始
        //if (KUREMU.equals(syosai_bean.getTairyu_hantei())) {
        if (KUREMU.equals(syosai_bean.getTairyuHantei())) {
        	form.setKureemu_saiken(true);
        }
        //修正完了
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
        KureemuSyosaiDbAcc dbacc = new KureemuSyosaiDbAcc(sqlExec, log, appContext);
        
        // T10_滞留判定の滞留判定案件Noを取得
        String tairyu_ankenno = dbacc.getTairyuAnkenNo();
        
        // T10_滞留判定の更新
        dbacc.setUpdateT1000(tairyu_ankenno);	
        
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
        // 判定事由
        String hanteiJiyu = syosai_bean.getHantei_jiyu();
        
    	// クレーム債権、且つ判定事由が未入力の場合
        if (form.isKureemu_saiken() && check.isNullBlank(hanteiJiyu)) {
            appContext.setMsgCode(GL.ERR_SELECT, GL.OS3103_HANTEIJIYU);
            return false;
        }
    	// 判定事由が1000バイトを超える場合
        if (check.lenB(hanteiJiyu) > MAX_LEN_HANTEI_JIYU) {
            appContext.setMsgCode(GL.ERR_LENGTH, GL.OS3103_HANTEIJIYU);
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
}