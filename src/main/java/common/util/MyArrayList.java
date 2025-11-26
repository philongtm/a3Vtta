/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.util;

import java.util.ArrayList;

/**
 * ArrayListクラス（機能追加版）
 * 
 */
public class MyArrayList extends ArrayList {

	public String[] toStrArray() {
		int listSize = size();
		String[] returnValue = new String[size()];
		for (int i = 0; i < listSize; i++) {
			returnValue[i] = (String)get(i);
		}
		return returnValue;
	}
}