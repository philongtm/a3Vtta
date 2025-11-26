/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
0001	09/05/15		SSC				1.5次版機能組込
******************************************************************************/
package app.common.bss;

import app.SessionDataZen;
import app.common.dbAcc.TenpuSentakuDbAcc;
import app.common.form.TenpuSentakuForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.struts.AppDownloadAction;
import common.util.Ftp;
import common.util.Function;
import common.util.Log;
import common.util.TempFile;
import common.util.TempFileList;
import common.struts.adapter.action.ActionMapping;
import common.struts.adapter.upload.FormFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 共通_添付選択 ビジネスロジッククラス
 */
public class TenpuSentakuBss {
	private String CLASSNAME = getClass().getName(); // クラス名
	
	private AppContext appContext = null;	// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;		// ＤＢアクセス
	private Log log = null;					// LOG
	private TempFile tempFile;
	//課題No.09
	//追加開始
	//ハッシュマップキー
	private final String FILE_NM			= "file_nm";
	private final String TENPU_CHECK		= "tenpuCheck";
	private final String CHK_BOX_ON		= "on";
	private final String TENPU_CHECK_DIS	= "tenpuCheckDis";
	private final String SAKUJO_CHECK_DIS	= "sakujoCheckDis";
	//文書一覧表示用
	private final String TRUE				= "disabled";
	//追加完了
	/**
	 * コンストラクタ
	 */
	public TenpuSentakuBss(AppContext appContext) {
		this.appContext = appContext;		
		this.log = appContext.getLog();
	}
	
	/**
	 * 対象先検索を行う。
	 */
	public String execute() throws Exception {

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
		// DBから画面表示する値を取得し、セッションに格納
		TenpuSentakuDbAcc dbacc = new TenpuSentakuDbAcc(sqlExec, log, appContext);
		dbacc.execute();
		
		// コネクションの開放
		appContext.destroy();		
		
		return GS.OZ1101;
	}
	
	/**
	 * 一時ファイル作成、登録。
	 * @param formFile
	 * @throws Exception
	 */
	public void uploadFile(FormFile formFile) throws Exception{
		if(formFile == null || formFile.getFileName() != null && formFile.getFileName().length() == 0){
			// 登録ファイルパスが入力されていないため、処理は行わない。
			return;
		}
		// 障害対応200808150001　2008/8/15　中島　アップロードファイル名が51バイト以上の場合、エラーとする。
		if(formFile.getFileName().getBytes().length > 50){
    		appContext.setMsgCode("err.0123");
			//No554, 2008/06/06, SJA遠藤, エラー時のフォーカス制御追加
			appContext.setFocusField("fileUp");
    		return;
		}
		
		
		// 一時ファイルの作成
		tempFile = new TempFile(formFile.getFileName());

		BufferedInputStream  is = null;
		OutputStream os = null;
		
		int length;
		int contents = 0;

        try {
			is = new BufferedInputStream (formFile.getInputStream());
        	os = new FileOutputStream(tempFile.getPath());
        	
			///////////////////////////////////////
			//障害票No354
			//チェックイン日 2008/05/14
			//対応者 上地
			//修正概要 ファイル存在チェック追加
			///////////////////////////////////////
			///////////////////////////////////////
			//障害票No354
			//チェックイン日 2008/05/15
			//対応者 SJA渡辺
			//修正概要 ファイルがある場合、ファイル存在チェックに使用した1バイトを書き込むよう修正
			///////////////////////////////////////
        	if((contents = is.read()) == -1) {
        		appContext.setMsgCode("warning.0009");
				//No554, 2008/06/06, SJA遠藤, エラー時のフォーカス制御追加
				appContext.setFocusField("fileUp");
        		return;
        	} else {
        		os.write(contents);
        	}
			while ((contents = is.read()) != -1){
				os.write(contents);
			}
        	if(is!=null) {
        		is.close();
        		is = null;
        	}
        	if(os!=null) {
        		os.close();
        		os = null;
        	}
    	}catch(Exception e){
			log.write(GS.LOG_INF,CLASSNAME,"end - " + e.getMessage());
			throw e;
        } finally {
        	try{
            	if(is!=null) {
            		is.close();
            		is = null;
            	}
        	}finally{
            	if(os!=null) {
            		os.close();
            		os = null;
            	}
        	}
        }
		// DBから画面表示する値を取得し、セッションに格納
		this.sqlExec = appContext.getSqlExecuter();
		TenpuSentakuDbAcc dbacc = new TenpuSentakuDbAcc(sqlExec, log, appContext);

		dbacc.setHoyubunsyo(formFile,tempFile);
	    //課題No.09
	    //追加開始
		dbacc.execute();
		TenpuSentakuForm form = (TenpuSentakuForm)appContext.getActionForm();
		HashMap map = (HashMap)form.getList().get(0);
		map.put(TENPU_CHECK,CHK_BOX_ON);
	    //追加完了
	}
	
