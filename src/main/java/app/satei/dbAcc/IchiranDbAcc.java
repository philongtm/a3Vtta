/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2016/03/22		SSC				BJ201602002_部門廃止対応（一次）_本部絞込対応 
******************************************************************************/
package app.satei.dbAcc;

import app.TorihikisakiBean;
import app.satei.form.IchiranForm;
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
 * OC1101_査定_対象先一覧 DBアクセスクラス <br>
 */
public class IchiranDbAcc extends SateiDbAcc {

	private IchiranForm koForm = null;                       //アクションフォーム
	
	//Resultset用文字列    
	private static final String SATEIKI						= "satei_ki";
	private static final String HYOJI_SATEI_KI				= "hyoji_satei_ki";
	private static final String KBN_VAL						= "kbn_val";
	private static final String KBN_HYOUJI_VAL				= "kbn_hyouji_val";
	private static final String ANKEN_NO						= "anken_no";
	private static final String KIKAN_TORI_CD					= "kikan_tori_cd";
	private static final String BUSINESS_NM					= "business_nm";
	private static final String KINGAKU						= "kingaku";
	private static final String TUUKA_CD						= "tuuka_cd";
	private static final String YM							= "ym";
	private static final String HYOJI_YM						= "hyoji_ym";
	private static final String SATEI_KAISHA_CD				= "satei_kaisha_cd";
	private static final String KAISHA_CD						= "kaisha_cd";
	private static final String INIT_BUNRUI2					= "init_bunrui2";
	private static final String BUNRUI2						= "bunrui2";
	private static final String INIT_BU_CD					= "init_bu_cd";
	private static final String BU_CD							= "bu_cd";
	private static final String INIT_BUNRUI3					= "init_bunrui3";
	private static final String INIT_BUNRUI2_NM				= "init_bunrui2_nm";
	private static final String INIT_BU_NM					= "init_bu_nm";
	private static final String INIT_BUNRUI3_NM				= "init_bunrui3_nm";
	private static final String TANTO_USER_NM					= "tanto_user_nm";
	private static final String KTK							= "ktk";
	private static final String SASI_TEN_FLG					= "sasi_ten_flg";
	private static final String SATEI_KI						= "satei_ki";
	private static final String SYSTEM_KBN					= "system_kbn";
	private static final String SATEI_GAMEN					= "satei_gamen";
	private static final String FLGSAKI_MOTOANKEN_NO			= "flgsaki_motoanken_no";
	private static final String MISE_CD						= "mise_cd";
	private static final String KIJUNBI_KBN					= "kijunbi_kbn";
	private static final String SYORI_KAISU					= "syori_kaisu";
	private static final String DAIKO_USER_NM					= "daiko_user_nm";
	private static final String PHASE							= "phase";
	private static final String STATUS						= "status";
	private static final String TOGO_TORI_CD					= "togo_tori_cd";
	private static final String HANKI_SIHANKI_KBN				= "hanki_sihanki_kbn";
	private static final String SIKIBETU_CD					= "sikibetu_cd";
	private static final String SOSHIKI						= "soshiki";
	private static final String SHINCHOKU						= "shinchoku";
	private static final String HOJI_USER_ID					= "hoji_user_id";
	private static final String DAIKO_USER_ID					= "daiko_user_id";
	private static final String JIYU_NM						= "jiyu_nm";
	private static final String JIYU_CD						= "jiyu_cd";
	private static final String ADR							= "adr";
	private static final String WB_COUNTRY_NM					= "wb_country_nm";
	private static final String WB_COUNTRY_CD					= "wb_country_cd";
	private static final String GAIBU_KTK						= "gaibu_ktk";
	private static final String KTK_KIKAN						= "ktk_kikan";
	private static final String OYA_BUSINESS_NM				= "oya_business_nm";
	private static final String FSS							= "fss";
	private static final String DUNS_RATING					= "duns_rating";
	private static final String OYA_KTK						= "oya_ktk";
	private static final String OYA_DUNS_NO					= "oya_duns_no";
	private static final String OYA_ITTAI_DOKURITU			= "oya_ittai_dokuritu";
	private static final String COUNT							= "cnt";

