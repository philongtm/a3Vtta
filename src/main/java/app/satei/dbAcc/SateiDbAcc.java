/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/20		SSC				課題No.60 引当金確認案件の取引先区分・債権区分設定
003		2009/10/22		SSC				課題No.70 転送先部門を分類2に修正
004		2009/11/18		SSC				課題No.129 案件保持ユーザをNULLで更新
005		2009/11/19		SSC				課題No.151 ユーザID更新処理修正
006		2015/09/08		SSC				BJ201408049 IA化対応時の機能改善
******************************************************************************/
package app.satei.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.global.GL;
import common.global.GS;
import common.struts.AppPagerActionForm;
import common.util.Function;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 査定DBアクセス親クラス <br>
 */
public class SateiDbAcc extends CommonDbAcc {

	protected AppContext appContext = null;			//ＡＰＰコンテキスト
	protected SessionData cmnData = null;				//機能共通セッション
	protected UserBean user_bean = null;				//ユーザ情報
	protected AppPagerActionForm form = null;			//アクションフォーム
	
	//結果セット取得用文字列
	protected static final String TOROKU_POINT		= "toroku_point";
	protected static final String COMMENT_VAL			= "comment_val";
	private static final String COUNT					= "cnt";
	private static final String ANKEN_NO				= "anken_no";
	private static final String PHASE					= "phase";
    private static final String OUTPHASE 				= "outPhase";				//フェーズ
    private static final String OUTHYOJIYM 			= "outHyojiYm";				//表示用年月
    private static final String OUTYM 				= "outYm";					//年月
    private static final String OUTSYORIKAISU 		= "outSyoriKaisu";			//処理回数
    //課題No.60
    //追加開始
	private static final String SAIKEN_KBN			= "saiken_kbn";
	private static final String TORIHIKISAKI_KBN		= "torihikisaki_kbn";
    //追加完了
	
	//プロシージャ名
    //課題No.60
    //追加開始
    private static final String SP_SS_OC_SELECT_T1400			= "SP_SS_OC_SELECT_T1400";      //引当金確認案件No.取得プロシージャ
	private static final String SP_SS_OC_UPDATE_T1500			= "SP_SS_OC_UPDATE_T1500";		//引当金確認案件の取引先区分・債権区分更新プロシージャ
    //追加完了
	private static final String SP_SS_OC_SELECT_T1500			= "SP_SS_OC_SELECT_T1500";     	//前フェーズ取得用プロシージャ
    private static final String SP_SS_OL_SELECT_T1500			= "SP_SS_OL_SELECT_T1500";     	//査定登録内容取得用プロシージャ
    private static final String SP_SS_O_SELECT_T1200			= "SP_SS_O_SELECT_T1200";      	//初期表示判定用プロシージャ
    private static final String SP_SS_OL_SELECT_T1200			= "SP_SS_OL_SELECT_T1200";      //コメント取得用プロシージャ
    private static final String SP_SS_O_DELETE_T1200			= "SP_SS_O_DELETE_T1200";      	//コメント削除用プロシージャ
    private static final String SP_SS_O_INSERT_T1200			= "SP_SS_O_INSERT_T1200";      //コメント登録用プロシージャ
    private static final String SP_SS_O_SELECT_T1403			= "SP_SS_O_SELECT_T1403";      	//前回実施案件No.取得用プロシージャ
    private static final String SP_SS_OL_SELECT_T1402			= "SP_SS_OL_SELECT_T1402";      //前回実施フェーズ等取得用プロシージャ
    private static final String SP_SS_OC_INSERT_T1500			= "SP_SS_OC_INSERT_T1500";      //査定登録プロシージャ
    private static final String SP_SS_OC_INSERT_T1900			= "SP_SS_OC_INSERT_T1900";      //留保債務登録プロシージャ
    private static final String SP_SS_OC_INSERT_T2000			= "SP_SS_OC_INSERT_T2000";      //第三者留保債務登録プロシージャ
    private static final String SP_SS_O_INSERT_T1300			= "SP_SS_O_INSERT_T1300";       //入力履歴登録プロシージャ
    private static final String SP_SS_O_INSERT_T0400			= "SP_SS_O_INSERT_T0400";       //メール配信登録プロシージャ
    private static final String SP_SS_O_UPDATE_T1400			= "SP_SS_O_UPDATE_T1400";       //進捗更新プロシージャ

