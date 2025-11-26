/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.dbAcc;

import app.TorihikisakiBean;
import app.satei.form.SyoninForm;
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
 * OC1106_査定_承認一覧 DBアクセスクラス <br>
 */
public class SyoninDbAcc extends SateiDbAcc {

	private SyoninForm koForm = null;			//アクションフォーム

	//Resultset用文字列    
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

    private static final String SP_SS_OC1106_SELECT_ICHIRAN	= "SP_SS_OC1106_SELECT_ICHIRAN";//査定承認一覧取得プロシージャ
    private static final String SP_SS_O_UPDATE_T1403			= "SP_SS_O_UPDATE_T1403";       //取戻不可フラグ更新プロシージャ
	private static final String SHOW							= "show";
	/**
	 * コンストラクタ <br>
	 * 
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public SyoninDbAcc(AppContext appcontext) {
		super(appcontext);
		//Bean取得
		form = (SyoninForm)appContext.getActionForm();
	}
	
	/**
	 * 表示件数セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getShow() throws SQLException {

		koForm = (SyoninForm)form;
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
     * T14_査定進捗管理の更新(取戻不可で更新する) <br>
     * @throws SQLException
     */
    public void setTorimodoshiFukaFlg() throws SQLException {

		koForm = (SyoninForm)form;
		
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1403,sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(user_bean.getComSansyoBunrui2());
        exCstmt.setStringIn(koForm.getSansyo_phase());
        exCstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
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

		koForm = (SyoninForm)form;
		ResultSet rs = null;

        //ストアドプロシージャ生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OC1106_SELECT_ICHIRAN,sqlExec);
        exCstmt.setStringIn(cmnData.getComLangMode());
        exCstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
        exCstmt.setStringIn(koForm.getSansyo_phase());
        exCstmt.setStringIn(user_bean.getComSansyoBunrui2());
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
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
                //承認チェックボックス
                listBean.setSyonin_chk(GS.EMPTY_CHARCTER);
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