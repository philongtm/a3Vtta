/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2016/03/09		SSC				BJ201602002_部門廃止対応（一次）_本部絞込対応
******************************************************************************/
package app.tairyu.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.tairyu.form.IchiranForm;
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
import java.util.LinkedHashMap;
import java.util.List;

/**
* OB1101_実質滞留債権判定_対象先一覧 DBアクセスクラス <br>
*/
public class IchiranDbAcc extends CommonDbAcc {
	
	private SessionData cmnData = null;				// 機能共通セッション
	private UserBean user_bean = null;					// ユーザ情報
	private IchiranForm form = null;					// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列	
	private static final String COUNT					= "cnt";
	private static final String KBN_HYOUJI_VAL		= "kbn_hyouji_val";
	private static final String KBN_VAL				= "kbn_val";
	private static final String SATEIKI				= "satei_ki";
	private static final String HYOJI_SATEI_KI		= "hyoji_satei_ki";
	private static final String CNT					= "cnt";
	private static final String PHASE					= "phase";
	private static final String STATUS				= "status";
	private static final String ANKEN_NO				= "anken_no";
	private static final String KIKAN_TORI_CD			= "kikan_tori_cd";
	private static final String BUSINESS_NM			= "business_nm";
	private static final String WB_COUNTRY_NM			= "wb_country_nm";
	private static final String KINGAKU				= "kingaku";
	private static final String TUUKA_CD				= "tuuka_cd";
	private static final String YM					= "ym";
	private static final String SATEI_KAISHA_CD		= "satei_kaisha_cd";
	private static final String INIT_BUNRUI2			= "init_bunrui2";
	private static final String INIT_BU_CD			= "init_bu_cd";
	private static final String BU_CD					= "bu_cd";
	private static final String INIT_BUNRUI3			= "init_bunrui3";
	private static final String INIT_BUNRUI2_NM		= "init_bunrui2_nm";
	private static final String INIT_BU_NM			= "init_bu_nm";
	private static final String HOJI_USER_ID			= "hoji_user_id";
	private static final String USER_NM				= "user_nm";
	private static final String SINTYOKU				= "sintyoku";
	private static final String KTK					= "ktk";
	private static final String SASI_TEN_FLG			= "sasi_ten_flg";
	private static final String SATEI_KI				= "satei_ki";
	private static final String SYSTEM_KBN			= "system_kbn";
	private static final String MISE_CD				= "mise_cd";
	private static final String KIJUNBI_KBN			= "kijunbi_kbn";
	private static final String SYORI_KAISU			= "syori_kaisu";
	private static final String DAIKO_NM				= "daiko_nm";
	private static final String TOGO_TORI_CD			= "togo_tori_cd";		// 統合取引先コード
	private static final String SIKIBETU_CD			= "sikibetu_cd";		// 識別コード
	private static final String HYOJI_YM				= "hyoji_ym";			// 対象年月(画面表示用)
	private static final String INIT_BUNRUI3_NM		= "init_bunrui3_nm";	// 初期分類３名称
	private static final String SOSHIKI				= "soshiki"; 			// 組織
	private static final String HANKI_SIHANKI_KBN		= "hanki_sihanki_kbn";	// 半期・四半期区分
	private static final String BUNRUI2 				= "bunrui2";			// 分類２
	private static final String KAISHA7_CD 		 	= "kaisha7_cd";			// 会社コード
	
	private static final String SASI_FLG			= "1";
	private static final String ASTA				= "*";
	private static final String JITANTO         	= "1";				//自担当分
	private static final String HANYO2          	= "2";				//汎用２
	private static final String SORT_ITEM       	= "sort_OB1101";	//区分キー（ソート項目）
	private static final String SORT_ORDER      	= "sort_order";		//区分キー（整列方向）
	private static final String SHOW            	= "show";			//区分キー（表示件数）
	private static final String TAIRYUHANTEI    	= "1";				//滞留判定対象
	private static final String MOGITORI        	= "30";				//入力区分(30:もぎ取り)
	private static final String DAIKO           	= "( 代行 ";
	private static final String DAIKO_EN        	= "( proxy ";
	private static final String TORIMODOSHI_FUKA	= "1";				//'1'：取戻不可
    private static final String NUM_FMT_KOKUNAI 	= "##,###,###,###,###,##0.##";      // 数字のフォーマット：国内
    private static final String NUM_FMT_KAIGAI  	= "##,###,###,###,###,##0.00";      // 数字のフォーマット：海外
	
