/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.util;

import java.util.Vector;

/**
 * 一時ファイル一覧クラス
 * 
 */
public class TempFileList {

	private Vector list = new Vector();

	/**
	 * 一時ファイルの追加を行う。
	 * 
	 * @param tmp 一時ファイルクラス
	 */
	public void add(TempFile tmp) {
		list.add(tmp);
	}
	
	/**
	 * 一時ファイルの削除を行う。
	 * <pre>
	 * 一覧から削除すると同時に、物理ファイルの削除も行う。
	 * </pre>
	 * @param index
	 * 			一覧のインデックス
	 */
	public void delete(int index) {
		TempFile tmp;
		tmp = (TempFile)list.get(index);
		tmp.delete();
		tmp = null;
		list.remove(index);
	}

	/**
	 * 一覧の全件削除を行う。
	 * <pre>
	 * 物理ファイルの削除も行う。
	 *</pre>
	 */
	public void deleteAll() {
		for( int i=list.size()-1; i>=0; i-- ) {
			delete(i);
		}
	}

	/**
	 * 一覧の取得を行う。
	 * 
	 * @return 一時ファイルのリスト
	 */
	public Vector getList(){
		return list;
	}

	/**
	 * 一時ファイルの絶対パスの取得を行う。
	 * 
	 * @param index
	 * 			一覧のインデックス
	 * @return 一時ファイルの絶対パス
	 */
	public String getPath(int index) {
    	return ((TempFile)list.get(index)).getPath();
    }
    
	/**
	 * 一時ファイルのサイズの取得を行う。
	 * 
	 * @param index
	 * 			一覧のインデックス
	 * @return 一時ファイルのサイズ
	 */
    public int getLength(int index) {
    	return ((TempFile)list.get(index)).getLength();
    }	

	/**
	 * 一時ファイルの表示用ファイル名の取得を行う。
	 * 
	 * @param index
	 * 			一覧のインデックス
	 * @return 表示用ファイル名
	 */
	public String getDisplayFilename(int index) {
    	return ((TempFile)list.get(index)).getDisplayFilename();
	}

	/**
	 * 一時ファイルの表示用ファイル名の設定を行う。
	 * 
	 * @param index
	 * 			一覧のインデックス
	 * @param displayFilename
	 * 			表示用ファイル名
	 */
	public void setDisplayFilename(int index, String displayFilename) {
		((TempFile)list.get(index)).setDisplayFilename(displayFilename);
	}

	/**
	 * サーバ登録ファイル名の取得を行う。
	 * 
	 * @param index
	 * 			一覧のインデックス
	 * @return 表示用ファイル名
	 */
	public String getServerFilename(int index) {
    	return ((TempFile)list.get(index)).getServerFilename();
	}

	/**
	 * サーバ登録ファイル名の設定を行う。
	 * 
	 * @param index
	 * 			一覧のインデックス
	 * @param displayFilename
	 * 			表示用ファイル名
	 */
	public void setServerFilename(int index, String serverFilename) {
		((TempFile)list.get(index)).setServerFilename(serverFilename);
	}
	
	/**
	 * 一覧のサイズ取得を行う。
	 * 
	 * @return 一覧のサイズ
	 */
	public int size() {
		return list.size();
	}

	protected void finalize() throws Throwable {
		list.clear();
	}
}