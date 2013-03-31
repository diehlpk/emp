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
 * Handles all stuff related to the initial setup of IMP and its database backed
 * @author kuleszdl
 *
 */
public class Setup extends HttpServlet{
	private static final long serialVersionUID = 1495082085725038598L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		String action = null;
		RequestDispatcher dispatcher;
		if (request.getParameter("sid") != null) {
			if (InputSanitizer.validate(request.getParameter("sid"))) {
				request.setAttribute("sid", request.getParameter("sid"));
			}
		}
		if (request.getParameter("action") != null ) {
			action = request.getParameter("action");
		}
		if (action != null && action.equals("initializetables")) {
			try {
				DBAdministration.initDatabase();
				request.setAttribute("source", "setup-dbinitialization-successful");
			} catch (Exception e) {
				Logging.getLogger().severe("Failed to initializate database. " + e.getMessage());
				e.printStackTrace();
				request.setAttribute("source", "setup-dbinitialization-failed");
			}
		} else if (action != null && action.equals("insertsampledata")) {
			try {
				DBAdministration.insertSampleData();
				request.setAttribute("source", "setup-insertsampledata-successful");
			} catch (Exception e) {
				Logging.getLogger().severe("Failed to insert sample data. " + e.getMessage());
				e.printStackTrace();
				request.setAttribute("source", "setup-insertsampledata-failed");
			}
		}	/*
		 	 * else: we explicitly do not set a request parameter to trigger
			 * default error handling in information.jsp
			 */
		dispatcher = request.getRequestDispatcher("information.jsp");
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
