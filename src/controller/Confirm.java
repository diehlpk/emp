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
 * Servlet implementation class Confirm
 */
public class Confirm extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Confirm() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		RequestDispatcher dispatcher;
		
		String userId = request.getParameter("userId");
		String adId = request.getParameter("adId");
		/*
		if (userId != null && !"".equals(userId) && InputSanitizer.validate(userId)) {
				request.setAttribute("userId", userId);
		}
		
		// if the user confirmed the deletion of the ad, the ad is set to
		// archived and all followers will receive a message
		if (id != null && !"".equals(id) && InputSanitizer.validate(id) &&
				request.getParameter("confirm") != null) {
			Ad ad = null;
			if (request.getParameter("confirm").contains("Ja")) {
				try {
					ad = DBAdministration.getAd(id);
					DBAdministration.archivedAd(id);
					try {
						Mailer mail = new Mailer();
			    		String title = ad.getTitle();
						for (model.Follower follower : ad.getFollowers()){
				    		mail.sendAdDeleted(title, follower.geteMail(), follower.getSurname(), ad.getAcademicTitle() + "" + ad.getSurname());
						}
						request.setAttribute("source", "advertisement-deleted");
					} catch (Exception e) {
						e.printStackTrace();
						request.setAttribute("source", "advertisement-deleted-followerFail");
					}	
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("source", "advertisement-notDeleted");
				}
			} else {
				request.setAttribute("source", "advertisement-deletion-aborted");
				request.setAttribute("id", id);
			}
		} else {
			request.setAttribute("source", null);
		}
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
