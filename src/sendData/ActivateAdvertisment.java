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

public class ActivateAdvertisment 
{
	public static HttpServletRequest sendData (HttpServletRequest request, String adId, String userId) throws Exception
	{
		if (adId == null)
		{
			throw new Exception ("sendData.ActivateAdvertisment.java: no adId");
		}
		if (userId == null)
		{
			throw new Exception ("sendData.ActivateAdvertisment.java: no userId");
		}
		
		Ad ad = DBAdministration.getAd(adId);
		ad.setDeactivationDate();
		ad.activateAd();
		
		DBAdministration.updateAd(ad);
		
		request.setAttribute("userId", userId);
		request.setAttribute("adId", adId);
		
		if (request.getParameter("activateAd").equals("activate")) {
			request.setAttribute("source", "advertisement-activate");	
		} else if (request.getParameter ("activateAd").equals("reactivate")) {
			request.setAttribute("source", "advertisement-reactivate");	
		} else {
			request.setAttribute("source", "advertisement-auth");		
		}
		return request;
	}
}
