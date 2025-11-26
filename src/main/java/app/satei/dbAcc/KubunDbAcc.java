/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2014/03/11		SSC				案件No.D13493 改善対応
******************************************************************************/
package app.satei.dbAcc;

import app.TorihikisakiBean;
import app.satei.form.KubunForm;
import common.AppContext;
import common.db.ExCallableStatement;
import common.global.GS;
import common.util.Function;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
 * OC1103_査定_取引先区分 DBアクセスクラス <br>
 */
public class KubunDbAcc extends SateiDbAcc {

	private KubunForm koForm = null;			//アクションフォーム

	//結果セット取得用文字列
	private static final String KBN_KEY			= "saiken2_kbn";
	private static final String KBN_KEY2			= "torihikisaki3_kbn";
	private static final String KBN_VAL			= "kbn_val";
	private static final String KBN_HYOUJI_VAL	= "kbn_hyouji_val";
	private static final String TAIRYU_KBN_NM		= "tairyu_kbn_nm";
	private static final String TAIRYU_KBN		= "tairyu_kbn";
	private static final String TORIHIKISAKI_KBN	= "konki_torihikisaki_kbn";
	private static final String SAIKEN_KBN		= "konki_saiken_kbn";
	private static final String SEIJO_CHK			= "seijo_chk";
	private static final String YOCHUI_CHK		= "yochui_chk";
	private static final String TYOKA_CHK			= "tyoka_chk";
	private static final String KANWA_CHK			= "kanwa_chk";
	private static final String ENTAI_CHK			= "entai_chk";
	private static final String HASANHO_CHK		= "hasanho_chk";
	private static final String KAISHAHO_CHK		= "kaishaho_chk";
	private static final String KOSEHO_CHK		= "koseho_chk";
	private static final String SAISEHO_CHK		= "saiseho_chk";
	private static final String SHOBUN_CHK		= "shobun_chk";
	private static final String SONOTA_CHK		= "sonota_chk";
	private static final String SYOZAI_ADR		= "syozai_adr";

	//プロシージャ名
	private static final String SP_SS_OL_SELECT_E0400			= "SP_SS_OL_SELECT_E0400";//所在地取得用プロシージャ
    private static final String SP_SS_OC1103_UPDATE_T1500		= "SP_SS_OC1103_UPDATE_T1500";//査定登録内容更新用プロシージャ
	
	private static final String POINT_KONKYO	= "20";		//判定根拠(登録ポイント20)
	private static final String POINT_JIYU 	= "30";		//判定事由(登録ポイント30)
	private static final String POINT_KEII 	= "40";		//発生経緯(登録ポイント40)
	
	private static final String TORI_SONOTA	= "5";		//取引先区分その他
	private static final String SAI_SONOTA	= "4";		//債権区分その他

	/**
	 * コンストラクタ <br>
	 * @param appcontext
	 */
	public KubunDbAcc(AppContext appcontext) {
		super(appcontext);
		//Bean取得
		form = (KubunForm)appContext.getActionForm();
	}
	
	/**
	 * 所在地取得処理 <br>
	 * @exception SQLException
	 */
	public void getSyozaichi(TorihikisakiBean tori_bean) throws SQLException {

		//ResultSet取得
		ResultSet rs = null;

    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_E0400,sqlExec);
        exCstmt.setStringIn(cmnData.getComLangMode());
        exCstmt.setStringIn(tori_bean.getSyozaikoku_cd());
        exCstmt.setStringIn(tori_bean.getShikibetu_cd());
        exCstmt.setStringIn(tori_bean.getTaisyo_ym());
        exCstmt.setStringIn(tori_bean.getTogo_tori_cd());
        exCstmt.setStringIn(tori_bean.getSyori_kaisu());
        exCstmt.setStringIn(tori_bean.getSystem_kbn());
        exCstmt.setStringIn(tori_bean.getSateikaisya_cd());
        exCstmt.setStringIn(tori_bean.getMise_cd());
        exCstmt.setResultSet(RESULTSET);
        
        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
	        rs = exCstmt.getResultSet(RESULTSET);
            
