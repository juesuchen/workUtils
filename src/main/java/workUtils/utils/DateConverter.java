package workUtils.utils;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.time.DateUtils;

/**
 * @ClassName: DateConverter
 * @Description: TODO(DateConverter.)
 * @author juesu.chen
 * @date Jun 16, 2014 9:57:41 AM
 * @version 1.0
 */
public class DateConverter implements Converter {

    private static String[] dateParsePatterns = { "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd" };

    private static String[] dateTimeParsePatterns = { "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss",
            "yyyy.MM.dd HH:mm:ss" };

    public Object convert(Class type, Object value) {
        if (value == null) {
            return value;
        }
        // java.sql.Timestamp
        if (value instanceof Timestamp) {
            try {
                Timestamp ts = (Timestamp) value;
                return new Date(ts.getTime());
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        else {// java.util.Date,java.sql.Date
            String p = ((String) value).trim();
            if (p.length() == 0) {
                return null;
            }
            try {
                if (p.length() > 10) {
                    return DateUtils.parseDate(p, dateTimeParsePatterns);
                }
                else {
                    return DateUtils.parseDate(p, dateParsePatterns);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
