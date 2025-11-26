/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/02		SSC				課題No.28 引当対象外/帳簿外対応 
******************************************************************************/
package app.common.form;

import common.global.GS;
import common.struts.AppPagerActionForm;
/**
 * OZ6106_引当金確認照会タブ アクションフォームクラス
 * 
 */
public class KakuninSyokaiForm extends AppPagerActionForm {
	
	private static final long serialVersionUID = 1L; // serialVersionUID
	private String tuuka_cd;							// 単位
	private String zenki_ym;							// 前期.表示用年月
	private String zenki_ktk;							// 前期.信用格付
	private String zenki_oya_ktk;						// 前期.親会社信用格付
	private String zenki_company_nm;					// 前期.親会社名称
	private String zenki_oya_flg;						// 前期.親会社一体独立
	private String zenki_tairyu_kbn;					// 前期.滞留区分
	private String zenki_tairyu_kbn_nm;				// 前期.滞留区分名称
	private String zenki_torihikisaki_kbn;				// 前期.取引先区分
	private String zenki_saiken_kbn;					// 前期.債権区分
	private String zenki_uketori_tegata;				// 前期.受取手形
	private String zenki_yushutu_uketori_tegata;		// 前期.輸出受取手形
	private String zenki_urikake_kin;					// 前期.売掛金
	private String zenki_torihiki_maewatashie_kin;		// 前期.取引前渡金
	private String zenki_tatekae_kin;					// 前期.立替金
	private String zenki_mishuunyuu_kin;				// 前期.未収入金
	private String zenki_mishuu_shuueki;				// 前期.未収収益
	private String zenki_tanki_kashituke_kin;			// 前期.短期貸付金
	private String zenki_sashiire_hoshou_kin;			// 前期.差入保証金
	private String zenki_karibarai_kin;				// 前期.仮払金
	private String zenki_chouki_kashituke_kin;			// 前期.長期貸付金
	private String zenki_sonota_toushi;				// 前期.その他投資
	private String zenki_ippan_saiken_kei;				// 前期.一般債権計
	private String zenki_saiken_zandaka_goukei;		// 前期.債権残高合計①
	private String zenki_hoshou_saimu_goukei;			// 前期.保証債務合計
	private String zenki_ki_hikiate_kin;				// 前期.既引当金⑥
	private String zenki_commond;						// 前期.区分判定根拠
	private String kijun_ym;							// 仮基準日.表示用年月
	private String kijun_ktk;							// 仮基準日.信用格付
	private String kijun_oya_ktk;						// 仮基準日.親会社信用格付
	private String kijun_company_nm;					// 仮基準日.親会社名称
	private String kijun_oya_flg;						// 仮基準日.親会社一体独立
	private String kijun_tairyu_kbn;					// 仮基準日.滞留区分
	private String kijun_tairyu_kbn_nm;				// 仮基準日.滞留区分名称
	private String kijun_torihikisaki_kbn;				// 仮基準日.取引先区分
	private String kijun_saiken_kbn;					// 仮基準日.債権区分
	private String kijun_uketori_tegata;				// 仮基準日.受取手形
	private String kijun_yushutu_uketori_tegata;		// 仮基準日.輸出受取手形
	private String kijun_urikake_kin;					// 仮基準日.売掛金
	private String kijun_torihiki_maewatashie_kin;		// 仮基準日.取引前渡金
	private String kijun_tatekae_kin;					// 仮基準日.立替金
	private String kijun_mishuunyuu_kin;				// 仮基準日.未収入金
	private String kijun_mishuu_shuueki;				// 仮基準日.未収収益
	private String kijun_tanki_kashituke_kin;			// 仮基準日.短期貸付金
	private String kijun_sashiire_hoshou_kin;			// 仮基準日.差入保証金
	private String kijun_karibarai_kin;				// 仮基準日.仮払金
	private String kijun_chouki_kashituke_kin;			// 仮基準日.長期貸付金
	private String kijun_sonota_toushi;				// 仮基準日.その他投資
	private String kijun_ippan_saiken_kei;				// 仮基準日.一般債権計
	private String kijun_komoku1;						// 仮基準日.通貨調整
	private String kijun_saiken_zandaka_goukei;		// 仮基準日.債権残高合計①
	private String kijun_ryuhosaimu;					// 仮基準日.留保債務
	private String kijun_oth_ryuhosaimu;				// 仮基準日.第三者留保債務
	private String kijun_ryuuho_saimu_kei;				// 仮基準日.留保債務計②
	private String kijun_hozen;						// 仮基準日.保全③
	private String kijun_sonotakaisyu;					// 仮基準日.その他回収④
	private String kijun_hoshou_saimu_goukei;			// 仮基準日.保証債務合計
	private String kijun_riko_kenen;					// 仮基準日.履行請求懸念⑤
	private String kijun_ki_hikiate_kin;				// 仮基準日.既引当金⑥
	private String kijun_hikiate_taishou_kingaku;		// 仮基準日.引当対象金額
	private String kijun_tuika_hikiate;				// 仮基準日.追加引当金
	private String kijun_komoku2;						// 仮基準日.通貨調整2
	private String kijun_tuika_hikiate_usiro;			// 仮基準日.追加引当金調整後
	private String kijun_commond;						// 仮基準日.引当金算定根拠
	private String konki_ym;							// 今期.表示用年月
	private String konki_ktk;							// 今期.信用格付
	private String konki_oya_ktk;						// 今期.親会社信用格付
	private String konki_company_nm;					// 今期.親会社名称
	private String konki_oya_flg;						// 今期.親会社一体独立
	private String konki_tairyu_kbn;					// 今期.滞留区分
	private String konki_tairyu_kbn_nm;				// 今期.滞留区分名称
	private String konki_torihikisaki_kbn;				// 今期.取引先区分
	private String konki_saiken_kbn;					// 今期.債権区分
	private String konki_uketori_tegata;				// 今期.受取手形
	private String konki_yushutu_uketori_tegata;		// 今期.輸出受取手形
	private String konki_urikake_kin;					// 今期.売掛金
	private String konki_torihiki_maewatashie_kin;		// 今期.取引前渡金
	private String konki_tatekae_kin;					// 今期.立替金
	private String konki_mishuunyuu_kin;				// 今期.未収入金
	private String konki_mishuu_shuueki;				// 今期.未収収益
	private String konki_tanki_kashituke_kin;			// 今期.短期貸付金
	private String konki_sashiire_hoshou_kin;			// 今期.差入保証金
	private String konki_karibarai_kin;				// 今期.仮払金
	private String konki_chouki_kashituke_kin;			// 今期.長期貸付金
	private String konki_sonota_toushi;				// 今期.その他投資
	private String konki_ippan_saiken_kei;				// 今期.一般債権計
	private String konki_saiken_zandaka_goukei;		// 今期.債権残高合計①
	private String konki_hoshou_saimu_goukei;			// 今期.保証債務合計
	private String konki_ki_hikiate_kin;				// 今期.既引当金⑥
	private String konki_commond;						// 今期.区分判定根拠
	private String kijun_anken_no;						// 仮基準日データ取得用案件No.
	private String kijun_phase;						// 仮基準日フェーズ
	// 課題No.28
	// 追加開始
	private String hikiatekin_shosai;					// 引当金詳細
	// 追加完了
	
		
	/**
     * 変数初期化 <br>
     */
    public KakuninSyokaiForm() {
        
        super.gamenId = GS.OD1102;
        
        this.tuuka_cd = GS.EMPTY_CHARCTER;
        this.zenki_ym = GS.EMPTY_CHARCTER;
        this.zenki_ktk = GS.EMPTY_CHARCTER;
        this.zenki_oya_ktk = GS.EMPTY_CHARCTER;
        this.zenki_company_nm = GS.EMPTY_CHARCTER;
        this.zenki_oya_flg = GS.EMPTY_CHARCTER;
        this.zenki_tairyu_kbn = GS.EMPTY_CHARCTER;
        this.zenki_tairyu_kbn_nm = GS.EMPTY_CHARCTER;
        this.zenki_torihikisaki_kbn = GS.EMPTY_CHARCTER;
        this.zenki_saiken_kbn = GS.EMPTY_CHARCTER;
        this.zenki_uketori_tegata = GS.EMPTY_CHARCTER;
        this.zenki_yushutu_uketori_tegata = GS.EMPTY_CHARCTER;
        this.zenki_urikake_kin = GS.EMPTY_CHARCTER;
        this.zenki_torihiki_maewatashie_kin = GS.EMPTY_CHARCTER;
        this.zenki_tatekae_kin = GS.EMPTY_CHARCTER;
        this.zenki_mishuunyuu_kin = GS.EMPTY_CHARCTER;
        this.zenki_mishuu_shuueki = GS.EMPTY_CHARCTER;
        this.zenki_tanki_kashituke_kin = GS.EMPTY_CHARCTER;
        this.zenki_sashiire_hoshou_kin = GS.EMPTY_CHARCTER;
        this.zenki_karibarai_kin = GS.EMPTY_CHARCTER;
        this.zenki_chouki_kashituke_kin = GS.EMPTY_CHARCTER;
        this.zenki_sonota_toushi = GS.EMPTY_CHARCTER;
        this.zenki_ippan_saiken_kei = GS.EMPTY_CHARCTER;
        this.zenki_saiken_zandaka_goukei = GS.EMPTY_CHARCTER;
        this.zenki_hoshou_saimu_goukei = GS.EMPTY_CHARCTER;
        this.zenki_ki_hikiate_kin = GS.EMPTY_CHARCTER;
        this.kijun_ym = GS.EMPTY_CHARCTER;
        this.kijun_ktk = GS.EMPTY_CHARCTER;
        this.kijun_oya_ktk = GS.EMPTY_CHARCTER;
        this.kijun_company_nm = GS.EMPTY_CHARCTER;
        this.kijun_oya_flg = GS.EMPTY_CHARCTER;
        this.kijun_tairyu_kbn = GS.EMPTY_CHARCTER;
        this.kijun_tairyu_kbn_nm = GS.EMPTY_CHARCTER;
        this.kijun_torihikisaki_kbn = GS.EMPTY_CHARCTER;
        this.kijun_saiken_kbn = GS.EMPTY_CHARCTER;
        this.kijun_uketori_tegata = GS.EMPTY_CHARCTER;
        this.kijun_yushutu_uketori_tegata = GS.EMPTY_CHARCTER;
        this.kijun_urikake_kin = GS.EMPTY_CHARCTER;
        this.kijun_torihiki_maewatashie_kin = GS.EMPTY_CHARCTER;
        this.kijun_tatekae_kin = GS.EMPTY_CHARCTER;
        this.kijun_mishuunyuu_kin = GS.EMPTY_CHARCTER;
        this.kijun_mishuu_shuueki = GS.EMPTY_CHARCTER;
        this.kijun_tanki_kashituke_kin = GS.EMPTY_CHARCTER;
        this.kijun_sashiire_hoshou_kin = GS.EMPTY_CHARCTER;
        this.kijun_karibarai_kin = GS.EMPTY_CHARCTER;
        this.kijun_chouki_kashituke_kin = GS.EMPTY_CHARCTER;
        this.kijun_sonota_toushi = GS.EMPTY_CHARCTER;
        this.kijun_ippan_saiken_kei = GS.EMPTY_CHARCTER;
        this.kijun_komoku1 = GS.EMPTY_CHARCTER;
        this.kijun_saiken_zandaka_goukei = GS.EMPTY_CHARCTER;
        this.kijun_ryuhosaimu = GS.EMPTY_CHARCTER;
        this.kijun_oth_ryuhosaimu = GS.EMPTY_CHARCTER;
        this.kijun_ryuuho_saimu_kei = GS.EMPTY_CHARCTER;
        this.kijun_hozen = GS.EMPTY_CHARCTER;
        this.kijun_sonotakaisyu = GS.EMPTY_CHARCTER;
        this.kijun_hoshou_saimu_goukei = GS.EMPTY_CHARCTER;
        this.kijun_riko_kenen = GS.EMPTY_CHARCTER;
        this.kijun_ki_hikiate_kin = GS.EMPTY_CHARCTER;
        this.kijun_hikiate_taishou_kingaku = GS.EMPTY_CHARCTER;
        this.kijun_tuika_hikiate = GS.EMPTY_CHARCTER;
        this.kijun_komoku2 = GS.EMPTY_CHARCTER;
        this.kijun_tuika_hikiate_usiro = GS.EMPTY_CHARCTER;
        this.konki_ym = GS.EMPTY_CHARCTER;
        this.konki_ktk = GS.EMPTY_CHARCTER;
        this.konki_oya_ktk = GS.EMPTY_CHARCTER;
        this.konki_company_nm = GS.EMPTY_CHARCTER;
        this.konki_oya_flg = GS.EMPTY_CHARCTER;
        this.konki_tairyu_kbn = GS.EMPTY_CHARCTER;
        this.konki_tairyu_kbn_nm = GS.EMPTY_CHARCTER;
        this.konki_torihikisaki_kbn = GS.EMPTY_CHARCTER;
        this.konki_saiken_kbn = GS.EMPTY_CHARCTER;
        this.konki_uketori_tegata = GS.EMPTY_CHARCTER;
        this.konki_yushutu_uketori_tegata = GS.EMPTY_CHARCTER;
        this.konki_urikake_kin = GS.EMPTY_CHARCTER;
        this.konki_torihiki_maewatashie_kin = GS.EMPTY_CHARCTER;
        this.konki_tatekae_kin = GS.EMPTY_CHARCTER;
        this.konki_mishuunyuu_kin = GS.EMPTY_CHARCTER;
        this.konki_mishuu_shuueki = GS.EMPTY_CHARCTER;
        this.konki_tanki_kashituke_kin = GS.EMPTY_CHARCTER;
        this.konki_sashiire_hoshou_kin = GS.EMPTY_CHARCTER;
        this.konki_karibarai_kin = GS.EMPTY_CHARCTER;
        this.konki_chouki_kashituke_kin = GS.EMPTY_CHARCTER;
        this.konki_sonota_toushi = GS.EMPTY_CHARCTER;
        this.konki_ippan_saiken_kei = GS.EMPTY_CHARCTER;
        this.konki_saiken_zandaka_goukei = GS.EMPTY_CHARCTER;
        this.konki_hoshou_saimu_goukei = GS.EMPTY_CHARCTER;
        this.konki_ki_hikiate_kin = GS.EMPTY_CHARCTER;
        this.zenki_commond = GS.EMPTY_CHARCTER;
        this.kijun_commond = GS.EMPTY_CHARCTER;
        this.konki_commond = GS.EMPTY_CHARCTER;								
        this.kijun_anken_no = GS.EMPTY_CHARCTER;									
        this.kijun_phase = GS.EMPTY_CHARCTER;						
    	// 課題No.28
    	// 追加開始
        this.hikiatekin_shosai = GS.EMPTY_CHARCTER;
    	// 追加完了
    }

