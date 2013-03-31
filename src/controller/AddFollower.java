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
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Follower;
import model.User;
import nl.captcha.Captcha;

/**
 * Servlet implementation class AddFollower
 */
public class AddFollower extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddFollower()
    {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		request.setCharacterEncoding("UTF-8");
		RequestDispatcher dispatcher;
		
		String adId = request.getParameter("adId");
		String userId = request.getParameter("userId");
		
		String forename = request.getParameter("forename");
		String surname = request.getParameter("surname");
		String email = request.getParameter("email");
		String comment = request.getParameter("comment");
		
		Boolean correct = true;
		String captchaResponse = request.getParameter("captchaResponse");
		Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);

		String emailVal = InputSanitizer.checkEmail(email);
		if (emailVal != null)
		{
			correct = false;
			request.setAttribute("emailValidate", emailVal);
		}
		
		String forenameVal = InputSanitizer.checkString(forename);
		if (forenameVal != null)
		{
			correct = false;
			request.setAttribute("forenameValidate", forenameVal);
		}
		
		String surnameVal = InputSanitizer.checkString(surname);
		if (surnameVal != null)
		{
			correct = false;
			request.setAttribute("surnameValidate", surnameVal);
		}
	    
		String commentVal = InputSanitizer.checkTinyMCEText(comment);
		if (commentVal != null && !"".equals(comment) && comment != null)
		{
			correct = false;
			request.setAttribute("commentValidate", commentVal);
		}
		
	    // if the input was valid the follower is being added to the ad and
	    // receives a confirmation mail, else he will be redirected to change
	    // his input
	    if (correct) 
	    {
	    	if (userId == null)
			{
	    		// Hier wird einfach nur das Captcha überprüft ob das übereinstimmt
				String captchaVal = InputSanitizer.checkCaptcha(captcha, captchaResponse);
				if (captchaVal != null)
				{
					request.setAttribute("captcha", captchaVal);
			    	try 
			    	{
						sendData.ViewAdvertisment.sendData(request, adId, userId, forename, surname, email, comment);
						dispatcher = request.getRequestDispatcher("./AdDetails.jsp");
			    	} 
			    	catch (Exception e) 
			    	{
						e.printStackTrace();
						dispatcher = request.getRequestDispatcher("./information.jsp");
					}
				}
				else 
				{
					try 
					{
						/*
						 *  Da userId = null wird hier nun geprüft ob es einen Benutzer mit dieser
						 *  Mailadresse schon in der Datenbank vorhanden ist.
						 *  Falls ja, dann wird der User aus der Datenbank als Follower-Objekt initialisiert
						 *  Sonst wird ein neuer Follower.
						 */
						 
						Follower follower = null;
						if (!DBAdministration.existsUserByEmail(email)) 
						{
							follower = new Follower("", forename, surname, email,
									comment, false, UUID.randomUUID().toString());
						} 
						else 
						{
							//  User exists
							User user = DBAdministration.getUserByEmail(email);
							follower = new Follower(user, comment, false);
							
							//  Checks if user is adCreator
							if (DBAdministration.getActivAd(adId).getInitiatorId().equals(user.getUserId()))
							{
								throw new Exception ("Sie sind Initiatior dieser Anzeige.");
							}
						}
						
						/*
						 * Hier wird nun geschaut ob es der User schon als Follower bei dieser Anzeige
						 * eingetragen ist. Fall nicht wird eine E-Mail an ihn rausgeschickt und er
						 * wird als unsichtbarer Follower eingetragen
						 */
						if (!DBAdministration.existsFollower(follower.getUserId(), adId)) 
						{
							DBAdministration.addFollower(follower, adId);
							Mailer mail = new Mailer();
				    		String url = request.getRequestURL().toString();
				    		url = url.replace("AddFollower", "PersonalSection?adId=" + adId + "&userId=" + follower.getUserId());
				    		mail.sendFollowerLink(url, follower.getEmail(),
				    				DBAdministration.getActivAd(adId).getTitle(),
				    				follower.getSurname());
							request.setAttribute("source", "follower");
						} 
						else
						{
							request.setAttribute("source", "follower-twice");
						}
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
						Logging.getLogger().severe(e.getMessage());
						request.setAttribute("source", e.getMessage());
					}
				
					request.setAttribute("adId", adId);
					request.setAttribute("userId", userId);
					
					dispatcher = request.getRequestDispatcher("./information.jsp");	
				}
			}
	    	else
	    	{
	    		try 
				{
	    			/*
					 *  Da userId = null wird hier nun geprüft ob es einen Benutzer mit dieser
					 *  Mailadresse schon in der Datenbank vorhanden ist.
					 *  Falls ja, dann wird der User aus der Datenbank als Follower-Objekt initialisiert
					 *  Sonst wird ein neuer Follower.
					 */
					Follower follower = null;
					follower = DBAdministration.getFollower(userId);
					
					if (follower == null) {
						
						request.setAttribute("source", "Kein gültiger Benutzer!");
						
					}else if (!DBAdministration.existsFollower(follower.getUserId(), adId)) 
					{
						/*
						 * Da der Benutzer eine gültige userId besitzt wird er als Follower
						 * zur Anzeige hinzugefügt und dann gleich auf ein aktiven Follower
						 */
						DBAdministration.addFollower(follower, adId);
						DBAdministration.setFollowerActive(follower.getUserId(), adId);
						request.setAttribute("source", "follower-trusted");
					} 
					else
					{
						request.setAttribute("source", "follower-twice");
					}
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					Logging.getLogger().severe(e.getMessage());
					request.setAttribute("source", e.getMessage());
				}
			
				request.setAttribute("adId", adId);
				request.setAttribute("userId", userId);
				
				dispatcher = request.getRequestDispatcher("./information.jsp");	
			}		
		}
/*Fehlerhafte Eingabe: zurueck zu AdDetails.jsp*/
	    else 
	    {
	    	try 
	    	{
				sendData.ViewAdvertisment.sendData(request, adId, userId, forename, surname, email, comment);
				dispatcher = request.getRequestDispatcher("./AdDetails.jsp");
	    	} 
	    	catch (Exception e) 
	    	{
				e.printStackTrace();
				dispatcher = request.getRequestDispatcher("./information.jsp");
			}
		}
    	dispatcher.forward(request,response);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
