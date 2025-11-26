/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2014/03/11		SSC				案件No.D13493 改善対応
******************************************************************************/
package app.satei.bss;

import app.satei.dbAcc.KubunDbAcc;
import app.satei.form.KubunForm;
import common.AppContext;
import common.global.GL;
import common.global.GS;
import common.util.Function;

import java.util.HashMap;

/**
 * OC1103_査定_取引先区分判定 ビジネス ロジッククラス <br>
 */
public class KubunBss extends SateiBss{

	private KubunForm form				= null;			//アクションフォーム

	//タブ選択値
	private static final String SEIJO_YOTYUI_TAB		= "1";
	private static final String KASIDAORE_TAB			= "2";
	private static final String HASANKOUSEI_TAB		= "3";
	private static final String SANSYO_POINT = "'20','30','40'";

	//要素名
	private static final String txtValKonkyo		= "txtValKonkyo";
	private static final String txtValJiyuu		= "txtValJiyuu";
	private static final String txtValKeii		= "txtValKeii";
	
	private static final String POINT_KONKYO	= "20";		//判定根拠(登録ポイント20)
	private static final String POINT_JIYU 	= "30";		//判定事由(登録ポイント30)
	private static final String POINT_KEII 	= "40";		//発生経緯(登録ポイント40)
	private static final String TOROKU_GAMEN 	= "2";		//OC1103_取引先区分判定
	private static final String NYURYOKU_KBN 	= "20";		//20: 一次保存
	private static final String KTK_9			= "9";		//9
	private static final String KTK_W			= "W";		//9
	private static final String KTK_T			= "T";		//9
	
	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public KubunBss(AppContext appContext) throws Exception {
		super(appContext);
		this.form = (KubunForm)appContext.getActionForm();
	}
	
    /**
     * 【取引先区分設定処理】 <br>
     */
    public void setToriKbn(){

    	//取引先区分を設定
    	if(KASIDAORE_TAB.equals(form.getCurrentTab())){
    		form.setKbnTorihiki(GS.KASIDAORE_TORIKBN);
    		form.setChkFlgSeijo(GS.OFF);
    		form.setChkFlgYochui(GS.OFF);
			form.setChkFlgHasanho(GS.OFF);
			form.setChkFlgKaishaho(GS.OFF);
			form.setChkFlgKoseho(GS.OFF);
			form.setChkFlgSaiseho(GS.OFF);
			form.setChkFlgShobun(GS.OFF);
			form.setChkFlgSonota(GS.OFF);
    	}else if(HASANKOUSEI_TAB.equals(form.getCurrentTab())){
    		form.setKbnTorihiki(GS.HASANKOUSEI_TORIKBN);
    		form.setChkFlgSeijo(GS.OFF);
    		form.setChkFlgYochui(GS.OFF);
    		form.setChkFlgTyoka(GS.OFF);
    		form.setChkFlgKanwa(GS.OFF);
			form.setChkFlgEntai(GS.OFF);
    	}else if(GS.ON.equals(form.getChkFlgSeijo())){
    		form.setKbnTorihiki(GS.SEIJOUSAKI_TORIKBN);
    		form.setChkFlgTyoka(GS.OFF);
    		form.setChkFlgKanwa(GS.OFF);
			form.setChkFlgEntai(GS.OFF);
			form.setChkFlgHasanho(GS.OFF);
			form.setChkFlgKaishaho(GS.OFF);
			form.setChkFlgKoseho(GS.OFF);
			form.setChkFlgSaiseho(GS.OFF);
			form.setChkFlgShobun(GS.OFF);
			form.setChkFlgSonota(GS.OFF);
    	}else{
    		form.setKbnTorihiki(GS.YOUTYUI_TORIKBN);
    		form.setChkFlgTyoka(GS.OFF);
    		form.setChkFlgKanwa(GS.OFF);
			form.setChkFlgEntai(GS.OFF);
			form.setChkFlgHasanho(GS.OFF);
			form.setChkFlgKaishaho(GS.OFF);
			form.setChkFlgKoseho(GS.OFF);
			form.setChkFlgSaiseho(GS.OFF);
			form.setChkFlgShobun(GS.OFF);
			form.setChkFlgSonota(GS.OFF);
    	}
    }
    
