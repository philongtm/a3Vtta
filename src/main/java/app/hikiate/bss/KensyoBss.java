/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/05/18		SSC				1.5次版機能組込 
******************************************************************************/
package app.hikiate.bss;

import app.SessionDataZen;
import app.hikiate.dbAcc.KensyoDbAcc;
import app.hikiate.form.KensyoForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.sql.SQLException;


/**
 * 引当金検証画面ビジネスロジッククラス
 */
public class KensyoBss {

	private String CLASSNAME = getClass().getName(); // クラス名
	
	private AppContext appContext = null;	// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;		// ＤＢアクセス
	private Log log = null;					// LOG

	private SessionDataZen cmnData;	// 共通セッション
	private KensyoForm form;

	/**
	 * コンストラクタ
	 */
	public KensyoBss(AppContext appContext) throws SQLException {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		cmnData = appContext.getCMNZen();
		form = (KensyoForm)appContext.getActionForm();
	}

	/**
	 * 対象先検索
	 */
	public String execute() throws Exception {

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
		// DBから画面表示する値を取得し、セッションに格納
		KensyoDbAcc dbacc = new KensyoDbAcc(sqlExec, log, appContext);
		dbacc.execute();
		
		calcSaikenIppan();
		calcSaikenZandaka();
		calcRyuhosaimu();
		calcHikiateTaisyo();
		calcFinalSaikenIppan();
		calcFinalSaikenZandaka();
		calcFinalRyuhosaimu();
		calcFinalZandaka();
		calcFinalKingakuAfterHosei();
		calcFinalZnadakaAfterHosei();
		
		return GS.OD1105;
	}
	
	/**
	 * 一時保存
	 */
	// 障害表：540　チェックイン日：2008/6/3　SJA中島　一次保存の入力履歴は、一次保存ボタン押下時のみとする。
	public boolean firstPreserveExecute(boolean isNyuryokuFlg) throws Exception {
		
	    // 処理結果フラグ
	    boolean result = false;

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
		// DBから画面表示する値を取得し、セッションに格納
		KensyoDbAcc dbacc = new KensyoDbAcc(sqlExec, log, appContext);
		result = dbacc.firstPreserveExecute(isNyuryokuFlg);
		
		return result;
	}
	
	/**
	 * 登録
	 */
	public boolean registerExecute() throws Exception {
		
	    // 処理結果フラグ
	    boolean result = false;

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
		// DBから画面表示する値を取得し、セッションに格納
		KensyoDbAcc dbacc = new KensyoDbAcc(sqlExec, log, appContext);
		result = dbacc.registerExecute();
		
		return result;
	}
	
	/**
	 * もぎ取り解除
	 */
	public boolean releaseExecute() throws Exception {
		
	    // 処理結果フラグ
	    boolean result = false;

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
		KensyoDbAcc dbacc = new KensyoDbAcc(sqlExec, log, appContext);
		result = dbacc.releaseExecute();
		
		return result;
	}
	
	/**
	 * 一般債権計
	 */
	public void calcSaikenIppan() {
		double kingaku1 = Function.getValueOfDouble(Function.removeComma(form.getKingaku01()));
		double kingaku2 = Function.getValueOfDouble(Function.removeComma(form.getKingaku02()));
		double kingaku3 = Function.getValueOfDouble(Function.removeComma(form.getKingaku03()));
		double kingaku4 = Function.getValueOfDouble(Function.removeComma(form.getKingaku04()));
		double kingaku5 = Function.getValueOfDouble(Function.removeComma(form.getKingaku05()));
		double kingaku6 = Function.getValueOfDouble(Function.removeComma(form.getKingaku06()));
		double kingaku7 = Function.getValueOfDouble(Function.removeComma(form.getKingaku07()));
		double kingaku8 = Function.getValueOfDouble(Function.removeComma(form.getKingaku08()));
		double kingaku9 = Function.getValueOfDouble(Function.removeComma(form.getKingaku09()));
		double kingaku10 = Function.getValueOfDouble(Function.removeComma(form.getKingaku10()));
		double kingaku11 = Function.getValueOfDouble(Function.removeComma(form.getKingaku11()));
		double kingaku12 = Function.getValueOfDouble(Function.removeComma(form.getKingaku12()));
		
		double total = kingaku1 + kingaku2 + kingaku3 + kingaku4 + kingaku5 + kingaku6 + kingaku7 + kingaku8 + kingaku9 + kingaku10 + kingaku11 + kingaku12;
		
		form.setTotalSaikenIppan(Function.format("##,###,###,###,###,##0.##",total));
	}
	
