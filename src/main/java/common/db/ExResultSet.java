/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2014/09/09		SSC				ICIS（BJ201407061）
******************************************************************************/
package common.db;

import common.util.Function;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * 文字化け対策済みResultSetクラス
 *
 */
public class ExResultSet implements ResultSet {

	private ResultSet rs;
	private Statement statement = null;
	private PreparedStatement pstmt = null;
	private CallableStatement cstmt = null;

	/**
	 * クラスのコンストラクタ
	 *
	 * @param rs
	 */
	public ExResultSet(ResultSet rs,Statement statement) {
		this.rs = rs;
		this.statement = statement;
	}

	public ExResultSet(ResultSet rs,PreparedStatement pstmt) {
		this.rs = rs;
		this.pstmt = pstmt;
	}
	public ExResultSet(ResultSet rs,CallableStatement cstmt) {
		this.rs = rs;
		this.cstmt = cstmt;
	}
	public ExResultSet(ResultSet rs) {
		this.rs = rs;
	}

	/**
	 * @see ResultSet#getConcurrency()
	 */
	public int getConcurrency() throws SQLException {
		return rs.getConcurrency();
	}

	/**
	 * @see ResultSet#getFetchDirection()
	 */
	public int getFetchDirection() throws SQLException {
		return rs.getFetchDirection();
	}

	/**
	 * @see ResultSet#getFetchSize()
	 */
	public int getFetchSize() throws SQLException {
		return rs.getFetchSize();
	}

	/**
	 * @see ResultSet#getRow()
	 */
	public int getRow() throws SQLException {
		return rs.getRow();
	}

	/**
	 * @see ResultSet#getType()
	 */
	public int getType() throws SQLException {
		return rs.getType();
	}

	/**
	 * @see ResultSet#afterLast()
	 */
	public void afterLast() throws SQLException {
		rs.afterLast();
	}

	/**
	 * @see ResultSet#beforeFirst()
	 */
	public void beforeFirst() throws SQLException {
		rs.beforeFirst();
	}

	/**
	 * @see ResultSet#cancelRowUpdates()
	 */
	public void cancelRowUpdates() throws SQLException {
		rs.cancelRowUpdates();
	}

	/**
	 * @see ResultSet#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		rs.clearWarnings();
	}

	/**
	 * @see ResultSet#close()
	 */
	public void close() throws SQLException {
		SQLException sqlException = null;
		if(rs!=null) try{rs.close();}catch(SQLException e){sqlException = e;}
		if(statement!=null) try{statement.close();}catch(SQLException e){sqlException = e;}
		if(pstmt!=null) try{pstmt.close();}catch(SQLException e){sqlException = e;}
		if(cstmt!=null) try{cstmt.close();}catch(SQLException e){sqlException = e;}
		if(sqlException != null) throw sqlException;
		rs = null;
		statement = null;
		pstmt = null;
		cstmt = null;
	}

	/**
	 * @see ResultSet#deleteRow()
	 */
	public void deleteRow() throws SQLException {
		rs.deleteRow();
	}

	/**
	 * @see ResultSet#insertRow()
	 */
	public void insertRow() throws SQLException {
		rs.insertRow();
	}

	/**
	 * @see ResultSet#moveToCurrentRow()
	 */
	public void moveToCurrentRow() throws SQLException {
		rs.moveToCurrentRow();
	}

	/**
	 * @see ResultSet#moveToInsertRow()
	 */
	public void moveToInsertRow() throws SQLException {
		rs.moveToInsertRow();
	}

	/**
	 * @see ResultSet#refreshRow()
	 */
	public void refreshRow() throws SQLException {
		rs.refreshRow();
	}

	/**
	 * @see ResultSet#updateRow()
	 */
	public void updateRow() throws SQLException {
		rs.updateRow();
	}

	/**
	 * @see ResultSet#first()
	 */
	public boolean first() throws SQLException {
		return rs.first();
	}

	/**
	 * @see ResultSet#isAfterLast()
	 */
	public boolean isAfterLast() throws SQLException {
		return rs.isAfterLast();
	}

	/**
	 * @see ResultSet#isBeforeFirst()
	 */
	public boolean isBeforeFirst() throws SQLException {
		return rs.isBeforeFirst();
	}

