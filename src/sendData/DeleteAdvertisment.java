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
import controller.DBAdministration;

public class DeleteAdvertisment
{
	public static HttpServletRequest sendData (HttpServletRequest request, String adId, String userId) throws Exception
	{
		if (adId == null)
		{
			throw new Exception ("sendData.DeleteAdvertisment.java: no adId");
		}
		
		if (userId == null)
		{
			throw new Exception ("sendData.DeleteAdvertisment.java: no userId");
		}
		
		Ad ad = DBAdministration.getAd(adId);
		
		request.setAttribute("userId", userId);
		request.setAttribute("adId", adId);
		
		request.setAttribute("title", ad.getTitle());
		request.setAttribute("forename", ad.getForename());
		request.setAttribute("surname", ad.getSurname());
		request.setAttribute("email", ad.getEmailToInitiator());
		request.setAttribute("projectStart", ad.getProjectStart().toString());
		request.setAttribute("deactivationDate", ad.getExpiryDate().toString ());
		request.setAttribute("description", ad.getDescription());
		request.setAttribute("followerCount", " " + ad.getFollowers().size() + " ");
		
		return request;
	}
}
