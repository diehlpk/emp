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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.codec.binary.Hex;

import nl.captcha.Captcha;

public class InputSanitizer 
{

	/**
	 * Replaces ' and " with '' and checks the input for html code.  //  does not
	 * If some code is found the input is cleared.
	 * 
	 * @param input The input to be validated.
	 * 
	 * @return Returns true if the input is null.
	 */
	public static boolean validate(String input)
	{
		if (input == null) 
		{
			return true;
		} 
		else 
		{
			return (!input.contains("<") &&
					!input.contains(">") &&
					!input.contains("'") &&
					!input.contains("\""));
		}
	}
	
	/**
	 * validates Description that are edited with TinyMCE Editor
	 * @param input
	 * @return
	 */
	public static boolean validateDescription (String input)
	{
		return true;
	}
	
	public static boolean validateEmail (String email)
	{
		boolean correct = false;
		
		if (email == null)
			return false;
		
		// checks if the Mail contains an allowed domain
		for (String mail: Settings.getProperty("mailadresses")) 
		{
			if (email.contains(mail)) 
			{
				correct = true;
			}
		}
		
		return correct;
	}
	
	/**
	 * 
	 * @param email
	 * @return null if email is valid
	 * else returns the error message
	 */
	public static String checkEmail (String email)
	{
		boolean correct = false;
		
		if (email == null || "".equals(email))
			return "Die Email darf nicht leer sein.";
		
		// checks if the Mail contains an allowed domain
		for (String mail: Settings.getProperty("mailadresses")) 
		{
			if (email.contains(mail)) 
			{
				correct = true;
			}
		}
		
		if (!correct)
		{
			return "Die von Ihnen eingegebene E-Mail-Adresse ist f&uuml;r das Erstellen " +
						"einer Anzeige ung&uuml;ltig!";
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param source
	 * @return null if source is valid
	 * else returns the error message
	 */
	public static String checkString (String source)
	{
		if (source == null || "".equals(source))
		{
			return "Dieses Feld darf nicht leer sein!";
		} 
		else if (!InputSanitizer.validate(source)) 
		{
			String temp = "Ihre Anzeige enth&auml;llt folgende Zeichen die nicht aktzeptiert werden: ";
			boolean needsComma = false;
			String [] restrictedString = {"<", ">", "'", "\""};
			
			for (String rs : restrictedString)
			{
				if(source.contains(rs))
				{
					if (needsComma)
					{
						temp += ", ";
					}
					temp += "'" + rs + "'";
					needsComma = true;
				}
			}
			
			//temp += "\n";
			//temp += "Bitte entfernen sie alle aufgelisteten Zeichen";
			
			return temp;
		}
		
		return null;
	}
	
	/**
	 * Ueberprueft ob ein Datum day.month.year ein gueltiges datum ergibt
	 * @param day
	 * @param month
	 * @param year
	 * @return Date.getTime () if valid
	 * else error message
	 */
	public static String checkDate (String day, String month, String year)
	{
		String result = null;
		
		DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		Calendar cal = new GregorianCalendar ();
		
		Date projectStartDate = null;
		String projectStart = day + "." + month + "." + year;
		
		int monthInt = Integer.parseInt(month) - 1;
		int dayInt = Integer.parseInt(day);
		
		cal.set (Calendar.MONTH, monthInt);
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		if (dayInt <= maxDay)
		{
			try 
			{
				formatter.setLenient(false);
				projectStartDate = formatter.parse(projectStart);
				result = Long.toString(projectStartDate.getTime());
			} 
			catch (ParseException e) 
			{
				result = "Ihre Eingabe ist kein g&uuml;ltiges Datum.";
			}
		}
		else
		{
			result = "Ihre Eingabe ist kein g&uuml;ltiges Datum.";
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param targetGroups
	 * @return null if valid
	 * else error message
	 */
	public static String checkTargetGroups (String [] targetGroups)
	{
		if (targetGroups == null)
		{
			return "Es muss mindestens eine Zielgruppe ausgewaehlt werden.";
		}
		else
		{
			for (String item: targetGroups)
			{
				if (!InputSanitizer.validate(item))
				{
					return "Fehler bei Zielgruppen Auswahl.";
				}	
			}
		}
		
		return null;
	}
	
	public static String checkCaptcha (Captcha captcha, String captchaResponse)
	{
		
		if (captchaResponse == null || captcha == null)
		{
			return "Fehlerhafte Eingabe.";
		}
		else if ("".equals(captchaResponse) || !captcha.isCorrect(captchaResponse)) 
		{
			return "Fehlerhafte Eingabe.";
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param maxTitleLength
	 * @param maxWordLength = max Length of each word
	 * @return null if valid, else return error message
	 */
	public static String checkString (String source, int maxTitleLength, int maxWordLength)
	{
		String result = null;
		if (source.length() > maxTitleLength)
		{
			result = "Ihre Eingabe ist mit " + source.length () + " Zeichen zu lang. Die maximale " + 
					"erlaubte L&auml;nge betr&auml;gt " + maxTitleLength + " Zeichen.";
		}
		else
		{
			String [] temp = source.split(" ");
			for (int i = 0; i < temp.length; i++)
			{
				// maxWordLength muss um eins erhöht werden, da length() mit 1 anfängt zu zählen
				if (temp[i].length() > maxWordLength)
				{
					result = "Ihre Eingabe enth&auml;lt W&ouml;rter die zu lang sind. "
							+ "Bitte achten sie darauf dass kein Wort l&auml;nger als " 
							+ maxWordLength + " Zeichen ist.";
					
					i = temp.length;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param source
	 * @return null if valid
	 * else error message
	 */
	public static String checkTinyMCEText (String source)
	{
		if (source == null || "".equals(source))
			return "Diese Eingabe darf nicht leer sein";
		
		return null;
	}
	
	private static String getDigest(InputStream is, MessageDigest md, int byteArraySize)
			throws NoSuchAlgorithmException, IOException {

		md.reset();
		byte[] bytes = new byte[byteArraySize];
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			md.update(bytes, 0, numBytes);
		}
		byte[] digest = md.digest();
		String result = new String(Hex.encodeHex(digest));
		return result;
	}
	
	/**
	 * Benennt hochgeladene Dateien um und aendert die zugehoerigen Verweise auf die Datei
	 */
	public static String checkAttachment(String link) throws NoSuchAlgorithmException, FileNotFoundException, IOException
	{
		String uploadDir1 = "/Users/schraphy/Documents/UNI/Semester6/Projekt/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/uploads/";
		String uploadDir2 = "/home/eldorado/Dokumente/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/uploads/";
		String uploadDir3 = "C:/Users/Tobi/Documents/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/uploads/";
		String uploadDirOnServer = "webapps/uploads/";
		
		//  Setze uploadDir auf das richtige Verzeichnis
		String uploadDir = uploadDirOnServer;
		//  ab hier wird nur noch uploadDir genutzt, d.h. keine festen Strings als Directory fuer den upload Ordner
		
		link = checkAttachmentForFiles(link, uploadDir);
		link = checkAttachmentForPictures(link, uploadDir);
		
		return link;
	}
	
	private static String checkAttachmentForPictures (String description, String uploadDir) throws NoSuchAlgorithmException, FileNotFoundException, IOException
	{
		String modifiedDescription ="";
		
		if (description.contains("src=\"/uploads/")) // suche Bilder
		{
			// die Schleife ist notwenidig falls mehrere Dateien hinzugeuegt werden
			// andernfalls wuerde nur die erste Datei einen MD5 Title erhalten
			String[] splitDescription = description.split("<img");
			
			String newSourceString = "src=\"/"+uploadDir;
			if (uploadDir.equals("webapps/uploads/"))
			{
				newSourceString = "src=\"/uploads/";
			}
			
			for (String img : splitDescription) 
			{
				if (img.contains("src=\"/uploads/"))
				{
					img = img.replace("src=\"/uploads/", newSourceString);
					
					int indexBegin = img.indexOf(newSourceString) + newSourceString.length();
					int indexEnd = img.indexOf("\"", indexBegin + 1);
					String oldFilename = img.substring(indexBegin, indexEnd);
					String oldFilePath = uploadDir + oldFilename;							
					
					MessageDigest md = MessageDigest.getInstance("MD5");
					String digest = getDigest(new FileInputStream(oldFilePath), md, 2048);					
					
					if (!oldFilename.equals(digest))
					{
						String oldFileBigPath = uploadDir + oldFilename + "_big";
						String bigDigest = getDigest(new FileInputStream(oldFileBigPath), md, 2048);
						
						String newFilePath = uploadDir + digest;
						String newFileBigPath = uploadDir + bigDigest; 
						
						// definiere thumbnail als newFile und neue Datei md5File
						File oldFile = new File(oldFilePath);
						File md5File = new File(newFilePath); // new File

						// definiere Original File (thumbnail) als newFile und neue Datei md5File
						File oldFileOrg = new File(oldFileBigPath);
						File md5FileOrg = new File(newFileBigPath); // new File
						
						// Benenne Files um
						if (oldFile.renameTo(md5File))
						{
							img = img.replace(oldFilename, digest);
						}
						
						String linkToBigPictureBegin="";
						String linkToBigPictureEnd="";
						
						if (oldFileOrg.renameTo(md5FileOrg))
						{
							newSourceString = "href=\"" + uploadDir;
							if (uploadDir.equals("webapps/uploads/")) // if real server use this
							{
								newSourceString = "href=\"/uploads/";
							}
							
							linkToBigPictureBegin = "<a " + newSourceString + bigDigest + "\" target=\"_blank\">";
							linkToBigPictureEnd = "</a>";
						}
						
						img = linkToBigPictureBegin + "<img " + img + linkToBigPictureEnd;
						System.out.println (img);
					}
					else
					{
						System.out.println (img);
						img = "<img" + img;
					}
				}
				else
				{
					if (!description.startsWith(img))
					{
						img = "<img" + img;
					}
				}
				modifiedDescription = modifiedDescription + img;
			}
		}
		else // no images => no changes
		{ 
			modifiedDescription = description;
		}
		return modifiedDescription;
	}
	
	private static String checkAttachmentForFiles (String description, String uploadDir) throws NoSuchAlgorithmException, FileNotFoundException, IOException
	{
		String modifiedDescription = "";
		if ( description.contains("href=\"/uploads/")) // suche andere Files (PDF, Code... etc)
		{
			// die Schleife ist notwenidig falls mehrere Dateien hinzugeuegt werden
			// andernfalls wuerde nur die erste zu MD5 
			String[] split = description.split("<a");
			String newSourceString = "href=\"" + uploadDir;
			System.out.println ("newSourceString: " + newSourceString);
			
			if (uploadDir.equals("webapps/uploads/")) // if real server use this
			{
				newSourceString = "href=\"/uploads/";
			}
			
			for (String s : split) 
			{
				if (s.contains("href=\"/uploads/"))
				{
					s = s.replaceFirst("href=\"/uploads/", newSourceString);
					
					int indexBegin = s.indexOf(newSourceString) + newSourceString.length();
					int indexEnd = s.indexOf("\"", indexBegin + 1);
					
					String oldFilename = s.substring(indexBegin, indexEnd);
					String file = uploadDir + s.substring(indexBegin, indexEnd);
					
					MessageDigest md = MessageDigest.getInstance("MD5");
					String digest = getDigest(new FileInputStream(file), md, 2048);
					
					File oldFile = new File(uploadDir + s.substring(indexBegin, indexEnd));
					File md5File = new File(uploadDir + digest);
					//  Funktioniert unter windows nicht
					if (!oldFile.renameTo(md5File))
					{
						System.out.println ("Renaming went wrong");
					}
					
					s = s.replace(oldFilename, digest);
				}

				if (s.contains("href=\"http") || s.contains("href=\"www") || s.contains(newSourceString)) // http links abfangen
				{
					s = "<a" + s;
				}
				
				modifiedDescription = modifiedDescription + s;
			}
		}
		else
		{
			modifiedDescription = description;
		}

		System.out.println ("descr: " + modifiedDescription);
		return modifiedDescription;
	}
}
