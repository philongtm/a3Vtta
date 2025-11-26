/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.util;

import java.util.Calendar;

/**
 * 共通関数クラス<br>
 * <br>
 * 和暦/西暦カレンダークラス
 * 
 */
public class JCalendar {

	public static final String G_MEIJI = "1";		// 明治
	public static final String G_TAISHO = "2";	// 大正
	public static final String G_SHOWA = "3";		// 昭和
	public static final String G_HEISEI = "4";	// 平成

	// 西暦での明治の開始日 1868/09/08
	static private final int FIRST_MEIJI = 18680908;
	// 西暦での明治の最終日 1912/07/29
	static private final int LAST_MEIJI = 19120729;
	
	// 西暦での大正の開始日 1912/07/30
	static private final int FIRST_TAISHO = 19120730;
	// 西暦での大正の最終日 1926/12/24
	static private final int LAST_TAISHO = 19261224;
	
	// 西暦での昭和の開始日 1926/12/25
	static private final int FIRST_SHOWA = 19261225;
	// 西暦での昭和の最終日 1989/01/07
	static private final int LAST_SHOWA = 19890107;
	
	// 西暦での平成の開始日 1989/01/08
	static private final int FIRST_HEISEI = 19890108;

	static private final int MEIJI  = 1;
	static private final int TAISHO = 2;
	static private final int SHOWA  = 3;
	static private final int HEISEI = 4;
	
	static private final int WAREKI[][] = {
							{MEIJI,  FIRST_MEIJI,  LAST_MEIJI},
							{TAISHO, FIRST_TAISHO, LAST_TAISHO},
							{SHOWA,  FIRST_SHOWA,  LAST_SHOWA},
							{HEISEI, FIRST_HEISEI, 0x7fffffff}
							};

	// 西暦
	private int year;
	private int month;
	private int day;
	// 和暦
	private int jGengou;
	private int jYear;
	private int jMonth;
	private int jDay;
	private boolean valid;
	// 時間
	private int hour;
	private int minute;
	private int second;
	
	/**
	 * コンストラクタ
	 */
	public JCalendar() {
		valid = false;
	}

	/**
	 * コンストラクタ(日付(西暦)の設定)<br>
	 * 
 	 * YYYY/MM/DDの書式の日付で設定を行う。
	 * 時間は0:0:0に初期化する。
	 *
	 * @param strDate YYYY/MM/DDの書式の日付
	 */
	public JCalendar(String strDate) {
		valid = false;
		setDate(strDate);
	}
	
	/**
	 * コンストラクタ(日付(西暦)の設定)<br>
	 * 
	 * java.sql.Date dateクラスで日付の設定を行う。<br>
	 * 時間は0:0:0に初期化する。
	 * 
	 *@param date 日付
	 */
	public JCalendar(java.sql.Date date) {
		valid = false;
		set(date);
	}

	/**
	 * コンストラクタ(日付(西暦)の設定)<br>
	 * 
	 * java.sql.Timestampクラスで日付の設定を行う。<br>
	 * 
	 *@param timestamp 日付
	 */
	public JCalendar(java.sql.Timestamp timestamp) {
		valid = false;
		set(timestamp);
	}

	/**
	 * 日付(和暦)の設定<br>
	 * 時間は0:0:0に初期化する。
	 * 
	 *@param gengou 元号区分
	 *@param yy 年(YY)
	 *@param mm 月(MM)
	 *@param dd 日(DD)
	 */
	public void set(String gengou, String yy, String mm, String dd) {
		
		int y = Function.getValueOfInt(yy);
		int m = Function.getValueOfInt(mm);
		int d = Function.getValueOfInt(dd);
		int first;
		int last;
		int yyyymmdd;

		// 和暦を保存
		jGengou = Function.getValueOfInt(gengou);
		jYear = y;
		jMonth = m;
		jDay = d;
		
		// 西暦に変換
		for( int i=0; i<WAREKI.length; i++ ){
			if( jGengou == WAREKI[i][0] ) {
				first = WAREKI[i][1];
				last = WAREKI[i][2];
				yyyymmdd = ((first/10000) + (y-1))*10000 + m*100 + d;
				if( (first<=yyyymmdd) && (yyyymmdd<=last) ) {
					// 西暦を保存
					year = yyyymmdd / 10000;
					month = m;
					day = d;
					// 日付の有効／無効を設定
					valid = Function.isDate(year,month,day);
					// 時間を初期化
					setTime(0,0,0);
				} else {
					// 日付は無効
					valid = false;
				}
				return;
			}
		}

		// 日付は無効
		valid = false;
		return;
	}

