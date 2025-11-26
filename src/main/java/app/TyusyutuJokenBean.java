/******************************************************************************
’˜ìŒ î•ñ				:
g—pJDK ƒo[ƒWƒ‡ƒ“		:1.5.0.18
XV—š—ğ
No		“ú•t			C³Ò			C³“à—e
001		2009/06/30		SSC				V‹Kì¬ 
******************************************************************************/
package app;

import common.global.GS;

/**
 * ’ŠoğŒƒƒ“ƒeƒiƒ“ƒXî•ñBeanƒNƒ‰ƒX
 * 
 */
public class TyusyutuJokenBean {

	private String id						= null;	// –¾×ID
	private String joken_no				= null;	// ğŒNo
	private String system_kbn				= null;	// ƒVƒXƒeƒ€‹æ•ª	
	private String system_kbn_nm			= null;	// ƒVƒXƒeƒ€‹æ•ª–¼Ì
    private String bunrui1					= null;	// •ª—Ş‚P
    private String bunrui2					= null;	// •ª—Ş‚Q
    private String kessanki_kbn			= null;	// ŒˆZŠú‹æ•ª
    private String kessanki_kbn_nm			= null;	// ŒˆZŠú‹æ•ª–¼Ì
    private String kijunbi					= null;	// Šî€“ú
    private String kijunbi_nm				= null;	// Šî€“ú–¼Ì
	private String tyusyutu_jiyu			= null;	// ’Šo–—R
	private String joken_nm_ja_en			= null;	// ğŒ–¼Ì(“ú–{Œê/‰pŒê)
	private String joken_nm				= null;	// ğŒ–¼Ì
	private String joken_nm_en				= null;	// ğŒ–¼Ì(‰pŒê)
	private String ktk						= null;	// Ši•t
	private String kingaku1_kingaku		= null;	// ‹àŠzğŒ‚P(‹àŠz)
	private String kingaku_tuuka			= null;	// ‹àŠzğŒ(’Ê‰İ)
	private String kingaku2_tairyu			= null;	// ‹àŠzğŒ‚Q(‘Ø—¯‹æ•ª)
	private String kingaku2_tairyu_nm		= null;	// ‹àŠzğŒ‚Q(‘Ø—¯‹æ•ª)–¼Ì
	private String kingaku2_kingaku		= null;	// ‹àŠzğ‚Q(‹àŠz)
	private String tairyu_kikan_tuki		= null;	// ‘Ø—¯ŠúŠÔ(Œ)
	private String tairyu_kikan_from		= null;	// ‘Ø—¯ŠúŠÔ(From)
	private String tairyu_kikan_to			= null;	// ‘Ø—¯ŠúŠÔ(To)
	private String tairyu_hantei_taisyo	= null;	// À¿‘Ø—¯”»’è‘ÎÛ
	private String tairyu_hantei_nm		= null;	// ‘Ø—¯”»’è(–¼Ì)
	private String satei_taisyo			= null;	// ¸’è‘ÎÛ
	private String satei_taisyo_nm			= null;	// ¸’è‘ÎÛ(–¼Ì)
	private String kako_ktk_flg			= null;	// ‰ß‹Ši•tƒtƒ‰ƒO
	private String kako_ktk_nm				= null;	// ‰ß‹Ši•t(–¼Ì)
	private String kako_ktk_from			= null;	// ‰ß‹Ši•t(From)
	private String kako_ktk_to				= null;	// ‰ß‹Ši•t(To)
	private String kako_ktk_sansyo_jiten	= null;	// ‰ß‹Ši•tQÆ“_
	
	/**
	 * ‰Šú‰»ˆ—‚ğs‚¤B
	 */
	public void initialize() {
		
		this.id							= GS.EMPTY_CHARCTER;
		this.joken_no					= GS.EMPTY_CHARCTER;			
		this.system_kbn					= GS.EMPTY_CHARCTER;
		this.system_kbn_nm				= GS.EMPTY_CHARCTER;
		this.bunrui1					= GS.EMPTY_CHARCTER;
		this.bunrui2					= GS.EMPTY_CHARCTER;
		this.kessanki_kbn				= GS.EMPTY_CHARCTER;
		this.kessanki_kbn_nm			= GS.EMPTY_CHARCTER;
		this.kijunbi					= GS.EMPTY_CHARCTER;
		this.kijunbi_nm					= GS.EMPTY_CHARCTER;
		this.tyusyutu_jiyu				= GS.EMPTY_CHARCTER;
		this.joken_nm_ja_en				= GS.EMPTY_CHARCTER;
		this.joken_nm					= GS.EMPTY_CHARCTER;
		this.joken_nm_en				= GS.EMPTY_CHARCTER;
		this.ktk						= GS.EMPTY_CHARCTER;
		this.kingaku1_kingaku			= GS.EMPTY_CHARCTER;
		this.kingaku_tuuka				= GS.EMPTY_CHARCTER;
		this.kingaku2_tairyu			= GS.EMPTY_CHARCTER;
		this.kingaku2_tairyu_nm			= GS.EMPTY_CHARCTER;
		this.kingaku2_kingaku			= GS.EMPTY_CHARCTER;
		this.tairyu_kikan_tuki			= GS.EMPTY_CHARCTER;
		this.tairyu_kikan_from			= GS.EMPTY_CHARCTER;
		this.tairyu_kikan_to			= GS.EMPTY_CHARCTER;
		this.tairyu_hantei_taisyo		= GS.EMPTY_CHARCTER;
		this.tairyu_hantei_nm			= GS.EMPTY_CHARCTER;
		this.satei_taisyo				= GS.EMPTY_CHARCTER;
		this.satei_taisyo_nm			= GS.EMPTY_CHARCTER;
		this.kako_ktk_flg				= GS.EMPTY_CHARCTER;
		this.kako_ktk_nm				= GS.EMPTY_CHARCTER;
		this.kako_ktk_from				= GS.EMPTY_CHARCTER;
		this.kako_ktk_to				= GS.EMPTY_CHARCTER;
		this.kako_ktk_sansyo_jiten		= GS.EMPTY_CHARCTER;
	}
	
