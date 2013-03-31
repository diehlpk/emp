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

import model.User;

/**
 * Servlet implementation class EditUser
 */
public class EditUser extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUser() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        RequestDispatcher dispatcher;

        String forename = request.getParameter("forename");
        String surname = request.getParameter("surname");
        String academicTitle = request.getParameter("academicTitle");
        String sid = request.getParameter("sid");
        String userId = request.getParameter("userId");
        String changeData = request.getParameter("changeData");
        boolean correct = true;
        
        if (sid == null || "".equals(sid) || !InputSanitizer.validate(sid)) {
            request.setAttribute("source", null);
            dispatcher = request.getRequestDispatcher("./information.jsp");
            dispatcher.forward(request,response);
            return;
        } else {
            request.setAttribute("sid", sid);
        }

        if (userId == null || forename == null || surname == null || academicTitle == null) {
            correct = false;
        }

        if ("".equals(forename)) {
            request.setAttribute("forename", "Dieses Feld darf nicht leer sein!");
            correct = false;
        } else if (!InputSanitizer.validate(forename)) {
            request.setAttribute("forename", "Ihre Eingabe enthielt Code" +
                    "oder Codefragmente!");
            correct = false;
            forename = "";
        }

        if ("".equals(surname)) {
            request.setAttribute("surname", "Dieses Feld darf nicht leer sein!");
            correct = false;
        } else if (!InputSanitizer.validate(surname)) {
            request.setAttribute("surname", "Ihre Eingabe enthielt Code" +
                    "oder Codefragmente!");
            correct = false;
            surname = "";
        }

        if (!InputSanitizer.validate(academicTitle)) {
            academicTitle = "";
            request.setAttribute("academicTitle", "  Ihre Eingabe enthielt Code oder Codefragmente" +
            " und wurde aus Sicherheitsgr�nden gel&ouml;scht!");
            correct = false;
        }

        if (userId != null && !InputSanitizer.validate(userId)) {
            userId = "";
            correct = false;
        }

        if (correct && "speichern".equals(changeData)) {
            try {
                DBAdministration.updateUser(new User(academicTitle, forename, surname, null, userId));
                request.setAttribute("source", "user-data-changed");
            } catch (Exception e) {
            	Logging.getLogger().severe(e.getMessage());
                e.printStackTrace();
                request.setAttribute("source", "user-data-fail");
            }
            dispatcher = request.getRequestDispatcher("./information.jsp");
        } else {
            try {
                request.setAttribute("user", DBAdministration.getUserByPassword(sid));
                dispatcher = request.getRequestDispatcher("./EditUser.jsp");
            } catch (Exception e) {
            	Logging.getLogger().severe(e.getMessage());
                e.printStackTrace();
                request.setAttribute("source", "db");
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
