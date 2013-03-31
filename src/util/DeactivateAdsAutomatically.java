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

import java.sql.SQLException;
import java.util.*;
import javax.mail.MessagingException;

import model.Ad;

import controller.DBAdministration;
import controller.Logging;
import controller.Mailer;

/*
 * actualTime                              deadlineTime
 * |                                       |
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~> := differenceTime = deadlineTime - actualTime
 *                          ^
 *                          warningTime
 * 
 */
public class DeactivateAdsAutomatically {

	private LinkedList<Ad> adList = new LinkedList<Ad>();
	private String deactivationSendMail = controller.Settings.getProperty("deactivationSendMail")[0].toLowerCase();
	private long deactivationIntervalTimeInSeconds = Integer.parseInt(controller.Settings.getProperty("deactivationIntervalTimeInSeconds")[0]);
	long actTime = System.currentTimeMillis();
	private String hostname = controller.Settings.getProperty("hostname")[0];
	private String urlPersonalArea ="";
	

	/*
	 * Achtung!
	 * deadlineTime muss von Hand nachträglich gesetzt werden
	 * hierfür gibt es die Methode setDeadlineTime(Ad), die 
	 * anhand von der Anzeige die richtige Zeit setzt
	 */
	DeactivateAdsAutomatically() {
		// Hiermit bekommt man nur alle aktiven Ads, die visible == true haben
		try 
		{
			this.adList = DBAdministration.getAds();
		}
		catch (SQLException e)
		{
			Logging.getLogger().severe("Failed to get ads at DeactivateAdsAutomatically.java. " + e.getMessage());
		}
	}
	
	/**
	 * Diese Funktion ruft einfach nur jede einzelne Anzeige auf. Eigentlich kann man diese auch
	 * direckt in die checkAd mit einbauen.
	 */
	public void checkAllAds() 
	{	
		// Gehe jede sichtbare Anzeige durch und checke ob diese Abgelaufen ist
		for (Ad item: adList)
		{
			checkAd(item);
		}
	}
	
	/**
	 * Falls die Anzeige abgelaufen ist, deaktiviere sie
	 * @param adId
	 */
	public void checkAd (Ad ad)
	{
		// Muss manuell für jede ad gesetzt werden, da sonst die Zeit nicht zu jeweiligen ad passt
		urlPersonalArea = hostname + "PersonalSection?userId=" + ad.getInitiatorId();
		
		try
		{
			if (ad.getDifferenceTime() >= 0 && ad.getDifferenceTime() <= ad.getWarningTime())
			{
				if (deactivationSendMail.equals("once")){
					/**
					 * Berechnung des Zeitfensters 
					 * 
					 *                  intervalTime          deadlineTime
					 *                    v-----v             |
					 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
					 *    ^               ^
					 *    actTime         warningTime
					 * 
					 * Erst wenn die actTime in die intervalTime reinläuft wird eine E-Mail
					 * rausgeschickt davor nicht und danach auch nicht mehr
					 * 
					 */
					if (  ad.getExpiryDateAsTime()-ad.getWarningTime() <= actTime && 
					     (ad.getExpiryDateAsTime()-ad.getWarningTime()+(deactivationIntervalTimeInSeconds*1000)) > actTime)
					{	
						Mailer mail = new Mailer ();
						/*
						 * TODO: Es wäre schön, wenn hier noch eine Abfrage drin wär, wenn an diesem Tag
						 * 		 schon eine Mail an den Initiator rausgegangen ist, damit er nicht nochmals
						 * 		 von dem System eine Mail bekommt
						 */
						mail.sendDeactivationDeadlineMessage(ad.getEmailToInitiator(), 
															 ad.getInitiatorIAsString(), 
															 ad.getTitle(), 
															 ad.getRestDays(), 
															 ad.getExpiryDateAsString(), 
															 urlPersonalArea);
					}
				}
				else
				{
					Mailer mail = new Mailer ();
					/*
					 * TODO: Es wäre schön, wenn hier noch eine Abfrage drin wär, wenn an diesem Tag
					 * 		 schon eine Mail an den Initiator rausgegangen ist, damit er nicht nochmals
					 * 		 von dem System eine Mail bekommt
					 */
					mail.sendDeactivationDeadlineMessage(ad.getEmailToInitiator(), 
														 ad.getInitiatorIAsString(), 
														 ad.getTitle(), 
														 ad.getRestDays(), 
														 ad.getExpiryDateAsString(), 
														 urlPersonalArea);
				
				}
			}
			else if (ad.getDifferenceTime() < 0 && ad.isActiv())
			{
				Mailer mail = new Mailer ();
				mail.sendDeactivatedAdAutomatic(ad.getEmailToInitiator(), 
												ad.getInitiatorIAsString(), 
												ad.getTitle(), 
												urlPersonalArea);
				
				// Muss geupdatet werden, da die Anzeige nun deaktiviert wurde
				ad.deactivateAd();
				DBAdministration.updateAd(ad);
			}
		}
		catch (SQLException e) 
		{
			Logging.getLogger().severe("SQL: Failed to get ads at DeactivateAdsAutomatically.java. " + e.getMessage());
		}
		catch (MessagingException e) 
		{
			Logging.getLogger().severe("MSG: Failed at DeactivateAdsAutomatically.java. " + e.getMessage());
		}
	}
}
