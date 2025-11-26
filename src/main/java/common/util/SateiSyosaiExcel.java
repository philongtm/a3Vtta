/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/21		SSC				1.5次版機能組込
******************************************************************************/
package common.util;

import app.SessionDataZen;
import app.commonZen.dbAcc.HikiatekinHanteiSyokaiDbAcc;
import app.commonZen.dbAcc.RyuhoSaimuSyokaiDbAcc;
import app.commonZen.dbAcc.SaikenMeisaiSyokaiDbAcc;
import app.commonZen.dbAcc.TorihikisakiGaiyoSyokaiDbAcc;
import app.commonZen.dbAcc.TorihikisakiKubunHanteiSyokaiDbAcc;
import app.commonZen.form.HikiatekinHanteiSyokaiForm;
import app.commonZen.form.RyuhoSaimuSyokaiForm;
import app.commonZen.form.SaikenMeisaiSyokaiForm;
import app.commonZen.form.TorihikisakiGaiyoSyokaiForm;
import app.commonZen.form.TorihikisakiKubunHanteiSyokaiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.struts.adapter.action.ActionForm;

import java.util.ArrayList;

//No219, 2008/05/29, SJA渡辺, 帳票をまとめる処理追加
/**
 * 査定詳細Excel(債務明細一覧、債権明細一覧、判定シートをまとめたもの)
 */
public class SateiSyosaiExcel {
	
	private String CLASSNAME = getClass().getName(); // クラス名
	
	private AppContext appContext = null;		// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;		// ＤＢアクセス
	private Log log = null;					// LOG

	private SessionDataZen cmnData;				// 共通セッション
	
	/**
	 * コンストラクタ
	 */
	public SateiSyosaiExcel(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		cmnData = appContext.getCMNZenRe();
	}
	
	/**
	 * 出力項目検索処理
	 * 
	 * @return GS.RC_OK
	 * @throws Exception
	 */
	public String execute() throws Exception {	
	    // コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		ActionForm form = appContext.getActionForm();

		// 判定シート項目取得
		appContext.setActionForm(new TorihikisakiGaiyoSyokaiForm());
		TorihikisakiGaiyoSyokaiDbAcc dbacc1 = new TorihikisakiGaiyoSyokaiDbAcc(sqlExec, log, appContext);
		TorihikisakiGaiyoSyokaiForm torihikisakiGaiyoSyokaiForm = 
			dbacc1.getTorihikisakiGaiyoForm(cmnData.getSatei_anken_no());
		
		appContext.setActionForm(new TorihikisakiKubunHanteiSyokaiForm());
		TorihikisakiKubunHanteiSyokaiDbAcc dbacc2 = new TorihikisakiKubunHanteiSyokaiDbAcc(sqlExec, log, appContext);
		TorihikisakiKubunHanteiSyokaiForm torihikisakiKubunHanteiSyokaiForm = 
			dbacc2.getTorihikisakiKubunHanteiForm(cmnData.getSatei_anken_no(),cmnData.getPhase());
		
		appContext.setActionForm(new HikiatekinHanteiSyokaiForm());
		HikiatekinHanteiSyokaiDbAcc dbacc3 = new HikiatekinHanteiSyokaiDbAcc(sqlExec, log, appContext);
		HikiatekinHanteiSyokaiForm hikiatekinHanteiSyokaiForm = 
			dbacc3.getHikiatekinHanteiForm(cmnData.getSatei_anken_no(),
				Function.removeDateSlash(cmnData.getYm()),
				cmnData.getPhase(),cmnData.getKanjo_cd(),new ArrayList());
		
		// 債権明細一覧シート項目取得
		appContext.setActionForm(new SaikenMeisaiSyokaiForm());
		SaikenMeisaiSyokaiDbAcc dbacc4 = new SaikenMeisaiSyokaiDbAcc(sqlExec, log, appContext);
		// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
		//ArrayList saikenMeisaiList = dbacc4.getSaikenMeisaiComList(cmnData.getKanjo_cd(),cmnData.getYm(),cmnData.getComSateiKaishaCd(),cmnData);
		ArrayList saikenMeisaiList = dbacc4.getSaikenMeisaiComList(cmnData.getKanjo_cd(),cmnData.getYm(),cmnData.getAnken_satei_kaisya_cd(),cmnData);
		
		// 債務明細一覧シート項目取得
		appContext.setActionForm(new RyuhoSaimuSyokaiForm());
		RyuhoSaimuSyokaiDbAcc dbacc5 = new RyuhoSaimuSyokaiDbAcc(sqlExec, log, appContext);
		ArrayList ryuhoSaimuList = dbacc5.getRyuhoSaimuSyosaiList();
		
		appContext.setActionForm(form);
		
		// 帳票作成
		SateiSyosaiExcelDbAcc dbacc = new SateiSyosaiExcelDbAcc(sqlExec, log, appContext);
		dbacc.execute(torihikisakiGaiyoSyokaiForm,torihikisakiKubunHanteiSyokaiForm,
				hikiatekinHanteiSyokaiForm,saikenMeisaiList,ryuhoSaimuList);
		// コネクションの開放
		appContext.destroy();		
		return GS.RC_OK;
	}

}