	private static final String SP_SS_O_SELECT_T1400			= "SP_SS_O_SELECT_T1400";		//保持案件取得用プロシージャ
	private static final String SP_SS_O_UPDATE_T1401			= "SP_SS_O_UPDATE_T1401";		//もぎ取り更新用プロシージャ
	private static final String SP_SS_OC1101_SELECT_DIFF		= "SP_SS_OC1101_SELECT_DIFF";	//登録内容判定用プロシージャ
	private static final String SP_SS_O_SELECT_T2600			= "SP_SS_O_SELECT_T2600";		//査定期取得用プロシージャ
    private static final String SP_SS_OC1101_SELECT_ICHIRAN	= "SP_SS_OC1101_SELECT_ICHIRAN";//査定対象先一覧取得プロシージャ
    private static final String SP_SS_O_SELECT_T1401			= "SP_SS_O_SELECT_T1401";       //進捗表取得用プロシージャ
    private static final String SP_SS_O_SELECT_T1402			= "SP_SS_O_SELECT_T1402";		//もぎ取りチェック用プロシージャ

    private static final String DESC_VAL						= "1";				//降順
    private static final String JITANTO         				= "1";				//自担当分
	private static final String HANYO2          				= "2";				//汎用２
    private static final String DESC							= "DESC";			//降順
    private static final String ASC							= "ASC";			//昇順
	private static final String SHOW							= "show";			//区分キー（表示件数）
	private static final String SORT_ITEM       				= "sort_OC1101";	//区分キー（ソート項目）
	private static final String SORT_ORDER      				= "sort_order";		//区分キー（整列方向）
    private static final String SANSHO_PHASE					= "'40','50','60','70','80'";
    /**
	 * コンストラクタ <br>
	 * 
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public IchiranDbAcc(AppContext appcontext) {
		super(appcontext);
		//Bean取得
		form = (IchiranForm)appContext.getActionForm();
	}
	
	/**
	 * 自担当分/汎用２ラジオボタン初期判定処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getInitTanto() throws SQLException {

		koForm = (IchiranForm)form;
		
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_T1400,sqlExec);
		exCstmt.setStringIn(user_bean.getComUserId());
		exCstmt.setStringIn(koForm.getSateiki());
		exCstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
		exCstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
		exCstmt.setStringIn(koForm.getSansyo_phase());
		exCstmt.setIntOut(COUNT);

		//SQL実行	
	    exCstmt.execute();
	    isError(exCstmt);
    	
	    //自担当分/汎用２ラジオボタン初期値設定
    	if(exCstmt.getInt(COUNT) > 0) {
    		koForm.setTanto(JITANTO);
	    }else{
	    	koForm.setTanto(HANYO2);
	    }
	}

	/**
	 * 査定期セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSateiki() throws SQLException {

		koForm = (IchiranForm)form;
		
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_T2600,sqlExec);
		exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
		exCstmt.setStringIn(user_bean.getComSansyoBunrui2());
		exCstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
		exCstmt.setResultSet(RESULTSET);

		try{
			//ResultSet取得
            rs = null;
            //SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
            
			//ActionFormに取得値を格納
	    	int i = 0;
	    	boolean flg = false;
			LinkedHashMap<String,String> ar_sateiki = new LinkedHashMap<String,String>();
			while(rs.next()){
				if(i==0){
					koForm.setSateiki(rs.getString(SATEIKI));
				}
	    		ar_sateiki.put(rs.getString(HYOJI_SATEI_KI), rs.getString(SATEIKI));	    			
	    		flg = true;
	    		i++;
			}
	    	if(!flg){
		    	// 査定期が取得できなかった場合、ブランクをセット
		    	ar_sateiki.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
	    	}
	    	koForm.setAr_sateiki(ar_sateiki);	
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 * 表示件数セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getShow() throws SQLException {

		koForm = (IchiranForm)form;
		ResultSet rs = null;

		try{
            //SQL実行
			rs = super.getKbnval(SHOW,user_bean.getComWorkflowSystemkbn(),cmnData.getComLangMode());
            
			//ActionFormに取得値を格納
			LinkedHashMap<String,String> ar_show = new LinkedHashMap<String,String>();
			while(rs.next()){
				ar_show.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
			}
			koForm.setAr_show(ar_show);
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 * ソート順セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSort() throws SQLException {

		koForm = (IchiranForm)form;
		ResultSet rs = null;
		//ソート項目取得
		try{
			//ResultSet取得
			rs = getKbnval(SORT_ITEM,user_bean.getComWorkflowSystemkbn(),cmnData.getComLangMode());

			//ActionFormに取得値を格納
			int i = 0;
			LinkedHashMap<String,String> ar_sort_item = new LinkedHashMap<String,String>();
			while ( rs.next() ) {
				//初期設定
				if(i==0){
					koForm.setSort_item(rs.getString(KBN_VAL));
				}
				ar_sort_item.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
				i++;
			}
			koForm.setAr_sort_item(ar_sort_item);
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	    
		rs = null;
		//整列方向取得
		try{
			//ResultSet取得
			rs = getKbnval(SORT_ORDER,user_bean.getComWorkflowSystemkbn(),cmnData.getComLangMode());

			//ActionFormに取得値を格納
			LinkedHashMap<String,String> ar_sort_order = new LinkedHashMap<String,String>();
			int i = 0;
			while ( rs.next() ) {
				//初期設定
				if(i==0){
					koForm.setSort_order(this.toSortOrder(rs.getString(KBN_VAL)));
				}
				ar_sort_order.put(rs.getString(KBN_HYOUJI_VAL),this.toSortOrder(rs.getString(KBN_VAL)));
				i++;
			}
			koForm.setAr_sort_order(ar_sort_order);	    	
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
	
	/**
     * 区分値を整列方向文字列に変換<br>
     * 
     * @param toriBean
     * @throws SQLException
     */
	private String toSortOrder(String val){
		if(Function.trim(val).equals(DESC_VAL)){
			return DESC;
		}else{
			return ASC;
		}
	}

