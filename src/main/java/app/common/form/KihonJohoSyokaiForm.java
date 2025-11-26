/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

/**
 * OZ6108_基本情報照会タブ アクションフォームクラス
 * 
 */
public class KihonJohoSyokaiForm extends AppPagerActionForm {

    private String tuuka_cd;			// 通貨コード
    private String kbn01;				// 受取手形
    private String kbn02;				// 輸出受取手形
    private String kbn03;				// 売掛金
    private String kbn04;				// 取引前渡金
    private String kbn05;				// 立替金
    private String kbn06;				// 未収入金
    private String kbn07;				// 未収収益
    private String kbn08;				// 短期貸付金
    private String kbn09;				// 差入保証金
    private String kbn10;				// 仮払金
    private String kbn11;				// 長期貸付金
    private String kbn12;				// その他投資
    private String kbn14;				// 保証債務合計
    private String kbn15;				// 既引当金
    private String ippan_saiken_kei;	// 一般債権計
    private String saiken_kei;			// 債権残高合計
    private String hanyou1_lbl;		// 汎用１タイトル
    private String hanyou1;			// 汎用１
    private String oya_kaisya_nm;		// 親会社名称
	
    // 変数初期化
    public KihonJohoSyokaiForm() {
    	super.gamenId          = GS.OZ6108;
    	this.tuuka_cd          = GS.EMPTY_CHARCTER;
        this.kbn01             = GS.EMPTY_CHARCTER;
        this.kbn02             = GS.EMPTY_CHARCTER;
        this.kbn03             = GS.EMPTY_CHARCTER;
        this.kbn04             = GS.EMPTY_CHARCTER;
        this.kbn05             = GS.EMPTY_CHARCTER;
        this.kbn06             = GS.EMPTY_CHARCTER;
        this.kbn07             = GS.EMPTY_CHARCTER;
        this.kbn08             = GS.EMPTY_CHARCTER;
        this.kbn09             = GS.EMPTY_CHARCTER;
        this.kbn10             = GS.EMPTY_CHARCTER;
        this.kbn11             = GS.EMPTY_CHARCTER;
        this.kbn12             = GS.EMPTY_CHARCTER;
        this.kbn14             = GS.EMPTY_CHARCTER;
        this.kbn15             = GS.EMPTY_CHARCTER;
        this.ippan_saiken_kei  = GS.EMPTY_CHARCTER;
        this.saiken_kei        = GS.EMPTY_CHARCTER;
    	this.hanyou1_lbl       = GS.EMPTY_CHARCTER;
    	this.hanyou1           = GS.EMPTY_CHARCTER;
    	this.oya_kaisya_nm     = GS.EMPTY_CHARCTER;
    }

	/**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}

    // アクセスメソッド

	//通貨コード    
    public String getTuuka_cd() {
        return this.tuuka_cd;
    }
    public void setTuuka_cd(String tuuka_cd) {
        this.tuuka_cd = tuuka_cd;
    }

	//受取手形 
    public String getKbn01() {
        return this.kbn01;
    }
    public void setKbn01(String kbn01) {
        this.kbn01 = kbn01;
    }
	//輸出受取手形
    public String getKbn02() {
        return this.kbn02;
    }
    public void setKbn02(String kbn02) {
        this.kbn02 = kbn02;
    }
	//売掛金
    public String getKbn03() {
        return this.kbn03;
    }
    public void setKbn03(String kbn03) {
        this.kbn03 = kbn03;
    }
	//取引前渡金
    public String getKbn04() {
        return this.kbn04;
    }
    public void setKbn04(String kbn04) {
        this.kbn04 = kbn04;
    }
	//立替金
    public String getKbn05() {
        return this.kbn05;
    }
    public void setKbn05(String kbn05) {
        this.kbn05 = kbn05;
    }
	//未収入金
    public String getKbn06() {
        return this.kbn06;
    }
    public void setKbn06(String kbn06) {
        this.kbn06 = kbn06;
    }
	//未収収益
    public String getKbn07() {
        return this.kbn07;
    }
    public void setKbn07(String kbn07) {
        this.kbn07 = kbn07;
    }
	//短期貸付金
    public String getKbn08() {
        return this.kbn08;
    }
    public void setKbn08(String kbn08) {
        this.kbn08 = kbn08;
    }
	//差入保証金
    public String getKbn09() {
        return this.kbn09;
    }
    public void setKbn09(String kbn09) {
        this.kbn09 = kbn09;
    }
	//仮払金
    public String getKbn10() {
        return this.kbn10;
    }
    public void setKbn10(String kbn10) {
        this.kbn10 = kbn10;
    }
	//長期貸付金
    public String getKbn11() {
        return this.kbn11;
    }
    public void setKbn11(String kbn11) {
        this.kbn11 = kbn11;
    }
	//その他投資
    public String getKbn12() {
        return this.kbn12;
    }
    public void setKbn12(String kbn12) {
        this.kbn12 = kbn12;
    }
	//保証債務合計
    public String getKbn14() {
        return this.kbn14;
    }
    public void setKbn14(String kbn14) {
        this.kbn14 = kbn14;
    }
	//既引当金
    public String getKbn15() {
        return this.kbn15;
    }
    public void setKbn15(String kbn15) {
        this.kbn15 = kbn15;
    }
	//一般債権計
    public String getIppan_saiken_kei() {
        return this.ippan_saiken_kei;
    }
    public void setIppan_saiken_kei(String ippan_saiken_kei) {
    	this.ippan_saiken_kei = ippan_saiken_kei;
    }
	//債権残高合計
    public String getSaiken_kei() {
        return this.saiken_kei;
    }
    public void setSaiken_kei(String saiken_kei) {
    	this.saiken_kei = saiken_kei;
    }
	//汎用１タイトル
    public String getHanyou1_lbl() {
        return this.hanyou1_lbl;
    }
    public void setHanyou1_lbl(String hanyou1_lbl) {
        this.hanyou1_lbl = hanyou1_lbl;
    }
	//汎用１
    public String getHanyou1() {
        return this.hanyou1;
    }
    public void setHanyou1(String hanyou1) {
        this.hanyou1 = hanyou1;
    }
	//親会社名称
    public String getOya_kaisya_nm() {
        return this.oya_kaisya_nm;
    }
    public void setOya_kaisya_nm(String oya_kaisya_nm) {
        this.oya_kaisya_nm = oya_kaisya_nm;
    }
}