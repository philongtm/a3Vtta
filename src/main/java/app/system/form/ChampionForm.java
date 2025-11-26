/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
///////////////////////////////////////
//障害票No359
//チェックイン日 2008/05/15
//対応者 　福士
//修正概要
//①ラジオボタン選択値を取得可能な状態に修正
//  （変数chkとget＆setメソッド追加）
///////////////////////////////////////
package app.system.form;

import common.struts.AppPagerActionForm;

/**
 * チャンピオン部メンテナンス画面Formクラス
 */
public class ChampionForm extends AppPagerActionForm {

	
    private int id;				// 勘定先ＣＤリンク押下時の引数 
    private String anken_no;		// 勘定先ＣＤリンク押下時の引数(情報なしのためnull固定)
    private String[] selectArr;	// ラジオボタンの選択情報

    private String chk;			// 画面選択行リスト
    								// ※ 親明細行No.-部明細行No.で選択行を表したものを『/』区切りにした文字列
	
    /**
     * コンストラクタ
     */
    public ChampionForm() {
    	// 変数初期化
    	this.id = 0;
        this.anken_no = null;
        this.selectArr = null;
        this.chk = "";
    }

    /**
     * @return chk を取得。
     */
    public String toString() {
        return super.gamenId;
    }

    /**
     * @return chk を取得。
     */
    public String getChk() {
        return this.chk;
    }
    /**
     * @param chk chk を設定。
     */
    public void setChk(String chk) {
        this.chk = chk;
    }
 
    /**
     * @return id を取得。
     */
    public int getId() {
        return this.id;
    }
    /**
     * @param id id を設定。
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return anken_no を取得。
     */
    public String getAnken_no() {
        return this.anken_no;
    }
    /**
     * @param anken_no anken_no を設定。
     */
    public void setAnken_no(String anken_no) {
        this.anken_no = anken_no;
    }
    
    /**
     * @return selectArr を取得。
     */
    public String[] getSelectArr() {
        return this.selectArr;
    }
    /**
     * @param selectArr selectArr を設定。
     */
    public void setSelectArr(String[] selectArr) {
        this.selectArr = selectArr;
    }

}