	/**
	 * 日付(西暦)の設定<br>
	 * 時間は0:0:0に初期化する。
	 * 
	 *@param yyyy 年(YYYY)
	 *@param mm 月(MM)
	 *@param dd 日(DD)
	 */
	public void set(String yyyy, String mm, String dd) {
		set( Function.getValueOfInt(yyyy),
				Function.getValueOfInt(mm),
				Function.getValueOfInt(dd) );
	}

	/**
	 * 日付(西暦)の設定<br>
	 * 時間は0:0:0に初期化する。
	 * 
	 *@param yyyy 年(YYYY)
	 *@param mm 月(MM)
	 *@param dd 日(DD)
	 */
	public void set(int yyyy, int mm, int dd) {

		// 西暦を保存
		year = yyyy;
		month = mm;
		day = dd;

		// 日付の有効／無効を設定
		valid = Function.isDate(year,month,day);
		if( valid == false ) return;
		
		// 和暦に変換
		int yyyymmdd = year*10000 + month*100 + day;
		int first;
		int last;
		for( int i=0; i<WAREKI.length; i++ ){
				first = WAREKI[i][1];
				last = WAREKI[i][2];
				if( (first<=yyyymmdd) && (yyyymmdd<=last) ) {
					// 和暦を保存
					jGengou = WAREKI[i][0];
					jYear = year - first/10000 + 1;
					jMonth = month;
					jDay = day;
					// 時間を初期化
					setTime(0,0,0);
					return;
				}
		}
		
		// 日付は無効
		valid = false;
		return;
	}

	/**
	 * 日付(西暦)の設定<br>
	 * java.sql.Date dateクラスで日付の設定を行う。<br>
	 * 時間は0:0:0に初期化する。
	 * 
	 *@param date 日付
	 */
	public void set(java.sql.Date date) {
		if( date == null ) {
			valid = false;
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			set( calendar.get(Calendar.YEAR) ,
				calendar.get(Calendar.MONTH)+1,
				calendar.get(Calendar.DAY_OF_MONTH) );
			setTime(0,0,0);
		}
	}
	
