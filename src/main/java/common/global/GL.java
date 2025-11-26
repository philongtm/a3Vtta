/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2008/01/13		SSC				新規作成
002		2009/10/23		SSC				課題No.73 取引先区分プルダウン設定値を2.0次に合わせる
003		2009/10/27		SSC				課題No.28 引当対象外/帳簿外対応
004		2009/11/13		SSC				課題No.09 文書添付仕様変更
005		2009/11/16		SSC				課題No.119
006		2009/11/26		SSC				課題No.148 検証NG対応
007		2009/11/27		SSC				課題No.105 業務フローパターンのエラーメッセージ修正
008		2010/01/08		SSC				課題No.230 ログインユーザでログインユーザを更新時対応
009		2014/03/11		SSC				案件No.D13493 改善対応（コメントのみ修正）
010		2014/12/01		SSC				判定事由の文言修正対応(BJ201412002、コメントのみ修正)
011		2015/03/18		SSC				IA化対応時の機能改善(BJ201408049)
012		2016/03/24		SSC				部門廃止対応（一次）(BJ201602002)
******************************************************************************/
package common.global;

/**
 * 共通定数を定義するクラス
 *
 */
public interface GL {

	/************************************************************************************
	 * システムタイトル
	 ***********************************************************************************/
	public static final String TITLE_SYSTEM ="title.system";
	public static final String TITLE_HELP ="title.help";
	public static final String TITLE_SENTAKU ="title.sentaku";
	public static final String IMG_TITLE    ="img.title";

	/************************************************************************************
	 * ヘルプリンク
	 ***********************************************************************************/
	public static final String LINK_HELP ="link.help";

	/************************************************************************************
	 * メニューリンクタイトル
	 ***********************************************************************************/
	public static final String LINK_TAIRYU                ="link.tairyu";
	public static final String LINK_SATEI1                ="link.satei1";
	public static final String LINK_SATEI2                ="link.satei2";
	public static final String LINK_HIKIATEKENSYO_KAKUNIN ="link.hikiatekensyo_Kakunin";
	public static final String LINK_SYOUKAI               ="link.syoukai";
	public static final String LINK_TYOHYO                ="link.tyohyo";
	public static final String LINK_SYSTEM                ="link.system";

	/************************************************************************************
	 * タブタイトル
	 ***********************************************************************************/
	public static final String TAB_SEIJO_YOUTYUUI			="tab.seijo_youtyuui";
	public static final String TAB_KASHITAORE				="tab.kashitaore";
	public static final String TAB_HASAN_KOSEI			="tab.hasan_kosei";
	public static final String TAB_TORI_GAIYO				="tab.tori_gaiyo";
	public static final String TAB_TORI_KBN				="tab.tori_kbn";
	public static final String TAB_HIKIATE_HANTEI			="tab.hikiate_hantei";
	public static final String TAB_RYUHO_SAIMU			="tab.ryuho_saimu";
	public static final String TAB_SAIKEN_MEISAI			="tab.saiken_meisai";

	/************************************************************************************
	 * メニューリンク
	 ***********************************************************************************/
	public static final String LINK_OB1101        ="link.OB1101";			//判定登録
	public static final String LINK_OB1104        ="link.OB1104";			//判定承認
	public static final String LINK_OB2101        ="link.OB2101";			//対象先選定
	public static final String LINK_OB2104        ="link.OB2104";			//対象先選定確認
	public static final String LINK_OC1101        ="link.OC1101";			//査定登録
	public static final String LINK_OC1106        ="link.OC1106";			//査定承認
	public static final String LINK_HIKIATEKENSYO ="link.hikiatekensyo";	//引当金検証
	public static final String LINK_OD1101        ="link.OD1101";			//引当金確認
	public static final String LINK_OD1103        ="link.OD1103";			//引当金承認
	public static final String LINK_OS6101        ="link.OS6101";			//査定内容照会
	public static final String LINK_OS6103        ="link.OS6103";			//進捗状況照会
	public static final String LINK_OS3101        ="link.OS3101";			//クレーム債権再設定
	public static final String LINK_OS3104        ="link.OS3104";			//クレーム債権再設定承認
	public static final String LINK_OS7101        ="link.OS7101";			//代行設定
	public static final String LINK_OS7102        ="link.OS7102";			//査定会社メンテナンス
	public static final String LINK_OS7104        ="link.OS7104";			//業務フローパターンメンテナンス
	public static final String LINK_OS7106        ="link.OS7106";			//ユーザマスタメンテナンス
	public static final String LINK_OS7108        ="link.OS7108";			//勘定科目マスタメンテナンス
	public static final String LINK_TYUSYUTUJOKEN ="link.tyusyutujoken";	//抽出条件メンテナンス(本社)
	public static final String LINK_OS7110        ="link.OS7110";			//抽出条件メンテナンス
	public static final String LINK_CHAMPION      ="link.champion";		//チャンピオン部メンテナンス
	public static final String LINK_OS4101        ="link.OS4101";			//ゴルフ会員権メンテナンス
	public static final String LINK_RENKETU       ="link.renketu";		//連結区分マスタUPLOAD
	public static final String LINK_OS8101        ="link.OS8101";			//帳票ダウンロード
	public static final String LINK_OS1101        ="link.OS1101";			//ログオフ

	/************************************************************************************
	 * 共通画面リンク
	 ***********************************************************************************/
	public static final String LINK_OZ4101 ="link.OZ4101";				//コメント表示

	/************************************************************************************
	 * ボタン
	 ***********************************************************************************/
	public static final String BTN_KETTEI          ="btn.kettei";
	public static final String BTN_MASTERDL        ="btn.masterDl";
	public static final String BTN_LOGIN           ="btn.login";
	public static final String BTN_CANCEL          ="btn.cancel";
	public static final String BTN_CLOSE           ="btn.close";
	public static final String BTN_BACK            ="btn.back";
	public static final String BTN_SAVE            ="btn.save";
	public static final String BTN_TEMPORALLYSAVE  ="btn.temporallysave";
	public static final String BTN_REGISTER        ="btn.register";
	public static final String BTN_DOWNLOAD        ="btn.download";
	public static final String BTN_CLEARUSER       ="btn.clearuser";
	public static final String BTN_SASHIMODOSHI    ="btn.sashimodoshi";
	public static final String BTN_DO_SASHIMODOSHI ="btn.do.sashimodoshi";
	public static final String BTN_TEMPUSENTAKU    ="btn.tempusentaku";
	public static final String BTN_APPROVE         ="btn.approve";
	public static final String BTN_SEARCH          ="btn.search";
	public static final String BTN_CONFIRMATION    ="btn.confirmation";
	public static final String BTN_CONFIRM         ="btn.confirm";
	public static final String BTN_OB2103          ="btn.OB2103";
	public static final String BTN_TRANSFER        ="btn.transfer";
	public static final String BTN_TRANSFERMATION  ="btn.transfermation";
	public static final String BTN_ANNULMENT       ="btn.annulment";
	public static final String BTN_ADD             ="btn.add";
	public static final String BTN_ADDSTOP         ="btn.addstop";
	public static final String BTN_DELETE          ="btn.delete";
	public static final String BTN_TAKE_ALL        ="btn.take_all";
	public static final String BTN_NEXT_SCREEN     ="btn.next_screen";
	public static final String BTN_TOP             ="btn.top";
	public static final String BTN_RESERVATION     ="btn.reservation";
	public static final String BTN_CL              ="btn.cl";
	public static final String BTN_MENU            ="btn.menu";
	public static final String BTN_LIST_DOWNLOAD   ="btn.list_download";
    public static final String BTN_UPLOAD_ALL      ="btn.upload_all";
    public static final String BTN_DAIKOSETEI      ="btn.daikosetei";
    public static final String BTN_SATEI_KEKKA     ="btn.satei_kekka";
    public static final String BTN_SHINKI          ="btn.shinki";
    public static final String BTN_SATEI_KEKA      ="btn.satei_keka";
    public static final String BTN_TENPU_SENTAKU   ="btn.tenpu_sentaku";
    public static final String BTN_TENPU_SANSYO    ="btn.tenpu_sansyo";
    public static final String BTN_TORIMODOSHI     ="btn.torimodoshi";
    public static final String BTN_SANSYO          ="btn.sansyo";
	public static final String BTN_SEND				="btn.send";
	public static final String BTN_MAILSEND			="btn.mailsend";
	public static final String BTN_HANEI			="btn.hanei";
	public static final String BTN_SOUSHINSAKI		="btn.soushinsaki";
	public static final String BTN_TANTOSOSHIKI		="btn.tantososhiki";
	public static final String BTN_HANEI2			="btn.hanei2";
	
	/************************************************************************************
	 * コンテンツタイトル
	 ***********************************************************************************/
	public static final String TITLE_OB1101                       ="title.OB1101";
	public static final String TITLE_OB1102                       ="title.OB1102";
	public static final String TITLE_OB1103                       ="title.OB1103";
	public static final String TITLE_OB1104                       ="title.OB1104";
	public static final String TITLE_OB1105                       ="title.OB1105";
	public static final String TITLE_OB2101                       ="title.OB2101";
	public static final String TITLE_OB2102                       ="title.OB2102";
	public static final String TITLE_OB2103                       ="title.OB2103";
	public static final String TITLE_OB2104                       ="title.OB2104";
	public static final String TITLE_OB2105                       ="title.OB2105";
	public static final String TITLE_OC1101                       ="title.OC1101";
	public static final String TITLE_OC1102A                      ="title.OC1102A";
	public static final String TITLE_OC1102B                      ="title.OC1102B";
	public static final String TITLE_OC1102C                      ="title.OC1102C";
	public static final String TITLE_OC1103A                      ="title.OC1103A";
	public static final String TITLE_OC1103B                      ="title.OC1103B";
	public static final String TITLE_OC1103C                      ="title.OC1103C";
	public static final String TITLE_OC1104A                      ="title.OC1104A";
	public static final String TITLE_OC1104B                      ="title.OC1104B";
	public static final String TITLE_OC1104C                      ="title.OC1104C";
	public static final String TITLE_OC1105A                      ="title.OC1105A";
	public static final String TITLE_OC1105B                      ="title.OC1105B";
	public static final String TITLE_OC1105C                      ="title.OC1105C";
	public static final String TITLE_OC1106                       ="title.OC1106";
	public static final String TITLE_OC1107A                      ="title.OC1107A";
	public static final String TITLE_OC1107B                      ="title.OC1107B";
	public static final String TITLE_OC1107C                      ="title.OC1107C";
	public static final String TITLE_HIKIATEKENSYO_ICHIRAN        ="title.hikiatekensyo_ichiran";
	public static final String TITLE_HIKIATEKENSYO                ="title.hikiatekensyo";
	public static final String TITLE_HIKIATEKENSYO_SYONIN_ICHIRAN ="title.hikiatekensyo_syonin_ichiran";
	public static final String TITLE_HIKIATEKENSYO_SYONIN         ="title.hikiatekensyo_syonin";
	public static final String TITLE_OD1101                       ="title.OD1101";
	public static final String TITLE_OD1102                       ="title.OD1102";
	public static final String TITLE_OD1103                       ="title.OD1103";
	public static final String TITLE_OD1104                       ="title.OD1104";
	public static final String TITLE_OZ1101                       ="title.OZ1101";
	public static final String TITLE_OZ1102                       ="title.OZ1102";
	public static final String TITLE_OZ2101                       ="title.OZ2101";
	public static final String TITLE_OZ3101                       ="title.OZ3101";
	public static final String TITLE_OZ4101                       ="title.OZ4101";
	public static final String TITLE_OZ5101                       ="title.OZ5101";
	public static final String TITLE_OZ6101                       ="title.OZ6101";
	public static final String TITLE_OZ6102                       ="title.OZ6102";
	public static final String TITLE_OZ6103                       ="title.OZ6103";
	public static final String TITLE_OZ6104                       ="title.OZ6104";
	public static final String TITLE_OZ6105                       ="title.OZ6105";
	public static final String TITLE_OZ6106                       ="title.OZ6106";
	public static final String TITLE_OZ6107                       ="title.OZ6107";
	public static final String TITLE_OZ6108                       ="title.OZ6108";
	public static final String TITLE_HIKIATEKENSYO_TAB            ="title.hikiatekensyo_tab";
	public static final String TITLE_OS3101                       ="title.OS3101";
	public static final String TITLE_OS3102                       ="title.OS3102";
	public static final String TITLE_OS3103                       ="title.OS3103";
	public static final String TITLE_OS3104                       ="title.OS3104";
	public static final String TITLE_OS3105                       ="title.OS3105";
	public static final String TITLE_OS4101                       ="title.OS4101";
	public static final String TITLE_OS6101                       ="title.OS6101";
	public static final String TITLE_OS6102                       ="title.OS6102";
	public static final String TITLE_OS6103                       ="title.OS6103";
	public static final String TITLE_OS6104                       ="title.OS6104";
	public static final String TITLE_OS6105                       ="title.OS6105";
	public static final String TITLE_OS7101                       ="title.OS7101";
	public static final String TITLE_OS7102                       ="title.OS7102";
	public static final String TITLE_OS7103                       ="title.OS7103";
	public static final String TITLE_OS7104                       ="title.OS7104";
	public static final String TITLE_OS7105                       ="title.OS7105";
	public static final String TITLE_OS7106                       ="title.OS7106";
	public static final String TITLE_OS7107                       ="title.OS7107";
	public static final String TITLE_OS7108                       ="title.OS7108";
	public static final String TITLE_OS7109                       ="title.OS7109";
	public static final String TITLE_OS7110                       ="title.OS7110";
	public static final String TITLE_OS7111                       ="title.OS7111";
	public static final String TITLE_OS7115                       ="title.OS7115";
	public static final String TITLE_OS7116                       ="title.OS7116";
	public static final String TITLE_OS8101                       ="title.OS8101";
	public static final String TITLE_CHAMPION                     ="title.champion";
	public static final String TITLE_TYUSYUTUJOKEN                ="title.tyusyutujoken";
	public static final String TITLE_RENKETU                      ="title.renketu";

	/************************************************************************************
	 * コンテンツ
	 ***********************************************************************************/

