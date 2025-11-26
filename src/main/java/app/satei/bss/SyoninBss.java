/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/20		SSC				課題No.60 引当金確認案件の取引先区分・債権区分設定 
******************************************************************************/
package app.satei.bss;

import app.TorihikisakiBean;
import app.satei.dbAcc.SyoninDbAcc;
import app.satei.form.SyoninForm;
import common.AppContext;
import common.global.GS;
import common.util.Function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OC1106_査定_承認一覧 ビジネス ロジッククラス <br>
 */
public class SyoninBss extends SateiBss{

	private SyoninForm form		= null;							//アクションフォーム
    
	//課題No.60
	//追加開始
	private static final String SAIKEN_KBN				= "saiken_kbn";
	private static final String TORIHIKISAKI_KBN			= "torihikisaki_kbn";
	//追加完了
    private static final String CHECKBOX_STATUS_ON		= "1";		//チェックボックスのステータス:オン
	private static final String NYURYOKU_KBN				= "80";		//入力区分：承認
	private static final String ITIJISATEI_SANSYO_FLG		= "1";
    protected static final String KANRYOU_FLG				= "1";		//完了フラグ
	
	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public SyoninBss(AppContext appContext) throws Exception {
		super(appContext);
		this.form = (SyoninForm) appContext.getActionForm();
	}
	
	/**
	 * 画面初期表示処理<br>
	 * 
	 * @throws Exception
	 */
	public void executeInit() throws Exception {

		//DBアクセスクラス生成
		SyoninDbAcc dbacc = new SyoninDbAcc(appContext);

		//表示件数セレクトボックス値取得
		dbacc.getShow();

        //ユーザの参照フェーズ設定
		setSansyoPhase();

        //取引先一覧取得
        dbacc.getMeisai();        

        //T14_査定進捗管理の更新(取戻不可設定)
        dbacc.setTorimodoshiFukaFlg();

        //コミット
        dbacc.commit();
	}

