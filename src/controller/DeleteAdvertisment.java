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

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Ad;
import nl.captcha.Captcha;

public class DeleteAdvertisment extends HttpServlet 
{
	public DeleteAdvertisment ()
	{
		super ();
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{		
		request.setCharacterEncoding("UTF-8");
		RequestDispatcher dispatcher;
		
		
		// Get sid and adId to get all the relevant information
		String userId = request.getParameter("userId");
		String adId = request.getParameter("adId");
		
		boolean correct = true;
		
		Ad ad = null;
		try
		{
			ad = DBAdministration.getAd(adId);
		}
		catch (SQLException e) 
		{
			correct = false;
			System.out.println ("DeleteAdvertisment.java: Ad nicht bekommen.");
			e.printStackTrace();
		}
		
		String captchaResponse = request.getParameter("captchaResponse");
		Captcha captcha = (Captcha) request.getSession().getAttribute (Captcha.NAME);
		
		String captchaVal = InputSanitizer.checkCaptcha(captcha, captchaResponse);
		if (captchaVal != null || "".equals(captchaResponse))
		{
			correct = false;
			request.setAttribute("captcha", captchaVal);
		}		

		if (ad == null)
		{
			//  es gibt keine ad mit der zugehoerigen adId
			request.setAttribute("userId", userId);
			dispatcher = request.getRequestDispatcher("./information.jsp");
		}
		else if (!correct) // Fehlerhafte Eingabe: Zurueck zu DeleteAdvertisment.jsp
		{			
			try 
			{
				request = sendData.DeleteAdvertisment.sendData(request, adId, userId);
				dispatcher = request.getRequestDispatcher("./DeleteAdvertisment.jsp");
			}
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				dispatcher = request.getRequestDispatcher("./information.jsp");
			}			
		}
		else
		{
			try 
			{
				String description = ad.getDescription();
				DBAdministration.deleteAd(adId);
				
				removeAttachments(description);
				
				if (!DBAdministration.existsUserByUserId(userId)) 
				{
					request.setAttribute("source", "user-dont-exist");
					request.setAttribute("userId", null);
				}
				else 
				{
					request.setAttribute("userId", userId);
					request.setAttribute("source", "PositiveDeleteAdvert");
				}			

				dispatcher = request.getRequestDispatcher("./information.jsp");	
			
			}
			catch (Exception e)
			{
				dispatcher = request.getRequestDispatcher("./information.jsp");
				e.printStackTrace();
			}
		}
		
		
		dispatcher.forward(request,response);
	}
	
	/*
	 * Sollte eigentlich in die DBAdministration Methode zur L�schung von Anzeigen
	 */
	protected void removeAttachments(String description)
	{
		if (description.contains("/uploads/")) 
		{
			String[] split = description.split("\"");
			for (String s : split) {
				if (s.contains("uploads/")){
					File newFile = new File("/var/lib/tomcat6/webapps/" + s);
					if (s.startsWith("/uploads/"))
					{
						newFile.delete();
					}
				}
			}		
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException 
	{
		String adId = request.getParameter("adId");
		Ad ad = null;
		
		boolean correct = true;

		String captchaResponse = request.getParameter("captchaResponse");
		Captcha captcha = (Captcha) request.getSession().getAttribute (Captcha.NAME);
				
		String captchaVal = InputSanitizer.checkCaptcha(captcha, captchaResponse);
		if (captchaVal != null || "".equals(captchaResponse))
		{
			correct = false;
			request.setAttribute("captcha", captchaVal);
		}	
		
		if (correct) {
			try {
				ad = DBAdministration.getAd(adId);
				request.setAttribute("description", ad.getDescription());
				String description =  ad.getDescription();
				Mailer mail = new Mailer();
				
				removeAttachments(description);
				mail.sendDeleteNotification(description, ad.getEmailToInitiator(), ad.getTitle(), ad.getSurname());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		doGet(request, response);
	}

}
