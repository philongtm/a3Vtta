/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/20		SSC				課題No.33 保有株数カンマ編集処理
******************************************************************************/
package app.satei.form;

import app.ZaimuBean;
import common.global.GS;
import common.struts.AppPagerActionForm;
import common.util.Function;

import java.util.List;

/**
 * OC1102_査定_取引先概要 アクションフォームクラス
 * 
 */
public class TorokuForm extends AppPagerActionForm {
	
    private String kabunusiNm1;	//株主名称1
    private String kabunusiNm2;	//株主名称2
    private String kabunusiNm3;	//株主名称3
    private String kabunusiNm4;	//株主名称4
    private String kabunusiNm5;	//株主名称5
    private String hiritu1;		//保有比率1
    private String hiritu2;		//保有比率2
    private String hiritu3;		//保有比率3
    private String hiritu4;		//保有比率4
    private String hiritu5;		//保有比率5
    private String kabusu1;		//保有株数1
    private String kabusu2;		//保有株数2
    private String kabusu3;		//保有株数3
    private String kabusu4;		//保有株数4
    private String kabusu5;   		//保有株数5
    private String jigyoNaiyo;		//事業内容

    private String kesanGaikyo;	//決算概況(登録ポイント10)
    private String tukaCd;			//通貨コード
    private String sicSm;			//業種小分類
    private String hyoujiTani;		//表示単位
    private List<ZaimuBean> zaimu;	//財務情報
    private String hanyoTitle1;	//国内：経常利益、海外：営業外収益
    private String hanyoTitle2;	//国内：特別利益、海外：営業外費用
    private String hanyoTitle3;	//国内：特別損失、海外：法人税

