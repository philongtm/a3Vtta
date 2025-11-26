/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2011/06/30		SSC				案件No.D9059 もぎ取り解除時に記入内容をクリアしない 
******************************************************************************/
package app.satei.bss;

import app.satei.dbAcc.TorokuDbAcc;
import app.satei.form.TorokuForm;
import common.AppContext;
import common.global.GL;
import common.global.GS;
import common.util.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * OC1102_査定_取引先概要 ビジネス ロジッククラス <br>
 */
public class TorokuBss extends SateiBss{

	private TorokuForm form				= null;		//アクションフォーム

	private static final String SANSYO_POINT	= "'10'";
	private static final String POINT_KESAN	= "10";
	private static final String ALL_POINT = "'10','20','30','40','50','60','70','80','90','95'";

	//要素名
    private static final String KABUNUSI_NM1	= "kabunusiNm1";	//株主名称1
    private static final String KABUNUSI_NM2	= "kabunusiNm2";	//株主名称2
    private static final String KABUNUSI_NM3	= "kabunusiNm3";	//株主名称3
    private static final String KABUNUSI_NM4	= "kabunusiNm4";	//株主名称4
    private static final String KABUNUSI_NM5	= "kabunusiNm5";	//株主名称5
    private static final String HIRITU1		= "hiritu1";		//保有比率1
    private static final String HIRITU2		= "hiritu2";		//保有比率2
    private static final String HIRITU3		= "hiritu3";		//保有比率3
    private static final String HIRITU4		= "hiritu4";		//保有比率4
    private static final String HIRITU5		= "hiritu5";		//保有比率5
    private static final String KABUSU1		= "kabusu1";		//保有株数1
    private static final String KABUSU2		= "kabusu2";		//保有株数2
    private static final String KABUSU3		= "kabusu3";		//保有株数3
    private static final String KABUSU4		= "kabusu4";		//保有株数4
    private static final String KABUSU5		= "kabusu5";   		//保有株数5
    private static final String JIGYONAIYO	= "jigyoNaiyo";		//事業内容
    private static final String KESANGAIKYO	= "kesanGaikyo";	//決算概況
	
	private static final String TOROKU_GAMEN 		= "1";		//OC1102_取引先概要
	private static final String NYURYOKU_HOZON 	= "20";		//20:一次保存
	private static final String NYURYOKU_KAIJO 	= "40";		//40:もぎ取り解除
	private List<String> msgList = null;
	private static final String KABUSU_PATTERN	= "^[-+]?[0-9]{1,10}$";	
	private static final String HIRITU_PATTERN	= "^[-+]?[0-9]{1,3}\\.[0-9]{1,2}$";	

	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public TorokuBss(AppContext appContext) throws Exception {
		super(appContext);
		this.form = (TorokuForm)appContext.getActionForm();
	}
    
    /**
     * 【入力チェック処理】 <br>
     * @return boolean
     */
    public boolean check() throws Exception{
    	
    	boolean rs = TRUE;

    	if(!this.isByteChk()){
			//入力バイトチェック
        	rs = FALSE;
		}else if(!this.isKinshiMojiChk()){
			//入力禁止文字入力チェック
        	rs = FALSE;
		}else if(!this.isNumChk()){
			//数値チェック
        	rs = FALSE;
		}
    	
    	return rs;
    }

