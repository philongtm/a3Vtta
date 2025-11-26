/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/11/03		SSC				課題No.30 パフォーマンスアップ対応
******************************************************************************/
package app;

import common.global.GS;

/**
 * 取引先情報Beanクラス
 */
public class TorihikisakiBean {

    private String id;						// 明細のID(0から連番)
    private String sincyoku_kbn;			// 進捗区分
    private String anken_no;				// 案件No.
    private String last_anken_no;			// 前回実施案件No.
    private String last_phase;				// 前回実施フェーズ
    private String flg_saki_anken_no;		// フラグ先抽出元案件No.
    private String kanjo_cd;				// 基幹取引先コード
    private String kanjo_nm;				// 勘定先名称
    private String togo_tori_cd;			// 統合取引先コード(DUNS No)
    private String system_kbn;				// システム区分
    private String shikibetu_cd;			// 識別コード
    private String mise_cd;				// 店コード
    private String syozaikoku;				// 所在国
    private String syozaikoku_cd;			// 所在国コード
    private String syozaichi;				// 所在地
    private String satei_toroku_gamen;		// 査定登録画面
    private String kijunbi_kbn;			// 基準日区分
    private String gaibu_ktk;				// 外部格付
    private String ktk_kikan;				// 格付機関
    private String fss;					// FSS
    private String duns_rating;			// DUNS Rating
    private String sinyoktk;				// 信用格付情報
    private String oya_duns_no;			// 親会社Duns_No
    private String oya_ktk;				// 親会社格付
    private String oya_business_nm;		// 親会社名称
    private String oya_ittai_dokuritu;		// 親会社一体独立
    private String jiyu_cd;				// 抽出事由コード
    private String jiyu_nm;				// 抽出事由名称
    private String soshiki_cd;				// 組織コード
    private String soshiki_nm;				// 組織名称
    private String kaisya_cd;				// 会社コード
    private String sateikaisya_cd;			// 査定会社コード
    private String bunrui1;				// 分類１
    private String bunrui2;				// 分類２
    private String bunrui3;				// 分類３
    private String bu_cd;					// 部コード
    private String bunrui1_nm;				// 分類１名称
    private String bunrui2_nm;				// 分類２名称
    private String bunrui3_nm;				// 分類３名称
    private String init_bunrui2;			// 初期分類２
    private String init_bunrui3;			// 初期分類３
    private String init_bunrui2_nm;		// 初期分類２名称
    private String init_bunrui3_nm;		// 初期分類３名称
    private String init_bu_cd;				// 初期部コード
    private String init_bu_nm;				// 初期部名称
    private String soshiki;				// 組織
    private String sasi_ten_flg;			// 差戻・転送フラグ
    private String taisyogai_flg;			// 対象外フラグ
    private String golf_flg;				// ゴルフ会員権フラグ
    private String sentei_kbn;				// 選定区分
    private String hanki_sihanki_kbn;		// 半期・四半期区分
    private String satei_ki;				// 査定期
    private String satei_ki_hyouji;		// 査定期表示用
    private String syori_kaisu;			// 処理回数
    private String taisyo_ym;				// 対象年月(yyyymm)
    private String taisyo_ym_hyoji;		// 対象年月(日本語モード時：yyyy/mm、英語モード時：mm/yyyy)
    private String phase;					// フェーズ
    private String status;					// ステータス
    private String hoji_user_id;			// 案件保持ユーザID
    private String tanto_nm;				// 担当者
    private String daiko_user_id;			// 代行ユーザID
    private String sintyoku;				// 進捗
    private String kingaku;				// 金額計
    private String tuuka_cd;				// 通貨コード
    private String tairyu_kingaku;			// 滞留判定金額計
    private String saiken_kingaku;			// 債権残計
    private String koteika_saiken;			// 固定化営業債権
    private String kasi_hikiatekin;		// 貸倒引当金
    private String temp_cnt;				// 添付COUNT
    private boolean link_flg;				// リンク表示フラグ
    private boolean differ_flg;			// 差異フラグ
    private String syonin_chk;				// 承認チェック
    private String mogitori_chk;			// もぎ取りチェック
    private String tori_kbn;	     		// 取引先区分
    private String tori_kbn_cp;			// 比較用取引先区分
    private String tori_kbn_nm;			// 取引先区分名称
    private String sai_kbn;	     		// 債権区分
    private String sai_kbn_cp;	    		// 比較用債権区分
    private String sai_kbn_nm; 			// 債権区分名称
    private String wb_country_cd;			// ワールドベース国コード
    private String tuika_kingaku;			// 追加引当金額
    private String c_phase;				// 帳票用フェーズ
	// 課題No.30
	// 追加開始
	private String torimodoshi_fuka_flg;	// 取戻不可フラグ
	private String kousin_user_handan;		// 更新ユーザID判定
	private String phase_handan;			// フェーズ判定
	// 追加完了

