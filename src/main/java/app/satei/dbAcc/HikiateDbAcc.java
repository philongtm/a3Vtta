/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/12/17		SSC				課題No.209 承認プルダウン修正
003		2016/03/18		SSC				BJ201602002 部門廃止対応（一次）
******************************************************************************/
package app.satei.dbAcc;

import app.TorihikisakiBean;
import app.satei.form.HikiateForm;
import common.AppContext;
import common.db.ExCallableStatement;
import common.global.GS;
import common.util.Function;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static app.satei.form.HikiateForm.TAB_HUKA;
import static app.satei.form.HikiateForm.YOMITORI_TRUE;

/**
 * OC1104_査定_引当金判定 DBアクセスクラス <br>
 */
public class HikiateDbAcc extends SateiDbAcc {

	private HikiateForm koForm = null;			//アクションフォーム

	private static final String KBN_KEY			= "common_OC1104";
	private static final String FLG_KBN_KEY		= "flg_kbn";
	private static final String HANYO1			= "1";
	private static final String STR_ZERO			= "0";
	//結果セット取得用文字列
	private static final String SATEI_ANKEN_NO			= "satei_anken_no";
	private static final String KBN_VAL					= "kbn_val";
	private static final String KBN_HYOUJI_VAL			= "kbn_hyouji_val";
	private static final String SHONIN_ID					= "togo_id";
	private static final String SHONINSHA					= "tanto_name";
	private static final String SAIKEN_KBN				= "saiken_kbn";
	private static final String TUUKA_CD					= "tuuka_cd";
	private static final String KANJO_HYOUJI_KBN			= "kanjo_hyouji_kbn";
	private static final String KINGAKU					= "kingaku";
	private static final String SIHANKI_TYUSYUTU_FLG		= "sihanki_tyusyutu_flg";
	private static final String FLG_KBN					= "flg_kbn";
    private static final String RYUHOSAIMU				= "ryuhosaimu";
    private static final String OTHRYUHOSAIMU				= "oth_ryuhosaimu";
    private static final String HOZEN						= "hozen";
    private static final String SONOTAKAISHU				= "sonotakaisyu";
    private static final String RIKOSEIKYUKENEN			= "riko_kenen";
    private static final String TUIKAHIKIATEKINGAKU		= "tuika_hikiate";
    private static final String KOMOKU1					= "komoku1";
    private static final String TUKACHOSEI				= "komoku2";
    private static final String OTHRISKFLG				= "other_risk_flg";
    private static final String KEIYAKUGAKU_HUDOUSANTANPO	= "hudosan_k";
    private static final String KEIYAKUGAKU_DOUSANTANPO	= "dosan_k";
    private static final String KEIYAKUGAKU_BOUEKIHOZEN	= "hoken_k";
    private static final String KEIYAKUGAKU_SONOTA		= "sonota_k";
    private static final String HYOKAGAKU_HUDOUSANTANPO	= "hudosan_h";
    private static final String HYOKAGAKU_DOUSANTANPO		= "dosan_h";
    private static final String HYOKAGAKU_BOUEKIHOZEN		= "hoken_h";
    private static final String HYOKAGAKU_SONOTA			= "sonota_h";
    private static final String EDA						= "eda";
    private static final String KIKAN_TORI_CD				= "kikan_tori_cd";
    private static final String TORISAKI_NM				= "torisaki_nm";
    private static final String KANJO_NM					= "kanjo_nm";
	
	

