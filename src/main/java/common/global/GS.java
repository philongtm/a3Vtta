/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2008/01/13		SSC				1.5次版に修正を施し流用
002		2014/03/11		SSC				案件No.D13493 改善対応
003		2014/05/15		SSC				案件No.D13493 改善対応（ファイル名文字化け対応）
004		2015/03/18		SSC				BJ201408049 IA化対応時の機能改善
005		2016/12/16		SSC				BJ201612070 SSO対応
******************************************************************************/
package common.global;

import common.util.Profile;

/**
 * 共通定数を定義するクラス
 *
 */
public interface GS {

	/************************************************************************************
	 * 1.5次版機能吸収用定数
	 ***********************************************************************************/
	public static final String CMN_SES_NM				= "app.SessionData";
	public static final String CMN_SES_NM_ZEN			= "app.SessionDataZen";
	public static final String KENGENARI				= "Y";
	public static final String KENGENASI				= "N";
	public static final String RISKKANRIBU			= "1";
	public static final String HOMUBU					= "2";
	public static final String RISKKANRIBU_HOMUBU		= "3";
	public static final String UNIQUE_REGIST			= "01";
	public static final String UNIQUE_RECOG			= "02";
	public static final String UNIQUE_JIMU			= "03";
	public static final String UNIQUE_SYS				= "99";
	//パターンID
	public static final String EIGYO_REGIST_ID		= "";
	public static final String EIGYO_RECOG_ID			= "";
	public static final String EIGYOKAIKEI_REGIST_ID	= "";
	public static final String EIGYOKAIKEI_RECOG_ID	= "";
	public static final String RISKKANRIBU_ID			= "";
	public static final String HOMUBU_ID				= "";
	public static final String RISKKANRIBU_HOMUBU_ID	= "";
	public static final String NIJISATEI_REGIST_ID	= "";
	public static final String NIJISATEI_RECOG_ID		= "";
	public static final String UNIQUE_REGIST_ID		= "";
	public static final String UNIQUE_RECOG_ID		= "";
	public static final String UNIQUE_JIMU_ID			= "";
	public static final String UNIQUE_SYS_ID			= "";

	/************************************************************************************
	 * 画面ＩＤ
	 ***********************************************************************************/
	public static final String OB1101                       = "OB1101";
	public static final String OB1102                       = "OB1102";
	public static final String OB1103                       = "OB1103";
	public static final String OB1104                       = "OB1104";
	public static final String OB1105                       = "OB1105";
	public static final String OB2101                       = "OB2101";
	public static final String OB2102                       = "OB2102";
	public static final String OB2103                       = "OB2103";
	public static final String OB2104                       = "OB2104";
	public static final String OB2105                       = "OB2105";
	public static final String OC1101                       = "OC1101";
	public static final String OC1102                       = "OC1102";
	public static final String OC1103                       = "OC1103";
	public static final String OC1104                       = "OC1104";
	public static final String OC1105                       = "OC1105";
	public static final String OC1106                       = "OC1106";
	public static final String OC1107                       = "OC1107";
	public static final String SYOKAI_SATEI_SYOSAI          = "03-01-01";
	public static final String SYSTEM_GOLF                  = "04-04";
	public static final String TAIRYU_SENTEITUIKA           = "01-02C";
	public static final String SATEI_HIKIATE                = "02-00-03";
	public static final String SYOKAI_SATEI                 = "03-01-00";
	public static final String HIKIATEKENSYO_ICHIRAN        = "hikiatekensyo_ichiran";
	public static final String HIKIATEKENSYO                = "hikiatekensyo";
	public static final String HIKIATEKENSYO_SYONIN_ICHIRAN = "hikiatekensyo_syonin_ichiran";
	public static final String HIKIATEKENSYO_SYONIN         = "hikiatekensyo_syonin";
	public static final String OD1101                       = "OD1101";
	public static final String OD1102                       = "OD1102";
	public static final String OD1103                       = "OD1103";
	public static final String OD1104                       = "OD1104";
	public static final String OD1105                       = "OD1105";
	public static final String OZ1101                       = "OZ1101";
	public static final String OZ1102                       = "OZ1102";
	public static final String OZ2101                       = "OZ2101";
	public static final String OZ3101                       = "OZ3101";
	public static final String OZ4101                       = "OZ4101";
	public static final String OZ5101                       = "OZ5101";
	public static final String OZ6101                       = "OZ6101";
	public static final String OZ6102                       = "OZ6102";
	public static final String OZ6103                       = "OZ6103";
	public static final String OZ6104                       = "OZ6104";
	public static final String OZ6105                       = "OZ6105";
	public static final String OZ6106                       = "OZ6106";
	public static final String OZ6107                       = "OZ6107";
	public static final String OZ6108                       = "OZ6108";
	public static final String OZ6109                       = "OZ6109";
	public static final String OZ6110                       = "OZ6110";
	public static final String HIKIATEKENSYO_TAB            = "hikiatekensyo_tab";
	public static final String OS1101                       = "OS1101";
	public static final String OS2101                       = "OS2101";
	public static final String OS3101                       = "OS3101";
	public static final String OS3102                       = "OS3102";
	public static final String OS3103                       = "OS3103";
	public static final String OS3104                       = "OS3104";
	public static final String OS3105                       = "OS3105";
	public static final String OS4101                       = "OS4101";
	public static final String OS6101                       = "OS6101";
	public static final String OS6102                       = "OS6102";
	public static final String OS6103                       = "OS6103";
	public static final String OS6104                       = "OS6104";
	public static final String OS6105                       = "OS6105";
	public static final String OS7101                       = "OS7101";
	public static final String OS7102                       = "OS7102";
	public static final String OS7103                       = "OS7103";
	public static final String OS7104                       = "OS7104";
	public static final String OS7105                       = "OS7105";
	public static final String OS7106                       = "OS7106";
	public static final String OS7107                       = "OS7107";
	public static final String OS7108                       = "OS7108";
	public static final String OS7109                       = "OS7109";
	public static final String OS7110                       = "OS7110";
	public static final String OS7111                       = "OS7111";
	public static final String OS7113                       = "OS7113";
	public static final String OS7114                       = "OS7114";
	public static final String OS7115                       = "OS7115";
	public static final String OS7116                       = "OS7116";
	public static final String OS8101                       = "OS8101";
	public static final String OS5101                       = "OS5101";
	public static final String TYUSYUTUJOKEN                = "tyusyutujoken";
	public static final String RENKETU                      = "renketu";
	public static final String DOWNLOAD                     = "DOWNLOAD";

