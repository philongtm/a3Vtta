/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/5/19		SSC				1.5次版機能組込
******************************************************************************/
package app.system.action;

import app.system.bss.CyusyutujyokenHqBss;
import app.system.form.CyusyutujyokenHqForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 抽出条件メンテナンス画面アクションクラス
 */
@Controller
@RequestMapping("/system/cyusyutujyoken_hq.do")
public class CyusyutujyokenHqAction extends AppMenuAction {

	private String CLASSNAME = getClass().getName(); // クラス名
	
	/**=========================================
	 * ディスパッチマップ作成&変数初期化
	 *==========================================*/
	public HashMap getKeyMethodMap() {
	    //ディスパッチマップ作成
		HashMap map = new HashMap();
		map = super.getKeyMethodMap(map);
		map.put("kbn_change", "kbn_change");
		map.put("type", "type");
		map.put("kentou_joken", "kentou_joken");
		map.put("satei_joken", "satei_joken");
		map.put("kentou_touroku", "kentou_touroku");
		map.put("kentou_sakujo", "kentou_sakujo");
		map.put("satei_touroku", "satei_touroku");
		map.put("satei_sakujo", "satei_sakujo");
		map.put("jiyuu_link", "jiyuu_link");
		map.put("tairyu_link", "tairyu_link");
		map.put("check_kentou", "check_kentou");
		map.put("check_tairyu", "check_tairyu");
		map.put("check_satei", "check_satei");
		map.put("check_saimutyouka", "check_saimutyouka");
		map.put("check_akaji", "check_akaji");
		map.put("check_riki", "check_riki");
		map.put("sateikaisya_change","sateikaisya_change");
		map.put("mise_change","mise_change");
		//要件No.四-13
		//追加開始
		map.put("kessanki_change","kessanki_change");
		//追加完了
		
		return map;
	}

	
	/**=========================================
	 * 共通セッション＆ActionFormBeanオブジェクト取得
	 *==========================================*/
	public Object prevX(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
	    CyusyutujyokenHqForm form = (CyusyutujyokenHqForm)appContext.getActionForm();
		form.setPrevList();
	    return GS.OS7114;
	}
	
	public Object nextY(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
	    CyusyutujyokenHqForm form = (CyusyutujyokenHqForm)appContext.getActionForm();
		form.setNextList();
	    return GS.OS7114;	    
	}
	
	/**
	 * 【画面初期表示処理】
	 */
	public Object appExecute(AppContext appContext) throws Exception {
	    // この画面用のActionFormを作成
	    CyusyutujyokenHqForm form = new CyusyutujyokenHqForm();
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
	    	    
	    return select(appContext, 1);
	}
	
	/**
	 * 【抽出条件区分選択処理】
	 * 　システムセレクトボックス、処理区分セレクトボックス変更時
	 */
	public Object kbn_change(AppContext appContext) throws Exception {

		return select(appContext, 0);
	}
	
	/**
	 * 【店コード選択処理】
	 * 　システムセレクトボックス、処理区分セレクトボックス変更時
	 */
	public Object mise_change(AppContext appContext) throws Exception {

		return select(appContext, 0);
	}
	
	//要件No.四-13
	//追加開始
	/**
	 * 【決算期区分選択処理】
	 * 　システムセレクトボックス、処理区分セレクトボックス変更時
	 */
	public Object kessanki_change(AppContext appContext) throws Exception {

		return select(appContext, 0);
	}
	//追加完了
	
