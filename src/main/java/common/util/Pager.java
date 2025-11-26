/******************************************************************************
 著作権情報				:
 使用JDK バージョン		: 1.4.2.05
 更新履歴
 No		日付			修正者			修正内容
 ******************************************************************************/
package common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * ページ管理クラス
 * 
 *  
 */
public class Pager {

	List searchBeanList;
	int currentIndex;
	int pageCount;
	int currentPage;
	int listSize;
	boolean enable;
	boolean nextPageEnable;
	boolean previousPageEnable;
	int pageSize = 10;
	
	/**
	 * コンストラクタ<br>
	 * 
	 * １ページあたりのサイズを指定してページャクラス<br>
	 * のインスタンスを作成する。<br>
	 * 
	 * @param list
	 * @param pageSize
	 */
	public Pager(List list, int pageSize) {
		if (pageSize > 1) {
			this.pageSize = pageSize;
		}
		initialize(list);
	}

	/**
	 * コンストラクタ<br>
	 * 
	 * ページャクラスのインスタンスを作成する。<br>
	 * 
	 * @param list
	 */
	public Pager(List list) {
		initialize(list);
	}
	
	/**
	 * リスト取得<br>
	 * 
	 * ページャクラスに設定されているリストを取得する。<br>
	 * 
	 * @return searchBeanList
	 */
	public List getList() {
		if (enable) {
			return searchBeanList;
		} else {
			return null;
		}
	}
	
