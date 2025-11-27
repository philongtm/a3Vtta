/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/21		SSC				1.5次版機能組込
******************************************************************************/
package app.commonZen.form;

import config.adapter.struts.action.ActionForm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 引当金判定タブFormクラス
 */
public class HikiatekinHanteiSyokaiForm extends ActionForm {
	/**
	 * 受取手形
	 */
	private String uketoritegata="";
	/**
	 * 輸出受取手形
	 */
	private String yusyutu_uketoritegata="";
	/**
	 * 売掛金
	 */
	private String urikakekin="";
	/**
	 * 取引前渡金
	 */
	private String torihikimaetokin="";
	/**
	 * 立替金
	 */
	private String tatekaekin="";
	/**
	 * 未収入金
	 */
	private String misyunyukin="";
	/**
	 * 未収収益
	 */
	private String misyusyueki="";
	/**
	 * 短期貸付金
	 */
	private String tanki_kashitsukekin="";
	/**
	 * 差入保証金
	 */
	private String sashiire_hosyokin="";
	/**
	 * 仮払金
	 */
	private String karibaraikin="";
	/**
	 * 長期貸付金
	 */
	private String tyoki_kashitsukekin="";
	/**
	 * その他投資
	 */
	private String sonota_toshi="";
	/**
	 * 一般債権計
	 */
	private String ipan_saimukei="";
	/**
	 * 固定化営業債権
	 */
	private String koteika_eigyosaiken="";
	/**
	 * 債権残高計
	 */
	private String saiken_zankei="";
	/**
	 * 留保債務
	 */
	private String ryuhosaimu="";
	/**
	 * 第三者留保債務
	 */
	private String oth_ryuhosaimu="";
	/**
	 * 留保債務計
	 */
	private String ryuhosaimu_kei="";
	/**
	 * 保全
	 */
	private String hozen="";
	/**
	 * その他回収
	 */
	private String sonotakaisyu="";
	/**
	 * 保証債務合計
	 */
	private String hosyosaimu_gokei="";
	/**
	 * 履行請求懸念
	 */
	private String riko_kenen="";
	/**
	 * 既引当金
	 */
	private String kibikiatekin="";
	/**
	 * 引当対象金額
	 */
	private String hikiate_taisyokingaku="";
	/**
	 * 追加引当金額
	 */
	private String tuika_hikiate="";
	/**
	 * 第三者留保債務内訳
	 */
	private ArrayList oth_ryuhosaimu_uchiwake; 
	/**
	 * 契約額-不動産担保
	 */
	private String hudosan_k;
	/**
	 * 契約額-動産担保
	 */
	private String dosan_k;
	/**
	 * 契約額-貿易保険
	 */
	private String hoken_k;
	/**
	 * 契約額-その他
	 */
	private String sonota_k;
	/**
	 * 評価額-不動産担保
	 */
	private String hudosan_h;
	/**
	 * 評価額-動産担保
	 */
	private String dosan_h;
	/**
	 * 評価額-貿易保険
	 */
	private String hoken_h;
	/**
	 * 評価額-その他
	 */
	private String sonota_h;
	/**
	 * その他の内容
	 */
	private String sonota_naiyo;
	/**
	 * その他回収の内容
	 */
	private String sonota_kaisyu_naiyo;
	/**
	 * 「履行請求概念」の内容説明
	 */
	private String rikoseikyu_kenen_naiyo;
	/**
	 * 「引当金算定根拠｣の内容説明
	 */
	private String hikiatekin_konkyo_naiyo;
	/**
	 * 今後の回収見通しなど
	 */
	private String kaisyu_naiyo;
	/**
	 * 通貨コード
	 */
	private String tuuka_cd;
	/**
	 * 処理回数制御
	 */
	private int initmode;
	/**
	 * 通貨単位
	 */
	private String tuuka_tani;
	//要件No.四-11
	//追加開始
	/**
	 * 第1/3四半期フラグ
	 */
	private String sihanki_flg = "";			//1:対象、それ以外:対象でない;
	/**
	 * フラグ区分
	 */
	private String flg_kbn = "";
	/**
	 * フラグ区分設定値
	 */
	private LinkedHashMap hm_flg_kbn = null;	
	/**
	 * 第2/4四半期抽出
	 */
	private String hanki_tyusyutu = "";		//1:対象、それ以外:対象でない  (仕様変更 画面には表示しない)
	/**
	 * フラグ期限
	 */
	private String flg_kigen = "";				//YYYYMM形式  (仕様変更 画面には表示しない)
	/**
	 * フラグコメント (登録ポイント95)
	 */
	private String flg_comment = "";
	//追加完了
	/**
	 * @return tuuka_tani を戻します。
	 */
	public String getTuuka_tani() {
		return tuuka_tani;
	}
	/**
	 * @param tuuka_tani tuuka_tani を設定。
	 */
	public void setTuuka_tani(String tuuka_tani) {
		this.tuuka_tani = tuuka_tani;
	}
	
