/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/5/19		SSC 			1.5次版機能組込 
002		2009/10/20		SSC				課題No.60 引当金検証時の取引先区分・債権区分設定
003		2009/10/22		SSC				課題No.73 取引先区分プルダウン設定値を2.0次に合わせる
004		2009/12/1		SSC				課題No.158 年月表示(英語版)を(MM/YYYY)に対応
******************************************************************************/
package app.common.dbAcc;

import app.SessionDataZen;
import app.common.form.KensyoSyokaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 引当金検証タブDBアクセスクラス
 */
public class KensyoSyokaiDbAcc extends CommonDbAcc {

	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;		// ＡＰＰコンテキスト

	private SessionDataZen cmnData = null;	// 共通セッションデータ
	private KensyoSyokaiForm form=null;
	
	// 受取手形
	private String UKETORI_TEGATA="01";
	// 輸出受取手形
	private String YUSYUTSU_UKETORI="02";
	// 売掛金
	private String URIKAKE="03";
	// 取引前渡金
	private String TORI_MAE="04";
	// 05:立替金
	private String TATEKAE="05";
	//06:未収入金
	private String MISYUNYU="06";
	//07:未収収益
	private String MISYUSYU="07";
	//08:短期貸付金
	private String TANKI="08";
	//09:差入保証金
	private String SASHIIRE="09";
	//10:仮払金
	private String KARIHARAI="10";
	//11:長期貸付金
	private String TYOKI="11";
	//12:その他投資
	private String SONOTA="12";
	//14:保証債務
	private String HOSYO="14";
	//15:既引当金
	private String KIBIKIATE="15";
	//16:固定化営業債権
	private String KOTEI="16";
	// 査定案件No
	private String satei_anken_no;
	// 基準年月
	private String kijun_ym;
	// 査定会社コード
	private String satei_kaisya_cd;
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public KensyoSyokaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) throws SQLException {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//ビーン取得
		cmnData = appContext.getCMNZen();
		form = (KensyoSyokaiForm)appContext.getActionForm();
		
		// 査定案件No
		satei_anken_no = cmnData.getSatei_anken_no();
		
		// 査定会社コード
		/*if(!GS.EMPTY_CHARCTER.equals(Function.trim(cmnData.getSelectSateiKaishaCd()))){
			satei_kaisya_cd = cmnData.getSelectSateiKaishaCd();
		}else{
			satei_kaisya_cd = cmnData.getComSateiKaishaCd();
		}*/
		// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
		satei_kaisya_cd = cmnData.getAnken_satei_kaisya_cd();
		
		// 基準年月
		kijun_ym = Function.removeDateSlash(cmnData.getYm());
		
		// 課題No.158対応
		// 追加開始
		//form.setKijun_ym(kijun_ym);
		form.setKijun_ym(Function.insertYmSlash(kijun_ym,cmnData.getComLangMode()));
		// 追加完了
	}
	
	/**
	 * 初回月・中間月検索SQL実行処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void execute() throws SQLException {
		///////////////////////////////////////
		//障害票：465
		//チェックイン日：2008/5/25
		//対応者：上田
		//概要：ResultSetの循環使用対応
		////////////////////////////////////////
//		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		// 基準年
		String kijun_year = kijun_ym.substring(0,4);
		// 基準月
		String kijun_month = kijun_ym.substring(4,6);
		
		// 査定年月(初回月)
		StringBuffer satei_ym_syokai = new StringBuffer();
		// 査定年月(中間月)
		StringBuffer satei_ym_chukan = new StringBuffer();
		
		// 初回月・中間月査定案件No
		String satei_anken_no_syokai = null;
		
		//TODO テスト
		//cmnData.setSystem_kbn("01");
		//cmnData.setComSateiKaishaCd("SJ");
		//cmnData.setMise_cd("NIC");
		
		// 初回月・中間月の年月を取得。
		
		StringBuffer sql1 = new StringBuffer()
		.append(" select distinct ")
		.append(" SK.kikan, ")
		.append(" SK.SYOKAI_ZUKI, ")
		.append(" SK.CYUUKAN_ZUKI, ")
		//障害No.CO001対応
		//追加開始
		.append(" SK.hanki_sihanki_kbn ")
		//追加完了
		.append(" from ")
		.append(" SSM_SYORIZUKI SK, ")
		.append(" SST_SATEI_STAT ST ")
		.append(" where ")
		.append(" SK.kaisha_cd = ST.mise_cd || '0' ")
		//要件No.四-16
		//追加開始
		.append(" and ST.hanki_sihanki_kbn = SK.hanki_sihanki_kbn ")
		//追加完了
		.append(" and ST.anken_no = '")
		.append(satei_anken_no)
		// No768, 2008/06/10, SJA渡辺, 帳票と初回月、中間月の取り方を合わせるように修正
		.append("' and (SK.syokai_zuki ='")
		.append(kijun_month)
		.append("' or SK.cyuukan_zuki ='")
		.append(kijun_month)
		.append("' or SK.saisyu_zuki ='")
		.append(kijun_month)
		.append("')");
		
		
		try{
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// 初回月・中間月の月をセット。
//			rs = sqlExec.execQuery(sql1.toString());
//			while(rs.next()){
//				if("2".equals(rs.getString("kikan"))){
//					//下半期なので、初回月の年を一年戻す。
//					int syokai_year = Function.getValueOfInt(kijun_year) -1;
//					satei_ym_syokai.append(Function.getStringOfInt(syokai_year)).append(rs.getString("SYOKAI_ZUKI"));
//				}else{
//					satei_ym_syokai.append(kijun_year).append(rs.getString("SYOKAI_ZUKI"));
//				}
//				satei_ym_chukan.append(kijun_year).append(rs.getString("CYUUKAN_ZUKI"));
//			}
			rs1 = sqlExec.execQuery(sql1.toString());
			while(rs1.next()){
				//障害No.CO001対応
				//修正開始
				if("2".equals(rs1.getString("kikan")) && "1".equals(rs1.getString("hanki_sihanki_kbn"))){
				//修正完了
					//下半期なので、初回月の年を一年戻す。
					int syokai_year = Function.getValueOfInt(kijun_year) -1;
					satei_ym_syokai.append(Function.getStringOfInt(syokai_year)).append(rs1.getString("SYOKAI_ZUKI"));
				}else{
					satei_ym_syokai.append(kijun_year).append(rs1.getString("SYOKAI_ZUKI"));
				}
				satei_ym_chukan.append(kijun_year).append(rs1.getString("CYUUKAN_ZUKI"));
			}
			// 課題No.60 引当金検証時の取引先区分・債権区分設定
			// 削除開始
			// 初回月・中間月の査定案件No、システム区分、を取得。
			/*StringBuffer sql2 = new StringBuffer()
								.append(" select ")
								.append(" distinct HH.satei_anken_no,HH.ym ")
								.append(" from ")
								.append(" SST_HIKIATEHANTEI HH, ")
								.append(" SST_KENSYOUSUM HS ")
								.append(" where ")
								.append(" HH.system_kbn = '")
								.append(cmnData.getSystem_kbn())
								.append("' and HH.sateikaisya_cd = '")
								.append(satei_kaisya_cd)
								.append("' and HH.mise_cd = '")
								.append(cmnData.getMise_cd())
								.append("' and HH.ym in ('")
								.append(satei_ym_syokai.toString())
								.append("','")
								.append(satei_ym_chukan.toString())
								.append("') and HH.syori_kaisu = '0' ") // 初回月・中間月は処理回数0で固定
								.append(" and HH.tori_cd = '")
								.append(cmnData.getKanjo_cd())
								.append("'")
								//要件No.四-16
								//追加開始
								.append(" and HH.hanki_sihanki_kbn = HS.hanki_sihanki_kbn ")
								//追加完了
								.append(" order by HH.ym desc");
			// 初回月・中間月の査定案件No、システム区分、を取得。
			StringBuffer sql2 = new StringBuffer()
								.append(" select ")
								.append(" distinct HH.satei_anken_no,HH.ym ")
								.append(" from ")
								.append(" SST_HIKIATEHANTEI HH, ")
								.append(" SST_KENSYOUSUM HS ")
								.append(" where ")
								.append(" HH.system_kbn = '")
								.append(cmnData.getSystem_kbn())
								.append("' and HH.sateikaisya_cd = '")
								.append(satei_kaisya_cd)
								.append("' and HH.mise_cd = '")
								.append(cmnData.getMise_cd())
								.append("' and HH.ym in ('")
								.append(satei_ym_syokai.toString())
								.append("','")
								.append(satei_ym_chukan.toString())
								.append("') and HH.syori_kaisu = '0' ") // 初回月・中間月は処理回数0で固定
								.append(" and HH.tori_cd = '")
								.append(cmnData.getKanjo_cd())
								.append("'")
								//要件No.四-16
								//追加開始
								.append(" and HH.hanki_sihanki_kbn = HS.hanki_sihanki_kbn ")
								//追加完了
								.append(" order by HH.ym desc");*/
			// 削除完了
			// 追加開始
			StringBuffer sql2 = new StringBuffer().append("SELECT SS.anken_no AS satei_anken_no,")
									.append("SS.ym")
									.append(" FROM SST_SATEI_STAT SS ")
									.append(" WHERE SS.system_kbn = '" )
									.append(cmnData.getSystem_kbn())
									.append("' and SS.satei_kaisha_cd = '")
									.append(satei_kaisya_cd)
									.append("' and SS.mise_cd='" )
									.append(cmnData.getMise_cd())
									.append("' and SS.syori_kaisu = '0'")
									.append(" and SS.phase = '60'")
									.append(" and SS.status = '40'")
									.append(" and SS.kikan_tori_cd = '")
									.append(cmnData.getKanjo_cd())
									.append("' and SS.hanki_sihanki_kbn = '")
									.append(cmnData.getHanki_sihanki_kbn())
									.append("' and SS.ym IN ('")
									.append(satei_ym_syokai.toString())
									.append("','")
									.append(satei_ym_chukan.toString())
									.append("') ORDER BY SS.ym DESC");
			// 追加完了
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// 初回月・中間月査定案件Noセット。
//			rs = sqlExec.execQuery(sql2.toString());
//			if(rs.next()){
//				satei_anken_no_syokai = rs.getString("satei_anken_no");
//				form.setSatei_ym(Function.trim(rs.getString("ym")));
//			}
			rs2 = sqlExec.execQuery(sql2.toString());
			if(rs2.next()){
				satei_anken_no_syokai = rs2.getString("satei_anken_no");
				// 課題No.158
				// 追加開始
				//form.setSatei_ym(Function.trim(rs2.getString("ym")));
				form.setSatei_ym(Function.insertYmSlash(rs2.getString("ym"),cmnData.getComLangMode()));
				// 追加完了

			}
			// 課題No.158
			// 追加開始
			String satei_ym = Function.trim(rs2.getString("ym"));
			// 追加完了
			
			// 引当金判定表示用テーブルから表示項目取得。
			StringBuffer sql3 = new StringBuffer()
								.append(" select ")
								.append(" HH.kanjo_hyouji_kbn, ")
								.append(" HH.tuuka_cd, ")
								.append(" HH.kingaku, ")
								.append(" HH.ym ")
								.append(" from ")
								.append(" SST_HIKIATEHANTEI HH ")
								.append(" where ")
								.append(" HH.satei_anken_no = '")
								.append(satei_anken_no_syokai)
								.append("' and HH.system_kbn = '")
								.append(cmnData.getSystem_kbn())
								.append("' and HH.sateikaisya_cd = '")
								.append(satei_kaisya_cd)
								.append("' and HH.mise_cd = '")
								.append(cmnData.getMise_cd())
								.append("' and HH.ym = '")
								// 課題No.158
								// 追加開始
								//.append(form.getSatei_ym())
								.append(satei_ym)
								// 追加完了
								.append("' and HH.syori_kaisu = '0' ") // 初回月・中間月は処理回数0で固定
								.append(" and HH.tori_cd = '")
								.append(cmnData.getKanjo_cd())
								.append("' and HH.kanjo_hyouji_kbn in(")
								.append("'01','02','03','04','05','06','07','08','09','10','11','12','14','15','16'")
								.append(")");
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// 初回月・中間月表示項目取得
//			rs = sqlExec.execQuery(sql3.toString());
//			while(rs.next()){
//				// add tuuka_cd (2008/4/15 nakajima)
//				form.setTuuka_cd_1(Function.trim(rs.getString("tuuka_cd")));
//				
//				
//				String kingaku = rs.getString("kingaku");
//				String kbn = rs.getString("kanjo_hyouji_kbn");
			rs3 = sqlExec.execQuery(sql3.toString());
			while(rs3.next()){
				// add tuuka_cd (2008/4/15 nakajima)
				form.setTuuka_cd_1(Function.trim(rs3.getString("tuuka_cd")));
				
				
				String kingaku = rs3.getString("kingaku");
				String kbn = rs3.getString("kanjo_hyouji_kbn");
				
				if(UKETORI_TEGATA.equals(kbn)){
					form.setUketoritegata_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (YUSYUTSU_UKETORI.equals(kbn)){
					form.setYusyutu_uketoritegata_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (URIKAKE.equals(kbn)){
					form.setUrikakekin_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (TORI_MAE.equals(kbn)){
					form.setTorihikimaetokin_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (TATEKAE.equals(kbn)){
					form.setTatekaekin_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (MISYUNYU.equals(kbn)){
					form.setMisyunyukin_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (MISYUSYU.equals(kbn)){
					form.setMisyusyueki_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (TANKI.equals(kbn)){
					form.setTanki_kashitsukekin_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (SASHIIRE.equals(kbn)){
					form.setSashiire_hosyokin_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (KARIHARAI.equals(kbn)){
					form.setKaribaraikin_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (TYOKI.equals(kbn)){
					form.setTyoki_kashitsukekin_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (SONOTA.equals(kbn)){
					form.setSonota_toshi_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (HOSYO.equals(kbn)){
					form.setHosyosaimu_gokei_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (KIBIKIATE.equals(kbn)){
					form.setKibikiatekin_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (KOTEI.equals(kbn)){
					form.setKoteika_eigyosaiken_1(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}
			}

			double ipan_saimukei_1 = 0;
			ipan_saimukei_1 =
							Function.getValueOfDoubleC(form.getUketoritegata_1())+			// 受取手形
							Function.getValueOfDoubleC(form.getYusyutu_uketoritegata_1())+	// 輸出受取手形
							Function.getValueOfDoubleC(form.getUrikakekin_1())+				// 売掛金
							Function.getValueOfDoubleC(form.getTorihikimaetokin_1())+		// 取引前渡金
							Function.getValueOfDoubleC(form.getTatekaekin_1())+				// 立替金
							Function.getValueOfDoubleC(form.getMisyunyukin_1())+			// 未収入金
							Function.getValueOfDoubleC(form.getMisyusyueki_1())+			// 未収収益
							Function.getValueOfDoubleC(form.getTanki_kashitsukekin_1())+	// 短期貸付金
							Function.getValueOfDoubleC(form.getSashiire_hosyokin_1())+		// 差入保証金
							Function.getValueOfDoubleC(form.getKaribaraikin_1())+			// 仮払金
							Function.getValueOfDoubleC(form.getTyoki_kashitsukekin_1())+	// 長期貸付金
							Function.getValueOfDoubleC(form.getSonota_toshi_1());			// その他投資
			// 一般債権計
			form.setIpan_saimukei_1(Function.format("##,###,###,###,##0.##",ipan_saimukei_1));
			
			double saiken_zankei_1 = 0;
			saiken_zankei_1 = ipan_saimukei_1 + Function.getValueOfDoubleC(form.getKoteika_eigyosaiken_1());
			
			// 債権残高計
			form.setSaiken_zankei_1(Function.format("##,###,###,###,##0.##",saiken_zankei_1));			
			
			// 一次二次査定テーブルから表示項目取得。
			HashMap map = getSST_SATEI(satei_anken_no_syokai,null);//仮基準日データ取得
			form.setRyuhosaimu_1((String)map.get("ryuhosaimu"));
			form.setOth_ryuhosaimu_1((String)map.get("oth_ryuhosaimu"));
			form.setHozen_1((String)map.get("hozen"));
			form.setSonotakaisyu_1((String)map.get("sonotakaisyu"));
			form.setRiko_kenen_1((String)map.get("riko_kenen"));
			form.setTuika_hikiate_1((String)map.get("tuika_hikiate"));
			form.setSaiken_kbn((String)map.get("saiken_kbn_nm"));
			form.setTori_kbn((String)map.get("tori_kbn"));

			// 留保債務計
			double ryuhosaimu = Function.getValueOfDoubleC(form.getRyuhosaimu_1());
			double ryuhosaimu3 = Function.getValueOfDoubleC(form.getOth_ryuhosaimu_1());
			double total = ryuhosaimu + ryuhosaimu3;
			
			///////////////////////////////////////
			//障害票：803
			//チェックイン日：2008/6/9
			//対応者：中島
			//概要：留保債務計が債権残高合計より大きい場合で、かつ債権残高がマイナスの場合0をセットする。
			////////////////////////////////////////
			// 債権残高合計より大きな額か判定
			if (total > saiken_zankei_1) {
				if(saiken_zankei_1 > 0){
					form.setRyuhosaimu_kei_1(Function.format("##,###,###,###,##0.##",saiken_zankei_1));
				}else{
					form.setRyuhosaimu_kei_1(Function.format("##,###,###,###,##0.##",0));
				}
			} else {
				form.setRyuhosaimu_kei_1(Function.format("###,###,###,###,##0.##",total));
			}

			// 引当対象金額
			double hikiate_taisyo = Function.getValueOfDoubleC(form.getSaiken_zankei_1()) - 	// 債権残高合計①
									 Function.getValueOfDoubleC(form.getRyuhosaimu_kei_1()) - 	// 留保債務計②
									 Function.getValueOfDoubleC(form.getHozen_1()) -			// 保全③
									 Function.getValueOfDoubleC(form.getSonotakaisyu_1()) +		// その他回収④
									 Function.getValueOfDoubleC(form.getRiko_kenen_1()) -		// 履行請求懸念⑤
									 Function.getValueOfDoubleC(form.getKibikiatekin_1());		// 既引当金⑥		

			form.setHikiate_taisyokingaku_1(Function.format("###,###,###,###,##0.##",hikiate_taisyo));
			
			// コメントテーブルから表示項目取得。
			form.setHikiate_cmt(getSST_COMMENT(satei_anken_no_syokai,"80"));
			
			// 最終月のデータを取得
			execute_saisyu(); // 1回目(仮〆)
			
		}finally{
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
//			if(rs != null){
//				try{
//					rs.close();
//				}catch(Exception e){
//					// 処理なし。
//					throw new SQLException(e.getMessage());
//				}
//			}
			if(rs1 != null){
				try{
					rs1.close();
				}catch(Exception e){
					// 処理なし。
					throw new SQLException(e.getMessage());
				}
			}
			if(rs2 != null){
				try{
					rs2.close();
				}catch(Exception e){
					// 処理なし。
					throw new SQLException(e.getMessage());
				}
			}
			if(rs3 != null){
				try{
					rs3.close();
				}catch(Exception e){
					// 処理なし。
					throw new SQLException(e.getMessage());
				}
			}
		}
		
	}
	
	/**
	 * 最終月のデータを取得。
	 * @throws SQLException
	 */
	public void execute_saisyu() throws SQLException {
		ResultSet rs = null;
		String syori_cnt = form.getHyoji();

		// 障害票：413 2008/5/20 細野 基準日表示対応
		cmnData.setSyoriCnt(syori_cnt);
		
		// 一旦Formに設定している最終月のデータを初期化する。
			form.setUketoritegata_2("");
			form.setYusyutu_uketoritegata_2("");
			form.setUrikakekin_2("");
			form.setTorihikimaetokin_2("");
			form.setTatekaekin_2("");
			form.setMisyunyukin_2("");
			form.setMisyusyueki_2("");
			form.setTanki_kashitsukekin_2("");
			form.setSashiire_hosyokin_2("");
			form.setKaribaraikin_2("");
			form.setTyoki_kashitsukekin_2("");
			form.setSonota_toshi_2("");
			form.setHosyosaimu_gokei_2("");
			form.setKibikiatekin_2("");
			form.setKoteika_eigyosaiken_2("");
			form.setHikiatekin_hosei("");
		
		// 引当金検証サマリテーブルから表示項目取得。
		StringBuffer sql4 = new StringBuffer()
							.append(" select ")
							.append(" HS.kanjo_hyouji_kbn, ")
							.append(" HS.tuuka_cd, ")		// add tuuka_cd (2008/4/15 nakajima)
							.append(" HS.kingaku ")
							.append(" from ")
							.append(" SST_KENSYOUSUM HS ")
							.append(" where ")
							.append(" HS.system_kbn = '")
							.append(cmnData.getSystem_kbn())
							.append("' and HS.sateikaisya_cd = '")
							.append(satei_kaisya_cd)
							.append("' and HS.mise_cd = '")
							.append(cmnData.getMise_cd())
							.append("' and HS.ym = '")
							.append(kijun_ym)
							.append("' and HS.syori_kaisu = '") 
							.append(syori_cnt)
							.append("' and HS.tori_cd = '")
							.append(cmnData.getKanjo_cd())
							.append("' and HS.kanjo_hyouji_kbn in(")
							.append("'01','02','03','04','05','06','07','08','09','10','11','12','14','15','16'")
							.append(")");
		
		try{
			
			// 最終月表示項目取得
			rs = sqlExec.execQuery(sql4.toString());
			while(rs.next()){
				// add tuuka_cd (2008/4/15 nakajima)
				form.setTuuka_cd_2(Function.trim(rs.getString("tuuka_cd")));
				
				String kingaku = rs.getString("kingaku");
				String kbn = rs.getString("kanjo_hyouji_kbn");
				
				if(UKETORI_TEGATA.equals(kbn)){
					form.setUketoritegata_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (YUSYUTSU_UKETORI.equals(kbn)){
					form.setYusyutu_uketoritegata_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (URIKAKE.equals(kbn)){
					form.setUrikakekin_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (TORI_MAE.equals(kbn)){
					form.setTorihikimaetokin_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (TATEKAE.equals(kbn)){
					form.setTatekaekin_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (MISYUNYU.equals(kbn)){
					form.setMisyunyukin_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (MISYUSYU.equals(kbn)){
					form.setMisyusyueki_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (TANKI.equals(kbn)){
					form.setTanki_kashitsukekin_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (SASHIIRE.equals(kbn)){
					form.setSashiire_hosyokin_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (KARIHARAI.equals(kbn)){
					form.setKaribaraikin_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (TYOKI.equals(kbn)){
					form.setTyoki_kashitsukekin_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (SONOTA.equals(kbn)){
					form.setSonota_toshi_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (HOSYO.equals(kbn)){
					form.setHosyosaimu_gokei_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (KIBIKIATE.equals(kbn)){
					form.setKibikiatekin_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}else if (KOTEI.equals(kbn)){
					form.setKoteika_eigyosaiken_2(
							Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
				}
			}

			double ipan_saimukei_2 = 0;
			ipan_saimukei_2 =
							Function.getValueOfDoubleC(form.getUketoritegata_2())+			// 受取手形
							Function.getValueOfDoubleC(form.getYusyutu_uketoritegata_2())+	// 輸出受取手形
							Function.getValueOfDoubleC(form.getUrikakekin_2())+				// 売掛金
							Function.getValueOfDoubleC(form.getTorihikimaetokin_2())+		// 取引前渡金
							Function.getValueOfDoubleC(form.getTatekaekin_2())+				// 立替金
							Function.getValueOfDoubleC(form.getMisyunyukin_2())+			// 未収入金
							Function.getValueOfDoubleC(form.getMisyusyueki_2())+			// 未収収益
							Function.getValueOfDoubleC(form.getTanki_kashitsukekin_2())+	// 短期貸付金
							Function.getValueOfDoubleC(form.getSashiire_hosyokin_2())+		// 差入保証金
							Function.getValueOfDoubleC(form.getKaribaraikin_2())+			// 仮払金
							Function.getValueOfDoubleC(form.getTyoki_kashitsukekin_2())+	// 長期貸付金
							Function.getValueOfDoubleC(form.getSonota_toshi_2());			// その他投資
			// 一般債権計
			form.setIpan_saimukei_2(Function.format("##,###,###,###,##0.##",ipan_saimukei_2));
			
			double saiken_zankei_2 = 0;
			saiken_zankei_2 = ipan_saimukei_2 + Function.getValueOfDoubleC(form.getKoteika_eigyosaiken_2());
			
			// 債権残高計
			form.setSaiken_zankei_2(Function.format("##,###,###,###,##0.##",saiken_zankei_2));			
			
			// 一次二次査定テーブルから表示項目取得。
			// No768, 2008/06/12, SJA渡辺, 一次保存されていない場合値を設定しないように修正
			if (isIchijiSave()) {
				// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス70を定数化
				HashMap map = getSST_SATEI(satei_anken_no,GS.PHASE_HIKIATEKIN_KENSYO);
				form.setRyuhosaimu_2((String)map.get("ryuhosaimu"));
				form.setOth_ryuhosaimu_2((String)map.get("oth_ryuhosaimu"));
				form.setHozen_2((String)map.get("hozen"));
				form.setSonotakaisyu_2((String)map.get("sonotakaisyu"));
				form.setRiko_kenen_2((String)map.get("riko_kenen"));
				form.setTuika_hikiate_2((String)map.get("tuika_hikiate"));
				//form.setSaiken_kbn((String)map.get("saiken_kbn_nm"));
				//form.setTori_kbn((String)map.get("tori_kbn"));
				// 課題No.60 引当金検証時の取引先区分・債権区分設定
				// 追加開始
				form.setFinal_Saiken_kbn((String)map.get("saiken_kbn_nm"));
				form.setFinal_tori_kbn((String)map.get("tori_kbn"));
				// 追加完了
				
				///////////////////////////////////////////////////
				//障害票：392
				//チェックイン日：2008/5/19
				//対応者：SJA中島
				//概要：本〆、〆後修正では、引当金補正額を画面Formにセットしないように修正(仮〆は、処理回数1)
				///////////////////////////////////////////////////
				if("1".equals(syori_cnt)){
					form.setHikiatekin_hosei((String)map.get("hikiate_hosei"));
				}
				// 課題No.60 引当金検証時の取引先区分・債権区分設定
				// 追加開始
			}else{
				HashMap map = getSaikenToriKbn(satei_anken_no);
				form.setFinal_Saiken_kbn((String)map.get("saiken_kbn_nm"));
				form.setFinal_tori_kbn((String)map.get("tori_kbn"));
				// 追加完了
			}
			
			// 留保債務計
			double ryuhosaimu = Function.getValueOfDoubleC(form.getRyuhosaimu_2());
			double ryuhosaimu3 = Function.getValueOfDoubleC(form.getOth_ryuhosaimu_2());
			double total = ryuhosaimu + ryuhosaimu3;

			///////////////////////////////////////
			//障害票：803
			//チェックイン日：2008/6/9
			//対応者：中島
			//概要：留保債務計が債権残高合計より大きい場合で、かつ債権残高がマイナスの場合0をセットする。
			////////////////////////////////////////
			// 債権残高合計より大きな額か判定
			if (total > saiken_zankei_2) {
				if(saiken_zankei_2 > 0){
					form.setRyuhosaimu_kei_2(Function.format("##,###,###,###,##0.##",saiken_zankei_2));
				}else{
					form.setRyuhosaimu_kei_2(Function.format("##,###,###,###,##0.##",0));
				}
			} else {
				form.setRyuhosaimu_kei_2(Function.format("###,###,###,###,##0.##",total));
			}

			// 引当控除後残高
			double hikiate_taisyo = Function.getValueOfDoubleC(form.getSaiken_zankei_2()) - 	// 債権残高合計①
									 Function.getValueOfDoubleC(form.getRyuhosaimu_kei_2()) - 	// 留保債務計②
									 Function.getValueOfDoubleC(form.getHozen_2()) -			// 保全③
									 Function.getValueOfDoubleC(form.getSonotakaisyu_2()) +		// その他回収④
									 Function.getValueOfDoubleC(form.getRiko_kenen_2()) -		// 履行請求懸念⑤
									 Function.getValueOfDoubleC(form.getKibikiatekin_2());		// 既引当金⑥		

			form.setHikiate_kojo(Function.format("###,###,###,###,##0.##",hikiate_taisyo));
			
			// 補正後引当金額
			double hosei_hikiate = Function.getValueOfDoubleC(form.getHikiatekin_hosei()) +		// 引当金補正額
									Function.getValueOfDoubleC(form.getKibikiatekin_2());		// 既引当金
			
			form.setHoseigo_hikiate(Function.format("###,###,###,###,##0.##",hosei_hikiate));
			
			// 補正後引当控除後残高
			double hosei_hikiate_zan = hikiate_taisyo -											// 引当控除後残高	
										Function.getValueOfDoubleC(form.getHikiatekin_hosei()); // 引当金補正額

			form.setHoseigo_hikiatekojo_zan(Function.format("###,###,###,###,##0.##",hosei_hikiate_zan));
			
			// コメントテーブルから表示項目取得。
			form.setHikiatekin_kensyo_cmt(getSST_COMMENT(satei_anken_no,"00"));
			
			// 通貨コード取得
			//form.setTuuka_cd(getTuuka_cd());	delete tuuka_cd (2008/4/15 nakajima)
			
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(Exception e){
					// 処理なし。
					throw new SQLException(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * 一次二次査定テーブルから、表示項目を取得する。
	 * @param satei_anken_no
	 * @param phase
	 */
	private HashMap getSST_SATEI(String satei_anken_no,String phase) throws SQLException{
		ResultSet rs = null;
		///////////////////////////////////////
		//障害票：465
		//チェックイン日：2008/5/25
		//対応者：上田
		//概要：ResultSetの循環使用対応
		////////////////////////////////////////
//		StringBuffer sql = new StringBuffer();
		String sql = null;
		String ZERO_NO = "0";
		String ONE_NO ="1";
		String TWO_NO ="2";
		String THREE_NO ="3";
		HashMap map = new HashMap();

		if(phase == null){
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// 初回月・中間月の場合、フェーズは査定進捗管理のフェーズを使用する。
//			sql = new StringBuffer()
//				.append(" select ")
//			   	.append(" ST.seijo_chk, ")
//				.append(" ST.yochui_chk, ")
//				.append(" ST.tyoka_chk, ")
//				.append(" ST.kanwa_chk, ")
//				.append(" ST.entai_chk, ")
//				.append(" ST.hasanho_chk, ")
//				.append(" ST.kaishaho_chk, ")
//				.append(" ST.koseho_chk, ")
//				.append(" ST.saiseho_chk, ")
//				.append(" ST.shobun_chk, ")
//				.append(" ST.sonota_chk, ")
//				.append(" ST.ryuhosaimu, ")
//				.append(" ST.oth_ryuhosaimu, ")
//				.append(" ST.hozen, ")
//				.append(" ST.sonotakaisyu, ")
//				.append(" ST.riko_kenen, ")
//				.append(" ST.tuika_hikiate, ")
//				.append(" ST.hikiate_hosei, ")
//				.append(" ST.saiken_kbn ")
//				.append(" from ")
//				.append(" SST_SATEI ST, ")
//				.append(" SST_SATEI_STAT SS ")
//				.append(" where ")
//				.append(" ST.anken_no ='")
//				.append(satei_anken_no)
//				.append("' and ST.anken_no = SS.anken_no ")
//				.append(" and ST.phase = SS.phase ");
			sql = new StringBuffer(" select ")
							//課題No.73
							//削除開始
							//.append(" ST.seijo_chk, ")
							//.append(" ST.yochui_chk, ")
							//.append(" ST.tyoka_chk, ")
							//.append(" ST.kanwa_chk, ")
							//.append(" ST.entai_chk, ")
							//.append(" ST.hasanho_chk, ")
							//.append(" ST.kaishaho_chk, ")
							//.append(" ST.koseho_chk, ")
							//.append(" ST.saiseho_chk, ")
							//.append(" ST.shobun_chk, ")
							//.append(" ST.sonota_chk, ")
							//削除完了
							.append(" ST.ryuhosaimu, ")
							.append(" ST.oth_ryuhosaimu, ")
							.append(" ST.hozen, ")
							.append(" ST.sonotakaisyu, ")
							.append(" ST.riko_kenen, ")
							.append(" ST.tuika_hikiate, ")
							.append(" ST.hikiate_hosei, ")
							.append(" ST.torihikisaki_kbn, ")
							.append(" ST.saiken_kbn ")
							.append(" from ")
							.append(" SST_SATEI ST, ")
							.append(" SST_SATEI_STAT SS ")
							.append(" where ")
							.append(" ST.anken_no ='")
							.append(satei_anken_no)
							.append("' and ST.anken_no = SS.anken_no ")
							.append(" and ST.phase = SS.phase ").toString();
		}else{	
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
//			sql = new StringBuffer()
//				.append(" select ")
//				.append(" seijo_chk,")
//				.append(" yochui_chk,")
//				.append(" tyoka_chk,")
//				.append(" kanwa_chk,")
//				.append(" entai_chk,")
//				.append(" hasanho_chk,")
//				.append(" kaishaho_chk,")
//				.append(" koseho_chk,")
//				.append(" saiseho_chk,")
//				.append(" shobun_chk,")
//				.append(" sonota_chk,")
//				.append(" ryuhosaimu, ")
//				.append(" oth_ryuhosaimu, ")
//				.append(" hozen, ")
//				.append(" sonotakaisyu, ")
//				.append(" riko_kenen, ")
//				.append(" tuika_hikiate, ")
//				.append(" hikiate_hosei, ")
//				.append(" saiken_kbn ")
//				.append(" from SST_SATEI ")
//				.append(" where ")
//				.append(" anken_no ='")
//				.append(satei_anken_no)
//				.append("' and phase = '")
//				.append(phase)
//				.append("'");
			sql = new StringBuffer(" select ")
							//課題No.73
							//削除開始
							//.append(" seijo_chk,")
							//.append(" yochui_chk,")
							//.append(" tyoka_chk,")
							//.append(" kanwa_chk,")
							//.append(" entai_chk,")
							//.append(" hasanho_chk,")
							//.append(" kaishaho_chk,")
							//.append(" koseho_chk,")
							//.append(" saiseho_chk,")
							//.append(" shobun_chk,")
							//.append(" sonota_chk,")
							//削除完了
							.append(" ryuhosaimu, ")
							.append(" oth_ryuhosaimu, ")
							.append(" hozen, ")
							.append(" sonotakaisyu, ")
							.append(" riko_kenen, ")
							.append(" tuika_hikiate, ")
							.append(" hikiate_hosei, ")
							.append(" NVL(henkogo_torihikisaki_kbn,torihikisaki_kbn) AS torihikisaki_kbn, ")
							.append(" NVL(henkogo_saiken_kbn,saiken_kbn) AS saiken_kbn ")
							.append(" from SST_SATEI ")
							.append(" where ")
							.append(" anken_no ='")
							.append(satei_anken_no)
							.append("' and phase = '")
							.append(phase)
							.append("'").toString();
		}
		
		try{
			rs = sqlExec.execQuery(sql.toString());
			while(rs.next()){
				if(rs.getString("ryuhosaimu") != null){
					map.put("ryuhosaimu",Function.format("##,###,###,###,##0",Function.getValueOfDouble(rs.getString("ryuhosaimu"))));					
				}else{
					map.put("ryuhosaimu","");
				}
				if(rs.getString("oth_ryuhosaimu") != null){
					map.put("oth_ryuhosaimu",Function.format("##,###,###,###,##0",Function.getValueOfDouble(rs.getString("oth_ryuhosaimu"))));					
				}else{
					map.put("oth_ryuhosaimu","");
				}
				if(rs.getString("hozen") != null){
					map.put("hozen",Function.format("##,###,###,###,##0",Function.getValueOfDouble(rs.getString("hozen"))));
				}else{
					map.put("hozen","");
				}
				if(rs.getString("sonotakaisyu") != null){
					map.put("sonotakaisyu",Function.format("##,###,###,###,##0",Function.getValueOfDouble(rs.getString("sonotakaisyu"))));
				}else{
					map.put("sonotakaisyu","");
				}
				if(rs.getString("riko_kenen") != null){
					map.put("riko_kenen",Function.format("##,###,###,###,##0",Function.getValueOfDouble(rs.getString("riko_kenen"))));
				}else{
					map.put("riko_kenen","");
				}
				if(rs.getString("tuika_hikiate") != null){
					map.put("tuika_hikiate",Function.format("##,###,###,###,##0",Function.getValueOfDouble(rs.getString("tuika_hikiate"))));
					
				}else{
					map.put("tuika_hikiate","");
				}
				if(rs.getString("hikiate_hosei") != null){
					map.put("hikiate_hosei",Function.format("##,###,###,###,##0",Function.getValueOfDouble(rs.getString("hikiate_hosei"))));
					
				}else{
					map.put("hikiate_hosei","");
				}
				map.put("saiken_kbn",rs.getString("saiken_kbn"));

				// 課題No.60,73
				// 削除開始
				// 取引先区分判定時に選択された判定によって出力する画面を選択する。
				/*if(!ZERO_NO.equals(rs.getString("seijo_chk")) ||
				   !ZERO_NO.equals(rs.getString("yochui_chk"))){
					// 正常先・要注意にチェックがあった場合
					map.put("tori_kbn",appContext.getMsg("msg.0012"));
				}else if(!ZERO_NO.equals(rs.getString("tyoka_chk")) ||
						  !ZERO_NO.equals(rs.getString("kanwa_chk")) ||
						  !ZERO_NO.equals(rs.getString("entai_chk"))
						){
					// 貸倒懸念先該当事由にチェックがあった場合
					map.put("tori_kbn",appContext.getMsg("msg.0013"));
				}else{
					// 破産更生先該当事由にチェックがあった場合
					map.put("tori_kbn",appContext.getMsg("msg.0014"));
				}*/
				// 削除完了
				// 追加開始
				if(ONE_NO.equals(rs.getString("torihikisaki_kbn"))){
					map.put("tori_kbn",appContext.getMsg("label.seijo"));
				}else if(TWO_NO.equals(rs.getString("torihikisaki_kbn"))){
					map.put("tori_kbn",appContext.getMsg("label.youtyuui"));
				}else if(THREE_NO.equals(rs.getString("torihikisaki_kbn"))){
					map.put("tori_kbn",appContext.getMsg("msg.0013"));
				}else{
					map.put("tori_kbn",appContext.getMsg("msg.0014"));
				}
				// 追加完了
				
				if(ONE_NO.equals(rs.getString("saiken_kbn"))){
					map.put("saiken_kbn_nm",appContext.getMsg(GL.LABEL_IPAN_SAIKEN));
				}else if(TWO_NO.equals(rs.getString("saiken_kbn"))){
					map.put("saiken_kbn_nm",appContext.getMsg(GL.LABEL_KASHITAORE_KENEN_SAIKEN));
				}else{
					map.put("saiken_kbn_nm",appContext.getMsg(GL.LABEL_HASAN_KOSEI_SAIKEN));
				}
			}
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch (Exception e){
					// 処理なし
					throw new SQLException(e.getMessage());
				}
			}
		}
		return map;
	}
	
	// 課題No.60 引当金検証時の取引先区分・債権区分設定
	// 追加開始
	/**
	 * 一次二次査定テーブルから、表示項目を取得する。
	 * @param satei_anken_no
	 */
	private HashMap getSaikenToriKbn(String satei_anken_no) throws SQLException{
		ResultSet rs = null;
		String sql = null;
		String ONE_NO ="1";
		String TWO_NO ="2";
		String THREE_NO ="3";
		HashMap map = new HashMap();
		sql = new StringBuffer(" select ")
						.append(" NVL(henkogo_torihikisaki_kbn,torihikisaki_kbn) AS torihikisaki_kbn, ")
						.append(" NVL(henkogo_saiken_kbn,saiken_kbn) AS saiken_kbn ")
						.append(" from SST_SATEI ")
						.append(" where ")
						.append(" anken_no ='")
						.append(satei_anken_no)
						.append("' and phase = '70'").toString();
	
		try{
			rs = sqlExec.execQuery(sql.toString());
			while(rs.next()){
				if(ONE_NO.equals(rs.getString("torihikisaki_kbn"))){
					map.put("tori_kbn",appContext.getMsg("label.seijo"));
				}else if(TWO_NO.equals(rs.getString("torihikisaki_kbn"))){
					map.put("tori_kbn",appContext.getMsg("label.youtyuui"));
				}else if(THREE_NO.equals(rs.getString("torihikisaki_kbn"))){
					map.put("tori_kbn",appContext.getMsg("msg.0013"));
				}else{
					map.put("tori_kbn",appContext.getMsg("msg.0014"));
				}
				
				if(ONE_NO.equals(rs.getString("saiken_kbn"))){
					map.put("saiken_kbn_nm",appContext.getMsg(GL.LABEL_IPAN_SAIKEN));
				}else if(TWO_NO.equals(rs.getString("saiken_kbn"))){
					map.put("saiken_kbn_nm",appContext.getMsg(GL.LABEL_KASHITAORE_KENEN_SAIKEN));
				}else{
					map.put("saiken_kbn_nm",appContext.getMsg(GL.LABEL_HASAN_KOSEI_SAIKEN));
				}
			}
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch (Exception e){
					// 処理なし
					throw new SQLException(e.getMessage());
				}
			}
		}
		return map;
	}
	// 追加完了

	/**
	 * コメントテーブルから、コメント内容を取得する。
	 * @param satei_anken_no
	 * @param phase
	 * @param toroku_point
	 * @return
	 * @throws SQLException
	 */
	private String getSST_COMMENT(String satei_anken_no,String toroku_point) throws SQLException{
		ResultSet rs = null;
		String rtn_val=null;
		StringBuffer sql = new StringBuffer();
			sql.append(" select ")
			   .append(" CM.COMMENT_VAL ")
			   .append(" from ")
			   .append(" SST_COMMENT CM, ")
			   .append(" SST_SATEI_STAT SS ")
			   .append(" where ")
			   .append(" SS.anken_no ='")
			   .append(satei_anken_no)
			   .append("' and SS.anken_no = CM.anken_no ")
			   .append(" and SS.phase = CM.phase ")
			   .append(" and toroku_point ='")
			   .append(toroku_point)
			   .append("'");
		
		try{
			rs = sqlExec.execQuery(sql.toString());
			while(rs.next()){
				///////////////////////////////////////////
				//障害表：482,486
				//チェックイン日：2008/5/29
				//対応者：SJA中島
				//概要：改行コードをBRタグに置き換える。
				///////////////////////////////////////////
				rtn_val = rs.getString("COMMENT_VAL");
			}
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch (Exception e){
					// 処理なし
					throw new SQLException(e.getMessage());
				}
			}
		}
		return rtn_val;
	}
	
	/**
	 * 処理回数セレクトボックス値設定
	 */
	public void setSyoriKaisuKbn() throws SQLException {
		
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer().append(this.getKbnSql("syori_kaisu",cmnData.getComLangMode()));
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			
		    LinkedHashMap syoriKaisuKbn = new LinkedHashMap(getRsCount(rs));
		    
			while ( rs.next() ) {
			    String tmp_kbn_val = Function.trim(rs.getString("kbn_val"));
			    if (!("2".equals(tmp_kbn_val))) {
			    	// 勘定科目に表示する項目の作成
			    	String kbn_val = tmp_kbn_val;
			    	String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
			    	
			    	syoriKaisuKbn.put(kbn_hyouji_val, kbn_val );
			    }
			} // while
			
			form.setSyoriKaisuList(syoriKaisuKbn);
			
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
	 * 一時保存されたことがあるか判定
	 * 
	 * @exception SQLException
	 */
	private boolean isIchijiSave() throws SQLException {
		// No768, 2008/06/12, SJA渡辺, 一次保存されていなかれば値を表示しないように修正
		boolean result = false;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer().append("SELECT COUNT(CM.anken_no) AS cnt")
											.append(" FROM SST_COMMENT CM")
											.append(" WHERE CM.anken_no='")
											.append(satei_anken_no)
											.append("' and CM.phase='70'")
											.append(" and CM.toroku_point='00'");
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			if (rs.next()) {
				if(rs.getInt("cnt") > 0) {
					result = true;
				}
			}
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
		return result;
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
}