	/**
	 * コンストラクタの実処理です。<br>
	 * 
	 * @param list
	 */
	private void initialize(List list) {
		searchBeanList = list;
		listSize = list.size();
		if (listSize > 0) {
			currentIndex = 1;
			pageCount = listSize / pageSize;
			if (listSize % pageSize > 0) {
				pageCount += 1;
			}
			currentPage = 1;
			previousPageEnable = false;
			if (pageCount == 1) {
				nextPageEnable = false;
			} else {
				nextPageEnable = true;
			}
//			if (listSize == 1) {
//				enable = false;
//			} else {
//				enable = true;
//			}
			enable = true;
		} else {
			currentIndex = 0;
			pageCount = 0;
			currentPage = 0;
			listSize = 0;
			enable = false;
			nextPageEnable = false;
			previousPageEnable = false;
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
		if (!enable) {
			return 0;
		}
		int lastIndexOfCurrentPage = currentPage * pageSize;
		if (lastIndexOfCurrentPage > listSize) {
			lastIndexOfCurrentPage = listSize;
		}
		return lastIndexOfCurrentPage;
	}

	/**
	 * 次のＹ件取得<br>
	 * 
	 * 画面明細右上の『次のＹ件』のリンク文字列を取得します。<br>
	 * カレントページが最終ページの場合は""を取得します。<br>
	 * 
	 * @return String
	 */
	public String getY() {
	    int lastPage = (int)Math.floor(pageCount);
	    int next = currentPage + 1;
		if(next > lastPage) {
		    return "";
		} else if(next == lastPage) {
		    return "次の" + (listSize - (pageSize * currentPage)) + "件→";
		} else {
		    return "次の" + pageSize + "件→";
		}
	}
	
	/**
	 * NextＹData取得<br>
	 * 
	 * 画面明細右上の『次のＹ件』のリンク文字列を取得します。<br>
	 * カレントページが最終ページの場合は""を取得します。<br>
	 * 
	 * @return String
	 */
	public String getYen() {
	    int lastPage = (int)Math.floor(pageCount);
	    int next = currentPage + 1;
		if(next > lastPage) {
		    return "";
		} else if(next == lastPage) {
		    return "Next" + (listSize - (pageSize * currentPage)) + "Data>>";
		} else {
		    return "Next" + pageSize + "Data>>";
		}
	}

	/**
	 * 前のＸ件取得<br>
	 * 
	 * 画面明細右上の『前のＸ件』のリンク文字列を取得します。<br>
	 * カレントページが１ページの場合は""を取得します。<br>
	 * 
	 * @return String
	 */
	public String getX() {
		if(currentPage > 1) {
		    return "←前の" + pageSize + "件";
		} else {
		    return "";
		}
	}
	
	/**
	 * PrevＸData取得<br>
	 * 
	 * 画面明細右上の『前のＸ件』のリンク文字列を取得します。<br>
	 * カレントページが１ページの場合は""を取得します。<br>
	 * 
	 * @return String
	 */
	public String getXen() {
		if(currentPage > 1) {
		    return "<<Prev" + pageSize + "Data";
		} else {
		    return "";
		}
	}

	/**
	 * 次ページ取得<br>
	 * 
	 * 次ページの番号を取得します。<br>
	 * カレントページが最終ページの場合は最終ページの番号を取得します。<br>
	 * 
	 * @return 次ページの番号
	 */
	public int getNextPage() {
		if (!enable) {
			return 0;
		}
		currentPage += 1;
		previousPageEnable = true;
		if (currentPage >= pageCount) {
			currentPage = pageCount;
			nextPageEnable = false;
		}
		return currentPage;
	}
	
	/**
	 * 前ページ取得<br>
	 * 
	 * 前ページの番号を取得します。<br>
	 * カレントページが先頭ページの場合は先頭ページの番号を取得します。<br>
	 * 
	 * @return 前ページの番号
	 */
	public int getPreviousPage() {
		if (!enable) {
			return 0;
		}
		currentPage -= 1;
		nextPageEnable = true;
		if (currentPage <= 1) {
			currentPage = 1;
			previousPageEnable = false;
		}
		return currentPage;
	}
	
	/**
	 * リストサイズ取得<br>
	 * 
	 * コンストラクタにて設定したリストのサイズを取得します。<br>
	 * 
	 * @return リストサイズ
	 */
	public int getListSize() {
		return listSize;
	}
	
	/**
	 * カレントページのリスト取得<br>
	 * 
	 * カレントページが保持するリストを取得します。<br>
	 * また、このメソッドを実行するとカレントインデックスが<br>
	 * 変更されることに注意してください。<br>
	 * 
	 * @return カレントページのリスト
	 */
	public List getObjectListOfCurrentPage() throws PagerException {
		if (!enable) {
			return null;
		}
		List objectList = new ArrayList();
		String[] index = getIndexOfCurrentPage();
		for (int i = 0; i < index.length; i++) {
			objectList.add(getBeanOfCurrentIndex(index[i]));
		}
		return objectList;
	}

	/**
	 * 次ページのリスト取得<br>
	 * 
	 * 次ページに遷移し、保持するリストを取得します。<br>
	 * 既にカレントページが最終ページの場合は、最終ページ<br>
	 * のリストが返されます。<br>
	 * また、このメソッドを実行するとカレントインデックスが<br>
	 * 変更されることに注意してください。<br>
	 * 
	 * @return カレントページのリスト
	 */
	public List getObjectListOfNextPage() throws PagerException {
		if (!enable) {
			return null;
		}
		List objectList = new ArrayList();
		String[] index = getNextIndexOfCurrentPage();
		for (int i = 0; i < index.length; i++) {
			objectList.add(getBeanOfCurrentIndex(index[i]));
		}
		return objectList;
	}

	/**
	 * 前ページのリスト取得<br>
	 * 
	 * 前ページに遷移し、保持するリストを取得します。<br>
	 * 既にカレントページが先頭ページの場合は、先頭ページ<br>
	 * のリストが返されます。<br>
	 * また、このメソッドを実行するとカレントインデックスが<br>
	 * 変更されることに注意してください。<br>
	 * 
	 * @return カレントページのリスト
	 */
	public List getObjectListOfPreviouPage() throws PagerException {
		if (!enable) {
			return null;
		}
		List objectList = new ArrayList();
		String[] index = getPreviouIndexOfCurrentPage();
		for (int i = 0; i < index.length; i++) {
			objectList.add(getBeanOfCurrentIndex(index[i]));
		}
		return objectList;
	}

	/**
	 * カレントページのインデックス取得<br>
	 * 
	 * カレントページにて表示するインデックスをString配列にて取得します。<br>
	 * 
	 * @return カレントページのインデックスの配列
	 */
	public String[] getIndexOfCurrentPage() {
		if (!enable) {
			return null;
		}
		String[] indexOfPage;
		int lastIndex;
		if (currentPage == pageCount) {
			lastIndex = listSize - ((pageCount - 1) * pageSize);
		} else {
			lastIndex = pageSize;
		}
		indexOfPage = new String [lastIndex];
		for (int i = 0; i < lastIndex; i++) {
			indexOfPage[i] = Integer.toString((i + 1) + ((currentPage -1) * pageSize));
		}
		return indexOfPage;
	}

	/**
	 * 次ページのインデックス取得<br>
	 * 
	 * 次ページにて表示するインデックスをString配列にて取得します。<br>
	 * 
	 * @return 次ページのインデックスの配列
	 */
	public String[] getNextIndexOfCurrentPage() {
		if (!enable) {
			return null;
		}
		if (nextPageEnable) {
			getNextPage();
		}
		return getIndexOfCurrentPage();
	}
	
	/**
	 * 前ページのインデックス取得<br>
	 * 
	 * 前ページにて表示するインデックスをString配列にて取得します。<br>
	 * 
	 * @return 前ページのインデックスの配列
	 */
	public String[] getPreviouIndexOfCurrentPage() {
		if (!enable) {
			return null;
		}
		if (previousPageEnable) {
			getPreviousPage();
		}
		return getIndexOfCurrentPage();
	}
	
	/**
	 * ページャ使用フラグ取得<br>
	 * 
	 * ページャの使用可否を示すフラグを取得します。
	 * 
	 * @return true:使用可 false:使用不可
	 */
	public boolean getEnable() {
		return enable;
	}
	
	/**
	 * 次ページ使用フラグ取得<br>
	 * 
	 * 次ページの使用可否を示すフラグを取得します。
	 * 
	 * @return true:使用可 false:使用不可
	 */
	public boolean getNextPageEnable() {
		return nextPageEnable;
	}
	
	/**
	 * 前ページ使用フラグ取得<br>
	 * 
	 * 前ページの使用可否を示すフラグを取得します。
	 * 
	 * @return true:使用可 false:使用不可
	 */
	public boolean getPreviousPageEnable() {
		return previousPageEnable;
	}
	
	/**
	 * カレントインデックスのリストオブジェクト取得<br>
	 * 
	 * カレントインデックスを指定し、該当のインデックスの<br>
	 * リストオブジェクトを取得します。<br>
	 * 
	 * @param index
	 * @return パラメータのインデックスに該当するリストオブジェクト
	 * @exception PagerException
	 */
	public Object getBeanOfCurrentIndex(String index) throws PagerException{
		if (!enable) {
			return null;
		}
		int intIndex;
		try {
			intIndex = Integer.parseInt(index);
		} catch (NumberFormatException e) {
			throw new PagerException(e);
		} catch (NullPointerException ne) {
			throw new PagerException(ne);
		}
		return getBeanOfCurrentIndex(intIndex);
		
	}

	/**
	 * カレントインデックスのリストオブジェクト取得<br>
	 * 
	 * カレントインデックスを指定し、該当のインデックスの<br>
	 * リストオブジェクトを取得します。<br>
	 * 
	 * @param index
	 * @return パラメータのインデックスに該当するリストオブジェクト
	 * @exception PagerException
	 */
	public Object getBeanOfCurrentIndex(int index) throws PagerException{
		if (!enable) {
			return null;
		}
		setCurrentIndex(index);
		return getBeanOfCurrentIndex();
	}
	
	/**
	 * カレントインデックスのリストオブジェクト取得<br>
	 * 
	 * カレントインデックスのインデックスのリストオブジェクトを取得します。<br>
	 * 
	 * @return カレントインデックスに該当するリストオブジェクト
	 */
	public Object getBeanOfCurrentIndex() {
		if (!enable && listSize == 0) {
			return null;
		}
		return searchBeanList.get(currentIndex -1);
	}

	/**
	 * カレントインデックス取得<br>
	 * 
	 * カレントインデックスを取得します。<br>
	 * 
	 * @return カレントインデックス
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	/**
	 * カレントインデックス設定<br>
	 * 
	 * カレントインデックスを設定します。<br>
	 * 
	 * @param index インデックス
	 */
	public void setCurrentIndex(int index) throws PagerException {
		if (index < 1 || listSize < index) {
			throw new PagerException("カレントインデックスがリストの範囲外です。"); 
		}
		currentIndex = index;
	}
}