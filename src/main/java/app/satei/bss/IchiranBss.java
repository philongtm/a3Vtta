/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002     2012/07/25      SSC             案件No.D10907_排他制御不具合障害対応
******************************************************************************/
package app.satei.bss;

import app.TorihikisakiBean;
import app.satei.dbAcc.IchiranDbAcc;
import app.satei.form.IchiranForm;
import common.AppContext;
import common.global.GL;
import common.global.GS;
import common.util.Function;

import java.sql.SQLException;
import java.util.List;

/**
 * OC1101_査定_対象先一覧 ビジネス ロジッククラス <br>
 */
public class IchiranBss extends SateiBss{

	private IchiranForm form		= null;							//アクションフォーム
    
    private static final String CHECKBOX_STATUS_ON		= "1";		//チェックボックスのステータス:オン
	private static final String NYURYOKU_KBN				= "30";		//入力区分：もぎ取り
	private static final String ITIJISATEI_SANSYO_FLG		= "1";
	private static final String NIJISATEI_SANSYO_FLG		= "2";
    
	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public IchiranBss(AppContext appContext) throws Exception {
		super(appContext);
		this.form = (IchiranForm) appContext.getActionForm();
	}
	
	/**
	 * 画面初期表示処理<br>
	 * 
	 * @throws Exception
	 */
	public void executeInit() throws Exception {

		//DBアクセスクラス生成
		IchiranDbAcc dbacc = new IchiranDbAcc(appContext);

		//査定期セレクトボックス値取得
		dbacc.getSateiki();

		//表示件数セレクトボックス値取得
		dbacc.getShow();

		//ソートセレクトボックス値取得
		dbacc.getSort();

        //ユーザの参照フェーズ設定
		this.setSansyoPhase();

		//自担当分/汎用２ラジオボタン初期判定
		dbacc.getInitTanto();

        //取引先一覧取得
        dbacc.getMeisai();

        //進捗表取得
        dbacc.getStat();

        //一次査定・一次査定検証の登録内容差異判定
		if(cmnData.getSateiSansyoFlg().equals(NIJISATEI_SANSYO_FLG)){
        	this.isSaiFlg(dbacc);
        }
	}

