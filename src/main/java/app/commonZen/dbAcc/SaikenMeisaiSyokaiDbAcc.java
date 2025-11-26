/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/21		SSC				1.5次版機能組込
002		2009/12/1		SSC				課題No.158 年月表示(英語版)を(MM/YYYY)に対応
003		2009/12/3		SSC				課題No.185 ソート順に案件No枝番を追加
******************************************************************************/
package app.commonZen.dbAcc;

import app.SessionDataZen;
import app.commonZen.form.SaikenMeisaiSyokaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 債権明細タブDBアクセスクラス
 */
public class SaikenMeisaiSyokaiDbAcc extends CommonDbAcc {
	
	private final String CLASSNAME = getClass().getName();
	// アプリケーションContext
	private AppContext appContext = null;
	private SessionDataZen cmnData = null;	// 共通セッションデータ
	private SaikenMeisaiSyokaiForm form = null;	// アクションフォーム	
	private String kijunbi_kbn = null;	// 基準日区分
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public SaikenMeisaiSyokaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//Bean取得
		cmnData = appContext.getCMNZenRe();
		form = (SaikenMeisaiSyokaiForm)appContext.getActionForm();
		kijunbi_kbn = appContext.getCMN().getTori_bean().getKijunbi_kbn();

	}

	/**
	 * 検索SQL実行処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void execute() throws SQLException {
		
		ResultSet rs = null;
		StringBuffer sql = null;
		// 検索条件項目取得
		// 案件の登録フェーズ
		String phase = cmnData.getPhase();
		// TODO 取引先コード
		String kanjo_cd = cmnData.getKanjo_cd();
		// 年月
		String ym = cmnData.getYm();
		// 査定会社コード
		// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
		//String sateikaisya_cd = cmnData.getComSateiKaishaCd();
		String sateikaisya_cd = cmnData.getAnken_satei_kaisya_cd();
		
		try {
			ArrayList list = getSaikenMeisaiComList(kanjo_cd, ym, sateikaisya_cd, cmnData);
			
			// 明細配列をFormにセット
			form.setAr_meisai(list);
			// Pagerにセット
			form.setPager(list);
			
			// 一時二次査定テーブルから、留保債務合計、保全、その他回収、履行請求懸念を取得
			/* No.845 getSaikenMeisaiComList実行時に纏めて情報を取得するよう修正
			//setSST_SATEI();												/* No.845 */
			
		}finally{
			if(rs != null){
				try{
					rs.close();
				} catch(Exception e ){
					throw new SQLException(e.getMessage());
				}
			}
		}
	}
	
	// No453, 2008/05/, SJA渡辺, 債権明細タブと帳票の共通化処理修正
	/**
	 * 債権明細リスト取得メソッド
	 * @param kanjo_cd
	 * @param ym
	 * @param sateikaisya_cd
	 * @throws SQLException
	 */
	public ArrayList getSaikenMeisaiComList(String kanjo_cd, String ym, String sateikaisya_cd,SessionDataZen cmnData) throws SQLException {
		//ResultSet rs1 = null;												/* No.845 */
		ResultSet rs2 = null;
		//StringBuffer sql1;												/* No.845 */
		StringBuffer sql2;
		// 障害票No388 2008/05/17 細野 追加対象先選定対応
		// 障害票No388 2008/05/20 細野 遷移元ゴルフの追加
		
		//障害票No388 2008/05/20 細野 遷移元ゴルフの追加
		// ログインユーザの査定会社の確定
	    // 特殊権限（01,02,03）保有者はユーザ参照組織マスタ全ての部門が参照可能で
	    // 査定会社SJを優先としているこのため、ログインユーザ本来の査定会社を確定
	    // する必要があるため再度査定会社コードを取得する
	    
		// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
	    /*sql1 = new StringBuffer()
						.append("SELECT ")
						.append("satei_kaisha_cd ")
						.append("FROM ")
						.append("SSM_SATEIKAISYA ")
						.append("WHERE ")
						.append("kaisha_cd = '")
						.append(cmnData.getComKaishaCd())
						.append("'");*/

			//////////////////////////////////////////////////
			//障害票：845
			//チェックイン日：2008/7/XX
			//対応者：SJA佐々木
			//概要：性能改善施策の一環として、以下の対応を実施。
			//  ・DBアクセス回数の削減および処理効率の改善
			//  ・不要項目・メソッドの削除（コメントアウト）
			//////////////////////////////////////////////////

		try{
			/*rs1 = sqlExec.execQuery(sql1.toString());
	    	while ( rs1.next() ) {
	    		if("01".equals(cmnData.getComUniqueKg())
	    		|| "02".equals(cmnData.getComUniqueKg())
				|| "03".equals(cmnData.getComUniqueKg())){
		    		sateikaisya_cd = rs1.getString("satei_kaisha_cd");
	    		}
	    	}*/
	    
			if(GS.OB2103.equals(cmnData.getOrgReturnId())
			|| GS.SYSTEM_GOLF.equals(cmnData.getOrgReturnId()) 
			|| (GS.OS5101.equals(cmnData.getOrgReturnId()) &&
				!checkHikiateBSMeisai(cmnData))){
				// 障害対応：200807240003 2008/7/27 中島　対象先選定、ゴルフ会員権、チャンピオン部で引当金BS明細に無い案件はBS明細を参照する。
				String sLangMode = cmnData.getComLangMode();
		        sql2 = new StringBuffer()
							.append("SELECT ")
							.append("BS.system_kbn, ")
							.append("BS.tori_cd, ")
							.append("BS.cell_cd, ")
							.append("BS.kanjo_cd, ")
							.append("BS.kanjo_uchi_cd, ")
							// 課題No.158
							// 追加開始
							.append("PG_SS_FUNCTION.SF_SS_ISLANG(TO_CHAR(BS.shusi_dt,'yyyy/mm/dd'),TO_CHAR(BS.shusi_dt,'mm/dd/yyyy'),'")
							.append(sLangMode) 
							.append("') AS shusi_dt,")
							.append("PG_SS_FUNCTION.SF_SS_ISLANG(TO_CHAR(BS.manki_dt,'yyyy/mm/dd'),TO_CHAR(BS.manki_dt,'mm/dd/yyyy'),'")
							.append(sLangMode) 
							.append("') AS manki_dt,")
							.append("PG_SS_FUNCTION.SF_SS_ISLANG(TO_CHAR(BS.syori_dt,'yyyy/mm/dd'),TO_CHAR(BS.syori_dt,'mm/dd/yyyy'),'")
							.append(sLangMode) 
							.append("') AS syori_dt,")
							/*
							.append("BS.shusi_dt, ")
							.append("BS.manki_dt, ")
							.append("BS.syori_dt, ")
							*/
							// 追加完了
							.append("BS.keiyku_denpyo_no AS keiyaku_denpyo_no, ")
							.append("BS.kingaku, ")
							.append("BS.tuuka_cd, ")
							.append("SH.cell_nm, ")
							.append("SH.bu_nm, ")
							.append("SH.seisiki_bumon_cd,")
							.append("SH.bu_cd,")
							.append("BS.kanjo_nm, ")
							.append("BS.kanjo_uchi_nm, ")
							.append("TM.BUSINESS_NM_KJ AS torihikisakimei2, ")
							.append("TM.BUSINESS_NM AS torihikisakimei_en2, ")
							.append("SUBSTR(BS.tori_cd,1,7) AS tori_cd_7,")
		        			.append("KB.KBN_HYOUJI_VAL AS TAIRYU_KBN_VAL,")
							.append("K.saiken_flg ")
							.append("FROM ")
							.append("SSW_BSMEISAI BS LEFT JOIN ")
							.append("VW_SS_SOHEN SH ON ")
							.append("BS.cell_cd = SH.original_cell_cd ")
							.append("AND BS.kaisya_cd= SH.original_kaisya_cd LEFT JOIN ")
							.append("SSM_KANJO K ON ")
							.append("BS.system_kbn = K.system_kbn ")
							.append("AND BS.kanjo_cd = K.kanjo_cd ")
							.append("AND BS.kanjo_uchi_cd = K.kanjo_uchi_cd ")
							.append("AND BS.sateikaisya_cd = K.sateikaisya_cd ")
							.append("LEFT JOIN TM_TAIHI_TBL@VIR_SJLMA TT ON ")
							.append("TT.SYSTEM_KBN = BS.system_kbn ")
							.append("and TT.OFFICE_CD = BS.mise_cd ")
							.append("and TT.KIKAN_TORI_CD = BS.tori_cd_7 ")
							.append("and TT.DEL_FLAG = '0' ")
							.append("LEFT JOIN TM_TOGO_MST@VIR_SJLMA TM ON ")
							.append("TM.TOGO_TORI_CD = TT.TOGO_TORI_CD ")
							.append("LEFT JOIN ")
							.append("SSM_TAIRYU_KBN TK ")
							.append("ON BS.SYSTEM_KBN = TK.SYSTEM_KBN ")
							.append("AND BS.SATEIKAISYA_CD = TK.SATEIKAISYA_CD ")
							.append("AND BS.MISE_CD = TK.MISE_CD ")
							.append("AND BS.TAIRYU_TUKISU BETWEEN TK.TAIRYU_FROM AND TK.TAIRYU_TO ")
							.append("AND TK.SYORI_KBN = PG_SS_FUNCTION.SF_SS_KIJUNBITOSYORI_KBN('")
							.append(kijunbi_kbn)
							.append("') ")
							.append("LEFT JOIN ")
							.append("SSP_KBN KB ")
							.append("ON TRIM(KB.kbn_key) = 'tairyu_kbn' ")
							.append("AND TRIM(KB.system_kbn) = BS.system_kbn ")
							.append("AND TRIM(KB.lang_mode) = '")
							.append(sLangMode)
							.append("' AND TRIM(KB.kbn_val) = DECODE(BS.TAIRYU_TUKISU,NULL,'1',GREATEST(BS.TAIRYU_TUKISU,1000),'1',LEAST(BS.TAIRYU_TUKISU,-1000),'1',NVL(TK.tairyu_kbn,'1')) ")
							.append("WHERE ")
							.append("BS.tori_cd_5 ='")
							.append(kanjo_cd)
							.append("' AND BS.ym = '")
							.append(Function.removeDateSlash(ym))
							.append("' AND BS.sateikaisya_cd = '")
							.append(sateikaisya_cd)
							.append("' AND K.saiken_flg IN ('1','2','3','9') ")
							.append("ORDER BY BS.ka_cd ASC, BS.cell_cd ASC, BS.kanjo_cd ASC, BS.kanjo_uchi_cd ASC, BS.kingaku DESC");
				
			}else{
				String phase = cmnData.getPhase();
				String sLangMode = cmnData.getComLangMode();

				// 対象先選定以外は引当金ＢＳ明細をを参照する。
				//sql2 = new StringBuffer()
				//.append("select distinct ")
				//.append("HB.satei_anken_no,")
				//.append("HB.anken_no,")
				//.append("HB.anken_no_eda,")
				//.append("HB.system_kbn,")
				//.append("HB.tori_cd,")
				//.append("HB.cell_cd,")
				//.append("HB.kanjo_cd,")
				//.append("HB.kanjo_uchi_cd,")
				//.append("HB.shusi_dt,")
							//.append("HB.manki_dt,")
							//.append("HB.syori_dt,")
							//.append("HB.keiyaku_denpyo_no,")
							//.append("HB.kingaku,")
							//.append("HB.tuuka_cd,")
							//.append("SUBSTR(HB.tori_cd,1,7) AS tori_cd_7,")
							//.append("SH.cell_nm,")
							//.append("SH.bu_nm,")
							//.append("SH.seisiki_bumon_cd,")
							//.append("SH.bu_cd,")
							//.append("K.kanjo_nm,")
							//.append("K.kanjo_uchi_nm,")
							//.append("K.saiken_flg,")
				//.append("TM.business_nm_kj AS torihikisakimei2, ")
				//.append("TM.business_nm AS torihikisakimei_en2, ")
				//.append("TH.tairyu_kbn,")
				//.append("TH.hantei_jiyuu,")
				//.append("TH.tairyu_hantei ")
							//.append(",HY.DEL_FLG ")						/* No.845 */
							//.append(", HY.BUNSYO_NO ")					/* No.845 */
				//.append(", X.DEL_FLG ")							/* No.845 */
				//.append(", X.MAX_BUNSYO_NO BUNSYO_NO ")			/* No.845 */
							//.append(", BT.PHASE ")						/* No.845 */
				//.append(" ,SS.HOZEN, SS.SONOTAKAISYU, SS.RIKO_KENEN ") /* No.845 */
				//.append(", KB0.KBN_HYOUJI_VAL TAIRYU_KBN_VAL")	/* No.845 */
				//.append(", KB1.KBN_HYOUJI_VAL TAIRYU_HANTEI_VAL ")/* No.845 */
				//.append("FROM SST_TAIRYUHANTEI TH ")			/* No.845 */
				//.append("LEFT JOIN SSP_KBN KB0 ON ")			/* No.845 */
				//.append("KB0.KBN_KEY = 'tairyu_kbn' AND ")		/* No.845 */
				//.append("KB0.LANG_MODE = '")					/* No.845 */
				//.append(sLangMode)								/* No.845 */
				//.append("' AND KB0.KBN_VAL = TH.TAIRYU_KBN ")	/* No.845 */
				//.append("LEFT JOIN SSP_KBN KB1 ON ")			/* No.845 */
				//.append("KB1.KBN_KEY = 'tairyu_jdg' AND ")		/* No.845 */
				//.append("KB1.LANG_MODE = '")					/* No.845 */
				//.append(sLangMode)								/* No.845 */
				//.append("' AND KB1.KBN_VAL = TH.TAIRYU_HANTEI, ")/* No.845 */
				//.append("VW_SS_SOHEN SH,")
				//.append("SSM_KANJO K,")
				//.append("SST_HIKIATEBSMEISAI HB ")
				//.append("LEFT JOIN SSE_TAIHI TT ON ")
				//.append("TT.ym = HB.ym ")
				//.append("and TT.syori_kaisu = HB.syori_kaisu ")
				//.append("and TT.system_kbn = HB.system_kbn ")
				//.append("and TT.office_cd = HB.mise_cd ")
				//.append("and TT.kikan_tori_cd = HB.tori_cd_syusei_7 ")
				//.append("LEFT JOIN SST_SATEI SS ON ")			/* No.845 */
				//.append("HB.SATEI_ANKEN_NO = SS.ANKEN_NO AND ")	/* No.845 */
				//.append("SS.PHASE = '").append(phase).append("' ")/* No.845 */
							//.append("LEFT JOIN SST_BUNSYOTEMPU BT ON ")	/* No.845 */
							//.append("HB.ANKEN_NO = BT.ANKEN_NO ")			/* No.845 */
							//.append(createPhaseConditionStr(phase))		/* No.845 */
							//.append(" LEFT JOIN SSM_HOYUBUNSYO HY ON ")	/* No.845 */
							//.append("BT.BUNSYO_NO = hy.BUNSYO_NO ")		/* No.845 */
				//.append("LEFT JOIN (SELECT B0.ANKEN_NO, ")		/* No.845 */
				//.append("B0.ANKEN_NO_EDA, B0.MAX_BUNSYO_NO, ")	/* No.845 */
				//.append("HY0.DEL_FLG FROM (SELECT B.ANKEN_NO, ")/* No.845 */
				//.append("B.ANKEN_NO_EDA, ")						/* No.845 */
				//.append("MAX(B.BUNSYO_NO) MAX_BUNSYO_NO ")		/* No.845 */
				//.append("FROM SST_BUNSYOTEMPU B  ")				/* No.845 */
				//.append(createPhaseConditionStr(phase))			/* No.845 */
				//.append(" GROUP BY B.ANKEN_NO, B.ANKEN_NO_EDA ")/* No.845 */
				//.append(") B0 INNER JOIN SSM_HOYUBUNSYO HY0 ON ")/* No.845 */
				//.append("B0.MAX_BUNSYO_NO = HY0.BUNSYO_NO ) X ON ")/* No.845 */
				//.append("HB.ANKEN_NO = X.ANKEN_NO AND ")		/* No.845 */
				//.append("HB.ANKEN_NO_EDA = X.ANKEN_NO_EDA ")	/* No.845 */
				//.append("LEFT JOIN SSE_TOGO_MST TM ON ")
				//.append("TM.ym = TT.ym ")
				//.append("and TM.sikibetu_cd = TT.sikibetu_cd ")
				//.append("and TM.togo_tori_cd = TT.togo_tori_cd ")
				//.append("and TM.syori_kaisu = TT.syori_kaisu ")
				//.append("where ")
				//.append("HB.tori_cd_5 ='")
				//.append(kanjo_cd)
				//.append("' and HB.ym = '")
				//.append(Function.removeDateSlash(ym))
				//.append("' and HB.sateikaisya_cd = '");
		        			// No410, 2008/05/23, SJA渡辺, チャンピオン部からの遷移時には条件変更するように修正
		        			//if ("".equals(cmnData.getSatei_anken_no()) && GS.SYSTEM_CHAMPION.equals(cmnData.getOrgReturnId())) {
								//sql2.append(cmnData.getAnken_satei_kaisya_cd())
								//.append("' and HB.system_kbn ='")
								//.append(cmnData.getSystem_kbn())
								//.append("' and HB.mise_cd ='")
								//.append(cmnData.getAnken_mise_cd())
								//.append("' and HB.syori_kaisu ='")
								//.append(cmnData.getSyoriCnt());
								//} else {
								// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
								//sql2.append(sateikaisya_cd)
								//sql2.append(cmnData.getAnken_satei_kaisya_cd())
								//.append("' and HB.satei_anken_no ='")
								//.append(cmnData.getSatei_anken_no());
								//}
							//sql2.append("' and HB.cell_cd = SH.original_cell_cd")
							//.append(" and HB.kaisya_cd= SH.original_kaisya_cd")
							//.append(" and HB.system_kbn = K.system_kbn")
							//.append(" and HB.kanjo_cd = K.kanjo_cd")
							//.append(" and HB.kanjo_uchi_cd = K.kanjo_uchi_cd")
							//.append(" and HB.anken_no = TH.anken_no")
							//.append(" and HB.anken_no_eda = TH.anken_no_eda")
							//.append(" and K.saiken_flg in ('1','2','3','9')")
							//.append(" and TH.phase = (select MAX(phase) from SST_TAIRYUHANTEI where HB.anken_no = anken_no AND HB.anken_no_eda = anken_no_eda)")
							//.append(" ORDER BY HB.tori_cd, HB.cell_cd");
		        			
		        			sql2 = new StringBuffer()
		        			.append(" select distinct HB.satei_anken_no")
		        			// 課題No.185
		        			// 追加開始
		        			.append(" ,HB.satei_anken_no_eda")
		        			// 追加完了
		        			.append(" ,HB.anken_no")
		        			.append(" ,HB.anken_no_eda")
		        			.append(" ,HB.system_kbn")
		        			.append(" ,HB.tori_cd")
		        			.append(" ,HB.ka_cd")
		        			.append(" ,HB.cell_cd")
		        			.append(" ,HB.kanjo_cd")
		        			.append(" ,HB.kanjo_uchi_cd,")
		        			// 課題No.158
		        			// 追加開始
							.append("PG_SS_FUNCTION.SF_SS_ISLANG(TO_CHAR(HB.shusi_dt,'yyyy/mm/dd'),TO_CHAR(HB.shusi_dt,'mm/dd/yyyy'),'")
							.append(sLangMode) 
							.append("') AS shusi_dt,")
							.append("PG_SS_FUNCTION.SF_SS_ISLANG(TO_CHAR(HB.manki_dt,'yyyy/mm/dd'),TO_CHAR(HB.manki_dt,'mm/dd/yyyy'),'")
							.append(sLangMode) 
							.append("') AS manki_dt,")
							.append("PG_SS_FUNCTION.SF_SS_ISLANG(TO_CHAR(HB.syori_dt,'yyyy/mm/dd'),TO_CHAR(HB.syori_dt,'mm/dd/yyyy'),'")
							.append(sLangMode) 
							.append("') AS syori_dt")
							// 追加完了
							/*
		        			.append(" ,HB.shusi_dt")
		        			.append(" ,HB.manki_dt")
		        			.append(" ,HB.syori_dt")
		        			*/
		        			.append(" ,HB.keiyaku_denpyo_no")
		        			.append(" ,HB.kingaku")
		        			.append(" ,HB.tuuka_cd")
		        			.append(" ,SUBSTR(HB.tori_cd, 1, 7) AS tori_cd_7")
		        			.append(" ,SH.cell_nm")
		        			.append(" ,SH.bu_nm")
		        			.append(" ,SH.seisiki_bumon_cd")
		        			.append(" ,SH.bu_cd")
		        			.append(" ,K.kanjo_nm")
		        			.append(" ,K.kanjo_uchi_nm")
		        			.append(" ,K.saiken_flg")
		        			.append(" ,TM.business_nm_kj AS torihikisakimei2")
		        			.append(" ,TM.business_nm AS torihikisakimei_en2")
		        			.append(" ,TH.tairyu_kbn")
		        			.append(" ,TH.hantei_jiyuu")
		        			.append(" ,TH.tairyu_hantei")
		        			.append(" ,X.DEL_FLG")
		        			.append(" ,X.MAX_BUNSYO_NO BUNSYO_NO")
		        			.append(" ,SS.HOZEN")
		        			.append(" ,SS.SONOTAKAISYU")
		        			.append(" ,SS.RIKO_KENEN")
		        			.append(" ,KB0.KBN_HYOUJI_VAL TAIRYU_KBN_VAL")
		        			.append(" ,KB1.KBN_HYOUJI_VAL TAIRYU_HANTEI_VAL")
		        			.append(" FROM")
		        			.append(" SST_HIKIATEBSMEISAI HB")
							.append(" LEFT JOIN")
							.append(" SSM_TAIRYU_KBN TK")
							.append(" ON HB.SYSTEM_KBN = TK.SYSTEM_KBN")
							.append(" AND HB.SATEIKAISYA_CD = TK.SATEIKAISYA_CD")
							.append(" AND HB.MISE_CD = TK.MISE_CD")
							.append(" AND HB.TAIRYU_TUKISU BETWEEN TK.TAIRYU_FROM AND TK.TAIRYU_TO")
							.append(" AND TK.SYORI_KBN = PG_SS_FUNCTION.SF_SS_KIJUNBITOSYORI_KBN('")
							.append(kijunbi_kbn)
							.append("')")
		        			.append(" LEFT JOIN SSE_TAIHI TT")
		        			.append(" ON TT.ym = HB.ym")
		        			.append(" and TT.syori_kaisu = HB.syori_kaisu")
		        			.append(" and TT.system_kbn = HB.system_kbn")
		        			.append(" and TT.office_cd = HB.mise_cd")
		        			.append(" and TT.kikan_tori_cd = HB.tori_cd_syusei_7")
		        			.append(" LEFT JOIN SST_TAIRYUHANTEI TH")
		        			.append(" ON HB.anken_no = TH.anken_no")
		        			.append(" and HB.anken_no_eda = TH.anken_no_eda")
		        			.append(" LEFT JOIN VW_SS_SOHEN SH")
		        			.append(" ON HB.cell_cd = SH.original_cell_cd")
		        			.append(" and HB.kaisya_cd = SH.original_kaisya_cd")
		        			.append(" LEFT JOIN SSM_KANJO K")
		        			.append(" ON HB.system_kbn = K.system_kbn")
		        			.append(" and HB.kanjo_cd = K.kanjo_cd")
		        			.append(" and HB.kanjo_uchi_cd = K.kanjo_uchi_cd")
		        			.append(" and HB.sateikaisya_cd = K.sateikaisya_cd")
		        			.append(" LEFT JOIN SSP_KBN KB0")
		        			.append(" ON KB0.KBN_KEY = 'tairyu_kbn'")
		        			.append(" AND KB0.system_kbn = '01'")
		        			.append(" AND KB0.LANG_MODE = '")
		        			.append(sLangMode)
							.append("' AND TRIM(KB0.KBN_VAL) = DECODE(HB.TAIRYU_TUKISU,NULL,'1',GREATEST(HB.TAIRYU_TUKISU,1000),'1',LEAST(HB.TAIRYU_TUKISU,-1000),'1',NVL(TK.tairyu_kbn,'1'))")
		        			.append(" LEFT JOIN SSP_KBN KB1")
		        			.append(" ON KB1.KBN_KEY = 'tairyu_jdg'")
		        			.append(" AND KB1.system_kbn = '01'")
		        			.append(" AND KB1.LANG_MODE = '")
		        			.append(sLangMode)
		        			.append("' AND KB1.KBN_VAL = TH.TAIRYU_HANTEI")
		        			.append(" LEFT JOIN SST_SATEI SS")
		        			.append(" ON HB.SATEI_ANKEN_NO = SS.ANKEN_NO")
		        			.append(" AND SS.PHASE = '")
		        			.append(phase)
							.append("' LEFT JOIN (")
		        			.append(" SELECT B0.ANKEN_NO")
		        			.append(" ,B0.ANKEN_NO_EDA")
		        			.append(" ,B0.MAX_BUNSYO_NO")
		        			.append(" ,HY0.DEL_FLG")
		        			.append(" FROM (")
		        			.append(" SELECT B.ANKEN_NO")
		        			.append(" ,B.ANKEN_NO_EDA")
		        			.append(" ,MAX(B.BUNSYO_NO) MAX_BUNSYO_NO")
		        			.append(" FROM SST_BUNSYOTEMPU B ")
							.append(createPhaseConditionStr(phase))
		        			.append(" GROUP BY B.ANKEN_NO")
		        			.append(" ,B.ANKEN_NO_EDA) B0")
		        			.append(" INNER JOIN SSM_HOYUBUNSYO HY0")
		        			.append(" ON B0.MAX_BUNSYO_NO = HY0.BUNSYO_NO) X")
		        			.append(" ON HB.ANKEN_NO = X.ANKEN_NO")
		        			.append(" AND HB.ANKEN_NO_EDA = X.ANKEN_NO_EDA")
		        			.append(" LEFT JOIN SSE_TOGO_MST TM")
		        			.append(" ON TM.ym = TT.ym")
		        			.append(" and TM.sikibetu_cd = TT.sikibetu_cd")
		        			.append(" and TM.togo_tori_cd = TT.togo_tori_cd")
		        			.append(" and TM.syori_kaisu = TT.syori_kaisu")
		        			.append(" where HB.tori_cd_5 = '")
							.append(kanjo_cd)
		        			.append("' and HB.ym = '")
							.append(Function.removeDateSlash(ym))
		        			.append("' and HB.sateikaisya_cd = '");
							if ("".equals(cmnData.getSatei_anken_no()) && GS.OS5101.equals(cmnData.getOrgReturnId())) {
								sql2.append(cmnData.getAnken_satei_kaisya_cd())
								.append("' and HB.system_kbn ='")
								.append(cmnData.getSystem_kbn())
								.append("' and HB.mise_cd ='")
								.append(cmnData.getAnken_mise_cd())
								.append("' and HB.syori_kaisu ='")
								.append(cmnData.getSyoriCnt());
							} else {
								sql2.append(cmnData.getAnken_satei_kaisya_cd())
								.append("' and HB.satei_anken_no ='")
								.append(cmnData.getSatei_anken_no());
							}
		        			sql2.append("' and K.saiken_flg in ('1', '2', '3', '9')")
		        			.append(" and ((TH.phase = (")
		        			.append(" select MAX(phase)")
		        			.append(" from SST_TAIRYUHANTEI")
		        			.append(" where HB.anken_no = anken_no")
		        			.append(" AND HB.anken_no_eda = anken_no_eda)) OR (TH.phase IS NULL))")
		           			// 課題No.185
		        			// 追加開始
		        			//.append(" ORDER BY HB.ka_cd ASC, HB.cell_cd ASC, HB.kanjo_cd ASC, HB.kanjo_uchi_cd ASC, HB.kingaku DESC ");
		        			.append(" ORDER BY HB.ka_cd ASC, HB.cell_cd ASC, HB.kanjo_cd ASC, HB.kanjo_uchi_cd ASC, HB.kingaku DESC, HB.satei_anken_no_eda ASC ");
		        			// 追加完了
			}
			rs2 = sqlExec.execQuery(sql2.toString());
			ArrayList list = new ArrayList();
			int i = 0;
			// 課題No.158
			// 追加開始
			String sLangMode = cmnData.getComLangMode();
			// 追加完了
			while ( rs2.next() ) {
				HashMap map = new HashMap();
				
				if (!(GS.TAIRYU_SENTEITUIKA.equals(cmnData.getOrgReturnId()) 
						|| GS.SYSTEM_GOLF.equals(cmnData.getOrgReturnId())
						|| (GS.OS5101.equals(cmnData.getOrgReturnId()) &&
							!checkHikiateBSMeisai(cmnData)))) {
					map.put("satei_anken_no",rs2.getString("satei_anken_no"));
					map.put("anken_no",rs2.getString("anken_no"));
					map.put("anken_no_eda",rs2.getString("anken_no_eda"));
					//map.put("tairyu_kbn",rs2.getString("tairyu_kbn"));	/* No.845 */
					///////////////////////////////////////////
					//障害表：482,486
					//チェックイン日：2008/5/29
					//対応者：SJA中島
					//概要：改行コードをBRタグに置き換える。
					///////////////////////////////////////////
					map.put("hantei_jiyuu",rs2.getString("hantei_jiyuu"));
					map.put("tairyu_hantei",rs2.getString("tairyu_hantei"));
					/* No.845 1レコード毎にDBアクセスせず、はじめに情報を取得しておくよう修正
					map = getHyojiKbn(map,"tairyu_kbn",cmnData.getComLangMode(),(String)map.get("tairyu_kbn"));
					map = getHyojiKbn(map,"tairyu_jdg",cmnData.getComLangMode(),(String)map.get("tairyu_hantei"));
					*/
					map.put("tairyu_jdg", rs2.getString("tairyu_hantei_val"));/* No.845 */
					
					// 添付選択フラグをセット。
					/* No.845 1レコード毎にDBアクセスせず、はじめに情報を取得しておくよう修正 */
					//map = getTenpuFlg(map);								/* No.845 */
					if("0".equals(rs2.getString("del_flg"))){				/* No.845 */
						map.put("bunsyo_no",rs2.getString("bunsyo_no"));	/* No.845 */
					}														/* No.845 */
				}
				
				map.put("tairyu_kbn", rs2.getString("tairyu_kbn_val"));	/* No.845 */
				map.put("system_kbn",rs2.getString("system_kbn"));
				map.put("saiken_flg",rs2.getString("saiken_flg"));
				// 通貨コードを取得(modify tuuka_cd 2008/4/15 nakajima)
				map.put("tuuka_cd",Function.trim(rs2.getString("tuuka_cd")));
				
				map.put("keiyaku_denpyo_no",rs2.getString("keiyaku_denpyo_no"));
				
				map.put("cell_cd",rs2.getString("cell_cd"));
				map.put("kanjo_cd",rs2.getString("kanjo_cd"));
				map.put("kanjo_uchi_cd",rs2.getString("kanjo_uchi_cd"));
				// 課題No.158
				// 追加開始
				map.put("shusi_dt",Function.insertDateSlash(rs2.getString("shusi_dt"),sLangMode));
				map.put("manki_dt",Function.insertDateSlash(rs2.getString("manki_dt"),sLangMode));
				map.put("syori_dt",Function.insertDateSlash(rs2.getString("syori_dt"),sLangMode));
				/*
				map.put("shusi_dt",Function.insertDateSlash(rs2.getDate("shusi_dt")));
				map.put("manki_dt",Function.insertDateSlash(rs2.getDate("manki_dt")));
				map.put("syori_dt",Function.insertDateSlash(rs2.getDate("syori_dt")));
				*/
				// 追加完了
				
				// No786, 2008/06/05, SJA渡辺, 不要な改行が入るを修正
				//map.put("bu_nm",Function.stringCutterByteAddN(rs2.getString("bu_nm"),16));
				//map.put("cell_nm",Function.stringCutterByteAddN(rs2.getString("cell_nm"),16));
				map.put("bu_nm",rs2.getString("bu_nm"));
				map.put("cell_nm",rs2.getString("cell_nm"));
				map.put("kanjo_nm",rs2.getString("kanjo_nm"));
				map.put("kanjo_uchi_nm",rs2.getString("kanjo_uchi_nm"));
				map.put("kingaku",Function.format("###,###,###,##0",rs2.getDouble("kingaku")));
				
				map.put("tori_cd",rs2.getString("tori_cd"));
				map.put("tori_cd_7",rs2.getString("tori_cd_7"));
				map.put("seisiki_bumon_cd",rs2.getString("seisiki_bumon_cd"));
				map.put("bu_cd",rs2.getString("bu_cd"));
				
				map.put("torihikisakimei2",rs2.getString("torihikisakimei2"));
				map.put("torihikisakimei_en2",rs2.getString("torihikisakimei_en2"));
				
				
				// 障害票No388 2008/05/20 細野 遷移元ゴルフの追加
				/*if(GS.TAIRYU_SENTEITUIKA.equals(cmnData.getOrgReturnId())
				|| GS.SYSTEM_GOLF.equals(cmnData.getOrgReturnId()) ){
					map.put("keiyaku_denpyo_no",rs.getString("keiyku_denpyo_no"));
					
				}else{
					map.put("keiyaku_denpyo_no",rs.getString("keiyaku_denpyo_no"));
					map.put("satei_anken_no",rs.getString("satei_anken_no"));
					map.put("anken_no",rs.getString("anken_no"));
					map.put("anken_no_eda",rs.getString("anken_no_eda"));
					map.put("tairyu_kbn",rs.getString("tairyu_kbn"));
					map.put("hantei_jiyuu",Function.stringCutterByteAddN(rs.getString("hantei_jiyuu"),40));
					map.put("tairyu_hantei",rs.getString("tairyu_hantei"));
					map = getHyojiKbn(map,"tairyu_kbn",cmnData.getComLangMode(),(String)map.get("tairyu_kbn").toString());
					map = getHyojiKbn(map,"tairyu_jdg",cmnData.getComLangMode(),(String)map.get("tairyu_hantei"));
					// 添付選択フラグをセット。
					map = getTenpuFlg(map);
				}
					
				map.put("system_kbn",rs.getString("system_kbn"));
				map.put("cell_cd",rs.getString("cell_cd"));
				map.put("kanjo_cd",rs.getString("kanjo_cd"));
				map.put("kanjo_uchi_cd",rs.getString("kanjo_uchi_cd"));
				map.put("shusi_dt",Function.insertDateSlash(rs.getDate("shusi_dt")));
				map.put("manki_dt",Function.insertDateSlash(rs.getDate("manki_dt")));
				map.put("syori_dt",Function.insertDateSlash(rs.getDate("syori_dt")));
				map.put("kingaku",Function.format("###,###,###,##0",rs.getDouble("kingaku")));
				map.put("cell_nm",Function.stringCutterByteAddN(rs.getString("cell_nm"),16));
				map.put("bu_nm",Function.stringCutterByteAddN(rs.getString("bu_nm"),16));
				map.put("kanjo_nm",rs.getString("kanjo_nm"));
				map.put("kanjo_uchi_nm",rs.getString("kanjo_uchi_nm"));
				map.put("saiken_flg",rs.getString("saiken_flg"));

				
				// 通貨コードを取得(modify tuuka_cd 2008/4/15 nakajima)
				map.put("tuuka_cd",Function.trim(rs.getString("tuuka_cd")));*/

					
				list.add(i, map);
			    i++;   
			}
			// 明細件数に取得レコード数を設定
			form.setCnt_meisai(rs2.getMetaData().getColumnCount());
			
			return list;
			
		}finally{
			/* No.845
			if(rs1 != null){
				try{
					rs1.close();
				} catch(Exception e ){
					throw new SQLException(e.getMessage());
				}
			}
			*/
			if(rs2 != null){
				try{
					rs2.close();
				} catch(Exception e ){
					throw new SQLException(e.getMessage());
				}
			}
		}
	}

	/**
	 * 一時二次査定テーブルから、留保債務合計、保全、その他回収、履行請求懸念を取得し、FORMにセット
	 *
	 */
	/* No.845
	private void setSST_SATEI() throws SQLException{
		StringBuffer sql = new StringBuffer().append("select ")
											.append(" hozen,")
											.append(" sonotakaisyu,")
											.append(" riko_kenen ")
											.append(" from SST_SATEI ")
											.append(" where ")
											.append(" phase ='")
											.append(cmnData.getSatei_anken_no())
											.append("' and phase ='")
											.append(cmnData.getPhase())
											.append("'");
		ResultSet rs = null;
		try{
			rs = sqlExec.execQuery(sql.toString());
			while(rs.next()){
				form.setHozen(rs.getString("hozen"));
				form.setSonotakaisyu(rs.getString("sonotakaisyu"));
				form.setRiko_kenen(rs.getString("riko_kenen"));
			}
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
		}
	} */

	
	/**
	 * 通貨コードを取得し、mapにセットする。
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	/*  No.845
	private HashMap getTuuka_cd(HashMap map) throws SQLException{
		ResultSet rs = null;
		
		// Duns No
		String duns_no = cmnData.getDuns_no();
		
		StringBuffer sql = new StringBuffer()
							.append(" select ")
							.append(" DT.ISO_CURRENCY_NM ")
							.append(" from ")
							.append(" TM_DB_KIHON_TBL@VIR_SJLMA DC, ")
							.append(" TM_DB_CURENCY_MST@VIR_SJLMA DT ")
							.append(" where ")
							.append(" DC.DUNS_NO ='")
							.append(duns_no)
							.append("' and DC.CURRENCY_CD = DT.WB_CURRENCY_CD ");
		
		try{
			rs = sqlExec.execQuery(sql.toString());
			while(rs.next()){
				map.put("tuuka_cd",rs.getString("ISO_CURRENCY_NM"));
			}
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
		}
		return map;
	}
	*/

	/**
	 * 案件に紐付く添付ファイルが存在しているかチェックする。
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	/* No.845
	private HashMap getTenpuFlg(HashMap map) throws SQLException{
		ResultSet rs = null;
		String phase = cmnData.getPhase();
		
		StringBuffer sql = new StringBuffer()
							.append("select HY.del_flg,HY.bunsyo_no ")
							.append("from SSM_HOYUBUNSYO HY,")
							.append("SST_BUNSYOTEMPU BT ")
							.append("where BT.anken_no = '")
							.append(map.get("anken_no").toString())
							.append("' and BT.anken_no_eda ='")
							.append(map.get("anken_no_eda"))
		////////////////////////////////////////////////////////
		//障害票：593
		//チェックイン日：2008/6/5
		//対応者：SJA中島
		//概要：文書テーブルの文書添付フラグが1のものを取得するように修正。
		////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////
		//障害票：593
		//チェックイン日：2008/6/8
		//対応者：SJA中島
		//概要：文書テーブルの文書添付フラグ削除による対応
		////////////////////////////////////////////////////////
							.append("'")
							.append(" and BT.bunsyo_no = HY.bunsyo_no");
		
		// 遷移元の画面フェーズ以前のフェーズ判定
		// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス65を定数化
		if (phase.equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)) {
			sql.append(" and BT.phase IN ('10','20','30','40','50','60','65')");
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス60を定数化
		} else if (phase.equals(GS.PHASE_NIJI_SATEI)) {
			sql.append(" and BT.phase IN ('10','20','30','40','50','60')");
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
		} else if (phase.equals(GS.PHASE_ICHIJI_SATEI_KENSYO)) {
			sql.append(" and BT.phase IN ('10','20','30','40','50')");
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
		} else if (phase.equals(GS.PHASE_ICHIJI_SATEI)) {
			sql.append(" and BT.phase IN ('10','20','30','40')");
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス30を定数化
		} else if (phase.equals(GS.PHASE_TAISHOSAKI_SENTEI)) {
			sql.append(" and BT.phase IN ('10','20','30')");
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス20を定数化
		} else if (phase.equals(GS.PHASE_TAIRYU_HANTEI_KENSHO)) {
			sql.append(" and BT.phase IN ('10','20')");
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス10を定数化
		} else if (phase.equals(GS.PHASE_TAIRYU_HANTEI)) {
			sql.append(" and BT.phase IN ('10')");
		}
		
		
		try{
			rs = sqlExec.execQuery(sql.toString());
			
			while(rs.next()){
				if("0".equals(rs.getString("del_flg"))){
					map.put("bunsyo_no",rs.getString("bunsyo_no"));
				}
			}
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
		}
		return map;
	}*/
	
	/**
	 * 区分テーブルから情報取得。
	 * @param map
	 * @param key
	 * @param val
	 * @return
	 * @throws SQLException
	 */
	/* No.845
	private HashMap getHyojiKbn(HashMap map,String key,String mode,String val) throws SQLException {

		ResultSet rs = null;

		// 表示区分を取得するためのSQL文を作成。
		StringBuffer sql5 = new StringBuffer()
							.append("SELECT ")
							.append("KB.KBN_HYOUJI_VAL ")
							.append("FROM SSP_KBN KB ")
							.append("WHERE ")
							.append("KB.KBN_KEY='")
							.append(key)
							.append("' and KB.LANG_MODE='")
							.append(mode)
							.append("' and KB.KBN_VAL='")
							.append(val)
							.append("'");
		try{
			rs = sqlExec.execQuery(sql5.toString());					
			while ( rs.next() ) {
				//map.put(key,Function.stringCutterByteAddN(rs.getString("KBN_HYOUJI_VAL"),10));
				map.put(key,rs.getString("KBN_HYOUJI_VAL"));
			}
		} finally {
			if(rs != null){
				try{
					// Resultset close
					rs.close();
				} catch(Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
		return map;
	}
	*/

	/**
	 * 件数セレクトボックス値設定
	 */
	public void setShowKbn() throws SQLException {
		
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer().append(this.getKbnSql("show",cmnData.getComLangMode()));
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			
		    LinkedHashMap showKbn = new LinkedHashMap(getRsCount(rs));
		    
			while ( rs.next() ) {
			    
			    // 勘定科目に表示する項目の作成
			    String kbn_val = Function.trim(rs.getString("kbn_val"));
			    String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
			    
			    showKbn.put(kbn_hyouji_val, kbn_val );
			} // while
			
			form.setShowList(showKbn);
			
		} finally {
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
	}

	/**
	 * フェーズ毎の検索条件文字列作成.
	 * <p>
	 * フェーズ毎に異なる検索条件文字列を作成し、リターンする。
	 * </p>
	 * @param sPhase 対象となるフェーズ
	 * @return 検索条件文字列
	 */
	private String createPhaseConditionStr(String sPhase) {					/* No.845 */

		StringBuffer oConditionBuf = new StringBuffer(32);					/* No.845 */

		if (GS.PHASE_KUREMU_SAIKEN_SAISETTEI.equals(sPhase)) {				/* No.845 */
			oConditionBuf.append(" WHERE B.PHASE IN ('10','20','30','40','50','60','65') ");/* No.845 */
		} else if (GS.PHASE_NIJI_SATEI.equals(sPhase)) {					/* No.845 */
			oConditionBuf.append(" WHERE B.PHASE IN ('10','20','30','40','50','60') ");/* No.845 */
		} else if (GS.PHASE_ICHIJI_SATEI_KENSYO.equals(sPhase)) {			/* No.845 */
			oConditionBuf.append(" WHERE B.PHASE IN ('10','20','30','40','50') ");/* No.845 */
		} else if (GS.PHASE_ICHIJI_SATEI.equals(sPhase)) {					/* No.845 */
			oConditionBuf.append(" WHERE B.PHASE IN ('10','20','30','40') ");/* No.845 */
		} else if (GS.PHASE_TAISHOSAKI_SENTEI.equals(sPhase)) {				/* No.845 */
			oConditionBuf.append(" WHERE B.PHASE IN ('10','20','30') ");		/* No.845 */
		} else if (GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(sPhase)) {			/* No.845 */
			oConditionBuf.append(" WHERE B.PHASE IN ('10','20') ");			/* No.845 */
		} else if (GS.PHASE_TAIRYU_HANTEI.equals(sPhase)) {					/* No.845 */
			oConditionBuf.append(" WHERE B.PHASE = '10' ");					/* No.845 */
		}																	/* No.845 */

		return oConditionBuf.toString();									/* No.845 */
	}																		/* No.845 */

	/**
	 * 引当金BS明細にデータが存在するかチェックします
	 * @return true:引当金BS明細に存在する false:引当金BS明細に存在しない
	 */
	private boolean checkHikiateBSMeisai(SessionDataZen cmnData) throws SQLException{
		ResultSet rs = null;
		boolean rtn_val = false;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("COUNT(SATEI_ANKEN_NO) as CNT ");
		sql.append("FROM SST_HIKIATEBSMEISAI ");
		sql.append("WHERE SYSTEM_KBN='");
		sql.append(cmnData.getSystem_kbn());
		sql.append("' AND SATEIKAISYA_CD='");
		sql.append(cmnData.getAnken_satei_kaisya_cd());
		sql.append("' AND YM='");
		sql.append(cmnData.getYm());
		sql.append("' AND MISE_CD='");
		sql.append(cmnData.getAnken_mise_cd());
		sql.append("' AND TORI_CD_5='");
		sql.append(cmnData.getKanjo_cd());
		sql.append("'");
		
		try{
			rs = sqlExec.execQuery(sql.toString());
			rs.next();
			if(rs.getInt("CNT")!=0){
				rtn_val = true;
			}
			return rtn_val;
		} finally {
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
	}
	/**
	 * 区分値取得SQL取得
	 */
	private String getKbnSql(String key,String langMode) {
		
		StringBuffer sql = new StringBuffer().append("SELECT KB.kbn_hyouji_val,")
											.append("KB.kbn_val")
											.append(" FROM SSP_KBN KB")
											.append(" WHERE KB.kbn_key='")
											.append(key)
											.append("' and KB.lang_mode='")
											.append(langMode)
											.append("' and KB.system_kbn ='")
											.append(cmnData.getSystem_kbn())
											.append("' ORDER BY KB.kbn_order ASC");
		
		return sql.toString();
	}
	
	/**
	 * Resultsetの件数取得<br>
	 * 
	 * @param ResultSet
	 * @return int
	 */
	protected int getRsCount(ResultSet rs) {
	    try {
	        rs.last();
	        int count = rs.getRow();
	        rs.beforeFirst();
		    return count;
	    } catch(SQLException e) {
	        return 0;
	    }
	}
}
