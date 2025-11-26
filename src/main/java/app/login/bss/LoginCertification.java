/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/4/13		SSC				1.5次版流用
******************************************************************************/
package app.login.bss;

import common.AppContext;
import common.global.GS;
import common.util.Log;
import common.util.Profile;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

/**
 * 
 */
public class LoginCertification {

	private String CLASSNAME = getClass().getName(); // クラス名

	private Log log = null;					// ＬＯＧ

	/**
	 * コンストラクタ
	 */
	public LoginCertification(AppContext appContext) {
		log = appContext.getLog();
	}
	
	/**
	 * 認証メソッド<br>
	 * 	認証サーバにユーザＩＤとパスワードを投げ、認証結果を受け取る。
	 * 	認証結果をそのまま返り値として呼び出し元へ返却する
	 * 
	 * @param userid ユーザID
	 * @param password パスワード
	 * @return boolean(認証OK/true 認証NG/false)
	 */
	public boolean execAtt( String userid, String password ) throws Exception {

/*		
		try{
		
			//InfoDirectory接続準備
			Hashtable env = new Hashtable();
			
			env.put("java.naming.factory.initial",
						ComAppUtil.getProperty("INFOD.FACTORY"));
			env.put("java.naming.provider.url",
						"ldap:" + ComAppUtil.getProperty("INFOD.URL") + ":389");
			env.put("java.naming.security.authentication",
						ComAppUtil.getProperty("INFOD.AUTH"));
			env.put("java.naming.security.principal",userid);
			env.put("java.naming.security.credentials",password);
//			env.put("java.naming.ldap.factory.socket",
//										"com.fujitsussl.FjSSLSocketFactory"); 
//			env.put("java.naming.security.protocol","ssl");
//			env.put("java.naming.referral",
//								ComAppUtil.getProperty("INFOD.REFER"));
			
			// SSL通信向けのシステム変数設定
			//properties prop = System.getProperties();
			//prop.put("user.sslenvfile","c:\ssl_env\sslconfig.cfg");
			//prop.put("user.ssllogidr","c:\ssl_env");
			
			DirContext context = null;
			
			try{
				//認証処理
				context = new InitialDirContext(env);
			}
			catch(NamingException e){
				return false;
			}
		}
        catch(Exception e){
        	throw new RuntimeException(e);
		}
*/
		
		try{
//			String infodEnabled = ComAppUtil.getProperty("INFOD.ENABLED");
			String infodEnabled = Profile.getString("INFOD.ENABLED","");
			
			if( infodEnabled == null ||
				! infodEnabled.equals("true")){
				// [テスト用]
				// ユーザIDが99999999の場合は存在エラーとする
//				if( "99999999".equals(userid) ) {
//					return false;
//				}
				return true;
			}
			
		}catch( Exception e){
			//ComAppUtil#getProperty()メソッドの例外
			// 発生しえない為無視する
		}
		
		DirContext context = null;
		String userDN = null;
		
		try{
			//検索専用ユーザによる接続
			
			String searchUserDN = null;
			String searchUserPassword = null;
			String baseJPDN = null;
			String baseENDN = null;
			
			try{
//				searchUserDN = ComAppUtil.getProperty("INFOD.SEARCHUSER.DN");
//				searchUserPassword = 
//						ComAppUtil.getProperty("INFOD.SEARCHUSER.PASSWORD");
//				baseJPDN = ComAppUtil.getProperty("INFOD.SEARCHBASE.JP");
//				baseENDN = ComAppUtil.getProperty("INFOD.SEARCHBASE.EN");
				
//				searchUserDN = "uid=group,o=admin,l=admin,c=jp";
//				searchUserPassword = "group"; 
//				baseJPDN = "l=japan,c=jp";
//				baseENDN = "l=world,c=jp";

				searchUserDN = Profile.getString("INFOD.SEARCHUSER.DN","");
				searchUserPassword = Profile.getString("INFOD.SEARCHUSER.PASSWORD","");
				baseJPDN = Profile.getString("INFOD.SEARCHBASE.JP","");
				baseENDN = Profile.getString("INFOD.SEARCHBASE.EN","");

			}catch( Exception e){
				//ComAppUtil#getProperty()メソッドの例外
				// 発生しえない為無視する
			}
			
			context = bindDirectoryService(searchUserDN,searchUserPassword);
			
			//ユーザIDからDNを特定
			userDN = findUserDN(context,baseJPDN,userid);
			
			if( userDN == null ){
				//検索ベース「日本語用」から見つからなかった場合は、
				//検索ベース「英語用」から再検索
				userDN = findUserDN(context,baseENDN,userid);
				
				if( userDN == null ){
					//ユーザが存在しなかった
//					ComLogUtil.putLog(50,ComLogUtil.LEV_DEBUG,
//							"認証に失敗しました。(ユーザ " + userid + 
//							"が存在しません)",null);
					log.write(GS.LOG_WAR,CLASSNAME,
							"認証に失敗しました。(ユーザ " + userid + 
							"が存在しません)");
							
					return false;
				}
			}
			
			closeContext(context);
			
			try{
				//ユーザ認証
				context = bindDirectoryService(userDN,password);
			}catch( AuthenticationException e){
				// J2sdk APIリファレンス - 抜粋 -
				//この例外は、ネーミングサービスまたはディレクトリサービスに
				//アクセスする際に認証エラーが発生した場合にスローされます。
				//認証エラーは、ユーザプログラムで提供される資格が無効である
				//などの理由でネーミングサービスまたはディレクトリサービスに
				//対してユーザを認証できなかった場合に発生します。 
//				ComLogUtil.putLog(50,ComLogUtil.LEV_DEBUG,
//						"認証に失敗しました。(ユーザ " + userid + 
//							"のパスワードに誤りがあります)",null);
				log.write(GS.LOG_WAR,CLASSNAME,
						"認証に失敗しました。(ユーザ " + userid + 
						"のパスワードに誤りがあります)");
				return false;
			}
			return true;
		}catch( NamingException e){
			// ★★★エラー処理の仕様不明のため保留
			//   ディレクトリサーバダウン、検索ユーザの認証失敗など・・・
//			ComLogUtil.putLog(10,ComLogUtil.LEV_ERROR,
//					"接続もしくはディレクトリサービスの操作に失敗しました。",
//					e);
//			e.printStackTrace();
//			return false;
			log.write(GS.LOG_ERR,CLASSNAME,
					"接続もしくはディレクトリサービスの操作に失敗しました。");
			throw e;
		}finally{
			closeContext(context);
		}

	}

