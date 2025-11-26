/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001     2009/05/26      SSC				1.5次版組込
******************************************************************************/
package app.syokaiZen.form;

import common.global.GS;
import common.struts.AppPagerActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class SateiForm extends AppPagerActionForm {

	/**	 セレクトボックス用システム区分配列 */
	private LinkedHashMap ar_system_kbn;
	/** システム区分(01:GSS、02:MTS、03:FOCUS) */
	private String systemKbn;

	/**	 セレクトボックス用債権区分配列 */
	private LinkedHashMap ar_saiken_kbn;
	/** 債権区分(01:一般債権、02:貸倒懸念債権、03:破産更生債権) */
	private String saikenKbn;
	
	/** 選択対象年月 */
    private String ym;	
    /** ソート順 */
    private int sort;
    /** 勘定先CDリンク選択時案件No */
    private String anken_no;
    /** 勘定先CDリンク選択時List列番号 */
    private int id;
    
    /** 検索用基準年月 */
    private String baseDate;
    /** 検索用勘定先CD */ 
    private String kanjoCd;
    //要件No.四-10
    //追加開始
    /** 検索用半期四半期区分 */
    private String hanki_sihanki_kbn;
    //追加完了
    /** 検索用システム区分 */
    private String systemSection;
    /** 検索用DUNS No. */
    private String dunsNo;
    /** 検索用勘定先名称 */
    private String kanjoName;
    /** 検索用所属国名 */
    private String country;
    /** 検索用取引先区分 */
    private String dealSection;
    /** 検索用債権区分 */
	private String claimSection;
	/** 検索用抽出事由 */
	private String caseSelection;
	/** 検索用査定会社 */
	private String judgeCorp;
	/** 検索用部門 */
    private String bumonCode;
    /** 検索用部名 */
    private String buCode;
    /** 参照分類２ */
    private String sansyoBunrui2;
    
    /** 検索用範囲年月リスト */
    private List dateList;
    /** ソート順プルダウンメニューマップ */
    private LinkedHashMap sortList;
    /** 査定会社プルダウンメニューリスト */
    private List sateiCorp;
    /** 所属国プルダウンメニューマップ */
    private LinkedHashMap countryMap;
    /** 抽出事由プルダウンメニューマップ */
    private LinkedHashMap jiyuu;
    /** 部門名プルダウンメニューマップ */
    private LinkedHashMap bumon;
    /** 部名プルダウンメニューマップ */
    private LinkedHashMap bu;
    /** 取引先区分プルダウンメニューマップ */
    private LinkedHashMap toriKbnList;
    /** ページ内表示メッセージ */
    private String infoMsg;
    /** 検索モード切替判定フラグ */
    private int searchMode;		// 0:査定検索 1:滞留判定検索
    /** 画面遷移元判定フラグ */
    private int searchFlag;		// 0:通常 1:チャンピオン部遷移
    /** ダウンロードボタン表示判定フラグ */
    private int downloadFlag;		// 0:非表示１ 1:表示
    /** 初回画面表示判定フラグ */
    private int initMode;			// 1:初回 2:再表示
    //要件No.四-10
    //追加開始
    /** 半期四半期区分セレクトボックス値 */
    private LinkedHashMap hanki_sihanki_kbn_List;
    //追加完了
    /** システムセレクトボックス値 */
    private LinkedHashMap systemList;
    /** 債権セレクトボックス値 */
    private LinkedHashMap saikenList;
    /** 件数セレクトボックス値 */
    private LinkedHashMap showList;
    
    // No594, 2008/06/12, SJA渡辺, 	帳票セレクトボックス等追加
    /** 〆区分セレクトボックス値 */
    private LinkedHashMap kaisuList;
    /** 帳票用〆区分値 */
    private String kaisu;    
    /** 帳票用対象年月 */
    private String objectDate;
    /** 帳票種別セレクトボックス値 */
    private LinkedHashMap tyohyoList;
    /** 帳票用帳票種別値 */
    private String tyohyo;
    /** ダウンロードチェックボックスフラグ */
    private int downloadFlg;
    
    // No423, 2008/05/22, SJA渡辺, 検索ボタン押下時の条件を退避しておくように修正
    /** 検索用退避基準年月 */
    private String saveBaseDate;
    /** 検索用退避勘定先CD */ 
    private String saveKanjoCd;
    /** 検索用退避システム区分 */
    private String saveSystemSection;
    //要件No.四-10
    //追加開始
    /** 検索用退避半期四半期区分 */
    private String saveHanki_sihanki_kbn;
    //追加完了
    /** 検索用退避DUNS No. */
    private String saveDunsNo;
    /** 検索用退避勘定先名称 */
    private String saveKanjoName;
    /** 検索用退避所属国名 */
    private String saveCountry;
    /** 検索用退避取引先区分 */
    private String saveDealSection;
    /** 検索用退避債権区分 */
	private String saveClaimSection;
	/** 検索用退避抽出事由 */
	private String saveCaseSelection;
	/** 検索用退避査定会社 */
	private String saveJudgeCorp;
	/** 検索用退避部門 */
    private String saveBumonCode;
    /** 検索用退避部名 */
    private String saveBuCode;
    /** 退避部名プルダウンメニューマップ */
    private LinkedHashMap saveBu;
    // No407, 2008/06/01, SJA渡辺, ページ表示個所修正ようにプロパティ追加
    private int idForPager;

    // No459, 2008/05/31, SJA平道, 画面再表示でフォーカスを保持するように修正
    /** 画面のフォーカス情報 */
    private String focusId;
    
    // 管理票No200807071037, 2008/07/09, SJA渡辺, 説明番号チェックボックス値
    private int chk;
   
    /** コンストラクタ・変数初期化 */
    public SateiForm() {
        sansyoBunrui2 = GS.EMPTY_CHARCTER;
    	this.ar_system_kbn = null;
    	this.systemKbn = "00";
    	this.ar_saiken_kbn = null;
    	this.saikenKbn = "00";
        this.ym = "";
        this.sort = 0;
        this.anken_no = "";
        this.id = 0;
        
        this.baseDate="";
        this.kanjoCd="";
        //要件No.四-10
        //追加開始
        this.hanki_sihanki_kbn="";
        //追加完了
        this.systemSection="";
        this.dunsNo="";
        this.kanjoName="";
        this.country="";
        this.dealSection="";
        this.claimSection="";
        this.caseSelection="";
        this.judgeCorp="";
        this.bumonCode="";
		this.buCode="";
		
		this.dateList=new ArrayList();
		this.sateiCorp = new ArrayList();
		this.infoMsg = "";
		
		this.setPager(new ArrayList());
		this.setAr_meisai(new ArrayList());
		this.searchMode = 0;
		this.searchFlag = 0;
		this.downloadFlag=1;
		this.initMode = 0;
		
		//要件No.四-10
		//追加開始
		hanki_sihanki_kbn_List = null;
		//追加完了
		systemList = null;
		saikenList = null;
		showList = null;
		kaisuList = null;
		tyohyoList = null;
		
		this.saveBaseDate="";
        this.saveKanjoCd="";
        this.saveSystemSection="";
        //要件No.四-10
        //追加開始
        this.saveHanki_sihanki_kbn = "";
        //追加完了
        this.saveDunsNo="";
        this.saveKanjoName="";
        this.saveCountry="";
        this.saveDealSection="";
        this.saveClaimSection="";
        this.saveCaseSelection="";
        this.saveJudgeCorp="";
        this.saveBumonCode="";
		this.saveBuCode="";
		this.saveBu = new LinkedHashMap();

    	this.focusId = "";
    	this.idForPager = 0;
    	
    	this.kaisu = "";
    	this.objectDate = "";
    	this.tyohyo = "";
    	this.downloadFlag = 0;
    	this.chk = 0;
    }
    
    public String getSansyoBunrui2() {
		return sansyoBunrui2;
	}
	public void setSansyoBunrui2(String sansyoBunrui2) {
		this.sansyoBunrui2 = sansyoBunrui2;
	}
    // アクセスメソッド   
    /**
     * @return gamenId を取得。
     */
    public String toString() {
        return super.gamenId;
    }
	/**
	 * @return ar_system_kbn を取得。
	 */
	public LinkedHashMap getAr_system_kbn() {
		return ar_system_kbn;
	}
	/**
	 * @param ar_system_kbn ar_system_kbn を設定。
	 */
	public void setAr_system_kbn(LinkedHashMap ar_system_kbn) {
		this.ar_system_kbn = ar_system_kbn;
	}
    /**
     * @return systemKbn を取得。
     */
    public String getSystemKbn() {
       return this.systemKbn;
    }
    /**
     * @param systemKbn systemKbn を設定。
     */
    public void setSystemKbn(String systemKbn) {
        this.systemKbn = systemKbn;
    }	

	/**
	 * @return ar_saiken_kbn を取得。
	 */
	public LinkedHashMap getAr_saiken_kbn() {
		return ar_saiken_kbn;
	}
	/**
	 * @param ar_saiken_kbn ar_saiken_kbn を設定。
	 */
	public void setAr_saiken_kbn(LinkedHashMap ar_saiken_kbn) {
		this.ar_saiken_kbn = ar_saiken_kbn;
	}
    /**
     * @return saikenKbn を取得。
     */
    public String getSaikenKbn() {
       return this.saikenKbn;
    }
    /**
     * @param saikenKbn saikenKbn を設定。
     */
    public void setSaikenKbn(String saikenKbn) {
        this.saikenKbn = saikenKbn;
    }	

    public String getYm() {
        return this.ym;
    }
    public void setYm(String ym) {
        this.ym = ym;
    }
    
    public int getSort() {
        return this.sort;
    }
    public void setSort(int sort) {
        this.sort = sort;
    }
    public String getAnken_no() {
        return this.anken_no;
    }
    public void setAnken_no(String anken_no) {
        this.anken_no = anken_no;
    }

    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
	public String getKanjoCd() {
		return kanjoCd;
	}
	public String getBaseDate() {
		return baseDate;
	}
	public String getCaseSelection() {
		return caseSelection;
	}
	public String getClaimSection() {
		return claimSection;
	}
	public String getCountry() {
		return country;
	}
	public String getDealSection() {
		return dealSection;
	}
	public String getDunsNo() {
		return dunsNo;
	}
	public String getJudgeCorp() {
		return judgeCorp;
	}
	public String getKanjoName() {
		return kanjoName;
	}
	public String getSystemSection() {
		return systemSection;
	}
	//要件No.四-10
	//追加開始
	public String getHanki_sihanki_kbn() {
		return hanki_sihanki_kbn;
	}
	//追加完了
	public void setKanjoCd(String kanjoCd) {
		this.kanjoCd = kanjoCd;
	}
	public void setBaseDate(String baseDate) {
		this.baseDate = baseDate;
	}
	public void setCaseSelection(String caseSelection) {
		this.caseSelection = caseSelection;
	}
	public void setClaimSection(String claimSection) {
		this.claimSection = claimSection;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setDealSection(String dealSection) {
		this.dealSection = dealSection;
	}
	public void setDunsNo(String dunsNo) {
		this.dunsNo = dunsNo;
	}
	public void setJudgeCorp(String judgeCorp) {
		this.judgeCorp = judgeCorp;
	}
	public void setKanjoName(String kanjoName) {
		this.kanjoName = kanjoName;
	}
	public void setSystemSection(String systemSection) {
		this.systemSection = systemSection;
	}
	//要件No.四-10
	//追加開始
	public void setHanki_sihanki_kbn(String hanki_sihanki_kbn) {
		this.hanki_sihanki_kbn = hanki_sihanki_kbn;
	}
	//追加完了
	public LinkedHashMap getJiyuu() {
		return jiyuu;
	}
	public void setJiyuu(LinkedHashMap jiyuu) {
		this.jiyuu = jiyuu;
	}
	public String getBumonCode() {
		return bumonCode;
	}
	public String getBuCode() {
		return buCode;
	}
	public void setBumonCode(String BumonCode) {
		this.bumonCode = BumonCode;
	}
	public void setBuCode(String BuCode) {
		this.buCode = BuCode;
	}
	public void setSearchMode(int searchMode) {
		this.searchMode = searchMode;
	}
	public LinkedHashMap getCountryMap() {
		return countryMap;
	}
	public void setCountryMap(LinkedHashMap countryMap) {
		this.countryMap = countryMap;
	}
	public List getDateList() {
		return dateList;
	}
	public void setDateList(List dateList) {
		this.dateList = dateList;
	}
	public LinkedHashMap getBu() {
		return bu;
	}
	public LinkedHashMap getBumon() {
		return bumon;
	}
	public int getSearchMode() {
		return searchMode;
	}
	public void setBu(LinkedHashMap bu) {
		this.bu = bu;
	}
	public void setBumon(LinkedHashMap bumon) {
		this.bumon = bumon;
	}
	public List getSateiCorp() {
		return sateiCorp;
	}
	public void setSateiCorp(List sateiCorp) {
		this.sateiCorp = sateiCorp;
	}
	public LinkedHashMap getSortList() {
		return sortList;
	}
	public void setSortList(LinkedHashMap sortList) {
		this.sortList = sortList;
	}
    public String getInfoMsg() {
        return infoMsg;
    }
    public LinkedHashMap getToriKbnList() {
        return toriKbnList;
    }
    public void setInfoMsg(String infoMsg) {
        this.infoMsg = infoMsg;
    }
    public void setToriKbnList(LinkedHashMap toriKbnList) {
        this.toriKbnList = toriKbnList;
    }
    public int getInitMode() {
        return initMode;
    }
    public void setInitMode(int initMode) {
        this.initMode = initMode;
    }
    public int getSearchFlag() {
        return searchFlag;
    }
    public void setSearchFlag(int searchFlag) {
        this.searchFlag = searchFlag;
    }
    public int getDownloadFlag() {
        return downloadFlag;
    }
    public void setDownloadFlag(int downloadFlag) {
        this.downloadFlag = downloadFlag;
    }

	public LinkedHashMap getSaikenList() {
		return saikenList;
	}
	public void setSaikenList(LinkedHashMap saikenList) {
		this.saikenList = saikenList;
	}
	//要件No.四-10
	//追加開始
	public LinkedHashMap getHanki_sihanki_kbn_List() {
		return hanki_sihanki_kbn_List;
	}
	public void setHanki_sihanki_kbn_List(LinkedHashMap hanki_sihanki_kbn_List) {
		this.hanki_sihanki_kbn_List = hanki_sihanki_kbn_List;
	}
	//追加完了
	public LinkedHashMap getSystemList() {
		return systemList;
	}
	public void setSystemList(LinkedHashMap systemList) {
		this.systemList = systemList;
	}
	public LinkedHashMap getShowList() {
		return showList;
	}
	public void setShowList(LinkedHashMap showList) {
		this.showList = showList;
	}
	
	public String getSaveBaseDate() {
		return saveBaseDate;
	}
	public void setSaveBaseDate(String saveBaseDate) {
		this.saveBaseDate = saveBaseDate;
	}
	public String getSaveBuCode() {
		return saveBuCode;
	}
	public void setSaveBuCode(String saveBuCode) {
		this.saveBuCode = saveBuCode;
	}
	public String getSaveBumonCode() {
		return saveBumonCode;
	}
	public void setSaveBumonCode(String saveBumonCode) {
		this.saveBumonCode = saveBumonCode;
	}
	public String getSaveCaseSelection() {
		return saveCaseSelection;
	}
	public void setSaveCaseSelection(String saveCaseSelection) {
		this.saveCaseSelection = saveCaseSelection;
	}
	public String getSaveClaimSection() {
		return saveClaimSection;
	}
	public void setSaveClaimSection(String saveClaimSection) {
		this.saveClaimSection = saveClaimSection;
	}
	public String getSaveCountry() {
		return saveCountry;
	}
	public void setSaveCountry(String saveCountry) {
		this.saveCountry = saveCountry;
	}
	public String getSaveDealSection() {
		return saveDealSection;
	}
	public void setSaveDealSection(String saveDealSection) {
		this.saveDealSection = saveDealSection;
	}
	public String getSaveDunsNo() {
		return saveDunsNo;
	}
	public void setSaveDunsNo(String saveDunsNo) {
		this.saveDunsNo = saveDunsNo;
	}
	public String getSaveJudgeCorp() {
		return saveJudgeCorp;
	}
	public void setSaveJudgeCorp(String saveJudgeCorp) {
		this.saveJudgeCorp = saveJudgeCorp;
	}
	public String getSaveKanjoCd() {
		return saveKanjoCd;
	}
	public void setSaveKanjoCd(String saveKanjoCd) {
		this.saveKanjoCd = saveKanjoCd;
	}
	public String getSaveKanjoName() {
		return saveKanjoName;
	}
	public void setSaveKanjoName(String saveKanjoName) {
		this.saveKanjoName = saveKanjoName;
	}
	public String getSaveSystemSection() {
		return saveSystemSection;
	}
	public void setSaveSystemSection(String saveSystemSection) {
		this.saveSystemSection = saveSystemSection;
	}
	//要件No.四-10
	//追加開始
	public String getSaveHanki_sihanki_kbn() {
		return saveHanki_sihanki_kbn;
	}
	public void setSaveHanki_sihanki_kbn(String saveHanki_sihanki_kbn) {
		this.saveHanki_sihanki_kbn = saveHanki_sihanki_kbn;
	}
	//追加完了
	
	public LinkedHashMap getSaveBu() {
		return saveBu;
	}
	public void setSaveBu(LinkedHashMap saveBu) {
		this.saveBu = saveBu;
	}
	/** 検索用退避データの初期化を行う */
	public void setInitSaveData() {
		this.saveBaseDate="";
        this.saveKanjoCd="";
        this.saveSystemSection="";
        //要件No.四-10
        //追加開始
        this.saveHanki_sihanki_kbn="";
        //追加完了
        this.saveDunsNo="";
        this.saveKanjoName="";
        this.saveCountry="";
        this.saveDealSection="";
        this.saveClaimSection="";
        this.saveCaseSelection="";
        this.saveJudgeCorp="";
        this.saveBumonCode="";
		this.saveBuCode="";
		this.saveBu = new LinkedHashMap();
	}

	
	/**
	 * @return focusId を戻します。
	 */
	public String getFocusId() {
		return focusId;
	}
	/**
	 * @param focusId focusId を設定。
	 */
	public void setFocusId(String focusId) {
		this.focusId = focusId;
	}
	public int getIdForPager() {
		return idForPager;
	}
	public void setIdForPager(int idForPager) {
		this.idForPager = idForPager;
	}
	public LinkedHashMap getKaisuList() {
		return kaisuList;
	}
	public void setKaisuList(LinkedHashMap kaisuList) {
		this.kaisuList = kaisuList;
	}
	public String getKaisu() {
		return kaisu;
	}
	public void setKaisu(String kaisu) {
		this.kaisu = kaisu;
	}
	public String getObjectDate() {
		return objectDate;
	}
	public void setObjectDate(String objectDate) {
		this.objectDate = objectDate;
	}
	public LinkedHashMap getTyohyoList() {
		return tyohyoList;
	}
	public void setTyohyoList(LinkedHashMap tyohyoList) {
		this.tyohyoList = tyohyoList;
	}
	public String getTyohyo() {
		return tyohyo;
	}
	public void setTyohyo(String tyohyo) {
		this.tyohyo = tyohyo;
	}
	public int getDownloadFlg() {
		return downloadFlg;
	}
	public void setDownloadFlg(int downloadFlg) {
		this.downloadFlg = downloadFlg;
	}

	public int getChk() {
		return chk;
	}
	public void setChk(int chk) {
		this.chk = chk;
	}
	
	
	// No594, 2008/06/12, SJA渡辺, リセットメソッド追加
	public void reset(ActionMapping mapping, HttpServletRequest request){
		downloadFlg = 0;
		//  管理票No200807071037, 2008/07/09, SJA渡辺, リセット項目追加
		chk = 0;
	}
}