    /**
     * 【入力チェック処理】 <br>
     * @return boolean
     */
    public boolean check() throws Exception{
    	
    	boolean rs = TRUE;

    	if(!this.isRelateTabChk()){
			//タブ関連チェック
        	rs = FALSE;
		}else if(!this.isRelateSaikenChk()){
			//債権区分関連チェック
        	rs = FALSE;
		}else if(!this.isRelateIppanSaikenChk()){
			//債権区分関連チェック
        	rs = FALSE;
		}else if(!this.isInputCommentByteChk()){
			//コメント類バイトチェック
        	rs = FALSE;
		}else if(!this.isInputCommentKinshiChk()){
			//コメント類入力禁止文字チェック
        	rs = FALSE;
		}
    	
    	return rs;
    }

    /**
     * 【タブ関連チェック処理】 <br>
     * @return boolean
     */
    private boolean isRelateTabChk() throws Exception{
    	
    	boolean rs = FALSE;

    	//選択タブ内のチェックボックスが全てオフの場合はfalse
    	if(SEIJO_YOTYUI_TAB.equals(form.getCurrentTab())){
    		//正常先・要注意先タブ
    		if(GS.ON.equals(form.getChkFlgSeijo())){
    			rs = TRUE;
    		}else if(GS.ON.equals(form.getChkFlgYochui())){
        		rs = TRUE;
        	}else{
    			appContext.setMsgCode(GL.ERR_INPUT,GL.OC1103_SEIJOSAKI_YOCHUISAKI);
        	}
    	}else if(KASIDAORE_TAB.equals(form.getCurrentTab())){
    		//貸倒懸念先タブ
    		if(GS.ON.equals(form.getChkFlgTyoka())){
    			rs = TRUE;
    		}else if(GS.ON.equals(form.getChkFlgKanwa())){
        		rs = TRUE;
    		}else if(GS.ON.equals(form.getChkFlgEntai())){
        		rs = TRUE;
        	}else{
    			appContext.setMsgCode(GL.ERR_INPUT,GL.OC1103_KASHITAORE);
        	}
    	}else{
    		//破産更生先タブ
    		if(GS.ON.equals(form.getChkFlgHasanho())){
    			rs = TRUE;
    		}else if(GS.ON.equals(form.getChkFlgKaishaho())){
        		rs = TRUE;
    		}else if(GS.ON.equals(form.getChkFlgKoseho())){
        		rs = TRUE;
    		}else if(GS.ON.equals(form.getChkFlgSaiseho())){
        		rs = TRUE;
    		}else if(GS.ON.equals(form.getChkFlgShobun())){
        		rs = TRUE;
    		}else if(GS.ON.equals(form.getChkFlgSonota())){
        		rs = TRUE;
        	}else{
    			appContext.setMsgCode(GL.ERR_INPUT,GL.OC1103_HASAN_KOSEI);
        	}
    	}
    	
    	return rs;
    }

    /**
     * 【債権区分関連チェック処理】 <br>
     * @return boolean
     */
    private boolean isRelateSaikenChk() throws Exception{
    	
    	boolean rs = FALSE;

    	if(form.getCurrentTab().equals(form.getKbnSaiken())){
    		//債権区分とカレントタブが一致する場合
    		rs = TRUE;
    	}else{
    		//債権区分とカレントタブが一致しない場合、判定事由入力チェック
    		if(GS.EMPTY_CHARCTER.equals(form.getTxtValJiyuu())){
    			//判定事由未入力
    			appContext.setMsgCode(GL.ERR_HANTEIJIYU);
    		}else{
        		rs = TRUE;
    		}
    	}
    	
    	return rs;
    }

