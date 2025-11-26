/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2008/01/13		SSC				1.5次版に修正を施し、ファイル名も変更して流用
002		09/10/20		SSC				課題No.52 HTMLファイルオープン対応
003		2014/03/17		SSC				案件No.D13493 改善対応
******************************************************************************/
package common.struts;

import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;
import common.util.SplitPath;
import common.util.TempFile;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * ダウンロードクラス
 * 
 */
public class AppDownload {

	private String CLASSNAME = getClass().getName(); // クラス名
    private HashMap extMap = null;
    private Log log = new Log();
    
	private final static String CONTENT_TYPE_EXCEL = "application/vnd.ms-excel";
	private final static String CONTENT_TYPE_JPEG = "image/jpeg";
	private final static String CONTENT_TYPE_GIF = "image/gif";
	private final static String CONTENT_TYPE_HTML = "text/html";
	// 課題No.52
	// 追加開始
	private final static String CONTENT_TYPE_XML = "text/xml";
	// 追加完了
	private final static String CONTENT_TYPE = "application/octet-stream";
	private final static String DOT = ".";
	private final static String JPG = "jpg";
	private final static String GIF = "gif";
	private final static String XLS = "xls";
	private final static String CSV = "csv";
	private final static String HTML = "html";
	// 課題No.52
	// 追加開始
	private final static String HTM = "htm";
	private final static String XML = "xml";
	// 追加完了
	private final static String SHIFT_JIS = "SJIS";
	private final static String ISO_8859_1 = "ISO-8859-1";
	private final static String CONTENT_DISPOTISION = "Content-Disposition";
	private final static String ATTACHMENT = "attachment; filename={1}";
	// 課題No.52
	// 追加開始
	private final static String INLINE = "inline; filename={1}";
	// 追加完了
	private final static String MSG_START = "start - ";
	private final static String MSG_END = "end - ";
	private final static String UTF8 = "UTF-8";

	/**
	 * コンストラクタ
	 */
	public AppDownload() {
	    if(extMap == null) {
	        extMap = new HashMap();
	        extMap.put(JPG, CONTENT_TYPE_JPEG);
	        extMap.put(GIF, CONTENT_TYPE_GIF);
	        extMap.put(XLS, CONTENT_TYPE_EXCEL);
	        extMap.put(CSV, CONTENT_TYPE_EXCEL);
	        extMap.put(HTML, CONTENT_TYPE_HTML);
			// 課題No.52
			// 追加開始
	        extMap.put(HTM, CONTENT_TYPE_HTML);
	        extMap.put(XML, CONTENT_TYPE_XML);
			// 追加完了
	    }
	}
    
	/**
	 * 帳票を除くファイルのダウンロードを実行する。
	 * @param file
	 * 				一時ファイル
	 * @param fileNm
	 * 				ウィンドウ表示用ファイルネーム
	 * @param response
	 * 				HTTPレスポンスオブジェクト
	 */
	public void execute (File file,String fileNm,HttpServletResponse response) throws Exception {

		SplitPath sp = null;
		FileInputStream is = null;
        byte[] buf =  new byte[1024];
		int length;

        try {
        	sp = new SplitPath(file.getAbsolutePath());
        	String contentType = (String)extMap.get(sp.getExt().toLowerCase());
        	if(contentType==null){
        		contentType = CONTENT_TYPE;
        	}
			is = new FileInputStream(file);

			log.write(GS.LOG_INF,CLASSNAME,MSG_START + file.getAbsolutePath() + GS.SPACE_CHARCTER + fileNm);

			response.setContentType(contentType);
			
			InputCheck chk = new InputCheck();
			String filename = fileNm.replace(GS.SPACE_CHARCTER, GS.SPACE_CHARCTER_ENCODE);
			if( !chk.isHankaku(filename) ) {
				SplitPath path = new SplitPath(filename);
				// 2022/06/10 Fix bug No.9 START
				//filename = new String((path.getFname()).getBytes(SHIFT_JIS),ISO_8859_1) + DOT + path.getExt();
				filename = URLEncoder.encode(path.getFname(),UTF8) + DOT + path.getExt();
				// 2022/06/10 Fix bug No.9 END
			}
			// 課題No.52
			// 修正開始
			// String attachment = Function.replaceString(ATTACHMENT,filename);
			// response.setHeader(CONTENT_DISPOTISION,attachment); 
			String strExt = Function.trim(sp.getExt().toLowerCase());
			String conDis = GS.EMPTY_CHARCTER;
			String conDisRs = GS.EMPTY_CHARCTER;
			if(strExt.equals(HTML) || strExt.equals(HTM) || strExt.equals(XML)){
				conDis = Function.replaceString(INLINE,filename);
			}else{
				conDis = Function.replaceString(ATTACHMENT,filename);
			}
			response.setHeader(CONTENT_DISPOTISION,conDis); 
			// 修正完了
			response.setContentLength((int)file.length());
			
			OutputStream os = response.getOutputStream();
			while ( (length = is.read(buf)) != -1 ) {
				os.write(buf, 0, length);
			}

			os.flush();
			os.close();

			log.write(GS.LOG_INF,CLASSNAME,MSG_END + file.getAbsolutePath() + GS.SPACE_CHARCTER + fileNm);

        } catch(java.net.SocketException e){
        	//クライアントのダイアログでキャンセルされた時の例外を無視
			log.write(GS.LOG_INF,CLASSNAME,MSG_END + e.getMessage());
        } finally {
    		sp = null;
        	buf = null;
        	if(is!=null){
        		try{
        			is.close();
        		}catch(Exception e){
        			log.write(GS.LOG_INF,CLASSNAME,MSG_END + e.getMessage());
        			throw e;
        		}
        	}
        	if(file!=null){
        		try{
            		file.delete();
            	}catch(Exception e){
            		log.write(GS.LOG_INF,CLASSNAME,MSG_END + e.getMessage());
        			throw e;
            	}
        	}
        	file = null;
        	is = null;
        }
	}
	
