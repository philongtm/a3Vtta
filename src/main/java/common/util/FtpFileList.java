/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.util;

/**
 * ＦＴＰファイル一覧クラス
 * 
 */
public class FtpFileList {
	
    private int size;
	private int[] length;
	private String[] name;
	private String[] timestamp;
	private String[] attr;
	
    /**
     * コンストラクタ
     * 
     * @param lines
     * 			lsコマンドの応答
     */
    public FtpFileList(String[] lines) {
		int	idx;
    	String[] part;
		
		if( lines.length <= 0 ) {
			size = 0;
			return;
		}

		if( Function.StrSplitToken(lines[0]," ").length < 9 ) { 
			idx = 1;
			size = lines.length - 1;
	    } else {
	    	idx = 0;
			size = lines.length;
	    }

	    length = new int[size];
	    name = new String[size];
	    timestamp = new String[size];
	    attr = new String[size];

	    for( int i=0; i<size; i++ ){
			part = Function.StrSplitToken(lines[idx+i]," ");
			length[i] = Function.getValueOfInt(part[4]);
			attr[i] = part[0];
			timestamp[i] = part[5] + " " + part[6] + " " + part[7]; 
			name[i] = part[8];
		}
    }
	
	/**
	 * ファイル数を戻す。
	 */
    public int getSize() {
		return size;
	}

	/**
	 * ファイル名を戻す。
	 * 
	 * @param index
	 * 			インデックス
	 */
	public String getName(int index) {
		return name[index];
	}
	
	/**
	 * ファイルサイズを戻す。
	 * 
	 * @param index
	 * 			インデックス
	 */
	public int getLength(int index) {
		return length[index];
	}
	
	/**
	 * ファイルの属性を戻す。
	 * 
	 * @param index
	 * 			インデックス
	 */
	public String getAttr(int index) {
		return attr[index];
	}
	
	/**
	 * ファイルのタイムスタンプを戻す。
	 * 
	 * @param index
	 * 			インデックス
	 */
	public String getTimestamp(int index) {
		return timestamp[index];
	}
}