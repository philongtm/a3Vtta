/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/12/21		SSC				課題No.214 初期表示タブ修正
003		2014/10/01		SSC				BP201411002 査定照会画面からの差戻し処理対応（恒久対応）
004		2015/08/11		SSC				BP201508005 査定内容詳細画面の差戻不正対応
******************************************************************************/

package app.syokai.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.syokai.dbAcc.SateisyosaiDbAcc;
import app.syokai.form.SateisyosaiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;
import org.apache.commons.beanutils.BeanUtils;

/**
 *  OS6102_査定内容詳細 <br>
 */
public class SateisyosaiBss {
	
	private AppContext appContext = null;                  		// ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;                     		// ＤＢアクセス
    private Log log = null;                                 		// LOG
    private SessionData cmnData = null;							// 機能共通セッション
    private UserBean user_bean = null;								// ユーザー情報
    private TorihikisakiBean tori_bean = null;						// 取引先情報
    private SateisyosaiForm form = null;                    		// アクションフォーム

    private static final String SYSTEM_MANAGER 			= "1";				// システム管理者
	private static final String HIHYOUJI 					= "1";				// コメント非表示フラグ
	private static final String SINCHOKU_HANTEI 			= "1";				// 進捗区分：滞留判定
    
    private static final int TAB_TORIHIKI_GAIYO 			= 1;				// 取引先概要タブ
    private static final int TAB_SAIKEN_MEISAI 			= 4;				// 債権明細タブ
    private static final int TAB_TAIRYU_SAIKEN 			= 6;				// 滞留債権明細タブ
    private static final int TAB_HIKIATE_KAKUNIN 		= 7;				// 引当金確認タブ
    private static final int TAB_HIKIATE_KENSYO 			= 8;				// 引当金検証タブ
    
    /**
     * コンストラクタ
     * 
     * @param appContext AppContext
     * @throws Exception 
     */
    public SateisyosaiBss(AppContext appContext) throws Exception {
        this.appContext = appContext;    
        this.log = appContext.getLog();
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        tori_bean = cmnData.getTori_bean();
        form = (SateisyosaiForm)appContext.getActionForm();
    }
	
    /**
     * 画面初期表示値取得
     * 
     * @throws Exception 
     */
    public void executeInit() throws Exception {

        // フェーズセレクトボックスの表示
        this.dispPhase();
        // 共)取引先情報.フェーズを参照し、表示タブを確定する。
        this.initTabLink();
        // 差戻ボタンの表示
        this.dispBtnSasi();
        // ダウンロードボタンの表示
        this.dispBtnDownLoad();
        // コメント表示リンクの表示
        this.dispLinkComment();
        // 登録担当者の表示
        this.dispTanto();
    }
    
    /**
     * 差戻ボタンの表示 <br>
     * 
     */
    private void dispBtnSasi() throws Exception{
    	
        //課題No.197
        //追加開始
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SateisyosaiDbAcc dbacc = new SateisyosaiDbAcc(sqlExec, log, appContext);
        //追加完了

        // 差戻ボタンを非表示にする。
    	form.setBtn_sasimodo_flg(GS.OFF);
    	// 共)ユーザ情報.システム管理者フラグが｢1：システム管理者｣
    	if (!SYSTEM_MANAGER.equals(user_bean.getComSystemManager_flg())) {
    		return;
    	}
    	// 共)遷移元画面IDが｢OS6101：査定内容照会｣
    	if (!GS.OS6101.equals(cmnData.getReturn_gamenId())) {
    		return;
    	}
    	// フェーズセレクトボックス選択値が査定フェーズ(40：一次査定,50：一次査定検証,60：二次査定,70：引当金検証,80：引当金確認)に
    	// 該当するフェーズのMAXフェーズ TODO
    	// 共)取引先情報.ステータスが｢40：完了｣の場合
		if (!GS.STATUS_KANRYO.equals(tori_bean.getStatus())
				|| !(GS.PHASE_NIJI_SATEI.equals(tori_bean.getPhase())
						|| GS.PHASE_HIKIATEKIN_KENSYO.equals(tori_bean.getPhase())
						|| GS.PHASE_HIKIATEKIN_KAKUNIN.equals(tori_bean.getPhase()))) {
    		return;
    	}
    	
        //課題No.197
        //追加開始
    	if(GS.PHASE_TAIRYU_HANTEI.equals(tori_bean.getPhase()) || GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(tori_bean.getPhase())){
        	if(!dbacc.getSateiCheck()){
        		return;
        	}
    	}
        //追加完了
    	
    	// 差戻ボタンを表示する。
    	form.setBtn_sasimodo_flg(GS.ON);
    }
    
