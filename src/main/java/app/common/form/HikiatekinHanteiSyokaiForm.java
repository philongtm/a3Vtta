/******************************************************************************
’˜ìŒ î•ñ				:
g—pJDK ƒo[ƒWƒ‡ƒ“		:1.5.0.18
XV—š—ğ
No		“ú•t			C³Ò			C³“à—e
001		2009/06/30		SSC				V‹Kì¬ 
******************************************************************************/
package app.common.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.LinkedHashMap;
/**
 * OZ6104_ˆø“–‹à”»’èÆ‰ïƒ^ƒu ƒAƒNƒVƒ‡ƒ“ƒtƒH[ƒ€ƒNƒ‰ƒX
 * 
 */
public class HikiatekinHanteiSyokaiForm extends AppPagerActionForm {
	
	private String tani;						//’PˆÊ
	private String uketoritegata;				//óæèŒ`
	private String yusyutu_uketoritegata;		//—AoóæèŒ`
	private String urikakekin;					//”„Š|‹à
	private String torihikimaetokin;			//æˆø‘O“n‹à
	private String tatekaekin;					//—§‘Ö‹à
	private String misyunyukin;				//–¢û“ü‹à
	private String misyusyueki;				//–¢ûû‰v
	private String tanki_kashitsukekin;		//’ZŠú‘İ•t‹à
	private String sashiire_hosyokin;			//·“ü•ÛØ‹à
	private String karibaraikin;				//‰¼•¥‹à
	private String tyoki_kashitsukekin;		//’·Šú‘İ•t‹à
	private String sonota_toshi;				//‚»‚Ì‘¼“Š‘
	private String ipan_saimukei;				//ˆê”ÊÂŒ Œv
	private String hanyo1_title;				//”Ä—p‚Pƒ^ƒCƒgƒ‹
	private String hanyo1;						//”Ä—p‚P
	private String saiken_zankei;				//ÂŒ c‚‡Œv
	private String ryuhosaimu;					//—¯•ÛÂ–±
	private String oth_ryuhosaimu;				//‘æOÒ—¯•ÛÂ–±
	private String ryuhosaimu_kei;				//—¯•ÛÂ–±Œv
	private String hozen;						//•Û‘S‡A
	private String sonotakaisyu;				//‚»‚Ì‘¼‰ñû
	private String hosyosaimu_gokei;			//•ÛØÂ–±‡Œv
	private String riko_kenen;					//—šs¿‹Œœ”O
	private String kibikiatekin;				//Šùˆø“–‹à
	private String hikiate_taisyokingaku;		//ˆø“–‘ÎÛ‹àŠz
	private String tuika_hikiate;				//’Ç‰Áˆø“–‹à
	private String tuuka_tyousei;				//’Ê‰İ’²®
	private String tuika_kingaku_go;			//’Ç‰Áˆø“–‹àŠz(’²®Œã)
	private String tori_cd_1;					//Š¨’èæCD‚P
	private String tor_nm_1;					//æˆøæ–¼‚P
	private String kanjo_nm_1;					//Š¨’è‰È–Ú‚P
	private String kingaku_1;					//‹àŠz‚P
	private String tori_cd_2;					//Š¨’èæCD‚Q
	private String tor_nm_2;					//æˆøæ–¼‚Q
	private String kanjo_nm_2;					//Š¨’è‰È–Ú‚Q
	private String kingaku_2;					//‹àŠz‚Q
	private String tori_cd_3;					//Š¨’èæCD‚R
	private String tor_nm_3;					//æˆøæ–¼‚R
	private String kanjo_nm_3;					//Š¨’è‰È–Ú‚S
	private String kingaku_3;					//‹àŠz‚R
	private String hudosan_k;					//Œ_–ñŠz-•s“®Y’S•Û
	private String dosan_k;					//Œ_–ñŠz-“®Y’S•Û
	private String hoken_k;					//Œ_–ñŠz-–fˆÕ•ÛŒ¯
	private String sonota_k;					//Œ_–ñŠz-‚»‚Ì‘¼
	private String hudosan_h;					//•]‰¿Šz-•s“®Y’S•Û
	private String dosan_h;					//•]‰¿Šz-“®Y’S•Û
	private String hoken_h;					//•]‰¿Šz-–fˆÕ•ÛŒ¯
	private String sonota_h;					//•]‰¿Šz-‚»‚Ì‘¼
	private String sonota_naiyo;				//‚»‚Ì‘¼‚Ì“à—e
	private String sonota_kaisyu_naiyo;		//‚»‚Ì‘¼‰ñû‚Ì“à—e
	private String rikoseikyu_kenen_naiyo;		//u—šs¿‹ŠT”Ov‚Ì“à—eà–¾
	private String hikiatekin_konkyo_naiyo;	//uˆø“–‹àZ’èª‹’£‚Ì“à—eà–¾
	private String kaisyu_naiyo;				//¡Œã‚Ì‰ñûŒ©’Ê‚µ‚È‚Ç
	private String sihanki_flg ;				//‘æ1/3l”¼Šúƒtƒ‰ƒO
	private String flg_kbn;					//ƒtƒ‰ƒO‹æ•ª
	private LinkedHashMap ar_flg_kbn;			//ƒtƒ‰ƒO‹æ•ª–¼—p(ƒŠƒXƒg)
	private String flg_comment;				//ƒtƒ‰ƒOƒRƒƒ“ƒg (“o˜^ƒ|ƒCƒ“ƒg95)
	private String tasya_risuku;				//‘¼ĞƒŠƒXƒN
	
