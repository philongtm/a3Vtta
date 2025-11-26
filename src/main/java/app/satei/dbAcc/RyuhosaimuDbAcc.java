/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.dbAcc;

import app.MeisaisyosaiBean;
import app.TorihikisakiBean;
import app.satei.form.RyuhosaimuForm;
import common.AppContext;
import common.db.ExCallableStatement;
import common.global.GS;
import common.util.Function;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * OC1105_査定_留保債務登録 DBアクセスクラス <br>
 */
public class RyuhosaimuDbAcc extends SateiDbAcc {

	private RyuhosaimuForm koForm = null;			//アクションフォーム

	//判定用文字列
	private static final String RYUHO_SAIMU = "1";
	private static final String STR_ZERO = "0";
	
	//結果セット取得用文字列
	private static final String SHOW						= "show";
	private static final String KBN_VAL					= "kbn_val";
	private static final String KBN_HYOUJI_VAL			= "kbn_hyouji_val";
	private static final String RYUHOSAIMU_KBN			= "ryuhosaimu_kbn";
	private static final String KINGAKU					= "kingaku";
	private static final String SATEI_ANKEN_NO_EDA		= "satei_anken_no_eda";
	private static final String SOSHIKI             		= "soshiki";
	private static final String KANJO_NM         		   	= "kanjo_nm";
	private static final String SHUSI_DT            		= "shusi_dt";
	private static final String KEIYAKU_DENPYO_NO   		= "keiyaku_denpyo_no";
	private static final String HANTEI			   		= "hantei";
	private static final String BIKO						= "biko";
	private static final String TUUKA_CD					= "tuuka_cd";
	private static final String KANJO_UCHI_NM				= "kanjo_uchi_nm";

	//プロシージャ名
    private static final String SP_SS_O_SELECT_RYUHOKEI	= "SP_SS_O_SELECT_RYUHOKEI";//債務計取得用プロシージャ
    private static final String SP_SS_OL_SELECT_RYUHO		= "SP_SS_OL_SELECT_RYUHO";//債務一覧取得用プロシージャ
    private static final String SP_SS_OC1105_DELETE_T1900	= "SP_SS_OC1105_DELETE_T1900";//留保債務削除用プロシージャ
    private static final String SP_SS_OC1105_INSERT_T1900	= "SP_SS_OC1105_INSERT_T1900";//留保債務登録用プロシージャ
    private static final String SP_SS_OC1105_UPDATE_T1500	= "SP_SS_OC1105_UPDATE_T1500";//査定データ更新用プロシージャ

    /**
	 * コンストラクタ <br>
	 * @param appcontext
	 */
	public RyuhosaimuDbAcc(AppContext appcontext) {
		super(appcontext);
		//Bean取得
		form = (RyuhosaimuForm)appContext.getActionForm();
	}
	