	/**
	 * 日付(西暦)と時間の設定。<br>
	 * java.sql.Timestampクラスで日付の設定を行う。<br>
	 * 
	 *@param timestamp 日付
	 */
	public void set(java.sql.Timestamp timestamp) {
		if( timestamp == null ) {
			valid = false;
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(timestamp);
			set( calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH)+1,
					calendar.get(Calendar.DAY_OF_MONTH) );
			setTime(calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE),
					calendar.get(Calendar.SECOND) );
		}
	}

	/**
	 * 日付(西暦)の設定<br>
	 * YYYY/MM/DD または YYYYMMDD の形式で日付の設定を行う。
	 * YYYY/MM/DD の場合 MMとDDについては１桁可能。
	 */
	public void setFreeFormatString(String format) {
		format = Function.StrReplace(format," ","");
		String[] parts = Function.StrSplitToken(format,"/");
		if( parts.length==1 ) {
			if(format.length()==8) {
				set(format+"000000");
			} else {
				valid = false;
			}
		} else if(parts.length==3) {
			set(parts[0],parts[1],parts[2]);
		} else {
			valid = false;
		}
	}
	
	/**
	 * 時間の設定<br>
	 * 
	 *@param hour 時(0～23)
	 *@param minute 時(0～59)
	 *@param second 時(0～59)
	 */
	public void setTime(int hour, int minute, int second) {
		if( ((0<=hour)&&(hour<=23)) && ((0<=minute)&&(minute<=59)) && ((0<=second)&&(second<=59)) ) {
			this.hour = hour;
			this.minute = minute;
			this.second = second;
		} else {
			valid = false;
		}
	}
	
	/**
	 * システム日時の設定<br>
	 * 
	 */
	public void setSystemDateTime() {
		Calendar calendar = Calendar.getInstance();	
		calendar.setTimeInMillis(System.currentTimeMillis());
		set( calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH)+1,
				calendar.get(Calendar.DAY_OF_MONTH) );
		setTime(calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND) );
	}

	/**
	 * システム日付の設定<br>
	 * 時間は0:0:0に初期化する。
	 * 
	 */
	public void setSystemDate() {
		Calendar calendar = Calendar.getInstance();	
		calendar.setTimeInMillis(System.currentTimeMillis());
		set( calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH)+1,
				calendar.get(Calendar.DAY_OF_MONTH) );
		setTime(0,0,0);
	}

	/**
	 * 日付の有効性を取得する。
	 * 
	 *@return true/正常 false/エラー(存在しない日付)
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * 元号区分を取得する。
	 * 
	 *@return 元号区分。日付が無効の場合はnullを返す。
	 */
	public String getGengou() {
		if( valid ) {
			return String.valueOf(jGengou);
		} else {
			return null;
		}
	}

	/**
	 * 年(西暦YYYY)を取得する。<br>
	 * 前ゼロ有り。
	 * 
	 *@return 年(西暦YYYY)。日付が無効の場合はnullを返す。
	 */
	public String getYear() {
		if( valid ) {
			return Function.format("0000",year);
		} else {
			return null;
		}
	}

	/**
	 * 年(和暦YY)を取得する。<br>
	 * 前ゼロ有り。
	 * 
	 *@return 年(和暦YY)。日付が無効の場合はnullを返す。
	 */
	public String getJYear() {
		if( valid ) {
			return Function.format("00",jYear);
		} else {
			return null;
		}
	}

	/**
	 * 月(MM)を取得する。<br>
	 * 前ゼロ有り。
	 * 
	 *@return 月(MM)。日付が無効の場合はnullを返す。
	 */
	public String getMonth() {
		if( valid ) {
			return Function.format("00",month);
		} else {
			return null;
		}
	}

	/**
	 * 日(DD)を取得する。<br>
	 * 前ゼロ有り。
	 * 
	 *@return 日(DD)。日付が無効の場合はnullを返す。
	 */
	public String getDay() {
		if( valid ) {
			return Function.format("00",day);
		} else {
			return null;
		}
	}

	/**
	 * 日付(西暦)を取得する。<br>
	 * java.sql.Dateクラスの取得を行う。
	 * 
	 *@return 日付(西暦)。日付が無効の場合はnullを返す。
	 */
	public java.sql.Date getDate() {
		if( valid ) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month-1, day);
			java.sql.Date date = new java.sql.Date(calendar.getTimeInMillis());
			return date;
		} else {
			return null;
		}
	}
	
	/**
	 * 日付(西暦)を取得する。<br>
	 * java.sql.Timestampクラスの取得を行う。<br>
	 * 時間も有効。
	 * 
	 *@return 日付(西暦)。日付が無効の場合はnullを返す。
	 */
	public java.sql.Timestamp getTimestamp() {
		if( valid ) {
			Calendar calendar = Calendar.getInstance();
//			calendar.set(year, month-1, day);
			calendar.set(year, month-1, day, hour, minute, second);
			java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTimeInMillis());
			return timestamp;
		} else {
			return null;
		}
	}
	
	/**
	 * 時(HH)を取得する。<br>
	 * HH:00～23(前ゼロ有り)
	 * 
	 *@return 時(HH)。日付が無効の場合はnullを返す。
	 */
	public String getHour() {
		if( valid ) {
			return Function.format("00",hour);
		} else {
			return null;
		}
	}

	/**
	 * 分(MM)を取得する。<br>
	 * MM:00～59(前ゼロ有り)
	 * 
	 *@return 分(MM)。日付が無効の場合はnullを返す。
	 */
	public String getMinute() {
		if( valid ) {
			return Function.format("00",minute);
		} else {
			return null;
		}
	}

	/**
	 * 秒(SS)を取得する。<br>
	 * SS:00～59(前ゼロ有り)
	 * 
	 *@return 秒(SS)。日付が無効の場合はnullを返す。
	 */
	public String getSecond() {
		if( valid ) {
			return Function.format("00",second);
		} else {
			return null;
		}
	}
	
	/**
	 * 書式化した和暦を取得する<br>
	 * 元号YY年MM月DD日<br>
	 * 
	 *@return 書式化した和暦。
	 */
