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
import app.common.form.HikiatekinHanteiSyokaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
 * 引当金判定タブDBアクセスクラス
 */
public class HikiatekinHanteiSyokaiDbAcc extends CommonDbAcc {

	private AppContext appContext = null;				// ＡＰＰコンテキスト
	private SessionData cmnData = null;				// 共通セッションデータ
	private HikiatekinHanteiSyokaiForm form=null;		// アクションホーム
	private TorihikisakiBean tori_bean = null;			// 取引先情報
	
	//Resultset用文字列	
	private static final String KBN_VAL	   	        = "KBN_VAL";			// 区分値
	private static final String KBN_HYOUJI_VAL		= "KBN_HYOUJI_VAL";		// 区分表示値
	private static final String TANI					= "tuuka_cd";			// 単位(通貨)
	private static final String UKETORITEGATA			="01";					// 受取手形
	private static final String YUSYUTSU_UKETORI		="02";					// 輸出受取手形
	private static final String URIKAKE				="03";					// 売掛金
	private static final String TORI_MAE				="04";					// 取引前渡金
	private static final String TATEKAE				="05";					// 立替金
	private static final String MISYUNYU				="06";					// 未収入金
	private static final String MISYUSYU				="07";					// 未収収益
	private static final String TANKI					="08";					// 短期貸付金
	private static final String SASHIIRE				="09";					// 差入保証金
	private static final String KARIHARAI				="10";					// 仮払金
	private static final String TYOKI					="11";					// 長期貸付金
	private static final String SONOTA				="12";					// その他投資
	private static final String HOSYO					="14";					// 保証債務
	private static final String KIBIKIATE				="15";					// 既引当金	
	private static final String KOTEI					="16";					// 固定化営業債権
	private static final String KANJO_HYOUJI_KBN		="kanjo_hyouji_kbn";	// 勘定表示区分
	private static final String KINGAKU				="kingaku";				// 金額
	private static final String RYUHOSAIMU_2			="ryuhosaimu_2";			// 留保債務
	private static final String OTH_RYUHOSAIMU		="oth_ryuhosaimu";		// 第三者留保債務
	private static final String HOZEN					="hozen";				// 保全
	private static final String SONOTAKAISYU			="sonotakaisyu";		// その他回収
	private static final String RIKO_KENEN			="riko_kenen";			// 履行請求懸念
	private static final String TUIKA_HIKIATE_2		="tuika_hikiate_2";		// 追加引当金額
	private static final String HUDOSAN_K				="hudosan_k";			// 契約額-不動産担保
	private static final String DOSAN_K				="dosan_k";				// 契約額-動産担保
	private static final String HOKEN_K				="hoken_k";				// 契約額-貿易保険
	private static final String SONOTA_K				="sonota_k";			// 契約額-その他
	private static final String HUDOSAN_H				="hudosan_h";			// 評価額-不動産担保
	private static final String DOSAN_H				="dosan_h";				// 評価額-動産担保
	private static final String HOKEN_H				="hoken_h";				// 評価額-貿易保険
	private static final String SONOTA_H				="sonota_h";			// 評価額-その他
	private static final String SIHANKI_FLG			="sihanki_tyusyutu_flg";// 第1/3四半期フラグ
	private static final String FLG_KBN				="flg_kbn";				// フラグ区分
	private static final String TASYA_RISUKU			="other_risk_flg";		// 他社リスク
	private static final String EDA					="eda";					// 枝番
	private static final String KIKAN_TORI_CD			="kikan_tori_cd";		// 勘定先CD
	private static final String TORISAKI_NM			="torisaki_nm";			// 取引先名称
	private static final String KANJO_NM				="kanjo_nm";			// 勘定科目
	private static final String TOROKU_POINT			="toroku_point";		// 登録ポイント
	private static final String COMMENT_VAL			="comment_val";			// コメント
	private static final String KOMOKU1				="komoku1";				// 項目1
	private static final String KOMOKU2				="komoku2";				// 項目2
	private static final String KINGAKU_FORMAT_JA		="##,###,###,###,###,##0.##";	// 金額フォーマット(国内)
	private static final String KINGAKU_FORMAT_EN		="##,###,###,###,###,##0.00";	// 金額フォーマット(海外)
	
