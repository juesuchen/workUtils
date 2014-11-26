package workUtils.db;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import workUtils.utils.Page;
import workUtils.utils.ReflectUtils;

/**
 * @ClassName: BaseDao
 * @Description: TODO(Base Dao class.)
 * @author juesu.chen
 * @date Jun 11, 2014 10:14:57 AM
 * @version 1.0
 */
public class BaseDao<T> {

    protected Logger logger = Logger.getLogger(getClass());

    private Class<T> entityClass;

    public BaseDao() {
        this.entityClass = ReflectUtils.getParameterizedType(getClass());
    }

    /**
     * @Fields runner : TODO(base class field execute runner.)
     */
    protected QueryRunner runner = new QueryRunner(DataBaseUtil.getDataSource());

    protected int update(String sql, Object... params) {
        try {
            logSql(sql, params);
            return runner.update(sql, params);
        } catch(SQLException e) {
            logger.error("update error:", e);
        }
        return 0;
    }

    private void logSql(String sql, Object[] params) {
        if (params != null && params.length > 0) {
            logger.info("execute sql :" + sql + " # params:" + StringUtils.join(params, ","));
        } else {
            logger.info("execute sql :" + sql);
        }
    }

    protected int update(String sql) {
        return update(sql, (Object[]) null);
    }

    /**
     * 
     * @Title: queryOne
     * @Description: TODO(query One bean from database.)
     * @author juesu.chen
     * @date Jun 11, 2014 1:03:53 PM
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    protected T queryOne(String sql, Object... params) {
        try {
            ResultSetHandler<T> h = new BeanHandler<T>(entityClass);
            logSql(sql, params);
            return runner.query(sql, h, params);
        } catch(SQLException e) {
            logger.error("queryOne error:", e);
        }
        return null;
    }

    protected T queryOne(String sql) {
        return queryOne(sql, (Object[]) null);
    }

    /**
     * 
     * @Title: findBy
     * @Description: TODO(find the columnIndex column value.)
     * @author juesu.chen
     * @date Jun 16, 2014 2:53:18 PM
     * @param sql
     * @param columnIndex
     *            begin from 1
     * @param params
     * @return
     */
    protected Object findBy(String sql, int columnIndex, Object... params) {
        Object object = null;
        try {
            logSql(sql, params);
            object = runner.query(sql, new ScalarHandler(columnIndex), params);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return object;
    }

    public Object findBy(String sql, int columnIndex) {
        return findBy(sql, columnIndex, null);
    }

    /**
     * 
     * @Title: queryList
     * @Description: TODO(query bean List from database.)
     * @author juesu.chen
     * @date Jun 11, 2014 1:41:03 PM
     * @param sql
     * @param params
     * @return
     */
    protected List<T> queryList(String sql, Object... params) {
        try {
            ResultSetHandler<List<T>> h = new BeanListHandler<T>(entityClass);
            logSql(sql, params);
            return runner.query(sql, h, params);
        } catch(SQLException e) {
            logger.error("queryOne error:", e);
        }
        return null;
    }

    protected List<T> queryList(String sql) {
        return queryList(sql, (Object[]) null);
    }

    /**
     * 
     * @Title: queryListByPage
     * @Description: TODO(query List By Page with parameter.)
     * @author juesu.chen
     * @date Jun 16, 2014 2:47:22 PM
     * @param sql
     * @param page
     * @param params
     */
    protected void queryListByPage(String sql, Page page, Object... params) {

        String queryListSql = "";

        String queryTotalSql = "";

        Object[] tempParams = ArrayUtils.addAll(params, new Object[] { page.getPageSize(), page.getOffset() });
        List<T> result = queryList(queryListSql, tempParams);

        Object obj = findBy(queryTotalSql, 1, params);
        Integer totalCount = NumberUtils.toInt(obj.toString());

        page.setResult(result);
        page.setTotalCount(totalCount);
    }

    /**
     * 
     * @Title: queryListByPage
     * @Description: TODO(query list without by page without parameter.)
     * @author juesu.chen
     * @date Jun 16, 2014 2:48:13 PM
     * @param sql
     * @param page
     */
    protected void queryListByPage(String sql, Page page) {
        queryListByPage(sql, page, null);
    }
}