	/************************************************************************************
	 * 業務フローパターンマップ取得キー
	 ***********************************************************************************/
	public static final String JI_JISHI_PHASE 			= "JI_JISHI_PHASE";           	//次実施フェーズ
	public static final String JI_KAISHI_STATUS 			= "JI_KAISHI_STATUS";       	//次開始ステータス
	public static final String JISHI_PHASE 				= "JISHI_PHASE";                //実施フェーズ
	public static final String KAISHI_STATUS 				= "KAISHI_STATUS";             	//開始ステータス
	public static final String JISHI_PHASE_KANRYO_FLG 	= "JISHI_PHASE_KANRYO_FLG";    	//完了フラグ

	/************************************************************************************
	 * セッションキー名
	 ***********************************************************************************/
    public static final String LOCALE   = "common.struts.adapter.action.LOCALE";
    public static final String APP_SESSION   = "app.SessionData";
    public static final String LOGINFORM   = "00LoginForm";
    public static final String MENUFORM   = "00MenuForm";

	/************************************************************************************
	 * リクエストキー名
	 ***********************************************************************************/
    public static final String HELP_PATH   = "help_path";

    /************************************************************************************
	 * 基幹システム区分
	 ***********************************************************************************/
    public static final String GSS   = "01";
    public static final String MTS   = "02";
    public static final String FOCUS = "03";

	/************************************************************************************
	 * フェーズコード
	 ***********************************************************************************/
    /** 滞留判定 */
    public static final String PHASE_TAIRYU_HANTEI			= "10";
    /** 滞留判定検証 */
    public static final String PHASE_TAIRYU_HANTEI_KENSHO		= "20";
    /** 対象先選定 */
    public static final String PHASE_TAISHOSAKI_SENTEI		= "30";
    /** 一次査定 */
    public static final String PHASE_ICHIJI_SATEI				= "40";
    /** 一次査定検証 */
    public static final String PHASE_ICHIJI_SATEI_KENSYO		= "50";
    /** 二次査定 */
    public static final String PHASE_NIJI_SATEI				= "60";
    /** クレーム債権再設定 */
    public static final String PHASE_KUREMU_SAIKEN_SAISETTEI	= "65";
    /** 引当金検証 */
    public static final String PHASE_HIKIATEKIN_KENSYO		= "70";
    /** 引当金確認 */
    public static final String PHASE_HIKIATEKIN_KAKUNIN		= "80";
    /** 仮基準査定追加 */
    public static final String PHASE_KARIKIJUN_SATEI_TUIKA	= "90";