	// 共通系
	public static final String COMMON_MYTASKS              ="Common.mytasks";
	public static final String COMMON_UNPROCESSED          ="Common.unprocessed";
	public static final String COMMON_PROCESSING           ="Common.processing";
	public static final String COMMON_WATING_APPROVAL      ="Common.waiting_approval";
	public static final String COMMON_COMPLETE             ="Common.complete";
	public static final String COMMON_ASSESSING_PERIOD     ="Common.assessing_period";
	public static final String COMMON_SORT                 ="Common.sort";
	public static final String COMMON_SHOW                 ="Common.show";
	public static final String COMMON_SLASH                ="Common.slash";
	public static final String COMMON_DATA                 ="Common.data";
	public static final String COMMON_1                    ="Common.1";
	public static final String COMMON_2                    ="Common.2";
	public static final String COMMON_3                    ="Common.3";
	public static final String COMMON_4                    ="Common.4";
	public static final String COMMON_5                    ="Common.5";
	public static final String COMMON_6                    ="Common.6";
	public static final String COMMON_PERCENT              ="Common.percent";
	public static final String COMMON_COLON                ="Common.colon";
	public static final String COMMON_ZENPOUICCHI          ="Common.zenpouicchi";
	public static final String COMMON_BUBUNICCHI           ="Common.bubunicchi";
	public static final String COMMON_APPROVE_ALL          ="Common.approve_all";
	public static final String COMMON_APPROVE              ="Common.approve";
	public static final String COMMON_DOT                  ="Common.dot";
	public static final String COMMON_KAKKO                ="Common.kakko";
	public static final String COMMON_KAKKO_TOJI           ="Common.kakko_toji";
	public static final String COMMON_TEN                  ="Common.ten";
	public static final String COMMON_SYOUKAKKO            ="Common.syoukakko";
	public static final String COMMON_SYOUKAKKO_TOJI       ="Common.syoukakko_toji";
	public static final String COMMON_HAIHUN               ="Common.haihun";
	//課題No.119
	//追加開始
    public static final String COMMON_KOMEJIRUSI   = "Common.komejirusi";
	//追加削除

	// OB1101_実質滞留債権判定 対象先一覧
	public static final String OB1101_TAIRYUHANTEI         ="OB1101.tairyuhantei";
	public static final String OB1101_TAIRYUHANTEI_K       ="OB1101.tairyuhantei_k";
	public static final String OB1101_KANJO_CD             ="OB1101.kanjo_cd";
	public static final String OB1101_KANJO_NM             ="OB1101.kanjo_nm";
	public static final String OB1101_COUNTRY              ="OB1101.country";
	public static final String OB1101_KINGAKU_TOTAL        ="OB1101.kingaku_total";
	public static final String OB1101_TAISYO_YM            ="OB1101.taisyo_ym";
	public static final String OB1101_SOSHIKI              ="OB1101.soshiki";
	public static final String OB1101_TANTO_NM             ="OB1101.tanto_nm";
	public static final String OB1101_PROGRESS             ="OB1101.progress";

    // OB1102_滞留判定登録 明細一覧
    public static final String OB1102_SOSIKI               ="OB1102.sosiki";
    public static final String OB1102_KANJYOSAKICD         ="OB1102.kanjyosakicd";
    public static final String OB1102_KANJYOSAKINAME       ="OB1102.kanjyosakiname";
    public static final String OB1102_SHINYOKAKUDUKE       ="OB1102.shinyokakuduke";
    public static final String OB1102_SHONINTANTOSHA       ="OB1102.shonintantosha";
    public static final String OB1102_EDABAN               ="OB1102.edaban";
    public static final String OB1102_OFFICE               ="OB1102.office";
    public static final String OB1102_CELL                 ="OB1102.cell";
    public static final String OB1102_KANJYOKAMOKU         ="OB1102.kanjyokamoku";
    public static final String OB1102_SYUSIYOTEIBI         ="OB1102.syusiyoteibi";
    public static final String OB1102_MANKIBI              ="OB1102.mankibi";
    public static final String OB1102_KANJYOSHORIBI        ="OB1102.kanjyoshoribi";
    public static final String OB1102_KEIYAKUDENPYONO      ="OB1102.keiyakudenpyono";
    public static final String OB1102_INVOICENO            ="OB1102.invoiceno";
    public static final String OB1102_KINGAKUKEI           ="OB1102.kingakukei";
    public static final String OB1102_TAIRYUHANTEI         ="OB1102.tairyuhantei";
    public static final String OB1102_SHOSAI               ="OB1102.shosai";

    // OB1103_滞留判定登録 明細詳細
    public static final String OB1103_KANJYOSAKICD         ="OB1103.kanjyosakicd";
    public static final String OB1103_KANJYOSAKINAME       ="OB1103.kanjyosakiname";
    public static final String OB1103_SHINYOKAKUDUKE       ="OB1103.shinyokakuduke";
    public static final String OB1103_EDABAN               ="OB1103.edaban";
    public static final String OB1103_OFFICE               ="OB1103.office";
    public static final String OB1103_CELL                 ="OB1103.cell";
    public static final String OB1103_KANJYOKAMOKU         ="OB1103.kanjyokamoku";
    public static final String OB1103_SYUSIYOTEIBI         ="OB1103.syusiyoteibi";
    public static final String OB1103_MANKIBI              ="OB1103.mankibi";
    public static final String OB1103_KANJYOSHORIBI        ="OB1103.kanjyoshoribi";
    public static final String OB1103_KEIYAKUDENPYONO      ="OB1103.keiyakudenpyono";
    public static final String OB1103_KINGAKUKEI           ="OB1103.kingakukei";
    public static final String OB1103_TAIRYUKUBUN          ="OB1103.tairyukubun";
    public static final String OB1103_TAIRYUHANTEI         ="OB1103.tairyuhantei";
    public static final String OB1103_HANTEIJIYU_KOKUNAI   ="OB1103.hanteijiyu_kokunai";
    public static final String OB1103_HANTEIJIYU_KAIGAI    ="OB1103.hanteijiyu_kaigai";
    public static final String OB1103_MSGHANTEIJIYU        ="OB1103.msghanteijiyu";
    public static final String OB1103_INVOICENO            ="OB1103.invoiceno";
    public static final String OB1103_ITEM1                ="OB1103.item1";
    public static final String OB1103_ITEM2                ="OB1103.item2";
    public static final String OB1103_ITEM3                ="OB1103.item3";
    public static final String OB1103_ITEM4                ="OB1103.item4";
    public static final String OB1103_ITEM5                ="OB1103.item5";
    public static final String OB1103_TENPUKAIJYO          ="OB1103.tenpukaijyo";

	// OB1104_実質滞留債権判定_承認一覧
	public static final String OB1104_KANJO_CD             ="OB1104.kanjo_cd";				// 勘定先CD
	public static final String OB1104_KANJO_NM             ="OB1104.kanjo_nm";				// 勘定先名称
	public static final String OB1104_TENPU                ="OB1104.tenpu";					// 添付
	public static final String OB1104_KINGAKU_TOTAL        ="OB1104.kingaku_total";			// 金額計
	public static final String OB1104_TAISYO_YM            ="OB1104.taisyo_ym";				// 対象年月
	public static final String OB1104_SOSHIKI              ="OB1104.soshiki";					// 組織
	public static final String OB1104_PHASE                ="OB1104.phase";					// フェーズ
	public static final String OB1104_TANTO_NM             ="OB1104.tanto_nm";				// 担当者
	public static final String OB1104_SYONIN               ="OB1104.syonin";					// 承認
	public static final String OB1104_IKKATUSYONIN         ="OB1104.Ikkatusyonin";			// 一括承認

    // OB1105_実質滞留債権判定_承認
    public static final String OB1105_LBLNM1               ="OB1105.lblnm1";                	// 汎用１
    public static final String OB1105_SOSHIKI              ="OB1105.soshiki";               	// 組織
    public static final String OB1105_KANJO_CD             ="OB1105.kanjo_cd";              	// 勘定先CD
    public static final String OB1105_KANJO_NM             ="OB1105.kanjo_nm";              	// 勘定先名称
    public static final String OB1105_SHINYOUKAKUDUKE      ="OB1105.Shinyoukakuduke";       	// 信用格付

	// OB2101_対象先選定 選定実行
	public static final String OB2101_KANJO_CD             ="OB2101.kanjo_cd";
	public static final String OB2101_KANJO_NM             ="OB2101.kanjo_nm";
	public static final String OB2101_TAISYO_YM            ="OB2101.taisyo_ym";
	public static final String OB2101_TAIRYU_KINGAKU       ="OB2101.tairyu_kingaku";
	public static final String OB2101_SAIKEN_KINGAKU       ="OB2101.saiken_kingaku";
	public static final String OB2101_SOSHIKI              ="OB2101.soshiki";
	public static final String OB2101_TANTO_NM             ="OB2101.tanto_nm";
	public static final String OB2101_REASON               ="OB2101.reason";

	// OB2102_対象先選定 選定詳細
	public static final String OB2102_KANJO_CD             ="OB2102.kanjo_cd";
	public static final String OB2102_KANJO_NM             ="OB2102.kanjo_nm";
	public static final String OB2102_SOSHIKI              ="OB2102.soshiki";
	public static final String OB2102_SENTEI_KBN           ="OB2102.sentei_kbn";
	public static final String OB2102_ANNULMENT_TYPE       ="OB2102.annulment_type";
	public static final String OB2102_REASON               ="OB2102.reason";
	public static final String OB2102_SYONIN_TANTO         ="OB2102.syonin_tanto";
	public static final String OB2102_COMMENT              ="OB2102.comment";

	// OB2103_対象先選定 追加対象先選択
	public static final String OB2103_KANJO_CD             ="OB2103.kanjo_cd";
	public static final String OB2103_KANJO_NM             ="OB2103.kanjo_nm";
	public static final String OB2103_TAISYO_YM            ="OB2103.taisyo_ym";
	public static final String OB2103_KTK                  ="OB2103.ktk";
	public static final String OB2103_SAIKEN_KINGAKU       ="OB2103.saiken_kingaku";

	// OB2104_対象先選定 承認一覧
	public static final String OB2104_KANJO_CD             ="OB2104.kanjo_cd";
	public static final String OB2104_KANJO_NM             ="OB2104.kanjo_nm";
	public static final String OB2104_SAIKEN_KINGAKU       ="OB2104.saiken_kingaku";
	public static final String OB2104_REASON               ="OB2104.reason";
	public static final String OB2104_SOSHIKI              ="OB2104.soshiki";
	public static final String OB2104_TANTO_NM             ="OB2104.tanto_nm";
	public static final String OB2104_SENTEI_KBN           ="OB2104.sentei_kbn";

	// OB2105_対象先選定 仮基準査定選択
	public static final String OB2105_KANJO_CD             ="OB2105.kanjo_cd";
	public static final String OB2105_KANJO_NM             ="OB2105.kanjo_nm";
	public static final String OB2105_TAISYO_YM            ="OB2105.taisyo_ym";
	public static final String OB2105_KTK                  ="OB2105.ktk";
	public static final String OB2105_SAIKEN_KINGAKU       ="OB2105.saiken_kingaku";

	// OC1102_査定_取引先概要 OZ6102_取引先概要照会タブ
	public static final String OC1102_JIYU                 ="OC1102.jiyu";
	public static final String OC1102_KANJO                ="OC1102.kanjo";
	public static final String OC1102_KANJO_NM             ="OC1102.kanjo_nm";
	public static final String OC1102_DUNS_NO              ="OC1102.duns_no";
	public static final String OC1102_SYOZAI_COUNTRY       ="OC1102.syozai_country";
	public static final String OC1102_SYOZAI_CHI           ="OC1102.syozai_chi";
	public static final String OC1102_GYOSYU               ="OC1102.gyosyu";
	public static final String OC1102_JIGYO                ="OC1102.jigyo";
	public static final String OC1102_KABUNUSHI_KOUSEI     ="OC1102.kabunushi_kousei";
	public static final String OC1102_KABUNUSHI_NM         ="OC1102.kabunushi_nm";
	public static final String OC1102_HOYUU_KABUSUU        ="OC1102.hoyuu_kabusuu";
	public static final String OC1102_HOYUU_RITU           ="OC1102.hoyuu_ritu";
	public static final String OC1102_ZAIMU_GAIYO          ="OC1102.zaimu_gaiyo";
	public static final String OC1102_REASON               ="OC1102.reason";
	public static final String OC1102_TUUKA                ="OC1102.tuuka";
	public static final String OC1102_TANTAI               ="OC1102.tantai";
	public static final String OC1102_RENKETU              ="OC1102_renketu";
	public static final String OC1102_URIAGE               ="OC1102.uriage";
	public static final String OC1102_URIAGE_TOTAL         ="OC1102.uriage_total";
	public static final String OC1102_HANBAI_KANRI         ="OC1102.hanbai_kanri";
	public static final String OC1102_EIGYO_RIEKI          ="OC1102.eigyo_rieki";
	public static final String OC1102_KEIJO_RIEKI          ="OC1102.keijo_rieki";
	public static final String OC1102_TOKIBETU_RIEKI       ="OC1102.tokibetu_rieki";
	public static final String OC1102_TOKIBETU_SON         ="OC1102.tokibetu_son";
	public static final String OC1102_TOKI_JUN_RIEKI       ="OC1102.toki_jun_rieki";
	public static final String OC1102_HAITO                ="OC1102.haito";
	public static final String OC1102_GENKA_SYOKYAKU       ="OC1102.genka_syokyaku";
	public static final String OC1102_EIGYO_CF             ="OC1102.eigyo_CF";
	public static final String OC1102_RYUDO_SHISAN         ="OC1102.ryudo_shisan";
	public static final String OC1102_KOTEI_SHISAN         ="OC1102.kotei_shisan";
	public static final String OC1102_SHISAN_KEI           ="OC1102.shisan_kei";
	public static final String OC1102_RYUDO_HUSAI          ="OC1102.ryudo_husai";
	public static final String OC1102_KOTEI_HUSAI          ="OC1102.kotei_husai";
	public static final String OC1102_HUSAI_KEI            ="OC1102.husai_kei";
	public static final String OC1102_SHIHONKIN            ="OC1102.shihonkin";
	public static final String OC1102_NAIBU_RYUHO          ="OC1102.naibu_ryuho";
	public static final String OC1102_JIKO_SHIHON          ="OC1102.jiko_shihon";
	public static final String OC1102_HYOUJI_TANI          ="OC1102.hyouji_tani";
	public static final String OC1102_THOUSAND             ="OC1102.thousand";
	public static final String OC1102_FITCH                ="OC1102.fitch ";
	public static final String OC1102_FSS                  ="OC1102.fss";
	public static final String OC1102_DUNS_RATING          ="OC1102.duns_rating";
	public static final String OC1102_KESAN_GAIKYO         ="OC1102.kesan_gaikyo";

	//OZ6107_債務明細タブ
	public static final String OZ6107_SOSHIKI             ="OZ6107.soshiki";				// 組織
	public static final String OZ6107_KANJO_KAMOKU_CD     ="OZ6107.kanjo_kamoku_cd";		// 勘定先CD
	public static final String OZ6107_KANJO_KAMOKU_NM     ="OZ6107.kanjo_kamoku_nm";		// 勘定先名称
	public static final String OZ6107_KINGAKU_TOTAL       ="OZ6107.kingaku_total";		// 金額計
	public static final String OZ6107_SHUSI_DT            ="OZ6107.shusi_dt";				// 収支予定美
	public static final String OZ6107_KANJO_DT            ="OZ6107.kanjo_dt";				// 勘定処理日
	public static final String OZ6107_KEIYAKU_DENPYO_NO   ="OZ6107.keiyaku_denpyo_no";	// 契約伝票No
	public static final String OZ6107_BIKO                ="OZ6107.biko";					// 備考
	public static final String OZ6107_RYUHOSAIMU          ="OZ6107.ryuhosaimu";			// 留保債務
	public static final String OZ6107_SAIMU_KEI           ="OZ6107.saimu_kei";			// 債務総計
	public static final String OZ6107_RYUHO_SAIMU_KEI     ="OZ6107.ryuho_saimu_kei";		// 留保債務計

