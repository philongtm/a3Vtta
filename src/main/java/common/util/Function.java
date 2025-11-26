/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2008/01/09		SSC				1.5次版に修正を施し流用
002		2008/03/03		SSC				新規メソッド追加
003		2008/05/13		SSC				新規メソッド追加
004		2009/12/1		SSC				課題No.158 年月表示(英語版)を(MM/YYYY)に対応
******************************************************************************/
package common.util;

import common.global.GS;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 汎用共通関数<br>
 * 全てstaticメソッドのため、インスタン作成不要。
 * 
 */
public class Function {

	private static final char[] SJIS={
			0x00a5, // ＼
			0x2014, // ―
			0x301c, // ～
			0x2016, // ∥
			0x2212, // －
			0x00a2, // ￠
			0x00a3, // ￡
			0x00ac, // ￢
	};

	private static final char[] MS932={
			0x005c,
			0x2015,
			0xff5e,
			0x2225,
			0xff0d,
			0xffe0,
			0xffe1,
			0xffe2,
	};
	
	private static final String PATTERN = "\\{[0-9]\\}";

	/**
	 * 文字列をトークンに分割します。
	 * 
	 * @param expression 解析される文字列
	 * @param delimiter 区切り文字(列)。
	 * <pre>
	 * １文字が区切り文字となります。
	 * nullの場合はデフォルトの区切り文字を使用します。
	 * ----> " \t\n\r\f" (空白文字、タブ文字、改行文字、復帰改行文字、および用紙送り文字)
	 * </pre>
	 * @return 分割された文字列の１次元配列
	 */
	public static String[] StrSplitToken(String expression, String delimiter) {
	    String[] token = null;
		StringTokenizer st;
		if( delimiter == null ) {
			st = new StringTokenizer(expression);
		} else {
			st = new StringTokenizer(expression, delimiter);
		}
		int n;
		if( (n=st.countTokens()) > 0 ) {
		    token = new String[n];
		    for( int i=0; i<n; i++ ) {
				token[i] = st.nextToken();
		    }
		}
		return token;
	}

	/**
	 * 文字列の置換を行います。
	 * 
	 * @param expression	文字列
	 * @param find			検索文字列
	 * @param replacewith	置換文字列
	 * <pre>
	 * ・「検索文字列」を「置換文字列」で置換します。
	 * ・正規化表現は不要です。
	 * </pre>
	 * @return 置換後の文字列
	 */
	public static String StrReplace(String expression, String find, String replacewith) {
		int index;
		String rv = "";
		String s = expression;
		boolean replace = false;
		while( (index=s.indexOf(find)) >= 0 ) {
			replace = true;
			if( index > 0 ) {
				rv += s.substring(0,index);
			}
			rv += replacewith;
			s = s.substring(index+find.length());
		}
		if( replace ) {
			return rv + s;
		} else {
			return expression;
		}
	}
	
	/**
	 * 文字列の前後の半角/全角スペースを削除する。<br>
	 * <br>
	 * nullの場合は空文字""を返す。
	 * 
	 * @param string 文字列
	 * @return 半角/全角スペース削除後の文字列
	 */
	public static String trim(String string) {

		if( string == null ) return "";

		// 前後の半角スペースを削除
		int length;
		String trimString = string.trim();
		if( (length = trimString.length()) == 0 ) return "";

		// 前後の全角スペースを削除
		int top, tail;

		for( top=0; top<length; top++ ) {
			if( trimString.charAt(top) != '　' ) break;
		}

		if( !(top<length) ) return "";

		for( tail=length-1; tail>=0; tail-- ) {
			if( trimString.charAt(tail) != '　' ) break;
		}

		if( (top!=0) || (tail!=(length-1)) ) {
			trimString = trimString.substring(top,tail+1);
		}

		return trimString;
	}
	
	/**
	 * 文字列の切り出しを行う<br>
	 * <br>
	 * nullの場合は空文字""を返す。
	 * 
	 * @param str 文字列
	 * @param len 文字数
	 * @return 切り出し後の文字列
	 */
	public static String left(String str, int len) {
		if( (str==null) || (len<=0) ) return "";
		if( str.length() <= len ) return str;
		return str.substring(0,len);
	}
	