    // 変数初期化
    public TorihikisakiBean() {

    	this.id                 = GS.EMPTY_CHARCTER;
        this.anken_no           = GS.EMPTY_CHARCTER;
        this.last_anken_no      = GS.EMPTY_CHARCTER;
    	this.last_phase         = GS.EMPTY_CHARCTER;
    	this.flg_saki_anken_no  = GS.EMPTY_CHARCTER;
        this.kanjo_cd           = GS.EMPTY_CHARCTER;
        this.kanjo_nm           = GS.EMPTY_CHARCTER;
    	this.togo_tori_cd       = GS.EMPTY_CHARCTER;
        this.system_kbn         = GS.EMPTY_CHARCTER;
    	this.shikibetu_cd       = GS.EMPTY_CHARCTER;
        this.mise_cd            = GS.EMPTY_CHARCTER;
        this.syozaikoku         = GS.EMPTY_CHARCTER;
        this.syozaikoku_cd      = GS.EMPTY_CHARCTER;
    	this.syozaichi          = GS.EMPTY_CHARCTER;
    	this.satei_toroku_gamen = GS.EMPTY_CHARCTER;
        this.kijunbi_kbn        = GS.EMPTY_CHARCTER;
    	this.gaibu_ktk          = GS.EMPTY_CHARCTER;
    	this.ktk_kikan          = GS.EMPTY_CHARCTER;
    	this.fss                = GS.EMPTY_CHARCTER;
    	this.duns_rating        = GS.EMPTY_CHARCTER;
        this.sinyoktk           = GS.EMPTY_CHARCTER;
    	this.oya_duns_no        = GS.EMPTY_CHARCTER;
    	this.oya_ktk            = GS.EMPTY_CHARCTER;
    	this.oya_business_nm    = GS.EMPTY_CHARCTER;
    	this.oya_ittai_dokuritu = GS.EMPTY_CHARCTER;
    	this.jiyu_cd            = GS.EMPTY_CHARCTER;
    	this.jiyu_nm            = GS.EMPTY_CHARCTER;
    	this.soshiki_cd         = GS.EMPTY_CHARCTER;
    	this.soshiki_nm         = GS.EMPTY_CHARCTER;
    	this.sateikaisya_cd     = GS.EMPTY_CHARCTER;
    	this.kaisya_cd          = GS.EMPTY_CHARCTER;
        this.bunrui1            = GS.EMPTY_CHARCTER;
        this.bunrui2            = GS.EMPTY_CHARCTER;
        this.bunrui3            = GS.EMPTY_CHARCTER;
        this.bu_cd              = GS.EMPTY_CHARCTER;
        this.bunrui1_nm         = GS.EMPTY_CHARCTER;
        this.bunrui2_nm         = GS.EMPTY_CHARCTER;
        this.bunrui3_nm         = GS.EMPTY_CHARCTER;
        this.init_bunrui2       = GS.EMPTY_CHARCTER;
        this.init_bunrui3       = GS.EMPTY_CHARCTER;
        this.init_bunrui2_nm    = GS.EMPTY_CHARCTER;
        this.init_bunrui3_nm    = GS.EMPTY_CHARCTER;
        this.init_bu_cd         = GS.EMPTY_CHARCTER;
        this.init_bu_nm         = GS.EMPTY_CHARCTER;
        this.soshiki            = GS.EMPTY_CHARCTER;
        this.sasi_ten_flg       = GS.EMPTY_CHARCTER;
        this.taisyogai_flg      = GS.EMPTY_CHARCTER;
        this.golf_flg           = GS.EMPTY_CHARCTER;
    	this.sentei_kbn         = GS.EMPTY_CHARCTER;
    	this.hanki_sihanki_kbn  = GS.EMPTY_CHARCTER;
        this.satei_ki           = GS.EMPTY_CHARCTER;
        this.satei_ki_hyouji    = GS.EMPTY_CHARCTER;
        this.syori_kaisu        = GS.EMPTY_CHARCTER;
        this.taisyo_ym          = GS.EMPTY_CHARCTER;
        this.taisyo_ym_hyoji    = GS.EMPTY_CHARCTER;
        this.phase              = GS.EMPTY_CHARCTER;
        this.status             = GS.EMPTY_CHARCTER;
        this.hoji_user_id       = GS.EMPTY_CHARCTER;
        this.tanto_nm           = GS.EMPTY_CHARCTER;
        this.daiko_user_id      = GS.EMPTY_CHARCTER;
        this.sintyoku           = GS.EMPTY_CHARCTER;
        this.kingaku            = GS.EMPTY_CHARCTER;
        this.tuuka_cd           = GS.EMPTY_CHARCTER;
        this.tairyu_kingaku     = GS.EMPTY_CHARCTER;
        this.saiken_kingaku     = GS.EMPTY_CHARCTER;
        this.koteika_saiken     = GS.EMPTY_CHARCTER;
        this.kasi_hikiatekin    = GS.EMPTY_CHARCTER;
        this.temp_cnt           = GS.EMPTY_CHARCTER;
        this.link_flg           = false;
        this.differ_flg         = false;
        this.syonin_chk         = GS.EMPTY_CHARCTER;
        this.mogitori_chk       = GS.EMPTY_CHARCTER;
        this.tori_kbn           = GS.EMPTY_CHARCTER;
        this.tori_kbn_cp        = GS.EMPTY_CHARCTER;
        this.tori_kbn_nm        = GS.EMPTY_CHARCTER;
        this.sai_kbn            = GS.EMPTY_CHARCTER;
        this.sai_kbn_cp         = GS.EMPTY_CHARCTER;
        this.sai_kbn_nm         = GS.EMPTY_CHARCTER;
        this.wb_country_cd      = GS.EMPTY_CHARCTER;
        this.tuika_kingaku      = GS.EMPTY_CHARCTER;
        this.c_phase            = GS.EMPTY_CHARCTER;
    	// 課題No.30
    	// 追加開始
        this.torimodoshi_fuka_flg	= GS.EMPTY_CHARCTER;
        this.kousin_user_handan		= GS.EMPTY_CHARCTER;
        this.phase_handan			= GS.EMPTY_CHARCTER;
    	// 追加完了
    }