	/**
	 * 参照フェーズ設定<br>
	 */
	private void setSansyoPhase(){
		StringBuffer sansyo_phase = new StringBuffer();
		
		if(cmnData.getSateiSansyoFlg().equals(ITIJISATEI_SANSYO_FLG)){
			if (user_bean.getComIchiji_satei_s_flg().equals(GS.ON) && user_bean.getComIchiji_sateikensyo_s_flg().equals(GS.ON)){
				//一次査定・一次査定検証
				sansyo_phase.append(GS.SINGLE_QUOTATION)
							.append(GS.PHASE_ICHIJI_SATEI)
							.append(GS.SINGLE_QUOTATION)
							.append(GS.COMMA)
							.append(GS.SINGLE_QUOTATION)
							.append(GS.PHASE_ICHIJI_SATEI_KENSYO)
							.append(GS.SINGLE_QUOTATION);
			}else if(user_bean.getComIchiji_satei_s_flg().equals(GS.ON)){
				//一次査定
				sansyo_phase.append(GS.SINGLE_QUOTATION)
							.append(GS.PHASE_ICHIJI_SATEI)
							.append(GS.SINGLE_QUOTATION);
			}else{
				//一次査定検証
				sansyo_phase.append(GS.SINGLE_QUOTATION)
							.append(GS.PHASE_ICHIJI_SATEI_KENSYO)
							.append(GS.SINGLE_QUOTATION);
			}
		}else{	
			//二次査定
			sansyo_phase.append(GS.SINGLE_QUOTATION)
						.append(GS.PHASE_NIJI_SATEI)
						.append(GS.SINGLE_QUOTATION);
		}
        form.setSansyo_phase(sansyo_phase.toString());
	}
	/**
	 * 承認チェックボックスチェック<br>
	 */
	public boolean isSyouninCheckBox(){

		//取引先一覧を取得
        List<TorihikisakiBean> list = form.getAr_meisai();
        
        boolean syonin_flg = false;
        if (list != null) {
            for(int i=0;i<list.size();i++){       
                if ((CHECKBOX_STATUS_ON).equals(list.get(i).getSyonin_chk())){
                    syonin_flg = true;
                    break;
                }
            }
        }
		return syonin_flg;
	}
	/**
	 * 承認処理<br>
     * @param dbacc
     * @param list
     * @throws Exception
	 */
	private void execSyounin(SyoninDbAcc dbacc) throws Exception{
        
        //取引先一覧を取得
        List<TorihikisakiBean> list = form.getAr_meisai();
        
		String upd_user_id = user_bean.getComUserId();
		if(!GS.EMPTY_CHARCTER.equals(Function.trim(user_bean.getComDaiko_userId()))){
			upd_user_id = Function.trim(user_bean.getComDaiko_userId());
		}
        if (list != null) {
    		for(int i=0;i<list.size();i++){
            	TorihikisakiBean toriBean = list.get(i);
                //承認チェックボックスオフの場合次のデータへ
                if(!(CHECKBOX_STATUS_ON).equals(list.get(i).getSyonin_chk())){
                    continue;
                }
                //実施業務取得
        		HashMap map = getJishiGyoumu(toriBean.getPhase(),GS.STATUS_SYONIN_MACHI);
        		if(!(((String)map.get(GS.JISHI_PHASE_KANRYO_FLG)).equals(KANRYOU_FLG)) && ((String)map.get(GS.JI_KAISHI_STATUS)).equals(GS.STATUS_MISYORI)){
        			dbacc.setSateiData(toriBean,map);
        			dbacc.setRyuhosaimu(toriBean,map);
        			dbacc.setOthRyuhosaimu(toriBean,map);
        			dbacc.setMailHaishin(upd_user_id,GS.EMPTY_CHARCTER,toriBean,map);
        		//課題No.60
        		//追加開始
        		}else if(((String)map.get(GS.JISHI_PHASE_KANRYO_FLG)).equals(KANRYOU_FLG) && !(GS.GSS.equals(toriBean.getSystem_kbn()))){
            		//既に引当金確認が開始されている場合、引当金確認案件の取引先区分と案件No.を更新
            		String hikiateKakuninAnkenNo = dbacc.getHikiateKakuninAnkenNo(toriBean);
            		if(!(GS.EMPTY_CHARCTER.equals(hikiateKakuninAnkenNo))){
                		Map<String,String> kbnMap = dbacc.getSaikenToriKbn(toriBean);
                		dbacc.setHikiateKakuninSaikenToriKbn(hikiateKakuninAnkenNo,kbnMap.get(SAIKEN_KBN),kbnMap.get(TORIHIKISAKI_KBN));
            		}
            	//追加完了
        		}
    			dbacc.setSateiStat(toriBean,map,GS.EMPTY_CHARCTER);
    			dbacc.setNyuryokuHist(toriBean,NYURYOKU_KBN,GS.EMPTY_CHARCTER);
            }
        }
	}
	/**
     * 画面初期表示値取得(メニューリンク以外から遷移時) <br>
     * @throws Exception
     */
    public void execute() throws Exception {  
		
		//DBアクセスクラス生成
		SyoninDbAcc dbacc = new SyoninDbAcc(appContext);
		
        //取引先一覧取得
        dbacc.getMeisai();        
        
        //T14_査定進捗管理の更新(取戻不可設定)
        dbacc.setTorimodoshiFukaFlg();

        //コミット
        dbacc.commit();
    }
    
    /**
     * 【承認実行ボタン押し処理】 <br>
     * @throws Exception
     */
    public void doSyonin() throws Exception {
    	
        //コネクションの取得
        SyoninDbAcc dbacc = new SyoninDbAcc(appContext);
        
        //承認チェックボックスがオンの取引先に対し承認処理実行
        this.execSyounin(dbacc);
        
        //一覧情報再表示
        dbacc.getMeisai();
        
        //T14_査定進捗管理の更新(取戻不可設定)
        dbacc.setTorimodoshiFukaFlg();

        //コミット
        dbacc.commit();
        
    }
    
    /**
     * 【一括承認チェックボックス実行処理】 <br>
     * 
     * @throws Exception
     */
    public void doIkatuSyonin() throws Exception {

        List<TorihikisakiBean> ar_meisai = form.getList();

        //一括承認チェックボックスの判定
        String syonin = GS.EMPTY_CHARCTER;
        if (form.getIkt_syonin().equals(CHECKBOX_STATUS_ON)) {
            syonin = CHECKBOX_STATUS_ON;
        }
        
        //画面表示分の取引先を全て一括承認チェックボックスと同じにする
        if(ar_meisai != null){
            for(int i=0;i<ar_meisai.size();i++){
                ar_meisai.get(i).setSyonin_chk(syonin);
            }
        }
    }
}
