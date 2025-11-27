/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
0001	09/05/15		SSC				1.5次版機能組込
0002	09/11/13		SSC				課題No.09 保有文書添付仕様変更
******************************************************************************/
package app.common.dbAcc;

import app.SessionDataZen;
import app.common.form.TenpuSentakuForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Ftp;
import common.util.Function;
import common.util.Log;
import common.util.TempFile;
import common.util.TempFileList;
import config.adapter.struts.upload.FormFile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 共通_添付選択 DBアクセスクラス
 */
public class TenpuSentakuDbAcc extends CommonDbAcc{
	
	private final String CLASSNAME = getClass().getName();
	// アプリケーションContext
	private AppContext appContext = null;
	// 共通セッションデータ
	private SessionDataZen cmnData = null;
	// 共通_滞留債権明細照会タブ Form
	private TenpuSentakuForm form = null;
	
	// 案件No
	private String anken_no;
	// 案件No枝番
	private String anken_no_eda;
	// 指定フェーズ
	private String phase;
	// ログインユーザID
	private String userId;

	//課題No.09
	//追加開始
	//ResultSet用定数
	private final String BUNSYO_NO				= "bunsyo_no";
	private final String TEMPU_KBN				= "tempu_kbn";
	private final String OTH_TEMPU_KBN			= "oth_tempu_kbn";
	private final String TAIRYU_KBN			= "tairyu_kbn";
	private final String MEISAI_KBN			= "meisai_kbn";
	private final String ANKEN_NO_EDA			= "anken_no_eda";
	private final String SYSTEM_KBN			= "system_kbn";
	private final String MISE_CD				= "mise_cd";
	private final String KIKAN_TORI_CD			= "kikan_tori_cd";
	private final String SYOYUU_KAISHA_CD		= "syoyuu_kaisha_cd";
	private final String SYOYUU_SOSHIKI_CD		= "syoyuu_soshiki_cd";
	private final String SYOYUU_ETURANKENGEN	= "syoyuu_eturankengen";
	private final String FILE_NM				= "file_nm";
	private final String JITU_FILE_NM			= "jitu_file_nm";
	private final String PHASE					= "phase";
	//ハッシュマップキー
	private final String ROW_COLOR				= "rowColor";
	private final String TENPU_CHECK			= "tenpuCheck";
	private final String SAKUJO_CHECK			= "sakujoCheck";
	private final String TENPU_CHECK_DIS		= "tenpuCheckDis";
	private final String SAKUJO_CHECK_DIS		= "sakujoCheckDis";
	//文書一覧表示用
	private final String SHIROIRO			= "background-color:#FFFFFF";
	private final String MIZUIRO			= "background-color:#CCFFFF";
	private final String TEMPU_ZUMI		= "1";
	private final String CHK_BOX_ON		= "on";
	private final String CHK_BOX_OFF		= "off";
	private final String TRUE				= "disabled";
	private final String FALSE				= "";
	//追加完了
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public TenpuSentakuDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) throws SQLException {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//Bean取得
		cmnData = appContext.getCMNZen();
		form = (TenpuSentakuForm)appContext.getActionForm();
		
		//Beanの値を変数に設定
		userId = cmnData.getComUserId();	
		phase=cmnData.getPhase();
		anken_no=form.getAnken_no();
		anken_no_eda=form.getAnken_no_eda();
		
	}

	/**
	 * 検索SQL実行処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void execute() throws SQLException {
		
		ResultSet rs = null;
		
		// 遷移元のフェーズ以前の値を取得。
		// 遷移元の画面フェーズ以前のフェーズ判定
		/*String phaseBuffer = null; 
		//課題No.09
		//修正開始
		// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス70を定数化
		if (phase.equals(GS.PHASE_HIKIATEKIN_KENSYO)) {
			phaseBuffer = "('10','20','30','40','50','60','65','70')";
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス65を定数化
		} else if (phase.equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)) {
			phaseBuffer = "('10','20','30','40','50','60','65')";
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス60を定数化
		} else if (phase.equals(GS.PHASE_NIJI_SATEI)) {
			phaseBuffer = "('10','20','30','40','50','60')";
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
		} else if (phase.equals(GS.PHASE_ICHIJI_SATEI_KENSYO)) {
			phaseBuffer = "('10','20','30','40','50')";
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
		} else if (phase.equals(GS.PHASE_ICHIJI_SATEI)) {
			phaseBuffer = "('10','20','30','40')";
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス30を定数化
		} else if (phase.equals(GS.PHASE_TAISHOSAKI_SENTEI)) {
			phaseBuffer = "('10','20','30')";
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス20を定数化
		} else if (phase.equals(GS.PHASE_TAIRYU_HANTEI_KENSHO)) {
			phaseBuffer = "('10','20')";
			// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス10を定数化
		} else if (phase.equals(GS.PHASE_TAIRYU_HANTEI)) {
			phaseBuffer = "('10')";
		} else {
			//
		}
		
		////////////////////////////////////////////////////////
		//障害票：237
		//チェックイン日：2008/5/22
		//対応者：SJA中島
		//概要：文書テーブルから取得するデータに案件Noを追加。
		////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////
		//障害票：593
		//チェックイン日：2008/6/5
		//対応者：SJA中島
		//概要：文書テーブルから取得するデータに添付フラグを追加。
		////////////////////////////////////////////////////////
		// SQL【No1】文書添付情報取得
		StringBuffer sql1 = new StringBuffer()
							.append("select distinct ")
							.append("BT.bunsyo_no as bt_bunsyo_no,")
							.append("HY.bunsyo_no,")
							.append("HY.system_kbn,")
							.append("HY.mise_cd,")
							.append("HY.kikan_tori_cd,")
							.append("HY.syoyuu_kaisha_cd,")
							.append("HY.syoyuu_soshiki_cd,")
							.append("HY.syoyuu_eturankengen,")
							.append("HY.del_flg,")
							.append("HY.file_nm,")
							.append("HY.jitu_file_nm ")
							.append("from SSM_HOYUBUNSYO HY ")
							.append("LEFT JOIN SST_BUNSYOTEMPU BT ON ")
							.append("HY.bunsyo_no = BT.bunsyo_no ")
							.append("where ");
		//////////////////////////////////////////////////////
		//障害票：237
		//チェックイン日：2008/5/17
		//対応者：SJA中島
		//概要：滞留判定明細から遷移してきた場合も勘定先CD毎に文書添付情報を
		//     取得するように修正。
		//////////////////////////////////////////////////////
		
		sql1.append("HY.kikan_tori_cd = '")
			.append(form.getTori_cd())
			.append("' ")
			.append("and HY.del_flg = '0' ")
			.append("and ")
			.append("HY.mise_cd = '")
			.append(cmnData.getMise_cd())
			.append("' ")
			.append("and ")
			.append("HY.syoyuu_kaisha_cd = '")
			// No797, 2008/06/09, SJA渡辺, 条件を案件の査定会社コードにするように修正
			//.append(cmnData.getComSateiKaishaCd())
			.append(form.getSateikaisya_cd())
			.append("'");
		
		try{
			// SQL実行		
			rs = sqlExec.execQuery(sql1.toString());
			
			ArrayList list = new ArrayList();

			// 明細情報格納カウンタ
			int i = 0;
			
			while ( rs.next() ) {
				// 明細Beanクラス生成
				HashMap map = new HashMap();
			    map.put("bunsyo_no", Function.trim(rs.getString("bunsyo_no")));
			    map.put("system_kbn", rs.getString("system_kbn"));
			    map.put("mise_cd", rs.getString("mise_cd"));
			    map.put("kikan_tori_cd", rs.getString("kikan_tori_cd"));
			    map.put("syoyuu_kaisha_cd", rs.getString("syoyuu_kaisha_cd"));
			    map.put("syoyuu_soshiki_cd", rs.getString("syoyuu_soshiki_cd"));
			    map.put("syoyuu_eturankengen", rs.getString("syoyuu_eturankengen"));
			    map.put("file_nm", rs.getString("file_nm"));
			    map.put("jitu_file_nm", rs.getString("jitu_file_nm"));	    
				////////////////////////////////////////////////////////
				//障害票：237
				//チェックイン日：2008/5/28
				//対応者：SJA中島
				//概要：チェックボックスの初期チェック条件を文書Noがある、かつ案件Noが同一である
			    //     に変更
				////////////////////////////////////////////////////////
			    if(!checkBunsyoTemp(map)){
			    	map.put("tenpuCheck","on");
			    	
			    	// 登録前に戻るボタン押下時には、リンクを張らないように修正。
			    	map.put("touroku_flg","1");
			    }else{
			    	map.put("tenpuCheck","off");
			    	// 障害票：593　チェックイン日：2008/6/13　SJA中島　他の課で添付したファイルについて、デフォルトでリンクを張るように修正
			    	map.put("touroku_flg","1");
			    }
			    map.put("sakujoCheck","off");
		    	
				// 障害票No522　2008/05/28　細野　英語モード対応			 
		    	map = getHyojiKbn(map,"phase",cmnData.getComLangMode());*/
		StringBuffer sql1 = new StringBuffer();
		sql1.append("SELECT ")
			.append("HY.bunsyo_no,")
			.append("NVL(BT.tempu_kbn,'0') AS tempu_kbn,")
			.append("NVL(BT.oth_tempu_kbn,'0') AS oth_tempu_kbn,")
			.append("NVL(BT.meisai_kbn,'0') AS meisai_kbn,")
			.append("NVL(BT.tairyu_kbn,'0') AS tairyu_kbn,")
			.append("BT.anken_no_eda,")
			.append("HY.system_kbn,")
			.append("HY.mise_cd,")
			.append("HY.kikan_tori_cd,")
			.append("HY.syoyuu_kaisha_cd,")
			.append("HY.syoyuu_soshiki_cd,")
			.append("HY.syoyuu_eturankengen,")
			.append("HY.file_nm,")
			.append("HY.jitu_file_nm,")
			.append("KB.kbn_hyouji_val AS phase ")
			.append("FROM ")
			.append("SSM_HOYUBUNSYO HY ")
			.append("LEFT JOIN (")
			.append("SELECT ")
			.append("SUB_BT.bunsyo_no,")
			.append("MAX(SUB_BT.anken_no_eda) AS anken_no_eda,")
			.append("MAX(SUB_BT.tempu_kbn) AS tempu_kbn,")
			.append("MAX(SUB_BT.oth_tempu_kbn) AS oth_tempu_kbn,")
			.append("MAX(SUB_BT.tairyu_kbn) AS tairyu_kbn,")
			.append("MAX(SUB_BT.meisai_kbn) AS meisai_kbn,")
			.append("MAX(SUB_BT.kigen_kbn) AS kigen_kbn,")
			.append("MAX(SUB_BT.phase) AS phase,")
			.append("MAX(SUB_BT.ins_dt) AS ins_dt ")
			.append("FROM (")
			.append("SELECT ")
			.append("BU.bunsyo_no,")
			.append("'1' AS tempu_kbn,")
			.append("'0' AS oth_tempu_kbn,")
			.append("DECODE(BU.anken_no,'").append(anken_no).append("','0','1') AS tairyu_kbn,")
			.append("BU.anken_no_eda,")
			.append("DECODE(BU.anken_no_eda,NULL,'0','").append(anken_no_eda).append("','1','0') AS meisai_kbn,")
			.append("'0' AS kigen_kbn,")
			.append("BU.phase,")
			.append("BU.ins_dt ")
			.append("FROM ")
			.append("SST_BUNSYOTEMPU BU ")
			.append("WHERE ")
			.append("(BU.anken_no = '").append(anken_no).append("' ")
			.append("OR EXISTS ")
			.append("(SELECT 1 ")
			.append("FROM SST_TAIRYU_STAT TS ")
			.append("WHERE TS.anken_no = BU.anken_no ")
			.append("AND TS.satei_anken_no = '").append(anken_no).append("')) ")
			.append("UNION ALL ")
			.append("SELECT ")
			.append("OTH_BU.bunsyo_no,")
			.append("'0' AS tempu_kbn,")
			.append("'1' AS oth_tempu_kbn,")
			.append("'0' AS tairyu_kbn,")
			.append("NULL AS anken_no_eda,")
			.append("'0' AS meisai_kbn,")
			.append("DECODE(SUB_KIGEN.anken_no, NULL, '1', '0') AS kigen_kbn,")
			.append("NULL AS phase,")
			.append("OTH_BU.ins_dt ")
			.append("FROM ")
			.append("SST_BUNSYOTEMPU OTH_BU ")
			.append("LEFT JOIN (")
			.append("SELECT ")
			.append("SS.anken_no ")
			.append("FROM ")
			.append("SST_SATEI_STAT SS ")
			.append("WHERE ")
			.append("SS.satei_ki > PG_SS_FUNCTION.SF_SS_GETZENZENSATEIKI('").append(cmnData.getSatei_ki()).append("') ")
			.append("UNION ALL ")
			.append("SELECT ")
			.append("TS.anken_no ")
			.append("FROM ")
			.append("SST_TAIRYU_STAT TS ")
			.append("WHERE ")
			.append("TS.satei_ki > PG_SS_FUNCTION.SF_SS_GETZENZENSATEIKI('").append(cmnData.getSatei_ki()).append("')) SUB_KIGEN ")
			.append("ON OTH_BU.anken_no = SUB_KIGEN.anken_no ")
			.append("WHERE ")
			.append("OTH_BU.anken_no <> '").append(anken_no).append("') SUB_BT ")
			.append("GROUP BY ")
			.append("SUB_BT.bunsyo_no) BT ")
			.append("ON  HY.bunsyo_no = BT.bunsyo_no ")
			.append("LEFT JOIN ")
			.append("SSP_KBN KB ")
			.append("ON TRIM(KB.kbn_key) = 'phase' ")
			.append("AND TRIM(KB.system_kbn) = HY.system_kbn ")
			.append("AND TRIM(KB.lang_mode) = '").append(cmnData.getComLangMode()).append("' ")
			.append("AND TRIM(KB.kbn_val) = BT.phase ")
			.append("WHERE ")
			.append("HY.del_flg = '0' ")
			.append("AND HY.syoyuu_eturankengen = '1' ")
			.append("AND HY.kikan_tori_cd = '").append(form.getTori_cd()).append("' ");
		if(!GS.SATEIKAISYA_SJ.equals(form.getSateikaisya_cd()) && !GS.SATEIKAISYA_PN.equals(form.getSateikaisya_cd())){
			sql1.append("AND HY.mise_cd = '").append(cmnData.getMise_cd()).append("' ");
		}
		sql1.append("AND HY.syoyuu_kaisha_cd = '").append(form.getSateikaisya_cd()).append("' ")
			.append("AND NVL(BT.kigen_kbn, '0') = '0' ")
			.append("ORDER BY ")
			.append("tempu_kbn,tairyu_kbn,meisai_kbn DESC,BT.ins_dt DESC,HY.ins_dt DESC");
		try{
			// SQL実行		
			rs = sqlExec.execQuery(sql1.toString());
			
			ArrayList list = new ArrayList();

			// 明細情報格納カウンタ
			int i = 0;
			
			while ( rs.next() ) {
				// 明細Beanクラス生成
				HashMap map = new HashMap();
			    map.put(BUNSYO_NO, Function.trim(rs.getString(BUNSYO_NO)));
			    map.put(ANKEN_NO_EDA, Function.trim(rs.getString(ANKEN_NO_EDA)));	    
			    map.put(SYSTEM_KBN, rs.getString(SYSTEM_KBN));
			    map.put(MISE_CD, rs.getString(MISE_CD));
			    map.put(KIKAN_TORI_CD, rs.getString(KIKAN_TORI_CD));
			    map.put(SYOYUU_KAISHA_CD, rs.getString(SYOYUU_KAISHA_CD));
			    map.put(SYOYUU_SOSHIKI_CD, rs.getString(SYOYUU_SOSHIKI_CD));
			    map.put(SYOYUU_ETURANKENGEN, rs.getString(SYOYUU_ETURANKENGEN));
			    map.put(FILE_NM, rs.getString(FILE_NM));
			    map.put(JITU_FILE_NM, rs.getString(JITU_FILE_NM));	    
			    map.put(PHASE, Function.trim(rs.getString(PHASE)));	    
		    	map.put(TENPU_CHECK,CHK_BOX_OFF);
		    	map.put(SAKUJO_CHECK,CHK_BOX_OFF);
		    	map.put(TENPU_CHECK_DIS,FALSE);
		    	map.put(SAKUJO_CHECK_DIS,FALSE);
		    	map.put(ROW_COLOR,SHIROIRO);

		    	//滞留判定の場合、明細単位で処理
			    if(GS.PHASE_TAIRYU_HANTEI.equals(cmnData.getPhase()) || GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(cmnData.getPhase())){
			    	//自案件に添付されている場合、水色表示
			    	if(TEMPU_ZUMI.equals(rs.getString(TEMPU_KBN))){
				    	map.put(ROW_COLOR,MIZUIRO);
				    	//自明細に添付されている場合、デフォルトチェック
				    	if(TEMPU_ZUMI.equals(rs.getString(MEISAI_KBN))){
					    	map.put(TENPU_CHECK,CHK_BOX_ON);
					    //他明細に添付されている場合、チェックボックス使用不可
				    	}else{
					    	map.put(TENPU_CHECK_DIS,TRUE);
					    	map.put(SAKUJO_CHECK_DIS,TRUE);
				    	}
			    	}
				    //他案件に添付されている場合、削除チェックボックス使用不可
			    	if(TEMPU_ZUMI.equals(rs.getString(OTH_TEMPU_KBN))){
				    	map.put(SAKUJO_CHECK_DIS,TRUE);
			    	}
				//査定・引当金検証・引当金確認の場合
			    }else{
					//自案件に添付されている場合、水色表示でデフォルトチェック
			    	if(TEMPU_ZUMI.equals(rs.getString(TEMPU_KBN))){
				    	map.put(TENPU_CHECK,CHK_BOX_ON);
				    	map.put(ROW_COLOR,MIZUIRO);
			    	}
					//自案件の滞留時に添付されている場合、チェックボックス使用不可(査定フェーズのみ)
			    	if((!GS.PHASE_HIKIATEKIN_KENSYO.equals(cmnData.getPhase()) || !GS.PHASE_HIKIATEKIN_KAKUNIN.equals(cmnData.getPhase())) && TEMPU_ZUMI.equals(rs.getString(TAIRYU_KBN))){
				    	map.put(TENPU_CHECK,CHK_BOX_OFF);
				    	map.put(SAKUJO_CHECK_DIS,TRUE);
				    	map.put(TENPU_CHECK_DIS,TRUE);
			    	}
					//他案件に添付されている場合、削除チェックボックス使用不可
			    	if(TEMPU_ZUMI.equals(rs.getString(OTH_TEMPU_KBN))){
				    	map.put(SAKUJO_CHECK_DIS,TRUE);
			    	}
			    }
				// 明細配列に取得レコードを格納
			    list.add(i, map);
			    i++;   
			}
			// 明細配列をFormにセット
			form.setList(list);
		} finally {
			if(rs != null){
				try{
					rs.close();
				}catch (Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * 保有文書情報登録
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void setHoyubunsyo(FormFile formFile,TempFile tempFile) throws Exception{
		
		ResultSet rs = null;
		Ftp ftp = new Ftp();
		TempFileList tmpFileList = new TempFileList();
		
		// 登録値取得
	    String sysdate = sqlExec.getDate();
	    String system_kbn = form.getKikan_sys_kbn();
	    //String mise_cd = form.getMise_code();
	    String kikan_tori_cd = form.getTori_cd();
	    // No797, 2008/06/09, SJA渡辺, 所有会社コードには案件の査定会社コードを入れるように修正。
	    //String syoyuu_kaisha_cd = cmnData.getComSateiKaishaCd();
	    String syoyuu_kaisha_cd = form.getSateikaisya_cd();
	    String syoyuu_soshiki_cd = cmnData.getComSoshikiCd();
	    
	    	    
	    String mise_cd = cmnData.getMise_cd();
	    String syoyuu_eturankengen = "1";

	    // 選択ファイル名
	    String file_nm = formFile.getFileName();
	    
	    // ファイルサーバ登録ファイル名
	    String jitu_file_nm = null;

	    // 拡張子を取得。
	    String jitu_file_nm_ext = "";
	    int index = file_nm.lastIndexOf('.');
	    if (index!=-1){
	    	jitu_file_nm_ext = file_nm.substring(index, file_nm.length());
	    }

	    
	     formFile.getFileName();
	    long bunsyo_id_max = 0;
	    
		// 文書No最大値取得
		StringBuffer sql = new StringBuffer()
							.append("select ")
							.append("MAX(bunsyo_no) as maxNo ")
							.append("from SSM_HOYUBUNSYO");
	
		try{
			// SQL実行		
			rs = sqlExec.execQuery(sql.toString());	
			while(rs.next()){
				bunsyo_id_max=rs.getLong("maxNo")+1;
			}
			// 文書No＋拡張子がファイルサーバ登録ファイル名になる。
			jitu_file_nm = new StringBuffer()
						  .append(bunsyo_id_max)
						  .append(jitu_file_nm_ext).toString();
			
			// TempFileオブジェクトの登録ファイル名にセット。
			tempFile.setServerFilename(jitu_file_nm);
			
		    // トランザクション開始
		    sqlExec.beginTran();

		    // TODO　障害表：
		    
			// SQL【No2】保有文章情報登録
			StringBuffer sql2 = new StringBuffer()
								.append("insert into SSM_HOYUBUNSYO values('")
								.append(bunsyo_id_max)
								.append("', '")
								.append(system_kbn)
								.append("', '")
								.append(mise_cd)
								.append("', '")
								.append(kikan_tori_cd)
								.append("','")
								.append(syoyuu_kaisha_cd)
								.append("',");
						if(syoyuu_soshiki_cd != null){
							sql2.append("'")
								.append(syoyuu_soshiki_cd)
								.append("','");
						}else{
							sql2.append("null")
								.append(",'");
						}
							sql2.append(syoyuu_eturankengen)
								.append("', '")
								.append(file_nm)
								.append("', '")
								.append(jitu_file_nm)
								.append("', '")
								.append("0")
								.append("', '")
								.append("")
								.append("', '")
								.append(userId)
								.append("', TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
								.append(", '")
								.append(userId)
								.append("', TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
								.append(")");
		    sqlExec.addBatch(sql2.toString());

		    if(sqlExec.execBatch()){
			    //課題No.09
			    //削除開始
		    	// 登録したファイル情報をFormにセット。
		    	/*HashMap map = new HashMap();
		    	map.put("bunsyo_no", new String(new Long(bunsyo_id_max).toString()));
		    	map.put("system_kbn", system_kbn);
		    	map.put("mise_cd", mise_cd);
		    	map.put("kikan_tori_cd", kikan_tori_cd);
		    	map.put("syoyuu_kaisha_cd", syoyuu_kaisha_cd);
		    	map.put("syoyuu_soshiki_cd", syoyuu_soshiki_cd);
		    	map.put("syoyuu_eturankengen", syoyuu_eturankengen);
		    	map.put("file_nm", file_nm);
		    	map.put("jitu_file_nm", jitu_file_nm);
		    	map.put("tmp_file",tempFile);
		    	map.put("tenpuCheck","on");
		    	map.put("sakujoCheck","off");
// 障害票No522　2008/05/28　細野　英語モード対応			 
		    	map = getHyojiKbn(map,"phase",cmnData.getComLangMode());
		    	
		    	ArrayList list = form.getList();
		    	list.add(map);
		    	form.setList(list);
		    
		    	appContext.setActionForm(form);*/
			    //削除完了
		    	
		    	// 管理票No200808130001, SJA渡辺, 2008/08/13, OKボタン押下時にファイルサーバーにファイルを登録するように修正
		    	if(tempFile != null) {
		    		tmpFileList.add(tempFile);
		    	}
		    	
		    	if(tmpFileList.size() != 0){
					ftp.Put(tmpFileList);
				}
		    	
		    	tmpFileList.deleteAll();
		    }
		} finally{
			if(rs != null){
				try{
					rs.close();
				}catch(Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
		}
	}

	/**
	 * 文章添付情報登録
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean setBunsyotenpu(HashMap map) throws Exception{
		////////////////////////////////////////////////////////
		//障害票：593
		//チェックイン日：2008/6/5
		//対応者：SJA中島
		//概要：文書テーブルに存在する場合には、添付フラグを1にするように修正。
		////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////
		//障害票：593
		//チェックイン日：2008/6/5
		//対応者：SJA中島
		//概要：文書テーブルの添付フラグ削除対応
		////////////////////////////////////////////////////////
		String sysdate = sqlExec.getDate();
	    StringBuffer sql3 = new StringBuffer()
							.append("insert into SST_BUNSYOTEMPU ");
	    if(anken_no_eda != null){
	    	sql3.append("values('")
				.append(anken_no)
				.append("', '")
				.append(map.get("bunsyo_no"))
				.append("', '")
				.append(phase)
				.append("', '")
				.append(anken_no_eda)
				.append("', '")
				.append(userId)
				.append("', TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
				.append(", '")
				.append(userId)
				.append("', TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
				.append(")");
	    }else{
	    	sql3.append("(ANKEN_NO,BUNSYO_NO,PHASE,INS_USER,INS_DT,UPD_USER,UPD_DT) ")
				.append("values('")
				.append(anken_no)
				.append("', '")
				.append(map.get("bunsyo_no"))
				.append("', '")
				.append(phase)
				.append("', '")
				.append(userId)
				.append("', TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
				.append(", '")
				.append(userId)
				.append("', TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
				.append(")");
	    }
	    
		// トランザクション開始
	    sqlExec.beginTran();

	    sqlExec.addBatch(sql3.toString());

		return sqlExec.execBatch();
		
	}
	
	/**
	 * 文書添付削除
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean deleteBunsyotenpu(HashMap map) throws Exception{

		//////////////////////////////////////////////
		//障害票：237
		//チェックイン日：2008/5/23
		//対応者：SJA中島
		//概要：文書添付チェックボックスにチェックが無い場合、ONフラグを解除する。
		/////////////////////////////////////////////		 
    	map.put("tenpuCheck","off");
		
		// トランザクション開始
	    sqlExec.beginTran();

		////////////////////////////////////////////////////////
		//障害票：593
		//チェックイン日：2008/6/5
		//対応者：SJA中島
		//概要：文書テーブルを削除するのではなく、添付フラグを0にするように修正
		////////////////////////////////////////////////////////
		// SQL【No4】文章添付情報削除
		StringBuffer sql4 = new StringBuffer()
							.append("delete from SST_BUNSYOTEMPU where ")
							.append("anken_no = '")
							.append(anken_no)
							.append("' and  bunsyo_no = '")
							.append(map.get("bunsyo_no"))
							.append("'");

		if(anken_no_eda != null){
			sql4.append(" and  anken_no_eda = '")
				.append(anken_no_eda)
				.append("'");	
		}
		
	    sqlExec.addBatch(sql4.toString());

		return sqlExec.execBatch();
	}
	
	/**
	 * 保有文書マスタ削除
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean deleteHoyubunsyo(HashMap map) throws Exception{
	    String sysdate = sqlExec.getDate();
	    /////////////////////////////////////////////////
	    //障害票：237
	    //チェックイン日：2008/5/28
	    //対応者：SJA中島
	    //概要：文書添付テーブルに同一文書Noで複数案件あるならば削除しないように修正。
	    /////////////////////////////////////////////////
	    int bunsyo_cnt = 0;
	    // 文書添付テーブルに同一文書Noで複数案件あるならば、削除しない
		// 障害票：593　2008/6/6　SJA中島　文書添付テーブルに添付フラグを追加したことによる対応
	    ResultSet rs = null;
	    StringBuffer checkSql = new StringBuffer();
	    checkSql.append("SELECT COUNT(BT.BUNSYO_NO) as cnt ");
	    checkSql.append("FROM SSM_HOYUBUNSYO HY,SST_BUNSYOTEMPU BT ");
	    checkSql.append("WHERE HY.BUNSYO_NO=BT.BUNSYO_NO ");
	    checkSql.append("AND HY.BUNSYO_NO='");
	    checkSql.append(map.get("bunsyo_no"));
	    checkSql.append("'");
	    try{
	    	rs = sqlExec.execQuery(checkSql.toString());
	    	rs.next();
	    	bunsyo_cnt = rs.getInt("cnt");
	    	if(bunsyo_cnt > 1){
	    		return false;
	    	}
		}finally {
			if(rs != null){
				try{
					rs.close();
				}catch (Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
		}
	    
		if(bunsyo_cnt == 1){
			// 文書添付テーブルに一件だけ登録されていた場合、その文書を登録した案件と
			// 同じかチェックする。
			// 障害票：593　2008/6/6　SJA中島　文書添付テーブルに添付フラグを追加したことによる対応
		    StringBuffer checkBunsyoSql = new StringBuffer();
		    checkBunsyoSql.append("SELECT COUNT(BT.BUNSYO_NO) as cnt ");
		    checkBunsyoSql.append("FROM SST_BUNSYOTEMPU BT ");
		    checkBunsyoSql.append("WHERE BT.ANKEN_NO='");
		    checkBunsyoSql.append(anken_no);
		    checkBunsyoSql.append("' AND BT.BUNSYO_NO='");
		    checkBunsyoSql.append(map.get("bunsyo_no"));
		    checkBunsyoSql.append("'");
		    try{
		    	rs = sqlExec.execQuery(checkBunsyoSql.toString());
		    	rs.next();
		    	if(rs.getInt("cnt") == 0){
		    		return false;
		    	}
			}finally {
				if(rs != null){
					try{
						rs.close();
					}catch (Exception e){
		    			throw new SQLException(e.getMessage());
					}
				}
			}			
		}
		
		
		// トランザクション開始
	    sqlExec.beginTran();

		// SQL【No5】文章添付情報削除
		StringBuffer sql5 = new StringBuffer()
							.append("update SSM_HOYUBUNSYO set del_flg = '1' ,")
							.append(" del_dt = TO_DATE('").append(sysdate).append("', 'yyyy/mm/dd hh24:mi:ss')")
							.append(" where bunsyo_no = '")
							.append(map.get("bunsyo_no"))
							.append("'");
	    sqlExec.addBatch(sql5.toString());

	    return sqlExec.execBatch();
		
	}

	/**
	 * 文書添付削除
	 * @return
	 * @throws Exception
	 */
	public boolean deleteBunsyotenpu2(HashMap map) throws Exception{
		// トランザクション開始
	    sqlExec.beginTran();

		// SQL【No6】文章添付情報削除
		StringBuffer sql6 = new StringBuffer()
							.append("delete from SST_BUNSYOTEMPU where ")
							.append("bunsyo_no = '")
							.append(map.get("bunsyo_no"))
							.append("'");
	    sqlExec.addBatch(sql6.toString());

		return sqlExec.execBatch();
		
	}
	
	/**
	 * 文章添付テーブルに登録されているか確認。
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean checkBunsyoTemp(HashMap map) throws SQLException{
		boolean returnValue=true;
		ResultSet rs = null;
		long count = 0;

		/////////////////////////////////////////////////
		//障害票：237
		//チェックイン日：2008/5/29
		//対応者：SJA中島
		//概要：滞留判定時のみ、案件Noも条件に追加するように修正。
		/////////////////////////////////////////////////
		
		StringBuffer sql =  new StringBuffer();
		sql.append("select ");
		sql.append("count(bunsyo_no) as count ");
		sql.append("from SST_BUNSYOTEMPU where ");
	    //課題No.09
	    //削除開始
		//if(GS.PHASE_TAIRYU_HANTEI.equals(phase) ||
			//GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(phase)){
		sql.append("anken_no = '");
		sql.append(anken_no);
		sql.append("' and ");
		//}
	    //削除完了
		sql.append(" bunsyo_no = '");
		sql.append(map.get("bunsyo_no"));
		sql.append("'");
		try{
			rs = sqlExec.execQuery(sql.toString());	
			while(rs.next()){
				count=rs.getLong("count");
				if(count != 0){
					returnValue = false;
				}
			}
		}finally {
			if(rs != null){
				try{
					rs.close();
				}catch (Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
		}
		return returnValue;
	}
	
	/**
	 * 区分テーブルから区分名称を取得
	 * @param map
	 * @param key
	 * @param mode
	 * @param val
	 * @return
	 * @throws SQLException
	 */
	private HashMap getHyojiKbn(HashMap map,String key,String mode) throws SQLException {

		ResultSet rs = null;
		String phaseVal = null;
		try{
			/////////////////////////////////////////////////
			//障害票：237,385
			//チェックイン日：2008/5/29
			//対応者：SJA中島
			//概要：文書Noからと案件Noから文書のフェーズを取得。(二重登録防止)
			/////////////////////////////////////////////////
			StringBuffer phaseSql = new StringBuffer();
			phaseSql.append("SELECT phase ");
			phaseSql.append("FROM SST_BUNSYOTEMPU ");
			phaseSql.append("WHERE  ");
			phaseSql.append("bunsyo_no = '");
			phaseSql.append(map.get("bunsyo_no"));
			phaseSql.append("'");
			if(GS.PHASE_TAIRYU_HANTEI.equals(phase) ||
	            GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(phase)){
				phaseSql.append(" AND anken_no='");
				phaseSql.append(anken_no);
				phaseSql.append("'");
			}
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
	}
}
