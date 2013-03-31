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

public class Follower extends User 
{
	
	private String comment; // == Kommentar zur Anzeige (von Follower)
	private boolean visible;
	
	public Follower ()
	{
		super();
		this.comment = "";
		this.visible = false;
	}
	
	public Follower (String academicTitle, String forename, String surname,
			String eMail, String description, boolean visible, String userId)
	{
		super(academicTitle, forename, surname, eMail, userId);
		this.comment = description;
		this.visible = visible;
	}
	
	public Follower (User user,	String description, boolean visible) 
	{
		this.setAcademicTitle(user.getAcademicTitle());
		this.setEmail(user.getEmail());
		this.setForename(user.getForname());
		this.setSurname(user.getSurname());
		this.setUserId(user.getUserId());
		
		this.comment = description;
		this.visible = visible;
	}
	
	/**
	 * @return the comment
	 */
	public String getComment() 
	{
		return comment;
	}
	
	public boolean isVisible() 
	{
		return visible;
	}
	
	/**
	 * @param set a new comment
	 */
	public void setComment(String comment) 
	{
		this.comment = comment;
	}

	public void setVisible(boolean visible) 
	{
		this.visible = visible;
	}

}