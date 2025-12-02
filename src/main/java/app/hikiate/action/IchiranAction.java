/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.hikiate.action;

import app.SessionData;
import app.TorihikisakiBean;
import app.hikiate.bss.IchiranBss;
import app.hikiate.form.IchiranForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OD1101_引当金確認_対象先一覧 アクションクラス
 */
@Controller
@RequestMapping("/hikiate/ichiran.do")
public class IchiranAction extends AppMenuAction {

    private static final String ICHIRANFORM = "06IchiranForm";
    
    /**
     * ディスパッチマップ作成
     */
    public HashMap getKeyMethodMap() {
        // ディスパッチアップ作成
        HashMap<String,String> map = new HashMap<String,String>();
        map = super.getKeyMethodMap(map);
        map.put("tanto","tanto");
        map.put("sateiki","sateiki");
        map.put("sort_item","sort_item");
        map.put("sort_order","sort_order");
        map.put("show","show");
        map.put("mogitori","mogitori");
        map.put("ikkatuMogitori","ikkatuMogitori");
        return map;
    }
    
    /**
     * 【画面初期表示処理(メニューリンクから遷移時)】
     */
    public Object appExecute(AppContext appContext) throws Exception {  
        
        // appContextのActionFormを上書き
        IchiranForm form = new IchiranForm();
        appContext.setActionForm(form);
        // 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();        
        // ビジネスロジック実行
        IchiranBss bss = new IchiranBss(appContext);            
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(ICHIRANFORM, form);
        return result;
    }

    /**
     * 【画面初期表示処理(メニューリンク以外から遷移時)】
     */
    
    public Object appReExecute(AppContext appContext) throws Exception {        
    	appContext.removeActionFormExcept(ICHIRANFORM);
        // sessionからActionForm取得
        HttpSession session = appContext.getRequest().getSession( true );
        IchiranForm form = (IchiranForm)session.getAttribute(ICHIRANFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean(); 
        // ビジネスロジック実行
        IchiranBss bss = new IchiranBss(appContext);            
        String result = bss.execute();
        // 前回表示時のページ設定をPagerにセット
        //HashMap map = (HashMap)form.getAr_meisai().get(form.getId());
        //form.setPager(Function.getValueOfInt((String)map.get(ID)) + 1);
        form.setPager(form.getId() + 1);
        return result;
    }
    
    /**
     * 【自担当分/汎用２ラジオボタン処理】
     */
    public Object tanto(AppContext appContext) throws Exception {
        IchiranBss bss = new IchiranBss(appContext);            
        String result = bss.execute();
        return result;
    }

    /**
     * 【査定期セレクトボックス処理】
     */
    public Object sateiki(AppContext appContext) throws Exception {
        IchiranBss bss = new IchiranBss(appContext);            
        String result = bss.execute();
        return result;
    }

    /**
     * 【ソート項目セレクトボックス処理】
     */
    public Object sort_item(AppContext appContext) throws Exception {
        IchiranBss bss = new IchiranBss(appContext);            
        String result = bss.execute();
        return result;
    }

    /**
     * 【整列方向セレクトボックス処理】
     */
    public Object sort_order(AppContext appContext) throws Exception {
        IchiranBss bss = new IchiranBss(appContext);            
        String result = bss.execute();
        return result;
    }
    
    /**
     * 【表示件数セレクトボックス処理】
     */
    public Object show(AppContext appContext) throws Exception {
        // 表示件数の変更をPagerオブジェクトに設定
        IchiranForm form = (IchiranForm)appContext.getActionForm();
        form.setPager();
        return GS.OD1101;
    }

    /**
     * 【←前のXX件】
     */ 
    public Object prevX(AppContext appContext) throws Exception {
        // 表示部分の変更をListオブジェクトに設定
        IchiranForm form = (IchiranForm)appContext.getActionForm();
        form.setPrevList();
        return GS.OD1101;
    }
    
    /**
     * 【次のXX件→】
     */ 
    public Object nextY(AppContext appContext) throws Exception {
        // 表示部分の変更をListオブジェクトに設定
        IchiranForm form = (IchiranForm)appContext.getActionForm();
        form.setNextList();
        return GS.OD1101;       
    }

    /**
     * 【勘定先CDリンク処理】
     */
    public Object mogitori(AppContext appContext) throws Exception {

        // もぎ取り処理
        TorihikisakiBean toriBean = null;
        IchiranBss bss = new IchiranBss(appContext);
        if(bss.doMogitori()) {
            // クリックされた勘定先情報を機能共通セッションに格納
            IchiranForm form = (IchiranForm)appContext.getActionForm();
            SessionData cmnData = appContext.getCMN();
            toriBean = (TorihikisakiBean)form.getAr_meisai().get(form.getId());
            cmnData.setTori_bean(toriBean);

            // OD1102_実質滞留債権判定_明細一覧に遷移
            if(toriBean.getPhase().equals(GS.PHASE_HIKIATEKIN_KAKUNIN)){
                KakuninAction acc = new KakuninAction();
                acc.appExecute(appContext);
                return GS.OD1102;
            }else{
                KensyoAction acc = new KensyoAction();
                acc.appExecute(appContext);
            	return GS.OD1105;
            }
        } else {
            return GS.OD1101;
        }
    }
    
    /**
     * 【一括もぎ取りアクション】
     */
    public Object ikkatuMogitori(AppContext appContext) throws Exception {
    	// 表示部分の変更をListオブジェクトに設定
        IchiranForm form = (IchiranForm)appContext.getActionForm();
    	// もぎ取り処理
        IchiranBss bss = new IchiranBss(appContext);
        bss.doIkkatuMogitori();
        form.setSelectedMountains(null);
        // 一覧/詳細部を再検索し
        IchiranAction acc = new IchiranAction();
        acc.appReExecute(appContext);
        return GS.OD1101;
        
    }
}