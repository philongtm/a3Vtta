/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.ZaimuBean;
import app.common.form.TorihikisakiGaiyoSyokaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
* OZ6102_取引先概要照会タブ DBアクセスクラス
*/
public class TorihikisakiGaiyoSyokaiDbAcc extends CommonDbAcc {

	private TorihikisakiBean tori_bean = null;						// 取引先情報
	private SessionData cmnData = null;								// 機能共通セッション
	private TorihikisakiGaiyoSyokaiForm form = null;				// アクションフォーム
	private AppContext appContext = null;							// ＡＰＰコンテキスト
	
	private static final String COMMON_OC1102	= "common_OC1102";				//汎用項目ラベル取得用区分キー
	private static final String HANYO1			= "1";
	private static final String HANYO2			= "2";
	private static final String HANYO3			= "3";

	// Resultset用文字列
	private static final String KBN_HYOUJI_VAL			= "kbn_hyouji_val";				// 表示値
	private static final String KBN_VAL					= "kbn_val";
	private static final String SIC_SIM_NM				= "sic_sim_nm";
	private static final String SYOZAI_ADR				= "syozai_adr";
	private static final String COMMENT_VAL				= "comment_val";				// コメント内容
	private static final String KABUNUSI_NM1			= "kabunusi_nm1";				// 株主名称1
	private static final String KABUNUSI_NM2			= "kabunusi_nm2";				// 株主名称2
	private static final String KABUNUSI_NM3			= "kabunusi_nm3";				// 株主名称3
	private static final String KABUNUSI_NM4			= "kabunusi_nm4";				// 株主名称4
	private static final String KABUNUSI_NM5			= "kabunusi_nm5";				// 株主名称5
	private static final String KABUSU1					= "kabusu1";					// 保有株数1
	private static final String KABUSU2					= "kabusu2";					// 保有株数2
	private static final String KABUSU3					= "kabusu3";					// 保有株数3
	private static final String KABUSU4					= "kabusu4";					// 保有株数4
	private static final String KABUSU5					= "kabusu5";					// 保有株数5
	private static final String HIRITU1					= "hiritu1";					// 保有率1
	private static final String HIRITU2					= "hiritu2";					// 保有率2
	private static final String HIRITU3					= "hiritu3";					// 保有率3
	private static final String HIRITU4					= "hiritu4";					// 保有率4
	private static final String HIRITU5					= "hiritu5";					// 保有率5
	private static final String JIGYONAIYO				= "jigyonaiyo";					// 事業内容
	private static final String URIAGEDAKA				= "uriagedaka";					// 売上高
	private static final String URIAGESORIEKI			= "uriagesorieki";				// 売上総利益
	private static final String HANBAIHIKANRIHI			= "hanbaihikanrihi";			// 販売管理費
	private static final String EIGYORIEKI				= "eigyorieki";					// 営業利益
	private static final String HANYOU1					= "hanyou1";					// 汎用１
	private static final String HANYOU2					= "hanyou2";					// 汎用２
	private static final String HANYOU3					= "hanyou3";					// 汎用３
	private static final String TOKIJUNRIEKI			= "tokijunrieki";				// 当期純利益
	private static final String HAITOKIN				= "haitokin";					// 配当金
	private static final String GENKASYOKYAKUHI			= "genkasyokyakuhi";			// 減価償却費
	private static final String EIGYO_CF				= "eigyo_cf";					// 営業キャッシュフロー
	private static final String RYUDOSISAN				= "ryudosisan";					// 流動資産
	private static final String KOTEISISAN				= "koteisisan";					// 固定資産
	private static final String SISANGOKEI				= "sisangokei";					// 資産合計
	private static final String RYUDOHUSAI				= "ryudohusai";					// 流動負債
	private static final String KOTEIHUSAI				= "koteihusai";					// 固定負債
	private static final String HUSAIGOKEI				= "husaigokei";					// 負債合計
	private static final String SIHONKIN				= "sihonkin";					// 資本金
	private static final String NAIBURYUHO				= "naiburyuho";					// 内部留保等
	private static final String JIKOSIHONGOKEI			= "jikosihongokei";				// 自己資本合計
	private static final String KANSAN_ISO_CURRENCY_NM	= "kansan_iso_currency_nm";		// ISO通貨略称
	private static final String HYOUJI_TANI				= "hyouji_tani";				// 表示単位
	private static final String TANTAI_RENKETU			= "tantai_renketu";				// 単体連結
	private static final String KESSAN_KI				= "kessan_ki";					// 決算期

