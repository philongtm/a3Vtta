/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.util;

import org.apache.commons.validator.EmailValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 入力項目チェック共通クラス
 * <pre>
 * チェックでエラーの場合は、エラーコードを設定する。
 * </pre>
 */
public class InputCheck {

	// 使用禁止文字
	private static final String kinshiMoji = 
		"①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑯⑰⑱⑲⑳ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩ" +
		"㍉㌔㌢㍍㌘㌧㌃㌶㍑㍗㌍㌦㌣㌫㍊㌻㎜㎝㎞㎎㎏㏄㎡" +
		"㍻〝〟㏍℡㊤㊥㊦㊧㊨㈱㈲㈹㍾㍽㍼∮∑∟⊿";

    /**
     * 渡された文字のバイト数を返却します。
     *
     * @param strMoji 文字列
     * @return バイト数
     */
	public int lenB(String strMoji) {
        if(strMoji == null) return 0;
        try{
            byte[] byteMoji = strMoji.getBytes("EUC-JP");
            return byteMoji.length;
        }catch(Exception e){
            return 0;
        }
    }

    /**
     * 渡された文字が指定されたバイト数の範囲内かチェックします。
     *
     * @param strMoji 文字列
     * @param nSize バイト数
     * @return true:範囲内 false:範囲外
     */
    public boolean islength(String strMoji,int nSize) {
        return lenB(strMoji) <= nSize;
    }

    /**
     * 渡された文字が半角数字かチェックします。
     *
     * @param strMoji 文字列
     * @return true:数字 false:数字以外
     *
     */
    public boolean isNumeric(String strMoji) {
       	if((strMoji == null)||(Function.trim(strMoji).length() == 0)){
            return false;
        }
    	if( strMoji.length() != lenB(strMoji) ) {
            return false;
    	}
    	for(int i=0 ; i<strMoji.length() ; i++){
            if(Character.isDigit(strMoji.charAt(i))) continue; 
            return false;
        }
        return true;
    }

    /**
     * 渡された文字が半角英数字かチェックします。
     *
     * @param strMoji 文字列
     * @return true:数字 false:数字以外
     *
     */
    public boolean isNumLetter(String strMoji){

    	if((strMoji == null)||(strMoji.trim().length() == 0)){
            return false;
        }
    	if( strMoji.length() != lenB(strMoji) ) {
            return false;
    	}
        for(int i=0 ; i<strMoji.length() ; i++){
            if(Character.isLetterOrDigit(strMoji.charAt(i))) continue; 
            return false;
        }
        return true;
    }

    /**
     * 渡された引数がＮＵＬＬもしくはブランクかどうかのチェックを行う。
     *
     * @param　　sValue 入力値
     * @return 　渡された引数がＮＵＬＬもしくはブランクであればtrue、そうでなければfalseを返す。<BR>
     *           また文字列の全てが半角スペース、全角スペースのいずれかである場合はfalseを返す。
     */
    public boolean isNullBlank(String sValue){
    	String s = Function.trim(sValue);
        return (s==null) || (s.length()==0);
    }

    /**
     * 渡された引数（日付）が西暦かチェックを行う。
     *
     * @param　　strYYYYMMDD 入力値
     * @return 　西暦であればtrue、そうでなければfalseを返す。<BR>
     *          ※YYYY/MM/DD形式,YYYYMMDD形式両方チェック
     *
     */
    public boolean isYYYY_MM_DD(String strYYYYMMDD){
    	String[]     DE1 = null;
//    	StringBuffer ab  = null;

 		//入力形式がYYYY/MM/DDならば、
		//if((Function.trim(strYYYYMMDD).indexOf("/")!=-1)&&(Function.trim(strYYYYMMDD).length()==10)){
		if(Function.trim(strYYYYMMDD).indexOf("/")!=-1){
//			DE1 = new String[2];
			DE1 = Function.trim(strYYYYMMDD).split("/");
//			ab  = new StringBuffer();
//			for(int i=0;i<DE1.length;i++){
//				//連結：文字列連結
//				ab.append(DE1[i]);
//			}
			//YYYY/MM/DD
//			String sb = ab.toString(); // 文字列バッファから文字列に変換
//			if(sb.length()==8){
//				//西暦かどうかのチェック
//				if(!Function.isDate(sb.substring(0,3),sb.substring(4,5),sb.substring(6,7))){
//					return false;
//				}
//			}else{
//				//8文字ではない
//				return false;
//			}
			if (DE1.length != 3) {
				return false;
			}
			// 月・日の前スペース埋めはエラーとする
			if (Function.trim(DE1[0]).length() != 4 || (Function.trim(DE1[1]).length() != 2 && DE1[1].length() != 1) || (Function.trim(DE1[2]).length() != 2 && DE1[2].length() != 1)) {
				return false;
			}

		//YYYYMMDD
		}else if((Function.trim(strYYYYMMDD).indexOf("/")==-1)&&(Function.trim(strYYYYMMDD).length()==8)){
			// 月・日の前スペース埋めはエラーとする
			if (Function.trim(Function.trim(strYYYYMMDD).substring(4,6)).length() != 2 || Function.trim(Function.trim(strYYYYMMDD).substring(6,8)).length() != 2) {
				return false;
			}
			//西暦かどうかのチェック
//			if(!Function.isDate(Function.trim(strYYYYMMDD).substring(0,3),Function.trim(strYYYYMMDD).substring(4,5),Function.trim(strYYYYMMDD).substring(6,7))){
			if(!Function.isDate(Function.trim(strYYYYMMDD).substring(0,4),Function.trim(strYYYYMMDD).substring(4,6),Function.trim(strYYYYMMDD).substring(6,8))){
				//日付を入力してください
				return false;
			}
		}
		else{
			//日付を入力してください
			return false;
		}
		return true;
	}