	// OC1103_査定_取引先区分判定・OZ6103_取引先区分判定照会タブ
	public static final String OC1103_KANJO                  	="OC1103.kanjo";
	public static final String OC1103_KANJO_NM              	="OC1103.kanjo_nm";
	public static final String OC1103_SYOZAIKOKU             	="OC1103.syozaikoku";
	public static final String OC1103_DUNS_NO              	="OC1103.duns_no";
	public static final String OC1103_SYOZAITI               	="OC1103.syozaiti";
	public static final String OC1103_KTK                  	="OC1103.ktk";						// 信用格付
	public static final String OC1103_OYA_KAISHA				="OC1103.oya_kaisha";
	public static final String OC1103_OYA_ITTAI_HANDAN     	="OC1103.oya_ittai_handan";			// (親会社一体判断)
	public static final String OC1103_TAIRYU_KBN           	="OC1103.tairyu_kbn";				// 滞留区分
	public static final String OC1103_TORIHIKISAKI_KBN     	="OC1103.torihikisaki_kbn";			// 取引先区分
	public static final String OC1103_SEIJOSAKI           	="OC1103.seijosaki";				// 正常先
	public static final String OC1103_YOCHUISAKI           	="OC1103.yochuisaki";				// 要注意先
	public static final String OC1103_SEIJOSAKI_YOCHUISAKI	="OC1103.seijosaki_yochuisaki";		// 正常先・要注意先
	public static final String OC1103_KASHITAORE           	="OC1103.kashitaore";				// 貸倒懸念先
	public static final String OC1103_GAITO_JIYU           	="OC1103.gaito_jiyu";				// 該当事由
	public static final String OC1103_HASAN_KOSEI          	="OC1103.hasan_kosei";				// 破産構成先
	public static final String OC1103_HANTEI_KONKYO        	="OC1103.hantei_konkyo";			// 判定根拠
	public static final String OC1103_SAIKEN_KBN           	="OC1103.saiken_kbn";				// 債権区分
	public static final String OC1103_SAIKEN_KBN_HANTEI		="OC1103.saiken_kbn_hantei";		// 債権区分判定
	public static final String OC1103_TORI_KBN_HANTEI			="OC1103.tori_kbn_hantei";				// 取引先区分判定
	public static final String OC1103_HANTEI_JIYU          	="OC1103.hantei_jiyu";				// 判定事由
	public static final String OC1103_HASSEI_KEII          	="OC1103.hassei_keii";				// 発生経緯
	public static final String OC1103_KASHITAORE_HANTEI      	="OC1103.kashitaore_hantei";		// 貸倒懸念先判定
	public static final String OC1103_HASANKOUSEI_HANTEI		="OC1103.hasankousei_hantei";		// 破産更生先判定
	public static final String OC1103_EXP						="OC1103.exp";						// 該当事由にチェックしてください(複数選択可)
	public static final String OC1103_SEIJO_EXP				="OC1103.seijo_exp";				// 業績が良好であり、かつ、財務内容にも特段問題無いと認められる取引先
	public static final String OC1103_YOCHUI_EXP				="OC1103.yochui_exp";				// 業況が低調ないし不安定な取引先または財務内容に問題がある取引先など今後の管理に厳重注意が必要な取引先
	public static final String OC1103_KASHITAORE_EXP			="OC1103.kashitaore_exp";			// 経営破綻の状態には至っていないが、債務の弁済に重大な問題が生じているか又は生じる可能性が高い取引先
	public static final String OC1103_KASHITAORE_EXP1			="OC1103.kashitaore_exp1";			// 業況が低調ないし不安定、債務超過若しくは実質的に債務超過の状態にあり、過去の経営成績又は経営改善計画の実現性を考慮しても債務の一部を条件通りに弁済できない可能性が高い
	public static final String OC1103_KASHITAORE_EXP2			="OC1103.kashitaore_exp2";			// 取引先に対して弁済期間の延長、弁済の一時棚上げ、元金又は利息の一部免除を行っている等、弁済条件の大幅な緩和を行っている
	public static final String OC1103_KASHITAORE_EXP3			="OC1103.kashitaore_exp3";			// 債務の弁済がおおむね1年以上延滞している
	public static final String OC1103_HASAN_KOSEI_EXP			="OC1103.hasan_kosei_exp";			// 経営破綻又は実質的に経営破綻に陥っている取引先
	public static final String OC1103_HASAN_KOSEI_EXP1		="OC1103.hasan_kosei_exp1";			// 破産法による破産の申立て
	public static final String OC1103_HASAN_KOSEI_EXP2		="OC1103.hasan_kosei_exp2";			// 会社法の規定による整理開始又は特別清算開始の申立て
	public static final String OC1103_HASAN_KOSEI_EXP3		="OC1103.hasan_kosei_exp3";			// 会社更生法による更生手続開始の申立て
	public static final String OC1103_HASAN_KOSEI_EXP4		="OC1103.hasan_kosei_exp4";			// 民事再生法による民事再生手続き開始の申立て
	public static final String OC1103_HASAN_KOSEI_EXP5		="OC1103.hasan_kosei_exp5";			// 手形交換所による取引停止処分
	public static final String OC1103_HASAN_KOSEI_EXP6		="OC1103.hasan_kosei_exp6";			// その他の理由

	// OC1101_査定 対象先一覧
	public static final String OC1101_MOGITORI				="OC1101.mogitori";
	public static final String OC1101_ITIJISATEI				="OC1101.itijisatei";
	public static final String OC1101_ITIJISATEI_K			="OC1101.itijisatei_k";
	public static final String OC1101_NIJISATEI				="OC1101.nijisatei_k";
	public static final String OC1101_KANJO_CD				="OC1101.kanjo_cd";
	public static final String OC1101_KANJO_NM				="OC1101.kanjo_nm";
	public static final String OC1101_JIYU					="OC1101.jiyu";
	public static final String OC1101_KINGAKU					="OC1101.kingaku";
	public static final String OC1101_TAISYO_YM				="OC1101.taisyo_ym";
	public static final String OC1101_SOSHIKI					="OC1101.soshiki";
	public static final String OC1101_TANTO_NM				="OC1101.tanto_nm";
	public static final String OC1101_PROGRESS				="OC1101.progress";

	// OC1104_査定 引当金判定　OZ6104 引当金判定タブ OZ6108 基本情報照会タブ
	public static final String OC1104_KANJO                  	 ="OC1104.kanjo";
	public static final String OC1104_KANJO_NM              	 ="OC1104.kanjo_nm";
	public static final String OC1104_SHONINSHA		         ="OC1104.shoninsha";
	public static final String OC1104_HIKIATEHANTEI            ="OC1104.hikiatehantei";
	public static final String OC1104_KANJO_CD                 ="OC1104.kanjo_cd";
	public static final String OC1104_TORIHIKISAKI_NM          ="OC1104.torihikisaki_nm";
	public static final String OC1104_KANJOKAMOKU              ="OC1104.kanjokamoku";
	public static final String OC1104_KINGAKU                  ="OC1104.kingaku";
	public static final String OC1104_HUDOUSAN_TANPO           ="OC1104.hudousan_tanpo";
	public static final String OC1104_DOSAN_TANPO              ="OC1104.dosan_tanpo";
	public static final String OC1104_BOEKI_HOKEN              ="OC1104.boeki_hoken";
	public static final String OC1104_SONOTA                   ="OC1104.sonota";
	public static final String OC1104_KEIYAKU_GAKU             ="OC1104.keiyaku_gaku";
	public static final String OC1104_HYOKA_GAKU               ="OC1104.hyoka_gaku";
	public static final String OC1104_HOZEN                    ="OC1104.hozen";
	public static final String OC1104_ETC_NAIYO                ="OC1104.etc_naiyo";
	public static final String OC1104_ETC_KAISYU_NAIYO         ="OC1104.etc_kaisyu_naiyo";
	public static final String OC1104_RIKO_NAIYOU              ="OC1104.riko_naiyo";
	public static final String OC1104_HIKIATE_NAIYO            ="OC1104.hikiate_naiyo";
	public static final String OC1104_KONGO_MITOSHI            ="OC1104.kongo_mitoshi";
	public static final String OC1104_SIHANKI_FLG              ="OC1104.sihanki_flg";
	public static final String OC1104_FLG_KBN                  ="OC1104.flg_kbn";
	public static final String OC1104_FLG_COMMENT              ="OC1104.flg_comment";
	public static final String OC1104_TASYA_RISUKU             ="OC1104.tasya_risuku";
	public static final String OC1104_TANI                     ="OC1104.tani";
	public static final String OC1104_UKETORI_TEGATA           ="OC1104.uketori_tegata";
	public static final String OC1104_YUSHUTU_UKETORI_TEGATA   ="OC1104.yushutu_uketori_tegata";
	public static final String OC1104_URIKAKE_KIN              ="OC1104.urikake_kin";
	public static final String OC1104_TORIHIKI_MAEWATASHI_KIN  ="OC1104.torihiki_maewatashi_kin";
	public static final String OC1104_TATEKAE_KIN              ="OC1104.tatekae_kin";
	public static final String OC1104_MISHUUNYUU_KIN           ="OC1104.mishuunyuu_kin";
	public static final String OC1104_MISHUU_SHUUEKI           ="OC1104.mishuu_shuueki";
	public static final String OC1104_TANKI_KASHITUKE_KIN      ="OC1104.tanki_kashituke_kin";
	public static final String OC1104_SASHIIRE_HOSHOU_KIN      ="OC1104.sashiire_hoshou_kin";
	public static final String OC1104_KARIBARAI_KIN            ="OC1104.karibarai_kin";
	public static final String OC1104_CHOUKI_KASHITUKE_KIN     ="OC1104.chouki_kashituke_kin";
	public static final String OC1104_SONOTA_TOUSHI            ="OC1104.sonota_toushi";
	public static final String OC1104_IPPAN_SAIKEN_KEI         ="OC1104.ippan_saiken_kei";
	public static final String OC1104_SAIKEN_ZANDAKA_GOUKEI    ="OC1104.saiken_zandaka_goukei";
	public static final String OC1104_SAIKEN_ZANDAKA_GOUKEI_1  ="OC1104.saiken_zandaka_goukei_1";
    public static final String OC1104_RYUUHO_SAIMU             ="OC1104.ryuuho_saimu";
    public static final String OC1104_NO3_RYUUHO_SAIMU         ="OC1104.no3_ryuuho_saimu";
    public static final String OC1104_NO3_RYUUHO_SAIMU_UTIWAKE ="OC1104.no3_ryuuho_saimu_utiwake";
    public static final String OC1104_RYUUHO_SAIMU_KEI_2       ="OC1104.ryuuho_saimu_kei_2";
    public static final String OC1104_HOZEN_3                  ="OC1104.hozen_3";
    public static final String OC1104_SONOTA_KAISHUU_4         ="OC1104.sonota_kaishuu_4";
	public static final String OC1104_HOSHOU_SAIMU_GOUKEI      ="OC1104.hoshou_saimu_goukei";
    public static final String OC1104_RIKOU_SEIKYUU_KENEN_5    ="OC1104.rikou_seikyuu_kenen_5";
	public static final String OC1104_KI_HIKIATE_KIN           ="OC1104.ki_hikiate_kin";
    public static final String OC1104_KI_HIKIATE_KIN_6         ="OC1104.ki_hikiate_kin_6";
	public static final String OC1104_HIKIATE_TAISHOU_KINGAKU  ="OC1104.hikiate_taishou_kingaku";
	public static final String OC1104_HIKIATE_BANGO            ="OC1104.hikiate_bango";
    public static final String OC1104_TUIKA_HIKIATE_KIN        ="OC1104.tuika_hikiate_kin";
    public static final String OC1104_TUUKA_CHOUSEI            ="OC1104.tuuka_chousei";
    public static final String OC1104_TUIKA_HIKIATE_KIN_CHOUSEIGO ="OC1104.tuika_hikiate_kin_chouseigo";
    //エラーメッセージ使用のため
    public static final String OC1104_HANYO1_NAI					="OC1104.hanyo1_nai";
    public static final String OC1104_HANYO1_GAI					="OC1104.hanyo1_gai";

	//OC1105_査定_留保債務登録
	public static final String OC1105_TORI_CD			    ="OC1105.tori_cd";				//取引先
	public static final String OC1105_TORI_NM			    ="OC1105.tori_nm";				//取引先名
	public static final String OC1105_SAIMUSOUKEI			="OC1105.saimusoukei";			//債務総計
	public static final String OC1105_RYUHOSAIMUKEI		="OC1105.ryuhosaimukei";		//留保債務計
	public static final String OC1105_IKKATUHANTEI		="OC1105.ikkatuhantei";			//一括判定
	public static final String OC1105_KANJO_NM			="OC1105.kanjo_nm";				//勘定先名称
	public static final String OC1105_SOSHIKI				="OC1105.soshiki";				//組織
	public static final String OC1105_KANJO_KAMOKU		="OC1105.kanjo_kamoku";			//勘定科目
	public static final String OC1105_SHUSI_DT			="OC1105.shusi_dt";				//収支予定日
	public static final String OC1105_KINGAKU_TOTAL		="OC1105.kingaku_total";		//金額計
	public static final String OC1105_KEIYAKU_NO			="OC1105.keiyaku_no";			//契約No
	public static final String OC1105_HANTEI				="OC1105.hantei";				//判定
	public static final String OC1105_BIKOU				="OC1105.bikou";				//備考

    // OC1106_査定 承認一覧
	public static final String OC1106_SYONIN               ="OC1106.syonin";					// 承認
	public static final String OC1106_KANJO_CD             ="OC1106.kanjo_cd";				// 勘定先CD
	public static final String OC1106_KANJO_NM             ="OC1106.kanjo_nm";				// 勘定先名称
	public static final String OC1106_KINGAKU_TOTAL        ="OC1106.kingaku";					// 金額計
	public static final String OC1106_TAISYO_YM            ="OC1106.taisyo_ym";				// 対象年月
	public static final String OC1106_SOSHIKI              ="OC1106.soshiki";					// 組織
	public static final String OC1106_PHASE                ="OC1106.phase";					// フェーズ
	public static final String OC1106_TANTO_NM             ="OC1106.tanto_nm";				// 担当者
	public static final String OC1106_IKKATUSYONIN         ="OC1106.Ikkatusyonin";			// 一括承認

    // OC1107_査定 承認
	public static final String OC1107_SOSHIKI               ="OC1107.soshiki";				// 組織
	public static final String OC1107_KANJO_CD               ="OC1107.kanjo_cd";				// 勘定先CD
	public static final String OC1107_KANJO_NM               ="OC1107.kanjo_nm";				// 勘定先名称

