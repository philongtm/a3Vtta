/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/27		SSC				課題No.28 引当対象外/帳簿外対応 
003		2011/06/30		SSC				案件No.D9059 もぎ取り解除時に記入内容をクリアしない
******************************************************************************/

package app.hikiate.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.hikiate.dbAcc.KakuninDbAcc;
import app.hikiate.form.KakuninForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *  OD1102_引当金確認 ビジネスロジッククラス<br>
 */
public class KakuninBss {
    
    private SessionData cmnData;            // 共通セッション
	private UserBean user_bean = null;     // ユーザ情報
    private TorihikisakiBean tori_bean;     // 取引先情報
    private KakuninForm form;                // アクションフォーム

    private AppContext appContext       = null; // ＡＰＰコンテキスト
    private SqlExecuter sqlExec         = null; // ＤＢアクセス
    private Log log                     = null; // LOG
    
    private static final String NUM_FMT_KOKUNAI = "##,###,###,###,###,##0.00";      // 数字のフォーマット：海外
    private static final String NUM_FMT_KAIGAI  = "##,###,###,###,###,##0";      	   // 数字のフォーマット：国内
    
    private static final String POINT_01 					= "01";						// 前期と今期の登録箇所
    private static final String POINT_80 					= "80";						// 仮基準日の登録箇所
    private static final String OPE_KBN_40				= "40";						// 入力区分:もぎ取り解除
    private static final String OPE_KBN_20				= "20";						// 入力区分:一時保存
    private static final String OPE_KBN_10				= "10";						// 入力区分:登録
    private static final String STATUS_10					= "10";						// ステータス:未処理
    private static final String STATUS_20					= "20";						// ステータス:処理中
    private static final String MODOSHI_FLG_0				= "0";						// 取戻不可フラグ:取戻可
    private static final String MODOSHI_FLG_1				= "1";						// 取戻不可フラグ:取戻不可   
    
	private static final String TAISHO_YM					= "taisho_ym";	
	private static final String TAISHO_YM_HYOUJI			= "taisho_ym_hyoji";	
	
    private static final String KTK 						= "ktk";					// 信用格付情報
    private static final String OYAKTK 					= "oya_ktk";				// 親会社信用格付情報
    private static final String OYAITTAI 					= "oya_ittai";				// 親会社一体独立
    private static final String OYA_NM 					= "oya_nm";					// 親会社名称 
    private static final String TAIRYU_KBN_NM 			= "tairyu_kbn_nm";			// 滞留区分名称
    private static final String KONKI_TORIHIKISAKI_KBN 	= "konki_torihikisaki_kbn";	// 今期取引先区分
    private static final String KONKI_SAIKEN_KBN 			= "konki_saiken_kbn";		// 今期債権区分
    private static final String KARI_TORIHIKISAKI_KBN 	= "konki_torihikisaki_kbn";	// 取引先区分
    private static final String KARI_SAIKEN_KBN 			= "konki_saiken_kbn";		// 債権区分
    private static final String KOMOKU1 					= "komoku1";				// 項目１
    private static final String RYUHOSAIMU 				= "ryuhosaimu";				// 留保債務
    private static final String OTH_RYUHOSAIMU 			= "oth_ryuhosaimu";			// 第三者留保債務
    private static final String HOZEN 					= "hozen";					// 保全
    private static final String SONOTAKAISYU 				= "sonotakaisyu";			// その他回収
    private static final String RIKO_KENEN 				= "riko_kenen";				// 履行請求懸念
    private static final String TUIKA_HIKIATE 			= "tuika_hikiate";			// 追加引当金額
    private static final String KOMOKU2 					= "komoku2";				// 項目２
    private static final String TAIRYU_KBN 				= "tairyu_kbn";				// 滞留区分
    private static final String KIJUN_SAIKEN_KBN 			= "kijun_saiken_kbn";		// 仮基準日債権区分名称
    private static final String KIJUN_TORIHIKISAKI_KBN 	= "kijun_torihikisaki_kbn";	// 仮基準日取引先区分名称
    private static final String SAIKEN_KBN_NM 			= "saiken_kbn_nm";			// 債権区分名称
    private static final String TORIHIKISAKI_KBN_NM 		= "torihikisaki_kbn_nm";	// 取引先区分名称
    private static final String TUUKA_CD					= "tuuka_cd";				// 通貨コード
    private static final String KANJO_HYOUJI_KBN			= "kanjo_hyouji_kbn";				// 勘定科目表示区分
    private static final String KINGAKU					= "kingaku";				// 金額
    private static final String COMMENT_VAL				= "comment_val";			// コメント内容
    private static final String JISHI_PHASE 				= "JISHI_PHASE";            // 実施フェーズ
    private static final String JI_JISHI_PHASE          	= "JI_JISHI_PHASE";     	// 次実施フェーズ
    private static final String JI_KAISHI_STATUS        	= "JI_KAISHI_STATUS";   	// 次開始ステータス
    private static final String OUTPHASE 					= "outPhase";				// 輸出フェーズ
    private static final String OUTHYOJIYM 				= "outHyojiYm";				// 輸出表示用年月
    private static final String OUTYM 					= "outYm";					// 輸出年月
    private static final String OUTSYORIKAISU 			= "outSyoriKaisu";			// 輸出処理回数
	// 課題No.28
	// 追加開始
	private static final String HIKIATEKIN_SHOSAI				= "hikiatekin_shosai";			// 引当金詳細
	// 追加完了