	/************************************************************************************
	 * ステータスコード
	 ***********************************************************************************/
    /** 未処理 */
    public static final String STATUS_MISYORI			= "10";
    /** 処理中 */
    public static final String STATUS_SYORICHU		= "20";
    /** 承認待 */
    public static final String STATUS_SYONIN_MACHI	= "30";
    /** 完了 */
    public static final String STATUS_KANRYO			= "40";

	/************************************************************************************
	 * 基準日
	 ***********************************************************************************/
	public static final String SYOKAI		= "1";
	public static final String TYUUKAN	= "5";
	public static final String SAISYU		= "9";

	/************************************************************************************
	 * カンマ
	 ***********************************************************************************/
	public static final String COMMA		= ",";

	/************************************************************************************
	 * アンスコ
	 ***********************************************************************************/
	public static final String UNDERLINE	= "_";

	/************************************************************************************
	 * カンマ
	 ***********************************************************************************/
	public static final String SLASH		= "/";

	/************************************************************************************
	 * ハイフン
	 ***********************************************************************************/
	public static final String HAIHUN		= "-";

	/************************************************************************************
	 * シングルクォーテーション
	 ***********************************************************************************/
	public static final String SINGLE_QUOTATION	= "'";

	/************************************************************************************
	 * ダブルクォーテーション
	 ***********************************************************************************/
	public static final String DOUBLE_QUOTATION	= "\"";


	/************************************************************************************
	 * 空文字
	 ***********************************************************************************/
	public static final String EMPTY_CHARCTER		= "";

	/************************************************************************************
	 * 括弧
	 ***********************************************************************************/
	public static final String KAKKO_HIDARI		= "(";
	public static final String KAKKO_MIGI			= ")";

	/************************************************************************************
	 * 第1/3四半期判別用文字
	 ***********************************************************************************/
	public static final String QUARTER_CHARCTER	= "(Q)";

	/************************************************************************************
	 * 半期・四半期区分
	 ***********************************************************************************/
	public static final String HANKI      = "1";
	public static final String SIHANKI    = "2";

	/************************************************************************************
	 * 取引先区分
	 ***********************************************************************************/
	public static final String SEIJOUSAKI_TORIKBN		= "1";
	public static final String YOUTYUI_TORIKBN		= "2";
	public static final String KASIDAORE_TORIKBN		= "3";
	public static final String HASANKOUSEI_TORIKBN	= "4";
	public static final String SONOTA_TORIKBN			= "5";

	/************************************************************************************
	 * 取引先区分タブ用
	 ***********************************************************************************/
	public static final String SEIJOU_YOUTYUI_TORIKBNTAB		= "1";
	public static final String KASIDAORE_TORIKBNTAB			= "2";
	public static final String HASANKOUSEI_TORIKBNTAB			= "3";

	/************************************************************************************
	 * 債権区分
	 ***********************************************************************************/
	public static final String IPPAN_SAIKEN			= "1";
	public static final String KASIDAORE_SAIKEN		= "2";
	public static final String HASANKOUSEI_SAIKEN		= "3";
	public static final String SONOTA_SAIKEN			= "4";

	/************************************************************************************
	 * 半角スペース文字
	 ***********************************************************************************/
	public static final String SPACE_CHARCTER	= " ";
	public static final String SPACE_CHARCTER_ENCODE	= "_";

	/************************************************************************************
	 * 真偽文字
	 ***********************************************************************************/
	public static final String TRUE_CHARCTER	= "true";
	public static final String FALSE_CHARCTER	= "false";

   /************************************************************************************
	 * FLG
	 ***********************************************************************************/
	public static final String ON  = "1";
	public static final String OFF = "0";

	/************************************************************************************
	 * 入力チェック種別
	 ***********************************************************************************/
	public static final int VALIDATE_KINSHIMOJI = 1;
	public static final int VALIDATE_MAILADDR   = 2;
	public static final int VALIDATE_BLANK      = 3;

	/************************************************************************************
	 * 登録ポイント
	 ***********************************************************************************/
	/** 取引先概要決算概況 */
	public static final String COMMENT_VAL_10			= "10";

	/** 取引先区分判定根拠 */
	public static final String COMMENT_VAL_20 		= "20";

	/** 債権区分判定根拠 */
	public static final String COMMENT_VAL_30  		= "30";

	/** 債権区分判定発生経緯 */
	public static final String COMMENT_VAL_40  		= "40";

	/** 引当金判定その他 */
	public static final String COMMENT_VAL_50  		= "50";

	/** 引当金判定その他回収 */
	public static final String COMMENT_VAL_60  		= "60";

	/** 引当金判定「履行請求懸念」*/
	public static final String COMMENT_VAL_70   		= "70";