	private static final String COMMON_OC1104            = "common_OC1104";				//区分キー（ラベル）
	
	private static final String SP_SS_OL_SELECT_T1700 	= "SP_SS_OL_SELECT_T1700";		//勘定表示区分の各金額【リスト】を取得
	private static final String SP_SS_OL_SELECT_T1500 	= "SP_SS_OL_SELECT_T1500";		//引当金判定情報の登録内容を取得
	private static final String SP_SS_OL_SELECT_T2000 	= "SP_SS_OL_SELECT_T2000";		//第三者留保債務内訳を取得
	private static final String SP_SS_OL_SELECT_T1200 	= "SP_SS_OL_SELECT_T1200";		//当画面のコメント類を取得
		
	// INパラメータ
	private String system_kbn;			// システム区分
	private String phase;				// フェーズ
	private String anken_no;			// 案件No
	private String toroku_point	=	"'50','60','70','80','90','95'";	// 登録ポイント
	
	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	
	public HikiatekinHanteiSyokaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {	
		super(sqlExec, log);
		this.appContext = appcontext;
	
		//ビーン取得
		cmnData = appContext.getCMN();
		tori_bean = cmnData.getTori_bean();
		form = (HikiatekinHanteiSyokaiForm)appContext.getActionForm();

		//ビーンの値を変数に設定
		system_kbn = tori_bean.getSystem_kbn();
		phase = tori_bean.getPhase();
		anken_no = tori_bean.getAnken_no();	
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
		system_kbn	= GS.EMPTY_CHARCTER;
		phase 		= GS.EMPTY_CHARCTER;
		anken_no 	= GS.EMPTY_CHARCTER;
		toroku_point = GS.EMPTY_CHARCTER;
	}
		
