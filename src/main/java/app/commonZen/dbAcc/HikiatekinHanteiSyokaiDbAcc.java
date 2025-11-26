/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/21		SSC				1.5次版機能組込
******************************************************************************/
package app.commonZen.dbAcc;

import app.SessionDataZen;
import app.commonZen.form.HikiatekinHanteiSyokaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 引当金判定タブDBアクセスクラス
 */
public class HikiatekinHanteiSyokaiDbAcc extends CommonDbAcc {

	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;		// ＡＰＰコンテキスト

	private SessionDataZen cmnData = null;	// 共通セッションデータ
	private HikiatekinHanteiSyokaiForm form=null;
	
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
	
	//結合テスト障害No.IT043対応
	//追加開始
	//決算期項目取得用SQL
	private static final StringBuffer sateiSql = new StringBuffer().append("SELECT ")
																		.append("sihanki_tyusyutu_flg,")
																		.append("flg_kbn,")
																		.append("flg_kigen,")
																		.append("hanki_tyusyutu_flg ")
																		.append("FROM sst_satei ")
																		.append("WHERE ")
																		.append("anken_no = ? ")
																		.append("and ")
																		.append("phase = ?");
	
	private static final StringBuffer comSql = new StringBuffer().append("SELECT ")
																	.append("comment_val ")
																	.append("FROM sst_comment ")
																	.append("WHERE ")
																	.append("anken_no = ? ")
																	.append("and ")
																	.append("toroku_point = '95' ")
																	.append("and ")
																	//障害No.0054対応
																	//修正開始
																	//.append("phase <= ? ")
																	.append("phase = ? ")
																	//修正完了
																	.append("ORDER BY ")
																	.append("phase ")
																	.append("DESC");
	
	private static final StringBuffer statSql = new StringBuffer().append("SELECT ")
																	.append("flgsaki_motoanken_no,")
																	.append("phase ")
																	.append("FROM sst_satei_stat ")
																	.append("WHERE ")
																	.append("anken_no = ?");
	