	/**
	 * 【処理種別ラジオボタン切り替え処理】
	 */
	public Object type(AppContext appContext) throws Exception {
		// ラジオボタン情報の取得
	    CyusyutujyokenHqForm form = (CyusyutujyokenHqForm)appContext.getActionForm();
		String type = form.getType();
		String old_type = null;
		
		boolean isKentou = "1".equals(form.getJokenKbn());
		if (isKentou) {
			old_type = form.getType_old_kentou();
		} else {
			old_type = form.getType_old_satei();
		}
		
		// 切り替え判定
		if (!type.equals(old_type)) {
			// 切り替えありの場合、表示入力項目の初期化
			if (isKentou) {
				form.setNo_kentou(null);
				form.setJiyuu_cd(null);
				form.setJiyuu_nm(null);
				form.setKakuzuke("");
				form.setTuuka_kentou(null);
				form.setKingaku_kentou(null);
				form.setTairyu_from(null);
				form.setTairyu_to(null);
				form.setSaimutyouka_flg("");
				form.setAkaji_flg("");
				form.setRiki_flg("");
				form.setKentou_flg("");
				form.setTairyu_flg("");
				form.setSatei_flg("");
				form.setSaiken_data_flg(new String[4]);
				form.setSaiken_kentou_flg(new String[4]);
				form.setSaiken_tairyu_flg(new String[4]);
				
				// 現在の処理種別を古い種別に設定
				form.setType_old_kentou(type);
			} else {
				form.setNo_satei(null);
				form.setTairyu_hantei("0");
			    // 管理票No200808070006, 2008/08/07, SJA平道, 滞留判定名称の初期化を追加
				form.setTairyu_hantei_nm(null);
				form.setTuuka_satei(null);
				form.setKingaku_satei(null);

				// 現在の処理種別を古い種別に設定
				form.setType_old_satei(type);
			}
		}
			
	    return GS.OS7114;
	}
	
	/**
	 * 【査定会社切り替え処理】
	 */
	public Object sateikaisya_change(AppContext appContext) throws Exception {

	    // 検索実行
	    CyusyutujyokenHqBss bss = new CyusyutujyokenHqBss(appContext);
	    bss.executeSateiKaisya();
			
	    return select(appContext, 0);
	}
	
	
	/**
	 * 【検討対象先条件タブ切り替え処理】
	 */
	public Object kentou_joken(AppContext appContext) throws Exception {
	    CyusyutujyokenHqForm form = (CyusyutujyokenHqForm)appContext.getActionForm();

	    form.setJokenKbn("1");
			
	    return GS.OS7114;
	}
	
	/**
	 * 【査定対象先条件タブ切り替え処理】
	 */
	public Object satei_joken(AppContext appContext) throws Exception {
	    CyusyutujyokenHqForm form = (CyusyutujyokenHqForm)appContext.getActionForm();

	    form.setJokenKbn("2");
			
	    return GS.OS7114;
	}
	
	/**
	 * 【検討対象先条件登録処理】
	 */
	public Object kentou_touroku(AppContext appContext) throws Exception {
	    CyusyutujyokenHqBss bss = new CyusyutujyokenHqBss(appContext); 
	    bss.torokuKentouTaisyou();
			
	    return GS.OS7114;
	}
	
	/**
	 * 【検討対象先条件削除処理】
	 */
	public Object kentou_sakujo(AppContext appContext) throws Exception {
	    CyusyutujyokenHqBss bss = new CyusyutujyokenHqBss(appContext); 
	    bss.sakujoKentouTaisyou();
			
	    return GS.OS7114;
	}
	
	/**
	 * 【査定対象先条件登録処理】
	 */
	public Object satei_touroku(AppContext appContext) throws Exception {
	    CyusyutujyokenHqBss bss = new CyusyutujyokenHqBss(appContext); 
	    bss.torokuSateiTaisyou();
			
	    return GS.OS7114;
	}
	
	/**
	 * 【査定対象先条件削除処理】
	 */
	public Object satei_sakujo(AppContext appContext) throws Exception {
	    CyusyutujyokenHqBss bss = new CyusyutujyokenHqBss(appContext); 
	    bss.sakujoSateiTaisyou();
			
	    return GS.OS7114;
	}
	
