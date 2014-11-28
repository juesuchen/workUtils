package workUtils.email;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class ISprintMailUtils {

    private static final Logger logger = Logger.getLogger(ISprintMailUtils.class);

    private static final Map<String, String> templateCache = new HashMap<String, String>();

    private static boolean isInNewThread = true;

    public void send(String user, String pass, String to, String subject, String body) throws Exception {
        sendBase(user, pass, to, null, subject, body);
    }

    public void sendBase(String user, String pass, String to, String cc, String subject, String body) throws Exception {

        Properties props = getSmtpProperties();

        final String username = user;
        final String password = pass;

        Session session = getSession(props, username, password);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(username));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        if (cc != null && cc.length() > 0) {
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
        }
        msg.setSubject(subject);
        msg.setText(body);
        msg.setSentDate(new Date());
        sendMsg(msg);

    }

    private void sendMsg(final Message msg) throws MessagingException {
        final long now = System.currentTimeMillis();
        final String to = msg.getRecipients(Message.RecipientType.TO)[0].toString();

        if (isInNewThread) {
            Executors.newCachedThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        Transport.send(msg);
                        logger.info("send to [" + to + "] successfull, cost seconds in "
                                + (System.currentTimeMillis() - now) / 1000);
                    } catch(MessagingException e) {
                        logger.error("sendMsg error.", e);
                    }
                }
            });
        } else {
            Transport.send(msg);
            logger.info("send to [" + to + "] successfull, cost seconds in " + (System.currentTimeMillis() - now)
                    / 1000);
        }
    }

    public void send(String to, String subject, String body) throws Exception {
        send(SysInitService.getEmailUser(), SysInitService.getEmailPassword(), to, subject, body);
    }

    public void sendWithCC(String to, String cc, String subject, String body) throws Exception {
        sendBase(SysInitService.getEmailUser(), SysInitService.getEmailPassword(), to, cc, subject, body);
    }

    public void sendHtmlWithImage(String to, String subject, String html, byte[] imageData) throws Exception {
        Properties props = getSmtpProperties();
        final String username = SysInitService.getEmailUser();
        final String password = SysInitService.getEmailPassword();
        Session session = getSession(props, username, password);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(username));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        msg.setSubject(subject);

        Multipart mp = new MimeMultipart("related");

        /** 解释并发送 html 里面有_IMG_ 的标记，并读取对应文件发送 begin **/
        Pattern p = Pattern.compile("_IMG_\\d+");
        Matcher m = p.matcher(html);
        while (m.find()) {
            String img = m.group();
            File file = new File(SysInitService.getWebRoot() + "customImgs/" + img);
            if (!file.exists()) {
                logger.info("add customer images error:" + img);
                break;
            }
            MimeBodyPart imgBody = new MimeBodyPart();
            imgBody.setDataHandler(new DataHandler(new FileDataSource(file)));
            imgBody.setContentID(img);
            html = html.replaceFirst(img, "cid:" + img);
            mp.addBodyPart(imgBody);
        }
        /** 解释并发送 html 里面有_IMG_ 的标记，并读取对应文件发送 end **/

        MimeBodyPart imgBodyPart = new MimeBodyPart();
        imgBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(imageData, "application/octet-stream")));
        imgBodyPart.setContentID("_IMG");// used in html ima tag
        mp.addBodyPart(imgBodyPart);

        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setContent(html, "text/html;charset=GBK");
        mp.addBodyPart(textBodyPart);

        msg.setContent(mp);
        msg.saveChanges();

        msg.setSentDate(new Date());
        sendMsg(msg);
    }

    public void send(String user, String pass, String to, String subject, String html, String affix) throws Exception {
        Properties props = getSmtpProperties();
        final String username = user;
        final String password = pass;
        Session session = getSession(props, username, password);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(username));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        msg.setSubject(subject);
        Multipart mp = new MimeMultipart();
        msg.setContent(mp);
        BodyPart bp = new MimeBodyPart();
        bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>" + html,
                "text/html;charset=UTF-8");
        mp.addBodyPart(bp);
        if (affix != null && !"".equals(affix)) {
            BodyPart bp1 = new MimeBodyPart();
            FileDataSource fileds = new FileDataSource(affix);
            bp1.setDataHandler(new DataHandler(fileds));
            bp1.setFileName(fileds.getName());
            mp.addBodyPart(bp1);
            msg.setContent(mp);
        }
        msg.setSentDate(new Date());
        sendMsg(msg);
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

    protected static String decodeText(String text) throws UnsupportedEncodingException {
        if (text == null)
            return null;
        if (text.startsWith("=?GB") || text.startsWith("=?gb"))
            text = MimeUtility.decodeText(text);
        else
            text = new String(text.getBytes("ISO8859_1"));
        return text;
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
        String to = "315944451@qq.com";
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
