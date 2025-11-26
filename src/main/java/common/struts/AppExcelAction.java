/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.struts;

import common.AppContext;
import common.global.GS;
import common.util.Log;
import common.util.TempFile;

import java.io.File;
import java.util.HashMap;

/**
 * 拡張アクションクラス
 * 
 */
public class AppExcelAction extends AppAction {

	private String CLASSNAME = getClass().getName();

	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
		return null;
	}

	/**
	 * デフォルトのアクション
	 */
	public Object appExecute(AppContext appContext) throws Exception {
		Log log = appContext.getLog();
		String name = appContext.getRequest().getParameter(GS.NAME);

		log.write(GS.LOG_INF,CLASSNAME,name);

		// マージされたEXCELをダウンロード
		String path = appContext.getRealPath(GS.EXCELDIR+name);
		TempFile tmp = new TempFile(name);
		tmp.read(path);
		File wkFile = new File(path);
		wkFile.delete();
		return tmp;			
	}
}