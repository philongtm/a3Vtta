/******************************************************************************
 著作権情報				:
 使用JDK バージョン		: 1.4.2.05
 更新履歴
 No		日付			修正者			修正内容
 ******************************************************************************/
package common.util;

/**
 * 共通関数クラス <br>
 * <br>
 * 汎用数値クラス
 * 
 */
public class TNumber {

	private Double dNumber;

	/**
	 * コンストラクタ
	 */
	public TNumber() {
		this.dNumber = null;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param value
	 */
	public TNumber(Double value) {
		this.dNumber = value;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param value
	 */
	public TNumber(int value) {
		Integer temp = new Integer(value);
		this.dNumber = new Double(temp.doubleValue());
	}

	/**
	 * コンストラクタ
	 * 
	 * @param value
	 */
	public TNumber(long value) {
		Long temp = new Long(value);
		this.dNumber = new Double(temp.doubleValue());
	}

	/**
	 * コンストラクタ
	 * 
	 * @param value
	 */
	public TNumber(float value) {
		Float temp = new Float(value);
		this.dNumber = new Double(temp.doubleValue());
	}

	/**
	 * コンストラクタ
	 * 
	 * @param value
	 */
	public TNumber(double value) {
		this.dNumber = new Double(value);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param value
	 */
	public TNumber(String value) {
		if (value == null) {
			this.dNumber = null;
		} else if (Function.isDouble(value)) {
			this.dNumber = new Double(value);
		} else {
			this.dNumber = null;
		}
	}

	/**
	 * 初期化
	 *  
	 */
	private void initialize() {
		this.dNumber = null;
	}

	/**
	 * セット
	 * 
	 * @param value
	 */
	public void setTNumber(Double value) {
		initialize();
		if (value.isNaN()) {
			this.dNumber = new Double(null);
		} else {
			this.dNumber = new Double(value.doubleValue());
		}
	}

	/**
	 * セット
	 * 
	 * @param value
	 */
	public void setTNumber(int value) {
		initialize();
		Integer temp = new Integer(value);
		this.dNumber = new Double(temp.doubleValue());
	}

	/**
	 * セット
	 * 
	 * @param value
	 */
	public void setTNumber(long value) {
		initialize();
		Long temp = new Long(value);
		this.dNumber = new Double(temp.doubleValue());
	}

	/**
	 * セット
	 * 
	 * @param value
	 */
	public void setTNumber(double value) {
		initialize();
		this.dNumber = new Double(value);
	}

	/**
	 * セット
	 * 
	 * @param value
	 */
	public void setTNumber(float value) {
		initialize();
		Float temp = new Float(value);
		this.dNumber = new Double(temp.doubleValue());
	}

	/**
	 * セット
	 * 
	 * @param value
	 */
	public void setTNumber(String value) {
		initialize();
		if (Function.isDouble(value) == false) {
			this.dNumber = null;
		} else {
			this.dNumber = new Double(Function.getValueOfDouble(value));
		}
	}

	/**
	 * ゲット
	 * 
	 * @return
	 */
	public Double getDouTNumber() {
		return new Double(this.dNumber.doubleValue());
	}

	/**
	 * ゲット
	 * 
	 * @return
	 */
	public int getIntTNumber() {
		return this.dNumber.intValue();
	}

	/**
	 * ゲット
	 * 
	 * @return
	 */
	public long getLngTNumber() {
		return this.dNumber.longValue();
	}

	/**
	 * ゲット
	 * 
	 * @return
	 */
	public Float getFloatTNumber() {
		return new Float(this.dNumber.doubleValue());
	}

	/**
	 * ゲット
	 * 
	 * @return
	 */
	public double getdouTNumber() {
		return this.dNumber.doubleValue();
	}

	/**
	 * ゲット
	 * 
	 * @return
	 */
	public String getTNumber() {
		return this.dNumber.toString();
	}

	/**
	 * ステータス
	 * 
	 * @return
	 */
	public boolean isNull() {
		if (this.dNumber == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * TNumberクラスの文字列表現
	 */
	public String toString() {
		if (this.dNumber == null) {
			return "";
		} else {
			return this.dNumber.toString();
		}
	}
	
	/**
	 * 小数を含む表示
	 * 
	 * @return 小数を含む値
	 */
	public String displayDecimal() {
		return toString();
	}

	/**
	 * 整数のみ表示
	 * 
	 * @return 整数のみの値
	 */
	public String displayInteger() {
		if (this.dNumber == null) {
			return "";
		} else {
			return Long.toString(getLngTNumber());
		}
	}

	/**
	 * Nullセット
	 *
	 */
	public void setNull() {
		this.dNumber = null;
	}
}