	// OD1101_引当金確認 対象先一覧
    public static final String OD1101_SATEI1               ="OD1101.satei1";
    public static final String OD1101_ICHIJISATEIKENSHOU   ="OD1101.ichijisateikenshou";
    public static final String OD1101_SATEI2               ="OD1101.satei2";
    public static final String OD1101_HIKIATEKAKUNIN       ="OD1101.hikiatekakunin";
    public static final String OD1101_KANJO_CD             ="OD1101.kanjo_cd";
    public static final String OD1101_KANJO_NM             ="OD1101.kanjo_nm";
    public static final String OD1101_SAIKENZANKEI         ="OD1101.saikenzankei";
    public static final String OD1101_KIJUN_YM             ="OD1101.kijun_ym";
    public static final String OD1101_SOSHIKI              ="OD1101.soshiki";
    public static final String OD1101_TANTO_NM             ="OD1101.tanto_nm";
    public static final String OD1101_PROGRESS             ="OD1101.progress";
    public static final String OD1101_MOGITORI             ="OD1101.mogitori";
    public static final String OD1101_IKKATU               ="OD1101.ikkatu";

	// OD1102_引当金確認
    // 取引先
    public static final String OD1102_TORI_CD                                 = "OD1102.tori_cd";
    // 取引先名
    public static final String OD1102_TORI_NM                                 = "OD1102.tori_nm";
    // 承認担当者
    public static final String OD1102_SHOUNIN_TANTOUSHA                       = "OD1102.shounin_tantousha";
    // 単位
    public static final String OD1102_TANI                                    = "OD1102.tani";
    // 【前期(半期決算前)】
    public static final String OD1102_TITLE_ZENKI                             = "OD1102.title_zenki";
    // 【仮基準】
    public static final String OD1102_TITLE_KIJUN                             = "OD1102.title_kijun";
    // 【今期】
    public static final String OD1102_TITLE_KONKI                             = "OD1102.title_konki";
    // 年月
    public static final String OD1102_YM                                      = "OD1102.ym";
    // 信用格付
    public static final String OD1102_KTK                                     = "OD1102.ktk";
    // 親会社信用格付
    public static final String OD1102_OYA_KTK                                 = "OD1102.oya_ktk";
    // 親会社名称
    public static final String OD1102_COMPANY_NM                              = "OD1102.company_nm";
    // 親会社一体判断
    public static final String OD1102_OYA_FLG                                 = "OD1102.oya_flg";
    // 滞留区分
    public static final String OD1102_TAIRYU_KBN                              = "OD1102.tairyu_kbn";
    // 滞留区分名称
    public static final String OD1102_TAIRYU_KBN_NM                           = "OD1102.tairyu_kbn_nm";
    // 取引先区分
    public static final String OD1102_TORIHIKISAKI_KBN                        = "OD1102.torihikisaki_kbn";
    // 債権区分
    public static final String OD1102_SAIKEN_KBN                              = "OD1102.saiken_kbn";
    // 受取手形
    public static final String OD1102_UKETORI_TEGATA                          = "OD1102.uketori_tegata";
    // 輸出受取手形
    public static final String OD1102_YUSHUTU_UKETORI_TEGATA                  = "OD1102.yushutu_uketori_tegata";
    // 売掛金
    public static final String OD1102_URIKAKE_KIN                             = "OD1102.urikake_kin";
    // 取引前渡金
    public static final String OD1102_TORIHIKI_MAEWATASHIE_KIN                = "OD1102.torihiki_maewatashie_kin";
    // 立替金
    public static final String OD1102_TATEKAE_KIN                             = "OD1102.tatekae_kin";
    // 未収入金
    public static final String OD1102_MISHUUNYUU_KIN                          = "OD1102.mishuunyuu_kin";
    // 未収収益
    public static final String OD1102_MISHUU_SHUUEKI                          = "OD1102.mishuu_shuueki";
    // 短期貸付金
    public static final String OD1102_TANKI_KASHITUKE_KIN                     = "OD1102.tanki_kashituke_kin";
    // 差入保証金
    public static final String OD1102_SASHIIRE_HOSHOU_KIN                     = "OD1102.sashiire_hoshou_kin";
    // 仮払金
    public static final String OD1102_KARIBARAI_KIN                           = "OD1102.karibarai_kin";
    // 長期貸付金
    public static final String OD1102_CHOUKI_KASHITUKE_KIN                    = "OD1102.chouki_kashituke_kin";
    // その他投資
    public static final String OD1102_SONOTA_TOUSHI                           = "OD1102.sonota_toushi";
    // 一般債権計
    public static final String OD1102_IPPAN_SAIKEN_KEI                        = "OD1102.ippan_saiken_kei";
    // 通貨調整
    public static final String OD1102_TUUKA_CHOUSEI                           = "OD1102.tuuka_chousei";
    // 債権残高合計①
    public static final String OD1102_SAIKEN_ZANDAKA_GOUKEI                   = "OD1102.saiken_zandaka_goukei";
    // 留保債務
    public static final String OD1102_RYUUHO_SAIMU                            = "OD1102.ryuuho_saimu";
    // 第三者留保債務
    public static final String OD1102_NO3_RYUUHO_SAIMU                        = "OD1102.no3_ryuuho_saimu";
    // 留保債務計②
    public static final String OD1102_RYUUHO_SAIMU_KEI                        = "OD1102.ryuuho_saimu_kei";
    // 保全③
    public static final String OD1102_HOZEN                                   = "OD1102.hozen";
    // その他回収④
    public static final String OD1102_SONOTA_KAISHUU                          = "OD1102.sonota_kaishuu";
    // 保証債務合計
    public static final String OD1102_HOSHOU_SAIMU_GOUKEI                     = "OD1102.hoshou_saimu_goukei";
    // 履行請求懸念⑤
    public static final String OD1102_RIKOU_SEIKYUU_KENEN                     = "OD1102.rikou_seikyuu_kenen";
    // 既引当金⑥
    public static final String OD1102_KI_HIKIATE_KIN                          = "OD1102.ki_hikiate_kin";
    // 引当対象金額<br>[ ①-(②+③+④)+⑤-⑥ ]
    public static final String OD1102_HIKIATE_TAISHOU_KINGAKU                 = "OD1102.hikiate_taishou_kingaku";
    // 追加引当金
    public static final String OD1102_TUIKA_HIKIATE_KIN                       = "OD1102.tuika_hikiate_kin";
    // 追加引当金(調整後)
    public static final String OD1102_TUIKA_HIKIATE_KIN_CHOUSEI_USIRO         = "OD1102.tuika_hikiate_kin_chousei_usiro";
    // 区分判定根拠<BR>(前期(半期決算前))
    public static final String OD1102_ZENKI_COMMOND                           = "OD1102.zenki_commond";
    // 引当金算定根拠（仮基準）
    public static final String OD1102_KIJUN_COMMOND                           = "OD1102.kijun_commond";
    // 区分判定根拠（今期）
    public static final String OD1102_KONKI_COMMOND                           = "OD1102.konki_commond";

    // 課題No.82 英語版メッセージ対応
    // 追加開始
    // 訓判定根拠 (今期)メッセージ用のラベル
    public static final String OD1102_KONKI_COMMOND_MSG                       = "OD1102.konki_commond_msg";
    // 追加完了

	// 課題No.28
	// 追加開始
    public static final String OD1102_HIKIATEKIN_SHOSAI						="OD1102.hikiatekin_shosai";
	// 追加完了

    // OD1103_引当金確認 承認一覧
    // 一括承認
    public static final String OD1103_IKKATU_SYONIN     ="OD1103.ikkatu_syonin";
    // 勘定先CD
    public static final String OD1103_KANJO_CD          ="OD1103.kanjo_cd";
    // 勘定先名称
    public static final String OD1103_KANJO_NM          ="OD1103.kanjo_nm";
    // 取引先区分
    public static final String OD1103_TORIHIKISAKI_KBN  ="OD1103.torihikisaki_kbn";
    // 債権区分
    public static final String OD1103_SAIKEN_KBN        ="OD1103.saiken_kbn";
    // 承認
    public static final String OD1103_SYONIN            ="OD1103.syonin";
    // 組織
    public static final String OD1103_SOSHIKI           ="OD1103.soshiki";
    // 補正後引当金額
    public static final String OD1103_HIKIATE_HOSEIGAKU_KOKUNAI ="OD1103.hikiate_hoseigaku_kokunai";
    // 引当金額
    public static final String OD1103_HIKIATE_HOSEIGAKU_KAIGAI ="OD1103.hikiate_hoseigaku_kaigai";
    // 担当者
    public static final String OD1103_TANTO_NM          ="OD1103.tanto_nm";
    // 対象年月
    public static final String OD1103_TAISYO_YM         ="OD1103.taisyo_ym";
    // 一括承認
    public static final String OD1103_IKKATUSYONIN      ="OD1103.ikkatusyonin";

    // OD1104 引当金確認_承認
    // 組織
    public static final String OD1104_SOSHIKI           ="OD1104.soshiki";
    // 勘定先CD
    public static final String OD1104_KANJO_CD          ="OD1104.kanjo_cd";
    // 勘定先名称
    public static final String OD1104_KANJO_NM          ="OD1104.kanjo_nm";

	// OS1101_ログイン
	public static final String OS1101_USERID               ="OS1101.userid";
	public static final String OS1101_PASSWORD             ="OS1101.password";

	// OS2101_メインメニュー
	// 業務フロー切替
	public static final String OS2101_GYOUMU_HURO        	 	="OS2101.gyoumu_huro";
	// 査定期
	public static final String OS2101_SATEI_KI             	="OS2101.satei_ki";
	// 対象年月
	public static final String OS2101_YM             			="OS2101.ym";
	// 査代行画面切替期
	public static final String OS2101_DAIKO_GAMEN             ="OS2101.daiko_gamen";
	// 受信日
	public static final String OS2101_JUSHIN_BI        		="OS2101.jushin_bi";
	// 未処理
	public static final String OS2101_TAIRYU_MI_SHORI         ="OS2101.tairyu_mi_shori";
	// 処理中
	public static final String OS2101_TAIRYU_SHORI            ="OS2101.tairyu_shori";
	// 済み
	public static final String OS2101_TAIRYU_ZUMI            	="OS2101.tairyu_zumi";
	// 一次査定中
	public static final String OS2101_SATEI_ICHI             	="OS2101.satei_ichi";
	// 二次査定中
	public static final String OS2101_SATEI_NI             	="OS2101.satei_ni";
	// 実質滞留債権判定
	public static final String OS2101_JISSHI_TAIRYU_HANTEI   	="OS2101.jisshi_tairyu_hantei";
	// 査定
	public static final String OS2101_SATEI             		="OS2101.satei";
	// 貸倒懸念・破産更生債権判定先件数
	public static final String OS2101_KASHIDAOREKENEN        	="OS2101.kashidaorekenen";
	// ※( ) 内は滞留件数
	public static final String OS2101_TAIRYUUKENSUU          	="OS2101.tairyuukensuu";
	// ステータス
	public static final String OS2101_TITLE_STATUS          	="OS2101.title.status";
	// 査定結果
	public static final String OS2101_TITLE_RESUALT          	="OS2101.title.resualt";
	// 追加(2.5次対応)
    public static final String OS2101_A						= "OS2101.A";
    public static final String OS2101_B						= "OS2101.B";
    public static final String OS2101_C						= "OS2101.C";
    public static final String OS2101_MI_SYORI_A				= "OS2101.mi_syori.A";
    public static final String OS2101_SYORI_TYU_B				= "OS2101.syori_tyu.B";
    public static final String OS2101_SYORI_ZUMI_C			= "OS2101.syori_zumi.C";

	// OS3101_クレーム債権_対象先一覧
	public static final String OS3101_SAISETTEIKENSUU         ="OS3101.saisetteikensuu";
	public static final String OS3101_KANJO_CD             	="OS3101.kanjo_cd";
	public static final String OS3101_KANJO_NM             	="OS3101.kanjo_nm";
	public static final String OS3101_COUNTRY              	="OS3101.country";
	public static final String OS3101_KINGAKU_TOTAL        	="OS3101.kingaku_total";
	public static final String OS3101_TAISYO_YM            	="OS3101.taisyo_ym";
	public static final String OS3101_SOSHIKI              	="OS3101.soshiki";
	public static final String OS3101_TANTO_NM             	="OS3101.tanto_nm";
	public static final String OS3101_PROGRESS             	="OS3101.progress";


    // OS3102_クレーム債権再設定_明細一覧
    public static final String OS3102_SOSIKI               ="OS3102.sosiki";
    public static final String OS3102_KANJYOSAKICD         ="OS3102.kanjyosakicd";
    public static final String OS3102_KANJYOSAKINAME       ="OS3102.kanjyosakiname";
    public static final String OS3102_SHINYOKAKUDUKE       ="OS3102.shinyokakuduke";
    public static final String OS3102_SHONINTANTOSHA       ="OS3102.shonintantosha";
    public static final String OS3102_CELL                 ="OS3102.cell";
    public static final String OS3102_KANJYOKAMOKU         ="OS3102.kanjyokamoku";
    public static final String OS3102_SYUSIYOTEIBI         ="OS3102.syusiyoteibi";
    public static final String OS3102_MANKIBI              ="OS3102.mankibi";
    public static final String OS3102_KANJYOSHORIBI        ="OS3102.kanjyoshoribi";
    public static final String OS3102_KINGAKUKEI           ="OS3102.kingakukei";
    public static final String OS3102_TAIRYUHANTEI         ="OS3102.tairyuhantei";
    public static final String OS3102_SHOSAI               ="OS3102.shosai";
    public static final String OS3102_OFFICE	              ="OS3102.jimusyo";
    public static final String OS3102_KEIYAKUDENPYONO      ="OS3102.keiyakudenpyono";
    public static final String OS3102_INVOICENO		     ="OS3102.invoiceno";

    // OS3103_クレーム債権_明細詳細
    public static final String OS3103_KANJYOSAKICD         = "OS3103.kanjyosakicd";
    public static final String OS3103_KANJYOSAKINAME       = "OS3103.kanjyosakiname";
    public static final String OS3103_SHINYOKAKUDUKE       = "OS3103.shinyokakuduke";
    public static final String OS3103_CELL                 = "OS3103.cell";
    public static final String OS3103_KANJYOKAMOKU         = "OS3103.kanjyokamoku";
    public static final String OS3103_SYUSIYOTEIBI         = "OS3103.syusiyoteibi";
    public static final String OS3103_MANKIBI              = "OS3103.mankibi";
    public static final String OS3103_KANJYOSHORIBI        = "OS3103.kanjyoshoribi";
    public static final String OS3103_KIYAKUDENPYONO       = "OS3103.kiyakudenpyono";
    public static final String OS3103_KINGAKUKEI           = "OS3103.kingakukei";
    public static final String OS3103_TAIRYUKUBUN          = "OS3103.tairyukubun";
    public static final String OS3103_TAIRYUHANTEI         = "OS3103.tairyuhantei";
    public static final String OS3103_HANTEIJIYU           = "OS3103.hanteijiyu";
    public static final String OS3103_MSGHANTEIJIYU        = "OS3103.msghanteijiyu";
    public static final String OS3103_INVOICENO            = "OS3103.invoiceno";
    public static final String OS3103_ITEM1                = "OS3103.item1";
    public static final String OS3103_ITEM2                = "OS3103.item2";
    public static final String OS3103_ITEM3                = "OS3103.item3";
    public static final String OS3103_ITEM4                = "OS3103.item4";
    public static final String OS3103_ITEM5                = "OS3103.item5";
    public static final String OS3103_TENPUKAIJYO          = "OS3103.tenpukaijyo";
    public static final String OS3103_OFFICE	              ="OS3103.jimusyo";
    public static final String OS3103_KUREEMU_SAIKEN       ="OS3103.kureemu_saiken";

