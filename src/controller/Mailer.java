/* This file is part of IMP.

    IMP is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    IMP is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with IMP. If not, see <http://www.gnu.org/licenses/>.
 */
package controller;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import util.*;

public class Mailer {

	private String from = Settings.getProperty("mailfrom")[0];
	private String user = controller.Settings.getProperty("mailuser")[0];
	private String password = controller.Settings.getProperty("mailpassword")[0];
	private String host = controller.Settings.getProperty("mailhost")[0];
	private String useLocalhost = Settings.getProperty("uselocalhost")[0];
	private String adminmail = Settings.getProperty("adminmail")[0];
	private int PORT = Integer.parseInt(Settings.getProperty("mailport")[0]); //995 usally secure

	private Properties props = null;

	/**
	 * This method prepare the session for sending a Mail.
	 * 
	 * @return The prepared Message with all Properties.
	 */
	private Session prepareMessage() {
		if (props == null) {
			props = new Properties();

			if (useLocalhost.equals("false")) {
				props.put("mail.smtp.starttls.enable", "true");
			}

		}
		Session session = Session.getDefaultInstance(props, null);
		return session;
	}

	/**
	 * This method send the given message with the session.
	 * 
	 * @param message
	 *            The message to send.
	 * 
	 * @param session
	 *            The session, which belongs so the message.
	 * 
	 * @throws MessagingException
	 */
	private void sendMessage(Message message, Session session)
			throws MessagingException {

		Transport transport = session.getTransport("smtp");

		if (useLocalhost.equals("false")) {
			transport.connect(host, PORT, user, password);
		} else {
			transport.connect();
		}

		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

	/**
	 * This method sends a message with the given link. The content of the
	 * message is the creation of an ad.
	 * 
	 * @param url
	 *            The Link that is send to the receiver.
	 * 
	 * @param toAdress
	 *            The eMail of the receiver.
	 * 
	 * @param title
	 *            The Title of the receiver.
	 * 
	 * @param advertiser
	 *            The name of the receiver.
	 * 
	 * @throws MessagingException
	 */
	public void sendAdLink(String url, String toAdress, String title,
			String advertiser) throws MessagingException {

		// The text of the email is set here.
		String text = Messages.getString("createText").replaceAll("%url%", url);
		text = text.replaceAll("%title%", title);
		text = text.replaceAll("%advertiser%", advertiser);
		text = text.replaceAll("%adminmail%", adminmail);

		// Stuff to send the email
		Session session = prepareMessage();
		
		MimeMessage message = new MimeMessage(session);

		message.setSubject(Messages.getString("createSubject").replaceAll(
				"%title%", title), "UTF-8");
		message.setText(text, "UTF-8");
		Address address = new InternetAddress(from);
		message.setFrom(address);
		Address toAddress = new InternetAddress(parseTestAddesses(toAdress));
		message.addRecipient(Message.RecipientType.TO, toAddress);
		sendMessage(message, session);
	}

	/**
	 * This method sends a message. The content of the message is the activation
	 * of an ad.
	 * 
	 * @param id
	 *            The id of the activated ad.
	 * 
	 * @param toAdress
	 *            The eMail of the receiver.
	 * 
	 * @param title
	 *            The Title of the receiver.
	 * 
	 * @throws MessagingException
	 */
	public void sendAdAuth(String id, String toAdress, String title)
			throws MessagingException {

		// The text of the email is set here.
		String text = Messages.getString("activateText").replaceAll("%id%", id);
		text = text.replaceAll("%adminmail%", adminmail);

		// Stuff to send the email
		Session session = prepareMessage();
		MimeMessage message = new MimeMessage(session);

		message.setSubject(Messages.getString("activateSubject").replaceAll(
				"%title%", title), "UTF-8");
		message.setText(text, "UTF-8");
		Address address = new InternetAddress(from);
		message.setFrom(address);
		Address toAddress = new InternetAddress(parseTestAddesses(toAdress));
		message.addRecipient(Message.RecipientType.TO, toAddress);
		sendMessage(message, session);

	}

	/**
	 * This method sends a message with the given link. The content of the
	 * message is the registration as a follower.
	 * 
	 * @param url
	 *            The Link that is send to the receiver.
	 * 
	 * @param toAdress
	 *            The eMail of the receiver.
	 * 
	 * @param title
	 *            The Title of the receiver.
	 * 
	 * @param follower
	 *            The name of the receiver.
	 * 
	 * @throws MessagingException
	 */
	public void sendFollowerLink(String url, String toAdress, String title,
			String follower) throws MessagingException {

		// The text of the email is set here.
		String text = Messages.getString("followerText").replaceAll("%url%",
				url);
		text = text.replaceAll("%title%", title);
		text = text.replaceAll("%follower%", follower);
		text = text.replaceAll("%adminmail%", adminmail);

		// Stuff to send the email
		Session session = prepareMessage();
		MimeMessage message = new MimeMessage(session);

		message.setSubject(Messages.getString("followerSubject").replaceAll(
				"%title%", title), "UTF-8");
		message.setText(text, "UTF-8");
		Address address = new InternetAddress(from);
		message.setFrom(address);
		Address toAddress = new InternetAddress(parseTestAddesses(toAdress));
		message.addRecipient(Message.RecipientType.TO, toAddress);
		sendMessage(message, session);
	}

	/**
	 * This method send the message. The content of the message is the deletion
	 * of an ad.
	 * 
	 * @param title
	 *            The title of the receiver.
	 * 
	 * @param toAdress
	 *            The eMail of the receiver.
	 * 
	 * @param title
	 *            The Title of the receiver.
	 * 
	 * @param advertiser
	 *            The name of the receiver.
	 * 
	 * @throws MessagingException
	 */
	public void sendAdDeleted(String title, String toAdress, String follower,
			String advertiser) throws MessagingException {

		// The text of the email is set here.
		String text = Messages.getString("deleteText").replaceAll("%title%",
				title);
		text = text.replaceAll("%follower%", follower);
		text = text.replaceAll("%advertiser%", advertiser);
		text = text.replaceAll("%adminmail%", adminmail);

		// Stuff to send the email
		Session session = prepareMessage();
		MimeMessage message = new MimeMessage(session);

		message.setSubject(Messages.getString("deleteSubject").replaceAll(
				"%title%", title), "UTF-8");
		message.setText(text, "UTF-8");
		Address address = new InternetAddress(from);
		message.setFrom(address);
		Address toAddress = new InternetAddress(parseTestAddesses(toAdress));
		message.addRecipient(Message.RecipientType.TO, toAddress);
		sendMessage(message, session);
	}

	/**
	 * This method sends a message with the information that a new follower
	 * registered.
	 * 
	 * @param url
	 *            The Link that is send to the receiver.
	 * 
	 * @param toAdress
	 *            The eMail of the receiver.
	 * 
	 * @param advertiser
	 *            The name of the receiver.
	 * 
	 * @param title
	 *            The Title of the receiver.
	 * 
	 * @throws MessagingException
	 */
	public void sendNewFollower(String url, String toAdress, String advertiser,
			String title) throws MessagingException {

		// The text of the email is set here.
		// It will soon contain a short description and the url to edit the ad.
		String text = Messages.getString("newFollowerText").replaceAll("%url%",
				url);
		text = text.replaceAll("%advertiser%", advertiser);
		text = text.replaceAll("%title%", title);
		text = text.replaceAll("%adminmail%", adminmail);

		// Stuff to send the email
		Session session = prepareMessage();
		MimeMessage message = new MimeMessage(session);

		message.setSubject(Messages.getString("newFollowerSubject").replaceAll(
				"%title%", title), "UTF-8");
		message.setText(text, "UTF-8");
		Address address = new InternetAddress(from);
		message.setFrom(address);
		Address toAddress = new InternetAddress(parseTestAddesses(toAdress));
		message.addRecipient(Message.RecipientType.TO, toAddress);
		sendMessage(message, session);
	}

	/**
	 * Sends a message to the Initiator of a ad
	 * 
	 * @param toAdress
	 *            The eMail of the receiver.
	 * 
	 * @param replyAdress
	 *            The address of the customer.
	 * 
	 * @param title
	 *            The Title of the ad.
	 * 
	 * @param customerMessage
	 *            The message of the customer.
	 * 
	 * @throws MessagingException
	 */
	public void sendCustomerMessageInitiator(String initiatorEmailAdress,
			String replyEmailAdress, String customerMessage, String title, String initiatorName)
			throws MessagingException {

		String text = Messages.getString("customerMessageText").replaceAll(
				"%email%", replyEmailAdress);
		text = text.replaceAll("%text%", customerMessage);
		text = text.replaceAll("%adminmail%", adminmail);
		text = text.replaceAll("%advertiser%", initiatorName);
		text = text.replaceAll("%title%", title);
		

		// Stuff to send the email
		Session session = prepareMessage();
		MimeMessage message = new MimeMessage(session);

		message.setSubject(Messages
				.getString("customerMessageInitiatorSubject").replaceAll(
						"%title%", title), "UTF-8");
		message.setText(text, "UTF-8");
		Address address = new InternetAddress(from);
		message.setFrom(address);
		Address toAddress = new InternetAddress(parseTestAddesses(initiatorEmailAdress));
		message.addRecipient(Message.RecipientType.TO, toAddress);
		Address replyAddress = new InternetAddress(
				parseTestAddesses(replyEmailAdress));
		message.setReplyTo(new Address[] { replyAddress });
		sendMessage(message, session);
	}

	/**
	 * This method sends a customer message to the follower of the specified ad
	 * 
	 * @param toAdress
	 *            The eMail of the receiver.
	 * 
	 * @param replyAdress
	 *            The address of the customer.
	 * 
	 * @param title
	 *            The Title of the ad.
	 * 
	 * @param customerMessage
	 *            The message of the customer.
	 * 
	 * @throws MessagingException
	 */
	public void sendCustomerMessageFollower(String toAdress,
			String replyAdress, String customerMessage, String title, String follower, String initiatorName)
			throws MessagingException {

		String text = Messages.getString("customerMessageFollowerText").replaceAll(
				"%email%", replyAdress);
		text = text.replaceAll("%title%", title);
		text = text.replaceAll("%follower%", follower);
		text = text.replaceAll("%text%", customerMessage);
		text = text.replaceAll("%adminmail%", adminmail);

		// Stuff to send the email
		Session session = prepareMessage();
		MimeMessage message = new MimeMessage(session);

		String subjectText = Messages.getString("customerMessageFollowerSubject")
				.replaceAll("%title%", title);
		subjectText = subjectText.replace("%initiatorName%", initiatorName);
		message.setSubject(subjectText, "UTF-8");
		message.setText(text, "UTF-8");
		Address address = new InternetAddress(from);
		message.setFrom(address);
		Address toAddress = new InternetAddress(parseTestAddesses(toAdress));
		message.addRecipient(Message.RecipientType.TO, toAddress);
		Address replyAddress = new InternetAddress(
				parseTestAddesses(replyAdress));
		message.setReplyTo(new Address[] { replyAddress });
		sendMessage(message, session);
	}
	
	/**
	 * Sends a message to the administrator
	 * 
	 * @param replyAdress
	 *            The address of the customer.
	 * 
	 * @param customerMessage
	 *            The message of the customer.
	 * 
	 * @throws MessagingException
	 */
	public void sendCustomerMessageAdmin(String replyEmailAdress, String customerMessage)
			throws MessagingException {

		String text = Messages.getString("customerMessageAdminText").replaceAll(
				"%email%", replyEmailAdress);
		text = text.replaceAll("%text%", customerMessage);
		text = text.replaceAll("%adminmail%", adminmail);
		

		// Stuff to send the email
		Session session = prepareMessage();
		MimeMessage message = new MimeMessage(session);

		String subjectText = Messages.getString("customerMessageAdminSubject");
		message.setSubject(subjectText, "UTF-8");
		
		message.setText(text, "UTF-8");
		Address address = new InternetAddress(from);
		message.setFrom(address);
		Address toAddress = new InternetAddress(parseTestAddesses(controller.Settings.getProperty("adminmail")[0]));
		message.addRecipient(Message.RecipientType.TO, toAddress);
		Address replyAddress = new InternetAddress(
				parseTestAddesses(replyEmailAdress));
		message.setReplyTo(new Address[] { replyAddress });
		sendMessage(message, session);
	}

	/**
	 * This method sends a message with the information that a new follower
	 * registered.
	 * 
	 * @param url
	 *            The Link that is send to the receiver.
	 * 
	 * @param toAdress
	 *            The eMail of the receiver.
	 * 
	 * @param advertiser
	 *            The name of the receiver.
	 * 
	 * @param title
	 *            The Title of the receiver.
	 * 
	 * @throws MessagingException
	 */
	public void sendLink(String url, String toAdress, String advertiser)
			throws MessagingException {

		// The text of the email is set here.
		// It will soon contain a short description and the url to edit the ad.
		String text = Messages.getString("linkText").replaceAll("%url%", url);
		text = text.replaceAll("%advertiser%", advertiser);
		text = text.replaceAll("%adminmail%", adminmail);

		// Stuff to send the email
		Session session = prepareMessage();
		MimeMessage message = new MimeMessage(session);

		message.setSubject(Messages.getString("linkSubject"), "UTF-8");
		message.setText(text, "UTF-8");
		Address address = new InternetAddress(from);
		message.setFrom(address);
		Address toAddress = new InternetAddress(toAdress);
		message.addRecipient(Message.RecipientType.TO, toAddress);
		sendMessage(message, session);
	}
	
	public void sendDeleteNotification(String delString, String toAdress, String title, String advertiser)
			throws MessagingException {

		// The text of the email is set here.
		// It will soon contain a short description and the deleted description.
		String text = Messages.getString("deleteText").replaceAll("%title%", title);
		text = text.replaceAll("%description%", delString);
		text = text.replaceAll("%advertiser%", advertiser);
		text = text.replaceAll("%adminmail%", adminmail);

		// Stuff to send the email
		Session session = prepareMessage();
		MimeMessage message = new MimeMessage(session);

		message.setSubject(Messages.getString("deleteSubject").replaceAll("%title%", title), "UTF-8");
		message.setText(text, "UTF-8");
		Address address = new InternetAddress(from);
		message.setFrom(address);
		Address toAddress = new InternetAddress(toAdress);
		message.addRecipient(Message.RecipientType.TO, toAddress);
		sendMessage(message, session);
	}
	
	/**
	 * 
	 * @param toAdress
	 * @param initiatorName
	 * @param title
	 * @param deadline
	 * @param date
	 * @param url
	 * @throws MessagingException
	 */
	public void sendDeactivationDeadlineMessage (String toAdress, String initiatorName, String title, String deadline, String date, String url) throws MessagingException
	{
		String messageSubject = Messages.getString("deactivateDeadlineSubject");
		messageSubject = messageSubject.replaceAll("%title%", title);
		
		String messageText = Messages.getString("deactivateDeadlineText");
		messageText = messageText.replaceAll("%initiator%", initiatorName);
		messageText = messageText.replaceAll("%deadline%", deadline);
		messageText = messageText.replaceAll("%date%", date);
		messageText = messageText.replaceAll("%personalarea%", "<a href=\"" + url +"\">" + url + "</a>");
		messageText = messageText.replaceAll("\\n", "<br>");
		
		Session session = prepareMessage();
		MimeMessage message = new MimeMessage(session);
		message.setSubject(messageSubject, "UTF-8");
		message.setContent(messageText, "text/html; charset=utf-8");
		message.saveChanges();
		
		Address address = new InternetAddress(from);
		message.setFrom(address);
		Address toAddress = new InternetAddress(toAdress);
		message.addRecipient(Message.RecipientType.TO, toAddress);
		
		sendMessage(message, session);
	}

	/**
	 * 
	 * @param toAdress
	 * @param initiatorName
	 * @param title
	 * @param url
	 * @throws MessagingException
	 */
	public void sendDeactivatedAdAutomatic (String toAdress, String initiatorName, String title, String url) throws MessagingException
	{
		String messageSubject = Messages.getString("deactivatedAdAutomaticSubject");
		messageSubject = messageSubject.replaceAll("%title%", title);
		
		String messageText = Messages.getString("deactivatedAdAutomaticText");
		messageText = messageText.replaceAll("%initiator%", initiatorName);
		messageText = messageText.replaceAll("%personalarea%", url);
		messageText = messageText.replaceAll("\\n", "<br/>");
		
		Session session = prepareMessage();
		MimeMessage message = new MimeMessage(session);
		message.setSubject(messageSubject, "UTF-8");
		message.setContent(messageText, "text/html; charset=utf-8");
		message.saveChanges();
		
		Address address = new InternetAddress(from);
		message.setFrom(address);
		Address toAddress = new InternetAddress(toAdress);
		message.addRecipient(Message.RecipientType.TO, toAddress);
		
		sendMessage(message, session);
	}
	
	private String parseTestAddesses(String address) {
		if (address.endsWith("@imp.test.change.me")) {
			return "somestudi@studi.change.me";
		} else {
			return address;
		}
	}

}