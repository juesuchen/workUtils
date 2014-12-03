package workUtils.email;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

public class ISprintMailUtils {

    private static final Logger logger = Logger.getLogger(ISprintMailUtils.class);

    private static final Map<String, String> templateCache = new HashMap<String, String>();

    public void send(String user, String pass, String to, String subject, String body) throws Exception {
        sendBase(user, pass, to, null, subject, body);
    }

    public void sendBase(String user, String pass, String to, String cc, String subject, String body) throws Exception {
        sendHTMLBase(to, cc, subject, body, null);
    }

    public void sendHTMLBase(String to, String cc, String subject, String body, byte[] imageData) throws Exception {
        Properties props = getSmtpProperties();
        final String username = SysInitService.getEmailUser();
        final String password = SysInitService.getEmailPassword();
        Session session = getSession(props, username, password);

        HtmlEmail email = new HtmlEmail();
        email.setMailSession(session);
        email.setFrom(username);
        email.addTo(to);
        email.setSubject(subject);

        if (cc != null && cc.length() > 0) {
            email.addCc(cc);
        }

        if (imageData != null) {
            email.embed(new ByteArrayDataSource(imageData, "image/png"), "qr-code.png", "_IMG");
        }

        Pattern p = Pattern.compile("src=[\"|']((.+?)\\.(.+?))[\"|']");
        Matcher m = p.matcher(body);
        while (m.find()) {
            String fileName = m.group(1);
            String fileType = m.group(3);
            File file = new File(SysInitService.getWebRoot() + "customImgs/" + fileName);
            if (!file.exists()) {
                logger.info("add customer images error:" + fileName);
                break;
            }
            email.embed(new ByteArrayDataSource(FileUtils.readFileToByteArray(file), "image/" + fileType), fileName,
                    fileName);
            body = body.replaceFirst(fileName, "cid:" + fileName);
        }

        email.setHtmlMsg(body);
        sendMsg(email);
    }

    private void sendMsg(final HtmlEmail email) throws MessagingException {
        final long now = System.currentTimeMillis();
        final String to = email.getToAddresses().toString();
        Executors.newCachedThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    email.send();
                    logger.info("send to [" + to + "] successfull, cost seconds in "
                            + (System.currentTimeMillis() - now) / 1000);
                } catch(Exception e) {
                    logger.error("sendMsg error.", e);
                }
            }
        });
    }

    public void send(String to, String subject, String body) throws Exception {
        send(SysInitService.getEmailUser(), SysInitService.getEmailPassword(), to, subject, body);
    }

    public void sendWithCC(String to, String cc, String subject, String body) throws Exception {
        sendBase(SysInitService.getEmailUser(), SysInitService.getEmailPassword(), to, cc, subject, body);
    }

    public void sendHtmlWithImage(String to, String subject, String html, byte[] imageData) throws Exception {
        sendHTMLBase(to, null, subject, html, imageData);
    }

    public void send(String user, String pass, String to, String subject, String html, String affix) throws Exception {
        sendHTMLBase(to, null, subject, html, null);
    }

    private Session getSession(Properties props, final String username, final String password) {
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        return session;
    }

    private Properties getSmtpProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SysInitService.getSmtpHost());
        properties.put("mail.smtp.port", SysInitService.getSmtpPort());
        properties.put("mail.smtp.auth", "true");
        if ("ssl".equalsIgnoreCase(SysInitService.getEmailType())) {
            properties.put("mail.smtp.socketFactory.port", SysInitService.getSmtpPort());
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.socketFactory.fallback", "false");
            if (SysInitService.getSmtpHost().indexOf("gmail") != -1) {
                properties.put("mail.smtp.EnableSSL.enable", "true");
                properties.setProperty("mail.smtp.socketFactory.fallback", "false");
            }
        } else if ("tls".equalsIgnoreCase(SysInitService.getEmailType())) {
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.trust", SysInitService.getSmtpHost());
        }

        return properties;
    }

    /**
     * 
     * @Title: main
     * @Description: TODO(simple description this method what to do.)
     * @author liyuying
     * @date 2014年6月12日 下午5:04:30
     * @param args
     *            void
     */
    public static void main(String[] args) {
        ISprintMailUtils mg = new ISprintMailUtils();
        String to = "juesu.chen@axbsec.com";
        String subject = "Subject";
        String body = "This is a test mail ... :-)<img src=\"cid:_IMG\" width=\"200px\"/>xx<img src=\"_IMG_1\" width=\"200px\"/>yyzz<img src=\"_IMG_3\" width=\"200px\"/>";
        try {
            File file = new File(SysInitService.getWebRoot() + "customImgs/_IMG_2");
            mg.sendHtmlWithImage(to, subject, body, FileUtils.readFileToByteArray(file));
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static String getTempleteToString(String tmpPath, String... args) throws IOException {
        String content = templateCache.get(tmpPath);
        if (content == null) {
            content = IOUtils.toString(ISprintMailUtils.class.getResourceAsStream(tmpPath));
        }
        content = MessageFormat.format(content, args);
        return content;
    }
}