    /**
     * コンストラクタ <br>
     * 
     * @param appContext AppContext
     * @throws Exception 
     */
    public KakuninBss(AppContext appContext) throws Exception {
        this.appContext = appContext;
        this.log = appContext.getLog();
        this.cmnData = appContext.getCMN();
        this.user_bean = cmnData.getUser_bean();
        this.tori_bean = cmnData.getTori_bean();
        this.form = (KakuninForm) appContext.getActionForm(); 
    }

    /**
     * 画面初期表示値取得(メニューリンクから遷移時) <br>
     * 
     * @return forward
     * @throws Exception 
     */
    public String executeInit() throws Exception {
    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        KakuninDbAcc dbacc = new KakuninDbAcc(sqlExec, log, appContext);
        
        // 共)ユーザ情報.実施業務【リスト】
        for(int i = 0; i < user_bean.getComOparation().size(); i++){
        	Map<String,String> map = (Map<String,String>) user_bean.getComOparation().get(i);
        	if(map.get(JI_JISHI_PHASE).equals(tori_bean.getPhase()) && GS.STATUS_MISYORI.equals(map.get(GS.KAISHI_STATUS))){
        		// 実施フェーズ
        		form.setJishi_phase(map.get(JISHI_PHASE));
        		// 次実施フェーズを設定
                form.setJi_jishi_phase(map.get(JI_JISHI_PHASE)); 
                // 次開始ステータス
                form.setJi_kaishi_status(map.get(JI_KAISHI_STATUS));
        	}
        }
        
        // 承認担当者セレクトボックス設定値取得する
        dbacc.getTanto();
        
        // 取引先区分【リスト】を取得する
        dbacc.getTorihiki();
        
        // 債権区分【リスト】を取得する
        dbacc.getSaiken();
        
    	// 課題No.28
    	// 追加開始
        // 引当金詳細【リスト】を取得する
        dbacc.getHikiatekinShosai();
    	// 追加完了
        
        // 前期データ取得用案件Noを取得する
        String zenkiAnkenNo = dbacc.getZenkiAnkenNo();
        
        // 前期データ取得用案件No.のフェーズ及び年月を取得する
        Map<String,String> map1 = dbacc.getYm(zenkiAnkenNo);
        
        // 前期.フェーズを取得する
        String zenkiPhase = map1.get(OUTPHASE);
        
        // 仮基準日データ取得用案件Noを取得する
        String kijunAnkenNo = dbacc.getKijunAnkenNo();

        if(kijunAnkenNo == null){
        	form.setSateiKekkaBtnFlg(false);
        }
        
        // 仮基準日データ取得用案件No.のフェーズ及び年月を取得する
        Map<String,String> map2 = dbacc.getYm(kijunAnkenNo);
        
        // 仮基準日.フェーズを取得する
        String kijunPhase = map2.get(OUTPHASE);
        
        // 前期データデータの基本情報を取得する
        Map<String,String> map3 = dbacc.getKtk(map1.get(OUTYM),map1.get(OUTSYORIKAISU)); 
        
        // 仮基準日データの基本情報を取得する
        Map<String,String> map4 = dbacc.getKtk(map2.get(OUTYM),map2.get(OUTSYORIKAISU));
        
        // 今期の親会社名称を取得する
        String oyaKaisya = tori_bean.getOya_business_nm();
        
        // 前期データ・滞留区分、債権区分、取引先区分、引当金確認の取得
        Map<String,String> map5 = dbacc.getSelT15(zenkiAnkenNo,zenkiPhase);
        
        // 仮基準日の変更後取引先区分・変更後債権区分初期化
        dbacc.uptT15_init(kijunAnkenNo,kijunPhase);
        dbacc.commit();
        // 仮基準日データ・滞留区分、債権区分、取引先区分、引当金確認の取得
        Map<String,String> map6 = dbacc.getSelT15(kijunAnkenNo,kijunPhase);
        
        // 今期データ・滞留区分、債権区分、取引先区分、引当金確認の取得
        Map<String,String> map7 = dbacc.getSelT15(tori_bean.getAnken_no(),tori_bean.getPhase());
        
        // 今期データがNULLの場合、仮基準日データと一致させる
        if(map7.get(KONKI_TORIHIKISAKI_KBN) == null && map6.get(KARI_TORIHIKISAKI_KBN) != null){
        	map7.put(KONKI_TORIHIKISAKI_KBN, new String(map6.get(KARI_TORIHIKISAKI_KBN)));
        }
        if(map7.get(KONKI_SAIKEN_KBN) == null && map6.get(KARI_SAIKEN_KBN) != null){
        	map7.put(KONKI_SAIKEN_KBN, new String(map6.get(KARI_SAIKEN_KBN)));
        }
        
        String zenSateiki = this.getZenSateiki(tori_bean.getSatei_ki());
        
        // 前期の引当金確認情報を取得
        List<Map<String,String>> list1 = new ArrayList<Map<String,String>>();
        if(GS.EMPTY_CHARCTER.equals(Function.trim(zenkiAnkenNo))){
			list1 = dbacc.getMeisai(zenSateiki);
            if(list1.size() > 0){
    			form.setZenki_ym(this.getSateikiHyouji(zenSateiki,cmnData.getComLangMode()));
            }
        }else{
            list1 = dbacc.getSelT18(zenkiAnkenNo);
        }
        
        Map<String,String> ymMap = dbacc.getTaishoYmMap(tori_bean.getSatei_ki());
    	// 仮基準日の引当金確認情報を取得
        List<Map<String,String>> list2 = new ArrayList<Map<String,String>>();
        if(GS.EMPTY_CHARCTER.equals(Function.trim(kijunAnkenNo))){
        	list2 = dbacc.getMeisai(ymMap.get(TAISHO_YM));
            if(list2.size() > 0){
            	form.setKijun_ym(ymMap.get(TAISHO_YM_HYOUJI));
            }
        }else{
            list2 = dbacc.getSelT17(kijunAnkenNo);
        }
        
        // 今期の引当金確認情報を取得
        List<Map<String,String>> list3 = dbacc.getSelT18(tori_bean.getAnken_no());
        
        // 前期.区分判定根拠を取得する
        Map<String,String> map8 = dbacc.getSelT12(zenkiAnkenNo, zenkiPhase, POINT_01);
        
        // 仮基準日.引当金算定根拠を取得する
        Map<String,String> map9 = dbacc.getSelT12(kijunAnkenNo, kijunPhase, POINT_80);
        
        // 今期.区分判定根拠を取得する
        Map<String,String> map10 = dbacc.getSelT12(tori_bean.getAnken_no(),tori_bean.getPhase(),POINT_01);
        
        
        // 取引先
        form.setKanjyo_cd(tori_bean.getKanjo_cd());
        
        // 取引先名
        form.setKanjyo_nm(tori_bean.getKanjo_nm());
        
        if(!(GS.EMPTY_CHARCTER.equals(Function.trim(map1.get(OUTHYOJIYM))))){
            // 前期.表示用年月を設定
            form.setZenki_ym(map1.get(OUTHYOJIYM));
        }
        
        // 単位
        if(list1.size() > 0){
            form.setTuuka_cd(list1.get(0).get(TUUKA_CD));
        }
        if(list2.size() > 0){
            form.setTuuka_cd(list2.get(0).get(TUUKA_CD));
        }
        if(list3.size() > 0){
            form.setTuuka_cd(list3.get(0).get(TUUKA_CD));
        }
        
        if(!(GS.EMPTY_CHARCTER.equals(Function.trim(map2.get(OUTHYOJIYM))))){
            //仮基準日.表示用年月を設定
            form.setKijun_ym(map2.get(OUTHYOJIYM));
        }
        
        // 今期.表示用年月を設定
        form.setKonki_ym(tori_bean.getTaisyo_ym_hyoji());
        
        // 前期.信用格付を設定
        form.setZenki_ktk(map3.get(KTK));
        
        // 仮基準日.信用格付を設定
        form.setKijun_ktk(map4.get(KTK));
        
        // 今期.信用格付を設定
        form.setKonki_ktk(tori_bean.getSinyoktk());
        
        // 前期.親会社信用格付を設定
        form.setZenki_oya_ktk(map3.get(OYAKTK));
        
        // 仮基準日.親会社信用格付を設定
        form.setKijun_oya_ktk(map4.get(OYAKTK));
        
        // 今期.親会社信用格付を設定
        form.setKonki_oya_ktk(tori_bean.getOya_ktk());
        
        // 前期.親会社名称を設定
        form.setZenki_company_nm(map3.get(OYA_NM));
        
        // 仮基準日.親会社名称を設定
        form.setKijun_company_nm(map4.get(OYA_NM));
        
        // 今期.親会社名称を設定
        form.setKonki_company_nm(oyaKaisya);
        
        // 前期.親会社一体独立を設定
        form.setZenki_oya_flg(map3.get(OYAITTAI));
        
        // 仮基準日.親会社一体独立を設定
        form.setKijun_oya_flg(map4.get(OYAITTAI));
        
        // 今期.親会社一体独立を設定
        form.setKonki_oya_flg(tori_bean.getOya_ittai_dokuritu());
        
        // 前期.滞留区分を設定
        form.setZenki_tairyu_kbn(map5.get(TAIRYU_KBN));
        
        // 仮基準日.滞留区分を設定
        form.setKijun_tairyu_kbn(map6.get(TAIRYU_KBN));
        
        // 今期.滞留区分を設定
        form.setKonki_tairyu_kbn(map7.get(TAIRYU_KBN));
        
        // 前期.滞留区分名称を設定
        form.setZenki_tairyu_kbn_nm(map5.get(TAIRYU_KBN_NM));
        
        // 仮基準日.滞留区分名称を設定
        form.setKijun_tairyu_kbn_nm(map6.get(TAIRYU_KBN_NM));
        
        // 今期.滞留区分名称を設定
        form.setKonki_tairyu_kbn_nm(map7.get(TAIRYU_KBN_NM));
        
        // 前期.取引先区分を設定
        form.setZenki_torihikisaki_kbn(map5.get(TORIHIKISAKI_KBN_NM));
        
        // 仮基準日.取引先区分を設定
        form.setKijun_torihikisaki_kbn(map6.get(KIJUN_TORIHIKISAKI_KBN));
        
        // 今期.取引先区分を設定
        form.setKonki_torihikisaki_kbn(map7.get(KONKI_TORIHIKISAKI_KBN));
        // 今期.取引先区分を設定hidden
        form.setHiddn_torihikisaki_kbn(map6.get(KARI_TORIHIKISAKI_KBN));
        
        // 前期.債権区分を設定
        form.setZenki_saiken_kbn(map5.get(SAIKEN_KBN_NM));
        
        // 仮基準日.債権区分を設定
        form.setKijun_saiken_kbn(map6.get(KIJUN_SAIKEN_KBN));
        
        // 今期.債権区分を設定
        form.setKonki_saiken_kbn(map7.get(KONKI_SAIKEN_KBN));
        // 今期.債権区分を設定
        form.setHiddn_saiken_kbn(map6.get(KARI_SAIKEN_KBN));

        // 課題No.28
        // 追加開始
        // 引当金詳細を設定
        form.setHikiatekin_shosai(map7.get(HIKIATEKIN_SHOSAI));
        // 追加完了
        
        // 前期.既引当金⑥
        double zenkiKiHikiateKin = 0;
        
        // 前期の引当金確認情報を設定
        for(int i = 0; i < list1.size(); i++){
        	Map<String,String> map = (Map<String,String>)list1.get(i);
        	switch (Integer.parseInt(map.get(KANJO_HYOUJI_KBN))){
        		// 前期.受取手形を設定
        		case 1:
        			form.setZenki_uketori_tegata(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 前期.輸出受取手形を設定
        		case 2:
        			form.setZenki_yushutu_uketori_tegata(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        		
        		// 前期.売掛金を設定
        		case 3:
        			form.setZenki_urikake_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 前期.取引前渡を設定
        		case 4:
        			form.setZenki_torihiki_maewatashie_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        		
        		// 前期.立替金を設定
        		case 5:
        			form.setZenki_tatekae_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 前期.未収入金を設定
        		case 6:
        			form.setZenki_mishuunyuu_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 前期.未収収益を設定
        		case 7:
        			form.setZenki_mishuu_shuueki(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 前期.短期貸付金を設定
        		case 8:
        			form.setZenki_tanki_kashituke_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 前期.差入保証金を設定
        		case 9:
        			form.setZenki_sashiire_hoshou_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 前期.仮払金を設定
        		case 10:
        			form.setZenki_karibarai_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 前期.長期貸付金を設定
        		case 11:
        			form.setZenki_chouki_kashituke_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 前期.その他投資を設定
        		case 12:
        			form.setZenki_sonota_toushi(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 前期.保証債務合計を設定
        		case 14:
        			form.setZenki_hoshou_saimu_goukei(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        		
        		// 前期.既引当金⑥を設定
        		case 15:
        			zenkiKiHikiateKin = Function.getValueOfDouble(map.get(KINGAKU));
        			
        			form.setZenki_ki_hikiate_kin(formatKingaku(zenkiKiHikiateKin, user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		default:
        			
        			break;
        	}
        }
               
        // 仮基準日.既引当金⑥
        double kijunKiHikiateKin = 0;
        
        // 仮基準日の引当金確認情報を設定
        for(int i = 0; i < list2.size(); i++){
        	Map<String,String> map = (Map<String,String>)list2.get(i);
        	switch (Integer.parseInt(map.get(KANJO_HYOUJI_KBN))){
        		// 仮基準日.受取手形を設定
        		case 1:
        			form.setKijun_uketori_tegata(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 仮基準日.輸出受取手形を設定
        		case 2:
        			form.setKijun_yushutu_uketori_tegata(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        		
        		// 仮基準日.売掛金を設定
        		case 3:
        			form.setKijun_urikake_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 仮基準日.取引前渡を設定
        		case 4:
        			form.setKijun_torihiki_maewatashie_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        		
        		// 仮基準日.立替金を設定
        		case 5:
        			form.setKijun_tatekae_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 仮基準日.未収入金を設定
        		case 6:
        			form.setKijun_mishuunyuu_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 仮基準日.未収収益を設定
        		case 7:
        			form.setKijun_mishuu_shuueki(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 仮基準日.短期貸付金を設定
        		case 8:
        			form.setKijun_tanki_kashituke_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 仮基準日.差入保証金を設定
        		case 9:
        			form.setKijun_sashiire_hoshou_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 仮基準日.仮払金を設定
        		case 10:
        			form.setKijun_karibarai_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 仮基準日.長期貸付金を設定
        		case 11:
        			form.setKijun_chouki_kashituke_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 仮基準日.その他投資を設定
        		case 12:
        			form.setKijun_sonota_toushi(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 仮基準日.保証債務合計を設定
        		case 14:
        			form.setKijun_hoshou_saimu_goukei(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        		
        		// 仮基準日.既引当金⑥を設定
        		case 15:
        			kijunKiHikiateKin = Function.getValueOfDouble(map.get(KINGAKU));
        			
        			form.setKijun_ki_hikiate_kin(formatKingaku(kijunKiHikiateKin, user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		default:
        			
        			break;
        	}
        }
        
        // 今期.既引当金⑥
        double konkiKiHikiateKin = 0;
        
        // 今期の引当金確認情報を設定
        for(int i = 0; i < list3.size(); i++){
        	Map<String,String> map = (Map<String,String>)list3.get(i);
        	switch (Integer.parseInt(map.get(KANJO_HYOUJI_KBN))){
        		// 今期.受取手形を設定
        		case 1:
        			form.setKonki_uketori_tegata(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 今期.輸出受取手形を設定
        		case 2:
        			form.setKonki_yushutu_uketori_tegata(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        		
        		// 今期.売掛金を設定
        		case 3:
        			form.setKonki_urikake_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 今期.取引前渡を設定
        		case 4:
        			form.setKonki_torihiki_maewatashie_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        		
        		// 今期.立替金を設定
        		case 5:
        			form.setKonki_tatekae_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 今期.未収入金を設定
        		case 6:
        			form.setKonki_mishuunyuu_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 今期.未収収益を設定
        		case 7:
        			form.setKonki_mishuu_shuueki(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 今期.短期貸付金を設定
        		case 8:
        			form.setKonki_tanki_kashituke_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 今期.差入保証金を設定
        		case 9:
        			form.setKonki_sashiire_hoshou_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 今期.仮払金を設定
        		case 10:
        			form.setKonki_karibarai_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 今期.長期貸付金を設定
        		case 11:
        			form.setKonki_chouki_kashituke_kin(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 今期.その他投資を設定
        		case 12:
        			form.setKonki_sonota_toushi(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		// 今期.保証債務合計を設定
        		case 14:
        			form.setKonki_hoshou_saimu_goukei(formatKingaku(Function.getValueOfDouble(
        					 map.get(KINGAKU)), user_bean.getComWorkflowSystemkbn()));
        			break;
        		
        		// 今期.既引当金⑥を設定
        		case 15:
        			konkiKiHikiateKin = Function.getValueOfDouble(map.get(KINGAKU));
        			
        			form.setKonki_ki_hikiate_kin(formatKingaku(konkiKiHikiateKin, user_bean.getComWorkflowSystemkbn()));
        			break;
        			
        		default:
        			
        			break;
        	}
        }
        
        // 前期.一般債権計
        double zenkiKingakuSum = 0;
        // 前期.一般債権計(前期.受取手形～仮基準日.その他投資の合計)
        for(int i = 0; i < list1.size(); i++){
        	Map<String,String> map = (Map<String,String>)list1.get(i);
        	if(Integer.parseInt(map.get(KANJO_HYOUJI_KBN)) <= 12){
        		double kingaku = zenkiKingakuSum + Double.parseDouble(map.get(KINGAKU))*100;
        		zenkiKingakuSum = kingaku;
        	}
        }
        
        // 前期.一般債権計を設定
        zenkiKingakuSum = Function.marume(zenkiKingakuSum);
        form.setZenki_ippan_saiken_kei(formatKingaku(zenkiKingakuSum/100,user_bean.getComWorkflowSystemkbn()));
        
        // 仮基準日.一般債権計
        double kijunKingakuSum = 0;
        // 仮基準日.一般債権計(仮基準日.受取手形～仮基準日.その他投資の合計)
        for(int i = 0; i < list2.size(); i++){
        	Map<String,String> map = (Map<String,String>)list2.get(i);
        	if(Integer.parseInt(map.get(KANJO_HYOUJI_KBN)) <= 12){
        		double kingaku = kijunKingakuSum + Double.parseDouble(map.get(KINGAKU))*100;
        		kijunKingakuSum = kingaku;
        	}
        }
        // 仮基準日.一般債権計を設定
        kijunKingakuSum = Function.marume(kijunKingakuSum);
        form.setKijun_ippan_saiken_kei(formatKingaku(kijunKingakuSum/100,user_bean.getComWorkflowSystemkbn()));
        
        // 今期.一般債権計
        double konkiKingakuSum = 0;
        // 今期.一般債権計(今期.受取手形～今期.その他投資の合計)
        for(int i = 0; i < list3.size(); i++){
        	Map<String,String> map = (Map<String,String>)list3.get(i);
        	if(Integer.parseInt(map.get(KANJO_HYOUJI_KBN)) <= 12){
        		double kingaku = konkiKingakuSum + Double.parseDouble(map.get(KINGAKU))*100;
        		konkiKingakuSum = kingaku;
        	}
        }
        // 今期.一般債権計を設定
        konkiKingakuSum = Function.marume(konkiKingakuSum);
        form.setKonki_ippan_saiken_kei(formatKingaku(konkiKingakuSum/100,user_bean.getComWorkflowSystemkbn()));
        
        // 仮基準日.通貨調整1を設定
        form.setKijun_komoku1(formatKingaku(Function.getValueOfDouble(map6.get(KOMOKU1)),user_bean.getComWorkflowSystemkbn()));
                
        // 前期.債権残高合計①
        double zenkiZandaka = zenkiKingakuSum + Function.getValueOfDouble(map5.get(KOMOKU1))*100;
        // 前期.債権残高合計①を設定
        zenkiZandaka = Function.marume(zenkiZandaka);
        form.setZenki_saiken_zandaka_goukei(formatKingaku(zenkiZandaka/100,user_bean.getComWorkflowSystemkbn()));
        
        // 仮基準日.債権残高合計①
        double kijunZandaka = kijunKingakuSum + Function.getValueOfDouble(map6.get(KOMOKU1))*100;
        // 仮基準日.債権残高合計①を設定
        kijunZandaka = Function.marume(kijunZandaka);
        form.setKijun_saiken_zandaka_goukei(formatKingaku(kijunZandaka/100,user_bean.getComWorkflowSystemkbn()));
        
        // 今期.債権残高合計①
        double konkiZandaka = konkiKingakuSum + Function.getValueOfDouble(map7.get(KOMOKU1))*100;
        // 今期.債権残高合計①を設定
        konkiZandaka = Function.marume(konkiZandaka);
        form.setKonki_saiken_zandaka_goukei(formatKingaku(konkiZandaka/100,user_bean.getComWorkflowSystemkbn()));
        
        // 仮基準日.留保債務を設定
        form.setKijun_ryuhosaimu(formatKingaku(Function.getValueOfDouble(map6.get(RYUHOSAIMU)),user_bean.getComWorkflowSystemkbn()));
        
        // 仮基準日.第三者留保債務を設定
        form.setKijun_oth_ryuhosaimu(formatKingaku(Function.getValueOfDouble(map6.get(OTH_RYUHOSAIMU)),user_bean.getComWorkflowSystemkbn()));
        
        // 仮基準日.留保債務計②
        double kijunRyuuhoSaimuKei = Function.getValueOfDouble(map6.get(RYUHOSAIMU))*100 + Function.getValueOfDouble(map6.get(OTH_RYUHOSAIMU))*100;
        // 仮基準日.債権残高合計①より大きい場合は同額を表示
        if(kijunRyuuhoSaimuKei > kijunZandaka){
        	kijunRyuuhoSaimuKei = kijunZandaka;
        }
        // 仮基準日.債権残高合計①がマイナスの場合は'0'(海外版'0.00')を表示
        if(kijunZandaka < 0){
        	kijunRyuuhoSaimuKei = 0;
        }
        // 仮基準日.留保債務計②を設定
        kijunRyuuhoSaimuKei = Function.marume(kijunRyuuhoSaimuKei);
        form.setKijun_ryuuho_saimu_kei(formatKingaku(kijunRyuuhoSaimuKei/100,user_bean.getComWorkflowSystemkbn()));
        
        // 仮基準日.保全③
        double kijunHozen = Function.getValueOfDouble(map6.get(HOZEN));
        // 仮基準日.保全③を設定
        form.setKijun_hozen(formatKingaku(kijunHozen,user_bean.getComWorkflowSystemkbn()));
        
        // 仮基準日.その他回収④
        double kijunSonotakaisyu = Function.getValueOfDouble(map6.get(SONOTAKAISYU));
        // 仮基準日.その他回収④を設定
        form.setKijun_sonotakaisyu(formatKingaku(kijunSonotakaisyu,user_bean.getComWorkflowSystemkbn()));
        
        // 仮基準日.履行請求懸念⑤
        double kijunRiko_kenen = Function.getValueOfDouble(map6.get(RIKO_KENEN));
        // 仮基準日.履行請求懸念⑤を設定
        form.setKijun_riko_kenen(formatKingaku(kijunRiko_kenen,user_bean.getComWorkflowSystemkbn()));
        
        // 仮基準日.引当対象金額
        double kijunHikiateTaishouKingaku = kijunZandaka - 
        		(kijunRyuuhoSaimuKei + kijunHozen*100 + kijunSonotakaisyu*100) + kijunRiko_kenen*100 - kijunKiHikiateKin*100;
        // 仮基準日.引当対象金額を設定
        kijunHikiateTaishouKingaku = Function.marume(kijunHikiateTaishouKingaku);
        form.setKijun_hikiate_taishou_kingaku(formatKingaku(kijunHikiateTaishouKingaku/100,user_bean.getComWorkflowSystemkbn()));
        
        // 仮基準日.追加引当金
        double kijunTuikaHikiate = Function.getValueOfDouble(map6.get(TUIKA_HIKIATE));
        // 仮基準日.追加引当金を設定
        form.setKijun_tuika_hikiate(formatKingaku(kijunTuikaHikiate,user_bean.getComWorkflowSystemkbn()));
        
        // 仮基準日.通貨調整2
        double kijunKomoku2 = Function.getValueOfDouble(map6.get(KOMOKU2));
        // 仮基準日.通貨調整2を設定
        form.setKijun_komoku2(formatKingaku(kijunKomoku2,user_bean.getComWorkflowSystemkbn()));
        
        // 仮基準日.追加引当金調整後
        double kijunTuikaHikiateUsiro = kijunTuikaHikiate*100 + kijunKomoku2*100;
        // 仮基準日.追加引当金調整後を設定
        kijunTuikaHikiateUsiro = Function.marume(kijunTuikaHikiateUsiro);
        form.setKijun_tuika_hikiate_usiro(formatKingaku(kijunTuikaHikiateUsiro/100,user_bean.getComWorkflowSystemkbn()));
        
        // 前期.区分判定根拠を設定
        form.setZenki_commond(map8.get(COMMENT_VAL));
        
        // 仮基準日.引当金算定根拠を設定
        form.setKijun_commond(map9.get(COMMENT_VAL));
        
        // 今期.区分判定根拠を設定
        form.setKonki_commond(map10.get(COMMENT_VAL));
        
        // 仮基準日.査定案件no.を設定
        form.setKijun_anken_no(kijunAnkenNo);
        
        // 仮基準日.フェーズを設定
        form.setKijun_phase(kijunPhase);
        
        return GS.OD1102;
    }
    /**
     * 画面初期表示値取得(メニューリンク以外から遷移時) <br>
     * 
     * @return forward
     * @throws Exception 
     */
    public String execute() throws Exception {
        
    	this.executeInit();
        return GS.OD1102;
    }
    
    /**
     * 
     *  もぎ取り解除処理<br>
     * 
     * @return
     * @throws Exception
     */
    public void doKaijyo() throws Exception {
    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        KakuninDbAcc dbacc = new KakuninDbAcc(sqlExec, log, appContext);
        // T14_査定進捗管理の更新
        dbacc.uptT14(STATUS_10,GS.EMPTY_CHARCTER,MODOSHI_FLG_1);
        // T15_一次二次査定(今期・仮基準日末時点)の更新
        //課題No.60
        //修正開始
        //dbacc.uptT15(GS.EMPTY_CHARCTER, GS.EMPTY_CHARCTER);
        // 案件No.D9059 もぎ取り解除時に記入内容をクリアしない
        //dbacc.uptT15_konki(GS.EMPTY_CHARCTER, GS.EMPTY_CHARCTER, GS.EMPTY_CHARCTER);
        //修正完了
        // T13_入力履歴の登録
        dbacc.intT13(OPE_KBN_40);
        // T12_コメントの削除
    	// 案件No.D9059 もぎ取り解除時に記入内容をクリアしない
        //dbacc.delT12();
        // コミット処理
        dbacc.commit();
    }
    
    /**
     * 転送処理 <br>
     * 
     * @throws Exception 
     */
    public boolean doTenso() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        KakuninDbAcc dbacc = new KakuninDbAcc(sqlExec, log, appContext);
        // 入力チェック
        if(!chkKonkiCommond()){
        	return false;
        }
        // T12_コメントの削除(データの再作成)
        dbacc.delT12();
        // T12_コメントの登録(データの再作成)
        dbacc.intT12();
        // T15_一次二次査定(今期・仮基準日末時点)の更新
        dbacc.uptT15_konki(form.getKonki_torihikisaki_kbn(),form.getKonki_saiken_kbn(),form.getHikiatekin_shosai());
        // T14_査定進捗管理の更新
        dbacc.uptT14(STATUS_20,user_bean.getComUserId());
        // コミット処理
        dbacc.commit();
        
        return true;
    }
    
    /**
     * 差戻処理 <br>
     * 
     * @throws Exception 
     */
    public boolean doSashi() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        KakuninDbAcc dbacc = new KakuninDbAcc(sqlExec, log, appContext);
        // 入力チェック
        if(!chkKonkiCommond()){
        	return false;
        }
        // T12_コメントの削除(データの再作成)
        dbacc.delT12();
        // T12_コメントの登録(データの再作成)
        dbacc.intT12();
        // T15_一次二次査定(今期・仮基準日末時点)の更新
        dbacc.uptT15_konki(form.getKonki_torihikisaki_kbn(),form.getKonki_saiken_kbn(),form.getHikiatekin_shosai());
        // T14_査定進捗管理の更新
        dbacc.uptT14(STATUS_20,user_bean.getComUserId());
        // コミット処理
        dbacc.commit();
        
        return true;
    }
    
    /**
     * 
     * 一次保存処理<br>
     * 
     * @return
     * @throws Exception
     */
    public boolean doTempSave() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        KakuninDbAcc dbacc = new KakuninDbAcc(sqlExec, log, appContext);
        // 入力チェック
        if(!chkKonkiCommond()){
        	return false;
        }
        // T12_コメントの削除(データの再作成)
        dbacc.delT12();
        // T12_コメントの登録(データの再作成)
        dbacc.intT12();
        // T15_一次二次査定(今期・仮基準日末時点)の更新
        dbacc.uptT15_konki(form.getKonki_torihikisaki_kbn(),form.getKonki_saiken_kbn(),form.getHikiatekin_shosai());
        // T14_査定進捗管理の更新
        dbacc.uptT14(STATUS_20,user_bean.getComUserId());
        // T13_入力履歴の登録
        dbacc.intT13(OPE_KBN_20);
        // コミット処理
        dbacc.commit();
        
        return true;
    }
    
    /**
     * 
     *  登録処理<br>
     * 
     * @return
     * @throws Exception
     */
    public boolean doRegist() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        KakuninDbAcc dbacc = new KakuninDbAcc(sqlExec, log, appContext);
        // 入力チェックを実施する
        if(!chkKonkiCommond()){
        	return false;
        }
        if(!chkTanto()){
        	return false;
        }
        // T12_コメントの削除(データの再作成)
        dbacc.delT12();
        // T12_コメントの登録(データの再作成)
        dbacc.intT12();
        // T15_一次二次査定(今期・仮基準日末時点)の更新
        dbacc.uptT15_zenki(form.getKonki_torihikisaki_kbn(),form.getKonki_saiken_kbn());
        dbacc.uptT15(form.getKonki_torihikisaki_kbn(),form.getKonki_saiken_kbn(),form.getHikiatekin_shosai());
        // T04_メール配信の登録
        dbacc.intT04();
        // T14_査定進捗管理の更新を行う
        dbacc.uptT14(form.getJi_kaishi_status(),form.getShonin_tanto(),MODOSHI_FLG_0);
        // T13_入力履歴の登録
        dbacc.intT13(OPE_KBN_10);
        // コミット処理
        dbacc.commit();
        
        return true;
    }
    
    /**
     * 
     *  査定結果処理<br>
     * 
     * @return
     * @throws Exception
     */
    public boolean doSyosai() throws Exception {
    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        KakuninDbAcc dbacc = new KakuninDbAcc(sqlExec, log, appContext);
        // 入力チェックを実施する
        if(!chkKonkiCommond()){
        	return false;
        }
        // T12_コメントの削除(データの再作成)
        dbacc.delT12();
        // T12_コメントの登録(データの再作成)
        dbacc.intT12();
        // T15_一次二次査定(今期・仮基準日末時点)の更新
        dbacc.uptT15_konki(form.getKonki_torihikisaki_kbn(),form.getKonki_saiken_kbn(),form.getHikiatekin_shosai());
        // T14_査定進捗管理の更新
        dbacc.uptT14(STATUS_20,user_bean.getComUserId(),MODOSHI_FLG_1);
        // コミット処理
        dbacc.commit();
        
    	return true;
    }
    
    /**
     * 
     *  添付選択処理<br>
     * 
     * @return
     * @throws Exception
     */
    public boolean doTemp() throws Exception {
    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        KakuninDbAcc dbacc = new KakuninDbAcc(sqlExec, log, appContext);
        // 入力チェックを実施する
        if(!chkKonkiCommond()){
        	return false;
        }
        // T12_コメントの削除(データの再作成)
        dbacc.delT12();
        // T12_コメントの登録(データの再作成)
        dbacc.intT12();
        // T15_一次二次査定(今期・仮基準日末時点)の更新
        dbacc.uptT15_konki(form.getKonki_torihikisaki_kbn(),form.getKonki_saiken_kbn(),form.getHikiatekin_shosai());
        // T14_査定進捗管理の更新
        dbacc.uptT14(STATUS_20,user_bean.getComUserId(),MODOSHI_FLG_1);
        // コミット処理
        dbacc.commit();
        
    	return true;
    }
    
    /**
     * 
     * コメント表示リンク処理<br>
     * 
     * @return
     * @throws Exception
     */
    public boolean doComment() throws Exception {
    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        KakuninDbAcc dbacc = new KakuninDbAcc(sqlExec, log, appContext);
        // 入力チェックを実施する
        if(!chkKonkiCommond()){
        	return false;
        }
        // T12_コメントの削除(データの再作成)
        dbacc.delT12();
        // T12_コメントの登録(データの再作成)
        dbacc.intT12();
        // T15_一次二次査定(今期・仮基準日末時点)の更新
        dbacc.uptT15_konki(form.getKonki_torihikisaki_kbn(),form.getKonki_saiken_kbn(),form.getHikiatekin_shosai());
        // T14_査定進捗管理の更新
        dbacc.uptT14(STATUS_20,user_bean.getComUserId(),MODOSHI_FLG_1);
        // コミット処理
        dbacc.commit();
        
    	return true;
    }
    
    /**
     * 
     *  今期.区分判定根拠入力チェックを実施する<br>
     * 
     * @return
     * @throws Exception
     */
    public boolean chkKonkiCommond() throws Exception {
    	
    	InputCheck inChk = new InputCheck();
    	// 500バイト以上入力された場合
    	if(!inChk.islength(form.getKonki_commond(),500)){
		    // 課題No.82 英語版メッセージ対応
		    // 追加開始
    		//appContext.setMsgCode(GL.ERR_LENGTH,GL.OD1102_KONKI_COMMOND);
    		appContext.setMsgCode(GL.ERR_LENGTH,GL.OD1102_KONKI_COMMOND_MSG);
		    // 追加完了
    		return false;
    	}
    	
    	if(!inChk.isNullBlank(form.getKonki_commond())){
    		// 入力禁止文字がデータに含まれていた場合
        	if(chkKinshiChar(form.getKonki_commond())){

        		return false;
        	}
    	}
    	// 取引先区分セレクトボックス・債権区分セレクトボックスが変更された場合
    	if((!inChk.isNullBlank(form.getHiddn_torihikisaki_kbn()) && !form.getHiddn_torihikisaki_kbn().equals(form.getKonki_torihikisaki_kbn())) || 
    			(!inChk.isNullBlank(form.getHiddn_saiken_kbn()) && !form.getHiddn_saiken_kbn().equals(form.getKonki_saiken_kbn()))){
    		if(inChk.isNullBlank(form.getKonki_commond())){
    		    // 課題No.82 英語版メッセージ対応
    		    // 追加開始
    			//appContext.setMsgCode(GL.ERR_INPUT,GL.OD1102_KONKI_COMMOND);
    			appContext.setMsgCode(GL.ERR_INPUT,GL.OD1102_KONKI_COMMOND_MSG);
    		    // 追加完了
        		return false;
    		}
    	}
    	return true;
    }
    
    /**
     * 
     *  承認担当者必須入力チェック<br>
     * 
     * @return
     * @throws Exception
     */
    public boolean chkTanto() throws Exception {
    	InputCheck inChk = new InputCheck();
    	// 承認担当者が未選択の場合
    	if(inChk.isNullBlank(form.getShonin_tanto())){
    		appContext.setMsgCode(GL.ERR_SELECT,GL.OD1102_SHOUNIN_TANTOUSHA);
    		return false;
    	}
    	return true;
    }
    
    /**
     * システム区分により、金額をフォーマットする。 <br>
     * 
     * @param kingaku 金額
     * @param systemKbn システム区分
     * @return フォーマットされた金額
     */
    public String formatKingaku(double kingaku, String systemKbn) {
        String formatKingaku = GS.EMPTY_CHARCTER;
        if (systemKbn.equals(GS.GSS)){
            //国内
        	formatKingaku = Function.format(NUM_FMT_KAIGAI, kingaku);
        }else{
            //海外
        	formatKingaku = Function.format(NUM_FMT_KOKUNAI, kingaku);
        }
        return formatKingaku;
    }
    
    /**
     * 
     * チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示 <br>
     * 
     * @return
     * @throws Exception
     */
    public boolean chkKinshiChar(String str) throws Exception {
    	InputCheck inChk = new InputCheck();
    	// エラーメッセージ(err.prohibitted、入力禁止文字)を表示する
    	for(int i = 0; i < str.length(); i++){
    		String kinshiChar = str.substring(i, i + 1);
    		if (inChk.haveKinshiMoji(kinshiChar)) {
				// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
				appContext.setMsgCd(GL.ERR_PROHIBITTED, kinshiChar);
				return true;
			}
    	}
    	return false;
    }

    /**
     * 
     * 引数で指定された査定期の前査定期を返す <br>
     * 
     * @param String
     * @param String
     * @return String
     * @throws Exception
     */
    private String getZenSateiki(String sateiki) throws Exception {
		DateFormat df = null;
    	String result = null;
    	Calendar cal = Calendar.getInstance();
    	String[] nengetsu = new String[2];
    	nengetsu[0] = sateiki.substring(0,4);
    	nengetsu[1] = sateiki.substring(4);
		df = new SimpleDateFormat("yyyyMM");
    	cal.set(Integer.parseInt(nengetsu[0]),Integer.parseInt(nengetsu[1]),1);
    	cal.setTime(cal.getTime());
    	cal.add(Calendar.MONTH,-7);
    	result = df.format(cal.getTime());
    	
    	return result;
    }
    /**
     * 
     * 引数で指定された査定期の表示用査定期を返す <br>
     * 
     * @param String
     * @param String
     * @return String
     * @throws Exception
     */
    private String getSateikiHyouji(String sateiki,String langMode) throws Exception {
    	StringBuffer sb = new StringBuffer();
    	String[] nengetsu = new String[2];
    	nengetsu[0] = sateiki.substring(0,4);
    	nengetsu[1] = sateiki.substring(4);

    	if(GS.LANG_JA.equals(langMode)){
    		sb.append(nengetsu[0]);
    		sb.append(GS.SLASH);
    		sb.append(nengetsu[1]);
    	}else{
    		sb.append(nengetsu[1]);
    		sb.append(GS.SLASH);
    		sb.append(nengetsu[0]);
    	}
    	
    	return sb.toString();
    }
}
