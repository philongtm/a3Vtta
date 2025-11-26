/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/01/14		SSC				新規作成
******************************************************************************/
package common.util;

import common.AppContext;
import common.global.GS;
import common.struts.AppDownload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * EXCELクラス
 * <pre>
 * EXCELを読み込む。
 * </pre>
 */
public class Excel {

	private AppContext appContext;
	private POIFSFileSystem poi;
	private HSSFWorkbook wb;
	private HSSFSheet sheet; 
	private HSSFRow row;
	private HSSFCell cell;
	private HSSFCellStyle style[];
	private HSSFFont font[];
	private String displayFileName;
	private final static boolean FALSE = false;
	private final static boolean TRUE = true;

	//↓セルの型を示す定数
	//呼出元でgetCellType()の戻り値と、以下の定数の判定を行い、セルの値取得用メソッドを決定して下さい。
	public final static int CELL_TYPE_BLANK = HSSFCell.CELL_TYPE_BLANK;			//空
	public final static int CELL_TYPE_BOOLEAN = HSSFCell.CELL_TYPE_BOOLEAN;		//論理値
	public final static int CELL_TYPE_ERROR = HSSFCell.CELL_TYPE_ERROR;			//エラー
	public final static int CELL_TYPE_FORMULA = HSSFCell.CELL_TYPE_FORMULA;		//式
	public final static int CELL_TYPE_NUMERIC = HSSFCell.CELL_TYPE_NUMERIC;		//数値
	public final static int CELL_TYPE_STRING = HSSFCell.CELL_TYPE_STRING;			//文字列

	//エクセルのエラーコード
	public final static byte ERROR_DIV_0 = 7;	//#DIV/0! 
	public final static byte ERROR_NA = 42; 		//#N/A 
	public final static byte ERROR_NAME = 29; 	//#NAME? 
	public final static byte ERROR_NULL = 0; 	//#NULL! 
	public final static byte ERROR_NUM = 36; 	//#NUM! 
	public final static byte ERROR_REF = 23; 	//#REF! 
	public final static byte ERROR_VALUE = 15;	//#VALUE! 

	//罫線
	public final static int BORDER_BOTTOM = 0;
	public final static int BORDER_TOP = 1;
	public final static int BORDER_LEFT = 2;
	public final static int BORDER_RIGHT = 3;
	public final static short BORDER_DASH_DOT = HSSFCellStyle.BORDER_DASH_DOT;
	public final static short BORDER_DASH_DOT_DOT = HSSFCellStyle.BORDER_DASH_DOT_DOT;
	public final static short BORDER_DASHED = HSSFCellStyle.BORDER_DASHED;
	public final static short BORDER_DOTTED = HSSFCellStyle.BORDER_DOTTED;
	public final static short BORDER_DOUBLE = HSSFCellStyle.BORDER_DOUBLE;
	public final static short BORDER_HAIR = HSSFCellStyle.BORDER_HAIR;
	public final static short BORDER_MEDIUM = HSSFCellStyle.BORDER_MEDIUM;
	public final static short BORDER_MEDIUM_DASH_DOT = HSSFCellStyle.BORDER_MEDIUM_DASH_DOT;
	public final static short BORDER_MEDIUM_DASH_DOT_DOT = HSSFCellStyle.BORDER_MEDIUM_DASH_DOT_DOT;
	public final static short BORDER_MEDIUM_DASHED = HSSFCellStyle.BORDER_MEDIUM_DASHED;
	public final static short BORDER_NONE = HSSFCellStyle.BORDER_NONE;
	public final static short BORDER_SLANTED_DASH_DOT = HSSFCellStyle.BORDER_SLANTED_DASH_DOT;
	public final static short BORDER_THICK = HSSFCellStyle.BORDER_THICK;
	public final static short BORDER_THIN = HSSFCellStyle.BORDER_THIN;

	//フォント
	public final static int FONT_BOLD = 0;
	public final static int FONT_COLOR = 1;
	public final static int FONT_HEIGHT = 2;
	public final static short BOLDWEIGHT_BOLD = HSSFFont.BOLDWEIGHT_BOLD;
	public final static short BOLDWEIGHT_NORMAL = HSSFFont.BOLDWEIGHT_NORMAL;
	public final static short COLOR_NORMAL = HSSFFont.COLOR_NORMAL;
	public final static short COLOR_RED = HSSFFont.COLOR_RED;

	/**
	 * コンストラクタ
	 * 
	 * エクセル新規作成
	 */
	public Excel(AppContext appCon,int num) throws IOException{
		this.appContext = appCon;
		poi = new POIFSFileSystem();
		wb = new HSSFWorkbook(poi);
		sheet = wb.createSheet();
		style = new HSSFCellStyle[num];
		font = new HSSFFont[num];
	}

