/******************************************************************************
 著作権情報				:
 使用JDK バージョン		: 1.4.2.05
 更新履歴
 No		日付			修正者			修正内容
 ******************************************************************************/
package common.util;

/**
 * ページャExceptionクラス
 * 
 *  
 */
public class PagerException extends Exception{
    /**
     * コンストラクタです。
     */
    public PagerException() {
        super();
    }

    /**
     * コンストラクタです。
     * @param message 詳細メッセージ
     */
    public PagerException(String message) {
        super(message);
    }

    /**
     * コンストラクタです。
     * @param cause 原因
     */
    public PagerException(Throwable cause) {
        super(cause);
    }

    /**
     * コンストラクタです。
     * @param message 詳細メッセージ
     * @param cause 原因
     */
    public PagerException(String message, Throwable cause) {
        super(message, cause);
    }

}