    // •Ï”‰Šú‰»
    public HikiatekinHanteiSyokaiForm() {
    	
    	super.gamenId = GS.OZ6104;
    	
    	this.tani = GS.EMPTY_CHARCTER;						
    	this.uketoritegata = GS.EMPTY_CHARCTER;				
    	this.yusyutu_uketoritegata = GS.EMPTY_CHARCTER;		
    	this.urikakekin = GS.EMPTY_CHARCTER;					
    	this.torihikimaetokin = GS.EMPTY_CHARCTER;			
    	this.tatekaekin = GS.EMPTY_CHARCTER;					
    	this.misyunyukin = GS.EMPTY_CHARCTER;					
    	this.misyusyueki = GS.EMPTY_CHARCTER;					
    	this.tanki_kashitsukekin = GS.EMPTY_CHARCTER;			
    	this.sashiire_hosyokin = GS.EMPTY_CHARCTER;			
    	this.karibaraikin = GS.EMPTY_CHARCTER;				
    	this.tyoki_kashitsukekin = GS.EMPTY_CHARCTER;			
    	this.sonota_toshi = GS.EMPTY_CHARCTER;				
    	this.ipan_saimukei = GS.EMPTY_CHARCTER;				
    	this.hanyo1_title = GS.EMPTY_CHARCTER;				
    	this.hanyo1 = GS.EMPTY_CHARCTER;						
    	this.saiken_zankei = GS.EMPTY_CHARCTER;				
    	this.ryuhosaimu = GS.EMPTY_CHARCTER;					
    	this.oth_ryuhosaimu = GS.EMPTY_CHARCTER;				
    	this.ryuhosaimu_kei = GS.EMPTY_CHARCTER;				
    	this.hozen = GS.EMPTY_CHARCTER;						
    	this.sonotakaisyu = GS.EMPTY_CHARCTER;				
    	this.hosyosaimu_gokei = GS.EMPTY_CHARCTER;			
    	this.riko_kenen = GS.EMPTY_CHARCTER;					
    	this.kibikiatekin = GS.EMPTY_CHARCTER;				
    	this.hikiate_taisyokingaku = GS.EMPTY_CHARCTER;		
    	this.tuika_hikiate = GS.EMPTY_CHARCTER;				
    	this.tuuka_tyousei = GS.EMPTY_CHARCTER;				
    	this.tuika_kingaku_go = GS.EMPTY_CHARCTER;			
    	this.tori_cd_1 = GS.EMPTY_CHARCTER;					
    	this.tor_nm_1 = GS.EMPTY_CHARCTER;					
    	this.kanjo_nm_1 = GS.EMPTY_CHARCTER;					
    	this.kingaku_1 = GS.EMPTY_CHARCTER;					
    	this.tori_cd_2 = GS.EMPTY_CHARCTER;					
    	this.tor_nm_2 = GS.EMPTY_CHARCTER;					
    	this.kanjo_nm_2 = GS.EMPTY_CHARCTER;					
    	this.kingaku_2 = GS.EMPTY_CHARCTER;					
    	this.tori_cd_3 = GS.EMPTY_CHARCTER;					
    	this.tor_nm_3 = GS.EMPTY_CHARCTER;					
    	this.kanjo_nm_3 = GS.EMPTY_CHARCTER;					
    	this.kingaku_3 = GS.EMPTY_CHARCTER;					
    	this.hudosan_k = GS.EMPTY_CHARCTER;					
    	this.dosan_k = GS.EMPTY_CHARCTER;						
    	this.hoken_k = GS.EMPTY_CHARCTER;						
    	this.sonota_k = GS.EMPTY_CHARCTER;					
    	this.hudosan_h = GS.EMPTY_CHARCTER;					
    	this.dosan_h = GS.EMPTY_CHARCTER;						
    	this.hoken_h = GS.EMPTY_CHARCTER;						
    	this.sonota_h = GS.EMPTY_CHARCTER;					
    	this.sonota_naiyo = GS.EMPTY_CHARCTER;				
    	this.sonota_kaisyu_naiyo = GS.EMPTY_CHARCTER;			
    	this.rikoseikyu_kenen_naiyo = GS.EMPTY_CHARCTER;		
    	this.hikiatekin_konkyo_naiyo = GS.EMPTY_CHARCTER;		
    	this.kaisyu_naiyo = GS.EMPTY_CHARCTER;				
    	this.sihanki_flg  = GS.EMPTY_CHARCTER;				
    	this.flg_kbn = GS.EMPTY_CHARCTER;
    	this.ar_flg_kbn = null;
    	this.flg_comment = GS.EMPTY_CHARCTER;					
    	this.tasya_risuku = GS.EMPTY_CHARCTER;	
    }
    