	/**
	 * 最終月 一般債権計
	 */
	public void calcFinalSaikenIppan() {
		double kingaku1 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku01()));
		double kingaku2 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku02()));
		double kingaku3 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku03()));
		double kingaku4 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku04()));
		double kingaku5 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku05()));
		double kingaku6 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku06()));
		double kingaku7 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku07()));
		double kingaku8 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku08()));
		double kingaku9 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku09()));
		double kingaku10 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku10()));
		double kingaku11 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku11()));
		double kingaku12 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku12()));
		
		double total = kingaku1 + kingaku2 + kingaku3 + kingaku4 + kingaku5 + kingaku6 + kingaku7 + kingaku8 + kingaku9 + kingaku10 + kingaku11 + kingaku12;
		
		form.setTotalFinalSaikenIppan(Function.format("##,###,###,###,###,##0.##",total));
	}
	
	/**
	 * 債権残高合計
	 */
	public void calcSaikenZandaka() {
		double totalSaikenIppan = Function.getValueOfDouble(Function.removeComma(form.getTotalSaikenIppan()));
		double kingaku16 = Function.getValueOfDouble(Function.removeComma(form.getKingaku16()));
		
		double total = totalSaikenIppan + kingaku16;
		
		form.setTotalSaikenZandaka(Function.format("##,###,###,###,###,##0.##",total));
	}
	
	/**
	 * 最終月 債権残高合計
	 */
	public void calcFinalSaikenZandaka() {
		double totalSaikenIppan = Function.getValueOfDouble(Function.removeComma(form.getTotalFinalSaikenIppan()));
		double kingaku16 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku16()));
		
		double total = totalSaikenIppan + kingaku16;
		
		form.setTotalFinalSaikenZandaka(Function.format("##,###,###,###,###,##0.##",total));
	}
	
	/**
	 * 引当控除後残高
	 */
	public void calcFinalZandaka() {
		double totalSaikenZandaka = Function.getValueOfDouble(Function.removeComma(form.getTotalFinalSaikenZandaka()));
		double totalRyuhosaimu = Function.getValueOfDouble(Function.removeComma(form.getTotalFinalRyuhosaimu()));
		double hozen = Function.getValueOfDouble(Function.removeComma(form.getFinalHozen()));
		double kingakuSonota = Function.getValueOfDouble(Function.removeComma(form.getFinalKingakuSonota()));
		double kingakuKenen = Function.getValueOfDouble(Function.removeComma(form.getFinalKingakuKenen()));
		double kingaku15 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku15()));
		
		double total = totalSaikenZandaka - (totalRyuhosaimu + hozen + kingakuSonota) + kingakuKenen - kingaku15;
		
		form.setFinalZandaka(Function.format("##,###,###,###,###,##0.##",total));
	}
	
	/**
	 * 留保債務計
	 */
	public void calcRyuhosaimu() {
		double ryuhosaimu = Function.getValueOfDouble(Function.removeComma(form.getRyuhosaimu()));
		double ryuhosaimu3 = Function.getValueOfDouble(Function.removeComma(form.getRyuhosaimu3()));
		
		double total = ryuhosaimu + ryuhosaimu3;

		///////////////////////////////////////
		//障害票：803
		//チェックイン日：2008/6/9
		//対応者：中島
		//概要：留保債務計が債権残高合計より大きい場合で、かつ債権残高がマイナスの場合0をセットする。
		////////////////////////////////////////
		double totalSaikenZandaka = Function.getValueOfDouble(Function.removeComma(form.getTotalSaikenZandaka()));
		// 債権残高合計より大きな額か判定
		if (total > totalSaikenZandaka) {
			if(totalSaikenZandaka > 0){
				form.setTotalRyuhosaimu(Function.format("##,###,###,###,###,##0.##",totalSaikenZandaka));
			}else{
				form.setTotalRyuhosaimu(Function.format("##,###,###,###,###,##0.##",0));
			}
		} else {
			form.setTotalRyuhosaimu(Function.format("##,###,###,###,###,##0.##",total));
		}
	}
	
	/**
	 * 最終月 留保債務計
	 */
	public void calcFinalRyuhosaimu() {
		double ryuhosaimu = Function.getValueOfDouble(Function.removeComma(form.getFinalRyuhosaimu()));
		double ryuhosaimu3 = Function.getValueOfDouble(Function.removeComma(form.getFinalRyuhosaimu3()));
		
		double total = ryuhosaimu + ryuhosaimu3;

		///////////////////////////////////////
		//障害票：803
		//チェックイン日：2008/6/9
		//対応者：中島
		//概要：留保債務計が債権残高合計より大きい場合で、かつ債権残高がマイナスの場合0をセットする。
		////////////////////////////////////////
		double totalSaikenZandaka = Function.getValueOfDouble(Function.removeComma(form.getTotalFinalSaikenZandaka()));
		// 債権残高合計より大きな額か判定
		if (total > totalSaikenZandaka) {
			if(totalSaikenZandaka > 0){
				form.setTotalFinalRyuhosaimu(Function.format("##,###,###,###,###,##0.##",totalSaikenZandaka));
			}else{
				form.setTotalFinalRyuhosaimu(Function.format("##,###,###,###,###,##0.##",0));
			}
		} else {
			form.setTotalFinalRyuhosaimu(Function.format("##,###,###,###,###,##0.##",total));
		}
	}
	
	/**
	 * 引当対象金額
	 */
	public void calcHikiateTaisyo() {
		double totalSaikenZandaka = Function.getValueOfDouble(Function.removeComma(form.getTotalSaikenZandaka()));
		double totalRyuhosaimu = Function.getValueOfDouble(Function.removeComma(form.getTotalRyuhosaimu()));
		double hozen = Function.getValueOfDouble(Function.removeComma(form.getHozen()));
		double kingakuSonota = Function.getValueOfDouble(Function.removeComma(form.getKingakuSonota()));
		double kingakuKenen = Function.getValueOfDouble(Function.removeComma(form.getKingakuKenen()));
		double kingaku15 = Function.getValueOfDouble(Function.removeComma(form.getKingaku15()));
		
		double total = totalSaikenZandaka - (totalRyuhosaimu + hozen + kingakuSonota) + kingakuKenen - kingaku15;
		
		form.setKingakuHikiateTaisyo(Function.format("##,###,###,###,###,##0.##",total));
	}
	
	/**
	 * 補正後引当金額
	 */
	public void calcFinalKingakuAfterHosei() {
		double finalKingaku15 = Function.getValueOfDouble(Function.removeComma(form.getFinalKingaku15()));
		double finalKingakuHosei = Function.getValueOfDouble(Function.removeComma(form.getFinalKingakuHosei()));
		
		double total = finalKingaku15 + finalKingakuHosei;
		
		form.setFinalKingakuAfterHosei(Function.format("##,###,###,###,###,##0.##",total));
	}
	
	/**
	 * 補正後引当控除後残高
	 */
	public void calcFinalZnadakaAfterHosei() {
		double finalZandaka = Function.getValueOfDouble(Function.removeComma(form.getFinalZandaka()));
		double finalKingakuHosei = Function.getValueOfDouble(Function.removeComma(form.getFinalKingakuHosei()));
		
		double total = finalZandaka - finalKingakuHosei;
		
		form.setFinalZnadakaAfterHosei(Function.format("##,###,###,###,###,##0.##",total));
	}
	
	/**
	 * 金額チェック
	 * @param check
	 * @param kingaku
	 * @param errMsg1 数値以外が入力されたエラーメッセージ
	 * @param errMsg2 桁数を越えたエラーメッセージ
	 * @param errMsg3 整数ではないエラーメッセージ // TODO エラーメッセージに追加せよ
	 * @return
	 */
	public boolean checkKingaku(InputCheck check, String kingaku, String errMsg1, String errMsg2, String errMsg3) {
		
		if (!(check.isNullBlank(kingaku))) {
			// 数値チェック
			if (!(Function.isFloat(kingaku))) {
				appContext.setMsgCode(errMsg1);
				return false;
			}
			// 整数チェック
			int idx = kingaku.indexOf('.');
			if (idx == -1) {
				// 入力lengthチェック
				if (kingaku.length() > 12) {
					appContext.setMsgCode(errMsg2);
					return false;
				}
			} else {
				appContext.setMsgCode(errMsg3);
				return false;
			}
		}
		return true;
	}

}