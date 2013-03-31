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

import controller.Settings;

public class ToolTipps 
{
	public static String getEmailToolTipp ()
	{
		String returnString = "Es werden nur E-Mail-Adressen akzeptiert, die auf ";
		String [] mailAdresses = Settings.getProperty("mailadresses");
		for (int i = 0; i <  mailAdresses.length - 1; i++)
		{
			returnString += mailAdresses [i] + ", ";
		}
		if (mailAdresses.length > 1)
			returnString += "oder ";
		
		returnString += mailAdresses [mailAdresses.length-1];
		returnString += " enden!";
		
		return returnString;
	}
	
	public static String getEmailContactToolTipp ()
	{
		return "&Uuml;ber das Kontaktformular dem Initiator eine E-Mail schreiben";
	}
	
	public static String getCommentToolTipp ()
	{
		return "In diesem Feld k&ouml;nnen Sie einen Kommentar eingeben. (optional)";
	}
	
	public static String getVisibilityToolTipp ()
	{
		String result = "Mit dieser Option k&ouml;nnen Sie festlegen, f&uuml;r wen die Liste "+
				"der Interessenten sichtbar sein soll. Sie k&ouml;nnen die Sichtbarkeit "+
				"sp&auml;ter nur noch einschr&auml;nken, jedoch nicht erweitern.";
		
		return result;
	}
	
	public static String getDescriptionToolTipp ()
	{
		String result = "Hier k&ouml;nnen Sie einen Freitext eingeben, um Ihre Anzeige "+
				"genauer zu beschreiben. Beachten Sie, dass die Eingabe von Code bzw. "+
				"Codefragmenten nicht gestattet ist. Falls Sie also eine l&auml;ngere "+
				"Beschreibung eingeben, sollten Sie diese vor dem Absenden sichern!";
		
		return result;
	}
}