    // アクセスメソッド
	//査定期表示用
	public String getSatei_ki_hyouji() {
		return satei_ki_hyouji;
	}
	public void setSatei_ki_hyouji(String satei_ki_hyouji) {
		this.satei_ki_hyouji = satei_ki_hyouji;
	}
	//id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	//案件No.
	public String getAnken_no() {
		return anken_no;
	}
	public void setAnken_no(String anken_no) {
		this.anken_no = anken_no;
	}
	//前回実施案件No.
	public String getLast_anken_no() {
		return last_anken_no;
	}
	public void setLast_anken_no(String last_anken_no) {
		this.last_anken_no = last_anken_no;
	}
	//前回実施フェーズ
	public String getLast_phase() {
		return last_phase;
	}
	public void setLast_phase(String last_phase) {
		this.last_phase = last_phase;
	}
	//フラグ先抽出元案件No.
	public String getFlg_saki_anken_no() {
		return flg_saki_anken_no;
	}
	public void setFlg_saki_anken_no(String flg_saki_anken_no) {
		this.flg_saki_anken_no = flg_saki_anken_no;
	}
	//基幹取引先コード
	public String getKanjo_cd() {
		return kanjo_cd;
	}
	public void setKanjo_cd(String kanjo_cd) {
		this.kanjo_cd = kanjo_cd;
	}
	//勘定先名称
	public String getKanjo_nm() {
		return kanjo_nm;
	}
	public void setKanjo_nm(String kanjo_nm) {
		this.kanjo_nm = kanjo_nm;
	}
	//統合取引先コード(DUNS No)
	public String getTogo_tori_cd() {
		return togo_tori_cd;
	}
	public void setTogo_tori_cd(String togo_tori_cd) {
		this.togo_tori_cd = togo_tori_cd;
	}
	//システム区分
	public String getSystem_kbn() {
		return system_kbn;
	}
	public void setSystem_kbn(String system_kbn) {
		this.system_kbn = system_kbn;
	}
	//識別コード
	public String getShikibetu_cd() {
		return shikibetu_cd;
	}
	public void setShikibetu_cd(String shikibetu_cd) {
		this.shikibetu_cd = shikibetu_cd;
	}
	//店コード
	public String getMise_cd() {
		return mise_cd;
	}
	public void setMise_cd(String mise_cd) {
		this.mise_cd = mise_cd;
	}
	//所在国
	public String getSyozaikoku() {
		return syozaikoku;
	}
	public void setSyozaikoku(String syozaikoku) {
		this.syozaikoku = syozaikoku;
	}
	//所在地
	public String getSyozaichi() {
		return syozaichi;
	}
	public void setSyozaichi(String syozaichi) {
		this.syozaichi = syozaichi;
	}
	//査定登録画面
	public String getSatei_toroku_gamen() {
		return satei_toroku_gamen;
	}
	public void setSatei_toroku_gamen(String satei_toroku_gamen) {
		this.satei_toroku_gamen = satei_toroku_gamen;
	}
	//基準日区分
	public String getKijunbi_kbn() {
		return kijunbi_kbn;
	}
	public void setKijunbi_kbn(String kijunbi_kbn) {
		this.kijunbi_kbn = kijunbi_kbn;
	}
	//外部格付
	public String getGaibu_ktk() {
		return gaibu_ktk;
	}
	public void setGaibu_ktk(String gaibu_ktk) {
		this.gaibu_ktk = gaibu_ktk;
	}
	//格付機関
	public String getKtk_kikan() {
		return ktk_kikan;
	}
	public void setKtk_kikan(String ktk_kikan) {
		this.ktk_kikan = ktk_kikan;
	}
	//FSS
	public String getFss() {
		return fss;
	}
	public void setFss(String fss) {
		this.fss = fss;
	}
	//DUNS Rating
	public String getDuns_rating() {
		return duns_rating;
	}
	public void setDuns_rating(String duns_rating) {
		this.duns_rating = duns_rating;
	}
	//信用格付情報
	public String getSinyoktk() {
		return sinyoktk;
	}
	public void setSinyoktk(String sinyoktk) {
		this.sinyoktk = sinyoktk;
	}
	//親会社Duns_No
	public String getOya_duns_no() {
		return oya_duns_no;
	}
	public void setOya_duns_no(String oya_duns_no) {
		this.oya_duns_no = oya_duns_no;
	}
	//親会社格付
	public String getOya_ktk() {
		return oya_ktk;
	}
	public void setOya_ktk(String oya_ktk) {
		this.oya_ktk = oya_ktk;
	}
	//親会社名称
	public String getOya_business_nm() {
		return oya_business_nm;
	}
	public void setOya_business_nm(String oya_business_nm) {
		this.oya_business_nm = oya_business_nm;
	}
	//親会社一体独立
	public String getOya_ittai_dokuritu() {
		return oya_ittai_dokuritu;
	}
	public void setOya_ittai_dokuritu(String oya_ittai_dokuritu) {
		this.oya_ittai_dokuritu = oya_ittai_dokuritu;
	}
	//抽出事由コード
	public String getJiyu_cd() {
		return jiyu_cd;
	}
	public void setJiyu_cd(String jiyu_cd) {
		this.jiyu_cd = jiyu_cd;
	}
	//抽出事由名称
	public String getJiyu_nm() {
		return jiyu_nm;
	}
	public void setJiyu_nm(String jiyu_nm) {
		this.jiyu_nm = jiyu_nm;
	}
	//組織コード
	public String getSoshiki_cd() {
		return soshiki_cd;
	}
	public void setSoshiki_cd(String soshiki_cd) {
		this.soshiki_cd = soshiki_cd;
	}
	//組織名称
	public String getSoshiki_nm() {
		return soshiki_nm;
	}
	public void setSoshiki_nm(String soshiki_nm) {
		this.soshiki_nm = soshiki_nm;
	}
	//査定会社コード
	public String getSateikaisya_cd() {
		return sateikaisya_cd;
	}
	public void setSateikaisya_cd(String sateikaisya_cd) {
		this.sateikaisya_cd = sateikaisya_cd;
	}
	//会社コード
	public String getKaisya_cd() {
		return kaisya_cd;
	}
	public void setKaisya_cd(String kaisya_cd) {
		this.kaisya_cd = kaisya_cd;
	}
	//分類１
	public String getBunrui1() {
		return bunrui1;
	}
	public void setBunrui1(String bunrui1) {
		this.bunrui1 = bunrui1;
	}
	//分類２
	public String getBunrui2() {
		return bunrui2;
	}
	public void setBunrui2(String bunrui2) {
		this.bunrui2 = bunrui2;
	}
	//分類３
	public String getBunrui3() {
		return bunrui3;
	}
	public void setBunrui3(String bunrui3) {
		this.bunrui3 = bunrui3;
	}
	//部コード
	public String getBu_cd() {
		return bu_cd;
	}
	public void setBu_cd(String bu_cd) {
		this.bu_cd = bu_cd;
	}
	//分類１名称
	public String getBunrui1_nm() {
		return bunrui1_nm;
	}
	public void setBunrui1_nm(String bunrui1_nm) {
		this.bunrui1_nm = bunrui1_nm;
	}
	//分類２名称
	public String getBunrui2_nm() {
		return bunrui2_nm;
	}
	public void setBunrui2_nm(String bunrui2_nm) {
		this.bunrui2_nm = bunrui2_nm;
	}
	//分類３名称
	public String getBunrui3_nm() {
		return bunrui3_nm;
	}
	public void setBunrui3_nm(String bunrui3_nm) {
		this.bunrui3_nm = bunrui3_nm;
	}
	//初期分類２
	public String getInit_bunrui2() {
		return init_bunrui2;
	}
	public void setInit_bunrui2(String init_bunrui2) {
		this.init_bunrui2 = init_bunrui2;
	}
	//初期分類３
	public String getInit_bunrui3() {
		return init_bunrui3;
	}
	public void setInit_bunrui3(String init_bunrui3) {
		this.init_bunrui3 = init_bunrui3;
	}
	//初期分類２名称
	public String getInit_bunrui2_nm() {
		return init_bunrui2_nm;
	}
	public void setInit_bunrui2_nm(String init_bunrui2_nm) {
		this.init_bunrui2_nm = init_bunrui2_nm;
	}
	//初期分類３名称
	public String getInit_bunrui3_nm() {
		return init_bunrui3_nm;
	}
	public void setInit_bunrui3_nm(String init_bunrui3_nm) {
		this.init_bunrui3_nm = init_bunrui3_nm;
	}
	//初期部コード
	public String getInit_bu_cd() {
		return init_bu_cd;
	}
	public void setInit_bu_cd(String init_bu_cd) {
		this.init_bu_cd = init_bu_cd;
	}
	//初期部名称
	public String getInit_bu_nm() {
		return init_bu_nm;
	}
	public void setInit_bu_nm(String init_bu_nm) {
		this.init_bu_nm = init_bu_nm;
	}
	//組織
	public String getSoshiki() {
		return soshiki;
	}
	public void setSoshiki(String soshiki) {
		this.soshiki = soshiki;
	}
	//差戻・転送フラグ
	public String getSasi_ten_flg() {
		return sasi_ten_flg;
	}
	public void setSasi_ten_flg(String sasi_ten_flg) {
		this.sasi_ten_flg = sasi_ten_flg;
	}
	//対象外フラグ
	public String getTaisyogai_flg() {
		return taisyogai_flg;
	}
	public void setTaisyogai_flg(String taisyogai_flg) {
		this.taisyogai_flg = taisyogai_flg;
	}
	//ゴルフ会員権フラグ
	public String getGolf_flg() {
		return golf_flg;
	}
	public void setGolf_flg(String golf_flg) {
		this.golf_flg = golf_flg;
	}
	//選定区分
	public String getSentei_kbn() {
		return sentei_kbn;
	}
	public void setSentei_kbn(String sentei_kbn) {
		this.sentei_kbn = sentei_kbn;
	}
	//半期・四半期区分
	public String getHanki_sihanki_kbn() {
		return hanki_sihanki_kbn;
	}
	public void setHanki_sihanki_kbn(String hanki_sihanki_kbn) {
		this.hanki_sihanki_kbn = hanki_sihanki_kbn;
	}
	//査定期
	public String getSatei_ki() {
		return satei_ki;
	}
	public void setSatei_ki(String satei_ki) {
		this.satei_ki = satei_ki;
	}
	//処理回数
	public String getSyori_kaisu() {
		return syori_kaisu;
	}
	public void setSyori_kaisu(String syori_kaisu) {
		this.syori_kaisu = syori_kaisu;
	}
	//対象年月
	public String getTaisyo_ym() {
		return taisyo_ym;
	}
	public void setTaisyo_ym(String taisyo_ym) {
		this.taisyo_ym = taisyo_ym;
	}
	//対象年月(画面表示用)
	public String getTaisyo_ym_hyoji() {
		return taisyo_ym_hyoji;
	}
	public void setTaisyo_ym_hyoji(String taisyo_ym_hyoji) {
		this.taisyo_ym_hyoji = taisyo_ym_hyoji;
	}
	//フェーズ
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	//ステータス
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	//案件保持ユーザID
	public String getHoji_user_id() {
		return hoji_user_id;
	}
	public void setHoji_user_id(String hoji_user_id) {
		this.hoji_user_id = hoji_user_id;
	}
	//担当者
	public String getTanto_nm() {
		return tanto_nm;
	}
	public void setTanto_nm(String tanto_nm) {
		this.tanto_nm = tanto_nm;
	}
	//代行ユーザID
	public String getDaiko_user_id() {
		return daiko_user_id;
	}
	public void setDaiko_user_id(String daiko_user_id) {
		this.daiko_user_id = daiko_user_id;
	}
	//進捗
	public String getSintyoku() {
		return sintyoku;
	}
	public void setSintyoku(String sintyoku) {
		this.sintyoku = sintyoku;
	}
	//金額計
	public String getKingaku() {
		return kingaku;
	}
	public void setKingaku(String kingaku) {
		this.kingaku = kingaku;
	}
	//通貨コード
	public String getTuuka_cd() {
		return tuuka_cd;
	}
	public void setTuuka_cd(String tuuka_cd) {
		this.tuuka_cd = tuuka_cd;
	}
	//滞留判定金額計
	public String getTairyu_kingaku() {
		return tairyu_kingaku;
	}
	public void setTairyu_kingaku(String tairyu_kingaku) {
		this.tairyu_kingaku = tairyu_kingaku;
	}
	//債権残計
	public String getSaiken_kingaku() {
		return saiken_kingaku;
	}
	public void setSaiken_kingaku(String saiken_kingaku) {
		this.saiken_kingaku = saiken_kingaku;
	}
	//固定化営業債権
	public String getKoteika_saiken() {
		return koteika_saiken;
	}
	public void setKoteika_saiken(String koteika_saiken) {
		this.koteika_saiken = koteika_saiken;
	}
	//貸倒引当金
	public String getKasi_hikiatekin() {
		return kasi_hikiatekin;
	}
	public void setKasi_hikiatekin(String kasi_hikiatekin) {
		this.kasi_hikiatekin = kasi_hikiatekin;
	}
	//添付COUNT
	public String getTemp_cnt() {
		return temp_cnt;
	}
	public void setTemp_cnt(String temp_cnt) {
		this.temp_cnt = temp_cnt;
	}
	//リンク表示フラグ
	public boolean getLink_flg() {
		return link_flg;
	}
	public void setLink_flg(boolean link_flg) {
		this.link_flg = link_flg;
	}
	//承認チェック
	public String getSyonin_chk() {
		return syonin_chk;
	}
	public void setSyonin_chk(String syonin_chk) {
		this.syonin_chk = syonin_chk;
	}
	//もぎ取りチェック
	public String getMogitori_chk() {
		return mogitori_chk;
	}
	public void setMogitori_chk(String mogitori_chk) {
		this.mogitori_chk = mogitori_chk;
	}

