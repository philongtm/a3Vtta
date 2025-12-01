/******************************************************************************
 著作権情報				:
 使用JDK バージョン		:1.5.0.18
 更新履歴
 No		日付			修正者			修正内容
 001		2009/06/30		SSC				新規作成
 002		2016/12/26		SSC				BJ201612070 SSO対応
 ******************************************************************************/
package app.login.action;

import app.SessionData;
import app.login.bss.MenuBss;
import app.login.form.MenuForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppLocale;
import common.struts.BaseAppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OS2101_メインメニュー アクションクラス
 */
@Controller
@RequestMapping("/login/menu.do")
public class MenuAction extends BaseAppMenuAction<MenuForm> {

    private static final String MENUFORM = "00MenuForm";

    /**
     * Constructor
     */
    public MenuAction() {
        super(MenuForm.class, MENUFORM);
    }

    /**
     * ディスパッチマップ作成
     */
    public HashMap getKeyMethodMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map = super.getKeyMethodMap(map);
        map.put("pattern", "pattern");
        map.put("satei", "satei");
        map.put("taishou_ym", "taishou_ym");
        map.put("daiko", "daiko");
        map.put("gengo_j", "gengo_j");
        map.put("gengo_e", "gengo_e");
        return map;
    }

    /**
     * デフォルトのアクション
     */
    public Object appExecute(AppContext appContext) throws Exception {
        // appContextのActionFormを上書き
        MenuForm form = new MenuForm();
        appContext.setActionForm(form);

        // ビジネスロジック実行
        MenuBss bss = new MenuBss(appContext);
        String result = bss.executeInit();

        appContext.setSessionActionForm(MENUFORM, form);

        return result;
    }

    /**
     * 一覧系画面から「戻る」のアクション
     */
    public Object appReExecute(AppContext appContext) throws Exception {
        appContext.removeActionFormAll();
        // sessionからActionForm取得
        MenuForm form = (MenuForm) appContext.getSessionActionForm(MENUFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);

        // ビジネスロジック実行
        MenuBss bss = new MenuBss(appContext);
        String result = bss.execute();

        return result;
    }

    /**
     * 業務フロー切替アクション
     */
    public Object pattern(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        MenuBss bss = new MenuBss(appContext);
        String result = bss.pattern();

        return result;
    }

    /**
     * 査定期アクション
     */
    public Object satei(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        MenuBss bss = new MenuBss(appContext);
        String result = bss.satei();

        return result;
    }

    /**
     * 対象年月アクション
     */
    public Object taishou_ym(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        MenuBss bss = new MenuBss(appContext);
        String result = bss.taishou_ym();

        return result;
    }

    /**
     * 代行者画面切替アクション
     */
    public Object daiko(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        MenuBss bss = new MenuBss(appContext);
        String result = bss.daiko();

        return result;
    }

    /**
     * 言語切替アクション(Japanese押下)
     *
     */
    public Object gengo_j(AppContext appContext) throws Exception {
        AppLocale.setJa(appContext.getSession());

        SessionData cmnData = appContext.getCMN();
        cmnData.setComLangMode(GS.LANG_JA);

        // ビジネスロジック実行
        MenuBss bss = new MenuBss(appContext);
        String result = bss.gengo();

        return result;
    }

    /**
     * 言語切替アクション(English押下)
     *
     */
    public Object gengo_e(AppContext appContext) throws Exception {

        AppLocale.setEn(appContext.getSession());

        SessionData cmnData = appContext.getCMN();
        cmnData.setComLangMode(GS.LANG_EN);

        // ビジネスロジック実行
        MenuBss bss = new MenuBss(appContext);
        String result = bss.gengo();

        return result;
    }

    /**
     * 【←前のXX件】未実装
     */
    public Object prevX(AppContext appContext) throws Exception {
        return GS.OS2101;
    }

    /**
     * 【次のXX件→】未実装
     */
    public Object nextY(AppContext appContext) throws Exception {
        return GS.OS2101;
    }
}