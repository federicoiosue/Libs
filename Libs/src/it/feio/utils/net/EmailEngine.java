/**
 * 
 */
package it.feio.utils.net;

import it.autostrade.bmt.util.log4jutil.LogStackTrace;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Classe per l'invio delle email di allerta.
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 12/ott/2012
 *
 */
public class EmailEngine {

	private String smtpHost;
	private Logger log;

	public EmailEngine(String smtpHost) {
		this.log = Logger.getLogger(this.getClass().getName());
		this.smtpHost = smtpHost;
	}

	public boolean Send(String emailSender, String emailReceiver, String subject, String body) {
		boolean result = true;

		// Get system properties
		Properties props = System.getProperties();

		// Setup mail server
		props.put("mail.smtp.host", smtpHost);

		// Get sessionema
		Session session = Session.getDefaultInstance(props, null);

		// Define message
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(emailSender));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailReceiver));
			message.setSubject(subject);

			// Create the multi-part
			Multipart multipart = new MimeMultipart();

			// Create part one
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setText(body);

			// Add the first part
			multipart.addBodyPart(messageBodyPart);


			// Put parts in message
			message.setContent(multipart);

			Transport.send(message);

		} catch (AddressException e) {
			result = false;
			log.error(e.getMessage());
			LogStackTrace.log2StackTrace(log, Level.ERROR, e.getStackTrace());
		} catch (MessagingException e) {
			result = false;
			log.error(e.getMessage());
			LogStackTrace.log2StackTrace(log, Level.ERROR, e.getStackTrace());
		}

		return result;
	}
}