	//所在国コード
	public String getSyozaikoku_cd() {
		return syozaikoku_cd;
	}
	public void setSyozaikoku_cd(String syozaikoku_cd) {
		this.syozaikoku_cd = syozaikoku_cd;
	}

	//進捗区分
	public String getSincyoku_kbn() {
		return sincyoku_kbn;
	}
	public void setSincyoku_kbn(String sincyoku_kbn) {
		this.sincyoku_kbn = sincyoku_kbn;
	}

	//差異フラグ
	public boolean isDiffer_flg() {
		return differ_flg;
	}
	public void setDiffer_flg(boolean differ_flg) {
		this.differ_flg = differ_flg;
	}

	//取引先区分
	public String getTori_kbn() {
		return tori_kbn;
	}
	public void setTori_kbn(String tori_kbn) {
		this.tori_kbn = tori_kbn;
	}

	//比較用取引先区分
	public String getTori_kbn_cp() {
		return tori_kbn_cp;
	}
	public void setTori_kbn_cp(String tori_kbn_cp) {
		this.tori_kbn_cp = tori_kbn_cp;
	}

	//取引先区分名称
	public String getTori_kbn_nm() {
		return tori_kbn_nm;
	}
	public void setTori_kbn_nm(String tori_kbn_nm) {
		this.tori_kbn_nm = tori_kbn_nm;
	}

