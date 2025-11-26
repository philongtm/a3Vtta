/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/21		SSC				1.5次版機能組込
******************************************************************************/
package app.commonZen.dbAcc;

import app.SessionDataZen;
import app.commonZen.form.TorihikisakiKubunHanteiSyokaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 取引先区分判定タブDBアクセスクラス
 */
public class TorihikisakiKubunHanteiSyokaiDbAcc extends CommonDbAcc {
	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;		// ＡＰＰコンテキスト

	private SessionDataZen cmnData = null;	// 共通セッションデータ
	private TorihikisakiKubunHanteiSyokaiForm form=null;
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public TorihikisakiKubunHanteiSyokaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//ビーン取得
		cmnData = appContext.getCMNZenRe();
		form = (TorihikisakiKubunHanteiSyokaiForm)appContext.getActionForm();
	}
	
	/**
	 * 検索SQL実行処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void execute() throws SQLException {
		
		///////////////////////////////////////
		//障害票：465
		//チェックイン日：2008/5/25
		//対応者：上田
		//概要：ResultSetの循環使用対応
		////////////////////////////////////////
//		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		// 査定案件No
		String satei_anken_no = cmnData.getSatei_anken_no();
		// 年月
		String ym = Function.removeDateSlash(cmnData.getYm());
		// フェーズ
		String phase = cmnData.getPhase();
		// DUNS NO
		String duns_no = cmnData.getDuns_no();
		// 区分判定チェック用
		String zero_no = "0";
		try {
			getTorihikisakiKubunHanteiForm(satei_anken_no, phase);
			// 障害票：551　チェックイン日：2008/5/31　対応者：SJA中島
			// 概要：一次査定フェーズ未処理の案件については区分概要については表示しない
			if(GS.PHASE_ICHIJI_SATEI.equals(form.getPhase()) &&
			   GS.STATUS_MISYORI.equals(form.getStatus())){
				// 取引先区分、債権区分をクリアしておく
				form.setTori_kbn_nm("");
				form.setSaiken_kbn_nm("");
			}else{
				// 取引先区分判定時に選択された判定によって出力する画面を選択する。
				if(!zero_no.equals(form.getSeijo_chk()) ||
				   !zero_no.equals(form.getYochui_chk())){
					// 正常先・要注意にチェックがあった場合
					form.setTori_kbn_nm(appContext.getMsg("msg.0012"));
					form.setSeijo_nm(appContext.getMsg("msg.0001"));
					form.setYochui_nm(appContext.getMsg("msg.0002"));
					form.setHantei_no("1");
				}else if(!zero_no.equals(form.getTyoka_chk()) ||
						  !zero_no.equals(form.getKanwa_chk()) ||
						  !zero_no.equals(form.getEntai_chk())
						){
					// 貸倒懸念先該当事由にチェックがあった場合
					form.setTori_kbn_nm(appContext.getMsg("msg.0013"));
					form.setTori_kbn_nm_detail(appContext.getMsg("msg.0015"));
					form.setTyoka_nm(appContext.getMsg("msg.0003"));
					form.setKanwa_nm(appContext.getMsg("msg.0004"));
					form.setEntai_nm(appContext.getMsg("msg.0005"));
					form.setHantei_no("2");
				}else if(!zero_no.equals(form.getHasanho_chk()) ||
						!zero_no.equals(form.getKaishaho_chk()) ||
						!zero_no.equals(form.getKoseho_chk()) ||
						!zero_no.equals(form.getSaiseho_chk()) ||
						!zero_no.equals(form.getShobun_chk()) ||
						!zero_no.equals(form.getSonota_chk())
						){
					// 破産更生先該当事由にチェックがあった場合
					form.setTori_kbn_nm(appContext.getMsg("msg.0014"));
					form.setTori_kbn_nm_detail(appContext.getMsg("msg.0016"));
					form.setHasanho_nm(appContext.getMsg("msg.0006"));
					form.setKaishaho_nm(appContext.getMsg("msg.0007"));
					form.setKoseho_nm(appContext.getMsg("msg.0008"));
					form.setSaiseho_nm(appContext.getMsg("msg.0009"));
					form.setShobun_nm(appContext.getMsg("msg.0010"));
					form.setSonota_nm(appContext.getMsg("msg.0011"));
					form.setHantei_no("3");
				}
			}
		}finally{
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			//			if(rs != null){
			//				try{
			//					rs.close();
			//				}catch(Exception e){
			//	    			throw new SQLException(e.getMessage());
			//				}
			//			}
			if(rs1 != null){
				try{
					rs1.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
			if(rs2 != null){
				try{
					rs2.close();
				}catch(Exception e){
					throw new SQLException(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * @param satei_anken_no
	 * @param phase
	 * @throws SQLException
	 */
	public TorihikisakiKubunHanteiSyokaiForm getTorihikisakiKubunHanteiForm(String satei_anken_no, String phase) throws SQLException {
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		
		form.setSeijo_chk("0");
		form.setYochui_chk("0");
		form.setTyoka_chk("0");
		form.setKanwa_chk("0");
		form.setEntai_chk("0");
		form.setHasanho_chk("0");
		form.setKaishaho_chk("0");
		form.setKoseho_chk("0");
		form.setSaiseho_chk("0");
		form.setShobun_chk("0");
		form.setSonota_chk("0");
		
		
        ///////////////////////////////////////////////////
		//障害票：453
		//チェックイン日：2008/5/28
		//対応者：SJA渡辺
		//概要：画面のメソッドと帳票のメソッドを共通化。
		///////////////////////////////////////////////////
		
		// 親会社取得
		StringBuffer sql1 = new StringBuffer();
		
		sql1.append(" SELECT");
		sql1.append(" SS.PHASE,");
		sql1.append(" SS.STATUS,");
		sql1.append(" SK.KTK,");
		sql1.append(" SK.OYA_KTK,");
		sql1.append(" SK.OYA_ITTAI_FLG,");
		sql1.append(" SK.OYA_DOKURITU_FLG,");
		sql1.append(" TRIM(SK.oya_duns_no) oya_duns_no,");
		sql1.append(" TM.BUSINESS_NM_KJ BUSINESS_NM_KJ,");
		sql1.append(" TM.BUSINESS_NM BUSINESS_NM");
		sql1.append(" FROM ");
		sql1.append(" SST_SATEI_STAT SS ");
		sql1.append(" INNER JOIN SSE_TAIHI ST");
		sql1.append(" ON SS.YM = ST.YM");
		sql1.append(" AND SS.MISE_CD = ST.OFFICE_CD");
		sql1.append(" AND SS.SYSTEM_KBN = ST.SYSTEM_KBN");
		sql1.append(" AND CONCAT(TRIM(SS.KIKAN_TORI_CD), '00') = ST.KIKAN_TORI_CD");
		sql1.append(" INNER JOIN SSE_KTK SK");
		sql1.append(" ON ST.YM = SK.YM");
		sql1.append(" AND ST.SIKIBETU_CD = SK.SIKIBETU_CD");
		sql1.append(" AND ST.TOGO_TORI_CD = SK.DUNS_NO");
		sql1.append(" AND ST.SYORI_KAISU = SK.SYORI_KAISU");
		sql1.append(" LEFT OUTER JOIN TM_TOGO_MST@VIR_SJLMA TM");
		sql1.append(" ON SK.OYA_DUNS_NO = TM.TOGO_TORI_CD");
		sql1.append(" WHERE SS.ANKEN_NO = ");
		sql1.append("'").append(satei_anken_no).append("'");

		try{
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			//SQL実行		
//			rs = sqlExec.execQuery(sql1.toString());
//			InputCheck check = new InputCheck();
//			while(rs.next()){
//				if(!rs.isLast()){
//					// 処理回数が最も多いものを取得する。
//					continue;
//				}
//				form.setOya_ktk(rs.getString("oya_ktk"));
//				form.setKtk(rs.getString("ktk"));
//				form.setOya_ittai_flg(rs.getString("oya_ittai_flg"));
//				form.setOya_dokuritu_flg(rs.getString("oya_dokuritu_flg"));
//				form.setOya_duns_no(rs.getString("oya_duns_no"));
//				form.setBusiness_nm(rs.getString("BUSINESS_NM"));
//				if ("Ja".equals(cmnData.getComLangMode()) && !(check.isNullBlank(rs.getString("BUSINESS_NM_KJ")))) {
//					form.setBusiness_nm_kj(rs.getString("BUSINESS_NM_KJ"));
//				} else {
//					form.setBusiness_nm_kj(rs.getString("BUSINESS_NM"));
//				}
//			}
			rs1 = sqlExec.execQuery(sql1.toString());
			InputCheck check = new InputCheck();
			while(rs1.next()){
				if(!rs1.isLast()){
					// 処理回数が最も多いものを取得する。
					continue;
				}
				form.setOya_ktk(rs1.getString("oya_ktk"));
				form.setKtk(rs1.getString("ktk"));
				form.setOya_ittai_flg(rs1.getString("oya_ittai_flg"));
				form.setOya_dokuritu_flg(rs1.getString("oya_dokuritu_flg"));
				form.setOya_duns_no(rs1.getString("oya_duns_no"));
				form.setBusiness_nm(rs1.getString("BUSINESS_NM"));
				if ("Ja".equals(cmnData.getComLangMode()) && !(check.isNullBlank(rs1.getString("BUSINESS_NM_KJ")))) {
					form.setBusiness_nm_kj(rs1.getString("BUSINESS_NM_KJ"));
				} else {
					form.setBusiness_nm_kj(rs1.getString("BUSINESS_NM"));
				}
				
				// 障害票：551　チェックイン日：2008/5/31　対応者：SJA中島　概要：案件のステータスを追加
				form.setStatus(rs1.getString("STATUS"));
				form.setPhase(rs1.getString("PHASE"));
			}
		
		
			StringBuffer sql = new StringBuffer()
							.append("select ")
							.append("ST.seijo_chk,")
							.append("ST.yochui_chk,")
							.append("ST.tyoka_chk,")
							.append("ST.kanwa_chk,")
							.append("ST.entai_chk,")
							.append("ST.hasanho_chk,")
							.append("ST.kaishaho_chk,")
							.append("ST.koseho_chk,")
							.append("ST.saiseho_chk,")
							.append("ST.shobun_chk,")
							.append("ST.sonota_chk,")
							.append("ST.saiken_kbn,")
							.append("ST.tairyu_kbn ")
							//.append(",ST.tairyu_kbn_nm ")
							.append(" from")
							.append(" SST_SATEI ST ")
							.append(" where ")
							.append(" ST.anken_no='")
							.append(satei_anken_no)
							.append("' and ST.phase='")
							.append(phase)
							.append("'");
		
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
//			rs = sqlExec.execQuery(sql.toString());
//			while(rs.next()){
//				form.setSeijo_chk(rs.getString("seijo_chk"));
//				form.setYochui_chk(rs.getString("yochui_chk"));
//				form.setTyoka_chk(rs.getString("tyoka_chk"));
//				form.setKanwa_chk(rs.getString("kanwa_chk"));
//				form.setEntai_chk(rs.getString("entai_chk"));
//				form.setHasanho_chk(rs.getString("hasanho_chk"));
//				form.setKaishaho_chk(rs.getString("kaishaho_chk"));
//				form.setKoseho_chk(rs.getString("koseho_chk"));
//				form.setSaiseho_chk(rs.getString("saiseho_chk"));
//				form.setShobun_chk(rs.getString("shobun_chk"));
//				form.setSonota_chk(rs.getString("sonota_chk"));
//				form.setSaiken_kbn(rs.getString("saiken_kbn"));
//				form.setTairyu_kbn(rs.getString("tairyu_kbn"));
//				form.setTairyu_kbn_nm(rs.getString("tairyu_kbn_nm"));
//			}
			rs2 = sqlExec.execQuery(sql.toString());
			while(rs2.next()){
				form.setSeijo_chk(rs2.getString("seijo_chk"));
				form.setYochui_chk(rs2.getString("yochui_chk"));
				form.setTyoka_chk(rs2.getString("tyoka_chk"));
				form.setKanwa_chk(rs2.getString("kanwa_chk"));
				form.setEntai_chk(rs2.getString("entai_chk"));
				form.setHasanho_chk(rs2.getString("hasanho_chk"));
				form.setKaishaho_chk(rs2.getString("kaishaho_chk"));
				form.setKoseho_chk(rs2.getString("koseho_chk"));
				form.setSaiseho_chk(rs2.getString("saiseho_chk"));
				form.setShobun_chk(rs2.getString("shobun_chk"));
				form.setSonota_chk(rs2.getString("sonota_chk"));
				form.setSaiken_kbn(rs2.getString("saiken_kbn"));
				form.setTairyu_kbn(rs2.getString("tairyu_kbn"));
				//form.setTairyu_kbn_nm(rs2.getString("tairyu_kbn_nm"));
			}
			
			// 区分テーブルより、名称取得	
			form.setTairyu_kbn_cd(setKbnNM(form,"tairyu_kbn",form.getTairyu_kbn()));
			// No765, 2008/06/06, SJA平道, 滞留区分名称は区分テーブルより取得（英語モード対応）
			form.setTairyu_kbn_nm(setKbnNM(form,"tairyu_kbn_nm",form.getTairyu_kbn()));
			form.setSaiken_kbn_nm(setKbnNM(form,"saiken_kbn",form.getSaiken_kbn()));			
			// コメント取得
			getCommentVal(form,satei_anken_no,phase);
			
			return form;
			
		}finally{
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
//			if(rs != null){
//				try{
//					rs.close();
//				}catch(Exception e){
//	    			throw new SQLException(e.getMessage());
//				}
//			}
			if(rs1 != null){
				try{
					rs1.close();
				}catch(Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
			if(rs2 != null){
				try{
					rs2.close();
				}catch(Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
		}
	}

	/**
	 * コメント欄に出力する文字列をFORMにセットする。
	 * @param form
	 * @throws SQLException
	 */
	private void getCommentVal(TorihikisakiKubunHanteiSyokaiForm form,String satei_anken_no,String phase) throws SQLException{
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer()
						  .append("select ")
						  .append("CM.comment_val,")
						  .append("CM.toroku_point")
						  .append(" from SST_COMMENT CM ")
						  .append(" where ")
						  .append(" CM.anken_no = '")
						  .append(satei_anken_no)
						  .append("' and CM.phase='")
						  .append(phase)
						  .append("' and CM.toroku_point in ('20','30','40')");
		
		try{
			rs = sqlExec.execQuery(sql.toString());
			while(rs.next()){
				String point = rs.getString("toroku_point");
				///////////////////////////////////////////
				//障害表：482,486
				//チェックイン日：2008/5/29
				//対応者：SJA中島
				//概要：改行コードをBRタグに置き換える。
				///////////////////////////////////////////
				if("20".equals(point)){
					form.setComment_val_20(rs.getString("comment_val"));
				}else if("30".equals(point)){
					form.setComment_val_30(rs.getString("comment_val"));
				}else{
					// 40:債券区分判定発生経緯しか入ってこないはず。
					form.setComment_val_40(rs.getString("comment_val"));
				}
			}
		}finally{
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
	 * 区分テーブルから名称を取得し、返します。
	 * @param form
	 * @param key
	 * @param val
	 * @return
	 * @throws SQLException
	 */
	private String setKbnNM(TorihikisakiKubunHanteiSyokaiForm form,
							String key,
							String val) throws SQLException{
		StringBuffer sql = new StringBuffer()
						.append("select KBN_HYOUJI_VAL from SSP_KBN where ")
						.append("TRIM(KBN_KEY)='")
						.append(key)
						.append("' and LANG_MODE='")
						.append(cmnData.getComLangMode())
						.append("' and SYSTEM_KBN ='")
						.append(cmnData.getSystem_kbn())
						.append("' and KBN_VAL='")
						.append(val)
						.append("'");

		ResultSet rs = null;
		String rtn_val = null;
		try{
			rs = sqlExec.execQuery(sql.toString());
			while(rs.next()){
				rtn_val = rs.getString("KBN_HYOUJI_VAL");
			}
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
		}
		return rtn_val;
	}
}