	private static final String SP_SS_O_SELECT_T2600			= "SP_SS_O_SELECT_T2600";			//査定期取得用プロシージャ
	private static final String SP_SS_OB1101_SELECT_T0800		= "SP_SS_OB1101_SELECT_T0800";		//自担当分/汎用２ラジオボタン初期判定用プロシージャ
	private static final String SP_SS_OB1101_SELECT_T0801		= "SP_SS_OB1101_SELECT_T0801";		//進捗件数取得プロシージャ
	private static final String SP_SS_OB1101_SELECT_ICHIRAN	= "SP_SS_OB1101_SELECT_ICHIRAN";	//滞留判定一覧情報取得プロシージャ
	private static final String SP_SS_OB1101_SELECT_T0802		= "SP_SS_OB1101_SELECT_T0802";		//もぎ取りチェック処理プロシージャ
	private static final String SP_SS_OB1101_UPDATE_T0800		= "SP_SS_OB1101_UPDATE_T0800";		//T08_滞留判定進捗管理（SST_TAIRYU_STAT）を更新するプロシージャ
	private static final String SP_SS_O_INSERT_T1300			= "SP_SS_O_INSERT_T1300";			//T13_入力履歴（SST_NYURYOKU_HIST）の登録をするプロシージャ
	
	// INパラメータ
	private String userId;				// ユーザＩＤ
	private String sateikaisya_cd;		// 業務フローパターン査定会社コード
	private String sansyoBunrui2;		// 参照分類２コード
	private String workflowSystemkbn;	// 業務フローパターンシステム区分
	private String langMode;			// 共)言語モード
	private String sansyoHonbuCd;		// 参照本部コード

	/**
	 * コンストラクタ <br>
	 * 
	 * @param sqlExec SqlExecuter
	 * @param log Log
	 * @param appcontext AppContext
	 */
	public IchiranDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;

		//ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		form = (IchiranForm)appContext.getActionForm();

