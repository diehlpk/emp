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
import model.Follower;
import controller.DBAdministration;

public class ChangeInterest 
{
	/**
	 * sends Attributes:
	 * titleAd (String), nameInitiatorAd (String), instituteAd (String), projectTypeAd (String), userForename (String)
	 * userSurname (String), adId (String), userId (String)
	 * @param request
	 * @param adId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static HttpServletRequest sendData (HttpServletRequest request, String adId, String userId) throws Exception
	{
		if (adId == null)
		{
			throw new Exception ("sendData.ChangeInterest.java: no adId");
		}
		else if (userId == null)
		{
			throw new Exception ("sendData.ChangeInterest.java: no userId");
		}
		else
		{
			Follower follower = DBAdministration.getFollower(userId);
			
			if (follower == null) //  no such follower with given UserId
			{
				throw new Exception ("Fehler: Ihre UserID ist nicht korrekt, oder die Anzeige wurde gel&ouml;scht.");
			} 
			else
			{
				Ad ad = DBAdministration.getAd(adId);
				
				if (!ad.isActiv())
					throw new Exception ("Das Interesse an der Anzeige kann momentan nicht ge&auml;ndert werden, da die Anzeige nicht aktiv ist");
				
				
				request.setAttribute("titleAd", ad.getTitle());
				request.setAttribute("nameInitiatorAd", ad.getAcademicTitle() + " " + ad.getForename() + " " + ad.getSurname());
				request.setAttribute("instituteAd", ad.getInstitute());
				request.setAttribute("projectTypeAd", ad.getProjectType());
				
				request.setAttribute ("userForename", follower.getForname());
				request.setAttribute("userSurname", follower.getSurname());
				
				request.setAttribute("adId", adId);
				request.setAttribute("userId", userId);
			}
		}
		
		return request;
	}
}
