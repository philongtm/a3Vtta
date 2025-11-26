/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/

package app.common.form;

import common.global.GS;

/**
 *  履歴情報Beanクラス
 */
public class RirekiBean{
	
	private String id;					// id
	private String anken_no;			// 案件No
	private String tanto_user_id;		// 担当ユーザID
	private String sateikaisya_cd;		// 査定会社コード
	private String bunrui2;			// 分類２
	private String tanto_nm;			// 担当者名
	private String soshiki;			// 組織
	private String nyuryoku_kbn;		// 入力区分
	private String phase;				// フェーズ
	private String phase_nm;			// フェーズ名称
	private String status;				// ステータス
	private String syori;				// 処理
	private String syori_dt;			// 処理日時
	private boolean soshiki_chk;		// 組織選択チェック表示フラグ
	private String td_style;			// TDスタイル
	private String td_styleBottom;		// TDスタイル

	public RirekiBean() {
		
		this.id = GS.EMPTY_CHARCTER;
		this.anken_no = GS.EMPTY_CHARCTER;
		this.tanto_user_id = GS.EMPTY_CHARCTER;
		this.sateikaisya_cd = GS.EMPTY_CHARCTER;
		this.bunrui2 = GS.EMPTY_CHARCTER;
		this.tanto_nm = GS.EMPTY_CHARCTER;
		this.soshiki = GS.EMPTY_CHARCTER;
		this.nyuryoku_kbn = GS.EMPTY_CHARCTER;
		this.phase = GS.EMPTY_CHARCTER;
		this.phase_nm = GS.EMPTY_CHARCTER;
		this.status = GS.EMPTY_CHARCTER;
		this.syori = GS.EMPTY_CHARCTER;
		this.syori_dt = GS.EMPTY_CHARCTER;
		this.soshiki_chk = false;
		this.td_style = GS.EMPTY_CHARCTER;;
		this.td_styleBottom = GS.EMPTY_CHARCTER;;
	}

	//id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	//案件No
	public String getAnken_no() {
		return anken_no;
	}
	public void setAnken_no(String anken_no) {
		this.anken_no = anken_no;
	}
	//担当ユーザID
	public String getTanto_user_id() {
		return tanto_user_id;
	}
	public void setTanto_user_id(String tanto_user_id) {
		this.tanto_user_id = tanto_user_id;
	}
	//査定会社コード
	public String getSateikaisya_cd() {
		return sateikaisya_cd;
	}
	public void setSateikaisya_cd(String sateikaisya_cd) {
		this.sateikaisya_cd = sateikaisya_cd;
	}
	//分類２
	public String getBunrui2() {
		return bunrui2;
	}
	public void setBunrui2(String bunrui2) {
		this.bunrui2 = bunrui2;
	}
	//担当者名
	public String getTanto_nm() {
		return tanto_nm;
	}
	public void setTanto_nm(String tanto_nm) {
		this.tanto_nm = tanto_nm;
	}
	//組織
	public String getSoshiki() {
		return soshiki;
	}
	public void setSoshiki(String soshiki) {
		this.soshiki = soshiki;
	}
	//入力区分
	public String getNyuryoku_kbn() {
		return nyuryoku_kbn;
	}
	public void setNyuryoku_kbn(String nyuryoku_kbn) {
		this.nyuryoku_kbn = nyuryoku_kbn;
	}
	//フェーズ
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	//フェーズ名称
	public String getPhase_nm() {
		return phase_nm;
	}
	public void setPhase_nm(String phase_nm) {
		this.phase_nm = phase_nm;
	}
	//ステータス
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	//処理
	public String getSyori() {
		return syori;
	}
	public void setSyori(String syori) {
		this.syori = syori;
	}
	//処理日時
	public String getSyori_dt() {
		return syori_dt;
	}
	public void setSyori_dt(String syori_dt) {
		this.syori_dt = syori_dt;
	}
	//組織選択チェックフラグ
	public boolean isSoshiki_chk() {
		return soshiki_chk;
	}
	public void setSoshiki_chk(boolean soshiki_chk) {
		this.soshiki_chk = soshiki_chk;
	}
	//TDスタイル
	public String getTd_style() {
		return td_style;
	}
	public void setTd_style(String td_style) {
		this.td_style = td_style;
	}
	//TDスタイル
	public String getTd_styleBottom() {
		return td_styleBottom;
	}
	public void setTd_styleBottom(String td_styleBottom) {
		this.td_styleBottom = td_styleBottom;
	}
}