/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.action;

import app.SessionDataZen;
import app.common.action.SashimodoshiAction;
import app.common.action.TenpuSentakuAction;
import app.common.action.TensouAction;
import app.print.bss.SateiSyosaiExcelBss;
import app.satei.bss.HikiateBss;
import app.satei.form.HikiateForm;
import app.syokai.action.SateisyosaiAction;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import common.struts.AppPagerActionForm;
import common.util.SateiSyosaiExcel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OC1104_査定_引当金判定 アクションクラス<br>
 */
@Controller
@RequestMapping("/satei/hikiate.do")
public class HikiateAction extends AppMenuAction {

	private static final String HIKIATEFORM			= "02HikiateForm";
	private static final String FORWARD				= "forward";		//転送
	private static final String SENDBACK				= "sendBack";		//差戻
	private static final String TOHEAD				= "toHead";			//先頭へ
	private static final String FIRSTPRESERVE			= "firstPreserve";	//一次保存
	private static final String REGISTER				= "register";		//登録
	private static final String DOWNLOAD				= "download";		//ダウンロード
	private static final String CLAIMDETAIL			= "claimDetail";	//債権明細
	private static final String RESERVEDEPT			= "reserveDebt";	//留保債務
	private static final String BACK					= "back";
	private static final String APPENDSELECTION		= "appendSelection";//添付選択
	private static final String SHIHANKIHYOUJI		= "shihankiHyouji";	//四半期抽出項目表示

	private static final String HYOUJI				= "1";				//四半期抽出項目表示TRUE

	//入力履歴登録判定用
	private static final boolean HOZON_TRUE		= true;
	private static final boolean HOZON_FALSE		= false;
	//承認チェック判定用
	private static final boolean SHONIN_FALSE	= false;
	private static final boolean SHONIN_TRUE		= true;

    /**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチマップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put(TOHEAD,TOHEAD);
        map.put(FORWARD,FORWARD);
        map.put(SENDBACK,SENDBACK);
        map.put(FIRSTPRESERVE,FIRSTPRESERVE);
        map.put(APPENDSELECTION,APPENDSELECTION);
        map.put(REGISTER,REGISTER);
        map.put(DOWNLOAD,DOWNLOAD);
        map.put(CLAIMDETAIL,CLAIMDETAIL);
        map.put(RESERVEDEPT,RESERVEDEPT);
        map.put(SHIHANKIHYOUJI,SHIHANKIHYOUJI);
        map.put(BACK,BACK);
		return map;
	}
	
	/**
	 * 【メニューへボタン処理】
	 */
	public Object back(AppContext appContext) throws Exception {
		//OC1101_査定_対象先一覧に遷移
		IchiranAction acc = new IchiranAction();
		acc.appReExecute(appContext);
		return GS.OC1101;
	}

	/**
	 * 【ダウンロード処理】
	 */
	public Object download(AppContext appContext) throws Exception {
		String systemKbn = appContext.getCMN().getTori_bean().getSystem_kbn();
		//国内版帳票メソッドに飛ばす!!
		if(systemKbn.equals(GS.GSS)){
			downloadKokunai(appContext);
		}else{
			//債権査定帳票・債権明細帳票・債務明細帳票
			SateiSyosaiExcelBss syosai_bss = new SateiSyosaiExcelBss(appContext);
			syosai_bss.execute();
		}
		
		return null;
	}
	
	/**
	 * 【四半期抽出項目表示処理】
	 */
	public Object shihankiHyouji(AppContext appContext) throws Exception {

		//四半期抽出項目表示フラグは、画面遷移しない限り一度表示したらそのまま
		HikiateForm form = (HikiateForm)appContext.getActionForm();
		form.setShihankichushutsukomokuFlg(HYOUJI);
		
	    return form.toString();
	}
	
	/**
	 * 【画面初期表示処理】
	 */
	public Object appExecute(AppContext appContext) throws Exception {

		//OC1103_査定_取引先区分にて債権区分が変更された場合を想定し
		//常にアクションフォームを生成
		HikiateForm form = new HikiateForm();
        appContext.setActionForm(form);

        //以下初期表示処理
        HikiateBss bss = new HikiateBss(appContext);
        bss.execInit(appContext);
        
        //sessionスコープにActionFormを登録
        appContext.setSessionActionForm(HIKIATEFORM,form);

	    return form.toString();
	}
	
	/**
	 * 【未使用メソッド】
	 */
	public Object prevX(AppContext appContext) throws Exception {
		return appContext.getActionForm().toString();
	}
	public Object nextY(AppContext appContext) throws Exception {
		return appContext.getActionForm().toString();
	}

	/**
	 * 【登録ボタン処理】
	 */
	public Object register(AppContext appContext) throws Exception {
		
		HikiateBss bss = new HikiateBss(appContext);

		//入力チェック
		if(!bss.check(SHONIN_TRUE)){

			return appContext.getActionForm().toString();
		}

		//登録処理
		bss.execRegister();

		//OC1101_査定_対象先一覧に遷移
		IchiranAction acc = new IchiranAction();
		acc.appReExecute(appContext);
		
		return GS.OC1101;
	}
	
