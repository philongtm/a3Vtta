/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/15		SSC				1.5次版機能組込
002		09/11/11		SSC				課題No.09 ソート順追加
******************************************************************************/
package app.common.dbAcc;

import app.SessionDataZen;
import app.common.form.TenpuSyokaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* 添付内容照会画面DBアクセスクラス
*/
public class TenpuSyokaiDbAcc extends CommonDbAcc {
	
	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;		// ＡＰＰコンテキスト

	private SessionDataZen cmnData = null;	// 共通セッションデータ
	private TenpuSyokaiForm form = null;	// アクションフォーム
	
	// INパラメータ
	private String ankenNo;		// 案件No
	private String phase;			// 現フェーズ
	
	private String langMode;		// 言語モード
	
	//課題No.09
	//追加開始
	//ResultSet用定数
	private final String ID = "id";
	private final String FILE_NM = "file_nm";
	private final String SYOYUU_KAISHA_CD = "syoyuu_kaisha_cd";
	private final String JITU_FILE_NM = "jitu_file_nm";
	private final String BUNSYO_NO = "bunsyo_no";
	private final String KBN_HYOUJI_VAL = "kbn_hyouji_val";
	private final String PHASE = "phase";
	private final String EDABAN = "edaban";
	//追加完了

	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public TenpuSyokaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) throws SQLException {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//ビーン取得
		cmnData = appContext.getCMNZen();
		form = (TenpuSyokaiForm)appContext.getActionForm();
		
		ankenNo = form.getAnkenNo();
		phase = form.getPhase();
		langMode = cmnData.getComLangMode();
	}

	/**
	 * 変数初期化
	 */
	public void initialize() {
		ankenNo = null;
		phase = null;
		langMode = null;
	}

	
	/**
	 * 検索SQL実行処理 <br>
	 * 
	 * @exception SQLExceptionk
	 */
	////////////////////////////////////////////////////////////
	// No297, 2008/05/16, SJA渡辺, 
	// フェーズが査定時の際は、滞留判定進捗管理テーブルの査定案件Noと画面から引き渡せれた案件Noを結びつけるように修正
	///////////////////////////////////////////////////////////
	public void execute() throws SQLException {
		
		ResultSet rs = null;
		//////////////////////////////////////////////
		//障害票：450
		//チェックイン日：2008/5/23
		//対応者：SJA中島
		//概要：査定フェーズでの文書添付ファイル検索時、滞留判定進捗テーブルの査定案件Noから
		//     滞留判定時の添付ファイルを取得している。
		//     複数の滞留判定案件が合った場合、その案件分査定時の添付ファイルが取得できてしまうため、
		//     distinctを行い、同じ添付ファイルを複数出力するのを抑制する。
		//////////////////////////////////////////////
		//課題No.09
		//削除開始
		/*StringBuffer sql = new StringBuffer()
		.append("SELECT distinct ")
		////////////////////////////////////////////////////////
		//障害票：237
		//チェックイン日：2008/5/30
		//対応者：SJA中島
		//概要：文書添付テーブルのフェーズを取得するのを止めて、保有文書の文書Noを取得するように変更
		////////////////////////////////////////////////////////
		.append("HY.bunsyo_no,")
		.append("HY.file_nm,")
		.append("HY.syoyuu_kaisha_cd,")
		.append("HY.jitu_file_nm")
		.append(" FROM SSM_HOYUBUNSYO HY,")
		.append("SST_BUNSYOTEMPU BT");
		if (30 <= Function.getValueOfInt(phase) && Function.getValueOfInt(phase) <= 70) {
		sql.append(" LEFT JOIN SST_TAIRYU_STAT ST ON")
			.append(" ST.kikan_tori_cd = '")
			.append(cmnData.getKanjo_cd())
			.append("'");
		}
		sql.append(" WHERE ");
		
		if(cmnData.getAnken_no_eda() != null){
			sql.append("(BT.anken_no='");
			sql.append(form.getAnkenNo());
			sql.append("' AND BT.ANKEN_NO_EDA ='");
			sql.append(cmnData.getAnken_no_eda());
		}else if(form.getBunsyo_no() != null){
		// 文書Noがセットされているならば、文書Noで検索
			sql.append("(BT.bunsyo_no='")
				.append(form.getBunsyo_no());
		}else{
		// 文書Noがセットされていないならば、案件Noで検索
			if (30 <= Function.getValueOfInt(phase) && Function.getValueOfInt(phase) <= 70) {
				sql.append("(BT.anken_no=ST.anken_no")
					.append(" OR BT.anken_no='")
					.append(form.getAnkenNo());
			} else {
				sql.append("(BT.anken_no='")
				.append(form.getAnkenNo());
			}
		}
		sql.append("') and BT.bunsyo_no=HY.bunsyo_no")
			.append(" and HY.del_flg='0'")
		////////////////////////////////////////////////////////
		//障害票：593
		//チェックイン日：2008/6/5
		//対応者：SJA中島
		//概要：文書テーブルの文書添付フラグが1のものを取得するように修正。
		////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////
		//障害票：593
		//チェックイン日：2008/6/8
		//対応者：SJA中島
		//概要：文書テーブルの文書添付フラグ削除による対応
		////////////////////////////////////////////////////////			
			.append(" and HY.syoyuu_eturankengen='1'")			
			// No797, 2008/06/09, SJA渡辺, 案件の査定会社コードを条件に加えるように修正
			.append(" and HY.syoyuu_kaisha_cd ='").append(form.getSateikaisya_cd()).append("'");
		
		// 遷移元の画面フェーズ以前のフェーズ判定
		////////////////////////////////////////////
		//障害票：386
		//チェックイン日：2008/5/19
		//対応者：SJA中島
		//概要：クレーム債権再設定フェーズで登録した文章については
		//      査定フェーズならば参照可能とする。
		////////////////////////////////////////////
		// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス65を定数化
		if (phase.equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)) {
			sql.append(" and BT.phase IN ('10','20','30','40','50','60','65')");
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス60を定数化
		} else if (phase.equals(GS.PHASE_NIJI_SATEI)) {
			sql.append(" and BT.phase IN ('10','20','30','40','50','60','65')");
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
		} else if (phase.equals(GS.PHASE_ICHIJI_SATEI_KENSYO)) {
			sql.append(" and BT.phase IN ('10','20','30','40','50','65')");
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
		} else if (phase.equals(GS.PHASE_ICHIJI_SATEI)) {
			sql.append(" and BT.phase IN ('10','20','30','40','65')");
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス30を定数化
		} else if (phase.equals(GS.PHASE_TAISHOSAKI_SENTEI)) {
			sql.append(" and BT.phase IN ('10','20','30')");
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス20を定数化
		} else if (phase.equals(GS.PHASE_TAIRYU_HANTEI_KENSHO)) {
			sql.append(" and BT.phase IN ('10','20')");
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス10を定数化
		} else if (phase.equals(GS.PHASE_TAIRYU_HANTEI)) {
			sql.append(" and BT.phase IN ('10')");
		}*/
		//削除完了
		//追加開始
		StringBuffer sql = new StringBuffer()
		.append("SELECT ")
		.append("HY.bunsyo_no,")
		.append("HY.file_nm,")
		.append("HY.syoyuu_kaisha_cd,")
		.append("HY.jitu_file_nm,")
		.append("BT.edaban,")
		.append("KB.kbn_hyouji_val ")
		.append("FROM SSM_HOYUBUNSYO HY ")
		.append("INNER JOIN ")
		.append("(SELECT ")
		.append("BU.bunsyo_no,")
		.append("MAX(BU.anken_no_eda) AS edaban,")
		.append("MAX(BU.phase) AS phase,")
		.append("MAX(BU.ins_dt) AS ins_dt ")
		.append("FROM SST_BUNSYOTEMPU BU ")
		.append("WHERE BU.anken_no = '").append(form.getAnkenNo()).append("' ");
		if (!GS.EMPTY_CHARCTER.equals(Function.trim(cmnData.getAnken_no_eda()))){
			//明細タブから遷移時は、明細に添付されたファイルのみ表示
			sql.append("AND BU.anken_no_eda = '").append(Function.trim(cmnData.getAnken_no_eda())).append("' ");
		}else if(30 <= Function.getValueOfInt(phase) && Function.getValueOfInt(phase) <= 65){
			//査定フェーズの場合、滞留時の添付ファイルも表示
			sql.append("OR EXISTS ")
			.append("(SELECT 1 ")
			.append("FROM SST_TAIRYU_STAT TS ")
			.append("WHERE TS.anken_no = BU.anken_no ")
			.append("AND TS.satei_anken_no = '").append(form.getAnkenNo()).append("') ");
		}
		sql.append("GROUP BY BU.bunsyo_no) BT ")
		.append("ON HY.bunsyo_no = BT.bunsyo_no ")
		.append("LEFT JOIN SSP_KBN KB ")
		.append("ON TRIM(KB.kbn_key) = 'phase' ")
		.append("AND TRIM(KB.system_kbn) = HY.system_kbn ")
		.append("AND TRIM(KB.lang_mode) = '").append(cmnData.getComLangMode()).append("' ")
		.append("AND TRIM(KB.kbn_val) = BT.phase ")
		.append("WHERE HY.del_flg = '0' ")
		.append("AND HY.syoyuu_eturankengen = '1' ")
		.append("ORDER BY BT.ins_dt DESC");
		//追加完了
		
		try {
			int i = 0;

			//SQL実行
			rs = sqlExec.execQuery(sql.toString());
			//ActionForm 明細件数に取得レコード数を格納
			form.setCnt_meisai(getRsCount(rs));
			
			List ar_meisai = new ArrayList();
			i = 0;
			while (rs.next()) {
				HashMap map = new HashMap();
				map.put(ID, Function.getStringOfInt(i));
				map.put(FILE_NM, rs.getString(FILE_NM));
				map.put(SYOYUU_KAISHA_CD, rs.getString(SYOYUU_KAISHA_CD));
				map.put(JITU_FILE_NM, rs.getString(JITU_FILE_NM));
			    map.put(BUNSYO_NO, Function.trim(rs.getString(BUNSYO_NO)));
			    map.put(PHASE, rs.getString(KBN_HYOUJI_VAL));
			    map.put(EDABAN, Function.trim(rs.getString(EDABAN)));
			    //明細配列に取得レコードを格納
			    ar_meisai.add(i, map);
			    i++;
			}
			//ActionForm に明細配列を格納
			form.setAr_meisai(ar_meisai);
			form.setPager(ar_meisai);
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
	}

	////////////////////////////////////////////////////////
	//障害票：237
	//チェックイン日：2008/5/30
	//対応者：SJA中島
	//概要：フェーズ名称を区分テーブルから取得するメソッドを追加
	////////////////////////////////////////////////////////
	/**
	 * 区分テーブルから区分名称を取得
	 * @param map
	 * @param key
	 * @param mode
	 * @param val
	 * @return
	 * @throws SQLException
	 */
	//課題No.09
	//削除開始
	/*private HashMap getHyojiKbn(HashMap map,String key,String mode) throws SQLException {

		ResultSet rs = null;
		String phaseVal = null;
		try{
			StringBuffer phaseSql = new StringBuffer();
			phaseSql.append("SELECT phase ");
			phaseSql.append("FROM SST_BUNSYOTEMPU ");
			phaseSql.append("WHERE  ");
			phaseSql.append("bunsyo_no = '");
			phaseSql.append(map.get("bunsyo_no"));
			phaseSql.append("'");
			rs = sqlExec.execQuery(phaseSql.toString());
			while(rs.next()){
				phaseVal = rs.getString("phase");
			}
			if(rs != null){
				try{
					rs.close();
				}catch (Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
			
			// 表示区分を取得するためのSQL文を作成。
			StringBuffer sql = new StringBuffer()
								.append("SELECT ")
								.append("KB.KBN_HYOUJI_VAL ")
								.append("FROM SSP_KBN KB ")
								.append("WHERE ")
								.append("KB.KBN_KEY='")
								.append(key)
								.append("' and KB.LANG_MODE='")
								.append(mode)
								.append("' and KB.KBN_VAL='")
								.append(phaseVal)
								.append("'");
			rs = sqlExec.execQuery(sql.toString());	
			while(rs.next()){
				map.put(key,rs.getString("KBN_HYOUJI_VAL"));
			}
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch (Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
		}
		return map;
	}*/
	//削除完了
	/**
	 * Resultsetの件数取得<br>
	 * 
	 * @param ResultSet
	 * @return int
	 */
	protected int getRsCount(ResultSet rs) {
	    try {
	        rs.last();
	        int count = rs.getRow();
	        rs.beforeFirst();
		    return count;
	    } catch(SQLException e) {
	        return 0;
	    }
	}
}