	/** 引当金判定「引当金算定根拠」*/
	public static final String COMMENT_VAL_80  		= "80";

	/** 引当金判定今後の回収見通し */
	public static final String COMMENT_VAL_90			= "90";

	/** 引当金検証引当金算定根拠 */
	public static final String COMMENT_VAL_00			= "00";

	/** フラグコメント */
	public static final String COMMENT_VAL_95  		= "95";

	/** 選定詳細 対象外・追加コメント */
	public static final String COMMENT_VAL_97  		= "97";

	/** 転送コメント */
	public static final String COMMENT_VAL_98  		= "98";

	/** 差戻コメント */
	public static final String COMMENT_VAL_99  		= "99";

	/** 引当金確認区分判定根拠 */
	public static final String COMMENT_VAL_01  		= "01";

	/************************************************************************************
	 * フレームワーク用
	 ***********************************************************************************/
	public static final String NAME     = "name";
	public static final String DOTXLS   = ".xls";
	public static final String TMP      = "tmp";
	public static final String EXCELDIR = "/excel/";
	public static final String LOGDIR   = "/WEB-INF/log";
	public static final String TMPDIR   = "/WEB-INF/tmp";
	public static final String HELPDIR   = "/help";

	/************************************************************************************
	 * ディスパッチ用リクエストパラメータ
	 ***********************************************************************************/
	public static final String EVENT = "event";

	/************************************************************************************
	 * プロパティファイルのキー
	 ***********************************************************************************/
	public static final String PROFILE_LOGLEVEL			= "log.level";
	public static final String PROFILE_SESSION_TIMEOUT	= "session.timeout";
	public static final String PROFILE_DB_NAME			= "db.Name";
	public static final String PROFILE_DB_URL			    = "db.URL";
	public static final String PROFILE_DB_USER			= "db.User";
	public static final String PROFILE_DB_PASSWORD		= "db.Password";
	public static final String PROFILE_DB_MAXCONN		    = "db.MaxConn";
	public static final String PROFILE_DB_TIMEOUT	    	= "db.Timeout";
	public static final String PROFILE_FTPHOST			= "ftp.host";
	public static final String PROFILE_FTPUSER			= "ftp.user";
	public static final String PROFILE_FTPPASS			= "ftp.pass";
	public static final String PROFILE_FTPDEBUG 		    = "ftp.debug";
	public static final String PROFILE_FTPDIR 			= "ftp.dir";
	public static final String PROFILE_FTPDIR_HELP 		= "ftp.dirHelp";
	public static final String PROFILE_SMTPHOST 		    = "smtp.host";
	public static final String PROFILE_SMTPFROM			= "smtp.from";
	public static final String PROFILE_SMTPDEBUG		    = "smtp.debug";
	public static final String PROFILE_SMTPENABLED		= "smtp.enabled";
	public static final String PROFILE_UPLOADMAXSZ		= "upload.maxsize";

	/************************************************************************************
	 * 拡張子
	 ***********************************************************************************/
	public static final String JSP    = ".jsp";
	public static final String ACTION = ".do";

	/************************************************************************************
	 * セッション管理
	 ***********************************************************************************/
	public static final String INIT              = "INIT";
	public static final String RB                = "ResourceBundle";
	public static final String LANG              = "LANG";
	public static final String LANG_JA           = "Ja";
	public static final String LANG_EN           = "En";
	public static final String MESSAGECONTEXT    = "MESSAGECONTEXT";
	public static final String DOWNLOADCONTEXT   = "DOWNLOADCONTEXT";
	public static final String APPCONTEXT        = "APPCONTEXT";
	public static final String MERGEDXLS         = "mergedxls";
	public static final String ERROR             = "ERROR";
	public static final String VALIDATEEXCEPTION = "VALIDATEEXCEPTION";

	/************************************************************************************
	 * ロケール
	 ***********************************************************************************/
	public static final String LOCALE_JA           = "ja";
	public static final String ACCEPT_LANGUAGE     = "accept-language";

	/************************************************************************************
	 * ログレベル
	 ***********************************************************************************/
	public static final int	LOG_ERR	= 0;
	public static final int	LOG_WAR	= 1;
	public static final int	LOG_INF	= 2;
	public static final int	LOG_DBG	= 3;

	/************************************************************************************
	 * 設定ファイル名
	 ***********************************************************************************/
	public static final String PROPERTY_WEBSYSTEM	= "application";
	public static final String PROPERTY_ERRORMSG	= "message";

