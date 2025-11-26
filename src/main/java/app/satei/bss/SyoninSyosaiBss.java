/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/20		SSC				課題No.60 引当金確認案件の取引先区分・債権区分設定 
******************************************************************************/
package app.satei.bss;

import app.satei.dbAcc.SateiDbAcc;
import common.AppContext;
import common.global.GS;
import common.util.Function;

import java.util.HashMap;
import java.util.Map;

/**
 * OC1107_査定_承認 ビジネス ロジッククラス <br>
 */
public class SyoninSyosaiBss extends SateiBss{

	private static final String NYURYOKU_KBN		= "80";		//入力区分：承認
	private static final String SAIKEN_KBN		= "saiken_kbn";
	private static final String TORIHIKISAKI_KBN	= "torihikisaki_kbn";
    protected static final String KANRYOU_FLG		= "1";		//完了フラグ
    
	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public SyoninSyosaiBss(AppContext appContext) throws Exception {
		super(appContext);
	}
	
    /**
     * 【承認実行ボタン押し処理】 <br>
     * @throws Exception
     */
    public void doSyonin() throws Exception {
    	
        //コネクションの取得
        SateiDbAcc dbacc = new SateiDbAcc(appContext);
        
        //更新ユーザ判定
		String upd_user_id = user_bean.getComUserId();
		if(!GS.EMPTY_CHARCTER.equals(Function.trim(user_bean.getComDaiko_userId()))){
			upd_user_id = Function.trim(user_bean.getComDaiko_userId());
		}

		//実施業務取得
    	HashMap map = getJishiGyoumu(tori_bean.getPhase(),GS.STATUS_SYONIN_MACHI);
    	
    	//更新処理
		if(!(((String)map.get(GS.JISHI_PHASE_KANRYO_FLG)).equals(KANRYOU_FLG)) && ((String)map.get(GS.JI_KAISHI_STATUS)).equals(GS.STATUS_MISYORI)){
    		dbacc.setSateiData(tori_bean,map);
    		dbacc.setRyuhosaimu(tori_bean,map);
    		dbacc.setOthRyuhosaimu(tori_bean,map);
    		dbacc.setMailHaishin(upd_user_id,GS.EMPTY_CHARCTER,tori_bean,map);
    	//課題No.60
    	//追加開始
		}else if(((String)map.get(GS.JISHI_PHASE_KANRYO_FLG)).equals(KANRYOU_FLG) && !(GS.GSS.equals(tori_bean.getSystem_kbn()))){
    		//既に引当金確認が開始されている場合、引当金確認案件の取引先区分と案件No.を更新
    		String hikiateKakuninAnkenNo = dbacc.getHikiateKakuninAnkenNo(tori_bean);
    		if(!(GS.EMPTY_CHARCTER.equals(hikiateKakuninAnkenNo))){
        		Map<String,String> kbnMap = dbacc.getSaikenToriKbn(tori_bean);
        		dbacc.setHikiateKakuninSaikenToriKbn(hikiateKakuninAnkenNo,kbnMap.get(SAIKEN_KBN),kbnMap.get(TORIHIKISAKI_KBN));
    		}
        //追加完了
		}
		dbacc.setSateiStat(tori_bean,map,GS.EMPTY_CHARCTER);
		dbacc.setNyuryokuHist(tori_bean,NYURYOKU_KBN,GS.EMPTY_CHARCTER);

		//コミット
        dbacc.commit();
    }
}
