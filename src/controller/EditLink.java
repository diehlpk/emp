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

import util.ToolTipps;

import model.User;
import nl.captcha.Captcha;

/**
 * Servlet implementation class EditLink
 */
public class EditLink extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditLink() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		RequestDispatcher dispatcher;
		
		String userId = request.getParameter("userId");
		String email = request.getParameter("email");
		
		String changeLink = request.getParameter("changeLink");
		String newLink = request.getParameter("newLink");
		String requestLink = request.getParameter("requestLink");
		
		String captchaResponse = request.getParameter("captchaResponse");
		Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		
		boolean correct = true;
		
		if (requestLink == null)
		{
			String emailVal = InputSanitizer.checkEmail(email);
			if (emailVal != null)
			{
				correct = false;
				request.setAttribute("emailValidate", emailVal);
			}
			
			String captchaVal = InputSanitizer.checkCaptcha(captcha, captchaResponse);
			if (captchaVal != null)
			{
				correct = false;
				request.setAttribute("captcha", captchaVal);
			}
		}
		
/*New Link*/
		if ("newLink".equals(newLink) && correct) 
		{
    		try 
    		{
    			User user = DBAdministration.getUserByEmail(email);
    			if (user == null)
    				throw new Exception ("User does not exist");
    			
    			Mailer mail = new Mailer();
        		String url = request.getRequestURL().toString();
				url = url.replace("EditLink", "PersonalSection?userId=" + user.getUserId());
	    		mail.sendLink(url, user.getEmail(), user.getSurname());
				request.setAttribute("source", "link-sended");
			} 
    		catch (Exception e) 
    		{				
				Logging.getLogger().severe(e.getMessage());
				request.setAttribute("source", "user-dont-exist-byEmail");
			}
    		
			dispatcher = request.getRequestDispatcher("./information.jsp");
			
			
		} 
/*Change Link*/ //  kann nun nicht mehr benutzt werden
		else if ("changeLink".equals(changeLink) && correct && false)
		{
			/*Man bekommt nur den bisherigen Link, keinen Neuen*/
			try 
    		{
    			User user = DBAdministration.getUserByEmail(email);
    			Mailer mail = new Mailer();
        		String url = request.getRequestURL().toString();
				url = url.replace("EditLink", "PersonalSection?userId=" + user.getUserId());
	    		mail.sendLink(url, user.getEmail(), user.getSurname());
				request.setAttribute("source", "link-sended");
			} 
    		catch (Exception e) 
    		{				
				e.printStackTrace();
				Logging.getLogger().severe(e.getMessage());
				request.setAttribute("source", e.getMessage());
			}
			
			dispatcher = request.getRequestDispatcher("./information.jsp");
		} 
/*Request Link*/
		else if ("requestLink".equals(requestLink))
		{
			if (userId != null)
				request.setAttribute("userId", userId);
		
			request.setAttribute("toolTippEmail", ToolTipps.getEmailToolTipp());
			dispatcher = request.getRequestDispatcher("./EditLink.jsp");
		}
/*Fehlerhafte Eingabe*/
		else
		{
			request.setAttribute("email", email);
			request.setAttribute("toolTippEmail", ToolTipps.getEmailToolTipp());
			
			if (userId != null)
				request.setAttribute("userId", userId);
			
			dispatcher = request.getRequestDispatcher("./EditLink.jsp");
		}
    	dispatcher.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
