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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils 
{

	/**
	 * Read file from the given path and return its contents as text
	 * Taken from http://snippets.dzone.com/posts/show/1335
	 * @param filePath
	 * @return
	 * @throws java.io.IOException
	 * @author Andrew Spencer
	 */
	public static String readFileAsString(String filePath)
			throws java.io.IOException 
	{
		byte[] buffer = new byte[(int) new File(filePath).length()];
		BufferedInputStream f = null;
	
		try 
		{
			f = new BufferedInputStream(new FileInputStream(filePath));
			f.read(buffer);
		}
		catch (IOException e)
		{
			System.out.println (e.getMessage());
		}
		finally 
		{
			if (f != null)
				try 
				{
					f.close();
				}
				catch (IOException ignored) 
				{}
		}
		
		return new String(buffer);
	}

}
