/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/05/18		SSC				1.5次版機能組込 
002		2009/10/19		SSC				課題No.60 引当金検証時の取引先区分・債権区分設定
003		2009/10/22		SSC				課題No.72 引当金検証時の取引先区分・債権区分設定
004		2009/10/22		SSC				課題No.73 取引先区分プルダウン設定値を2.0次に合わせる
005		2009/11/20		SSC				課題No.152 代行取戻対応
006		2009/12/2		SSC				課題No.181 年月の表示(英語版)MM/YYYYに統一
007		2010/02/24		SSC				案件No.D5938 上記課題No.181による仮基準月の債権残高表示対応
008		2011/06/30		SSC				案件No.D9059 もぎ取り解除時に記入内容をクリアしない
009		2014/06/27		SSC				案件No.BP201404002 引当金検証登録時の承認担当者表示不備 
010		2015/05/21		SSC				BJ201502003 ICISプロジェクト対応
011		2015/09/08		SSC				BJ201408049 IA化対応時の機能改善
012		2016.03.17		SSC				BJ201602002 部門廃止対応（一次）
******************************************************************************/
package app.hikiate.dbAcc;

import app.SessionDataZen;
import app.UserBean;
import app.hikiate.form.KensyoForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
* 引当金検証画面DBアクセスクラス
*/
public class KensyoDbAcc extends CommonDbAcc {
    //////////////////////////////////
    //障害票：427
    //チェックイン日：2008/05/23
    //対応者：SJA 関
    //概要：ハードコードの定数化(フェーズ、ステータス)
    //////////////////////////////////

	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;		// ＡＰＰコンテキスト

	private SessionDataZen cmnData = null;	// 共通セッションデータ
	//課題No.152
	//追加開始
	private UserBean userBean = null;	// 共通セッションデータ
	//追加完了
	private KensyoForm form = null;	// アクションフォーム
	
	// INパラメータ
	private String userId;			// ユーザＩＤ（統合ＩＤ）
	private ArrayList honbuCd;		// 担当部門コード
	private String bumonCd;		// 部門コード（ユーザ所属）
	private String buCd;			// 部コード（ユーザ所属）
	private String kaCd;			// 課コード（ユーザ所属）
	private String sateiKaishaCd;
	private String langMode;		// 言語モード
	
	private String ankenNo;		// 案件No.
	private String phase;			// フェーズ
	private String ym;				// 年月
	
	private final String UKETORI_TEGATA = "01";			// 受取手形
	private final String YUSYUTU_UKETORI_TEGATA = "02";	// 輸出受取手形
	private final String URIKAKEKIN = "03";				// 売掛金
	private final String TORIHIKI_MAEWATASHIKIN = "04";	// 取引前渡金
	private final String TATEKAEKIN = "05";				// 立替金
	private final String MISYU_NYUKIN = "06";				// 未収入金
	private final String MISYU_SYUEKI = "07";				// 未収収益
	private final String TANKI_KASHITUKEKIN = "08";		// 短期貸付金
	private final String SASHIIRE_HOSYOKIN = "09";			// 差入保証金
	private final String KARIBARAIKIN = "10";				// 仮払金
	private final String TYOKI_KASHITUKEKIN = "11";		// 長期貸付金
	private final String SONOTA_TOUSHI = "12";				// その他投資
	private final String RYUHO_SAIMU = "13";
	private final String HOSYO_SAIMU = "14";				// 保証債務
	private final String KI_HIKIATEKIN = "15";				// 既引当金
	private final String KOTEIKA_EIGYO_SAIKEN = "16";		// 固定化営業債権
	
	private final String JAPANESE = "Ja";					// 日本語
	private final String ENGLISH = "En";					// 英語
	
	private final String ON_CHECK = "1";					// チェック有
	
	private final String KAMIKI_LAST_MONTH = "09";			// 上期最終月
	private final String SHIMOKI_LAST_MONTH = "03";		// 下期最終月
	
