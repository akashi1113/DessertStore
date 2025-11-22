package com.csu.userservice.config;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class MailUtil {

    // 发件人邮箱（QQ 邮箱）
    private static String senderMail = "1428188606@qq.com";
    // QQ 邮箱授权码
    private static String authCode = "llnxcjgpxxmoiedd";
    // SMTP 服务器
    private static String emailSMTPHost = "smtp.qq.com";

    // 收件人信息（由业务设置）
    private static String receiverMail = "";
    private static String receiverName = "";

    public static String getSenderMail() {
        return senderMail;
    }

    public static void setSenderMail(String senderMail) {
        MailUtil.senderMail = senderMail;
    }

    public static String getAuthCode() {
        return authCode;
    }

    public static void setAuthCode(String authCode) {
        MailUtil.authCode = authCode;
    }

    public static String getEmailSMTPHost() {
        return emailSMTPHost;
    }

    public static void setEmailSMTPHost(String emailSMTPHost) {
        MailUtil.emailSMTPHost = emailSMTPHost;
    }

    public static String getReceiverMail() {
        return receiverMail;
    }

    public static String getReceiverName() {
        return receiverName;
    }

    public static void setReceiverMail(String receiverMail) {
        MailUtil.receiverMail = receiverMail;
    }

    public static void setReceiverName(String receiverName) {
        MailUtil.receiverName = receiverName;
    }

    // 创建带 HTML 内容的 MimeMessage
    public static MimeMessage creatMimeMessage(Session session, String html) {
        MimeMessage message = new MimeMessage(session);
        try {
            // 发件人
            message.setFrom(new InternetAddress(senderMail, "Cloud Bakery", "UTF-8"));
            // 收件人
            message.setRecipient(MimeMessage.RecipientType.TO,
                    new InternetAddress(receiverMail, receiverName, "UTF-8"));
            // 主题
            message.setSubject("Verify your email", "UTF-8");
            // 内容
            message.setContent(html, "text/html;charset=UTF-8");
            // 发送时间
            message.setSentDate(new Date());
            message.saveChanges();
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return message;
    }
}