	// OS3104_クレーム債権再設定_承認一覧
	public static final String OS3104_KANJO_CD             ="OS3104.kanjo_cd";				// 勘定先CD
	public static final String OS3104_KANJO_NM             ="OS3104.kanjo_nm";				// 勘定先名称
	public static final String OS3104_TENPU                ="OS3104.tenpu";					// 添付
	public static final String OS3104_KINGAKU_TOTAL        ="OS3104.kingaku_total";			// 金額計
	public static final String OS3104_TAISYO_YM            ="OS3104.taisyo_ym";				// 対象年月
	public static final String OS3104_SOSHIKI              ="OS3104.soshiki";					// 組織
	public static final String OS3104_PHASE                ="OS3104.phase";					// フェーズ
	public static final String OS3104_TANTO_NM             ="OS3104.tanto_nm";				// 担当者

    // OS3105_クレーム債権再設定_承認
    public static final String OS3105_SOSHIKI              ="OS3105.soshiki";               	// 組織
    public static final String OS3105_KANJO_CD             ="OS3105.kanjo_cd";              	// 勘定先CD
    public static final String OS3105_KANJO_NM             ="OS3105.kanjo_nm";              	// 勘定先名称
    public static final String OS3105_SHINYOUKAKUDUKE      ="OS3105.Shinyoukakuduke";       	// 信用格付

	// OS4101_ゴルフ会員権一覧
	public static final String OS4101_KANJYOCD 		= "OS4101.kanjyocd";
	public static final String OS4101_KANJYONAME 		= "OS4101.kanjyoname";
	public static final String OS4101_SAIKENZANKEI 	= "OS4101.saikenzankei";
	public static final String OS4101_KOTEIKA 		= "OS4101.koteika";
	public static final String OS4101_KASHIDAORE 		= "OS4101.kashidaore";
	public static final String OS4101_SOSHIKI 		= "OS4101.soshiki";
	public static final String OS4101_TAISYOYM 		= "OS4101.taisyoym";
	public static final String OS4101_SHOZAIKOKU 		= "OS4101.shozaikoku";

    // OS6101 査定内容照会
    public static final String OS6101_SATEI_KENSAKU		= "OS6101.satei_kensaku";    			// 査定検索
    public static final String OS6101_TAIRYU_KENSAKU		= "OS6101.tairyu_kensaku";    			// 滞留判定検索
    public static final String OS6101_SATEIKI				= "OS6101.sateiki";    					// 査定期
    public static final String OS6101_YM					= "OS6101.ym";    						// 対象年月
    public static final String OS6101_YM_LABEL			= "OS6101.ym_label";					// (YYYYMM)
    public static final String OS6101_YMD_LABEL    		= "OS6101.ymd_label";   				// (YYYYMMDD)
    public static final String OS6101_KANJYOCD    		= "OS6101.kanjyocd";    				// 勘定先CD
    public static final String OS6101_KANJYONAME			= "OS6101.kanjyoname";    				// 勘定先名称
    public static final String OS6101_PREFIX_SEARCH  		= "OS6101.prefix_search";  				// (前方一致)
    public static final String OS6101_PARTIAL_SEARCH		= "OS6101.partial_search";    			// (部分一致)
    public static final String OS6101_HEAD_TORI_KBN		= "OS6101.head_tori_kbn";    			// 取引先区分(head)
    public static final String OS6101_TORI_KBN			= "OS6101.tori_kbn";    				// 取引先区分
    public static final String OS6101_KANKEISHA			= "OS6101.kankeisha";    				// 関係者
    public static final String OS6101_SYORIBI				= "OS6101.syoribi";    					// 処理日
    public static final String OS6101_NAMIGATA			= "OS6101.namigata";    				// 「～」マーク
    public static final String OS6101_HEAD_SAIKEN_KBN		= "OS6101.head_saiken_kbn";    			// 債権区分(head)
    public static final String OS6101_SAIKEN_KBN			= "OS6101.saiken_kbn";    				// 債権区分
    public static final String OS6101_EMAIL_LABEL			= "OS6101.email_label";    				// (e-mail address)
    public static final String OS6101_DUNS_NO				= "OS6101.duns_no";    					// DUNS No.
    public static final String OS6101_COUNTRY    			= "OS6101.country";    					// 所在国
    public static final String OS6101_TYUSYUTU			= "OS6101.tyusyutu";    				// 抽出事由
    public static final String OS6101_SOUSAIKENZAN		= "OS6101.sousaikenzan";    			// 総債権残
    public static final String OS6101_TUIKA_HIKIATEKIN	= "OS6101.tuika_hikiatekin";    		// 追加引当金
    public static final String OS6101_SHINCHOKU			= "OS6101.shinchoku";    				// 進捗
    public static final String OS6101_SOSHIKI				= "OS6101.soshiki";    					// 組織
    public static final String OS6101_KINGAKU_TOTAL		= "OS6101.kingaku_total";    			// 金額計
    public static final String OS6101_FROM				= "OS6101.from";						// 処理日FROM
    public static final String OS6101_TO					= "OS6101.to";							// 処理日TO

    // OS6102 査定内容詳細
    public static final String OS6102_PHASE        			= "OS6102.phase";    			// フェーズ
    public static final String OS6102_YM						= "OS6102.ym";    				// 対象年月
    public static final String OS6102_SOSHIKI					= "OS6102.soshiki";    			// 組織
    public static final String OS6102_KANJYOCD				= "OS6102.kanjyocd";    		// 勘定先CD
    public static final String OS6102_KANJYONAME				= "OS6102.kanjyoname";    		// 勘定先名称
    public static final String OS6102_TOROKU_TANTO			= "OS6102.toroku_tanto";    	// 登録担当者/登録日時
    public static final String OS6102_SYONIN_TANTO			= "OS6102.syonin_tanto";    	// 承認担当者/承認日時
    public static final String OS6102_ITIZISATEI      		= "OS6102.itizisatei";    		// 一次査定
    public static final String OS6102_ITIZISATEI_KENSHOU 		= "OS6102.itizisatei_kenshou";  // 一次査定検証
    public static final String OS6102_NIZISATEI           	= "OS6102.nizisatei";    		// 二次査定

    // OS6103_進捗状況照会
    public static final String OS6103_KENSAKU_MONTH        = "OS6103.kensaku_month";    		// 対象年月
    public static final String OS6103_KENSAKU_KANJO_CD     = "OS6103.kensaku_kanjo_cd";    	// 勘定先CD
    public static final String OS6103_KENSAKU_DUNS_NO      = "OS6103.kensaku_duns_no";    	// DUNS No.
    public static final String OS6103_KENSAKU_KANJO_NM     = "OS6103.kensaku_kanjo_nm";    	// 勘定先名称
    public static final String OS6103_KENSAKU_COUNTRY      = "OS6103.kensaku_country";    	// 所在国
    public static final String OS6103_KENSAKU_PHASE        = "OS6103.kensaku_phase";    		// フェーズ
    public static final String OS6103_KENSAKU_STATUS       = "OS6103.kensaku_status";    		// ステータス
    public static final String OS6103_KENSAKU_PARTIES      = "OS6103.kensaku_parties";    	// 関係者
    public static final String OS6103_KENSAKU_SYORI_DTFROM = "OS6103.kensaku_syori_dtFrom";   // 処理日
    public static final String OS6103_KENSAKU_YM           = "OS6103.kensaku_ym";    			// (YYYYMM)
    public static final String OS6103_KENSAKU_YMD          = "OS6103.kensaku_ymd";    		// (YYYYMMDD)
    public static final String OS6103_KENSAKU_MAIL         = "OS6103.kensaku_mail";    		// (e-mail address)
    public static final String OS6103_SOSHIKI              = "OS6103.soshiki";				// 組織
    public static final String OS6103_TANTO_NM             = "OS6103.tanto_nm";				// 担当者
    public static final String OS6103_PROGRESS             = "OS6103.progress";				// 進捗
    public static final String OS6103_FROM                 = "OS6103.from";					// 処理日FROM
    public static final String OS6103_TO                   = "OS6103.to";						// 処理日TO
    // 課題No.148
    // 追加開始
    public static final String OS6103_NAMIGATA             = "OS6103.namigata";				// 日本語：～ 英語：-
    // 追加完了

    // OS6104 進捗状況詳細
    public static final String OS6104_KENSAKU_KANJO_CD     = "OS6104.kensaku_kanjo_cd";		// 勘定先CD
    public static final String OS6104_KENSAKU_KANJO_NM     = "OS6104.kensaku_kanjo_nm";		// 勘定先名称
    public static final String OS6104_GENZAI_SHINCHOKU     = "OS6104.genzai_shinchoku";		// 現在進捗
    public static final String OS6104_SATEI                = "OS6104.satei";					// 査定
    public static final String OS6104_ZISSHITU             = "OS6104.zisshitu";				// 実質滞留債権判定

	// OS6105 督促メール配信
	public static final String OS6105_SOUSHINSAKI			= "OS6105.soushinsaki";			// 送信先セレクトボックス
	public static final String OS6105_SHINKITANTO			= "OS6105.shinkitanto";			// 新規担当者選択のラベル
	public static final String OS6105_SATEITAB				= "OS6105.sateitab";			// 査定タブ
	public static final String OS6105_ZISSHITUTAB			= "OS6105.zisshitutab";			// 実質滞留債権判定タブ
	// OS6105 督促メール配信 査定進捗照会タブ/実質滞留債権判定進捗照会タブ
	public static final String OS6105_PHASE					= "OS6105.phase";				// フェーズ
	public static final String OS6105_SOSHIKI				= "OS6105.soshiki";				// 組織
	public static final String OS6105_TANTO					= "OS6105.tantou";				// 担当
	public static final String OS6105_SYORI					= "OS6105.syori";				// 処理
	public static final String OS6105_SYORI_NITIZI			= "OS6105.syori_nitizi";		// 処理日時
	public static final String OS6105_SHINCHOKU				= "OS6105.shinchoku";			// 進捗
	public static final String OS6105_SENTAKU				= "OS6105.sentaku";				// 選択
	// OS6105 督促メール配信 担当者選択
	public static final String OS6105_NYURYOKUKENSAKU		= "OS6105.nuryokukensaku";		// 入力検索
	public static final String OS6105_TANTO_SENTAKU			= "OS6105.tanto_sentaku";		// 担当選択
	public static final String OS6105_TANTO_ICHIRAN			= "OS6105.tanto_ichiran";		// 担当一覧

    // OS7101_代行設定
	public static final String OS7101_HIDAIKOSHASENTAKU    = "OS7101.hidaikoshasentaku";
	public static final String OS7101_NYURYOKUKENSAKU      = "OS7101.nyuryokukensaku";
	public static final String OS7101_TANTOSHASENTAKU      = "OS7101.tantoshasentaku";
	public static final String OS7101_TANROSHAICHIRAN      = "OS7101.tanroshaichiran";
	public static final String OS7101_HIDAIKOSHA           = "OS7101.hidaikosha";
	public static final String OS7101_DAIKOSHASENTAKU      = "OS7101.daikoshasentaku";
	public static final String OS7101_DAIKOSHA             = "OS7101.daikosha";
	public static final String OS7101_DAIKOSHAICHIRAN      = "OS7101.daikoshaichiran";
	public static final String OS7101_DAIKOSHAMSG          = "OS7101.daikoshamsg";
	public static final String OS7101_DAIKOSHAMEI          = "OS7101.daikoshamei";
	public static final String OS7101_DELETE               = "OS7101.delete";
	public static final String OS7101_HIDAIKOSHAICHIRAN    = "OS7101.hidaikoshaichiran";
	public static final String OS7101_HIDAIKOSHAMSG        = "OS7101.hidaikoshamsg";
	public static final String OS7101_HIDAIKOSHAMEI        = "OS7101.hidaikoshamei";
	public static final String OS7101_DAIKOSHA_HIDAIKOSHA  = "OS7101.daikosha_hidaikosha";

	// OS7102_査定会社メンテナンス_一覧
	public static final String OS7102_SYSTEM 			= "OS7102.system";
	public static final String OS7102_MEISYO 			= "OS7102.meisyo";
	public static final String OS7102_BUBUNICHI 		= "OS7102.bubunichi";
	public static final String OS7102_NIHONGO 		= "OS7102.nihongo";
	public static final String OS7102_EIGO 			= "OS7102.eigo";
	public static final String OS7102_HYOJUNJIKOKU 	= "OS7102.hyojunjikoku";
	public static final String OS7102_CHUSYUTUTAISYO 	= "OS7102.chusyututaisyo";
	public static final String OS7102_ZENPOICHI 		= "OS7102.zenpoichi";

	// OS7103_査定会社メンテナンス_登録
	public static final String OS7103_SYSTEM 			= "OS7103.system";
	public static final String OS7103_MEISYO 			= "OS7103.meisyo";
	public static final String OS7103_NIHONGO 		= "OS7103.nihongo";
	public static final String OS7103_EIGO 			= "OS7103.eigo";
	public static final String OS7103_HYOJUNJIKOKU 	= "OS7103.hyojunjikoku";
	public static final String OS7103_CHUSYUTUTAISYO 	= "OS7103.chusyututaisyo";

    // OS7104_業務フローパターンメンテナンス_一覧
    // システム
    public static final String OS7104_SYSTEM			= "OS7104.system";
    // 汎用1
    public static final String OS7104_HANYOU1			= "OS7104.hanyou1";
    // 業務フローパターン名称
    public static final String OS7104_WORKFLOW_NM		= "OS7104.workflow_nm";
    // パターンID
    public static final String OS7104_PATTERN_ID		= "OS7104.pattern_id";
    // 業務フローパターン名称(日本語)
    public static final String OS7104_WORKFLOW_NM_JP	= "OS7104.workflow_nm_jp";
    //  業務フローパターン名称(英語)
    public static final String OS7104_WORKFLOW_NM_EN	= "OS7104.workflow_nm_en";
    //　(部分一致)
    public static final String OS7104_BUBUN_ITTI		= "OS7104.bubun_itti";

