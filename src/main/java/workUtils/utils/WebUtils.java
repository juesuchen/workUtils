package workUtils.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class WebUtils {

    public static String getCookieValue(HttpServletRequest request, String name) {
        String value = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (name.equals(cookie.getName())) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        return value;
    }

    public static boolean isConnection(String ip, int port) {
        Socket connect = new Socket();
        try {
            connect.connect(new InetSocketAddress(ip, port), 500);
            return connect.isConnected();
        } catch(Exception e) {
            return false;
        } finally {
            try {
                connect.close();
            } catch(IOException e) {
            }
        }
    }
}
