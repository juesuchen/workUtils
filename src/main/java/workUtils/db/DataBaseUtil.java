package workUtils.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @ClassName: DataBaseUtil
 * @Description: TODO(database utils)
 * @author juesu.chen
 * @date Jun 10, 2014 11:07:15 AM
 * @version 1.0
 */
public class DataBaseUtil {

    private static final Logger logger = Logger.getLogger(DataBaseUtil.class);

    /**
     * @Fields dataSource : TODO(use to store dataSource instance.)
     */
    private static DataSource dataSource;

    public static DataSource getDataSource() {
        return dataSource;
    }

    private static ThreadLocal<Connection> threadLocalCon = new ThreadLocal<Connection>();

    /**
     * 
     * @Title: initConnection
     * @Description: TODO(init dataSource by db type.)
     * @author juesu.chen
     * @date Jun 10, 2014 11:27:30 AM
     * @param db
     * @throws SQLException
     */
    public static void initDataSource(String db) throws SQLException {
        dataSource = new ComboPooledDataSource(db);
    }

    /**
     * 
     * @Title: getConnection
     * @Description: TODO(get One Connection from dataSource.)
     * @author juesu.chen
     * @date Jun 11, 2014 9:17:58 AM
     * @return
     * @throws SQLException
     */
    public static synchronized Connection getConnection() throws SQLException {
        Connection conn = threadLocalCon.get();
        if (conn == null) {
            try {
                conn = dataSource.getConnection();
                threadLocalCon.set(conn);
            } catch(SQLException e) {
                logger.error("getConnection error:", e);
                throw e;
            }
        }
        return conn;
    }

    /**
     * 
     * @Title: closeCurrentConnection
     * @Description: TODO(close Current Connection.)
     * @author juesu.chen
     * @date Jun 11, 2014 2:33:45 PM
     */
    public static void closeCurrentConnection() {
        try {
            DbUtils.close(getConnection());
            threadLocalCon.remove();
        } catch(SQLException e) {
            logger.error("close Connection error:", e);
        }
    }

    /**
     * 
     * @Title: close
     * @Description: TODO(close the db resources if they are not null.)
     * @author juesu.chen
     * @date Jun 10, 2014 11:28:44 AM
     * @param rs
     * @param pstat
     * @param conn
     */
    public static void close(ResultSet rs, PreparedStatement pstat, Connection conn) {
        DbUtils.closeQuietly(conn, pstat, rs);
    }

    public static void setDataSource(DataSource dataSource) {
        DataBaseUtil.dataSource = dataSource;
    }

}
