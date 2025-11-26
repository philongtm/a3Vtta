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
import app.common.form.KihonJohoSyokaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* OZ6108_基本情報照会タブ DBアクセスクラス
*/
public class KihonJohoSyokaiDbAcc extends CommonDbAcc {
	
	private AppContext appContext = null;		// ＡＰＰコンテキスト
	private SessionData cmnData = null;		// 共通セッションデータ
	private TorihikisakiBean tori_bean = null;	// 取引先情報
	private KihonJohoSyokaiForm form = null;	// アクションフォーム

	//Resultset用文字列	
	private static final String KBN_HYOUJI_VAL       = "kbn_hyouji_val";
	private static final String KANJO_HYOUJI_KBN     = "kanjo_hyouji_kbn";	
	private static final String TUUKA_CD             = "tuuka_cd";	
	private static final String KINGAKU              = "kingaku";	
	private static final String KOMOKU1              = "komoku1";	
	private static final String OYA_BUSINESS_NM      = "oya_business_nm";	

    private static final String KBN01 = "01";				// 受取手形
    private static final String KBN02 = "02";				// 輸出受取手形
    private static final String KBN03 = "03";				// 売掛金
    private static final String KBN04 = "04";				// 取引前渡金
    private static final String KBN05 = "05";				// 立替金
    private static final String KBN06 = "06";				// 未収入金
    private static final String KBN07 = "07";				// 未収収益
    private static final String KBN08 = "08";				// 短期貸付金
    private static final String KBN09 = "09";				// 差入保証金
    private static final String KBN10 = "10";				// 仮払金
    private static final String KBN11 = "11";				// 長期貸付金
    private static final String KBN12 = "12";				// その他投資
    private static final String KBN14 = "14";				// 保証債務合計
    private static final String KBN15 = "15";				// 既引当金
    private static final String KBN16 = "16";				// 固定化営業債権
	private static final String COMMON_OZ6108   = "common_OZ6108";				// 区分キー(汎用ラベル名)	
    private static final String NUM_FMT_KOKUNAI = "##,###,###,###,###,##0.##";   // 金額のフォーマット：国内
    private static final String NUM_FMT_KAIGAI  = "##,###,###,###,###,##0.00";   // 金額のフォーマット：海外
	
	private static final String SP_SS_OL_SELECT_T1700         = "SP_SS_OL_SELECT_T1700";			//明細取得用プロシージャ
	private static final String SP_SS_OZ6108_SELECT_MEISAI    = "SP_SS_OZ6108_SELECT_MEISAI";		//明細取得用プロシージャ
	private static final String SP_SS_OL_SELECT_T1500         = "SP_SS_OL_SELECT_T1500";			//項目１取得用プロシージャ
	private static final String SP_SS_OZ6108_SELECT_G0100     = "SP_SS_OZ6108_SELECT_G0100";		//親会社名称取得用プロシージャ

	// INパラメータ
	private String anken_no;			// 案件No
	private String systemkbn;			// システム区分
	private String sateikaisya_cd;		// 査定会社コード
	private String tori_cd;			// 基幹取引先コード
	private String mise_cd;			// 店コード
	private String taisyo_ym;			// 対象年月
	private String phase;				// フェーズ
    private String comLangMode;        // 共)言語モード
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 * @param appLog
	 */
	public KihonJohoSyokaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		
		//ビーン取得
		cmnData = appContext.getCMN();
		tori_bean = cmnData.getTori_bean();
		form = (KihonJohoSyokaiForm)appContext.getActionForm();

