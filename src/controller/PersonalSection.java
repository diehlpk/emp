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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Ad;
import sendData.ViewPersonalSection;
import util.ToolTipps;

/**
 * Servlet implementation class PersonalSection
 */
public class PersonalSection extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PersonalSection() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		RequestDispatcher dispatcher;
		
		String adId = request.getParameter("adId");
		String userId = request.getParameter("userId");
		
		String activateAd = request.getParameter ("activateAd");
		String deactivateAd = request.getParameter("deactivateAd");
		String showFollowers = request.getParameter("showFollowers");
		String deleteAd = request.getParameter("deleteAd");

		
/* Anzeige aktivieren und reaktivieren */
		if (activateAd != null && adId != null && !"".equals(adId) && 
				InputSanitizer.validate(adId)) 
		{
			try 
			{
				 if (activateAd.equals("activate") || activateAd.equals("reactivate")) {
	                    Ad ad = DBAdministration.getAd(adId);
	                    ad.setDeactivationDate();
	                }
				request = sendData.ActivateAdvertisment.sendData(request, adId, userId);
				dispatcher = request.getRequestDispatcher("./information.jsp");
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				Logging.getLogger().severe(e.getMessage());
				request.setAttribute("source", e.getMessage());
				request.setAttribute("userId", userId);
				dispatcher = request.getRequestDispatcher("./information.jsp");	
			}
		} 
/* Alle Interessenten einer Anzeige anzeigen */
		else if (showFollowers != null && adId != null) 
		{
			try 
			{
				request.setAttribute("toolTippEmailContact", ToolTipps.getEmailContactToolTipp());
				request = sendData.ShowFollowers.sendData(request, adId, userId);		
				dispatcher = request.getRequestDispatcher("./ShowFollowers.jsp");
			}
			catch (Exception e) 
			{
				System.out.println (e.getMessage());
				request.setAttribute("userId", userId);
				request.setAttribute("adId", adId);
				request.setAttribute("source", e.getMessage());
				dispatcher = request.getRequestDispatcher("./information.jsp");
			}
		} 
/* Anzeige Deaktivieren*/
		else if (deactivateAd != null && adId != null && adId != "")
		{
			try 
			{
				request = sendData.DeactivateAdvertisment.sendData(request, adId, userId);				
				dispatcher = request.getRequestDispatcher("./DeactivateAdvertisment.jsp");
			} 
			catch (Exception e) 
			{
				request.setAttribute("userId", userId);
				request.setAttribute("adId", adId);
				dispatcher = request.getRequestDispatcher(".information.jsp");
			}
				
		}
/* Anzeige loeschen*/
		else if (deleteAd != null && adId != null && userId != null)
		{
			try 
			{
				request = sendData.DeleteAdvertisment.sendData(request, adId, userId);
				dispatcher = request.getRequestDispatcher("./DeleteAdvertisment.jsp");

			}
			catch (Exception e) 
			{
				dispatcher = request.getRequestDispatcher("./information.jsp");	
				e.printStackTrace();
			}
			
		}
/* Zum Persoenlichen Bereich */
		else
		{
			try
			{
				if (!DBAdministration.existsUserByUserId(userId)) 
				{
					request.setAttribute("source", "user-dont-exist");
					request.setAttribute("userId", null);
					dispatcher = request.getRequestDispatcher("./information.jsp");	
				}
				else 
				{	
					/* Follower aktiv schalten */
					if (adId != null && userId != null) {
						DBAdministration.setFollowerActive(userId, adId);
					}
					request = ViewPersonalSection.sendData(request, userId);
					
					dispatcher = request.getRequestDispatcher("./PersonalSection.jsp");
				}
			} 
			catch (Exception e)
			{
				e.printStackTrace();
				Logging.getLogger().severe(e.getMessage());
				request.setAttribute("source", e.getMessage());
				dispatcher = request.getRequestDispatcher("./information.jsp");	
			}
		}
    	dispatcher.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
