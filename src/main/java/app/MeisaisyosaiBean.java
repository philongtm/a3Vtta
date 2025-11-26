/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/27		SSC				課題No.169 滞留判定取得 
******************************************************************************/
package app;


import common.global.GS;

/**
 * 明細詳細情報Beanクラス
 * 
 */
public class MeisaisyosaiBean {

	private String id					= null;	// 明細ID
	private String anken_no_eda		= null;	// 案件No枝番
	private String tenpu_anken_no		= null;	// 添付選択画面で使用する滞留案件No.
	private String tenpu_anken_no_eda  = null;	
	private String cell_nm				= null;	// セル名称
	private String syusi_yoteibi		= null;	// 収支予定日
	private String mankibi				= null;	// 満期日	
	private String kanjo_syoribi		= null;	// 勘定処理日	
	private String kingaku_kei	       	= null;	// 金額計	
	private String tuuka_cd			= null;	// 通貨コード
	private String keiyaku_denpyo_no	= null;	// 契約伝票No
	private String invoice_no			= null;	// インボイスNo
	private String komoku1				= null;	// 項目１
	private String komoku2				= null;	// 項目２
	private String komoku3				= null;	// 項目３
	private String komoku4				= null;	// 項目４
	private String komoku5				= null;	// 項目５
	private String jimusyo_cd			= null;	// 事務所コード
	private String kanjo_kamoku_cd		= null;	// 勘定科目CD
	private String kanjo_kamoku_nm		= null;	// 勘定科目
	private String tairyu_kbn			= null;	// 滞留区分
	//課題No.169
	//追加開始
	private String tairyuHantei		= null;	// 滞留判定
	//追加完了
	private String tairyu_hantei		= null;	// 滞留判定表示地
	private String hantei_jiyu			= null;	// 判定事由
	private String tairyu_kbn_cd		= null;	// 滞留区分コード
	private String bunsyo_no			= null;	// 文書No
	private String hanyo1				= null;	// 汎用１
	private String soshiki				= null;	// 組織
	private String ryuhosaimu			= null;	// 留保債務
	private String biko				= null;	// 備考
	
	/**
	 * 初期化処理を行う。
	 */
	public void initialize() {
		//ユーザ情報
		id					= GS.EMPTY_CHARCTER;
		anken_no_eda		= GS.EMPTY_CHARCTER;
		cell_nm				= GS.EMPTY_CHARCTER;
		syusi_yoteibi		= GS.EMPTY_CHARCTER;
		mankibi				= GS.EMPTY_CHARCTER;
		kanjo_syoribi		= GS.EMPTY_CHARCTER;
		kingaku_kei	       	= GS.EMPTY_CHARCTER;
		tuuka_cd		   	= GS.EMPTY_CHARCTER;
		keiyaku_denpyo_no	= GS.EMPTY_CHARCTER;
		invoice_no			= GS.EMPTY_CHARCTER;
		komoku1				= GS.EMPTY_CHARCTER;
		komoku2				= GS.EMPTY_CHARCTER;
		komoku3				= GS.EMPTY_CHARCTER;
		komoku4				= GS.EMPTY_CHARCTER;
		komoku5				= GS.EMPTY_CHARCTER;
		jimusyo_cd			= GS.EMPTY_CHARCTER;
		kanjo_kamoku_cd		= GS.EMPTY_CHARCTER;
		kanjo_kamoku_nm		= GS.EMPTY_CHARCTER;
		tairyu_kbn			= GS.EMPTY_CHARCTER;
		tairyu_hantei		= GS.EMPTY_CHARCTER;
		hantei_jiyu			= GS.EMPTY_CHARCTER;
		tairyu_kbn_cd		= GS.EMPTY_CHARCTER;
		bunsyo_no			= GS.EMPTY_CHARCTER;
		hanyo1				= GS.EMPTY_CHARCTER;
		soshiki				= GS.EMPTY_CHARCTER;
		ryuhosaimu			= GS.EMPTY_CHARCTER;
		biko				= GS.EMPTY_CHARCTER;
		//課題No.169
		//追加開始
		tairyuHantei		= GS.EMPTY_CHARCTER;
		//追加完了
	}
	
	/**
	 * オブジェクトの破棄を行う。
	 */
	public void destroy() {
		initialize();
	}

	// アクセスメソッド
	
