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
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import model.Ad;

/**
 * Servlet implementation class FindAdvertisement
 */
public class FilterAdvertisement extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FilterAdvertisement() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		request.setCharacterEncoding("UTF-8");
		RequestDispatcher dispatcher;
		
		String[] types = request.getParameterValues("projectTypes");
		String[] institutes = request.getParameterValues("institute");

			
		//  Get all Ads
		int length = institutes.length + types.length;
		String[] constraints  = new String [length];

		for (int i = 0; i <  institutes.length; i++)
			constraints [i] = institutes [i];
		for (int i = institutes.length; i < length; i++)
			constraints [i] = types [i - institutes.length];
		
		LinkedList<Ad> ads = new LinkedList <Ad> ();;
		try 
		{
			ads = DBAdministration.getAds(constraints);
		} 
		catch (SQLException e) {
			System.out.println ("FilterAdvertisment.java: Error getting ads with given constraints!");
			e.printStackTrace();
		}
		
		//  Ads found:
		request.setAttribute("types", types);
		request.setAttribute("institutes", institutes);
		
		
		request.setAttribute("ad", ads);
		dispatcher = request.getRequestDispatcher("main.jsp");
		dispatcher.forward(request,response); //FIXME
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
