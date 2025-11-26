/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
0001	09/05/15		SSC				1.5次版機能組込
******************************************************************************/
package app.common.form;

import app.hikiate.form.KakuninForm;
import app.hikiate.form.KensyoForm;
import app.satei.form.HikiateForm;
import app.system.form.KureemuMeisaiForm;
import app.system.form.KureemuSyosaiForm;
import app.tairyu.form.SyoninsyosaiForm;
import app.tairyu.form.SyosaiForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import java.io.File;
import java.util.ArrayList;

/**
 * 共通_添付選択 Formクラス
 */
public class TenpuSentakuForm extends ActionForm{
	
	// 添付ファイル名ボックスに指定されたファイルパス
	private String tenpu_file_path;
	// アップロードファイル
	private FormFile fileUp;
	// 表示用ファイル名
	private String hyoujiFileName;
	// 明細情報リスト
	private ArrayList list;
	// インデックス情報
	private String indexId;
	// 前画面からの取得情報(基幹システム区分)
	private String kikan_sys_kbn;
	// 前画面からの取得情報(店コード)
	private String mise_code;
	// 前画面からの取得情報(基幹取引先コード)
	private String tori_cd;
	// 案件No
	private String anken_no;
	// 案件No枝番
	private String anken_no_eda;
	
	// 滞留判定登録明細Form
	private SyosaiForm syosaiForm;
	
	// 滞留判定承認明細Form
	private SyoninsyosaiForm syoninSyosaiForm;
	
	// 引当金判定Form
	private HikiateForm hikiateForm;
	
	// 引当金検証Form
	private KensyoForm KensyoForm;

	// 引当金確認Form
	private KakuninForm KakuninForm;
	
	// クレーム債権再設定 明細一覧Form
	private KureemuMeisaiForm kureemuMeisaiForm;
	
	// クレーム債権再設定 明細詳細Form
	private KureemuSyosaiForm kureemuSyosaiForm;
	
    // No797, 2008/06/09, SJA渡辺, 案件の査定会社コードを保持するプロパティ追加
	// 前画面からの取得情報(査定会社コード)
	private String sateikaisya_cd;
	
	
	
	public String getSateikaisya_cd() {
		return sateikaisya_cd;
	}
	public void setSateikaisya_cd(String sateikaisya_cd) {
		this.sateikaisya_cd = sateikaisya_cd;
	}
	/**
	 * 処理回数制御
	 */
	private int initmode;
	
	public int getInitmode() {
		return initmode;
	}
	public void setInitmode(int initmode) {
		this.initmode = initmode;
	}
	
	public KureemuMeisaiForm getKureemuMeisaiForm() {
		return kureemuMeisaiForm;
	}
	public void setKureemuMeisaiForm(KureemuMeisaiForm kureemuMeisaiForm) {
		this.kureemuMeisaiForm = kureemuMeisaiForm;
	}
	public KureemuSyosaiForm getKureemuSyosaiForm() {
		return kureemuSyosaiForm;
	}
	public void setKureemuSyosaiForm(KureemuSyosaiForm kureemuSyosaiForm) {
		this.kureemuSyosaiForm = kureemuSyosaiForm;
	}
	/**
	 * @return hikiateForm を戻します。
	 */
	public HikiateForm getHikiateForm() {
		return hikiateForm;
	}
	/**
	 * @param hikiateForm hikiateForm を設定。
	 */
	public void setHikiateForm(HikiateForm hikiateForm) {
		this.hikiateForm = hikiateForm;
	}
	/**
	 * @return syoninSyosaiForm を戻します。
	 */
	public SyoninsyosaiForm getSyoninSyosaiForm() {
		return syoninSyosaiForm;
	}
	/**
	 * @param syoninSyosaiForm syoninSyosaiForm を設定。
	 */
	public void setSyoninSyosaiForm(SyoninsyosaiForm syoninSyosaiForm) {
		this.syoninSyosaiForm = syoninSyosaiForm;
	}
	/**
	 * @return syoninForm を戻します。
	 */
	public SyosaiForm getSyosaiForm() {
		return syosaiForm;
	}
	/**
	 * @param syoninForm syoninForm を設定。
	 */
	public void setSyosaiForm(SyosaiForm syosaiForm) {
		this.syosaiForm = syosaiForm;
	}
	/**
	 * @return anken_no を戻します。
	 */
	public String getAnken_no() {
		return anken_no;
	}
	/**
	 * @param anken_no anken_no を設定。
	 */
	public void setAnken_no(String anken_no) {
		this.anken_no = anken_no;
	}
	/**
	 * @return anken_no_eda を戻します。
	 */
	public String getAnken_no_eda() {
		return anken_no_eda;
	}
	/**
	 * @param anken_no_eda anken_no_eda を設定。
	 */
	public void setAnken_no_eda(String anken_no_eda) {
		this.anken_no_eda = anken_no_eda;
	}
	/**
	 * @return kikan_sys_kbn を戻します。
	 */
	public String getKikan_sys_kbn() {
		return kikan_sys_kbn;
	}
	/**
	 * @param kikan_sys_kbn kikan_sys_kbn を設定。
	 */
	public void setKikan_sys_kbn(String kikan_sys_kbn) {
		this.kikan_sys_kbn = kikan_sys_kbn;
	}
	/**
	 * @return mise_code を戻します。
	 */
	public String getMise_code() {
		return mise_code;
	}
	/**
	 * @param mise_code mise_code を設定。
	 */
	public void setMise_code(String mise_code) {
		this.mise_code = mise_code;
	}
	/**
	 * @return tori_cd を戻します。
	 */
	public String getTori_cd() {
		return tori_cd;
	}
	/**
	 * @param tori_cd tori_cd を設定。
	 */
	public void setTori_cd(String tori_cd) {
		this.tori_cd = tori_cd;
	}
	/**
	 * @return indexId を戻します。
	 */
	public String getIndexId() {
		return indexId;
	}
	/**
	 * @param indexId indexId を設定。
	 */
	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}
	/**
	 * @return list を戻します。
	 */
	public ArrayList getList() {
		return list;
	}
	/**
	 * @param list list を設定。
	 */
	public void setList(ArrayList list) {
		this.list = list;
	}
	/**
	 * @return fileName を戻します。
	 */
	public String getHyoujiFileName() {
		return hyoujiFileName;
	}
	/**
	 * @param fileName fileName を設定。
	 */
	public void setHyoujiFileName(String hyoujiFileName) {
		this.hyoujiFileName = hyoujiFileName;
	}
	/**
	 * @return tenpu_file_path を戻します。
	 */
	public String getTenpu_file_path() {
		return tenpu_file_path;
		
	}
	/**
	 * @param tenpu_file_path tenpu_file_path を設定。
	 */
	public void setTenpu_file_path(String tenpu_file_path) {
		this.tenpu_file_path = tenpu_file_path;
		File tmpFile = new File(tenpu_file_path);
		this.hyoujiFileName = tmpFile.getName();

	}
	/**
	 * @return fileUp を戻します。
	 */
	public FormFile getFileUp() {
		return fileUp;
	}
	/**
	 * @param fileUp fileUp を設定。
	 */
	public void setFileUp(FormFile fileUp) {
		this.fileUp = fileUp;
	}
	public KakuninForm getKakuninForm() {
		return KakuninForm;
	}
	public void setKakuninForm(KakuninForm kakuninForm) {
		KakuninForm = kakuninForm;
	}
	public KensyoForm getKensyoForm() {
		return KensyoForm;
	}
	public void setKensyoForm(KensyoForm kensyoForm) {
		KensyoForm = kensyoForm;
	}
}