	private static final StringBuffer torokuSql = new StringBuffer().append("SELECT ")
																		.append("COUNT(CM.anken_no) AS cnt ")
																		.append("FROM SST_COMMENT CM ")
																		.append("WHERE CM.anken_no = ? ")
																		.append("and ")
																		.append("CM.toroku_point = ?");
	//フラグコメント(登録ポイント95)
	private final String POINT95 = "95";
	//sst_sateiカラム名
	private static final String SIHANKI_TYUSYUTU_FLG = "sihanki_tyusyutu_flg";
	private static final String FLG_KBN = "flg_kbn";
	private static final String FLG_KIGEN = "flg_kigen";
	private static final String HANKI_TYUSYUTU_FLG = "hanki_tyusyutu_flg";
	private static final String INS_USER = "ins_user";
	private static final String UPD_USER = "upd_user";
	//sst_commentカラム名
	private static final String COMMENT_VAL = "comment_val";
	//sst_satei_statカラム名
	private static final String FLGSAKI_MOTOANKEN_NO = "flgsaki_motoanken_no";
	private static final String PHASE = "phase";
	//追加完了
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public HikiatekinHanteiSyokaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//ビーン取得
		cmnData = appContext.getCMNZenRe();
		form = (HikiatekinHanteiSyokaiForm)appContext.getActionForm();
	}
	
	/**
	 * 検索SQL実行処理 <br>
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
		ResultSet rs4 = null;
		// 査定案件No
		String satei_anken_no = cmnData.getSatei_anken_no();
		// 年月
		String ym = Function.removeDateSlash(cmnData.getYm());
		// フェーズ
		String phase = cmnData.getPhase();
		// 取引先コード
		String kanjo_cd = cmnData.getKanjo_cd();
		// 第三者留保債務内訳
		ArrayList list = new ArrayList();
		// TODO システム区分(フェーズ１はGSSのみ)
		String system_kbn = "01";
		// DUNS No.
		String duns_no = cmnData.getDuns_no();
		
		try {
			getHikiatekinHanteiForm(satei_anken_no, ym, phase, kanjo_cd, list);
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
			//					throw new SQLException(e.getMessage());
			//				}
			//			}
			if(rs1 != null){
				try{
					rs1.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
			if(rs2 != null){
				try{
					rs2.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
			if(rs3 != null){
				try{
					rs3.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
			if(rs4 != null){
				try{
					rs4.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * @param satei_anken_no
	 * @param ym
	 * @param phase
	 * @param kanjo_cd
	 * @param list
	 * @throws SQLException
	 */
	public HikiatekinHanteiSyokaiForm getHikiatekinHanteiForm(String satei_anken_no, String ym, String phase, String kanjo_cd, ArrayList list) throws SQLException {
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		
        ///////////////////////////////////////////////////
		//障害票：453
		//チェックイン日：2008/5/28
		//対応者：SJA渡辺
		//概要：画面のメソッドと帳票のメソッドを共通化。
		///////////////////////////////////////////////////
		
		try{
			//要件No.四-11 フラグ区分値取得
			//追加開始
			this.setFlgkbn();
			//追加完了
			
			// 引当金判定表示用から項目取得
			StringBuffer sql1 = new StringBuffer()
								.append(" select ")
								.append(" HH.kingaku, ")
								.append(" HH.tuuka_cd, ")		// add tuuka_cd(2008/4/15 nakajima) 
								.append(" HH.kanjo_hyouji_kbn ")
								.append(" from SST_HIKIATEHANTEI HH")
								.append(" where ")
								.append(" HH.satei_anken_no ='")
								.append(satei_anken_no)
								.append("' and HH.ym ='")
								.append(ym)
								.append("' and HH.sateikaisya_cd ='")
								// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
								//.append(cmnData.getComSateiKaishaCd())
								.append(cmnData.getAnken_satei_kaisya_cd())
								.append("' and HH.tori_cd ='")
								.append(kanjo_cd)
								.append(" ' and HH.system_kbn = '")
								.append(cmnData.getSystem_kbn())
								/*.append("' and sateikaisya_cd = '")
								.append(cmnData.getComSateiKaishaCd())*/
								.append("' and HH.mise_cd = '")
								.append(cmnData.getMise_cd())
								.append("' and HH.kanjo_hyouji_kbn in(")
								.append("'01','02','03','04','05','06','07','08','09','10','11','12','14','15','16'")
								.append(")");
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
//			rs = sqlExec.execQuery(sql1.toString());
//			while(rs.next()){
//				// add tuuka_cd (2008/4/15 nakajima)
//				form.setTuuka_cd(Function.trim(rs.getString("tuuka_cd")));
//				
//				String kingaku = rs.getString("kingaku");
//				String kbn = rs.getString("kanjo_hyouji_kbn");
			rs1 = sqlExec.execQuery(sql1.toString());
			while(rs1.next()){
				// add tuuka_cd (2008/4/15 nakajima)
				form.setTuuka_cd(Function.trim(rs1.getString("tuuka_cd")));
				
				String kingaku = rs1.getString("kingaku");
				String kbn = rs1.getString("kanjo_hyouji_kbn");
				if(!GS.EMPTY_CHARCTER.equals(Function.trim(kingaku))){
					if(UKETORI_TEGATA.equals(kbn)){
						form.setUketoritegata(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}else if (YUSYUTSU_UKETORI.equals(kbn)){
						form.setYusyutu_uketoritegata(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}else if (URIKAKE.equals(kbn)){
						form.setUrikakekin(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}else if (TORI_MAE.equals(kbn)){
						form.setTorihikimaetokin(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}else if (TATEKAE.equals(kbn)){
						form.setTatekaekin(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}else if (MISYUNYU.equals(kbn)){
						form.setMisyunyukin(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}else if (MISYUSYU.equals(kbn)){
						form.setMisyusyueki(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}else if (TANKI.equals(kbn)){
						form.setTanki_kashitsukekin(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}else if (SASHIIRE.equals(kbn)){
						form.setSashiire_hosyokin(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}else if (KARIHARAI.equals(kbn)){
						form.setKaribaraikin(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}else if (TYOKI.equals(kbn)){
						form.setTyoki_kashitsukekin(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}else if (SONOTA.equals(kbn)){
						form.setSonota_toshi(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}else if (HOSYO.equals(kbn)){
						form.setHosyosaimu_gokei(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}else if (KIBIKIATE.equals(kbn)){
						form.setKibikiatekin(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}else if (KOTEI.equals(kbn)){
						form.setKoteika_eigyosaiken(
								Function.format("##,###,###,###,##0.##",Function.getValueOfDouble(kingaku)));
					}
				}
			}
			
			double ipan_saimukei = 0;
			ipan_saimukei = Function.getValueOfDoubleC(form.getUketoritegata())+			// 受取手形
							Function.getValueOfDoubleC(form.getYusyutu_uketoritegata())+	// 輸出受取手形
							Function.getValueOfDoubleC(form.getUrikakekin())+				// 売掛金
							Function.getValueOfDoubleC(form.getTorihikimaetokin())+			// 取引前渡金
							Function.getValueOfDoubleC(form.getTatekaekin())+				// 立替金
							Function.getValueOfDoubleC(form.getMisyunyukin())+				// 未収入金
							Function.getValueOfDoubleC(form.getMisyusyueki())+				// 未収収益
							Function.getValueOfDoubleC(form.getTanki_kashitsukekin())+		// 短期貸付金
							Function.getValueOfDoubleC(form.getSashiire_hosyokin())+		// 差入保証金
							Function.getValueOfDoubleC(form.getKaribaraikin())+				// 仮払金
							Function.getValueOfDoubleC(form.getTyoki_kashitsukekin())+		// 長期貸付金
							Function.getValueOfDoubleC(form.getSonota_toshi());				// その他投資
			// 一般債権計
			form.setIpan_saimukei(Function.format("##,###,###,###,##0.##",ipan_saimukei));
			
			double saiken_zankei = 0;
			saiken_zankei = ipan_saimukei + Function.getValueOfDoubleC(form.getKoteika_eigyosaiken());
			
			// 債権残高計
			form.setSaiken_zankei(Function.format("##,###,###,###,##0.##",saiken_zankei));
			
			
			//結合テスト障害No.IT043対応
			//今回決算期項目が未登録の場合、抽出元案件Noから情報を取得するよう修正
			//追加開始
			
			//今回決算期項目が登録されたかチェック
			boolean torokuFlg = this.isKessankiToroku(satei_anken_no);
			
			//決算期項目取得
			this.getKessankiKoumoku(torokuFlg,satei_anken_no,phase);
			//追加完了
			
			// 一次二次査定テーブルから項目取得
			StringBuffer sql2 = new StringBuffer()
								.append(" select ")
								.append(" ST.ryuhosaimu, ")
								.append(" ST.oth_ryuhosaimu, ")
								.append(" ST.hozen, ")
								.append(" ST.sonotakaisyu, ")
								.append(" ST.riko_kenen, ")
								.append(" ST.tuika_hikiate, ")
								.append(" ST.hudosan_k, ")
								.append(" ST.hudosan_h, ")
								.append(" ST.dosan_k, ")
								.append(" ST.dosan_h, ")
								.append(" ST.hoken_k, ")
								.append(" ST.hoken_h, ")
								.append(" ST.sonota_k, ")
								.append(" ST.sonota_h ")
								//結合テスト障害No.IT043対応
								//今回決算期項目が未登録の場合、抽出元案件Noを使用するため削除
								//削除開始
								//要件No.四-11
								//追加開始
								//.append(" ST.sihanki_tyusyutu_flg, ")
								//.append(" ST.flg_kbn, ")
								//.append(" ST.flg_kigen, ")
								//.append(" ST.hanki_tyusyutu_flg ")
								//追加完了
								//削除完了
								.append(" from SST_SATEI ST")
								.append(" where ")
								.append(" ST.anken_no ='")
								.append(satei_anken_no)
								.append("' and ST.phase ='")
								.append(phase)
								.append("'");
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
//			rs = sqlExec.execQuery(sql2.toString());
//			while(rs.next()){
//				form.setRyuhosaimu(
//						Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("ryuhosaimu"))));
//				if(rs.getString("oth_ryuhosaimu") == null) {
//					form.setOth_ryuhosaimu("");
//				} else {
//					form.setOth_ryuhosaimu(
//						Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("oth_ryuhosaimu"))));
//				}
//				if(rs.getString("hozen") == null) {
//					form.setHozen("");
//				} else {
//					form.setHozen(
//						Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("hozen"))));
//				}
//				if(rs.getString("sonotakaisyu") != null){
//					form.setSonotakaisyu(
//							Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("sonotakaisyu"))));
//				}
//				if(rs.getString("riko_kenen") != null){
//					form.setRiko_kenen(
//							Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("riko_kenen"))));
//				}
//				if(rs.getString("tuika_hikiate") != null){
//					form.setTuika_hikiate(
//							Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("tuika_hikiate"))));
//				}
//				if(rs.getString("hudosan_k") != null){
//					form.setHudosan_k(
//							Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("hudosan_k"))));
//				}
//				if(rs.getString("hudosan_h") != null){
//					form.setHudosan_h(
//							Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("hudosan_h"))));
//				}
//				if( rs.getString("dosan_k")!= null){
//					form.setDosan_k(
//							Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("dosan_k"))));
//				}
//				if(rs.getString("dosan_h") != null){
//					form.setDosan_h(
//							Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("dosan_h"))));
//				}
//				if(rs.getString("hoken_k") != null){
//					form.setHoken_k(
//							Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("hoken_k"))));
//				}
//				if(rs.getString("hoken_h") != null){
//					form.setHoken_h(
//							Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("hoken_h"))));
//				}
//				if(rs.getString("sonota_k") != null){
//					form.setSonota_k(
//							Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("sonota_k"))));
//				}
//				if(rs.getString("sonota_h") != null){
//					form.setSonota_h(
//							Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("sonota_h"))));
//				}
//			}
			rs2 = sqlExec.execQuery(sql2.toString());

			while(rs2.next()){
				form.setRyuhosaimu(
						Function.format("###,###,###,##0",Function.getValueOfLong(rs2.getString("ryuhosaimu"))));
				if(rs2.getString("oth_ryuhosaimu") == null) {
					form.setOth_ryuhosaimu("");
				} else {
					form.setOth_ryuhosaimu(
						Function.format("###,###,###,##0",Function.getValueOfLong(rs2.getString("oth_ryuhosaimu"))));
				}
				if(rs2.getString("hozen") == null) {
					form.setHozen("");
				} else {
					form.setHozen(
						Function.format("###,###,###,##0",Function.getValueOfLong(rs2.getString("hozen"))));
				}
				if(rs2.getString("sonotakaisyu") != null){
					form.setSonotakaisyu(
							Function.format("###,###,###,##0",Function.getValueOfLong(rs2.getString("sonotakaisyu"))));
				}
				if(rs2.getString("riko_kenen") != null){
					form.setRiko_kenen(
							Function.format("###,###,###,##0",Function.getValueOfLong(rs2.getString("riko_kenen"))));
				}
				if(rs2.getString("tuika_hikiate") != null){
					form.setTuika_hikiate(
							Function.format("###,###,###,##0",Function.getValueOfLong(rs2.getString("tuika_hikiate"))));
				}
				if(rs2.getString("hudosan_k") != null){
					form.setHudosan_k(
							Function.format("###,###,###,##0",Function.getValueOfLong(rs2.getString("hudosan_k"))));
				}
				if(rs2.getString("hudosan_h") != null){
					form.setHudosan_h(
							Function.format("###,###,###,##0",Function.getValueOfLong(rs2.getString("hudosan_h"))));
				}
				if(rs2.getString("dosan_k")!= null){
					form.setDosan_k(
							Function.format("###,###,###,##0",Function.getValueOfLong(rs2.getString("dosan_k"))));
				}
				if(rs2.getString("dosan_h") != null){
					form.setDosan_h(
							Function.format("###,###,###,##0",Function.getValueOfLong(rs2.getString("dosan_h"))));
				}
				if(rs2.getString("hoken_k") != null){
					form.setHoken_k(
							Function.format("###,###,###,##0",Function.getValueOfLong(rs2.getString("hoken_k"))));
				}
				if(rs2.getString("hoken_h") != null){
					form.setHoken_h(
							Function.format("###,###,###,##0",Function.getValueOfLong(rs2.getString("hoken_h"))));
				}
				if(rs2.getString("sonota_k") != null){
					form.setSonota_k(
							Function.format("###,###,###,##0",Function.getValueOfLong(rs2.getString("sonota_k"))));
				}
				if(rs2.getString("sonota_h") != null){
					form.setSonota_h(
							Function.format("###,###,###,##0",Function.getValueOfLong(rs2.getString("sonota_h"))));
				}
				//結合テスト障害No.IT043対応
				//削除開始
				//要件No.四-11
				//追加開始
				//第1/3四半期フラグ
				//if(rs2.getString("sihanki_tyusyutu_flg") != null){
				//	form.setSihanki_flg(rs2.getString("sihanki_tyusyutu_flg"));										
				//}
				//フラグ区分
				//if(rs2.getString("flg_kbn") != null){
				//	form.setFlg_kbn(Function.trim(rs2.getString("flg_kbn")));
				//}
				//フラグ期限 (仕様変更 画面には表示しない)
				//if(rs2.getString("flg_kigen") != null){
				//	form.setFlg_kigen(rs2.getString("flg_kigen"));					
				//}
				//第2/4四半期抽出 (仕様変更 画面には表示しない)
				//if(rs2.getString("hanki_tyusyutu_flg") != null){
				//	form.setHanki_tyusyutu(rs2.getString("hanki_tyusyutu_flg"));					
				//}
				//追加完了
				//削除完了
			}

			// 留保債務計
			double ryuhosaimu_kei = 0;
			
			ryuhosaimu_kei = Function.getValueOfDoubleC(form.getRyuhosaimu()) + 
								Function.getValueOfDoubleC(form.getOth_ryuhosaimu());
			
			
			///////////////////////////////////////
			//障害票：803
			//チェックイン日：2008/6/9
			//対応者：中島
			//概要：留保債務計が債権残高合計より大きい場合で、かつ債権残高がマイナスの場合0をセットする。
			////////////////////////////////////////
			// 債権残高合計より大きな額か判定
			if (ryuhosaimu_kei > saiken_zankei) {
				if(saiken_zankei > 0){
					form.setRyuhosaimu_kei(Function.format("##,###,###,###,##0.##",saiken_zankei));
				}else{
					form.setRyuhosaimu_kei(Function.format("##,###,###,###,##0.##",0));
				}
			} else {
				form.setRyuhosaimu_kei(Function.format("#,###,###,###,##0",ryuhosaimu_kei));
			}
			
			// 第三者留保債務から項目取得
			StringBuffer sql3 = new StringBuffer()
								.append(" select ")
								.append(" DR.torisaki_nm, ")
								.append(" DR.kanjo_nm, ")
								.append(" DR.kingaku, ")
								.append(" DR.kikan_tori_cd ")
								.append(" from SST_OTH_RYUHOSAIMU DR ")
								.append(" where ")
								.append(" DR.anken_no ='")
								.append(satei_anken_no)
								.append("' and DR.phase ='")
								.append(phase)
								.append("' order by DR.eda ASC");
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
//			rs = sqlExec.execQuery(sql3.toString());
//			while(rs.next()){
//				HashMap map = new HashMap();
//				map.put("torisaki_nm",Function.trim(rs.getString("torisaki_nm")));
//				map.put("kanjo_nm",Function.trim(rs.getString("kanjo_nm")));
//				if(rs.getString("kingaku") != null){
//					map.put("kingaku",Function.format("###,###,###,##0",Function.getValueOfLong(rs.getString("kingaku"))));
//				}else{
//					map.put("kingaku"," ");
//				}
//				map.put("kikan_tori_cd",Function.trim(rs.getString("kikan_tori_cd")));
//				list.add(map);
//			}
			rs3 = sqlExec.execQuery(sql3.toString());
			while(rs3.next()){
				HashMap map = new HashMap();
				map.put("torisaki_nm",Function.trim(rs3.getString("torisaki_nm")));
				map.put("kanjo_nm",Function.trim(rs3.getString("kanjo_nm")));
				if(rs3.getString("kingaku") != null){
					map.put("kingaku",Function.format("###,###,###,##0",Function.getValueOfLong(rs3.getString("kingaku"))));
				}else{
					map.put("kingaku"," ");
				}
				map.put("kikan_tori_cd",Function.trim(rs3.getString("kikan_tori_cd")));
				list.add(map);
			}
			
			if(list.size() != 3){
				int i=list.size();
				while(i<3){
					HashMap map = new HashMap();
					map.put("torisaki_nm"," ");
					map.put("kanjo_nm"," ");
					map.put("kingaku"," ");
					map.put("kikan_tori_cd"," ");
					list.add(map);
					i++;
				}
			}
			form.setOth_ryuhosaimu_uchiwake(list);
			
			// コメントから項目取得
			StringBuffer sql4 = new StringBuffer()
								.append(" select ")
								.append(" CM.toroku_point,")
								.append(" CM.comment_val")
								.append(" from SST_COMMENT CM")
								.append(" where ")
								.append(" CM.anken_no ='")
								.append(satei_anken_no)
								.append("' and CM.phase ='")
								.append(phase)
								.append("' and CM.toroku_point in (")
								//結合テスト障害No.IT043対応 
								//今回決算期項目未登録の場合、抽出元の情報を取得するため'95'を削除
								//修正開始
								//要件No.四-11 登録ポイント'95'追加
								//追加開始
								//.append("'50','60','70','80','90','95'")
								.append("'50','60','70','80','90'")
								//追加完了
								//修正完了
								.append(")");
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
//			rs = sqlExec.execQuery(sql4.toString());
//			while(rs.next()){
//				String toroku_point = rs.getString("toroku_point");
//				
//				if("50".equals(toroku_point)){
//					form.setSonota_naiyo(Function.trim(rs.getString("comment_val")));
//				}else if ("60".equals(toroku_point)){
//					form.setSonota_kaisyu_naiyo(Function.trim(rs.getString("comment_val")));
//				}else if ("70".equals(toroku_point)){
//					form.setRikoseikyu_kenen_naiyo(Function.trim(rs.getString("comment_val")));
//				}else if ("80".equals(toroku_point)){
//					form.setHikiatekin_konkyo_naiyo(Function.trim(rs.getString("comment_val")));
//				}else if ("90".equals(toroku_point)){
//					form.setKaisyu_naiyo(Function.trim(rs.getString("comment_val")));
//				}
//			}
			rs4 = sqlExec.execQuery(sql4.toString());
			while(rs4.next()){
				String toroku_point = rs4.getString("toroku_point");
				
				///////////////////////////////////////////
				//障害表：482,486
				//チェックイン日：2008/5/29
				//対応者：SJA中島
				//概要：改行コードをBRタグに置き換える。
				///////////////////////////////////////////
				if("50".equals(toroku_point)){
					form.setSonota_naiyo(Function.trim(rs4.getString("comment_val")));
				}else if ("60".equals(toroku_point)){
					form.setSonota_kaisyu_naiyo(Function.trim(rs4.getString("comment_val")));
				}else if ("70".equals(toroku_point)){
					form.setRikoseikyu_kenen_naiyo(Function.trim(rs4.getString("comment_val")));
				}else if ("80".equals(toroku_point)){
					form.setHikiatekin_konkyo_naiyo(Function.trim(rs4.getString("comment_val")));
				}else if ("90".equals(toroku_point)){
					form.setKaisyu_naiyo(Function.trim(rs4.getString("comment_val")));
				//結合テスト障害No.IT043対応 
				//今回決算期項目未登録の場合、抽出元の情報を取得するため'95'を削除
				//削除開始	
				//要件No.四-11
				//追加開始
				//}else if ("95".equals(toroku_point)){
				//	form.setFlg_comment(Function.trim(rs4.getString("comment_val")));
				//追加完了
				//削除完了
				}
			}
			
			// 引当対象金額を取得。
			double hikiate_taisyo = 0;
			hikiate_taisyo = Function.getValueOfDoubleC(form.getSaiken_zankei()) - 	// 債権残高合計①
							 Function.getValueOfDoubleC(form.getRyuhosaimu_kei()) - // 留保債務計②
							 Function.getValueOfDoubleC(form.getHozen()) -			// 保全③
							 Function.getValueOfDoubleC(form.getSonotakaisyu()) +	// その他回収④
							 Function.getValueOfDoubleC(form.getRiko_kenen()) -		// 履行請求懸念⑤
							 Function.getValueOfDoubleC(form.getKibikiatekin());	// 既引当金⑥		
			
			form.setHikiate_taisyokingaku(Function.format("##,###,###,###,##0.##",hikiate_taisyo));
			/*
			// 通貨コード取得
			StringBuffer sql5 = new StringBuffer()
								.append(" select ")
								.append(" DT.ISO_CURRENCY_NM ")
								.append(" from ")
								.append(" TM_DB_KIHON_TBL@VIR_SJLMA DC, ")
								.append(" TM_DB_CURENCY_MST@VIR_SJLMA DT ")
								.append(" where ")
								.append(" DC.DUNS_NO ='")
								.append(duns_no)
								.append("' and DC.CURRENCY_CD = DT.WB_CURRENCY_CD ");
			
			// 通貨コードを取得
			rs = sqlExec.execQuery(sql5.toString());
			while(rs.next()){
				form.setTuuka_cd(rs.getString("ISO_CURRENCY_NM"));
			}
			*/
			getTuukaTani();
			
			return form;
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
//					throw new SQLException(e.getMessage());
//				}
//			}
			if(rs1 != null){
				try{
					rs1.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
			if(rs2 != null){
				try{
					rs2.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
			if(rs3 != null){
				try{
					rs3.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
			if(rs4 != null){
				try{
					rs4.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
		}
	}

	/**
	 * 通貨単位を取得する。
	 * @return
	 * @throws SQLException
	 */
	private void getTuukaTani() throws SQLException{
		StringBuffer sql = new StringBuffer()
						.append("SELECT ")
						.append("KB.KBN_HYOUJI_VAL ")
						.append("FROM SSP_KBN KB ")
						.append("WHERE ")
						.append("KB.KBN_KEY='tuuka_tani          '")
						.append(" and KB.LANG_MODE='")
						.append(cmnData.getComLangMode())
						.append("' and KB.system_kbn ='")
						.append(cmnData.getSystem_kbn())
						.append("' and KB.KBN_VAL='1'");
		ResultSet rs = null;
		String rtn_str = null;
		try{
			rs = sqlExec.execQuery(sql.toString());
			while(rs.next()){
				form.setTuuka_tani(rs.getString("KBN_HYOUJI_VAL"));
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
	}
	
	/**　要件No.四-11 フラグ区分取得
	 * フラグ区分を取得する。
	 * @return
	 * @throws SQLException
	 */
	private void setFlgkbn() throws SQLException{
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer().append(this.getKbnSql("flg_kbn",cmnData.getComLangMode()));
				
		try {
			rs = sqlExec.execQuery(sql.toString());
			
		    LinkedHashMap hm_flg_kbn = new LinkedHashMap(getRsCount(rs));
		    
		    //先頭にブランク追加
		    hm_flg_kbn.put("","");
		    
			while ( rs.next() ) {
			    String kbn_val = Function.trim(rs.getString("kbn_val"));
			    String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
			    hm_flg_kbn.put(kbn_val,kbn_hyouji_val );
			} // while
			
			form.setHm_flg_kbn(hm_flg_kbn);
	
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
	
	/**結合テスト障害No.IT043対応
	 * 
	 * 決算期項目登録判定
	 */
	public boolean isKessankiToroku(String ankenNo) throws SQLException {
		boolean existFlg = false;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement sql = null;
    	con = sqlExec.getConnection();
		try{
			//コメントテーブルに登録されているか判定
    		sql = con.prepareStatement(torokuSql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    		sql.setString(1,ankenNo);
    		sql.setString(2,POINT95);
    		rs = sqlExec.execQueryP(sql);
			while(rs.next()){
				if(rs.getInt("cnt") > 0) {
					existFlg = true;
				}
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
		
		return existFlg;
	}
	
	/**結合テスト障害No.IT043対応
	 * 
	 * 一次・二次査定テーブルより決算期項目取得
	 */
	public void getKessankiKoumoku(boolean torokuFlg,String anken_no,String phase) throws SQLException {
		
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement sql = null;
    	con = sqlExec.getConnection();
		String motoAnkenNo = null;
		String motoPhase = null;
		try{
			
    		sql = con.prepareStatement(sateiSql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
    		if (torokuFlg){
    			//登録済の場合
	    		sql.setString(1,anken_no);
	    		sql.setString(2,phase);
			}else{
				//今回決算期項目が未登録の場合、抽出元案件Noを取得
				motoAnkenNo = this.getMotoAnkenNo(anken_no,true);
				if(motoAnkenNo != null && !(motoAnkenNo.equals(GS.EMPTY_CHARCTER))){
					//抽出元案件Noが存在する場合、抽出元フェーズ取得
					motoPhase = this.getMotoAnkenNo(motoAnkenNo,false);
				}
		    	sql.setString(1,motoAnkenNo);
		    	sql.setString(2,motoPhase);					
			}				

    		rs = sqlExec.execQueryP(sql);
			while(rs.next()){
				//第1/3四半期フラグ
				if(rs.getString(SIHANKI_TYUSYUTU_FLG) != null && !(Function.trim(rs.getString(SIHANKI_TYUSYUTU_FLG))).equals(GS.EMPTY_CHARCTER)){
					form.setSihanki_flg(rs.getString(SIHANKI_TYUSYUTU_FLG));					
				}
				//フラグ区分
				if(rs.getString(FLG_KBN) != null && !(Function.trim(rs.getString(FLG_KBN))).equals(GS.EMPTY_CHARCTER)){
					form.setFlg_kbn(Function.trim(rs.getString(FLG_KBN)));
				}
				//フラグ期限
				if(rs.getString(FLG_KIGEN) != null && !(Function.trim(rs.getString(FLG_KIGEN))).equals(GS.EMPTY_CHARCTER)){
					form.setFlg_kigen(rs.getString(FLG_KIGEN));					
				}
				//第2/4四半期抽出
				if(rs.getString(HANKI_TYUSYUTU_FLG) != null && !(Function.trim(rs.getString(HANKI_TYUSYUTU_FLG))).equals(GS.EMPTY_CHARCTER)){
					form.setHanki_tyusyutu(rs.getString(HANKI_TYUSYUTU_FLG));					
				}
				//フラグコメント
				if(torokuFlg){
					//登録済の場合
					this.getComment(anken_no,phase);					
				}else{
					//未登録の場合
					this.getComment(motoAnkenNo,motoPhase);

				}
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
	}
	
	/**結合テスト障害No.IT043対応
	 * 
	 * 査定進捗テーブルより抽出元案件Noまたは抽出元フェーズ取得
	 */
	public String getMotoAnkenNo(String ankenNo,boolean flg) throws SQLException {
		String strRs = null;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement sql = null;
    	con = sqlExec.getConnection();
		try{
    		sql = con.prepareStatement(statSql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    		sql.setString(1,ankenNo);
    		rs = sqlExec.execQueryP(sql);
			while(rs.next()){
				if(flg){
					//フラグ先抽出元案件No取得
					if(rs.getString(FLGSAKI_MOTOANKEN_NO) != null && !(rs.getString(FLGSAKI_MOTOANKEN_NO)).equals(GS.EMPTY_CHARCTER)){
						strRs = rs.getString(FLGSAKI_MOTOANKEN_NO);					
					}
				}else{
					//抽出元フェーズ取得
					if(rs.getString(PHASE) != null && !(rs.getString(PHASE)).equals(GS.EMPTY_CHARCTER)){
						strRs = rs.getString(PHASE);
					}
				}
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
		return strRs;
	}
	
	/**結合テスト障害No.IT043対応
	 * 
	 * コメントテーブルよりコメント取得
	 */
	public void getComment(String ankenNo,String qPhase) throws SQLException {
		boolean existFlg = false;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement sql = null;
    	con = sqlExec.getConnection();
		try{
    		sql = con.prepareStatement(comSql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    		sql.setString(1,ankenNo);
    		sql.setString(2,qPhase);
    		rs = sqlExec.execQueryP(sql);
			//フラグコメント
			while(rs.next()){
				if(rs.getString(COMMENT_VAL) != null && !(Function.trim(rs.getString(COMMENT_VAL))).equals(GS.EMPTY_CHARCTER)){
					form.setFlg_comment(Function.trim(rs.getString(COMMENT_VAL)));					
					break;
				}
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
	}

	
	/**
	 * 前回実施年月を取得し、前回実施の査定案件Noを取得する。
	 * @param ym
	 * @return
	 */
	// No623, 2008/06/10, SJA渡辺, 使用していないのでコメントアウト
	/*private HashMap getLastTimeAnken(String ym) throws SQLException{
		ResultSet rs = null;
		///////////////////////////////////////
		//障害票：465
		//チェックイン日：2008/5/25
		//対応者：上田
		//概要：ResultSetの循環使用対応
		////////////////////////////////////////
		ResultSet rs1 = null;
		
		String year = null;
		String month = null;
		String lastAnken_no=null;
		HashMap result_map = new HashMap();
		
		if (!(ym == null || ym.length() != 6)) {
			year = ym.substring(0,4);
			month = ym.substring(4,6);
		}
		// 年取得
		StringBuffer result = new StringBuffer().append(year);
		
		StringBuffer sql = new StringBuffer()
							.append("SELECT distinct SK.kikan,")
							.append(" SK.syokai_zuki,")
							.append(" SK.cyuukan_zuki,")
							.append(" SK.saisyu_zuki ")
							.append(" FROM SSM_SYORIZUKI SK,")
							.append(" SST_HIKIATEHANTEI HH")
							.append(" WHERE SK.kaisha_cd=HH.kaisya_cd")
							.append(" and HH.satei_anken_no='")
							.append(cmnData.getSatei_anken_no())
							.append("' order by SK.kikan desc");
		try{
			HashMap map = new HashMap();
			rs = sqlExec.execQuery(sql.toString());
			boolean syokai_flg = false;
			while(rs.next()){
				if(syokai_flg){
					// 下期の初回月と同じだった場合、上期の最終月を前回実施月とする。 
					result.append(rs.getString("saisyu_zuki"));
					break;
				}
				if(month.equals(rs.getString("syokai_zuki"))){
					// 初回月と同じならば、最終月を前回実施月とするため、syokai_flgをtrueに変更する。
					syokai_flg=true;
					map.put("saisyu_zuki",rs.getString("saisyu_zuki"));
				}else if(month.equals(rs.getString("cyuukan_zuki"))){
					// 中間月と同じならば、初回月を前回実施月する。
					result.append(rs.getString("syokai_zuki"));
					break;
				}else if(month.equals(rs.getString("saisyu_zuki"))){
					// 最終月と同じならば、中間月を前回実施月する。
					result.append(rs.getString("cyuukan_zuki"));
					break;
				}
			}
			
			if(result.toString().length() != 6){
				if(map.get("saisyu_zuki") != null){
					// 上期の初回月と基準月が同じだった場合、下期の最終月を前回実施月とする。
					result.append(map.get("saisyu_zuki"));
				}else{
					// 当てはまらない場合、基準月をそのまま使用する。
					result.append(month);
				}
			}
			
			
			// 年度チェック
			int lastTime = Function.getValueOfInt(result.toString());
			int thisTime = Function.getValueOfInt(ym);
			if(lastTime > thisTime){
				// 取得した前回年月が、基準年月よりも未来になっていた場合、
				// 前回実施年度を1年戻す。
				result.replace(0,4,Function.format("####",Function.getValueOfInt(year)-1));
			}
			
			// 前回実施年月から、査定案件Noを取得する。
			StringBuffer sql1 = new StringBuffer()
								.append(" select distinct")
								.append(" satei_anken_no ")
								.append(" from ")
								.append(" SST_HIKIATEHANTEI ")
								.append(" where ")
								.append(" system_kbn = '")
								.append(cmnData.getSystem_kbn())
								.append("' and sateikaisya_cd = '")
								.append(cmnData.getComSateiKaishaCd())
								.append("' and mise_cd = '")
								.append(cmnData.getMise_cd())
								.append("' and ym = '")
								.append(result.toString())
								.append("' and tori_cd = '")
								.append(cmnData.getKanjo_cd())
								.append("'");
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
//			rs = sqlExec.execQuery(sql1.toString());
//			while(rs.next()){
//				result_map.put("lastAnken_no",rs.getString("satei_anken_no"));
//				result_map.put("lastYm",result.toString());
//			}
			rs1 = sqlExec.execQuery(sql1.toString());
			while(rs1.next()){
				result_map.put("lastAnken_no",rs1.getString("satei_anken_no"));
				result_map.put("lastYm",result.toString());
			}
			
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if(rs1 != null){
				try{
					rs1.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
		}
		return result_map;
	}*/
	
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
