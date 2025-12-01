/******************************************************************************
 著作権情報				:
 使用JDK バージョン		: 1.4.2.05
 更新履歴
 No		日付			修正者			修正内容
 001		2008/01/13		SSC				新規作成
 002		2008/03/03		SSC				メソッド名変更
 003		2016/12/16		SSC				BJ201612070 SSO対応
 ******************************************************************************/
package common.struts;

import app.login.action.MenuAction;
import common.AppContext;
import common.global.GS;
import config.adapter.struts.action.ActionForm;

import java.util.HashMap;

/**
 * 拡張アクションクラス(画面左メニューあり用）
 *
 */
public abstract class BaseAppMenuAction<F extends ActionForm> extends BaseAppAction<F> {

    /**
     * コンストラクタ
     *
     * @param formClass    form class
     * @param formBeanName bean name
     */
    protected BaseAppMenuAction(Class<F> formClass, String formBeanName) {
        super(formClass, formBeanName);
    }

    /* (非 Javadoc)
     * @see common.struts.AppAction#appExecute(common.AppContext)
     */
    public Object appExecute(AppContext appContext) throws Exception {
        return null;
    }

    /* (非 Javadoc)
     * @see common.struts.AppAction#getKeyMethodMap()
     */
    public HashMap getKeyMethodMap() {
        return null;
    }

    /*
     * 継承クラスから呼び出し
     */
    public HashMap<String, String> getKeyMethodMap(HashMap<String, String> map) {
        //メニューへボタン
        map.put("menuLinkOS2101", "menuLinkOS2101");

        //画面左メニュー
        map.put("menuLinkOB1101", "menuLinkOB1101");                            //判定登録
        map.put("menuLinkOB1104", "menuLinkOB1104");                            //判定承認
        map.put("menuLinkOB2101", "menuLinkOB2101");                            //対象先選定
        map.put("menuLinkOB2104", "menuLinkOB2104");                            //対象先選定確認
        map.put("menuLinkOC1101", "menuLinkOC1101");                            //査定登録
        map.put("menuLinkOC1106", "menuLinkOC1106");                            //査定承認
        map.put("menuLinkOD1101", "menuLinkOD1101");                            //引当金確認
        map.put("menuLinkOD1103", "menuLinkOD1103");                            //引当金承認
        map.put("menuLinkOS6101", "menuLinkOS6101");                            //査定内容照会
        map.put("menuLinkOS6103", "menuLinkOS6103");                            //進捗状況照会
        map.put("menuLinkOS3101", "menuLinkOS3101");                            //クレーム債権再設定
        map.put("menuLinkOS3104", "menuLinkOS3104");                            //クレーム債権再設定承認
        map.put("menuLinkOS7101", "menuLinkOS7101");                            //代行設定
        map.put("menuLinkOS7102", "menuLinkOS7102");                            //査定会社メンテナンス
        map.put("menuLinkOS7104", "menuLinkOS7104");                            //業務フローパターンメンテナンス
        map.put("menuLinkOS7106", "menuLinkOS7106");                            //ユーザマスタメンテナンス
        map.put("menuLinkOS7108", "menuLinkOS7108");                            //勘定科目マスタメンテナンス
        map.put("menuLinkOS7114", "menuLinkOS7114");                            //抽出条件メンテナンス(本社)
        map.put("menuLinkOS7110", "menuLinkOS7110");                            //抽出条件メンテナンス
        map.put("menuLinkOS5101", "menuLinkOS5101");                            //チャンピオン部メンテナンス
        map.put("menuLinkOS4101", "menuLinkOS4101");                            //ゴルフ会員権メンテナンス
        map.put("menuLinkOS7113", "menuLinkOS7113");                            //連結区分マスタUPLOAD
        map.put("menuLinkOS8101", "menuLinkOS8101");                            //帳票ダウンロード
        map.put("menuLinkOS1101", "menuLinkOS1101");                            //ログオフ

        //前のＸ件/次のＹ件処理
        map.put("prevX", "prevX");
        map.put("nextY", "nextY");
        return map;
    }

    /**
     * 【戻るボタン処理】
     */
    public Object menuLinkOS2101(AppContext appContext) throws Exception {
        //メインメニュー画面に遷移
        MenuAction acc = new MenuAction();
        acc.appReExecute(appContext);
        return GS.OS2101;
    }

    /**
     * 画面左メニュー
     */
    public Object menuLinkOB1101(AppContext appContext) throws Exception {
        app.tairyu.action.IchiranAction acc = new app.tairyu.action.IchiranAction();
        acc.appExecute(appContext);
        return GS.OB1101;
    }

    public Object menuLinkOB1104(AppContext appContext) throws Exception {
        app.tairyu.action.SyoninAction acc = new app.tairyu.action.SyoninAction();
        acc.appExecute(appContext);
        return GS.OB1104;
    }

    public Object menuLinkOB2101(AppContext appContext) throws Exception {
        app.tairyu.action.SenteiAction acc = new app.tairyu.action.SenteiAction();
        acc.appExecute(appContext);
        return GS.OB2101;
    }

    public Object menuLinkOB2104(AppContext appContext) throws Exception {
        app.tairyu.action.SenteisyoninAction acc = new app.tairyu.action.SenteisyoninAction();
        acc.appExecute(appContext);
        return GS.OB2104;
    }

