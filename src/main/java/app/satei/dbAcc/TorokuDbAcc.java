/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.dbAcc;

import app.TorihikisakiBean;
import app.ZaimuBean;
import app.satei.form.TorokuForm;
import common.AppContext;
import common.db.ExCallableStatement;
import common.global.GS;
import common.util.Function;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * OC1102_査定_取引先概要 DBアクセスクラス <br>
 */
public class TorokuDbAcc extends SateiDbAcc {

	private TorokuForm koForm = null;			//アクションフォーム

	private static final String KBN_KEY			= "common_OC1102";
	private static final String HANYO1			= "1";
	private static final String HANYO2			= "2";
	private static final String HANYO3			= "3";
	//結果セット取得用文字列
	private static final String KBN_VAL					= "kbn_val";
	private static final String KBN_HYOUJI_VAL			= "kbn_hyouji_val";
	private static final String JIGYONAIYO				= "jigyonaiyo";
	private static final String KABUNUSI_NM1				= "kabunusi_nm1";
	private static final String KABUNUSI_NM2				= "kabunusi_nm2";
	private static final String KABUNUSI_NM3				= "kabunusi_nm3";
	private static final String KABUNUSI_NM4				= "kabunusi_nm4";
	private static final String KABUNUSI_NM5				= "kabunusi_nm5";
	private static final String KABUSU1					= "kabusu1";
	private static final String KABUSU2					= "kabusu2";
	private static final String KABUSU3					= "kabusu3";
	private static final String KABUSU4					= "kabusu4";
	private static final String KABUSU5					= "kabusu5";
	private static final String HIRITU1					= "hiritu1";
	private static final String HIRITU2					= "hiritu2";
	private static final String HIRITU3					= "hiritu3";
	private static final String HIRITU4					= "hiritu4";
	private static final String HIRITU5					= "hiritu5";
	private static final String SIC_SIM_NM				= "sic_sim_nm";
	private static final String SYOZAI_ADR				= "syozai_adr";
	private static final String URIAGEDAKA				= "uriagedaka";
	private static final String URIAGESORIEKI				= "uriagesorieki";
	private static final String HANBAIHIKANRIHI			= "hanbaihikanrihi";
	private static final String EIGYORIEKI				= "eigyorieki";
	private static final String HANYOU1					= "hanyou1";
	private static final String HANYOU2					= "hanyou2";
	private static final String HANYOU3					= "hanyou3";
	private static final String TOKIJUNRIEKI				= "tokijunrieki";
	private static final String HAITOKIN					= "haitokin";
	private static final String GENKASYOKYAKUHI			= "genkasyokyakuhi";
	private static final String EIGYO_CF					= "eigyo_cf";
	private static final String RYUDOSISAN				= "ryudosisan";
	private static final String KOTEISISAN				= "koteisisan";
	private static final String SISANGOKEI				= "sisangokei";
	private static final String RYUDOHUSAI				= "ryudohusai";
	private static final String KOTEIHUSAI				= "koteihusai";
	private static final String HUSAIGOKEI				= "husaigokei";
	private static final String SIHONKIN					= "sihonkin";
	private static final String NAIBURYUHO				= "naiburyuho";
	private static final String JIKOSIHONGOKEI			= "jikosihongokei";
	private static final String KANSAN_ISO_CURRENCY_NM	= "kansan_iso_currency_nm";
	private static final String HYOUJI_TANI				= "hyouji_tani";
	private static final String TANTAI_RENKETU			= "tantai_renketu";
	private static final String KESSAN_KI					= "kessan_ki";

	//プロシージャ名
    private static final String SP_SS_O_UPDATE_T1400			= "SP_SS_O_UPDATE_T1400";       //進捗更新プロシージャ
	private static final String SP_SS_OL_SELECT_ZAIMU			= "SP_SS_OL_SELECT_ZAIMU";//財務情報取得用プロシージャ
	private static final String SP_SS_OL_SELECT_E0400			= "SP_SS_OL_SELECT_E0400";//D&B情報取得用プロシージャ
    private static final String SP_SS_OC1102_UPDATE_T1500		= "SP_SS_OC1102_UPDATE_T1500";//査定登録内容更新用プロシージャ
	
	private static final String POINT_KESAN			= "10";		//決算概況(登録ポイント10)
    private static final String NUM_FMT 				= "##,###,###,###,###,##0";	//フォーマット
    private static final String HIRITU_FMT 			= "##,###,###,###,###,##0.00";	//フォーマット

	/**
	 * コンストラクタ <br>
	 * @param appcontext
	 */
	public TorokuDbAcc(AppContext appcontext) {
		super(appcontext);
		//Bean取得
		form = (TorokuForm)appContext.getActionForm();
	}
	
