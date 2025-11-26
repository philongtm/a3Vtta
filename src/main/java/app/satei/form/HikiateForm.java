/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.form;

import common.global.GS;
import common.struts.AppPagerActionForm;
import common.util.Function;
import common.struts.adapter.action.ActionMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

/**
 * OC1104_査定_引当金判定 アクションフォームクラス
 * 
 */
public class HikiateForm extends AppPagerActionForm {
	
	//カンマ補正はJavaScriptにて実施
	//更新登録用金額項目は全てセッター内にてカンマ除去
	//入力項目は全てセッター内にてトリム
	
	//一般債権ならtrue
	private boolean ippanFlg;
	
	//読取判定～読取専用ならtrue、readonlyやdisabledで使用～
    private boolean hanyo1Read;								//汎用1読取
	private boolean ryuhosaimuRead;							//留保債務読取
	private boolean othRyuhosaimuRead;						//第三者留保債務読取
	private boolean hozenRead;								//保全読取
    private boolean sonotakaishuRead;						//その他回収読取
    private boolean rikoseikyukenenRead;					//履行請求懸念読取
    private boolean tuikahikiatekingakuRead;				//追加引当金額読取
    private boolean tukachoseiRead;							//通貨調整読取(海外のみ)
    private boolean othryuhosaimunaiyakuRead;				//[第三者留保債務内訳]読取
    private boolean hozenHyoRead;							//[保全]読取
    private boolean othriskflgRead;							//他社リスクフラグ読取
    private boolean rikoseikyukenennonaiyosetumeiRead;		//｢履行請求懸念｣の内容説明読取
    private boolean commentRuiRead;							//コメント類読取
    private boolean shihankichushutsuflgRead;				//四半期抽出フラグ読取
    private boolean flgkbnRead;								//フラグ区分読取
    private boolean flgCommentRead;							//フラグコメント読取
	//スタイル
    private String hanyo1Style;							//汎用1スタイル
	private String ryuhosaimuStyle;						//留保債務スタイル
	private String othRyuhosaimuStyle;						//第三者留保債務スタイル
	private String hozenStyle;								//保全スタイル
    private String sonotakaishuStyle;						//その他回収スタイル
    private String rikoseikyukenenStyle;					//履行請求懸念スタイル
    private String tuikahikiatekingakuStyle;				//追加引当金額スタイル
    private String tukachoseiStyle;						//通貨調整スタイル(海外のみ)
    private String othryuhosaimunaiyakuStyle;				//[第三者留保債務内訳]スタイル
    private String hozenHyoStyle;							//[保全]スタイル
    private String rikoseikyukenennonaiyosetumeiStyle;		//｢履行請求懸念｣の内容説明スタイル
    private String commentRuiStyle;						//コメント類スタイル
    private String flgCommentStyle;						//フラグコメントスタイル
	//スタイルTD
    private String hanyo1StyleTD;							//汎用1スタイルTD
	private String ryuhosaimuStyleTD;						//留保債務スタイルTD
	private String othRyuhosaimuStyleTD;					//第三者留保債務スタイルTD
	private String hozenStyleTD;							//保全スタイルTD
    private String sonotakaishuStyleTD;					//その他回収スタイルTD
    private String rikoseikyukenenStyleTD;					//履行請求懸念スタイルTD
    private String tuikahikiatekingakuStyleTD;				//追加引当金額スタイルTD
    private String tukachoseiStyleTD;						//通貨調整スタイルTD(海外のみ)
    private String othryuhosaimunaiyakuStyleTD;			//[第三者留保債務内訳]スタイルTD
    private String hozenHyoStyleTD;						//[保全]スタイルTD
	//タブインデックス～読取専用なら-1、そうでないなら0～
    private String hanyo1Tab;								//汎用1タブ
	private String ryuhosaimuTab;							//留保債務タブ
	private String othRyuhosaimuTab;						//第三者留保債務タブ
	private String hozenTab;								//保全タブ
    private String sonotakaishuTab;						//その他回収タブ
    private String rikoseikyukenenTab;						//履行請求懸念タブ
    private String tuikahikiatekingakuTab;					//追加引当金額タブ
    private String tukachoseiTab;							//通貨調整タブ(海外のみ)
    private String othryuhosaimunaiyakuTab;				//[第三者留保債務内訳]タブ
    private String hozenHyoTab;							//[保全]タブ
    private String othriskflgTab;							//他社リスクフラグタブ
    private String rikoseikyukenennonaiyosetumeiTab;		//｢履行請求懸念｣の内容説明タブ
    private String commentRuiTab;							//コメント類タブ
    private String shihankichushutsuflgTab;				//四半期抽出フラグタブ
    private String flgkbnTab;								//フラグ区分タブ
    private String flgCommentTab;							//フラグコメントタブ
    //表示非表示フラグ～非表示なら0、表示は1～
    private String shihankichushutsukomokuFlg;				//四半期抽出項目表示フラグ
    private String shoninshaListFlg;						//承認者セレクトボックス表示フラグ
    