    private static final String NYURYOKU_SATEI				= "2";		//査定
    protected static final String KANRYOU_FLG					= "1";		//完了フラグ
	private static final String HAISINZUMI_FLG_N				= "N";		//配信済みフラグ
	private static final String TOROKU_KBN					= "1";		//登録区分
	private static final String SASI_FLG						= "1";
	private static final String ASTA							= "*";
	protected static final String NUM_FMT_KOKUNAI 				= "##,###,###,###,###,##0.##";	//フォーマット：国内
	protected static final String NUM_FMT_KAIGAI  				= "##,###,###,###,###,##0.00";	//フォーマット：海外
	protected static final String NUM_FMT_KOKUNAI_NOTKANMA 		= "################0.##";	//フォーマット：国内
	protected static final String NUM_FMT_KAIGAI_NOTKANMA  		= "################0.00";	//フォーマット：海外
	private static final String DAIKO							= "( 代行 ";
	private static final String DAIKO_EN						= "( proxy ";

	private static final boolean TRUE						= true;
	private static final boolean FALSE						= false;
	
	/**
	 * コンストラクタ <br>
	 * 
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public SateiDbAcc(AppContext appcontext) {
		super(appcontext.getSqlExecuter(),appcontext.getLog());
		this.appContext = appcontext;
		//Bean取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		form = (AppPagerActionForm)appContext.getActionForm();
	}

	/**
	 * 初期表示判定処理 <br>
	 * @param TorihikisakiBean
	 * @param String
	 * @return boolean
	 * 
	 * @exception SQLException
	 */
	public boolean isSyokiHyouji(TorihikisakiBean toriBean,String sansyoPoint) throws SQLException {

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_T1200, sqlExec);
		boolean flg = FALSE;

		exCstmt.setStringIn(toriBean.getAnken_no());
		exCstmt.setStringIn(toriBean.getPhase());
		exCstmt.setStringIn(sansyoPoint);//例：'10','20','30'
		exCstmt.setIntOut(COUNT);

		//SQL実行	
	    exCstmt.execute();
	    isError(exCstmt);
    	
	    //初期表示
    	if(exCstmt.getInt(COUNT) == 0) {
    		flg = TRUE;
	    }
    	
