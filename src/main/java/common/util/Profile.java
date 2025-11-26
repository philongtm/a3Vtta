/******************************************************************************
 著作権情報				:
 使用JDK バージョン		: 1.4.2.05
 更新履歴
 No		日付			修正者			修正内容
 ******************************************************************************/
package common.util;

import common.global.GS;

import java.util.ResourceBundle;

/**
 * 設定ファイルの内容を取得するクラス
 * 「ApplicationResources.properties」プロパティファイルから値を取得します。
 *
 */
public class Profile {

    private static ResourceBundle rb;

    static {
        try {
            rb = ResourceBundle.getBundle(GS.PROPERTY_WEBSYSTEM);
        } catch (Exception e) {
            System.err.println("Warning: ResourceBundle not found: " + e.getMessage());
            rb = null;
        }
    }

    /**
     * 文字列の取得を行う。
     *
     * @param key          設定ファイルのキー
     * @param defaultValue キーが無い場合のreturn値
     * @return 文字列
     */
    public static String getString(String key, String defaultValue) {
        String value;
        try {
            value = rb.getString(key);
        } catch (Exception e) {
            value = "";
        }
        if (value.length() == 0) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 数値の取得を行う。
     *
     * @param key          設定ファイルのキー
     * @param defaultValue キーが無い場合のreturn値
     * @return 数値
     */
    public static int getInt(String key, int defaultValue) {
        int value;
        String strValue;
        strValue = getString(key, String.valueOf(defaultValue));
        try {
            value = Integer.parseInt(strValue);
        } catch (Exception e) {
            value = defaultValue;
        }
        return value;
    }
}