    public Object menuLinkOC1101(AppContext appContext) throws Exception {
        app.satei.action.IchiranAction acc = new app.satei.action.IchiranAction();
        acc.appExecute(appContext);
        return GS.OC1101;
    }

    public Object menuLinkOC1106(AppContext appContext) throws Exception {
        app.satei.action.SyoninAction acc = new app.satei.action.SyoninAction();
        acc.appExecute(appContext);
        return GS.OC1106;
    }

    public Object menuLinkhikiatekensyo(AppContext appContext) throws Exception {
        return GS.HIKIATEKENSYO;
    }

    public Object menuLinkOD1101(AppContext appContext) throws Exception {
        app.hikiate.action.IchiranAction acc = new app.hikiate.action.IchiranAction();
        acc.appExecute(appContext);
        return GS.OD1101;
    }

    public Object menuLinkOD1103(AppContext appContext) throws Exception {
        app.hikiate.action.HikiateSyoninAction acc = new app.hikiate.action.HikiateSyoninAction();
        acc.appExecute(appContext);
        return GS.OD1103;
    }

    public Object menuLinkOS6101(AppContext appContext) throws Exception {
        app.syokai.action.SateiAction acc = new app.syokai.action.SateiAction();
        acc.appExecute(appContext);
        return GS.OS6101;
    }

    public Object menuLinkOS6103(AppContext appContext) throws Exception {
        app.syokai.action.SincyokuAction acc = new app.syokai.action.SincyokuAction();
        acc.appExecute(appContext);
        return GS.OS6103;
    }

    public Object menuLinkOS3101(AppContext appContext) throws Exception {
        app.system.action.KureemuAction acc = new app.system.action.KureemuAction();
        acc.appExecute(appContext);
        return GS.OS3101;
    }

    public Object menuLinkOS3104(AppContext appContext) throws Exception {
        app.system.action.KureemuSyoninAction acc = new app.system.action.KureemuSyoninAction();
        acc.appExecute(appContext);
        return GS.OS3104;
    }

    public Object menuLinkOS7101(AppContext appContext) throws Exception {
        app.system.action.DaikoAction acc = new app.system.action.DaikoAction();
        acc.appExecute(appContext);
        return GS.OS7101;
    }

    public Object menuLinkOS7102(AppContext appContext) throws Exception {
        app.system.action.SateikaisyaIchiranAction acc = new app.system.action.SateikaisyaIchiranAction();
        acc.appExecute(appContext);
        return GS.OS7102;
    }

    public Object menuLinkOS7104(AppContext appContext) throws Exception {
        app.system.action.WorkFlowIchiranAction acc = new app.system.action.WorkFlowIchiranAction();
        acc.appExecute(appContext);
        return GS.OS7104;
    }

    public Object menuLinkOS7106(AppContext appContext) throws Exception {
        app.system.action.UserIchiranAction acc = new app.system.action.UserIchiranAction();
        acc.appExecute(appContext);
        return GS.OS7106;
    }

    public Object menuLinkOS7108(AppContext appContext) throws Exception {
        app.system.action.KanjyoAction acc = new app.system.action.KanjyoAction();
        acc.appExecute(appContext);
        return GS.OS7108;
    }

    public Object menuLinkOS7114(AppContext appContext) throws Exception {
        app.system.action.CyusyutujyokenHqAction acc = new app.system.action.CyusyutujyokenHqAction();
        acc.appExecute(appContext);
        return GS.OS7114;
    }

    public Object menuLinkOS7110(AppContext appContext) throws Exception {
        app.system.action.CyusyutujyokenAction acc = new app.system.action.CyusyutujyokenAction();
        acc.appExecute(appContext);
        return GS.OS7110;
    }

    public Object menuLinkOS5101(AppContext appContext) throws Exception {
        app.system.action.ChampionAction acc = new app.system.action.ChampionAction();
        acc.appExecute(appContext);
        return GS.OS5101;
    }

    public Object menuLinkOS4101(AppContext appContext) throws Exception {
        app.system.action.GolfAction acc = new app.system.action.GolfAction();
        acc.appExecute(appContext);
        return GS.OS4101;
    }

    public Object menuLinkOS7113(AppContext appContext) throws Exception {
        app.system.action.RenketuAction acc = new app.system.action.RenketuAction();
        acc.appExecute(appContext);
        return GS.OS7113;
    }

    public Object menuLinkOS8101(AppContext appContext) throws Exception {
        app.system.action.DownloadAction acc = new app.system.action.DownloadAction();
        acc.appExecute(appContext);
        return GS.OS8101;
    }

    public Object menuLinkOS1101(AppContext appContext) throws Exception {
        app.login.action.LoginAction acc = new app.login.action.LoginAction();
        acc.appExecute(appContext);
        return GS.RC_CLOSE;
    }

    /**
     * 前のＸ件リンク押下処理
     * <p>
     * 継承したクラスに対応するActionFormのsetPrevList()を実行
     *
     * @return GS.RC_OK
     */
    public abstract Object prevX(AppContext appContext) throws Exception;

    /**
     * 次のＹ件リンク押下処理
     * <p>
     * 継承したクラスに対応するActionFormのsetNextList()を実行
     *
     * @return GS.RC_OK
     */
    public abstract Object nextY(AppContext appContext) throws Exception;
}
