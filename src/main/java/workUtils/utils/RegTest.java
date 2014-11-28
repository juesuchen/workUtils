package workUtils.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest {

    public static void findStr() {
        Pattern p = Pattern.compile("_IMG\\d");
        Matcher m = p.matcher("<img src=\"_IMG1\" width=\"200px\"/>xx<img src=\"_IMG2\" width=\"200px\"/>");
        while (m.find()) {
            String img = m.group();
            System.out.println(img);
        }
    }
}