/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2015/03/18		SSC				案件No.BJ201408049 IA化対応時の機能改善 
003		2016/02/02		SSC				案件No.BP201602008 障害対応（もぎ取り依頼メール・転送・差戻）
004		2016/03/24		SSC				案件No.BJ201602002 部門廃止対応（一次）
005		2017/02/21		SSC				案件No.BJ201702058 転送機能の改善対応
******************************************************************************/
package app.common.bss;


import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.common.dbAcc.TensouDbAcc;
import app.common.form.TensouForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

/**
 * OZ3101_転送先選択 ビジネスロジッククラス
 */
public class TensouBss {

	//判定査定区分
	private static final String NYURYOKU_T    = "1";
	private static final String NYURYOKU_S    = "2";
	//フォーカス制御用
	private static final String COMMENT       = "comment";

    private AppContext appContext = null;		// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;		// ＤＢアクセス
	private Log log = null;					// LOG
	private SessionData cmnData;				// 共通セッション
	private UserBean user_bean;				// ユーザ情報
	private TorihikisakiBean tori_bean;		// 取引先情報
	private TensouForm form;					// アクションフォーム
	
	/**
	 * コンストラクタ
	 */
	public TensouBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		this.tori_bean = cmnData.getTori_bean();
		this.form = (TensouForm)appContext.getActionForm();
	}

	/**
	 * 画面初期表示処理
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {	
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();		
		TensouDbAcc dbacc = new TensouDbAcc(sqlExec, log, appContext);
		
		// 入力項目初期化
		initNyuryoku();
		// 汎用1(右)セレクトボックス設定値取得
		dbacc.getHanyo1();
		 // 汎用2セレクトボックスの設定値取得
        dbacc.getHanyo2();
        // 査定会社別担当者一覧取得
		if(GS.GSS.equals(tori_bean.getSystem_kbn()) && 
		   GS.SATEIKAISYA_SJ.equals(tori_bean.getSateikaisya_cd())){
    		// 担当者一覧取得（本部）
    		dbacc.getTantoIchiran2();
        } else {
    		// 担当者一覧取得
    		dbacc.getTantoIchiran();
        }
		// 汎用１～３表示チェック
		this.chkHanyouHyouji();
	}

	/**
	 * 汎用１セレクトボックス処理
     * 
	 * @throws Exception
	 */
	public void hanyo1() throws Exception {	
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();		
		TensouDbAcc dbacc = new TensouDbAcc(sqlExec, log, appContext);
		 // 汎用2セレクトボックスの設定値を取得する
        dbacc.getHanyo2();
		// 担当者初期化
		initTanto();
        // 査定会社別担当者一覧取得
		if(GS.GSS.equals(tori_bean.getSystem_kbn()) && 
		   GS.SATEIKAISYA_SJ.equals(tori_bean.getSateikaisya_cd())){
    		// 担当者一覧再取得（本部）
    		dbacc.getTantoIchiran2();
        } else {
    		// 担当者一覧再取得
    		dbacc.getTantoIchiran();
        }
	}

	/**
	 * 汎用２セレクトボックス処理
     * 
	 * @throws Exception
	 */
	public void hanyo2() throws Exception {	
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();		
		TensouDbAcc dbacc = new TensouDbAcc(sqlExec, log, appContext);
		// 担当者初期化
		initTanto();
		// 担当者一覧再取得
		dbacc.getTantoIchiran2();
	}

	/**
	 * 入力項目初期化
     * 
	 * @throws Exception
	 */
	public void initNyuryoku() throws Exception {	
		form.setComment(GS.EMPTY_CHARCTER);
		initTanto();
	}

	/**
	 * 担当者初期化
     * 
	 * @throws Exception
	 */
	public void initTanto() throws Exception {	
		form.setInTanto(GS.EMPTY_CHARCTER);
		form.setTxtTanto(GS.EMPTY_CHARCTER);
		form.setSelectedTantoId(GS.EMPTY_CHARCTER);
	}

	/**
	 * 転送処理
     * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean doTensou() throws Exception {
		boolean result = false;
		String phase = tori_bean.getPhase();

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		TensouDbAcc dbacc = new TensouDbAcc(sqlExec, log, appContext);

		//転送コメント入力チェック
		if(!chkComment()){
			return result;
		}
		//担当者選択チェック(システム区分01以外または汎用1～3非表示のみ実施)
		this.chkHanyouHyouji();
		if(!GS.GSS.equals(tori_bean.getSystem_kbn()) || !form.isHanyouHyoujiFlg()){
			if(!chkTanto()){
				return result;
			}
		}
		//進捗テーブル更新
		doUpdSintyoku(phase);
		//T13_入力履歴の登録
		doInsT13(phase);
		//T12_コメントの削除
		dbacc.delT12();
		//T12_コメントの登録
		dbacc.insT12();
		//T04_メール配信の登録
		doInsT04();
		//コミット
		dbacc.commit();
		return true;
	}

	/**
	 * 転送コメント入力チェック
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkComment() throws Exception {
		InputCheck check = new InputCheck();
		String comment = form.getComment();
		String kinshiChar = GS.EMPTY_CHARCTER;

    	// コメントが2000byteを超える場合エラー
		if(!(check.islength(comment,2000))){
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OZ3101_COMMENT);
			appContext.setFocusField(COMMENT);
			return false;
		}
	    // 入力禁止文字が含まれている場合エラー
		for (int i = 0; i < comment.length(); i++) {
			kinshiChar = comment.substring(i,i + 1);
			if(check.haveKinshiMoji(kinshiChar)) {
				//チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示
				appContext.setMsgCd(GL.ERR_PROHIBITTED,kinshiChar);
				return false;
			}
		}
		return true;
	}

	/**
	 * 担当者選択チェック
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkTanto() throws Exception {
		InputCheck check = new InputCheck();
		if(check.isNullBlank(form.getTxtTanto())){
			appContext.setMsgCode(GL.ERR_SELECT,GL.OZ3101_TANTO);
			return false;
		}
		return true;
	}	

	/**
	 * 進捗テーブルの更新
	 * 
     * @param String
	 * @throws Exception
	 */
	public void doUpdSintyoku(String phase) throws Exception {

		if(!form.isHanyouHyoujiFlg()){
			// 汎用1～3表示フラグがfalse（非表示）の場合、
			// 案件の分類3、部コードをセットする（変更なしと同じ扱い）
			form.setHanyou1(Function.trim(tori_bean.getBunrui2()));
			form.setHanyou2(Function.trim(tori_bean.getBu_cd()));
		}

		InputCheck check = new InputCheck();
		String status = GS.STATUS_MISYORI;
		String hoji_user = null;
		String bunrui2 = form.getHanyou1();			// 画面の部門コード
		String bu_cd = null;
		String upd_user = user_bean.getComUserId();
		form.setBu_cd_upd_flg(true);

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		TensouDbAcc dbacc = new TensouDbAcc(sqlExec, log, appContext);

		//画面に部コードが入力されている場合（査定会社SJ）
		if(!check.isNullBlank(form.getHanyou2())){
			bu_cd = form.getHanyou2();
			//担当者が入力されている場合
			if(!check.isNullBlank(form.getTxtTanto())){
				status = GS.STATUS_SYORICHU;
				hoji_user = form.getSelectedTantoId();
			}
		//画面に部コードが入力されていない場合（査定会社SJ以外）
		}else{
			form.setBu_cd_upd_flg(false);
			// システム区分01、査定会社がPNの時
			if (GS.GSS.equals(tori_bean.getSystem_kbn())
					&& GS.SATEIKAISYA_PN.equals(tori_bean.getSateikaisya_cd())){
				// 案件の部門コードと画面の部門コードが異なる場合
				if(!Function.trim(tori_bean.getBunrui2()).equals(bunrui2)){
					form.setBu_cd_upd_flg(true); // 部コードをnullで更新
				}
				//担当者が入力されている場合
				if(!check.isNullBlank(form.getTxtTanto())){
					status = GS.STATUS_SYORICHU;
					hoji_user = form.getSelectedTantoId();
				}
			// 海外の時（担当者必須、部の更新なし）
			}else{
				status = GS.STATUS_SYORICHU;
				hoji_user = form.getSelectedTantoId();
			}
		}
		if(!check.isNullBlank(user_bean.getComDaiko_userId())){
			upd_user = user_bean.getComDaiko_userId();
        }

		if(GS.PHASE_TAIRYU_HANTEI.equals(phase) || GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(phase)){
			//T08_滞留判定進捗管理の更新
			dbacc.updT08(status,hoji_user,bu_cd,upd_user);
		}else{
			//T14_査定進捗管理の更新
			dbacc.updT14(status,hoji_user,bu_cd,upd_user);
		}
	}

	/**
	 * T13_入力履歴の登録
	 * 
     * @param String
	 * @throws Exception
	 */
	public void doInsT13(String phase) throws Exception {
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		TensouDbAcc dbacc = new TensouDbAcc(sqlExec, log, appContext);

		String nyuryoku_kbn;
		if(GS.PHASE_TAIRYU_HANTEI.equals(phase) || GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(phase)){
			nyuryoku_kbn = NYURYOKU_T;
		}else{
			nyuryoku_kbn = NYURYOKU_S;
		}
		//入力履歴登録
		dbacc.insT13(nyuryoku_kbn);
	}

	/**
	 * T04_メール配信の登録
	 * 
	 * @throws Exception
	 */
	public void doInsT04() throws Exception {
		InputCheck check = new InputCheck();
		String tanto = null;
		String upd_user = user_bean.getComUserId();

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		TensouDbAcc dbacc = new TensouDbAcc(sqlExec, log, appContext);

		if(!check.isNullBlank(form.getTxtTanto())){
			tanto = form.getSelectedTantoId();
		}
		if(!check.isNullBlank(user_bean.getComDaiko_userId())){
			upd_user = user_bean.getComDaiko_userId();
        }
		//メール配信登録
		dbacc.insT04(tanto,upd_user);
	}
	
	/**
	 * 汎用１～３表示チェック。<br>
	 * 取引先情報のシステム区分・フェーズより判定する。<br>
	 * 判定結果は汎用１～３表示フラグにtrue/falseで設定する。<br>
	 * 
	 * @throws Exception
	 */
	public void chkHanyouHyouji() throws Exception {

		String sysKbn = tori_bean.getSystem_kbn();

		if(GS.GSS.equals(sysKbn)){
			String phase = tori_bean.getPhase();

			if(GS.PHASE_ICHIJI_SATEI.equals(phase)){
				// システム区分：01、フェーズ：一次査定の場合
				form.setHanyouHyoujiFlg(true);
			}else{
				// システム区分：01、フェーズ：一次査定以外の場合
				form.setHanyouHyoujiFlg(false);
			}
		}else{
			// システム区分：01以外の場合
			form.setHanyouHyoujiFlg(true);
		}
	}
}