	private final String KAMIKI = "1";						// 上期
	private final String SHIMOKI = "2";					// 下期
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public KensyoDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) throws SQLException {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//ビーン取得
		cmnData = appContext.getCMNZen();
		form = (KensyoForm)appContext.getActionForm();

		//ビーンの値を変数に設定

		//課題No.152
		//追加開始
		userBean = appContext.getCMN().getUser_bean();
		if(!GS.EMPTY_CHARCTER.equals(Function.trim(userBean.getComDaiko_userId()))){
			userId = userBean.getComDaiko_userId();
		}else{
			userId = userBean.getComUserId();
		}
		//追加完了
		
		honbuCd = cmnData.getComTantoCd();
		// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
		//sateiKaishaCd = cmnData.getComSateiKaishaCd();
		sateiKaishaCd = cmnData.getAnken_satei_kaisya_cd();
		bumonCd = cmnData.getComBumonCd();
		buCd = cmnData.getComBuCd();
		kaCd = cmnData.getComKaCd();
		langMode = cmnData.getComLangMode();
		
		ankenNo = form.getAnken_no();
		phase = form.getPhase();
		ym = form.getYm();
	}

	/**
	 * 検索SQL実行処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void execute() throws SQLException {
		
		ResultSet rs = null;

		// ヘッダ表示用部品取得
		StringBuffer sql = new StringBuffer()
						  .append("SELECT distinct TT.TOGO_TORI_CD")
						  .append(" FROM SST_SATEI_STAT SS,")
						  .append("SSE_TAIHI TT")
						  .append(" WHERE SS.anken_no='")
						  .append(ankenNo)
						  .append("' and trim(SS.kikan_tori_cd) || '00'=trim(TT.KIKAN_TORI_CD)")
						  .append(" and SS.ym=TT.ym ")
						  .append(" and SS.mise_cd=TT.office_cd ");

		// SQL実行		
		try {		
			rs = sqlExec.execQuery(sql.toString());
			while (rs.next()) {
				form.setTxtNoDuns(rs.getString("TOGO_TORI_CD"));
			}
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
				}
			}	
			
			// 親会社情報取得
			sql = null;
			sql = new StringBuffer();
			
			sql.append(" SELECT");
			sql.append(" SS.SASI_TEN_FLG,");
			sql.append(" SI.tairyu_kbn,");
			// 課題No.60 引当金検証時の取引先区分・債権区分設定
			// 追加開始
			sql.append(" NVL(SI.henkogo_torihikisaki_kbn,SI.torihikisaki_kbn) as torihikisaki_kbn,");
			sql.append(" NVL(SI.henkogo_saiken_kbn,SI.saiken_kbn) as saiken_kbn,");
			// 追加完了
			sql.append(" SK.ktk,");
			sql.append(" SK.oya_ktk,");
			sql.append(" SK.oya_ittai_flg,");
			sql.append(" SK.oya_dokuritu_flg,");
			sql.append(" TRIM(SK.oya_duns_no) oya_duns_no,");
			sql.append(" TM.BUSINESS_NM_KJ oyaBusinessNm_kj,");
			sql.append(" TM.BUSINESS_NM oyaBusinessNm_en");
			sql.append(" FROM ");
			sql.append(" SST_SATEI_STAT SS ");
			sql.append(" LEFT OUTER JOIN SST_SATEI SI");
			sql.append(" ON SS.ANKEN_NO = SI.anken_no");
			sql.append(" AND SI.phase='70'");
			sql.append(" LEFT OUTER JOIN SSE_TAIHI ST");
			sql.append(" ON SS.YM = ST.YM");
			sql.append(" AND SS.MISE_CD = ST.OFFICE_CD");
			sql.append(" AND SS.SYSTEM_KBN = ST.SYSTEM_KBN");
			sql.append(" AND CONCAT(TRIM(SS.KIKAN_TORI_CD), '00') = ST.KIKAN_TORI_CD");
			sql.append(" LEFT OUTER JOIN SSE_KTK SK");
			sql.append(" ON ST.YM = SK.YM");
			sql.append(" AND ST.SIKIBETU_CD = SK.SIKIBETU_CD");
			sql.append(" AND ST.TOGO_TORI_CD = SK.DUNS_NO");
			sql.append(" AND ST.SYORI_KAISU = SK.SYORI_KAISU");
			sql.append(" LEFT OUTER JOIN TM_TOGO_MST@VIR_SJLMA TM");
			sql.append(" ON SK.OYA_DUNS_NO = TM.TOGO_TORI_CD");
			sql.append(" WHERE SS.ANKEN_NO = ");
			sql.append("'").append(ankenNo).append("'");


			
			rs = sqlExec.execQuery(sql.toString());
			InputCheck check = new InputCheck();
			while (rs.next()) {
				form.setHidFlgSasiTen(rs.getString("SASI_TEN_FLG"));
				form.setShowKbnTairyu(getKbnVal("tairyu_kbn",langMode,rs.getString("tairyu_kbn")));
				form.setTxtKtk(rs.getString("ktk"));
				form.setTxtNoOyaDuns(rs.getString("oya_duns_no"));
				form.setTxtOyaKtk(rs.getString("oya_ktk"));
				if (langMode.equals("Ja") && !(check.isNullBlank(rs.getString("oyaBusinessNm_kj")))) {
					form.setTxtNameOya(rs.getString("oyaBusinessNm_kj"));
				} else {
					form.setTxtNameOya(rs.getString("oyaBusinessNm_en"));
				}
				form.setTxtFlgIttai(rs.getString("oya_ittai_flg"));
				form.setTxtFlgDokuritu(rs.getString("oya_dokuritu_flg"));
				
				// 課題No.60 引当金検証時の取引先区分・債権区分設定
				// 追加開始
				form.setFinalKbnTorihiki(rs.getString("torihikisaki_kbn"));
				form.setFinalKbnSaiken(rs.getString("saiken_kbn"));
				// 追加完了
			}
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
				}
			}	
			
			// 承認担当者取得
			//1.5次版機能組込対応～SQL変更～
			//String userNm = "YM.USER_NM AS USER_NM_KJ,YM.first_nm || ' ' || YM.last_nm AS USER_NM,YM.LAST_NM";

			/*StringBuffer tanto_cd = new StringBuffer();
			Iterator itr = honbuCd.iterator();
			while(itr.hasNext()){
				tanto_cd.append("'");
				tanto_cd.append(itr.next());
				tanto_cd.append("',");
			}*/
			
			
			//1.5次版機能組込対応～SQL変更～
			//StringBuffer togo_id_sql = new StringBuffer()
									//.append("(SELECT YS.TOGO_ID")
									//.append(" FROM SSM_USERSANSYOUSOSIKI YS")
									//.append(" WHERE YS.SATEI_KAISHA_CD='")
									// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
									//.append(cmnData.getComSateiKaishaCd())
									//.append(cmnData.getAnken_satei_kaisya_cd())
									//.append("' and YS.TANTOU_BUMON_CD ='")
									//.append(tanto_cd.toString().substring(0,tanto_cd.toString().length()-1))
									//.append(cmnData.getSatei_bumon_cd())
									//.append("') tmp_YS");


			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// StringBufferの初期化
			//1.5次版機能組込対応～SQL変更～
			sql = null;
			sql = new StringBuffer().append("SELECT US.togo_id,")
										.append("UM.USER_NM AS USER_NM_KJ,")
										.append("UM.first_nm || ' ' || UM.last_nm AS USER_NM,")
										.append("UM.LAST_NM ")
										.append("FROM SSM_USERSANSYOUSOSIKI US ")
										.append("INNER JOIN SSM_USERGYOMU UG ")
										.append("ON UG.togo_id = US.togo_id ")
										.append("AND UG.sateikaisya_cd = US.satei_kaisha_cd ")
										.append("INNER JOIN SSM_OPERATION OP ")
										.append("ON OP.system_kbn = UG.system_kbn ")
										.append("AND OP.sateikaisya_cd = UG.sateikaisya_cd ")
										.append("AND OP.pattern_id = UG.pattern_id ")
										.append("INNER JOIN SSM_TM_USER_MST UM ")
										.append("ON UM.togo_id = US.togo_id ")
										.append("WHERE ")
										.append("US.satei_kaisha_cd = '")
										.append(appContext.getCMN().getTori_bean().getSateikaisya_cd());
			// 査定会社により取得条件変更
			if ( GS.SATEIKAISYA_SJ.equals(appContext.getCMN().getTori_bean().getSateikaisya_cd()) ) {
				// 双日
				sql.append("' AND US.tantou_honbu_cd IN ((SELECT DISTINCT VH.HONBU_CD ")
					.append("FROM VW_SS_HONBU VH ")
					.append("WHERE VH.BU_CD = '")
					.append(Function.paddingRight(appContext.getCMN().getTori_bean().getBu_cd(), GS.SPACE_CHARCTER, 2)) 
					.append("' AND VH.YM = '")
					.append(ym)
					.append("') ,'  ') ");
			}
			else {
				// 双日以外
				sql.append("' AND US.tantou_bumon_cd IN ('")
					.append(Function.paddingRight(appContext.getCMN().getTori_bean().getBunrui2(), GS.SPACE_CHARCTER, 7))
					.append("' ,'       ') ");
			}
			sql.append("AND OP.jishi_phase = '70' ")
				.append("AND OP.kaishi_status = '30' ")
				.append("GROUP BY US.togo_id, UM.user_nm, UM.first_nm, UM.last_nm ")
				.append("ORDER BY PG_SS_FUNCTION.SF_SS_ISLANG(UM.user_nm, UM.last_nm,'")
				.append(cmnData.getComLangMode())
				.append("') ASC");
			//sql = new StringBuffer().append("SELECT distinct tmp_YS.TOGO_ID,")
			//	.append(userNm)
			//	.append(" FROM TM_USER_MST@VIR_SJLMA YM,")
			//	.append("SSM_USERLEVEL YL,")
			//	.append(togo_id_sql)
			//	.append(" WHERE tmp_YS.TOGO_ID=YL.togo_id")
			//	.append(" and YL.eigyou_kaikei_syounin_kengen='Y'")
			//	.append(" and tmp_YS.TOGO_ID=YM.TOGO_ID");
			// No601, 2008/06/06, SJA渡辺, 日本語モード時は漢字順でソートするように修正
			//if (JAPANESE.equals(cmnData.getComLangMode())) {
			//	sql.append(" order by USER_NM_KJ asc");
			//} else {
			//	sql.append(" order by YM.LAST_NM asc");
			//}
			//StringBuffer tanto_cd = new StringBuffer();
			
			/*
			sql = new StringBuffer().append(" SELECT ")
									.append(userNm).append(",")
									.append(" YL.togo_id, ")
									.append(" YM.SOSHIKI_CD ")
									.append(" FROM TM_USER_MST@VIR_SJLMA YM, ")
									.append(" SSM_USERLEVEL YL ")
									.append(" WHERE YM.TOGO_ID=YL.togo_id ")
									.append(" and YL.eigyou_kaikei_syounin_kengen='Y'" )
									.append(" and YM.SOSHIKI_CD='" )
									.append(kaCd)
									.append("'");
			*/
			
			rs = sqlExec.execQuery(sql.toString());
			List recoName = new ArrayList(1 + getRsCount(rs));
			
			HashMap firstMap = new HashMap();
			firstMap.put("id","");
			firstMap.put("USER_NM","");
			firstMap.put("togo_id","");
			firstMap.put("SOSHIKI_CD","");
			recoName.add(0, firstMap);
			
			int i = 1;
			while (rs.next()) {
				HashMap map = new HashMap();
				map.put("id",Function.getStringOfInt(i));
				if (JAPANESE.equals(cmnData.getComLangMode()) && !(check.isNullBlank(rs.getString("USER_NM_KJ")))) {
					map.put("USER_NM",rs.getString("USER_NM_KJ"));
				} else {
					map.put("USER_NM",rs.getString("USER_NM"));
				}
				map.put("togo_id",rs.getString("togo_id"));
				map.put("SOSHIKI_CD",cmnData.getComBumonCd());
				
				recoName.add(i, map);
				i++;
			}
			form.setRecoName(recoName);
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
				}
			}	
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// StringBufferの初期化
			sql = null;
			// 初回月・中間月表示用取得
			// 査定案件No,年月取得
			// 課題No.60 引当金検証時の取引先区分・債権区分設定
			// 削除開始
			/*sql = new StringBuffer().append("SELECT distinct HH.satei_anken_no,")
									.append("HH.ym")
									.append(" FROM SST_HIKIATEHANTEI HH ")
									.append(" WHERE HH.system_kbn='" )
									.append(form.getSystem_kbn())
									.append("' and HH.sateikaisya_cd='")
									// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
									//.append(cmnData.getComSateiKaishaCd())
									.append(cmnData.getAnken_satei_kaisya_cd())
									.append("' and HH.mise_cd='" )
									.append(form.getMise_cd())
									.append("' and HH.syori_kaisu='0'")
									.append(" and HH.tori_cd='")
									.append(form.getTxtCdTorihiki())
									//結合テスト障害No38対応
									//追加開始
									.append("' and HH.hanki_sihanki_kbn = '")
									.append(form.getHanki_sihanki_kbn())
									.append("' and HH.ym < '")
									.append(ym)
									.append("' ORDER BY HH.ym DESC");*/
			// 削除完了
			// 追加開始
			sql = new StringBuffer().append("SELECT SS.anken_no as satei_anken_no,")
			// 課題No.181
			// 追加開始
			//.append("SS.ym")
			.append(" PG_SS_FUNCTION.SF_SS_GETNENGETSUSLASH(SS.ym,'")
			.append(langMode)
			.append("') AS ym ")
			// 追加完了
			.append(" FROM SST_SATEI_STAT SS ")
			.append(" WHERE SS.system_kbn = '" )
			.append(form.getSystem_kbn())
			.append("' and SS.satei_kaisha_cd = '")
			.append(cmnData.getAnken_satei_kaisya_cd())
			.append("' and SS.mise_cd='" )
			.append(form.getMise_cd())
			.append("' and SS.syori_kaisu = '0'")
			.append(" and SS.phase = '60'")
			.append(" and SS.status = '40'")
			.append(" and SS.kikan_tori_cd = '")
			.append(form.getTxtCdTorihiki())
			.append("' and SS.hanki_sihanki_kbn = '")
			.append(form.getHanki_sihanki_kbn())
			.append("' and SS.ym < '")
			.append(ym)
			.append("' ORDER BY SS.ym DESC");
			// 追加完了
			rs = sqlExec.execQuery(sql.toString());
			if (rs.next()) {
				form.setSateiAnkenNo(rs.getString("satei_anken_no"));
				form.setTxtYmSatei(rs.getString("ym"));
			}
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
				}
			}	
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// StringBufferの初期化
			sql = null;
			// 取引先区分判定、債権区分判定、表示用金額取得
			sql = new StringBuffer().append("SELECT ST.ryuhosaimu,")
									.append("ST.oth_ryuhosaimu,")
									.append("ST.hozen,")
									.append("ST.sonotakaisyu,")
									.append("ST.riko_kenen,")
									.append("ST.tuika_hikiate,")
									.append("ST.saiken_kbn,")
									// 課題No.73
									// 修正開始
									.append("ST.torihikisaki_kbn")
									//.append("ST.seijo_chk,")
									//.append("ST.yochui_chk,")
									//.append("ST.tyoka_chk,")
									//.append("ST.kanwa_chk,")
									//.append("ST.entai_chk,")
									//.append("ST.hasanho_chk,")
									//.append("ST.kaishaho_chk,")
									//.append("ST.koseho_chk,")
									//.append("ST.saiseho_chk,")
									//.append("ST.shobun_chk,")
									//.append("ST.sonota_chk")
									// 修正完了
									.append(" FROM SST_SATEI ST,")
									.append("SST_SATEI_STAT SS")
									.append(" WHERE ST.anken_no=SS.anken_no")
									.append(" and SS.anken_no='")								
									.append(form.getSateiAnkenNo())
									.append("' and ST.phase=SS.phase");
			
			rs = sqlExec.execQuery(sql.toString());
			while (rs.next()) {
				form.setRyuhosaimu(getCommaValue(rs.getString("ryuhosaimu")));
				form.setRyuhosaimu3(getCommaValue(rs.getString("oth_ryuhosaimu")));
				form.setHozen(getCommaValue(rs.getString("hozen")));
				form.setKingakuSonota(getCommaValue(rs.getString("sonotakaisyu")));
				form.setKingakuKenen(getCommaValue(rs.getString("riko_kenen")));
				form.setKingakuTuika(getCommaValue(rs.getString("tuika_hikiate")));
				form.setTxtKbnSaiken(rs.getString("saiken_kbn"));
				// 課題No.60,73
				// 修正開始
				form.setTxtKbnTorihiki(rs.getString("torihikisaki_kbn"));
				/*if (ON_CHECK.equals(rs.getString("seijo_chk"))
						|| ON_CHECK.equals(rs.getString("yochui_chk"))) {
					form.setTxtKbnTorihiki("1");
				} else if (ON_CHECK.equals(rs.getString("tyoka_chk"))
						|| ON_CHECK.equals(rs.getString("kanwa_chk"))
						|| ON_CHECK.equals(rs.getString("entai_chk"))) {
					form.setTxtKbnTorihiki("2");
				} else {
					form.setTxtKbnTorihiki("3");
				}*/
				// 修正完了
				
				// 一次保存されていない場合ようのデータ設定
				form.setFinalRyuhosaimu(getCommaValue(rs.getString("ryuhosaimu")));
				form.setFinalRyuhosaimu3(getCommaValue(rs.getString("oth_ryuhosaimu")));
				form.setFinalHozen(getCommaValue(rs.getString("hozen")));
				form.setFinalKingakuSonota(getCommaValue(rs.getString("sonotakaisyu")));
				form.setFinalKingakuKenen(getCommaValue(rs.getString("riko_kenen")));
				// 課題No.60 引当金検証時の取引先区分・債権区分設定
				// 削除開始
				//form.setFinalKbnSaiken(rs.getString("saiken_kbn"));
				// 削除開始
			}
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
				}
			}	
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// StringBufferの初期化
			sql = null;
			// 金額取得
			sql = new StringBuffer().append("SELECT HH.kingaku,")
									.append("HH.tuuka_cd,")
									.append("HH.kanjo_hyouji_kbn")
									.append(" FROM SST_HIKIATEHANTEI HH")
									.append(" WHERE HH.satei_anken_no='")
									.append(form.getSateiAnkenNo())
									//案件 No.D5938 修正開始 Java側Functionで"/"の削除、Oracle側Functionで年月表記の変換
									//.append("' and HH.ym='")
									//.append(form.getTxtYmSatei())
									//.append("' and HH.tori_cd='")
									.append("' and HH.ym=")
									.append(" PG_SS_FUNCTION.SF_SS_GETNENGETSU_REVERSE('")
									.append(Function.removeDateSlash(form.getTxtYmSatei()))
									.append("','")
									.append(langMode)
									.append("')")
									.append(" and HH.tori_cd='")
									//案件 No.D5938 修正完了
									.append(form.getTxtCdTorihiki())
									.append("' and HH.system_kbn='" )
									.append(form.getSystem_kbn())
									.append("' and HH.mise_cd='" )
									.append(form.getMise_cd())
									.append("' and HH.sateikaisya_cd='")
									// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
									//.append(cmnData.getComSateiKaishaCd())
									.append(cmnData.getAnken_satei_kaisya_cd())
									.append("' and HH.syori_kaisu='0'");
			
			rs = sqlExec.execQuery(sql.toString());
			while (rs.next()) {
				form.setTxtNameTuuka(Function.trim(rs.getString("tuuka_cd")));
				
				if (UKETORI_TEGATA.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku01(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (YUSYUTU_UKETORI_TEGATA.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku02(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (URIKAKEKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku03(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (TORIHIKI_MAEWATASHIKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku04(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (TATEKAEKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku05(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (MISYU_NYUKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku06(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (MISYU_SYUEKI.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku07(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (TANKI_KASHITUKEKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku08(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (SASHIIRE_HOSYOKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku09(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (KARIBARAIKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku10(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (TYOKI_KASHITUKEKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku11(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (SONOTA_TOUSHI.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku12(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (HOSYO_SAIMU.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku14(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (KI_HIKIATEKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku15(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (KOTEIKA_EIGYO_SAIKEN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setKingaku16(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else {
					
				}
			}
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
				}
			}	
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// StringBufferの初期化
			sql = null;
			// コメント取得
			sql = new StringBuffer().append("SELECT CM.comment_val")
									.append(" FROM SST_COMMENT CM,")
									.append("SST_SATEI_STAT SS")
									.append(" WHERE CM.anken_no=SS.anken_no")
									.append(" and CM.phase=SS.phase")
									.append(" and SS.anken_no='")
									.append(form.getSateiAnkenNo())
									.append("' and CM.toroku_point='80'");
			
			rs = sqlExec.execQuery(sql.toString());
			while (rs.next()) {
				form.setCmtValKonkyo(rs.getString("comment_val"));
			}
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
				}
			}	
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// StringBufferの初期化
			sql = null;
			// 最終月表示用(一次二次査定テーブルから取得する値は、初回・中間月のデータ)
			sql = new StringBuffer().append("SELECT HS.kingaku,")
									.append("HS.tuuka_cd,")
									.append("HS.kanjo_hyouji_kbn")
									.append(" FROM SST_KENSYOUSUM HS")
									.append(" WHERE HS.satei_anken_no='")
									.append(ankenNo)						// 査定案件No
									.append("' and HS.ym='")				// 年月
									.append(ym)
									.append("' and HS.tori_cd='")			// 取引先コード
									.append(form.getTxtCdTorihiki())
									.append("' and HS.syori_kaisu='1'")		// 処理回数(仮〆)
									.append(" and HS.system_kbn='" )		// システム区分
									.append(form.getSystem_kbn())
									.append("' and HS.mise_cd='" )			// 店コード
									.append(form.getMise_cd())
									.append("' and HS.sateikaisya_cd='")	// 査定会社コード
									// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
									//.append(cmnData.getComSateiKaishaCd())
									.append(cmnData.getAnken_satei_kaisya_cd())
									.append("'");
			
			
			rs = sqlExec.execQuery(sql.toString());
			while (rs.next()) {
				// modify tuuka_cd 2008/4/15 nakajima
				form.setTxtNameFinalTuuka(Function.trim(rs.getString("tuuka_cd")));
				
				if (UKETORI_TEGATA.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku01(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (YUSYUTU_UKETORI_TEGATA.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku02(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (URIKAKEKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku03(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (TORIHIKI_MAEWATASHIKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku04(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (TATEKAEKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku05(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (MISYU_NYUKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku06(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (MISYU_SYUEKI.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku07(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (TANKI_KASHITUKEKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku08(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (SASHIIRE_HOSYOKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku09(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (KARIBARAIKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku10(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (TYOKI_KASHITUKEKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku11(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (SONOTA_TOUSHI.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku12(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (HOSYO_SAIMU.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku14(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (KI_HIKIATEKIN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku15(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else if (KOTEIKA_EIGYO_SAIKEN.equals(rs.getString("kanjo_hyouji_kbn"))) {
					form.setFinalKingaku16(Function.format("###,###,###,##0.##", Function.getValueOfDouble(rs.getString("kingaku"))));
				} else {
					
				}
			}
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
				}
			}	
			
			// 一次保存された場合、一次保存時のデータ取得
			if (isIchijiSave()) {
				///////////////////////////////////////
				//障害票：465
				//チェックイン日：2008/5/25
				//対応者：上田
				//概要：ResultSetの循環使用対応
				////////////////////////////////////////
				// StringBufferの初期化
				sql = null;
				sql = new StringBuffer().append("SELECT ST.ryuhosaimu,")
										// 課題No.60 引当金検証時の取引先区分・債権区分設定
										// 追加開始
										.append("NVL(ST.henkogo_torihikisaki_kbn,ST.torihikisaki_kbn) as torihikisaki_kbn,")
										.append("NVL(ST.henkogo_saiken_kbn,ST.saiken_kbn) as saiken_kbn,")
										// 追加完了
										.append("ST.oth_ryuhosaimu,")
										.append("ST.hozen,")
										.append("ST.sonotakaisyu,")
										.append("ST.riko_kenen,")
										.append("ST.hikiate_hosei")
										.append(" FROM SST_SATEI ST")
										.append(" WHERE ST.anken_no='")
										.append(ankenNo)
										.append("' and ST.phase='70'");
				
				rs = sqlExec.execQuery(sql.toString());
				while (rs.next()) {
					// 課題No.60 引当金検証時の取引先区分・債権区分設定
					// 追加開始
					form.setFinalKbnTorihiki(rs.getString("torihikisaki_kbn"));
					form.setFinalKbnSaiken(rs.getString("saiken_kbn"));
					// 追加完了
					form.setFinalRyuhosaimu(getCommaValue(rs.getString("ryuhosaimu")));
					form.setFinalRyuhosaimu3(getCommaValue(rs.getString("oth_ryuhosaimu")));
					form.setFinalHozen(getCommaValue(rs.getString("hozen")));
					form.setFinalKingakuSonota(getCommaValue(rs.getString("sonotakaisyu")));
					form.setFinalKingakuKenen(getCommaValue(rs.getString("riko_kenen")));
					form.setFinalKingakuHosei(getCommaValue(rs.getString("hikiate_hosei")));
				}
				///////////////////////////////////////
				//障害票：465
				//チェックイン日：2008/5/25
				//対応者：上田
				//概要：ResultSetの循環使用対応
				////////////////////////////////////////
				if (rs != null) {
					try {
						//Resultset close
						rs.close();
					} catch (Exception e) {
		    			throw new SQLException(e.getMessage());
					}
				}	
				
				///////////////////////////////////////
				//障害票：465
				//チェックイン日：2008/5/25
				//対応者：上田
				//概要：ResultSetの循環使用対応
				////////////////////////////////////////
				// StringBufferの初期化
				sql = null;
				sql = new StringBuffer().append("SELECT CM.comment_val")
										.append(" FROM SST_COMMENT CM")
										.append(" WHERE CM.anken_no='")
										.append(ankenNo)
										.append("' and CM.phase='70'")
										.append(" and CM.toroku_point='00'");
				
				rs = sqlExec.execQuery(getCmtSelectSql());
				while (rs.next()) {
					form.setFinalCmtValKonkyo(rs.getString("comment_val"));
				}
				///////////////////////////////////////
				//障害票：465
				//チェックイン日：2008/5/25
				//対応者：上田
				//概要：ResultSetの循環使用対応
				////////////////////////////////////////
				if (rs != null) {
					try {
						//Resultset close
						rs.close();
					} catch (Exception e) {
		    			throw new SQLException(e.getMessage());
					}
				}	
			}
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// StringBufferの初期化
			sql = null;
			// 債権セレクトボックス値取得
			sql = new StringBuffer().append(this.getKbnSql("saiken_kbn",cmnData.getComLangMode()));
			
			rs = sqlExec.execQuery(sql.toString());
			
			LinkedHashMap saikenKbn = new LinkedHashMap(getRsCount(rs));
			
			while ( rs.next() ) {
				
				String kbn_val = Function.trim(rs.getString("kbn_val"));
				String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
				
				saikenKbn.put(kbn_hyouji_val, kbn_val );
			} // while
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
				}
			}	
			
			form.setSaikenList(saikenKbn);
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// StringBufferの初期化
			sql = null;
			// 取引先セレクトボックス値取得
			//課題No.73
			//修正開始
			//sql = new StringBuffer().append(this.getKbnSql("torihikisaki_kbn",cmnData.getComLangMode()));
			sql = new StringBuffer().append(this.getKbnSql("torihikisaki2_kbn",cmnData.getComLangMode()));
			//修正完了
			
			rs = sqlExec.execQuery(sql.toString());
			
			LinkedHashMap torihikiKbn = new LinkedHashMap(getRsCount(rs));
			
			while ( rs.next() ) {
				
				String kbn_val = Function.trim(rs.getString("kbn_val"));
				String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
				
				torihikiKbn.put(kbn_hyouji_val, kbn_val );
			} // while
			
			form.setTorihikiList(torihikiKbn);
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
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
	}
	
	/*
	 * フォーマットした文字列を返す（NULLの場合はそのまま返す）
	 */
	private String getCommaValue(String val) {
		String result = val;
		
		if (val != null && val.length() > 0) {
			result = Function.format("###,###,###,##0", Function.getValueOfLong(val));
		}
		
		return result;
	}
	
	
	/**
	 * 一時保存されたことがあるか判定
	 * 
	 * @exception SQLException
	 */
	private boolean isIchijiSave() throws SQLException {
		
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer().append("SELECT COUNT(CM.anken_no) AS cnt")
											.append(" FROM SST_COMMENT CM")
											.append(" WHERE CM.anken_no='")
											.append(ankenNo)
											.append("' and CM.phase='70'")
											.append(" and CM.toroku_point='00'");
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			boolean result = false;
			if (rs.next()) {
				if(rs.getInt("cnt") > 0) {
					result = true;
				}
			}
			
			return result;
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
	 * 区分表示値
	 * @param kbnKey 区分キー
	 * @param langMode 言語モード
	 * @param kbnVal 区分値
	 * @return 区分表示値
	 * @throws SQLException
	 */
	public String getKbnVal(String kbnKey,String langMode,String kbnVal) throws SQLException {
		
		ResultSet rs = null;
		
		String result = "";
		
		StringBuffer sql = new StringBuffer().append("SELECT KB.kbn_hyouji_val")
											.append(" FROM SSP_KBN KB")
											.append(" WHERE KB.kbn_key='")
											.append(kbnKey)
											.append("' and KB.lang_mode='")
											.append(langMode)
											.append("' and KB.kbn_val='")
											.append(kbnVal)
											.append("' and KB.system_kbn='")
											.append(cmnData.getSystem_kbn())
											.append("'");
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			while (rs.next()) {
				result = rs.getString("kbn_hyouji_val");
			}
			
			return result;
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
	 * 一次保存処理。一次二次査定テーブルとコメントテーブルを更新する。
	 * @return true:更新処理成功　false:更新処理失敗
	 * @throws Exception
	 */
	// 障害表：540　チェックイン日：2008/6/3　SJA中島　一次保存の入力履歴は、一次保存ボタン押下時のみとする。
	public boolean firstPreserveExecute(boolean isNyuryokuFlg) throws Exception {
		// 処理結果フラグ
		boolean result = false;
		
		ResultSet rs = null;
		///////////////////////////////////////
		//障害票：465
		//チェックイン日：2008/5/26
		//対応者：SJA中島
		//概要：カーソルOPEN問題対応
		////////////////////////////////////////
		PreparedStatement ps1 = null;		// 更新処理用PreparedStatement
		PreparedStatement ps2 = null;		// 更新処理用PreparedStatement
		PreparedStatement ps3 = null;		// 更新処理用PreparedStatement
		
		// 更新値設定
		// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
		//String kaisya = cmnData.getComSateiKaishaCd();
		String kaisya = cmnData.getAnken_satei_kaisya_cd();
		String sysdate = sqlExec.getDate();
		Timestamp timestamp = sqlExec.getSystemDate();
		String tanto_nm_ja = cmnData.getComTanto_User_Nm();
		String tanto_nm_en = cmnData.getComTanto_User_Nm_En();
		String syozoku_ja = cmnData.getComSoshiki_Nm();
		String syozoku_en = cmnData.getComSoshiki_Nm_En();
		
		StringBuffer sql = new StringBuffer().append("UPDATE SST_SATEI")
											.append(" SET ryuhosaimu='")
											.append(Function.removeComma(form.getFinalRyuhosaimu()))
											.append("',oth_ryuhosaimu='")
											.append(Function.removeComma(form.getFinalRyuhosaimu3()))
											.append("',hozen='")
											.append(Function.removeComma(form.getFinalHozen()))
											.append("',sonotakaisyu='")
											.append(Function.removeComma(form.getFinalKingakuSonota()))
											.append("',riko_kenen='")
											.append(Function.removeComma(form.getFinalKingakuKenen()))
											.append("',hikiate_hosei='")
											.append(Function.removeComma(form.getFinalKingakuHosei()))
											//課題No.60 引当金検証時の取引先区分・債権区分設定
											//追加開始
											.append("',henkogo_torihikisaki_kbn='").append(form.getFinalKbnTorihiki())
											.append("',henkogo_saiken_kbn='").append(form.getFinalKbnSaiken())
											//追加完了
											.append("',upd_user='").append(userId)
											.append("',upd_dt=TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
											.append(" WHERE anken_no='").append(ankenNo)
											.append("' and phase='70'");
		
		try {
			
			rs = sqlExec.execQuery(getCmtSelectSql());
			if (rs.next()) {
				form.setCmt00(true);
			}
			
			// コネクションの取得
		    Connection con = sqlExec.beginTranP();
			
			// PreparedStatementの作成/実行
	    	ps1 = con.prepareStatement(sql.toString());
	    	result = sqlExec.execBatchP(ps1, false, true);			// commitなし
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// StringBufferの初期化
			sql = null;
			if(isNyuryokuFlg){
				sql = new StringBuffer().append("insert into SST_NYURYOKU_HIST VALUES ('")
										.append(ankenNo).append("',TO_TIMESTAMP('")
										.append(timestamp).append("', 'yyyy-mm-dd hh24:mi:ssxff'),'2','")
										.append(kaisya).append("','")
										//課題No.152
										//修正開始
										.append(userBean.getComUserId()).append("','")
										//.append(userId).append("','")
										//修正完了
										.append(tanto_nm_ja).append("','")
										.append(tanto_nm_en).append("','")
										.append(syozoku_ja).append("','")
										.append(syozoku_en).append("',")
										.append("'70','20','")
										.append(userId).append("',TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
										.append(",'")
										.append(userId).append("',TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
										//課題No.152
										//修正開始
										.append(",'").append(Function.trim(userBean.getComDaiko_userId())).append("','").append(Function.trim(userBean.getComDaiko_user_nm())).append("','").append(Function.trim(userBean.getComDaiko_user_nm_en())).append("','','','','','')");
										//.append(",'','','','','','','','')");
										//修正完了
				// PreparedStatementの作成/実行
				ps2 = con.prepareStatement(sql.toString());
				result = sqlExec.execBatchP(ps2, false, result);			// commitなし
			}
			
			if (form.isCmt00()) {		
				// PreparedStatementの作成/実行
		    	ps3 = con.prepareStatement("UPDATE SST_COMMENT SET " +
												"comment_val= ?, " +
												"upd_user= ?, " +
												"upd_dt=TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss') " +
											"WHERE " +
												"anken_no= ? and " +
												"phase= ? and " +
												"toroku_point= ?");
		    	ps3.setString(1, form.getFinalCmtValKonkyo());
		    	ps3.setString(2, userId);
		    	ps3.setString(3, sysdate);
		    	ps3.setString(4, ankenNo);
		    	ps3.setString(5, GS.PHASE_HIKIATEKIN_KENSYO);		// phase
		    	ps3.setString(6, "00");		// toroku_point
		    	result = sqlExec.execBatchP(ps3, false, result);			// commitなし	
			} else {
				// PreparedStatementの作成/実行
		    	ps3 = con.prepareStatement("INSERT INTO SST_COMMENT CM VALUES (" +
											"?, ?, ?, ?, ?, " +
											"?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'), " +
											"?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'))");
		    	ps3.setString(1, ankenNo);
		    	ps3.setString(2, GS.PHASE_HIKIATEKIN_KENSYO);		// phase
		    	ps3.setString(3, "00");		// toroku_point
		    	ps3.setString(4, "1");		// toroku_div
		    	ps3.setString(5, form.getFinalCmtValKonkyo());
		    	ps3.setString(6, userId);
		    	ps3.setString(7, sysdate);
		    	ps3.setString(8, userId);
		    	ps3.setString(9, sysdate);
		    	result = sqlExec.execBatchP(ps3, false, result);			// commitなし
			}
			
			if (result) {
				sqlExec.commit();
				form.setCmt00(true);
			}
			
			return result;
			
		} finally {
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
	    			throw new Exception(e.getMessage());
				}
			}
			if (ps1 != null) {
				try {
					ps1.close();
				} catch (Exception e) {
	    			throw new Exception(e.getMessage());
				}
			}
			if (ps2 != null) {
				try {
					ps2.close();
				} catch (Exception e) {
	    			throw new Exception(e.getMessage());
				}
			}
			if (ps3 != null) {
				try {
					ps3.close();
				} catch (Exception e) {
	    			throw new Exception(e.getMessage());
				}
			}
		}
		
	}
	
	/**
	 * 登録処理実行。
	 * @return	true：登録処理成功　false：登録処理失敗
	 * @throws Exception
	 */
	public boolean registerExecute() throws Exception {
		// 処理結果フラグ
		boolean result = false;
		
		ResultSet rs = null;
		///////////////////////////////////////
		//障害票：465
		//チェックイン日：2008/5/26
		//対応者：SJA中島
		//概要：カーソルOPEN問題対応
		////////////////////////////////////////
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;

		// 更新値設定
		// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
		//String kaisya = cmnData.getComSateiKaishaCd();
		String kaisya = cmnData.getAnken_satei_kaisya_cd();
		String phase = form.getPhase();
		String sysdate = sqlExec.getDate();
		Timestamp timestamp = sqlExec.getSystemDate();
		String tanto_nm_ja = cmnData.getComTanto_User_Nm();
		String tanto_nm_en = cmnData.getComTanto_User_Nm_En();
		String syozoku_ja = cmnData.getComSoshiki_Nm();
		String syozoku_en = cmnData.getComSoshiki_Nm_En();
		
		try {
			
			rs = sqlExec.execQuery(getCmtSelectSql());
			if (rs.next()) {
				form.setCmt00(true);
			}
			
			// コネクションの取得
		    Connection con = sqlExec.beginTranP();
			
			// 一次二次査定テーブルへ登録
			StringBuffer sql = new StringBuffer().append("UPDATE SST_SATEI")
												.append(" SET ryuhosaimu='")
												.append(Function.removeComma(form.getFinalRyuhosaimu()))
												.append("',oth_ryuhosaimu='")
												.append(Function.removeComma(form.getFinalRyuhosaimu3()))
												.append("',hozen='")
												.append(Function.removeComma(form.getFinalHozen()))
												.append("',sonotakaisyu='")
												.append(Function.removeComma(form.getFinalKingakuSonota()))
												.append("',riko_kenen='")
												.append(Function.removeComma(form.getFinalKingakuKenen()))
												.append("',hikiate_hosei='")
												.append(Function.removeComma(form.getFinalKingakuHosei()))
												//課題No.60 引当金検証時の取引先区分・債権区分設定
												//追加開始
												.append("',henkogo_torihikisaki_kbn='").append(form.getFinalKbnTorihiki())
												.append("',henkogo_saiken_kbn='").append(form.getFinalKbnSaiken())
												//追加完了
												.append("',upd_user='").append(userId)
												.append("',upd_dt=TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
												.append(" WHERE anken_no='")
												.append(ankenNo)
												.append("' and phase='70'");

			// PreparedStatementの作成/実行
	    	ps1 = con.prepareStatement(sql.toString());
	    	result = sqlExec.execBatchP(ps1, false, true);			// commitなし
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// StringBufferの初期化
			sql = null;
			// 査定進捗管理テーブルへ登録
			sql = new StringBuffer().append("UPDATE SST_SATEI_STAT")
									.append(" SET hoji_user_id='")
									.append(form.getTantoSyonin())
									//.append("',phase='70'")
									.append("',status='30'")
									.append(",upd_user='").append(userId)
									//課題No.152
									//追加開始
									.append("',daiko_user_id='").append(Function.trim(userBean.getComDaiko_userId()))
									.append("',sasi_ten_flg = NULL")
									//追加完了
									.append(",upd_dt=TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
									//IT371対応
									.append(" ,torimodoshi_fuka_flg='0'")
									//ここまで
									.append(" WHERE anken_no='")
									.append(ankenNo)
									.append("'");

			// PreparedStatementの作成/実行
	    	ps2 = con.prepareStatement(sql.toString());
	    	result = sqlExec.execBatchP(ps2, false, result);			// commitなし
			
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// StringBufferの初期化
			sql = null;
			// メール配信テーブルへ登録。
			sql = new StringBuffer().append("INSERT INTO SST_MAILHAISHIN MH")
									.append(" VALUES ('")
									.append(ankenNo)				// 査定案件No
									.append("','")
									.append(kaisya)					// 査定会社コード
									.append("','")
									.append(ym)						// 年月
									.append("','")
									.append(phase)					// フェーズ
									.append("',")
									.append("'20'")					// ステータス(20: 承認依頼)
									.append(",'")
									//課題No.72
									//修正開始
									//.append(form.getBumonSyonin())	// 配信先部門(承認者の部門コード)
									.append(cmnData.getSatei_bumon_cd())
									.append("','")
									.append(cmnData.getSatei_bu_cd())
									//修正完了
									.append("','")
									.append(form.getTantoSyonin())	// 配信担当者(承認者の統合ID)
									.append("','")
									//課題No.152
									//修正開始
									//.append(userId)					// 依頼元担当者(ログインユーザ)
									.append(userBean.getComUserId())	// 依頼元担当者(ログインユーザ)
									//修正完了
									.append("',")
									.append("'N'")					// 配信済みフラグ(N:未配信)
									.append(",'")
									.append(userId)					// 登録ユーザID(ログインユーザ)
									.append("',")
									.append("TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")				// 登録日(SYSDATE)
									.append(",'")
									.append(userId)					// 更新ユーザID(ログインユーザ)
									.append("',")
									.append("TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")				// 更新日(SYSDATE)
									.append(")");

			// PreparedStatementの作成/実行
	    	ps3 = con.prepareStatement(sql.toString());
	    	result = sqlExec.execBatchP(ps3, false, result);			// commitなし
			
			if (form.isCmt00()) {	
				// PreparedStatementの作成/実行
		    	ps4 = con.prepareStatement("UPDATE SST_COMMENT SET " +
						"comment_val= ?, " +
						"upd_user= ?, " +
						"upd_dt= TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss') " +
					"WHERE " +
						"anken_no= ? and " +
						"phase= ? and " +
						"toroku_point= ?");
		    	ps4.setString(1, form.getFinalCmtValKonkyo());
		    	ps4.setString(2, userId);
		    	ps4.setString(3, sysdate);
		    	ps4.setString(4, ankenNo);
		    	ps4.setString(5, GS.PHASE_HIKIATEKIN_KENSYO);		// phase
		    	ps4.setString(6, "00");		// toroku_point
		    	result = sqlExec.execBatchP(ps4, false, result);			// commitなし	
			} else {
				// PreparedStatementの作成/実行
		    	/*
				ps = con.prepareStatement("INSERT INTO SST_COMMENT CM VALUES (" +
						"?, ?, ?, ?, ?, " +
						"?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'), " +
						"?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss')");
		    	*/
		    	sql = new StringBuffer().append("INSERT INTO SST_COMMENT CM VALUES (")
										.append("?, ?, ?, ?, ?, ")
										.append("?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'), ")
										.append("?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'))");
		    	ps4 = con.prepareStatement(sql.toString());
		    	ps4.setString(1, ankenNo);
		    	ps4.setString(2, GS.PHASE_HIKIATEKIN_KENSYO);		// phase
		    	ps4.setString(3, "00");		// toroku_point
		    	ps4.setString(4, "1");		// toroku_div
		    	ps4.setString(5, form.getFinalCmtValKonkyo());
		    	ps4.setString(6, userId);
		    	ps4.setString(7, sysdate);
		    	ps4.setString(8, userId);
		    	ps4.setString(9, sysdate);
		    	result = sqlExec.execBatchP(ps4, false, result);			// commitなし	
		    }
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// StringBufferの初期化
			sql = null;
			sql = new StringBuffer().append("insert into SST_NYURYOKU_HIST VALUES ('")
									.append(ankenNo)
									.append("',TO_TIMESTAMP('").append(timestamp).append("', 'yyyy-mm-dd hh24:mi:ssxff'),'2','")
									.append(kaisya).append("','")
									//課題No.152
									//修正開始
									.append(userBean.getComUserId()).append("','")
									//.append(userId).append("','")
									//修正完了
									.append(tanto_nm_ja).append("','")
									.append(tanto_nm_en).append("','")
									.append(syozoku_ja).append("','")
									.append(syozoku_en).append("',")
									.append("'70','10','")
									.append(userId)
									.append("',TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
									.append(",'")
									.append(userId)
									.append("',TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
									//課題No.152
									//修正開始
									//.append(",'','','','','','','','")
									.append(",'").append(Function.trim(userBean.getComDaiko_userId())).append("','").append(Function.trim(userBean.getComDaiko_user_nm())).append("','").append(Function.trim(userBean.getComDaiko_user_nm_en())).append("','','','','','")
									//修正完了
									.append(Function.trim(form.getTantoSyonin()))
									.append("')");
			// PreparedStatementの作成/実行
	    	ps5 = con.prepareStatement(sql.toString());
	    	result = sqlExec.execBatchP(ps5, true, result);			// commitあり
			
			return result;
			
		} finally {
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
	    			throw new Exception(e.getMessage());
				}
			}
			if (ps1 != null) {
				try {
					ps1.close();
				} catch (Exception e) {
	    			throw new Exception(e.getMessage());
				}
			}
			if (ps2 != null) {
				try {
					ps2.close();
				} catch (Exception e) {
	    			throw new Exception(e.getMessage());
				}
			}
			if (ps3 != null) {
				try {
					ps3.close();
				} catch (Exception e) {
	    			throw new Exception(e.getMessage());
				}
			}
			if (ps4 != null) {
				try {
					ps4.close();
				} catch (Exception e) {
	    			throw new Exception(e.getMessage());
				}
			}
			if (ps5 != null) {
				try {
					ps5.close();
				} catch (Exception e) {
	    			throw new Exception(e.getMessage());
				}
			}
		}
		
	}
	
	/**
	 * もぎ取り解除SQL実行処理
	 * @return
	 * @throws Exception
	 */
	public boolean releaseExecute() throws Exception {
		// 処理結果フラグ
		boolean result = false;
		// 更新値設定
		// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
		//String kaisya = cmnData.getComSateiKaishaCd();
		String kaisya = cmnData.getAnken_satei_kaisya_cd();
		String phase = form.getPhase();
		String sysdate = sqlExec.getDate();
		Timestamp timestamp = sqlExec.getSystemDate();
		String tanto_nm_ja = cmnData.getComTanto_User_Nm();
		String tanto_nm_en = cmnData.getComTanto_User_Nm_En();
		String syozoku_ja = cmnData.getComSoshiki_Nm();
		String syozoku_en = cmnData.getComSoshiki_Nm_En();
		
		
		// トランザクション開始
		sqlExec.beginTran();

		// 課題No.60 引当金検証時の取引先区分・債権区分設定
		// 追加開始
		// 一次二次査定テーブルの更新
		// 案件No.D9059 もぎ取り解除時に記入内容をクリアしない
		//StringBuffer sqlKbn = new StringBuffer().append("UPDATE SST_SATEI")
		//									.append(" SET henkogo_torihikisaki_kbn=''")
		//									.append(",henkogo_saiken_kbn=''")
		//									.append(",upd_user='").append(userId)
		//									.append("',upd_dt=TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
		//									.append(" WHERE anken_no='")
		//									.append(ankenNo)
		//									.append("' and phase='70'");
		// トランザクション内容設定
		//sqlExec.addBatch(sqlKbn.toString());
		// 追加完了

		// 査定進捗管理テーブルの更新
		StringBuffer sql1 = new StringBuffer()
							.append("update SST_SATEI_STAT")
							.append(" SET status='10',")
							.append("hoji_user_id=NULL,")
							//課題No.152
							//追加開始
							.append("daiko_user_id=NULL,")
							//追加完了
							.append("satei_gamen=NULL,")
							.append("upd_user='")
							.append(userId)
							.append("',upd_dt=TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
							.append(" where anken_no='")
							.append(ankenNo)
							.append("'");
		
		// トランザクション内容設定
		sqlExec.addBatch(sql1.toString());
		
		StringBuffer sql2 = new StringBuffer()
							.append("insert into SST_NYURYOKU_HIST VALUES ('")
							.append(ankenNo)						// 査定案件No
							.append("',")
							.append("TO_TIMESTAMP('").append(timestamp).append("', 'yyyy-mm-dd hh24:mi:ssxff')") // 処理日時
							.append(",'")
							.append("2")							// 判定査定区分(2: 一次・二次査定)
							.append("','")
							.append(kaisya)							// 査定会社コード
							.append("','")
							//課題No.152
							//修正開始
							.append(userBean.getComUserId())
							//.append(userId)
							//修正完了
							.append("','")
							.append(tanto_nm_ja)					// 担当者名日本語
							.append("','")
							.append(tanto_nm_en)					// 担当者名英語
							.append("','")
							.append(syozoku_ja)						// 所属部署名日本語
							.append("','")
							.append(syozoku_en)						// 所属部署名英語
							.append("','")
							.append(phase)							// フェーズ
							.append("','")
							.append("40")							// 入力区分(40: もぎ取り解除)
							.append("','")
							.append(userId)							// 登録ユーザID
							.append("',")
							.append("TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")						// 登録日時
							.append(",'")
							.append(userId)							// 更新ユーザID	
							.append("',")
							.append("TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")						// 更新日時
							//課題No.152
							//修正開始
							.append(",'").append(Function.trim(userBean.getComDaiko_userId())).append("','").append(Function.trim(userBean.getComDaiko_user_nm())).append("','").append(Function.trim(userBean.getComDaiko_user_nm_en())).append("','','','','','')");
							//.append(",'','','','','','','','')");
							//修正完了
		
		sqlExec.addBatch(sql2.toString());

		// 案件No.D9059 もぎ取り解除時に記入内容をクリアしない
		// もぎ取り解除時に現在のフェーズで作成したコメントを削除する
		//StringBuffer sql = new StringBuffer().append("DELETE SST_COMMENT")
		//								.append(" WHERE anken_no='")
		//								.append(ankenNo)
		//								.append("' and phase='70'")
		//								.append(" and toroku_point='00'");
		//sqlExec.addBatch(sql.toString());
		
		// トランザクション実行
		result = sqlExec.execBatch();
		
		return result;

	}
	
	/**
	 * コメントテーブルセレクト処理
	 * @return
	 */
	private String getCmtSelectSql() {
		StringBuffer result = new StringBuffer().append("SELECT comment_val")
												.append(" FROM SST_COMMENT CT")
												.append(" WHERE CT.anken_no='").append(ankenNo)
												.append("' and phase='70'")
												.append(" and toroku_point='00'");
		return result.toString();
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