	/**
	 * D&B企業情報取得処理 <br>
	 * @param TorihikisakiBean
	 * @exception SQLException
	 */
	public void getDBkihon(TorihikisakiBean tori_bean) throws SQLException {

		koForm = (TorokuForm)form;
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
				koForm.setSicSm(Function.trim(rs.getString(SIC_SIM_NM)));
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
	 * 財務情報取得処理 <br>
	 * @param TorihikisakiBean
	 * @exception SQLException
	 */
	public void getZaimu(TorihikisakiBean tori_bean) throws SQLException {

		koForm = (TorokuForm)form;
		ResultSet rs = null;
        ZaimuBean zaimu = null;
        List<ZaimuBean> list = new ArrayList<ZaimuBean>();
        list.add(0,new ZaimuBean());
        list.add(1,new ZaimuBean());
        list.add(2,new ZaimuBean());
        koForm.setZaimu(list);

    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_ZAIMU,sqlExec);
        exCstmt.setStringIn(cmnData.getComLangMode());
        exCstmt.setStringIn(tori_bean.getSystem_kbn());
        exCstmt.setStringIn(tori_bean.getTaisyo_ym());
        exCstmt.setStringIn(tori_bean.getShikibetu_cd());
        exCstmt.setStringIn(tori_bean.getTogo_tori_cd());
        exCstmt.setStringIn(tori_bean.getSyori_kaisu());
        exCstmt.setStringIn(tori_bean.getSateikaisya_cd());
        exCstmt.setStringIn(tori_bean.getMise_cd());
        exCstmt.setResultSet(RESULTSET);
        
        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
	        rs = exCstmt.getResultSet(RESULTSET);
            
			//ActionFormに取得値を格納
	        int i = 0;
			while(rs.next()){
				zaimu = (ZaimuBean)list.get(i);
				//財務表左部
				zaimu.setUriagedaka(formatKingaku(rs.getString(URIAGEDAKA)));
				zaimu.setUriagesorieki(formatKingaku(rs.getString(URIAGESORIEKI)));
				zaimu.setHanbaihikanrihi(formatKingaku(rs.getString(HANBAIHIKANRIHI)));
				zaimu.setEigyo_cf(formatKingaku(rs.getString(EIGYO_CF)));
				zaimu.setEigyorieki(formatKingaku(rs.getString(EIGYORIEKI)));
				zaimu.setHanyou1(formatKingaku(rs.getString(HANYOU1)));
				zaimu.setHanyou2(formatKingaku(rs.getString(HANYOU2)));
				zaimu.setHanyou3(formatKingaku(rs.getString(HANYOU3)));
				zaimu.setTokijunrieki(formatKingaku(rs.getString(TOKIJUNRIEKI)));
				zaimu.setHaitokin(formatKingaku(rs.getString(HAITOKIN)));
				zaimu.setGenkasyokyakuhi(formatKingaku(rs.getString(GENKASYOKYAKUHI)));
				//資産
				zaimu.setRyudosisan(formatKingaku(rs.getString(RYUDOSISAN)));
				zaimu.setKoteisisan(formatKingaku(rs.getString(KOTEISISAN)));
				zaimu.setSisangokei(formatKingaku(rs.getString(SISANGOKEI)));
				//負債
				zaimu.setRyudohusai(formatKingaku(rs.getString(RYUDOHUSAI)));
				zaimu.setKoteihusai(formatKingaku(rs.getString(KOTEIHUSAI)));
				zaimu.setHusaigokei(formatKingaku(rs.getString(HUSAIGOKEI)));
				//自己資本
				zaimu.setSihonkin(formatKingaku(rs.getString(SIHONKIN)));
				zaimu.setNaiburyuho(formatKingaku(rs.getString(NAIBURYUHO)));
				zaimu.setJikosihongokei(formatKingaku(rs.getString(JIKOSIHONGOKEI)));
				//財務表タイトル部
				zaimu.setTantai_renketu(Function.trim(rs.getString(TANTAI_RENKETU)));
				zaimu.setKessan_ki(Function.trim(rs.getString(KESSAN_KI)));
				
				//通貨コード＆表示単位設定
				if(!Function.trim(rs.getString(KANSAN_ISO_CURRENCY_NM)).equals(GS.EMPTY_CHARCTER)){
					zaimu.setTukaCd(Function.trim(rs.getString(KANSAN_ISO_CURRENCY_NM)));
				}
				if(!Function.trim(rs.getString(HYOUJI_TANI)).equals(GS.EMPTY_CHARCTER)){
					zaimu.setHyoujiTani(GS.KAKKO_HIDARI + Function.trim(rs.getString(HYOUJI_TANI)) + GS.KAKKO_MIGI);
				}
				
				i++;
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
	public void getCommentOC1102(String ankenNo,String phase,String sansyoPoint) throws SQLException {

		koForm = (TorokuForm)form;
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getComment(ankenNo,phase,sansyoPoint);
            
			//ActionFormに取得値を格納
			while(rs.next()){
				if(POINT_KESAN.equals(rs.getString(TOROKU_POINT))){
					koForm.setKesanGaikyo(Function.trim(rs.getString(COMMENT_VAL)));
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
	public void getSateiDataOC1102(String ankenNo,String phase,String sysKbn) throws SQLException {

		koForm = (TorokuForm)form;
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getSateiData(ankenNo,phase,sysKbn);
            
			//ActionFormに取得値を格納
			while(rs.next()){
				koForm.setJigyoNaiyo(Function.trim(rs.getString(JIGYONAIYO)));
				koForm.setKabunusiNm1(Function.trim(rs.getString(KABUNUSI_NM1)));
				koForm.setKabunusiNm2(Function.trim(rs.getString(KABUNUSI_NM2)));
				koForm.setKabunusiNm3(Function.trim(rs.getString(KABUNUSI_NM3)));
				koForm.setKabunusiNm4(Function.trim(rs.getString(KABUNUSI_NM4)));
				koForm.setKabunusiNm5(Function.trim(rs.getString(KABUNUSI_NM5)));
				//IT042対応
				koForm.setKabusu1(Function.trim(rs.getString(KABUSU1)));
				koForm.setKabusu2(Function.trim(rs.getString(KABUSU2)));
				koForm.setKabusu3(Function.trim(rs.getString(KABUSU3)));
				koForm.setKabusu4(Function.trim(rs.getString(KABUSU4)));
				koForm.setKabusu5(Function.trim(rs.getString(KABUSU5)));
				//IT042ここまで
				koForm.setHiritu1(formatHiritu(rs.getString(HIRITU1)));
				koForm.setHiritu2(formatHiritu(rs.getString(HIRITU2)));
				koForm.setHiritu3(formatHiritu(rs.getString(HIRITU3)));
				koForm.setHiritu4(formatHiritu(rs.getString(HIRITU4)));
				koForm.setHiritu5(formatHiritu(rs.getString(HIRITU5)));
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

		koForm = (TorokuForm)form;
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getKbnval(KBN_KEY,user_bean.getComWorkflowSystemkbn(),cmnData.getComLangMode());
            
			//ActionFormに取得値を格納
			while(rs.next()){
				if(HANYO1.equals(rs.getString(KBN_VAL))){
					koForm.setHanyoTitle1(rs.getString(KBN_HYOUJI_VAL));
				}else if(HANYO2.equals(rs.getString(KBN_VAL))){
					koForm.setHanyoTitle2(rs.getString(KBN_HYOUJI_VAL));
				}else if(HANYO3.equals(rs.getString(KBN_VAL))){
					koForm.setHanyoTitle3(rs.getString(KBN_HYOUJI_VAL));
				}
			}
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

    	koForm = (TorokuForm)form;
    	
    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC1102_UPDATE_T1500,sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn(toriBean.getPhase());
        exCstmt.setStringIn(koForm.getJigyoNaiyo());
        exCstmt.setStringIn(koForm.getKabunusiNm1());
        exCstmt.setStringIn(koForm.getKabunusiNm2());
        exCstmt.setStringIn(koForm.getKabunusiNm3());
        exCstmt.setStringIn(koForm.getKabunusiNm4());
        exCstmt.setStringIn(koForm.getKabunusiNm5());
        exCstmt.setStringIn(koForm.getKabusu1());
        exCstmt.setStringIn(koForm.getKabusu2());
        exCstmt.setStringIn(koForm.getKabusu3());
        exCstmt.setStringIn(koForm.getKabusu4());
        exCstmt.setStringIn(koForm.getKabusu5());
        exCstmt.setStringIn(koForm.getHiritu1());
        exCstmt.setStringIn(koForm.getHiritu2());
        exCstmt.setStringIn(koForm.getHiritu3());
        exCstmt.setStringIn(koForm.getHiritu4());
        exCstmt.setStringIn(koForm.getHiritu5());
        
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }
    

    /**
     * T14_査定進捗管理の更新(処理中用)<br>
     * 
     * @param toriBean
     * @param map
     * @throws SQLException
     */
    public void setSateiStatKaijo(TorihikisakiBean toriBean,HashMap map,String torokuGamen,String stat) throws SQLException{
    	String phase = (String)map.get(GS.JISHI_PHASE);
        //ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1400,sqlExec);
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn(phase);
        exCstmt.setStringIn(stat);
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        exCstmt.setStringIn(torokuGamen);
        exCstmt.setStringIn(toriBean.getSasi_ten_flg());
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(GS.TORIMODOSHI_HUKA);
        
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
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
}