	/**
	 * @return ‰æ–ÊID‚ğ–ß‚µ‚Ü‚·B
	 */
	public String toString(){
		return super.gamenId;
	}

	// ’PˆÊ
	
	public String getTani() {
		return tani;
	}

	public void setTani(String tani) {
		this.tani = tani;
	}

	// óæèŒ`
	
	public String getUketoritegata() {
		return uketoritegata;
	}

	public void setUketoritegata(String uketoritegata) {
		this.uketoritegata = uketoritegata;
	}

	// —AoóæèŒ`
	
	public String getYusyutu_uketoritegata() {
		return yusyutu_uketoritegata;
	}

	public void setYusyutu_uketoritegata(String yusyutu_uketoritegata) {
		this.yusyutu_uketoritegata = yusyutu_uketoritegata;
	}

	// ”„Š|‹à
	
	public String getUrikakekin() {
		return urikakekin;
	}

	public void setUrikakekin(String urikakekin) {
		this.urikakekin = urikakekin;
	}

	// æˆø‘O“n‹à
	
	public String getTorihikimaetokin() {
		return torihikimaetokin;
	}

	public void setTorihikimaetokin(String torihikimaetokin) {
		this.torihikimaetokin = torihikimaetokin;
	}

	// —§‘Ö‹à
	
	public String getTatekaekin() {
		return tatekaekin;
	}

	public void setTatekaekin(String tatekaekin) {
		this.tatekaekin = tatekaekin;
	}

	// –¢û“ü‹à
	
	public String getMisyunyukin() {
		return misyunyukin;
	}

	public void setMisyunyukin(String misyunyukin) {
		this.misyunyukin = misyunyukin;
	}

	// –¢ûû‰v
	
	public String getMisyusyueki() {
		return misyusyueki;
	}

	public void setMisyusyueki(String misyusyueki) {
		this.misyusyueki = misyusyueki;
	}

	// ’ZŠú‘İ•t‹à
	
	public String getTanki_kashitsukekin() {
		return tanki_kashitsukekin;
	}

	public void setTanki_kashitsukekin(String tanki_kashitsukekin) {
		this.tanki_kashitsukekin = tanki_kashitsukekin;
	}

	// ·“ü•ÛØ‹à
	
	public String getSashiire_hosyokin() {
		return sashiire_hosyokin;
	}

	public void setSashiire_hosyokin(String sashiire_hosyokin) {
		this.sashiire_hosyokin = sashiire_hosyokin;
	}

	// ‰¼•¥‹à
	