	/**
	 * @see ResultSet#isFirst()
	 */
	public boolean isFirst() throws SQLException {
		return rs.isFirst();
	}

	/**
	 * @see ResultSet#isLast()
	 */
	public boolean isLast() throws SQLException {
		return rs.isLast();
	}

	/**
	 * @see ResultSet#last()
	 */
	public boolean last() throws SQLException {
		return rs.last();
	}

	/**
	 * @see ResultSet#next()
	 */
	public boolean next() throws SQLException {
		return rs.next();
	}

	/**
	 * @see ResultSet#previous()
	 */
	public boolean previous() throws SQLException {
		return rs.previous();
	}

	/**
	 * @see ResultSet#rowDeleted()
	 */
	public boolean rowDeleted() throws SQLException {
		return rs.rowDeleted();
	}

	/**
	 * @see ResultSet#rowInserted()
	 */
	public boolean rowInserted() throws SQLException {
		return rs.rowInserted();
	}

	/**
	 * @see ResultSet#rowUpdated()
	 */
	public boolean rowUpdated() throws SQLException {
		return rs.rowUpdated();
	}

	/**
	 * @see ResultSet#wasNull()
	 */
	public boolean wasNull() throws SQLException {
		return rs.wasNull();
	}

	/**
	 * @see ResultSet#getByte(int)
	 */
	public byte getByte(int columnIndex) throws SQLException {
		return rs.getByte(columnIndex);
	}

	/**
	 * @see ResultSet#getDouble(int)
	 */
	public double getDouble(int columnIndex) throws SQLException {
		return rs.getDouble(columnIndex);
	}

	/**
	 * @see ResultSet#getFloat(int)
	 */
	public float getFloat(int columnIndex) throws SQLException {
		return rs.getFloat(columnIndex);
	}

	/**
	 * @see ResultSet#getInt(int)
	 */
	public int getInt(int columnIndex) throws SQLException {
		return rs.getInt(columnIndex);
	}

	/**
	 * @see ResultSet#getLong(int)
	 */
	public long getLong(int columnIndex) throws SQLException {
		return rs.getLong(columnIndex);
	}

	/**
	 * @see ResultSet#getShort(int)
	 */
	public short getShort(int columnIndex) throws SQLException {
		return rs.getShort(columnIndex);
	}

	/**
	 * @see ResultSet#setFetchDirection(int)
	 */
	public void setFetchDirection(int direction) throws SQLException {
		rs.setFetchDirection(direction);
	}

	/**
	 * @see ResultSet#setFetchSize(int)
	 */
	public void setFetchSize(int rows) throws SQLException {
		rs.setFetchSize(rows);
	}

	/**
	 * @see ResultSet#updateNull(int)
	 */
	public void updateNull(int columnIndex) throws SQLException {
		rs.updateNull(columnIndex);
	}

	/**
	 * @see ResultSet#absolute(int)
	 */
	public boolean absolute(int row) throws SQLException {
		return rs.absolute(row);
	}

	/**
	 * @see ResultSet#getBoolean(int)
	 */
	public boolean getBoolean(int columnIndex) throws SQLException {
		return rs.getBoolean(columnIndex);
	}

	/**
	 * @see ResultSet#relative(int)
	 */
	public boolean relative(int rows) throws SQLException {
		return rs.relative(rows);
	}

	/**
	 * @see ResultSet#getBytes(int)
	 */
	public byte[] getBytes(int columnIndex) throws SQLException {
		return rs.getBytes(columnIndex);
	}

	/**
	 * @see ResultSet#updateByte(int, byte)
	 */
	public void updateByte(int columnIndex, byte x) throws SQLException {
		rs.updateByte(columnIndex, x);
	}

	/**
	 * @see ResultSet#updateDouble(int, double)
	 */
	public void updateDouble(int columnIndex, double x) throws SQLException {
		rs.updateDouble(columnIndex, x);
	}

	/**
	 * @see ResultSet#updateFloat(int, float)
	 */
	public void updateFloat(int columnIndex, float x) throws SQLException {
		rs.updateFloat(columnIndex, x);
	}

