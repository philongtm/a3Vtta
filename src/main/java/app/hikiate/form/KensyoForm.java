/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/05/18		SSC				1.5次版機能組込 
******************************************************************************/
package app.hikiate.form;

import common.struts.AppPagerActionForm;

import java.util.LinkedHashMap;
import java.util.List;

public class KensyoForm extends AppPagerActionForm {

	/** 案件No. */
	private String anken_no;
	/** 取引先コード */
	private String txtCdTorihiki;
	/** 取引先名 */
	private String txtNameTorihiki;
	/** 現フェーズ */
	private String phase;
	/** 年月 */
	private String ym;
	/** DUNS NO */
	private String txtNoDuns;
	/** 承認担当者リスト */
	private List recoName;
	/** 承認担当者 DUNS No */
	private String tantoSyonin;
	/** 承認担当者 部門 */
	private String bumonSyonin;
	/** 承認担当者 index */
	private String indexSyonin;
	/** 滞留区分 */
	private String showKbnTairyu;
	/** 信用格付 */
	private String txtKtk;
	/** 親会社信用格付 */
	private String txtOyaKtk;
	/** 親会社名 */
	private String txtNameOya;
	/** 親DUNS NO */
	private String txtNoOyaDuns;
	/** 親会社一体判断 */
	private String txtFlgIttai;
	/** 親会社独立判断 */
	private String txtFlgDokuritu;
	/** 当期決算日 */
	private String LtFiscClosYmd;
	/** 通貨名称 */
	private String txtNameTuuka;
	/** 通貨名称(最終月) */
	private String txtNameFinalTuuka;
	/**	店コード */
	private String mise_cd;
	/** システム区分 */
	private String system_kbn;
	/**  */
	private boolean cmt00;
	/** 差戻し、転送フラグ*/
    private String hidFlgSasiTen;
    //結合テスト障害No038対応
    //追加開始
    /** 半期四半期区分*/
    private String hanki_sihanki_kbn;	
    //追加完了
	
	// 初回月、中間月
	/** 査定案件No */
	private String sateiAnkenNo;
	/** 受取手形 */
	private String kingaku01;
	/** 輸出受取手形 */
	private String kingaku02;
	/** 売掛金 */
	private String kingaku03;
	/** 取引前渡金 */
	private String kingaku04;
	/** 立替金 */
	private String kingaku05;
	/** 未収入金 */
	private String kingaku06;
	/** 未収収益 */
	private String kingaku07;
	/** 短期貸付金 */
	private String kingaku08;
	/** 差入保証金 */
	private String kingaku09;
	/** 仮払金 */
	private String kingaku10;
	/** 長期貸付金 */
	private String kingaku11;
	/** その他投資 */
	private String kingaku12;
	/** 保証債務 */
	private String kingaku14;
	/** 既引当金 */
	private String kingaku15;
	/** 固定化営業債権 */
	private String kingaku16;
	/** 一般債権計 */
	private String totalSaikenIppan;
	/** 債権残高合計 */
	private String totalSaikenZandaka;
	/** 留保債務計 */
	private String totalRyuhosaimu;
	/** 引当対象金額 */
	private String kingakuHikiateTaisyo;
	/** 留保債務 */
	private String ryuhosaimu;
	/** 第三者留保債務 */
	private String ryuhosaimu3;
	/** 保全 */
	private String hozen;
	/** その他回収 */
	private String kingakuSonota;
	/** 履行請求懸念 */
	private String kingakuKenen;
	/** 追加引当金額 */
	private String kingakuTuika;
	/** 引当金算定根拠 */
	private String cmtValKonkyo;
	/** 査定年月 */
	private String txtYmSatei;
	/** 取引先区分 */
	private String txtKbnTorihiki;
	/** 債権区分 */
	private String txtKbnSaiken;

