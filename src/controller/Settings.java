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
package controller;
import java.util.Properties;

// This class is used to get the settings written in settings.cfg

public class Settings {

	/*
	 * This method returns an array of the properties set in the settings.cfg.
	 * The values are split by commata.
	 */
	/**
	 * This method loads the properties from "settings.cfg".
	 * 
	 * @param The identifier of the properties that should be loaded.
	 * 
	 * @return An Array of Strings that contains the properties.
	 * Returns null if it can not load the properties.
	 */
	public static String[] getProperty(String property) {

		String[] props;
		String prop;
		Properties p = new Properties(); 
		try {
			p.load(Settings.class.getResourceAsStream("/settings.cfg"));
		} catch (Exception e) {
			Logging.getLogger().severe(e.getMessage());
			e.printStackTrace();
			return null;
		}
		prop = p.getProperty(property);
		props = prop.split(",");
		return props;
	}

}