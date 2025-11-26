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
import app.common.form.TairyuSincyokuForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  OZ6110_実質滞留債権判定進捗照会タブ DBアクセスクラス <br>
 */
public class TairyuSincyokuDbAcc extends CommonDbAcc {

    private TairyuSincyokuForm form = null;                 	// アクションフォーム
    //IT159対応
	private static final String H_DAIKO							= "( 代行 ";
	private static final String H_DAIKO_EN						= "( proxy ";
    //IT159ここまで

    //Resultset用文字列
	private static final String ID						= "id";				// id
	
	private static final String HANYOU1					= "hanyou1";		// 汎用1
    private static final String SOSHIKI					= "soshiki";		// 組織
    private static final String ANKEN_NO 					= "anken_no";		// 案件No.
    private static final String SYORI_DT_TIL 				= "syori_dt_til";	// 処理日時タイトル
    private static final String DAIKO						= "daiko";			// 代行者
	private static final String PHASE						= "phase";			// フェーズ
    private static final String TANTOU					= "tantou";			// 担当者
    private static final String SYORI						= "syori";			// 処理
    private static final String SYORI_DT					= "syori_dt";		// 処理日時
    private static final String INPUT_KBN 				= "input_kbn";		// 入力区分
    private static final String TOROKU_POINT 				= "toroku_point";	// 登録箇所
    private static final String COMMENT_VAL 				= "comment_val";	// コメント内容
    private static final String LINK_FLG 					= "link_flg";		// リンクフラグ
    
    private static final String NYURYOKU_TENSOU 			= "50";				// 50：転送
    private static final String NYURYOKU_SASHIMODOSHI		= "60";				// 60：差戻
    // ResultSet用
    private static final String SINTYOKU 					= "sintyoku";
	private static final String ORA 						= "ora";
	private static final String SATEI_KAISHA_CD 			= "satei_kaisha_cd";
	private static final String OPE_KBN 					= "ope_kbn";
    
    private static final String SP_SS_OZ6110_SELECT_SOSHIKI           = "SP_SS_OZ6110_SELECT_SOSHIKI";        // 取引先に対し滞留判定を行った組織を取得する。
    private static final String SP_SS_OZ_SELECT_T1300                 = "SP_SS_OZ_SELECT_T1300";              // 取引先の進捗一覧を取得する。
 
    // INパラメータ
    private String systemKbn;           // 共)取引先情報.システム区分
    private String sateiKaishaCd;		 // 共)取引先情報.査定会社コード
    private String miseCd;				 // 共)取引先情報.店コード
    private String sateiKi;			 // 共)取引先情報.査定期
    private String toriCd;				 // 共)取引先情報.取引先コード
    private String comLangMode;         // 共)言語モード
    private String init_bunrui2;		 // 共)取引先情報.初期分類2
    
