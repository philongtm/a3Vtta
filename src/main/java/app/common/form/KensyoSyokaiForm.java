/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/5/19		SSC 			1.5次版機能組込 
002		2009/10/20		SSC				課題No.60 引当金検証時の取引先区分・債権区分設定
******************************************************************************/
package app.common.form;

import common.struts.adapter.action.ActionForm;

import java.util.LinkedHashMap;

/**
 * 引当金検証タブFormクラス
 */
public class KensyoSyokaiForm extends ActionForm {

	/**
	 * 表示データセレクトボックス値
	 */
	private String hyoji;
	/**
	 * 査定年月
	 */
	private String satei_ym;
	/**
	 * 基準年月
	 */
	private String kijun_ym;
	// 初回・中間月データ
	/**
	 * 取引先区分
	 */
	private String tori_kbn;
	/**
	 * 債権区分
	 */
	private String saiken_kbn;
	/**
	 * 通貨コード
	 */
	private String tuuka_cd_1;
	/**
	 * 受取手形
	 */
	private String uketoritegata_1;
	/**
	 * 輸出受取手形
	 */
	private String yusyutu_uketoritegata_1;
	/**
	 * 売掛金
	 */
	private String urikakekin_1;
	/**
	 * 取引前渡金
	 */
	private String torihikimaetokin_1;
	/**
	 * 立替金
	 */
	private String tatekaekin_1;
	/**
	 * 未収入金
	 */
	private String misyunyukin_1;
	/**
	 * 未収収益
	 */
	private String misyusyueki_1;
	/**
	 * 短期貸付金
	 */
	private String tanki_kashitsukekin_1;
	/**
	 * 差入保証金
	 */
	private String sashiire_hosyokin_1;
	/**
	 * 仮払金
	 */
	private String karibaraikin_1;
	/**
	 * 長期貸付金
	 */
	private String tyoki_kashitsukekin_1;
	/**
	 * その他投資
	 */
	private String sonota_toshi_1;
	/**
	 * 一般債権計
	 */
	private String ipan_saimukei_1;
	/**
	 * 固定化営業債権
	 */
	private String koteika_eigyosaiken_1;
	/**
	 * 債権残高計
	 */
	private String saiken_zankei_1;
	/**
	 * 留保債務
	 */
	private String ryuhosaimu_1;
	/**
	 * 第三者留保債務
	 */
	private String oth_ryuhosaimu_1;
	/**
	 * 留保債務計
	 */
	private String ryuhosaimu_kei_1;
	/**
	 * 保全
	 */
	private String hozen_1;
	/**
	 * その他回収
	 */
	private String sonotakaisyu_1;
	/**
	 * 保証債務合計
	 */
	private String hosyosaimu_gokei_1;
	/**
	 * 履行請求懸念
	 */
	private String riko_kenen_1;
	/**
	 * 既引当金
	 */
	private String kibikiatekin_1;
	/**
	 * 引当対象金額
	 */
	private String hikiate_taisyokingaku_1;
	/**
	 * 追加引当金額
	 */
	private String tuika_hikiate_1;
	/**
	 * 引当金算定根拠
	 */
	private String hikiate_cmt;
	
	// 最終月データ
	// 課題No.60
	// 追加開始
	/**
	 * 取引先区分
	 */
	private String final_tori_kbn;
	/**
	 * 債権区分
	 */
	private String final_saiken_kbn;
	// 追加完了
	/**
	 * 通貨コード
	 */
	private String tuuka_cd_2;
	