	//債権区分
	public String getSai_kbn() {
		return sai_kbn;
	}
	public void setSai_kbn(String sai_kbn) {
		this.sai_kbn = sai_kbn;
	}

	//比較用債権区分
	public String getSai_kbn_cp() {
		return sai_kbn_cp;
	}
	public void setSai_kbn_cp(String sai_kbn_cp) {
		this.sai_kbn_cp = sai_kbn_cp;
	}

	//債権区分名称
	public String getSai_kbn_nm() {
		return sai_kbn_nm;
	}
	public void setSai_kbn_nm(String sai_kbn_nm) {
		this.sai_kbn_nm = sai_kbn_nm;
	}

	//ワールドベース国コード
	public String getWb_country_cd() {
		return wb_country_cd;
	}
	public void setWb_country_cd(String wb_country_cd) {
		this.wb_country_cd = wb_country_cd;
	}

	// 追加引当金額
	public String getTuika_kingaku() {
		return tuika_kingaku;
	}
	public void setTuika_kingaku(String tuika_kingaku) {
		this.tuika_kingaku = tuika_kingaku;
	}

	// 帳票用フェーズ
    public String getC_phase() {
		return c_phase;
	}
	public void setC_phase(String c_phase) {
		this.c_phase = c_phase;
	}
	// 課題No.30
	// 追加開始
	// 取戻不可フラグ
	public String getTorimodoshi_fuka_flg() {
		return torimodoshi_fuka_flg;
	}
	public void setTorimodoshi_fuka_flg(String torimodoshi_fuka_flg) {
		this.torimodoshi_fuka_flg = torimodoshi_fuka_flg;
	}
	// 更新ユーザID判定
	public String getKousin_user_handan() {
		return kousin_user_handan;
	}
	public void setKousin_user_handan(String kousin_user_handan) {
		this.kousin_user_handan = kousin_user_handan;
	}
	// フェーズ判断
	public String getPhase_handan() {
		return phase_handan;
	}
	public void setPhase_handan(String phase_handan) {
		this.phase_handan = phase_handan;
	}
	// 追加完了
}