	/**
	 * 勘定表示区分の各金額【リスト】を取得<br>
	 * 
	 * @exception SQLException
	 */
	public void kanjo_kingaku() throws SQLException {

		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1700, sqlExec);
	    exCstmt.setStringIn(anken_no);
	    exCstmt.setStringIn(system_kbn);
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			
			//ActionForm に取得値を格納
		    while ( rs.next() ) {
		    	
		    	form.setTani(rs.getString(TANI));
		    		//単位(通貨)
		    	
		    	if(null != rs.getString(KINGAKU)){
		    	
			    	if (UKETORITEGATA.equals(rs.getString(KANJO_HYOUJI_KBN))){
			    		//	受取手形	
			    			form.setUketoritegata(formatKingaku(rs.getString(KINGAKU),system_kbn));
			    	}
			    	else if (YUSYUTSU_UKETORI.equals(rs.getString(KANJO_HYOUJI_KBN))){
			    		// 輸出受取手形
			    			form.setYusyutu_uketoritegata(formatKingaku(rs.getString(KINGAKU),system_kbn));
			    	}
			    	else if (URIKAKE.equals(rs.getString(KANJO_HYOUJI_KBN))){
			    		// 売掛金
			    			form.setUrikakekin(formatKingaku(rs.getString(KINGAKU),system_kbn));
			    	}
			    	else if (TORI_MAE.equals(rs.getString(KANJO_HYOUJI_KBN))){
			    		// 取引前渡金
			    			form.setTorihikimaetokin(formatKingaku(rs.getString(KINGAKU),system_kbn));
			    	}
			    	else if (TATEKAE.equals(rs.getString(KANJO_HYOUJI_KBN))){
			    		// 立替金
			    			form.setTatekaekin(formatKingaku(rs.getString(KINGAKU),system_kbn));
			    	}
			    	else if (MISYUNYU.equals(rs.getString(KANJO_HYOUJI_KBN))){
			    		// 未収入金
			    			form.setMisyunyukin(formatKingaku(rs.getString(KINGAKU),system_kbn));
			    	}
			    	else if (MISYUSYU.equals(rs.getString(KANJO_HYOUJI_KBN))){
			    		// 未収収益
			    			form.setMisyusyueki(formatKingaku(rs.getString(KINGAKU),system_kbn));
			    	}
			    	else if (TANKI.equals(rs.getString(KANJO_HYOUJI_KBN))){
			    		// 短期貸付金
			    			form.setTanki_kashitsukekin(formatKingaku(rs.getString(KINGAKU),system_kbn));
			    	}
			    	else if (SASHIIRE.equals(rs.getString(KANJO_HYOUJI_KBN))){
			    		// 差入保証金
			    			form.setSashiire_hosyokin(formatKingaku(rs.getString(KINGAKU),system_kbn));
			    	}
			    	else if (KARIHARAI.equals(rs.getString(KANJO_HYOUJI_KBN))){
			    		// 仮払金
			    		form.setKaribaraikin(formatKingaku(rs.getString(KINGAKU),system_kbn));
			    	}
			    	else if (TYOKI.equals(rs.getString(KANJO_HYOUJI_KBN))){
			    		// 長期貸付金
			    			form.setTyoki_kashitsukekin(formatKingaku(rs.getString(KINGAKU),system_kbn));		    		
			    	}
			    	else if (SONOTA.equals(rs.getString(KANJO_HYOUJI_KBN))){
			    		// その他投資
			    		form.setSonota_toshi(formatKingaku(rs.getString(KINGAKU),system_kbn));
			    	}
			    	else if (HOSYO.equals(rs.getString(KANJO_HYOUJI_KBN))){
			    		// 保証債務
			    		form.setHosyosaimu_gokei(formatKingaku(rs.getString(KINGAKU),system_kbn));
			    	}
			    	else if (KIBIKIATE.equals(rs.getString(KANJO_HYOUJI_KBN))){
			    		// 既引当金	
			    		form.setKibikiatekin(formatKingaku(rs.getString(KINGAKU),system_kbn));
			    	}
			    	
		    		//	汎用１(国内の場合)
		    		if (KOTEI.equals(rs.getString(KANJO_HYOUJI_KBN))){
		    				form.setHanyo1(formatKingaku(rs.getString(KINGAKU),system_kbn));
		    		}
		    	}
		    }
	    } finally {
	    	if (rs != null) {
	    		//Resultset close
	    		rs.close();
	    	}
	    }
	}
	
	/**
	 * 引当金判定の登録内容を取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void gethikiate() throws SQLException {
				
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1500, sqlExec);
	    exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(system_kbn);
	    exCstmt.setStringIn(phase);
	    exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setResultSet(RESULTSET);
		
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			//ActionForm に取得値を格納
            while ( rs.next() ) {
            	
		    	if (!system_kbn.equals(GS.GSS)){
		    		// 汎用１(海外の場合)
		    			form.setHanyo1(formatKingaku(rs.getString(KOMOKU1),system_kbn));
		    		// 通貨調整(海外の場合)
		    			form.setTuuka_tyousei(formatKingaku(rs.getString(KOMOKU2),system_kbn));
		    	}
		    	
		    		// 留保債務
		    			form.setRyuhosaimu(formatKingaku(rs.getString(RYUHOSAIMU_2),system_kbn));
		 
		    		// 第三者留保債務
		    			form.setOth_ryuhosaimu(formatKingaku(rs.getString(OTH_RYUHOSAIMU),system_kbn));
		    	
	    			// 保全
	    				form.setHozen(formatKingaku(rs.getString(HOZEN),system_kbn));
	    		
	    			// その他回収
	    				form.setSonotakaisyu(formatKingaku(rs.getString(SONOTAKAISYU),system_kbn));
	    		
	    			// 履行請求懸念
	    				form.setRiko_kenen(formatKingaku(rs.getString(RIKO_KENEN),system_kbn));
	    				
	    			// 追加引当金額
	    				form.setTuika_hikiate(formatKingaku(rs.getString(TUIKA_HIKIATE_2),system_kbn));
				
					// 契約額-不動産担保
						form.setHudosan_k(formatKingaku(rs.getString(HUDOSAN_K),system_kbn));
				
					// 契約額-動産担保
						form.setDosan_k(formatKingaku(rs.getString(DOSAN_K),system_kbn));
				
					// 契約額-貿易保険
						form.setHoken_k(formatKingaku(rs.getString(HOKEN_K),system_kbn));
				
					// 契約額-その他
					form.setSonota_k(formatKingaku(rs.getString(SONOTA_K),system_kbn));
				
					// 評価額-不動産担保
						form.setHudosan_h(formatKingaku(rs.getString(HUDOSAN_H),system_kbn));
				
					// 評価額-動産担保
						form.setDosan_h(formatKingaku(rs.getString(DOSAN_H),system_kbn));
				
					// 評価額-貿易保険
						form.setHoken_h(formatKingaku(rs.getString(HOKEN_H),system_kbn));
				
					// 評価額-その他
						form.setSonota_h(formatKingaku(rs.getString(SONOTA_H),system_kbn));
					
				// 第1/3四半期フラグ
				form.setSihanki_flg(rs.getString(SIHANKI_FLG));
				// フラグ区分
				form.setFlg_kbn(Function.trim(rs.getString(FLG_KBN)));
				// 他社リスク
				form.setTasya_risuku(rs.getString(TASYA_RISUKU));
            }	
            
	    } finally {
	    	if (rs != null) {
	    		//Resultset close
	    		rs.close();
	    	}
	    }
	}
	
	/**
	 * 第三者留保債務内訳を取得<br>
	 * 
	 * @exception SQLException
	 */
	public void ryuhosaimu() throws SQLException {
		
		//	変数宣言
		String eda_1 = "01";
		String eda_2 = "02";
		String eda_3 = "03";

		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T2000, sqlExec);
		exCstmt.setStringIn(system_kbn);
	    exCstmt.setStringIn(phase);
	    exCstmt.setStringIn(anken_no);
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			
			//ActionForm に取得値を格納
		    while ( rs.next() ) {   	
		    	
		    	if (eda_1.equals(rs.getString(EDA))){
		    		// 枝番が"1"
		    		form.setTori_cd_1(rs.getString(KIKAN_TORI_CD));
		    		form.setTor_nm_1(rs.getString(TORISAKI_NM));
		    		form.setKanjo_nm_1(rs.getString(KANJO_NM));
		    		
		    		if(null != rs.getString(KINGAKU)) {
		    			if(system_kbn.equals(GS.GSS)){
		    				form.setKingaku_1(Function.format(KINGAKU_FORMAT_JA, Function.getValueOfDouble(rs.getString(KINGAKU))));
		    			}else{
		    				form.setKingaku_1(Function.format(KINGAKU_FORMAT_EN, Function.getValueOfDouble(rs.getString(KINGAKU))));
		    			}
		    		}else{
		    			form.setKingaku_1(GS.EMPTY_CHARCTER);
		    		}
		    	}
		    	else if (eda_2.equals(rs.getString(EDA))){
		    		// 枝番が"2"
		    		form.setTori_cd_2(rs.getString(KIKAN_TORI_CD));
		    		form.setTor_nm_2(rs.getString(TORISAKI_NM));
		    		form.setKanjo_nm_2(rs.getString(KANJO_NM));
		    		if(null != rs.getString(KINGAKU)) {
		    			if(system_kbn.equals(GS.GSS)){
		    				form.setKingaku_2(Function.format(KINGAKU_FORMAT_JA, Function.getValueOfDouble(rs.getString(KINGAKU))));
		    			}else{
		    				form.setKingaku_2(Function.format(KINGAKU_FORMAT_EN, Function.getValueOfDouble(rs.getString(KINGAKU))));
		    			}
		    		}else{
		    			form.setKingaku_2(GS.EMPTY_CHARCTER);
		    		}
		    	}
		    	else if (eda_3.equals(rs.getString(EDA))){
		    		//	枝番が"3"
		    		form.setTori_cd_3(rs.getString(KIKAN_TORI_CD));
		    		form.setTor_nm_3(rs.getString(TORISAKI_NM));
		    		form.setKanjo_nm_3(rs.getString(KANJO_NM));
		    		if(null != rs.getString(KINGAKU)) {
		    			if(system_kbn.equals(GS.GSS)){
		    				form.setKingaku_3(Function.format(KINGAKU_FORMAT_JA, Function.getValueOfDouble(rs.getString(KINGAKU))));
		    			}else{
		    				form.setKingaku_3(Function.format(KINGAKU_FORMAT_EN, Function.getValueOfDouble(rs.getString(KINGAKU))));
		    			}
			    	}else{
		    			form.setKingaku_3(GS.EMPTY_CHARCTER);
		    		}
		    	}
		    }
		    
	    } finally {
	    	if (rs != null) {
	    		//Resultset close
	    		rs.close();
	    	}
	    }
	}
	
	/**
	 * 当画面のコメント類を取得<br>
	 * 
	 * @exception SQLException
	 */
	public void comment() throws SQLException {
		
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1200, sqlExec);
	    exCstmt.setStringIn(anken_no);
	    exCstmt.setStringIn(phase);
	    exCstmt.setStringIn(toroku_point);
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			
			//ActionForm に取得値を格納
		    while ( rs.next() ) {
		    	
		    	if(GS.COMMENT_VAL_50.equals(rs.getString(TOROKU_POINT))){
		    		// その他の内容
		    		form.setSonota_naiyo(rs.getString(COMMENT_VAL));
		    		
		    	}else if(GS.COMMENT_VAL_60.equals(rs.getString(TOROKU_POINT))){
		    		// その他回収の内容
		    		form.setSonota_kaisyu_naiyo(rs.getString(COMMENT_VAL));
		    		
		    	}else if(GS.COMMENT_VAL_70.equals(rs.getString(TOROKU_POINT))){
		    		// 「履行請求懸念」の内容説明
		    		form.setRikoseikyu_kenen_naiyo(rs.getString(COMMENT_VAL));
		    		
	    		}else if(GS.COMMENT_VAL_80.equals(rs.getString(TOROKU_POINT))){
	    			// 「引当金算定根拠」の内容説明
	    			form.setHikiatekin_konkyo_naiyo(rs.getString(COMMENT_VAL));

		    	}else if(GS.COMMENT_VAL_90.equals(rs.getString(TOROKU_POINT))){
		    		// 今後の回収見通しなど
		    		form.setKaisyu_naiyo(rs.getString(COMMENT_VAL));
		    	
	    		}else if(GS.COMMENT_VAL_95.equals(rs.getString(TOROKU_POINT))){
	    			// フラグコメント
	    			form.setFlg_comment(rs.getString(COMMENT_VAL));
	    		}
		    }
	    } finally {
	    	if (rs != null) {
	    		//Resultset close
	    		rs.close();
	    	}
	    }
	}
	
	/**
	 * 汎用項目（ラベル）を取得<br>
	 * 
	 * @exception SQLException
	 */
	public void hanyo() throws SQLException {

		ResultSet rs = null;

		try{
			//ResultSet取得
			rs = getKbnval(COMMON_OC1104,system_kbn,cmnData.getComLangMode());

			// ActionForm に取得値を格納
			while ( rs.next() ) {				
				form.setHanyo1_title(rs.getString(KBN_HYOUJI_VAL));
			}
		} finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	}	

	/**
	 * フラグ区分(リスト)を取得<br>
	 * 
	 * @exception SQLException
	 */
	public void flg_kbn() throws SQLException {

		ResultSet rs = null;

		try{
			//ResultSet取得
			rs = getKbnval(FLG_KBN,system_kbn,cmnData.getComLangMode());

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_flg_kbn = new LinkedHashMap<String,String>();
			ar_flg_kbn.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
			int i = 0;
			while ( rs.next() ) {
				ar_flg_kbn.put(rs.getString(KBN_VAL),rs.getString(KBN_HYOUJI_VAL));	    			
				i++;
			}
			form.setAr_flg_kbn(ar_flg_kbn);	 
		} finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }	
	}
	
    /**
     * システム区分により、金額をフォーマットする。<br>
     * 
     * @param kingaku 金額
     * @param systemKbn システム区分
     * @return フォーマットされた金額
     */
    public String formatKingaku(String kingaku,String systemKbn) {
        String formatKingaku = null;
        if(kingaku == null){
        	return GS.EMPTY_CHARCTER;
    	}else if (systemKbn.equals(GS.GSS)){
            //国内
        	formatKingaku = Function.format(KINGAKU_FORMAT_JA,Function.getValueOfDouble(kingaku));
        }else{
            //海外
        	formatKingaku = Function.format(KINGAKU_FORMAT_EN,Function.getValueOfDouble(kingaku));
        }
        return formatKingaku;
    }
}
