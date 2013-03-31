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

package util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import controller.Logging;

public class Messages 
{
	private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	public static String getString(String key) 
	{
		try 
		{
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e) 
		{
			Logging.getLogger().severe("Unable to find object with the key: "
					+ key);
			return '!' + key + '!';
		}
	}
}