    // OS7105_業務フローパターンメンテナンス_登録
    // システム
    public static final String OS7105_SYSTEM                     = "OS7105.system";
    // 汎用1
    public static final String OS7105_HANYOU1                    = "OS7105.hanyou1";
    // 業務フローパターン名称(日本語)
    public static final String OS7105_NAME_JP                    = "OS7105.name_jp";
    // 業務フローパターン名称(英語)
    public static final String OS7105_NAME_EN                    = "OS7105.name_en";
    // 登録
    public static final String OS7105_TOUROKU                    = "OS7105.touroku";
    // 承認
    public static final String OS7105_SHOUNIN                    = "OS7105.shounin";
    // [実質滞留債権判定]
    public static final String OS7105_TAIRYU_TITLE               = "OS7105.tairyu_title";
    // 滞留判定
    public static final String OS7105_TAIRYU_HANTEI              = "OS7105.tairyu_hantei";
    // 滞留判定検証
    public static final String OS7105_TAIRYU_KENSHOU             = "OS7105.tairyu_kenshou";
    // 対象先選定
    public static final String OS7105_TAISHOU_SENTEI             = "OS7105.taishou_sentei";
    // 滞留判定への差戻は事務局を経由
    public static final String OS7105_TAISHOU_SA                 = "OS7105.taishou_sa";
    // [査定]
    public static final String OS7105_SATEI_TITLE                = "OS7105.satei_title";
    // 一次査定
    public static final String OS7105_ITI_SATEI                  = "OS7105.iti_satei";
    // 一次査定検証
    public static final String OS7105_ITI_KENSHOU                = "OS7105.iti_kenshou";
    // 二次査定
    public static final String OS7105_NI_SATEI                   = "OS7105.ni_satei";
    // 査定完了後の差戻を行う
    public static final String OS7105_SATEI_SA                   = "OS7105.satei_sa";
    // 二次査定区分
    public static final String OS7105_NI_SATEI_KBN               = "OS7105.ni_satei_kbn";
    // [引当金検証/確認]
    public static final String OS7105_HIKIATE_TITLE              = "OS7105.hikiate_title";
    // 引当金検証
    public static final String OS7105_HIKIATE_KENSHOU            = "OS7105.hikiate_kenshou";
    // 引当金確認
    public static final String OS7105_HIKIATE_KAKUNIN            = "OS7105.hikiate_kakunin";
    // [システム管理]
    public static final String OS7105_SYSTEM_TITLE               = "OS7105.system_title";
    // クレーム債権
    public static final String OS7105_KUREMU                     = "OS7105.kuremu";
    // 代行設定
    public static final String OS7105_DAIKOU_SETTEI              = "OS7105.daikou_settei";
    // 査定会社メンテナンス
    public static final String OS7105_SATEI_MENTENANSU           = "OS7105.satei_mentenansu";
    // 業務フローパターンメンテナンス
    public static final String OS7105_GYOUMU_MENTENANSU          = "OS7105.gyoumu_mentenansu";
    // ユーザマスタメンテナンス
    public static final String OS7105_USER_MENTENANSU            = "OS7105.user_mentenansu";
    // 勘定科目マスタメンテナンス
    public static final String OS7105_KANJOU_MENTENANSU          = "OS7105.kanjou_mentenansu";
    // 抽出条件メンテナンス(本社)
    public static final String OS7105_CHUUSHUTU_HONSHA           = "OS7105.chuushutu_honsha";
    // 抽出条件メンテナンス
    public static final String OS7105_CHUUSHUTU_MENTENANSU       = "OS7105.chuushutu_mentenansu";
    // チャンピオン部メンテナンス
    public static final String OS7105_CHANPION_MENTENANSU        = "OS7105.chanpion_mentenansu";
    // ゴルフ会員権メンテナンス
    public static final String OS7105_MEMBER_MENTENANSU          = "OS7105.member_mentenansu";
    // 連結区分マスタUPLOAD
    public static final String OS7105_RENKETU_UPLOAD             = "OS7105.renketu_upload";
    // システム管理者専用
    public static final String OS7105_ADMIN_ONLY                 = "OS7105.admin_only";
    // 課題No.105
    // 修正開始
    // 滞留判定 登録
    /*public static final String OS7105_TAIRYU_HANTEI_S            = "OS7105.tairyu_hantei_s";
    // 滞留判定 承認
    public static final String OS7105_TAIRYU_HANTEI_T            = "OS7105.tairyu_hantei_t";
    // 滞留判定検証 登録
    public static final String OS7105_TAIRYU_KENSHOU_S           = "OS7105.tairyu_kenshou_s";
    // 滞留判定検証 承認
    public static final String OS7105_TAIRYU_KENSHOU_T           = "OS7105.tairyu_kenshou_t";
    // 対象先選定 登録
    public static final String OS7105_TAISHOU_SENTEI_S           = "OS7105.taishou_sentei_s";
    // 対象先選定 承認
    public static final String OS7105_TAISHOU_SENTEI_T           = "OS7105.taishou_sentei_t";
    // 一次査定 登録
    public static final String OS7105_ITI_SATEI_S                = "OS7105.iti_satei_s";
    // 一次査定 承認
    public static final String OS7105_ITI_SATEI_T                = "OS7105.iti_satei_t";
    // 一次査定検証 登録
    public static final String OS7105_ITI_KENSHOU_S              = "OS7105.iti_kenshou_s";
    // 一次査定検証 承認
    public static final String OS7105_ITI_KENSHOU_T              = "OS7105.iti_kenshou_t";
    // 二次査定 登録
    public static final String OS7105_NI_SATEI_S                 = "OS7105.ni_satei_s";
    // 二次査定 承認
    public static final String OS7105_NI_SATEI_T                 = "OS7105.ni_satei_t";
    // 引当金検証 登録
    public static final String OS7105_HIKIATE_KENSHOU_S          = "OS7105.hikiate_kenshou_s";
    // 引当金確認 登録
    public static final String OS7105_HIKIATE_KAKUNIN_S          = "OS7105.hikiate_kakunin_s";
    // クレーム債権 登録
    public static final String OS7105_KUREMU_S                   = "OS7105.kuremu_s";
    // クレーム債権 承認
    public static final String OS7105_KUREMU_T                   = "OS7105.kuremu_t";*/
    public static final String OS7105_WORK_FLOW               	="OS7105.Workflow";                			// ユーザID
    // 修正完了

    // OS7106_ユーザマスタメンテナンス_一覧
    public static final String OS7106_USER_ID               		="OS7106.user_id";                			// ユーザID
    public static final String OS7106_USER_NM              		="OS7106.user_nm";               			// 氏名
    public static final String OS7106_EMAIL                  		="OS7106.email";              				// E-Mail
    public static final String OS7106_GYOUMU_HURO             	="OS7106.gyoumu_huro";            			// 業務フロー
    public static final String OS7106_GYOUMU_HURO_NM             	="OS7106.gyoumu_huro_nm";            		// 業務フロー名
    public static final String OS7106_KAISYA_NM               	="OS7106.kaisya_nm";       					// 会社名
    public static final String OS7106_SYOZOKUSOSHIKI_NM       	="OS7106.syozokusoshiki_nm";     			// 所属組織名
    public static final String OS7106_MAIL_ADDR               	="OS7106.mail_addr";       					// メールアドレス
    public static final String OS7106_PRINTOUT_DEFAULT_LANG_KBN   ="OS7106.printout_default_lang_kbn";       // 帳票出力言語
    public static final String OS7106_MAIL_HAISHIN                ="OS7106.mail_haishin";       				// メール配信
    public static final String OS7106_FIRST_LIKE              	="OS7106.first_like";       				// （前方一致）
    public static final String OS7106_ALL_LIKE		         	="OS7106.all_like";       					// （部分一致）

    // OS7107_ユーザマスタメンテナンス_登録
	public static final String OS7107_USER_ID						="OS7107.user_id";							// ユーザID
	public static final String OS7107_USER_NM						="OS7107.user_nm";							// 氏名
	public static final String OS7107_KAISYA_NM					="OS7107.kaisya_nm";						// 会社名
	public static final String OS7107_SYOZOKUSOSHIKI_NM			="OS7107.syozokusoshiki_nm";				// 所属組織名
	public static final String OS7107_MAIL_ADDR					="OS7107.mail_addr";						// メールアドレス
	public static final String OS7107_ADMIN_FLG					="OS7107.admin_flg";						// システム管理者
	public static final String OS7107_MAIL_HAISHIN				="OS7107.mail_haishin";						// メール配信
	public static final String OS7107_PRINTOUT_DEFAULT_LANG_KBN	="OS7107.printout_default_lang_kbn";		// 帳票出力言語
	public static final String OS7107_LANG_JP						="OS7107.lang_jp";							// 日本語
	public static final String OS7107_LANG_EN						="OS7107.lang_en";							// 英語
	public static final String OS7107_LANG_CHOICE					="OS7107.lang_choice";						// 出力時に選択
	public static final String OS7107_GYOUMU_HURO					="OS7107.gyoumu_huro";						// 業務フローパターン
	public static final String OS7107_DEFAULT_FLG					="OS7107.default_flg";						// 既定
	public static final String OS7107_PATTERN_NAME_JP				="OS7107.pattern_name_jp";					// 業務フローパターン名称
	public static final String OS7107_CODE						="OS7107.code";								// コード
	public static final String OS7107_NAME						="OS7107.name";								// 名称
	public static final String OS7107_TITLE_GYOUMU_HURO			="OS7107.title.gyoumu_huro";				// タイトル実施業務フロー
	public static final String OS7107_TITLE_HAISHINSAKI			="OS7107.title.haishinsaki";				// メール配信先（タイトル）
	public static final String OS7107_TITLE_TANTOSOSHIKI		="OS7107.title.tantososhiki";				// 担当組織（タイトル）
	public static final String OS7107_TANTOCLICK				="OS7107.tantoclick";						// 担当組織説明文上
	public static final String OS7107_TANTOPINK					="OS7107.tantopink";						// 担当組織説明文下
	public static final String OS7107_HONBU						="OS7107.honbu";							// 本部
	public static final String OS7107_SENTAKU					="OS7107.sentaku";							// 選択
	public static final String OS7107_ADDTANTOSOSHIKI			="OS7107.addtantososhiki";					// 追加する担当組織
	public static final String OS7107_FUKATANTOSOSHIKI			="OS7107.fukatantososhiki";					// 変更不可の担当組織が設定されている

    // OS7108_勘定科目マスタメンテナンス_一覧・登録
    public static final String OS7108_BTN_SINKI_SAKUSEI    ="OS7108.sinki_sakusei";         	// 新規作成ボタン
    public static final String OS7108_SYSTEM				 ="OS7108.system";					// システム
    public static final String OS7108_KANJO_CD			 ="OS7108.kanjo_cd";				// 勘定科目コード
    public static final String OS7108_KANJO_NM			 ="OS7108.kanjo_nm";				// 勘定科目名称
    public static final String OS7108_KANJO_UCHI_CD		 ="OS7108.kanjo_uchi_cd";			// 内分類コード
    public static final String OS7108_SAIKEN_FLG			 ="OS7108.saiken_flg";				// 債権フラグ
    public static final String OS7108_MEISAI_KANJO_CD		 ="OS7108.meisai_kanjo_cd";			// 明細の勘定科目
    public static final String OS7108_MEISAI_KANJO_UCHI_CD ="OS7108.meisai_kanjo_uchi_cd";	// 明細の内分類
    public static final String OS7108_MEISAI_HYOJIKUBUN	 ="OS7108.meisai_hyojikubun";		// 明細の表示区分
    public static final String OS7108_MEISAI_MANKIBI		 ="OS7108.meisai_mankibi";			// 明細の満期日優先
    public static final String OS7108_MEISAI_GORUFU		 ="OS7108.meisai_gorufu";			// 明細のゴルフ会員権
    public static final String OS7108_MEISAI_DRCR_KBN		 ="OS7108.meisai_drcr_kbn";			// 明細のDR/CR区分
    public static final String OS7108_FIRST_LIKE		 	 ="OS7108.first_like";				// （前方一致）
    public static final String OS7108_ALL_LIKE		 	 ="OS7108.all_like";				// （部分一致）

    // OS7109 勘定科目マスタメンテナンス_登録
    public static final String OS7109_SYSTEM				 	="OS7109.system";					// システム
    public static final String OS7109_KANJO_CD			 	="OS7109.kanjo_cd";					// 勘定科目コード
    public static final String OS7109_KANJO_NM			 	="OS7109.kanjo_nm";					// 勘定科目名称
    public static final String OS7109_KANJO_UCHI_CD		 	="OS7109.kanjo_uchi_cd";			// 内分類コード
    public static final String OS7109_KANJO_UCHI_NM		 	="OS7109.kanjo_uchi_nm";			// 内分類コード
    public static final String OS7109_SAIKEN_FLG			 	="OS7109.saiken_flg";				// 債権フラグ
    public static final String OS7109_HYOJIKUBUN	 			="OS7109.hyojikubun";				// 表示区分
    public static final String OS7109_MANKIBI		 			="OS7109.mankibi";					// 満期日優先
    public static final String OS7109_GORUFU		 			="OS7109.gorufu";					// ゴルフ会員権
    public static final String OS7109_DRCR_KBN		 		="OS7109.drcr_kbn";					// DR/CR区分

    // OS7110_抽出条件メンテナンス_一覧
    public static final String OS7110_SYSTEM 				= "OS7110.system";
    public static final String OS7110_KESSANKIKBN 		= "OS7110.kessankikbn";
    public static final String OS7110_KIJYUNBI 			= "OS7110.kijyunbi";
    public static final String OS7110_JYOKENMEISYO 		= "OS7110.jyokenmeisyo";
    public static final String OS7110_BUBUNICHI 			= "OS7110.bubunichi";
    public static final String OS7110_CHUSYUTUJIYU 		= "OS7110.chusyutujiyu";
    public static final String OS7110_ICHIRANJYOKEN 		= "OS7110.ichiranjyoken";
    public static final String OS7110_KAKUDUKE 			= "OS7110.kakuduke";
    public static final String OS7110_KINGAKUKIJUN1 		= "OS7110.kingakukijun1";
    public static final String OS7110_KINGAKUKIJUN2 		= "OS7110.kingakukijun2";
    public static final String OS7110_TAIRYUKIKAN 		= "OS7110.tairyukikan";
    public static final String OS7110_TAIRYUTAISYO 		= "OS7110.tairyutaisyo";
    public static final String OS7110_SATEITAISYO 		= "OS7110.sateitaisyo";
    public static final String OS7110_NAMIGATA 			= "OS7110.namigata";
    public static final String OS7110_IJYO 				= "OS7110.ijyo";


