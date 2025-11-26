/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.util;

import common.AppContext;
import common.global.GS;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * ＦＴＰクライアントクラス
 * 
 */
public class Ftp {

	//private String CLASSNAME = getClass().getName(); //クラス名
    //private Log log = new Log();
    private static String FTPHOST = Profile.getString(GS.PROFILE_FTPHOST,"");
	private static String FTPUSER = Profile.getString(GS.PROFILE_FTPUSER,"");
	private static String FTPPASS = Profile.getString(GS.PROFILE_FTPPASS,"");
	private static String FTPDEBUG = Profile.getString(GS.PROFILE_FTPDEBUG,"0");
	private static String FTPDIR = Profile.getString(GS.PROFILE_FTPDIR,"0");
	private static String FTPDIR_HELP = Profile.getString(GS.PROFILE_FTPDIR_HELP,"0");
	
    private static final int CTRLPORT = 21;
	private static final int TIMEOUT = 3000; // msec
	private static final int STAT_INIT = 0;
	private static final int STAT_CONN = 1;
	private static final int STAT_COMMAND = 2;
	private static final int STAT_WAITRESP = 3;
	private static final int TYPE_ASCII = 0;
	private static final int TYPE_EBCDIC = 1;
	private static final int TYPE_IMAGE = 2;

	private int status = STAT_INIT;
	private int type = TYPE_ASCII;
	private int timer = TIMEOUT;
	private Socket ctrlSock;
	private BufferedReader ctrlIn;
	private PrintWriter ctrlOut;
	private Socket dataSock;
	private InputStream dataIn;
	private OutputStream dataOut;
	private byte inetaddr[] = null;
	private boolean passive = false;

	/**
	 * ディレクトリ内のファイル一覧を取得する。
	 * 
	 * @param dir
	 * 			ディレクトリ
	 * @return FtpFileList
	 * 			ファイル一覧クラス
	 * @throws Exception
	 */
	public FtpFileList list(String dir) throws Exception {
		String[] sAry;
		String[] token;
		FtpFileList list = null;
		try {
			open(FTPHOST);
			login(FTPUSER, FTPPASS);
			sAry = ls("-l " + dir);
			list = new FtpFileList(sAry);
		} finally {
			quit();
		}
		return list;
	}
	
	/**
	 * リモートのファイルをＧＥＴし、一時ファイルを作成する。
	 * 
	 * @param remoteDir
	 * 			リモートのディレクトリ
	 * @param remoteFname
	 * 			リモートのファイル名
	 * @param localFname
	 * 			ローカルのファイル名
	 * @return TempFile
	 * 			一時ファイルクラス
	 * @throws Exception
	 */	
	public TempFile Get(String remoteDir, String remoteFname, String localFname) throws Exception {
		TempFile tmp = new TempFile(localFname);
		try {
			open(FTPHOST);
			login(FTPUSER, FTPPASS);
			binary();
			if( remoteDir.length() == 0 ) {
				get( remoteFname, tmp.getPath() );
			} else {
				get( remoteDir + "/" + remoteFname, tmp.getPath() );
			}
		} finally {
			quit();
		}
		return tmp;
	}

	public void GetHelp(String remoteDir, String remoteFname, String localFname) throws Exception {

		StringBuffer helpDir = new StringBuffer(AppContext.getHelpDir())
												.append(GS.SLASH)
												.append(localFname);
		StringBuffer remoDir = new StringBuffer(remoteDir)
												.append(GS.SLASH)
												.append(remoteFname);
		try {
			open(FTPHOST);
			login(FTPUSER, FTPPASS);
			binary();
			if(remoteDir.length() == 0) {
				get(remoteFname,helpDir.toString());
			} else {
				get(remoDir.toString(),helpDir.toString());
			}
		} finally {
			quit();
		}
	}

