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
package model;

import java.sql.SQLException;
import java.util.*;

import controller.DBAdministration;
import controller.Logging;

public class Ad 
{
	
	private String title; //  == Name der Anzeige
	
	private String academicTitle;   //  goto: initiator
	private String forename;  //  goto: initiator
	private String surname; //  goto: initiator
	private String eMail;  //  goto: initiator
	
	private Date projectStart; // == Begin des Projekts, zukünfitg auch ein String möglich
	private Date adCreated; // == Erstellungsdatum der Anzeige
	private Date archivedDate; // == ???

	
	// Automatic deactivation
	/*
	 * actualTime                              deadlineTime
	 * |                                       |
	 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~> := differenceTime = deadlineTime - actualTime
	 *                          ^
	 *                          warningTime
	 */
	private long actualTime;
	private Date deactivationDate; // == Deaktivierungsdatum der Anzeige
	private long differenceTime;
	private long warningTime;
	
	private String institute; // == zugeordnetes Institut
	private String [] projectType; // == zugeordnete Projekttypen
	private String[] targeted; // == Zielgruppe
	private String description; // == Beschreibung der Anzeige
	
	private String adId; // == adId == Id der Anzeige == Primary Key in der DB
	private String initiatorId; // == userId des initiators == Primary Key in der DB für User Objekte
	private String followersVisible; // == ???
	private String [] visibleTypes = {"Initiator und Follower", "Oeffentlich", "Nur Initiator"};

	private LinkedList<Follower> followers; // == Liste der Interessierten User
	private boolean isActive; // == Flag ob die Anzeige aktiv ist
	
	
	// TODO: Wird momentan nicht benutzt
	private boolean isQuest; 
	
	
	/**
	 * Private Methoden
	 */
	private void recalcDifferenceTime() {
		actualTime = System.currentTimeMillis();
		this.differenceTime = (deactivationDate.getTime() - actualTime);
	}
	
	// Constructor without parameter
	public Ad()
	{
		this.title = "";
		this.academicTitle = "";
		this.forename = "";
		this.surname = "";
		this.eMail ="";
		
		this.adCreated = new Date ();
		
		this.institute = "Keinem Institut zugeordnet";
		this.projectType = new String [8];
		this.projectType [0] = "Sonstige";
		
		this.targeted = new String [1];
		this.targeted [0] = "Super";
		this.description = "";
		this.adId = "";
		this.initiatorId = "";
		this.followersVisible = visibleTypes[0];
		this.followers = new LinkedList<Follower>();
	
		this.isQuest = false;
		this.isActive = false;
		
		// Automatic deactivation
		setDeactivationDate();
		recalcDifferenceTime();
		this.warningTime = Integer.parseInt(controller.Settings.getProperty("deactivationDeadlineTimeInDays")[0]) * 60 * 60 * 24;
	}
	
	
	//  Dieser Konstruktor wird hpts von DBAdministration genutzt
	/**
	 * Dieser Konstruktor wird hauptsächlich von der DBAdministration.java benutzt.
	 * 
	 * Keine Ahnung ob der Konstruktor noch irgendwo anders benutzt wird.
	 * 
	 * @param title
	 * @param academicTitle
	 * @param forename
	 * @param surname
	 * @param eMail
	 * @param projectStart
	 * @param deactivationDate
	 * @param adCreated
	 * @param institute
	 * @param projectType
	 * @param targeted
	 * @param description
	 * @param adId
	 * @param initiatorId
	 * @param followersVisible
	 * @param archivedDate
	 * @param quest
	 * @param isActiv
	 * @param follower
	 */
	public Ad(
			String title, 
			String academicTitle, 
			String forename,
			String surname, 
			String eMail, 
			Date projectStart, 
			Date deactivationDate,
			Date adCreated,	
			String institute, 
			String projectType,
			String targeted, 
			String description, 
			String adId, 
			String initiatorId,
			String followersVisible, 
			Date archivedDate, 
			boolean quest,
			boolean isActiv, 
			LinkedList<Follower> follower) 
	{
		this.title = title;
		this.academicTitle = academicTitle;
		this.forename = forename;
		this.surname = surname;
		this.eMail = eMail;
		
		this.projectStart = projectStart;
		this.deactivationDate = deactivationDate;
		this.adCreated = adCreated;
		
		this.institute = institute;
		
		this.projectType = new String [8];
		this.projectType [0] = projectType;
		
		this.targeted = wrapperStringToArray(targeted);
		this.description = description;
		this.adId = adId;
		this.initiatorId = initiatorId;
		
		boolean temp = false;
		for (String s : visibleTypes)
		{
			if (followersVisible.equals(s))
			{
				temp = true;
				this.followersVisible = followersVisible;
			}
		}
		
		if (!temp)
			this.followersVisible = visibleTypes[0];
		
		
		this.archivedDate = archivedDate;
		this.followers = follower;
	
		this.isQuest = false;
		this.isActive = isActiv;
		
		// Automatic deactivation
		//setDeactivationDate(); //darf hier nicht gesetzt werden, da sonst das Ablaufdatum immer neu gesetzt wird
		recalcDifferenceTime();
		this.warningTime = Integer.parseInt(controller.Settings.getProperty("deactivationDeadlineTimeInDays")[0]) * 60 * 60 * 24;
	}
	
