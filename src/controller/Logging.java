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

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Logging {
	private static final Logging instance = new Logging();
	private Logger logger =	Logger.getLogger(Logging.class.getName());
	private FileHandler fileHandler;

	private Logging() {
		if ("enabled".equals(Settings.getProperty("logginglevel")[0])) {
			logger.setLevel(Level.SEVERE);
		} else {
			logger.setLevel(Level.OFF);
		}
		try {
			fileHandler = new FileHandler(Settings.getProperty("loggingpath")[0]+Settings.getProperty
					("loggingfilename")[0], Integer.parseInt(
					Settings.getProperty("loggingfilelength")[0]),
					Integer.parseInt(Settings.getProperty(
					"loggingfilenumber")[0]));
			logger.addHandler(fileHandler);
		} catch (SecurityException e) {
			System.out.println("Unable to create logger. " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Failed to add FileHandler. " + e.getMessage());
		}
	}

	public static Logger getLogger () {
		return instance.logger;
	}
}