	//明細ID
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	//案件No枝番
	public String getAnken_no_eda() {
		return anken_no_eda;
	}
	public void setAnken_no_eda(String anken_no_eda) {
		this.anken_no_eda = anken_no_eda;
	}
	//セル名称
	public String getCell_nm() {
		return cell_nm;
	}
	public void setCell_nm(String cell_nm) {
		this.cell_nm = cell_nm;
	}
	//収支予定日	
	public String getSyusi_yoteibi() {
		return syusi_yoteibi;
	}
	public void setSyusi_yoteibi(String syusi_yoteibi) {
		this.syusi_yoteibi = syusi_yoteibi;
	}
	//満期日
	public String getMankibi() {
		return mankibi;
	}
	public void setMankibi(String mankibi) {
		this.mankibi = mankibi;
	}
	//勘定処理日
	public String getKanjo_syoribi() {
		return kanjo_syoribi;
	}
	public void setKanjo_syoribi(String kanjo_syoribi) {
		this.kanjo_syoribi = kanjo_syoribi;
	}
	//金額計
	public String getKingaku_kei() {
		return kingaku_kei;
	}
	public void setKingaku_kei(String kingaku_kei) {
		this.kingaku_kei = kingaku_kei;
	}
	//通貨コード
	public String getTuuka_cd() {
		return tuuka_cd;
	}
	public void setTuuka_cd(String tuuka_cd) {
		this.tuuka_cd = tuuka_cd;
	}
	//契約伝票No
	public String getKeiyaku_denpyo_no() {
		return keiyaku_denpyo_no;
	}
	public void setKeiyaku_denpyo_no(String keiyaku_denpyo_no) {
		this.keiyaku_denpyo_no = keiyaku_denpyo_no;
	}
	//インボイスNo
	public String getInvoice_no() {
		return invoice_no;
	}
	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}
	//項目１
	public String getKomoku1() {
		return komoku1;
	}
	public void setKomoku1(String komoku1) {
		this.komoku1 = komoku1;
	}
	//項目２
	public String getKomoku2() {
		return komoku2;
	}
	public void setKomoku2(String komoku2) {
		this.komoku2 = komoku2;
	}
	//項目３
	public String getKomoku3() {
		return komoku3;
	}
	public void setKomoku3(String komoku3) {
		this.komoku3 = komoku3;
	}
	//項目４
	public String getKomoku4() {
		return komoku4;
	}
	public void setKomoku4(String komoku4) {
		this.komoku4 = komoku4;
	}
	//項目５
	public String getKomoku5() {
		return komoku5;
	}
	public void setKomoku5(String komoku5) {
		this.komoku5 = komoku5;
	}
	//事務所コード
	public String getJimusyo_cd() {
		return jimusyo_cd;
	}
	public void setJimusyo_cd(String jimusyo_cd) {
		this.jimusyo_cd = jimusyo_cd;
	}
	//勘定科目CD
	public String getKanjo_kamoku_cd() {
		return kanjo_kamoku_cd;
	}
	public void setKanjo_kamoku_cd(String kanjo_kamoku_cd) {
		this.kanjo_kamoku_cd = kanjo_kamoku_cd;
	}
	//勘定科目
	public String getKanjo_kamoku_nm() {
		return kanjo_kamoku_nm;
	}
	public void setKanjo_kamoku_nm(String kanjo_kamoku_nm) {
		this.kanjo_kamoku_nm = kanjo_kamoku_nm;
	}
	//滞留区分
	public String getTairyu_kbn() {
		return tairyu_kbn;
	}
	public void setTairyu_kbn(String tairyu_kbn) {
		this.tairyu_kbn = tairyu_kbn;
	}
	//滞留判定
	public String getTairyu_hantei() {
		return tairyu_hantei;
	}
	public void setTairyu_hantei(String tairyu_hantei) {
		this.tairyu_hantei = tairyu_hantei;
	}
	//判定事由
	public String getHantei_jiyu() {
		return hantei_jiyu;
	}
	public void setHantei_jiyu(String hantei_jiyu) {
		this.hantei_jiyu = hantei_jiyu;
	}
	//滞留区分コード
	public String getTairyu_kbn_cd() {
		return tairyu_kbn_cd;
	}
	public void setTairyu_kbn_cd(String tairyu_kbn_cd) {
		this.tairyu_kbn_cd = tairyu_kbn_cd;
	}
	//文書No
	public String getBunsyo_no() {
		return bunsyo_no;
	}
	public void setBunsyo_no(String bunsyo_no) {
		this.bunsyo_no = bunsyo_no;
	}
	//汎用１
	public String getHanyo1() {
		return hanyo1;
	}
	public void setHanyo1(String hanyo1) {
		this.hanyo1 = hanyo1;
	}
	//組織
	public String getSoshiki() {
		return soshiki;
	}
	public void setSoshiki(String soshiki) {
		this.soshiki = soshiki;
	}
	//留保債務	 
	public String getRyuhosaimu() {
		return ryuhosaimu;
	}
	public void setRyuhosaimu(String ryuhosaimu) {
		this.ryuhosaimu = ryuhosaimu;
	}
	//備考
	public String getBiko() {
		return biko;
	}
	public void setBiko(String biko) {
		this.biko = biko;
	}
	public String getTenpu_anken_no() {
		return tenpu_anken_no;
	}
	public void setTenpu_anken_no(String tenpu_anken_no) {
		this.tenpu_anken_no = tenpu_anken_no;
	}
	public String getTenpu_anken_no_eda() {
		return tenpu_anken_no_eda;
	}
	public void setTenpu_anken_no_eda(String tenpu_anken_no_eda) {
		this.tenpu_anken_no_eda = tenpu_anken_no_eda;
	}
	//課題No.169
	//追加開始
	public String getTairyuHantei() {
		return tairyuHantei;
	}
	public void setTairyuHantei(String tairyuHantei) {
		this.tairyuHantei = tairyuHantei;
	}
	//追加完了
}