	/**
	 * プロパティに設定したリモートパスを使用してファイルをGET、一時ファイルを作成する。
	 * @param remoteFname
	 * @param localFname
	 * @return
	 * @throws Exception
	 * @see public TempFile Get(String remoteDir, String remoteFname, String localFname) throws Exception
	 */
	public TempFile Get(String remoteFname, String localFname) throws Exception {
		return Get(FTPDIR,remoteFname,localFname);
	}
	
	/**
	 * プロパティに設定したリモートパスを使用してヘルプ画像をGET、一時ファイルを作成する。
	 * @param remoteFname
	 * @param localFname
	 * @return
	 * @throws Exception
	 * @see public TempFile Get(String remoteDir, String remoteFname, String localFname) throws Exception
	 */
	public void getHelp(String remoteFname, String localFname) throws Exception {
		GetHelp(FTPDIR_HELP,remoteFname,localFname);
	}

	/**
	 * ローカルの複数ファイルをＰＵＴする。
	 * 
	 * @param localFileList
	 * 			リモートのファイル一覧クラス
	 * @param remoteDir
	 * 			リモートのディレクトリ
	 * @throws Exception
	 */	
	public void Put(TempFileList localFileList, String remoteDir) throws Exception {
		try {
			open(FTPHOST);
			login(FTPUSER, FTPPASS);
			//if( remoteDir.length() > 0 ) {
				//mkdir(remoteDir);
			//}
			binary();
			for( int i=0; i<localFileList.size(); i++ ) {
				if( remoteDir.length() == 0 ) {
					// TODO　putするファイル名をdisplayNameからserverFileNameに変更。
					put( localFileList.getPath(i), localFileList.getServerFilename(i) );
				} else {
					// TODO　putするファイル名をdisplayNameからserverFileNameに変更。
					put( localFileList.getPath(i), remoteDir + "/" + localFileList.getServerFilename(i) );
				}
			}
		} finally {
			quit();
		}
	}

	/**
	 * プロパティに設定したリモートパスを使用してファイルをPUTする。
	 * @param localFileList
	 * @throws Exception
	 * @see public void Put(TempFileList localFileList, String remoteDir) throws Exception
	 */
	public void Put(TempFileList localFileList) throws Exception {
		Put(localFileList,FTPDIR);
	}
	
	/**
	 * リモートのファイルを削除する。
	 * 
	 * @param dir
	 * 			リモートのディレクトリ
	 * @param fName
	 * 			リモートのファイル名
	 * @throws Exception
	 */	
	public void delete(String dir, String fName) throws Exception {
		try {
			open(FTPHOST);
			login(FTPUSER, FTPPASS);
			delete( dir + "/" + fName );
		} finally {
			quit();
		}
	}

	/**
	 * 指定したFTPサーバとの間でFTPセッションを開始します。
	 * 
	 * @param host
	 *            FTPサーバのホスト名もしくはIPアドレス
	 */
	private void open(String host) throws IOException {
		String resp;
		
		InetSocketAddress endpoint = new InetSocketAddress(host, CTRLPORT);

		ctrlSock = new Socket();
		ctrlSock.connect(endpoint,timer);
		ctrlSock.setSoTimeout(timer);
		ctrlIn = new BufferedReader(new InputStreamReader(ctrlSock
				.getInputStream()));
		ctrlOut = new PrintWriter(ctrlSock.getOutputStream());
		resp = waitResponse();
	}

	/**
	 * FTPサーバに対してFTPコマンドを発行し、レスポンスを受け取ります。
	 * 
	 * @param command
	 *            FTPサーバに対するリクエストコマンド（パラメータを含む）
	 * @return FTPサーバからのレスポンス文字列（コードを含む）
	 */
	private String doCommand(String command) throws IOException {
		String resp = null;

		ctrlOut.print(command + "\r\n");
		ctrlOut.flush();
		if(FTPDEBUG.equals("true")) {
			System.err.println("> " + command);
		}
		status = STAT_COMMAND;
		resp = waitResponse();

		return resp;
	}

