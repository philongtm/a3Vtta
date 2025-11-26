/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/29		SSC				課題No.87 取引先名称NULL考慮 
******************************************************************************/
package app.tairyu.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.tairyu.dbAcc.KarikijuntuikaDbAcc;
import app.tairyu.form.KarikijuntuikaForm;
import common.AppContext;
import common.global.GS;
import common.util.Function;

/**
 * OB2105_対象先選定_仮基準査定選択 ビジネスロジッククラス
 */
public class KarikijuntuikaBss {

	private AppContext appContext = null;			// ＡＰＰコンテキスト
	private SessionData cmnData;					// 共通セッション
	private TorihikisakiBean tori_bean;			// 取引先情報
	private KarikijuntuikaForm form;				// アクションフォーム
    private static final String MOGITORI_KAIJO	= "40";//入力区分 '40'： もぎ取り解除
    private static final String HAKI			    = "70";//入力区分 '70'： 破棄
    private static final String TAISYO			= "0";//対象外フラグ '0'： 対象
    private static final String TAISYOGAI			= "1";//対象外フラグ '1'： 対象外

	/**
	 * コンストラクタ
	 */
	public KarikijuntuikaBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.cmnData = appContext.getCMN();
		this.tori_bean = cmnData.getTori_bean();
		this.form = (KarikijuntuikaForm)appContext.getActionForm();
	}

	/**
	 * 画面初期表示値取得(メニューリンク以外から遷移時)
	 */
	public void execute() throws Exception {	

		String taisho_ym = GS.EMPTY_CHARCTER;
		form.setSystemKbn(new String(tori_bean.getSystem_kbn()));
		form.setSateiki(new String(tori_bean.getSatei_ki()));
		form.setSateiki_hyouji(new String(tori_bean.getSatei_ki_hyouji()));
		form.setKanjo_cd(new String(Function.trim(tori_bean.getKanjo_cd())));
		// 課題No.87
		// 修正開始
		//form.setKanjo_nm(new String(tori_bean.getKanjo_nm()));
		form.setKanjo_nm(new String(Function.trim(tori_bean.getKanjo_nm())));
		// 修正完了
		form.setSateikaisya_cd(new String(tori_bean.getSateikaisya_cd()));
		form.setHanyou2(new String(Function.trim(tori_bean.getInit_bunrui2())));
		form.setAnkenNo(new String(tori_bean.getAnken_no()));
		form.setPhase(new String(tori_bean.getPhase()));
		form.setSasiTenFlg(new String(Function.trim(tori_bean.getSasi_ten_flg())));
		KarikijuntuikaDbAcc dbacc = new KarikijuntuikaDbAcc(appContext);
		
		// 査定関連データ削除
		dbacc.delT15();
		dbacc.delT16();
		dbacc.delT17();

		// 表示件数セレクトボックス値取得
		dbacc.getShow();

		// 一覧情報取得
		taisho_ym = dbacc.getTaisho_ym();
		dbacc.getMeisai(taisho_ym);

		dbacc.commit();
	}
	
	/**
	 * もぎ取り解除処理<br>
	 */
	public void execKaijo() throws Exception{

    	KarikijuntuikaDbAcc dbacc = new KarikijuntuikaDbAcc(appContext);
		//コメント削除
		dbacc.delT12();
		//進捗更新
		dbacc.updT14(GS.STATUS_MISYORI,form.getSasiTenFlg(),TAISYO);
		//入力履歴登録
		dbacc.insT13(MOGITORI_KAIJO);
		//コミット
		dbacc.commit();
	}

	/**
	 * 追加中止処理<br>
	 */
	public void execCancel() throws Exception{

    	KarikijuntuikaDbAcc dbacc = new KarikijuntuikaDbAcc(appContext);
		
		//進捗更新
		dbacc.updT14(GS.STATUS_KANRYO,GS.EMPTY_CHARCTER,TAISYOGAI);
		//入力履歴登録
   		dbacc.insT13(HAKI);

        // コミット
       	dbacc.commit();
	}

	/**
	 * コメント画面遷移前処理<br>
	 */
	public void zenSashiCom() throws Exception{

		// 選定詳細から戻るボタン押下した場合、取引先情報は初期化される。
		// 故にコメント表示画面で取引先情報を参照することができない。
		// よってここでコメント表示画面に必要な情報を設定する。
		
		TorihikisakiBean toriBean = new TorihikisakiBean();
    	
		toriBean.setSystem_kbn(form.getSystemKbn());
		toriBean.setKanjo_cd(form.getKanjo_cd());
		toriBean.setKanjo_nm(form.getKanjo_nm());
		cmnData.setTori_bean(toriBean);
	}
}