package workUtils.utils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;

/**
 * @ClassName: MyBeanUtils
 * @Description: TODO(BeanUtils.)
 * @author juesu.chen
 * @date Jun 16, 2014 9:57:24 AM
 * @version 1.0
 */
public class MyBeanUtils {
    static {
        ConvertUtils.register(new DateConverter(), java.util.Date.class);
        ConvertUtils.register(new DateConverter(), java.sql.Date.class);
        ConvertUtils.register(new DateConverter(), java.sql.Timestamp.class);
    }

    /**
     * 
     * @Title: copyProperties
     * @Description: TODO(copy properties.)
     * @author juesu.chen
     * @date Jun 16, 2014 9:11:50 AM
     * @param to
     * @param from
     */
    public static void copyProperties(Object to, Object from) {
        try {
            BeanUtils.copyProperties(to, from);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @Title: getBatchObjectFromParam
     * @Description: TODO(get batch object from param,the name like
     *               name_0,name1_1.)
     * @author juesu.chen
     * @date Jun 16, 2014 9:06:48 AM
     * @param params
     * @param aclass
     * @param paramNum
     * @return
     */
    public static List getBatchObjectFromParam(Map params, Class aclass, int paramNum) {
        PropertyUtilsBean bean = new PropertyUtilsBean();
        PropertyDescriptor[] origDescriptors = bean.getPropertyDescriptors(aclass);
        List ls = new ArrayList();
        if (paramNum < 0)
            return ls;
        Map properties = new HashMap();
        for (int m = -1; m <= paramNum; m++) {
            Object obj = ReflectUtils.newInstance(aclass);
            for (int i = 0; i < origDescriptors.length; i++) {
                String name = origDescriptors[i].getName();
                if ("class".equals(name)) {
                    continue; // No point in trying to set an object's class
                }
                if (bean.isWriteable(obj, name)) {
                    String keyName = -1 == m ? name : (name + "_" + m);
                    Object paramObject = params.get(keyName);
                    if (null != paramObject)
                        properties.put(name, paramObject);
                }
            }
            if (!properties.isEmpty()) {
                copyProperties(obj, properties);
                ls.add(obj);
                properties.clear();
            }
        }
        return ls;
    }

    /**
     * 
     * @Title: getObjectFromParam
     * @Description: TODO(get one object from params.)
     * @author juesu.chen
     * @date Jun 16, 2014 9:15:48 AM
     * @param params
     * @param aclass
     * @return
     */
    public static Object getObjectFromParam(Map params, Class aclass) {
        List ls = getBatchObjectFromParam(params, aclass, 1);
        if (ls.isEmpty())
            return null;
        return ls.get(0);
    }
}