	/**
	 * コンストラクタ
	 * 
	 * エクセル読込
	 */
	public Excel(AppContext appCon,FileInputStream fis,int num) throws IOException{
		this.appContext = appCon;
		poi = new POIFSFileSystem(fis);
		wb = new HSSFWorkbook(poi);
		sheet = wb.getSheetAt(0);
		style = new HSSFCellStyle[num];
		font = new HSSFFont[num];
	}
	
	/**
	 * コンストラクタ
	 * 
	 * サーバ上のエクセル読込
	 */
	public Excel(AppContext appCon,String fileName,int num) throws IOException{
		this.appContext = appCon;
		this.displayFileName = fileName + GS.DOTXLS;
		poi = new POIFSFileSystem(new FileInputStream(appContext.getRealPath(GS.EXCELDIR + fileName + GS.DOTXLS)));
		wb = new HSSFWorkbook(poi);
		sheet = wb.getSheetAt(0);
		style = new HSSFCellStyle[num];
		font = new HSSFFont[num];
	}
	
	/**
	 * ウィンドウ表示用ファイル名設定
	 * @param name
	 * 			設定するファイル名
	 */
	public void setDisplayFileName(String name) {
		this.displayFileName = name;
	}

	/**
	 * ウィンドウ表示用ファイル名を返す
	 * @return String
	 * 			ファイル名
	 */
	public String getDisplayFileName() {
		return displayFileName;
	}

	/**
	 * シート数取得
	 * @return int
	 * 			ワークブックのシート数を返す
	 */
	public int getNumberOfSheetst() {
		return wb.getNumberOfSheets();
	}

	/**
	 * スタイルを生成する
	 * @param idx
	 */
	public void createCellStyle(int idx) {
		style[idx] = wb.createCellStyle();
	}

	/**
	 * フォントを生成する
	 * @param idx
	 */
	public void createFont(int idx) {
		font[idx] = wb.createFont();
	}

	/**
	 * セルのスタイルをコピーする
	 * @param rowIndex
	 * 			選択する行のインデックス
	 * @param colIndex
	 * 			選択する列のインデックス
	 * @param idx
	 * @return boolean
	 * 			セルスタイルが存在しない場合falseを返す
	 */
	public boolean copyCellStyle(int rowIndex,int colIndex,int idx) {
		style[idx] = sheet.getRow(rowIndex).getCell((short)colIndex).getCellStyle();
		if(style[idx] == null){
			return FALSE;
		}
		return TRUE;
	}

	/**
	 * 行とセル初期化
	 */
	private void initRowCell() {
		row = null;
		cell = null;
	}
	/**
	 * セル初期化
	 */
	private void initCell() {
		cell = null;
	}

	/**
	 * カレントシート選択
	 * @param index
	 * 			選択するシートのインデックス(先頭ゼロ)
	 * @return boolean
	 * 			シートが存在しない場合falseを返す
	 */
	public boolean selectSheet(int index) {
		this.initRowCell();
		sheet = wb.getSheetAt(index);
		if(sheet == null){
			return FALSE;
		}
		return TRUE;
	}

	/**
	 * カレントシート選択
	 * @param name
	 * 			選択するシート名
	 * @return boolean
	 * 			シートが存在しない場合falseを返す
	 */
	public boolean selectSheet(String name) {
		this.initRowCell();
		sheet = wb.getSheet(name);
		if(sheet == null){
			return FALSE;
		}
		return TRUE;
	}

	/**
	 * シートを作成し、カレントに設定
	 */
	public void createSheet() {
		this.initRowCell();
		sheet = wb.createSheet();
	}

	/**
	 * シートをコピーし、カレントに設定
	 * @param index
	 * 			コピー元シートのインデックス(先頭ゼロ)
	 * @return boolean
	 * 			シートが存在しない場合falseを返す
	 */
	public boolean cloneSheet(int index) {
		this.initRowCell();
		sheet = wb.cloneSheet(index);
		if(sheet == null){
			return FALSE;
		}
		return TRUE;
	}

	/**
	 * シートを削除する
	 * @param index
	 * 			削除するシートのインデックス(先頭ゼロ)
	 * @return boolean
	 * 			シートが存在しない場合falseを返す
	 */
	public boolean removeSheet(int index) {
		if(wb.getSheetAt(index) == null){
			return FALSE;
		}
		wb.removeSheetAt(index);
		return TRUE;
	}

