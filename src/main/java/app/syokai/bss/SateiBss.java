/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2014/02/24		SSC				案件No.D12883 米国SAP対応
******************************************************************************/

package app.syokai.bss;

import app.syokai.dbAcc.SateiDbAcc;
import app.syokai.form.SateiForm;
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
 * OS6101_査定内容照会 ビジネス ロジッククラス <br>
 */
public class SateiBss {
    private AppContext appContext = null;                         // ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;                           // ＤＢアクセス
    private SateiForm form = null;                    	// アクションフォーム
    private Log log = null;                                       // LOG


    /**
     * コンストラクタ
     */
    public SateiBss(AppContext appContext) throws Exception {
        this.appContext = appContext;    
        form = (SateiForm)appContext.getActionForm();
        this.log = appContext.getLog();
    }
    
    /**
     * 画面初期表示値取得(メニューリンクから遷移時)
     */
    public String executeInit() throws Exception {
        
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SateiDbAcc dbacc = new SateiDbAcc(sqlExec, log, appContext);
        
        // 査定検索/滞留判定検索
        form.setSatei_tairyu("2");
        
        // 最新査定期取得処理
        dbacc.getSateiki();
        
        // 半期・四半期区分【リスト】取得
        dbacc.getHanki_sihanki();
        
        // 所在国【リスト】取得
        dbacc.getCountry();
        
        // 取引先区分【リスト】取得
        dbacc.getTorihikisaki_kbn();
        
        // 債権区分【リスト】取得
        dbacc.getSaiken_kbn();
        
        // 抽出事由【リスト】取得
        dbacc.getTyusyutu();
        
        // 汎用項目ラベル【リスト】取得
        dbacc.getHanyou();
        
        // 汎用1セレクトボックスの設定値を取得する
        dbacc.getHanyou1();
        
        // 汎用2セレクトボックスの設定値を取得する
        dbacc.getHanyou2();
        
        // 汎用3セレクトボックスの設定値を取得する
        dbacc.getHanyou3();
        
        // ソート順セレクトボックス値取得
        dbacc.getSort();

        // 表示件数セレクトボックス値取得
        dbacc.getShow();

        return GS.OS6101;
    }
    
    /**
     * 画面初期表示値取得(メニューリンク以外から遷移時)
     */
    public String execute() throws Exception {  
    	
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SateiDbAcc dbacc = new SateiDbAcc(sqlExec, log, appContext);
        // 参照分類２コードを取得する
        String bunrui2 = dbacc.getBunrui2();
        // 対象外参照権限のを取得する
        String sansyo_systemkbn = dbacc.getKengen();
        if(!dbacc.getMeisai(bunrui2,sansyo_systemkbn)){
        	appContext.setMsgCode(GL.ERR_SEARCHOVER, GL.REPLACE_1000);
        }

        return GS.OS6101;
    }
    
    /**
     * 
     * ラジオボタンの変換 <br>
     * 
     * @return
     * @throws Exception
     */
    public String doSatei() throws Exception {
    	
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SateiDbAcc dbacc = new SateiDbAcc(sqlExec, log, appContext);
    	
        // ソート順セレクトボックス値取得
        dbacc.getSort();
        
        form.setAr_meisai(new ArrayList());
        form.setPager(new ArrayList());
        
        return GS.OS6101;
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
        SateiDbAcc dbacc = new SateiDbAcc(sqlExec, log, appContext);
        form.setHanyou2(null);
        form.setHanyou3(null);
        // 汎用2セレクトボックスの設定値を取得する
        dbacc.getHanyou2();
        
    	return GS.OS6101; 
    }
    
    /**
     * 
     * 汎用3セレクトボックスの設定値を取得する <br>
     * 
     * @return
     * @throws Exception
     */
    public String doChange2() throws Exception {
    	
    	//チェック
    	InputCheck inChk = new InputCheck();
    	
    	// 査定期
    	if(!inChk.isNullBlank(form.getSateiki()) && !inChk.isNumber(form.getSateiki())) {
    		return GS.OS6101; 
    	}
    	if(!inChk.isNullBlank(form.getSateiki()) && form.getSateiki().length() != 6){
    		return GS.OS6101; 
    	}
    	// 対象年月
    	if(!inChk.isNullBlank(form.getMonth()) && !inChk.isNumber(form.getMonth())) {
    		return GS.OS6101; 
    	}
    	if(!inChk.isNullBlank(form.getMonth()) && form.getMonth().length() != 6){
    		return GS.OS6101; 
    	}
    	
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SateiDbAcc dbacc = new SateiDbAcc(sqlExec, log, appContext);
        form.setHanyou3(null);
        // 汎用3セレクトボックスの設定値を取得する
        dbacc.getHanyou3();
    	
    	return GS.OS6101; 
    }
    
