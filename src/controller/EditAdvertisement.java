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
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Ad;
import model.Follower;
import model.User;
import nl.captcha.Captcha;

/**
 * Servlet implementation class Advertisement
 */
/*
 * This Servlet verifies the user input and, when there's no problem,
 * saves it in the database
 */
public class EditAdvertisement extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditAdvertisement() {
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

		String userId = request.getParameter("userId");
		String adId = request.getParameter("adId");
		
		String title = request.getParameter("title");
		String forename = request.getParameter("forename");
		String surname = request.getParameter("surname");
		String academicTitle = request.getParameter("academicTitle");
		String email = request.getParameter("email");

		String projectStartDay = request.getParameter("projectStartDay");
		String projectStartMonth = request.getParameter("projectStartMonth");
		String projectStartYear = request.getParameter("projectStartYear");
		
		String description = request.getParameter("description");
		String institute = request.getParameter("institute");
		String projectType = request.getParameter("projectType");
		String[] targeted = request.getParameterValues("targeted");
		
		String areFollowersVisible = request.getParameter("followersVisible");
		String captchaResponse = request.getParameter("captchaResponse");
		
		Integer maxTitleLength=Integer.parseInt(controller.Settings.getProperty("maxtitlelength")[0]);
		Integer maxWordLength=Integer.parseInt(controller.Settings.getProperty("maxwordlength")[0]);			
		
		boolean correct = true;
		
		boolean isActiv = false; 	


		Captcha captcha = (Captcha) request.getSession().getAttribute (Captcha.NAME);
		
		
		Ad ad;
		Date adCreatedDate = new Date();
		Date projectStartDate = new Date();

		//validating of the input starts here
		/**
		 *  If the input contained html code, the user has to correct the input
		 */
		
		String emailVal = InputSanitizer.checkEmail(email);
		if (emailVal != null)
		{
			correct = false;
			request.setAttribute("emailValidate", emailVal);
		}
		
		//  Get user Information if possible
		if (userId == null)
		{
			try 
			{
				boolean userExists = DBAdministration.existsUserByEmail(email);
				if (userExists)
				{
					User user = DBAdministration.getUserByEmail(email);
					academicTitle = user.getAcademicTitle();
					forename = user.getForname();
					surname = user.getSurname();
				}
			} 
			catch (SQLException e) 
			{
				Logging.getLogger().severe("Failed to get User Information at EditAdvertisment.java.\n" + e.getMessage());
			}
		}
		else
		{
			try 
			{
				boolean userExists = DBAdministration.existsUserByUserId(userId);
				if (userExists)
				{
					User user = DBAdministration.getUserByUserId(userId);
					academicTitle = user.getAcademicTitle();
					forename = user.getForname();
					surname = user.getSurname();
				}
				else
				{
					userId = null;
				}
			} 
			catch (SQLException e) 
			{
				Logging.getLogger().severe("Failed to get User Information at EditAdvertisment.java.\n" + e.getMessage());
				e.printStackTrace();
			}
		}
		