	/**
	 * シートに名前を付与する
	 * @param index
	 * 			名前を付与するシートのインデックス(先頭ゼロ)
	 * @param name
	 * 			シートに付与する名前
	 * @return boolean
	 * 			シートが存在しない場合falseを返す
	 */
	public boolean setSheetName(int index,String name) {
		if(wb.getSheetAt(index) == null){
			return FALSE;
		}
		wb.setSheetName(index,name,HSSFWorkbook.ENCODING_UTF_16);
		return TRUE;
	}

	
	/**
	 * カレント行選択
	 * @param index
	 * 			選択する行のインデックス(先頭ゼロ)
	 * @param flg
	 * 			true：行が存在しない場合新規作成
	 * @return boolean
	 * 			行が存在しない、かつ新規作成しない場合falseを返す
	 */
	public boolean selectRow(int index,boolean flg) {
		this.initCell();
		row = sheet.getRow(index);
		if(row == null){
			if(flg){
				this.createRow(index);
			}else{
				return FALSE;
			}
		}
		return TRUE;
	}
	/**
	 * 行を作成し、カレントに設定
	 * @param index
	 * 			作成する行のインデックス(先頭ゼロ)
	 */
	private void createRow(int index) {
		this.initCell();
		row = sheet.createRow(index);
	}

	
	/**
	 * 行を削除する
	 * @param index
	 * 			削除する行のインデックス(先頭ゼロ)
	 * @return boolean
	 * 			行が存在しない場合falseを返す
	 */
	public boolean removeRow(int index) {
		if(sheet.getRow(index) == null){
			return FALSE;
		}
		sheet.removeRowBreak((short)index);
		this.initRowCell();
		return TRUE;
	}

	/**
	 * カレント行を削除する
	 */
	public void removeRow() {
		sheet.removeRow(row);
		this.initRowCell();
	}

	/**
	 * カレントシートのデータが存在する開始行を返す
	 */
	public int getFirstRowNum() {
		return sheet.getFirstRowNum();
	}

	/**
	 * カレントシートのデータが存在する最終行を返す
	 */
	public int getLastRowNum() {
		return sheet.getLastRowNum();
	}
	

	/**
	 * カレントセル選択
	 * @param index
	 * 			選択するセルの列のインデックス(先頭ゼロ)
	 * @param flg
	 * 			true：セルが存在しない場合新規作成
	 * @return boolean
	 * 			セルが存在しない、かつ新規作成しない場合falseを返す
	 */
	public boolean selectCell(int index,boolean flg) {
		cell = row.getCell((short)index);
		if(cell == null){
			if(flg){
				this.createCell(index);
			}else{
				return FALSE;
			}
		}
		return TRUE;
	}

	/**
	 * カレントセル選択
	 * @param index1
	 * 			選択するセルの行のインデックス(先頭ゼロ)
	 * @param index2
	 * 			選択するセルの列のインデックス(先頭ゼロ)
	 * @param flg
	 * 			true：セルが存在しない場合新規作成
	 * @return boolean
	 * 			セルが存在しない、かつ新規作成しない場合falseを返す
	 */
	public boolean selectCell(int index1,int index2,boolean flg) {
		this.initCell();
		if(!this.selectRow(index1,flg)){
			return FALSE;
		}
		cell = row.getCell((short)index2);
		if(cell == null){
			if(flg){
				this.createCell(index2);
			}else{
				return FALSE;
			}
		}
		return TRUE;
	}

	/**
	 * セルを作成し、カレントに設定
	 * @param index
	 * 			作成するセルの列のインデックス(先頭ゼロ)
	 */
	private void createCell(int index) {
		cell = row.createCell((short)index);
	}
	
	/**
	 * カレントセルを削除する
	 */
	public void removeCell() {
		row.removeCell(cell);
		this.initCell();
	}
	
	/**
	 * カレントセルの型を取得する
	 */
	public int getCellType() {
		return cell.getCellType();
	}
	
	/**
	 * カレントセルの値を取得する
	 * @return boolean
	 * 			取得した論理値を返す
	 */
	public boolean getBooleanCellValue() {
		return cell.getBooleanCellValue();
	}
	
	/**
	 * カレントセルの値を取得する
	 * @return Date
	 * 			セル値を日時として返す
	 */
	public Date getDateCellValue() {
		return cell.getDateCellValue();
	}

	/**
	 * カレントセルの値を取得する
	 * @return double
	 * 			取得した数値を返す
	 */
	public double getNumericCellValue() {
		return cell.getNumericCellValue();
	}

	/**
	 * カレントセルの値を取得する
	 * @return String
	 * 			取得した文字列を返す
	 */
	public String getStringCellValue() {
		return cell.getStringCellValue();
	}

	/**
	 * カレントセルの値を取得する
	 * @return byte
	 * 			セル値をエラーコードとして返す
	 */
	public byte getErrorCellValue() {
		return cell.getErrorCellValue();
	}

	/**
	 * カレントセルの値を取得する
	 * @return byte
	 * 			取得した式を文字列で返す
	 */
	public String getCellFormula() {
		return cell.getCellFormula();
	}