	/**
	 * FTPサーバからのレスポンスを受信します。 レスポンスコードが400番以上の場合はIOExceptionが投げられます。
	 * 受信のための待ちうけ中にタイムアウトが発生した場合は、 SocketExceptionが投げられます。
	 * 
	 * @return FTPサーバからのレスポンス文字列（コードを含む）
	 */
	private String waitResponse() throws IOException {
		String resp = "";
		String line;
		int code;

		while (true) {
			line = ctrlIn.readLine();
			resp += line;
			if(FTPDEBUG.equals("true")) {
				System.err.println("< " + line);
			}
			if (line.length() >= 4 && line.substring(3, 4).equals(" ")) {
				break;
			}
		}

		code = Integer.parseInt(line.substring(0, 3));
		if (code >= 400) {
			throw new IOException(resp);
		}

		return resp;
	}

	/**
	 * 確立されたFTPセッションに対して、ログイン認証を行います。
	 * 
	 * @param user
	 *            ユーザID
	 * @param pass
	 *            パスワード
	 */
	private void login(String user, String pass) throws IOException {
		doCommand("USER " + user);
		doCommand("PASS " + pass);
	}

	/**
	 * 現在のFTPセッションを終了します。
	 */
	private void quit() throws IOException {
		doCommand("QUIT");
		if (ctrlSock != null) {
			ctrlSock.close();
			ctrlSock = null;
		}
		if (dataSock != null) {
			dataSock.close();
			dataSock = null;
		}
	}

	/**
	 * FTPサーバにNLSTコマンドを発行し、指定したディレクトリもしくはファイル名
	 * の一覧を返します。「*」などのワイルドカードの展開は行いませんので、 ファイル名を指定しても、同じ名前のファイル名が返されるだけなので意味は
	 * ありません。
	 * 
	 * @param dir
	 *            ディレクトリ名またはファイル名
	 * @return ファイル名を含む配列
	 */
	private String[] ls(String dir) throws IOException {
		String resp[] = null;
		Vector v = new Vector();
		byte buff[] = new byte[1024];

		ascii();

		dataSock = DataConnection("NLST " + dir);

		dataIn = dataSock.getInputStream();
		BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataIn));
		String file;
		while ((file = dataReader.readLine()) != null) {
			v.addElement(file);
		}
		dataIn.close();
		waitResponse();

		resp = new String[v.size()];
		for (int i = 0; i < resp.length; i++) {
			resp[i] = (String) v.elementAt(i);
		}
		return resp;
	}

	/**
	 * FTPサーバ上の現在のディレクトリを変更します。 ログイン直後はユーザのホームディレクトリですが、FTPサーバの設定により 異なります。
	 * 
	 * @param dir
	 *            移動先のディレクトリ名
	 */
//	private void cd(String dir) throws IOException {
//		doCommand("CWD " + dir);
//	}

	/**
	 * FTPサーバ上に新しいディレクトリを作成します。 指定された名前が既に存在している場合などの理由により、ディレクトリが
	 * 作成できなかった場合は、IOExceptionが投げられます。 実際にどのようなときにこの例外が投げられるかは、FTPサーバの返すレスポンス
	 * に依存します。
	 * 
	 * @param dir
	 *            作成するディレクトリ名
	 */
	//private void mkdir(String dir) throws IOException {
		//String[] part = dir.split("[/]");
		//String s = "";
		//for( int i=0; i<part.length; i++ ) {
			//if( i==0 ) {
				//s = part[0];
			//} else {
				//s += "/" + part[i];
			//}
			//try {
				//doCommand("MKD " + s);
			//} catch(IOException e){
				//log.write(GS.LOG_WAR,CLASSNAME,e.getCause().getMessage());
			//}
		//}
	//}

	/**
	 * FTPサーバ上のディレクトリを削除します。 通常は削除に失敗したときは、IOExceptionが投げられますが、FTPサーバの返す
	 * レスポンスに依存します。
	 * 
	 * @param dir
	 *            削除するディレクトリ名
	 */
//	private void rmdir(String dir) throws IOException {
//		doCommand("RMD " + dir);
//	}

	/**
	 * FTPサーバ上の現在のディレクトリを返します。 返される値はレスポンスコードを含み、形式はサーバに依存します。
	 * 
	 * @return 現在のディレクトリ情報
	 */