	/**
	 * 【抽出事由リンク処理】
	 */
	public Object jiyuu_link(AppContext appContext) throws Exception {
	    CyusyutujyokenHqForm form = (CyusyutujyokenHqForm)appContext.getActionForm();
	    
	    List list = form.getKentouList();
	    HashMap map = (HashMap)list.get(form.getSelectIdx());
	    form.setNo_kentou((String)map.get("jyouken_no"));
	    form.setJiyuu_cd((String)map.get("jiyuu_cd"));
	    form.setJiyuu_nm((String)map.get("jiyuu_nm"));
	    String mukakuzuke = (String)map.get("mukakuzuke_flg");
	    if ("1".equals(mukakuzuke)) {
	    	form.setKakuzuke("NON");
	    } else {
	    	form.setKakuzuke((String)map.get("kakuzuke"));
	    }
	    form.setTairyu_from((String)map.get("tairyu_from"));
	    form.setTairyu_to((String)map.get("tairyu_to"));
	    form.setTuuka_kentou((String)map.get("tuuka"));
	    form.setKingaku_kentou((String)map.get("kingaku"));
	    form.setKentou_flg((String)map.get("kentou_flg"));
	    form.setTairyu_flg((String)map.get("tairyu_flg"));
	    form.setSatei_flg((String)map.get("satei_flg"));
	    form.setSaimutyouka_flg((String)map.get("saimutyouka_flg"));
	    form.setAkaji_flg((String)map.get("akaji_flg"));
	    form.setRiki_flg((String)map.get("riki_flg"));

	    form.setSaiken_data_flg((String[])((ArrayList)map.get("data_flg_list")).toArray(new String[4]));
	    form.setSaiken_kentou_flg((String[])((ArrayList)map.get("kentou_flg_list")).toArray(new String[4]));
	    form.setSaiken_tairyu_flg((String[])((ArrayList)map.get("tairyu_flg_list")).toArray(new String[4]));
	    form.setSaiken_all_flg((String[])((ArrayList)map.get("saiken_flg_list")).toArray(new String[4])); 
	    form.setNo_kentou_flg((String[])((ArrayList)map.get("jyouken_flg_list")).toArray(new String[4]));   
		//要件No.四-13　障害No.0003対応
		//追加開始
		form.setKakoKtkFlg((String)map.get("kako_ktk_flg"));
	    form.setKakoKtkFrom((String)map.get("kako_ktk_from"));
	    form.setKakoKtkTo((String)map.get("kako_ktk_to"));
	    form.setKakoKtkSansyo((String)map.get("kako_ktk_sansyo"));
	    form.setGenzaiKoteiSaikengakuJyogen((String)map.get("genzai_kotei_saikengaku_jyogen"));
	    form.setGenzaiKoteiSaikengakuKagen((String)map.get("genzai_kotei_saikengaku_kagen"));
	    form.setKakoKoteiSaikengakuJyogen((String)map.get("kako_kotei_saikengaku_jyogen"));
	    form.setKakoKoteiSaikengakuKagen((String)map.get("kako_kotei_saikengaku_kagen"));
	    form.setKoteiSaikengakuSansyo((String)map.get("kotei_saikengaku_sansyo"));
	    form.setFlgsakiFlg((String)map.get("flgsaki_flg"));
		//追加完了
	    
	    return GS.OS7114;
	}
	
	/**
	 * 【滞留判定リンク処理】
	 */
	public Object tairyu_link(AppContext appContext) throws Exception {
	    CyusyutujyokenHqForm form = (CyusyutujyokenHqForm)appContext.getActionForm();
	    
	    List list = form.getSateiList();
	    HashMap map = (HashMap)list.get(form.getSelectIdx());
	    form.setNo_satei((String)map.get("jyouken_no"));
	    form.setTairyu_hantei((String)map.get("tairyu_hantei"));
	    form.setTairyu_hantei_nm((String)map.get("tairyu_hantei_nm"));
	    form.setTuuka_satei((String)map.get("tuuka"));
	    form.setKingaku_satei((String)map.get("kingaku"));
	    return GS.OS7114;
	}
	
	/**
	 * 【検討対象チェックボックス（検討対象先条件タブ）選択処理】
	 */
	public Object check_kentou(AppContext appContext) throws Exception {
	    CyusyutujyokenHqForm form = (CyusyutujyokenHqForm)appContext.getActionForm();
	    
		if ("1".equals(form.getKentou_flg())) {
			// 債権フラグ設定（検討対象）の初期化
			form.setSaiken_kentou_flg(new String[4]);
			
			form.setKentou_flg("");
		} else {
			form.setKentou_flg("1");
		}
			
	    return GS.OS7114;
	}
	