			//ActionFormに取得値を格納
			while(rs.next()){
				if(rs.getString(SYOZAI_ADR) != null && !(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(SYOZAI_ADR))))){
					tori_bean.setSyozaichi(Function.trim(rs.getString(SYOZAI_ADR)));
				}
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 * コメント取得処理 <br>
	 * @param String
	 * @param String
	 * @param String
	 * @exception SQLException
	 */
	public void getCommentOC1103(String ankenNo,String phase,String sansyoPoint) throws SQLException {

		koForm = (KubunForm)form;
		//ResultSet取得
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getComment(ankenNo,phase,sansyoPoint);
            
			//ActionFormに取得値を格納
			while(rs.next()){
				if(POINT_KONKYO.equals(rs.getString(TOROKU_POINT))){
					koForm.setTxtValKonkyo(Function.trim(rs.getString(COMMENT_VAL)));
				}else if(POINT_JIYU.equals(rs.getString(TOROKU_POINT))){
					koForm.setTxtValJiyuu(Function.trim(rs.getString(COMMENT_VAL)));
				}else if(POINT_KEII.equals(rs.getString(TOROKU_POINT))){
					koForm.setTxtValKeii(Function.trim(rs.getString(COMMENT_VAL)));
				}
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 * 査定登録内容取得処理 <br>
	 * @param String
	 * @param String
	 * @param String
	 * @exception SQLException
	 */
	public void getSateiDataOC1103(String ankenNo,String phase,String sysKbn) throws SQLException {

		koForm = (KubunForm)form;
		//ResultSet取得
		ResultSet rs = null;
        String kbnTori = GS.EMPTY_CHARCTER;
        String kbnSai = GS.EMPTY_CHARCTER;
        String chkSeijo = GS.EMPTY_CHARCTER;

		try{
            //SQL実行
			rs = super.getSateiData(ankenNo,phase,sysKbn);
            
			//ActionFormに取得値を格納
			while(rs.next()){
				//IT0038
				kbnTori = Function.trim(rs.getString(TORIHIKISAKI_KBN));
				kbnSai = Function.trim(rs.getString(SAIKEN_KBN));
				chkSeijo = Function.trim(rs.getString(SEIJO_CHK));
				//取引先区分その他の場合、取引先区分：正常先、正常先チェック：ON
				if(TORI_SONOTA.equals(kbnTori)){
					kbnTori = GS.SEIJOUSAKI_TORIKBN;
					chkSeijo = GS.ON;
				}
				//債権区分その他の場合、債権区分：一般債権
				if(SAI_SONOTA.equals(kbnSai)){
					kbnSai = GS.IPPAN_SAIKEN;
				}
				//IT0038ここまで
				koForm.setKbnTorihiki(kbnTori);
				koForm.setKbnSaiken(kbnSai);
				koForm.setChkFlgSeijo(chkSeijo);
				koForm.setChkFlgYochui(Function.trim(rs.getString(YOCHUI_CHK)));
				koForm.setChkFlgTyoka(Function.trim(rs.getString(TYOKA_CHK)));
				koForm.setChkFlgKanwa(Function.trim(rs.getString(KANWA_CHK)));
				koForm.setChkFlgEntai(Function.trim(rs.getString(ENTAI_CHK)));
				koForm.setChkFlgHasanho(Function.trim(rs.getString(HASANHO_CHK)));
				koForm.setChkFlgKaishaho(Function.trim(rs.getString(KAISHAHO_CHK)));
				koForm.setChkFlgKoseho(Function.trim(rs.getString(KOSEHO_CHK)));
				koForm.setChkFlgSaiseho(Function.trim(rs.getString(SAISEHO_CHK)));
				koForm.setChkFlgShobun(Function.trim(rs.getString(SHOBUN_CHK)));
				koForm.setChkFlgSonota(Function.trim(rs.getString(SONOTA_CHK)));
				//IT0038
				//取引先区分その他の場合、正常先チェック以外全てOFF
				if(TORI_SONOTA.equals(kbnTori)){
					koForm.setChkFlgYochui(GS.OFF);
					koForm.setChkFlgTyoka(GS.OFF);
					koForm.setChkFlgKanwa(GS.OFF);
					koForm.setChkFlgEntai(GS.OFF);
					koForm.setChkFlgHasanho(GS.OFF);
					koForm.setChkFlgKaishaho(GS.OFF);
					koForm.setChkFlgKoseho(GS.OFF);
					koForm.setChkFlgSaiseho(GS.OFF);
					koForm.setChkFlgShobun(GS.OFF);
					koForm.setChkFlgSonota(GS.OFF);
				}
				//IT0038ここまで
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 * 査定登録内容取得処理 <br>
	 * @param String
	 * @param String
	 * @param String
	 * @exception SQLException
	 */
	public void getSateiDataTairyu(String ankenNo,String phase,String sysKbn) throws SQLException {

		koForm = (KubunForm)form;
		//ResultSet取得
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getSateiData(ankenNo,phase,sysKbn);
            
			//ActionFormに取得値を格納
			while(rs.next()){
				koForm.setTxtKbnTairyuNm(Function.trim(rs.getString(TAIRYU_KBN_NM)));
				koForm.setTxtKbnTairyu(Function.trim(rs.getString(TAIRYU_KBN)));
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 * 債権区分セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSaikenList() throws SQLException {

		koForm = (KubunForm)form;
		//ResultSet取得
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getKbnval(KBN_KEY,user_bean.getComWorkflowSystemkbn(),cmnData.getComLangMode());
            
			//ActionFormに取得値を格納
			LinkedHashMap<String,String> saikenList = new LinkedHashMap<String,String>();
			while(rs.next()){
				saikenList.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
			}
			koForm.setSaikenList(saikenList);
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
	
	/**
	 * 取引先区分セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getToriSelectList() throws SQLException {

		koForm = (KubunForm)form;
		//ResultSet取得
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getKbnval(KBN_KEY2,user_bean.getComWorkflowSystemkbn(),cmnData.getComLangMode());
            
			//ActionFormに取得値を格納
			LinkedHashMap<String,String> toriSelectList = new LinkedHashMap<String,String>();
			while(rs.next()){
				toriSelectList.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
			}
			koForm.setToriSelectList(toriSelectList);
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

    /**
     * 査定登録内容更新処理<br>
	 * @param TorihikisakiBean
     * 
     * @throws SQLException
     */
    public void updateSateiData(TorihikisakiBean toriBean) throws SQLException{

    	koForm = (KubunForm)form;
    	
    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC1103_UPDATE_T1500,sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn(toriBean.getPhase());
        exCstmt.setStringIn(koForm.getChkFlgSeijo());
        exCstmt.setStringIn(koForm.getChkFlgYochui());
        exCstmt.setStringIn(koForm.getChkFlgTyoka());
        exCstmt.setStringIn(koForm.getChkFlgKanwa());
        exCstmt.setStringIn(koForm.getChkFlgEntai());
        exCstmt.setStringIn(koForm.getChkFlgHasanho());
        exCstmt.setStringIn(koForm.getChkFlgKaishaho());
        exCstmt.setStringIn(koForm.getChkFlgKoseho());
        exCstmt.setStringIn(koForm.getChkFlgSaiseho());
        exCstmt.setStringIn(koForm.getChkFlgShobun());
        exCstmt.setStringIn(koForm.getChkFlgSonota());
        exCstmt.setStringIn(koForm.getKbnTorihiki());
        exCstmt.setStringIn(koForm.getKbnSaiken());
        
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }
}