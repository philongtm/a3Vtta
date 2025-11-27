/******************************************************************************
’˜ìŒ î•ñ				:
g—pJDK ƒo[ƒWƒ‡ƒ“		:1.5.0.18
XV—š—ğ
No		“ú•t			C³Ò			C³“à—e
001		2009/06/30		SSC				V‹Kì¬ 
******************************************************************************/
package app.system.form;

import common.global.GS;
import common.struts.AppPagerActionForm;
import config.adapter.struts.action.ActionMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

/**
 * OS7111_’ŠoğŒƒƒ“ƒeƒiƒ“ƒX_“o˜^ ƒAƒNƒVƒ‡ƒ“ƒtƒH[ƒ€ƒNƒ‰ƒX <br>
 */
public class CyusyutujyokenTorokuForm extends AppPagerActionForm {

    private static final long serialVersionUID = 1L; // serialVersionUID
	
    private String seniMode; 						// ‰æ–Ê‘JˆÚƒ‚[ƒh
    private String cyusyutuJiyu; 					// ’Šo–—R
    private String jyokenNmJp; 					// ğŒ–¼Ì(“ú–{Œê)
    private String jyokenNmEn; 					// ğŒ–¼Ì(‰pŒê)
    private String systemKbn; 						// ƒVƒXƒeƒ€‹æ•ª
    private LinkedHashMap ar_systemKbn; 			// ƒVƒXƒeƒ€‹æ•ªyƒŠƒXƒgz
    private String hanyo1; 						// ”Ä—p‚P
    private LinkedHashMap ar_hanyo1; 				// ”Ä—p‚PyƒŠƒXƒgz
    private String hanyo2; 						// ”Ä—p‚Q
    private LinkedHashMap ar_hanyo2; 				// ”Ä—p‚QyƒŠƒXƒgz
    private String kesanKbn; 						// ŒˆZŠú‹æ•ª
    private LinkedHashMap ar_kesanKbn; 			// ŒˆZŠú‹æ•ªyƒŠƒXƒgz
    private String kijunbi; 						// Šî€“ú
    private LinkedHashMap ar_kijunbi; 				// Šî€“úyƒŠƒXƒgz
    private String kakuduke; 						// Ši•t
    private LinkedHashMap ar_kakuduke; 			// Ši•tyƒŠƒXƒgz
    private String kingakuJyoken1; 				// ‹àŠzğŒ‚P(‹àŠz)
    private String kingakuJyoken2Kbn; 				// ‹àŠzğŒ‚Q(‘Ø—¯‹æ•ª)
    private LinkedHashMap ar_kingakuJyoken2Kbn; 	// ‹àŠzğŒ‚Q(‘Ø—¯‹æ•ª)yƒŠƒXƒgz
    private String kingakuJyoken2; 				// ‹àŠzğŒ‚Q(‹àŠz)
    private String tairyuKikanFrom; 				// ‘Ø—¯ŠúŠÔ(From)
    private String tairyuKikanTo; 					// ‘Ø—¯ŠúŠÔ(To)
    private String tairyuTaisyo; 					// À¿‘Ø—¯”»’è‘ÎÛ(*1)
    private String sateiTaisyo; 					// ¸’è‘ÎÛ(*1)
    private String kakoKakudukeFlg; 				// ‰ß‹Ši•tƒtƒ‰ƒO(*1)
    private String kakoKakudukeFrom; 				// ‰ß‹Ši•t(From)
    private LinkedHashMap ar_kakoKakudukeFrom; 	// ‰ß‹Ši•t(From)yƒŠƒXƒgz
    private String kakoKakudukeTo; 				// ‰ß‹Ši•t(To)
    private LinkedHashMap ar_kakoKakudukeTo; 		// ‰ß‹Ši•t(To)yƒŠƒXƒgz
    private String kakoKakudukeJiten; 				// ‰ß‹Ši•tQÆ“_
    private String tukaCd; 						// (‹àŠzğŒ)’Ê‰İ
    private String jokenNo; 						// ğŒNo

