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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Ad;
import nl.captcha.Captcha;

public class DeactivateAdvertisment extends HttpServlet 
{
	private static final long serialVersionUID = -1259768395029630382L;

	public DeactivateAdvertisment ()
	{
		super ();
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{		
		request.setCharacterEncoding("UTF-8");
		RequestDispatcher dispatcher = null;
		boolean correct = true;
		
		// Get userId and adId to get all the relevant information
		String userId = request.getParameter("userId");
		String adId = request.getParameter("adId");
		
		Ad ad = null;
		try
		{
			ad = DBAdministration.getActivAd(adId);
		}
		catch (SQLException e) 
		{
			Logging.getLogger().severe("Failed to get ad at DeactivateAdvertisment.java. " + e.getMessage());
			dispatcher = request.getRequestDispatcher("./information.jsp");
		}
		
		if (ad == null)
			correct = false;
		else
		{
			// bisher nur lokal, wird noch nicht in der DB gespeichert
			ad.deactivateAd();
		}
		
		if (correct)
		{
			try 
			{
				//  try to update if successful goto PersonalSection
				DBAdministration.updateAd(ad);
				userId = ad.getInitiatorId();
				
				if (!DBAdministration.existsUserByUserId(userId)) 
				{
					request.setAttribute("source", "user-dont-exist");
				}
				else 
				{
					request.setAttribute("source", "positiveAdvertDeactivation");
					request.setAttribute("userId", userId);
				}
				dispatcher = request.getRequestDispatcher("./information.jsp");	
			} 
			catch (SQLException e) 
			{
				// updating ad didn't work
				Logging.getLogger().severe("Failed to get ad at DeactivateAdvertisment.java. " + e.getMessage());
				dispatcher = request.getRequestDispatcher("./information.jsp");
			}	
		}
		else // Dies sollte nie eintreten
		{
			try 
			{
				request = sendData.DeactivateAdvertisment.sendData(request, adId, userId);
				dispatcher = request.getRequestDispatcher("DeactivateAdvertisment.jsp");				
			} 
			catch (Exception e) 
			{
				Logging.getLogger().severe("Failed to get ad at DeactivateAdvertisment.java. " + e.getMessage());
				dispatcher = request.getRequestDispatcher("information.jsp");
			}
		}
		
		dispatcher.forward(request,response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}
}