	public int getInitmode() {
		return initmode;
	}
	public void setInitmode(int initmode) {
		this.initmode = initmode;
	}
	/**
	 * @return tuuka_cd を戻します。
	 */
	public String getTuuka_cd() {
		return tuuka_cd;
	}
	/**
	 * @param tuuka_cd tuuka_cd を設定。
	 */
	public void setTuuka_cd(String tuuka_cd) {
		this.tuuka_cd = tuuka_cd;
	}
	/**
	 * @return hyoka_boekihoken を戻します。
	 */
	public String getHoken_h() {
		return hoken_h;
	}
	/**
	 * @param hyoka_boekihoken hyoka_boekihoken を設定。
	 */
	public void setHoken_h(String hyoka_boekihoken) {
		this.hoken_h = hyoka_boekihoken;
	}
	/**
	 * @return hyoka_dousan を戻します。
	 */
	public String getDosan_h() {
		return dosan_h;
	}
	/**
	 * @param hyoka_dousan hyoka_dousan を設定。
	 */
	public void setDosan_h(String hyoka_dousan) {
		this.dosan_h = hyoka_dousan;
	}
	/**
	 * @return hyoka_hudosan を戻します。
	 */
	public String getHudosan_h() {
		return hudosan_h;
	}
	/**
	 * @param hyoka_hudosan hyoka_hudosan を設定。
	 */
	public void setHudosan_h(String hyoka_hudosan) {
		this.hudosan_h = hyoka_hudosan;
	}
	/**
	 * @return hyoka_sonota を戻します。
	 */
	public String getSonota_h() {
		return sonota_h;
	}
	/**
	 * @param hyoka_sonota hyoka_sonota を設定。
	 */
	public void setSonota_h(String hyoka_sonota) {
		this.sonota_h = hyoka_sonota;
	}
	/**
	 * @return keiyaku_boekihoken を戻します。
	 */
	public String getHoken_k() {
		return hoken_k;
	}
	/**
	 * @param keiyaku_boekihoken keiyaku_boekihoken を設定。
	 */
	public void setHoken_k(String keiyaku_boekihoken) {
		this.hoken_k = keiyaku_boekihoken;
	}
	/**
	 * @return keiyaku_dousan を戻します。
	 */
	public String getDosan_k() {
		return dosan_k;
	}
	/**
	 * @param keiyaku_dousan keiyaku_dousan を設定。
	 */
	public void setDosan_k(String keiyaku_dousan) {
		this.dosan_k = keiyaku_dousan;
	}
	/**
	 * @return keiyaku_hudosan を戻します。
	 */
	public String getHudosan_k() {
		return hudosan_k;
	}
	/**
	 * @param keiyaku_hudosan keiyaku_hudosan を設定。
	 */
	public void setHudosan_k(String keiyaku_hudosan) {
		this.hudosan_k = keiyaku_hudosan;
	}
	/**
	 * @return keiyaku_sonota を戻します。
	 */
	public String getSonota_k() {
		return sonota_k;
	}
	/**
	 * @param keiyaku_sonota keiyaku_sonota を設定。
	 */
	public void setSonota_k(String keiyaku_sonota) {
		this.sonota_k = keiyaku_sonota;
	}
	/**
	 * @return daisansya_ryuhosaimu を戻します。
	 */
	public String getOth_ryuhosaimu() {
		return oth_ryuhosaimu;
	}
	/**
	 * @param daisansya_ryuhosaimu daisansya_ryuhosaimu を設定。
	 */
	public void setOth_ryuhosaimu(String daisansya_ryuhosaimu) {
		this.oth_ryuhosaimu = daisansya_ryuhosaimu;
	}
	/**
	 * @return daisansya_ryuhosaimu_uchiwake を戻します。
	 */
	public ArrayList getOth_ryuhosaimu_uchiwake() {
		return oth_ryuhosaimu_uchiwake;
	}
	/**
	 * @param daisansya_ryuhosaimu_uchiwake daisansya_ryuhosaimu_uchiwake を設定。
	 */
	public void setOth_ryuhosaimu_uchiwake(
			ArrayList daisansya_ryuhosaimu_uchiwake) {
		this.oth_ryuhosaimu_uchiwake = daisansya_ryuhosaimu_uchiwake;
	}
	/**
	 * @return hikiate_taisyokingaku を戻します。
	 */
	public String getHikiate_taisyokingaku() {
		return hikiate_taisyokingaku;
	}
	/**
	 * @param hikiate_taisyokingaku hikiate_taisyokingaku を設定。
	 */
	public void setHikiate_taisyokingaku(String hikiate_taisyokingaku) {
		this.hikiate_taisyokingaku = hikiate_taisyokingaku;
	}
	/**
	 * @return hikiatekin_konkyo_naiyo を戻します。
	 */
	public String getHikiatekin_konkyo_naiyo() {
		return hikiatekin_konkyo_naiyo;
	}
	/**
	 * @param hikiatekin_konkyo_naiyo hikiatekin_konkyo_naiyo を設定。
	 */
	public void setHikiatekin_konkyo_naiyo(String hikiatekin_konkyo_naiyo) {
		this.hikiatekin_konkyo_naiyo = hikiatekin_konkyo_naiyo;
	}
	/**
	 * @return hosyosaimu_gokei を戻します。
	 */
	public String getHosyosaimu_gokei() {
		return hosyosaimu_gokei;
	}
	/**
	 * @param hosyosaimu_gokei hosyosaimu_gokei を設定。
	 */
	public void setHosyosaimu_gokei(String hosyosaimu_gokei) {
		this.hosyosaimu_gokei = hosyosaimu_gokei;
	}
	/**
	 * @return hozen を戻します。
	 */
	public String getHozen() {
		return hozen;
	}
	/**
	 * @param hozen hozen を設定。
	 */
	public void setHozen(String hozen) {
		this.hozen = hozen;
	}
	/**
	 * @return ipan_saimukei を戻します。
	 */
	public String getIpan_saimukei() {
		return ipan_saimukei;
	}
	/**
	 * @param ipan_saimukei ipan_saimukei を設定。
	 */
	public void setIpan_saimukei(String ipan_saimukei) {
		this.ipan_saimukei = ipan_saimukei;
	}
	/**
	 * @return kaisyu_naiyo を戻します。
	 */
	public String getKaisyu_naiyo() {
		return kaisyu_naiyo;
	}
	/**
	 * @param kaisyu_naiyo kaisyu_naiyo を設定。
	 */
	public void setKaisyu_naiyo(String kaisyu_naiyo) {
		this.kaisyu_naiyo = kaisyu_naiyo;
	}
	/**
	 * @return katibaraikin を戻します。
	 */
	public String getKaribaraikin() {
		return karibaraikin;
	}
	/**
	 * @param katibaraikin katibaraikin を設定。
	 */
	public void setKaribaraikin(String katibaraikin) {
		this.karibaraikin = katibaraikin;
	}
	/**
	 * @return kibikiatekin を戻します。
	 */
	public String getKibikiatekin() {
		return kibikiatekin;
	}
	/**
	 * @param kibikiatekin kibikiatekin を設定。
	 */
	public void setKibikiatekin(String kibikiatekin) {
		this.kibikiatekin = kibikiatekin;
	}
	/**
	 * @return koteika_eigyosaiken を戻します。
	 */
	public String getKoteika_eigyosaiken() {
		return koteika_eigyosaiken;
	}
	/**
	 * @param koteika_eigyosaiken koteika_eigyosaiken を設定。
	 */
	public void setKoteika_eigyosaiken(String koteika_eigyosaiken) {
		this.koteika_eigyosaiken = koteika_eigyosaiken;
	}
	/**
	 * @return misyunyukin を戻します。
	 */
	public String getMisyunyukin() {
		return misyunyukin;
	}
	/**
	 * @param misyunyukin misyunyukin を設定。
	 */
	public void setMisyunyukin(String misyunyukin) {
		this.misyunyukin = misyunyukin;
	}
	/**
	 * @return misyusyueki を戻します。
	 */
	public String getMisyusyueki() {
		return misyusyueki;
	}
	/**
	 * @param misyusyueki misyusyueki を設定。
	 */
	public void setMisyusyueki(String misyusyueki) {
		this.misyusyueki = misyusyueki;
	}
	/**
	 * @return riko_seikyu_kenen を戻します。
	 */
	public String getRiko_kenen() {
		return riko_kenen;
	}
	/**
	 * @param riko_seikyu_kenen riko_seikyu_kenen を設定。
	 */
	public void setRiko_kenen(String riko_seikyu_kenen) {
		this.riko_kenen = riko_seikyu_kenen;
	}
	/**
	 * @return rikoseikyu_kenen_naiyo を戻します。
	 */
	public String getRikoseikyu_kenen_naiyo() {
		return rikoseikyu_kenen_naiyo;
	}
	/**
	 * @param rikoseikyu_kenen_naiyo rikoseikyu_kenen_naiyo を設定。
	 */
	public void setRikoseikyu_kenen_naiyo(String rikoseikyu_kenen_naiyo) {
		this.rikoseikyu_kenen_naiyo = rikoseikyu_kenen_naiyo;
	}
	/**
	 * @return ryuhosaimu を戻します。
	 */
	public String getRyuhosaimu() {
		return ryuhosaimu;
	}
	/**
	 * @param ryuhosaimu ryuhosaimu を設定。
	 */
	public void setRyuhosaimu(String ryuhosaimu) {
		this.ryuhosaimu = ryuhosaimu;
	}
	/**
	 * @return ryuhosaimu_kei を戻します。
	 */
	public String getRyuhosaimu_kei() {
		return ryuhosaimu_kei;
	}
	/**
	 * @param ryuhosaimu_kei ryuhosaimu_kei を設定。
	 */
	public void setRyuhosaimu_kei(String ryuhosaimu_kei) {
		this.ryuhosaimu_kei = ryuhosaimu_kei;
	}
	/**
	 * @return saimu_zankei を戻します。
	 */
	public String getSaiken_zankei() {
		return saiken_zankei;
	}
	/**
	 * @param saimu_zankei saimu_zankei を設定。
	 */
	public void setSaiken_zankei(String saimu_zankei) {
		this.saiken_zankei = saimu_zankei;
	}
	/**
	 * @return sashiire_hosyokin を戻します。
	 */
	public String getSashiire_hosyokin() {
		return sashiire_hosyokin;
	}
	/**
	 * @param sashiire_hosyokin sashiire_hosyokin を設定。
	 */
	public void setSashiire_hosyokin(String sashiire_hosyokin) {
		this.sashiire_hosyokin = sashiire_hosyokin;
	}
	/**
	 * @return sonota_kaisyu を戻します。
	 */
	public String getSonotakaisyu() {
		return sonotakaisyu;
	}
	/**
	 * @param sonota_kaisyu sonota_kaisyu を設定。
	 */
	public void setSonotakaisyu(String sonota_kaisyu) {
		this.sonotakaisyu = sonota_kaisyu;
	}
	/**
	 * @return sonota_kaisyu_naiyo を戻します。
	 */
	public String getSonota_kaisyu_naiyo() {
		return sonota_kaisyu_naiyo;
	}
	/**
	 * @param sonota_kaisyu_naiyo sonota_kaisyu_naiyo を設定。
	 */
	public void setSonota_kaisyu_naiyo(String sonota_kaisyu_naiyo) {
		this.sonota_kaisyu_naiyo = sonota_kaisyu_naiyo;
	}
	/**
	 * @return sonota_naiyo を戻します。
	 */
	public String getSonota_naiyo() {
		return sonota_naiyo;
	}
	/**
	 * @param sonota_naiyo sonota_naiyo を設定。
	 */
	public void setSonota_naiyo(String sonota_naiyo) {
		this.sonota_naiyo = sonota_naiyo;
	}
	/**
	 * @return sonota_toshi を戻します。
	 */
	public String getSonota_toshi() {
		return sonota_toshi;
	}
	/**
	 * @param sonota_toshi sonota_toshi を設定。
	 */
	public void setSonota_toshi(String sonota_toshi) {
		this.sonota_toshi = sonota_toshi;
	}
	/**
	 * @return tanki_kashitsukekin を戻します。
	 */
	public String getTanki_kashitsukekin() {
		return tanki_kashitsukekin;
	}
	/**
	 * @param tanki_kashitsukekin tanki_kashitsukekin を設定。
	 */
	public void setTanki_kashitsukekin(String tanki_kashitsukekin) {
		this.tanki_kashitsukekin = tanki_kashitsukekin;
	}
	/**
	 * @return tatekaekin を戻します。
	 */
	public String getTatekaekin() {
		return tatekaekin;
	}
	/**
	 * @param tatekaekin tatekaekin を設定。
	 */
	public void setTatekaekin(String tatekaekin) {
		this.tatekaekin = tatekaekin;
	}
	/**
	 * @return torihikimaetokin を戻します。
	 */
	public String getTorihikimaetokin() {
		return torihikimaetokin;
	}
	/**
	 * @param torihikimaetokin torihikimaetokin を設定。
	 */
	public void setTorihikimaetokin(String torihikimaetokin) {
		this.torihikimaetokin = torihikimaetokin;
	}
	/**
	 * @return tuika_hikiatekin を戻します。
	 */
	public String getTuika_hikiate() {
		return tuika_hikiate;
	}
	/**
	 * @param tuika_hikiatekin tuika_hikiatekin を設定。
	 */
	public void setTuika_hikiate(String tuika_hikiatekin) {
		this.tuika_hikiate = tuika_hikiatekin;
	}
	/**
	 * @return tyoki_kashitsukekin を戻します。
	 */
	public String getTyoki_kashitsukekin() {
		return tyoki_kashitsukekin;
	}
	/**
	 * @param tyoki_kashitsukekin tyoki_kashitsukekin を設定。
	 */
	public void setTyoki_kashitsukekin(String tyoki_kashitsukekin) {
		this.tyoki_kashitsukekin = tyoki_kashitsukekin;
	}
	/**
	 * @return uketoritegata を戻します。
	 */
	public String getUketoritegata() {
		return uketoritegata;
	}
	/**
	 * @param uketoritegata uketoritegata を設定。
	 */
	public void setUketoritegata(String uketoritegata) {
		this.uketoritegata = uketoritegata;
	}
	/**
	 * @return urikakekin を戻します。
	 */
	public String getUrikakekin() {
		return urikakekin;
	}
	/**
	 * @param urikakekin urikakekin を設定。
	 */
	public void setUrikakekin(String urikakekin) {
		this.urikakekin = urikakekin;
	}
	/**
	 * @return yusyutu_uketoritegata を戻します。
	 */
	public String getYusyutu_uketoritegata() {
		return yusyutu_uketoritegata;
	}
	/**
	 * @param yusyutu_uketoritegata yusyutu_uketoritegata を設定。
	 */
	public void setYusyutu_uketoritegata(String yusyutu_uketoritegata) {
		this.yusyutu_uketoritegata = yusyutu_uketoritegata;
	}
	//要件No.四-11
	//追加開始
	//第1/3四半期フラグ
	public String getSihanki_flg() {
		return sihanki_flg;
	}
	public void setSihanki_flg(String sihanki_flg) {
		this.sihanki_flg = sihanki_flg;
	}
	//フラグ区分
	public String getFlg_kbn() {
		return flg_kbn;
	}
	public void setFlg_kbn(String flg_kbn) {
		this.flg_kbn = flg_kbn;
	}
	//フラグ区分設定値格納マップ
	public LinkedHashMap getHm_flg_kbn() {
		return hm_flg_kbn;
	}
	public void setHm_flg_kbn(LinkedHashMap hm_flg_kbn) {
		this.hm_flg_kbn = hm_flg_kbn;
	}
	//第2/4四半期抽出
	public String getHanki_tyusyutu() {
		return hanki_tyusyutu;
	}
	public void setHanki_tyusyutu(String hanki_tyusyutu) {
		this.hanki_tyusyutu = hanki_tyusyutu;
	}
	//フラグ期限
	public String getFlg_kigen() {
		return flg_kigen;
	}
	public void setFlg_kigen(String flg_kigen) {
		this.flg_kigen = flg_kigen;
	}
	//フラグコメント
	public String getFlg_comment() {
		return flg_comment;
	}
	public void setFlg_comment(String flg_comment) {
		this.flg_comment = flg_comment;
	}
	//追加完了
}