    /**
     * コンストラクタ
     * 
     * @param sqlExec SqlExecuter
     * @param log Log
     * @param appcontext AppContext
     */
    public TairyuSincyokuDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);

        //ビーン取得
        SessionData cmnData = appcontext.getCMN();
        TorihikisakiBean tori_bean = cmnData.getTori_bean();
        form = (TairyuSincyokuForm)appcontext.getActionForm();

        //ビーンの値を変数に設定
        comLangMode = cmnData.getComLangMode();
        systemKbn = tori_bean.getSystem_kbn();
        sateiKaishaCd = tori_bean.getSateikaisya_cd();
        miseCd = tori_bean.getMise_cd();
        sateiKi = tori_bean.getSatei_ki();
        toriCd = tori_bean.getKanjo_cd();
        init_bunrui2 = tori_bean.getInit_bunrui2();
    }
    
    /**
     * 変数初期化
     */
    public void initialize() {
        // INパラメータ
        systemKbn = GS.EMPTY_CHARCTER;
        comLangMode = GS.EMPTY_CHARCTER;
        sateiKaishaCd = GS.EMPTY_CHARCTER;
        miseCd = GS.EMPTY_CHARCTER;
        sateiKi = GS.EMPTY_CHARCTER;
        toriCd = GS.EMPTY_CHARCTER;
        init_bunrui2 = GS.EMPTY_CHARCTER;
    }

    //IT159対応
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
            if(comLangMode.equals(GS.LANG_JA)){
            	tanto_nm.append(H_DAIKO);
            }else{
            	tanto_nm.append(H_DAIKO_EN);
            }
            tanto_nm.append(Function.trim(Function.trim(daiko)))
                    .append(GS.KAKKO_MIGI);
        }
        return tanto_nm.toString();
    }
    //IT159ここまで

    /**
     * 取引先に対し滞留判定を行った組織を取得する。 <br>
     * 
     * @exception SQLException
     */
    public void getSosikiList() throws SQLException {
        
        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OZ6110_SELECT_SOSHIKI, sqlExec);
        cstmt.setStringIn(systemKbn);
        cstmt.setStringIn(sateiKaishaCd);
        cstmt.setStringIn(miseCd);
        cstmt.setStringIn(sateiKi);
        cstmt.setStringIn(toriCd);
        cstmt.setStringIn(comLangMode);
        cstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);    
            
			// 組織情報リスト
            List<Map<String, String>> ar_sosiki = new ArrayList<Map<String, String>>();
            int i = 0;
            while ( rs.next() ) {
            	// 組織情報
            	Map<String, String> sosiki = new HashMap<String, String>();
            	// ID
            	sosiki.put(ID, Function.getStringOfInt(i));
				// 汎用1
            	sosiki.put(HANYOU1, rs.getString(SATEI_KAISHA_CD));
				// 組織
            	sosiki.put(SOSHIKI, rs.getString(ORA));
				// 進捗
            	sosiki.put(SINTYOKU, rs.getString(SINTYOKU));
				// 案件No.
            	sosiki.put(ANKEN_NO, rs.getString(ANKEN_NO));
            	
            	ar_sosiki.add(i, sosiki);
            	i++;
            }
            
            form.setAr_sosiki(ar_sosiki);
            
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
     * 取引先の進捗一覧を取得する <br>
     * 
     * @exception SQLException
     */
    public void getSinchokuList() throws SQLException {
    	// 組織にはデータがないの場合
    	if (form.getAr_sosiki()==null || form.getAr_sosiki().size()==0) {
    		return;
    	}
        // 検索用案件No.
    	String srh_anken_no = form.getSrh_anken_no();
        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OZ_SELECT_T1300, sqlExec);
        cstmt.setStringIn(comLangMode);
        cstmt.setStringIn(systemKbn);
        cstmt.setStringIn(sateiKaishaCd);
        cstmt.setStringIn(init_bunrui2);
        cstmt.setStringIn(srh_anken_no);
        cstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);    
            
			// ActionForm に取得値を格納
            List<Map<String, String>> ar_sinchoku = new ArrayList<Map<String, String>>();

            int i = 0;
            while ( rs.next() ) {
                // 処理日時タイトル
            	if (i == 0) {
            		form.setSyori_dt_til(rs.getString(SYORI_DT_TIL));                
            	}

            	// 進捗情報
            	Map<String, String> sinchoku = new HashMap<String, String>();
                //id
                sinchoku.put(ID, Function.getStringOfInt(i));
                // フェーズ
                sinchoku.put(PHASE, rs.getString(PHASE));
                // 汎用1
                sinchoku.put(HANYOU1, rs.getString(HANYOU1));
                // 組織
                sinchoku.put(SOSHIKI, rs.getString(ORA));
                // 担当者
                // IT159対応
                sinchoku.put(TANTOU, getTanto_nm(rs.getString(TANTOU),rs.getString(DAIKO)));
                // IT159ここまで
                // 処理
                sinchoku.put(SYORI, rs.getString(SYORI));
                // 処理日時
                sinchoku.put(SYORI_DT, rs.getString(SYORI_DT));
                // 案件Nｏ．
                sinchoku.put(ANKEN_NO, rs.getString(ANKEN_NO));
                // 入力区分
                sinchoku.put(INPUT_KBN, rs.getString(OPE_KBN));
                // 登録箇所
                sinchoku.put(TOROKU_POINT, rs.getString(TOROKU_POINT));
                // コメント内容
                sinchoku.put(COMMENT_VAL, rs.getString(COMMENT_VAL));
				// リンク表示フラグ制御
				boolean link_flg = false;				
				if (NYURYOKU_TENSOU.equals(rs.getString(OPE_KBN))
					|| NYURYOKU_SASHIMODOSHI.equals(rs.getString(OPE_KBN))) {
					link_flg = true;
				}
				sinchoku.put(LINK_FLG, String.valueOf(link_flg));
                // 明細配列に取得情報を格納
				ar_sinchoku.add(i, sinchoku);
                i++;   
            }
            
            // ActionForm に明細を格納
            form.setView(i++);
            form.setAr_sinchoku(ar_sinchoku);  

            // ページ設定
            form.setPager(ar_sinchoku);

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
}