    /**
     * •Ï”‰Šú‰» <br>
     */
    public CyusyutujyokenTorokuForm() {
        
        super.gamenId = GS.OS7111;
        
        this.seniMode = GS.EMPTY_CHARCTER;
        this.cyusyutuJiyu = GS.EMPTY_CHARCTER;
        this.jyokenNmJp = GS.EMPTY_CHARCTER;
        this.jyokenNmEn = GS.EMPTY_CHARCTER;
        this.systemKbn = GS.EMPTY_CHARCTER;
        this.ar_systemKbn = null;
        this.hanyo1 = GS.EMPTY_CHARCTER;
        this.ar_hanyo1 = null;
        this.hanyo2 = GS.EMPTY_CHARCTER;
        this.ar_hanyo2 = null;
        this.kesanKbn = GS.EMPTY_CHARCTER;
        this.ar_kesanKbn = null;
        this.kijunbi = GS.EMPTY_CHARCTER;
        this.ar_kijunbi = null;
        this.kakuduke = GS.EMPTY_CHARCTER;
        this.ar_kakuduke = null;
        this.kingakuJyoken1 = GS.EMPTY_CHARCTER;
        this.kingakuJyoken2Kbn = GS.EMPTY_CHARCTER;
        this.ar_kingakuJyoken2Kbn = null;
        this.kingakuJyoken2 = GS.EMPTY_CHARCTER;
        this.tairyuKikanFrom = GS.EMPTY_CHARCTER;
        this.tairyuKikanTo = GS.EMPTY_CHARCTER;
        this.tairyuTaisyo = GS.EMPTY_CHARCTER;
        this.sateiTaisyo = GS.EMPTY_CHARCTER;
        this.kakoKakudukeFlg = GS.EMPTY_CHARCTER;
        this.kakoKakudukeFrom = GS.EMPTY_CHARCTER;
        this.ar_kakoKakudukeFrom = null;
        this.kakoKakudukeTo = GS.EMPTY_CHARCTER;
        this.ar_kakoKakudukeTo = null;
        this.kakoKakudukeJiten = GS.EMPTY_CHARCTER;
        this.tukaCd = GS.EMPTY_CHARCTER;
        this.jokenNo = GS.EMPTY_CHARCTER;
    }
    
    /**
     * @return ‰æ–ÊID‚ğ–ß‚µ‚Ü‚·B
     */
    public String toString(){
        return super.gamenId;
    }

	/**
	 * @return the ar_hanyo1
	 */
	public LinkedHashMap getAr_hanyo1() {
		return ar_hanyo1;
	}

	/**
	 * @param ar_hanyo1 the ar_hanyo1 to set
	 */
	public void setAr_hanyo1(LinkedHashMap ar_hanyo1) {
		this.ar_hanyo1 = ar_hanyo1;
	}

	/**
	 * @return the ar_hanyo2
	 */
	public LinkedHashMap getAr_hanyo2() {
		return ar_hanyo2;
	}

	/**
	 * @param ar_hanyo2 the ar_hanyo2 to set
	 */
	public void setAr_hanyo2(LinkedHashMap ar_hanyo2) {
		this.ar_hanyo2 = ar_hanyo2;
	}

	/**
	 * @return the ar_kakoKakudukeFrom
	 */
	public LinkedHashMap getAr_kakoKakudukeFrom() {
		return ar_kakoKakudukeFrom;
	}

	/**
	 * @param ar_kakoKakudukeFrom the ar_kakoKakudukeFrom to set
	 */
	public void setAr_kakoKakudukeFrom(LinkedHashMap ar_kakoKakudukeFrom) {
		this.ar_kakoKakudukeFrom = ar_kakoKakudukeFrom;
	}

	/**
	 * @return the ar_kakoKakudukeTo
	 */
	public LinkedHashMap getAr_kakoKakudukeTo() {
		return ar_kakoKakudukeTo;
	}

	/**
	 * @param ar_kakoKakudukeTo the ar_kakoKakudukeTo to set
	 */
	public void setAr_kakoKakudukeTo(LinkedHashMap ar_kakoKakudukeTo) {
		this.ar_kakoKakudukeTo = ar_kakoKakudukeTo;
	}

	/**
	 * @return the ar_kakuduke
	 */
	public LinkedHashMap getAr_kakuduke() {
		return ar_kakuduke;
	}

	/**
	 * @param ar_kakuduke the ar_kakuduke to set
	 */
	public void setAr_kakuduke(LinkedHashMap ar_kakuduke) {
		this.ar_kakuduke = ar_kakuduke;
	}

	/**
	 * @return the ar_kesanKbn
	 */
	public LinkedHashMap getAr_kesanKbn() {
		return ar_kesanKbn;
	}

	/**
	 * @param ar_kesanKbn the ar_kesanKbn to set
	 */
	public void setAr_kesanKbn(LinkedHashMap ar_kesanKbn) {
		this.ar_kesanKbn = ar_kesanKbn;
	}

	/**
	 * @return the ar_kijunbi
	 */
	public LinkedHashMap getAr_kijunbi() {
		return ar_kijunbi;
	}

	/**
	 * @param ar_kijunbi the ar_kijunbi to set
	 */
	public void setAr_kijunbi(LinkedHashMap ar_kijunbi) {
		this.ar_kijunbi = ar_kijunbi;
	}