	/**
	 * @see ResultSet#updateInt(int, int)
	 */
	public void updateInt(int columnIndex, int x) throws SQLException {
		rs.updateInt(columnIndex, x);
	}

	/**
	 * @see ResultSet#updateLong(int, long)
	 */
	public void updateLong(int columnIndex, long x) throws SQLException {
		rs.updateLong(columnIndex, x);
	}

	/**
	 * @see ResultSet#updateShort(int, short)
	 */
	public void updateShort(int columnIndex, short x) throws SQLException {
		rs.updateShort(columnIndex, x);
	}

	/**
	 * @see ResultSet#updateBoolean(int, boolean)
	 */
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		rs.updateBoolean(columnIndex, x);
	}

	/**
	 * @see ResultSet#updateBytes(int, byte[])
	 */
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		rs.updateBytes(columnIndex, x);
	}

	/**
	 * @see ResultSet#getAsciiStream(int)
	 */
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return rs.getAsciiStream(columnIndex);
	}

	/**
	 * @see ResultSet#getBinaryStream(int)
	 */
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return rs.getBinaryStream(columnIndex);
	}

	/**
	 * @see ResultSet#getUnicodeStream(int)
     * @deprecated
	 */
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return rs.getUnicodeStream(columnIndex);
	}

	/**
	 * @see ResultSet#updateAsciiStream(int, InputStream, int)
	 */
	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		rs.updateAsciiStream(columnIndex, x, length);
	}

	/**
	 * @see ResultSet#updateBinaryStream(int, InputStream, int)
	 */
	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		rs.updateBinaryStream(columnIndex, x, length);
	}

	/**
	 * @see ResultSet#getCharacterStream(int)
	 */
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return rs.getCharacterStream(columnIndex);
	}

	/**
	 * @see ResultSet#updateCharacterStream(int, Reader, int)
	 */
	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		rs.updateCharacterStream(columnIndex, x, length);
	}

	/**
	 * @see ResultSet#getObject(int)
	 */
	public Object getObject(int columnIndex) throws SQLException {
		return rs.getObject(columnIndex);
	}

	/**
	 * @see ResultSet#updateObject(int, Object)
	 */
	public void updateObject(int columnIndex, Object x) throws SQLException {
		rs.updateObject(columnIndex, x);
	}

	/**
	 * @see ResultSet#updateObject(int, Object, int)
	 */
	public void updateObject(int columnIndex, Object x, int scale)
			throws SQLException {
		rs.updateObject(columnIndex, x, scale);
	}

	/**
	 * @see ResultSet#getCursorName()
	 */
	public String getCursorName() throws SQLException {
		return rs.getCursorName();
	}

	/**
	 * @see ResultSet#getString(int)
	 */
	public String getString(int columnIndex) throws SQLException {
		return Function.convert(rs.getString(columnIndex));
	}

	/**
	 * @see ResultSet#updateString(int, String)
	 */
	public void updateString(int columnIndex, String x) throws SQLException {
		rs.updateString(columnIndex, x);
	}

	/**
	 * @see ResultSet#getByte(String)
	 */
	public byte getByte(String columnName) throws SQLException {
		return rs.getByte(columnName);
	}

	/**
	 * @see ResultSet#getDouble(String)
	 */
	public double getDouble(String columnName) throws SQLException {
		return rs.getDouble(columnName);
	}

	/**
	 * @see ResultSet#getFloat(String)
	 */
	public float getFloat(String columnName) throws SQLException {
		return rs.getFloat(columnName);
	}

	/**
	 * @see ResultSet#findColumn(String)
	 */
	public int findColumn(String columnName) throws SQLException {
		return rs.findColumn(columnName);
	}

	/**
	 * @see ResultSet#getInt(String)
	 */
	public int getInt(String columnName) throws SQLException {
		return rs.getInt(columnName);
	}

	/**
	 * @see ResultSet#getLong(String)
	 */
	public long getLong(String columnName) throws SQLException {
		return rs.getLong(columnName);
	}

	/**
	 * @see ResultSet#getShort(String)
	 */
	public short getShort(String columnName) throws SQLException {
		return rs.getShort(columnName);
	}

	/**
	 * @see ResultSet#updateNull(String)
	 */
	public void updateNull(String columnName) throws SQLException {
		rs.updateNull(columnName);
	}

	/**
	 * @see ResultSet#getBoolean(String)
	 */
	public boolean getBoolean(String columnName) throws SQLException {
		return rs.getBoolean(columnName);
	}

	/**
	 * @see ResultSet#getBytes(String)
	 */
	public byte[] getBytes(String columnName) throws SQLException {
		return rs.getBytes(columnName);
	}

	/**
	 * @see ResultSet#updateByte(String, byte)
	 */
	public void updateByte(String columnName, byte x) throws SQLException {
		rs.updateByte(columnName, x);
	}

	/**
	 * @see ResultSet#updateDouble(String, double)
	 */
	public void updateDouble(String columnName, double x) throws SQLException {
		rs.updateDouble(columnName, x);
	}

	/**
	 * @see ResultSet#updateFloat(String, float)
	 */
	public void updateFloat(String columnName, float x) throws SQLException {
		rs.updateFloat(columnName, x);
	}

	/**
	 * @see ResultSet#updateInt(String, int)
	 */
	public void updateInt(String columnName, int x) throws SQLException {
		rs.updateInt(columnName, x);
	}

	/**
	 * @see ResultSet#updateLong(String, long)
	 */
	public void updateLong(String columnName, long x) throws SQLException {
		rs.updateLong(columnName, x);
	}

	/**
	 * @see ResultSet#updateShort(String, short)
	 */
	public void updateShort(String columnName, short x) throws SQLException {
		rs.updateShort(columnName, x);
	}

	/**
	 * @see ResultSet#updateBoolean(String, boolean)
	 */
	public void updateBoolean(String columnName, boolean x) throws SQLException {
		rs.updateBoolean(columnName, x);
	}

	/**
	 * @see ResultSet#updateBytes(String, byte[])
	 */
	public void updateBytes(String columnName, byte[] x) throws SQLException {
		rs.updateBytes(columnName, x);
	}

	/**
	 * @see ResultSet#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return rs.getBigDecimal(columnIndex);
	}

	/**
	 * @see ResultSet#getBigDecimal(int, int)
     * @deprecated
	 */
	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		return rs.getBigDecimal(columnIndex, scale);
	}

	/**
	 * @see ResultSet#updateBigDecimal(int, BigDecimal)
	 */
	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		rs.updateBigDecimal(columnIndex, x);
	}

	/**
	 * @see ResultSet#getURL(int)
	 */
	public URL getURL(int columnIndex) throws SQLException {
		return rs.getURL(columnIndex);
	}

	/**
	 * @see ResultSet#getArray(int)
	 */
	public Array getArray(int i) throws SQLException {
		return rs.getArray(i);
	}

	/**
	 * @see ResultSet#updateArray(int, Array)
	 */
	public void updateArray(int columnIndex, Array x) throws SQLException {
		rs.updateArray(columnIndex, x);
	}

	/**
	 * @see ResultSet#getBlob(int)
	 */
	public Blob getBlob(int i) throws SQLException {
		return rs.getBlob(i);
	}

	/**
	 * @see ResultSet#updateBlob(int, Blob)
	 */
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		rs.updateBlob(columnIndex, x);
	}

	/**
	 * @see ResultSet#getClob(int)
	 */
	public Clob getClob(int i) throws SQLException {
		return rs.getClob(i);
	}

	/**
	 * @see ResultSet#updateClob(int, Clob)
	 */
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		rs.updateClob(columnIndex, x);
	}

	/**
	 * @see ResultSet#getDate(int)
	 */
	public Date getDate(int columnIndex) throws SQLException {
		return rs.getDate(columnIndex);
	}

	/**
	 * @see ResultSet#updateDate(int, Date)
	 */
	public void updateDate(int columnIndex, Date x) throws SQLException {
		rs.updateDate(columnIndex, x);
	}

	/**
	 * @see ResultSet#getRef(int)
	 */
	public Ref getRef(int i) throws SQLException {
		return rs.getRef(i);
	}

	/**
	 * @see ResultSet#updateRef(int, Ref)
	 */
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		rs.updateRef(columnIndex, x);
	}

	/**
	 * @see ResultSet#getMetaData()
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return rs.getMetaData();
	}

	/**
	 * @see ResultSet#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		return rs.getWarnings();
	}

	/**
	 * @see ResultSet#getStatement()
	 */
	public Statement getStatement() throws SQLException {
		return rs.getStatement();
	}

	/**
	 * @see ResultSet#getTime(int)
	 */
	public Time getTime(int columnIndex) throws SQLException {
		return rs.getTime(columnIndex);
	}

	/**
	 * @see ResultSet#updateTime(int, Time)
	 */
	public void updateTime(int columnIndex, Time x) throws SQLException {
		rs.updateTime(columnIndex, x);
	}

	/**
	 * @see ResultSet#getTimestamp(int)
	 */
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return rs.getTimestamp(columnIndex);
	}

	/**
	 * @see ResultSet#updateTimestamp(int, Timestamp)
	 */
	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		rs.updateTimestamp(columnIndex, x);
	}

	/**
	 * @see ResultSet#getAsciiStream(String)
	 */
	public InputStream getAsciiStream(String columnName) throws SQLException {
		return rs.getAsciiStream(columnName);
	}

	/**
	 * @see ResultSet#getBinaryStream(String)
	 */
	public InputStream getBinaryStream(String columnName) throws SQLException {
		return rs.getBinaryStream(columnName);
	}

	/**
	 * @see ResultSet#getUnicodeStream(String)
     * @deprecated
	 */
	public InputStream getUnicodeStream(String columnName) throws SQLException {
		return rs.getUnicodeStream(columnName);
	}

	/**
	 * @see ResultSet#updateAsciiStream(String, InputStream, int)
	 */
	public void updateAsciiStream(String columnName, InputStream x, int length)
			throws SQLException {
		rs.updateAsciiStream(columnName, x, length);
	}

	/**
	 * @see ResultSet#updateBinaryStream(String, InputStream, int)
	 */
	public void updateBinaryStream(String columnName, InputStream x, int length)
			throws SQLException {
		rs.updateBinaryStream(columnName, x, length);
	}

	/**
	 * @see ResultSet#getCharacterStream(String)
	 */
	public Reader getCharacterStream(String columnName) throws SQLException {
		return rs.getCharacterStream(columnName);
	}

	/**
	 * @see ResultSet#updateCharacterStream(String, Reader, int)
	 */
	public void updateCharacterStream(String columnName, Reader reader,
			int length) throws SQLException {
		rs.updateCharacterStream(columnName, reader, length);
	}

	/**
	 * @see ResultSet#getObject(String)
	 */
	public Object getObject(String columnName) throws SQLException {
		return rs.getObject(columnName);
	}

	/**
	 * @see ResultSet#updateObject(String, Object)
	 */
	public void updateObject(String columnName, Object x) throws SQLException {
		rs.updateObject(columnName, x);
	}

	/**
	 * @see ResultSet#updateObject(String, Object, int)
	 */
	public void updateObject(String columnName, Object x, int scale)
			throws SQLException {
		rs.updateObject(columnName, x, scale);
	}

	/**
	 * @see ResultSet#getObject(int, Map)
	 */
	public Object getObject(int i, Map map) throws SQLException {
		return rs.getObject(i, map);
	}

	/**
	 * @see ResultSet#getString(String)
	 */
	public String getString(String columnName) throws SQLException {
		return Function.convert(rs.getString(columnName));
	}

	/**
	 * @see ResultSet#updateString(String, String)
	 */
	public void updateString(String columnName, String x) throws SQLException {
		rs.updateString(columnName, x);
	}

	/**
	 * @see ResultSet#getBigDecimal(String)
	 */
	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		return rs.getBigDecimal(columnName);
	}

	/**
	 * @see ResultSet#getBigDecimal(String, int)
     * @deprecated
	 */
	public BigDecimal getBigDecimal(String columnName, int scale)
			throws SQLException {
		return rs.getBigDecimal(columnName, scale);
	}

	/**
	 * @see ResultSet#updateBigDecimal(String, BigDecimal)
	 */
	public void updateBigDecimal(String columnName, BigDecimal x)
			throws SQLException {
		rs.updateBigDecimal(columnName, x);
	}

	/**
	 * @see ResultSet#getURL(String)
	 */
	public URL getURL(String columnName) throws SQLException {
		return rs.getURL(columnName);
	}

	/**
	 * @see ResultSet#getArray(String)
	 */
	public Array getArray(String colName) throws SQLException {
		return rs.getArray(colName);
	}

	/**
	 * @see ResultSet#updateArray(String, Array)
	 */
	public void updateArray(String columnName, Array x) throws SQLException {
		rs.updateArray(columnName, x);
	}

	/**
	 * @see ResultSet#getBlob(String)
	 */
	public Blob getBlob(String colName) throws SQLException {
		return rs.getBlob(colName);
	}

	/**
	 * @see ResultSet#updateBlob(String, Blob)
	 */
	public void updateBlob(String columnName, Blob x) throws SQLException {
		rs.updateBlob(columnName, x);
	}

	/**
	 * @see ResultSet#getClob(String)
	 */
	public Clob getClob(String colName) throws SQLException {
		return rs.getClob(colName);
	}

	/**
	 * @see ResultSet#updateClob(String, Clob)
	 */
	public void updateClob(String columnName, Clob x) throws SQLException {
		rs.updateClob(columnName, x);
	}

	/**
	 * @see ResultSet#getDate(String)
	 */
	public Date getDate(String columnName) throws SQLException {
		return rs.getDate(columnName);
	}

	/**
	 * @see ResultSet#updateDate(String, Date)
	 */
	public void updateDate(String columnName, Date x) throws SQLException {
		rs.updateDate(columnName, x);
	}

	/**
	 * @see ResultSet#getDate(int, Calendar)
	 */
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return rs.getDate(columnIndex, cal);
	}

	/**
	 * @see ResultSet#getRef(String)
	 */
	public Ref getRef(String colName) throws SQLException {
		return rs.getRef(colName);
	}

	/**
	 * @see ResultSet#updateRef(String, Ref)
	 */
	public void updateRef(String columnName, Ref x) throws SQLException {
		rs.updateRef(columnName, x);
	}

	/**
	 * @see ResultSet#getTime(String)
	 */
	public Time getTime(String columnName) throws SQLException {
		return rs.getTime(columnName);
	}

	/**
	 * @see ResultSet#updateTime(String, Time)
	 */
	public void updateTime(String columnName, Time x) throws SQLException {
		rs.updateTime(columnName, x);
	}

	/**
	 * @see ResultSet#getTime(int, Calendar)
	 */
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return rs.getTime(columnIndex, cal);
	}

	/**
	 * @see ResultSet#getTimestamp(String)
	 */
	public Timestamp getTimestamp(String columnName) throws SQLException {
		return rs.getTimestamp(columnName);
	}

	/**
	 * @see ResultSet#updateTimestamp(String, Timestamp)
	 */
	public void updateTimestamp(String columnName, Timestamp x)
			throws SQLException {
		rs.updateTimestamp(columnName, x);
	}

	/**
	 * @see ResultSet#getTimestamp(int, Calendar)
	 */
	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		return rs.getTimestamp(columnIndex, cal);
	}

	/**
	 * @see ResultSet#getObject(String, Map)
	 */
	public Object getObject(String colName, Map map) throws SQLException {
		return rs.getObject(colName, map);
	}

	/**
	 * @see ResultSet#getDate(String, Calendar)
	 */
	public Date getDate(String columnName, Calendar cal) throws SQLException {
		return rs.getDate(columnName, cal);
	}

	/**
	 * @see ResultSet#getTime(String, Calendar)
	 */
	public Time getTime(String columnName, Calendar cal) throws SQLException {
		return rs.getTime(columnName, cal);
	}

	/**
	 * @see ResultSet#getTimestamp(String, Calendar)
	 */
	public Timestamp getTimestamp(String columnName, Calendar cal)
			throws SQLException {
		return rs.getTimestamp(columnName, cal);
	}

	/**
	 * @see ResultSet#getNString(int)
	 */
	public String getNString(int columnIndex) throws SQLException {
		return Function.convert(rs.getNString(columnIndex));
	}

	/**
	 * @see ResultSet#getNString(String)
	 */
	public String getNString(String columnName) throws SQLException {
		return Function.convert(rs.getNString(columnName));
	}

	/**
	 * @see ResultSet#updateClob(int, Reader, long)
	 */
	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		rs.updateClob(columnIndex, reader, length);
	}

	/**
	 * @see ResultSet#updateClob(String, Reader, long)
	 */
	public void updateClob(String columnName, Reader reader, long length) throws SQLException {
		rs.updateClob(columnName, reader, length);
	}

	/**
	 * @see ResultSet#updateClob(int, Reader)
	 */
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		rs.updateClob(columnIndex, reader);
	}

	/**
	 * @see ResultSet#updateClob(String, Reader)
	 */
	public void updateClob(String columnName, Reader reader) throws SQLException {
		rs.updateClob(columnName, reader);
	}

	/**
	 * @see ResultSet#updateBlob(int, InputStream, long)
	 */
	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		rs.updateBlob(columnIndex, inputStream, length);
	}

	/**
	 * @see ResultSet#updateBlob(String, InputStream, long)
	 */
	public void updateBlob(String columnName, InputStream inputStream, long length) throws SQLException {
		rs.updateBlob(columnName, inputStream, length);
	}

	/**
	 * @see ResultSet#updateBlob(int, InputStream)
	 */
	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		rs.updateBlob(columnIndex, inputStream);
	}

	/**
	 * @see ResultSet#updateBlob(String, InputStream)
	 */
	public void updateBlob(String columnName, InputStream inputStream) throws SQLException {
		rs.updateBlob(columnName, inputStream);
	}

	/**
	 * @see ResultSet#unwrap(Class<T>)
	 */
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return rs.unwrap(iface);
	}

	/**
	 * @see ResultSet#isWrapperFor(Class<T>)
	 */
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return rs.isWrapperFor(iface);
	}

	/**
	 * @see ResultSet#getRowId(int)
	 */
	public RowId getRowId(int columnIndex) throws SQLException {
		return rs.getRowId(columnIndex);
	}

	/**
	 * @see ResultSet#getRowId(String)
	 */
	public RowId getRowId(String columnLabel) throws SQLException {
		return rs.getRowId(columnLabel);
	}

	/**
	 * @see ResultSet#updateRowId(int, RowId)
	 */
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		rs.updateRowId(columnIndex, x);
	}

	/**
	 * @see ResultSet#updateRowId(String, RowId)
	 */
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		rs.updateRowId(columnLabel, x);
	}

	/**
	 * @see ResultSet#getHoldability()
	 */
	public int getHoldability() throws SQLException {
		return rs.getHoldability();
	}

	/**
	 * @see ResultSet#isClosed()
	 */
	public boolean isClosed() throws SQLException {
		return rs.isClosed();
	}

	/**
	 * @see ResultSet#updateNString(int, String)
	 */
	public void updateNString(int columnIndex, String nString)
			throws SQLException {
		rs.updateNString(columnIndex, nString);
	}

	/**
	 * @see ResultSet#updateNString(String, String)
	 */
	public void updateNString(String columnLabel, String nString)
			throws SQLException {
		rs.updateNString(columnLabel, nString);
	}

	/**
	 * @see ResultSet#updateNClob(int, NClob)
	 */
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		rs.updateNClob(columnIndex, nClob);
	}

	/**
	 * @see ResultSet#updateNClob(String, NClob)
	 */
	public void updateNClob(String columnLabel, NClob nClob)
			throws SQLException {
		rs.updateNClob(columnLabel, nClob);
	}

	/**
	 * @see ResultSet#getNClob(int)
	 */
	public NClob getNClob(int columnIndex) throws SQLException {
		return rs.getNClob(columnIndex);
	}

	/**
	 * @see ResultSet#getNClob(String)
	 */
	public NClob getNClob(String columnLabel) throws SQLException {
		return rs.getNClob(columnLabel);
	}

	/**
	 * @see ResultSet#getSQLXML(int)
	 */
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return rs.getSQLXML(columnIndex);
	}

	/**
	 * @see ResultSet#getSQLXML(String)
	 */
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return rs.getSQLXML(columnLabel);
	}

	/**
	 * @see ResultSet#updateSQLXML(int, SQLXML)
	 */
	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		rs.updateSQLXML(columnIndex, xmlObject);
	}

	/**
	 * @see ResultSet#updateSQLXML(String, SQLXML)
	 */
	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
			throws SQLException {
		rs.updateSQLXML(columnLabel, xmlObject);
	}

	/**
	 * @see ResultSet#getNCharacterStream(int)
	 */
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return rs.getNCharacterStream(columnIndex);
	}

	/**
	 * @see ResultSet#getNCharacterStream(String)
	 */
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return rs.getNCharacterStream(columnLabel);
	}

	/**
	 * @see ResultSet#updateNCharacterStream(int, Reader, long)
	 */
	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		rs.updateNCharacterStream(columnIndex, x, length);
	}

	/**
	 * @see ResultSet#updateNCharacterStream(String, Reader, long)
	 */
	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		rs.updateNCharacterStream(columnLabel, reader, length);
	}

	/**
	 * @see ResultSet#updateAsciiStream(int, InputStream, long)
	 */
	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		rs.updateAsciiStream(columnIndex, x, length);
	}

	/**
	 * @see ResultSet#updateBinaryStream(int, InputStream, long)
	 */
	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		rs.updateBinaryStream(columnIndex, x, length);
	}

	/**
	 * @see ResultSet#updateCharacterStream(int, Reader, long)
	 */
	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		rs.updateCharacterStream(columnIndex, x, length);

	}

	/**
	 * @see ResultSet#updateAsciiStream(String, InputStream, long)
	 */
	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		rs.updateAsciiStream(columnLabel, x, length);
	}

	/**
	 * @see ResultSet#updateBinaryStream(String, InputStream, long)
	 */
	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		rs.updateBinaryStream(columnLabel, x, length);
	}

	/**
	 * @see ResultSet#updateCharacterStream(String, Reader, long)
	 */
	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		rs.updateCharacterStream(columnLabel, reader, length);
	}

	/**
	 * @see ResultSet#updateNClob(int, Reader, long)
	 */
	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		rs.updateNClob(columnIndex, reader, length);
	}

	/**
	 * @see ResultSet#updateNClob(String, Reader, long)
	 */
	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		rs.updateNClob(columnLabel, reader, length);
	}

	/**
	 * @see ResultSet#updateNCharacterStream(int, Reader)
	 */
	public void updateNCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		rs.updateNCharacterStream(columnIndex, x);
	}

	/**
	 * @see ResultSet#updateNCharacterStream(String, Reader)
	 */
	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		rs.updateNCharacterStream(columnLabel, reader);
	}

	/**
	 * @see ResultSet#updateAsciiStream(int, InputStream)
	 */
	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		rs.updateAsciiStream(columnIndex, x);
	}

	/**
	 * @see ResultSet#updateBinaryStream(int, InputStream)
	 */
	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		rs.updateBinaryStream(columnIndex, x);
	}

	/**
	 * @see ResultSet#updateCharacterStream(int, Reader)
	 */
	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		rs.updateCharacterStream(columnIndex, x);
	}

	/**
	 * @see ResultSet#updateAsciiStream(String, InputStream)
	 */
	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		rs.updateAsciiStream(columnLabel, x);
	}

	/**
	 * @see ResultSet#updateBinaryStream(String, InputStream)
	 */
	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		rs.updateBinaryStream(columnLabel, x);
	}

	/**
	 * @see ResultSet#updateCharacterStream(String, Reader)
	 */
	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		rs.updateCharacterStream(columnLabel, reader);
	}

	/**
	 * @see ResultSet#updateNClob(int, Reader)
	 */
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		rs.updateNClob(columnIndex, reader);
	}

	/**
	 * @see ResultSet#updateNClob(String, Reader)
	 */
	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		rs.updateNClob(columnLabel, reader);
	}

	/**
	 * @see ResultSet#getObject(int, Class<T>)
	 */
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		return rs.getObject(columnIndex, type);
	}

	/**
	 * @see ResultSet#getObject(String, Class<T>)
	 */
	public <T> T getObject(String columnLabel, Class<T> type)
			throws SQLException {
		return rs.getObject(columnLabel, type);
	}

}
