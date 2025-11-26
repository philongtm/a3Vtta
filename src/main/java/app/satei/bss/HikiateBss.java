/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/20		SSC				課題No.60 引当金確認案件の取引先区分・債権区分設定 
003		2009/10/20		SSC				課題No.34 一般債権時のコメント取得処理変更 
004		2009/10/29		SSC				課題No.90 コメント｢今後の回収見通しなど｣仕様変更 
005		2009/11/26		SSC				課題No.167 留保債務計自動計算 
******************************************************************************/
package app.satei.bss;

import app.satei.dbAcc.HikiateDbAcc;
import app.satei.form.HikiateForm;
import common.AppContext;
import common.global.GL;
import common.global.GS;
import common.util.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.satei.form.HikiateForm.TAB_HUKA;
import static app.satei.form.HikiateForm.TD_MIZUIRO;
import static app.satei.form.HikiateForm.TXTA_HAIIRO;
import static app.satei.form.HikiateForm.TXTA_MIZUIRO;
import static app.satei.form.HikiateForm.TXT_MIZUIRO;
import static app.satei.form.HikiateForm.YOMITORI_TRUE;

/**
 * OC1104_査定_引当金判定 ビジネス ロジッククラス <br>
 */
public class HikiateBss extends SateiBss{

	private HikiateForm form	= null;		//アクションフォーム

	//T12_コメントの登録箇所
	private static final String SANSYO_POINT						= "'50','60','70','80','90','95'";
	private static final String NOTFLGSAKI_SANSYO_POINT			= "'50','60','70','80','90'";
	private static final String FLGSAKI_SANSYO_POINT				= "'95'";
	private static final String POINT_SONOTANONAIYO				= "50";
	private static final String POINT_SONOTAKAISHUNONAIYO			= "60";
	private static final String POINT_RIKOSEIKYUKENEN				= "70";
	private static final String POINT_HIKIATEKINSANTEIKONKYO		= "80";
	private static final String POINT_KONGONOKAISHUMITOSHI		= "90";
	private static final String POINT_FLGCOMMENT					= "95";

	//要素名
    private static final String shoninsha			= "shoninsha";
    private static final String kanjo_cd1			= "kanjo_cd1";
    private static final String tori_nm1			= "tori_nm1";
    private static final String kanjo_kamoku1		= "kanjo_kamoku1";
    private static final String kingaku1			= "kingaku1";
    private static final String kanjo_cd2			= "kanjo_cd2";
    private static final String tori_nm2			= "tori_nm2";
    private static final String kanjo_kamoku2		= "kanjo_kamoku2";
    private static final String kingaku2			= "kingaku2";
    private static final String kanjo_cd3			= "kanjo_cd3";
    private static final String tori_nm3			= "tori_nm3";
    private static final String kanjo_kamoku3		= "kanjo_kamoku3";
    private static final String kingaku3			= "kingaku3";
    private static final String sonotanonaiyo							= "sonotanonaiyo";
    private static final String sonotakaishunonaiyo					= "sonotakaishunonaiyo";
    private static final String rikoseikyukenennonaiyosetumei			= "rikoseikyukenennonaiyosetumei";
    private static final String hikiatekinsanteikonkyononaiyosetumei	= "hikiatekinsanteikonkyononaiyosetumei";
    private static final String kongonokaishumitoshi					= "kongonokaishumitoshi";
    private static final String flgcomment							= "flgcomment";
    
    private static final String ryuhosaimu							= "ryuhosaimu";
    private static final String othryuhosaimu							= "othryuhosaimu";
    private static final String hozen									= "hozen";
    private static final String sonotakaishu							= "sonotakaishu";
    private static final String rikoseikyukenen						= "rikoseikyukenen";
    private static final String tuikahikiatekingaku					= "tuikahikiatekingaku";
    private static final String tukachosei							= "tukachosei";
    private static final String keiyakugaku_hudousantanpo				= "keiyakugaku_hudousantanpo";
    private static final String keiyakugaku_dousantanpo				= "keiyakugaku_dousantanpo";
    private static final String keiyakugaku_bouekihoken				= "keiyakugaku_bouekihoken";
    private static final String keiyakugaku_sonota					= "keiyakugaku_sonota";
    private static final String hyokagaku_hudousantanpo				= "hyokagaku_hudousantanpo";
    private static final String hyokagaku_dousantanpo					= "hyokagaku_dousantanpo";
    private static final String hyokagaku_bouekihoken					= "hyokagaku_bouekihoken";
    private static final String hyokagaku_sonota						= "hyokagaku_sonota";
    private static final String hanyo1								= "hanyo1";
    private static final String shihankichushutsuflg					= "shihankichushutsuflg";	
	//課題No.60
	//追加開始
	private static final String SAIKEN_KBN		= "saiken_kbn";
	private static final String TORIHIKISAKI_KBN	= "torihikisaki_kbn";
	//追加完了

	//更新登録用
	private static final String TOROKU_GAMEN 		= "3";		//OC1104_査定_引当金判定
	private static final String NYURYOKU_TOROKU 	= "10";		//10:登録
	private static final String NYURYOKU_HOZON 	= "20";		//20:一次保存
	private List<String> msgList = null;
	private static final String KINGAKU_KOKUNAI_PATTERN	= "^[-+]?[0-9]{1,12}$";	
	private static final String KINGAKU_KAIGAI_PATTERN	= "^[-+]?[0-9]{1,12}\\.[0-9]{1,2}$";	
	private static final String KANRYOU_FLG				= "1";		//完了フラグ

	private static final String HIHYOUJI			= "0";		//非表示
	private static final String HYOUJI			= "1";		//表示
	private static final String FOCUS_US			= "US";

    /**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public HikiateBss(AppContext appContext) throws Exception {
		super(appContext);
		this.form = (HikiateForm)appContext.getActionForm();
	}
    
    /**
     * 【入力チェック処理】 <br>
     * @return boolean
     */
    public boolean check(boolean shoninChkFlg) throws Exception{
    	
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
		}else if(!this.isRelateOthRyuhoSaimuNaiyakuChk()){
			//第三者留保債務内訳関連チェック
        	rs = FALSE;
		}else if(!this.isRelateChk()){
			//関連チェック
        	rs = FALSE;
		}else if(shoninChkFlg && !this.isShoninChk()){
			//承認者選択チェック
        	rs = FALSE;
		}
    	