    public TorokuForm() {
    	super.gamenId			= GS.OC1102;
    	this.kabunusiNm1		= GS.EMPTY_CHARCTER;
    	this.kabunusiNm2		= GS.EMPTY_CHARCTER;
    	this.kabunusiNm3		= GS.EMPTY_CHARCTER;
    	this.kabunusiNm4		= GS.EMPTY_CHARCTER;
    	this.kabunusiNm5		= GS.EMPTY_CHARCTER;
    	this.hiritu1			= GS.EMPTY_CHARCTER;
    	this.hiritu2			= GS.EMPTY_CHARCTER;
    	this.hiritu3			= GS.EMPTY_CHARCTER;
    	this.hiritu4			= GS.EMPTY_CHARCTER;
    	this.hiritu5			= GS.EMPTY_CHARCTER;
    	this.kabusu1			= GS.EMPTY_CHARCTER;
    	this.kabusu2			= GS.EMPTY_CHARCTER;
    	this.kabusu3			= GS.EMPTY_CHARCTER;
    	this.kabusu4			= GS.EMPTY_CHARCTER;
    	this.kabusu5			= GS.EMPTY_CHARCTER;
    	this.jigyoNaiyo			= GS.EMPTY_CHARCTER;
    	this.kesanGaikyo		= GS.EMPTY_CHARCTER;
    	this.tukaCd				= GS.EMPTY_CHARCTER;
    	this.sicSm				= GS.EMPTY_CHARCTER;
    	this.hyoujiTani			= GS.EMPTY_CHARCTER;
    	this.zaimu				= null;
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
	
	//課題No.33
	//追加開始
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
	//追加完了
	
	//保有比率1
	public String getHiritu1() {
		return hiritu1;
	}
	public void setHiritu1(String hiritu1) {
		this.hiritu1 = getVal(hiritu1);
	}
	//保有比率2
	public String getHiritu2() {
		return hiritu2;
	}
	public void setHiritu2(String hiritu2) {
		this.hiritu2 = getVal(hiritu2);
	}
	//保有比率3
	public String getHiritu3() {
		return hiritu3;
	}
	public void setHiritu3(String hiritu3) {
		this.hiritu3 = getVal(hiritu3);
	}
	//保有比率4
	public String getHiritu4() {
		return hiritu4;
	}
	public void setHiritu4(String hiritu4) {
		this.hiritu4 = getVal(hiritu4);
	}
	//保有比率5
	public String getHiritu5() {
		return hiritu5;
	}
	public void setHiritu5(String hiritu5) {
		this.hiritu5 = getVal(hiritu5);
	}
	//事業内容
	public String getJigyoNaiyo() {
		return jigyoNaiyo;
	}
	public void setJigyoNaiyo(String jigyoNaiyo) {
		this.jigyoNaiyo = getVal(jigyoNaiyo);
	}
	//株主名称1
	public String getKabunusiNm1() {
		return kabunusiNm1;
	}
	public void setKabunusiNm1(String kabunusiNm1) {
		this.kabunusiNm1 = getVal(kabunusiNm1);
	}
	//株主名称2
	public String getKabunusiNm2() {
		return kabunusiNm2;
	}
	public void setKabunusiNm2(String kabunusiNm2) {
		this.kabunusiNm2 = getVal(kabunusiNm2);
	}
	//株主名称3
	public String getKabunusiNm3() {
		return kabunusiNm3;
	}
	public void setKabunusiNm3(String kabunusiNm3) {
		this.kabunusiNm3 = getVal(kabunusiNm3);
	}
	//株主名称4
	public String getKabunusiNm4() {
		return kabunusiNm4;
	}
	public void setKabunusiNm4(String kabunusiNm4) {
		this.kabunusiNm4 = getVal(kabunusiNm4);
	}
	//株主名称5
	public String getKabunusiNm5() {
		return kabunusiNm5;
	}
	public void setKabunusiNm5(String kabunusiNm5) {
		this.kabunusiNm5 = getVal(kabunusiNm5);
	}
	//保有株数1
	public String getKabusu1() {
		return kabusu1;
	}
	public void setKabusu1(String kabusu1) {
		//課題No.33
		//修正開始
		this.kabusu1 = getRemoveKanmaVal(kabusu1);
		//修正完了
	}
	//保有株数2
	public String getKabusu2() {
		return kabusu2;
	}
	public void setKabusu2(String kabusu2) {
		//課題No.33
		//修正開始
		this.kabusu2 = getRemoveKanmaVal(kabusu2);
		//修正完了
	}
	//保有株数3
	public String getKabusu3() {
		return kabusu3;
	}
	public void setKabusu3(String kabusu3) {
		//課題No.33
		//修正開始
		this.kabusu3 = getRemoveKanmaVal(kabusu3);
		//修正完了
	}
	//保有株数4
	public String getKabusu4() {
		return kabusu4;
	}
	public void setKabusu4(String kabusu4) {
		//課題No.33
		//修正開始
		this.kabusu4 = getRemoveKanmaVal(kabusu4);
		//修正完了
	}
	//保有株数5
	public String getKabusu5() {
		return kabusu5;
	}
	public void setKabusu5(String kabusu5) {
		//課題No.33
		//修正開始
		this.kabusu5 = getRemoveKanmaVal(kabusu5);
		//修正完了
	}
	//決算概況
	public String getKesanGaikyo() {
		return kesanGaikyo;
	}
	public void setKesanGaikyo(String kesanGaikyo) {
		this.kesanGaikyo = getVal(kesanGaikyo);
	}
	//業種小分類
	public String getSicSm() {
		return sicSm;
	}
	public void setSicSm(String sicSm) {
		this.sicSm = sicSm;
	}
	//通貨コード
	public String getTukaCd() {
		return tukaCd;
	}
	public void setTukaCd(String tukaCd) {
		this.tukaCd = tukaCd;
	}
	//財務情報
	public List<ZaimuBean> getZaimu() {
		return zaimu;
	}
	public void setZaimu(List<ZaimuBean> zaimu) {
		this.zaimu = zaimu;
	}
	//汎用1タイトル
	public String getHanyoTitle1() {
		return hanyoTitle1;
	}
	public void setHanyoTitle1(String hanyoTitle1) {
		this.hanyoTitle1 = hanyoTitle1;
	}
	//汎用2タイトル
	public String getHanyoTitle2() {
		return hanyoTitle2;
	}
	public void setHanyoTitle2(String hanyoTitle2) {
		this.hanyoTitle2 = hanyoTitle2;
	}
	//汎用3タイトル
	public String getHanyoTitle3() {
		return hanyoTitle3;
	}
	public void setHanyoTitle3(String hanyoTitle3) {
		this.hanyoTitle3 = hanyoTitle3;
	}
	//表示単位
	public String getHyoujiTani() {
		return hyoujiTani;
	}
	public void setHyoujiTani(String hyoujiTani) {
		this.hyoujiTani = hyoujiTani;
	}
}