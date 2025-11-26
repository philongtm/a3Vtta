/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/

package app.common.form;

import common.global.GS;

import java.util.List;


/**
 *  履歴情報リストBeanクラス
 */
public class RirekiListBean{
	
	private String chk_id;			// 組織選択チェックボックス
	private String radio_id;		// フェーズ選択ラジオボタン
	private List<RirekiBean> rireki_list;	 	// 履歴リスト情報【リスト】

	public RirekiListBean() {
		
		this.chk_id = GS.EMPTY_CHARCTER;
		this.radio_id = GS.EMPTY_CHARCTER;
		this.rireki_list = null;
	}

	//組織選択チェックボックス
	public String getChk_id() {
		return chk_id;
	}
	public void setChk_id(String chk_id) {
		this.chk_id = chk_id;
	}
	//フェーズ選択ラジオボタン
	public String getRadio_id() {
		return radio_id;
	}
	public void setRadio_id(String radio_id) {
		this.radio_id = radio_id;
	}
	//履歴リスト
	public List<RirekiBean> getRireki_list() {
		return rireki_list;
	}
	public void setRireki_list(List<RirekiBean> rireki_list) {
		this.rireki_list = rireki_list;
	}

}