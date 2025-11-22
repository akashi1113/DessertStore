package com.csu.userservice.service;

import com.csu.userservice.config.MailUtil;
import com.csu.userservice.config.RandomUtil;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    // 生成 HTML 内容
    public String html(String receiverMail, String username, String code) {
        return "Hey " + username + " !<br/>" +
                "Verify this email is yours<br/>" +
                receiverMail + "<br/>" +
                "This email address was recently entered to verify your email address.<br/>" +
                "You can use this code to verify that this email belongs to you: " +
                "<h3 style='color:red;'>" + code + "</h3><br/>";
    }

    // 发送邮件并返回验证码
    public String sendEmail(String email, String username) {

        // 收件人信息交给 MailUtil
        MailUtil.setReceiverName(username);
        MailUtil.setReceiverMail(email);

        Properties props = new Properties();
        props.setProperty("mail.debug", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", MailUtil.getEmailSMTPHost());
        props.setProperty("mail.auth", "true");

        try {
            // Jakarta Mail（Angus Mail） SSL 配置
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.trust", "*"); // 信任所有服务器
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Session session = Session.getInstance(props);
        session.setDebug(true);

        // 生成随机验证码
        String code = RandomUtil.getRandom();
        String html = html(email, username, code);

        MimeMessage mimeMessage = MailUtil.creatMimeMessage(session, html);

        try {
            Transport transport = session.getTransport();
            transport.connect(MailUtil.getSenderMail(), MailUtil.getAuthCode());
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();

            return code;

        } catch (MessagingException e) { // MessagingException 足够覆盖所有邮件异常
            throw new RuntimeException(e);
        }
    }
}