    // OS7111_抽出条件メンテナンス_登録
    public static final String OS7111_CHUSYUTUJIYU 		= "OS7111.chusyutujiyu";
    public static final String OS7111_JYOKENNIHONNGO 		= "OS7111.jyokennihonngo";
    public static final String OS7111_JYOKENEIGO 			= "OS7111.jyokeneigo";
    public static final String OS7111_SYSTEM 				= "OS7111.system";
    public static final String OS7111_KESANKIKBN 			= "OS7111.kesankikbn";
    public static final String OS7111_KIJYUNBI 			= "OS7111.kijyunbi";
    public static final String OS7111_KIHONJYOKEN 		= "OS7111.kihonjyoken";
    public static final String OS7111_KAKUDUKE 			= "OS7111.kakuduke";
    public static final String OS7111_KINGAKUKIJYUN1 		= "OS7111.kingakukijyun1";
    public static final String OS7111_KINGAKUKIJYUN2KBN	= "OS7111.kingakukijyun2kbn";
    public static final String OS7111_TAIRYUKIKAN 		= "OS7111.tairyukikan";
    public static final String OS7111_TAIRYUTAISYO 		= "OS7111.tairyutaisyo";
    public static final String OS7111_SATEITAISYO 		= "OS7111.sateitaisyo";
    public static final String OS7111_KAKOJYOKEN 			= "OS7111.kakojyoken";
    public static final String OS7111_KAKOFLAG 			= "OS7111.kakoflag";
    public static final String OS7111_KAKOKAKUDUKE 		= "OS7111.kakokakuduke";
    public static final String OS7111_KAKOJITEN 			= "OS7111.kakojiten";
    public static final String OS7111_KAGETU 				= "OS7111.kagetu";
    public static final String OS7111_KAGETUMAE 			= "OS7111.kagetumae";
    public static final String OS7111_NAMIGATA 			= "OS7111.namigata";
    public static final String OS7111_IJYO 				= "OS7111.ijyo";
    public static final String OS7111_KINGAKUKIJYUN2 		= "OS7111.kingakukijyun2";
    public static final String OS7111_KINGAKU 			= "OS7111.kingaku";
    public static final String OS7111_COMMA 				= "OS7111.comma";

	// OS7115_メール送信先選択
	public static final String OS7115_SAISHIN			="OS7115.saishin";			// 最新のみ取得
	public static final String OS7115_ZENKEN 			="OS7115.zenken";			// 全件取得
	public static final String OS7115_CODE				="OS7115.code";				// コード
	public static final String OS7115_NAME				="OS7115.name";				// 名称
	public static final String OS7115_SENTAKU			="OS7115.sentaku";			// 選択
	public static final String OS7115_ICHIRAN			="OS7115.ichiran";			// 担当 会社・部門・部
	public static final String OS7115_KAISYA			="OS7115.kaisya";			// 会社（国内）
	public static final String OS7115_CHIIKI			="OS7115.chiiki";			// 会社（海外）

	// OS7116_担当組織選択
	public static final String OS7116_CODE				="OS7116.code";				// コード
	public static final String OS7116_NAME				="OS7116.name";				// 名称
	public static final String OS7116_SENTAKU			="OS7116.sentaku";			// 選択
	public static final String OS7116_HONBU				="OS7116.honbu";			// 本部

	// OS8101_帳票ダウンロード
	public static final String OS8101_KANJO_CD           ="OS8101.kanjo_cd";
	public static final String OS8101_KANJO_NM           ="OS8101.kanjo_nm";
	public static final String OS8101_SELECT_LIST        ="OS8101.select_list";
	public static final String OS8101_LIST_TYPE          ="OS8101.list_type";
	public static final String OS8101_OUTPUT             ="OS8101.output";
	public static final String OS8101_YM_LABEL           ="OS8101.ym_label";
	public static final String OS8101_TAISYO_YM          ="OS8101.taisyo_ym";
	public static final String OS8101_DUNS_NO            ="OS8101.duns_no";

	// OZ2101_差戻先選択
	public static final String OZ2101_KANJO_CD             ="OZ2101.kanjo_cd";
	public static final String OZ2101_KANJO_NM             ="OZ2101.kanjo_nm";
	public static final String OZ2101_TAIRYU_SASHI         ="OZ2101.tairyu_sashi";
	public static final String OZ2101_SATEI_SASHI          ="OZ2101.satei_sashi";
	public static final String OZ2101_HIKIATE_SASHI        ="OZ2101.hikiate_sashi";
	public static final String OZ2101_JIMUKYOKU_SASHI      ="OZ2101.jimukyoku_sashi";
	public static final String OZ2101_SINKI_SASHI          ="OZ2101.sinki_sashi";
	public static final String OZ2101_SASHI_KBN            ="OZ2101.sashi_kbn";
	public static final String OZ2101_SASHI_PHASE          ="OZ2101.sashi_phase";
	public static final String OZ2101_NYURYOKUKENSAKU      ="OZ2101.nuryokukensaku";
	public static final String OZ2101_TANTO_SENTAKU        ="OZ2101.tanto_sentaku";
	public static final String OZ2101_TANTO_ICHIRAN        ="OZ2101.tanto_ichiran";
	public static final String OZ2101_TANTO                ="OZ2101.tanto";
	public static final String OZ2101_BU                   ="OZ2101.bu";
	public static final String OZ2101_COMMENT              ="OZ2101.comment";
	public static final String OZ2101_SOSHIKI              ="OZ2101.soshiki";
	public static final String OZ2101_SINTYOKU             ="OZ2101.sintyoku";
	public static final String OZ2101_SASHI_ICHIRAN        ="OZ2101.sashi_ichiran";
	public static final String OZ2101_SOSHIKI_SENTAKU      ="OZ2101.soshiki_sentaku";
	public static final String OZ2101_PHASE_SENTAKU        ="OZ2101.phase_sentaku";
	public static final String OZ2101_PHASE                ="OZ2101.phase";
	public static final String OZ2101_SYORI                ="OZ2101.syori";
	public static final String OZ2101_SYORI_DATE           ="OZ2101.syori_date";

	// OZ3101_転送先選択
	public static final String OZ3101_KANJO_CD             ="OZ3101.kanjo_cd";
	public static final String OZ3101_KANJO_NM             ="OZ3101.kanjo_nm";
	public static final String OZ3101_NYURYOKUKENSAKU      ="OZ3101.nuryokukensaku";
	public static final String OZ3101_TANTO_SENTAKU        ="OZ3101.tanto_sentaku";
	public static final String OZ3101_TANTO_ICHIRAN        ="OZ3101.tanto_ichiran";
	public static final String OZ3101_TANTO                ="OZ3101.tanto";
	public static final String OZ3101_BU                   ="OZ3101.bu";
	public static final String OZ3101_COMMENT              ="OZ3101.comment";

	//OZ6101_滞留債権明細照会タブ
	public static final String OZ6101_SOSHIKI				="OZ6101.soshiki";				// 組織
	public static final String OZ6101_KANJO_CD			="OZ6101.kanjo_cd";				// 科目
	public static final String OZ6101_KANJO_NM			="OZ6101.kanjo_nm";				// 科目名称
	public static final String OZ6101_KINGAKU_TOTAL		="OZ6101.kingaku_total";		// 金額計
	public static final String OZ6101_SHUSI_DT			="OZ6101.shusi_dt";				// 収支予定美
	public static final String OZ6101_KANJO_DT        	="OZ6101.kanjo_dt";				// 勘定処理日
	public static final String OZ6101_KEIYAKU_DENPYO_NO	="OZ6101.keiyaku_denpyo_no";	// 契約伝票No
	public static final String OZ6101_INVOICE_NO			="OZ6101.invoice_no";			// インボイスNo
	public static final String OZ6101_TAIRYU_KBN			="OZ6101.tairyu_kbn";			// 滞留区分
	public static final String OZ6101_TAIRYU_HITAIRYU		="OZ6101.tairyu_hitairyu";		// 滞留/非滞留
	public static final String OZ6101_HANTEI_JIYUU		="OZ6101.hantei_jiyuu";			// 判定事由
	public static final String OZ6101_TENPU_SANSYO		="OZ6101.tenp_sansyo";			// 添付参照

	// OZ6108_基本情報照会タブ
	public static final String OZ6108_REASON             ="OZ6108.reason";
	public static final String OZ6108_DUNS_NO            ="OZ6108.duns_no";
	public static final String OZ6108_KTK                ="OZ6108.ktk";
	public static final String OZ6108_FSS                ="OZ6108.fss";
	public static final String OZ6108_DUNS_RATING        ="OZ6108.duns_rating";
	public static final String OZ6108_OYA_KAISYA         ="OZ6108.oya_kaisya";

	//OZ6101_債権明細照会タブ
	public static final String OZ6105_SOSHIKI				="OZ6105.soshiki";				// 組織
	public static final String OZ6105_KANJO_CD			="OZ6105.kanjo_cd";				// 科目
	public static final String OZ6105_KANJO_NM			="OZ6105.kanjo_nm";				// 科目名称
	public static final String OZ6105_KINGAKU_TOTAL		="OZ6105.kingaku_total";		// 金額計
	public static final String OZ6105_SHUSI_DT			="OZ6105.shusi_dt";				// 収支予定美
	public static final String OZ6105_KANJO_DT        	="OZ6105.kanjo_dt";				// 勘定処理日
	public static final String OZ6105_KEIYAKU_DENPYO_NO	="OZ6105.keiyaku_denpyo_no";	// 契約伝票No
	public static final String OZ6105_INVOICE_NO			="OZ6105.invoice_no";			// インボイスNo
	public static final String OZ6105_TAIRYU_KBN			="OZ6105.tairyu_kbn";			// 滞留区分
	public static final String OZ6105_TAIRYU_HITAIRYU		="OZ6105.tairyu_hitairyu";		// 滞留/非滞留
	public static final String OZ6105_HANTEI_JIYUU		="OZ6105.hantei_jiyuu";			// 判定事由
	public static final String OZ6105_TENPU_SANSYO		="OZ6105.tenp_sansyo";			// 添付参照
	public static final String OZ6105_SAIKEN_ZAN			="OZ6105.saiken_zan";			// 債権残高合計
	public static final String OZ6105_HOSYO_SAIMU			="OZ6105.hosyo_saimu";			// 保所債務合計
	public static final String OZ6105_HIKIATE_TOTAL		="OZ6105.hikiate_total";		// 引当金合計
	public static final String OZ6105_CREDIT_TOTAL		="OZ6105.credit_total";			// 滞留債権計

	// OZ6109 査定進捗照会タブ
	public static final String OZ6109_PHASE             	="OZ6109.phase";
	public static final String OZ6109_SOSHIKI            	="OZ6109.soshiki";
	public static final String OZ6109_TANTOU             	="OZ6109.tantou";
	public static final String OZ6109_SYORI              	="OZ6109.syori";
	public static final String OZ6109_SYORI_NITIZI        ="OZ6109.syori_nitizi";

	// OZ6110_実質滞留債権判定進捗照会タブ
	public static final String OZ6110_PHASE             	="OZ6110.phase";
	public static final String OZ6110_SOSHIKI            	="OZ6110.soshiki";
	public static final String OZ6110_TANTOU             	="OZ6110.tantou";
	public static final String OZ6110_SYORI              	="OZ6110.syori";
	public static final String OZ6110_SYORI_NITIZI		="OZ6110.syori_nitizi";
	public static final String OZ6110_SHINCHOKU			="OZ6110.shinchoku";

	// OZ6106_引当金確認照会タブ
	// 区分判定根拠<BR>(前期(半期決算前))
    	public static final String OZ6106_ZENKI_COMMOND        = "OD1102.zenki_commond";
    	// 引当金算定根拠（仮基準）
    	public static final String OZ6106_KIJUN_COMMOND        = "OD1102.kijun_commond";
    	// 区分判定根拠（今期）
    	public static final String OZ6106_KONKI_COMMOND        = "OD1102.konki_commond";

	/************************************************************************************
	 * 確認メッセージ
	 ***********************************************************************************/
	public static final String CONFIRM_TAKE           ="confirm.take";
	public static final String CONFIRM_DISAPPEARE     ="confirm.disappeare";
	public static final String CONFIRM_DELETE         ="confirm.delete";
	public static final String CONFIRM_DIFFER         ="confirm.differ";
	public static final String CONFIRM_HIKIATEKAJOU   ="confirm.hikiatekajou";
	public static final String CONFIRM_HIKIATEHUSOKU  ="confirm.hikiatehusoku";
	// 課題No.230
	// 追加開始
	public static final String CONFIRM_LOGOFF                ="confirm.logoff";
	// 追加完了
	public static final String CONFIRM_SEND           = "confirm.send";
	public static final String CONFIRM_LASTMAILDATE           = "confirm.lastMailDate";
	public static final String CONFIRM_LASTSAVEDATE           = "confirm.lastSaveDate";

	/************************************************************************************
	 * 警告メッセージ
	 ***********************************************************************************/
	public static final String WARNING_WAITJAVASCRIPT        ="warning.waitJavaScript";
	public static final String WARNING_EXTENSIONJAVASCRIPT   ="warning.extensionJavaScript";
	public static final String WARNING_INIT                  ="warning.init";
	/************************************************************************************
	 * エラー画面メッセージ
	 ***********************************************************************************/
	public static final String SYSTEM_FATAL      ="system.fatal";
	public static final String SYSTEM_DBACC      ="system.dbacc";
	public static final String SYSTEM_TIMEOUT    ="system.timeout";
	public static final String SYSTEM_CLOSE      ="system.close";
	public static final String SYSTEM_STOPTIME   ="system.stoptime";
	public static final String SYSTEM_STOPTIME2  ="system.stoptime2";
	public static final String SYSTEM_STOPTIME3  ="system.stoptime3";

