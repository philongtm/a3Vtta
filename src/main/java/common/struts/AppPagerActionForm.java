/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.struts;

import common.util.Pager;
import org.apache.struts.action.ActionForm;

import java.util.List;

/**
 * 拡張アクションフォームクラス(Pager用）
 * 
 */
public abstract class AppPagerActionForm extends ActionForm {

    private int view;			// 表示件数（10,30,50,100件）
    private int cnt_meisai;	// 明細データ件数
    private List ar_meisai;	// 明細配列
    private Pager pager;		// ページ制御クラスオブジェクト
    private List list;			// 明細配列の画面表示部分
	protected String gamenId;	//画面ID
	private String langMode;	//選択された帳票言語
    
    // 変数初期化
    public AppPagerActionForm() {
        this.view = 50; 
        this.cnt_meisai = 0;
        this.ar_meisai = null;
        this.pager = null;
        this.list = null;
    }

	/**
	 * @return 画面IDを戻します。
	 */
	public abstract String toString();

	public int getView() {
        return this.view;
    }
    public void setView(int view) {
        this.view = view;
    }
    public int getCnt_meisai() {
        return this.cnt_meisai;
    }
    public void setCnt_meisai(int cnt_meisai) {
        this.cnt_meisai = cnt_meisai;
    }
    public List getAr_meisai() {
        return this.ar_meisai;
    }
    // 実行時にsetPager()を必ず実行
    public void setAr_meisai(List ar_meisai) {
        this.ar_meisai = ar_meisai;
    }
     
	/**
	 * Pagerクラス取得メソッド
	 */
    public Pager getPager() {
        return this.pager;
    }
	/**
	 * Pagerクラス作成メソッド[明細取得時] <br>
	 * setAr_meisai()実行時に必ず実行
	 */
    public void setPager(List ar_meisai) {
        this.pager = new Pager(ar_meisai, view);
        setCurrentList();
    }
	/**
	 * Pagerクラス作成メソッド[表示件数変更時] <br>
	 */
    public void setPager() {
        this.pager = new Pager(ar_meisai, view);
        setCurrentList();
    }
	/**
	 * Pagerクラス作成メソッド<br>
	 * [ar_meisaiの指定したＩＤのページをカレントページに設定] <br>
	 * 
	 * @param int ID:ar_meisaiのインデックス番号
	 * @param int view:画面の表示件数
	 */
    public void setPager(int id, int view) {
        this.view = view;
        this.pager = new Pager(ar_meisai, view);
        setCurrentList();
        
	    int i = 1;
	    while(i == 1) {
	        if(id > view) {
	            setNextList();
	            id = id - view;
	        } else if(id <= view) {
	            i = 0;
	        }
	    }
    }
    /**
     * Pagerクラス作成メソッド<br>
	 * [ar_meisaiの指定したＩＤのページをカレントページに設定] <br>
     */
    public void setPager(int id) {
        this.pager = new Pager(ar_meisai, view);
        setCurrentList();
        int arSize = 0;
        arSize = ar_meisai.size();
        if (ar_meisai != null && arSize != 0) {
        	while (id > arSize) {
        		id--;
        	}
        }
        
	    int i = 1;
	    while(i == 1) {
	        if(id > view) {
	            setNextList();
	            id = id - view;
	        } else if(id <= view) {
	            i = 0;
	        }
	    }
    }    
	/**
	 * 前のＸ件取得<br>
	 */
    public String getX() {
        return pager.getX();
    }
	/**
	 * 次のＹ件取得<br>
	 */    
    public String getY() {
        return pager.getY();
    }
	/**
	 * PrevＸData取得<br>
	 */
    public String getXen() {
        return pager.getXen();
    }
	/**
	 * NextＹData取得<br>
	 */    
    public String getYen() {
        return pager.getYen();
    }
  
	/**
	 * 『明細配列の画面表示部分』取得メソッド
	 */
    public List getList() {
        return this.list;
    }
	/**
	 * 『明細配列の画面表示部分』を前ページに切替
	 */
    public void setPrevList() {
        try {
            this.list = pager.getObjectListOfPreviouPage();
        } catch(Exception e) {
        }
    }
	/**
	 * 『明細配列の画面表示部分』を次ページに切替
	 */
    public void setNextList() {
        try {
            this.list = pager.getObjectListOfNextPage();
        } catch(Exception e) {
        }
    }
	/**
	 * 『明細配列の画面表示部分』をカレントページに切替
	 */
    public void setCurrentList() {
        try {
            this.list = pager.getObjectListOfCurrentPage();
        } catch(Exception e) {
        }
    }
	/**
	 * カレントページの最終インデックス取得<br>
	 * 
	 * カレントページの最終インデックスを取得します。<br>
	 * 
	 * @return カレントページの最終インデックス
	 */
    public int getLastIndexOfCurrentPage() {
    	return pager.getLastIndexOfCurrentPage();
    }

    /**
	 * 帳票言語選択画面にて選択された言語モードを返します。
	 * 
	 * @return 帳票言語選択画面にて選択された言語モード
	 */
	public String getLangMode() {
		return langMode;
	}
	/**
	 * 帳票言語選択画面にて選択された言語モードを設定
	 */
	public void setLangMode(String langMode) {
		this.langMode = langMode;
	}
}