	/**
	 * Dieser Konstruktor wird von EditAdvertisement.java benutzt
	 * @param title
	 * @param academicTitle
	 * @param forename
	 * @param surname
	 * @param eMail
	 * @param projectStart
	 * @param deactivationDate
	 * @param adCreated
	 * @param institute
	 * @param projectType
	 * @param targeted
	 * @param description
	 * @param adId
	 * @param initiatorId
	 * @param followersVisible
	 * @param archivedDate
	 * @param quest
	 * @param isActiv
	 * @param follower
	 */
	public Ad(
			String title, 
			String academicTitle, 
			String forename,
			String surname, 
			String eMail, 
			Date projectStart, 
			Date adCreated,	
			String institute, 
			String projectType,
			String[] targeted, 
			String description, 
			String adId, 
			String initiatorId,
			String followersVisible, 
			Date archivedDate, 
			boolean quest,
			boolean isActiv, 
			LinkedList<Follower> follower) 
	{
		this.title = title;
		this.academicTitle = academicTitle;
		this.forename = forename;
		this.surname = surname;
		this.eMail = eMail;
		
		this.projectStart = projectStart;
		this.adCreated = adCreated;
		
		
		this.institute = institute;
		
		this.projectType = new String [8];
		this.projectType [0] = projectType;
		
		this.targeted = targeted;
		this.description = description;
		this.adId = adId;
		this.initiatorId = initiatorId;

		this.followersVisible = followersVisible;
		this.archivedDate = archivedDate;
		this.followers = follower;
		
		this.isQuest = false;
		this.isActive = isActiv;
		
		// Automatic deactivation
		setDeactivationDate();
		recalcDifferenceTime();
		this.warningTime = Integer.parseInt(controller.Settings.getProperty("deactivationDeadlineTimeInDays")[0]) * 60 * 60 * 24;

	}
	

	/**
	 * @param follower the follower to add to the list
	 */
	public void addFollower(Follower follower)
	{
		this.followers.add(follower);
	}
	
	public void activateAd ()
	{
		this.isActive = true;
	}
	
	public void deactivateAd ()
	{
		this.isActive = false;
	}
	
	/**
	 * ???  This makes no sense, an ad has no academic Title, this would be the user=initiator
	 * @return the academic Title 
	 */
	public String getAcademicTitle() 
	{
		return this.academicTitle;
	}
	
	public Date getAdCreatedDate() 
	{
		return adCreated;
	}
	
	/**
	 * @return the adId
	 */
	public String getAdId() 
	{
		return adId;
	}
	
	/**
	 * @return the archivedDate
	 */
	public Date getArchivedDate() 
	{
		return archivedDate;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() 
	{
		return description;
	}
	
	/**
	 * Gibt die DifferenceTime = deadlineTime - actualTime zurück
	 * 
	 * @return String - differenceTime
	 */
	public long getDifferenceTime (){
		//System.out.println("getDifferenceTime: "+ this.differenceTime / 1000);
		return this.differenceTime;
	}
	
	/**
	 * @return the eMail
	 */
	public String getEmailToInitiator() 
	{
		return this.eMail;
	}
	
	/**
	 * @return ExpiryDate
	 */
	public Date getExpiryDate() 
	{
		return deactivationDate;
	}
	
	/**
	 * @return ExpiryDate as calendar
	 */
	public String getExpiryDateAsString() 
	{
		String date = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.getExpiryDate());
		date = Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + 
			   "." + Integer.toString((cal.get(Calendar.MONTH)+1)) + 
			   "." + Integer.toString(cal.get(Calendar.YEAR));
		return date;
	}
	
	/**
	 * @return ExpiryDate as long
	 */
	public long getExpiryDateAsTime() 
	{
		return deactivationDate.getTime();
	}
	
	/**
	 * @return a LinkedList of all Followers
	 */
	public LinkedList<Follower> getFollowers()
	{
		return followers;
	}
	
	/**
	 * TODO: RENAME: areFollowersVisible ()
	 * @return the followersVisible
	 */
	public String getFollowersVisible() 
	{
		return followersVisible;
	}
	
	/**
	 * @return the forename
	 */
	public String getForename() 
	{
		return this.forename;
	}
	
	/**
	 * @return the initatorId
	 */
	public String getInitiatorId() 
	{
		return initiatorId;
	}
	