    /**
     * 【数値チェック処理】 <br>
     * @return boolean
     */
    private boolean isNumChk() throws Exception{
		
    	String yousoNm = null;
    	boolean rs = TRUE;
    	
    	//保有株数チェック
    	msgList = new ArrayList<String>();
    	msgList.add(GL.ERR_DIGITS);
    	msgList.add(GL.OC1102_HOYUU_KABUSUU);
    	msgList.add(GL.REPLACE_10);
    	msgList.add(GL.REPLACE_0);
		if(!GS.EMPTY_CHARCTER.equals(form.getKabusu1())){
			rs = Function.matches(form.getKabusu1(),KABUSU_PATTERN);
			yousoNm = KABUSU1;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKabusu2())){
			rs = Function.matches(form.getKabusu2(),KABUSU_PATTERN);
			yousoNm = KABUSU2;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKabusu3())){
			rs = Function.matches(form.getKabusu3(),KABUSU_PATTERN);
			yousoNm = KABUSU3;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKabusu4())){
			rs = Function.matches(form.getKabusu4(),KABUSU_PATTERN);
			yousoNm = KABUSU4;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKabusu5())){
			rs = Function.matches(form.getKabusu5(),KABUSU_PATTERN);
			yousoNm = KABUSU5;
		}
		if(rs == FALSE){
			appContext.setMsgCode(msgList);
			appContext.setFocusField(yousoNm);
			//要注意!!
			return rs;
		}

    	//保有比率チェック
		msgList = null;
		msgList = new ArrayList<String>();
    	msgList.add(GL.ERR_DIGITS);
    	msgList.add(GL.OC1102_HOYUU_RITU);
    	msgList.add(GL.REPLACE_3);
    	msgList.add(GL.REPLACE_2);
		if(!GS.EMPTY_CHARCTER.equals(form.getHiritu1())){
			rs = Function.matches(form.getHiritu1(),HIRITU_PATTERN);
			yousoNm = HIRITU1;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getHiritu2())){
			rs = Function.matches(form.getHiritu2(),HIRITU_PATTERN);
			yousoNm = HIRITU2;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getHiritu3())){
			rs = Function.matches(form.getHiritu3(),HIRITU_PATTERN);
			yousoNm = HIRITU3;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getHiritu4())){
			rs = Function.matches(form.getHiritu4(),HIRITU_PATTERN);
			yousoNm = HIRITU4;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getHiritu5())){
			rs = Function.matches(form.getHiritu5(),HIRITU_PATTERN);
			yousoNm = HIRITU5;
		}
		if(rs == FALSE){
			appContext.setMsgCode(msgList);
			appContext.setFocusField(yousoNm);
		}
    	return rs;
    }

    /**
     * 【入力バイトチェック処理】 <br>
     * @return boolean
     */
    private boolean isByteChk() throws Exception{
    	
    	boolean rs = FALSE;
		
		if(!(check.islength(form.getKabunusiNm1(),60))){
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1102_KABUNUSHI_NM);
			appContext.setFocusField(KABUNUSI_NM1);
		}else if(!(check.islength(form.getKabunusiNm2(),60))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1102_KABUNUSHI_NM);
			appContext.setFocusField(KABUNUSI_NM2);
		}else if(!(check.islength(form.getKabunusiNm3(),60))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1102_KABUNUSHI_NM);
			appContext.setFocusField(KABUNUSI_NM3);
		}else if(!(check.islength(form.getKabunusiNm4(),60))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1102_KABUNUSHI_NM);
			appContext.setFocusField(KABUNUSI_NM4);
		}else if(!(check.islength(form.getKabunusiNm5(),60))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1102_KABUNUSHI_NM);
			appContext.setFocusField(KABUNUSI_NM5);
		}else if(!(check.islength(form.getJigyoNaiyo(),80))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1102_JIGYO);
			appContext.setFocusField(JIGYONAIYO);
		}else if(!(check.islength(form.getKesanGaikyo(),1000))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1102_KESAN_GAIKYO);
			appContext.setFocusField(KESANGAIKYO);
		}else{
			rs = TRUE;
		}
		
    	return rs;
    }

    /**
     * 【入力禁止文字入力チェック処理】 <br>
     * @return boolean
     */
    private boolean isKinshiMojiChk() throws Exception{
    	
    	String yousoNm = null;
    	boolean rs = TRUE;
		
		if(!GS.EMPTY_CHARCTER.equals(form.getKabunusiNm1())){
			rs = super.isKinshiChk(form.getKabunusiNm1());
	    	yousoNm = KABUNUSI_NM1;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKabunusiNm2())){
			rs = super.isKinshiChk(form.getKabunusiNm2());
	    	yousoNm = KABUNUSI_NM2;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKabunusiNm3())){
			rs = super.isKinshiChk(form.getKabunusiNm3());
	    	yousoNm = KABUNUSI_NM3;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKabunusiNm4())){
			rs = super.isKinshiChk(form.getKabunusiNm4());
	    	yousoNm = KABUNUSI_NM4;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKabunusiNm5())){
			rs = super.isKinshiChk(form.getKabunusiNm5());
	    	yousoNm = KABUNUSI_NM5;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getJigyoNaiyo())){
			rs = super.isKinshiChk(form.getJigyoNaiyo());
	    	yousoNm = JIGYONAIYO;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKesanGaikyo())){
			rs = super.isKinshiChk(form.getKesanGaikyo());
	    	yousoNm = KESANGAIKYO;
		}
		if(rs == FALSE){
			appContext.setFocusField(yousoNm);
		}
    	return rs;
    }

    /**
	 * 初期表示処理<br>
     * @param AppContext
	 */
	public void execInit(AppContext appContext) throws Exception{
		
		TorokuDbAcc dbacc = new TorokuDbAcc(appContext);
		boolean initFlg = false;
		String kensakuyouAnkenNo = GS.EMPTY_CHARCTER;
		String kensakuyouPhase = GS.EMPTY_CHARCTER;
		
		//汎用タイトル取得
		dbacc.getHanyouList();
		
		//業種小分類・所在地取得
		dbacc.getDBkihon(tori_bean);
		
		//財務情報取得
		dbacc.getZaimu(tori_bean);
		
		//初期表示判定
		initFlg = dbacc.isSyokiHyouji(tori_bean,SANSYO_POINT);
		
		//検索用案件No.取得
		kensakuyouAnkenNo = getKensakuyouAnkenNo(dbacc,initFlg);
		if(GS.EMPTY_CHARCTER.equals(kensakuyouAnkenNo)){
			//検索用案件No.取得不可の場合、ここでリターン
			return;
		}else{
			//検索用フェーズ取得
			kensakuyouPhase = getKensakuyouPhase(dbacc,initFlg,kensakuyouAnkenNo);
		}
		
		//査定登録データ取得
		dbacc.getSateiDataOC1102(kensakuyouAnkenNo,kensakuyouPhase,tori_bean.getSystem_kbn());
		
		//コメント取得
		dbacc.getCommentOC1102(kensakuyouAnkenNo,kensakuyouPhase,SANSYO_POINT);
	}

	/**
	 * 保存処理<br>
     * @param boolean
	 */
	public void execHozon(boolean flg) throws Exception{

		//実施業務取得
    	HashMap map = getJishiGyoumu(tori_bean.getPhase(),GS.STATUS_MISYORI);
    	
		TorokuDbAcc dbacc = new TorokuDbAcc(appContext);
		//コメントの登録
		dbacc.deleteComment(tori_bean,SANSYO_POINT);
		dbacc.insertComment(tori_bean,POINT_KESAN,form.getKesanGaikyo());
		//査定内容登録
		dbacc.updateSateiData(tori_bean);
		//進捗更新
		dbacc.setSateiStatSyorityu(tori_bean,map,TOROKU_GAMEN,GS.STATUS_SYORICHU);
		//入力履歴登録
		if(flg){
			dbacc.setNyuryokuHist(tori_bean,NYURYOKU_HOZON,GS.EMPTY_CHARCTER);
		}
		//コミット
		dbacc.commit();
	}
	
	/**
	 * もぎ取り解除処理<br>
     * @param boolean
	 */
	public void execKaijo() throws Exception{

		//実施業務取得
    	HashMap map = getJishiGyoumu(tori_bean.getPhase(),GS.STATUS_MISYORI);
    	
    	TorokuDbAcc dbacc = new TorokuDbAcc(appContext);
    	// 案件No.D9059 もぎ取り解除時に記入内容をクリアしない
		//コメント全て削除
		//dbacc.deleteComment(tori_bean,ALL_POINT);
		//進捗更新
		dbacc.setSateiStatKaijo(tori_bean,map,TOROKU_GAMEN,GS.STATUS_MISYORI);
		//入力履歴登録
		dbacc.setNyuryokuHist(tori_bean,NYURYOKU_KAIJO,GS.EMPTY_CHARCTER);
		//コミット
		dbacc.commit();
	}
}