	private static final String KABU_FMT 				= "##,###,###,###,###,##0";	//フォーマット
	private static final String NUM_FMT 				= "##,###,###,###,###,##0";	//フォーマット
    private static final String HIRITU_FMT 			= "##,###,###,###,###,##0.00";	//フォーマット

	// 使用プロシージャ
	private static final String SP_SS_OL_SELECT_E0400	= "SP_SS_OL_SELECT_E0400";		// 業種、所在地の取得プロシージャ
	private static final String SP_SS_OL_SELECT_T1200	= "SP_SS_OL_SELECT_T1200";		// 決算概況の取得プロシージャ
	private static final String SP_SS_OL_SELECT_T1500	= "SP_SS_OL_SELECT_T1500";		// 事業内容、株主構成の取得プロシージャ
	private static final String SP_SS_OL_SELECT_ZAIMU	= "SP_SS_OL_SELECT_ZAIMU";		// 財務情報【リスト】取得プロシージャ
	
	// INパラメータ及びbean取得用
	private String comLangMode;				// 共)言語モード
	private String wb_country_cd;				// 共)取引先情報.ワールドベース国コード
	private String taisyo_ym;					// 共)取引先情報.年月
	private String shikibetu_cd;				// 共)取引先情報.識別コード
	private String togo_tori_cd;				// 共)取引先情報.統合取引先コード
	private String syori_kaisu;				// 共)取引先情報.処理回数
	private String system_kbn;					// 共)取引先情報.基幹システム区分
	private String sateikaisya_cd;				// 共)取引先情報.査定会社コード
	private String mise_cd;					// 共)取引先情報.店コード
	private String anken_no;					// 共)取引先情報.査定案件No.
	private String phase;						// 共)取引先情報.フェーズ
	
	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	
	public TorihikisakiGaiyoSyokaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		
		super(sqlExec, log);
		this.appContext = appcontext;

		//ビーン取得
		cmnData 			= appContext.getCMN();
		tori_bean			= cmnData.getTori_bean();
		form				= (TorihikisakiGaiyoSyokaiForm)appContext.getActionForm();