	public String getKaribaraikin() {
		return karibaraikin;
	}

	public void setKaribaraikin(String karibaraikin) {
		this.karibaraikin = karibaraikin;
	}

	// ’·Šú‘İ•t‹à
	
	public String getTyoki_kashitsukekin() {
		return tyoki_kashitsukekin;
	}

	public void setTyoki_kashitsukekin(String tyoki_kashitsukekin) {
		this.tyoki_kashitsukekin = tyoki_kashitsukekin;
	}

	// ‚»‚Ì‘¼“Š‘
	
	public String getSonota_toshi() {
		return sonota_toshi;
	}

	public void setSonota_toshi(String sonota_toshi) {
		this.sonota_toshi = sonota_toshi;
	}

	// ˆê”ÊÂŒ Œv
	
	public String getIpan_saimukei() {
		return ipan_saimukei;
	}

	public void setIpan_saimukei(String ipan_saimukei) {
		this.ipan_saimukei = ipan_saimukei;
	}

	// ”Ä—p‚Pƒ^ƒCƒgƒ‹
	
	public String getHanyo1_title() {
		return hanyo1_title;
	}

	public void setHanyo1_title(String hanyo1_title) {
		this.hanyo1_title = hanyo1_title;
	}

	// ”Ä—p‚P
	
	public String getHanyo1() {
		return hanyo1;
	}

	public void setHanyo1(String hanyo1) {
		this.hanyo1 = hanyo1;
	}

	// ÂŒ c‚‡Œv
	
	public String getSaiken_zankei() {
		return saiken_zankei;
	}

	public void setSaiken_zankei(String saiken_zankei) {
		this.saiken_zankei = saiken_zankei;
	}

	// —¯•ÛÂ–±
	
	public String getRyuhosaimu() {
		return ryuhosaimu;
	}

	public void setRyuhosaimu(String ryuhosaimu) {
		this.ryuhosaimu = ryuhosaimu;
	}

	// ‘æOÒ—¯•ÛÂ–±
	
	public String getOth_ryuhosaimu() {
		return oth_ryuhosaimu;
	}

	public void setOth_ryuhosaimu(String oth_ryuhosaimu) {
		this.oth_ryuhosaimu = oth_ryuhosaimu;
	}

	// —¯•ÛÂ–±Œv
	
	public String getRyuhosaimu_kei() {
		return ryuhosaimu_kei;
	}

	public void setRyuhosaimu_kei(String ryuhosaimu_kei) {
		this.ryuhosaimu_kei = ryuhosaimu_kei;
	}

	// •Û‘S
	
	public String getHozen() {
		return hozen;
	}

	public void setHozen(String hozen) {
		this.hozen = hozen;
	}

	// ‚»‚Ì‘¼‰ñû
	
	public String getSonotakaisyu() {
		return sonotakaisyu;
	}

	public void setSonotakaisyu(String sonotakaisyu) {
		this.sonotakaisyu = sonotakaisyu;
	}

	// •ÛØÂ–±‡Œv
	
	public String getHosyosaimu_gokei() {
		return hosyosaimu_gokei;
	}

	public void setHosyosaimu_gokei(String hosyosaimu_gokei) {
		this.hosyosaimu_gokei = hosyosaimu_gokei;
	}

	// —šs¿‹Œœ”O
	
	public String getRiko_kenen() {
		return riko_kenen;
	}

	public void setRiko_kenen(String riko_kenen) {
		this.riko_kenen = riko_kenen;
	}

	// Šùˆø“–‹à
	
	public String getKibikiatekin() {
		return kibikiatekin;
	}

	public void setKibikiatekin(String kibikiatekin) {
		this.kibikiatekin = kibikiatekin;
	}

	// ˆø“–‘ÎÛ‹àŠz
	
	public String getHikiate_taisyokingaku() {
		return hikiate_taisyokingaku;
	}

	public void setHikiate_taisyokingaku(String hikiate_taisyokingaku) {
		this.hikiate_taisyokingaku = hikiate_taisyokingaku;
	}

	// ’Ç‰Áˆø“–‹àŠz
	
	public String getTuika_hikiate() {
		return tuika_hikiate;
	}

	public void setTuika_hikiate(String tuika_hikiate) {
		this.tuika_hikiate = tuika_hikiate;
	}

