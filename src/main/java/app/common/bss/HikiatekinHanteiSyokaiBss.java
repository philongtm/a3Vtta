/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/30		SSC				課題No.174 金額フォーマット修正 
******************************************************************************/
package app.common.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.common.dbAcc.HikiatekinHanteiSyokaiDbAcc;
import app.common.form.HikiatekinHanteiSyokaiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

/**
 * OZ6104_引当金判定照会タブ　ビジネスロジッククラス
 */
public class HikiatekinHanteiSyokaiBss {
	
	private AppContext appContext = null;				// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;				// ＤＢアクセス
	private Log log = null;							// LOG
	private SessionData cmnData;						// 共通セッション
	private TorihikisakiBean tori_bean = null;			// 取引先情報
	private HikiatekinHanteiSyokaiForm form=null;		// アクションホーム
	
	private static final String KINGAKU_FORMAT_JA		="##,###,###,###,###,##0.##";	// 金額フォーマット(国内)
	private static final String KINGAKU_FORMAT_EN		="##,###,###,###,###,##0.00";	// 金額フォーマット(海外)
	private String system_kbn;
	
	/**
	 * コンストラクタ
	 */
	public HikiatekinHanteiSyokaiBss(AppContext appContext) {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.tori_bean = cmnData.getTori_bean();
		this.form = (HikiatekinHanteiSyokaiForm)appContext.getActionForm();	
		this.system_kbn = tori_bean.getSystem_kbn();
	}
	
	/**
	 * 対象先検索を行う。
	 */
	public String execute() throws Exception {

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		HikiatekinHanteiSyokaiDbAcc dbacc = new HikiatekinHanteiSyokaiDbAcc(sqlExec, log, appContext);
		
		////勘定表示区分の各金額【リスト】を取得
		dbacc.kanjo_kingaku();
		//引当金判定情報の登録内容を取得
		dbacc.gethikiate();
		//第三者留保債務内訳を取得
		dbacc.ryuhosaimu();
		//当画面のコメント類を取得
		dbacc.comment();
		//汎用項目ラベルを取得
		dbacc.hanyo();
		//フラグ区分を取得
		dbacc.flg_kbn();
		
	    // 一般債権計
		double ipan_saimukei = 0;
		ipan_saimukei = Function.getValueOfDoubleC(form.getUketoritegata())*100+			
						Function.getValueOfDoubleC(form.getYusyutu_uketoritegata())*100+	
						Function.getValueOfDoubleC(form.getUrikakekin())*100+				
						Function.getValueOfDoubleC(form.getTorihikimaetokin())*100+			
						Function.getValueOfDoubleC(form.getTatekaekin())*100+				
						Function.getValueOfDoubleC(form.getMisyunyukin())*100+				
						Function.getValueOfDoubleC(form.getMisyusyueki())*100+				
						Function.getValueOfDoubleC(form.getTanki_kashitsukekin())*100+		
						Function.getValueOfDoubleC(form.getSashiire_hosyokin())*100+		
						Function.getValueOfDoubleC(form.getKaribaraikin())*100+				
						Function.getValueOfDoubleC(form.getTyoki_kashitsukekin())*100+		
						Function.getValueOfDoubleC(form.getSonota_toshi())*100;	
		
		ipan_saimukei = Function.marume(ipan_saimukei);
		if(system_kbn.equals(GS.GSS)){
			form.setIpan_saimukei(Function.format(KINGAKU_FORMAT_JA, ipan_saimukei/100));
			
		}else{
			form.setIpan_saimukei(Function.format(KINGAKU_FORMAT_EN, ipan_saimukei/100));
		}
			
		//債権残高合計
		double saiken_zankei = 0;
		saiken_zankei = Function.getValueOfDoubleC(form.getIpan_saimukei())*100 +
						Function.getValueOfDoubleC(form.getHanyo1())*100;
		
		saiken_zankei = Function.marume(saiken_zankei);
		if(system_kbn.equals(GS.GSS)){
			form.setSaiken_zankei(Function.format(KINGAKU_FORMAT_JA,saiken_zankei/100));
		}else{
			form.setSaiken_zankei(Function.format(KINGAKU_FORMAT_EN,saiken_zankei/100));
		}
			
		//留保債務計
		double ryuhosaimu_kei = 0;
		ryuhosaimu_kei = Function.getValueOfDoubleC(form.getRyuhosaimu())*100 +
						 Function.getValueOfDoubleC(form.getOth_ryuhosaimu())*100;
		saiken_zankei = Function.marume(saiken_zankei);
		ryuhosaimu_kei = Function.marume(ryuhosaimu_kei);
		if (ryuhosaimu_kei > saiken_zankei) {
			if(saiken_zankei > 0){
				if(system_kbn.equals(GS.GSS)){
					form.setRyuhosaimu_kei(Function.format(KINGAKU_FORMAT_JA,saiken_zankei/100));
				}else{
					form.setRyuhosaimu_kei(Function.format(KINGAKU_FORMAT_EN,saiken_zankei/100));
				}
			} else
				if(system_kbn.equals(GS.GSS)){
					form.setRyuhosaimu_kei(Function.format(KINGAKU_FORMAT_JA,0));
				}else{
					//課題No.174
					//修正開始
					//form.setRyuhosaimu_kei(Function.format(KINGAKU_FORMAT_JA,0.00));
					form.setRyuhosaimu_kei(Function.format(KINGAKU_FORMAT_EN,0.00));
					//修正完了
				}
		} else {
				if(system_kbn.equals(GS.GSS)){
					form.setRyuhosaimu_kei(Function.format(KINGAKU_FORMAT_JA,ryuhosaimu_kei/100));
				}else{
					form.setRyuhosaimu_kei(Function.format(KINGAKU_FORMAT_EN,ryuhosaimu_kei/100));
				}
		}
		
		//引当金対象金額
		double hikiate_taisyokingaku = Function.getValueOfDoubleC(form.getSaiken_zankei())*100 -
										Function.getValueOfDoubleC(form.getRyuhosaimu_kei())*100 -
										Function.getValueOfDoubleC(form.getHozen())*100 -
										Function.getValueOfDoubleC(form.getSonotakaisyu())*100 -
										Function.getValueOfDoubleC(form.getKibikiatekin())*100 +
										Function.getValueOfDoubleC(form.getRiko_kenen())*100;
		hikiate_taisyokingaku = Function.marume(hikiate_taisyokingaku);
		if(system_kbn.equals(GS.GSS)){
			form.setHikiate_taisyokingaku(Function.format(KINGAKU_FORMAT_JA,hikiate_taisyokingaku/100));
		}else{
			form.setHikiate_taisyokingaku(Function.format(KINGAKU_FORMAT_EN,hikiate_taisyokingaku/100));
		}
			
		//追加引当金額(調整後)
		double tuika_kingaku_go =	Function.getValueOfDoubleC(form.getTuika_hikiate())*100 +
									Function.getValueOfDoubleC(form.getTuuka_tyousei())*100;
		
		tuika_kingaku_go = Function.marume(tuika_kingaku_go);
		if(system_kbn.equals(GS.GSS)){
			form.setTuika_kingaku_go(Function.format(KINGAKU_FORMAT_JA,tuika_kingaku_go/100));
		}else{
			form.setTuika_kingaku_go(Function.format(KINGAKU_FORMAT_EN,tuika_kingaku_go/100));
		}

		return cmnData.getTab_riyou_gamenId();
	}	
}