    //P02_区分より
    private String hanyo1title;							//国内：固定化営業債権、海外：通貨調整
	//T12_コメントより
    private String sonotanonaiyo;							//その他の内容(登録箇所：50)
    private String sonotakaishunonaiyo;					//その他回収の内容(登録箇所：60)
    private String rikoseikyukenennonaiyosetumei;			//｢履行請求懸念｣の内容説明(登録箇所：70)
    private String hikiatekinsanteikonkyononaiyosetumei;	//｢引当金算定根拠｣の内容説明(登録箇所：80)
    private String kongonokaishumitoshi;					//今後の回収見通しなど(登録箇所：90)
    private String flgcomment;								//フラグコメント(登録箇所：95)
	//T20_第三者留保債務より
    private String kanjo_cd1;					//勘定先コード1
    private String tori_nm1;					//取引先名1
    private String kanjo_kamoku1;				//勘定科目1
    private String kingaku1;					//金額1
    private String kanjo_cd2;					//勘定先コード2
    private String tori_nm2;					//取引先名2
    private String kanjo_kamoku2;				//勘定科目2
    private String kingaku2;					//金額2
    private String kanjo_cd3;					//勘定先コード3
    private String tori_nm3;					//取引先名3
    private String kanjo_kamoku3;				//勘定科目3
    private String kingaku3;					//金額3
	//国内：T17_引当金判定、海外：T15_一次二次査定より
    private String hanyo1;						//汎用１(国内：勘定科目表示区分'16'、海外：項目１)
	//T15_一次二次査定より
    private String ryuhosaimu;					//留保債務
    private String othryuhosaimu;				//第三者留保債務
    private String hozen;						//保全
    private String sonotakaishu;				//その他回収
    private String rikoseikyukenen;			//履行請求懸念
    private String tuikahikiatekingaku;		//追加引当金額(NULLの場合は、海外版：'0.00'、国内版：'0')
    private String tukachosei;					//通貨調整(海外のみ)
    private String keiyakugaku_hudousantanpo;	//契約額-不動産担保
    private String keiyakugaku_dousantanpo;	//契約額-動産担保
    private String keiyakugaku_bouekihoken;	//契約額-貿易保険
    private String keiyakugaku_sonota;			//契約額-その他
    private String hyokagaku_hudousantanpo;	//評価額-不動産担保
    private String hyokagaku_dousantanpo;		//評価額-動産担保
    private String hyokagaku_bouekihoken;		//評価額-貿易保険
    private String hyokagaku_sonota;			//評価額-その他
    private String shihankichushutsuflg;		//四半期抽出フラグ
    private String flgkbn;						//フラグ区分
    private String othriskflg;					//他社リスクフラグ(T10_滞留判定も参照)
	//T17_引当金判定より
    private String tani;						//単位
    private String uketoritegata;				//受取手形(勘定科目表示区分'1')
    private String yushutsuuketoritegata;		//輸出受取手形(勘定科目表示区分'2')
    private String urikakekin;					//売掛金(勘定科目表示区分'3')
    private String torihikimaewatashikin;		//取引前渡金(勘定科目表示区分'4')
    private String tatekaekin;					//立替金(勘定科目表示区分'5')
    private String mishunyukin;				//未収入金(勘定科目表示区分'6')
    private String mishushueki;				//未収収益(勘定科目表示区分'7')
    private String tankikashitsukekin;			//短期貸付金(勘定科目表示区分'8')
    private String sashiirehosyokin;			//差入保証金(勘定科目表示区分'9')
    private String karibaraikin;				//仮払金(勘定科目表示区分'10')
    private String chokikashitsukekin;			//長期貸付金(勘定科目表示区分'11')
    private String sonotatousi;				//その他投資(勘定科目表示区分'12')
    private String hoshosaimugokei;			//保証債務合計(勘定科目表示区分'14')
    private String kibikiatekin;				//既引当金(勘定科目表示区分'15')
    //合計値
    private String ippansaikenkei;				//一般債権計(受取手形＋その他投資)
    private String saikenzandakagokei;			//債権残高合計(一般債権＋汎用１)(一般債権計＋汎用１)
    private String ryuhosaimukei;				//留保債務計(留保債務＋第三者留保債務)(NULLの場合は、海外版：'0.00'、国内版：'0')
    private String hikiatetaishokingaku;		//引当対象金額(債権残高合計－(留保債務計＋保全＋その他回収)＋履行請求懸念－既引当金)
    private String tuikahikiatekingaku_after;	//追加引当金額(調整後)(追加引当金額＋通貨調整)(海外のみ)
    //セレクトボックス
    private LinkedHashMap flgKbnList;			//フラグ区分セレクトボックス
    private LinkedHashMap shoninList;			//承認者セレクトボックス
    private String shoninsha;					//承認者
    
    //スタイル用グローバル定数
    public static final String TD_MIZUIRO		= "OC1104_TD_MIZUIRO";
    public static final String TD_SIRO		= "OC1104_TD_SIRO";
    public static final String TD_KIIRO		= "OC1104_TD_KIIRO";
    public static final String TXT_MIZUIRO	= "OC1104_TXT_MIZUIRO";
    public static final String TXT_SIRO		= "OC1104_TXT_SIRO";
    public static final String TXT_KIIRO		= "OC1104_TXT_KIIRO";
    public static final String TXTA_MIZUIRO	= "OC1104_TXTA_MIZUIRO";
    public static final String TXTA_SIRO		= "OC1104_TXTA_SIRO";
    public static final String TXTA_HAIIRO	= "OC1104_TXTA_HAIIRO";
    public static final String TAB_KA			= "0";
    public static final String TAB_HUKA		= "-1";
    public static final boolean YOMITORI_TRUE	= true;
    public static final boolean YOMITORI_FALSE	= false;
    public static final String ZERO			= "0";
    