    	return flg;
	}

	/**
	 * コメント取得処理 <br>
	 * @param String
	 * @param String
	 * @param String
	 * @param ResultSet
	 * 
	 * @exception SQLException
	 */
	public ResultSet getComment(String ankenNo,String phase,String sansyoPoint) throws SQLException {

		//ResultSet取得
		ResultSet rs = null;
		
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1200, sqlExec);

		exCstmt.setStringIn(ankenNo);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(sansyoPoint);//例：'10','20','30'
		//resultSet
		exCstmt.setResultSet(RESULTSET);

		//SQL実行	
    	exCstmt.execute();
    	isError(exCstmt);
		rs = exCstmt.getResultSet(RESULTSET);

    	return rs;//呼出元でclose
	}	

	/**
	 * 査定登録内容取得処理 <br>
	 * @param String
	 * @param String
	 * @param String
	 * @param ResultSet
	 * 
	 * @exception SQLException
	 */
	public ResultSet getSateiData(String ankenNo,String phase,String sysKbn) throws SQLException {

		//ResultSet取得
		ResultSet rs = null;
		
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1500,sqlExec);

		exCstmt.setStringIn(ankenNo);
		exCstmt.setStringIn(sysKbn);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setResultSet(RESULTSET);

		//SQL実行	
    	exCstmt.execute();
    	isError(exCstmt);
		rs = exCstmt.getResultSet(RESULTSET);

    	return rs;//呼出元でclose
	}	


    //課題No.60
    //追加開始
	/**
	 * 査定登録内容取得処理 <br>
	 * @param TorihikisakiBean
	 * @exception SQLException
	 */
	public Map<String,String> getSaikenToriKbn(TorihikisakiBean tori_bean) throws SQLException {

		ResultSet rs = null;
		Map<String,String> map = new HashMap<String,String>();
		
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1500,sqlExec);

		exCstmt.setStringIn(tori_bean.getAnken_no());
		exCstmt.setStringIn(tori_bean.getSystem_kbn());
		exCstmt.setStringIn(tori_bean.getPhase());
		exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setResultSet(RESULTSET);

		try{
            //SQL実行
        	exCstmt.execute();
        	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			//ActionFormに取得値を格納
			while(rs.next()){
				map.put(SAIKEN_KBN,Function.trim(rs.getString(SAIKEN_KBN)));
				map.put(TORIHIKISAKI_KBN,Function.trim(rs.getString(TORIHIKISAKI_KBN)));
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
		return map;
	}
    //追加完了

	/**
	 * コメント削除処理 <br>
	 * @param TorihikisakiBean
	 * @param String
	 * 
	 * @exception SQLException
	 */
	public void deleteComment(TorihikisakiBean toriBean,String sansyoPoint) throws SQLException {

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1200,sqlExec);

		exCstmt.setStringIn(toriBean.getAnken_no());
		exCstmt.setStringIn(toriBean.getPhase());
		exCstmt.setStringIn(sansyoPoint);//例：'10','20','30'

		//SQL実行	
    	exCstmt.execute();
    	isError(exCstmt);
	}	

	/**
	 * コメント登録処理 <br>
	 * @param TorihikisakiBean
	 * @param String
	 * @param String
	 * 
	 * @exception SQLException
	 */
	public void insertComment(TorihikisakiBean toriBean,String point,String naiyo) throws SQLException {

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1200,sqlExec);

		exCstmt.setStringIn(toriBean.getAnken_no());
		exCstmt.setStringIn(toriBean.getPhase());
		exCstmt.setStringIn(point);
		exCstmt.setStringIn(TOROKU_KBN);
		exCstmt.setStringIn(naiyo);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());

        //SQL実行	
    	exCstmt.execute();
    	isError(exCstmt);
	}	

    /**
     * 前回実施案件No.取得処理 <br>
	 * @param TorihikisakiBean
	 * @return String
     * 
     * @exception SQLException
     */
    public String getZenAnkenNo(TorihikisakiBean toriBean) throws SQLException {
    	
    	String zenAnkenNo = GS.EMPTY_CHARCTER;

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_T1403,sqlExec);

		exCstmt.setStringIn(toriBean.getSystem_kbn());
		exCstmt.setStringIn(toriBean.getSateikaisya_cd());
		exCstmt.setStringIn(toriBean.getMise_cd());
		exCstmt.setStringIn(toriBean.getKanjo_cd());
		exCstmt.setStringIn(toriBean.getSatei_ki());
		exCstmt.setResultSet(RESULTSET);

        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
            while(rs.next()){
            	zenAnkenNo = Function.trim(rs.getString(ANKEN_NO));
            }

        } finally {
            if(rs != null){
            	rs.close();
            }
        }
    	
    	return zenAnkenNo;
    }

    //課題No.60
    //追加開始
    /**
     * 引当金確認案件No.取得処理 <br>
	 * @param TorihikisakiBean
	 * @return String
     * 
     * @exception SQLException
     */
    public String getHikiateKakuninAnkenNo(TorihikisakiBean toriBean) throws SQLException {
    	
    	String hikiateKakuninAnkenNo = GS.EMPTY_CHARCTER;

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC_SELECT_T1400,sqlExec);

		exCstmt.setStringIn(toriBean.getSystem_kbn());
		exCstmt.setStringIn(toriBean.getSateikaisya_cd());
		exCstmt.setStringIn(toriBean.getMise_cd());
		exCstmt.setStringIn(toriBean.getKanjo_cd());
		exCstmt.setStringIn(toriBean.getSatei_ki());
		exCstmt.setResultSet(RESULTSET);

        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
            while(rs.next()){
            	hikiateKakuninAnkenNo = Function.trim(rs.getString(ANKEN_NO));
            }

        } finally {
            if(rs != null){
            	rs.close();
            }
        }
    	
    	return hikiateKakuninAnkenNo;
    }
    //追加完了
    
    /**
     * 案件No.のフェーズ取得処理 <br>
	 * @param String
	 * @return String
     * 
     * @exception SQLException
     */
    public String getPhase(String ankenNo) throws Exception {
    	
    	String phase = GS.EMPTY_CHARCTER;

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1402,sqlExec);
		exCstmt.setStringIn(ankenNo);
		exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setStringOut(OUTPHASE);
		exCstmt.setStringOut(OUTHYOJIYM);
		exCstmt.setStringOut(OUTYM);
		exCstmt.setStringOut(OUTSYORIKAISU);

        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            phase = exCstmt.getString(OUTPHASE);
            if(form.toString().equals(GS.OC1102)){
            	//査定_取引先概要の場合、確認メッセージ表示
            	appContext.setMsgCd(GL.WARNING_INIT,exCstmt.getString(OUTHYOJIYM));
            }

        } finally {
            if(rs != null){
            	rs.close();
            }
        }
    	
    	return phase;
    }
    
    /**
     * 案件No.のフェーズ取得処理(前フェーズ用)<br>
	 * @param String
	 * @param String
     * 
     * @exception SQLException
     */
    public String getZenPhase(String ankenNo,String phase) throws SQLException {
    	
    	String rtnPhase = GS.EMPTY_CHARCTER;

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC_SELECT_T1500,sqlExec);
		exCstmt.setStringIn(ankenNo);
		exCstmt.setStringIn(phase);
		exCstmt.setResultSet(RESULTSET);

        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
            if(rs.next()){
            	rtnPhase = Function.trim(rs.getString(PHASE));
            }

        } finally {
            if(rs != null){
            	rs.close();
            }
        }
    	
    	return rtnPhase;
    }

    /**
     * T13_入力履歴の登録 <br>
     * 
     * @param toriBean
     * @throws SQLException
     */
    public void setNyuryokuHist(TorihikisakiBean toriBean,String nyuryokuKbn,String shoninUserId) throws SQLException {

    	//ストアドプロシージャ生成
    	ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300,sqlExec);
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn(NYURYOKU_SATEI);
        exCstmt.setStringIn(toriBean.getSateikaisya_cd());            
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComUser_Nm());
        exCstmt.setStringIn(user_bean.getComUser_Nm_En());            
        exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm());
        exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm_En());            
        exCstmt.setStringIn(toriBean.getPhase());
        exCstmt.setStringIn(nyuryokuKbn);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(user_bean.getComDaiko_user_nm());
        exCstmt.setStringIn(user_bean.getComDaiko_user_nm_en());
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        exCstmt.setStringIn(shoninUserId);

        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }
    
    /**
     * T04_メール配信の登録 <br>
     * 
     * @param toriBean
     * @param map
     * @param upd_user_id
     * @throws SQLException
     */
    public void setMailHaishin(String upd_user_id,String ji_tanto_id,TorihikisakiBean toriBean,HashMap map) throws SQLException {

    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T0400,sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn(toriBean.getTaisyo_ym());
        exCstmt.setStringIn(toriBean.getSateikaisya_cd());
        //課題No.70
        //修正開始
        //exCstmt.setStringIn(toriBean.getInit_bunrui2());
        exCstmt.setStringIn(toriBean.getBunrui2());
        // 部コード(システム区分01の場合のみ設定)
        if(GS.GSS.equals(toriBean.getSystem_kbn())){
        	exCstmt.setStringIn(toriBean.getBu_cd());
        } else {
        	exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        }
        //修正完了
        exCstmt.setStringIn((String)map.get(GS.JI_JISHI_PHASE));
        exCstmt.setStringIn((String)map.get(GS.JI_KAISHI_STATUS));
        exCstmt.setStringIn(ji_tanto_id);
        exCstmt.setStringIn(HAISINZUMI_FLG_N);
        exCstmt.setStringIn(form.toString());
        exCstmt.setStringIn(upd_user_id);
            
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
	}
    
    /**
     * T15_一次二次査定の登録(次フェーズ) <br>
     * 
     * @param toriBean
     * @param map
     * @throws SQLException
     */
    public void setSateiData(TorihikisakiBean toriBean,HashMap map) throws SQLException {
        //ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC_INSERT_T1500,sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn((String)map.get(GS.JISHI_PHASE));
        exCstmt.setStringIn((String)map.get(GS.JI_JISHI_PHASE));
            
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }
    
    /**
     * T14_査定進捗管理の更新(登録・承認用)<br>
     * 
     * @param toriBean
     * @param map
     * @throws SQLException
     */
    public void setSateiStat(TorihikisakiBean toriBean,HashMap map,String shoninsha) throws SQLException{
    	String phase = (String)map.get(GS.JI_JISHI_PHASE);
    	String stat = (String)map.get(GS.JI_KAISHI_STATUS);
    	String torimodoshiFlg = GS.TORIMODOSHI_HUKA;
    	String ankenHojiUser = Function.trim(shoninsha);
    	if(KANRYOU_FLG.equals((String)map.get(GS.JISHI_PHASE_KANRYO_FLG))){
    		//実施フェーズ完了の場合
    		phase = (String)map.get(GS.JISHI_PHASE);
        	stat = GS.STATUS_KANRYO;
        	//課題No.129
        	//修正開始
        	//ankenHojiUser = toriBean.getHoji_user_id();
        	ankenHojiUser = GS.EMPTY_CHARCTER;
        	//修正完了
    	}else if(GS.STATUS_SYONIN_MACHI.equals((String)map.get(GS.JI_KAISHI_STATUS))){
        	torimodoshiFlg = GS.TORIMODOSHI_KA;
    	}

    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1400,sqlExec);
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn(phase);
        exCstmt.setStringIn(stat);
        exCstmt.setStringIn(ankenHojiUser);
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        //課題No.130
        //修正開始
        if(GS.EMPTY_CHARCTER.equals(Function.trim(user_bean.getComDaiko_userId()))){
            exCstmt.setStringIn(user_bean.getComUserId());
        }else{
            exCstmt.setStringIn(user_bean.getComDaiko_userId());
        }
        //修正完了
        exCstmt.setStringIn(torimodoshiFlg);
        
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
    public void setSateiStatSyorityu(TorihikisakiBean toriBean,HashMap map,String torokuGamen,String stat) throws SQLException{
    	String phase = (String)map.get(GS.JISHI_PHASE);
        //ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1400,sqlExec);
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn(phase);
        exCstmt.setStringIn(stat);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(torokuGamen);
        exCstmt.setStringIn(toriBean.getSasi_ten_flg());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        //課題No.130
        //修正開始
        if(GS.EMPTY_CHARCTER.equals(Function.trim(user_bean.getComDaiko_userId()))){
            exCstmt.setStringIn(user_bean.getComUserId());
        }else{
            exCstmt.setStringIn(user_bean.getComDaiko_userId());
        }
        //修正完了
        exCstmt.setStringIn(GS.TORIMODOSHI_HUKA);
        
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }

    //課題No.60
    //追加開始
    /**
     * 査定登録内容更新処理<br>
	 * @param String
	 * @param String
	 * @param String
     * @throws SQLException
     */
    public void setHikiateKakuninSaikenToriKbn(String ankenNo,String saikenKbn,String toriKbn) throws SQLException{

    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC_UPDATE_T1500,sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(ankenNo);
        exCstmt.setStringIn(saikenKbn);
        exCstmt.setStringIn(toriKbn);
        
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }
    //追加完了

    /**
     * T19_留保債務の登録(次フェーズ) <br>
     * 
     * @param toriBean
     * @param map
     * @throws SQLException
     */
    public void setRyuhosaimu(TorihikisakiBean toriBean,HashMap map) throws SQLException {

    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC_INSERT_T1900,sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn((String)map.get(GS.JISHI_PHASE));
        exCstmt.setStringIn((String)map.get(GS.JI_JISHI_PHASE));
        
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }
    
    /**
     * T20_第三者留保債務の登録(次フェーズ) <br>
     * 
     * @param toriBean
     * @param map
     * @throws SQLException
     */
    public void setOthRyuhosaimu(TorihikisakiBean toriBean,HashMap map) throws SQLException {

        //ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC_INSERT_T2000,sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn((String)map.get(GS.JISHI_PHASE));
        exCstmt.setStringIn((String)map.get(GS.JI_JISHI_PHASE));
        
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }
    
    /**
     * システム区分により、金額をフォーマットする。<br>
     * 
     * @param kingaku 金額
     * @param systemKbn システム区分
     * @param tuuka_cd 通貨コード
     * @return フォーマットされた金額
     */
    protected String formatKingaku(double kingaku,String systemKbn,String tuuka_cd) {
        StringBuffer formatKingaku = new StringBuffer();
        if(systemKbn.equals(GS.GSS)){
            //国内
        	formatKingaku.append(Function.format(NUM_FMT_KOKUNAI,kingaku));
        }else{
            //海外
        	formatKingaku.append(Function.format(NUM_FMT_KAIGAI,kingaku));
        }
    	formatKingaku.append(Function.trim(tuuka_cd));
        return formatKingaku.toString();
    }

    /**
     * 差戻・転送フラグにより、進捗を編集する。<br>
     * 
     * @param sasitenFlg 差戻・転送フラグ
     * @param shinchoku 進捗
     * @return 編集された進捗
     */
    protected String sasiHantei(String sasitenFlg,String shinchoku) {
        StringBuffer shinchokuHyoujiyou = new StringBuffer();
        if(SASI_FLG.equals(Function.trim(sasitenFlg))){
        	shinchokuHyoujiyou.append(ASTA)
        					  .append(GS.SPACE_CHARCTER);
        }
        shinchokuHyoujiyou.append(Function.trim(shinchoku));
        return shinchokuHyoujiyou.toString();
    }

    /**
     * 担当者名編集処理<br>
     * 
     * @param tanto 担当者名
     * @param daiko 代行者名
     * @return 編集された担当名
     */
    protected String getTanto_nm(String tanto,String daiko) throws SQLException{
        StringBuffer tanto_nm = new StringBuffer(Function.trim(tanto));
        if(!Function.trim(daiko).equals(GS.EMPTY_CHARCTER)){
            //代行者名日本語 or 代行者名英語が存在する場合
            if(cmnData.getComLangMode().equals(GS.LANG_JA)){
            	tanto_nm.append(DAIKO);
            }else{
            	tanto_nm.append(DAIKO_EN);
            }
            tanto_nm.append(Function.trim(Function.trim(daiko)))
                    .append(GS.KAKKO_MIGI);
        }
        return tanto_nm.toString();
    }
    
    /**
     * システム区分により、金額をフォーマットする。<br>
     * 
     * @param kingaku 金額
     * @param systemKbn システム区分
     * @return フォーマットされた金額
     */
    protected String formatKingaku(String kingaku,String systemKbn) {
        String formatKingaku = null;
        if(kingaku == null){
        	return GS.EMPTY_CHARCTER;
    	}else if (systemKbn.equals(GS.GSS)){
            //国内
        	formatKingaku = Function.format(NUM_FMT_KOKUNAI,Function.getValueOfDouble(kingaku));
        }else{
            //海外
        	formatKingaku = Function.format(NUM_FMT_KAIGAI,Function.getValueOfDouble(kingaku));
        }
        return formatKingaku;
    }
    
    /**
     * システム区分により、金額をフォーマットする。<br>
     * 
     * @param kingaku 金額
     * @param systemKbn システム区分
     * @return フォーマットされた金額
     */
    protected String formatKingakuNotKanma(String kingaku,String systemKbn) {
        String formatKingaku = null;
        if(kingaku == null){
        	return GS.EMPTY_CHARCTER;
    	}else if (systemKbn.equals(GS.GSS)){
            //国内
        	formatKingaku = Function.format(NUM_FMT_KOKUNAI_NOTKANMA,Function.getValueOfDouble(kingaku));
        }else{
            //海外
        	formatKingaku = Function.format(NUM_FMT_KAIGAI_NOTKANMA,Function.getValueOfDouble(kingaku));
        }
        return formatKingaku;
    }
}