/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2014/02/24		SSC				案件No.D12883 米国SAP対応
******************************************************************************/

package app.syokai.bss;

import app.syokai.dbAcc.SincyokuDbAcc;
import app.syokai.form.SincyokuForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * OS6103 進捗状況照会 ビジネス ロジッククラス <br>
 */
public class SincyokuBss {
    private AppContext appContext = null;                         // ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;                           // ＤＢアクセス
    private SincyokuForm form = null;                    	// アクションフォーム
    private Log log = null;                                       // LOG


    /**
     * コンストラクタ
     */
    public SincyokuBss(AppContext appContext) throws Exception {
        this.appContext = appContext;    
        form = (SincyokuForm)appContext.getActionForm();
        this.log = appContext.getLog();
    }

    /**
     * 画面初期表示値取得(メニューリンクから遷移時)
     */
    public String executeInit() throws Exception {
        
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SincyokuDbAcc dbacc = new SincyokuDbAcc(sqlExec, log, appContext);

        // フェーズセレクトボックスの設定値を取得する
        dbacc.getPhase();
        
        // ステータスセレクトボックスの設定値を取得する
        dbacc.getStatus();
        
        // 所在国セレクトボックスの設定値を取得する
        dbacc.getCountry();
        
        // 当画面の汎用項目ラベルを取得する
        dbacc.getHanyouTitle();
        
        // 汎用1セレクトボックスの設定値を取得する
        dbacc.getHanyou1();
        
        // 汎用2セレクトボックスの設定値を取得する
        dbacc.getHanyou2();
        
        // 汎用3セレクトボックスの設定値を取得する
        dbacc.getHanyou3();

        // 表示件数セレクトボックス値取得
        dbacc.getShow();

        return GS.OS6103;
    }
    
    /**
     * 画面初期表示値取得(メニューリンク以外から遷移時)
     */
    public String execute() throws Exception {  
    	// 検索したときの条件を取得する
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SincyokuDbAcc dbacc = new SincyokuDbAcc(sqlExec, log, appContext);
        if(!dbacc.getMeisai()){
        	appContext.setMsgCode(GL.ERR_SEARCHOVER, GL.REPLACE_1000);
        }

        return GS.OS6103;
    }
    
    /**
     * 
     * 汎用2セレクトボックスの設定値を取得する <br>
     * 
     * @return
     * @throws Exception
     */
    public String doChange1() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SincyokuDbAcc dbacc = new SincyokuDbAcc(sqlExec, log, appContext);
        form.setHanyou2(null);
        form.setHanyou3(null);
        // 汎用2セレクトボックスの設定値を取得する
        dbacc.getHanyou2();
        
