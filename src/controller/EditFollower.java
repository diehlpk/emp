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

/**
 * Servlet implementation class EditFollower
 */
public class EditFollower extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditFollower() 
    {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		RequestDispatcher dispatcher;
		
		String adId = request.getParameter("adId");
		String userId = request.getParameter("userId");
		
/*Delete Follower*/
		if (request.getParameter("deleteFollower") != null) 
		{
			try 
			{
				DBAdministration.deleteFollower(userId, adId);
				request.setAttribute("userId", userId);
				request.setAttribute("adId", adId);
				request.setAttribute("source", "follower-deleted");
				
				dispatcher=request.getRequestDispatcher("./information.jsp");
			}
			catch (Exception e) 
			{
				Logging.getLogger().severe(e.getMessage());
				e.printStackTrace();
				request.setAttribute("userId", userId);
				request.setAttribute("adId", adId);
				request.setAttribute("source", "follower-notDeleted");
				dispatcher = request.getRequestDispatcher("./information.jsp");
			}
		}
		else
		{
			//  Fehlermeldung die ausgegeben werden soll, muss hier noch definiert werden
			request.setAttribute("userId", userId);
			request.setAttribute("adId", adId);
			dispatcher = request.getRequestDispatcher("./information.jsp");
		}
		
		dispatcher.forward(request,response);	
		/*
		//input validation
	    if (forename != null) 
	    {
	    	if ("".equals(forename)) 
	    	{
		    	request.setAttribute("forename", "Dieses Feld darf nicht leer sein!");
		    	correct = false;	
	    	}	    	
	    	if (!InputSanitizer.validate(forename)) 
	    	{
	    		forename = "";
		    	request.setAttribute("forename", "Ihre Eingabe enthielt Code oder Codefragmente" +
		    			" und wurde aus Sicherheitsgr�nden gelöscht!");
		    	correct = false;	
	    	}
		}
	    else 
	    {
	    	correct = false;
	    }	    
	    
	    
	    
	    if (surname != null) 
	    {
	    	if ("".equals(surname))
	    	{
		    	request.setAttribute("surname", "Dieses Feld darf nicht leer sein!");
		    	correct = false;	
	    	}	    	
	    	if (!InputSanitizer.validate(surname)) 
	    	{
	    		surname = "";
		    	request.setAttribute("surname", "Ihre Eingabe enthielt Code oder Codefragmente" +
		    			" und wurde aus Sicherheitsgr�nden gel�scht!");
		    	correct = false;	
	    	}
		} 
	    else 
	    {
	    	correct = false;
	    }
	    	    
	    
	    if (comment != null) 
	    {
	    	if (!InputSanitizer.validate(comment)) 
	    	{
	    		comment = "";
		    	request.setAttribute("description", "Ihre Eingabe enthielt Code oder Codefragmente" +
				" und wurde aus Sicherheitsgr�nden gel�scht!");
		    	correct = false;
	    	}	    	
		}
	    else 
	    {
	    	correct = false;
	    } 
	    
	    follower.setComment(comment);
	    follower.setForename(forename);
	    follower.setSurname(surname);
	    request.setAttribute("follower", follower);
	    //input correct? follower entry getting updated
		if (correct && captcha.isCorrect(captchaResponse)) 
		{
			try 
			{
				DBAdministration.updateFollower(follower);
				request.setAttribute("source", "follower-updated");
				RequestDispatcher dispatcher=request.getRequestDispatcher("./information.jsp");
		    	dispatcher.forward(request,response);
			} 
			catch (Exception e) 
			{
				Logging.getLogger().severe(e.getMessage());
				e.printStackTrace();
				request.setAttribute("source", "follower-NotUpdated");
				RequestDispatcher dispatcher=request.getRequestDispatcher("./information.jsp");
		    	dispatcher.forward(request,response);	
			}	
		} 
		else 
		{
			try 
			{
				request.setAttribute("ad", DBAdministration.getAd(request.getParameter("id")));
			}
			catch (Exception e) 
			{
				Logging.getLogger().severe(e.getMessage());
				e.printStackTrace();
				request.setAttribute("source", "db");
				RequestDispatcher dispatcher=request.getRequestDispatcher("./information.jsp");
		    	dispatcher.forward(request,response);	
			}
			if (!captcha.isCorrect(captchaResponse)) 
			{
				request.setAttribute("captcha", "Fehlerhafte Eingabe!");
			}
			
			RequestDispatcher dispatcher=request.getRequestDispatcher("./EditFollower.jsp");
	    	dispatcher.forward(request,response);	
		}*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