	/************************************************************************************
	 * アプリケーションフォルダ定義
	 ***********************************************************************************/
	public static final String WEB_ROOT			= "..";
	public static final String WEB_CSS			= WEB_ROOT+"/css/";
	public static final String WEB_INCLUDE		= WEB_ROOT+"/include/";
	public static final String WEB_EXCEL			= WEB_ROOT+EXCELDIR;
	public static final String WEB_IMAGE			= WEB_ROOT+"/image/";
	public static final String WEB_IMAGE_ICON		= WEB_IMAGE+"icon/";
	public static final String WEB_LOGIN			= WEB_ROOT+"/login/";
	public static final String WEB_COMMON			= WEB_ROOT+"/common/";
	public static final String WEB_GYOUMU			= WEB_ROOT+"/gyoumu/";
	public static final String FTP_DIR			= Profile.getString(GS.PROFILE_FTPDIR,"");;
	public static final String WEB_TAIRYU			= WEB_ROOT+"/tairyu/";
	public static final String WEB_SYSTEM			= WEB_ROOT+"/system/";
	public static final String TENPU_SENTAKU		="tenpu_sentaku";
	public static final String SYOSAI				="syosai";
	public static final String TENPU_SYOKAI		="tenpu_syokai";
	public static final String KUREEMU_SYOSAI		="kureemuSyosai";

	/************************************************************************************
	 * PlsqlAcc関連
	 ***********************************************************************************/
	public final int EXCEPTIOPN_ERR = 1;
	public final int EXCEPTIOPN_WAR = 2;

	/************************************************************************************
	 * ＪＳＰ/アクションモジュール相対パス
	 ***********************************************************************************/
	public static final String A00_DOWNLOAD		= "/download";
	public static final String A00_LOGIN			= "/login/login";
	public static final String A00_LOGIN_JSP		= A00_LOGIN+JSP;
	public static final String A00_MENU			= "/login/menu";

	/************************************************************************************
	 * Actionクラスのリターンコード
	 ***********************************************************************************/
	public static final String RC_OK				= "ok";
	public static final String RC_RELOAD			= "reload";
	public static final String RC_NG				= "ng";
	public static final String RC_CLOSE			= "close";
	public static final String RC_TOP				= "top";
	public static final String RC_MENU			= "menu";
	public static final String RC_LOGOUT			= "logout";
	public static final String RC_LINK			= "link";
	public static final String RC_CANCEL			= "cancel";
	public static final String RC_NEXT			= "next";
	public static final String RC_ADD				= "add";
	public static final String RC_DEL				= "delete";
	public static final String RC_UPD				= "insert";
	public static final String RC_PDF				= "pdf";
	public static final String RC_KENGEN_ERROR		= "kengen_error";

	/************************************************************************************
	 * メソッドのリターンコード
	 ***********************************************************************************/
	public static final int RC_DB_ERROR			= -1;
	public static final int RC_NORMAL				= 0;
	public static final int RC_OTHER				= 1;
	public static final int RC_WAR				= 2;
	public static final boolean A20_TRUE			= true;
	public static final boolean A20_FALSE		= false;

	/************************************************************************************
	 * モーダルダイアログの戻り値
	 ***********************************************************************************/
	public static final String MR_REGIST			= "regist";
	public static final String MR_ATTACH			= "attach";
	public static final String MR_CLOSE			= "close";

	/************************************************************************************
	 * スタイルシート名称
	 ***********************************************************************************/
	public static final String COMMON_STYLE		= "style.css";

	/************************************************************************************
	 * 起動モード
	 ***********************************************************************************/
	public static final String MODE_START_NORMAL					= "1";
	public static final String MODE_START_MASTER_SHINSEINYURYOKU	= "2";

	/************************************************************************************
	 * 査定会社コード
	 ***********************************************************************************/
	public static final String SATEIKAISYA_SJ			= "SJ";
	public static final String SATEIKAISYA_PN			= "PN";

	/************************************************************************************
	 * フィールド名
	 ***********************************************************************************/
    public static final String FOCUS_FIELD = "focus_field";

    /************************************************************************************
	 * 取戻不可フラグ
	 ***********************************************************************************/
    public static final String TORIMODOSHI_KA   = "0";
    public static final String TORIMODOSHI_HUKA = "1";

    /************************************************************************************
	 * ※印
	 ***********************************************************************************/
    public static final String KOMEJIRUSI   = "※";

    /************************************************************************************
	 * セミコロン
	 ***********************************************************************************/
    public static final String SEMICOLON   = ";";

    /************************************************************************************
	 * SSO
	 ***********************************************************************************/
    public static final String REQUEST_HEADER_TOGO_ID = "togo_id";
    public static final String REQUEST_HEADER_SSO_LANG = "sso_lang";
    public static final String SSO_LOGIN = Profile.getString("SSO.LOGIN", "");

}