	public String toString() {
		return super.gamenId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getKijun_anken_no() {
		return kijun_anken_no;
	}

	public void setKijun_anken_no(String kijun_anken_no) {
		this.kijun_anken_no = kijun_anken_no;
	}

	public String getKijun_chouki_kashituke_kin() {
		return kijun_chouki_kashituke_kin;
	}

	public void setKijun_chouki_kashituke_kin(String kijun_chouki_kashituke_kin) {
		this.kijun_chouki_kashituke_kin = kijun_chouki_kashituke_kin;
	}

	public String getKijun_commond() {
		return kijun_commond;
	}

	public void setKijun_commond(String kijun_commond) {
		this.kijun_commond = kijun_commond;
	}

	public String getKijun_company_nm() {
		return kijun_company_nm;
	}

	public void setKijun_company_nm(String kijun_company_nm) {
		this.kijun_company_nm = kijun_company_nm;
	}

	public String getKijun_hikiate_taishou_kingaku() {
		return kijun_hikiate_taishou_kingaku;
	}

	public void setKijun_hikiate_taishou_kingaku(
			String kijun_hikiate_taishou_kingaku) {
		this.kijun_hikiate_taishou_kingaku = kijun_hikiate_taishou_kingaku;
	}

	public String getKijun_hoshou_saimu_goukei() {
		return kijun_hoshou_saimu_goukei;
	}

	public void setKijun_hoshou_saimu_goukei(String kijun_hoshou_saimu_goukei) {
		this.kijun_hoshou_saimu_goukei = kijun_hoshou_saimu_goukei;
	}

	public String getKijun_hozen() {
		return kijun_hozen;
	}

	public void setKijun_hozen(String kijun_hozen) {
		this.kijun_hozen = kijun_hozen;
	}

	public String getKijun_ippan_saiken_kei() {
		return kijun_ippan_saiken_kei;
	}

	public void setKijun_ippan_saiken_kei(String kijun_ippan_saiken_kei) {
		this.kijun_ippan_saiken_kei = kijun_ippan_saiken_kei;
	}

	public String getKijun_karibarai_kin() {
		return kijun_karibarai_kin;
	}

	public void setKijun_karibarai_kin(String kijun_karibarai_kin) {
		this.kijun_karibarai_kin = kijun_karibarai_kin;
	}

	public String getKijun_ki_hikiate_kin() {
		return kijun_ki_hikiate_kin;
	}

	public void setKijun_ki_hikiate_kin(String kijun_ki_hikiate_kin) {
		this.kijun_ki_hikiate_kin = kijun_ki_hikiate_kin;
	}

	public String getKijun_komoku1() {
		return kijun_komoku1;
	}

	public void setKijun_komoku1(String kijun_komoku1) {
		this.kijun_komoku1 = kijun_komoku1;
	}

	public String getKijun_komoku2() {
		return kijun_komoku2;
	}

	public void setKijun_komoku2(String kijun_komoku2) {
		this.kijun_komoku2 = kijun_komoku2;
	}

	public String getKijun_ktk() {
		return kijun_ktk;
	}

	public void setKijun_ktk(String kijun_ktk) {
		this.kijun_ktk = kijun_ktk;
	}

	public String getKijun_mishuu_shuueki() {
		return kijun_mishuu_shuueki;
	}

	public void setKijun_mishuu_shuueki(String kijun_mishuu_shuueki) {
		this.kijun_mishuu_shuueki = kijun_mishuu_shuueki;
	}

	public String getKijun_mishuunyuu_kin() {
		return kijun_mishuunyuu_kin;
	}

	public void setKijun_mishuunyuu_kin(String kijun_mishuunyuu_kin) {
		this.kijun_mishuunyuu_kin = kijun_mishuunyuu_kin;
	}

	public String getKijun_oth_ryuhosaimu() {
		return kijun_oth_ryuhosaimu;
	}

	public void setKijun_oth_ryuhosaimu(String kijun_oth_ryuhosaimu) {
		this.kijun_oth_ryuhosaimu = kijun_oth_ryuhosaimu;
	}

	public String getKijun_oya_flg() {
		return kijun_oya_flg;
	}

	public void setKijun_oya_flg(String kijun_oya_flg) {
		this.kijun_oya_flg = kijun_oya_flg;
	}

	public String getKijun_oya_ktk() {
		return kijun_oya_ktk;
	}

	public void setKijun_oya_ktk(String kijun_oya_ktk) {
		this.kijun_oya_ktk = kijun_oya_ktk;
	}

	public String getKijun_phase() {
		return kijun_phase;
	}

	public void setKijun_phase(String kijun_phase) {
		this.kijun_phase = kijun_phase;
	}

	public String getKijun_riko_kenen() {
		return kijun_riko_kenen;
	}

	public void setKijun_riko_kenen(String kijun_riko_kenen) {
		this.kijun_riko_kenen = kijun_riko_kenen;
	}

	public String getKijun_ryuhosaimu() {
		return kijun_ryuhosaimu;
	}

	public void setKijun_ryuhosaimu(String kijun_ryuhosaimu) {
		this.kijun_ryuhosaimu = kijun_ryuhosaimu;
	}

	public String getKijun_ryuuho_saimu_kei() {
		return kijun_ryuuho_saimu_kei;
	}

	public void setKijun_ryuuho_saimu_kei(String kijun_ryuuho_saimu_kei) {
		this.kijun_ryuuho_saimu_kei = kijun_ryuuho_saimu_kei;
	}

	public String getKijun_saiken_kbn() {
		return kijun_saiken_kbn;
	}

	public void setKijun_saiken_kbn(String kijun_saiken_kbn) {
		this.kijun_saiken_kbn = kijun_saiken_kbn;
	}

	public String getKijun_saiken_zandaka_goukei() {
		return kijun_saiken_zandaka_goukei;
	}

	public void setKijun_saiken_zandaka_goukei(String kijun_saiken_zandaka_goukei) {
		this.kijun_saiken_zandaka_goukei = kijun_saiken_zandaka_goukei;
	}

	public String getKijun_sashiire_hoshou_kin() {
		return kijun_sashiire_hoshou_kin;
	}

	public void setKijun_sashiire_hoshou_kin(String kijun_sashiire_hoshou_kin) {
		this.kijun_sashiire_hoshou_kin = kijun_sashiire_hoshou_kin;
	}

	public String getKijun_sonota_toushi() {
		return kijun_sonota_toushi;
	}

	public void setKijun_sonota_toushi(String kijun_sonota_toushi) {
		this.kijun_sonota_toushi = kijun_sonota_toushi;
	}

	public String getKijun_sonotakaisyu() {
		return kijun_sonotakaisyu;
	}

	public void setKijun_sonotakaisyu(String kijun_sonotakaisyu) {
		this.kijun_sonotakaisyu = kijun_sonotakaisyu;
	}

	public String getKijun_tairyu_kbn() {
		return kijun_tairyu_kbn;
	}

	public void setKijun_tairyu_kbn(String kijun_tairyu_kbn) {
		this.kijun_tairyu_kbn = kijun_tairyu_kbn;
	}

	public String getKijun_tairyu_kbn_nm() {
		return kijun_tairyu_kbn_nm;
	}

	public void setKijun_tairyu_kbn_nm(String kijun_tairyu_kbn_nm) {
		this.kijun_tairyu_kbn_nm = kijun_tairyu_kbn_nm;
	}

	public String getKijun_tanki_kashituke_kin() {
		return kijun_tanki_kashituke_kin;
	}

	public void setKijun_tanki_kashituke_kin(String kijun_tanki_kashituke_kin) {
		this.kijun_tanki_kashituke_kin = kijun_tanki_kashituke_kin;
	}

	public String getKijun_tatekae_kin() {
		return kijun_tatekae_kin;
	}

	public void setKijun_tatekae_kin(String kijun_tatekae_kin) {
		this.kijun_tatekae_kin = kijun_tatekae_kin;
	}

	public String getKijun_torihiki_maewatashie_kin() {
		return kijun_torihiki_maewatashie_kin;
	}

	public void setKijun_torihiki_maewatashie_kin(
			String kijun_torihiki_maewatashie_kin) {
		this.kijun_torihiki_maewatashie_kin = kijun_torihiki_maewatashie_kin;
	}

	public String getKijun_torihikisaki_kbn() {
		return kijun_torihikisaki_kbn;
	}

	public void setKijun_torihikisaki_kbn(String kijun_torihikisaki_kbn) {
		this.kijun_torihikisaki_kbn = kijun_torihikisaki_kbn;
	}

	public String getKijun_tuika_hikiate() {
		return kijun_tuika_hikiate;
	}

	public void setKijun_tuika_hikiate(String kijun_tuika_hikiate) {
		this.kijun_tuika_hikiate = kijun_tuika_hikiate;
	}

	public String getKijun_tuika_hikiate_usiro() {
		return kijun_tuika_hikiate_usiro;
	}

	public void setKijun_tuika_hikiate_usiro(String kijun_tuika_hikiate_usiro) {
		this.kijun_tuika_hikiate_usiro = kijun_tuika_hikiate_usiro;
	}

	public String getKijun_uketori_tegata() {
		return kijun_uketori_tegata;
	}

	public void setKijun_uketori_tegata(String kijun_uketori_tegata) {
		this.kijun_uketori_tegata = kijun_uketori_tegata;
	}

	public String getKijun_urikake_kin() {
		return kijun_urikake_kin;
	}

	public void setKijun_urikake_kin(String kijun_urikake_kin) {
		this.kijun_urikake_kin = kijun_urikake_kin;
	}

	public String getKijun_ym() {
		return kijun_ym;
	}

	public void setKijun_ym(String kijun_ym) {
		this.kijun_ym = kijun_ym;
	}

	public String getKijun_yushutu_uketori_tegata() {
		return kijun_yushutu_uketori_tegata;
	}

	public void setKijun_yushutu_uketori_tegata(String kijun_yushutu_uketori_tegata) {
		this.kijun_yushutu_uketori_tegata = kijun_yushutu_uketori_tegata;
	}

	public String getKonki_chouki_kashituke_kin() {
		return konki_chouki_kashituke_kin;
	}

	public void setKonki_chouki_kashituke_kin(String konki_chouki_kashituke_kin) {
		this.konki_chouki_kashituke_kin = konki_chouki_kashituke_kin;
	}

	public String getKonki_commond() {
		return konki_commond;
	}

	public void setKonki_commond(String konki_commond) {
		this.konki_commond = konki_commond;
	}

	public String getKonki_company_nm() {
		return konki_company_nm;
	}

	public void setKonki_company_nm(String konki_company_nm) {
		this.konki_company_nm = konki_company_nm;
	}

	public String getKonki_hoshou_saimu_goukei() {
		return konki_hoshou_saimu_goukei;
	}

	public void setKonki_hoshou_saimu_goukei(String konki_hoshou_saimu_goukei) {
		this.konki_hoshou_saimu_goukei = konki_hoshou_saimu_goukei;
	}

	public String getKonki_ippan_saiken_kei() {
		return konki_ippan_saiken_kei;
	}

	public void setKonki_ippan_saiken_kei(String konki_ippan_saiken_kei) {
		this.konki_ippan_saiken_kei = konki_ippan_saiken_kei;
	}

	public String getKonki_karibarai_kin() {
		return konki_karibarai_kin;
	}

	public void setKonki_karibarai_kin(String konki_karibarai_kin) {
		this.konki_karibarai_kin = konki_karibarai_kin;
	}

	public String getKonki_ki_hikiate_kin() {
		return konki_ki_hikiate_kin;
	}

	public void setKonki_ki_hikiate_kin(String konki_ki_hikiate_kin) {
		this.konki_ki_hikiate_kin = konki_ki_hikiate_kin;
	}

	public String getKonki_ktk() {
		return konki_ktk;
	}

	public void setKonki_ktk(String konki_ktk) {
		this.konki_ktk = konki_ktk;
	}

	public String getKonki_mishuu_shuueki() {
		return konki_mishuu_shuueki;
	}

	public void setKonki_mishuu_shuueki(String konki_mishuu_shuueki) {
		this.konki_mishuu_shuueki = konki_mishuu_shuueki;
	}

	public String getKonki_mishuunyuu_kin() {
		return konki_mishuunyuu_kin;
	}

	public void setKonki_mishuunyuu_kin(String konki_mishuunyuu_kin) {
		this.konki_mishuunyuu_kin = konki_mishuunyuu_kin;
	}

	public String getKonki_oya_flg() {
		return konki_oya_flg;
	}

	public void setKonki_oya_flg(String konki_oya_flg) {
		this.konki_oya_flg = konki_oya_flg;
	}

	public String getKonki_oya_ktk() {
		return konki_oya_ktk;
	}

	public void setKonki_oya_ktk(String konki_oya_ktk) {
		this.konki_oya_ktk = konki_oya_ktk;
	}

	public String getKonki_saiken_kbn() {
		return konki_saiken_kbn;
	}

	public void setKonki_saiken_kbn(String konki_saiken_kbn) {
		this.konki_saiken_kbn = konki_saiken_kbn;
	}

	public String getKonki_saiken_zandaka_goukei() {
		return konki_saiken_zandaka_goukei;
	}

	public void setKonki_saiken_zandaka_goukei(String konki_saiken_zandaka_goukei) {
		this.konki_saiken_zandaka_goukei = konki_saiken_zandaka_goukei;
	}

	public String getKonki_sashiire_hoshou_kin() {
		return konki_sashiire_hoshou_kin;
	}

	public void setKonki_sashiire_hoshou_kin(String konki_sashiire_hoshou_kin) {
		this.konki_sashiire_hoshou_kin = konki_sashiire_hoshou_kin;
	}

	public String getKonki_sonota_toushi() {
		return konki_sonota_toushi;
	}

	public void setKonki_sonota_toushi(String konki_sonota_toushi) {
		this.konki_sonota_toushi = konki_sonota_toushi;
	}

	public String getKonki_tairyu_kbn() {
		return konki_tairyu_kbn;
	}

	public void setKonki_tairyu_kbn(String konki_tairyu_kbn) {
		this.konki_tairyu_kbn = konki_tairyu_kbn;
	}

	public String getKonki_tairyu_kbn_nm() {
		return konki_tairyu_kbn_nm;
	}

	public void setKonki_tairyu_kbn_nm(String konki_tairyu_kbn_nm) {
		this.konki_tairyu_kbn_nm = konki_tairyu_kbn_nm;
	}

	public String getKonki_tanki_kashituke_kin() {
		return konki_tanki_kashituke_kin;
	}

	public void setKonki_tanki_kashituke_kin(String konki_tanki_kashituke_kin) {
		this.konki_tanki_kashituke_kin = konki_tanki_kashituke_kin;
	}

	public String getKonki_tatekae_kin() {
		return konki_tatekae_kin;
	}

	public void setKonki_tatekae_kin(String konki_tatekae_kin) {
		this.konki_tatekae_kin = konki_tatekae_kin;
	}

	public String getKonki_torihiki_maewatashie_kin() {
		return konki_torihiki_maewatashie_kin;
	}

	public void setKonki_torihiki_maewatashie_kin(
			String konki_torihiki_maewatashie_kin) {
		this.konki_torihiki_maewatashie_kin = konki_torihiki_maewatashie_kin;
	}

	public String getKonki_torihikisaki_kbn() {
		return konki_torihikisaki_kbn;
	}

	public void setKonki_torihikisaki_kbn(String konki_torihikisaki_kbn) {
		this.konki_torihikisaki_kbn = konki_torihikisaki_kbn;
	}

	public String getKonki_uketori_tegata() {
		return konki_uketori_tegata;
	}

	public void setKonki_uketori_tegata(String konki_uketori_tegata) {
		this.konki_uketori_tegata = konki_uketori_tegata;
	}

	public String getKonki_urikake_kin() {
		return konki_urikake_kin;
	}

	public void setKonki_urikake_kin(String konki_urikake_kin) {
		this.konki_urikake_kin = konki_urikake_kin;
	}

	public String getKonki_ym() {
		return konki_ym;
	}

	public void setKonki_ym(String konki_ym) {
		this.konki_ym = konki_ym;
	}

	public String getKonki_yushutu_uketori_tegata() {
		return konki_yushutu_uketori_tegata;
	}

	public void setKonki_yushutu_uketori_tegata(String konki_yushutu_uketori_tegata) {
		this.konki_yushutu_uketori_tegata = konki_yushutu_uketori_tegata;
	}

	public String getTuuka_cd() {
		return tuuka_cd;
	}

	public void setTuuka_cd(String tuuka_cd) {
		this.tuuka_cd = tuuka_cd;
	}

	public String getZenki_chouki_kashituke_kin() {
		return zenki_chouki_kashituke_kin;
	}

	public void setZenki_chouki_kashituke_kin(String zenki_chouki_kashituke_kin) {
		this.zenki_chouki_kashituke_kin = zenki_chouki_kashituke_kin;
	}

	public String getZenki_commond() {
		return zenki_commond;
	}

	public void setZenki_commond(String zenki_commond) {
		this.zenki_commond = zenki_commond;
	}

	public String getZenki_company_nm() {
		return zenki_company_nm;
	}

	public void setZenki_company_nm(String zenki_company_nm) {
		this.zenki_company_nm = zenki_company_nm;
	}

	public String getZenki_hoshou_saimu_goukei() {
		return zenki_hoshou_saimu_goukei;
	}

	public void setZenki_hoshou_saimu_goukei(String zenki_hoshou_saimu_goukei) {
		this.zenki_hoshou_saimu_goukei = zenki_hoshou_saimu_goukei;
	}

	public String getZenki_ippan_saiken_kei() {
		return zenki_ippan_saiken_kei;
	}

	public void setZenki_ippan_saiken_kei(String zenki_ippan_saiken_kei) {
		this.zenki_ippan_saiken_kei = zenki_ippan_saiken_kei;
	}

	public String getZenki_karibarai_kin() {
		return zenki_karibarai_kin;
	}

	public void setZenki_karibarai_kin(String zenki_karibarai_kin) {
		this.zenki_karibarai_kin = zenki_karibarai_kin;
	}

	public String getZenki_ki_hikiate_kin() {
		return zenki_ki_hikiate_kin;
	}

	public void setZenki_ki_hikiate_kin(String zenki_ki_hikiate_kin) {
		this.zenki_ki_hikiate_kin = zenki_ki_hikiate_kin;
	}

	public String getZenki_ktk() {
		return zenki_ktk;
	}

	public void setZenki_ktk(String zenki_ktk) {
		this.zenki_ktk = zenki_ktk;
	}

	public String getZenki_mishuu_shuueki() {
		return zenki_mishuu_shuueki;
	}

	public void setZenki_mishuu_shuueki(String zenki_mishuu_shuueki) {
		this.zenki_mishuu_shuueki = zenki_mishuu_shuueki;
	}

	public String getZenki_mishuunyuu_kin() {
		return zenki_mishuunyuu_kin;
	}

	public void setZenki_mishuunyuu_kin(String zenki_mishuunyuu_kin) {
		this.zenki_mishuunyuu_kin = zenki_mishuunyuu_kin;
	}

	public String getZenki_oya_flg() {
		return zenki_oya_flg;
	}

	public void setZenki_oya_flg(String zenki_oya_flg) {
		this.zenki_oya_flg = zenki_oya_flg;
	}

	public String getZenki_oya_ktk() {
		return zenki_oya_ktk;
	}

	public void setZenki_oya_ktk(String zenki_oya_ktk) {
		this.zenki_oya_ktk = zenki_oya_ktk;
	}

	public String getZenki_saiken_kbn() {
		return zenki_saiken_kbn;
	}

	public void setZenki_saiken_kbn(String zenki_saiken_kbn) {
		this.zenki_saiken_kbn = zenki_saiken_kbn;
	}

	public String getZenki_saiken_zandaka_goukei() {
		return zenki_saiken_zandaka_goukei;
	}

	public void setZenki_saiken_zandaka_goukei(String zenki_saiken_zandaka_goukei) {
		this.zenki_saiken_zandaka_goukei = zenki_saiken_zandaka_goukei;
	}

	public String getZenki_sashiire_hoshou_kin() {
		return zenki_sashiire_hoshou_kin;
	}

	public void setZenki_sashiire_hoshou_kin(String zenki_sashiire_hoshou_kin) {
		this.zenki_sashiire_hoshou_kin = zenki_sashiire_hoshou_kin;
	}

	public String getZenki_sonota_toushi() {
		return zenki_sonota_toushi;
	}

	public void setZenki_sonota_toushi(String zenki_sonota_toushi) {
		this.zenki_sonota_toushi = zenki_sonota_toushi;
	}

	public String getZenki_tairyu_kbn() {
		return zenki_tairyu_kbn;
	}

	public void setZenki_tairyu_kbn(String zenki_tairyu_kbn) {
		this.zenki_tairyu_kbn = zenki_tairyu_kbn;
	}

	public String getZenki_tairyu_kbn_nm() {
		return zenki_tairyu_kbn_nm;
	}

	public void setZenki_tairyu_kbn_nm(String zenki_tairyu_kbn_nm) {
		this.zenki_tairyu_kbn_nm = zenki_tairyu_kbn_nm;
	}

	public String getZenki_tanki_kashituke_kin() {
		return zenki_tanki_kashituke_kin;
	}

	public void setZenki_tanki_kashituke_kin(String zenki_tanki_kashituke_kin) {
		this.zenki_tanki_kashituke_kin = zenki_tanki_kashituke_kin;
	}

	public String getZenki_tatekae_kin() {
		return zenki_tatekae_kin;
	}

	public void setZenki_tatekae_kin(String zenki_tatekae_kin) {
		this.zenki_tatekae_kin = zenki_tatekae_kin;
	}

	public String getZenki_torihiki_maewatashie_kin() {
		return zenki_torihiki_maewatashie_kin;
	}

	public void setZenki_torihiki_maewatashie_kin(
			String zenki_torihiki_maewatashie_kin) {
		this.zenki_torihiki_maewatashie_kin = zenki_torihiki_maewatashie_kin;
	}

	public String getZenki_torihikisaki_kbn() {
		return zenki_torihikisaki_kbn;
	}

	public void setZenki_torihikisaki_kbn(String zenki_torihikisaki_kbn) {
		this.zenki_torihikisaki_kbn = zenki_torihikisaki_kbn;
	}

	public String getZenki_uketori_tegata() {
		return zenki_uketori_tegata;
	}

	public void setZenki_uketori_tegata(String zenki_uketori_tegata) {
		this.zenki_uketori_tegata = zenki_uketori_tegata;
	}

	public String getZenki_urikake_kin() {
		return zenki_urikake_kin;
	}

	public void setZenki_urikake_kin(String zenki_urikake_kin) {
		this.zenki_urikake_kin = zenki_urikake_kin;
	}

	public String getZenki_ym() {
		return zenki_ym;
	}

	public void setZenki_ym(String zenki_ym) {
		this.zenki_ym = zenki_ym;
	}

	public String getZenki_yushutu_uketori_tegata() {
		return zenki_yushutu_uketori_tegata;
	}

	public void setZenki_yushutu_uketori_tegata(String zenki_yushutu_uketori_tegata) {
		this.zenki_yushutu_uketori_tegata = zenki_yushutu_uketori_tegata;
	}
	// 課題No.28
	// 追加開始
	public String getHikiatekin_shosai() {
		return hikiatekin_shosai;
	}
	public void setHikiatekin_shosai(String hikiatekin_shosai) {
		this.hikiatekin_shosai = hikiatekin_shosai;
	}
	// 追加完了
}