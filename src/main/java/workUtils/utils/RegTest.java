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

    public static void findStr1() {
        String s1 = "{username}，你好，你是{dep}";
        Pattern p = Pattern.compile("\\{(.+?)\\}");
        Matcher m = p.matcher(s1);
        while (m.find()) {
            System.out.println(m.group(1) + "--" + m.group());
            s1 = s1.replaceFirst("\\{" + m.group(1) + "\\}", "xx");
        }
        System.out.println(s1);
    }

    public static void main(String[] args) {
        findStr1();
    }

}