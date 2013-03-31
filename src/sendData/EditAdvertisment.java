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

import javax.servlet.http.HttpServletRequest;

import model.Ad;

import util.ToolTipps;

import controller.DBAdministration;
import controller.Settings;

public class EditAdvertisment 
{
	public static HttpServletRequest sendData (HttpServletRequest request, String adId, String userId) throws Exception
	{
		//  Data to send
		String title = "";
		String academicTitle ="";
		String forename ="";
		String surname ="";
		String email = "";
		String projectType = Settings.getProperty("types") [0];
		String [] targetGroups = new String [1];
		targetGroups [0] = Settings.getProperty("targeted") [0];
		String areFollowersVisible ="Initiator und Follower";
		String institute= "";
		String description= "";
		String adCreated ="";
		String projectStartDay = "";
		String projectStartMonth ="";
		String projectStartYear ="";
		
		//  get Ad by adId
		Ad ad = null;
		if (adId == null)
		{
			throw new Exception ("sendData.EditAdvertisment.java: no adId");
		}
		else if (userId == null)
		{
			throw new Exception ("sendData.EditAdvertisment.java: no userId");
		}
		else
		{
			ad = DBAdministration.getOwnAd(adId);
			
			title = ad.getTitle();
			academicTitle = ad.getAcademicTitle();
			forename = ad.getForename();
			surname = ad.getSurname();
			email = ad.getEmailToInitiator();
			projectType = ad.getProjectType();
			targetGroups = ad.getTargeted();
			
			areFollowersVisible = ad.getFollowersVisible();
			institute = ad.getInstitute();
			description = ad.getDescription();
			adCreated = ad.getAdCreatedDate().toString();
		
			String [] temp = ad.getProjectStart().toString().split("-");
			if (temp.length == 3)
			{
				projectStartYear = temp [0];
				projectStartMonth = temp [1];
				projectStartDay = temp [2];
			}
		}
		
		//  Send Data
		request.setAttribute("adId", adId);
		request.setAttribute("userId", userId);
		
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
}