	/**
	 * 参照フェーズ設定<br>
	 */
	private void setSansyoPhase(){
		StringBuffer sansyo_phase = new StringBuffer();
		if(cmnData.getSateiSansyoFlg().equals(ITIJISATEI_SANSYO_FLG)){
			if (user_bean.getComIchiji_satei_t_flg().equals(GS.ON) && user_bean.getComIchiji_sateikensyo_t_flg().equals(GS.ON)){
				//一次査定・一次査定検証
				sansyo_phase.append(GS.SINGLE_QUOTATION)
							.append(GS.PHASE_ICHIJI_SATEI)
							.append(GS.SINGLE_QUOTATION)
							.append(GS.COMMA)
							.append(GS.SINGLE_QUOTATION)
							.append(GS.PHASE_ICHIJI_SATEI_KENSYO)
							.append(GS.SINGLE_QUOTATION);
			}else if(user_bean.getComIchiji_satei_t_flg().equals(GS.ON)){
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
	 * もぎ取りチェックボックスチェックのチェック<br>
	 */
	public boolean isMogitoriCheckBox(){

		//取引先一覧を取得
        List<TorihikisakiBean> list = form.getAr_meisai();
        
        boolean mogitori_flg = false;
        if(list != null){
            for(int i=0;i<list.size();i++){       
                if ((CHECKBOX_STATUS_ON).equals(list.get(i).getMogitori_chk())){
                	mogitori_flg = true;
                    break;
                }
            }
        }
		return mogitori_flg;
	}

	/**
	 * 更新・登録処理<br>
     * @param dbacc
     * @param list
     * @throws Exception
	 */
	private void execMogitori(IchiranDbAcc dbacc) throws Exception{

        //取引先一覧を取得
        List<TorihikisakiBean> list = form.getAr_meisai();
        
        if(list != null){
    		for(int i=0;i<list.size();i++){
            	TorihikisakiBean toriBean = list.get(i);
                //もぎ取りチェックボックスオフの場合次のデータへ
                if(!((CHECKBOX_STATUS_ON).equals(list.get(i).getMogitori_chk()))){
                    continue;
                }
                //更新登録処理
                dbacc.setSateiStat(toriBean);
    	        dbacc.setNyuryokuHist(toriBean,NYURYOKU_KBN,GS.EMPTY_CHARCTER);
            }
            //コミット
            dbacc.commit();
        }
	}

	/**
     * 画面初期表示値取得(メニューリンク以外から遷移時) <br>
     * @throws Exception
     */
    public void execute() throws Exception {  
		
		//DBアクセスクラス生成
    	IchiranDbAcc dbacc = new IchiranDbAcc(appContext);
		
        //取引先一覧取得
        dbacc.getMeisai();
        
        //進捗表取得
        dbacc.getStat();

        //一次査定・一次査定検証の登録内容差異判定
		if(cmnData.getSateiSansyoFlg().equals(NIJISATEI_SANSYO_FLG)){
        	this.isSaiFlg(dbacc);
        }
    }
    
    /**
     * 【一括もぎ取り処理】 <br>
     * @throws Exception
     */
    public void doAllMogitori() throws Exception {
    	
        //コネクションの取得
        IchiranDbAcc dbacc = new IchiranDbAcc(appContext);
        
        //もぎ取りチェックボックスがオンの取引先が、既にもぎ取られた場合、エラーダイアログを表示し処理終了
        if (!this.isMogitoriCheck(dbacc)){
            return;
        }

        //もぎ取りチェックボックスがオンの取引先に対しもぎ取り処理実行
        this.execMogitori(dbacc);

        //コミット
        dbacc.commit();
        
        //一覧情報再表示
        dbacc.getMeisai();

        //進捗表取得
        dbacc.getStat();

        //一次査定・一次査定検証の登録内容差異判定
		if(cmnData.getSateiSansyoFlg().equals(NIJISATEI_SANSYO_FLG)){
        	this.isSaiFlg(dbacc);
        }
    }

	/**
	 * 一次査定と一次査定検証の登録内容差異判定 <br>
	 * 
	 * @exception SQLException
	 */
	private void isSaiFlg(IchiranDbAcc dbacc) throws SQLException {

		//取引先情報一覧取得
		List<TorihikisakiBean> list = form.getAr_meisai();
		TorihikisakiBean toriBean = null;
        if(list != null){
    		for(int i = 0;i < list.size();i++){
    			toriBean = (TorihikisakiBean)list.get(i);
    			dbacc.setSaiFlg(toriBean);
    			toriBean = null;
    		}
        }
	}

    /**
     * 【もぎ取り処理】 <br>
     * @throws Exception
     */
    public boolean doMogitori(TorihikisakiBean toriBean) throws Exception {
    	
        //コネクションの取得
        IchiranDbAcc dbacc = new IchiranDbAcc(appContext);
        
        //既にもぎ取られた場合、エラーダイアログを表示し処理終了
        //判定処理
        //if(toriBean.getHoji_user_id().equals(Function.trim(user_bean.getComUserId()))){
        	if(!dbacc.isMogitoriCheck(toriBean)){
            	appContext.setMsgCd(GL.ERR_TORITAKEN,toriBean.getKanjo_cd());
				return false;
			}
        //}

        //更新登録処理
        dbacc.setSateiStat(toriBean);
        if(!(toriBean.getHoji_user_id().equals(Function.trim(user_bean.getComUserId())))){
        	dbacc.setNyuryokuHist(toriBean,NYURYOKU_KBN,GS.EMPTY_CHARCTER);
        }
        
        //コミット
        dbacc.commit();
        
		return true;
    }

    /**
	 * もぎ取られていないか判定<br>
	 */
	private boolean isMogitoriCheck(IchiranDbAcc dbacc) throws Exception{

        //取引先一覧を取得
        List<TorihikisakiBean> list = form.getAr_meisai();
        
		boolean mogitori_flg = true;
        if(list != null){
    		for(int i=0;i<list.size();i++){
            	TorihikisakiBean toriBean = list.get(i);
                //もぎ取りチェックボックスオフの場合次のデータへ
                if(!((CHECKBOX_STATUS_ON).equals(list.get(i).getMogitori_chk()))){
                    continue;
                }
                //判定処理
        		if(!dbacc.isMogitoriCheck(toriBean)){
                    appContext.setMsgCd(GL.ERR_TORITAKEN,toriBean.getKanjo_cd());
                    mogitori_flg = false;
                    break;
        		}
            }
        }
		return mogitori_flg;
	}
 }