//	private String pwd() throws IOException {
//		String resp = doCommand("PWD");
//		return resp;
//	}
	
	private Socket DataConnection(String command) throws IOException {
		String cmd = "";
		Socket sock = null;
		ServerSocket servSock = null;
		try{
			if( passive ) {
				cmd = (doCommand("PASV "));
				//227 Entering Passive Mode (192,168,2,3,105,129)
				int start = cmd.indexOf('(');
	            int end = cmd.indexOf(')');
				String sockaddr = cmd.substring(start+1,end);
				String[] parts = sockaddr.split(",");
				String s_hostIP = parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
				int port = (Integer.parseInt(parts[4]) << 8) + Integer.parseInt(parts[5]);

				InetSocketAddress endpoint = new InetSocketAddress(s_hostIP, port);
				
				sock = new Socket();
				sock.connect(endpoint,timer);
				sock.setSoTimeout(timer);
				
				doCommand(command);

			} else {
				if (inetaddr == null)
					inetaddr = InetAddress.getLocalHost().getAddress();
		
				servSock = new ServerSocket(0, 1);

				for (int i = 0; i < 4; i++) {
					cmd += (inetaddr[i] & 0xff) + ",";
				}
		
				doCommand("PORT " + cmd + ((servSock.getLocalPort() / 256) & 0xff)
						+ "," + ((servSock.getLocalPort() % 256) & 0xff));

				doCommand(command);
				
				servSock.setSoTimeout(timer);
				sock = servSock.accept();
				sock.setSoTimeout(timer);
			}
		}finally{
			servSock.close();
		}

		return sock;
	}

	/**
	 * FTPサーバからローカルにファイルを受信します。
	 * 
	 * @param rfile
	 *            FTPサーバ上のファイル名
	 * @param lfile
	 *            ローカルファイル名
	 */
	private void get(String rfile, String lfile) throws IOException {
		FileOutputStream fos = null;
		byte buff[] = new byte[1024];
		int len = 0;
		dataSock = DataConnection("RETR " + rfile);

		dataIn = dataSock.getInputStream();
    	try{
    		fos = new FileOutputStream(lfile);
    		while ((len = dataIn.read(buff)) > 0) {
    			fos.write(buff, 0, len);
    		}
    		fos.close();
    		fos = null;
    	}finally{
    		if(fos != null){
        		fos.close();
        		fos = null;
    		}
    	}

		dataIn.close();
		waitResponse();
	}

	/**
	 * ローカルからFTPサーバにファイルを送信します。
	 * 
	 * @param lfile
	 *            ローカルファイル名
	 * @param rfile
	 *            FTPサーバ上のファイル名
	 */
	private void put(String lfile, String rfile) throws IOException {
		FileInputStream fis = null;
		byte buff[] = new byte[1024];
		int len = 0;

		dataSock = DataConnection("STOR " + rfile);

		dataOut = dataSock.getOutputStream();
    	try{
    		fis = new FileInputStream(lfile);
    		while ((len = fis.read(buff)) > 0) {
    			dataOut.write(buff, 0, len);
    		}
    		fis.close();
    		fis = null;
    	}finally{
    		if(fis != null){
        		fis.close();
        		fis = null;
    		}
    	}
		dataOut.close();
		waitResponse();
	}

	/**
	 * 転送モードをASCIIにします。
	 */
	private void ascii() throws IOException {
		doCommand("TYPE A");
		type = TYPE_ASCII;
	}

	/**
	 * 転送モードをBINARYにします。
	 */
	private void binary() throws IOException {
		doCommand("TYPE I");
		type = TYPE_IMAGE;
	}

	/**
	 * FTPサーバ上のファイルを削除します。
	 * 
	 * @param file
	 *            FTPサーバ上のファイル名
	 */
	private void delete(String file) throws IOException {
		doCommand("DELE " + file);
	}
}