	/**
	 * @return the ar_kingakuJyoken2Kbn
	 */
	public LinkedHashMap getAr_kingakuJyoken2Kbn() {
		return ar_kingakuJyoken2Kbn;
	}

	/**
	 * @param ar_kingakuJyoken2Kbn the ar_kingakuJyoken2Kbn to set
	 */
	public void setAr_kingakuJyoken2Kbn(LinkedHashMap ar_kingakuJyoken2Kbn) {
		this.ar_kingakuJyoken2Kbn = ar_kingakuJyoken2Kbn;
	}

	/**
	 * @return the ar_systemKbn
	 */
	public LinkedHashMap getAr_systemKbn() {
		return ar_systemKbn;
	}

	/**
	 * @param ar_systemKbn the ar_systemKbn to set
	 */
	public void setAr_systemKbn(LinkedHashMap ar_systemKbn) {
		this.ar_systemKbn = ar_systemKbn;
	}

	/**
	 * @return the cyusyutuJiyu
	 */
	public String getCyusyutuJiyu() {
		return cyusyutuJiyu;
	}

	/**
	 * @param cyusyutuJiyu the cyusyutuJiyu to set
	 */
	public void setCyusyutuJiyu(String cyusyutuJiyu) {
		this.cyusyutuJiyu = cyusyutuJiyu;
	}

	/**
	 * @return the hanyo1
	 */
	public String getHanyo1() {
		return hanyo1;
	}

	/**
	 * @param hanyo1 the hanyo1 to set
	 */
	public void setHanyo1(String hanyo1) {
		this.hanyo1 = hanyo1;
	}

	/**
	 * @return the hanyo2
	 */
	public String getHanyo2() {
		return hanyo2;
	}

	/**
	 * @param hanyo2 the hanyo2 to set
	 */
	public void setHanyo2(String hanyo2) {
		this.hanyo2 = hanyo2;
	}

	/**
	 * @return the jyokenNmEn
	 */
	public String getJyokenNmEn() {
		return jyokenNmEn;
	}

	/**
	 * @param jyokenNmEn the jyokenNmEn to set
	 */
	public void setJyokenNmEn(String jyokenNmEn) {
		this.jyokenNmEn = jyokenNmEn;
	}

	/**
	 * @return the jyokenNmJp
	 */
	public String getJyokenNmJp() {
		return jyokenNmJp;
	}

	/**
	 * @param jyokenNmJp the jyokenNmJp to set
	 */
	public void setJyokenNmJp(String jyokenNmJp) {
		this.jyokenNmJp = jyokenNmJp;
	}

	/**
	 * @return the kakoKakudukeFlg
	 */
	public String getKakoKakudukeFlg() {
		return kakoKakudukeFlg;
	}

	/**
	 * @param kakoKakudukeFlg the kakoKakudukeFlg to set
	 */
	public void setKakoKakudukeFlg(String kakoKakudukeFlg) {
		this.kakoKakudukeFlg = kakoKakudukeFlg;
	}

	/**
	 * @return the kakoKakudukeFrom
	 */
	public String getKakoKakudukeFrom() {
		return kakoKakudukeFrom;
	}

	/**
	 * @param kakoKakudukeFrom the kakoKakudukeFrom to set
	 */
	public void setKakoKakudukeFrom(String kakoKakudukeFrom) {
		this.kakoKakudukeFrom = kakoKakudukeFrom;
	}

	/**
	 * @return the kakoKakudukeJiten
	 */
	public String getKakoKakudukeJiten() {
		return kakoKakudukeJiten;
	}

	/**
	 * @param kakoKakudukeJiten the kakoKakudukeJiten to set
	 */
	public void setKakoKakudukeJiten(String kakoKakudukeJiten) {
		this.kakoKakudukeJiten = kakoKakudukeJiten;
	}

	/**
	 * @return the kakoKakudukeTo
	 */
	public String getKakoKakudukeTo() {
		return kakoKakudukeTo;
	}

	/**
	 * @param kakoKakudukeTo the kakoKakudukeTo to set
	 */
	public void setKakoKakudukeTo(String kakoKakudukeTo) {
		this.kakoKakudukeTo = kakoKakudukeTo;
	}

	/**
	 * @return the kakuduke
	 */
	public String getKakuduke() {
		return kakuduke;
	}

	/**
	 * @param kakuduke the kakuduke to set
	 */
	public void setKakuduke(String kakuduke) {
		this.kakuduke = kakuduke;
	}

	/**
	 * @return the kesanKbn
	 */
	public String getKesanKbn() {
		return kesanKbn;
	}