//	public String getJDateForamtString() {
//		if( valid ) {
//			return Master.getKbnMeisyou("元号",getGengou()) +
//				jYear + "年" +
//				jMonth + "月" +
//				jDay + "日";
//		} else {
//			return null;
//		}
//	}

	/**
	 * 書式化した和暦、時間を取得する（時間のみゼロサプレス無し）<br>
	 * 元号YY年MM月DD日HH時mm分SS秒<br>
	 * 
	 *@return 書式化した和暦、時間。
	 */
//	public String getJDateTimeForamtString() {
//		if( valid ) {
//			return Master.getKbnMeisyou("元号",getGengou()) +
//				jYear + "年" +
//				jMonth + "月" +
//				jDay + "日" +
//				" " +
//				getHour() + "時" +
//				getMinute() + "分" +
//				getSecond() + "秒";
//		} else {
//			return null;
//		}
//	}

	/**
	 * 書式化した時間を取得する（ゼロサプレス有り）<br>
	 * HH時mm分SS秒<br>
	 * 
	 *@return 書式化した時間。
	 */
	public String getJTimeForamtString() {
		if( valid ) {
			return 	hour + "時" +
				minute + "分" +
				second + "秒";
		} else {
			return null;
		}
	}
	
	/**
	 * JCalendarクラスの文字列表現<br>
	 * 和暦、時間を表示できない場合は空文字とする。<br>
	 * 
	 *@return 書式化した和暦、時間。
	 */
	public String toString() {
		if (valid) {
			StringBuffer dateTime = new StringBuffer();
			dateTime.append(year);
			dateTime.append("/");
			dateTime.append(month);
			dateTime.append("/");
			dateTime.append(day);
			dateTime.append(" ");
			dateTime.append(getHour());
			dateTime.append(":");
			dateTime.append(getMinute());
			dateTime.append(":");
			dateTime.append(getSecond());
			return dateTime.toString();
		} else {
			return "";
		}
	}

	/**
 	 * 日付(西暦)の設定<br>
 	 * YYYYMMDDHH24MISSの書式で並ぶ14桁のStringで設定する。
	 */
	public void set(String strDate) {
		if (strDate != null && strDate.length() == 14 ) {
			String yy = strDate.substring(0,4);
			String mm = strDate.substring(4,6);
			String dd = strDate.substring(6,8);
			int hh = Function.getValueOfInt(strDate.substring(8,10));
			int mi = Function.getValueOfInt(strDate.substring(10,12));
			int ss = Function.getValueOfInt(strDate.substring(12,14));
			set(yy,mm,dd);
			setTime(hh,mi,ss);
		}
	}

	/**
 	 * 日付(西暦)の設定<br>
 	 * YYYY/MM/DDの書式で並ぶ8～10桁のString、またはYYYYMMDDの書式で並ぶ8桁のStringで設定する。
	 */
	public void setDate(String strDate) {
		if (strDate != null) {

			// 「/」区切りの場合
			String[] splitDate = strDate.split("/");
			if (splitDate.length == 3) {
				set(splitDate[0],splitDate[1],splitDate[2]);
			} else {
				// 区切り無しの場合
				if (strDate.length() == 8) {
					String yy = strDate.substring(0,4);
					String mm = strDate.substring(4,6);
					String dd = strDate.substring(6,8);
					set(yy,mm,dd);
				}
			}
		}
	}

	/**
 	 * 日付(西暦)の設定<br>
 	 * YYYY/MM/DD HH24:MI:SSの書式で並ぶ19桁のStringで設定する。
	 */
	public void setDateTime(String strDate) {
		if (strDate != null && strDate.length() == 19 ) {
			String yy = strDate.substring(0,4);
			String mm = strDate.substring(5,7);
			String dd = strDate.substring(8,10);
			int hh = Function.getValueOfInt(strDate.substring(11,13));
			int mi = Function.getValueOfInt(strDate.substring(14,16));
			int ss = Function.getValueOfInt(strDate.substring(17,19));
			set(yy,mm,dd);
			setTime(hh,mi,ss);
		}
	}

	/**
	 * YYYYMMDD形式で日付(西暦)を返す。
	 * @return YYYYMMDD形式の日付(西暦)
	 */
	public String getDateString8() {
		if ( valid ) {
			StringBuffer sbDateFormat = new StringBuffer();
			sbDateFormat.append(getYear());
			sbDateFormat.append(getMonth());
			sbDateFormat.append(getDay());
			return sbDateFormat.toString();
		} else {
			return null;
		}
	}
	/**
	 * YYYY/MM/DD形式で日付(西暦)を返す。
	 * @return YYYY/MM/DD形式の日付(西暦)
	 */
	public String getDateString10() {
		if ( valid ) {
			StringBuffer sbDateFormat = new StringBuffer();
			sbDateFormat.append(getYear());
			sbDateFormat.append("/");
			sbDateFormat.append(getMonth());
			sbDateFormat.append("/");
			sbDateFormat.append(getDay());
			return sbDateFormat.toString();
		} else {
			return null;
		}
	}

	/**
	 * 2つのJCalendarを比較します。<br>
	 * 比較は日数単位で行い、時間は無視されます。
	 * @param compJc 比較対象のJCalendar
	 * @return このJCalendarが引数JCalendarと等しい場合は値0。このJCalendarが引数JCalendarより前の場合は負の日数。このJCalendarが引数JCalendarより後の場合は正の日数。
	 * @throws Exception JCalendarが不整合の場合、または引数がNULLの場合
	 */
	public int compareTo(JCalendar compJc) throws Exception {
		if (!valid) {
			throw new Exception("日付が不整合です。");
		}
		if (compJc == null) {
			throw new Exception("引数がNULLです。");
		}
		java.util.Date compDate = compJc.getDate();
		if (compDate == null) {
			throw new Exception("引数の日付が不整合です。");
		}
		java.util.Date thisDate = getDate();
		long longThisDate = thisDate.getTime();
		long longCompDate = compDate.getTime();
		return (int)((longThisDate - longCompDate) / DAY_BY_MILLISECOND);
	}
	
	private static final long DAY_BY_MILLISECOND = 86400000l;	// 1日のミリ秒単位