	/**
	 * カレントセルに値を設定する
	 * @return boolean
	 * 			セルに値を設定できなかったときfalseを返す
	 */
	public boolean setCellValue(Object obj) {
		if(obj instanceof Calendar){
			this.setCellValue((Calendar)obj);
		}else if(obj instanceof Date) {
			this.setCellValue((Date)obj);
		}else if(obj instanceof String) {
			switch (cell.getCellType()){
				case CELL_TYPE_FORMULA:
					this.setCellFormula((String)obj);
					break;
				default:
					this.setCellValue((String)obj);
					break;
			}
		}else{
			return FALSE;
		}
		return TRUE;
	}

	/**
	 * カレントセルに値を設定する
	 */
	public void setCellValue(String str) {
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue(str);
	}
	
	/**
	 * カレントセルに値を設定する
	 */
	public void setCellValue(Calendar cal) {
		cell.setCellValue(cal);
	}
	
	/**
	 * カレントセルに値を設定する
	 */
	public void setCellValue(Date date) {
		cell.setCellValue(date);
	}
	
	/**
	 * カレントセルに値を設定する
	 */
	public void setCellFormula(String str) {
		cell.setCellFormula(str);
	}

	/**
	 * カレントセルに値を設定する
	 */
	public void setCellValue(double num) {
		cell.setCellValue(num);
	}

	/**
	 * カレントセルに値を設定する
	 */
	public void setCellValue(boolean flg) {
		cell.setCellValue(flg);
	}
	
	/**
	 * カレントセルにスタイルを設定する
	 * @param idx
	 */
	public void setCellStyle(int idx) {
		cell.setCellStyle(style[idx]);
	}

	/**
	 * スタイルに日付けを設定する
	 * @param dateFormat
	 * @param idx
	 */
	public void setDataFormat(String dateFormat,int idx) {
		if(style[idx] == null){
			this.createCellStyle(idx);
		}
		style[idx].setDataFormat(HSSFDataFormat.getBuiltinFormat(dateFormat));
	}
	
	/**
	 * スタイルに罫線を設定する
	 * 引数にはフィールド宣言されている定数を用いること
	 * @param bor
	 * 			罫線を引く箇所
	 * @param borType
	 * 			罫線のタイプ
	 * @param idx
	 */
	public void setStyleBorder(int bor,short borType,int idx) {
		if(style[idx] == null){
			this.createCellStyle(idx);
		}
		switch (bor){
			case BORDER_BOTTOM:
				style[idx].setBorderBottom(borType);
				break;
			case BORDER_TOP:
				style[idx].setBorderTop(borType);
				break;
			case BORDER_LEFT:
				style[idx].setBorderLeft(borType);
				break;
			case BORDER_RIGHT:
				style[idx].setBorderRight(borType);
				break;
			default:
				break;
		}
	}

	/**
	 * フォントの設定をする
	 * 引数にはフィールド宣言されている定数を用いること
	 * @param fontType
	 * @param type
	 * @param idx
	 */
	public void setFont(int fontType,short type,int idx) {
		if(font[idx] == null){
			this.createFont(idx);
		}
		switch (fontType){
			case FONT_BOLD:
				font[idx].setBoldweight(type);
				break;
			case FONT_COLOR:
				font[idx].setColor(type);
				break;
			case FONT_HEIGHT:
				font[idx].setFontHeightInPoints(type);
				break;
			default:
				break;
		}
	}
	
	/**
	 * スタイルにフォントを設定する
	 * @param idx1
	 * 			style配列のインデックス
	 * @param idx2
	 * 			font配列のインデックス
	 */
	public void setStyleFont(int idx1,int idx2) {
		style[idx1].setFont(font[idx2]);
	}
	
	/**
	 * スタイルに文字の配置を設定する
	 * @param align
	 * 			文字の配置タイプ
	 * @param idx
	 * 			style配列のインデックス
	 */
	public void setStyleAlign(short align,int idx) {
		if(style[idx] == null){
			this.createCellStyle(idx);
		}
		style[idx].setAlignment(align);
	}

	/**
	 * 印刷範囲設定
	 */
	public void setPrintAria(int sheetIndex,int startColumn,int endColumn,int startRow,int endRow){
		wb.setPrintArea(sheetIndex,startColumn,endColumn,startRow,endRow);
	}

	/**
	 * カレントシートの最終行取得
	 */
	public int getLastRowIndex() throws Exception{
		return sheet.getLastRowNum();
	}

	/**
	 * カレント行の最終列取得
	 */
	public int getLastCellIndex() throws Exception{
		return row.getLastCellNum();
	}

	/**
	 * エクセルダウンロードを実行する
	 */
	public void download() throws Exception{
		AppDownload appDownLoad = new AppDownload();
		appDownLoad.excelDownload(wb,appContext.getResponse(),displayFileName);
	}
}