	/**
	 * @param kesanKbn the kesanKbn to set
	 */
	public void setKesanKbn(String kesanKbn) {
		this.kesanKbn = kesanKbn;
	}

	/**
	 * @return the kijunbi
	 */
	public String getKijunbi() {
		return kijunbi;
	}

	/**
	 * @param kijunbi the kijunbi to set
	 */
	public void setKijunbi(String kijunbi) {
		this.kijunbi = kijunbi;
	}

	/**
	 * @return the kingakuJyoken1
	 */
	public String getKingakuJyoken1() {
		return kingakuJyoken1;
	}

	/**
	 * @param kingakuJyoken1 the kingakuJyoken1 to set
	 */
	public void setKingakuJyoken1(String kingakuJyoken1) {
		this.kingakuJyoken1 = kingakuJyoken1;
	}

	/**
	 * @return the kingakuJyoken2
	 */
	public String getKingakuJyoken2() {
		return kingakuJyoken2;
	}

	/**
	 * @param kingakuJyoken2 the kingakuJyoken2 to set
	 */
	public void setKingakuJyoken2(String kingakuJyoken2) {
		this.kingakuJyoken2 = kingakuJyoken2;
	}

	/**
	 * @return the kingakuJyoken2Kbn
	 */
	public String getKingakuJyoken2Kbn() {
		return kingakuJyoken2Kbn;
	}

	/**
	 * @param kingakuJyoken2Kbn the kingakuJyoken2Kbn to set
	 */
	public void setKingakuJyoken2Kbn(String kingakuJyoken2Kbn) {
		this.kingakuJyoken2Kbn = kingakuJyoken2Kbn;
	}

	/**
	 * @return the sateiTaisyo
	 */
	public String getSateiTaisyo() {
		return sateiTaisyo;
	}

	/**
	 * @param sateiTaisyo the sateiTaisyo to set
	 */
	public void setSateiTaisyo(String sateiTaisyo) {
		this.sateiTaisyo = sateiTaisyo;
	}

	/**
	 * @return the seniMode
	 */
	public String getSeniMode() {
		return seniMode;
	}

	/**
	 * @param seniMode the seniMode to set
	 */
	public void setSeniMode(String seniMode) {
		this.seniMode = seniMode;
	}

	/**
	 * @return the systemKbn
	 */
	public String getSystemKbn() {
		return systemKbn;
	}

	/**
	 * @param systemKbn the systemKbn to set
	 */
	public void setSystemKbn(String systemKbn) {
		this.systemKbn = systemKbn;
	}

	/**
	 * @return the tairyuKikanFrom
	 */
	public String getTairyuKikanFrom() {
		return tairyuKikanFrom;
	}

	/**
	 * @param tairyuKikanFrom the tairyuKikanFrom to set
	 */
	public void setTairyuKikanFrom(String tairyuKikanFrom) {
		this.tairyuKikanFrom = tairyuKikanFrom;
	}

	/**
	 * @return the tairyuKikanTo
	 */
	public String getTairyuKikanTo() {
		return tairyuKikanTo;
	}

	/**
	 * @param tairyuKikanTo the tairyuKikanTo to set
	 */
	public void setTairyuKikanTo(String tairyuKikanTo) {
		this.tairyuKikanTo = tairyuKikanTo;
	}

	/**
	 * @return the tairyuTaisyo
	 */
	public String getTairyuTaisyo() {
		return tairyuTaisyo;
	}

	/**
	 * @param tairyuTaisyo the tairyuTaisyo to set
	 */
	public void setTairyuTaisyo(String tairyuTaisyo) {
		this.tairyuTaisyo = tairyuTaisyo;
	}

	/**
	 * @return the tukaCd
	 */
	public String getTukaCd() {
		return tukaCd;
	}

	/**
	 * @param tukaCd the tukaCd to set
	 */
	public void setTukaCd(String tukaCd) {
		this.tukaCd = tukaCd;
	}
	
    /* (non-Javadoc)
     * @see common.struts.adapter.action.ActionForm#reset(common.struts.adapter.action.ActionMapping, jakarta.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request){
    	// À{‘Ø—¯”»’è‘ÎÛ
    	this.setTairyuTaisyo(GS.OFF);
    	// ¸’è‘ÎÛ
    	this.setSateiTaisyo(GS.OFF);
    	// ‰ß‹Ši•tƒtƒ‰ƒO
    	this.setKakoKakudukeFlg(GS.OFF);
    }

	/**
	 * @return the jokenNo
	 */
	public String getJokenNo() {
		return jokenNo;
	}

	/**
	 * @param jokenNo the jokenNo to set
	 */
	public void setJokenNo(String jokenNo) {
		this.jokenNo = jokenNo;
	}
}