//	public static final int COMP_AFTER	= 1;	// 引数が後
//	public static final int COMP_EQUAL	= 0;	// 引数と等しい
//	public static final int COMP_BEFORE	= -1;	// 引数が前
//	public static final int COMP_ERROR	= -2;	// エラー発生
//	/**
//	 * 2つのJCalendarを比較します。<br>
//	 * 比較は日数単位で行い、時間は無視されます。
//	 * @param compJc 比較対象のJCalendar
//	 * @return このJCalendarが引数JCalendarと等しい場合は定数COMP_EQUAL。このJCalendarが引数JCalendarより前の場合は定数COMP_BEFORE。このJCalendarが引数JCalendarより後の場合は定数COMP_AFTER。比較が行えない場合は定数COMP_ERROR。
//	 */
//	public int compareTo(JCalendar compJc) {
//		if (!valid) {
//			return COMP_ERROR;
//		}
//		if (compJc == null) {
//			return COMP_ERROR;
//		}
//		java.util.Date compDate = compJc.getDate();
//		if (compDate == null) {
//			return COMP_ERROR;
//		}
//		java.util.Date thisDate = getDate();
//		long longThisDate = thisDate.getTime();
//		long longCompDate = compDate.getTime();
//		int returnValue = (int)((longThisDate - longCompDate) / DAY_BY_MILLISECOND);
//		if ( returnValue < 0) {
//			return COMP_BEFORE;
//		} else if (returnValue == 0) {
//			return COMP_EQUAL;
//		} else {
//			return COMP_AFTER;
//		}
//	}
}