	/**
	 * 指定したベースDNから、ユーザIDに紐付くユーザエントリを検索する。<br>
	 * 見つかった場合は、そのDN名を返す。<br>
	 * 見つからなかった場合は、 null を返す。<br>
	 * 
	 * @param context 接続済みのディレクトリコンテキスト
	 * @param baseDN 検索するベースDN
	 * @param userid 検索するユーザID
	 * @return ユーザIDに紐付くDN名
	 * @throws NamingException ディレクトリサービスの操作に失敗した場合
	 */
	private static String findUserDN(DirContext context,
				String baseDN,String userid)throws NamingException{
		
		SearchControls constraints = new SearchControls();
		constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
		
		NamingEnumeration results = context.search(
				baseDN,"uid=" + userid, constraints);
		
		if (results == null || results.hasMore() == false ) {
			//ユーザエントリが存在しない
			return null;
		}
		
		SearchResult si = (SearchResult)results.next();
		String s = si.getName() + "," + baseDN;
		results.close();
		return s;
	}
	
	/**
	 *ディレクトリサービスに接続を行い、DirContextを返す。
	 *
	 *@param userDN 接続ユーザのDN名
	 *@param password 接続ユーザのパスワード
	 *@return 接続済みのディレクトリコンテキスト
	 *@throws NamingException ディレクトリサービスへの接続に失敗した場合
	 */
	private static DirContext bindDirectoryService(String userDN,
								String password)throws NamingException{
		Hashtable env = new Hashtable(); 

		try{
//			env.put("java.naming.factory.initial",
//						ComAppUtil.getProperty("INFOD.FACTORY")); 
//			env.put("java.naming.provider.url",
//						ComAppUtil.getProperty("INFOD.URL")); 
			env.put("java.naming.factory.initial",
					Profile.getString("INFOD.FACTORY",""));
			env.put("java.naming.provider.url",
					Profile.getString("INFOD.URL",""));
			
			// 認証メカニズム
			//  none Anonymous認証を指定します。
			//  simple 簡易認証を指定します。
			//  external 証明書認証を指定します。
//			env.put("java.naming.security.authentication",
//						ComAppUtil.getProperty("INFOD.AUTH"));
			env.put("java.naming.security.authentication",
					Profile.getString("INFOD.AUTH",""));
			
			// DN名 - 認証を行うユーザのDN名を設定
			//  ex) cn=yokoyama, ou=landup, o=co, c=jp
			env.put("java.naming.security.principal",userDN);
			// パスワード - 認証を行うユーザのパスワードを設定
			env.put("java.naming.security.credentials",password);
			
			//env.put("java.naming.ldap.factory.socket",
			//			"com.fujitsussl.FjSSLSocketFactory");
			
			// 使用プロトコル
			//  ssl ディレクトリサーバとの通信にSSLプロトコルを使用する
			//  省略 ディレクトリサーバとの通信にSSLプロトコルを使用しない
			//env.put("java.naming.security.protocol","ssl");
			
			// リフェラル
			//  follow 参照に自動的に従います。
			//  throw 参照が見つかった場合、ReferralException をスローします。
//			env.put("java.naming.referral",
//						ComAppUtil.getProperty("INFOD.REFER"));
			env.put("java.naming.referral",
					Profile.getString("INFOD.REFER",""));

			// SSL通信向けのシステム変数設定
			//properties prop = System.getProperties();
			//prop.put("user.sslenvfile","c:\ssl_env\sslconfig.cfg");
			//prop.put("user.ssllogidr","c:\ssl_env");
			
		}catch( Exception e){
			//ComAppUtil#getProperty()メソッドの例外
			// 発生しえない為無視する
		}
		
		//ディレクトリサービス接続(認証)
		return (DirContext)new InitialDirContext(env);
	}

	/**
	 * コンテキストのクローズを行う
	 * 
	 * @param context コンテキスト
	 */
	private static void closeContext(Context context){
		try{
			if( context != null ){
				context.close();
			}
		}catch( NamingException e){
			//クローズの例外は無視
		}
	}
}