    public HikiateForm() {
    	super.gamenId						= GS.OC1104;
    	flgKbnList							= null;
    	shoninList							= null;
    	ippanFlg							= false;
    	//デフォルト読取可
        hanyo1Read							= YOMITORI_FALSE;
    	ryuhosaimuRead						= YOMITORI_FALSE;
    	othRyuhosaimuRead					= YOMITORI_FALSE;
    	hozenRead							= YOMITORI_FALSE;
        sonotakaishuRead					= YOMITORI_FALSE;
        rikoseikyukenenRead					= YOMITORI_FALSE;
        tuikahikiatekingakuRead				= YOMITORI_FALSE;
        tukachoseiRead						= YOMITORI_FALSE;
        othryuhosaimunaiyakuRead			= YOMITORI_FALSE;
        hozenHyoRead						= YOMITORI_FALSE;
        othriskflgRead						= YOMITORI_FALSE;
        rikoseikyukenennonaiyosetumeiRead	= YOMITORI_FALSE;
        commentRuiRead						= YOMITORI_FALSE;
        shihankichushutsuflgRead			= YOMITORI_FALSE;
        flgkbnRead							= YOMITORI_FALSE;
        flgCommentRead						= YOMITORI_FALSE;
    	//デフォルトTDスタイルは読取可用
        hanyo1StyleTD						= TD_SIRO;
    	ryuhosaimuStyleTD					= TD_KIIRO;
    	othRyuhosaimuStyleTD				= TD_KIIRO;
    	hozenStyleTD						= TD_KIIRO;
        sonotakaishuStyleTD					= TD_SIRO;
        rikoseikyukenenStyleTD				= TD_SIRO;
        tuikahikiatekingakuStyleTD			= TD_SIRO;
        tukachoseiStyleTD					= TD_SIRO;
        othryuhosaimunaiyakuStyleTD			= TD_SIRO;
        hozenHyoStyleTD						= TD_SIRO;
    	//デフォルトタブは0
        hanyo1Tab							= TAB_KA;
    	ryuhosaimuTab						= TAB_KA;
    	othRyuhosaimuTab					= TAB_KA;
    	hozenTab							= TAB_KA;
        sonotakaishuTab						= TAB_KA;
        rikoseikyukenenTab					= TAB_KA;
        tuikahikiatekingakuTab				= TAB_KA;
        tukachoseiTab						= TAB_KA;
        othryuhosaimunaiyakuTab				= TAB_KA;
        hozenHyoTab							= TAB_KA;
        othriskflgTab						= TAB_KA;
        rikoseikyukenennonaiyosetumeiTab	= TAB_KA;
        commentRuiTab						= TAB_KA;
        shihankichushutsuflgTab				= TAB_KA;
        flgkbnTab							= TAB_KA;
        flgCommentTab						= TAB_KA;
        //デフォルトスタイルは読取可用
        hanyo1Style							= TXT_SIRO;
    	ryuhosaimuStyle						= TXT_KIIRO;
    	othRyuhosaimuStyle					= TXT_KIIRO;
    	hozenStyle							= TXT_KIIRO;
        sonotakaishuStyle					= TXT_SIRO;
        rikoseikyukenenStyle				= TXT_SIRO;
        tuikahikiatekingakuStyle			= TXT_SIRO;
        tukachoseiStyle						= TXT_SIRO;
        othryuhosaimunaiyakuStyle			= TXT_SIRO;
        hozenHyoStyle						= TXT_SIRO;
        rikoseikyukenennonaiyosetumeiStyle	= TXTA_SIRO;
        commentRuiStyle						= TXTA_SIRO;
        flgCommentStyle						= TXTA_SIRO;

    	shihankichushutsukomokuFlg			= GS.EMPTY_CHARCTER;
    	shoninshaListFlg					= GS.EMPTY_CHARCTER;
    	
    	hanyo1title							= GS.EMPTY_CHARCTER;
    	sonotanonaiyo						= GS.EMPTY_CHARCTER;
    	sonotakaishunonaiyo					= GS.EMPTY_CHARCTER;
    	rikoseikyukenennonaiyosetumei		= GS.EMPTY_CHARCTER;
    	hikiatekinsanteikonkyononaiyosetumei= GS.EMPTY_CHARCTER;
    	kongonokaishumitoshi				= GS.EMPTY_CHARCTER;
    	flgcomment							= GS.EMPTY_CHARCTER;
    	kanjo_cd1							= GS.EMPTY_CHARCTER;
    	tori_nm1							= GS.EMPTY_CHARCTER;
    	kanjo_kamoku1						= GS.EMPTY_CHARCTER;
    	kingaku1							= GS.EMPTY_CHARCTER;
    	kanjo_cd2							= GS.EMPTY_CHARCTER;
    	tori_nm2							= GS.EMPTY_CHARCTER;
    	kanjo_kamoku2						= GS.EMPTY_CHARCTER;
    	kingaku2							= GS.EMPTY_CHARCTER;
    	kanjo_cd3							= GS.EMPTY_CHARCTER;
    	tori_nm3							= GS.EMPTY_CHARCTER;
    	kanjo_kamoku3						= GS.EMPTY_CHARCTER;
    	kingaku3							= GS.EMPTY_CHARCTER;
    	hanyo1								= GS.EMPTY_CHARCTER;
    	ryuhosaimu							= GS.EMPTY_CHARCTER;
    	othryuhosaimu						= GS.EMPTY_CHARCTER;
    	hozen								= GS.EMPTY_CHARCTER;
    	sonotakaishu						= GS.EMPTY_CHARCTER;
    	rikoseikyukenen						= GS.EMPTY_CHARCTER;
    	tuikahikiatekingaku					= ZERO;
    	tukachosei							= GS.EMPTY_CHARCTER;
    	keiyakugaku_hudousantanpo			= GS.EMPTY_CHARCTER;
    	keiyakugaku_dousantanpo				= GS.EMPTY_CHARCTER;
    	keiyakugaku_bouekihoken				= GS.EMPTY_CHARCTER;
    	keiyakugaku_sonota					= GS.EMPTY_CHARCTER;
    	hyokagaku_hudousantanpo				= GS.EMPTY_CHARCTER;
    	hyokagaku_dousantanpo				= GS.EMPTY_CHARCTER;
    	hyokagaku_bouekihoken				= GS.EMPTY_CHARCTER;
    	hyokagaku_sonota					= GS.EMPTY_CHARCTER;
    	shihankichushutsuflg				= GS.EMPTY_CHARCTER;
    	flgkbn								= GS.EMPTY_CHARCTER;
    	othriskflg							= GS.OFF;
    	tani								= GS.EMPTY_CHARCTER;
    	uketoritegata						= GS.EMPTY_CHARCTER;
    	yushutsuuketoritegata				= GS.EMPTY_CHARCTER;
    	urikakekin							= GS.EMPTY_CHARCTER;
    	torihikimaewatashikin				= GS.EMPTY_CHARCTER;
    	tatekaekin							= GS.EMPTY_CHARCTER;
    	mishunyukin							= GS.EMPTY_CHARCTER;
    	mishushueki							= GS.EMPTY_CHARCTER;
    	tankikashitsukekin					= GS.EMPTY_CHARCTER;
    	sashiirehosyokin					= GS.EMPTY_CHARCTER;
    	karibaraikin						= GS.EMPTY_CHARCTER;
    	chokikashitsukekin					= GS.EMPTY_CHARCTER;
    	sonotatousi							= GS.EMPTY_CHARCTER;
    	hoshosaimugokei						= GS.EMPTY_CHARCTER;
    	kibikiatekin						= GS.EMPTY_CHARCTER;
    	ippansaikenkei						= GS.EMPTY_CHARCTER;
    	saikenzandakagokei					= GS.EMPTY_CHARCTER;
    	ryuhosaimukei						= GS.EMPTY_CHARCTER;
    	hikiatetaishokingaku				= GS.EMPTY_CHARCTER;
    	tuikahikiatekingaku_after			= GS.EMPTY_CHARCTER;
    	shoninsha							= GS.EMPTY_CHARCTER;
    }