	/**
	 * 受取手形
	 */
	private String uketoritegata_2;
	/**
	 * 輸出受取手形
	 */
	private String yusyutu_uketoritegata_2;
	/**
	 * 売掛金
	 */
	private String urikakekin_2;
	/**
	 * 取引前渡金
	 */
	private String torihikimaetokin_2;
	/**
	 * 立替金
	 */
	private String tatekaekin_2;
	/**
	 * 未収入金
	 */
	private String misyunyukin_2;
	/**
	 * 未収収益
	 */
	private String misyusyueki_2;
	/**
	 * 短期貸付金
	 */
	private String tanki_kashitsukekin_2;
	/**
	 * 差入保証金
	 */
	private String sashiire_hosyokin_2;
	/**
	 * 仮払金
	 */
	private String karibaraikin_2;
	/**
	 * 長期貸付金
	 */
	private String tyoki_kashitsukekin_2;
	/**
	 * その他投資
	 */
	private String sonota_toshi_2;
	/**
	 * 一般債権計
	 */
	private String ipan_saimukei_2;
	/**
	 * 固定化営業債権
	 */
	private String koteika_eigyosaiken_2;
	/**
	 * 債権残高計
	 */
	private String saiken_zankei_2;
	/**
	 * 留保債務
	 */
	private String ryuhosaimu_2;
	/**
	 * 第三者留保債務
	 */
	private String oth_ryuhosaimu_2;
	/**
	 * 留保債務計
	 */
	private String ryuhosaimu_kei_2;
	/**
	 * 保全
	 */
	private String hozen_2;
	/**
	 * その他回収
	 */
	private String sonotakaisyu_2;
	/**
	 * 保証債務合計
	 */
	private String hosyosaimu_gokei_2;
	/**
	 * 履行請求懸念
	 */
	private String riko_kenen_2;
	/**
	 * 既引当金
	 */
	private String kibikiatekin_2;
	/**
	 * 引当対象金額
	 */
	private String hikiate_taisyokingaku_2;
	/**
	 * 追加引当金額
	 */
	private String tuika_hikiate_2;
	/**
	 * 引当控除後残高
	 */
	private String hikiate_kojo;
	/**
	 * 引当金補正額
	 */
	private String hikiatekin_hosei;
	/**
	 * 補正後引当金額
	 */
	private String hoseigo_hikiate;
	/**
	 * 補正後引当控除後残高
	 */
	private String hoseigo_hikiatekojo_zan;
	/**
	 * 引当金検証引当金算定根拠
	 */
	private String hikiatekin_kensyo_cmt;
	/**
	 * 処理回数制御
	 */
	private int initmode;
	/** 処理回数セレクトボックス値 */
	private LinkedHashMap syoriKaisuList;
	
	
	public LinkedHashMap getSyoriKaisuList() {
		return syoriKaisuList;
	}
	public void setSyoriKaisuList(LinkedHashMap syoriKaisuList) {
		this.syoriKaisuList = syoriKaisuList;
	}
	public int getInitmode() {
		return initmode;
	}
	public void setInitmode(int initmode) {
		this.initmode = initmode;
	}
	public String getHikiatekin_kensyo_cmt() {
		return hikiatekin_kensyo_cmt;
	}
	public void setHikiatekin_kensyo_cmt(String hikiatekin_kensyo_cmt) {
		this.hikiatekin_kensyo_cmt = hikiatekin_kensyo_cmt;
	}
	public String getHikiate_cmt() {
		return hikiate_cmt;
	}
	public void setHikiate_cmt(String hikiate_cmt) {
		this.hikiate_cmt = hikiate_cmt;
	}
	public String getKijun_ym() {
		return kijun_ym;
	}
	public void setKijun_ym(String kijun_ym) {
		this.kijun_ym = kijun_ym;
	}
	// 課題No.60 引当金検証時の取引先区分・債権区分設定
	// 追加開始
	public String getFinal_tori_kbn() {
		return final_tori_kbn;
	}
	public void setFinal_tori_kbn(String final_tori_kbn) {
		this.final_tori_kbn = final_tori_kbn;
	}
	public String getFinal_saiken_kbn() {
		return final_saiken_kbn;
	}
	public void setFinal_Saiken_kbn(String final_saiken_kbn) {
		this.final_saiken_kbn = final_saiken_kbn;
	}
	// 追加完了
	/**
	 * @return hyoji を戻します。
	 */
	public String getHyoji() {
		return hyoji;
	}
	/**
	 * @param hyoji hyoji を設定。
	 */
	public void setHyoji(String hyoji) {
		this.hyoji = hyoji;
	}
	/**
	 * @return saiken_kbn を戻します。
	 */
	public String getSaiken_kbn() {
		return saiken_kbn;
	}
	/**
	 * @param saiken_kbn saiken_kbn を設定。
	 */
	public void setSaiken_kbn(String saiken_kbn) {
		this.saiken_kbn = saiken_kbn;
	}
	/**
	 * @return satei_ym を戻します。
	 */
	public String getSatei_ym() {
		return satei_ym;
	}
	/**
	 * @param satei_ym satei_ym を設定。
	 */
	public void setSatei_ym(String satei_ym) {
		this.satei_ym = satei_ym;
	}
	/**
	 * @return tori_kbn を戻します。
	 */
	public String getTori_kbn() {
		return tori_kbn;
	}
	/**
	 * @param tori_kbn tori_kbn を設定。
	 */
	public void setTori_kbn(String tori_kbn) {
		this.tori_kbn = tori_kbn;
	}
	/**
	 * @return tuuka_cd_1 を戻します。
	 */
	public String getTuuka_cd_1() {
		return tuuka_cd_1;
	}
	/**
	 * @param tuuka_cd_1 tuuka_cd_1 を設定。
	 */
	public void setTuuka_cd_1(String tuuka_cd_1) {
		this.tuuka_cd_1 = tuuka_cd_1;
	}
	/**
	 * @return tuuka_cd_2 を戻します。
	 */
	public String getTuuka_cd_2() {
		return tuuka_cd_2;
	}
	/**
	 * @param tuuka_cd_2 tuuka_cd_2 を設定。
	 */
	public void setTuuka_cd_2(String tuuka_cd_2) {
		this.tuuka_cd_2 = tuuka_cd_2;
	}
	/**
	 * @return hikiate_kojo を戻します。
	 */
	public String getHikiate_kojo() {
		return hikiate_kojo;
	}
	/**
	 * @param hikiate_kojo hikiate_kojo を設定。
	 */
	public void setHikiate_kojo(String hikiate_kojo) {
		this.hikiate_kojo = hikiate_kojo;
	}
	/**
	 * @return hikiate_taisyokingaku_1 を戻します。
	 */
	public String getHikiate_taisyokingaku_1() {
		return hikiate_taisyokingaku_1;
	}
	/**
	 * @param hikiate_taisyokingaku_1 hikiate_taisyokingaku_1 を設定。
	 */
	public void setHikiate_taisyokingaku_1(String hikiate_taisyokingaku_1) {
		this.hikiate_taisyokingaku_1 = hikiate_taisyokingaku_1;
	}
	/**
	 * @return hikiate_taisyokingaku_2 を戻します。
	 */
	public String getHikiate_taisyokingaku_2() {
		return hikiate_taisyokingaku_2;
	}
	/**
	 * @param hikiate_taisyokingaku_2 hikiate_taisyokingaku_2 を設定。
	 */
	public void setHikiate_taisyokingaku_2(String hikiate_taisyokingaku_2) {
		this.hikiate_taisyokingaku_2 = hikiate_taisyokingaku_2;
	}
	/**
	 * @return hikiatekin_hosei を戻します。
	 */
	public String getHikiatekin_hosei() {
		return hikiatekin_hosei;
	}
	/**
	 * @param hikiatekin_hosei hikiatekin_hosei を設定。
	 */
	public void setHikiatekin_hosei(String hikiatekin_hosei) {
		this.hikiatekin_hosei = hikiatekin_hosei;
	}
	/**
	 * @return hoseigo_hikiate を戻します。
	 */
	public String getHoseigo_hikiate() {
		return hoseigo_hikiate;
	}
	/**
	 * @param hoseigo_hikiate hoseigo_hikiate を設定。
	 */
	public void setHoseigo_hikiate(String hoseigo_hikiate) {
		this.hoseigo_hikiate = hoseigo_hikiate;
	}
	/**
	 * @return hoseigo_hikiatekojo_zan を戻します。
	 */
	public String getHoseigo_hikiatekojo_zan() {
		return hoseigo_hikiatekojo_zan;
	}
	/**
	 * @param hoseigo_hikiatekojo_zan hoseigo_hikiatekojo_zan を設定。
	 */
	public void setHoseigo_hikiatekojo_zan(String hoseigo_hikiatekojo_zan) {
		this.hoseigo_hikiatekojo_zan = hoseigo_hikiatekojo_zan;
	}
	/**
	 * @return hosyosaimu_gokei_1 を戻します。
	 */
	public String getHosyosaimu_gokei_1() {
		return hosyosaimu_gokei_1;
	}
	/**
	 * @param hosyosaimu_gokei_1 hosyosaimu_gokei_1 を設定。
	 */
	public void setHosyosaimu_gokei_1(String hosyosaimu_gokei_1) {
		this.hosyosaimu_gokei_1 = hosyosaimu_gokei_1;
	}
	/**
	 * @return hosyosaimu_gokei_2 を戻します。
	 */
	public String getHosyosaimu_gokei_2() {
		return hosyosaimu_gokei_2;
	}
	/**
	 * @param hosyosaimu_gokei_2 hosyosaimu_gokei_2 を設定。
	 */
	public void setHosyosaimu_gokei_2(String hosyosaimu_gokei_2) {
		this.hosyosaimu_gokei_2 = hosyosaimu_gokei_2;
	}
	/**
	 * @return hozen_1 を戻します。
	 */
	public String getHozen_1() {
		return hozen_1;
	}
	/**
	 * @param hozen_1 hozen_1 を設定。
	 */
	public void setHozen_1(String hozen_1) {
		this.hozen_1 = hozen_1;
	}
	/**
	 * @return hozen_2 を戻します。
	 */
	public String getHozen_2() {
		return hozen_2;
	}
	/**
	 * @param hozen_2 hozen_2 を設定。
	 */
	public void setHozen_2(String hozen_2) {
		this.hozen_2 = hozen_2;
	}
	/**
	 * @return ipan_saimukei_1 を戻します。
	 */
	public String getIpan_saimukei_1() {
		return ipan_saimukei_1;
	}
	/**
	 * @param ipan_saimukei_1 ipan_saimukei_1 を設定。
	 */
	public void setIpan_saimukei_1(String ipan_saimukei_1) {
		this.ipan_saimukei_1 = ipan_saimukei_1;
	}
	/**
	 * @return ipan_saimukei_2 を戻します。
	 */
	public String getIpan_saimukei_2() {
		return ipan_saimukei_2;
	}
	/**
	 * @param ipan_saimukei_2 ipan_saimukei_2 を設定。
	 */
	public void setIpan_saimukei_2(String ipan_saimukei_2) {
		this.ipan_saimukei_2 = ipan_saimukei_2;
	}
	/**
	 * @return karibaraikin_1 を戻します。
	 */
	public String getKaribaraikin_1() {
		return karibaraikin_1;
	}
	/**
	 * @param karibaraikin_1 karibaraikin_1 を設定。
	 */
	public void setKaribaraikin_1(String karibaraikin_1) {
		this.karibaraikin_1 = karibaraikin_1;
	}
	/**
	 * @return karibaraikin_2 を戻します。
	 */
	public String getKaribaraikin_2() {
		return karibaraikin_2;
	}
	/**
	 * @param karibaraikin_2 karibaraikin_2 を設定。
	 */
	public void setKaribaraikin_2(String karibaraikin_2) {
		this.karibaraikin_2 = karibaraikin_2;
	}
	/**
	 * @return kibikiatekin_1 を戻します。
	 */
	public String getKibikiatekin_1() {
		return kibikiatekin_1;
	}
	/**
	 * @param kibikiatekin_1 kibikiatekin_1 を設定。
	 */
	public void setKibikiatekin_1(String kibikiatekin_1) {
		this.kibikiatekin_1 = kibikiatekin_1;
	}
	/**
	 * @return kibikiatekin_2 を戻します。
	 */
	public String getKibikiatekin_2() {
		return kibikiatekin_2;
	}
	/**
	 * @param kibikiatekin_2 kibikiatekin_2 を設定。
	 */
	public void setKibikiatekin_2(String kibikiatekin_2) {
		this.kibikiatekin_2 = kibikiatekin_2;
	}
	/**
	 * @return koteika_eigyosaiken_1 を戻します。
	 */
	public String getKoteika_eigyosaiken_1() {
		return koteika_eigyosaiken_1;
	}
	/**
	 * @param koteika_eigyosaiken_1 koteika_eigyosaiken_1 を設定。
	 */
	public void setKoteika_eigyosaiken_1(String koteika_eigyosaiken_1) {
		this.koteika_eigyosaiken_1 = koteika_eigyosaiken_1;
	}
	/**
	 * @return koteika_eigyosaiken_2 を戻します。
	 */
	public String getKoteika_eigyosaiken_2() {
		return koteika_eigyosaiken_2;
	}
	/**
	 * @param koteika_eigyosaiken_2 koteika_eigyosaiken_2 を設定。
	 */
	public void setKoteika_eigyosaiken_2(String koteika_eigyosaiken_2) {
		this.koteika_eigyosaiken_2 = koteika_eigyosaiken_2;
	}
	/**
	 * @return misyunyukin_1 を戻します。
	 */
	public String getMisyunyukin_1() {
		return misyunyukin_1;
	}
	/**
	 * @param misyunyukin_1 misyunyukin_1 を設定。
	 */
	public void setMisyunyukin_1(String misyunyukin_1) {
		this.misyunyukin_1 = misyunyukin_1;
	}
	/**
	 * @return misyunyukin_2 を戻します。
	 */
	public String getMisyunyukin_2() {
		return misyunyukin_2;
	}
	/**
	 * @param misyunyukin_2 misyunyukin_2 を設定。
	 */
	public void setMisyunyukin_2(String misyunyukin_2) {
		this.misyunyukin_2 = misyunyukin_2;
	}
	/**
	 * @return misyusyueki_1 を戻します。
	 */
	public String getMisyusyueki_1() {
		return misyusyueki_1;
	}
	/**
	 * @param misyusyueki_1 misyusyueki_1 を設定。
	 */
	public void setMisyusyueki_1(String misyusyueki_1) {
		this.misyusyueki_1 = misyusyueki_1;
	}
	/**
	 * @return misyusyueki_2 を戻します。
	 */
	public String getMisyusyueki_2() {
		return misyusyueki_2;
	}
	/**
	 * @param misyusyueki_2 misyusyueki_2 を設定。
	 */
	public void setMisyusyueki_2(String misyusyueki_2) {
		this.misyusyueki_2 = misyusyueki_2;
	}
	/**
	 * @return oth_ryuhosaimu_1 を戻します。
	 */
	public String getOth_ryuhosaimu_1() {
		return oth_ryuhosaimu_1;
	}
	/**
	 * @param oth_ryuhosaimu_1 oth_ryuhosaimu_1 を設定。
	 */
	public void setOth_ryuhosaimu_1(String oth_ryuhosaimu_1) {
		this.oth_ryuhosaimu_1 = oth_ryuhosaimu_1;
	}
	/**
	 * @return oth_ryuhosaimu_2 を戻します。
	 */
	public String getOth_ryuhosaimu_2() {
		return oth_ryuhosaimu_2;
	}
	/**
	 * @param oth_ryuhosaimu_2 oth_ryuhosaimu_2 を設定。
	 */
	public void setOth_ryuhosaimu_2(String oth_ryuhosaimu_2) {
		this.oth_ryuhosaimu_2 = oth_ryuhosaimu_2;
	}
	/**
	 * @return riko_kenen_1 を戻します。
	 */
	public String getRiko_kenen_1() {
		return riko_kenen_1;
	}
	/**
	 * @param riko_kenen_1 riko_kenen_1 を設定。
	 */
	public void setRiko_kenen_1(String riko_kenen_1) {
		this.riko_kenen_1 = riko_kenen_1;
	}
	/**
	 * @return riko_kenen_2 を戻します。
	 */
	public String getRiko_kenen_2() {
		return riko_kenen_2;
	}
	/**
	 * @param riko_kenen_2 riko_kenen_2 を設定。
	 */
	public void setRiko_kenen_2(String riko_kenen_2) {
		this.riko_kenen_2 = riko_kenen_2;
	}
	/**
	 * @return ryuhosaimu_1 を戻します。
	 */
	public String getRyuhosaimu_1() {
		return ryuhosaimu_1;
	}
	/**
	 * @param ryuhosaimu_1 ryuhosaimu_1 を設定。
	 */
	public void setRyuhosaimu_1(String ryuhosaimu_1) {
		this.ryuhosaimu_1 = ryuhosaimu_1;
	}
	/**
	 * @return ryuhosaimu_2 を戻します。
	 */
	public String getRyuhosaimu_2() {
		return ryuhosaimu_2;
	}
	/**
	 * @param ryuhosaimu_2 ryuhosaimu_2 を設定。
	 */
	public void setRyuhosaimu_2(String ryuhosaimu_2) {
		this.ryuhosaimu_2 = ryuhosaimu_2;
	}
	/**
	 * @return ryuhosaimu_kei_1 を戻します。
	 */
	public String getRyuhosaimu_kei_1() {
		return ryuhosaimu_kei_1;
	}
	/**
	 * @param ryuhosaimu_kei_1 ryuhosaimu_kei_1 を設定。
	 */
	public void setRyuhosaimu_kei_1(String ryuhosaimu_kei_1) {
		this.ryuhosaimu_kei_1 = ryuhosaimu_kei_1;
	}
	/**
	 * @return ryuhosaimu_kei_2 を戻します。
	 */
	public String getRyuhosaimu_kei_2() {
		return ryuhosaimu_kei_2;
	}
	/**
	 * @param ryuhosaimu_kei_2 ryuhosaimu_kei_2 を設定。
	 */
	public void setRyuhosaimu_kei_2(String ryuhosaimu_kei_2) {
		this.ryuhosaimu_kei_2 = ryuhosaimu_kei_2;
	}
	/**
	 * @return saiken_zankei_1 を戻します。
	 */
	public String getSaiken_zankei_1() {
		return saiken_zankei_1;
	}
	/**
	 * @param saiken_zankei_1 saiken_zankei_1 を設定。
	 */
	public void setSaiken_zankei_1(String saiken_zankei_1) {
		this.saiken_zankei_1 = saiken_zankei_1;
	}
	/**
	 * @return saiken_zankei_2 を戻します。
	 */
	public String getSaiken_zankei_2() {
		return saiken_zankei_2;
	}
	/**
	 * @param saiken_zankei_2 saiken_zankei_2 を設定。
	 */
	public void setSaiken_zankei_2(String saiken_zankei_2) {
		this.saiken_zankei_2 = saiken_zankei_2;
	}
	/**
	 * @return sashiire_hosyokin_1 を戻します。
	 */
	public String getSashiire_hosyokin_1() {
		return sashiire_hosyokin_1;
	}
	/**
	 * @param sashiire_hosyokin_1 sashiire_hosyokin_1 を設定。
	 */
	public void setSashiire_hosyokin_1(String sashiire_hosyokin_1) {
		this.sashiire_hosyokin_1 = sashiire_hosyokin_1;
	}
	/**
	 * @return sashiire_hosyokin_2 を戻します。
	 */
	public String getSashiire_hosyokin_2() {
		return sashiire_hosyokin_2;
	}
	/**
	 * @param sashiire_hosyokin_2 sashiire_hosyokin_2 を設定。
	 */
	public void setSashiire_hosyokin_2(String sashiire_hosyokin_2) {
		this.sashiire_hosyokin_2 = sashiire_hosyokin_2;
	}
	/**
	 * @return sonota_toshi_1 を戻します。
	 */
	public String getSonota_toshi_1() {
		return sonota_toshi_1;
	}
	/**
	 * @param sonota_toshi_1 sonota_toshi_1 を設定。
	 */
	public void setSonota_toshi_1(String sonota_toshi_1) {
		this.sonota_toshi_1 = sonota_toshi_1;
	}
	/**
	 * @return sonota_toshi_2 を戻します。
	 */
	public String getSonota_toshi_2() {
		return sonota_toshi_2;
	}
	/**
	 * @param sonota_toshi_2 sonota_toshi_2 を設定。
	 */
	public void setSonota_toshi_2(String sonota_toshi_2) {
		this.sonota_toshi_2 = sonota_toshi_2;
	}
	/**
	 * @return sonotakaisyu_1 を戻します。
	 */
	public String getSonotakaisyu_1() {
		return sonotakaisyu_1;
	}
	/**
	 * @param sonotakaisyu_1 sonotakaisyu_1 を設定。
	 */
	public void setSonotakaisyu_1(String sonotakaisyu_1) {
		this.sonotakaisyu_1 = sonotakaisyu_1;
	}
	/**
	 * @return sonotakaisyu_2 を戻します。
	 */
	public String getSonotakaisyu_2() {
		return sonotakaisyu_2;
	}
	/**
	 * @param sonotakaisyu_2 sonotakaisyu_2 を設定。
	 */
	public void setSonotakaisyu_2(String sonotakaisyu_2) {
		this.sonotakaisyu_2 = sonotakaisyu_2;
	}
	/**
	 * @return tanki_kashitsukekin_1 を戻します。
	 */
	public String getTanki_kashitsukekin_1() {
		return tanki_kashitsukekin_1;
	}
	/**
	 * @param tanki_kashitsukekin_1 tanki_kashitsukekin_1 を設定。
	 */
	public void setTanki_kashitsukekin_1(String tanki_kashitsukekin_1) {
		this.tanki_kashitsukekin_1 = tanki_kashitsukekin_1;
	}
	/**
	 * @return tanki_kashitsukekin_2 を戻します。
	 */
	public String getTanki_kashitsukekin_2() {
		return tanki_kashitsukekin_2;
	}
	/**
	 * @param tanki_kashitsukekin_2 tanki_kashitsukekin_2 を設定。
	 */
	public void setTanki_kashitsukekin_2(String tanki_kashitsukekin_2) {
		this.tanki_kashitsukekin_2 = tanki_kashitsukekin_2;
	}
	/**
	 * @return tatekaekin_1 を戻します。
	 */
	public String getTatekaekin_1() {
		return tatekaekin_1;
	}
	/**
	 * @param tatekaekin_1 tatekaekin_1 を設定。
	 */
	public void setTatekaekin_1(String tatekaekin_1) {
		this.tatekaekin_1 = tatekaekin_1;
	}
	/**
	 * @return tatekaekin_2 を戻します。
	 */
	public String getTatekaekin_2() {
		return tatekaekin_2;
	}
	/**
	 * @param tatekaekin_2 tatekaekin_2 を設定。
	 */
	public void setTatekaekin_2(String tatekaekin_2) {
		this.tatekaekin_2 = tatekaekin_2;
	}
	/**
	 * @return torihikimaetokin_1 を戻します。
	 */
	public String getTorihikimaetokin_1() {
		return torihikimaetokin_1;
	}
	/**
	 * @param torihikimaetokin_1 torihikimaetokin_1 を設定。
	 */
	public void setTorihikimaetokin_1(String torihikimaetokin_1) {
		this.torihikimaetokin_1 = torihikimaetokin_1;
	}
	/**
	 * @return torihikimaetokin_2 を戻します。
	 */
	public String getTorihikimaetokin_2() {
		return torihikimaetokin_2;
	}
	/**
	 * @param torihikimaetokin_2 torihikimaetokin_2 を設定。
	 */
	public void setTorihikimaetokin_2(String torihikimaetokin_2) {
		this.torihikimaetokin_2 = torihikimaetokin_2;
	}
	/**
	 * @return tuika_hikiate_1 を戻します。
	 */
	public String getTuika_hikiate_1() {
		return tuika_hikiate_1;
	}
	/**
	 * @param tuika_hikiate_1 tuika_hikiate_1 を設定。
	 */
	public void setTuika_hikiate_1(String tuika_hikiate_1) {
		this.tuika_hikiate_1 = tuika_hikiate_1;
	}
	/**
	 * @return tuika_hikiate_2 を戻します。
	 */
	public String getTuika_hikiate_2() {
		return tuika_hikiate_2;
	}
	/**
	 * @param tuika_hikiate_2 tuika_hikiate_2 を設定。
	 */
	public void setTuika_hikiate_2(String tuika_hikiate_2) {
		this.tuika_hikiate_2 = tuika_hikiate_2;
	}
	/**
	 * @return tyoki_kashitsukekin_1 を戻します。
	 */
	public String getTyoki_kashitsukekin_1() {
		return tyoki_kashitsukekin_1;
	}
	/**
	 * @param tyoki_kashitsukekin_1 tyoki_kashitsukekin_1 を設定。
	 */
	public void setTyoki_kashitsukekin_1(String tyoki_kashitsukekin_1) {
		this.tyoki_kashitsukekin_1 = tyoki_kashitsukekin_1;
	}
	/**
	 * @return tyoki_kashitsukekin_2 を戻します。
	 */
	public String getTyoki_kashitsukekin_2() {
		return tyoki_kashitsukekin_2;
	}
	/**
	 * @param tyoki_kashitsukekin_2 tyoki_kashitsukekin_2 を設定。
	 */
	public void setTyoki_kashitsukekin_2(String tyoki_kashitsukekin_2) {
		this.tyoki_kashitsukekin_2 = tyoki_kashitsukekin_2;
	}
	/**
	 * @return uketoritegata_1 を戻します。
	 */
	public String getUketoritegata_1() {
		return uketoritegata_1;
	}
	/**
	 * @param uketoritegata_1 uketoritegata_1 を設定。
	 */
	public void setUketoritegata_1(String uketoritegata_1) {
		this.uketoritegata_1 = uketoritegata_1;
	}
	/**
	 * @return uketoritegata_2 を戻します。
	 */
	public String getUketoritegata_2() {
		return uketoritegata_2;
	}
	/**
	 * @param uketoritegata_2 uketoritegata_2 を設定。
	 */
	public void setUketoritegata_2(String uketoritegata_2) {
		this.uketoritegata_2 = uketoritegata_2;
	}
	/**
	 * @return urikakekin_1 を戻します。
	 */
	public String getUrikakekin_1() {
		return urikakekin_1;
	}
	/**
	 * @param urikakekin_1 urikakekin_1 を設定。
	 */
	public void setUrikakekin_1(String urikakekin_1) {
		this.urikakekin_1 = urikakekin_1;
	}
	/**
	 * @return urikakekin_2 を戻します。
	 */
	public String getUrikakekin_2() {
		return urikakekin_2;
	}
	/**
	 * @param urikakekin_2 urikakekin_2 を設定。
	 */
	public void setUrikakekin_2(String urikakekin_2) {
		this.urikakekin_2 = urikakekin_2;
	}
	/**
	 * @return yusyutu_uketoritegata_1 を戻します。
	 */
	public String getYusyutu_uketoritegata_1() {
		return yusyutu_uketoritegata_1;
	}
	/**
	 * @param yusyutu_uketoritegata_1 yusyutu_uketoritegata_1 を設定。
	 */
	public void setYusyutu_uketoritegata_1(String yusyutu_uketoritegata_1) {
		this.yusyutu_uketoritegata_1 = yusyutu_uketoritegata_1;
	}
	/**
	 * @return yusyutu_uketoritegata_2 を戻します。
	 */
	public String getYusyutu_uketoritegata_2() {
		return yusyutu_uketoritegata_2;
	}
	/**
	 * @param yusyutu_uketoritegata_2 yusyutu_uketoritegata_2 を設定。
	 */
	public void setYusyutu_uketoritegata_2(String yusyutu_uketoritegata_2) {
		this.yusyutu_uketoritegata_2 = yusyutu_uketoritegata_2;
	}
}