	/**
	 * @return the initatorId
	 */
	public String getInitiatorIAsString() 
	{
		String initiatorName = getAcademicTitle() + " " + getSurname();
		initiatorName = initiatorName.trim();
		return initiatorName;
	}
	
	/**
	 * @return the institute
	 */
	public String getInstitute() 
	{
		return institute;
	}
	
	/**
	 * @return the projectStart
	 */
	public Date getProjectStart() 
	{
		return projectStart;
	}
	
	/**
	 * @return the projectType
	 */
	public String getProjectType() 
	{
		return projectType [0];
	}
	
	/**
	 * Gibt die Resttage zurück, die noch verbleiben bis die Anzeige automatisch deaktiviert wird
	 * 
	 * @param Ad - ad
	 * @return String - restDays
	 */
	public String getRestDays (){
		String restDays = null;
		long out = 0;
		long unitDay = 60*60*24*1000;
		long unitHour = 60*60*1000;
		
		// nochmaliges Berechnen der neuen differenceTime
		recalcDifferenceTime();
		
		// Abschneiden der Nachkommastellen
		out =  differenceTime / unitDay;
		
		/*
		 * Prüfe ob der Rest <= 0 ist, da es auch sein kann, dass
		 * differenceTime negativ werden kann und dies nicht möglich
		 * sein kann
		 */
		if (differenceTime > unitDay){
			out = differenceTime/ unitDay;
			if (out == 1) {
				restDays = out + " Tag";	
			} else {
				restDays = out + " Tage";					
			}
		} else if (differenceTime >= 0 && differenceTime <= unitDay) {
			// Umwandeln in String
			out = differenceTime / unitHour;
			if (out == 1) {
				restDays = out + " Stunde";	
			} else {
				restDays = out + " Stunden";					
			}
		} else {
			// Umwandeln in String
			restDays = "deaktiviert";
		}
		
		return restDays;
	}
	
	/**
	 * @return the surname
	 */
	public String getSurname() 
	{
		return this.surname;
	}
	
	/**
	 * @return The different targets as an array of strings
	 */
	public String [] getTargeted() 
	{
		return targeted.clone();
	}
	
	/**
	 * @return The different targets as a string
	 */
	public String getTargetedToString ()
	{
		//  Erlaubt auch Zeilenumbrüche, durch das Leerzeichen nach dem Komma
		String temp ="";
		for (int i = 0; i < this.targeted.length - 1; i++)
			temp += targeted[i] + ", ";
				
		temp += targeted[targeted.length - 1];
		
		return temp;
	}
	
	/**
	 * @return The different targets as a string
	 */
	public String getTargetedToSaveinDB ()
	{
		return wrapperArrayToString(targeted);
	}

	/**
	 * @return the title
	 */
	public String getTitle() 
	{
		return title;
	}
	
	/**
	 * Gibt die Zeit in Milli zurück, die in den Settings angegeben wurde um zu entscheiden
	 * in welchem Zeitraum eine Infomail rausgeschickt werden soll, dass die Anzeige
	 * in den nächsten Tagen deaktiviert wird.
	 * 
	 * @return String - warningTime
	 */
	public long getWarningTime (){
		return this.warningTime * 1000;
	}
	
	/**
	 * If the ad is active, then the function return true
	 * @return active
	 */
	public boolean isActiv ()
	{
		return this.isActive;
	}
	/**
	 * @return the archived
	 */
	public boolean isArchived() 
	{
		return archivedDate != null;
	}
	
	public boolean isQuest() 
	{
		return false;
	}
	
	/**
	 * @return the visible
	 */
	public boolean isVisible() 
	{
		return this.isActive;
	}
	
	/*
	 * @param surname the surname to set
	 
	public void setAcademicTitle(String academicTitle) 
	{
		this.academicTitle = academicTitle;
	}*/
	
	/**
	 * @param id sets the adId
	 */
	public void setAdId(String id) 
	{
		this.adId = id;
	}
	
	public void setAdCreated(Date adCreated) 
	{
		this.adCreated = adCreated;
	}
	
