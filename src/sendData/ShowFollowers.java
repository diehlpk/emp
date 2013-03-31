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

import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import model.Ad;
import model.Follower;
import controller.DBAdministration;

public class ShowFollowers 
{
	public static HttpServletRequest sendData (HttpServletRequest request, String adId, String userId) throws Exception
	{
		if (adId == null)
		{
			throw new Exception ("sendData.ShowFollowers.java: no adId");
		}
		if (userId == null)
		{
			throw new Exception ("sendData.ShowFollowers.java: no userId");
		}
		
		/*
		 * Variableninitialisierung
		 */
		Ad ad = DBAdministration.getAd(adId);
        LinkedList<Follower> followerList = ad.getFollowers();
		String followerCountMessage = "";
        
        /*
        String[] followersIds = null;
        
        // Abfrage aller Follower und eintragen in eine LinkedList, damit man später die Follower durchgehen kann
        try {
        	followersIds =  DBAdministration.getFollowersOfAd(adId);
        	for (int i=0; i < followersIds.length; i++) {
        		followerList.add(DBAdministration.getFollower(adId, followersIds[i]));
        	}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        */
        
        if (followerList.size() == 0)
			followerCountMessage = "Bisher sind keine Interessenten eingetragen!";
		else if (followerList.size() == 1)
			followerCountMessage = "Bisher ist ein/e Interessent/in eingetragen!";
		else
			followerCountMessage = "Bisher sind " + followerList.size() + " Interessenten eingetragen!";
        
		request.setAttribute("title", ad.getTitle());
		request.setAttribute("description", ad.getDescription());
		request.setAttribute("followerCountMessage", followerCountMessage);
		request.setAttribute("followerList", followerList);
		request.setAttribute("userId", userId);
		request.setAttribute("adId", adId);
		
		return request;
	}
}