	// 最終月
	/** 最終月 受取手形 */
	private String finalKingaku01;
	/** 最終月 輸出受取手形 */
	private String finalKingaku02;
	/** 最終月 売掛金 */
	private String finalKingaku03;
	/** 最終月 取引前渡金 */
	private String finalKingaku04;
	/** 最終月 立替金 */
	private String finalKingaku05;
	/** 最終月 未収入金 */
	private String finalKingaku06;
	/** 最終月 未収収益 */
	private String finalKingaku07;
	/** 最終月 短期貸付金 */
	private String finalKingaku08;
	/** 最終月 差入保証金 */
	private String finalKingaku09;
	/** 最終月 仮払金 */
	private String finalKingaku10;
	/** 最終月 長期貸付金 */
	private String finalKingaku11;
	/** 最終月 その他投資 */
	private String finalKingaku12;
	/** 最終月 保証債務 */
	private String finalKingaku14;
	/** 最終月 既引当金 */
	private String finalKingaku15;
	/** 最終月 固定化営業債権 */
	private String finalKingaku16;
	/** 最終月 一般債権計 */
	private String totalFinalSaikenIppan;
	/** 最終月 債権残高合計 */
	private String totalFinalSaikenZandaka;
	/** 最終月 留保債務計 */
	private String totalFinalRyuhosaimu;
	/** 最終月 引当控除後残高 */
	private String finalZandaka;
	/** 最終月 引当金補正額 */
	private String finalKingakuHosei;
	/** 最終月 補正後引当金額 */
	private String finalKingakuAfterHosei;
	/** 最終月 補正後引当控除後残高 */
	private String finalZnadakaAfterHosei;
	/** 最終月 留保債務 */
	private String finalRyuhosaimu;
	/** 最終月 第三者留保債務 */
	private String finalRyuhosaimu3;
	/** 最終月 保全 */
	private String finalHozen;
	/** 最終月 その他回収 */
	private String finalKingakuSonota;
	/** 最終月 履行請求懸念 */
	private String finalKingakuKenen;
	/** 最終月 引当金算定根拠 */
	private String finalCmtValKonkyo;
	/** 最終月 基準年月 */
	private String txtFinalYmKijun;
	/** 最終月 取引先区分 */
	private String finalKbnTorihiki;
	/** 最終月 債権区分 */
	private String finalKbnSaiken;
    
	/**
	 * 処理回数制御
	 */
	private int initmode;
	/** 取引セレクトボックス値 */
	private LinkedHashMap torihikiList;
	/** 債権セレクトボックス値 */
	private LinkedHashMap saikenList;
    /** フォーカスの位置 */
	private int focusId;
	
    // No797, 2008/06/09, SJA渡辺, 案件の査定会社コードを保持するプロパティ追加
    /** 案件の査定会社コード */
	private String sateikaisya_cd;
	
	/** コンストラクタ */
	public KensyoForm() {
		cmt00 = false;
		focusId = 0;
    	tantoSyonin = "";
    	bumonSyonin = "";
    	indexSyonin = "";
	}

	public String toString() {
		return super.gamenId;
	}
	