	/**
	 * @param archivedDate the archivedDate to set
	 */
	public void setArchived(Date archivedDate) 
	{
		this.archivedDate = archivedDate;
	}
	
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) 
	{
		this.description = description;
	}
	
	/*
	public void setDeactivationDate (Date d)
	{
		this.deactivationDate = new Date (d.getTime());
	}
	*/
	/**
	 * Sets deactivationDate to today + deadlineExpiryDate
	 * @param days
	 */
	public void setDeactivationDate ()
	{
		long daysInMilli = 0;
		long tempTime = 0;
		
		// Tage aus den Settings nehmen
		long days = Integer.parseInt(controller.Settings.getProperty("deactivationExpiryDateInDays")[0]);
		
		// Weil sonst immer noch ein Tag fehlt
		//days -= 1;
		
		// Aktuelle Zeit nehmen
		this.actualTime = System.currentTimeMillis();
		
		// Die Tage in Millisekunden umrechnen und auf die aktuelle Zeit aufrechnen
		daysInMilli = days * 60 * 60 * 24 * 1000;
		
		tempTime = this.actualTime + daysInMilli;
		
		// Setzen des neuen Ablaufdatums
		this.deactivationDate = new Date (tempTime);
		
		try {
			DBAdministration.updateAdExpiryDate(this.getAdId(), this.deactivationDate.getTime());
		} catch (SQLException e) {
			Logging.getLogger().severe("SQL: Failed to set expiryDate at Ad.java. " + e.getMessage());
		}
	}
	
	/**
	 * Sets deactivationDate to today - 1 day
	 * @param days
	 */
	public void setDeactivationDateClear ()
	{

		// Aktuelle Zeit nehmen
		this.actualTime = System.currentTimeMillis();
		
		// Setzen des neuen Ablaufdatums aktuelle Zeit minus ein Tag
		this.deactivationDate = new Date (actualTime - 86400);
		
		try {
			DBAdministration.updateAdExpiryDate(this.getAdId(), this.deactivationDate.getTime());
		} catch (SQLException e) {
			Logging.getLogger().severe("SQL: Failed to set expiryDate at Ad.java. " + e.getMessage());
		}
	}

	/*
	 * @param eMail the eMail to set
	 
	public void seteMail(String eMail) 
	{
		this.eMail = eMail;
	}*/
	
	

	/**
	 * @param follower the follower to set
	 */
	public void setFollowers(LinkedList<Follower> followers)
	{
		this.followers = followers;
	}
	
	/**
	 * Setzt den Wert fuer wen die Interessenten sichtbar sind
	 * @param followersVisible the followersVisible to set
	 */
	public void setFollowersVisible(String followersVisible) 
	{
		this.followersVisible = followersVisible;
	}
	
	/*
	 * @param forename the forename to set
	 /
	public void setForename(String forename) 
	{
		this.forename = forename;
	}*
	

	
	/**
	 * @param id the id to set
	 */
	public void setInitiatorId(String initiatorId) 
	{
		this.initiatorId = initiatorId;
	}

	/**
	 * @param institute the institute to set
	 */
	public void setInstitute(String institute) 
	{
		this.institute = institute;
	}
	
	/**
	 * @param projectStart the projectStart to set
	 */
	public void setProjectStart(Date projectStart) 
	{
		this.projectStart = projectStart;
	}
	
	/**
	 * @param projectType the projectType to set
	 */
	public void setProjectType(String projectType) 
	{
		this.projectType [0] = projectType;
	}
	
	/*
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) 
	{
		this.surname = surname;
	}
	
	public void setTargeted(String[] targeted) 
	{
		this.targeted = targeted;
	}
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean isActive) 
	{
		this.isActive = isActive;
	}

	
	
	/**
	 * Die Funktion macht aus einem Stringarray einen durch Kommatas
	 * getrennten String. Die Funktion hängt keine Kommatas an das Ende
	 * des fertigen Strings.
	 * 
	 * Falls das übergebene Array keine Werte enthält so wird der String
	 * mit "" initialisiert.
	 * 
	 * 
	 * @param Stringarray
	 * @return String mit Kommatas getrennt
	 */
	public String wrapperArrayToString(String[] strings) 
	{
		StringBuffer string = new StringBuffer("");
		
		if (strings == null) 
		{
			return string.substring(0);
		}
		else if (strings.length == 1) {
			string.append(strings[0]);
		}
		else
		{
			for(int i = 0; i<= strings.length-1; i++){
				if (i == strings.length-1){
					string.append(strings[i]);
				}
				else
				{
					string.append(strings[i]);
					string.append(",");
				}
			}	
		}

		return string.substring(0);
	}
	
	/**
	 * Die Funktion wandelt einen String, der durch Kommatas getrennt ist in
	 * ein Array um, welches die Elemente enthält, die durch Kommatas getrennt
	 * wurden.
	 * Falls ein leerer String an die Funktion übergeben wurde gibt sie ein Array
	 * mit einem Element zurück und schreibt in dieses einen String "Fehler".
	 * 
	 * @param String mit Kommatas getrennt 
	 * @return Stringarray
	 */
	public String[] wrapperStringToArray(String source) 
	{
		String[] stringArray;
		
		if (source == null)
		{
			stringArray = new String [1];
			stringArray[0] = "Fehler";
		}
		else
		{
			if (source.contains(","))
			{
				stringArray = source.split(",");
			}
			else
			{
				stringArray = new String [1];
				stringArray[0] = source;
			}
		}
		
		return stringArray;
	}

}