		//ビーンの値を変数に設定
		userId = user_bean.getComUserId();
		sateikaisya_cd = user_bean.getComWorkflowSateikaisya_cd();
		sansyoBunrui2 = user_bean.getComSansyoBunrui2();
		workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
		langMode = cmnData.getComLangMode();
		sansyoHonbuCd = user_bean.getComSansyoHonbuCd();
	}
	
	/**
	 * 変数初期化 <br>
	 */
	public void initialize() {
	    // INパラメータ
	    userId = GS.EMPTY_CHARCTER;
	    sateikaisya_cd = GS.EMPTY_CHARCTER;
	    sansyoBunrui2 = GS.EMPTY_CHARCTER;
	    workflowSystemkbn = GS.EMPTY_CHARCTER;
	    langMode = GS.EMPTY_CHARCTER;
	    sansyoHonbuCd = GS.EMPTY_CHARCTER;
	}

	/**
	 * ソート順セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSort() throws SQLException {

		ResultSet rs = null;

		//ソート項目取得
		try{
			//ResultSet取得
			rs = getKbnval(SORT_ITEM,workflowSystemkbn,cmnData.getComLangMode());

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_sort_item = new LinkedHashMap<String,String>();
			int i = 0;
			while ( rs.next() ) {
				//初期設定
				if(i==0){
					form.setSort_item(rs.getString(KBN_VAL));
				}
				ar_sort_item.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
				i++;
			}
			form.setAr_sort_item(ar_sort_item);
		} finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	    
		rs = null;
		
		//整列方向取得
		try{
			//ResultSet取得
			rs = getKbnval(SORT_ORDER,workflowSystemkbn,cmnData.getComLangMode());

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_sort_order = new LinkedHashMap<String,String>();
			int i = 0;
			while ( rs.next() ) {
				//初期設定
				if(i==0){
					form.setSort_order(rs.getString(KBN_VAL));
				}
				ar_sort_order.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
				i++;
			}
			form.setAr_sort_order(ar_sort_order);	    	
		} finally {
	    	if (rs != null) {
    			//Resultset close
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

		ResultSet rs = null;

		try{
			//ResultSet取得
			rs = getKbnval(SHOW,workflowSystemkbn,cmnData.getComLangMode());

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_show = new LinkedHashMap<String,String>();
			int i = 0;
			while ( rs.next() ) {
				ar_show.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
				i++;
			}
			form.setAr_show(ar_show);	    	
		} finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	}	
	
	/**
	 * 査定期取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSateiki() throws SQLException {

		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_T2600, sqlExec);
		exCstmt.setStringIn(langMode);
		exCstmt.setStringIn(sateikaisya_cd);
		exCstmt.setStringIn(sansyoBunrui2);
	    exCstmt.setStringIn(workflowSystemkbn);
		exCstmt.setResultSet(RESULTSET);
	    
	    try {
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
	    	
	    	// ActionForm に取得値を格納
	    	LinkedHashMap<String,String> ar_sateiki = new LinkedHashMap<String,String>();
	    	int i = 0;
	    	boolean flg = false;
	    	while ( rs.next() ) {
				//初期設定
				if(i==0){
					form.setSateiki(rs.getString(SATEIKI));
				}
	    		ar_sateiki.put(rs.getString(HYOJI_SATEI_KI), rs.getString(SATEIKI));	    			
	    		flg = true;
	    		i++;
	    	}
	    	if(!flg){
		    	// 査定期が取得できなかった場合、ブランクをセット
		    	ar_sateiki.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
	    	}
	    	form.setAr_sateiki(ar_sateiki);	
	    } finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	}	

	/**
	 * 自担当分/汎用２ラジオボタン初期判定処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getInitTanto() throws SQLException {
		
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB1101_SELECT_T0800, sqlExec);
		exCstmt.setStringIn(userId);
		exCstmt.setStringIn(form.getSateiki());
		exCstmt.setStringIn(workflowSystemkbn);
		exCstmt.setStringIn(form.getSansyo_phase());
		exCstmt.setIntOut(COUNT);
	    
	    try {
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
    	
	    	//自担当分/汎用２ラジオボタン初期値設定
    		if(exCstmt.getInt(COUNT) > 0) {
	    		form.setTanto(JITANTO);
	    	}else{
	    		form.setTanto(HANYO2);
	    	}
	    } finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
	    	}
	    }
	}

	/**
	 * 各進捗件数取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSintyoku() throws SQLException {
		
		//各進捗件数を初期化する
		form.setHantei_misyori(0);
		form.setHantei_syorityu(0);		
		form.setHantei_syoninmati(0);
		form.setHantei_kanryo(0);
		form.setKensyo_misyori(0);
		form.setKensyo_syorityu(0);
		form.setKensyo_syoninmati(0);
		form.setKensyo_kanryo(0);
	    
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB1101_SELECT_T0801, sqlExec);
		exCstmt.setStringIn(form.getSateiki());
	    exCstmt.setStringIn(workflowSystemkbn);
	    exCstmt.setStringIn(sateikaisya_cd);
	    exCstmt.setStringIn(sansyoBunrui2);
		exCstmt.setResultSet(RESULTSET);
	    
	    try {
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);	    
	    	
	    	// ActionForm に取得値を格納
			while(rs.next()) {
				if (GS.PHASE_TAIRYU_HANTEI.equals(rs.getString(PHASE))) {
					if (GS.STATUS_MISYORI.equals(rs.getString(STATUS))) {
						//判定登録未処理件数を設定
						form.setHantei_misyori(rs.getInt(CNT));
					} else if (GS.STATUS_SYORICHU.equals(rs.getString(STATUS))) {
						//判定登録処理中件数を設定
						form.setHantei_syorityu(rs.getInt(CNT));
					} else if (GS.STATUS_SYONIN_MACHI.equals(rs.getString(STATUS))) {
						//判定登録承認待ち件数を設定
						form.setHantei_syoninmati(rs.getInt(CNT));
					} else if (GS.STATUS_KANRYO.equals(rs.getString(STATUS))) {
						//判定登録完了件数を設定
						form.setHantei_kanryo(rs.getInt(CNT));
					}	
				} else if (GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(rs.getString(PHASE))) {
					if (GS.STATUS_MISYORI.equals(rs.getString(STATUS))) {
						//判定検証未処理件数を設定
						form.setKensyo_misyori(rs.getInt(CNT));
					} else if (GS.STATUS_SYORICHU.equals(rs.getString(STATUS))) {
						//判定検証処理中件数を設定
						form.setKensyo_syorityu(rs.getInt(CNT));
					} else if (GS.STATUS_SYONIN_MACHI.equals(rs.getString(STATUS))) {
						//判定検証承認待ち件数を設定
						form.setKensyo_syoninmati(rs.getInt(CNT));
					} else if (GS.STATUS_KANRYO.equals(rs.getString(STATUS))) {
						//判定検証完了件数を設定
						form.setKensyo_kanryo(rs.getInt(CNT));
					}					
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

	/**
	 * 一覧情報取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getMeisai() throws SQLException {

		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB1101_SELECT_ICHIRAN, sqlExec);
		exCstmt.setStringIn(sateikaisya_cd);
		exCstmt.setStringIn(sansyoBunrui2);
		exCstmt.setStringIn(sansyoHonbuCd);
		exCstmt.setStringIn(form.getSansyo_phase());
		exCstmt.setStringIn(form.getSateiki());
		exCstmt.setStringIn(userId);
		exCstmt.setStringIn(form.getTanto());
		exCstmt.setStringIn(form.getSort_item());
		exCstmt.setStringIn(form.getSort_order());
		exCstmt.setStringIn(workflowSystemkbn);
		exCstmt.setStringIn(cmnData.getComLangMode());			
		exCstmt.setResultSet(RESULTSET);
	    
	    try {
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);	 
	    	
	    	// ActionForm に取得値を格納
	    	List<TorihikisakiBean> ar_meisai = new ArrayList<TorihikisakiBean>();	// 明細配列
			InputCheck check = new InputCheck();
	    	int i = 0;
	    	while ( rs.next() ) {
				TorihikisakiBean listBean = new TorihikisakiBean();
				
				//id
				listBean.setId(Function.getStringOfInt(i));
				
				//滞留判定案件Ｎｏ．
				listBean.setAnken_no(rs.getString(ANKEN_NO));
				
				// 勘定先CD
				listBean.setKanjo_cd(rs.getString(KIKAN_TORI_CD));
				
				// 勘定先名称
				listBean.setKanjo_nm(rs.getString(BUSINESS_NM));
				
				// 所在国
				listBean.setSyozaikoku(rs.getString(WB_COUNTRY_NM));

				// 金額計
				listBean.setKingaku(formatKingaku(rs.getDouble(KINGAKU), workflowSystemkbn) + rs.getString(TUUKA_CD));
				
				// 対象年月
				listBean.setTaisyo_ym(rs.getString(YM));

				// 対象年月(画面表示用)
				listBean.setTaisyo_ym_hyoji(rs.getString(HYOJI_YM));
				
				// 査定会社コード
				listBean.setSateikaisya_cd(rs.getString(SATEI_KAISHA_CD));
				
				// 分類２
				listBean.setBunrui2(rs.getString(BUNRUI2));
				
				// 初期分類２
				listBean.setInit_bunrui2(rs.getString(INIT_BUNRUI2));
				
				// 初期部コード
				listBean.setInit_bu_cd(rs.getString(INIT_BU_CD));
				
				// 部コード
				listBean.setBu_cd(rs.getString(BU_CD));
				
				// 初期分類３
				listBean.setInit_bunrui3(rs.getString(INIT_BUNRUI3));
				
				// 初期分類２名称
				listBean.setInit_bunrui2_nm(rs.getString(INIT_BUNRUI2_NM));
				
				// 初期部名称
				listBean.setInit_bu_nm(rs.getString(INIT_BU_NM));

				// 初期分類３名称
				listBean.setInit_bunrui3_nm(rs.getString(INIT_BUNRUI3_NM));

				// 組織
				listBean.setSoshiki(rs.getString(SOSHIKI));
				
				// 案件保持ユーザID
				listBean.setHoji_user_id(rs.getString(HOJI_USER_ID));
				
				// 担当者
				StringBuffer daiko_nm = new StringBuffer(GS.EMPTY_CHARCTER);
				if(!check.isNullBlank(rs.getString(DAIKO_NM))){
					//代行者名が存在する場合
					if(cmnData.getComLangMode().equals(GS.LANG_JA)){
						daiko_nm.append(DAIKO);
					} else {
						daiko_nm.append(DAIKO_EN);
					}

					daiko_nm.append(rs.getString(DAIKO_NM));
					daiko_nm.append(GS.SPACE_CHARCTER).append(GS.KAKKO_MIGI);					
				}
				if(!check.isNullBlank(rs.getString(USER_NM))){
					listBean.setTanto_nm(rs.getString(USER_NM) + daiko_nm);
				}
				
				// 進捗
				listBean.setSintyoku(sasiHantei(rs.getString(SASI_TEN_FLG),rs.getString(SINTYOKU)));
				
				// 信用格付情報
				listBean.setSinyoktk(rs.getString(KTK));
				
				// フェーズ
				listBean.setPhase(rs.getString(PHASE));
				
				// ステータス
				listBean.setStatus(rs.getString(STATUS));
				
				// 査定期
				listBean.setSatei_ki(rs.getString(SATEI_KI));
				
				// システム区分
				listBean.setSystem_kbn(rs.getString(SYSTEM_KBN));
				
				// 店コード
				listBean.setMise_cd(rs.getString(MISE_CD));
				
				// 基準日区分
				listBean.setKijunbi_kbn(rs.getString(KIJUNBI_KBN));
				
				// 処理回数
				listBean.setSyori_kaisu(rs.getString(SYORI_KAISU));

				// 半期・四半期区分
				listBean.setHanki_sihanki_kbn(rs.getString(HANKI_SIHANKI_KBN));

				// 統合取引先コード
				listBean.setTogo_tori_cd(rs.getString(TOGO_TORI_CD));
				
				// 識別コード
				listBean.setShikibetu_cd(rs.getString(SIKIBETU_CD));
				
				// 差戻・転送フラグ
				listBean.setSasi_ten_flg(rs.getString(SASI_TEN_FLG));

                //会社コード
                listBean.setKaisya_cd(rs.getString(KAISHA7_CD));

                // リンク表示フラグ制御
				boolean link_flg = false;
				// 案件保持ユーザID
				String hoji_user_id = rs.getString(HOJI_USER_ID);
				if (check.isNullBlank(hoji_user_id)) {
					link_flg = true;
				} else if (hoji_user_id.equals(userId)) {
					link_flg = true;
				}
				if (rs.getString(STATUS).equals(GS.STATUS_SYONIN_MACHI)) {
					link_flg = false;
				}
				listBean.setLink_flg(link_flg);

	    		// 明細配列に取得情報を格納
	    		ar_meisai.add(i, listBean);
	    		i++;   
	    	}
		    	
		    // ActionForm に明細を格納
		    form.setAr_meisai(ar_meisai);    
		    // ページ設定
		    form.setPager(ar_meisai);
		
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
	 * もぎ取りチェック処理 <br>
	 * 
	 * @return boolean(true:もぎ取り可、false:もぎ取り不可)
	 * @exception SQLException
	 */
	public boolean checkMogitori() throws SQLException {
		
	    // 処理結果フラグ
	    boolean result = false;
	    // チェック用パラメータ取得
	    TorihikisakiBean listBean = (TorihikisakiBean)form.getAr_meisai().get(form.getId());
	    String anken_no = form.getAnken_no();			// 滞留判定案件No.
	    String hoji_user = listBean.getHoji_user_id();	// 案件保持ユーザ
	    String phase = listBean.getPhase();				// フェーズ(一覧表示時)
	    String status = listBean.getStatus();			// ステータス(一覧表示時)
	    String mogitori_phase = null;					// フェーズ(もぎ取りチェック時)
	    String mogitori_status = null;					// ステータス(もぎ取りチェック時)

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB1101_SELECT_T0802, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringOut(HOJI_USER_ID);		
		exCstmt.setStringOut(PHASE);
		exCstmt.setStringOut(STATUS);

	    try{
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
	    	hoji_user = exCstmt.getString(HOJI_USER_ID);
       		mogitori_phase = Function.trim(exCstmt.getString(PHASE));
       		mogitori_status = Function.trim(exCstmt.getString(STATUS));
        	if((hoji_user == null || hoji_user.equals(userId)) && mogitori_phase.equals(phase) && mogitori_status.equals(status)) {
    			result = true;
        	}
        } finally {
        	if (rs != null) {
       			//Resultset close
       			rs.close();
        	}
        }
		return result;
	}		
	
	/**
	 * もぎ取り処理 T08_滞留判定進捗管理（SST_TAIRYU_STAT）の更新 <br>
	 * 
	 * @exception SQLException
	 */
	public void doMogitoriUpdate() throws SQLException {

		InputCheck check = new InputCheck();
	    // INパラメータ取得
	    TorihikisakiBean listBean = (TorihikisakiBean)form.getAr_meisai().get(form.getId());
	    String anken_no = form.getAnken_no();					// 滞留判定案件No.
	    String status = GS.STATUS_SYORICHU;						// ステータス(T08_更新用 20:処理中)
   		String tm_fuka_flg = TORIMODOSHI_FUKA;					// 取戻不可フラグ
   		String bu_cd = null;									// 部コード
   		if (check.isNullBlank(listBean.getBu_cd())
				&& listBean.getBunrui2().equals(user_bean.getComSyozokuBunrui2())) {
   			bu_cd = user_bean.getComSyozokuBuCd();
   		} else {
   			bu_cd = listBean.getBu_cd();
   		}
   		String daiko_userId = user_bean.getComDaiko_userId();	// 代行ユーザID
   		
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB1101_UPDATE_T0800, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(userId);
		exCstmt.setStringIn(status);
		exCstmt.setStringIn(tm_fuka_flg);
		exCstmt.setStringIn(bu_cd);
		exCstmt.setStringIn(daiko_userId);
		
		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				//Resultset close
				rs.close();
			}	
		}
	}
	
	/**
	 * もぎ取り処理 T13_入力履歴（SST_NYURYOKU_HIST）の登録 <br>
	 * 
	 * @exception SQLException
	 */
	public void doMogitoriInsert() throws SQLException {

	    // INパラメータ取得
	    TorihikisakiBean listBean = (TorihikisakiBean)form.getAr_meisai().get(form.getId());
	    String anken_no = form.getAnken_no();			// 滞留判定案件No.
	    String hoji_user = listBean.getHoji_user_id();	// 案件保持ユーザ

	    //ログインユーザが既にもぎ取り済みの場合は更新・登録は行わない
		if(userId.equals(hoji_user)) {
			return;
		}
		InputCheck check = new InputCheck();
   		
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(TAIRYUHANTEI);
		exCstmt.setStringIn(listBean.getSateikaisya_cd());
		exCstmt.setStringIn(userId);
		exCstmt.setStringIn(user_bean.getComUser_Nm());
		exCstmt.setStringIn(user_bean.getComUser_Nm_En());
		exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm());
		exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm_En());
		exCstmt.setStringIn(listBean.getPhase());
		exCstmt.setStringIn(MOGITORI);
		exCstmt.setStringIn(userId);
		if (check.isNullBlank(user_bean.getComDaiko_userId())) {
			exCstmt.setStringIn(null);
			exCstmt.setStringIn(null);
			exCstmt.setStringIn(null);
		} else {
			exCstmt.setStringIn(user_bean.getComDaiko_userId());
			exCstmt.setStringIn(user_bean.getComDaiko_user_nm());
			exCstmt.setStringIn(user_bean.getComDaiko_user_nm_en());
		}
		exCstmt.setStringIn(null);
		exCstmt.setStringIn(null);
		exCstmt.setStringIn(null);
		exCstmt.setStringIn(null);
		exCstmt.setStringIn(null);
		
		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				//Resultset close
				rs.close();
			}	
		}
	}
    
    /**
     * システム区分により、金額をフォーマットする。 <br>
     * 
     * @param kingaku 金額
     * @param systemKbn システム区分
     * @return フォーマットされた金額
     */
    private String formatKingaku(double kingaku, String systemKbn) {
        String formatKingaku = GS.EMPTY_CHARCTER;
        if (systemKbn.equals(GS.GSS)){
            //国内
        	formatKingaku = Function.format(NUM_FMT_KOKUNAI, kingaku);
        }else{
            //海外
        	formatKingaku = Function.format(NUM_FMT_KAIGAI, kingaku);
        }
        return formatKingaku;
    }

    /**
     * 差戻・転送フラグにより、進捗を編集する。<br>
     * 
     * @param sasitenFlg 差戻・転送フラグ
     * @param shinchoku 進捗
     * @return 編集された進捗
     */
    private String sasiHantei(String sasitenFlg,String shinchoku) {
        StringBuffer shinchokuHyoujiyou = new StringBuffer();
        if(SASI_FLG.equals(Function.trim(sasitenFlg))){
        	shinchokuHyoujiyou.append(ASTA)
        					  .append(GS.SPACE_CHARCTER);
        }
        shinchokuHyoujiyou.append(Function.trim(shinchoku));
        return shinchokuHyoujiyou.toString();
    }
}