    /**
     * 渡された引数が全角文字だけで構成されているかチェックします。
     *
     * @param　　sValue 入力値
     * @return 　全角文字で構成されていればtrueそうでなければfalse
     */
    public boolean isZenkaku(String value){
		return (value.length()*2) == lenB(value);
    }
    
    /**
     * 渡された引数が半角文字だけで構成されているかチェックします。
     *
     * @param　　sValue 入力値
     * @return 　半角文字で構成されていればtrueそうでなければfalse
     */
    public boolean isHankaku(String value){
		return value.length() == lenB(value);
    }

	/**
	 * 渡された年月が整合性の取れた値であることを確認する。
	 * 
	 * @param ym	年月
	 * @return		整合性が取れていればTRUE、不整合であればFALSE
	 */
    public boolean isYmChk(String ym) {
    	if (ym == null || ym.length() != 6) {
    		return false;
    	}
    	// 月の前スペース埋めはエラーとする。
    	if (Function.trim(ym.substring(4,6)).length() != 2) {
    		return false;
    	}
    	return Function.isDate(ym.substring(0,4), ym.substring(4,6),"1");
    }

	/**
	 * 渡された値が指定された範囲内であることを確認する。
	 * 
	 * @param target	対象の値
	 * @param lowValue	最小値
	 * @param hgihValue	最大値
	 * @return		範囲内：TRUE、範囲外FALSE
	 */
    public boolean isInLimitsLong(String target, long lowValue, long hgihValue) {
    	if (target == null || target.length() == 0) {
    		return false;
    	}
    	long longTarget = Function.getValueOfLong(target);
    	
    	if (longTarget < lowValue || hgihValue < longTarget) {
    		return false;
    	}
    	return true;
    }
    
	/**
	 * 渡された値が指定された範囲内であることを確認する。
	 * 
	 * @param target	対象の値
	 * @param lowValue	最小値
	 * @param hgihValue	最大値
	 * @return		範囲内：TRUE、範囲外FALSE
	 */
    public boolean isInLimitsDouble(String target, double lowValue, double hgihValue) {
    	if (target == null || target.length() == 0) {
    		return false;
    	}
    	double doubleTarget = Function.getValueOfDouble(target);
    	
    	if ( doubleTarget < lowValue || hgihValue <  doubleTarget) {
    		return false;
    	}
    	return true;
    }

	/**
	 * 渡された値が数値かどうかを確認する。(マイナス値対応)
	 * 
	 * @param strMoji	対象の値
	 * @return		数値：TRUE、数値以外FALSE
	 */    
    public boolean isNumber(String strMoji){

		if (!Function.isDouble(strMoji)) {
			return false;
		}
		
		// Not a Numberをチェック
		if (strMoji.equals("NaN")) {
			return false;
		}

        return true;
    }

	/**
	 * パラメータの値が半角文字のみで構成されているか確認する。
	 * 半角カナを1バイトと換算するために、SJISにてgetBytesする。
	 * （null、空文字はエラーとしない）
	 * 
	 * @param target　対象の値
	 * @return　半角のみ：TRUE、全角を含む：FALSE
	 */
	public boolean hankakuCheck(String target) {

		if (target == null || target.length() == 0) {

			// 文字無しはエラーとしない
			return true;
		}
		
		int byteSize;
		try {
			// 半角カナ文字を1バイトと数えるため、SJISでgetBytesする
        	byteSize = target.getBytes("SJIS").length;
		} catch (Exception e) {
        	byteSize = target.getBytes().length;
	    }
		
		if (byteSize != target.length()) {

			return false;
		}

		return true;
	}
	
	/**
	 * メールアドレスの書式チェックを行います。
	 * アドレスが実在するかどうかはチェックしません。
	 * 
	 * @param maikAddr　メールアドレス
	 * @return	true/正常 false/エラー
	 */
	public boolean isMailAddr(String mailAddr) {
		EmailValidator ev = EmailValidator.getInstance();
		return ev.isValid(mailAddr);
	}
	
	/**
	 * 小数の値ではないことを確認する
	 * 
	 * @param target
	 * @return true/整数 false/小数
	 */
	public boolean isNotDecimal(String target) {
		if (target == null) {
			// 引数nullはエラーとしない
			return true;
		}
		if (target.indexOf(".") >= 0) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 入力禁止文字の有無をチェックする
	 * @param text
	 * @return true/禁止文字有り  false/禁止文字無し
	 */
	public boolean haveKinshiMoji(String text) {
    	for(int i=0 ; i<text.length() ; i++){
    		if( kinshiMoji.indexOf(text.charAt(i)) >= 0 ) {
                return true;
    		}
        }
		return false;
	}
	
	/**
	 * 半角英数、半角記号であるかチェックする
	 * @param text
	 * @return true/半角英数・半角記号のみ  false/半角英数・半角記号以外あり
	 */
	public boolean isAlphaNumeric(String text){
		Pattern pattern = Pattern.compile("^[0-9a-zA-Z -~]*$");
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();

	}

	/**
	 * YYYYMM形式で入力されているかチェックする
	 * @param String
	 * @return boolean
	 */
	public boolean isDateFormat(String date){
	    SimpleDateFormat dateF = new SimpleDateFormat("yyyyMM");
	    try {
	        Date date1 = dateF.parse(date);
	    } catch (ParseException e) {
	    	return true;
	    }
	    return false;
	}
}