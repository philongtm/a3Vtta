/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.struts;

import common.AppContext;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.struts.adapter.action.ActionErrors;
import common.struts.adapter.action.ActionForm;
import common.struts.adapter.action.ActionMapping;
import common.struts.adapter.action.ActionMessage;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 拡張アクションフォームクラス
 * 
 */
public abstract class AppActionForm extends ActionForm {

	private ActionErrors errors = new ActionErrors();

	protected AppContext appContext;
	protected String gamenId;
	
	private List list = null;

	/**
	 * @return 画面IDを戻します。
	 */
	public abstract String toString();

	/**
	 * アプリケーション用のvalidate()メソッド
	 * @param mapping
	 * 				アクションマッピングオブジェクト
	 * @param request
	 * 				HTTPリクエストオブジェクト
	 */
	public abstract void appValidate(ActionMapping mapping,
			HttpServletRequest request);

	/**
	 * バリデートマップ取得
	 * @return
	 */
	public List getValidateList() {
		return null;
	}

	/**
	 * コンストラクタ
	 */	
	public AppActionForm() {
	 	
	}

	/**
	 * NULLなら空文字変換、それ以外はトリム
	 * @param String
	 * 				ストリングオブジェクト
	 */	
	public String getVal(String val) {
	 	if(val == null){
	 		val = GS.EMPTY_CHARCTER;
	 	}else{
	 		val = val.trim();
	 	}
	 	return val;
	}

	/**
	 * カスタムreset()メソッド
	 * アプリケーション用のreset()メソッドを呼び出す。
	 * 
	 * @param mapping
	 * 				アクションマッピングオブジェクト
	 * @param request
	 * 				リクエストオブジェクト
	 */
	public final void reset(ActionMapping mapping, ServletRequest request) {
		super.reset(mapping, request);
	}

	/**
	 * カスタムvalidate()メソッド
	 * 共通の前処理を行い、アプリケーション用のexecute()メソッドを呼び出す。
	 * 
	 * @param mapping
	 * 				アクションマッピングオブジェクト
	 * @param request
	 * 				HTTPリクエストオブジェクト
	 * @return ActionErrorsオブジェクト
	 */
	public final ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		// エラーオブジェクトの初期化
		errors.clear();

		// 検証ロジック実行
		boolean exception = false;
		try {
			if( validator() ) {
				appValidate(mapping, request);
			}
		} catch (Exception e) {
			// 例外をスローできないのでアクションクラスに引き継ぐ
			exception = true;
			request.setAttribute(GS.VALIDATEEXCEPTION,e);
		}

		if( !exception ) {
			// エラー有無の判定
			if( request.getAttribute(GS.MESSAGECONTEXT) != null) {
				// エラー有の時、ダミーのエラーオブジェクトを作成
				errors.add("",new ActionMessage("") );
			}
		}
		
		return errors;
	}
	
	/**
	 * 入力内容の自動検証パラメータ作成
	 * @param checkType		チェック種別
	 * @param event			チェック対象イベント(nullは全て) ex. "kensaku,close"
	 * @param symbol		アクションフォームの変数名
	 * @param emsgcd		エラーメッセージコード
	 * @return
	 */
	public String[] validateInfo(int checkType, String event, String symbol, String emsgcd) {
		String[] info = new String[4];
		info[0] = Function.format("0",checkType);
		info[1] = event;
		info[2] = symbol;
		info[3] = emsgcd;
		return info;
	}

	/**
	 * 入力内容の自動検証パラメータ作成
	 * 埋め込み形式のエラーメッセージ
	 * 
	 * @param checkType		チェック種別
	 * @param event			チェック対象イベント(nullは全て) ex. "kensaku,close"
	 * @param symbol		アクションフォームの変数名
	 * @param emsgcd1		エラーメッセージコード１
	 * @param emsgcd2		エラーメッセージコード２
	 * @return
	 */
	public String[] validateInfo(int checkType, String event, String symbol, String emsgcd1, String emsgcd2) {
		String[] info = new String[5];
		info[0] = Function.format("0",checkType);
		info[1] = event;
		info[2] = symbol;
		info[3] = emsgcd1;
		info[4] = emsgcd2;
		return info;
	}
	
	/**
	 * 入力内容の自動検証
	 * @return true/エラー無 false/エラー有
	 * @throws Exception
	 */
	public boolean validator() throws Exception {

		if(list==null) return true;

		String[] info = null;
		boolean error = false;

		InputCheck incpuChk = new InputCheck();
		
		for(int i=0; (i<list.size())&&(!error); i++) {

			info = (String[])list.get(i);
			String type = info[0];
			String event = info[1];
			String symbol = info[2];

			// チェック対象イベントの判定
			if(event!=null) {
				String[] sary = Function.StrSplitToken(event,",");
				String param = appContext.getRequest().getParameter(GS.EVENT);
				boolean skip = true;
				if(param!=null){
					for(int x=0;x<sary.length;x++){
						if(param.equals(sary[x])) {
							skip = false;
							break;
						}
					}
					if(skip) continue;
				}
			}

			// ゲッターで入力内容を取得
			symbol = "get" + symbol.substring(0,1).toUpperCase() + symbol.substring(1);
			Method method = this.getClass().getMethod(symbol,null);
			String value = (String)method.invoke(this,null);

			// 入力なしの場合は検証を行わない
			if( (value==null) || (value.length()==0) ) continue;
			
			// 入力内容を検証
			switch(Function.getValueOfInt(type)) {

				case GS.VALIDATE_KINSHIMOJI:
					if(incpuChk.haveKinshiMoji(value)) {
						error = true;
					}
					break;
				
				case GS.VALIDATE_MAILADDR:
					if(!incpuChk.isMailAddr(value)){
						error = true;
					}
					break;
					
				case GS.VALIDATE_BLANK:
					if(incpuChk.isNullBlank(value)){
						error = true;
					}
					break;
				default:
					break;
			}
		}
		
		if(error) {
			if(info.length==4) {
				appContext.setMsgCode(info[3]);
			} else {
				appContext.setMsgCode(info[3],info[4]);
			}
			return false;
		} else {
			return true;
		}
	}
}