    	return GS.OS6103; 
    }
    
    /**
     * 
     * 汎用3セレクトボックスの設定値を取得する <br>
     * 
     * @return
     * @throws Exception
     */
    public String doChange2() throws Exception {
    	
    	InputCheck inChk = new InputCheck();
    	// 対象年月
    	if(!inChk.isNullBlank(form.getMonth()) && !inChk.isNumber(form.getMonth())) {
    		return GS.OS6103;
    	}
    	if(!inChk.isNullBlank(form.getMonth()) && form.getMonth().length() != 6) {
    		return GS.OS6103;
    	}
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SincyokuDbAcc dbacc = new SincyokuDbAcc(sqlExec, log, appContext);
        form.setHanyou3(null);
        // 汎用3セレクトボックスの設定値を取得する
        dbacc.getHanyou3();
    	
    	return GS.OS6103; 
    }
    
    /**
     * 
     * 検索アクション <br>
     * 
     * @return
     * @throws Exception
     */
    public String doSearch() throws Exception {  
    	// 入力チェック
    	if(!doCheck()){
    		return GS.OS6103;
    	}
    	// 検索したときの条件を設定する
    	doSetForm();
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SincyokuDbAcc dbacc = new SincyokuDbAcc(sqlExec, log, appContext);
        if(!dbacc.getMeisai()){
        	appContext.setMsgCode(GL.ERR_SEARCHOVER, GL.REPLACE_1000);
        }
    	return GS.OS6103;
    }
    
    /**
     * 
     * 入力チェック <br>
     * 
     * @return
     * @throws Exception
     */
    public boolean doCheck() throws Exception {  
    	
    	InputCheck inChk = new InputCheck();
    	// 対象年月
    	if(!inChk.isNullBlank(form.getMonth()) && !inChk.isNumber(form.getMonth())) {
    		List<String> list = new ArrayList<String>();
        	list.add(GL.ERR_DIGITS2);
        	list.add(GL.OS6103_KENSAKU_MONTH);
        	list.add("6");
        	appContext.setMsgCode(list);
        	return false;
    	}
    	if(!inChk.isNullBlank(form.getMonth()) && form.getMonth().length() != 6) {
    		List<String> list = new ArrayList<String>();
        	list.add(GL.ERR_DIGITS2);
        	list.add(GL.OS6103_KENSAKU_MONTH);
        	list.add("6");
        	appContext.setMsgCode(list);
        	return false;
    	}
    	// 勘定先CD(12バイトを超えるデータが入力されていた場合)
    	if(!inChk.isNullBlank(form.getKanjo_cd()) && !inChk.islength(form.getKanjo_cd(), 12)){
    		appContext.setMsgCd(GL.ERR_LENGTH, GL.OS6103_KENSAKU_KANJO_CD);
    		return false;
    	}
    	// 勘定先CD(入力禁止文字がデータに含まれていた場合)
    	if(!inChk.isNullBlank(form.getKanjo_cd()) && chkKinshiChar(form.getKanjo_cd())){

    		return false;
    	}
    	// DUNS No.(9バイトを超えるデータが入力されていた場合)
    	if(!inChk.isNullBlank(form.getDuns_no()) && !inChk.islength(form.getDuns_no(), 9)){
    		appContext.setMsgCd(GL.ERR_LENGTH, GL.OS6103_KENSAKU_DUNS_NO);
    		return false;
    	}
    	// DUNS No.(入力禁止文字がデータに含まれていた場合)
    	if(!inChk.isNullBlank(form.getDuns_no()) && chkKinshiChar(form.getDuns_no())){

    		return false;
    	}
    	// 勘定先名称(120バイトを超えるデータが入力されていた場合)
    	if(!inChk.isNullBlank(form.getKanjo_nm()) && !inChk.islength(form.getKanjo_nm(), 120)){
    		appContext.setMsgCd(GL.ERR_LENGTH, GL.OS6103_KENSAKU_KANJO_NM);
    		return false;
    	}
    	// 勘定先名称(入力禁止文字がデータに含まれていた場合)
    	if(!inChk.isNullBlank(form.getKanjo_nm()) && chkKinshiChar(form.getKanjo_nm())){

    		return false;
    	}
    	// 関係者(100バイトを超えるデータが入力されていた場合)
    	if(!inChk.isNullBlank(form.getParties()) && !inChk.islength(form.getParties(), 100)){
    		appContext.setMsgCd(GL.ERR_LENGTH, GL.OS6103_KENSAKU_PARTIES);
    		return false;
    	}
    	// 関係者(入力禁止文字がデータに含まれていた場合)
    	if(!inChk.isNullBlank(form.getParties()) && chkKinshiChar(form.getParties())){

    		return false;
    	}
    	// 処理日FROM(整数8桁(半角)以外で入力されていた場合)
    	if(!inChk.isNullBlank(form.getSyori_dtFrom()) && !inChk.isNumber(form.getSyori_dtFrom())){
    		List<String> list = new ArrayList<String>();
    		list.add(GL.ERR_DIGITS2);
    		list.add(GL.OS6103_FROM);
    		list.add("8");
    		appContext.setMsgCode(list);
    		return false;
    	}
    	if(!inChk.isNullBlank(form.getSyori_dtFrom()) && form.getSyori_dtFrom().length() != 8){
    		List<String> list = new ArrayList<String>();
    		list.add(GL.ERR_DIGITS2);
    		list.add(GL.OS6103_FROM);
    		list.add("8");
    		appContext.setMsgCode(list);
    		return false;
    	}
    	// 処理日TO(整数8桁(半角)以外で入力されていた場合)
    	if(!inChk.isNullBlank(form.getSyori_dtTo()) && !inChk.isNumber(form.getSyori_dtTo())){
    		List<String> list = new ArrayList<String>();
    		list.add(GL.ERR_DIGITS2);
    		list.add(GL.OS6103_TO);
    		list.add("8");
    		appContext.setMsgCode(list);
    		return false;
    	}
    	if(!inChk.isNullBlank(form.getSyori_dtTo()) && form.getSyori_dtTo().length() != 8){
    		List<String> list = new ArrayList<String>();
    		list.add(GL.ERR_DIGITS2);
    		list.add(GL.OS6103_TO);
    		list.add("8");
    		appContext.setMsgCode(list);
    		return false;
    	}
    	
    	return true;
    }
    
    /**
     * 
     * チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示 <br>
     * 
     * @return
     * @throws Exception
     */
    public boolean chkKinshiChar(String str) throws Exception {
    	InputCheck inChk = new InputCheck();
    	// エラーメッセージ(err.prohibitted、入力禁止文字)を表示する
    	for(int i = 0; i < str.length(); i++){
    		String kinshiChar = str.substring(i, i + 1);
    		if (inChk.haveKinshiMoji(kinshiChar)) {
				// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
				appContext.setMsgCd(GL.ERR_PROHIBITTED, kinshiChar);
				return true;
			}
    	}
    	return false;
    }
    
    /**
     * 
     * 検索したときの条件を設定する <br>
     * 
     * @throws Exception
     */
    public void doSetForm() throws Exception {
    	// 対象年月検索
    	form.setKensaku_month(new String(Function.trim(form.getMonth())));
    	// 勘定先CD検索
    	form.setKensaku_kanjo_cd(new String(Function.trim(form.getKanjo_cd())));
    	// DUNSNo.検索
    	form.setKensaku_duns_no(new String(Function.trim(form.getDuns_no())));
    	// 勘定先名称検索
    	form.setKensaku_kanjo_nm(new String(Function.trim(form.getKanjo_nm())));
    	// 所在国検索
    	form.setKensaku_country(new String(Function.trim(form.getCountry())));
    	// フェーズ検索
    	form.setKensaku_phase(new String(Function.trim(form.getPhase())));
    	// ステータス検索
    	form.setKensaku_status(new String(Function.trim(form.getStatus())));
    	// 汎用1検索
    	form.setKensaku_hanyou1(new String(Function.trim(form.getHanyou1())));
    	// 汎用2検索
    	form.setKensaku_hanyou2(new String(Function.trim(form.getHanyou2())));
    	// システム区分検索
    	form.setKensaku_systemKbn(new String(Function.trim(form.getSystemKbn())));
    	// 参照分類２コード検索
    	form.setKensaku_bunrui2(new String(Function.trim(form.getBunrui2())));
    	// 汎用3検索
    	form.setKensaku_hanyou3(new String(Function.trim(form.getHanyou3())));
    	// 関係者検索
    	form.setKensaku_parties(new String(Function.trim(form.getParties())));
    	// 処理日FROM検索
    	form.setKensaku_syori_dtFrom(new String(Function.trim(form.getSyori_dtFrom())));
    	// 処理日TO検索
    	form.setKensaku_syori_dtTo(new String(Function.trim(form.getSyori_dtTo())));	
    }
}
