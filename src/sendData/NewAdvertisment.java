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

package sendData;

import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import util.ToolTipps;

import model.User;

import controller.DBAdministration;
import controller.Settings;

public class NewAdvertisment 
{
	public static HttpServletRequest sendData (HttpServletRequest request, String userId, String title,
			String academicTitle, String forename, String surname, String email, String projectType,
			String [] targetGroups, String areFollowersVisible, String institute, String description,
			String adCreated, String projectStartDay, String projectStartMonth, String projectStartYear)
	{		
		// Set userData if it exists
		if (userId != null)
		{
			try 
			{
				if (DBAdministration.existsUserByUserId(userId))
				{
					User user = DBAdministration.getUserByUserId(userId);
					academicTitle = user.getAcademicTitle();
					forename = user.getForname();
					surname = user.getSurname();
					email = user.getEmail();
				}
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (userId != null)
		{
			request.setAttribute("userId", userId);
		}
		
		request.setAttribute ("title", title);
		request.setAttribute ("academicTitle", academicTitle);
		request.setAttribute ("forename", forename);
		request.setAttribute("surname", surname);
		request.setAttribute ("email", email);
		request.setAttribute("projectType", projectType);
		request.setAttribute("targetGroups", targetGroups);
		request.setAttribute("areFollowersVisible", areFollowersVisible);
		request.setAttribute ("institute", institute);
		request.setAttribute ("description", description);
		request.setAttribute("adCreated", adCreated);
		request.setAttribute("projectStartDay",projectStartDay);
		request.setAttribute("projectStartMonth", projectStartMonth);
		request.setAttribute("projectStartYear", projectStartYear);
		
		request.setAttribute("toolTippEmail", ToolTipps.getEmailToolTipp());
		request.setAttribute("toolTippVisibility", ToolTipps.getVisibilityToolTipp());
		request.setAttribute("toolTippDescription", ToolTipps.getDescriptionToolTipp());
		
		return request;
	}
	
	public static HttpServletRequest sendData (HttpServletRequest request, String userId)
	{
		String academicTitle="";
		String forename ="";
		String surname ="";
		String email ="";
		if (userId != null)
		{
			try 
			{
				User user = DBAdministration.getUserByUserId(userId);
				academicTitle = user.getAcademicTitle();
				forename = user.getForname();
				surname = user.getSurname();
				email = user.getEmail();
			} 
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String projectType = Settings.getProperty("types")[0];
		String [] targetGroups = new String [1];
		targetGroups [0] = Settings.getProperty("targeted")[0];
		String areFollowersVisible ="Initiator und Follower";
		String institute = Settings.getProperty("institutes")[0];
		String adCreated = Long.toString(new Date ().getTime());
        String projectStartDay = "";
        String projectStartMonth ="";
        String projectStartYear ="";

		
		return NewAdvertisment.sendData(request, userId, "", academicTitle, forename, surname, email, projectType, targetGroups, areFollowersVisible, institute, "", 
				adCreated, projectStartDay, projectStartMonth, projectStartYear);
	}
}
