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

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Provides Access to the Database via Singleton Pattern
 * @author Daniel Kulesz
 *
 */
public class DBConnection {
	
	private static final DBConnection instance = new DBConnection();
	private Connection conn;
	
	private DBConnection() {
		// TODO: Some parts cloned from DBAdministration; Separate class should handle properties
		Properties props = new Properties();
		
		props.setProperty("connectionstring", Settings.getProperty("connectionstring")[0]);
		props.setProperty("driver", Settings.getProperty("driver")[0]);
		
		try {
			Class.forName(props.getProperty("driver")).newInstance();
			conn = DriverManager.getConnection (props.getProperty("connectionstring"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			// TODO: The caller needs to get informed about the failed DB Connection in a better way than now.
			
		}
	}
	
	/**
	 * Provides centralized access to the database connection
	 * @return
	 */
	public static Connection getConnection() {
		return instance.conn;
	}

}