    /**
     * 【一般債権以外関連チェック処理】 <br>
     * @return boolean
     */
    private boolean isRelateIppanSaikenChk() throws Exception{
    	
    	boolean rs = FALSE;

    	if(GS.IPPAN_SAIKEN.equals(form.getKbnSaiken())){
    		//一般債権の場合
    		rs = TRUE;
    	}else{
    		//一般債権以外の場合、発生経緯入力チェック
    		if(GS.EMPTY_CHARCTER.equals(form.getTxtValKeii())){
    			//判定事由未入力
    			appContext.setMsgCode(GL.ERR_HASSEIKEII);
    		}else{
        		rs = TRUE;
    		}
    	}
    	
    	return rs;
    }

    /**
     * 【コメント類バイトチェック処理】 <br>
     * @return boolean
     */
    private boolean isInputCommentByteChk() throws Exception{
    	
    	boolean rs = FALSE;
		
		if(!(check.islength(form.getTxtValKeii(),2000))){
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1103_HASSEI_KEII);
			appContext.setFocusField(txtValKeii);
		}else if(!(check.islength(form.getTxtValKonkyo(),1000))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1103_HANTEI_KONKYO);
			appContext.setFocusField(txtValKonkyo);
		}else if(!(check.islength(form.getTxtValJiyuu(),500))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1103_HANTEI_JIYU);
			appContext.setFocusField(txtValJiyuu);
		}else{
			rs = true;
		}
		
    	return rs;
    }

    /**
     * 【コメント類入力禁止文字チェック処理】 <br>
     * @return boolean
     */
    private boolean isInputCommentKinshiChk() throws Exception{
    	
    	String yousoNm = null;
    	boolean rs = TRUE;
		
		if(!GS.EMPTY_CHARCTER.equals(form.getTxtValKeii())){
			rs = isKinshiChk(form.getTxtValKeii());
	    	yousoNm = txtValKeii;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getTxtValKonkyo())){
			rs = isKinshiChk(form.getTxtValKonkyo());
	    	yousoNm = txtValKonkyo;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getTxtValJiyuu())){
			rs = isKinshiChk(form.getTxtValJiyuu());
	    	yousoNm = txtValJiyuu;
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
		
		KubunDbAcc dbacc = new KubunDbAcc(appContext);
		boolean initFlg = false;
		String kensakuyouAnkenNo = GS.EMPTY_CHARCTER;
		String kensakuyouPhase = GS.EMPTY_CHARCTER;
		
		//取引先区分セレクトボックス取得
		dbacc.getToriSelectList();

		//債権区分セレクトボックス取得
		dbacc.getSaikenList();
		
		//所在地取得
		dbacc.getSyozaichi(tori_bean);
		
		//初期表示判定
		initFlg = dbacc.isSyokiHyouji(tori_bean,SANSYO_POINT);
		
		//査定登録データ(滞留区分名称)取得
		dbacc.getSateiDataTairyu(tori_bean.getAnken_no(),tori_bean.getPhase(),tori_bean.getSystem_kbn());

		//検索用案件No.取得
		kensakuyouAnkenNo = getKensakuyouAnkenNo(dbacc,initFlg);
		if(GS.EMPTY_CHARCTER.equals(kensakuyouAnkenNo)){
			//IT038対応
			this.kbnHanteiKtk();
			//IT038ここまで
			//検索用案件No.取得不可の場合、ここでリターン
			return;
		}else{
			//検索用フェーズ取得
			kensakuyouPhase = getKensakuyouPhase(dbacc,initFlg,kensakuyouAnkenNo);
		}
		
		//査定登録データ取得
		dbacc.getSateiDataOC1103(kensakuyouAnkenNo,kensakuyouPhase,tori_bean.getSystem_kbn());
		
		//カレントタブ・取引先区分設定
		if(form.getKbnTorihiki().equals(GS.KASIDAORE_TORIKBN)){
			form.setCurrentTab(KASIDAORE_TAB);
			form.setKbnToriSelect(GS.KASIDAORE_TORIKBNTAB);
		}else if(form.getKbnTorihiki().equals(GS.HASANKOUSEI_TORIKBN)){
			form.setCurrentTab(HASANKOUSEI_TAB);
			form.setKbnToriSelect(GS.HASANKOUSEI_TORIKBNTAB);
		}else{
			form.setCurrentTab(SEIJO_YOTYUI_TAB);
			form.setKbnToriSelect(GS.SEIJOU_YOUTYUI_TORIKBNTAB);
			if(!(form.getChkFlgSeijo().equals(GS.ON)) && !(form.getChkFlgYochui().equals(GS.ON))){
				form.setChkFlgSeijo(GS.ON);
			}
		}

		//コメント取得
		dbacc.getCommentOC1103(kensakuyouAnkenNo,kensakuyouPhase,SANSYO_POINT);
	}

	/**
	 * 保存処理<br>
     * @param boolean
	 */
	public void execHozon(boolean flg) throws Exception{

		//実施業務取得
    	HashMap map = getJishiGyoumu(tori_bean.getPhase(),GS.STATUS_MISYORI);
    	
		KubunDbAcc dbacc = new KubunDbAcc(appContext);
		//コメントの登録
		dbacc.deleteComment(tori_bean,SANSYO_POINT);
		dbacc.insertComment(tori_bean,POINT_KONKYO,form.getTxtValKonkyo());
		dbacc.insertComment(tori_bean,POINT_JIYU,form.getTxtValJiyuu());
		dbacc.insertComment(tori_bean,POINT_KEII,form.getTxtValKeii());
		//査定内容登録
		dbacc.updateSateiData(tori_bean);
		//進捗更新
		dbacc.setSateiStatSyorityu(tori_bean,map,TOROKU_GAMEN,GS.STATUS_SYORICHU);
		//入力履歴登録
		if(flg){
			dbacc.setNyuryokuHist(tori_bean,NYURYOKU_KBN,GS.EMPTY_CHARCTER);
		}
		//コミット
		dbacc.commit();
	}
	
	//IT038対応
	/**
	 * 格付より取引先区分・債権区分を設定する<br>
	 */
	private void kbnHanteiKtk() throws Exception{
		if(KTK_9.equals(tori_bean.getSinyoktk())){
			form.setChkFlgYochui(GS.ON);
			form.setCurrentTab(SEIJO_YOTYUI_TAB);
			form.setKbnTorihiki(GS.YOUTYUI_TORIKBN);
			form.setKbnSaiken(GS.IPPAN_SAIKEN);
			form.setKbnToriSelect(GS.SEIJOU_YOUTYUI_TORIKBNTAB);
		}else if(KTK_W.equals(tori_bean.getSinyoktk())){
			form.setCurrentTab(KASIDAORE_TAB);
			form.setKbnTorihiki(GS.KASIDAORE_TORIKBN);
			form.setKbnSaiken(GS.KASIDAORE_SAIKEN);
			form.setKbnToriSelect(GS.KASIDAORE_TORIKBNTAB);
		}else if(KTK_T.equals(tori_bean.getSinyoktk())){
			form.setCurrentTab(HASANKOUSEI_TAB);
			form.setKbnTorihiki(GS.HASANKOUSEI_TORIKBN);
			form.setKbnSaiken(GS.HASANKOUSEI_SAIKEN);
			form.setKbnToriSelect(GS.HASANKOUSEI_TORIKBNTAB);
		}else if(GS.EMPTY_CHARCTER.equals(Function.trim(tori_bean.getSinyoktk()))){
			form.setCurrentTab(SEIJO_YOTYUI_TAB);
			form.setKbnTorihiki(GS.SEIJOUSAKI_TORIKBN);
			form.setKbnSaiken(GS.IPPAN_SAIKEN);
			form.setKbnToriSelect(GS.SEIJOU_YOUTYUI_TORIKBNTAB);
		}else{
			form.setChkFlgSeijo(GS.ON);
			form.setCurrentTab(SEIJO_YOTYUI_TAB);
			form.setKbnTorihiki(GS.SEIJOUSAKI_TORIKBN);
			form.setKbnSaiken(GS.IPPAN_SAIKEN);
			form.setKbnToriSelect(GS.SEIJOU_YOUTYUI_TORIKBNTAB);
		}
	}
	//IT038ここまで
}