	/**
	 * @param wb
	 * 				HSSFWorkbook
	 * @param response
	 * 				HTTPレスポンスオブジェクト
	 * @param fileName
	 * 				表示用ファイル名
	 */
	public void excelDownload (HSSFWorkbook wb,HttpServletResponse response,String fileName)throws Exception {
		SplitPath sp = null;
		try {
			String fileNameEdit = fileName.replace(GS.SPACE_CHARCTER, GS.SPACE_CHARCTER_ENCODE);
			sp = new SplitPath(fileNameEdit);
        	String contentType = (String)extMap.get(sp.getExt().toLowerCase());
			response.setContentType(contentType);
			InputCheck chk = new InputCheck();
			String filenm = fileNameEdit;
			if( !chk.isHankaku(filenm) ) {
				// 2022/06/10 Fix bug No.7 START
				//filenm = new String((fileNameEdit).getBytes(SHIFT_JIS),ISO_8859_1);
				filenm = URLEncoder.encode(fileNameEdit,UTF8);
				// 2022/06/10 Fix bug No.7 END
			}
			String attachment = Function.replaceString(ATTACHMENT,filenm);
			response.setHeader(CONTENT_DISPOTISION,attachment);
			ServletOutputStream os = response.getOutputStream();
			wb.write(os);
			os.close();
        } catch(IOException e){
			// クライアントのダイアログでキャンセルされた時の例外を無視
        	if(e.getMessage() != null){
				log.write(GS.LOG_INF,CLASSNAME,"end - " + e.getMessage());
				throw e;
        	}else{
				log.write(GS.LOG_INF,CLASSNAME,"end - キャンセルボタンが押下されました。");
        	}
        } finally {
        	wb = null;
        	sp = null;
        }
	}	
	
	/**
	 * 帳票を除くファイルのダウンロードを実行する。
	 * @param tmp
	 * 				一時ファイル
	 * @param response
	 * 				HTTPレスポンスオブジェクト
	 */
	public void execute (TempFile tmp,HttpServletResponse response) throws Exception {

		SplitPath sp = null;
		File file = null; 
		FileInputStream is = null;
        byte[] buf =  new byte[1024];
		int length;

        try {
        	sp = new SplitPath(tmp.getDisplayFilename());
        	String contentType = (String)extMap.get(sp.getExt().toLowerCase());
        	if(contentType==null){
        		contentType = CONTENT_TYPE;
        	}
           	file = new File(tmp.getPath());
			is = new FileInputStream(file);

			log.write(GS.LOG_INF,CLASSNAME,MSG_START + tmp.getPath() + GS.SPACE_CHARCTER + tmp.getDisplayFilename());

			response.setContentType(contentType);
			
			InputCheck chk = new InputCheck();
			String filename = tmp.getDisplayFilename().replace(GS.SPACE_CHARCTER, GS.SPACE_CHARCTER_ENCODE);
			if( !chk.isHankaku(filename) ) {
				SplitPath path = new SplitPath(filename);
				// 2022/06/10 Fix bug No.7 START
				//filename = new String((path.getFname()).getBytes(SHIFT_JIS),ISO_8859_1) + DOT + path.getExt();
				filename = URLEncoder.encode(path.getFname(),UTF8) + DOT + path.getExt();
				// 2022/06/10 Fix bug No.7 END
			}
			// 課題No.52
			// 修正開始
			// String attachment = Function.replaceString(ATTACHMENT,filename);
			// response.setHeader(CONTENT_DISPOTISION,attachment); 
			String strExt = Function.trim(sp.getExt().toLowerCase());
			String conDis = GS.EMPTY_CHARCTER;
			String conDisRs = GS.EMPTY_CHARCTER;
			if(strExt.equals(HTML) || strExt.equals(HTM) || strExt.equals(XML)){
				conDis = Function.replaceString(INLINE,filename);
			}else{
				conDis = Function.replaceString(ATTACHMENT,filename);
			}
			response.setHeader(CONTENT_DISPOTISION,conDis); 
			// 修正完了
			response.setContentLength(tmp.getLength());
			
			OutputStream os = response.getOutputStream();
			while ( (length = is.read(buf)) != -1 ) {
				os.write(buf, 0, length);
			}

			os.flush();
			os.close();

			log.write(GS.LOG_INF,CLASSNAME,MSG_END + tmp.getPath() + GS.SPACE_CHARCTER + tmp.getDisplayFilename());

        } catch(IOException e){
			// クライアントのダイアログでキャンセルされた時の例外を無視
        	if(e.getMessage() != null){
				log.write(GS.LOG_INF,CLASSNAME,"end - " + e.getMessage());
				throw e;
        	}else{
				log.write(GS.LOG_INF,CLASSNAME,"end - キャンセルボタンが押下されました。");
        	}
        } finally {
    		sp = null;
        	file = null;
        	buf = null;
        	if(is!=null) try{is.close();}catch(Exception e){}
        	if(tmp!=null) try{tmp.delete();}catch(Exception e){}
        	tmp = null;
        	is = null;
        }
	}
}