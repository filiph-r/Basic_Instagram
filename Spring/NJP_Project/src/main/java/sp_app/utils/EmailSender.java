package sp_app.utils;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.common.io.Files;

public class EmailSender {

	private final static String hostMail = "rafweb2018@outlook.com";
	private final static String password = "H6X\\GvWLRq";

	public static synchronized void sendVerification(String email, String token) {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "outlook.office365.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(hostMail, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(hostMail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject("E-mail verification");
			message.setText("verification link: http://hadziserver.ddns.net:8080/users/validation?token=" + token);

			Transport.send(message);

			System.out.println("Verification mail sent...");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
