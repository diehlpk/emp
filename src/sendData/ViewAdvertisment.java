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

import util.ToolTipps;

import model.Ad;
import model.Follower;
import model.User;

import controller.DBAdministration;

public class ViewAdvertisment 
{
	/**
	 * 
	 * @param request
	 * @param adId
	 * @param userId
	 * @param forename
	 * @param surname
	 * @param email
	 * @param comment
	 * @return
	 * @throws Exception
	 */
	public static HttpServletRequest sendData (HttpServletRequest request, String adId, String userId, 
				String forename, String surname, String email, String comment) throws Exception
	{
		if (adId == null)
		{
			throw new Exception ("no adId");
		}
		else
		{
			Ad ad = DBAdministration.getAd(adId);
			if (ad == null)
			{
				throw new Exception ("invalid adId, there is no ad with this adId");
			}
			else // Ad exists
			{
				boolean isInitiator = false;
				boolean isFollower = false;
				
				if (!ad.isActiv()) // Anzeige wurde noch nicht aktiviert
				{
					//  Der Initiator kann auch nicht aktivierte Anzeigen ansehen (nur eigene)
					if (ad.getInitiatorId().equals(userId) && userId != null)
						isInitiator = true;
					else
						ad = null;
				}
				else // ad is activ
				{
					if (ad.getInitiatorId().equals(userId) && userId != null)
					{
						isInitiator = true;
					}
					else if (userId != null)
					{
						for (String followerId : DBAdministration.getFollowersOfAd(adId))
						{
							if (userId.equals(followerId))
								isFollower = true;
						}
					}
				}
				
				if (ad == null)
				{
					throw new Exception ("Die Anzeige ist nicht aktiviert " +
							"und kann nur vom Initiator eingesehen werden.");
				}
							
				String [] followerIds = DBAdministration.getFollowersOfAd(adId);
				Follower [] fol = new Follower [followerIds.length];
				for (int i = 0; i  < followerIds.length; i++)
				{
					fol [i] = DBAdministration.getFollower(followerIds [i]);
				}
				//Follower [] fol = ad.getFollowers().toArray(new Follower [0]);
				
				String initiatorName = ad.getAcademicTitle() + " " + ad.getForename() + " " + ad.getSurname();
				String openForAllMessage;
				if (fol.length == 0)
				{
					openForAllMessage = "Bisher sind keine Interessenten eingetragen: <br/>";
				}
				else if (fol.length == 1)
				{
					openForAllMessage ="Bisher ist ein(e) Interessent(in) eingetragen: <br/>";
				}
				else 
				{
					openForAllMessage = "Bisher sind " + fol.length + " Interessenten eingetragen: <br/>";
				}
				
				if (ad.getFollowersVisible().equals("Oeffentlich"))
				{
					for (Follower f : fol)
						openForAllMessage += f.getForname() + " " + f.getSurname() + " <br/>";
				}
				else if (ad.getFollowersVisible().equals("Initiator und Follower") && (isFollower || isInitiator))
				{
					for (Follower f : fol)
						openForAllMessage += f.getForname() + " " + f.getSurname() + " <br/>";
				}
				else if (isInitiator)
				{
					for (Follower f : fol)
						openForAllMessage += f.getForname() + " " + f.getSurname() + " <br/>";
				}
				
				
				request.setAttribute("adId", adId);
				
				request.setAttribute("visiblityOfFollowers", ad.getFollowersVisible());
				request.setAttribute("comment", "");
				request.setAttribute ("title", ad.getTitle());
				request.setAttribute("nameInitiator", initiatorName);
				request.setAttribute("emailInitiator", ad.getEmailToInitiator());
				request.setAttribute("institute", ad.getInstitute());
				request.setAttribute("projectType", ad.getProjectType());
				request.setAttribute("targeted", ad.getTargetedToString());
				request.setAttribute("projectStart", ad.getProjectStart().toString());
				request.setAttribute("description", ad.getDescription());
				request.setAttribute ("openForAll", ad.getFollowersVisible());
				request.setAttribute ("openForAllMessage", openForAllMessage);
				
				request.setAttribute("toolTippEmailContact", ToolTipps.getEmailContactToolTipp());
				request.setAttribute("toolTippEmail", ToolTipps.getEmailToolTipp());
				request.setAttribute("toolTippComment", ToolTipps.getCommentToolTipp());
				
				if (isInitiator)
					request.setAttribute("isInitiator", "true");
				else 
					request.setAttribute("isInitiator", "false");
				
				if (isFollower)
					request.setAttribute("isFollower", "true");
				else
					request.setAttribute("isFollower", "false");
				
				if (userId != null && !userId.equals(""))
				{
					User user = DBAdministration.getUserByUserId(userId);
					request.setAttribute("forename", user.getForname());
					request.setAttribute("surname", user.getSurname());
					request.setAttribute("email", user.getEmail());
					request.setAttribute("readonly", "readonly=\"readonly\"");
					
					request.setAttribute("userId", userId);
				}
				else
				{
					if (forename == null)
						forename ="";
					if (surname == null)
						surname = "";
					if (email == null)
						email ="";
					
					request.setAttribute("forename", forename);
					request.setAttribute("surname", surname);
					request.setAttribute("email", email);
				}	
				if (comment == null)
					comment = "";
				request.setAttribute("comment", comment);
			}
		}
		
		return request;
	}
	
	/**
	 * Gets all information of an Ad
	 * Sends attributes: 
	 * visiblityOfFollowers (String), comment (String), title (String), nameInitiator (String), emailInitiator (String),
	 * institute (String), projectType (String), targeted (String), projectStart (String), description (String),
	 * openForAll (String), openForAllMessage (String), toolTippEmail (String), toolTippComment (String), 
	 * isInitiator (String), isFollower (String),
	 * (only if userId != null: forename (String), surname (String), email (String), readonly (String))
	 * @param request
	 * @param adId
	 * @param userId if unkown set to null
	 * @return
	 * @throws Exception
	 */
	public static HttpServletRequest sendData (HttpServletRequest request, String adId, String userId) throws Exception
	{
		return ViewAdvertisment.sendData(request, adId, userId, null, null, null, null);
	}
}