    /**
     * ダウンロードボタンの表示 <br>
     * 
     */
    private void dispBtnDownLoad() {
    	// 遷移元画面が査定内容照会の場合
    	if (GS.OS6101.equals(cmnData.getReturn_gamenId())) {
    		form.setBtn_download_flg(GS.ON);
    		return;
    	} else {
    		if (form.getCurrentTab() == TAB_SAIKEN_MEISAI) {
    			form.setBtn_download_flg(GS.ON);
        		return;
    		}
    	}
    	form.setBtn_download_flg(GS.OFF);
    }
    
    /**
     * コメント表示リンクの表示 <br>
     * 
     */
    private void dispLinkComment() throws Exception {
    	// コメント表示リンクを非表示にする。
    	form.setLink_comment_flg(GS.OFF);
    	// 共)遷移元画面IDが｢OS6101：査定内容照会｣の場合
    	if (GS.OS6101.equals(cmnData.getReturn_gamenId())) {
            // コネクションの取得
            this.sqlExec = appContext.getSqlExecuter();
            SateisyosaiDbAcc dbacc = new SateisyosaiDbAcc(sqlExec, log, appContext);
    		if (HIHYOUJI.equals(dbacc.getComentHihyoujiFlg())) {
    	    	// コメント表示リンクを表示する。
        		form.setLink_comment_flg(GS.ON);
    		}
    	}
    }
    
    /**
     * フェーズセレクトボックスの表示 <br>
     * 
     */
    private void dispPhase() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SateisyosaiDbAcc dbacc = new SateisyosaiDbAcc(sqlExec, log, appContext);
    	