	// ’Ê‰İ’²®
	
	public String getTuuka_tyousei() {
		return tuuka_tyousei;
	}

	public void setTuuka_tyousei(String tuuka_tyousei) {
		this.tuuka_tyousei = tuuka_tyousei;
	}

	// ’Ç‰Áˆø“–‹àŠz(’²®Œã)
	
	public String getTuika_kingaku_go() {
		return tuika_kingaku_go;
	}

	public void setTuika_kingaku_go(String tuika_kingaku_go) {
		this.tuika_kingaku_go = tuika_kingaku_go;
	}

	// Š¨’èæCD‚P
	
	public String getTori_cd_1() {
		return tori_cd_1;
	}

	public void setTori_cd_1(String tori_cd_1) {
		this.tori_cd_1 = tori_cd_1;
	}

	// æˆøæ–¼‚P
	
	public String getTor_nm_1() {
		return tor_nm_1;
	}

	public void setTor_nm_1(String tor_nm_1) {
		this.tor_nm_1 = tor_nm_1;
	}

	// Š¨’è‰È–Ú‚P
	
	public String getKanjo_nm_1() {
		return kanjo_nm_1;
	}

	public void setKanjo_nm_1(String kanjo_nm_1) {
		this.kanjo_nm_1 = kanjo_nm_1;
	}

	// ‹àŠz‚P
	
	public String getKingaku_1() {
		return kingaku_1;
	}

	public void setKingaku_1(String kingaku_1) {
		this.kingaku_1 = kingaku_1;
	}

	// Š¨’èæCD‚Q
	
	public String getTori_cd_2() {
		return tori_cd_2;
	}

	public void setTori_cd_2(String tori_cd_2) {
		this.tori_cd_2 = tori_cd_2;
	}

	// æˆøæ–¼‚Q
	
	public String getTor_nm_2() {
		return tor_nm_2;
	}

	public void setTor_nm_2(String tor_nm_2) {
		this.tor_nm_2 = tor_nm_2;
	}

	// Š¨’è‰È–Ú‚Q
	
	public String getKanjo_nm_2() {
		return kanjo_nm_2;
	}

	public void setKanjo_nm_2(String kanjo_nm_2) {
		this.kanjo_nm_2 = kanjo_nm_2;
	}

	// ‹àŠz‚Q
	
	public String getKingaku_2() {
		return kingaku_2;
	}

	public void setKingaku_2(String kingaku_2) {
		this.kingaku_2 = kingaku_2;
	}

	// Š¨’èæCD‚R
	
	public String getTori_cd_3() {
		return tori_cd_3;
	}

	public void setTori_cd_3(String tori_cd_3) {
		this.tori_cd_3 = tori_cd_3;
	}

	// æˆøæ–¼‚R
	
	public String getTor_nm_3() {
		return tor_nm_3;
	}

	public void setTor_nm_3(String tor_nm_3) {
		this.tor_nm_3 = tor_nm_3;
	}

	// Š¨’è‰È–Ú‚R
	
	public String getKanjo_nm_3() {
		return kanjo_nm_3;
	}

	public void setKanjo_nm_3(String kanjo_nm_3) {
		this.kanjo_nm_3 = kanjo_nm_3;
	}

	// ‹àŠz‚R
	
	public String getKingaku_3() {
		return kingaku_3;
	}

	public void setKingaku_3(String kingaku_3) {
		this.kingaku_3 = kingaku_3;
	}

	// Œ_–ñŠz-•s“®Y’S•Û
	
	public String getHudosan_k() {
		return hudosan_k;
	}

	public void setHudosan_k(String hudosan_k) {
		this.hudosan_k = hudosan_k;
	}

	// Œ_–ñŠz-“®Y’S•Û
	
	public String getDosan_k() {
		return dosan_k;
	}

	public void setDosan_k(String dosan_k) {
		this.dosan_k = dosan_k;
	}

	// Œ_–ñŠz-–fˆÕ•ÛŒ¯
	
	public String getHoken_k() {
		return hoken_k;
	}

	public void setHoken_k(String hoken_k) {
		this.hoken_k = hoken_k;
	}

	// Œ_–ñŠz-‚»‚Ì‘¼
	
