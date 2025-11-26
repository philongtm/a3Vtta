/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.common.dbAcc.KihonJohoSyokaiDbAcc;
import app.common.form.KihonJohoSyokaiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

/**
 * OZ6108_基本情報照会タブ ビジネスロジッククラス
 */
public class KihonJohoSyokaiBss {

	private AppContext appContext = null;		// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;		// ＤＢアクセス
	private Log log = null;					// LOG
	private KihonJohoSyokaiForm form = null;	// アクションフォーム
	private SessionData cmnData;				// 共通セッション
	private TorihikisakiBean tori_bean;		// 取引先情報
	
	/**
	 * コンストラクタ
	 */
	public KihonJohoSyokaiBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.tori_bean = cmnData.getTori_bean();
		this.form = (KihonJohoSyokaiForm)appContext.getActionForm();
	}

	/**
	 * 画面初期表示値取得
	 */
	public String execute() throws Exception {	

		double ippan_saiken_kei = 0;
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		KihonJohoSyokaiDbAcc dbacc = new KihonJohoSyokaiDbAcc(sqlExec, log, appContext);

		// 汎用項目ラベル名取得
		dbacc.getLabel();

		// 明細情報取得
        SessionData cmnData = appContext.getCMN();
        String rtn_id = cmnData.getReturn_gamenId();
        ippan_saiken_kei = dbacc.getMeisai(rtn_id);

		// 項目１取得(海外のみ)
		if(!GS.GSS.equals(tori_bean.getSystem_kbn())){
			dbacc.getKomoku1(ippan_saiken_kei);
		}

		// 親会社名称設定
		if(GS.OB2103.equals(rtn_id) || GS.OB2105.equals(rtn_id) || GS.OS4101.equals(rtn_id)){
			//OB2103_対象先選定_追加対象先選択、OB2105_対象先選定_仮基準査定選択、OS4101_ゴルフ会員権一覧から遷移時
			dbacc.getOya_kaisya_nm();
		}else{
			//OB2101_対象先選定_選定実行、OB2104_対象先選定_承認一覧から遷移時
			form.setOya_kaisya_nm(tori_bean.getOya_business_nm());
		}
		return cmnData.getTab_riyou_gamenId();
	}
}