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

import sendData.ChangeInterest;
import sendData.EditAdvertisment;
import sendData.MainPage;
import sendData.NewAdvertisment;
import sendData.ViewAdvertisment;
/**
 * Servlet implementation class Main
 */
public class Main extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Main() 
    {
        super();
        
        /*
         *  Startet den Quartz-Scheduler damit die automatische Deaktivierung (falls diese
         *  über die Settings eingeschaltet ist) gestartet wird
         */
        
        util.Quartz.main(null);
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
		
		// Die wichtigsten Parameter, alles andere kann hieraus abeleitet werden
		String userId = request.getParameter("userId");
		String adId = request.getParameter("adId");
		
		//  Steuerungsvariablen, zur eindeutigen zuordnung
		// Hier von sollte immer nur maximal ein String != null sein
		String changeInterest = request.getParameter ("changeInterest");
		String editAd = request.getParameter("editAd");
		String AdDetail = request.getParameter("AdDetail");

		
/* changeInterest */	
		if (changeInterest != null) 
		{
			try 
			{
				ChangeInterest.sendData(request, adId, userId);
				dispatcher = request.getRequestDispatcher("EditFollower.jsp");
			} 
			catch (Exception e) 
			{
				Logging.getLogger().severe("Failed to select follower from database. " + e.getMessage());
				request.setAttribute("source", e.getMessage());
				dispatcher = request.getRequestDispatcher("information.jsp");
			}			
		}
/* AdDetail */		
		else if (AdDetail != null) 
		{
			try 
			{
				request = ViewAdvertisment.sendData(request, adId, userId);
				dispatcher = request.getRequestDispatcher("./AdDetails.jsp");
			}
			catch (Exception e) 
			{
				Logging.getLogger().severe(e.getMessage());
				System.out.println (e.getMessage());
				request.setAttribute("source", e.getMessage());
				dispatcher = request.getRequestDispatcher("information.jsp");
			}
		} 
/* editAd */
		else if	(editAd != null)
		{
			
			if (adId != null)
			{
				try 
				{
					request = EditAdvertisment.sendData(request, adId, userId);
					dispatcher = request.getRequestDispatcher("./EditAdvertisement.jsp");
				} 
				catch (Exception e)
				{
					e.printStackTrace();

					Logging.getLogger().severe("Failed to select \"private\" ad from database. " + e.getMessage());
					dispatcher = request.getRequestDispatcher("./information.jsp");
				}
			}
			else
			{
				request = NewAdvertisment.sendData(request, userId);
				dispatcher = request.getRequestDispatcher("./EditAdvertisement.jsp");
			}
		} 
/* ELSE */
		else
		{	
			//  This happens by default
			//	e.g. when the website is first started 
			try 
			{
				if (!DBAdministration.isSchemaCreated()) 
				{
					dispatcher = request.getRequestDispatcher("setup.jsp");
				} 
				else 
				{
					request = MainPage.sendData(request, userId);
					dispatcher = request.getRequestDispatcher("./main.jsp");
				}
			} 
			catch (Exception e) 
			{
				Logging.getLogger().severe("Failed to connect to databese or to get ads. " +
						e.getMessage());
				e.printStackTrace();
				request.setAttribute("source", "db");
				dispatcher = request.getRequestDispatcher("information.jsp");
			}
		}
		dispatcher.forward(request, response);
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		doGet(request, response);
	}
	

}