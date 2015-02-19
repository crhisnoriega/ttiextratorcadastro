package br.com.tti.extratorcadastro.email;

import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPAddressFailedException;
import com.sun.mail.smtp.SMTPAddressSucceededException;
import com.sun.mail.smtp.SMTPSendFailedException;
import com.sun.mail.smtp.SMTPTransport;

public class EmailSender {

	// private ResourceBundle bundlescnpjs;

	private ResourceBundle bundleconfig;
	private Hashtable<String, String> emails = new Hashtable<String, String>();

	private boolean autenticar;
	private boolean ssl;

	public void myMailRaw(String[] to, String subj, String mesagem,
			String[] filenames) throws Exception {

		String from = "cnoriega@ttisolucoes.com.br"; // "crhisnoriega.noriega@taragona.com.br";
		// // from address
		String subject = subj; // the subject line
		String message = mesagem; // the body of the message
		String mailhost = "smtp.terra.com.br"; // "smtp.sao.terra.com.br";
		// //
		// SMTP
		// server
		String user = "cnoriega@ttisolucoes.com.br"; // "taragona.crhistian";
		// // user ID
		String password = "crhisn25"; // "tarachr"; //
		// password
		// password
		boolean auth = true;
		boolean ssl = false;
		Properties props = System.getProperties();

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", mailhost);
		props.put("mail.smtp.port", "587");
		props.put("mail.transport.protocol", "smtp");

		// Get a Session object
		javax.mail.Session session = javax.mail.Session
				.getInstance(props, null);

		// Construct the message
		javax.mail.Message msg = new MimeMessage(session);
		// Set message details
		InternetAddress[] addressTo = new InternetAddress[to.length];
		for (int i = 0; i < to.length; i++) {
			addressTo[i] = new InternetAddress(to[i]);
		}

		msg.setFrom(new InternetAddress(from));
		msg.setRecipients(javax.mail.Message.RecipientType.TO, addressTo);
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		msg.setText(message);

		Multipart mp = new MimeMultipart();

		MimeBodyPart mbp2 = new MimeBodyPart();
		mbp2.setText(message);
		mp.addBodyPart(mbp2);

		for (String filename : filenames) {

			if (filename != null) {
				MimeBodyPart mbp = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(filename);
				mbp.setDataHandler(new DataHandler(fds));
				mbp.setFileName(fds.getName());
				mp.addBodyPart(mbp);
			}

			msg.setContent(mp);
		}
		// Send the thing off
		SMTPTransport t = (SMTPTransport) session.getTransport(ssl ? "smtps"
				: "smtp");
		try {
			t.connect(mailhost, user, password);
			t.sendMessage(msg, msg.getAllRecipients());
		} finally {
			t.close();
		}
		System.out.println("Email OK");

	}

	private void log(String string) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		EmailSender emailsender = new EmailSender();
		String[] to = new String[] { "crhisnoriega@gmail.com" };
		String subj = "Teste";
		String mesagem = "Mensagem";
		String[] filenames = new String[] { "C:\\TTINFE2.0\\conf\\configuracao.xml" };
		try {
			emailsender.myMailRaw(to, subj, mesagem, filenames);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}