    /**
     * 
     * 検索処理 <br>
     * 
     * @return
     * @throws Exception
     */
    public String doSearch() throws Exception {
    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SateiDbAcc dbacc = new SateiDbAcc(sqlExec, log, appContext);
        
        // 入力チェック
        if(!doCheck()){
        	
        	return GS.OS6101;
        }
        // 検索したときの条件を設定する
        doSetForm();
        // 参照分類２コードを取得する
        String bunrui2 = dbacc.getBunrui2();
        // 対象外参照権限のを取得する
        String sansyo_systemkbn = dbacc.getKengen();
        // 一覧/詳細部の検索
        if(!dbacc.getMeisai(bunrui2,sansyo_systemkbn)){
        	appContext.setMsgCode(GL.ERR_SEARCHOVER, GL.REPLACE_1000);
        }
        
        return GS.OS6101; 
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

    	// 査定期
    	if(!inChk.isNullBlank(form.getSateiki()) && !inChk.isNumber(form.getSateiki())) {
    		List<String> list = new ArrayList<String>();
    		list.add(GL.ERR_DIGITS2);
    		list.add(GL.OS6101_SATEIKI);
    		list.add("6");
    		appContext.setMsgCode(list);
    		return false;
    	}
    	if(!inChk.isNullBlank(form.getSateiki()) && form.getSateiki().length() != 6){
    		List<String> list = new ArrayList<String>();
    		list.add(GL.ERR_DIGITS2);
    		list.add(GL.OS6101_SATEIKI);
    		list.add("6");
    		appContext.setMsgCode(list);
    		return false;
    	}
    	// 対象年月
    	if(!inChk.isNullBlank(form.getMonth()) && !inChk.isNumber(form.getMonth())) {
    		List<String> list = new ArrayList<String>();
    		list.add(GL.ERR_DIGITS2);
    		list.add(GL.OS6101_YM);
    		list.add("6");
    		appContext.setMsgCode(list);
    		return false;
    	}
    	if(!inChk.isNullBlank(form.getMonth()) && form.getMonth().length() != 6){
    		List<String> list = new ArrayList<String>();
    		list.add(GL.ERR_DIGITS2);
    		list.add(GL.OS6101_YM);
    		list.add("6");
    		appContext.setMsgCode(list);
    		return false;
    	}
    	// 12バイトを超えるデータが入力されていた場合
    	if(!inChk.isNullBlank(form.getKanjo_cd()) && !inChk.islength(form.getKanjo_cd(), 12)){
    		appContext.setMsgCd(GL.ERR_LENGTH, GL.OS6101_KANJYOCD);
    		return false;
    	}
    	// 勘定先CD(入力禁止文字がデータに含まれていた場合)
    	if(!inChk.isNullBlank(form.getKanjo_cd()) && chkKinshiChar(form.getKanjo_cd())){

    		return false;
    	}
    	// DUNS No.9バイトを超えるデータが入力されていた場合
    	if(!inChk.isNullBlank(form.getDuns_no()) && !inChk.islength(form.getDuns_no(), 9)){
    		appContext.setMsgCd(GL.ERR_LENGTH, GL.OS6101_DUNS_NO);
    		return false;
    	}
    	// DUNS No.(入力禁止文字がデータに含まれていた場合)
    	if(!inChk.isNullBlank(form.getDuns_no()) && chkKinshiChar(form.getDuns_no())){

    		return false;
    	}
    	// 勘定先名称120バイトを超えるデータが入力されていた場合
    	if(!inChk.isNullBlank(form.getKanjo_nm()) && !inChk.islength(form.getKanjo_nm(), 120)){
    		appContext.setMsgCd(GL.ERR_LENGTH, GL.OS6101_KANJYONAME);
    		return false;
    	}
    	// 勘定先名称(入力禁止文字がデータに含まれていた場合)
    	if(!inChk.isNullBlank(form.getKanjo_nm()) && chkKinshiChar(form.getKanjo_nm())){

    		return false;
    	}
    	// 関係者(100バイトを超えるデータが入力されていた場合)
    	if(!inChk.isNullBlank(form.getParties()) && !inChk.islength(form.getParties(), 100)){
    		appContext.setMsgCd(GL.ERR_LENGTH, GL.OS6101_KANKEISHA);
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
    		list.add(GL.OS6101_FROM);
    		list.add("8");
    		appContext.setMsgCode(list);
    		return false;
    	}
    	if(!inChk.isNullBlank(form.getSyori_dtFrom()) && form.getSyori_dtFrom().length() != 8){
    		List<String> list = new ArrayList<String>();
    		list.add(GL.ERR_DIGITS2);
    		list.add(GL.OS6101_FROM);
    		list.add("8");
    		appContext.setMsgCode(list);
    		return false;
    	}
    	// 処理日TO(整数8桁(半角)以外で入力されていた場合)
    	if(!inChk.isNullBlank(form.getSyori_dtTo()) && !inChk.isNumber(form.getSyori_dtTo())){
    		List<String> list = new ArrayList<String>();
    		list.add(GL.ERR_DIGITS2);
    		list.add(GL.OS6101_TO);
    		list.add("8");
    		appContext.setMsgCode(list);
    		return false;
    	}
    	if(!inChk.isNullBlank(form.getSyori_dtTo()) && form.getSyori_dtTo().length() != 8){
    		List<String> list = new ArrayList<String>();
    		list.add(GL.ERR_DIGITS2);
    		list.add(GL.OS6101_TO);
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
    	// 査定期検索
    	form.setKensaku_sateiki(new String(Function.trim(form.getSateiki())));
    	// 半期・四半期区分検索
    	form.setKensaku_hanki_sihanki(new String(Function.trim(form.getHanki_sihanki())));
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
    	// 取引先区分検索
    	form.setKensaku_torihikisaki_kbn(new String(Function.trim(form.getTorihikisaki_kbn())));
    	// 債権区分検索
    	form.setKensaku_saiken_kbn(new String(Function.trim(form.getSaiken_kbn())));
    	// 抽出事由検索
    	form.setKensaku_tyusyutu(new String(Function.trim(form.getTyusyutu())));
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