	public String getSateikaisya_cd() {
		return sateikaisya_cd;
	}
	public void setSateikaisya_cd(String sateikaisya_cd) {
		this.sateikaisya_cd = sateikaisya_cd;
	}
	public String getHidFlgSasiTen() {
		return hidFlgSasiTen;
	}
	public void setHidFlgSasiTen(String hidFlgSasiTen) {
		this.hidFlgSasiTen = hidFlgSasiTen;
	}
	public int getFocusId() {
		return focusId;
	}
	public void setFocusId(int focusId) {
		this.focusId = focusId;
	}
	public String getTxtNameFinalTuuka() {
		return txtNameFinalTuuka;
	}
	public void setTxtNameFinalTuuka(String txtNameFinalTuuka) {
		this.txtNameFinalTuuka = txtNameFinalTuuka;
	}
	public LinkedHashMap getSaikenList() {
		return saikenList;
	}
	public void setSaikenList(LinkedHashMap saikenList) {
		this.saikenList = saikenList;
	}
	public LinkedHashMap getTorihikiList() {
		return torihikiList;
	}
	public void setTorihikiList(LinkedHashMap torihikiList) {
		this.torihikiList = torihikiList;
	}
	public int getInitmode() {
		return initmode;
	}
	public void setInitmode(int initmode) {
		this.initmode = initmode;
	}
    
	
	public boolean isCmt00() {
		return cmt00;
	}
	public void setCmt00(boolean cmt00) {
		this.cmt00 = cmt00;
	}
	public String getMise_cd() {
		return mise_cd;
	}
	public void setMise_cd(String mise_cd) {
		this.mise_cd = mise_cd;
	}
	public String getSystem_kbn() {
		return system_kbn;
	}
	public void setSystem_kbn(String system_kbn) {
		this.system_kbn = system_kbn;
	}
	public String getTxtNameTuuka() {
		return txtNameTuuka;
	}
	public void setTxtNameTuuka(String txtNameTuuka) {
		this.txtNameTuuka = txtNameTuuka;
	}
	public String getSateiAnkenNo() {
		return sateiAnkenNo;
	}
	public void setSateiAnkenNo(String sateiAnkenNo) {
		this.sateiAnkenNo = sateiAnkenNo;
	}
	public String getLtFiscClosYmd() {
		return LtFiscClosYmd;
	}
	public void setLtFiscClosYmd(String ltFiscClosYmd) {
		LtFiscClosYmd = ltFiscClosYmd;
	}
	public String getTxtNoOyaDuns() {
		return txtNoOyaDuns;
	}
	public void setTxtNoOyaDuns(String txtNoOyaDuns) {
		this.txtNoOyaDuns = txtNoOyaDuns;
	}
	public String getShowKbnTairyu() {
		return showKbnTairyu;
	}
	public void setShowKbnTairyu(String showKbnTairyu) {
		this.showKbnTairyu = showKbnTairyu;
	}
	public String getTxtFlgDokuritu() {
		return txtFlgDokuritu;
	}
	public void setTxtFlgDokuritu(String txtFlgDokuritu) {
		this.txtFlgDokuritu = txtFlgDokuritu;
	}
	public String getTxtFlgIttai() {
		return txtFlgIttai;
	}
	public void setTxtFlgIttai(String txtFlgIttai) {
		this.txtFlgIttai = txtFlgIttai;
	}
	public String getTxtKtk() {
		return txtKtk;
	}
	public void setTxtKtk(String txtKtk) {
		this.txtKtk = txtKtk;
	}
	public String getTxtNameOya() {
		return txtNameOya;
	}
	public void setTxtNameOya(String txtNameOya) {
		this.txtNameOya = txtNameOya;
	}
	public String getTxtOyaKtk() {
		return txtOyaKtk;
	}
	public void setTxtOyaKtk(String txtOyaKtk) {
		this.txtOyaKtk = txtOyaKtk;
	}
	public String getAnken_no() {
		return anken_no;
	}
	public void setAnken_no(String anken_no) {
		this.anken_no = anken_no;
	}
	public String getBumonSyonin() {
		return bumonSyonin;
	}
	public void setBumonSyonin(String bumonSyonin) {
		this.bumonSyonin = bumonSyonin;
	}
	public String getCmtValKonkyo() {
		return cmtValKonkyo;
	}
	public void setCmtValKonkyo(String cmtValKonkyo) {
		this.cmtValKonkyo = cmtValKonkyo;
	}
	public String getFinalCmtValKonkyo() {
		return finalCmtValKonkyo;
	}
	public void setFinalCmtValKonkyo(String finalCmtValKonkyo) {
		this.finalCmtValKonkyo = finalCmtValKonkyo;
	}
	public String getFinalHozen() {
		return finalHozen;
	}
	public void setFinalHozen(String finalHozen) {
		this.finalHozen = finalHozen;
	}
	public String getFinalKingaku01() {
		return finalKingaku01;
	}
	public void setFinalKingaku01(String finalKingaku01) {
		this.finalKingaku01 = finalKingaku01;
	}
	public String getFinalKingaku02() {
		return finalKingaku02;
	}
	public void setFinalKingaku02(String finalKingaku02) {
		this.finalKingaku02 = finalKingaku02;
	}
	public String getFinalKingaku03() {
		return finalKingaku03;
	}
	public void setFinalKingaku03(String finalKingaku03) {
		this.finalKingaku03 = finalKingaku03;
	}
	public String getFinalKingaku04() {
		return finalKingaku04;
	}
	public void setFinalKingaku04(String finalKingaku04) {
		this.finalKingaku04 = finalKingaku04;
	}
	public String getFinalKingaku05() {
		return finalKingaku05;
	}
	public void setFinalKingaku05(String finalKingaku05) {
		this.finalKingaku05 = finalKingaku05;
	}
	public String getFinalKingaku06() {
		return finalKingaku06;
	}
	public void setFinalKingaku06(String finalKingaku06) {
		this.finalKingaku06 = finalKingaku06;
	}
	public String getFinalKingaku07() {
		return finalKingaku07;
	}
	public void setFinalKingaku07(String finalKingaku07) {
		this.finalKingaku07 = finalKingaku07;
	}
	public String getFinalKingaku08() {
		return finalKingaku08;
	}
	public void setFinalKingaku08(String finalKingaku08) {
		this.finalKingaku08 = finalKingaku08;
	}
	public String getFinalKingaku09() {
		return finalKingaku09;
	}
	public void setFinalKingaku09(String finalKingaku09) {
		this.finalKingaku09 = finalKingaku09;
	}
	public String getFinalKingaku10() {
		return finalKingaku10;
	}
	public void setFinalKingaku10(String finalKingaku10) {
		this.finalKingaku10 = finalKingaku10;
	}
	public String getFinalKingaku11() {
		return finalKingaku11;
	}
	public void setFinalKingaku11(String finalKingaku11) {
		this.finalKingaku11 = finalKingaku11;
	}
	public String getFinalKingaku12() {
		return finalKingaku12;
	}
	public void setFinalKingaku12(String finalKingaku12) {
		this.finalKingaku12 = finalKingaku12;
	}
	public String getFinalKingaku14() {
		return finalKingaku14;
	}
	public void setFinalKingaku14(String finalKingaku14) {
		this.finalKingaku14 = finalKingaku14;
	}
	public String getFinalKingaku15() {
		return finalKingaku15;
	}
	public void setFinalKingaku15(String finalKingaku15) {
		this.finalKingaku15 = finalKingaku15;
	}
	public String getFinalKingaku16() {
		return finalKingaku16;
	}
	public void setFinalKingaku16(String finalKingaku16) {
		this.finalKingaku16 = finalKingaku16;
	}
	public String getFinalKingakuAfterHosei() {
		return finalKingakuAfterHosei;
	}
	public void setFinalKingakuAfterHosei(String finalKingakuAfterHosei) {
		this.finalKingakuAfterHosei = finalKingakuAfterHosei;
	}
	public String getFinalKingakuHosei() {
		return finalKingakuHosei;
	}
	public void setFinalKingakuHosei(String finalKingakuHosei) {
		this.finalKingakuHosei = finalKingakuHosei;
	}
	public String getFinalKingakuKenen() {
		return finalKingakuKenen;
	}
	public void setFinalKingakuKenen(String finalKingakuKenen) {
		this.finalKingakuKenen = finalKingakuKenen;
	}
	public String getFinalKingakuSonota() {
		return finalKingakuSonota;
	}
	public void setFinalKingakuSonota(String finalKingakuSonota) {
		this.finalKingakuSonota = finalKingakuSonota;
	}
	public String getFinalRyuhosaimu() {
		return finalRyuhosaimu;
	}
	public void setFinalRyuhosaimu(String finalRyuhosaimu) {
		this.finalRyuhosaimu = finalRyuhosaimu;
	}
	public String getFinalRyuhosaimu3() {
		return finalRyuhosaimu3;
	}
	public void setFinalRyuhosaimu3(String finalRyuhosaimu3) {
		this.finalRyuhosaimu3 = finalRyuhosaimu3;
	}
	public String getFinalZandaka() {
		return finalZandaka;
	}
	public void setFinalZandaka(String finalZandaka) {
		this.finalZandaka = finalZandaka;
	}
	public String getFinalZnadakaAfterHosei() {
		return finalZnadakaAfterHosei;
	}
	public void setFinalZnadakaAfterHosei(String finalZnadakaAfterHosei) {
		this.finalZnadakaAfterHosei = finalZnadakaAfterHosei;
	}
	public String getHozen() {
		return hozen;
	}
	public void setHozen(String hozen) {
		this.hozen = hozen;
	}
	public String getIndexSyonin() {
		return indexSyonin;
	}
	public void setIndexSyonin(String indexSyonin) {
		this.indexSyonin = indexSyonin;
	}
	public String getKingaku01() {
		return kingaku01;
	}
	public void setKingaku01(String kingaku01) {
		this.kingaku01 = kingaku01;
	}
	public String getKingaku02() {
		return kingaku02;
	}
	public void setKingaku02(String kingaku02) {
		this.kingaku02 = kingaku02;
	}
	public String getKingaku03() {
		return kingaku03;
	}
	public void setKingaku03(String kingaku03) {
		this.kingaku03 = kingaku03;
	}
	public String getKingaku04() {
		return kingaku04;
	}
	public void setKingaku04(String kingaku04) {
		this.kingaku04 = kingaku04;
	}
	public String getKingaku05() {
		return kingaku05;
	}
	public void setKingaku05(String kingaku05) {
		this.kingaku05 = kingaku05;
	}
	public String getKingaku06() {
		return kingaku06;
	}
	public void setKingaku06(String kingaku06) {
		this.kingaku06 = kingaku06;
	}
	public String getKingaku07() {
		return kingaku07;
	}
	public void setKingaku07(String kingaku07) {
		this.kingaku07 = kingaku07;
	}
	public String getKingaku08() {
		return kingaku08;
	}
	public void setKingaku08(String kingaku08) {
		this.kingaku08 = kingaku08;
	}
	public String getKingaku09() {
		return kingaku09;
	}
	public void setKingaku09(String kingaku09) {
		this.kingaku09 = kingaku09;
	}
	public String getKingaku10() {
		return kingaku10;
	}
	public void setKingaku10(String kingaku10) {
		this.kingaku10 = kingaku10;
	}
	public String getKingaku11() {
		return kingaku11;
	}
	public void setKingaku11(String kingaku11) {
		this.kingaku11 = kingaku11;
	}
	public String getKingaku12() {
		return kingaku12;
	}
	public void setKingaku12(String kingaku12) {
		this.kingaku12 = kingaku12;
	}
	public String getKingaku14() {
		return kingaku14;
	}
	public void setKingaku14(String kingaku14) {
		this.kingaku14 = kingaku14;
	}
	public String getKingaku15() {
		return kingaku15;
	}
	public void setKingaku15(String kingaku15) {
		this.kingaku15 = kingaku15;
	}
	public String getKingaku16() {
		return kingaku16;
	}
	public void setKingaku16(String kingaku16) {
		this.kingaku16 = kingaku16;
	}
	public String getKingakuHikiateTaisyo() {
		return kingakuHikiateTaisyo;
	}
	public void setKingakuHikiateTaisyo(String kingakuHikiateTaisyo) {
		this.kingakuHikiateTaisyo = kingakuHikiateTaisyo;
	}
	public String getKingakuKenen() {
		return kingakuKenen;
	}
	public void setKingakuKenen(String kingakuKenen) {
		this.kingakuKenen = kingakuKenen;
	}
	public String getKingakuSonota() {
		return kingakuSonota;
	}
	public void setKingakuSonota(String kingakuSonota) {
		this.kingakuSonota = kingakuSonota;
	}
	public String getKingakuTuika() {
		return kingakuTuika;
	}
	public void setKingakuTuika(String kingakuTuika) {
		this.kingakuTuika = kingakuTuika;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public List getRecoName() {
		return recoName;
	}
	public void setRecoName(List recoName) {
		this.recoName = recoName;
	}
	public String getRyuhosaimu() {
		return ryuhosaimu;
	}
	public void setRyuhosaimu(String ryuhosaimu) {
		this.ryuhosaimu = ryuhosaimu;
	}
	public String getRyuhosaimu3() {
		return ryuhosaimu3;
	}
	public void setRyuhosaimu3(String ryuhosaimu3) {
		this.ryuhosaimu3 = ryuhosaimu3;
	}
	public String getTantoSyonin() {
		return tantoSyonin;
	}
	public void setTantoSyonin(String tantoSyonin) {
		this.tantoSyonin = tantoSyonin;
	}
	public String getTotalFinalRyuhosaimu() {
		return totalFinalRyuhosaimu;
	}
	public void setTotalFinalRyuhosaimu(String totalFinalRyuhosaimu) {
		this.totalFinalRyuhosaimu = totalFinalRyuhosaimu;
	}
	public String getTotalFinalSaikenIppan() {
		return totalFinalSaikenIppan;
	}
	public void setTotalFinalSaikenIppan(String totalFinalSaikenIppan) {
		this.totalFinalSaikenIppan = totalFinalSaikenIppan;
	}
	public String getTotalFinalSaikenZandaka() {
		return totalFinalSaikenZandaka;
	}
	public void setTotalFinalSaikenZandaka(String totalFinalSaikenZandaka) {
		this.totalFinalSaikenZandaka = totalFinalSaikenZandaka;
	}
	public String getTotalRyuhosaimu() {
		return totalRyuhosaimu;
	}
	public void setTotalRyuhosaimu(String totalRyuhosaimu) {
		this.totalRyuhosaimu = totalRyuhosaimu;
	}
	public String getTotalSaikenIppan() {
		return totalSaikenIppan;
	}
	public void setTotalSaikenIppan(String totalSaikenIppan) {
		this.totalSaikenIppan = totalSaikenIppan;
	}
	public String getTotalSaikenZandaka() {
		return totalSaikenZandaka;
	}
	public void setTotalSaikenZandaka(String totalSaikenZandaka) {
		this.totalSaikenZandaka = totalSaikenZandaka;
	}
	public String getTxtCdTorihiki() {
		return txtCdTorihiki;
	}
	public void setTxtCdTorihiki(String txtCdTorihiki) {
		this.txtCdTorihiki = txtCdTorihiki;
	}
	public String getFinalKbnSaiken() {
		return finalKbnSaiken;
	}
	public void setFinalKbnSaiken(String txtFinalKbnSaiken) {
		this.finalKbnSaiken = txtFinalKbnSaiken;
	}
	public String getFinalKbnTorihiki() {
		return finalKbnTorihiki;
	}
	public void setFinalKbnTorihiki(String txtFinalKbnTorihiki) {
		this.finalKbnTorihiki = txtFinalKbnTorihiki;
	}
	public String getTxtFinalYmKijun() {
		return txtFinalYmKijun;
	}
	public void setTxtFinalYmKijun(String txtFinalYmSatei) {
		this.txtFinalYmKijun = txtFinalYmSatei;
	}
	public String getTxtKbnSaiken() {
		return txtKbnSaiken;
	}
	public void setTxtKbnSaiken(String txtKbnSaiken) {
		this.txtKbnSaiken = txtKbnSaiken;
	}
	public String getTxtKbnTorihiki() {
		return txtKbnTorihiki;
	}
	public void setTxtKbnTorihiki(String txtKbnTorihiki) {
		this.txtKbnTorihiki = txtKbnTorihiki;
	}
	public String getTxtNameTorihiki() {
		return txtNameTorihiki;
	}
	public void setTxtNameTorihiki(String txtNameTorihiki) {
		this.txtNameTorihiki = txtNameTorihiki;
	}
	public String getTxtNoDuns() {
		return txtNoDuns;
	}
	public void setTxtNoDuns(String txtNoDuns) {
		this.txtNoDuns = txtNoDuns;
	}
	public String getTxtYmSatei() {
		return txtYmSatei;
	}
	public void setTxtYmSatei(String txtYmSatei) {
		this.txtYmSatei = txtYmSatei;
	}
	public String getYm() {
		return ym;
	}
	public void setYm(String ym) {
		this.ym = ym;
	}
    //結合テスト障害No038対応
    //追加開始
	public String getHanki_sihanki_kbn() {
		return hanki_sihanki_kbn;
	}
	public void setHanki_sihanki_kbn(String hanki_sihanki_kbn) {
		this.hanki_sihanki_kbn = hanki_sihanki_kbn;
	}	
    //追加完了
}