	/**
	 * 登録内容の差異フラグ設定 <br>
	 * 
	 * @exception SQLException
	 */
	public void setSaiFlg(TorihikisakiBean toriBean) throws SQLException {

		//ResultSet取得
		ResultSet rs = null;
		try{
            //SQL実行
    		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC1101_SELECT_DIFF,sqlExec);
    		exCstmt.setStringIn(toriBean.getAnken_no());
    		exCstmt.setResultSet(RESULTSET);
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
            
			//取引先Beanに差異フラグを設定
    		int i = 0;
    		boolean flg = false;
			while(rs.next()){
				if(rs.getInt(COUNT) == 0){
					break;
				}else if(rs.getInt(COUNT) == 1 && i != 0){
					flg = true;
					break;
				}
				i++;
			}
			toriBean.setDiffer_flg(flg);
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 *  進捗表取得<br>
	 * @exception SQLException
	 */
	public void getStat() throws SQLException {

		koForm = (IchiranForm)form;
		//ResultSet取得
		ResultSet rs = null;

		try{
            //SQL実行
    		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_T1401,sqlExec);
    		exCstmt.setStringIn(koForm.getSateiki());
    		exCstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
    		exCstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
    		exCstmt.setStringIn(user_bean.getComSansyoBunrui2());
    		exCstmt.setStringIn(SANSHO_PHASE);
    		exCstmt.setResultSet(RESULTSET);
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
            
			koForm.setItijisatei_misyori(0);
			koForm.setItijisatei_syorityu(0);
			koForm.setItijisatei_syoninmati(0);
			koForm.setItijisatei_kanryo(0);
			koForm.setItijisateikensyo_misyori(0);
			koForm.setItijisateikensyo_syorityu(0);
			koForm.setItijisateikensyo_syoninmati(0);
			koForm.setItijisateikensyo_kanryo(0);
			koForm.setNijisatei_misyori(0);
			koForm.setNijisatei_syorityu(0);
			koForm.setNijisatei_syoninmati(0);
			koForm.setNijisatei_kanryo(0);
			koForm.setHanyou3_misyori(0);
			koForm.setHanyou3_syorityu(0);
			koForm.setHanyou3_syoninmati(0);
			koForm.setHanyou3_kanryo(0);

			while(rs.next()){
				if (GS.PHASE_ICHIJI_SATEI.equals(rs.getString(PHASE))) {
					if (GS.STATUS_MISYORI.equals(rs.getString(STATUS))) {
						//一次査定登録未処理件数を設定
						koForm.setItijisatei_misyori(rs.getInt(COUNT));
					} else if (GS.STATUS_SYORICHU.equals(rs.getString(STATUS))) {
						//一次査定登録処理中件数を設定
						koForm.setItijisatei_syorityu(rs.getInt(COUNT));
					} else if (GS.STATUS_SYONIN_MACHI.equals(rs.getString(STATUS))) {
						//一次査定登録承認待ち件数を設定
						koForm.setItijisatei_syoninmati(rs.getInt(COUNT));
					} else if (GS.STATUS_KANRYO.equals(rs.getString(STATUS))) {
						//一次査定登録完了件数を設定
						koForm.setItijisatei_kanryo(rs.getInt(COUNT));
					}
				} else if (GS.PHASE_ICHIJI_SATEI_KENSYO.equals(rs.getString(PHASE))) {
					if (GS.STATUS_MISYORI.equals(rs.getString(STATUS))) {
						//一次査定検証未処理件数を設定
						koForm.setItijisateikensyo_misyori(rs.getInt(COUNT));
					} else if (GS.STATUS_SYORICHU.equals(rs.getString(STATUS))) {
						//一次査定検証処理中件数を設定
						koForm.setItijisateikensyo_syorityu(rs.getInt(COUNT));
					} else if (GS.STATUS_SYONIN_MACHI.equals(rs.getString(STATUS))) {
						//一次査定検証承認待ち件数を設定
						koForm.setItijisateikensyo_syoninmati(rs.getInt(COUNT));
					} else if (GS.STATUS_KANRYO.equals(rs.getString(STATUS))) {
						//一次査定検証完了件数を設定
						koForm.setItijisateikensyo_kanryo(rs.getInt(COUNT));
					}					
				} else if (GS.PHASE_NIJI_SATEI.equals(rs.getString(PHASE))) {
					if (GS.STATUS_MISYORI.equals(rs.getString(STATUS))) {
						//二次査定検証未処理件数を設定
						koForm.setNijisatei_misyori(rs.getInt(COUNT));
					} else if (GS.STATUS_SYORICHU.equals(rs.getString(STATUS))) {
						//二次査定検証処理中件数を設定
						koForm.setNijisatei_syorityu(rs.getInt(COUNT));
					} else if (GS.STATUS_SYONIN_MACHI.equals(rs.getString(STATUS))) {
						//二次査定検証承認待ち件数を設定
						koForm.setNijisatei_syoninmati(rs.getInt(COUNT));
					} else if (GS.STATUS_KANRYO.equals(rs.getString(STATUS))) {
						//二次査定検証完了件数を設定
						koForm.setNijisatei_kanryo(rs.getInt(COUNT));
					}
				} else if (GS.PHASE_HIKIATEKIN_KENSYO.equals(rs.getString(PHASE)) && user_bean.getComWorkflowSystemkbn().equals(GS.GSS)) {
					if (GS.STATUS_MISYORI.equals(rs.getString(STATUS))) {
						//引当金検証未処理件数を設定
						koForm.setHanyou3_misyori(rs.getInt(COUNT));
					} else if (GS.STATUS_SYORICHU.equals(rs.getString(STATUS))) {
						//引当金検証処理中件数を設定
						koForm.setHanyou3_syorityu(rs.getInt(COUNT));
					} else if (GS.STATUS_SYONIN_MACHI.equals(rs.getString(STATUS))) {
						//引当金検証承認待ち件数を設定
						koForm.setHanyou3_syoninmati(rs.getInt(COUNT));
					} else if (GS.STATUS_KANRYO.equals(rs.getString(STATUS))) {
						//引当金検証完了件数を設定
						koForm.setHanyou3_kanryo(rs.getInt(COUNT));
					}
				} else if (GS.PHASE_HIKIATEKIN_KAKUNIN.equals(rs.getString(PHASE)) && !(user_bean.getComWorkflowSystemkbn().equals(GS.GSS))) {
					if (GS.STATUS_MISYORI.equals(rs.getString(STATUS))) {
						//引当金確認未処理件数を設定
						koForm.setHanyou3_misyori(rs.getInt(COUNT));
					} else if (GS.STATUS_SYORICHU.equals(rs.getString(STATUS))) {
						//引当金確認処理中件数を設定
						koForm.setHanyou3_syorityu(rs.getInt(COUNT));
					} else if (GS.STATUS_SYONIN_MACHI.equals(rs.getString(STATUS))) {
						//引当金確認承認待ち件数を設定
						koForm.setHanyou3_syoninmati(rs.getInt(COUNT));
					} else if (GS.STATUS_KANRYO.equals(rs.getString(STATUS))) {
						//引当金確認完了件数を設定
						koForm.setHanyou3_kanryo(rs.getInt(COUNT));
					}
				}
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 *  もぎ取られていないか判定<br>
	 * 
	 * @exception SQLException
	 */
	public boolean isMogitoriCheck(TorihikisakiBean toriBean) throws SQLException {

		//ResultSet取得
		//ResultSet取得
		ResultSet rs = null;

        try{
            //SQL実行
    		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_T1402,sqlExec);
    		exCstmt.setStringIn(toriBean.getAnken_no());
    		exCstmt.setResultSet(RESULTSET);
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
    		rs = exCstmt.getResultSet(RESULTSET);
            
    		boolean flg = false;
			while(rs.next()){
				if(!(user_bean.getComUserId().equals(Function.trim(rs.getString(HOJI_USER_ID)))) && !(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(HOJI_USER_ID))))){
					break;
				}else if(!(toriBean.getPhase().equals(Function.trim(rs.getString(PHASE))))){
					break;
				}else if(!(toriBean.getStatus().equals(Function.trim(rs.getString(STATUS))))){
					break;
				}
				flg = true;
			}
			return flg;
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
	
    /**
     * T14_査定進捗管理の更新<br>
     * 
     * @param toriBean
     * @param map
     * @throws SQLException
     */
    public void setSateiStat(TorihikisakiBean toriBean) throws SQLException{

    	//ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1401,sqlExec);
        exCstmt.setStringIn(toriBean.getAnken_no());
        exCstmt.setStringIn(Function.trim(user_bean.getComDaiko_userId()));
        exCstmt.setStringIn(Function.trim(user_bean.getComUserId()));
        exCstmt.setStringIn(toriBean.getBunrui2());
        exCstmt.setStringIn(Function.trim(user_bean.getComSyozokuBunrui2()));
        exCstmt.setStringIn(toriBean.getBu_cd());
        exCstmt.setStringIn(Function.trim(user_bean.getComSyozokuBuCd()));
        
        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }
    
    /**
     * 一覧情報取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getMeisai() throws SQLException {

		koForm = (IchiranForm)form;
		//ResultSet取得
		ResultSet rs = null;
        //ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC1101_SELECT_ICHIRAN,sqlExec);
        exCstmt.setStringIn(cmnData.getComLangMode());
        exCstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
        exCstmt.setStringIn(koForm.getSansyo_phase());
        exCstmt.setStringIn(user_bean.getComSansyoBunrui2());
        exCstmt.setStringIn(user_bean.getComSansyoHonbuCd());
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
		exCstmt.setStringIn(koForm.getSateiki());
		exCstmt.setStringIn(koForm.getTanto());
		exCstmt.setStringIn(Function.trim(user_bean.getComNiji_satei_kbn()));
		exCstmt.setStringIn(koForm.getSort_item());
		exCstmt.setStringIn(koForm.getSort_order());
        exCstmt.setResultSet(RESULTSET);

        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
            //ActionFormに取得値を格納
            List<TorihikisakiBean> list = new ArrayList<TorihikisakiBean>();   
            int i = 0;
            boolean TRUE = true;
            while(rs.next()){

                TorihikisakiBean listBean = new TorihikisakiBean();
                //リンク表示フラグ
                listBean.setLink_flg(TRUE);
                //id
                listBean.setId(Function.getStringOfInt(i));
                //査定案件No.
                listBean.setAnken_no(Function.trim(rs.getString(ANKEN_NO)));
                //取引先コード
                listBean.setKanjo_cd(Function.trim(rs.getString(KIKAN_TORI_CD)));
                //査定会社コード
                listBean.setSateikaisya_cd(Function.trim(rs.getString(SATEI_KAISHA_CD)));
                //店コード
                listBean.setMise_cd(Function.trim(rs.getString(MISE_CD)));
                //初期分類２
                listBean.setInit_bunrui2(Function.trim(rs.getString(INIT_BUNRUI2)));
                //分類２
                listBean.setBunrui2(Function.trim(rs.getString(BUNRUI2)));
                //初期分類３
                listBean.setInit_bunrui3(Function.trim(rs.getString(INIT_BUNRUI3)));
                //初期部コード
                listBean.setInit_bu_cd(Function.trim(rs.getString(INIT_BU_CD)));
                //部コード
                listBean.setBu_cd(Function.trim(rs.getString(BU_CD)));
                //フェーズ
                listBean.setPhase(Function.trim(rs.getString(PHASE)));
                //ステータス
                listBean.setStatus(Function.trim(rs.getString(STATUS)));
                //査定期
                listBean.setSatei_ki(Function.trim(rs.getString(SATEI_KI)));
                //対象年月
                listBean.setTaisyo_ym(Function.trim(rs.getString(YM)));
                //差戻・転送フラグ
                listBean.setSasi_ten_flg(Function.trim(rs.getString(SASI_TEN_FLG)));
                //案件保持ユーザ
                listBean.setHoji_user_id(Function.trim(rs.getString(HOJI_USER_ID)));
                //システム区分
                listBean.setSystem_kbn(Function.trim(rs.getString(SYSTEM_KBN)));
                //査定登録画面
                listBean.setSatei_toroku_gamen(Function.trim(rs.getString(SATEI_GAMEN)));
                //フラグ先抽出元案件No.
                listBean.setFlg_saki_anken_no(Function.trim(rs.getString(FLGSAKI_MOTOANKEN_NO)));
                //基準日区分
                listBean.setKijunbi_kbn(Function.trim(rs.getString(KIJUNBI_KBN)));
                //処理回数
                listBean.setSyori_kaisu(Function.trim(rs.getString(SYORI_KAISU)));
                //半期・四半期区分
                listBean.setHanki_sihanki_kbn(Function.trim(rs.getString(HANKI_SIHANKI_KBN)));
                //会社コード
                listBean.setKaisya_cd(Function.trim(rs.getString(KAISHA_CD)));
                //代行ユーザID
                listBean.setDaiko_user_id(Function.trim(rs.getString(DAIKO_USER_ID)));
                //対象年月表示用
                listBean.setTaisyo_ym_hyoji(Function.trim(rs.getString(HYOJI_YM)));
                //統合取引先コード
                listBean.setTogo_tori_cd(Function.trim(rs.getString(TOGO_TORI_CD)));
                //識別コード
                listBean.setShikibetu_cd(Function.trim(rs.getString(SIKIBETU_CD)));
                //取引先名称
                listBean.setKanjo_nm(Function.trim(rs.getString(BUSINESS_NM)));
                //抽出事由名称
                listBean.setJiyu_nm(Function.trim(rs.getString(JIYU_NM)));
                //抽出事由コード
                listBean.setJiyu_cd(Function.trim(rs.getString(JIYU_CD)));
                //所在地
                listBean.setSyozaichi(Function.trim(rs.getString(ADR)));
                //所在国
                listBean.setSyozaikoku(Function.trim(rs.getString(WB_COUNTRY_NM)));
                //所在国コード
                listBean.setSyozaikoku_cd(Function.trim(rs.getString(WB_COUNTRY_CD)));
                listBean.setWb_country_cd(Function.trim(rs.getString(WB_COUNTRY_CD)));
                //外部格付
                listBean.setGaibu_ktk(Function.trim(rs.getString(GAIBU_KTK)));
                //格付機関
                listBean.setKtk_kikan(Function.trim(rs.getString(KTK_KIKAN)));
                //親取引先名称
                listBean.setOya_business_nm(Function.trim(rs.getString(OYA_BUSINESS_NM)));
                //FSS
                listBean.setFss(Function.trim(rs.getString(FSS)));
                //DunsRating
                listBean.setDuns_rating(Function.trim(rs.getString(DUNS_RATING)));
                //信用格付情報
                listBean.setSinyoktk(Function.trim(rs.getString(KTK)));
                //親格付
                listBean.setOya_ktk(Function.trim(rs.getString(OYA_KTK)));
                //親DunsNo.
                listBean.setOya_duns_no(Function.trim(rs.getString(OYA_DUNS_NO)));
                //親一体独立
                listBean.setOya_ittai_dokuritu(Function.trim(rs.getString(OYA_ITTAI_DOKURITU)));
                //担当者
                listBean.setTanto_nm(getTanto_nm(rs.getString(TANTO_USER_NM),rs.getString(DAIKO_USER_NM)));
                // 初期分類２名称
                listBean.setInit_bunrui2_nm(Function.trim(rs.getString(INIT_BUNRUI2_NM)));
                // 初期部名称
                listBean.setInit_bu_nm(Function.trim(rs.getString(INIT_BU_NM)));
                // 初期分類３名称
                listBean.setInit_bunrui3_nm(Function.trim(rs.getString(INIT_BUNRUI3_NM)));
                //組織
                listBean.setSoshiki(Function.trim(rs.getString(SOSHIKI)));
                //金額
				listBean.setKingaku(formatKingaku(rs.getDouble(KINGAKU),user_bean.getComWorkflowSystemkbn(),rs.getString(TUUKA_CD)));
                //進捗
				listBean.setSintyoku(sasiHantei(rs.getString(SASI_TEN_FLG),rs.getString(SHINCHOKU)));
                //もぎ取りチェックボックス
                listBean.setMogitori_chk(GS.EMPTY_CHARCTER);
				// リンク表示フラグ制御
				boolean link_flg = false;
				String hoji_user_id = Function.trim(rs.getString(HOJI_USER_ID));
				if (GS.EMPTY_CHARCTER.equals(hoji_user_id)) {
					link_flg = true;
				} else if (hoji_user_id.equals(user_bean.getComUserId())) {
					link_flg = true;
				}
				if (rs.getString(STATUS).equals(GS.STATUS_SYONIN_MACHI)) {
					link_flg = false;
				}
				listBean.setLink_flg(link_flg);
                //取得情報を格納
                list.add(i,listBean);
                i++;
            }
            //ActionForm に明細を格納
            koForm.setAr_meisai(list);
            //ページ設定
            koForm.setPager(list);
        } finally {
            if(rs != null){
            	rs.close();
            }
        }
    }
}