	/**
	 * 数値チェック<br>
	 * <br>
	 * 文字列がintに変換可能かチェックを行う。<br>
	 * 文字列の前後の半角/全角スペースは無視する。<br>
	 * intの有効範囲：-2,147,483,648～2,147,483,647
	 * 
	 * @param value 文字列
	 * @return true/正常 false/エラー
	 */
	public static boolean isInt(String value) {
		if( value == null ) return false;
		try {
			int n = new Integer(trim(value)).intValue();
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * 数値チェック<br>
	 * <br>
	 * 文字列がlongに変換可能かチェックを行う。<br>
	 * 文字列の前後の半角/全角スペースは無視する。<br>
	 * longの有効範囲：-9,223,372,036,854,775,808～9,223,372,036,854,775,807
	 * 
	 * @param value 文字列
	 * @return true/正常 false/エラー
	 */
	public static boolean isLong(String value) {
		if( value == null ) return false;
		try {
			long n = new Long(trim(value)).longValue();
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 数値チェック<br>
	 * <br>
	 * 文字列がfloatに変換可能かチェックを行う。<br>
	 * 文字列の前後の半角/全角スペースは無視する。<br>
	 * floatの有効範囲：-3.40282347E+38～+3.40282347E+38
	 * 
	 * @param value 文字列
	 * @return true/正常 false/エラー
	 */
	public static boolean isFloat(String value) {
		if( value == null ) return false;
		try {
			float n = new Float(trim(value)).floatValue();
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * 数値チェック<br>
	 * <br>
	 * 文字列がdoubleに変換可能かチェックを行う。<br>
	 * 文字列の前後の半角/全角スペースは無視する。<br>
	 * doubleの有効範囲：-1.79769313486231570E+308～+1.79769313486231570E+308
	 * 
	 * @param value 文字列
	 * @return true/正常 false/エラー
	 */
	public static boolean isDouble(String value) {
		if( value == null ) return false;
		try {
			double n = new Double(trim(value)).doubleValue();
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * 文字列をintに変換する。<br>
	 * <br>
	 * 文字列の前後の半角/全角スペースは無視する。<br>
	 * 変換できない場合は0を返す。
	 * 
	 * @param value 文字列
	 * @return int
	 */
	public static int getValueOfInt(String value) {
		if( value == null ) return 0;
		try {
			return new Integer(trim(value)).intValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 文字列をintに変換する。<br>
	 * カンマは削除する。
	 * <br>
	 * 文字列の前後の半角/全角スペースは無視する。<br>
	 * 変換できない場合は0を返す。
	 * 
	 * @param value 文字列
	 * @return int
	 */
	public static int getValueOfIntC(String value) {
		if( value == null ) return 0;
		try {
			return new Integer(trim(value.replaceAll(",",""))).intValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 文字列をlongに変換する。<br>
	 * カンマは削除する。
	 * <br>
	 * 文字列の前後の半角/全角スペースは無視する。<br>
	 * 変換できない場合は0を返す。
	 * 
	 * @param value 文字列
	 * @return long
	 */
	public static long getValueOfLongC(String value) {
		if( value == null ) return 0;
		try {
			return new Long(trim(value.replaceAll(",",""))).longValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * 文字列をlongに変換する。<br>
	 * <br>
	 * 文字列の前後の半角/全角スペースは無視する。<br>
	 * 変換できない場合は0を返す。
	 * 
	 * @param value 文字列
	 * @return long
	 */
	public static long getValueOfLong(String value) {
		if( value == null ) return 0;
		try {
			return new Long(trim(value)).longValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * 文字列をfloatに変換する。<br>
	 * <br>
	 * 文字列の前後の半角/全角スペースは無視する。<br>
	 * 変換できない場合は0.0を返す。
	 * 
	 * @param value 文字列
	 * @return float
	 */
	public static float getValueOfFloat(String value) {
		if( value == null ) return 0;
		try {
			return new Float(trim(value)).floatValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * 文字列をfloatに変換する。<br>
	 * カンマは削除する。
	 * <br>
	 * 文字列の前後の半角/全角スペースは無視する。<br>
	 * 変換できない場合は0を返す。
	 * 
	 * @param value 文字列
	 * @return float
	 */
	public static float getValueOfFloatC(String value) {
		if( value == null ) return 0;
		try {
			return new Float(trim(value.replaceAll(",",""))).floatValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 文字列をdoubleに変換する。<br>
	 * <br>
	 * 文字列の前後の半角/全角スペースは無視する。<br>
	 * 変換できない場合は0.0を返す。
	 * 
	 * @param value 文字列
	 * @return double
	 */
	public static double getValueOfDouble(String value) {
		if( value == null ) return 0;
		try {
			return new Double(trim(value)).doubleValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * 文字列をdoubleに変換する。<br>
	 * カンマは削除する。
	 * <br>
	 * 文字列の前後の半角/全角スペースは無視する。<br>
	 * 変換できない場合は0を返す。
	 * 
	 * @param value 文字列
	 * @return double
	 */
	public static double getValueOfDoubleC(String value) {
		if( value == null ) return 0;
		try {
			return new Double(trim(value.replaceAll(",",""))).doubleValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * intを文字列に変換する。<br>
	 * <br>
	 * 文字列の前後の半角/全角スペースは無視する。<br>
	 * 変換できない場合は0.0を返す。
	 * 
	 * @param int
	 * @return String
	 */
	public static String getStringOfInt(int value) {
		return new Integer(value).toString();
	}
	
	/**
	 * 日付(西暦)の存在チェック。<br>
	 * 
	 * @param year 年(YYYY)
	 * @param month	 月(MM)
	 * @param day 日(DD)
	 * @return true/正常 false/エラー
	 */
	public static boolean isDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
	    //日付、時刻のチェックを厳密に行うように設定する
	    cal.setLenient(false);
	    cal.set(year, month-1, day);
	    try {
	        Date date = cal.getTime();
	    } catch(IllegalArgumentException e) {
	        return false;
	    } 
	    return true;                                    
	}

	/**
	 * 日付(西暦)の存在チェック。<br>
	 * 
	 * @param year 年(YYYY)
	 * @param month	 月(MM)
	 * @param day 日(DD)
	 * @return true/正常 false/エラー
	 */
	public static boolean isDate(String year, String month, String day) {
	    return isDate(getValueOfInt(year), getValueOfInt(month), getValueOfInt(day));
	}

	/**
	 * 数値の書式化<br>
	 * <br>
	 * (書式の形式)<br>
	 * パターンの記号<br>
	 * 0 ･･･任意の数字を表す。小数桁数を設定する場合、桁数分の 0 を付ける<br>
	 * # ･･･任意の数字を表し、先頭部分がゼロだと表示されない<br>
	 * .(ﾋﾟﾘｵﾄﾞ) ･･･数値桁区切り子<br>
	 * ,(ｶﾝﾏ) ･･･グループ区切り子<br>
	 * (例)<br>
	 * "###,###,###,##0"："12,345"<br>
	 * "000,000,000,000"："000,000,012,345"<br>
	 * "0.000"："12345.000"<br>
	 * 
	 * @param format 書式
	 * @param value	 数値
	 * @return 書式化された文字列
	 */
	public static String format(String format, int value) {
		 DecimalFormat df = new DecimalFormat(format);
		 return df.format(value);
	}

	/**
	 * 数値の書式化<br>
	 * <br>
	 * (書式の形式)<br>
	 * パターンの記号<br>
	 * 0 ･･･任意の数字を表す。小数桁数を設定する場合、桁数分の 0 を付ける<br>
	 * # ･･･任意の数字を表し、先頭部分がゼロだと表示されない<br>
	 * .(ﾋﾟﾘｵﾄﾞ) ･･･数値桁区切り子<br>
	 * ,(ｶﾝﾏ) ･･･グループ区切り子<br>
	 * (例)<br>
	 * "###,###,###,##0"："12,345"<br>
	 * "000,000,000,000"："000,000,012,345"<br>
	 * "0.000"："12345.000"<br>
	 * 
	 * @param format 書式
	 * @param value	 数値
	 * @return 書式化された文字列
	 */
	public static String format(String format, long value) {
		 DecimalFormat df = new DecimalFormat(format);
		 return df.format(value);
	}
	
	/**
	 * 数値の書式化<br>
	 * <br>
	 * (書式の形式)<br>
	 * パターンの記号<br>
	 * 0 ･･･任意の数字を表す。小数桁数を設定する場合、桁数分の 0 を付ける<br>
	 * # ･･･任意の数字を表し、先頭部分がゼロだと表示されない<br>
	 * .(ﾋﾟﾘｵﾄﾞ) ･･･数値桁区切り子<br>
	 * ,(ｶﾝﾏ) ･･･グループ区切り子<br>
	 * (例)<br>
	 * "###,###,###,##0"："12,345"<br>
	 * "000,000,000,000"："000,000,012,345"<br>
	 * "0.000"："12345.678"<br>
	 * 
	 * @param format 書式
	 * @param value	 数値
	 * @return 書式化された文字列
	 */
	public static String format(String format, float value) {
		 DecimalFormat df = new DecimalFormat(format);
		 return df.format(value);
	}
	
	/**
	 * 数値の書式化<br>
	 * <br>
	 * (書式の形式)<br>
	 * パターンの記号<br>
	 * 0 ･･･任意の数字を表す。小数桁数を設定する場合、桁数分の 0 を付ける<br>
	 * # ･･･任意の数字を表し、先頭部分がゼロだと表示されない<br>
	 * .(ﾋﾟﾘｵﾄﾞ) ･･･数値桁区切り子<br>
	 * ,(ｶﾝﾏ) ･･･グループ区切り子<br>
	 * (例)<br>
	 * "###,###,###,##0"："12,345"<br>
	 * "000,000,000,000"："000,000,012,345"<br>
	 * "0.000"："12345.678"<br>
	 * 
	 * @param format 書式
	 * @param value	 数値
	 * @return 書式化された文字列
	 */
	public static String format(String format, double value) {
		 DecimalFormat df = new DecimalFormat(format);
		 return df.format(value);
	}
	
	public static double marume(double value) {
		double rs = 0;
		if(value >= 0){
			rs = value + 0.5;
			rs = Math.floor(rs);
		}else{
			rs = value - 0.5;
			rs = Math.ceil(rs);
		}
		return rs;
	}
	
	/**
	 * 文字列の比較<br>
	 * nullも比較可能。<br>
	 * 文字列がnullの場合は、比較結果は常にfalseとなる。
	 * 
	 * @param str1 文字列１
	 * @param str2 文字列２
	 * @return true/同じ false/異なる
	 */
	public static boolean strEquals(String str1, String str2) {
		if( (str1==null) || (str2==null) ) {
			return false;
		} else {
			return str1.equals(str2);
		}
	}

	/**
	 * SJIS→MS932<br>
	 * SJISのWAVE DASH「～(0x301c)」をMS932のFULLWIDTH TILDE「～(0xff5e)」<br>
	 * 変換します。
	 * 
	 * @param strSJis 「～」を含む文字列
	 * @return 変換後の文字列
	 */
	public static String toMs932(String strSJis) {  

		if (strSJis == null) {
			return strSJis;
		}
		
		StringBuffer buf = new StringBuffer();
		char ch = 0x0000;
	    
		for (int i = 0; i < strSJis.length(); i++) {
			ch = strSJis.charAt(i);
			if (ch == 0x301c) {
				ch = 0xff5e;
			} else if (ch == 0x2212) {
				ch = 0xFF0D;
			}
			
			buf.append(ch);
		}
		return buf.toString();
	}
	
	/**
	 * ハイフン削除<br>
	 * 文字列から半角ハイフンを削除します。文字列がnullの場合はnullのまま返します。
	 * @param target 「-」半角ハイフンを含む文字列
	 * @return 「-」半角ハイフンをはずした文字列
	 */
	public static String deleteHyphen(String target) {
		if (target == null) {
			return target;
		}
		return target.replaceAll("-","");
	}
	
	/**
	 * 半角シングルクォーテーションを全角に変換する
	 * @param target
	 * @return
	 */
	public static String replaceSingleQuotation(String target){
		if(target == null){
			return target;
		}
		return target.replaceAll("'","’");
	}
	
	/**
	 * ～∥－￠￡￢のSJIS→MS932変換
	 * @param target 変換を行う文字列
	 * @return SJIS→MS932の変換を行った文字列
	 */
	public static String convert(String target) {
		if (target == null) {
			return null;
		}
		
		char[] buf = target.toCharArray();
		for (int i = 0; i < buf.length; i++) {
			for (int j = 0; j < SJIS.length; j++) {
				if (buf[i] == SJIS[j]) {
					buf[i] = MS932[j];
					break;
				}
			}
		}
		return new String(buf);
	}

	/**
	 * 文字列をバイト数単位で指定した長さで改行。
	 * @param target 文字列
	 * @param length 指定する長さ
	 * @return 指定する長さで改行された文字列
	 */
	public static String stringCutterByteAddN(String target, int length) {

		// 引数チェック 文字列がnull
		if (target == null) {
			return target;
		}

		StringBuffer comment = new StringBuffer();

		int targetBLen = 0;
		
		// バイト数取得
		try {
			targetBLen = target.getBytes("EUC-JP").length;
		} catch (UnsupportedEncodingException e){
			targetBLen = target.getBytes().length;
		}
			
		// 引数チェック 文字列がnull、指定する長さが0未満、文字列より長い場合処理を行わない
		if (target == null || length < 0 || length > targetBLen) {
			return target;
		}
		
		String tmp = new String();
		while(target != null) {
		    comment.append(Function.stringCutterUseByte(target, length)).append("\n");
		    
		    // 開始位置を取得するため、一時的に保存
		    tmp = Function.stringCutterUseByte(target, length);
		    target = target.substring(tmp.length(), target.length());
		    
		    try {
		    	targetBLen = target.getBytes("EUC-JP").length;
		    } catch (UnsupportedEncodingException e) {
		    	targetBLen = target.getBytes().length;
		    }
		    if(targetBLen <= length) {
		        comment.append(target);
		        target = null;
		    }
		}
		
		return comment.toString();
	}

//	/**
//	 * 文字列をバイト数単位で指定した長さで改行。
//	 * @param target 文字列
//	 * @param length 指定する長さ
//	 * @return 指定する長さで改行された文字列
//	 */
//	public static String stringCutterByteAddN(String target, int length) {
//		// 引数チェック 文字列がnull、指定する長さが0未満、文字列より長い場合処理を行わない
//		if (target == null || length < 0 || length > target.length()) {
//			return target;
//		}
//		
//		StringBuffer comment = new StringBuffer();
//		while(target != null) {
//		    comment.append(Function.stringCutterUseByte(target, length)).append("\n");
//		    target = target.substring(length, target.length());
//		    if(target.length() <= length) {
//		        comment.append(target);
//		        target = null;
//		    }
//		}
//		return comment.toString();
//	}
	
	/**
	 * 全角・半角の区別をせずに指定した文字列の長さでカットする。
	 * @param target 文字列
	 * @param length 指定する長さ
	 * @return 指定する長さでカットされた文字列
	 */
	public static String stringCutter(String target, int length) {
		// 引数チェック 文字列がnull、指定する長さが0未満、文字列より長い場合処理を行わない
		if (target == null || length < 0 || length > target.length()) {
			return target;
		}
		return target.substring(0,length);
	}

	/**
	 * バイト数単位で指定した文字列の長さでカットする。
	 * 指定した長さが全角文字の途中となる場合は、その文字は含まない。
	 * @param target 文字列
	 * @param length 指定する長さ（バイト数単位）
	 * @return 指定する長さでカットされた文字列
	 */
	public static String stringCutterUseByte(String target, int length) {
		// 引数チェック 文字列がnull、指定する長さが0未満、文字列より長い場合処理を行わない
		if (target == null) {
			return target;
		}
		byte[] targetArray;
		try {
			targetArray = target.getBytes("EUC-JP");
		} catch (UnsupportedEncodingException e) {
			targetArray = target.getBytes();
		}
		if (length < 0 || length > targetArray.length) {
			return target;
		}
		char[] charArray = target.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < charArray.length; i++) {
			byte[] current;
			try {
				current = Character.toString(charArray[i]).getBytes("EUC-JP");
			} catch (UnsupportedEncodingException e) {
				current = Character.toString(charArray[i]).getBytes();
			}
			length -= current.length;
			if (length >= 0) {
				sb.append(charArray[i]);
			} else {
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 単位「年」の付加
	 * @param term
	 * @return 編集後の文字列
	 */
	public static String connectTerm(String term) {
		if (term == null) {
			return term;
		}
		StringBuffer termBuffer = new StringBuffer();
		termBuffer.append(term);
		termBuffer.append("年");
		return termBuffer.toString();
	}

	/**
	 * 単位「万円」の付加
	 * @param yen
	 * @return 編集後の文字列
	 */
	public static String connectTenThousandYen(String yen) {
		if (yen == null) {
			return yen;
		}
		StringBuffer yenBuffer = new StringBuffer();
		yenBuffer.append(yen);
		yenBuffer.append("万円");
		return yenBuffer.toString();
	}

	/**
	 * 単位「円」の付加
	 * @param yen
	 * @return 編集後の文字列
	 */
	public static String connectYen(String yen) {
		if (yen == null) {
			return yen;
		}
		StringBuffer yenBuffer = new StringBuffer();
		yenBuffer.append(yen);
		yenBuffer.append("円");
		return yenBuffer.toString();
	}

	/**
	 * 単位「％」の付加
	 * @param percent
	 * @return 編集後の文字列
	 */
	public static String connectPercent(String percent) {
		if (percent == null) {
			return percent;
		}
		StringBuffer percentBuffer = new StringBuffer();
		percentBuffer.append(percent);
		percentBuffer.append("％");
		return percentBuffer.toString();
	}
	
	public static String[] perMonthToPerYear(String perMonthValue) {
		String[] returnValue = {GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER};
		if (perMonthValue == null || perMonthValue.equals(GS.EMPTY_CHARCTER)) {
			return returnValue;
		}
		if (!isInt(perMonthValue)) {
			return returnValue;
		}
		int intValue = getValueOfInt(perMonthValue);
		returnValue[0] = Integer.toString(intValue / 12);
		returnValue[1] = Integer.toString(intValue % 12);
		return returnValue;
	}
	
	public static String perYearToPerMounth(String perYearValue, String perMonthValue) {
		String returnValue = null;
		if (perYearValue == null || perYearValue.equals(GS.EMPTY_CHARCTER)) {
			return returnValue;
		}
		if (perMonthValue == null || perMonthValue.equals(GS.EMPTY_CHARCTER)) {
			return returnValue;
		}
		if (!isInt(perYearValue) || !isInt(perMonthValue)) {
			return returnValue;
		}
		int intYearValue = getValueOfInt(perYearValue);
		int intMonthValue = getValueOfInt(perMonthValue);
		returnValue = Integer.toString(intYearValue * 12 + intMonthValue);
		return returnValue;
	}

	public static String[] dateStringCutter(String date) {
		String[] returnValue = {GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER};
		if (date == null || date.length() != 8) {
			return returnValue;
		}
		if (!isInt(date)) {
			return returnValue;
		}
		returnValue[0] = date.substring(0,4);
		returnValue[1] = date.substring(4,6);
		returnValue[2] = date.substring(6,8);
		return returnValue;
	}

	/**
	 * 小数点削除<br>
	 * 文字列から半角ハイフンを削除します。文字列がnullの場合はnullのまま返します。
	 * @param target 「-」半角ハイフンを含む文字列
	 * @return 「-」半角ハイフンをはずした文字列
	 */
	public static String deleteDecimalPoint(String target) {
		if (target == null) {
			return target;
		}
		boolean executeFlg = false;
		char[] charArray = target.toCharArray();
		StringBuffer sbReturn = new StringBuffer();
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] != '.' || executeFlg) {
				sbReturn.append(charArray[i]);
			} else {
				executeFlg = true;
			}
		}
		return sbReturn.toString();
	}
	
	/**
	 * 計算後合算年収の計算
	 * @param yearIncome 本人年収
	 * @param sumIncome 合算者年収
	 * @param sumFlg 収入合算区分
	 * @param rateSumIncome 収入合算比率
	 * @return 計算後合算年収
	 */
//	public static String calcSumIncome(String yearIncome, String sumIncome, String sumFlg, String rateSumIncome) {
//		if (!isLong(yearIncome) || !isLong(sumIncome) || !isInt(rateSumIncome)) {
//			return null;
//		}
//		if (sumFlg != null && sumFlg.equals(GS.EMPTY_CHARCTER)) {
//			return null;
//		}
//
//		// 計算用合算区分が「2:なし」の場合、本人年収を計算後合算年収とする
//		if (Function.strEquals(sumFlg, GS.KBN_SUMINCOME_NOTEXIST)) {
//			return yearIncome;
//		}
//
//		long longYearIncome = getValueOfLong(yearIncome);
//		long longSumIncome = getValueOfLong(sumIncome);
//		int intRateSumIncome = getValueOfInt(rateSumIncome) / 100;
//		
//		longSumIncome = longSumIncome * intRateSumIncome;
//		
//		// 合算者年収 × 収入合算比率 が本人年収より大きい場合、本人年収の値を上限とする
//		if (longYearIncome > longSumIncome) {
//			longSumIncome = longYearIncome;
//		}
//
//		// 合算した値を返す
//		return Long.toString(longYearIncome + longSumIncome);
//	}
//	
//	public static String convertLineFeed(String param) {
//		if (param == null) {
//			return null;
//		}
//		return param.replaceAll("\r\n", "<br>");
//	}
	
	/**
	 * HTMLの改行コード（<br>）を追加する
	 * @param param 改行コードを追加する文字列
	 * @param length 改行コードを追加する位置
	 * @return 追加位置に改行コードを設定した文字列
	 */
	public static String insertLineFeed(String param, int length) {
		if (param == null) {
			return null;
		}
		if (param.length() < length) {
			return param;
		}
		StringBuffer sbParam = new StringBuffer();
		sbParam.append(param.substring(0,length));
		sbParam.append("<br>");
		sbParam.append(param.substring(length));
		return sbParam.toString();
	}

//	/**
//	 * アクションフォーム（app.GyoumuForm）より申込人名を取り出す
//	 * @param form アクションフォーム
//	 * @return 申込人名
//	 */
//	public static String getNameReq(GyoumuForm form) {
//		if (form == null) {
//			return GS.EMPTY_CHARCTER;
//		} else if (form.getTNameReq() != null &&
//				!form.getTNameReq().equals(GS.EMPTY_CHARCTER) &&
//				!form.getTNameReq().equals("-")) {
//			return form.getTNameReq();
//		} else if (form.getTNameK() != null &&
//				!form.getTNameK().equals(GS.EMPTY_CHARCTER)) {
//			return form.getTNameK();
//		} else {
//			return GS.EMPTY_CHARCTER;
//		}
//	}

	/**
	 * カンマ付きの数値からカンマを外す。
	 * @param date スラッシュあり日付 
	 * @return スラッシュなし日付
	 */
	public static String removeComma(String value) {
		if (value == null || value.length() == 0) {
			return value;
		}
		StringBuffer returnValue = new StringBuffer();
		String[] valueArray = value.split(",");
		for(int i = 0; i < valueArray.length; i++) {
			returnValue.append(valueArray[i]);
		}
		return returnValue.toString();
	}
	
	/**
	 * シングルコート削除
	 * @param String  
	 * @return String
	 */
	public static String removeSingle(String value) {
		if (value == null || value.length() == 0) {
			return value;
		}
		StringBuffer returnValue = new StringBuffer();
		String[] valueArray = value.split("'");
		for(int i = 0; i < valueArray.length; i++) {
			returnValue.append(valueArray[i]);
		}
		return returnValue.toString();
	}

	/**
	 * スラッシュ付きの日付からスラッシュを外す。
	 * 月、日が一桁の場合は十の位に0を付加する。
	 * @param date スラッシュあり日付 
	 * @return スラッシュなし日付
	 */
	public static String removeDateSlash(String date) {
		if (date == null || date.length() == 0) {
			return date;
		}
		StringBuffer returnDate = new StringBuffer();
		String[] dateArray = date.split("/");
		for(int i = 0; i < dateArray.length; i++) {
			if (dateArray[i].length() == 1) {
				returnDate.append("0");
			}
			returnDate.append(dateArray[i]);
		}
		return returnDate.toString();
	}

	/**
	 * 6桁のスラッシュなし年月に「年」「月」を加える
	 * @param ym 6桁のスラッシュなし年月 
	 * @return 7桁の「年」「月」あり年月
	 */
	public static String insertYmNengetu(String ym) {
		if (ym == null || ym.length() != 6) {
			return ym;
		}
		StringBuffer sbYm = new StringBuffer();
		sbYm.append(ym.substring(0,4));
		sbYm.append("年");
		sbYm.append(ym.substring(4,6));
		sbYm.append("月");
		return sbYm.toString();
	}

	/**
	 * 6桁のスラッシュなし年月の前2桁を削除し、スラッシュを加える
	 * @param ym 6桁のスラッシュなし年月 
	 * @return 5桁のスラッシュあり年月
	 */
	public static String insertYmSlash_Cutter(String ym) {
		if (ym == null || ym.length() != 6) {
			return ym;
		}
		StringBuffer sbYm = new StringBuffer();
		sbYm.append(ym.substring(2,4));
		sbYm.append("/");
		sbYm.append(ym.substring(4,6));
		return sbYm.toString();
	}
	
	/**
	 * 6桁のスラッシュなし年月にスラッシュを加える
	 * @param ym 6桁のスラッシュなし年月 
	 * @return 7桁のスラッシュあり年月
	 */
	public static String insertYmSlash(String ym,String langmode) {
		if (ym == null || ym.length() != 6) {
			return ym;
		}
		
		if (langmode.equals(GS.LANG_JA)){
			StringBuffer sbYm = new StringBuffer();
			sbYm.append(ym.substring(0,4));
			sbYm.append("/");
			sbYm.append(ym.substring(4,6));
			return sbYm.toString();			
		}else{
			StringBuffer sbYm = new StringBuffer();
			sbYm.append(ym.substring(4,6));
			sbYm.append("/");
			sbYm.append(ym.substring(0,4));
			return sbYm.toString();			
		}
	}
	
	/**
	 * 6桁のスラッシュなし年月にスラッシュを加える
	 * @param ym 6桁のスラッシュなし年月 
	 * @return 7桁のスラッシュあり年月
	 */
	public static String insertYmSlash(String ym) {
		if (ym == null || ym.length() != 6) {
			return ym;
		}
		
		StringBuffer sbYm = new StringBuffer();
		sbYm.append(ym.substring(0,4));
		sbYm.append("/");
		sbYm.append(ym.substring(4,6));
		return sbYm.toString();			
	}	
	
	/**
	 * 8桁のスラッシュなし日付にスラッシュを加える
	 * @param date 8桁のスラッシュなし日付 
	 * @return 10桁のスラッシュあり日付
	 */
	public static String insertDateSlash(String date,String lang_mode) {
		if (date == null || date.length() != 8) {
			return date;
		}
		// 課題No.158
		// 追加開始
		/*
		StringBuffer sbDate = new StringBuffer();
		sbDate.append(date.substring(0,4));
		sbDate.append("/");
		sbDate.append(date.substring(4,6));
		sbDate.append("/");
		sbDate.append(date.substring(6,8));
		return sbDate.toString();
		*/
		
		if (lang_mode.equals(GS.LANG_JA)){
			StringBuffer sbYm = new StringBuffer();
			sbYm.append(date.substring(0,4));
			sbYm.append(GS.SLASH);
			sbYm.append(date.substring(4,6));
			sbYm.append(GS.SLASH);
			sbYm.append(date.substring(6,8));
			return sbYm.toString();			
		}else{
			StringBuffer sbYm = new StringBuffer();
			sbYm.append(date.substring(4,6));
			sbYm.append(GS.SLASH);
			sbYm.append(date.substring(6,8));
			sbYm.append(GS.SLASH);
			sbYm.append(date.substring(0,4));
			return sbYm.toString();			
		}
		// 追加完了
	}

	/**
	 * Date型変数にスラッシュを加えた日付を返す。
	 * @param date Date型日付 
	 * @return 10桁のスラッシュあり日付
	 */
	public static String insertDateSlash(Date date) {
		if(date == null){
			String dateToString = null;
			return dateToString;
		}
		String dateToString = date.toString();
		if (dateToString == null || dateToString.length() != 10) {
			return dateToString;
		}
		StringBuffer sbDate = new StringBuffer();
		sbDate.append(dateToString.substring(0,4));
		sbDate.append("/");
		sbDate.append(dateToString.substring(5,7));
		sbDate.append("/");
		sbDate.append(dateToString.substring(8,10));
		return sbDate.toString();
	}

	/**
	 * 6桁の年月に指定された月数を加減算した年月を取得する
	 * @param ym 6桁のスラッシュなし年月 
	 * @param val 加減算する月数 
	 * @return 6桁のスラッシュなし年月
	 */
	public static String calcYmNngetsu(String ym, int val) {
		if (ym == null || ym.length() != 6) {
			return ym;
		}
		int year = Integer.parseInt(ym.substring(0, 4));
		int month = Integer.parseInt(ym.substring(4, 6)) + val;
		while(true) {
			if (month > 12) {
				year += 1;
				month -= 12;
			} else if (month < 1) {
				year -= 1;
				month += 12;
			} else {
				break;
			}
		}
		return format("0000", year) + format("00", month);
	}

	/**
	 * 
	 * 数値の書式化(小数点第一位切り捨て)<br>
	 * <br>
	 * (書式の形式)<br>
	 * パターンの記号<br>
	 * 0 ･･･任意の数字を表す。小数桁数を設定する場合、桁数分の 0 を付ける<br>
	 * # ･･･任意の数字を表し、先頭部分がゼロだと表示されない<br>
	 * @return 書式化された文字列(小数点第一位切捨て)
	 */
//右の数値が16.9→16.8に変換してしまうためコメントアウトする　笠井　1114	
//	public static String formatCellOne5(String format,double nuｍ) {
       // 出力フォーマットを用意しておきます。
//	   DecimalFormat decimalFormat = new DecimalFormat(format);

//	   BigDecimal bigDecimal = new BigDecimal(nuｍ);

	   // 小数点以下1位の数に切り捨てします。
//       return decimalFormat.format( bigDecimal.setScale( 1, BigDecimal.ROUND_DOWN) );
//   }
	
	/**
	 * 
	 * 数値の書式化(小数点第一位切り捨て)<br>
	 * @param num	 double型：数値
	 * @return 小数点第一位切捨
	 */
	public static String formatCellOne(double nuｍ) {
		String answer = Double.toString(nuｍ);
		if(answer.indexOf(".")==-1){
			return answer;
		}
		int count=answer.indexOf(".");
		// 第一次画面評価、項番2
		// 50.0場合は、50に設定
//		if(answer.substring(0,count+2).endsWith("0")){
//			return answer.substring(0,count);
//		}
		// 小数点以下1位の数に切り捨てします。
		return answer.substring(0,count+2);
   }

	/**
	 * 数値の書式化(指定した数値で小数点切り捨て)<br>
	 * @param keta	 桁数指定 :数値
	 * @param num	 double型:数値
	 * @return 整数切り捨て、(小数点第1位)及び小数点第2位を切り捨て
	 */
	public static String formatCell(int keta, double nuｍ) {
		
		String answer = Double.toString(nuｍ);
		
		int count=answer.indexOf(".");
		if(count==-1){
			// 小数点が存在しない場合は、そのまま戻す
			return answer;
		}
		
		if (keta <= 0) {
			// 0以下の桁が指定されている場合は、小数点で切り捨てる
			return answer.substring(0,count);
		} else {
			count += (keta + 1); // 小数点があるので＋１する
			int test = answer.length();
			// 切捨て位置は文字列の長さで制限をかける
			if (count > answer.length()) {
				count = answer.length();
			}
			return answer.substring(0,count);
		}
	}
	/**
	 * フラグ設定値のboolean型変換
	 * 
	 * @param flag
	 *            flag を設定。
	 * @return boolean
	 * 				true,false
	 * @exception 無し
	 * 
	 */
	public static boolean convFlag(String flag) {
		return strEquals(trim(flag), "1") ? true : false;
	}

	/**
	 * フラグ設定値のString型変換
	 * 
	 * @param flag
	 *            flag を設定。
	 * @return String
	 * 				ON:1,OFF:0
	 * @exception 無し
	 * 
	 */
	public static String convFlag(boolean flag) {
		return flag ? "1" : "0";
	}

	/**
	 * 小数点第二位で四捨五入する
	 * 
	 * @param value
	 * @return
	 */
	public static String halfAdjust(double value) {
		value += 0.005;	// おまじない
		return format("0.0",value);
		
	}
	
	public static String halfAdjust(int digit, double value) {

		String strValue = Double.toString(value);
		StringBuffer sbValue = new StringBuffer();
		int count = strValue.indexOf(".");
		// 小数点が存在しない場合は、そのまま戻す
		if (count == -1) {
			return strValue;
		}
		//count += 1;
		sbValue.append(strValue.substring(0,count));
		strValue = strValue.substring(count+1);
		if (digit <= 0) {
			return sbValue.toString();
		}
		
		int remain = strValue.length();
		if (remain < digit + 1) {
			sbValue.append(".");
			sbValue.append(strValue);
			return sbValue.toString();
		}
		sbValue.append(strValue.substring(0,digit-1));
		strValue = strValue.substring(digit-1);
		
		int first	= getValueOfInt(strValue.substring(0,1));
		int second	= getValueOfInt(strValue.substring(1,2));
		if (second >= 5) {
			first += 1;
		}
		if (first >= 10) {
			sbValue = new StringBuffer(getValueOfInt(sbValue.toString()) + 1);
			sbValue.append(".");
			first = 0;
		}
		sbValue.append(first);
		sbValue.insert(count,".");
		
		return sbValue.toString();
	}
	
	public static String[] execMailAdr(String[] adrArray) {
		
		boolean skipFlg = false;
		MyArrayList execAdrArray = new MyArrayList();
		for (int i = 0; i < adrArray.length; i++) {
			if (i == 0) {
				execAdrArray.add(adrArray[i]);
			} else {
				for (int j = 0; j < execAdrArray.size(); j++) {
					if (strEquals(adrArray[i], (String)execAdrArray.get(j))) {
						skipFlg = true;
					}
				}
				if (!skipFlg) {
					execAdrArray.add(adrArray[i]);
				}
				skipFlg = false;
			}
		}
		return execAdrArray.toStrArray();
	}
	
	public static String[] execMailAdr(MyArrayList adrArray) {
		return execMailAdr(adrArray.toStrArray());
	}
	
	/**
	 * 年月に(Q)を付与
	 * @param ym
	 *         変換対象文字列
	 * @return reYm
	 *         変換後文字列
	 */
	public static String addQuarter(String ym) {
		
		String reYm = null;
        
		if(ym == null){
			ym = GS.EMPTY_CHARCTER;
		}
		
		reYm = ym + GS.QUARTER_CHARCTER; 
		
        return reYm;
	}

	/**
	 * 対象文字列にパディング文字列を指定byte迄右埋め
	 * @param expression
	 *         対象文字列
	 * @param strPadd
	 *         パディング文字列
	 * @param ar_replace_list
	 *         指定byte数
	 * @return expression
	 *          右埋め後文字列
	 */
	public static String paddingRight(String expression,String strPadd,int confByte){
		StringBuffer reExpression = new StringBuffer(expression);
		String strCd = "EUC-JP";
		byte[] targetArray;
		try {
			targetArray = expression.getBytes(strCd);
		} catch (UnsupportedEncodingException e) {
			targetArray = expression.getBytes();
		}
		if (targetArray.length < 0 || confByte <= targetArray.length) {
			return expression;
		}
		if (strPadd.equals(GS.EMPTY_CHARCTER)) {
			return expression;
		}
		while(targetArray.length < confByte){
			reExpression.append(strPadd);
			try {
				targetArray = reExpression.toString().getBytes(strCd);
			} catch (UnsupportedEncodingException e) {
				targetArray = reExpression.toString().getBytes();
			}
		}
		return reExpression.toString();
	}

	/**
 	* 検索対象文字列をカスタマイズ
 	* @param String
 	*         検索対象文字列
 	* @param List
 	*         置換用文字列リスト
 	* @return String
 	*         置換後文字列
 	*/
	public static String replaceExpression(String expression,List list){
		StringBuffer sb = new StringBuffer();
    	int i = 0;
    	int listIdx = 0;
		Pattern pattern = Pattern.compile(PATTERN);
    	Matcher matcher = pattern.matcher(expression);
    	while(matcher.find()){
    		listIdx = Integer.parseInt(matcher.group().substring(1,2)) - 1;
    		try{
        		matcher.appendReplacement(sb,(String)list.get(listIdx));
    		}catch(Exception ex){
        		matcher.appendReplacement(sb,(String)list.get(i));
    		}
    		i++;
    	}
    	matcher.appendTail(sb);
    	return sb.toString();
	}

	/**
 	* 検索対象文字列をカスタマイズ
 	* @param String
 	*         検索対象文字列
 	* @param String
 	*         置換用文字列
 	* @return String
 	*         置換後文字列
 	*/
	public static String replaceExpression(String expression,String strRep){
		StringBuffer sb = new StringBuffer();
    	int i = 0;
		Pattern pattern = Pattern.compile(PATTERN);
    	Matcher matcher = pattern.matcher(expression);
    	while(matcher.find()){
    		matcher.appendReplacement(sb,strRep);
    		i++;
    	}
    	matcher.appendTail(sb);
    	return sb.toString();
	}
	/**
 	* 検索対象文字列をカスタマイズ
 	* @param String
 	*         検索対象文字列
 	* @param String
 	*         検索用文字列
 	* @return boolean
 	*         パターンマッチ判定結果
 	*/
	public static boolean matches(String expression,String strRep){
		Pattern pattern = Pattern.compile(strRep);
    	Matcher matcher = pattern.matcher(expression);
    	return matcher.matches();
	}

	/**
 	* 検索対象文字列をカスタマイズ
 	* @param String
 	*         検索対象文字列
 	* @param String
 	*         置換用文字列
 	* @return String
 	*         置換後文字列
 	*/
	public static String replaceString(String expression,String strRep){
		String msg = null;
		Pattern pattern = Pattern.compile(PATTERN);
    	Matcher matcher = pattern.matcher(expression);
    	msg = matcher.replaceAll(strRep);
    	return msg;
	}
	
	/**
	 * 要件No.四-08
	 * 検索対象文字列をカスタマイズ
	 * @param expression
	 *         検索対象文字列
	 * @param strFind
	 *         検索文字列
	 * @param ar_replace_list
	 *         置換用文字列リスト
	 * @return expression
	 *         置換後文字列
	 */
	public static String replaceFolder(String expression,String strFind,ArrayList ar_replace_list) throws Exception{
		StringBuffer sb = new StringBuffer();
		String reExpression = null;
        int i = 0;
		Pattern pattern = Pattern.compile(strFind);
        Matcher matcher = pattern.matcher(expression);
        
        while(matcher.find()){
        	matcher.appendReplacement(sb,(String)ar_replace_list.get(i));
        	i++;
        }
        matcher.appendTail(sb);
        //検索文字列数と置換文字列格納リストの要素数が異なる場合エラーとする
        if(i != ar_replace_list.size()){
        	Exception e = new Exception();
        	throw e;
        }
        reExpression = sb.toString();
        return reExpression;
	}
	
    /**
     * 
     * @param var 入力文字列
     * @return シングルコート付与文字列
     */
	public static String addSingleQuotation(String var) {
		if(GS.EMPTY_CHARCTER.equals(trim(var))){
			return var;
		}
    	StringBuffer sb = new StringBuffer();
    	String token = null;
    	for(int i = 0;i < var.length();i++){
    		token = var.substring(i,i+1);
    		if(GS.SINGLE_QUOTATION.equals(token)){
        		sb.append(GS.SINGLE_QUOTATION).append(GS.SINGLE_QUOTATION);
    		}else{
        		sb.append(token);
    		}
    	}
    	return sb.toString();
    }
}