/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.util;

import java.util.LinkedList;

/**
 * メール送信内容クラス
 * 
 */
public class MailEnvelope {
	
	private LinkedList to = new LinkedList(); // 宛先リスト
	private String body = "";		// 本文
	private String subject = "";	// 件名
	private TempFile Attachment = null; // 添付ファイル

	/**
	 * 宛先リストの取得を行う。
	 */
	public LinkedList getTo() {
		return to;
	}

	/**
	 * 宛先(メールアドレス)の追加を行う。
	 * 
	 * @param to 宛先
	 */
	public void addTo(String to) {
		to = Function.trim(to);
		if( to.length()==0 ) {
			return;
		}
		this.to.add(to);
	}

	/**
	 * 本文の取得を行う。
	 * 
	 * @return 本文
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * 本文の設定を行う。
	 * 
	 * @param body 本文
	 */
	public void setBody(String body) {
		this.body = body;
	}
	
	/**
	 * 件名の取得を行う。
	 * 
	 * @return 件名
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * 件名の設定を行う。
	 * 
	 * @param subject 件名
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * 添付ファイルの取得を行う。
	 * 
	 * @return 添付ファイル
	 */
	public TempFile getAttachment() {
		return Attachment;
	}
	
	/**
	 * 添付ファイルの設定を行う。
	 * 
	 * @param attachment 添付ファイル
	 */
	public void setAttachment(TempFile attachment) {
		Attachment = attachment;
	}
}