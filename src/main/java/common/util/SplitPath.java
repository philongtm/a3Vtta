/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
003		2014/03/17		SSC				案件No.D13493 改善対応
******************************************************************************/
package common.util;

import java.io.File;

/**
 * ファイルパスの分割を行うクラス
 * 
 */
public class SplitPath {

    private String path = "";
    private String dir = "";
    private String fname = "";
    private String ext = "";

    /**
     * コンストラクタ
     */
    public SplitPath() {
    }
    
    /**
     * コンストラクタ
     * 
     * @param path ファイルパス
     */
    public SplitPath(String path) {
        this.path = path;
        splitPath();
     }

    /**
     * ディレクトリの取得を行う。
     * <pre>
     * パスが"/aaa/bbb/ccc/ddd.eee"の場合
     * "/aaa/bbb/ccc/"が取得される。
     * </pre>
     * @return ディレクトリ
     */
    public String getDir() {
        return dir;
    }

    /**
     * ファイル拡張子の取得を行う。
     * <pre>
     * パスが"/aaa/bbb/ccc/ddd.eee"の場合
     * "eee"が取得される。
     * </pre>
     * @return ディレクトリ
     */
    public String getExt() {
        return ext;
    }

    /**
     * ファイル名の取得を行う。
     * <pre>
     * パスが"/aaa/bbb/ccc/ddd.eee"の場合
     * "ddd"が取得される。
     * </pre>
     * @return ファイル名
     */
    public String getFname() {
        return fname;
    }

    /**
     * ファイルパスの取得を行う。
     * <pre>
     * パスが"/aaa/bbb/ccc/ddd.eee"の場合
     * "/aaa/bbb/ccc/ddd.eee"が取得される。
     * </pre>
     * @return ファイルパス
     */
    public String getPath() {
        return path;
    }

    /**
     * ファイルパスの設定を行う。
     * 
     * @param path ファイルパス
     */
    public void setPath(String path) {
		this.path = path;
		splitPath();
    }
    
    /**
     * パスの分割を行う
     */
	private void splitPath() {
		String[] part;

		part = Function.StrSplitToken( path, File.separator );

		dir = "";
	
		if( part.length > 0 ) {
			// ディレクトリ
			if( path.charAt(0) ==  File.separatorChar ) {
				dir = File.separator;
			}
			for( int i=0; i<(part.length-1); i++ ) {
				dir += part[i] + File.separator;
			}
		}
		
		// パスなしファイル名
		fname = part[part.length-1];
		
		// 拡張子
		part = Function.StrSplitToken( fname, "." );
		ext = part[part.length-1];
		
		// 拡張子なしファイル名
		fname = fname.substring(0,fname.length()-ext.length()-1);

		part = null;
	}
}