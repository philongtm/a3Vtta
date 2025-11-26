
/*
 * 作成日: 2005/10/20
 *
 * TODO この生成されたファイルのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
 */
package common.db;

/**
 *
 * TODO この生成された型コメントのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
 */
public class ParamMapBean {

	
	private int index			= -1;
	private int type			= -1;
	private String paramName	= null;
	private Object paramValue	= null;
	private boolean inFlg		= false;
	private boolean outFlg	= false;
	
	/**
	 * @return index を戻します。
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index index を設定。
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return paramName を戻します。
	 */
	public String getParamName() {
		return paramName;
	}
	/**
	 * @param paramName paramName を設定。
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	/**
	 * @return paramValue を戻します。
	 */
	public Object getParamValue() {
		return paramValue;
	}
	/**
	 * @param paramValue paramValue を設定。
	 */
	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}
	/**
	 * @return type を戻します。
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type type を設定。
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return inFlg を戻します。
	 */
	public boolean isInFlg() {
		return inFlg;
	}
	/**
	 * @param inFlg inFlg を設定。
	 */
	public void setInFlg(boolean inFlg) {
		this.inFlg = inFlg;
	}
	/**
	 * @return outFlg を戻します。
	 */
	public boolean isOutFlg() {
		return outFlg;
	}
	/**
	 * @param outFlg outFlg を設定。
	 */
	public void setOutFlg(boolean outFlg) {
		this.outFlg = outFlg;
	}
}
