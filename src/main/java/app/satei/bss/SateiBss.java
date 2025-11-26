/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.satei.dbAcc.SateiDbAcc;
import common.AppContext;
import common.global.GL;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;

import java.util.HashMap;
import java.util.List;

/**
 * 査定 ビジネス ロジック親クラス <br>
 */
public class SateiBss {

	protected AppContext appContext		= null;			//APPコンテキスト
	protected SessionData cmnData			= null;			//共通セッション
	protected UserBean user_bean			= null;			//ユーザー情報Bean
	protected TorihikisakiBean tori_bean	= null;			//取引先情報Bean

	protected static final boolean TRUE		= true;
	protected static final boolean FALSE		= false;

	//入力チェック用クラス
	protected InputCheck check = null;
	
    private static final String NUM_FMT_KOKUNAI 	= "##,###,###,###,###,##0.##";	//フォーマット：国内
    private static final String NUM_FMT_KAIGAI  	= "##,###,###,###,###,##0.00";	//フォーマット：海外
    private static final String NUM_FMT_KOKUNAI_NOTKANMA 		= "################0.##";	//フォーマット：国内
    private static final String NUM_FMT_KAIGAI_NOTKANMA  		= "################0.00";	//フォーマット：海外

    /**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public SateiBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		this.tori_bean = cmnData.getTori_bean();
		this.check = new InputCheck();
	}
    /**
     * 【入力禁止文字チェックループ処理】 <br>
     * @param String
     * @return boolean
     */
    protected boolean isKinshiChk(String comment) throws Exception{
    	
    	boolean rs = TRUE;
		String kinshiChar = GS.EMPTY_CHARCTER;
		
		for (int i = 0; i < comment.length(); i++) {
			kinshiChar = comment.substring(i,i + 1);
			if(check.haveKinshiMoji(kinshiChar)) {
				//チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示
				appContext.setMsgCd(GL.ERR_PROHIBITTED,kinshiChar);
		    	rs = FALSE;
				break;
			}
		}
		return rs;
    }
    /**
	 * 検索用案件No.取得処理<br>
     * @param SateiDbAcc
     * @param boolean
     * @return String
	 */
	protected String getKensakuyouAnkenNo(SateiDbAcc dbacc,boolean initFlg) throws Exception{
		String lastAnkenNo = GS.EMPTY_CHARCTER;
		if(!initFlg){
			//初期表示でない場合、今回案件No.をリターン
			return tori_bean.getAnken_no();
		}else if(!GS.PHASE_ICHIJI_SATEI.equals(tori_bean.getPhase())){
			//一次査定でない場合、今回案件No.をリターン
			return tori_bean.getAnken_no();
		}else{
			//初期表示の場合
			if(GS.EMPTY_CHARCTER.equals(Function.trim(tori_bean.getLast_anken_no()))){
				//共通セッションに前回実施案件No.が存在しないのでDBアクセス
				lastAnkenNo = Function.trim(dbacc.getZenAnkenNo(tori_bean));
				tori_bean.setLast_anken_no(lastAnkenNo);
				return lastAnkenNo;
			}else{
				//存在するのでリターン
				return Function.trim(tori_bean.getLast_anken_no());
			}
		}
	}
	/**
	 * 検索用案件No.のフェーズ取得処理<br>
     * @param TorokuDbAcc
     * @param boolean
     * @param String
     * @return String
	 */
	protected String getKensakuyouPhase(SateiDbAcc dbacc,boolean initFlg,String kensakuyouAnkenNo) throws Exception{
		if(!initFlg){
			//初期表示でない場合、今回案件No.のフェーズをリターン
			return tori_bean.getPhase();
		}else if(!GS.PHASE_ICHIJI_SATEI.equals(tori_bean.getPhase())){
			//一次査定でない場合、今回案件No.の前フェーズをリターン
			return dbacc.getZenPhase(tori_bean.getAnken_no(),tori_bean.getPhase());
		}else{
			//初期表示の場合、前回案件No.のフェーズをリターン
			return dbacc.getPhase(kensakuyouAnkenNo);
		}
	}
	/**
	 * 実施業務取得<br>
     * @param String
     * @return HashMap
	 */
	protected HashMap getJishiGyoumu(String phase,String stat){
		List<HashMap> list = user_bean.getComOparation();
		HashMap map = null;
		for(int i=0;i<list.size();i++){
			map = list.get(i);
			if(phase.equals((String)map.get(GS.JISHI_PHASE)) && stat.equals((String)map.get(GS.KAISHI_STATUS))){
				break;
			}
		}
		return map;
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