		//ビーンの値を変数に設定
		anken_no = tori_bean.getAnken_no();
		systemkbn = tori_bean.getSystem_kbn();
		sateikaisya_cd = tori_bean.getSateikaisya_cd();
		tori_cd = tori_bean.getKanjo_cd();
		mise_cd = tori_bean.getMise_cd();
		taisyo_ym = tori_bean.getTaisyo_ym();
		phase= tori_bean.getPhase();
        comLangMode = cmnData.getComLangMode();
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
		anken_no = GS.EMPTY_CHARCTER;
	    systemkbn = GS.EMPTY_CHARCTER;
		sateikaisya_cd = GS.EMPTY_CHARCTER;
		tori_cd = GS.EMPTY_CHARCTER;
		mise_cd = GS.EMPTY_CHARCTER;
		taisyo_ym = GS.EMPTY_CHARCTER;
		phase= GS.EMPTY_CHARCTER;
        comLangMode = GS.EMPTY_CHARCTER;
	}
		
	/**
	 * 汎用項目ラベル名取得処理 <br>
	 * @exception SQLException
	 */
	public void getLabel() throws SQLException {

		ResultSet rs = null;
		try{
			//ResultSet取得
			rs = getKbnval(COMMON_OZ6108,systemkbn,comLangMode);

			// ActionForm に取得値を格納
			if ( rs.next() ) {
				form.setHanyou1_lbl(rs.getString(KBN_HYOUJI_VAL));
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 * 明細情報取得処理 <br>
	 * 
	 * @return double 一般債権計
     * @param  rtn_id 遷移元画面ID
	 * @exception SQLException
	 */
	public double getMeisai(String rtn_id) throws SQLException {

		ResultSet rs = null;
		ExCallableStatement exCstmt = null;
		double k16 = 0;
        double ippan_saiken_kei = 0;
        int i = 0;
        String formatType = GS.EMPTY_CHARCTER;

        //金額のフォーマット
        if(GS.GSS.equals(systemkbn)){
        	formatType = NUM_FMT_KOKUNAI;
        }else{
        	formatType = NUM_FMT_KAIGAI;
        }

        if(GS.OB2101.equals(rtn_id) || GS.OB2104.equals(rtn_id)) {
	        // 対象先選定_選定実行,対象先選定_承認一覧より遷移時
	        //ExCallableStatement生成
			exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1700, sqlExec);
			exCstmt.setStringIn(anken_no);
			exCstmt.setStringIn(systemkbn);
			exCstmt.setResultSet(RESULTSET);
		}else{
	        // 対象先選定_追加対象先選択,ゴルフ会員権一覧より遷移時
			//ExCallableStatement生成
			exCstmt = new ExCallableStatement(SP_SS_OZ6108_SELECT_MEISAI, sqlExec);
			exCstmt.setStringIn(systemkbn);
			exCstmt.setStringIn(sateikaisya_cd);
			exCstmt.setStringIn(tori_cd);
			exCstmt.setStringIn(mise_cd);
			exCstmt.setStringIn(taisyo_ym);
			exCstmt.setResultSet(RESULTSET);
		}

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			while ( rs.next() ) {
				//通貨コード
				if(i == 0){
					form.setTuuka_cd(rs.getString(TUUKA_CD));
				}
				if(KBN01.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//受取手形
					form.setKbn01(Function.format(formatType,rs.getDouble(KINGAKU)));
				}else if(KBN02.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//輸出受取手形
					form.setKbn02(Function.format(formatType,rs.getDouble(KINGAKU)));
				}else if(KBN03.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//売掛金
					form.setKbn03(Function.format(formatType,rs.getDouble(KINGAKU)));
				}else if(KBN04.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//取引前渡金
					form.setKbn04(Function.format(formatType,rs.getDouble(KINGAKU)));
				}else if(KBN05.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//立替金
					form.setKbn05(Function.format(formatType,rs.getDouble(KINGAKU)));
				}else if(KBN06.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//未収入金
					form.setKbn06(Function.format(formatType,rs.getDouble(KINGAKU)));
				}else if(KBN07.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//未収収益
					form.setKbn07(Function.format(formatType,rs.getDouble(KINGAKU)));
				}else if(KBN08.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//短期貸付金
					form.setKbn08(Function.format(formatType,rs.getDouble(KINGAKU)));
				}else if(KBN09.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//差入保証金
					form.setKbn09(Function.format(formatType,rs.getDouble(KINGAKU)));
				}else if(KBN10.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//仮払金
					form.setKbn10(Function.format(formatType,rs.getDouble(KINGAKU)));
				}else if(KBN11.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//長期貸付金
					form.setKbn11(Function.format(formatType,rs.getDouble(KINGAKU)));
				}else if(KBN12.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//その他投資
					form.setKbn12(Function.format(formatType,rs.getDouble(KINGAKU)));
				}else if(KBN14.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//保証債務合計
					form.setKbn14(Function.format(formatType,rs.getDouble(KINGAKU)));
				}else if(KBN15.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//既引当金
					form.setKbn15(Function.format(formatType,rs.getDouble(KINGAKU)));
				}else if(KBN16.equals(rs.getString(KANJO_HYOUJI_KBN))){
					//汎用１
					form.setHanyou1(Function.format(formatType,rs.getDouble(KINGAKU)));
					k16 = rs.getDouble(KINGAKU)*100;
				}else{
					++i;
					continue;
				}

				if(!KBN14.equals(rs.getString(KANJO_HYOUJI_KBN)) && !KBN15.equals(rs.getString(KANJO_HYOUJI_KBN)) && !KBN16.equals(rs.getString(KANJO_HYOUJI_KBN))){
					ippan_saiken_kei = ippan_saiken_kei + rs.getDouble(KINGAKU)*100;
				}
				++i;
			}
			
			//一般債権計
			form.setIppan_saiken_kei(Function.format(formatType,ippan_saiken_kei/100));

			//債権残高合計
			if(GS.GSS.equals(systemkbn)){
				//国内：一般債権計 + 固定化営業債権
				form.setSaiken_kei(Function.format(formatType,(ippan_saiken_kei + k16)/100));
			}
			return ippan_saiken_kei;

	    } finally {
	    	if (rs != null) {
	    			rs.close();
	    	}
	    }
	}

	/**
	 * 項目１取得処理 <br>
	 * 
     * @param  double 一般債権計
	 * @exception SQLException
	 */
	public void getKomoku1(double ippan_saiken_kei) throws SQLException {

		ResultSet rs = null;
		double komoku1 = 0;
		String satei_phase = phase;
		String formatType = GS.EMPTY_CHARCTER;

        //金額のフォーマット
        if(GS.GSS.equals(systemkbn)){
        	formatType = NUM_FMT_KOKUNAI;
        }else{
        	formatType = NUM_FMT_KAIGAI;
        }

        //対象先選定の場合、一次査定時のデータを取得する
		if(GS.PHASE_TAISHOSAKI_SENTEI.equals(phase)){
			satei_phase = GS.PHASE_ICHIJI_SATEI;
		}

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1500, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(satei_phase);
		exCstmt.setStringIn(comLangMode);
		exCstmt.setResultSet(RESULTSET);
	    
	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
	    	
			// ActionForm に取得値を格納
			if ( rs.next() ) {
				form.setHanyou1(Function.format(formatType,rs.getDouble(KOMOKU1)));
				komoku1 = rs.getDouble(KOMOKU1)*100;
			}
			//海外：一般債権計 + 通貨調整
			form.setSaiken_kei(Function.format(formatType,(ippan_saiken_kei + komoku1)/100));
		
	    } finally {
	    	if (rs != null) {
	    			rs.close();
	    	}
	    }
	}

	/**
	 * 親会社名称取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getOya_kaisya_nm() throws SQLException {
		ResultSet rs = null;

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ6108_SELECT_G0100, sqlExec);
		exCstmt.setStringIn(comLangMode);
		exCstmt.setStringIn(tori_bean.getOya_duns_no());
		exCstmt.setResultSet(RESULTSET);
	    
	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			if ( rs.next() ) {
				form.setOya_kaisya_nm(rs.getString(OYA_BUSINESS_NM));
			}
	    } finally {
	    	if (rs != null) {
	    			rs.close();
	    	}
	    }
	}
}