	/**
	 * 表示件数セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getShow() throws SQLException {

		koForm = (RyuhosaimuForm)form;
		//ResultSet取得
		ResultSet rs = null;
		
		try{
			//ResultSet取得
            rs = null;
            //SQL実行
			rs = super.getKbnval(SHOW,user_bean.getComWorkflowSystemkbn(),cmnData.getComLangMode());
            
			//ActionFormに取得値を格納
			LinkedHashMap<String,String> ar_show = new LinkedHashMap<String,String>();
			while(rs.next()){
				ar_show.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
			}
			koForm.setShowList(ar_show);
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
	public void getKei(TorihikisakiBean tori_bean) throws SQLException {

		koForm = (RyuhosaimuForm)form;
		double RYUHOSAIMU_KEI = 0;
		double SAIMU_KEI = 0;
		//ResultSet取得
		ResultSet rs = null;
        
    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_RYUHOKEI,sqlExec);
        exCstmt.setStringIn(tori_bean.getSystem_kbn());
        exCstmt.setStringIn(tori_bean.getPhase());
        exCstmt.setStringIn(tori_bean.getAnken_no());
        exCstmt.setResultSet(RESULTSET);

        try{
            //SQL実行
        	exCstmt.execute();
        	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			//ActionFormに取得値を格納
			while(rs.next()){
		    	if(RYUHO_SAIMU.equals(rs.getString(RYUHOSAIMU_KBN))){
		    		RYUHOSAIMU_KEI += rs.getDouble(KINGAKU);
		    	}
		    	SAIMU_KEI += rs.getDouble(KINGAKU);
			}
			
			//金額フォーマット
			koForm.setRyuhosaimukei(this.formatKingaku(RYUHOSAIMU_KEI,tori_bean.getSystem_kbn()));
			koForm.setSaimusoukei(this.formatKingaku(SAIMU_KEI,tori_bean.getSystem_kbn()));
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
	public void getRyuhosaimuData(TorihikisakiBean tori_bean) throws SQLException {

		koForm = (RyuhosaimuForm)form;
		//ResultSet取得
		ResultSet rs = null;
        
    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_RYUHO,sqlExec);
        exCstmt.setStringIn(tori_bean.getSystem_kbn());
        exCstmt.setStringIn(tori_bean.getPhase());
        exCstmt.setStringIn(tori_bean.getAnken_no());
        exCstmt.setStringIn(cmnData.getComLangMode());
        exCstmt.setResultSet(RESULTSET);
        
        try{
            //SQL実行
        	exCstmt.execute();
        	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			//ActionFormに取得値を格納
            List<MeisaisyosaiBean> list = new ArrayList<MeisaisyosaiBean>();   
			while(rs.next()){
				MeisaisyosaiBean listBean = new MeisaisyosaiBean();
				koForm.setTuka(Function.trim(rs.getString(TUUKA_CD)));
				listBean.setAnken_no_eda(Function.trim(rs.getString(SATEI_ANKEN_NO_EDA)));
				listBean.setSoshiki(Function.trim(rs.getString(SOSHIKI)));
				
				if(tori_bean.getSystem_kbn().equals(GS.GSS)){
					listBean.setKanjo_kamoku_nm(Function.trim(rs.getString(KANJO_UCHI_NM)));
				}else{
					listBean.setKanjo_kamoku_nm(Function.trim(rs.getString(KANJO_NM)));
				}
				listBean.setSyusi_yoteibi(Function.trim(rs.getString(SHUSI_DT)));
				listBean.setKingaku_kei(super.formatKingaku(rs.getString(KINGAKU),tori_bean.getSystem_kbn()));
				listBean.setKeiyaku_denpyo_no(Function.trim(rs.getString(KEIYAKU_DENPYO_NO)));
				listBean.setRyuhosaimu(Function.trim(rs.getString(HANTEI)));
				listBean.setBiko(Function.trim(rs.getString(BIKO)));
				list.add(listBean);
			}
			koForm.setAr_meisai(list);
            //ページ設定
            koForm.setPager(list);
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

    	koForm = (RyuhosaimuForm)form;
    	double ryuhoKei = Function.getValueOfDoubleC(koForm.getRyuhosaimukei());
    	
    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC1105_UPDATE_T1500,sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn(toriBean.getPhase());
        exCstmt.setDoubleIn(ryuhoKei);
        
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }
    
    /**
     * 留保債務削除処理<br>
	 * @param TorihikisakiBean
     * 
     * @throws SQLException
     */
    public void deleteRyuhoSaimu(TorihikisakiBean toriBean) throws SQLException{

    	koForm = (RyuhosaimuForm)form;
    	
    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC1105_DELETE_T1900,sqlExec);
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn(toriBean.getPhase());
        
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }
    
    
    /**
     * 留保債務登録処理<br>
	 * @param TorihikisakiBean
	 * @param List
     * @throws SQLException
     */
    public void insertRyuhoSaimu(TorihikisakiBean toriBean,MeisaisyosaiBean saimuBean) throws SQLException{

    	koForm = (RyuhosaimuForm)form;
    		
    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC1105_INSERT_T1900,sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn(toriBean.getPhase());
        exCstmt.setStringIn(saimuBean.getAnken_no_eda());
        exCstmt.setStringIn(this.toZero(saimuBean.getRyuhosaimu()));
        exCstmt.setStringIn(saimuBean.getBiko());
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }

    /**
     * システム区分により、金額をフォーマットする。<br>
     * 
     * @param kingaku 金額
     * @param systemKbn システム区分
     * @return フォーマットされた金額
     */
    protected String formatKingaku(double kingaku,String systemKbn) {
        String formatKingaku = null;
    	if (systemKbn.equals(GS.GSS)){
            //国内
        	formatKingaku = Function.format(NUM_FMT_KOKUNAI,kingaku);
        }else{
            //海外
        	formatKingaku = Function.format(NUM_FMT_KAIGAI,kingaku);
        }
        return formatKingaku;
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