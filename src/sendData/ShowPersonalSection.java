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

/**
 * DUBLIKAT:
 * SOLLTE NICHT GENUTZT WERDEN. STATTDESSEN ViewPersonalSection.java
 * Sobald sichergestellt ist dass ShowPersonalSection.java nicht genutzt wird, wird diese
 * Datei gelöscht
 */


import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import model.Ad;

import controller.DBAdministration;
import controller.Mailer;

public class ShowPersonalSection 
{
	public static HttpServletRequest sendData (HttpServletRequest request, String userId) throws Exception
	{
		if (userId == null)
		{
			throw new Exception ("sendData.ShowPersonalSection.java: ");
		}
		else
		{
			if (!DBAdministration.existsUserByUserId(userId))
			{
				throw new Exception ("sendData.ShowPersonalSection.java: No user found with this userId");
			}
			else
			{
				request = ShowPersonalSection.sendDataOwnAds(request, userId);
				request = ShowPersonalSection.sendDateFollowedAds(request, userId);
				
				request.setAttribute("userId", userId);
			}	
		}
		
		return request;
	}
	
	private static HttpServletRequest sendDataOwnAds (HttpServletRequest request, String userId) throws SQLException
	{
		//  Get all data relating to own Adverts
		Ad [] ownAdverts = DBAdministration.getOwnAds(userId).toArray(new Ad[0]);
		
		String [] title = new String [ownAdverts.length];
		String [] projectType = new String [ownAdverts.length];
		String [] targeted = new String [ownAdverts.length];
		String [] creationDate = new String [ownAdverts.length];
		String [] projectStart = new String [ownAdverts.length];
		String [] adIdArray = new String [ownAdverts.length];
		boolean [] isActiv = new boolean [ownAdverts.length];
					
		for (int i = 0; i < ownAdverts.length; i++)
		{
			title [i] = ownAdverts [i].getTitle();
			projectType [i] = ownAdverts [i].getProjectType();
			targeted [i] = ownAdverts [i].getTargetedToString();
			creationDate [i] = ownAdverts [i].getAdCreatedDate().toString();
			projectStart[i] = ownAdverts [i].getProjectStart().toString();
			adIdArray [i] = ownAdverts[i].getAdId();
			isActiv [i] = ownAdverts [i].isVisible();
		}
					
		request.setAttribute ("titleArray", title);
		request.setAttribute("projectTypeArray", projectType);
		request.setAttribute("targetedArray", targeted);
		request.setAttribute("creationDateArray", creationDate);
		request.setAttribute("projectStartArray", projectStart);
		request.setAttribute("adIdArray", adIdArray);
		request.setAttribute("isActivArray", isActiv);
		
		return request;
	}
	
	private static HttpServletRequest sendDateFollowedAds (HttpServletRequest request, String userId) throws SQLException
	{
		//  Get all data relating to followed Ads
		Ad [] followAds = DBAdministration.getAllFollowers(userId).toArray(new Ad [0]);
		int length = followAds.length;
		String [] titleFollow = new String [length];
		String [] projectTypeFollow = new String [length];
		String [] targetedFollow = new String [length];
		String [] instituteFollow = new String [length];
		String [] creationDateFollow = new String [length];
		String [] adIdFollow = new String [length];

		for (int i = 0; i < length; i++)
		{
			titleFollow [i] = followAds[i].getTitle();
			projectTypeFollow[i] = followAds[i].getProjectType();
			targetedFollow[i] = followAds[i].getTargetedToString();
			instituteFollow [i] = followAds[i].getInstitute();
			creationDateFollow [i] = followAds[i].getAdCreatedDate().toString();
			adIdFollow [i] = followAds[i].getAdId();
						
			//  aktiviere alle Follower Ads
			if (!DBAdministration.existsActivFollower(userId, adIdFollow[i]))
			{
				try
				{
					DBAdministration.activateFollower(userId, adIdFollow[i]);
					Mailer mail = new Mailer ();
					String url = request.getRequestURL().toString();
					int tempIndex = url.indexOf("PersonalSection");
					tempIndex += "PersonalSection".length();
					url = url.substring(0, tempIndex) + "?userId=" + followAds[i].getInitiatorId();
					
					mail.sendNewFollower(url, followAds[i].getEmailToInitiator(), followAds [i].getSurname(), followAds[i].getTitle());	 							
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
				}
			}
		}
		
		request.setAttribute("titleFollow", titleFollow);
		request.setAttribute("projectTypeFollow", projectTypeFollow);
		request.setAttribute("targetedFollow", targetedFollow);
		request.setAttribute("instituteFollow", instituteFollow);
		request.setAttribute("creationDateFollow", creationDateFollow);
		request.setAttribute("adIdFollow", adIdFollow);
					
		return request;
	}
}