	/**
	 * 【先頭へボタン処理】
	 */
	public Object toHead(AppContext appContext) throws Exception {

		HikiateBss bss = new HikiateBss(appContext);

		//入力チェック
		if(!bss.check(SHONIN_FALSE)){
			return appContext.getActionForm().toString();
		}
		
		//保存処理(入力履歴未登録)
		bss.execHozon(HOZON_FALSE);

		//OC1102_査定_取引先概要に遷移
		TorokuAction acc = new TorokuAction();
		acc.appExecute(appContext);

		return GS.OC1102;
	}

	/**
	 * 【転送ボタン処理】
	 */
	public Object forward(AppContext appContext) throws Exception {

		HikiateBss bss = new HikiateBss(appContext);

		//入力チェック
		if(!bss.check(SHONIN_FALSE)){
			return appContext.getActionForm().toString();
		}

		//保存処理(入力履歴未登録)
		bss.execHozon(HOZON_FALSE);
		
		//遷移元画面IDを設定
		appContext.getCMN().setReturn_gamenId(appContext.getActionForm().toString());
		
		//OZ3101_転送先選択に遷移
		TensouAction acc = new TensouAction();
		acc.appExecute(appContext);

		return GS.OZ3101;
	}
	
	/**
	 * 【差戻ボタン処理】
	 */
	public Object sendBack(AppContext appContext) throws Exception {

		HikiateBss bss = new HikiateBss(appContext);

		//入力チェック
		if(!bss.check(SHONIN_FALSE)){
			return appContext.getActionForm().toString();
		}

		//保存処理(入力履歴未登録)
		bss.execHozon(HOZON_FALSE);

		//遷移元画面IDを設定
		appContext.getCMN().setReturn_gamenId(appContext.getActionForm().toString());
		
		//OZ2101_差戻先選択に遷移
		SashimodoshiAction acc = new SashimodoshiAction();
		acc.appExecute(appContext);

		return GS.OZ2101;
	}
	
	/**
	 * 【債権明細ボタン処理】
	 */
	public Object claimDetail(AppContext appContext) throws Exception {

		HikiateBss bss = new HikiateBss(appContext);

		//入力チェック
		if(!bss.check(SHONIN_FALSE)){
			return appContext.getActionForm().toString();
		}

		//保存処理(入力履歴未登録)
		bss.execHozon(HOZON_FALSE);

		//遷移元画面IDを設定
		appContext.getCMN().setReturn_gamenId(appContext.getActionForm().toString());
		
		//OS6102_査定内容詳細に遷移
		SateisyosaiAction acc = new SateisyosaiAction();
		acc.appExecute(appContext);

		return GS.OS6102;
	}
	
	/**
	 * 【添付選択ボタン処理】
	 */
	public Object appendSelection(AppContext appContext) throws Exception {

		HikiateBss bss = new HikiateBss(appContext);

		//入力チェック
		if(!bss.check(SHONIN_FALSE)){
			return appContext.getActionForm().toString();
		}

		//保存処理(入力履歴未登録)
		bss.execHozon(HOZON_FALSE);

		//遷移元画面IDを設定
		appContext.getCMN().setReturn_gamenId(appContext.getActionForm().toString());
		
		//添付選択に遷移
		TenpuSentakuAction acc = new TenpuSentakuAction();
		acc.appExecute(appContext);

		return GS.OZ1101;
	}
	
	/**
	 * 【留保債務ボタン処理】
	 */
	public Object reserveDebt(AppContext appContext) throws Exception {

		HikiateBss bss = new HikiateBss(appContext);

		//入力チェック
		if(!bss.check(SHONIN_FALSE)){
			return appContext.getActionForm().toString();
		}

		//保存処理(入力履歴未登録)
		bss.execHozon(HOZON_FALSE);

		//遷移元画面IDを設定
		appContext.getCMN().setReturn_gamenId(appContext.getActionForm().toString());
		
		//OC1105_査定_留保債務登録に遷移
		RyuhosaimuAction acc = new RyuhosaimuAction();
		acc.appExecute(appContext);

		return GS.OC1105;
	}
	
	/**
	 * 【一次保存ボタン処理】
	 */
	public Object firstPreserve(AppContext appContext) throws Exception {

		HikiateBss bss = new HikiateBss(appContext);

		//入力チェック
		if(!bss.check(SHONIN_FALSE)){
			return appContext.getActionForm().toString();
		}

		//保存処理(入力履歴登録)
		bss.execHozon(HOZON_TRUE);

	    return appContext.getActionForm().toString();
	}


	//以下1.5次版帳票処理↓
	/**
	 * 【ダウンロードボタン処理】
	 */
	private void downloadKokunai(AppContext appContext) throws Exception {
		
		SessionDataZen cmnData = appContext.getCMNZen();
		cmnData.setReturnId(GS.SATEI_HIKIATE);
		cmnData.setComLangMode(((AppPagerActionForm)appContext.getActionForm()).getLangMode());
		SateiSyosaiExcel satei = new SateiSyosaiExcel(appContext);
		satei.execute();
		
	}
}