		if (adId != null) // Anzeige wird nur bearbeitet
		{
			try 
			{
				Ad adv = DBAdministration.getAd(adId);
				if (adv != null)
				{
					isActiv = adv.isActiv();
				}
			}
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		 
		
		String titleVal = InputSanitizer.checkString(title);
		if (titleVal != null)
		{
			correct = false;
			request.setAttribute("titleValidate", titleVal);
		}
		else
		{
			titleVal = InputSanitizer.checkString(title, maxTitleLength, maxWordLength);
			if (titleVal != null)
			{
				correct = false;
				request.setAttribute("titleValidate", titleVal);
			}
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
		
		String academicTitleVal = InputSanitizer.checkString(academicTitle);
		if (academicTitleVal != null && !"".equals(academicTitle))
		{
			correct = false;
			request.setAttribute("academicTitleValidate", academicTitleVal);
		}

		String startDateVal = InputSanitizer.checkDate(projectStartDay, projectStartMonth, projectStartYear);
		if (startDateVal != null)
		{
			try
			{
				// Pruefe den Projektstart auf den Tag genau (nicht auf die Milisekunde)
				
				long startTime = Long.parseLong(startDateVal);
				double timeNow = new Date ().getTime();
				
				long var = (1000 * 60 * 60 * 24); //  1 Tag in Milisekunden
				boolean tmp = (timeNow - startTime < var); // startTime darf nicht mehr als 1 Tag in der Vergangenheit liegen
				
				if (!tmp)
				{
					startDateVal = "Der Projektstart darf nicht in der Vergangenheit liegen.";
					throw new Exception ("Fehlerhaftes Projektstartdatum");
				}
				
				projectStartDate = new Date (startTime);
			}
			catch (Exception e)
			{
				projectStartDate = new Date ();
				//  Es gab eine Fehlermeldung bei der Validierung
				request.setAttribute("projectStartValidate", startDateVal);
				correct = false;
			}
		}
		
				
		String descriptionVal = InputSanitizer.checkTinyMCEText(description);
		if (descriptionVal != null)
		{
			correct = false;
			request.setAttribute("descriptionValidate", descriptionVal);
		}
		else
		{
			try
			{
				description = InputSanitizer.checkAttachment(description);
			} 
			catch (NoSuchAlgorithmException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
		}
		
		if (areFollowersVisible == null || !InputSanitizer.validate(areFollowersVisible)) 
		{
			correct = false;
		}
		
		
		if (institute == null || !InputSanitizer.validate(institute) || institute.equals("Bitte auswählen")) 
		{
			request.setAttribute("instituteValidate", "Es muss ein Institut ausgewählt werden.");	
			correct = false;
		}

		if (projectType == null || !InputSanitizer.validate(projectType)) 
		{
			
			request.setAttribute("projectTypeValidate", "Es muss ein Projekttyp ausgewählt werden.");

			correct = false;
		}
		
		String targetGroupsVal = InputSanitizer.checkTargetGroups(targeted);
		if (targetGroupsVal != null)
		{
			request.setAttribute("targetedValidate", targetGroupsVal);
			correct = false;
		}

		if (request.getParameter("adCreated") != null || !"".equals(request.getParameter("adCreated")))
		{
			try
			{
				long temp = Long.parseLong(request.getParameter("adCreated"));
				adCreatedDate = new Date (temp);
			}
			catch (Exception e)
			{
				adCreatedDate = new Date ();
				//System.out.println ("adCreated" + e.getMessage());
			}
		}
		else
		{
			adCreatedDate = new Date ();
		}

		if (correct && (adId == null || "".equals(adId)))
		{
			adId = UUID.randomUUID().toString();
		} 
		
		// If the input is accepted the ad will be saved in the database,
		// otherwise the user is redirected to the input page to correct the
		// errors
		ad = new Ad(
				title, 
				academicTitle, 
				forename, 
				surname, 
				email.toLowerCase(),
				projectStartDate, 
				adCreatedDate, 
				institute,
				projectType, 
				targeted, 
				description, 
				adId, 
				null,
				areFollowersVisible, 
				null, 
				false, 
				isActiv,
				new LinkedList<Follower>());

		// if the ad does not yet have an id, the ad is new and will be created
		// here
		if ("createNewAd".equals(request.getParameter("edit")) && correct) 
		{
			//Captchaprüfung nur beim Erstellen einer neuen Anzeige (auch wenn der User schon existiert)
			String captchaVal = InputSanitizer.checkCaptcha(captcha, captchaResponse);
			if (captchaVal != null)
			{
				request.setAttribute("captcha", captchaVal);
				request = sendData.NewAdvertisment.sendData(request, userId, title, academicTitle, forename, 
						surname, email, projectType, targeted, areFollowersVisible, institute, description, 
						Long.toString(adCreatedDate.getTime()), projectStartDay, projectStartMonth, projectStartYear);			
				
				// Hier darf die adId nicht gesetzt werden, da sonst der Ablauf in den Bearbeitungsmodus
				// über geht und somit nie eine Anzeige erstellt wird
				
				dispatcher = request.getRequestDispatcher("EditAdvertisement.jsp");
			}
			else
			{
				try 
				{
					ad.setVisible(false);
					String initiatorId = DBAdministration.addActiveAd(ad);
					
					Mailer mail = new Mailer();
					String url = request.getRequestURL().toString();
					url = url.replace("EditAdvertisement", "PersonalSection?userId=" + initiatorId);
					
					mail.sendAdLink(url, ad.getEmailToInitiator(), ad.getTitle(),
							ad.getAcademicTitle() + " " + ad.getSurname());
					
					if (userId != null)
					{
						request.setAttribute("userId", userId);
						request.setAttribute ("adId", adId);
					}
					
					
					request.setAttribute("source", "advertisement");
				} 
				catch (MessagingException me)
				{
					Logging.getLogger().severe("Unable to send Email after creating new Ad!\n" + me.getMessage());
					request.setAttribute("source", "sending-mail-failed");
				}
				catch (SQLException e) 
				{
					Logging.getLogger().severe("Failed to create ad at EditAdvertisment.java. " + e.getMessage());
					request.setAttribute("source", "advertisement-fail");
				}
				dispatcher = request.getRequestDispatcher("information.jsp");				
			}
		} 
/*Anzeige bearbeiten*/
		else if ("editAd".equals(request.getParameter("edit")) && correct) 
		{
			try 
			{
				DBAdministration.updateAd(ad);
				request.setAttribute("userId", userId);
				request.setAttribute("adId", ad.getAdId());
				request.setAttribute("source", "advertisement-edit");
			} 
			catch (SQLException e) 
			{
				Logging.getLogger().severe("Failed to update ad at EditAdvertisment.java. " + e.getMessage());
				request.setAttribute("source", "advertisement-edit-fail");
				if (userId != null)
					request.setAttribute("userId", userId);
				
				if (adId != null)
					request.setAttribute("adId", adId);
			}
			dispatcher = request.getRequestDispatcher("information.jsp");
		} 
/*Fehlerhafte Eingabe: */
		else 
		{	
			//  Diese Funktion funktioniert auch für das bearbeiten von Anzeigen
			request = sendData.NewAdvertisment.sendData(request, userId, title, academicTitle, forename, 
					surname, email, projectType, targeted, areFollowersVisible, institute, description, 
					Long.toString(adCreatedDate.getTime()), projectStartDay, projectStartMonth, projectStartYear);			
			
			if (adId != null){
				request.setAttribute("adId", adId);				
			}
			
			dispatcher = request.getRequestDispatcher("EditAdvertisement.jsp");
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

}
