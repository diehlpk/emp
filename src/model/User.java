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

public class User 
{

	private String academicTitle; // == Akademischer Titel des Users
	private String forname; // == Vorname des Users
	private String surname; // == Nachname des Users
	private String eMail; // == Email des Users
	private String userId; // == UserId == Primary Key in DB
	
	public User()
	{
		this.academicTitle = "";
		this.forname = "";
		this.surname = "";
		this.eMail = "";
		this.userId = "";
	}
	
	public User (String academicTitle, String forname, String surname,
			String eMail, String id)
	{
		this.academicTitle = academicTitle;
		this.forname = forname;
		this.surname = surname;
		this.eMail = eMail;
		this.userId = id;
	}

	/**
	 * @return the academicTitle
	 */
	public String getAcademicTitle() 
	{
		return academicTitle;
	}
	
	/**
	 * @return the eMail
	 */
	public String getEmail() 
	{
		return eMail;
	}
	
	/**
	 * @return the forename
	 */
	public String getForname() 
	{
		return forname;
	}
	
	/**
	 * @return the surname
	 */
	public String getSurname() 
	{
		return surname;
	}
	
	public String getUserId()
	{
		return userId;
	}
	
	/**
	 * @param academicTitle the academicTitle to set
	 */
	public void setAcademicTitle(String academicTitle) 
	{
		this.academicTitle = academicTitle;
	}
	
	/**
	 * @param eMail the eMail to set
	 */
	public void setEmail(String eMail) 
	{
		this.eMail = eMail;
	}
	
	/**
	 * @param forename the forename to set
	 */
	public void setForename(String forename) 
	{
		this.forname = forename;
	}
	
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) 
	{
		this.surname = surname;
	}
	

	public void setUserId(String id) 
	{
		this.userId = id;
	}
}
