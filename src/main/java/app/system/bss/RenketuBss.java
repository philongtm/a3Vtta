/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package app.system.bss;

import app.SessionDataZen;
import app.system.dbAcc.RenketuDbAcc;
import app.system.form.RenketuForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.struts.AppDownloadAction;
import common.util.InputCheck;
import common.util.Log;
import common.util.TempFile;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import common.struts.adapter.action.ActionMapping;
import common.struts.adapter.upload.FormFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class RenketuBss {
	private String CLASSNAME = getClass().getName(); // クラス名
	
	private AppContext appContext = null;	// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;	// ＤＢアクセス
	private Log log = null;				// LOG

	private SessionDataZen cmnData;	// 共通セッション
	private RenketuForm form;		// ActionForm
	
	// 一時ファイル作成時のフォーマット
	private  SimpleDateFormat sdfYMD = null;
	// チェッククラスのインスタンス
	private InputCheck inpChk = null;
	
	// 定数
	// 作成ファイル名
	private final String FILE_NAME = "GroupDivMST.xls";
	// 作成ファイルシート名
	private final String SHEET_NAME = "Sheet1";
	
	/**
	 * コンストラクタ
	 */
	public RenketuBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMNZen();
		this.form = (RenketuForm)appContext.getActionForm();
		
		// ユーザ保持権限チェック
//		checkUserType();
	}

	/**
	 * 選択されたファイルの内容を登録
	 * @param formFile
	 * @throws Exception
	 */
	public void uploadFile(FormFile formFile) throws Exception{

		String filename = formFile.getFileName();
		
        // エラーフラグ
        boolean errFlg = GS.A20_TRUE;
        // エラーメッセージ
        String errMsg = null;
        // エラーメッセージ用取引先コード
        String errKikanCode = null;
        // 共通チェックのインスタンス
        inpChk = new InputCheck();
        
		// 登録ファイルが入力されていない場合
		if (filename.length() < 1 ){
			appContext.setMsgCode("warning.0002");
        	return;
		}
		
		// 拡張子が".xls"か".XLS"ではない場合 
		if ( !(filename.endsWith(GS.DOTXLS) || filename.endsWith( GS.DOTXLS.toUpperCase() )) ) {
        	// メッセージコードをセット
        	appContext.setMsgCode("warning.0001");
        	return;
		}
		
		// 一時ファイルを作成する
		TempFile tmpExcel = new TempFile(filename);
		
		// 作成した一時ファイルにデータを書き込む
		BufferedInputStream  is = null;
		OutputStream os = null;
		
		int contents = 0;

        try {
			is = new BufferedInputStream (formFile.getInputStream());
        	os = new FileOutputStream(tmpExcel.getPath());
        	
			while ((contents = is.read()) != -1){
				os.write(contents);
 			}
       		os.flush();
			
        } finally {
        	if(is!=null) {
        		is.close();
        		is = null;
        	}
        	if(os!=null) {
        		os.close();
        		os = null;
        	}
        }

        // 入力されたデータのリスト
        ArrayList data = new ArrayList();
        
        // 重複チェック用の入力データマップ
        HashMap dataMap = new HashMap();
        
        
        FileInputStream fis = null;
        POIFSFileSystem filein = null;
        
        // サイズが0の場合、ファイルが存在しない旨のエラーをセット
        try {
	        fis = new FileInputStream(tmpExcel.getPath());
	        
	        if ( fis.available() == 0 ) {
	        	errFlg = GS.A20_FALSE;
	        	appContext.setMsgCode("err.0068");
	        } else {
	        	filein = new POIFSFileSystem(fis);
	        }
        } finally {
        	fis.close();
        }
        
        if ( !errFlg ) {
        	return;
        }
        
        HSSFWorkbook wb = new HSSFWorkbook(filein);
        
        // 先頭シートを読み込む
        HSSFSheet sheet = wb.getSheetAt(0);
        
        HSSFRow row = null;
        
        // 先頭1行目は項目名称のため、2行目から取得する
        int i = 1;
        
        // データの取得・データの形式チェックを行う
    	while ( (row = sheet.getRow(i)) != null ){
    		
            String[] rowData = new String[4];
            
            
            //----- 基幹取引先コード(英数5桁) -----
            HSSFCell cell_kikan_cd = row.getCell((short) 0);
            
            rowData[0] = chkKikanCd(cell_kikan_cd);
            
            if ( rowData[0] == null ) {
            	// エラーメッセージ作成
               	errMsg = appContext.getMsg("err.0081");
               	errKikanCode = "";
    			
    			break;
            }
            
            // 取得した文字列が英数字のみかをチェック
            if ( !inpChk.isNumLetter(rowData[0]) ) {
            	// エラーメッセージ作成
               	errMsg = appContext.getMsg("err.0081");
               	errKikanCode = rowData[0];
    			
    			break;
            }
            
            // サイズチェック(5桁)
            if ( rowData[0].length() != 5 ) {
            	// エラーメッセージ作成
               	errMsg = appContext.getMsg("err.0081");
               	errKikanCode = rowData[0];
    			
    			break;
            }
            
            //----- 年月(YYYYMM形式) -----
            HSSFCell cell_ym = row.getCell((short) 1);
            
            rowData[1] = chkYm(cell_ym);
            
            // チェックの結果がnullの場合エラー
            if ( chkYm(cell_ym) == null ) {
               	errMsg = appContext.getMsg("err.0083");
               	errKikanCode = rowData[0];  
               	break;
            }
            
            //----- 連結区分(英数2桁) -----
            HSSFCell cell_renketu_kbn = row.getCell((short) 2);
            
            rowData[2] = chkRenketuKbn(cell_renketu_kbn);
            
            // チェックの結果がnullの場合エラー
            if ( rowData[2] == null ) {
               	errMsg = appContext.getMsg("err.0097");
               	errKikanCode = rowData[0];  
               	break;
            }
            
            //----- 削除フラグ(「1」または「0」) -----
            HSSFCell cell_del_flg = row.getCell((short) 3);
            
            rowData[3] = chkDelFlg(cell_del_flg);
            
            // チェックの結果がnullの場合エラー
            if ( rowData[3] == null ) {
               	errMsg = appContext.getMsg("err.0098");
               	errKikanCode = rowData[0];  
               	break;
            }
                        
            data.add(rowData);
            
            // １件分のデータを連結してユニークなキーとする。
            // 障害票：558　チェックイン日：2008/6/3　SJA中島 チェックする内容を一件分のデータ全て連結するように変更。
            String dataMapKey = "";
			for (int j = 0; j < rowData.length; j++) {
				dataMapKey += rowData[j];
			}
			
			// 同一のデータが既に登録されている場合は
			// 重複データとしてエラーとする。
			if (dataMap.get(dataMapKey) != null) {
				errMsg = appContext.getMsg("err.0110");
               	errKikanCode = rowData[0];  
               	break;
			}
			
            dataMap.put(dataMapKey, rowData);
            
            i++;
        } // while
    	
    	// 重複チェック用のマップをクリアする。
    	dataMap.clear();
    	
    	// ファイル内容を取得後、一時ファイルを削除する
    	tmpExcel.delete();
    	
    	// チェックでエラーが有る場合、メッセージを設定し、処理を終了する
        if (errMsg != null){
        	// エラーメッセージ作成
        	appContext.setMessage( createErrMsg( errMsg, errKikanCode) );
        	
        	return;
        }
        
        
        // 存在チェックを行う
		this.sqlExec = appContext.getSqlExecuter();

		RenketuDbAcc dbacc = new RenketuDbAcc(sqlExec, log, appContext);
		
		// 存在チェック実行
		Iterator it = data.iterator();
		
		while( it.hasNext() ) {
			String[] rowData = new String[4];
			
			rowData = (String[])it.next();
			
			// 基幹取引先コードで件数を取得
			int dataCnt = dbacc.selectKikanToriCd(rowData[0]);
			
			// データが存在しない場合、エラーメッセージを表示する
			if( dataCnt < 1 ) {
	        	appContext.setMessage( createErrMsg( appContext.getMsg("err.0099"), rowData[0]) );
	        	
	    		// コネクションの開放を行う
	    		appContext.destroy();

	        	return;
			}
			
			
			// データの重複をチェック
			if (dbacc.repetitionCheck(rowData)) {
				errMsg = appContext.getMsg("err.0110");
				appContext.setMessage( createErrMsg( errMsg, rowData[0]) );
				
				// コネクションの開放を行う
	    		appContext.destroy();

				return;
			}

		}
		
				
		// ファイルの内容を設定する
		dbacc.insertRenketu(data);
		
		// コネクションの開放を行う
		appContext.destroy();

	}
	
	/**
	 * ファイルのダウンロード処理
	 * @throws Exception
	 */
	public void downloadFile() throws Exception{
        	
		// 連結区分マスタからデータを取得	
		this.sqlExec = appContext.getSqlExecuter();
		RenketuDbAcc dbacc = new RenketuDbAcc(sqlExec, log, appContext);
		
		dbacc.selectDownloadData();
		
		// コネクションの開放を行う
		appContext.destroy();
		
		// 実行結果をFormより取得する
		ArrayList dataList = (ArrayList)form.getAr_meisai();
		
		// エクセルの一時ファイルを作成する
		sdfYMD = new SimpleDateFormat("yyyyMMdd");
		
		String suffix = sdfYMD.format(new Date());
		File dir = new File(AppContext.getTmpDir());
		
		if( dir.isDirectory()==false ) {
			dir.mkdir();
		}
		
		File tmpExcel = File.createTempFile(suffix+".",".xls",dir);
    	dir = null;
        
		// 作成した一時ファイル(エクセル)の内容を書き込む
		// 新規ワークブックを作成する
        HSSFWorkbook wb = new HSSFWorkbook();
        FileOutputStream fileOut = null;
		
        try{
        	fileOut = new FileOutputStream( tmpExcel.getAbsolutePath() );
            
            // 新規ワークシートを作成する
            HSSFSheet sheet1 = wb.createSheet();
            wb.setSheetName(0,SHEET_NAME);    

            // 行オブジェクトの作成（行番号は0スタート）
            HSSFRow row[] = new HSSFRow[dataList.size() + 1];
            
            // セルオブジェクトの作成（セル番号は0スタート）
            // ヘッダ部分の作成(1行目は項目名称)
            row[0] = sheet1.createRow(0);
            
            HSSFCell[] cellHd = new HSSFCell[10];
            
            for (int i=0; i<4; i++ ) {
            	cellHd[i] = row[0].createCell((short)i);
                // cellHd[i].setEncoding(HSSFCell.ENCODING_UTF_16);
            }
            
            // セルに値を設定する
            cellHd[0].setCellValue(appContext.getMsg(GL.LABEL_CSTM_CD));
            cellHd[1].setCellValue(appContext.getMsg(GL.LABEL_YM));
            cellHd[2].setCellValue(appContext.getMsg(GL.LABEL_GROUP_DIV));
            cellHd[3].setCellValue(appContext.getMsg(GL.LABEL_DEL_FLG));
            
            // データ部分の作成(2行目から)
            int j = 1;
            
            Iterator dataIte = dataList.iterator();
    		
            while ( dataIte.hasNext() ) {
	            // j行目の作成
            	row[j] = sheet1.createRow(j);
	            
            	HashMap map = (HashMap)dataIte.next();
	            HSSFCell[] cellData = new HSSFCell[10];
	            
	            for (int i=0; i<4; i++ ) {
	            	cellData[i] = row[j].createCell((short)i);
	            	// cellData[i].setEncoding(HSSFCell.ENCODING_UTF_16);
	            }
	            				
	            cellData[0].setCellValue( (String)map.get("kikan_tori_cd") );	// 基幹取引先コード
	            cellData[1].setCellValue( (String)map.get("ym") );				// 年月
	            cellData[2].setCellValue( (String)map.get("renketsu_kbn") );	// 連結区分
	            cellData[3].setCellValue( (String)map.get("del_flg") );			// 削除フラグ
	            j++;
	            
            }
            //作成したワークブックを保存する
            wb.write(fileOut);
            
        	fileOut.flush();

        } finally {
            fileOut.close();
        }
        
        
        // 作成したエクセルファイルのダウンロード用tmpファイルを作成する
		TempFile tmp = new TempFile(FILE_NAME);
		
		FileInputStream fis = null;
		BufferedInputStream  is = null;
		OutputStream os = null;
		
		int contents = 0;

		// 一時ファイルに、エクセル情報を書き込む
        try {
        	fis = new FileInputStream(tmpExcel.getAbsolutePath());

			is = new BufferedInputStream (fis);
        	os = new FileOutputStream(tmp.getPath());
        	
			while ((contents = is.read()) != -1){
				os.write(contents);
 			}
       		os.flush();
			
        } finally {
        	if (fis!=null) {
            	fis.close();
            	fis = null;
        	}
        	if(is!=null) {
        		is.close();
        		is = null;
        	}
        	if(os!=null) {
        		os.close();
        		os = null;
        	}
        }
        
        // エクセルの一時ファイルを削除
        tmpExcel.delete();
        
        // セッションデータにtmpファイルをセット
	    HttpServletRequest req = appContext.getRequest();
	    HttpServletResponse res = appContext.getResponse();
	    
	    // リクエストスコープのデータを登録
	    req.setAttribute(GS.DOWNLOADCONTEXT,tmp);
	    
	    // ダウンロード
	    AppDownloadAction acc = new AppDownloadAction();
	    acc.execute( new ActionMapping(), form, req, res );
	    
	    tmp.delete();

	}
	
	// 【アップロード処理用チェックメソッド】
	/**
	 * 基幹コードチェック
	 * ・未入力はエラー
	 * ・英数字のみ
	 * ・英数字チェックの場合とエラーメッセージが異なるので、桁チェックは別で行う
	 * @param cell_kikan_cd
	 * @return 基幹コード(エラーの場合はnull)
	 */

	private String chkKikanCd (HSSFCell cell_kikan_cd ) {
		String kikan_cd = null;
		
		if ( cell_kikan_cd == null) {
			return kikan_cd;
		}
		
		int type = cell_kikan_cd.getCellType();
        
        if ( type == 0 ) {
        	// 数値のみの場合
        	kikan_cd = ( new Integer( (int)cell_kikan_cd.getNumericCellValue() ) ).toString();
        	
        } else if ( type == 1 ) {
        	// String型の場合
        	kikan_cd = cell_kikan_cd.getStringCellValue();
        } 
        
		return kikan_cd;
	}
	
	/**
	 * 年月のチェック
	 * ・未入力はエラー
	 * ・数値型
	 * ・6桁
	 * ・MMが1～12
	 * @param cell_ym
	 * @return 年月(エラーの場合はnull)
	 */
	private String chkYm( HSSFCell cell_ym ) {
		String ym = null;
		
		if ( cell_ym == null ) {
			return null;
		}
		
		int type = cell_ym.getCellType();
		
		// セルの型チェック
		if ( type == 0 ) {
			// 数値の場合桁数のチェックを行う
            ym = (new Integer( (int)cell_ym.getNumericCellValue()) ).toString();
            
		} else if ( type == 1 ) {
			// 文字型の場合
            ym = cell_ym.getStringCellValue();
            
        } else {
        	// 数値・文字列以外はエラー
        	return null;
        }
		
        // サイズチェック(6桁)
        if ( ym.length() != 6 ) {
        	return null;
        	
        } 
        
        // MM部分のチェック
        int mm = new Integer( ym.substring(4,6) ).intValue();
        
        if ( 12 < mm || mm < 0) {
        	return null;
        }
		
		return ym;
	}
	
	/**
	 * 連結区分チェック
	 * ・未入力の場合""を設定
	 * ・英数字
	 * ・2桁
	 * @param cell_renketu_kbn
	 * @return 連結区分(エラーの場合はnull)
	 */
	private String chkRenketuKbn ( HSSFCell cell_renketu_kbn ) {
		String renketu_kbn = null;
		
		if ( cell_renketu_kbn == null ) {
			return null;
		}
		
		int type = cell_renketu_kbn.getCellType();
		
		// セルの型チェック
		if ( type == 0 ) {
        	// 数値のみの場合
        	renketu_kbn = ( new Integer( (int)cell_renketu_kbn.getNumericCellValue() ) ).toString();
        	
        } else if ( type == 1 ) {
        	// String型の場合
        	renketu_kbn = cell_renketu_kbn.getStringCellValue();
        	
        } else {
        	// 数値かString型以外の場合はエラー
        	return null;
        }
		
		// 英数字チェック
		if ( !inpChk.isNumLetter(renketu_kbn) ) {
			return null;
		}
		
        // サイズチェック(2桁)
		if ( renketu_kbn.length() != 2 ) {
			return null;
		}
        
		// NULL以外をInsertする場合はここで''を追加した形式にしておく
		StringBuffer sb = new StringBuffer();
		
		sb.append("'")
			.append(renketu_kbn)
			.append("'");
		
		return sb.toString();
	}
	
	/**
	 * 削除フラグチェック
	 * ・未入力の場合0を設定
	 * ・数値
	 * ・0か1
	 * @param cell_del_flg
	 * @return 削除フラグ(エラーの場合はnull)
	 */
	private String chkDelFlg (HSSFCell cell_del_flg) {
		String delFlg = null;
		
		if ( cell_del_flg == null ) {
			// 未入力の場合"0"を返す
			return "0";
			
		}
		
		int type = cell_del_flg.getCellType();
		
		if ( type == 0 ) {
			// String型で取得
        	delFlg = new Integer ((int)cell_del_flg.getNumericCellValue() ).toString();
        	
		} else if ( type == 1 ) {
			delFlg = cell_del_flg.getStringCellValue();
			
        } else {
        	return null;
        }
		
		// 数値の場合0か1かをチェック
		if ( !("1".equals(delFlg)) && !("0".equals(delFlg)) ){
    		delFlg = null;
    	}

		
        return delFlg;
	}
	
	/**
	 * エラーメッセージを生成する
	 * @param errMsg
	 * @param errKikanCode
	 * @return エラーメッセージ
	 */
	private String createErrMsg (String errMsg, String errKikanCode) {
    	StringBuffer sb = new StringBuffer();
 		
		sb.append(errMsg);
		
		if ( !("").equals(errKikanCode) ) {
			// 基幹取引先コードが設定されている場合
			sb.append("(")
				.append(errKikanCode)
				.append(")");
			
		}
		return(sb.toString());
	}
}