    	return rs;
    }
    
    /**
     * 【承認者選択チェック処理】 <br>
     * @return boolean
     */
    private boolean isShoninChk() throws Exception{
    	
    	if(HIHYOUJI.equals(form.getShoninshaListFlg())){
    		return TRUE;
    	}
    	if(GS.EMPTY_CHARCTER.equals(form.getShoninsha())){
			appContext.setMsgCode(GL.ERR_SELECT,GL.OC1104_SHONINSHA);
			appContext.setFocusField(shoninsha);
    		return FALSE;
    	}
    	return TRUE;
    }
    
    /**
     * 【関連チェック処理】 <br>
     * @return boolean
     */
    private boolean isRelateChk() throws Exception{
    	
    	msgList = new ArrayList<String>();
    	msgList.add(GL.ERR_INPUTWHEN);

		if(!form.isIppanFlg() && ((Function.getValueOfDouble(form.getHyokagaku_sonota())>0) || (Function.getValueOfDouble(form.getKeiyakugaku_sonota())>0))){
			if(GS.EMPTY_CHARCTER.equals(form.getSonotanonaiyo())){
		    	msgList.add(GL.REPLACE_HOZENSONOTA);
		    	msgList.add(GL.OC1104_ETC_NAIYO);
				appContext.setMsgCode(msgList);
				appContext.setFocusField(sonotanonaiyo);
				return FALSE;
			}
		}
		if(!form.isIppanFlg() && (Function.getValueOfDouble(form.getSonotakaishu())>0)){
			if(GS.EMPTY_CHARCTER.equals(form.getSonotakaishunonaiyo())){
		    	msgList.add(GL.OC1104_SONOTA_KAISHUU_4);
		    	msgList.add(GL.OC1104_ETC_KAISYU_NAIYO);
				appContext.setMsgCode(msgList);
				appContext.setFocusField(sonotakaishunonaiyo);
				return FALSE;
			}
		}
		if(!form.isIppanFlg() && (Function.getValueOfDouble(form.getRikoseikyukenen())>0)){
			if(GS.EMPTY_CHARCTER.equals(form.getRikoseikyukenennonaiyosetumei())){
				msgList.add(GL.OC1104_RIKOU_SEIKYUU_KENEN_5);
				msgList.add(GL.OC1104_RIKO_NAIYOU);
				appContext.setMsgCode(msgList);
				appContext.setFocusField(rikoseikyukenennonaiyosetumei);
				return FALSE;
			}
		}
		if(GS.ON.equals(form.getShihankichushutsuflg())){
			if(GS.EMPTY_CHARCTER.equals(form.getFlgkbn())){
				appContext.setMsgCode(GL.ERR_SELECT,GL.OC1104_FLG_KBN);
				appContext.setFocusField(shihankichushutsuflg);
				return FALSE;
			}
		}
		return TRUE;
    }
    
    /**
     * 【第三者留保債務内訳関連チェック処理】 <br>
     * @return boolean
     */
    private boolean isRelateOthRyuhoSaimuNaiyakuChk() throws Exception{
    	
    	boolean cdFlg = FALSE;
    	boolean nmFlg = FALSE;
    	boolean kamokuFlg = FALSE;
    	boolean kingakuFlg = FALSE;
		
		if(GS.EMPTY_CHARCTER.equals(form.getKanjo_cd1())){
			cdFlg = TRUE;
		}
		if(GS.EMPTY_CHARCTER.equals(form.getTori_nm1())){
	    	nmFlg = TRUE;
		}
		if(GS.EMPTY_CHARCTER.equals(form.getKanjo_kamoku1())){
			kamokuFlg = TRUE;
		}
		if(GS.EMPTY_CHARCTER.equals(form.getKingaku1())){
			kingakuFlg = TRUE;
		}
		
		if(cdFlg && nmFlg && kamokuFlg && kingakuFlg){
			kingakuFlg = FALSE;
			nmFlg = FALSE;
			kamokuFlg = FALSE;
			kingakuFlg = FALSE;
		}else if(!cdFlg && !nmFlg && !kamokuFlg && !kingakuFlg){
			//スルー
		}else{
			appContext.setMsgCode(GL.ERR_OTHRYUHOSAIMU);
			appContext.setFocusField(kanjo_cd1);
	    	return FALSE;
		}
		
		if(GS.EMPTY_CHARCTER.equals(form.getKanjo_cd2())){
			cdFlg = TRUE;
		}
		if(GS.EMPTY_CHARCTER.equals(form.getTori_nm2())){
	    	nmFlg = TRUE;
		}
		if(GS.EMPTY_CHARCTER.equals(form.getKanjo_kamoku2())){
			kamokuFlg = TRUE;
		}
		if(GS.EMPTY_CHARCTER.equals(form.getKingaku2())){
			kingakuFlg = TRUE;
		}

		if(cdFlg && nmFlg && kamokuFlg && kingakuFlg){
			kingakuFlg = FALSE;
			nmFlg = FALSE;
			kamokuFlg = FALSE;
			kingakuFlg = FALSE;
		}else if(!cdFlg && !nmFlg && !kamokuFlg && !kingakuFlg){
			//スルー
		}else{
			appContext.setMsgCode(GL.ERR_OTHRYUHOSAIMU);
			appContext.setFocusField(kanjo_cd2);
	    	return FALSE;
		}
		
		if(GS.EMPTY_CHARCTER.equals(form.getKanjo_cd3())){
			cdFlg = TRUE;
		}
		if(GS.EMPTY_CHARCTER.equals(form.getTori_nm3())){
	    	nmFlg = TRUE;
		}
		if(GS.EMPTY_CHARCTER.equals(form.getKanjo_kamoku3())){
			kamokuFlg = TRUE;
		}
		if(GS.EMPTY_CHARCTER.equals(form.getKingaku3())){
			kingakuFlg = TRUE;
		}

		if(cdFlg && nmFlg && kamokuFlg && kingakuFlg){
			kingakuFlg = FALSE;
			nmFlg = FALSE;
			kamokuFlg = FALSE;
			kingakuFlg = FALSE;
		}else if(!cdFlg && !nmFlg && !kamokuFlg && !kingakuFlg){
			//スルー
		}else{
			appContext.setMsgCode(GL.ERR_OTHRYUHOSAIMU);
			appContext.setFocusField(kanjo_cd3);
	    	return FALSE;
		}

		return TRUE;
    }

    /**
     * 【数値チェック処理】 <br>
     * @return boolean
     */
    private boolean isNumChk() throws Exception{
		
    	String yousoNm = null;
    	boolean rs = TRUE;
    	String nmChekFmt = null;
    	String GL_REP_SHOSU = null;
    	String GL_LABEL = null;
    	if(GS.GSS.equals(tori_bean.getSystem_kbn())){
    		nmChekFmt = KINGAKU_KOKUNAI_PATTERN;
        	GL_REP_SHOSU = GL.REPLACE_0;
    	}else{
    		nmChekFmt = KINGAKU_KAIGAI_PATTERN;
        	GL_REP_SHOSU = GL.REPLACE_2;
    	}
    	
		if(!GS.EMPTY_CHARCTER.equals(form.getHanyo1())){
			String label = GL.OC1104_HANYO1_GAI;
			if(GS.GSS.equals(tori_bean.getSystem_kbn())){
				label = GL.OC1104_HANYO1_NAI;
			}
			rs = Function.matches(form.getHanyo1(),nmChekFmt);
			GL_LABEL = label;
			yousoNm = hanyo1;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getRyuhosaimu())){
			rs = Function.matches(form.getRyuhosaimu(),nmChekFmt);
			GL_LABEL = GL.OC1104_RYUUHO_SAIMU;
			yousoNm = ryuhosaimu;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getOthryuhosaimu())){
			rs = Function.matches(form.getOthryuhosaimu(),nmChekFmt);
			GL_LABEL = GL.OC1104_NO3_RYUUHO_SAIMU;
			yousoNm = othryuhosaimu;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getHozen())){
			rs = Function.matches(form.getHozen(),nmChekFmt);
			GL_LABEL = GL.OC1104_HOZEN_3;
			yousoNm = hozen;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getSonotakaishu())){
			rs = Function.matches(form.getSonotakaishu(),nmChekFmt);
			GL_LABEL = GL.OC1104_SONOTA_KAISHUU_4;
			yousoNm = sonotakaishu;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getRikoseikyukenen())){
			rs = Function.matches(form.getRikoseikyukenen(),nmChekFmt);
			GL_LABEL = GL.OC1104_RIKOU_SEIKYUU_KENEN_5;
			yousoNm = rikoseikyukenen;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getTuikahikiatekingaku())){
			rs = Function.matches(form.getTuikahikiatekingaku(),nmChekFmt);
			GL_LABEL = GL.OC1104_TUIKA_HIKIATE_KIN;
			yousoNm = tuikahikiatekingaku;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getTukachosei())){
			rs = Function.matches(form.getTukachosei(),nmChekFmt);
			GL_LABEL = GL.OC1104_TUUKA_CHOUSEI;
			yousoNm = tukachosei;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKingaku1())){
			rs = Function.matches(form.getKingaku1(),nmChekFmt);
			GL_LABEL = GL.OC1104_KINGAKU;
			yousoNm = kingaku1;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKingaku2())){
			rs = Function.matches(form.getKingaku2(),nmChekFmt);
			GL_LABEL = GL.OC1104_KINGAKU;
			yousoNm = kingaku2;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKingaku3())){
			rs = Function.matches(form.getKingaku3(),nmChekFmt);
			GL_LABEL = GL.OC1104_KINGAKU;
			yousoNm = kingaku3;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKeiyakugaku_hudousantanpo())){
			rs = Function.matches(form.getKeiyakugaku_hudousantanpo(),nmChekFmt);
			GL_LABEL = GL.OC1104_HUDOUSAN_TANPO;
			yousoNm = keiyakugaku_hudousantanpo;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKeiyakugaku_dousantanpo())){
			rs = Function.matches(form.getKeiyakugaku_dousantanpo(),nmChekFmt);
			GL_LABEL = GL.OC1104_DOSAN_TANPO;
			yousoNm = keiyakugaku_dousantanpo;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKeiyakugaku_bouekihoken())){
			rs = Function.matches(form.getKeiyakugaku_bouekihoken(),nmChekFmt);
			GL_LABEL = GL.OC1104_BOEKI_HOKEN;
			yousoNm = keiyakugaku_bouekihoken;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKeiyakugaku_sonota())){
			rs = Function.matches(form.getKeiyakugaku_sonota(),nmChekFmt);
			GL_LABEL = GL.OC1104_SONOTA;
			yousoNm = keiyakugaku_sonota;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getHyokagaku_hudousantanpo())){
			rs = Function.matches(form.getHyokagaku_hudousantanpo(),nmChekFmt);
			GL_LABEL = GL.OC1104_HUDOUSAN_TANPO;
			yousoNm = hyokagaku_hudousantanpo;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getHyokagaku_dousantanpo())){
			rs = Function.matches(form.getHyokagaku_dousantanpo(),nmChekFmt);
			GL_LABEL = GL.OC1104_DOSAN_TANPO;
			yousoNm = hyokagaku_dousantanpo;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getHyokagaku_bouekihoken())){
			rs = Function.matches(form.getHyokagaku_bouekihoken(),nmChekFmt);
			GL_LABEL = GL.OC1104_BOEKI_HOKEN;
			yousoNm = hyokagaku_bouekihoken;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getHyokagaku_sonota())){
			rs = Function.matches(form.getHyokagaku_sonota(),nmChekFmt);
			GL_LABEL = GL.OC1104_SONOTA;
			yousoNm = hyokagaku_sonota;
		}
	
    	msgList = new ArrayList<String>();
    	msgList.add(GL.ERR_DIGITS);
    	msgList.add(GL_LABEL);
    	msgList.add(GL.REPLACE_12);
    	msgList.add(GL_REP_SHOSU);
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
		
		if(!(check.islength(form.getKanjo_cd1(),12))){
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_KANJO_CD);
			appContext.setFocusField(kanjo_cd1);
		}else if(!(check.islength(form.getKanjo_cd2(),12))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_KANJO_CD);
			appContext.setFocusField(kanjo_cd2);
		}else if(!(check.islength(form.getKanjo_cd3(),13))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_KANJO_CD);
			appContext.setFocusField(kanjo_cd3);
		}else if(!(check.islength(form.getTori_nm1(),80))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_TORIHIKISAKI_NM);
			appContext.setFocusField(tori_nm1);
		}else if(!(check.islength(form.getTori_nm2(),80))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_TORIHIKISAKI_NM);
			appContext.setFocusField(tori_nm2);
		}else if(!(check.islength(form.getTori_nm3(),80))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_TORIHIKISAKI_NM);
			appContext.setFocusField(tori_nm3);
		}else if(!(check.islength(form.getKanjo_kamoku1(),80))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_KANJOKAMOKU);
			appContext.setFocusField(kanjo_kamoku1);
		}else if(!(check.islength(form.getKanjo_kamoku2(),80))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_KANJOKAMOKU);
			appContext.setFocusField(kanjo_kamoku2);
		}else if(!(check.islength(form.getKanjo_kamoku3(),80))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_KANJOKAMOKU);
			appContext.setFocusField(kanjo_kamoku3);
		}else if(!(check.islength(form.getSonotanonaiyo(),500))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_ETC_NAIYO);
			appContext.setFocusField(sonotanonaiyo);
		}else if(!(check.islength(form.getSonotakaishunonaiyo(),2000))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_ETC_KAISYU_NAIYO);
			appContext.setFocusField(sonotakaishunonaiyo);
		}else if(!(check.islength(form.getRikoseikyukenennonaiyosetumei(),500))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_RIKO_NAIYOU);
			appContext.setFocusField(rikoseikyukenennonaiyosetumei);
		}else if(!(check.islength(form.getHikiatekinsanteikonkyononaiyosetumei(),500))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_HIKIATE_NAIYO);
			appContext.setFocusField(hikiatekinsanteikonkyononaiyosetumei);
		}else if(!(check.islength(form.getKongonokaishumitoshi(),2000))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_KONGO_MITOSHI);
			appContext.setFocusField(kongonokaishumitoshi);
		}else if(!(check.islength(form.getFlgcomment(),1000))) {
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1104_FLG_COMMENT);
			appContext.setFocusField(flgcomment);
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
		
		if(!GS.EMPTY_CHARCTER.equals(form.getKanjo_cd1())){
			rs = super.isKinshiChk(form.getKanjo_cd1());
	    	yousoNm = kanjo_cd1;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKanjo_cd2())){
			rs = super.isKinshiChk(form.getKanjo_cd2());
	    	yousoNm = kanjo_cd2;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKanjo_cd3())){
			rs = super.isKinshiChk(form.getKanjo_cd3());
	    	yousoNm = kanjo_cd3;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getTori_nm1())){
			rs = super.isKinshiChk(form.getTori_nm1());
	    	yousoNm = tori_nm1;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getTori_nm2())){
			rs = super.isKinshiChk(form.getTori_nm2());
	    	yousoNm = tori_nm2;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getTori_nm3())){
			rs = super.isKinshiChk(form.getTori_nm3());
	    	yousoNm = tori_nm3;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKanjo_kamoku1())){
			rs = super.isKinshiChk(form.getKanjo_kamoku1());
	    	yousoNm = kanjo_kamoku1;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKanjo_kamoku2())){
			rs = super.isKinshiChk(form.getKanjo_kamoku2());
	    	yousoNm = kanjo_kamoku2;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKanjo_kamoku3())){
			rs = super.isKinshiChk(form.getKanjo_kamoku3());
	    	yousoNm = kanjo_kamoku3;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getSonotanonaiyo())){
			rs = super.isKinshiChk(form.getSonotanonaiyo());
	    	yousoNm = sonotanonaiyo;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getSonotakaishunonaiyo())){
			rs = super.isKinshiChk(form.getSonotakaishunonaiyo());
	    	yousoNm = sonotakaishunonaiyo;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getRikoseikyukenennonaiyosetumei())){
			rs = super.isKinshiChk(form.getRikoseikyukenennonaiyosetumei());
	    	yousoNm = rikoseikyukenennonaiyosetumei;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getHikiatekinsanteikonkyononaiyosetumei())){
			rs = super.isKinshiChk(form.getHikiatekinsanteikonkyononaiyosetumei());
	    	yousoNm = hikiatekinsanteikonkyononaiyosetumei;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getKongonokaishumitoshi())){
			rs = super.isKinshiChk(form.getKongonokaishumitoshi());
	    	yousoNm = kongonokaishumitoshi;
		}
		if(rs == TRUE && !GS.EMPTY_CHARCTER.equals(form.getFlgcomment())){
			rs = super.isKinshiChk(form.getFlgcomment());
	    	yousoNm = flgcomment;
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
		
		HikiateDbAcc dbacc = new HikiateDbAcc(appContext);
		boolean initFlg = false;
		String kensakuyouAnkenNo = GS.EMPTY_CHARCTER;
		String kensakuyouPhase = GS.EMPTY_CHARCTER;
		
		//実施業務取得
    	HashMap map = getJishiGyoumu(tori_bean.getPhase(),GS.STATUS_MISYORI);

    	//汎用タイトル取得
		dbacc.getHanyouList();
		
		//フラグ区分セレクトボックス取得
		dbacc.getFlgKbnList();
		
		if(KANRYOU_FLG.equals((String)map.get(GS.JISHI_PHASE_KANRYO_FLG))){
			//承認セレクトボックス未取得・非表示
			form.setShoninshaListFlg(HIHYOUJI);
		//IT081対応
		}else if(((String)map.get(GS.JI_KAISHI_STATUS)).equals(GS.STATUS_MISYORI)){
			//承認セレクトボックス未取得・非表示
			form.setShoninshaListFlg(HIHYOUJI);
		//IT081ここまで
		}else{
			//承認セレクトボックス取得
			form.setShoninshaListFlg(HYOUJI);
			dbacc.getShoninList(tori_bean,map);
		}
		
		//引当金判定情報取得
		dbacc.getHikiate(tori_bean);
		
		//初期表示判定
		initFlg = dbacc.isSyokiHyouji(tori_bean,SANSYO_POINT);
		
		//四半期抽出項目＆他社リスク取得処理
		this.getShihankiChushutsuKoumoku(dbacc,initFlg);

		//一般債権の場合以下の処理後リターン
		dbacc.isIppanSaiken(tori_bean);
		if(form.isIppanFlg()){
			//課題No.34,90
			//追加開始
			//検索用案件No.取得
			kensakuyouAnkenNo = getKensakuyouAnkenNo(dbacc,initFlg);
			//検索用案件No.取得可能な場合
			if(!GS.EMPTY_CHARCTER.equals(kensakuyouAnkenNo)){
				//検索用フェーズ取得
				kensakuyouPhase = getKensakuyouPhase(dbacc,initFlg,kensakuyouAnkenNo);
			}
			//コメント取得
			dbacc.getCommentOC1104(kensakuyouAnkenNo,kensakuyouPhase,NOTFLGSAKI_SANSYO_POINT);
			//前回実施時の登録内容は｢今後の回収見通し｣のみ表示
			if(!kensakuyouAnkenNo.equals(tori_bean.getAnken_no())){
				form.setSonotanonaiyo(GS.EMPTY_CHARCTER);
				form.setSonotakaishunonaiyo(GS.EMPTY_CHARCTER);
				form.setRikoseikyukenennonaiyosetumei(GS.EMPTY_CHARCTER);
				form.setHikiatekinsanteikonkyononaiyosetumei(GS.EMPTY_CHARCTER);
			}
			//追加完了
			
			this.setWhenIppanSaiken();
			this.setStyle();
			//他社リスク設定処理
			dbacc.getOthriskflg(tori_bean);
			return;
		}
		
		//検索用案件No.取得
		kensakuyouAnkenNo = getKensakuyouAnkenNo(dbacc,initFlg);
		if(GS.EMPTY_CHARCTER.equals(kensakuyouAnkenNo)){
			this.calcKingaku();
			this.setStyle();
			//他社リスク設定処理
			dbacc.getOthriskflg(tori_bean);
			//検索用案件No.取得不可の場合、ここでリターン
			return;
		}else{
			//検索用フェーズ取得
			kensakuyouPhase = getKensakuyouPhase(dbacc,initFlg,kensakuyouAnkenNo);
		}
		
		//留保債務取得
		dbacc.getRyuhosaimuData(kensakuyouAnkenNo,kensakuyouPhase,tori_bean.getSystem_kbn());
		
		//査定登録データ取得
		dbacc.getSateiDataOC1104(kensakuyouAnkenNo,kensakuyouPhase,tori_bean.getSystem_kbn());
		
		//コメント取得
		dbacc.getCommentOC1104(kensakuyouAnkenNo,kensakuyouPhase,NOTFLGSAKI_SANSYO_POINT);
		
		//他社リスク設定処理
		dbacc.getOthriskflg(tori_bean);

		//金額計算
		this.calcKingaku();
		
		this.setStyle();
	}
	
	/**
	 * システム区分毎にスタイルを確定する<br>
	 */
	private void setStyle() throws Exception{
		//GSSの場合の設定
		if(GS.GSS.equals(tori_bean.getSystem_kbn())){
			this.setWhenGSS();
		}
		//MTSの場合の設定
		if(GS.MTS.equals(tori_bean.getSystem_kbn())){
			this.setWhenMTS();
		}
		//FOCUSかつUSの場合の設定
		if(GS.FOCUS.equals(tori_bean.getSystem_kbn()) && FOCUS_US.equals(tori_bean.getSateikaisya_cd())){
			this.setWhenFOCUS_US();		
		}
	}
		
	/**
	 * 四半期抽出項目取得＆スタイル設定処理<br>
	 * @param HikiateDbAcc
	 * @param boolean
	 */
	private void getShihankiChushutsuKoumoku(HikiateDbAcc dbacc,boolean initFlg) throws Exception{

		String phase = null;
		
		if(!initFlg){
			//現フェーズの登録データ取得
			dbacc.getShihankiChushutsuKoumoku(tori_bean.getAnken_no(),tori_bean.getPhase(),tori_bean.getSystem_kbn());
			dbacc.getFlgComment(tori_bean.getAnken_no(),tori_bean.getPhase(),FLGSAKI_SANSYO_POINT);
		}else if(!GS.PHASE_ICHIJI_SATEI.equals(tori_bean.getPhase())){
			//前フェーズの登録データ取得
			phase = dbacc.getZenPhase(tori_bean.getAnken_no(),tori_bean.getPhase());
			dbacc.getShihankiChushutsuKoumoku(tori_bean.getAnken_no(),phase,tori_bean.getSystem_kbn());
			dbacc.getFlgComment(tori_bean.getAnken_no(),phase,FLGSAKI_SANSYO_POINT);
		}else if(!GS.EMPTY_CHARCTER.equals(Function.trim(tori_bean.getFlg_saki_anken_no()))){
			//抽出元案件No.の登録データ取得
			phase = dbacc.getPhase(Function.trim(tori_bean.getFlg_saki_anken_no()));
			dbacc.getShihankiChushutsuKoumoku(Function.trim(tori_bean.getFlg_saki_anken_no()),phase,tori_bean.getSystem_kbn());
			dbacc.getFlgComment(Function.trim(tori_bean.getFlg_saki_anken_no()),phase,FLGSAKI_SANSYO_POINT);
		}
		
		//四半期抽出項目表示非表示フラグ設定
		if(GS.EMPTY_CHARCTER.equals(form.getShihankichushutsuflg())){
			form.setShihankichushutsukomokuFlg(HIHYOUJI);
		}else{
			form.setShihankichushutsukomokuFlg(HYOUJI);
		}
		
		//スタイル設定
		if(GS.SIHANKI.equals(tori_bean.getHanki_sihanki_kbn())){
			form.setShihankichushutsuflgRead(YOMITORI_TRUE);
			form.setFlgkbnRead(YOMITORI_TRUE);
			form.setFlgCommentRead(YOMITORI_TRUE);
			form.setShihankichushutsuflgTab(TAB_HUKA);
			form.setFlgkbnTab(TAB_HUKA);
			form.setFlgCommentTab(TAB_HUKA);
			form.setFlgCommentStyle(TXTA_HAIIRO);
		}
	}
		
	/**
	 * 一般債権計の金額計算<br>
	 */
	private void calcIppanSaikenKei(){
		double ippanSaikenKei = 	Function.getValueOfDouble(form.getUketoritegata())*100 +
									Function.getValueOfDouble(form.getYushutsuuketoritegata())*100 +
									Function.getValueOfDouble(form.getUrikakekin())*100 +
									Function.getValueOfDouble(form.getTorihikimaewatashikin())*100 +
									Function.getValueOfDouble(form.getTatekaekin())*100 +
									Function.getValueOfDouble(form.getMishunyukin())*100 +
									Function.getValueOfDouble(form.getMishushueki())*100 +
									Function.getValueOfDouble(form.getTankikashitsukekin())*100 +
									Function.getValueOfDouble(form.getSashiirehosyokin())*100 +
									Function.getValueOfDouble(form.getKaribaraikin())*100 +
									Function.getValueOfDouble(form.getChokikashitsukekin())*100 +
									Function.getValueOfDouble(form.getSonotatousi())*100;
		ippanSaikenKei = Function.marume(ippanSaikenKei);
		form.setIppansaikenkei(formatKingakuNotKanma(Double.toString(ippanSaikenKei/100),tori_bean.getSystem_kbn()));
	}

	/**
	 * 債権残高合計の金額計算<br>
	 */
	private void calcSaikenZandakaGokei(){
		double calcSaikenZandakaGokei = 	Function.getValueOfDouble(form.getIppansaikenkei())*100 +
											Function.getValueOfDouble(form.getHanyo1())*100;
		calcSaikenZandakaGokei = Function.marume(calcSaikenZandakaGokei);
		form.setSaikenzandakagokei(formatKingakuNotKanma(Double.toString(calcSaikenZandakaGokei/100),tori_bean.getSystem_kbn()));
	}

	/**
	 * 留保債務計の金額計算<br>
	 */
	private void calcRyuhosaimuKei(){
		//課題No.167
		//追加開始
		double calcSaikenZandakaGokei = Function.getValueOfDouble(form.getSaikenzandakagokei())*100;
		//追加完了
		double calcRyuhosaimuKei = 	Function.getValueOfDouble(form.getRyuhosaimu())*100 +
									Function.getValueOfDouble(form.getOthryuhosaimu())*100;
		//課題No.167
		//修正開始
		//if(calcRyuhosaimuKei < 0){
		if(calcSaikenZandakaGokei < 0){
			calcRyuhosaimuKei = 0;
		}else if(calcRyuhosaimuKei > calcSaikenZandakaGokei){
			calcRyuhosaimuKei = calcSaikenZandakaGokei;
		}
		//修正完了
		calcRyuhosaimuKei = Function.marume(calcRyuhosaimuKei);
		form.setRyuhosaimukei(formatKingakuNotKanma(Double.toString(calcRyuhosaimuKei/100),tori_bean.getSystem_kbn()));
	}
	
	/**
	 * 引当対象金額の金額計算<br>
	 */
	private void calcHikiateTaishoKingaku(){
		double calcHikiateTaishoKingaku = 	Function.getValueOfDouble(form.getSaikenzandakagokei())*100 +
											Function.getValueOfDouble(form.getRikoseikyukenen())*100 -
											Function.getValueOfDouble(form.getRyuhosaimukei())*100 -
											Function.getValueOfDouble(form.getHozen())*100 -
											Function.getValueOfDouble(form.getSonotakaishu())*100 -
											Function.getValueOfDouble(form.getKibikiatekin())*100;
		calcHikiateTaishoKingaku = Function.marume(calcHikiateTaishoKingaku);
		form.setHikiatetaishokingaku(formatKingakuNotKanma(Double.toString(calcHikiateTaishoKingaku/100),tori_bean.getSystem_kbn()));
	}
		
	/**
	 * 追加引当金額(調整後)の金額計算<br>
	 */
	private void calcTuikahikiateKingakuChoseigo(){
		double calcTuikahikiateKingakuChoseigo = 	Function.getValueOfDouble(form.getTuikahikiatekingaku())*100 +
													Function.getValueOfDouble(form.getTukachosei())*100;
		calcTuikahikiateKingakuChoseigo = Function.marume(calcTuikahikiateKingakuChoseigo);
		form.setTuikahikiatekingaku_after(formatKingakuNotKanma(Double.toString(calcTuikahikiateKingakuChoseigo/100),tori_bean.getSystem_kbn()));
	}
	
	/**
	 * 一般債権時のみの処理<br>
	 */
	private void setWhenIppanSaiken(){
		//一般債権計の金額計算
		this.calcIppanSaikenKei();
		//債権残高合計の金額計算
		this.calcSaikenZandakaGokei();
		//留保債務計の金額計算
		this.calcRyuhosaimuKei();
		//その他回収の金額計算～GSSの場合のみ実施～
		if(GS.GSS.equals(tori_bean.getSystem_kbn())){
			double sonotakaishu = Function.getValueOfDouble(form.getSaikenzandakagokei())*100 - Function.getValueOfDouble(form.getRyuhosaimukei())*100 - Function.getValueOfDouble(form.getHozen())*100 + Function.getValueOfDouble(form.getRikoseikyukenen())*100 - Function.getValueOfDouble(form.getKibikiatekin())*100;
			sonotakaishu = Function.marume(sonotakaishu);
			form.setSonotakaishu(formatKingakuNotKanma(Double.toString(sonotakaishu/100),tori_bean.getSystem_kbn()));
		}
		//引当対象金額の金額計算
		this.calcHikiateTaishoKingaku();
		//海外のみ追加引当金額(調整後)の金額計算
		if(!GS.GSS.equals(tori_bean.getSystem_kbn())){
			this.calcTuikahikiateKingakuChoseigo();
		}
		//読取専用にする
		form.setHanyo1Read(YOMITORI_TRUE);
		form.setRyuhosaimuRead(YOMITORI_TRUE);
		form.setOthRyuhosaimuRead(YOMITORI_TRUE);
		form.setHozenRead(YOMITORI_TRUE);
		form.setSonotakaishuRead(YOMITORI_TRUE);
		form.setRikoseikyukenenRead(YOMITORI_TRUE);
		form.setTuikahikiatekingakuRead(YOMITORI_TRUE);
		form.setTukachoseiRead(YOMITORI_TRUE);
        form.setOthryuhosaimunaiyakuRead(YOMITORI_TRUE);
        form.setHozenHyoRead(YOMITORI_TRUE);
        form.setRikoseikyukenennonaiyosetumeiRead(YOMITORI_TRUE);
        form.setCommentRuiRead(YOMITORI_TRUE);
		form.setOthriskflgRead(YOMITORI_TRUE);
		//タブインデックスを-1に設定
        form.setHanyo1Tab(TAB_HUKA);
        form.setRyuhosaimuTab(TAB_HUKA);
        form.setOthRyuhosaimuTab(TAB_HUKA);
        form.setHozenTab(TAB_HUKA);
        form.setSonotakaishuTab(TAB_HUKA);
        form.setRikoseikyukenenTab(TAB_HUKA);
        form.setTuikahikiatekingakuTab(TAB_HUKA);
        form.setTukachoseiTab(TAB_HUKA);
        form.setOthryuhosaimunaiyakuTab(TAB_HUKA);
        form.setHozenHyoTab(TAB_HUKA);
        form.setRikoseikyukenennonaiyosetumeiTab(TAB_HUKA);
        form.setCommentRuiTab(TAB_HUKA);
		form.setOthriskflgTab(TAB_HUKA);
    	//TDスタイルは読取不可用
        form.setHanyo1StyleTD(TD_MIZUIRO);
        form.setRyuhosaimuStyleTD(TD_MIZUIRO);
        form.setOthRyuhosaimuStyleTD(TD_MIZUIRO);
        form.setHozenStyleTD(TD_MIZUIRO);
        form.setSonotakaishuStyleTD(TD_MIZUIRO);
        form.setRikoseikyukenenStyleTD(TD_MIZUIRO);
        form.setTuikahikiatekingakuStyleTD(TD_MIZUIRO);
        form.setTukachoseiStyleTD(TD_MIZUIRO);
        form.setOthryuhosaimunaiyakuStyleTD(TD_MIZUIRO);
        form.setHozenHyoStyleTD(TD_MIZUIRO);
    	//スタイルは読取不可用
        form.setHanyo1Style(TXT_MIZUIRO);
        form.setRyuhosaimuStyle(TXT_MIZUIRO);
        form.setOthRyuhosaimuStyle(TXT_MIZUIRO);
        form.setHozenStyle(TXT_MIZUIRO);
        form.setSonotakaishuStyle(TXT_MIZUIRO);
        form.setRikoseikyukenenStyle(TXT_MIZUIRO);
        form.setTuikahikiatekingakuStyle(TXT_MIZUIRO);
        form.setTukachoseiStyle(TXT_MIZUIRO);
        form.setOthryuhosaimunaiyakuStyle(TXT_MIZUIRO);
        form.setHozenHyoStyle(TXT_MIZUIRO);
        form.setRikoseikyukenennonaiyosetumeiStyle(TXTA_MIZUIRO);
        form.setCommentRuiStyle(TXTA_MIZUIRO);
	}
	
	/**
	 * システム区分がMTS時のみの処理<br>
	 */
	private void setWhenMTS(){
		//未使用項目に空文字セット
		form.setHanyo1(GS.EMPTY_CHARCTER);
		form.setOthryuhosaimu(GS.EMPTY_CHARCTER);
		form.setRikoseikyukenen(GS.EMPTY_CHARCTER);
		form.setTukachosei(GS.EMPTY_CHARCTER);
		form.setKanjo_cd1(GS.EMPTY_CHARCTER);
		form.setKanjo_kamoku1(GS.EMPTY_CHARCTER);
		form.setTori_nm1(GS.EMPTY_CHARCTER);
		form.setKingaku1(GS.EMPTY_CHARCTER);
		form.setKanjo_cd2(GS.EMPTY_CHARCTER);
		form.setKanjo_kamoku2(GS.EMPTY_CHARCTER);
		form.setTori_nm2(GS.EMPTY_CHARCTER);
		form.setKingaku2(GS.EMPTY_CHARCTER);
		form.setKanjo_cd3(GS.EMPTY_CHARCTER);
		form.setKanjo_kamoku3(GS.EMPTY_CHARCTER);
		form.setTori_nm3(GS.EMPTY_CHARCTER);
		form.setKingaku3(GS.EMPTY_CHARCTER);
		form.setRikoseikyukenennonaiyosetumei(GS.EMPTY_CHARCTER);
		//読取専用にする
		form.setHanyo1Read(YOMITORI_TRUE);
		form.setOthRyuhosaimuRead(YOMITORI_TRUE);
		form.setRikoseikyukenenRead(YOMITORI_TRUE);
		form.setTukachoseiRead(YOMITORI_TRUE);
        form.setOthryuhosaimunaiyakuRead(YOMITORI_TRUE);
        form.setRikoseikyukenennonaiyosetumeiRead(YOMITORI_TRUE);
		//タブインデックスを-1に設定
        form.setHanyo1Tab(TAB_HUKA);
        form.setOthRyuhosaimuTab(TAB_HUKA);
        form.setRikoseikyukenenTab(TAB_HUKA);
        form.setTukachoseiTab(TAB_HUKA);
        form.setOthryuhosaimunaiyakuTab(TAB_HUKA);
        form.setRikoseikyukenennonaiyosetumeiTab(TAB_HUKA);
    	//TDスタイルは読取不可用
        form.setHanyo1StyleTD(TD_MIZUIRO);
        form.setOthRyuhosaimuStyleTD(TD_MIZUIRO);
        form.setRikoseikyukenenStyleTD(TD_MIZUIRO);
        form.setTukachoseiStyleTD(TD_MIZUIRO);
        form.setOthryuhosaimunaiyakuStyleTD(TD_MIZUIRO);
    	//スタイルは読取不可用
        form.setHanyo1Style(TXT_MIZUIRO);
        form.setOthRyuhosaimuStyle(TXT_MIZUIRO);
        form.setRikoseikyukenenStyle(TXT_MIZUIRO);
        form.setTukachoseiStyle(TXT_MIZUIRO);
        form.setOthryuhosaimunaiyakuStyle(TXT_MIZUIRO);
        form.setRikoseikyukenennonaiyosetumeiStyle(TXTA_MIZUIRO);
	}
	
	/**
	 * システム区分がFOCUSでUS時のみの処理<br>
	 */
	private void setWhenFOCUS_US(){
		//未使用項目に空文字セット
		form.setOthryuhosaimu(GS.EMPTY_CHARCTER);
		form.setRikoseikyukenen(GS.EMPTY_CHARCTER);
		form.setKanjo_cd1(GS.EMPTY_CHARCTER);
		form.setKanjo_kamoku1(GS.EMPTY_CHARCTER);
		form.setTori_nm1(GS.EMPTY_CHARCTER);
		form.setKingaku1(GS.EMPTY_CHARCTER);
		form.setKanjo_cd2(GS.EMPTY_CHARCTER);
		form.setKanjo_kamoku2(GS.EMPTY_CHARCTER);
		form.setTori_nm2(GS.EMPTY_CHARCTER);
		form.setKingaku2(GS.EMPTY_CHARCTER);
		form.setKanjo_cd3(GS.EMPTY_CHARCTER);
		form.setKanjo_kamoku3(GS.EMPTY_CHARCTER);
		form.setTori_nm3(GS.EMPTY_CHARCTER);
		form.setKingaku3(GS.EMPTY_CHARCTER);
		form.setRikoseikyukenennonaiyosetumei(GS.EMPTY_CHARCTER);
		//読取専用にする
		form.setOthRyuhosaimuRead(YOMITORI_TRUE);
		form.setRikoseikyukenenRead(YOMITORI_TRUE);
        form.setOthryuhosaimunaiyakuRead(YOMITORI_TRUE);
        form.setRikoseikyukenennonaiyosetumeiRead(YOMITORI_TRUE);
		//タブインデックスを-1に設定
        form.setOthRyuhosaimuTab(TAB_HUKA);
        form.setRikoseikyukenenTab(TAB_HUKA);
        form.setOthryuhosaimunaiyakuTab(TAB_HUKA);
        form.setRikoseikyukenennonaiyosetumeiTab(TAB_HUKA);
    	//TDスタイルは読取不可用
        form.setOthRyuhosaimuStyleTD(TD_MIZUIRO);
        form.setRikoseikyukenenStyleTD(TD_MIZUIRO);
        form.setOthryuhosaimunaiyakuStyleTD(TD_MIZUIRO);
    	//スタイルは読取不可用
        form.setOthRyuhosaimuStyle(TXT_MIZUIRO);
        form.setRikoseikyukenenStyle(TXT_MIZUIRO);
        form.setOthryuhosaimunaiyakuStyle(TXT_MIZUIRO);
        form.setRikoseikyukenennonaiyosetumeiStyle(TXTA_MIZUIRO);
	}
	
	/**
	 * システム区分がGSS時のみの処理<br>
	 */
	private void setWhenGSS(){
		//未使用項目に空文字セット
		form.setTuikahikiatekingaku_after(GS.EMPTY_CHARCTER);
		form.setTukachosei(GS.EMPTY_CHARCTER);
		//読取専用にする
		form.setHanyo1Read(YOMITORI_TRUE);
    	//TDスタイルは読取不可用
        form.setHanyo1StyleTD(TD_MIZUIRO);
		//タブインデックスを-1に設定
        form.setHanyo1Tab(TAB_HUKA);
    	//スタイルは読取不可用
        form.setHanyo1Style(TXT_MIZUIRO);
	}
	
	//IT041対応
	/**
	 * 金額計算処理<br>
	 */
	private void calcKingaku(){
		//一般債権計の金額計算
		this.calcIppanSaikenKei();
		//債権残高合計の金額計算
		this.calcSaikenZandakaGokei();
		//留保債務計の金額計算
		this.calcRyuhosaimuKei();
		//引当対象金額の金額計算
		this.calcHikiateTaishoKingaku();
		//海外のみ追加引当金額(調整後)の金額計算
		if(!GS.GSS.equals(tori_bean.getSystem_kbn())){
			this.calcTuikahikiateKingakuChoseigo();
		}
	}
	//IT041ここまで
	
	/**
	 * 保存処理<br>
     * @param boolean
	 */
	public void execHozon(boolean flg) throws Exception{

		//実施業務取得
    	HashMap map = this.getJishiGyoumu(tori_bean.getPhase(),GS.STATUS_MISYORI);
    	
    	HikiateDbAcc dbacc = new HikiateDbAcc(appContext);
    	
		//コメントの登録
		dbacc.deleteComment(tori_bean,SANSYO_POINT);
		dbacc.insertComment(tori_bean,POINT_SONOTANONAIYO,form.getSonotanonaiyo());
		dbacc.insertComment(tori_bean,POINT_SONOTAKAISHUNONAIYO,form.getSonotakaishunonaiyo());
		dbacc.insertComment(tori_bean,POINT_RIKOSEIKYUKENEN,form.getRikoseikyukenennonaiyosetumei());
		dbacc.insertComment(tori_bean,POINT_HIKIATEKINSANTEIKONKYO,form.getHikiatekinsanteikonkyononaiyosetumei());
		dbacc.insertComment(tori_bean,POINT_KONGONOKAISHUMITOSHI,form.getKongonokaishumitoshi());
		dbacc.insertComment(tori_bean,POINT_FLGCOMMENT,form.getFlgcomment());
		
		//査定内容登録
		dbacc.updateSateiData(tori_bean);
		
		//第三者留保債務登録
		dbacc.insertOthRyuhoSaimu(tori_bean);
		
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
	 * 登録処理<br>
	 */
	public void execRegister() throws Exception{

		//実施業務取得
    	HashMap map = this.getJishiGyoumu(tori_bean.getPhase(),GS.STATUS_MISYORI);
    	
        //更新ユーザ判定
		String upd_user_id = user_bean.getComUserId();
		if(!GS.EMPTY_CHARCTER.equals(Function.trim(user_bean.getComDaiko_userId()))){
			upd_user_id = Function.trim(user_bean.getComDaiko_userId());
		}

		HikiateDbAcc dbacc = new HikiateDbAcc(appContext);

    	//コメントの登録
		dbacc.deleteComment(tori_bean,SANSYO_POINT);
		dbacc.insertComment(tori_bean,POINT_SONOTANONAIYO,form.getSonotanonaiyo());
		dbacc.insertComment(tori_bean,POINT_SONOTAKAISHUNONAIYO,form.getSonotakaishunonaiyo());
		dbacc.insertComment(tori_bean,POINT_RIKOSEIKYUKENEN,form.getRikoseikyukenennonaiyosetumei());
		dbacc.insertComment(tori_bean,POINT_HIKIATEKINSANTEIKONKYO,form.getHikiatekinsanteikonkyononaiyosetumei());
		dbacc.insertComment(tori_bean,POINT_KONGONOKAISHUMITOSHI,form.getKongonokaishumitoshi());
		dbacc.insertComment(tori_bean,POINT_FLGCOMMENT,form.getFlgcomment());

		//査定内容登録
		dbacc.updateSateiData(tori_bean);
		
		//第三者留保債務登録
		dbacc.insertOthRyuhoSaimu(tori_bean);

		//コミット
		dbacc.commit();

		//次フェーズ登録処理
		if(!(((String)map.get(GS.JISHI_PHASE_KANRYO_FLG)).equals(KANRYOU_FLG)) && ((String)map.get(GS.JI_KAISHI_STATUS)).equals(GS.STATUS_MISYORI)){
			dbacc.setSateiData(tori_bean,map);
    		dbacc.setRyuhosaimu(tori_bean,map);
    		dbacc.setOthRyuhosaimu(tori_bean,map);
    	}
		if(!((String)map.get(GS.JISHI_PHASE_KANRYO_FLG)).equals(KANRYOU_FLG)){
    		dbacc.setMailHaishin(upd_user_id,form.getShoninsha(),tori_bean,map);
    	//課題No.60
        //追加開始
    	}else if(!(GS.GSS.equals(tori_bean.getSystem_kbn()))){
    		//既に引当金確認が開始されている場合、引当金確認案件の取引先区分と案件No.を更新
    		String hikiateKakuninAnkenNo = dbacc.getHikiateKakuninAnkenNo(tori_bean);
    		if(!(GS.EMPTY_CHARCTER.equals(hikiateKakuninAnkenNo))){
        		Map<String,String> kbnMap = dbacc.getSaikenToriKbn(tori_bean);
        		dbacc.setHikiateKakuninSaikenToriKbn(hikiateKakuninAnkenNo,kbnMap.get(SAIKEN_KBN),kbnMap.get(TORIHIKISAKI_KBN));
    		}
        //追加完了
    	}
    	
		//進捗更新
		dbacc.setSateiStat(tori_bean,map,form.getShoninsha());
		
		//入力履歴登録
		dbacc.setNyuryokuHist(tori_bean,NYURYOKU_TOROKU,form.getShoninsha());

		//コミット
		dbacc.commit();
	}
}
