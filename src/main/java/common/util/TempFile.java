/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.util;

import common.AppContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一時ファイル作成クラス
 * 
 */
public class TempFile {

	private static final SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyyMMdd");
	private static final long DAY_BY_MILLISECOND = 86400000l;	// 1日のミリ秒
	private String displayFilename = null;
    private File tmpFile = null;
    private String serverFilename = null;	// ファイルサーバ上のファイル名
    
    /**
     * コンストラクタ
	 * <pre>
	 * 新しい空のファイルを生成する。
	 * ファイル名は重複のない一意な名称、"kaku.xxxxx.tmp"となる。
	 * ディレクトリは、webサーバのワークディレクトリが使用される。
	 * ファイルは、削除するまでワークディレクトリに残る。
	 * 使用ディレクトリ：/WEB-INF/tmp
	 * ファイル名：YYYYMMDD.xxxx.tmp
	 * </pre>
     * @param displayFilename 表示用ファイル名
     */
    public TempFile(String displayFilename) throws IOException {
    	this.displayFilename = displayFilename;
    	String suffix = sdfYMD.format(new Date());
    	File dir = new File(AppContext.getTmpDir());
    	if( dir.isDirectory()==false ) {
    		dir.mkdir();
    	}
        tmpFile = File.createTempFile(suffix+".",".tmp",dir);
        dir = null;
        //tmpFile.deleteOnExit();
    }

    /**
     * 一時ファイルの複製を作成する。
     * @return 一時ファイル
     */
    public TempFile getClone() throws Exception {
    	TempFile tmp = new TempFile(getDisplayFilename());
    	tmp.read(getPath());
		return tmp;
    }
   
    /**
     * 一時ファイルの絶対パスを取得する。
     * 
     * @return 絶対パス
     */
    public String getPath() {
    	return tmpFile.getAbsolutePath();
    }
    
    /**
     * 一時ファイルのサイズを取得する。
     * 
     * @return ファイルサイズ
     */
    public int getLength() {
    	return (int)tmpFile.length();
    }
	
   /**
    * 一時ファイルを削除する。
    */
    public void delete() {
		tmpFile.delete();
    }

    /**
     * 表示用ファイル名を取得する。
     * 
     * @return 表示用ファイル名
     */
	public String getDisplayFilename() {
		return displayFilename;
	}
	
    /**
     * 表示用ファイル名を設定する。
     * 
	 * @param displayFilename 表示用ファイル名
	 */
	public void setDisplayFilename(String displayFilename) {
		this.displayFilename = displayFilename;
	}

	public String getServerFilename() {
		return serverFilename;
	}
	public void setServerFilename(String serverFilename) {
		this.serverFilename = serverFilename;
	}
	
    /**
     * 他のファイルのコピーを行い、一時ファイルに保存する。
     * 
	 * @param path ファイルパス
	 */
	public void read(String path) throws Exception {
		File file = null; 
		FileInputStream is = null;
		OutputStream os = null;

        byte[] buf =  new byte[1024];
		int length;

        try {
           	file = new File(path);
			is = new FileInputStream(file);
        	os = new FileOutputStream(tmpFile.getPath());
			while ( (length = is.read(buf)) != -1 ) {
				os.write(buf, 0, length);
			}
			
        } finally {
        	file = null;
        	buf = null;
        	if(is!=null) {
        		is.close();
        		is = null;
        	}
        	if(os!=null) {
        		os.close();
        		os = null;
        	}
        }	
	}

	/**
	 * 一時ファイルの削除を行う。
	 * <pre>
	 * 削除されずに残っている一時ファイルの削除が目的。
	 * タイムスタンプが基準日付以前のものを削除対象とする。
	 * </pre>
	 * @param baseJDate 基準日付
	 */
    public synchronized static void cleanup(JCalendar baseJDate) throws IOException {
    	SplitPath sp = new SplitPath();

		Date baseDate = baseJDate.getDate();
		long longBaseDate = baseDate.getTime() / DAY_BY_MILLISECOND;
		//System.out.println("#1#"+longBaseDate);
		
			// 一時ファイルのディレクトリを取得
			TempFile tmp = new TempFile("");
			sp.setPath(tmp.getPath());
			tmp.delete();
	    	String tmpDir = sp.getDir();
	    	
	    	// 一時ファイルの一覧を作成
			File dir = new File(tmpDir);
			String[] dirList = dir.list();

			// 基準日以前のファイルを削除
			for (int i = 0; i < dirList.length; i++) {
				//if( dirList[i].startsWith(SUFFIX) ) {
					File wkFile = new File(tmpDir + File.separator + dirList[i]);
					long lastAccsDate = wkFile.lastModified() / DAY_BY_MILLISECOND;
					//System.out.println("#2#"+lastAccsDate);
					if( longBaseDate > lastAccsDate ) {
						// 2005/12/28 ---------------------- start
//						System.out.println("一時ファイル削除："+dirList[i]);
						// 2005/12/28 ---------------------- end
						wkFile.delete();
					}
					wkFile = null;
				//}
			}
    }   
}