package workUtils.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @ClassName: ReflectUtils
 * @Description: TODO(Reflect Utils.)
 * @author juesu.chen
 * @date Jun 16, 2014 10:57:22 AM
 * @version 1.0
 */
public class ReflectUtils {

    public static Class getParameterizedType(Class cls) {
        Type type = cls.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
            return (Class) parameterizedType[0];
        }
        return cls;
    }

    public static Object newInstance(Class entityClass) {
        Object obj = null;
        try {
            obj = entityClass.newInstance();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    public static Object newInstance(String className) {
        Object obj = null;
        try {
            obj = Class.forName(className).newInstance();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    public static byte[] steamToBytes(InputStream ins) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] bytes = new byte[2048];
        int len = ins.read(bytes);
        while (len != -1) {
            bout.write(bytes, 0, len);
            len = ins.read(bytes);
        }
        bytes = bout.toByteArray();
        bout.close();
        return bytes;
    }

}
