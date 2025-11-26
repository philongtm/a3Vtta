/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package app.system.dbAcc;

import app.SessionDataZen;
import app.system.form.RenketuForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.util.Log;

import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class RenketuDbAcc extends CommonDbAcc {

	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;		// ＡＰＰコンテキスト

	// No426, 2008/05/22, SJA渡辺, オブジェクト変数名を即した名称に修正。
	private HttpSession session;
	private SessionDataZen cmnData = null;	// 共通セッションデータ
	private RenketuForm form = null;		// アクションフォーム	
	
	private final String SYSTEM_KBN ="01";
	private final String MISE_CD = "NIC";
	
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public RenketuDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) throws SQLException {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//ビーン取得
		cmnData = appContext.getCMNZen();
		form = (RenketuForm)appContext.getActionForm();
	}
	
	/**
	 * 検索SQL実行処理 <br>
	 * アップロードされたファイルの基幹取引先コードで
	 * 統合マスタ退避より件数を取得
	 * @param kikan_cd
	 * @return 該当件数
	 * @throws SQLException
	 */
	public int selectKikanToriCd( String kikan_cd ) throws SQLException {
			
		ResultSet rs = null;
		int cnt;
		
		StringBuffer sql = new StringBuffer();
		
		// No532, 2008/05/30, SJA渡辺, 対比退避ではなく対比から取得するように修正
		// 検索分の作成
		sql.append("SELECT COUNT(KIKAN_TORI_CD) AS tori_cnt " +
				"FROM TM_TAIHI_TBL@VIR_SJLMA " +
				"WHERE TRIM(KIKAN_TORI_CD) = '")
			.append(kikan_cd)
			.append("00' AND del_flag = '0'");
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			
			rs.beforeFirst();
			rs.next();		
			
			// 該当件数を返す
			cnt =  rs.getInt("tori_cnt");
		
		} finally {
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
		return cnt;
	}
	
	/**
	 * 登録SQL実行処理 <br>
	 * ファイルのデータを連結区分マスタに登録
	 * @param data
	 * @throws Exception
	 */
	public void insertRenketu( ArrayList data ) throws Exception {
		
		Iterator it = data.iterator();
		
		// 共通セッションよりシステム日付を取得
		String sysdate = sqlExec.getDate();
		// 共通セッションよりユーザIDを取得
		String user = cmnData.getComUserId();

		// トランザクション開始
		sqlExec.beginTran();
		
		while( it.hasNext() ) {
			String[] rowData = new String[4];
			rowData = (String[])it.next();
			
			StringBuffer sql = new StringBuffer();
			
			sql.append("INSERT INTO SSM_RENKETUKUBUN(" +
				"system_kbn," +
				"mise_cd," +
				"kikan_tori_cd," +
				"ym," +
				"renketsu_kbn," +
				"del_flg," +
				"ins_user," +
				"ins_dt," +
				"upd_user," +
				"upd_dt) " +
				"VALUES( ")
		
				.append("'").append(SYSTEM_KBN).append("','")	// システム区分('01'固定)
				.append(MISE_CD).append("','")				// 店コード('NIC'固定)
//				.append(rowData[0]).append("0000000','")	// 基幹取引先コード+"0000000"
				.append(rowData[0]).append("','")			// 基幹取引先コード+"0000000"
				.append(rowData[1]).append("',")			// 年月
				.append(rowData[2]).append(",'")			// 連結区分(''は値生成時に含まれているためここでは設定しない)							
				.append(rowData[3]).append("','")			// 削除フラグ
				.append(user).append("',TO_DATE('")					// 登録ユーザID
				.append(sysdate).append("','yyyy/mm/dd hh24:mi:ss'),'")				// 登録日時
				.append(user).append("',TO_DATE('")					// 更新ユーザID
				.append(sysdate).append("','yyyy/mm/dd hh24:mi:ss'))");				// 更新日時
			
			sqlExec.addBatch(sql.toString());
			
		} // while
		
		// INSERTの実行
		if ( !sqlExec.execBatch() ) {
			// appContext.setMsgCode("err.0069");
			throw new SQLException();
			
		}
	} // insertRenketu
	
	/**
	 * 検索SQL実行処理 <br>
	 * ダウンロード処理で必要なデータの検索
	 * @throws SQLException
	 */
	public void selectDownloadData() throws SQLException {
		
		ResultSet rs = null;
		
		String sql = "SELECT " +
						"TRIM(kikan_tori_cd) AS kikan_tori_cd," +
						"TRIM(ym) AS ym," +
						"DECODE(renketsu_kbn, NULL, '', TRIM(renketsu_kbn)) AS renketsu_kbn," +
						"TRIM(del_flg) AS del_flg " +
					"FROM SSM_RENKETUKUBUN";
		
		try {
			// 検索SQLの実行
			rs = sqlExec.execQuery(sql);
			
		    // ActionForm に取得値を格納
		    ArrayList ar_meisai = new ArrayList();	// 明細配列	
		    
			int i = 0;
			while ( rs.next() ) {
			    HashMap map = new HashMap();
			    map.put("kikan_tori_cd", rs.getString("kikan_tori_cd"));
			    map.put("ym", rs.getString("ym"));
			    map.put("renketsu_kbn", rs.getString("renketsu_kbn"));
			    map.put("del_flg", rs.getString("del_flg"));
				
			    // 明細配列に取得レコードを格納
			    ar_meisai.add( map );
			    i++;   
				
			}
			
			form.setAr_meisai(ar_meisai);    

		} finally {
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		} // try
	} // selectDownloadData

	
	/**
	 * 重複データチェックSQL実行処理 <br>
	 * 重複データの有無をチェックします。
	 * @throws SQLException
	 */
	public boolean repetitionCheck(String[] rowData) throws SQLException {
		
		boolean isRepetition = false;
		
		ResultSet rs = null;
		
		String sql = "SELECT " +
						"TRIM(kikan_tori_cd) AS kikan_tori_cd," +
						"TRIM(ym) AS ym," +
						"DECODE(renketsu_kbn, NULL, '', TRIM(renketsu_kbn)) AS renketsu_kbn," +
						"TRIM(del_flg) AS del_flg " +
					"FROM SSM_RENKETUKUBUN" +
					" WHERE " +
					" TRIM(kikan_tori_cd) = TRIM('" + rowData[0] + "') " +
					" AND TRIM(ym) = TRIM('" + rowData[1] + "')" +
					" AND DECODE(renketsu_kbn, NULL, '', TRIM(renketsu_kbn)) = DECODE(" + rowData[2] + ", NULL, '', TRIM(" + rowData[2] + "))" + 
					" AND TRIM(del_flg) = TRIM('" + rowData[3] + "')";
		
		try {
			// 検索SQLの実行
			rs = sqlExec.execQuery(sql);
			
			while ( rs.next() ) {
				isRepetition = true;		
			}
			
		} finally {
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		} // try
		
		return isRepetition;
	} // repetitionCheck
}