    /**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}
	
    /**
	 * NULLなら空文字変換、それ以外はトリム
	 * @param String
	 * 				ストリングオブジェクト
	 */	
	public String getVal(String val) {
	 	if(val == null){
	 		val = GS.EMPTY_CHARCTER;
	 	}else{
	 		val = val.trim();
	 	}
	 	return val;
	}

	/**
	 * チェックボックス用リセット処理
	 */	
	public void reset(ActionMapping aMap, HttpServletRequest req) {

		//読取専用時チェックオンでもリクエストは送信されないことを要注意!!
		if(this.othriskflgRead == YOMITORI_FALSE){
			this.othriskflg = GS.OFF;
		}
		if(this.shihankichushutsuflgRead == YOMITORI_FALSE){
			this.shihankichushutsuflg = GS.EMPTY_CHARCTER;
		}
	}

	/**
	 * NULLなら空文字変換、それ以外はカンマ除去＆トリム
	 * @param String
	 * 				ストリングオブジェクト
	 */	
	public String getRemoveKanmaVal(String val) {
	 	if(val == null){
	 		val = GS.EMPTY_CHARCTER;
	 	}else{
	 		val = Function.removeComma(val.trim());
	 	}
	 	return val;
	}

	public String getChokikashitsukekin() {
		return chokikashitsukekin;
	}
	public void setChokikashitsukekin(String chokikashitsukekin) {
		this.chokikashitsukekin = chokikashitsukekin;
	}
	public String getCommentRuiStyle() {
		return commentRuiStyle;
	}
	public void setCommentRuiStyle(String commentRuiStyle) {
		this.commentRuiStyle = commentRuiStyle;
	}
	public String getFlgcomment() {
		return flgcomment;
	}
	public void setFlgcomment(String flgcomment) {
		this.flgcomment = getVal(flgcomment);
	}
	public String getFlgkbn() {
		return flgkbn;
	}
	public void setFlgkbn(String flgkbn) {
		this.flgkbn = flgkbn;
	}
	public LinkedHashMap getFlgKbnList() {
		return flgKbnList;
	}
	public void setFlgKbnList(LinkedHashMap flgKbnList) {
		this.flgKbnList = flgKbnList;
	}
	public String getHanyo1() {
		return hanyo1;
	}
	public void setHanyo1(String hanyo1) {
		this.hanyo1 = this.getRemoveKanmaVal(hanyo1);
	}
	public String getHanyo1Style() {
		return hanyo1Style;
	}
	public void setHanyo1Style(String hanyo1Style) {
		this.hanyo1Style = hanyo1Style;
	}
	public String getHanyo1title() {
		return hanyo1title;
	}
	public void setHanyo1title(String hanyo1title) {
		this.hanyo1title = hanyo1title;
	}
	public String getHikiatekinsanteikonkyononaiyosetumei() {
		return hikiatekinsanteikonkyononaiyosetumei;
	}
	public void setHikiatekinsanteikonkyononaiyosetumei(
			String hikiatekinsanteikonkyononaiyosetumei) {
		this.hikiatekinsanteikonkyononaiyosetumei = getVal(hikiatekinsanteikonkyononaiyosetumei);
	}
	public String getHikiatetaishokingaku() {
		return hikiatetaishokingaku;
	}
	public void setHikiatetaishokingaku(String hikiatetaishokingaku) {
		this.hikiatetaishokingaku = hikiatetaishokingaku;
	}
	public String getHoshosaimugokei() {
		return hoshosaimugokei;
	}
	public void setHoshosaimugokei(String hoshosaimugokei) {
		this.hoshosaimugokei = hoshosaimugokei;
	}
	public String getHozen() {
		return hozen;
	}
	public void setHozen(String hozen) {
		this.hozen = getRemoveKanmaVal(hozen);
	}
	public String getHozenHyoStyle() {
		return hozenHyoStyle;
	}
	public void setHozenHyoStyle(String hozenHyoStyle) {
		this.hozenHyoStyle = hozenHyoStyle;
	}
	public String getHozenStyle() {
		return hozenStyle;
	}
	public void setHozenStyle(String hozenStyle) {
		this.hozenStyle = hozenStyle;
	}
	public String getHyokagaku_bouekihoken() {
		return hyokagaku_bouekihoken;
	}
	public void setHyokagaku_bouekihoken(String hyokagaku_bouekihoken) {
		this.hyokagaku_bouekihoken = getRemoveKanmaVal(hyokagaku_bouekihoken);
	}
	public String getHyokagaku_dousantanpo() {
		return hyokagaku_dousantanpo;
	}
	public void setHyokagaku_dousantanpo(String hyokagaku_dousantanpo) {
		this.hyokagaku_dousantanpo = getRemoveKanmaVal(hyokagaku_dousantanpo);
	}
	public String getHyokagaku_hudousantanpo() {
		return hyokagaku_hudousantanpo;
	}
	public void setHyokagaku_hudousantanpo(String hyokagaku_hudousantanpo) {
		this.hyokagaku_hudousantanpo = getRemoveKanmaVal(hyokagaku_hudousantanpo);
	}
	public String getHyokagaku_sonota() {
		return hyokagaku_sonota;
	}
	public void setHyokagaku_sonota(String hyokagaku_sonota) {
		this.hyokagaku_sonota = getRemoveKanmaVal(hyokagaku_sonota);
	}
	public String getIppansaikenkei() {
		return ippansaikenkei;
	}
	public void setIppansaikenkei(String ippansaikenkei) {
		this.ippansaikenkei = ippansaikenkei;
	}
	public String getKanjo_cd1() {
		return kanjo_cd1;
	}
	public void setKanjo_cd1(String kanjo_cd1) {
		this.kanjo_cd1 = getVal(kanjo_cd1);
	}
	public String getKanjo_cd2() {
		return kanjo_cd2;
	}
	public void setKanjo_cd2(String kanjo_cd2) {
		this.kanjo_cd2 = getVal(kanjo_cd2);
	}
	public String getKanjo_cd3() {
		return kanjo_cd3;
	}
	public void setKanjo_cd3(String kanjo_cd3) {
		this.kanjo_cd3 = getVal(kanjo_cd3);
	}
	public String getKanjo_kamoku1() {
		return kanjo_kamoku1;
	}
	public void setKanjo_kamoku1(String kanjo_kamoku1) {
		this.kanjo_kamoku1 = getVal(kanjo_kamoku1);
	}
	public String getKanjo_kamoku2() {
		return kanjo_kamoku2;
	}
	public void setKanjo_kamoku2(String kanjo_kamoku2) {
		this.kanjo_kamoku2 = getVal(kanjo_kamoku2);
	}
	public String getKanjo_kamoku3() {
		return kanjo_kamoku3;
	}
	public void setKanjo_kamoku3(String kanjo_kamoku3) {
		this.kanjo_kamoku3 = getVal(kanjo_kamoku3);
	}
	public String getKaribaraikin() {
		return karibaraikin;
	}
	public void setKaribaraikin(String karibaraikin) {
		this.karibaraikin = karibaraikin;
	}
	public String getKeiyakugaku_bouekihoken() {
		return keiyakugaku_bouekihoken;
	}
	public void setKeiyakugaku_bouekihoken(String keiyakugaku_bouekihoken) {
		this.keiyakugaku_bouekihoken = getRemoveKanmaVal(keiyakugaku_bouekihoken);
	}
	public String getKeiyakugaku_dousantanpo() {
		return keiyakugaku_dousantanpo;
	}
	public void setKeiyakugaku_dousantanpo(String keiyakugaku_dousantanpo) {
		this.keiyakugaku_dousantanpo = getRemoveKanmaVal(keiyakugaku_dousantanpo);
	}
	public String getKeiyakugaku_hudousantanpo() {
		return keiyakugaku_hudousantanpo;
	}
	public void setKeiyakugaku_hudousantanpo(String keiyakugaku_hudousantanpo) {
		this.keiyakugaku_hudousantanpo = getRemoveKanmaVal(keiyakugaku_hudousantanpo);
	}
	public String getKeiyakugaku_sonota() {
		return keiyakugaku_sonota;
	}
	public void setKeiyakugaku_sonota(String keiyakugaku_sonota) {
		this.keiyakugaku_sonota = getRemoveKanmaVal(keiyakugaku_sonota);
	}
	public String getKibikiatekin() {
		return kibikiatekin;
	}
	public void setKibikiatekin(String kibikiatekin) {
		this.kibikiatekin = kibikiatekin;
	}
	public String getKingaku1() {
		return kingaku1;
	}
	public void setKingaku1(String kingaku1) {
		this.kingaku1 = getRemoveKanmaVal(kingaku1);
	}
	public String getKingaku2() {
		return kingaku2;
	}
	public void setKingaku2(String kingaku2) {
		this.kingaku2 = getRemoveKanmaVal(kingaku2);
	}
	public String getKingaku3() {
		return kingaku3;
	}
	public void setKingaku3(String kingaku3) {
		this.kingaku3 = getRemoveKanmaVal(kingaku3);
	}
	public String getKongonokaishumitoshi() {
		return kongonokaishumitoshi;
	}
	public void setKongonokaishumitoshi(String kongonokaishumitoshi) {
		this.kongonokaishumitoshi = getVal(kongonokaishumitoshi);
	}
	public String getMishunyukin() {
		return mishunyukin;
	}
	public void setMishunyukin(String mishunyukin) {
		this.mishunyukin = mishunyukin;
	}
	public String getMishushueki() {
		return mishushueki;
	}
	public void setMishushueki(String mishushueki) {
		this.mishushueki = mishushueki;
	}
	public String getOthriskflg() {
		return othriskflg;
	}
	public void setOthriskflg(String othriskflg) {
		this.othriskflg = othriskflg;
	}
	public String getOthryuhosaimu() {
		return othryuhosaimu;
	}
	public void setOthryuhosaimu(String othryuhosaimu) {
		this.othryuhosaimu = getRemoveKanmaVal(othryuhosaimu);
	}
	public String getOthryuhosaimunaiyakuStyle() {
		return othryuhosaimunaiyakuStyle;
	}
	public void setOthryuhosaimunaiyakuStyle(String othryuhosaimunaiyakuStyle) {
		this.othryuhosaimunaiyakuStyle = othryuhosaimunaiyakuStyle;
	}
	public String getOthRyuhosaimuStyle() {
		return othRyuhosaimuStyle;
	}
	public void setOthRyuhosaimuStyle(String othRyuhosaimuStyle) {
		this.othRyuhosaimuStyle = othRyuhosaimuStyle;
	}
	public String getRikoseikyukenen() {
		return rikoseikyukenen;
	}
	public void setRikoseikyukenen(String rikoseikyukenen) {
		this.rikoseikyukenen = getRemoveKanmaVal(rikoseikyukenen);
	}
	public String getRikoseikyukenennonaiyosetumei() {
		return rikoseikyukenennonaiyosetumei;
	}
	public void setRikoseikyukenennonaiyosetumei(
			String rikoseikyukenennonaiyosetumei) {
		this.rikoseikyukenennonaiyosetumei = getVal(rikoseikyukenennonaiyosetumei);
	}
	public String getRikoseikyukenennonaiyosetumeiStyle() {
		return rikoseikyukenennonaiyosetumeiStyle;
	}
	public void setRikoseikyukenennonaiyosetumeiStyle(
			String rikoseikyukenennonaiyosetumeiStyle) {
		this.rikoseikyukenennonaiyosetumeiStyle = rikoseikyukenennonaiyosetumeiStyle;
	}
	public String getRikoseikyukenenStyle() {
		return rikoseikyukenenStyle;
	}
	public void setRikoseikyukenenStyle(String rikoseikyukenenStyle) {
		this.rikoseikyukenenStyle = rikoseikyukenenStyle;
	}
	public String getRyuhosaimu() {
		return ryuhosaimu;
	}
	public void setRyuhosaimu(String ryuhosaimu) {
		this.ryuhosaimu = getRemoveKanmaVal(ryuhosaimu);
	}
	public String getRyuhosaimukei() {
		return ryuhosaimukei;
	}
	public void setRyuhosaimukei(String ryuhosaimukei) {
		this.ryuhosaimukei = getRemoveKanmaVal(ryuhosaimukei);
	}
	public String getRyuhosaimuStyle() {
		return ryuhosaimuStyle;
	}
	public void setRyuhosaimuStyle(String ryuhosaimuStyle) {
		this.ryuhosaimuStyle = ryuhosaimuStyle;
	}
	public String getSaikenzandakagokei() {
		return saikenzandakagokei;
	}
	public void setSaikenzandakagokei(String saikenzandakagokei) {
		this.saikenzandakagokei = saikenzandakagokei;
	}
	public String getSashiirehosyokin() {
		return sashiirehosyokin;
	}
	public void setSashiirehosyokin(String sashiirehosyokin) {
		this.sashiirehosyokin = sashiirehosyokin;
	}
	public String getShihankichushutsuflg() {
		return shihankichushutsuflg;
	}
	public void setShihankichushutsuflg(String shihankichushutsuflg) {
		this.shihankichushutsuflg = shihankichushutsuflg;
	}
	public String getShihankichushutsukomokuFlg() {
		return shihankichushutsukomokuFlg;
	}
	public void setShihankichushutsukomokuFlg(String shihankichushutsukomokuFlg) {
		this.shihankichushutsukomokuFlg = shihankichushutsukomokuFlg;
	}
	public LinkedHashMap getShoninList() {
		return shoninList;
	}
	public void setShoninList(LinkedHashMap shoninList) {
		this.shoninList = shoninList;
	}
	public String getShoninsha() {
		return shoninsha;
	}
	public void setShoninsha(String shoninsha) {
		this.shoninsha = shoninsha;
	}
	public String getShoninshaListFlg() {
		return shoninshaListFlg;
	}
	public void setShoninshaListFlg(String shoninshaListFlg) {
		this.shoninshaListFlg = shoninshaListFlg;
	}
	public String getSonotakaishu() {
		return sonotakaishu;
	}
	public void setSonotakaishu(String sonotakaishu) {
		this.sonotakaishu = getRemoveKanmaVal(sonotakaishu);
	}
	public String getSonotakaishunonaiyo() {
		return sonotakaishunonaiyo;
	}
	public void setSonotakaishunonaiyo(String sonotakaishunonaiyo) {
		this.sonotakaishunonaiyo = getVal(sonotakaishunonaiyo);
	}
	public String getSonotakaishuStyle() {
		return sonotakaishuStyle;
	}
	public void setSonotakaishuStyle(String sonotakaishuStyle) {
		this.sonotakaishuStyle = sonotakaishuStyle;
	}
	public String getSonotanonaiyo() {
		return sonotanonaiyo;
	}
	public void setSonotanonaiyo(String sonotanonaiyo) {
		this.sonotanonaiyo = getVal(sonotanonaiyo);
	}
	public String getSonotatousi() {
		return sonotatousi;
	}
	public void setSonotatousi(String sonotatousi) {
		this.sonotatousi = sonotatousi;
	}
	public String getTani() {
		return tani;
	}
	public void setTani(String tani) {
		this.tani = tani;
	}
	public String getTankikashitsukekin() {
		return tankikashitsukekin;
	}
	public void setTankikashitsukekin(String tankikashitsukekin) {
		this.tankikashitsukekin = tankikashitsukekin;
	}
	public String getTatekaekin() {
		return tatekaekin;
	}
	public void setTatekaekin(String tatekaekin) {
		this.tatekaekin = tatekaekin;
	}
	public String getTori_nm1() {
		return tori_nm1;
	}
	public void setTori_nm1(String tori_nm1) {
		this.tori_nm1 = getVal(tori_nm1);
	}
	public String getTori_nm2() {
		return tori_nm2;
	}
	public void setTori_nm2(String tori_nm2) {
		this.tori_nm2 = getVal(tori_nm2);
	}
	public String getTori_nm3() {
		return tori_nm3;
	}
	public void setTori_nm3(String tori_nm3) {
		this.tori_nm3 = getVal(tori_nm3);
	}
	public String getTorihikimaewatashikin() {
		return torihikimaewatashikin;
	}
	public void setTorihikimaewatashikin(String torihikimaewatashikin) {
		this.torihikimaewatashikin = torihikimaewatashikin;
	}
	public String getTuikahikiatekingaku() {
		return tuikahikiatekingaku;
	}
	public void setTuikahikiatekingaku(String tuikahikiatekingaku) {
		this.tuikahikiatekingaku = getRemoveKanmaVal(tuikahikiatekingaku);
	}
	public String getTuikahikiatekingaku_after() {
		return tuikahikiatekingaku_after;
	}
	public void setTuikahikiatekingaku_after(String tuikahikiatekingaku_after) {
		this.tuikahikiatekingaku_after = getRemoveKanmaVal(tuikahikiatekingaku_after);
	}
	public String getTuikahikiatekingakuStyle() {
		return tuikahikiatekingakuStyle;
	}
	public void setTuikahikiatekingakuStyle(String tuikahikiatekingakuStyle) {
		this.tuikahikiatekingakuStyle = tuikahikiatekingakuStyle;
	}
	public String getTukachosei() {
		return tukachosei;
	}
	public void setTukachosei(String tukachosei) {
		this.tukachosei = getRemoveKanmaVal(tukachosei);
	}
	public String getTukachoseiStyle() {
		return tukachoseiStyle;
	}
	public void setTukachoseiStyle(String tukachoseiStyle) {
		this.tukachoseiStyle = tukachoseiStyle;
	}
	public String getUketoritegata() {
		return uketoritegata;
	}
	public void setUketoritegata(String uketoritegata) {
		this.uketoritegata = uketoritegata;
	}
	public String getUrikakekin() {
		return urikakekin;
	}
	public void setUrikakekin(String urikakekin) {
		this.urikakekin = urikakekin;
	}
	public String getYushutsuuketoritegata() {
		return yushutsuuketoritegata;
	}
	public void setYushutsuuketoritegata(String yushutsuuketoritegata) {
		this.yushutsuuketoritegata = yushutsuuketoritegata;
	}
	public String getCommentRuiTab() {
		return commentRuiTab;
	}
	public void setCommentRuiTab(String commentRuiTab) {
		this.commentRuiTab = commentRuiTab;
	}
	public String getFlgkbnTab() {
		return flgkbnTab;
	}
	public void setFlgkbnTab(String flgkbnTab) {
		this.flgkbnTab = flgkbnTab;
	}
	public String getHanyo1StyleTD() {
		return hanyo1StyleTD;
	}
	public void setHanyo1StyleTD(String hanyo1StyleTD) {
		this.hanyo1StyleTD = hanyo1StyleTD;
	}
	public String getHanyo1Tab() {
		return hanyo1Tab;
	}
	public void setHanyo1Tab(String hanyo1Tab) {
		this.hanyo1Tab = hanyo1Tab;
	}
	public String getHozenHyoStyleTD() {
		return hozenHyoStyleTD;
	}
	public void setHozenHyoStyleTD(String hozenHyoStyleTD) {
		this.hozenHyoStyleTD = hozenHyoStyleTD;
	}
	public String getHozenHyoTab() {
		return hozenHyoTab;
	}
	public void setHozenHyoTab(String hozenHyoTab) {
		this.hozenHyoTab = hozenHyoTab;
	}
	public String getHozenStyleTD() {
		return hozenStyleTD;
	}
	public void setHozenStyleTD(String hozenStyleTD) {
		this.hozenStyleTD = hozenStyleTD;
	}
	public String getHozenTab() {
		return hozenTab;
	}
	public void setHozenTab(String hozenTab) {
		this.hozenTab = hozenTab;
	}
	public String getOthriskflgTab() {
		return othriskflgTab;
	}
	public void setOthriskflgTab(String othriskflgTab) {
		this.othriskflgTab = othriskflgTab;
	}
	public String getOthryuhosaimunaiyakuStyleTD() {
		return othryuhosaimunaiyakuStyleTD;
	}
	public void setOthryuhosaimunaiyakuStyleTD(String othryuhosaimunaiyakuStyleTD) {
		this.othryuhosaimunaiyakuStyleTD = othryuhosaimunaiyakuStyleTD;
	}
	public String getOthryuhosaimunaiyakuTab() {
		return othryuhosaimunaiyakuTab;
	}
	public void setOthryuhosaimunaiyakuTab(String othryuhosaimunaiyakuTab) {
		this.othryuhosaimunaiyakuTab = othryuhosaimunaiyakuTab;
	}
	public String getOthRyuhosaimuStyleTD() {
		return othRyuhosaimuStyleTD;
	}
	public void setOthRyuhosaimuStyleTD(String othRyuhosaimuStyleTD) {
		this.othRyuhosaimuStyleTD = othRyuhosaimuStyleTD;
	}
	public String getOthRyuhosaimuTab() {
		return othRyuhosaimuTab;
	}
	public void setOthRyuhosaimuTab(String othRyuhosaimuTab) {
		this.othRyuhosaimuTab = othRyuhosaimuTab;
	}
	public String getRikoseikyukenennonaiyosetumeiTab() {
		return rikoseikyukenennonaiyosetumeiTab;
	}
	public void setRikoseikyukenennonaiyosetumeiTab(
			String rikoseikyukenennonaiyosetumeiTab) {
		this.rikoseikyukenennonaiyosetumeiTab = rikoseikyukenennonaiyosetumeiTab;
	}
	public String getRikoseikyukenenStyleTD() {
		return rikoseikyukenenStyleTD;
	}
	public void setRikoseikyukenenStyleTD(String rikoseikyukenenStyleTD) {
		this.rikoseikyukenenStyleTD = rikoseikyukenenStyleTD;
	}
	public String getRikoseikyukenenTab() {
		return rikoseikyukenenTab;
	}
	public void setRikoseikyukenenTab(String rikoseikyukenenTab) {
		this.rikoseikyukenenTab = rikoseikyukenenTab;
	}
	public String getRyuhosaimuStyleTD() {
		return ryuhosaimuStyleTD;
	}
	public void setRyuhosaimuStyleTD(String ryuhosaimuStyleTD) {
		this.ryuhosaimuStyleTD = ryuhosaimuStyleTD;
	}
	public String getRyuhosaimuTab() {
		return ryuhosaimuTab;
	}
	public void setRyuhosaimuTab(String ryuhosaimuTab) {
		this.ryuhosaimuTab = ryuhosaimuTab;
	}
	public String getShihankichushutsuflgTab() {
		return shihankichushutsuflgTab;
	}
	public void setShihankichushutsuflgTab(String shihankichushutsuflgTab) {
		this.shihankichushutsuflgTab = shihankichushutsuflgTab;
	}
	public String getSonotakaishuStyleTD() {
		return sonotakaishuStyleTD;
	}
	public void setSonotakaishuStyleTD(String sonotakaishuStyleTD) {
		this.sonotakaishuStyleTD = sonotakaishuStyleTD;
	}
	public String getSonotakaishuTab() {
		return sonotakaishuTab;
	}
	public void setSonotakaishuTab(String sonotakaishuTab) {
		this.sonotakaishuTab = sonotakaishuTab;
	}
	public String getTuikahikiatekingakuStyleTD() {
		return tuikahikiatekingakuStyleTD;
	}
	public void setTuikahikiatekingakuStyleTD(String tuikahikiatekingakuStyleTD) {
		this.tuikahikiatekingakuStyleTD = tuikahikiatekingakuStyleTD;
	}
	public String getTuikahikiatekingakuTab() {
		return tuikahikiatekingakuTab;
	}
	public void setTuikahikiatekingakuTab(String tuikahikiatekingakuTab) {
		this.tuikahikiatekingakuTab = tuikahikiatekingakuTab;
	}
	public String getTukachoseiStyleTD() {
		return tukachoseiStyleTD;
	}
	public void setTukachoseiStyleTD(String tukachoseiStyleTD) {
		this.tukachoseiStyleTD = tukachoseiStyleTD;
	}
	public String getTukachoseiTab() {
		return tukachoseiTab;
	}
	public void setTukachoseiTab(String tukachoseiTab) {
		this.tukachoseiTab = tukachoseiTab;
	}
	public String getFlgCommentStyle() {
		return flgCommentStyle;
	}
	public void setFlgCommentStyle(String flgCommentStyle) {
		this.flgCommentStyle = flgCommentStyle;
	}
	public String getFlgCommentTab() {
		return flgCommentTab;
	}
	public void setFlgCommentTab(String flgCommentTab) {
		this.flgCommentTab = flgCommentTab;
	}
	public boolean isCommentRuiRead() {
		return commentRuiRead;
	}
	public void setCommentRuiRead(boolean commentRuiRead) {
		this.commentRuiRead = commentRuiRead;
	}
	public boolean isFlgCommentRead() {
		return flgCommentRead;
	}
	public void setFlgCommentRead(boolean flgCommentRead) {
		this.flgCommentRead = flgCommentRead;
	}
	public boolean isFlgkbnRead() {
		return flgkbnRead;
	}
	public void setFlgkbnRead(boolean flgkbnRead) {
		this.flgkbnRead = flgkbnRead;
	}
	public boolean isHanyo1Read() {
		return hanyo1Read;
	}
	public void setHanyo1Read(boolean hanyo1Read) {
		this.hanyo1Read = hanyo1Read;
	}
	public boolean isHozenHyoRead() {
		return hozenHyoRead;
	}
	public void setHozenHyoRead(boolean hozenHyoRead) {
		this.hozenHyoRead = hozenHyoRead;
	}
	public boolean isHozenRead() {
		return hozenRead;
	}
	public void setHozenRead(boolean hozenRead) {
		this.hozenRead = hozenRead;
	}
	public boolean isOthriskflgRead() {
		return othriskflgRead;
	}
	public void setOthriskflgRead(boolean othriskflgRead) {
		this.othriskflgRead = othriskflgRead;
	}
	public boolean isOthryuhosaimunaiyakuRead() {
		return othryuhosaimunaiyakuRead;
	}
	public void setOthryuhosaimunaiyakuRead(boolean othryuhosaimunaiyakuRead) {
		this.othryuhosaimunaiyakuRead = othryuhosaimunaiyakuRead;
	}
	public boolean isOthRyuhosaimuRead() {
		return othRyuhosaimuRead;
	}
	public void setOthRyuhosaimuRead(boolean othRyuhosaimuRead) {
		this.othRyuhosaimuRead = othRyuhosaimuRead;
	}
	public boolean isRikoseikyukenennonaiyosetumeiRead() {
		return rikoseikyukenennonaiyosetumeiRead;
	}
	public void setRikoseikyukenennonaiyosetumeiRead(
			boolean rikoseikyukenennonaiyosetumeiRead) {
		this.rikoseikyukenennonaiyosetumeiRead = rikoseikyukenennonaiyosetumeiRead;
	}
	public boolean isRikoseikyukenenRead() {
		return rikoseikyukenenRead;
	}
	public void setRikoseikyukenenRead(boolean rikoseikyukenenRead) {
		this.rikoseikyukenenRead = rikoseikyukenenRead;
	}
	public boolean isRyuhosaimuRead() {
		return ryuhosaimuRead;
	}
	public void setRyuhosaimuRead(boolean ryuhosaimuRead) {
		this.ryuhosaimuRead = ryuhosaimuRead;
	}
	public boolean isShihankichushutsuflgRead() {
		return shihankichushutsuflgRead;
	}
	public void setShihankichushutsuflgRead(boolean shihankichushutsuflgRead) {
		this.shihankichushutsuflgRead = shihankichushutsuflgRead;
	}
	public boolean isSonotakaishuRead() {
		return sonotakaishuRead;
	}
	public void setSonotakaishuRead(boolean sonotakaishuRead) {
		this.sonotakaishuRead = sonotakaishuRead;
	}
	public boolean isTuikahikiatekingakuRead() {
		return tuikahikiatekingakuRead;
	}
	public void setTuikahikiatekingakuRead(boolean tuikahikiatekingakuRead) {
		this.tuikahikiatekingakuRead = tuikahikiatekingakuRead;
	}
	public boolean isTukachoseiRead() {
		return tukachoseiRead;
	}
	public void setTukachoseiRead(boolean tukachoseiRead) {
		this.tukachoseiRead = tukachoseiRead;
	}
	public boolean isIppanFlg() {
		return ippanFlg;
	}
	public void setIppanFlg(boolean ippanFlg) {
		this.ippanFlg = ippanFlg;
	}
}