    	if (GS.OS6101.equals(cmnData.getReturn_gamenId())) {
        	// 共)遷移元画面IDが｢OS6101：査定内容照会｣の場合
    		
    		// 共)取引先情報.案件No.を検索用案件No.設定
    		form.setKensaku_anken_no(tori_bean.getAnken_no());
    		// 検索用案件No.を元にフェーズセレクトボックス設定値を取得する。
    		if (SINCHOKU_HANTEI.equals(tori_bean.getSincyoku_kbn())){
    			// 滞留判定検索時
        		dbacc.getPhase_ts();
    		} else {
    			// 査定検索時
        		dbacc.getPhase_th();
    		}
    		// フェーズセレクトボックスの初期値として、機)案件フェーズに共)取引先情報.案件No.と共)取引先情報.フェーズをカンマで区切った値を設定する。
    		form.setAnken_phase(tori_bean.getAnken_no() + GS.COMMA + tori_bean.getPhase());
    	} else if (GS.OD1102.equals(cmnData.getReturn_gamenId()) || GS.OD1105.equals(cmnData.getReturn_gamenId())) {
    		// 共)遷移元画面IDが｢OD1102：引当金確認｣｢引当金検証｣の場合
    		
    		// 共)取引先情報を機)取引先情報退避に退避する。
    		TorihikisakiBean toriBeanTaihi = new TorihikisakiBean();
    		BeanUtils.copyProperties(toriBeanTaihi, tori_bean);
    		form.setTori_taihi(toriBeanTaihi);
    		// 検索用案件No.を取得する。
    		dbacc.getAnken_no_hiki();
    		// 検索用案件No.を元にフェーズセレクトボックス設定値を取得する。
    		dbacc.getPhase_th();
    		// 機)案件フェーズを用いて取引先情報を取得し、共)取引先情報に設定する。
    		if (SINCHOKU_HANTEI.equals(tori_bean.getSincyoku_kbn())){
    			// 滞留判定検索時
        		dbacc.getToriInfoTairyu();
    		} else {
    			// 査定検索時
        		dbacc.getToriInfoSatei();
    		}
    		// 取引先情報を更新する。
    		tori_bean = cmnData.getTori_bean();
    	} else {
    		
    		// それ以外の場合
    		// 共)取引先情報.案件No.を元にフェーズセレクトボックス設定値を取得する。
    		dbacc.getPhase_read();
    		// フェーズセレクトボックスを読取専用で表示する。
    		form.setDisp_phase_flg(GS.OFF);
    	}
    }
    
    /**
     * 登録担当者の表示 <br>
     * 
     */
    private void dispTanto() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SateisyosaiDbAcc dbacc = new SateisyosaiDbAcc(sqlExec, log, appContext);
        // 共)取引先情報.進捗区分を参照し、T13_入力履歴より登録/承認担当者一覧を取得する。
        dbacc.getTantoList();
    }
    
    /**
     * 共)取引先情報.フェーズを参照し、表示タブを確定する。 <br>
     * 
     */
    private void initTabLink() {
    	// タブ表示状態を初期化する。
        form.setDispTabTorihikiGaiyo(GS.OFF);
        form.setDispTabSaikenKubun(GS.OFF);
        form.setDispTabHikiateHantei(GS.OFF);
        form.setDispTabSaikenMeisai(GS.OFF);
        form.setDispTabRyuhoSaimu(GS.OFF);
        form.setDispTabTairyuSaiken(GS.OFF);
        form.setDispTabHikiateKakunin(GS.OFF);
        form.setDispTabHikiateKensyo(GS.OFF);
    	
    	if (GS.OS6101.equals(cmnData.getReturn_gamenId())) {
    		// 遷移元画面が査定内容照会の場合
    		if (GS.PHASE_TAIRYU_HANTEI.equals(tori_bean.getPhase())
    				|| GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(tori_bean.getPhase())) {
    			// 滞留判定
    			
    			// 滞留債権明細タブを表示する。
    			form.setDispTabTairyuSaiken(GS.ON);
    			// カレントタブをセットする。
    			form.setCurrentTab(TAB_TAIRYU_SAIKEN);
    		} else if (GS.PHASE_ICHIJI_SATEI.equals(tori_bean.getPhase())
					|| GS.PHASE_TAISHOSAKI_SENTEI.equals(tori_bean.getPhase())
					|| GS.PHASE_KARIKIJUN_SATEI_TUIKA.equals(tori_bean.getPhase())
					|| GS.PHASE_KUREMU_SAIKEN_SAISETTEI.equals(tori_bean.getPhase())
					|| GS.PHASE_ICHIJI_SATEI_KENSYO.equals(tori_bean.getPhase())
					|| GS.PHASE_NIJI_SATEI.equals(tori_bean.getPhase())) {
    			// 査定
    			
    			// 取引先概要タブ
    			form.setDispTabTorihikiGaiyo(GS.ON);
    			// 取引先・債権区分判定タブ
    			form.setDispTabSaikenKubun(GS.ON);
    			// 引当金判定タブ
    			form.setDispTabHikiateHantei(GS.ON);
    			// 債権明細タブ
    			form.setDispTabSaikenMeisai(GS.ON);
    			// 留保債務タブ
    			form.setDispTabRyuhoSaimu(GS.ON);
    			// カレントタブをセットする。
    			form.setCurrentTab(TAB_TORIHIKI_GAIYO);
    		} else if (GS.PHASE_HIKIATEKIN_KAKUNIN.equals(tori_bean.getPhase())) {
    			// 引当金確認(海外)
    			
    			// 引当金確認タブ
    			form.setDispTabHikiateKakunin(GS.ON);
    			// カレントタブをセットする。
    			form.setCurrentTab(TAB_HIKIATE_KAKUNIN);
    		} else if (GS.PHASE_HIKIATEKIN_KENSYO.equals(tori_bean.getPhase())) {
    			// 引当金検証(国内)
    			
    			// 引当金検証タブ
    			form.setDispTabHikiateKensyo(GS.ON);
    			// カレントタブをセットする。
    			form.setCurrentTab(TAB_HIKIATE_KENSYO);
    		}
    	} else {
    		// 遷移元画面が査定内容照会以外の場合
    		
			// 取引先概要タブ
			form.setDispTabTorihikiGaiyo(GS.ON);
			// 取引先・債権区分判定タブ
			form.setDispTabSaikenKubun(GS.ON);
			// 引当金判定タブ
			form.setDispTabHikiateHantei(GS.ON);
			// 債権明細タブ
			form.setDispTabSaikenMeisai(GS.ON);
			// 留保債務タブ
			form.setDispTabRyuhoSaimu(GS.ON);
			// カレントタブをセットする。
			form.setCurrentTab(TAB_SAIKEN_MEISAI);    		
    	}
    }
	
    /**
     * タブ選択処理
     * 
     * @throws Exception
     */
    public void changeTab() throws Exception {
        // ダウンロードボタンの表示
        this.dispBtnDownLoad();
    }
	
    /**
     * フェーズ選択処理
     * 
     * @throws Exception
     */
    public void changePhase() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SateisyosaiDbAcc dbacc = new SateisyosaiDbAcc(sqlExec, log, appContext);
        
    	// 機)案件フェーズを用いて取引先情報を取得し、共)取引先情報に設定する。
		if (SINCHOKU_HANTEI.equals(tori_bean.getSincyoku_kbn())){
			// 滞留判定検索時
    		dbacc.getToriInfoTairyu();
		} else {
			// 査定検索時
    		dbacc.getToriInfoSatei();
		}
		// 取引先情報を更新する。
		tori_bean = cmnData.getTori_bean();
		// 共)取引先情報.進捗区分を参照し、T13_入力履歴より登録/承認担当者一覧を取得する。
		dbacc.getTantoList();
		// 差戻ボタンを表示する。
		this.dispBtnSasi();
		// コメントリンクの表示
		this.dispLinkComment();
		
    }
}
