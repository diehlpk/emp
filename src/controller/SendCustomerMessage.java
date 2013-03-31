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

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.captcha.Captcha;

import util.ToolTipps;

import model.Ad;
import model.Follower;
import model.User;

/**
 * Servlet implementation class SendCustomerMessage
 * wird von SendCustomerMessage.jsp aufgerufen
 * verarbeitet Daten und sendet falls korrekt eine Email
 */
public class SendCustomerMessage extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendCustomerMessage() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request,
     * HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException 
    {
        request.setCharacterEncoding("UTF-8");
        RequestDispatcher dispatcher;
        
        /*
         * 3 Moeglichkeiten: 
         * 1. Nachricht an Initiator
         * 2. Nachricht an Follower
         * 3. Nachricht an Admin
         */
        //  ("geheim") weitergegebene Variablen
        String userId = request.getParameter("userId");
        String adId = request.getParameter("adId");

        Ad ad = null;
		try {
			ad = DBAdministration.getAd(adId);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        
        LinkedList<Follower> followerList = new LinkedList<Follower>();
        followerList = ad.getFollowers();
        /*
        String[] followersIds = null;
        // Abfrage aller Follower und eintragen in eine LinkedList, damit man später die Follower durchgehen kann
        try {
        	followersIds =  DBAdministration.getFollowersOfAd(adId);
        	for (int i=0; i < followersIds.length; i++) {
        		followerList.add(DBAdministration.getFollower(followersIds[i]));
        	}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}        		        	
        */
        //String followerEmail = request.getParameter("followerEmail");
        // Steuerungsvariablen: Es sollte immer nur eine != null sein
        // falls beide != null => messageToInitiator wird bevorzugt
        String messageToInitiator = request.getParameter("messageToInitiator");
        String messageToFollower = request.getParameter("messageToFollower"); 
        String messageToAdmin = request.getParameter("messageToAdmin"); 
        String followerEmail = request.getParameter("followerEmail");
        
        // Eingabedaten
        String email = request.getParameter("email");
        String emailReply = request.getParameter("emailReply");
        String customerMessage = request.getParameter("customerMessage");
        String captchaResponse = request.getParameter("captchaResponse");
		Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
        
        //  Validierungsvariablen
        Boolean correct = true;
        
        //  userId darf null sein, aber falls userId existiert muss diese stimmen
       
        correct = checkUserId (userId);
        if (!correct)
        	userId = null;
        
        
        String messageVal = InputSanitizer.checkString(customerMessage);
        if (messageVal != null)
        {
        	correct = false;
        	request.setAttribute("customerMessageValidate", messageVal);
        }

        if (messageToInitiator != null)
        {	
        	// Prüfe ob die adId correct ist
        	correct = checkAdId(adId) && correct;
            
        	String captchaVal = InputSanitizer.checkCaptcha(captcha, captchaResponse);
			if (captchaVal != null)
			{
				request.setAttribute("captcha", captchaVal);
				correct = false;
			}
        	
        	String emailVal1 = InputSanitizer.checkEmail(email);
        	String emailVal2 = InputSanitizer.checkEmail(emailReply);
        	
        	if (emailVal1 != null)
        	{
        		correct = false;
        		request.setAttribute("emailValidate", emailVal1);
        	}
        	if (emailVal2 != null)
        	{
        		correct = false;
        		request.setAttribute("emailReplyValidate", emailVal2);
        	}
        	if (ad.getEmailToInitiator().equals(email))
        	{
        		correct = false;
        		request.setAttribute("emailReplyValidate", "Sie können sich keine E-Mail selbst schreiben!");
        	}
        	
        	if (correct)
        	{
        		if (!email.equals(emailReply))
        		{
        			correct = false;
        			request.setAttribute("emailReplyValidate", "Ihre Eingabe stimmt nicht mit der ersten &uuml;berein!");
        		}
        	}
        }
        else if (messageToFollower != null)
        {
        	// Prüfe ob die adId correct ist
        	correct = checkAdId(adId) && correct;
        	
        	/*
        	 * Prüfe ob die LinkedList richtig angelegt wurde und ob sich überhaupt Elemente darin
        	 * befinden. Weiter muss nichts geprüft werden, da beim Eintragen in die Datenbank
        	 * die Daten schon geprüft wurden
        	 */
        	
        	
        	if (followerList != null && followerList.size() == 0) {
        		correct = false;
        	}
        }
        else if (messageToAdmin != null)
        {
        	String emailVal1 = InputSanitizer.checkEmail(email);
        	String emailVal2 = InputSanitizer.checkEmail(emailReply);
        	
        	String captchaVal = InputSanitizer.checkCaptcha(captcha, captchaResponse);
			if (captchaVal != null)
			{
				request.setAttribute("captcha", captchaVal);
				correct = false;
			}
			
        	if (emailVal1 != null)
        	{
        		correct = false;
        		request.setAttribute("emailValidate", "Die von Ihnen eingegebene E-Mail-Adresse ist eine vom System nicht erlaubte E-Mail-Adresse");
        	}
        	if (emailVal2 != null)
        	{
        		correct = false;
        		request.setAttribute("emailReplyValidate", "Die von Ihnen eingegebenen E-Mail-Adressen stimmen nicht überein!");
        	}
        	
        	if (correct)
        	{
        		if (!email.equals(emailReply))
        		{
        			correct = false;
        			request.setAttribute("emailReplyValidate", "Ihre Eingabe stimmt nicht mit der ersten &uuml;berein!");
        		}
        	}
        }
        else
        {
        	correct = false;
        }
        //  Validierung abgeschlossen
        
/*Send the message*/
        if (correct) 
        {
            Mailer mail = new Mailer();
            
            try 
            {
            	String followerName = null;
                String initiatorName = null;
                
                if (messageToInitiator != null) // send message to Initiator
                {
                	initiatorName = ad.getAcademicTitle() + " " + ad.getForename() + " " + ad.getSurname();
                    initiatorName = initiatorName.trim();
                    
                	mail.sendCustomerMessageInitiator(ad.getEmailToInitiator(), email,
                            customerMessage, ad.getTitle(), initiatorName);
                }
                
                if (messageToFollower != null) //  send message to follower 
                {
                	initiatorName = ad.getAcademicTitle() + " " + ad.getForename() + " " + ad.getSurname();
                    initiatorName = initiatorName.trim();
                    
                    String followerEmailVal = InputSanitizer.checkEmail(followerEmail);
                    if (followerEmailVal != null)
                    {
                    	correct = false;
                    }
                    
                    if (correct){
                    	if (messageToFollower.equals("One")){
                        	for(Follower item: followerList ) {
                        		if (item.getEmail().equals(followerEmail)) {
                        			followerName = item.getForname().trim() + " " + item.getSurname().trim();
                        			mail.sendCustomerMessageFollower(item.getEmail(), ad.getEmailToInitiator(),
                                            customerMessage, ad.getTitle(), followerName, initiatorName);	
                        			break;
                        		}
                        	}
                    	}	
                    }
                    
    	        	if (messageToFollower.equals("All")){
    	        		for(Follower item: followerList ) {
    	        			followerName = item.getForname().trim() + " " + item.getSurname().trim();
                			mail.sendCustomerMessageFollower(item.getEmail(), ad.getEmailToInitiator(),
                                    customerMessage, ad.getTitle(), followerName, initiatorName);
                    	}
    	        	}	
                }
                
                if (messageToAdmin != null) // send message to Admin
                {
                	mail.sendCustomerMessageAdmin( email, customerMessage);
                }
         
                request.setAttribute("source", "customerMessage");
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                Logging.getLogger().severe(e.getMessage());
                request.setAttribute("source", e.getMessage());
            }
            finally
            {
            	if (userId != null)
            		request.setAttribute("userId", userId);
                
            	request.setAttribute("adId", adId);
                dispatcher = request.getRequestDispatcher("./information.jsp");
            }            
        } 
/* Not Correct */
        else 
        {        
            if (userId != null)
            {
            	request.setAttribute("userId", userId);
            	try 
            	{
					User user = DBAdministration.getUserByUserId(userId);
					email = user.getEmail();
					emailReply = user.getEmail();
					request.setAttribute("readonly", "readonly");
            	} 
            	catch (SQLException e)
            	{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            request.setAttribute("adId", adId);
            request.setAttribute("customerMessage", customerMessage);
            
            if (messageToInitiator != null)
            {
            	request.setAttribute("toolTippEmail", ToolTipps.getEmailToolTipp());
            	request.setAttribute("messageToInitiator", "messageToInitiator");
            	request.setAttribute("email", email);
            	request.setAttribute("emailReply", emailReply);
            	dispatcher = request.getRequestDispatcher("./SendCustomerMessage.jsp");
            }
            else if (messageToFollower != null)
            {
            	request.setAttribute("messageToFollower", messageToFollower);
            	dispatcher = request.getRequestDispatcher("./SendCustomerMessage.jsp");
            }
            else if (messageToAdmin != null)
            {
            	request.setAttribute("messageToAdmin", "messageToAdmin");
            	request.setAttribute("email", email);
            	request.setAttribute("emailReply", emailReply);
            	dispatcher = request.getRequestDispatcher("./SendCustomerMessage.jsp");
            }
            else
            {
            	request.setAttribute("source", "Kein Empfaenger fuer diese Nachricht.");
            	dispatcher = request.getRequestDispatcher("./information.jsp");
            }            
        }
        dispatcher.forward(request,response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request,
     * HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private boolean checkUserId (String userId)
    {
    	boolean correct = true;
    	
    	if (userId != null) 
        {
    		if (!InputSanitizer.validate(userId))
         		correct = false;
         	else if (!"".equals(userId))
         	{
         		boolean userExists = false;
         		try 
         		{
 					userExists = DBAdministration.existsUserByUserId(userId);
 				} 
         		catch (SQLException e)
         		{
 					e.printStackTrace();
 				}
         		
         		if (!userExists)
         			correct = false;
         	}
        }
    	 
    	 return correct;
    }
    
    private boolean checkAdId (String adId)
    {
    	boolean correct = true;
    	if (adId == null) 
        {
            correct = false;
        }
        else if (!InputSanitizer.validate(adId))
        {
        	adId = null;
        	correct = false;
        }

    	return correct;
    }
    
}