	/**
	 * ƒIƒuƒWƒFƒNƒg‚Ì”jŠü‚ğs‚¤B
	 */
	public void destroy() {
		initialize();
	}

	// ƒAƒNƒZƒXƒƒ\ƒbƒh
	
	//–¾×ID
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	//ğŒNo
	public String getJoken_no() {
		return joken_no;
	}
	public void setJoken_no(String joken_no) {
		this.joken_no = joken_no;
	}
	//ƒVƒXƒeƒ€‹æ•ª
	public String getSystem_kbn() {
		return system_kbn;
	}
	public void setSystem_kbn(String system_kbn) {
		this.system_kbn = system_kbn;
	}
	//ƒVƒXƒeƒ€‹æ•ª–¼Ì
	public String getSystem_kbn_nm() {
		return system_kbn_nm;
	}
	public void setSystem_kbn_nm(String system_kbn_nm) {
		this.system_kbn_nm = system_kbn_nm;
	}
	//•ª—Ş‚P
	public String getBunrui1() {
		return bunrui1;
	}
	public void setBunrui1(String bunrui1) {
		this.bunrui1 = bunrui1;
	}
	//•ª—Ş‚Q	
	public String getBunrui2() {
		return bunrui2;
	}
	public void setBunrui2(String bunrui2) {
		this.bunrui2 = bunrui2;
	}
	//ŒˆZŠú‹æ•ª
	public String getKessanki_kbn() {
		return kessanki_kbn;
	}
	public void setKessanki_kbn(String kessanki_kbn) {
		this.kessanki_kbn = kessanki_kbn;
	}
	//ŒˆZŠú‹æ•ª–¼Ì
	public String getKessanki_kbn_nm() {
		return kessanki_kbn_nm;
	}
	public void setKessanki_kbn_nm(String kessanki_kbn_nm) {
		this.kessanki_kbn_nm = kessanki_kbn_nm;
	}
	//Šî€“ú
	public String getKijunbi() {
		return kijunbi;
	}
	public void setKijunbi(String kijunbi) {
		this.kijunbi = kijunbi;
	}
	//Šî€“ú–¼Ì
	public String getKijunbi_nm() {
		return kijunbi_nm;
	}
	public void setKijunbi_nm(String kijunbi_nm) {
		this.kijunbi_nm = kijunbi_nm;
	}
	//’Šo–—R
	public String getTyusyutu_jiyu() {
		return tyusyutu_jiyu;
	}
	public void setTyusyutu_jiyu(String tyusyutu_jiyu) {
		this.tyusyutu_jiyu = tyusyutu_jiyu;
	}
	//ğŒ–¼Ì(“ú–{Œê/‰pŒê)
	public String getJoken_nm_ja_en() {
		return joken_nm_ja_en;
	}
	public void setJoken_nm_ja_en(String joken_nm_ja_en) {
		this.joken_nm_ja_en = joken_nm_ja_en;
	}
	//ğŒ–¼Ì
	public String getJoken_nm() {
		return joken_nm;
	}
	public void setJoken_nm(String joken_nm) {
		this.joken_nm = joken_nm;
	}
	//ğŒ–¼Ìi‰pŒêj
	public String getJoken_nm_en() {
		return joken_nm_en;
	}
	public void setJoken_nm_en(String joken_nm_en) {
		this.joken_nm_en = joken_nm_en;
	}
	//Ši•t
	public String getKtk() {
		return ktk;
	}
	public void setKtk(String ktk) {
		this.ktk = ktk;
	}
	//‹àŠzğŒ‚P(‹àŠz)
	public String getKingaku1_kingaku() {
		return kingaku1_kingaku;
	}
	public void setKingaku1_kingaku(String kingaku1_kingaku) {
		this.kingaku1_kingaku = kingaku1_kingaku;
	}
	//‹àŠzğŒ(’Ê‰İ)
	public String getKingaku_tuuka() {
		return kingaku_tuuka;
	}
	public void setKingaku_tuuka(String kingaku_tuuka) {
		this.kingaku_tuuka = kingaku_tuuka;
	}
	//‹àŠzğŒ‚Q(‘Ø—¯‹æ•ª)
	public String getKingaku2_tairyu() {
		return kingaku2_tairyu;
	}
	public void setKingaku2_tairyu(String kingaku2_tairyu) {
		this.kingaku2_tairyu = kingaku2_tairyu;
	}
	//‹àŠzğŒ‚Qi‘Ø—¯‹æ•ªj–¼Ì
	public String getKingaku2_tairyu_nm() {
		return kingaku2_tairyu_nm;
	}
	public void setKingaku2_tairyu_nm(String kingaku2_tairyu_nm) {
		this.kingaku2_tairyu_nm = kingaku2_tairyu_nm;
	}
	//‹àŠzğŒ‚Q(‹àŠz)
	public String getKingaku2_kingaku() {
		return kingaku2_kingaku;
	}
	public void setKingaku2_kingaku(String kingaku2_kingaku) {
		this.kingaku2_kingaku = kingaku2_kingaku;
	}
	//‘Ø—¯ŠúŠÔ(Œ)
	public String getTairyu_kikan_tuki() {
		return tairyu_kikan_tuki;
	}
	public void setTairyu_kikan_tuki(String tairyu_kikan_tuki) {
		this.tairyu_kikan_tuki = tairyu_kikan_tuki;
	}
	//‘Ø—¯ŠúŠÔ(Form)
	public String getTairyu_kikan_from() {
		return tairyu_kikan_from;
	}
	public void setTairyu_kikan_from(String tairyu_kikan_from) {
		this.tairyu_kikan_from = tairyu_kikan_from;
	}
	//‘Ø—¯ŠúŠÔ(To)
	public String getTairyu_kikan_to() {
		return tairyu_kikan_to;
	}
	public void setTairyu_kikan_to(String tairyu_kikan_to) {
		this.tairyu_kikan_to = tairyu_kikan_to;
	}
	//À¿‘Ø—¯”»’è‘ÎÛ
	public String getTairyu_hantei_taisyo() {
		return tairyu_hantei_taisyo;
	}
	public void setTairyu_hantei_taisyo(String tairyu_hantei_taisyo) {
		this.tairyu_hantei_taisyo = tairyu_hantei_taisyo;
	}
	//‘Ø—¯”»’è(–¼Ì)
	public String getTairyu_hantei_nm() {
		return tairyu_hantei_nm;
	}
	public void setTairyu_hantei_nm(String tairyu_hantei_nm) {
		this.tairyu_hantei_nm = tairyu_hantei_nm;
	}
	//¸’è‘ÎÛ
	public String getSatei_taisyo() {
		return satei_taisyo;
	}
	public void setSatei_taisyo(String satei_taisyo) {
		this.satei_taisyo = satei_taisyo;
	}
	//¸’è‘ÎÛ(–¼Ì)
	public String getSatei_taisyo_nm() {
		return satei_taisyo_nm;
	}
	public void setSatei_taisyo_nm(String satei_taisyo_nm) {
		this.satei_taisyo_nm = satei_taisyo_nm;
	}
	//‰ß‹Ši•tƒtƒ‰ƒO
	public String getKako_ktk_flg() {
		return kako_ktk_flg;
	}
	public void setKako_ktk_flg(String kako_ktk_flg) {
		this.kako_ktk_flg = kako_ktk_flg;
	}
	//‰ß‹Ši•t(–¼Ì)
	public String getKako_ktk_nm() {
		return kako_ktk_nm;
	}
	public void setKako_ktk_nm(String kako_ktk_nm) {
		this.kako_ktk_nm = kako_ktk_nm;
	}
	//‰ß‹Ši•tiFromj
	public String getKako_ktk_from() {
		return kako_ktk_from;
	}
	public void setKako_ktk_from(String kako_ktk_from) {
		this.kako_ktk_from = kako_ktk_from;
	}
	//‰ß‹Ši•tiToj
	public String getKako_ktk_to() {
		return kako_ktk_to;
	}
	public void setKako_ktk_to(String kako_ktk_to) {
		this.kako_ktk_to = kako_ktk_to;
	}
	//‰ß‹Ši•tQÆ“_
	public String getKako_ktk_sansyo_jiten() {
		return kako_ktk_sansyo_jiten;
	}
	public void setKako_ktk_sansyo_jiten(String kako_ktk_sansyo_jiten) {
		this.kako_ktk_sansyo_jiten = kako_ktk_sansyo_jiten;
	}
}