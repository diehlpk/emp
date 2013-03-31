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
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import model.Ad;
import controller.DBAdministration;

public class MainPage 
{
	/**
	 * Sends attributes: 
	 * isEmpty (String), titleArray (String[]), projectTypeArray (String[]), targetedArray (String[]),
	 * instituteArray (String[]), projectStartArray (String[]), adIdArray (String[]), userId (String, only if != null)
	 * 
	 * @param request
	 * @param userId if unknown set to null
	 * @return
	 * @throws SQLException
	 */
	public static HttpServletRequest sendData (HttpServletRequest request, String userId) throws SQLException
	{
		LinkedList<Ad> ads = DBAdministration.getAds();
		
		String [] titleArray = new String [ads.size()];
		String [] projectTypeArray = new String [ads.size()];
		String [] targetedArray = new String [ads.size()];
		String [] instituteArray = new String [ads.size()];
		String [] projectStartArray = new String [ads.size()];
		String [] adIdArray = new String [ads.size()];
		String [] projectCreateArray = new String[ads.size()];
		
		for (int i = 0; i < ads.size(); i++)
		{
			Ad adv = ads.get(i);
			titleArray [i] = adv.getTitle();
			projectTypeArray [i] = adv.getProjectType();
			targetedArray [i] = adv.getTargetedToString();
			instituteArray [i] = adv.getInstitute();
			projectStartArray [i] = adv.getProjectStart().toString();
			adIdArray [i] = adv.getAdId();
			projectCreateArray [i] = adv.getAdCreatedDate().toString();
		}
		
		String isEmpty;
		if (titleArray.length > 0)
			isEmpty = "false";
		else
			isEmpty = "true";
		
		request.setAttribute("isEmpty", isEmpty);
		request.setAttribute ("titleArray", titleArray);
		request.setAttribute("projectTypeArray", projectTypeArray);
		request.setAttribute("targetedArray", targetedArray);
		request.setAttribute("instituteArray", instituteArray);
		request.setAttribute("projectStartArray", projectStartArray);
		request.setAttribute ("adIdArray", adIdArray);
		request.setAttribute("projectCreateArray", projectCreateArray);
		
		if (userId != null)
		{
			request.setAttribute("userId", userId);
		}
		
		return request;
	}
}