	/************************************************************************************
	 * エラーメッセージ
	 ***********************************************************************************/
	// 課題No.230
	// 追加開始
	public static final String ERR_LOGIN_PATTERNID   ="err.loginPatternid";
	// 追加完了
	public static final String ERR_OUTPUT            ="err.output";
	public static final String ERR_TAKEN             ="err.taken";
	public static final String ERR_SELECT            ="err.select";
	public static final String ERR_LENGTH            ="err.length";
	public static final String ERR_INPUT             ="err.input";
	public static final String ERR_NUMERICONLY       ="err.numericOnly";
	public static final String ERR_REGIST            ="err.regist";
	public static final String ERR_REGISTERED        ="err.registered";
	public static final String ERR_CC_REGISTERED     ="err.cc.registered";
	public static final String ERR_DIGITS            ="err.digits";
	public static final String ERR_INVALID           ="err.invalid";
	public static final String ERR_PROHIBITTED       ="err.prohibitted";
	public static final String ERR_INPUTWHEN         ="err.inputWhen";
	public static final String ERR_INPUTWHEN2         ="err.inputWhen2";
	public static final String ERR_SEARCH            ="err.search";
	public static final String ERR_SEARCHOVER        ="err.searchOver";
	public static final String ERR_INPUTREASON       ="err.InputReason";
	public static final String ERR_MAKE              ="err.make";
	public static final String ERR_LOGIN             ="err.login";
	public static final String ERR_COMPANY           ="err.company";
	public static final String ERR_PERSONLOGIN       ="err.personLogin";
	public static final String ERR_UPDATE            ="err.update";
	public static final String ERR_COMPLETED         ="err.completed";
	public static final String ERR_HIKIATESTART      ="err.hikiatestart";
	public static final String ERR_UNREGISTERED      ="err.unregistered";
	public static final String ERR_REGISTEREDDATA    ="err.registeredData";
	public static final String ERR_COMPAREDDATA      ="err.comparedData";
	public static final String ERR_REQUIREDITEM      ="err.requiredItem";
	public static final String ERR_ACCOUNTINGMONTH   ="err.accountingMonth";
	public static final String ERR_ADD               ="err.add";
	public static final String ERR_OPERATE           ="err.operate";
	public static final String ERR_DUPLICATION       ="err.duplication";
	public static final String ERR_CHECKTAKE         ="err.checkTake";
	public static final String ERR_NEWFILE           ="err.newFile";
	public static final String ERR_BYTE              ="err.byte";
	public static final String ERR_CHECKANDDEL       ="err.checkAndDel";
	public static final String ERR_TMPFILE           ="err.tmpFile";
	public static final String ERR_NOTREGISTERED     ="err.notRegistered";
	public static final String ERR_FILE              ="err.file";
	public static final String ERR_STARTINGOVER      ="err.startingOver";
	public static final String ERR_DATA              ="err.data";
	public static final String ERR_EXCEL             ="err.excel";
	public static final String ERR_FILEINPUT         ="err.fileInput";
	public static final String ERR_TAISYOUGAI        ="err.taisyougai";
	public static final String ERR_HANTEIJIYU        ="err.hanteiJiyu";
	public static final String ERR_HASSEIKEII        ="err.hasseiKeii";
	public static final String ERR_OTHRYUHOSAIMU     ="err.othRyuhosaimu";
	public static final String ERR_NOTSET            ="err.notSet";
	public static final String ERR_SANSYO_NOTSET     ="err.sansyo_notSet";
	public static final String ERR_NOTEXECUTE        ="err.notexecute";
	public static final String ERR_NOTERASES         ="err.noterases";
	public static final String ERR_NOTREGISTERD      ="err.notregisterd";
	public static final String ERR_NOTANKEN          ="err.notanken";
	public static final String ERR_NOTSANSYO         ="err.notsansyo";
	//課題No.09
	//追加開始
	public static final String ERR_CHK_BUNSYO        ="err.chk_bunsyo";
	//追加完了
	//課題No.104
	//追加開始
	public static final String ERR_HANKAKUONLY        ="err.hankakuonly";
	//追加完了
	//課題No.107
	//追加開始
	public static final String ERR_SANSYOUCHECK        ="err.sansyoucheck";
	//追加完了
	//課題No.105
	//追加開始
	public static final String ERR_SELECT2            ="err.select2";
	//追加完了
	public static final String ERR_SELECT3 				="err.select3";
	public static final String ERR_HANEI				="err.hanei";
	public static final String ERR_CHECK				="err.check";
	public static final String ERR_SAMSYO_NOTSET2		="err.sansyo_notSet2";
	public static final String ERR_CHANGEDDATA = "err.changedData";

	
	/************************
	 * CNC.Add
	 ************************/
	public static final String ERR_TORITAKEN			= "err.toriTaken";
	public static final String ERR_NODATA			    = "err.noData";
	public static final String ERR_SYSTEM				= "err.System";
	public static final String ERR_NOTUPDATE			= "err.notUpdate";
	public static final String ERR_NOADD         		= "err.noAdd";
	public static final String ERR_NOTDELETE			= "err.notDelete";
	public static final String ERR_RE_REGISTRATION	= "err.Re-registration";
	public static final String ERR_DELETION			= "err.Deletion";
	public static final String ERR_NOTRATING          ="err.notRating";
	public static final String REPLACE_1000			= "replace.1000";
	public static final String REPLACE_10				= "replace.10";
	public static final String REPLACE_12				= "replace.12";
	public static final String REPLACE_0				= "replace.0";
	public static final String REPLACE_5				= "replace.5";
	public static final String REPLACE_6				= "replace.6";
	public static final String REPLACE_3				= "replace.3";
	public static final String REPLACE_2				= "replace.2";
	public static final String ERR_DIGITS2			= "err.digits2";
	public static final String ERR_TORIMODOSHI		= "err.torimodoshi";
	public static final String REPLACE_HOZENSONOTA    ="replace.hozenSonota";
	public static final String REPLACE_USER             ="replace.user";
	public static final String REPLACE_SASHIMODOSHISAKI ="replace.sashimodoshisaki";
	public static final String REPLACE_SIMEKBN ="replace.simekbn";
	public static final String REPLACE_HANTEIJIYU ="replace.hanteijiyu";
	public static final String REPLACE_DATA ="replace.data";
	public static final String ERR_KESSANKIKBN		= "err.kessankiKbn";

	/************************
	 * 1.5次版用
f	 ************************/
	//コメント表示画面
	public static final String LABEL_SHOW_C			= "label.show_c";
	public static final String LABEL_CUST_CD			= "label.cust_cd";
	public static final String LABEL_TORIHIKISAKI_NM	= "label.torihikisaki_nm";
	public static final String LABEL_COMMENT_KBN		= "label.comment_kbn";
	public static final String LABEL_COMMENT_INS_USR	= "label.comment_ins_usr";
	public static final String LABEL_COMMENT_NAIYO	= "label.comment_naiyo";
	//連結区分マスタアップロード画面
	public static final String LINK_MASTER_DL			= "link.master_dl";
	public static final String LABEL_UP_FILE			= "label.up_file";
	public static final String LABEL_CSTM_CD			= "label.cstm_cd";
	public static final String LABEL_YM				= "label.ym";
	public static final String LABEL_GROUP_DIV		= "label.group_div";
	public static final String LABEL_DEL_FLG			= "label.del_flg";
	//添付選択・添付照会画面
	public static final String LABEL_FILE_NM          = "label.file_nm";
	public static final String LABEL_PHASE			= "label.phase";
	public static final String LABEL_OWNER			= "label.owner";
	//課題No.09
	//追加開始
	public static final String LABEL_EDABAN			= "label.edaban";
	//添付選択画面
	public static final String TITLE_05_00            = "title_05_00";
	public static final String MSG_0020               = "msg.0020";
	public static final String LABEL_ATTACHMENT       = "label.attachment";
	//添付照会画面
	public static final String TITLE_05_01 			= "title_05_01";
	//引当金検証画面
	public static final String TITLE_02_04					= "title_02_04";
	public static final String TITLE_00_01BOTTOM				= "title_00_01bottom";
	public static final String BTN_TMPSENTAKU					= "btn.tempusentaku";
	public static final String LINK_COMMENT					= "link.comment";
	//課題No.73
	//追加開始
	public static final String ERR_CHK_SAIKENTORIKBN			= "err.chk_saikentorikbn";
	public static final String LABEL_SEIJO					= "label.seijo";
	public static final String LABEL_YOUTYUUI					= "label.youtyuui";
	//追加完了
	public static final String LABEL_TORIHIKISAKI				= "label.torihikisaki";
	public static final String LABEL_OP						= "label.op";
	public static final String LABEL_C_RATING					= "label.c_rating";
	public static final String LABEL_OYA_KAISYA				= "label.oya_kaisya";
	public static final String LABEL_OYA_ITTAI				= "label.oya_ittai";
	public static final String LABEL_OYA_DOKURITU				= "label.oya_dokuritu";
	public static final String LABEL_A_PERSON					= "label.a_person";
	public static final String LABEL_SATEI_YM					= "label.satei_ym";
	public static final String LABEL_BASE_YEAR				= "label.base_year";
	public static final String LABEL_SEIJO_YOUTYUUI			= "label.seijo_youtyuui";
	public static final String LABEL_KASHITAORE				= "label.kashitaore";
	public static final String LABEL_HASAN_KOSEI				= "label.hasan_kosei";
	public static final String LABEL_IPAN_SAIKEN				= "label.ipan_saiken";
	public static final String LABEL_KASHITAORE_KENEN_SAIKEN	= "label.kashitaore_kenen_saiken";
	public static final String LABEL_HASAN_KOSEI_SAIKEN		= "label.hasan_kousei_saiken";
	public static final String LABEL_CRED_CATEGORY			= "label.cred_category";
	public static final String LABEL_CUST_CATEGORY			= "label.cust_category";
	public static final String LABEL_HIKIATE_TAISYO			= "label.hikiate_taisyo";
	public static final String LABEL_TUIKA_HIKIATE			= "label.tuika_hikiate";
	public static final String LABEL_TUIKA_KEISAN				= "label.tuika_keisan";
	public static final String LABEL_TUUKA					= "label.tuuka";
	public static final String LABEL_COLON					= "label.colon";
	public static final String LABEL_UKETORI					= "label.uketori";
	public static final String LABEL_YUSYUTSU_UKETORI			= "label.yusyutsu_uketori";
	public static final String LABEL_UTIKAKE					= "label.utikake";
	public static final String LABEL_TORI_TOKIN				= "label.tori_tokin";
	public static final String LABEL_TATEKAE					= "label.tatekae";
	public static final String LABEL_MISYUNYU					= "label.misyunyu";
	public static final String LABEL_MISYUSYU					= "label.misyusyu";
	public static final String LABEL_TANKI_KASHI				= "label.tanki_kashi";
	public static final String LABEL_SASHIIRE					= "label.sashiire";
	public static final String LABEL_KARIBARAI				= "label.karibarai";
	public static final String LABEL_TYOKI_KASHI				= "label.tyoki_kashi";
	public static final String LABEL_SONOTA_TOSHI				= "label.sonota_toshi";
	public static final String LABEL_IPANSAIKEN_KEI			= "label.ipansaiken_kei";
	public static final String LABEL_KOTEI_EIGYO				= "label.kotei_eigyo";
	public static final String LABEL_SAIKEN_ZAN				= "label.saiken_zan";
	public static final String LABEL_HOSYO_SAIMU				= "label.hosyo_saimu";
	public static final String LABEL_KIBIKIATE				= "label.kibikiate";
	public static final String LABEL_ONE						= "label.one";
	public static final String LABEL_TWO						= "label.two";
	public static final String LABEL_THREE					= "label.three";
	public static final String LABEL_FOUR						= "label.four";
	public static final String LABEL_FIVE						= "label.five";
	public static final String LABEL_SIX						= "label.six";
	public static final String LABEL_RESERVATION				= "label.reservation";
	public static final String LABEL_OP_RYUHO					= "label.op_ryuho";
	public static final String LABEL_RYUHO_SAIMU_KEI			= "label.ryuho_saimu_kei";
	public static final String LABEL_HOZEN					= "label.hozen";
	public static final String LABEL_SONOTA_KAISYU			= "label.sonota_kaisyu";
	public static final String LABEL_RIKO_SEIKYU				= "label.riko_seikyu";
	public static final String LABEL_HIKIATE_KENSYO_ZAN		= "label.hikiate_kensyo_zan";
	public static final String LABEL_HIKIATE_HOSEI			= "label.hikiate_hosei";
	public static final String LABEL_HOSEIGO_HIKIATE			= "label.hoseigo_hikiate";
	public static final String LABEL_HOSEI_HIKIATE_KOJO		= "label.hosei_hikiate_kojo";
	public static final String LABEL_HIKIATE_KONKYO			= "label.hikiate_konkyo";
	//引当金検証タブ
	public static final String LABEL_HYOJI_DATA				= "label.hyoji_data";
	public static final String LABEL_RESERVE_K				= "label.reserve_k";
	//抽出条件マスタ画面
	public static final String TITLE_04_00_02					= "title_04_00_02";
	public static final String LABEL_SYS						= "label.sys";
	public static final String LABEL_SYORI_KBN				= "label.syori_kbn";
	public static final String LABEL_SATEI_CO					= "label.satei_co";
	public static final String LABEL_MISE_CD					= "label.mise_cd";
	public static final String LABEL_KESSANKI_KBN				= "label.kessanki_kbn";
	public static final String LABEL_KENTOU_J					= "label.kentou_j";
	public static final String LABEL_SATEI_TAISYOU			= "label.satei_taisyou";
	public static final String LABEL_TAIRYU_J					= "label.tairyu_j";
	public static final String CONFIRM_0014					= "confirm.0014";
	public static final String CONFIRM_0004					= "confirm.0004";
	public static final String LABEL_NEW_REGISTER				= "label.new_register";
	public static final String LABEL_UPDATE					= "label.update";
	public static final String LABEL_DELETE					= "label.delete";
	public static final String LABEL_TAIRYU_KIKAN				= "label.tairyu_kikan";
	public static final String LABEL_MON						= "label.mon";
	public static final String LABEL_SHITEI					= "label.shitei";
	public static final String LABEL_SAIKEN_FLG				= "label.saiken_flg";
	public static final String LABEL_IPPAN					= "label.ippan";
	public static final String LABEL_KOTEI					= "label.kotei";
	public static final String LABEL_HOSYOU					= "label.hosyou";
	public static final String LABEL_HIKIATE					= "label.hikiate";
	public static final String LABEL_DATA_S					= "label.data_s";
	public static final String LABEL_KAKO_KTK_FLG				= "label.kako_ktk_flg";
	public static final String LABEL_KAKO_KTK					= "label.kako_ktk";
	public static final String LABEL_KAKO_KTK_SANSYO			= "label.kako_ktk_sansyo";
	public static final String LABEL_GENZAI_KOTEI_SAIKENGAKU	= "label.genzai_kotei_saikengaku";
	public static final String LABEL_KAKO_KOTEI_SAIKENGAKU	= "label.kako_kotei_saikengaku";
	public static final String LABEL_KOTEI_SAIKENGAKU_SANSYO	= "label.kotei_saikengaku_sansyo";
	public static final String LABEL_KAGETUMAE				= "label.kagetumae";
	public static final String LABEL_FLGSAKI_FLG				= "label.flgsaki_flg";
	public static final String GUIDANCE_0008					= "guidance.0008";
	public static final String LABEL_REASON_I					= "label.reason_i";
	public static final String LABEL_JOKEN_NAME				= "label.joken_name";
	public static final String LABEL_KAKUTSUKE				= "label.kakutsuke";
	public static final String LABEL_KINGAKU 					= "label.kingaku";
	public static final String LABEL_FROM						= "label.from";
	public static final String LABEL_TO						= "label.to";
	public static final String LABEL_KENTOU_T 				= "label.kentou_t";
	public static final String LABEL_TAIRYU					= "label.tairyu";
	public static final String LABEL_SATEI_T					= "label.satei_t";
	public static final String LABEL_DATA_FLG					= "label.data_flg";
	public static final String LABEL_KENTOU_FLG				= "label.kentou_flg";
	public static final String LABEL_TAIRYU_FLG				= "label.tairyu_flg";
	public static final String LABEL_SAIMUCHOKA				= "label.saimuchoka";
	public static final String LABEL_AKAJI					= "label.akaji";
	public static final String LABEL_RIKI						= "label.riki";
	public static final String LABEL_MUKAKUTSUKE				= "label.mukakutsuke";
	public static final String CONFIRM_0017					= "confirm.0017";
	//チャンピオン部一覧画面
	public static final String TITLE_04_03					= "title_04_03";
	public static final String LABEL_CUST_NM					= "label.cust_nm";
	public static final String LABEL_COUNTRY					= "label.country";
	public static final String LABEL_MONTH					= "label.month";
	public static final String LABEL_TAD						= "label.tad";
	public static final String LABEL_CO						= "label.co";
	public static final String LABEL_DEPT						= "label.dept";
	public static final String LABEL_OJ_TTL					= "label.oj_ttl";
	public static final String LABEL_SAIKEN_ZANKEI			= "label.saiken_zankei";
	public static final String LABEL_SELECT					= "label.select";
	//査定内容照会一覧画面
	public static final String W_INPUT_0001					= "warning.input.0001";
}