		//ビーンの値を変数に設定
		comLangMode			= cmnData.getComLangMode();
		wb_country_cd		= tori_bean.getWb_country_cd();
		taisyo_ym 			= tori_bean.getTaisyo_ym();
		shikibetu_cd		= tori_bean.getShikibetu_cd();
		togo_tori_cd		= tori_bean.getTogo_tori_cd();
		syori_kaisu			= tori_bean.getSyori_kaisu();
		system_kbn			= tori_bean.getSystem_kbn();
		sateikaisya_cd		= tori_bean.getSateikaisya_cd();
		mise_cd				= tori_bean.getMise_cd();
		anken_no			= tori_bean.getAnken_no();
		phase				= tori_bean.getPhase();
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
		// INパラメータ
		comLangMode		= GS.EMPTY_CHARCTER;
		wb_country_cd	= GS.EMPTY_CHARCTER;
		taisyo_ym		= GS.EMPTY_CHARCTER;
		shikibetu_cd	= GS.EMPTY_CHARCTER;
		togo_tori_cd	= GS.EMPTY_CHARCTER;
		syori_kaisu		= GS.EMPTY_CHARCTER;
		system_kbn		= GS.EMPTY_CHARCTER;
		sateikaisya_cd	= GS.EMPTY_CHARCTER;
		mise_cd			= GS.EMPTY_CHARCTER;
		anken_no		= GS.EMPTY_CHARCTER;
		phase			= GS.EMPTY_CHARCTER;
	}

	/**
	 * 業種、所在地の表示データを取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getshozaichi() throws SQLException {

		InputCheck check = new InputCheck();
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_E0400, sqlExec);
		exCstmt.setStringIn(comLangMode);
		exCstmt.setStringIn(wb_country_cd);
		exCstmt.setStringIn(shikibetu_cd);
		exCstmt.setStringIn(taisyo_ym);
		exCstmt.setStringIn(togo_tori_cd);
		exCstmt.setStringIn(syori_kaisu);
		exCstmt.setStringIn(system_kbn);
		exCstmt.setStringIn(sateikaisya_cd);
		exCstmt.setStringIn(mise_cd);
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			if(rs.next()) {
				//業種小分類
				form.setSic_sm_nm(rs.getString(SIC_SIM_NM));
				//所在地を機能共通セッションに設定
				if (!check.isNullBlank(rs.getString(SYOZAI_ADR))){
					tori_bean.setSyozaichi(rs.getString(SYOZAI_ADR));
				}
			}	
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
	
	/**
	 * コメント取得（決算概況の取得）<br>
	 * 
	 * @exception SQLException
	 */
	public void getcomment() throws SQLException {

		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1200, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(GS.COMMENT_VAL_10);
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			
			//ActionForm に取得値を格納
			if(rs.next()){
				form.setComment_val(rs.getString(COMMENT_VAL));
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
	
	/**
	 * 事業内容・株主構成取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getKabunusi_kousei() throws SQLException {

		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1500, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(system_kbn);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(comLangMode);
		exCstmt.setResultSet(RESULTSET);
		
		try {
			//SQL実行	
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			
			// ActionForm に取得値を格納
			if ( rs.next() ) {
				form.setKabunusi_nm1(rs.getString(KABUNUSI_NM1));
				form.setKabunusi_nm2(rs.getString(KABUNUSI_NM2));
				form.setKabunusi_nm3(rs.getString(KABUNUSI_NM3));
				form.setKabunusi_nm4(rs.getString(KABUNUSI_NM4));
				form.setKabunusi_nm5(rs.getString(KABUNUSI_NM5));
				form.setKabusu1(formatKabusu(rs.getString(KABUSU1)));
				form.setKabusu2(formatKabusu(rs.getString(KABUSU2)));
				form.setKabusu3(formatKabusu(rs.getString(KABUSU3)));
				form.setKabusu4(formatKabusu(rs.getString(KABUSU4)));
				form.setKabusu5(formatKabusu(rs.getString(KABUSU5)));
				form.setHiritu1(formatHiritu(rs.getString(HIRITU1)));
				form.setHiritu2(formatHiritu(rs.getString(HIRITU2)));
				form.setHiritu3(formatHiritu(rs.getString(HIRITU3)));
				form.setHiritu4(formatHiritu(rs.getString(HIRITU4)));
				form.setHiritu5(formatHiritu(rs.getString(HIRITU5)));
				form.setJigyonaiyo(rs.getString(JIGYONAIYO));
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * 汎用項目（ラベル）を取得<br>
	 * 
	 * @exception SQLException
	 */
	public void getHanyo() throws SQLException {

		ResultSet rs = null;

		try{
			//ResultSet取得
			rs = getKbnval(COMMON_OC1102,system_kbn,comLangMode);

			// ActionForm に取得値を格納
			while ( rs.next() ) {
				if(HANYO1.equals(rs.getString(KBN_VAL))){
					form.setHanyou1(rs.getString(KBN_HYOUJI_VAL));
				}else if(HANYO2.equals(rs.getString(KBN_VAL))){
					form.setHanyou2(rs.getString(KBN_HYOUJI_VAL));
				}else if(HANYO3.equals(rs.getString(KBN_VAL))){
					form.setHanyou3(rs.getString(KBN_HYOUJI_VAL));
				}
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 * 財務情報【リスト】取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getZaimu() throws SQLException {

		ResultSet rs = null;
		try{
	        ZaimuBean listBean = null;
	        List<ZaimuBean> list = new ArrayList<ZaimuBean>();
	        list.add(0,new ZaimuBean());
	        list.add(1,new ZaimuBean());
	        list.add(2,new ZaimuBean());
			
			//ExCallableStatement生成
			ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_ZAIMU, sqlExec);
			exCstmt.setStringIn(comLangMode);
			exCstmt.setStringIn(system_kbn);
			exCstmt.setStringIn(taisyo_ym);
			exCstmt.setStringIn(shikibetu_cd);
			exCstmt.setStringIn(togo_tori_cd);
			exCstmt.setStringIn(syori_kaisu);
			exCstmt.setStringIn(sateikaisya_cd);
			exCstmt.setStringIn(mise_cd);
			exCstmt.setResultSet(RESULTSET);

			//SQL実行	
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);	 
			
			// ActionForm に取得値を格納
			int i = 0;
			while ( rs.next() ) {

				listBean = (ZaimuBean)list.get(i);
				//決算期
				listBean.setKessan_ki(rs.getString(KESSAN_KI));
				//単体連結
				listBean.setTantai_renketu(rs.getString(TANTAI_RENKETU));
				//売上高
				listBean.setUriagedaka(formatKingaku(rs.getString(URIAGEDAKA)));
				//売上総利益
				listBean.setUriagesorieki(formatKingaku(rs.getString(URIAGESORIEKI)));
				//販売管理費
				listBean.setHanbaihikanrihi(formatKingaku(rs.getString(HANBAIHIKANRIHI)));
				//営業利益
				listBean.setEigyorieki(formatKingaku(rs.getString(EIGYORIEKI)));
				//汎用１
				listBean.setHanyou1(formatKingaku(rs.getString(HANYOU1)));
				//汎用２
				listBean.setHanyou2(formatKingaku(rs.getString(HANYOU2)));
				//汎用３
				listBean.setHanyou3(formatKingaku(rs.getString(HANYOU3)));
				//当期純利益
				listBean.setTokijunrieki(formatKingaku(rs.getString(TOKIJUNRIEKI)));
				//配当金
				listBean.setHaitokin(formatKingaku(rs.getString(HAITOKIN)));
				//減価償却費
				listBean.setGenkasyokyakuhi(formatKingaku(rs.getString(GENKASYOKYAKUHI)));
				// 営業ＣＦ
				listBean.setEigyo_cf(formatKingaku(rs.getString(EIGYO_CF)));
				//流動資産
				listBean.setRyudosisan(formatKingaku(rs.getString(RYUDOSISAN)));
				//固定資産
				listBean.setKoteisisan(formatKingaku(rs.getString(KOTEISISAN)));
				//資産合計
				listBean.setSisangokei(formatKingaku(rs.getString(SISANGOKEI)));
				//流動負債
				listBean.setRyudohusai(formatKingaku(rs.getString(RYUDOHUSAI)));
				//固定負債
				listBean.setKoteihusai(formatKingaku(rs.getString(KOTEIHUSAI)));
				//負債合計
				listBean.setHusaigokei(formatKingaku(rs.getString(HUSAIGOKEI)));
				//資本金
				listBean.setSihonkin(formatKingaku(rs.getString(SIHONKIN)));
				//内部留保等
				listBean.setNaiburyuho(formatKingaku(rs.getString(NAIBURYUHO)));
				//自己資本合計
				listBean.setJikosihongokei(formatKingaku(rs.getString(JIKOSIHONGOKEI)));
				
				//通貨コード＆表示単位設定
				if(!Function.trim(rs.getString(KANSAN_ISO_CURRENCY_NM)).equals(GS.EMPTY_CHARCTER)){
					listBean.setTukaCd(Function.trim(rs.getString(KANSAN_ISO_CURRENCY_NM)));
				}
				if(!Function.trim(rs.getString(HYOUJI_TANI)).equals(GS.EMPTY_CHARCTER)){
					listBean.setHyoujiTani(GS.KAKKO_HIDARI + Function.trim(rs.getString(HYOUJI_TANI)) + GS.KAKKO_MIGI);
				}
				
				i++;
			}
	        form.setAr_zaimu(list);

		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * 株保有率をフォーマットする。<br>
     * 
     * @param hiritu 株保有率
     * @return フォーマットされた株保有率
     */
    private String formatHiritu(String hiritu) {
        String formatHiritu = null;
        if(hiritu == null){
        	return GS.EMPTY_CHARCTER;
    	}else{
    		formatHiritu = Function.format(HIRITU_FMT,Function.getValueOfDouble(hiritu));
        }
        return formatHiritu;
    }

    /**
     * 金額をフォーマットする。<br>
     * 
     * @param kingaku 金額
     * @param systemKbn システム区分
     * @return フォーマットされた金額
     */
    private String formatKingaku(String kingaku) {
        String formatKingaku = null;
        if(kingaku == null){
        	return GS.EMPTY_CHARCTER;
    	}else{
        	formatKingaku = Function.format(NUM_FMT,Function.getValueOfDouble(kingaku));
        }
        return formatKingaku;
    }

    /**
     * 保有株数をフォーマットする。<br>
     * 
     * @param kabusu 保有株数
     * @return フォーマットされた金額
     */
    private String formatKabusu(String kabusu) {
        String formatKabusu = null;
        if(kabusu == null){
        	return GS.EMPTY_CHARCTER;
    	}else{
    		formatKabusu = Function.format(KABU_FMT,Function.getValueOfDouble(kabusu));
        }
        return formatKabusu;
    }
}