	/**
	 * 【滞留判定チェックボックス（検討対象先条件タブ）選択処理】
	 */
	public Object check_tairyu(AppContext appContext) throws Exception {
	    CyusyutujyokenHqForm form = (CyusyutujyokenHqForm)appContext.getActionForm();
	    
		if ("1".equals(form.getTairyu_flg())) {
			// 債権フラグ設定（滞留判定）の初期化
			form.setSaiken_tairyu_flg(new String[4]);
			
			form.setTairyu_flg("");
		} else {
			form.setTairyu_flg("1");
		}
	    
	    return GS.OS7114;
	}
	
	/**
	 * 【査定対象チェックボックス（検討対象先条件タブ）選択処理】
	 */
	public Object check_satei(AppContext appContext) throws Exception {
	    CyusyutujyokenHqForm form = (CyusyutujyokenHqForm)appContext.getActionForm();
	    
		if ("1".equals(form.getSatei_flg())) {
			form.setSatei_flg("");
		} else {
			form.setSatei_flg("1");
		}
	    
	    return GS.OS7114;
	}
	
	/**
	 * 【債務超過チェックボックス（検討対象先条件タブ）選択処理】
	 */
	public Object check_saimutyouka(AppContext appContext) throws Exception {
	    CyusyutujyokenHqForm form = (CyusyutujyokenHqForm)appContext.getActionForm();
	    
		if ("1".equals(form.getSaimutyouka_flg())) {
			form.setSaimutyouka_flg("");
		} else {
			form.setSaimutyouka_flg("1");
			form.setAkaji_flg("");
			form.setRiki_flg("");
		}
	    
	    return GS.OS7114;
	}
	
	/**
	 * 【赤字チェックボックス（検討対象先条件タブ）選択処理】
	 */
	public Object check_akaji(AppContext appContext) throws Exception {
	    CyusyutujyokenHqForm form = (CyusyutujyokenHqForm)appContext.getActionForm();
	    
		if ("1".equals(form.getAkaji_flg())) {
			form.setAkaji_flg("");
		} else {
			form.setSaimutyouka_flg("");
			form.setAkaji_flg("1");
			form.setRiki_flg("");
		}
	    
	    return GS.OS7114;
	}
	
	/**
	 * 【リ企指定チェックボックス（検討対象先条件タブ）選択処理】
	 */
	public Object check_riki(AppContext appContext) throws Exception {
	    CyusyutujyokenHqForm form = (CyusyutujyokenHqForm)appContext.getActionForm();
	    
		if ("1".equals(form.getRiki_flg())) {
			form.setRiki_flg("");
		} else {
			form.setSaimutyouka_flg("");
			form.setAkaji_flg("");
			form.setRiki_flg("1");
		}
	    
	    return GS.OS7114;
	}

	/**=========================================
	 * 明細情報取得
	 *==========================================*/
	public Object select(AppContext appContext, int initmode) throws Exception {    		
	    // session取得（Pager用の処理）
		// No426, 2008/05/22, SJA渡辺, オブジェクト変数名を即した名称に修正。
	    HttpSession session = appContext.getRequest().getSession( true );
	    
	    CyusyutujyokenHqForm form = (CyusyutujyokenHqForm)appContext.getActionForm();
  
	    // 検索実行
	    CyusyutujyokenHqBss bss = new CyusyutujyokenHqBss(appContext);       	    
	    if(initmode == 1) { 
	    	// 画面初期表示時
	        String result = bss.execute();

	        // sessionスコープにActionFormを登録（Pager用の処理）
	        session.setAttribute("04CyusyutujyokenForm", form);

	        return result;
	    } else { 
	    	// 画面初期表示時以外
			String result =  bss.selectSyosaiList();
			return result;
	    }
	}
	
	
}