	/**
	 * 登録、削除処理。
	 * 登録：FTPを使用し、ファイルサーバへアップロード登録する。
	 * 削除：テーブルからファイル情報を削除
	 * @param list
	 * @throws Exception
	 */
	public void tourokuFile(ArrayList list) throws Exception{	
		
		this.sqlExec = appContext.getSqlExecuter();
		TenpuSentakuDbAcc dbacc = new TenpuSentakuDbAcc(sqlExec, log, appContext);
		SessionDataZen cmnData = appContext.getCMNZen();
		
		Iterator itr = list.iterator();
		TempFileList tmpFileList = new TempFileList();
		ArrayList uploadFiles=new ArrayList();
		Ftp ftp = new Ftp();
		
		while(itr.hasNext()){
			HashMap map = (HashMap)itr.next();
			
			boolean tenpu = map.get("tenpuCheck").equals("on");
			boolean sakujo = map.get("sakujoCheck").equals("on");
			//課題No.09
			//追加開始
			//チェックボックス使用不可の場合(他明細)、処理スキップ
			boolean tenpu_dis = ((String)map.get(TENPU_CHECK_DIS)).equals(TRUE);
			boolean sakujo_dis = ((String)map.get(SAKUJO_CHECK_DIS)).equals(TRUE);
			//追加完了
			
			///////////////////////////////////////
			//障害票No354
			//チェックイン日 2008/05/28
			//対応者 SJA中島
			//修正概要 チェックボックスのチェック有無による処理の条件を変更。
			///////////////////////////////////////
			if(tenpu){
				// 登録済みかチェックする。
				boolean touroku = dbacc.checkBunsyoTemp(map);
				
				// 一次査定以降のファイル添付時のみ、滞留判定・滞留判定検証で追加されたファイルを登録されているものとして扱う
				if (Function.getValueOfInt(cmnData.getPhase())>=40) {
					if ("滞留判定".equals(map.get("phase")) || "滞留判定検証".equals(map.get("phase"))) {
						touroku = false;
					}
				}
				
				if(touroku){
//				if(dbacc.checkBunsyoTemp(map)){
					
					// 登録されていないならば、文章添付テーブルに登録。
					dbacc.setBunsyotenpu(map);
					// FTPでファイル転送を行うリストに登録。
					// 初めて添付する場合のみ、FTPでファイルを登録する。
					// (ファイル登録した場合のみ。添付解除→再添付時にはアップロードしない)
				    //課題No.09
				    //削除開始
					/*if(map.get("tmp_file") != null){
						//tmpFileList.add((TempFile)map.get("tmp_file"));
						map.put("touroku_flg","1");
					}*/
				    //削除完了
				}

				// 管理票No200808130002, SJA渡辺, 2008/08/13, OKボタン押下時にファイルサーバーにファイルを登録するように修正
				// ファイルサーバへ登録。
				/*if(tmpFileList.size()!=0){
					ftp.Put(tmpFileList);
				}*/
				
				tmpFileList.deleteAll();
				
			}else if(sakujo){
				// 削除チェックボックスにチェックされているため、テーブルからファイル情報を削除する。
				// ただし、ファイルサーバのファイルは削除しない。
				if(dbacc.deleteHoyubunsyo(map)){
					dbacc.deleteBunsyotenpu2(map);
					itr.remove();
				}else{
					dbacc.deleteBunsyotenpu(map);
				    //課題No.09
				    //追加開始
					appContext.setMsgCd(GL.ERR_CHK_BUNSYO,(String)map.get(FILE_NM));
				    //追加完了
				}
			//課題No.09
			//修正開始
			//チェックボックス使用不可の場合(他明細)、処理スキップ
			//}else{
			}else if(!(tenpu_dis && sakujo_dis)){
			//修正完了
				// 添付チェックボックスにチェックされていないため、添付文書の解除を行う。
				dbacc.deleteBunsyotenpu(map);
			}
		}
	}
	
	/**
	 * ファイルのダウンロード処理
	 * @throws Exception
	 */
	public void downloadFile() throws Exception{
		Ftp ftp = new Ftp();
		AppDownloadAction appDownload = new AppDownloadAction();
		
		TenpuSentakuForm form = (TenpuSentakuForm)appContext.getActionForm();
		int i = Function.getValueOfInt(appContext.getRequest().getParameter("index"));
		HashMap map = (HashMap)form.getList().get(i);
		String remoteFile = (String)map.get("jitu_file_nm");
		String localFile = (String)map.get("file_nm");
		
		HttpServletRequest req = appContext.getRequest();
		HttpServletResponse res = appContext.getResponse();
		
		// 管理票No200808120001, 2008/08/12, SJA渡辺, ファイルサーバーにファイルが無い場合、エラーメッセージを表示するように修正
		try {
			
			TempFile tempFile = ftp.Get(remoteFile,localFile);
			req.setAttribute(GS.DOWNLOADCONTEXT,tempFile);
			
			appDownload.execute(new ActionMapping(),form,req,res);
			
			tempFile.delete();
			
		} catch(Exception e) {
			appContext.setMsgCode("err.0122");
		}
		
	}
	
}
