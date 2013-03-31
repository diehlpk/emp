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

import util.ToolTipps;

import model.User;

public class SendMessage extends HttpServlet 
{
	public SendMessage ()
	{
		super ();
	}
	
	/**
	 * navigiert (falls korrekte Daten) zu SendCustomerMessage.jsp
	 */
	protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException 
    {
		request.setCharacterEncoding("UTF-8");
        RequestDispatcher dispatcher;
        
		String userId = request.getParameter("userId");
		String adId = request.getParameter("adId");
		String messageToInitiator = request.getParameter("messageToInitiator");
		String messageToFollower = request.getParameter("messageToFollower");
		String messageToAdmin = request.getParameter("messageToAdmin");
		String followerEmail = request.getParameter("followerEmail");
		boolean correct = true;
		
		correct = checkUserId(userId) && correct;
		if (!correct)
			userId = null;
		
		correct = checkAdId(adId) && correct;
		
		/*
		if (messageToFollower != null)
		{
			if (followerEmail == null)
				correct = false;
		}
		*/
		
		if (correct)
		{
			if (userId != null)
            {
            	request.setAttribute("userId", userId);
            	try 
            	{
					User user = DBAdministration.getUserByUserId(userId);
					request.setAttribute("email", user.getEmail());
					request.setAttribute("emailReply", user.getEmail());
					request.setAttribute("readonly", "readonly");
            	} 
            	catch (SQLException e)
            	{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
			request.setAttribute("adId", adId);
			
			if (userId != null && !"".equals(userId))
			{
				request.setAttribute("userId", userId);
			}
			
			if (messageToInitiator != null)
			{
				request.setAttribute("toolTippEmail", ToolTipps.getEmailToolTipp());
				request.setAttribute("messageToInitiator","messageToInitiator");
				dispatcher = request.getRequestDispatcher("./SendCustomerMessage.jsp");
			}
			else if (messageToFollower != null)
			{
				//System.out.println ("SendMessage.java: " + followerEmail);
				request.setAttribute("toolTippEmail", ToolTipps.getEmailToolTipp());
				request.setAttribute("followerEmail", followerEmail);
				request.setAttribute("messageToFollower", messageToFollower);
				dispatcher = request.getRequestDispatcher("./SendCustomerMessage.jsp");
			}
			else
			{
				dispatcher = request.getRequestDispatcher("./information.jsp");
			}
		}
		else if (messageToAdmin != null)
		{
			request.setAttribute("toolTippEmail", ToolTipps.getEmailToolTipp());
			request.setAttribute("messageToAdmin","messageToAdmin");
			dispatcher = request.getRequestDispatcher("./SendCustomerMessage.jsp");
		}else 
		{
				request.setAttribute("userId", userId);
			request.setAttribute("adId", adId);
			
			request.setAttribute("source", "Fehler: Es kann aufgrund von fehlerhaften Daten " +
						"keine Nachricht versendet werden. Bitte wenden sie sich an ihren " +
						"Systemadministrator.");
			dispatcher = request.getRequestDispatcher("./information.jsp");			
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
    
    private boolean checkUserId (String userId)
    {
    	boolean correct = true;
    	
    	if (userId != null) 
        {
    		if (!"".equals(userId) && !InputSanitizer.validate(userId))
         		correct = false;
         	else
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
