/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/26		SSC				課題No.103 一覧初期化処理削除
003		2009/11/26		SSC				課題No.109 エラー時のフォーカス制御修正
004		2009/12/22		SSC				課題No.223 エラーメッセージ修正
******************************************************************************/
package app.system.bss;

import app.system.dbAcc.KanjyoDbAcc;
import app.system.form.KanjyoBean;
import app.system.form.KanjyoForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.struts.AppDownload;
import common.util.Log;
import common.util.Pager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;



/**
 * OS7108_勘定科目マスタメンテナンス_一覧・登録 ビジネス ロジッククラス <br>
 */
public class KanjyoBss {

	private AppContext appContext = null;					                    // ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						               	// ＤＢアクセス
	private Log log = null;									                // LOG
	private KanjyoForm form;                                                   // アクションフォーム
	
    
	private static final String INSERT 				= "insert";
	private static final String F_NAME 				= "勘定科目.csv";
	
	//課題No.108
	//追加開始
	private static final String WINDOWS_31J = "Windows-31J";
	//追加完了
	//課題No.109
	//追加開始
	private static final String START_INDEX = "startIndex";
	private static final String END_INDEX = "endIndex";
	private static final String ERR_INDEX = "errIndex";
	//追加完了
	
	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public KanjyoBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		this.log = appContext.getLog();
		this.form = (KanjyoForm) appContext.getActionForm();
	}
	
	/**
	 * 画面初期表示値取得(メニューリンクから遷移時) <br>
	 * 
	 * @return 画面ＩＤ
	 * @throws Exception
	 */
	public String executeInit() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		KanjyoDbAcc dbacc = new KanjyoDbAcc(sqlExec, log, appContext);

		// システムセレクトボックス値取得
		dbacc.getSystem();
		
		// 汎用１セレクトボックス値取得
		dbacc.getHanyo1();
		
		// 汎用２セレクトボックス値取得
		dbacc.getHanyo2();
		
		// 債権フラグセレクトボックス値取得
		dbacc.getSaiken_flg();
		
		// 表示件数セレクトボックス値取得
		dbacc.getShow();
		
		return GS.OS7108;
	}
    
    /**
     * 【システムセレクトボックス処理】 <br>
     * 
     * @return
     * @throws Exception
     */
    public String system() throws Exception {  

        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
		KanjyoDbAcc dbacc = new KanjyoDbAcc(sqlExec, log, appContext);
    
		// 汎用１セレクトボックス値取得
		dbacc.getHanyo1();
		
		// 汎用２セレクトボックス値取得
		dbacc.getHanyo2();
		
		// ヘッダ部債権フラグセレクトボックス値取得	
		dbacc.getSaiken_flg();
		
		
        // 課題No.103
        // 削除開始
        // ActionForm に明細を格納
		//form.setAr_meisai(new ArrayList<HashMap>());
        // ページ設定
		//form.setPager(new ArrayList<HashMap>());        
        // 削除完了
        
        return GS.OS7108;
    }
    
    /**
     * 【汎用１セレクトボックス処理】 <br>
     * 
     * @return
     * @throws Exception
     */
    public String hanyo1() throws Exception {  

        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
		KanjyoDbAcc dbacc = new KanjyoDbAcc(sqlExec, log, appContext);
    
		// 汎用２セレクトボックス値取得
		dbacc.getHanyo2();
        
        return GS.OS7108;
    }
    
    /**
     * 【検索処理】 <br>
     * 
     * @return
     * @throws Exception
     */
    public String doSearch() throws Exception {  

        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
		KanjyoDbAcc dbacc = new KanjyoDbAcc(sqlExec, log, appContext);
    
		// 検索条件を格納する
		form.setSearch_system_kbn(form.getSystem_kbn());
		form.setSearch_hanyo1(form.getHanyo1());
		form.setSearch_hanyo2(form.getHanyo2());
		form.setSearch_kanjo_cd(form.getKanjo_cd());
		form.setSearch_kanjo_nm(form.getKanjo_nm());
		form.setSearch_kanjo_uchi_cd(form.getKanjo_uchi_cd());
		form.setSearch_saiken_flg(form.getSaiken_flg());
		
		// DR/CR区分セレクトボックス値取得
		dbacc.getDrcrkbn();
		
		// 債権フラグセレクトボックス値取得
		dbacc.getMeisai_saiken_flg();
		
		// 表示区分セレクトボックス値取得
		dbacc.getHyojikbn();
		
		// 明細情報取得
		dbacc.getMeisai();

        return GS.OS7108;
    }
    
	/**
	 * 画面初期表示値取得(メニューリンク以外から遷移時) <br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception {	

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		KanjyoDbAcc dbacc = new KanjyoDbAcc(sqlExec, log, appContext);		
		
		// 登録画面「戻る」にするとき
		if (!(INSERT).equals(form.getAction_flg())) {
			return GS.OS7108;			
		}
		
		// DR/CR区分セレクトボックス値取得
		dbacc.getDrcrkbn();
		
		// 債権フラグセレクトボックス値取得
		dbacc.getMeisai_saiken_flg();
		
		// 表示区分セレクトボックス値取得
		dbacc.getHyojikbn();
		
		// 明細情報取得
		dbacc.getMeisai();
		
		return GS.OS7108;
		
	}
    
	/**
	 * 更新処理 <br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doUpdate() throws Exception {	

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		KanjyoDbAcc dbacc = new KanjyoDbAcc(sqlExec, log, appContext);

		// 登録するデータが無い場合
		if (form.getAr_meisai().size() < 1) {
			// 課題No.223
			// 追加開始
			//appContext.setMsgCode(GL.ERR_NODATA);
			appContext.setMsgCode(GL.ERR_REGISTEREDDATA);
			// 追加完了
			return GS.OS7108;
		}		
		
		//課題No.109
		//追加開始
		Map<String,String> map = getIndexMap();
		//追加完了
		
		// 一覧情報の債権フラグセレクトボックスがブランクの場合、エラーメッセージを表示する
		//課題No.109
		//修正開始
		//if (doChkSaikenFlgBlank()) {
		if (doChkSaikenFlgBlank(map)) {
		//修正完了
			appContext.setMsgCode(GL.ERR_SELECT, GL.OS7108_SAIKEN_FLG);
			return GS.OS7108;
		}
		
		// 債権フラグが「対象外」以外の場合で、DR/CR区分がブランクの場合はエラーメッセージを表示する
		//課題No.109
		//修正開始
		//if (doChkSaikenFlgTaishougai()) {
		if (doChkSaikenFlgTaishougai(map)) {
		//修正完了
			appContext.setMsgCode(GL.ERR_SELECT, GL.OS7108_MEISAI_DRCR_KBN);
			return GS.OS7108;
		}
		
		// M14_勘定科目マスタの更新
		for (int i = 0; i < form.getAr_meisai().size(); i++) {
			KanjyoBean meisaiBean = (KanjyoBean) form.getAr_meisai().get(i);
			dbacc.setUpdateM1400(meisaiBean);
		}
		
		dbacc.commit();
		
		// 検索条件が退避する
		form.setSystem_kbn(form.getSearch_system_kbn());
		form.setHanyo1(form.getSearch_hanyo1());
		form.setHanyo2(form.getSearch_hanyo2());
		form.setKanjo_cd(form.getSearch_kanjo_cd());
		form.setKanjo_nm(form.getSearch_kanjo_nm());
		form.setKanjo_uchi_cd(form.getSearch_kanjo_uchi_cd());
		form.setSaiken_flg(form.getSearch_saiken_flg());
		
		// 明細情報取得
		dbacc.getMeisai();
		
		return GS.OS7108;
	}
	
	/**
	 * 債権フラグセレクトボックスが「対象外」のチッェク <br>
	 * 
	 * @return
	 */
	//課題No.109
	//修正開始
	private boolean doChkSaikenFlgTaishougai(Map<String,String> map) {
	//修正完了
	
		boolean resualt = false;
		
		for (int i = 0; i < form.getAr_meisai().size(); i++) {
			
			KanjyoBean meisaiBean = (KanjyoBean) form.getAr_meisai().get(i);
			
			if ((!GS.GSS.equals(form.getSystem_kbn())) 
				&& (!("0").equals(meisaiBean.getMeisai_saiken_flg()))
				&& (GS.EMPTY_CHARCTER).equals(meisaiBean.getMeisai_drcr_kbn())) {
				//課題No.109
				//修正開始
				if(isFocus(map,i)){
					//appContext.setFocusField("list["+ i +"].meisai_drcr_kbn");
					appContext.setFocusField("list["+ map.get(ERR_INDEX) +"].meisai_drcr_kbn");
				}
				//修正完了
				return true;
			}
		}
		
		return resualt;
	}
	
	/**
	 * 一覧情報の債権フラグセレクトボックスがブランクのチッェク <br>
	 * 
	 * @return
	 */
	//課題No.109
	//修正開始
	//private boolean doChkSaikenFlgBlank() {
	private boolean doChkSaikenFlgBlank(Map<String,String> map) {
	//修正完了
	
		boolean resualt = false;
		
		for (int i = 0; i < form.getAr_meisai().size(); i++) {
			KanjyoBean meisaiBean = (KanjyoBean)form.getAr_meisai().get(i);
			
			if (form.getAr_meisai_saiken_flg() == null 
				|| GS.EMPTY_CHARCTER.equals(meisaiBean.getMeisai_saiken_flg())) {
				//課題No.109
				//修正開始
				if(isFocus(map,i)){
					//appContext.setFocusField("list["+ i +"].meisai_saiken_flg");
					appContext.setFocusField("list["+ map.get(ERR_INDEX) +"].meisai_saiken_flg");
				}
				//修正完了
				return true;				
			}
		}
		
		return resualt;
	}
	
	//課題No.109
	//追加開始
	/**
	 * フォーカス判定 <br>
	 * 
	 * @return boolean
	 */
	private boolean isFocus(Map<String,String> map,int i) {
		
		boolean rtn = Boolean.FALSE;
		int startIndex = Integer.parseInt(map.get(START_INDEX));
		int endIndex = Integer.parseInt(map.get(END_INDEX));
	
		if((startIndex <= i) && (endIndex >= i)){
			map.put(ERR_INDEX,Integer.toString(i - startIndex));
			rtn = Boolean.TRUE;
		}
		
		return rtn;
	}
	//追加完了

	//課題No.109
	//追加開始
	/**
	 * カレントページのインデックスマップ取得 <br>
	 * 
	 * @return map
	 */
	private Map<String,String> getIndexMap() {
	
		Map<String,String> map = new HashMap<String,String>();
		Pager pager = form.getPager();
		int endIndex = pager.getCurrentIndex() - 1;
		int startIndex = endIndex - endIndex%form.getView();
		
		if(pager != null){
			map.put(START_INDEX,Integer.toString(startIndex));
			map.put(END_INDEX,Integer.toString(endIndex));
		}
		
		return map;
	}
	//追加完了
	
	/**
	 * ダウンロード処理(CSV出力) <br>
	 * 
	 * @return
	 */
	public void download(AppContext appContext) throws Exception {	

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
		//CSV作成
		AppDownload down = new AppDownload();
		File file = new File(F_NAME);
		PrintWriter pw = null;
		try{
			//課題No.108
			//修正開始
			//pw = new PrintWriter(file);
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file),WINDOWS_31J));
			//修正完了
			
			KanjyoDbAcc dbacc = new KanjyoDbAcc(sqlExec, log, appContext);
			
			//ヘッダ部情報の取得
			dbacc.getHeader(pw);

			//一覧情報の取得
			dbacc.getMeisai_csv(pw);
			
			pw.close();
			pw = null;

			down.execute(file,F_NAME,appContext.getResponse());
			
        } catch(Exception e){
			log.write(GS.LOG_INF,this.getClass().getName(),"end - " + e.getMessage());
			throw e;
		}finally{
			if(pw != null){
				pw.close();
				pw = null;
			}
		}
	}
	
}