	public String getSonota_k() {
		return sonota_k;
	}

	public void setSonota_k(String sonota_k) {
		this.sonota_k = sonota_k;
	}

	// •]‰¿Šz-•s“®Y’S•Û
	
	public String getHudosan_h() {
		return hudosan_h;
	}

	public void setHudosan_h(String hudosan_h) {
		this.hudosan_h = hudosan_h;
	}

	// •]‰¿Šz-“®Y’S•Û
	
	public String getDosan_h() {
		return dosan_h;
	}

	public void setDosan_h(String dosan_h) {
		this.dosan_h = dosan_h;
	}

	// •]‰¿Šz-–fˆÕ•ÛŒ¯
	
	public String getHoken_h() {
		return hoken_h;
	}

	public void setHoken_h(String hoken_h) {
		this.hoken_h = hoken_h;
	}

	// •]‰¿Šz-‚»‚Ì‘¼
	
	public String getSonota_h() {
		return sonota_h;
	}

	public void setSonota_h(String sonota_h) {
		this.sonota_h = sonota_h;
	}

	// ‚»‚Ì‘¼‚Ì“à—e
	
	public String getSonota_naiyo() {
		return sonota_naiyo;
	}

	public void setSonota_naiyo(String sonota_naiyo) {
		this.sonota_naiyo = sonota_naiyo;
	}

	// ‚»‚Ì‘¼‰ñû‚Ì“à—e
	
	public String getSonota_kaisyu_naiyo() {
		return sonota_kaisyu_naiyo;
	}

	public void setSonota_kaisyu_naiyo(String sonota_kaisyu_naiyo) {
		this.sonota_kaisyu_naiyo = sonota_kaisyu_naiyo;
	}

	// u—šs¿‹ŠT”Ov‚Ì“à—eà–¾
	
	public String getRikoseikyu_kenen_naiyo() {
		return rikoseikyu_kenen_naiyo;
	}

	public void setRikoseikyu_kenen_naiyo(String rikoseikyu_kenen_naiyo) {
		this.rikoseikyu_kenen_naiyo = rikoseikyu_kenen_naiyo;
	}

	// uˆø“–‹àZ’èª‹’£‚Ì“à—eà–¾
	
	public String getHikiatekin_konkyo_naiyo() {
		return hikiatekin_konkyo_naiyo;
	}

	public void setHikiatekin_konkyo_naiyo(String hikiatekin_konkyo_naiyo) {
		this.hikiatekin_konkyo_naiyo = hikiatekin_konkyo_naiyo;
	}

	// ¡Œã‚Ì‰ñûŒ©’Ê‚µ‚È‚Ç
	
	public String getKaisyu_naiyo() {
		return kaisyu_naiyo;
	}

	public void setKaisyu_naiyo(String kaisyu_naiyo) {
		this.kaisyu_naiyo = kaisyu_naiyo;
	}

	// ‘æ1/3l”¼Šúƒtƒ‰ƒO
	
	public String getSihanki_flg() {
		return sihanki_flg;
	}

	public void setSihanki_flg(String sihanki_flg) {
		this.sihanki_flg = sihanki_flg;
	}

	// ƒtƒ‰ƒO‹æ•ª
	
	public String getFlg_kbn() {
		return flg_kbn;
	}

	public void setFlg_kbn(String flg_kbn) {
		this.flg_kbn = flg_kbn;
	}

	// ƒtƒ‰ƒOƒRƒƒ“ƒg (“o˜^ƒ|ƒCƒ“ƒg95)
	
	public String getFlg_comment() {
		return flg_comment;
	}

	public void setFlg_comment(String flg_comment) {
		this.flg_comment = flg_comment;
	}

	// ‘¼ĞƒŠƒXƒN
	
	public String getTasya_risuku() {
		return tasya_risuku;
	}

	public void setTasya_risuku(String tasya_risuku) {
		this.tasya_risuku = tasya_risuku;
	}

	//	ƒtƒ‰ƒO‹æ•ª–¼Ì(ƒŠƒXƒg)
	
	public LinkedHashMap getAr_flg_kbn() {
		return ar_flg_kbn;
	}

	public void setAr_flg_kbn(LinkedHashMap ar_flg_kbn) {
		this.ar_flg_kbn = ar_flg_kbn;
	}	
}