	//プロシージャ名
    private static final String SP_SS_OC1104_SELECT_RISK		= "SP_SS_OC1104_SELECT_RISK";//他社リスクフラグ判定用プロシージャ
    private static final String SP_SS_OL_SELECT_T2000			= "SP_SS_OL_SELECT_T2000";//留保債務取得用プロシージャ
    private static final String SP_SS_OC1104_UPDATE_T1500		= "SP_SS_OC1104_UPDATE_T1500";//査定登録内容更新用プロシージャ
    private static final String SP_SS_OL_SELECT_T1700			= "SP_SS_OL_SELECT_T1700";//引当金判定情報取得用プロシージャ
    private static final String SP_SS_O_SELECT_SHONINSHA		= "SP_SS_O_SELECT_SHONINSHA";//承認セレクトボックス取得用プロシージャ
    private static final String SP_SS_OC1104_INSERT_T2000		= "SP_SS_OC1104_INSERT_T2000";//第三者留保債務登録用プロシージャ
    
    //T17_引当金判定勘定表示区分
	private static final String KANJO_KBN_1				= "01";
	private static final String KANJO_KBN_2				= "02";
	private static final String KANJO_KBN_3				= "03";
	private static final String KANJO_KBN_4				= "04";
	private static final String KANJO_KBN_5				= "05";
	private static final String KANJO_KBN_6				= "06";
	private static final String KANJO_KBN_7				= "07";
	private static final String KANJO_KBN_8				= "08";
	private static final String KANJO_KBN_9				= "09";
	private static final String KANJO_KBN_10				= "10";
	private static final String KANJO_KBN_11				= "11";
	private static final String KANJO_KBN_12				= "12";
	private static final String KANJO_KBN_14				= "14";
	private static final String KANJO_KBN_15				= "15";
	private static final String KANJO_KBN_16				= "16";

	//T12_コメント登録箇所
    private static final String POINT_SONOTANONAIYO				= "50";
	private static final String POINT_SONOTAKAISHUNONAIYO			= "60";
	private static final String POINT_RIKOSEIKYUKENEN				= "70";
	private static final String POINT_HIKIATEKINSANTEIKONKYO		= "80";
	private static final String POINT_KONGONOKAISHUMITOSHI		= "90";
	private static final String POINT_FLGCOMMENT					= "95";

	//T20_留保債務枝番
	private static final String RYUHO_EDA01				= "01";
	private static final String RYUHO_EDA02				= "02";
	private static final String RYUHO_EDA03				= "03";

	/**
	 * コンストラクタ <br>
	 * @param appcontext
	 */
	public HikiateDbAcc(AppContext appcontext) {
		super(appcontext);
		//Bean取得
		form = (HikiateForm)appContext.getActionForm();
	}
	
