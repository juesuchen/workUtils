package workUtils.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: DateUtils
 * @Description: TODO(DateUtils.)
 * @author juesu.chen
 * @date Jun 19, 2014 5:56:33 PM
 * @version 1.0
 */
public class DateUtils {

    public static final String FT_YMD = "yyyy-MM-dd";

    public static final String FT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    public static final String FT_YMDHM = "yyyy-MM-dd HH:mm";

    public static String formatDateToString(Date date, String format) {
        if (date== null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(StringUtils.defaultString(format, FT_YMD), Locale.CHINA);
        return sdf.format(date);
    }

    public static Date formatStringToDate(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(StringUtils.defaultString(format, FT_YMD), Locale.CHINA);
        try {
            return sdf.parse(dateStr);
        }
        catch(ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDateToString(Date date) {
        return formatDateToString(date, FT_YMD);
    }

    public static String formatDateToStringYMDHM(Date date) {
        return formatDateToString(date, FT_YMDHM);
    }

    public static String formatDateToStringYMDHMS(Date date) {
        return formatDateToString(date, FT_YMDHMS);
    }

    public static String formatDateToString(Long time) {
        if (time != null) {
            return formatDateToString(new Date(time), FT_YMD);
        } else {
            return null;
        }
    }

    public static String formatDateToStringYMDHM(Long time) {
        if (time != null) {
            return formatDateToString(new Date(time), FT_YMDHM);
        } else {
            return null;
        }
    }

    public static Date formatStringToDateYMDHM(String dateStr) {
        return formatStringToDate(dateStr, FT_YMDHM);
    }

    public static Date formatStringToDate(String dateStr) {
        return formatStringToDate(dateStr, FT_YMD);
    }

}