	/**
	 * コメント取得処理 <br>
	 * @param String
	 * @param String
	 * @param String
	 * @exception SQLException
	 */
	public void getFlgComment(String ankenNo,String phase,String sansyoPoint) throws SQLException {

		koForm = (HikiateForm)form;
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getComment(ankenNo,phase,sansyoPoint);
            
			//ActionFormに取得値を格納
			while(rs.next()){
				if(POINT_FLGCOMMENT.equals(rs.getString(TOROKU_POINT))){
					koForm.setFlgcomment(Function.trim(rs.getString(COMMENT_VAL)));
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
	public void getCommentOC1104(String ankenNo,String phase,String sansyoPoint) throws SQLException {

		koForm = (HikiateForm)form;
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getComment(ankenNo,phase,sansyoPoint);
            
			//ActionFormに取得値を格納
			while(rs.next()){
				if(POINT_SONOTANONAIYO.equals(rs.getString(TOROKU_POINT))){
					koForm.setSonotanonaiyo(Function.trim(rs.getString(COMMENT_VAL)));
				}else if(POINT_SONOTAKAISHUNONAIYO.equals(rs.getString(TOROKU_POINT))){
					koForm.setSonotakaishunonaiyo(Function.trim(rs.getString(COMMENT_VAL)));
				}else if(POINT_RIKOSEIKYUKENEN.equals(rs.getString(TOROKU_POINT))){
					koForm.setRikoseikyukenennonaiyosetumei(Function.trim(rs.getString(COMMENT_VAL)));
				}else if(POINT_HIKIATEKINSANTEIKONKYO.equals(rs.getString(TOROKU_POINT))){
					koForm.setHikiatekinsanteikonkyononaiyosetumei(Function.trim(rs.getString(COMMENT_VAL)));
				}else if(POINT_KONGONOKAISHUMITOSHI.equals(rs.getString(TOROKU_POINT))){
					koForm.setKongonokaishumitoshi(Function.trim(rs.getString(COMMENT_VAL)));
				}
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
	
	/**
	 * 引当金判定情報取得処理 <br>
	 * @param TorihikisakiBean
	 * @exception SQLException
	 */
	public void getHikiate(TorihikisakiBean toriBean) throws SQLException {

		koForm = (HikiateForm)form;
		ResultSet rs = null;

    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1700,sqlExec);
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn(toriBean.getSystem_kbn());
        exCstmt.setResultSet(RESULTSET);

        try{
            //SQL実行
        	exCstmt.execute();
        	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			//ActionFormに取得値を格納
			while(rs.next()){
				if(KANJO_KBN_1.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					koForm.setUketoritegata(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
				}else if(KANJO_KBN_2.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					koForm.setYushutsuuketoritegata(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
				}else if(KANJO_KBN_3.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					koForm.setUrikakekin(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
				}else if(KANJO_KBN_4.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					koForm.setTorihikimaewatashikin(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
				}else if(KANJO_KBN_5.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					koForm.setTatekaekin(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
				}else if(KANJO_KBN_6.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					koForm.setMishunyukin(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
				}else if(KANJO_KBN_7.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					koForm.setMishushueki(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
				}else if(KANJO_KBN_8.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					koForm.setTankikashitsukekin(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
				}else if(KANJO_KBN_9.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					koForm.setSashiirehosyokin(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
				}else if(KANJO_KBN_10.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					koForm.setKaribaraikin(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
				}else if(KANJO_KBN_11.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					koForm.setChokikashitsukekin(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
				}else if(KANJO_KBN_12.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					koForm.setSonotatousi(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
				}else if(KANJO_KBN_14.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					koForm.setHoshosaimugokei(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
				}else if(KANJO_KBN_15.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					koForm.setKibikiatekin(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
				}else if(KANJO_KBN_16.equals(Function.trim(rs.getString(KANJO_HYOUJI_KBN)))){
					if(GS.GSS.equals(toriBean.getSystem_kbn())){
						koForm.setHanyo1(formatKingakuNotKanma(rs.getString(KINGAKU),toriBean.getSystem_kbn()));
					}
				}
				koForm.setTani(Function.trim(rs.getString(TUUKA_CD)));
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 * 一般債権判定処理 <br>
	 * @param TorihikisakiBean
	 * @exception SQLException
	 */
	public void isIppanSaiken(TorihikisakiBean tori_bean) throws SQLException {

		koForm = (HikiateForm)form;
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getSateiData(tori_bean.getAnken_no(),tori_bean.getPhase(),tori_bean.getSystem_kbn());
			//ActionFormに取得値を格納
			while(rs.next()){
				if(GS.IPPAN_SAIKEN.equals(rs.getString(SAIKEN_KBN))){
					koForm.setIppanFlg(true);
				}
				
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 * 留保債務取得処理 <br>
	 * @param String
	 * @param String
	 * @param String
	 * @exception SQLException
	 */
	public void getRyuhosaimuData(String ankenNo,String phase,String sysKbn) throws SQLException {

		koForm = (HikiateForm)form;
		ResultSet rs = null;
        
    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T2000,sqlExec);
        exCstmt.setStringIn(sysKbn);
        exCstmt.setStringIn(phase);
        exCstmt.setStringIn(ankenNo);
        exCstmt.setResultSet(RESULTSET);

        try{
            //SQL実行
        	exCstmt.execute();
        	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			//ActionFormに取得値を格納
			while(rs.next()){
				if(RYUHO_EDA01.equals(rs.getString(EDA))){
					koForm.setKanjo_cd1(Function.trim(rs.getString(KIKAN_TORI_CD)));
					koForm.setTori_nm1(Function.trim(rs.getString(TORISAKI_NM)));
					koForm.setKanjo_kamoku1(Function.trim(rs.getString(KANJO_NM)));
					koForm.setKingaku1(formatKingakuNotKanma(rs.getString(KINGAKU),sysKbn));
				}else if(RYUHO_EDA02.equals(rs.getString(EDA))){
					koForm.setKanjo_cd2(Function.trim(rs.getString(KIKAN_TORI_CD)));
					koForm.setTori_nm2(Function.trim(rs.getString(TORISAKI_NM)));
					koForm.setKanjo_kamoku2(Function.trim(rs.getString(KANJO_NM)));
					koForm.setKingaku2(formatKingakuNotKanma(rs.getString(KINGAKU),sysKbn));
				}else if(RYUHO_EDA03.equals(rs.getString(EDA))){
					koForm.setKanjo_cd3(Function.trim(rs.getString(KIKAN_TORI_CD)));
					koForm.setTori_nm3(Function.trim(rs.getString(TORISAKI_NM)));
					koForm.setKanjo_kamoku3(Function.trim(rs.getString(KANJO_NM)));
					koForm.setKingaku3(formatKingakuNotKanma(rs.getString(KINGAKU),sysKbn));
				}
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	public void getOthriskflg(TorihikisakiBean tori_bean) throws SQLException{

		koForm = (HikiateForm)form;
		ResultSet rs = null;

    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC1104_SELECT_RISK,sqlExec);
        exCstmt.setStringIn(tori_bean.getKanjo_cd());
        exCstmt.setStringIn(tori_bean.getSatei_ki());
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
				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(SATEI_ANKEN_NO))))){
					koForm.setOthriskflg(GS.ON);
					koForm.setOthriskflgRead(YOMITORI_TRUE);
					koForm.setOthriskflgTab(TAB_HUKA);
					break;
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
	public void getSateiDataOC1104(String ankenNo,String phase,String sysKbn) throws SQLException {

		koForm = (HikiateForm)form;
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getSateiData(ankenNo,phase,sysKbn);
            
			//ActionFormに取得値を格納
			while(rs.next()){
				koForm.setRyuhosaimu(formatKingakuNotKanma(rs.getString(RYUHOSAIMU),sysKbn));
				koForm.setOthryuhosaimu(formatKingakuNotKanma(rs.getString(OTHRYUHOSAIMU),sysKbn));
				koForm.setHozen(formatKingakuNotKanma(rs.getString(HOZEN),sysKbn));
				koForm.setSonotakaishu(formatKingakuNotKanma(rs.getString(SONOTAKAISHU),sysKbn));
				koForm.setRikoseikyukenen(formatKingakuNotKanma(rs.getString(RIKOSEIKYUKENEN),sysKbn));
				koForm.setTuikahikiatekingaku(formatKingakuNotKanma(rs.getString(TUIKAHIKIATEKINGAKU),sysKbn));
				koForm.setTukachosei(formatKingakuNotKanma(rs.getString(TUKACHOSEI),sysKbn));
				if(!GS.GSS.equals(sysKbn)){
					koForm.setHanyo1(formatKingakuNotKanma(rs.getString(KOMOKU1),sysKbn));
				}
				koForm.setOthriskflg(Function.trim(rs.getString(OTHRISKFLG)));
				//IT013対応
				koForm.setKeiyakugaku_hudousantanpo(formatKingakuNotKanma(rs.getString(KEIYAKUGAKU_HUDOUSANTANPO),sysKbn));
				koForm.setKeiyakugaku_dousantanpo(formatKingakuNotKanma(rs.getString(KEIYAKUGAKU_DOUSANTANPO),sysKbn));
				koForm.setKeiyakugaku_bouekihoken(formatKingakuNotKanma(rs.getString(KEIYAKUGAKU_BOUEKIHOZEN),sysKbn));
				koForm.setKeiyakugaku_sonota(formatKingakuNotKanma(rs.getString(KEIYAKUGAKU_SONOTA),sysKbn));
				koForm.setHyokagaku_hudousantanpo(formatKingakuNotKanma(rs.getString(HYOKAGAKU_HUDOUSANTANPO),sysKbn));
				koForm.setHyokagaku_dousantanpo(formatKingakuNotKanma(rs.getString(HYOKAGAKU_DOUSANTANPO),sysKbn));
				koForm.setHyokagaku_bouekihoken(formatKingakuNotKanma(rs.getString(HYOKAGAKU_BOUEKIHOZEN),sysKbn));
				koForm.setHyokagaku_sonota(formatKingakuNotKanma(rs.getString(HYOKAGAKU_SONOTA),sysKbn));
				//IT013ここまで
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 * 四半期抽出項目取得処理 <br>
	 * @param String
	 * @param String
	 * @param String
	 * @exception SQLException
	 */
	public void getShihankiChushutsuKoumoku(String ankenNo,String phase,String sysKbn) throws SQLException {

		koForm = (HikiateForm)form;
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getSateiData(ankenNo,phase,sysKbn);
            
			//ActionFormに取得値を格納
			while(rs.next()){
				koForm.setOthriskflg(Function.trim(rs.getString(OTHRISKFLG)));
				koForm.setShihankichushutsuflg(Function.trim(rs.getString(SIHANKI_TYUSYUTU_FLG)));
				koForm.setFlgkbn(Function.trim(rs.getString(FLG_KBN)));
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 * 汎用タイトル取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getHanyouList() throws SQLException {

		koForm = (HikiateForm)form;
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getKbnval(KBN_KEY,user_bean.getComWorkflowSystemkbn(),cmnData.getComLangMode());
            
			//ActionFormに取得値を格納
			while(rs.next()){
				if(HANYO1.equals(rs.getString(KBN_VAL))){
					koForm.setHanyo1title((rs.getString(KBN_HYOUJI_VAL)));
				}
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
	
	/**
	 * フラグ区分セレクトボック取得処理 <br>
	 * @exception SQLException
	 */
	public void getFlgKbnList() throws SQLException {

		koForm = (HikiateForm)form;
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getKbnval(FLG_KBN_KEY,user_bean.getComWorkflowSystemkbn(),cmnData.getComLangMode());
            
			//ActionFormに取得値を格納
			LinkedHashMap<String,String> flgKbnList = new LinkedHashMap<String,String>();
			flgKbnList.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
			while(rs.next()){
				flgKbnList.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
			}
	    	koForm.setFlgKbnList(flgKbnList);	
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
	
	/**
	 * 承認者セレクトボック取得処理 <br>
	 * @param TorihikisakiBean
	 * @exception SQLException
	 */
	public void getShoninList(TorihikisakiBean toriBean,HashMap map) throws SQLException {

		koForm = (HikiateForm)form;
		ResultSet rs = null;
    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_SHONINSHA,sqlExec);
        exCstmt.setStringIn(cmnData.getComLangMode());
        exCstmt.setStringIn(toriBean.getSateikaisya_cd());
        exCstmt.setStringIn(toriBean.getBunrui2());
        exCstmt.setStringIn(toriBean.getBu_cd());
        exCstmt.setStringIn(toriBean.getTaisyo_ym());
        exCstmt.setStringIn((String)map.get(GS.JI_JISHI_PHASE));
        exCstmt.setStringIn((String)map.get(GS.JI_KAISHI_STATUS));
        exCstmt.setStringIn(Function.trim(user_bean.getComNiji_satei_kbn()));
        exCstmt.setStringIn(Function.trim(toriBean.getSystem_kbn()));
        exCstmt.setResultSet(RESULTSET);
        
		try{
            //SQL実行
        	exCstmt.execute();
        	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
            
			//ActionFormに取得値を格納
			LinkedHashMap<String,String> shoninList = new LinkedHashMap<String,String>();
			shoninList.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
			while(rs.next()){
				//課題No.209
				//修正開始
				//shoninList.put(rs.getString(SHONINSHA),rs.getString(SHONIN_ID));	    			
				shoninList.put(rs.getString(SHONIN_ID),rs.getString(SHONINSHA));	    			
				//修正完了
			}
	    	koForm.setShoninList(shoninList);	
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

    	koForm = (HikiateForm)form;
    	
    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC1104_UPDATE_T1500,sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn(toriBean.getPhase());
        if(GS.GSS.equals(toriBean.getSystem_kbn())){
            exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        }else{
            exCstmt.setStringIn(koForm.getHanyo1());
        }
        exCstmt.setStringIn(this.toZero(koForm.getRyuhosaimu()));
        exCstmt.setStringIn(this.toZero(koForm.getOthryuhosaimu()));
        exCstmt.setStringIn(this.toZero(koForm.getHozen()));
        exCstmt.setStringIn(koForm.getSonotakaishu());
        exCstmt.setStringIn(koForm.getRikoseikyukenen());
        exCstmt.setStringIn(this.toZero(koForm.getTuikahikiatekingaku()));
        exCstmt.setStringIn(koForm.getTukachosei());
        exCstmt.setStringIn(koForm.getKeiyakugaku_hudousantanpo());
        exCstmt.setStringIn(koForm.getKeiyakugaku_dousantanpo());
        exCstmt.setStringIn(koForm.getKeiyakugaku_bouekihoken());
        exCstmt.setStringIn(koForm.getKeiyakugaku_sonota());
        exCstmt.setStringIn(koForm.getHyokagaku_hudousantanpo());
        exCstmt.setStringIn(koForm.getHyokagaku_dousantanpo());
        exCstmt.setStringIn(koForm.getHyokagaku_bouekihoken());
        exCstmt.setStringIn(koForm.getHyokagaku_sonota());
        exCstmt.setStringIn(koForm.getShihankichushutsuflg());
        exCstmt.setStringIn(koForm.getFlgkbn());
        exCstmt.setStringIn(koForm.getOthriskflg());
        
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }
    /**
     * 第三者留保債務登録処理<br>
	 * @param TorihikisakiBean
     * 
     * @throws SQLException
     */
    public void insertOthRyuhoSaimu(TorihikisakiBean toriBean) throws SQLException{

    	koForm = (HikiateForm)form;
    	
    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC1104_INSERT_T2000,sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn(toriBean.getPhase());
        exCstmt.setStringIn(toriBean.getSystem_kbn());
        exCstmt.setStringIn(toriBean.getMise_cd());
        exCstmt.setStringIn(koForm.getKanjo_cd1());
        exCstmt.setStringIn(koForm.getTori_nm1());
        exCstmt.setStringIn(koForm.getKanjo_kamoku1());
        exCstmt.setStringIn(koForm.getKingaku1());
        exCstmt.setStringIn(koForm.getKanjo_cd2());
        exCstmt.setStringIn(koForm.getTori_nm2());
        exCstmt.setStringIn(koForm.getKanjo_kamoku2());
        exCstmt.setStringIn(koForm.getKingaku2());
        exCstmt.setStringIn(koForm.getKanjo_cd3());
        exCstmt.setStringIn(koForm.getTori_nm3());
        exCstmt.setStringIn(koForm.getKanjo_kamoku3());
        exCstmt.setStringIn(koForm.getKingaku3());
        
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }
    /**
     * 空文字を"0"に変換<br>
	 * @param String
     */
    public String toZero(String str){
    	if(GS.EMPTY_CHARCTER.equals(str)){
        	return STR_ZERO;
    	}
    	return str;
    }
}