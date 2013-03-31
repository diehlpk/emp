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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Properties;
import java.util.UUID;

import model.Ad;
import model.Follower;
import model.User;
import util.FileUtils;

public class DBAdministration {

	private static Properties props = new Properties();

	/**
	 * Add a single Ad into the Database
	 * @param advert
	 * @return String UserID
	 * @throws SQLException
	 */
	public static String addActiveAd(Ad advert) throws SQLException 
	{
		User user;
		String SQLQuery = "INSERT INTO " +
				"\"advertisement_table\" " +
				"(\"adId\", \"title\", " +
				"\"projectedStartDate\", " +
				"\"expiryDate\"," +
				" \"institute\", " +
				"\"projectType\", " +
				"\"targetedAudience\", " +
				"\"description\", " +
				"\"followersVisible\", " +
				"\"archivedDate\", " +
				"\"Search\", " +
				"\"visible\") " + 
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, null, ?, false)";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement(SQLQuery);
		pst.setString(1, advert.getAdId());
		pst.setString(2, advert.getTitle());
		pst.setDate(3, new java.sql.Date(advert.getProjectStart().getTime()));
		pst.setDate(4, new java.sql.Date(advert.getExpiryDate().getTime()));
		pst.setString(5, advert.getInstitute());
		pst.setString(6, advert.getProjectType());
		pst.setString(7, advert.getTargetedToSaveinDB());
		pst.setString(8, advert.getDescription());
		pst.setString(9, advert.getFollowersVisible());
		pst.setBoolean(10, advert.isQuest());
		pst.executeUpdate();

		if (!existsUserByEmail(advert.getEmailToInitiator()))
		{
			user = new User (advert.getAcademicTitle(),
					advert.getForename(), advert.getSurname(),
					advert.getEmailToInitiator(), UUID.randomUUID().toString());
			insertUser(user);
		} 
		else 
		{
			user = getUserByEmail(advert.getEmailToInitiator());
		}

		SQLQuery = "INSERT INTO \"initiator_table\" " +
				"(\"userId\", \"adId\", \"adCreated\") " +
				"VALUES (?, ?, ?)";
		
		pst = DBConnection.getConnection().prepareStatement(SQLQuery);
		pst.setString(1, user.getUserId());
		pst.setString(2, advert.getAdId());
		pst.setDate(3, new java.sql.Date(advert.getAdCreatedDate().getTime()));
		pst.executeUpdate();

		for (Follower itFollower : advert.getFollowers()) 
		{
			if (!existsUserByEmail(itFollower.getEmail())) 
			{
				insertUser(itFollower);
			}
			SQLQuery = "INSERT INTO \"follower_table\" VALUES (?, ?, ?, ?)";
			pst = DBConnection.getConnection().prepareStatement(SQLQuery);
			pst.setString(1, itFollower.getUserId());
			pst.setString(2, advert.getAdId());
			pst.setBoolean(3, itFollower.isVisible());
			pst.setString(4, itFollower.getComment());
			pst.executeUpdate();
		}
		pst.close();
		return user.getUserId();
	}
	
	/**
	 * Ads a Follower with ID of the advert to Database
	 * @param follower
	 * @param adId
	 * @throws Exception
	 */
	// TODO: geworfene Exception verfeinern, dies ist zu ungenau
	public static void addFollower(Follower follower, String adId)
			throws Exception 
	{

		if (!existsUserByEmail(follower.getEmail())) 
		{
			insertUser(follower);
		}
		
		if (existsFollower(follower.getUserId(), adId)) 
		{
			throw new Exception("Follower existiert schon.");
		} 
		else
		{

			String SQLQuery = "INSERT INTO \"follower_table\" " +
					"(\"userId\", \"adId\", \"visible\", \"description\") " +
					"VALUES (?, ?, false, ?);";
			
			PreparedStatement pst = DBConnection.getConnection().prepareStatement(SQLQuery);
			pst.setString(1, follower.getUserId());
			pst.setString(2, adId);
			pst.setString(3, follower.getComment());
			pst.executeUpdate();

			pst.close();
		}

	}
	
	/**
	 * Set a follower as an active member of the given adId
	 * @param userId
	 * @param adId
	 * @throws Exception
	 */
	// TODO: geworfene Exception verfeinern, dies ist zu ungenau
	public static void setFollowerActive(String userId, String adId)
			throws Exception 
	{
		
		if (!existsFollower(userId, adId)) 
		{
			throw new Exception("Follower existiert nicht.");
		} 
		else
		{

			String SQLQuery = "UPDATE \"follower_table\" " +
							  "SET \"visible\" = true " +
							  "WHERE \"userId\"= ? " +
							  "AND \"adId\" = ? ;";		
			
			PreparedStatement pst = DBConnection.getConnection().prepareStatement(SQLQuery);
			pst.setString(1, userId);
			pst.setString(1, adId);
			pst.executeUpdate();
			pst.close();
		}

	}
	
	/**
	 * Activates an Ad
	 * @param id
	 * @throws SQLException
	 */
	public static void activateAd(String adId) throws SQLException 
	{

		String SQLQuery = "UPDATE \"advertisement_table\" SET \"visible\" = True WHERE \"adId\" = ?;";
		PreparedStatement pst = DBConnection.getConnection().prepareStatement(SQLQuery);
		pst.setString(1, adId);
		
		pst.executeUpdate();
		pst.close();
	}
	
	/**
	 * Activates an Follower
	 * @param userId
	 * @param adId
	 * @throws SQLException
	 */
	public static void activateFollower(String userId, String adId) throws SQLException 
	{
		
		String SQLQuery = "UPDATE \"follower_table\" SET \"visible\" = True WHERE \"userId\" = ? AND \"adId\" = ?;";
		PreparedStatement pst = DBConnection.getConnection().prepareStatement(SQLQuery);
		pst.setString(1, userId);
		pst.setString(2, adId);
		
		pst.executeUpdate();
		pst.close();
	}
	
	/**
	 * Archived an Ad
	 * @param id
	 * @throws SQLException
	 */
	public static void archivedAd(String id) throws SQLException 
	{

		String SQLQuery = "UPDATE \"advertisement_table\" SET \"archivedDate\" = ? WHERE \"adId\" = ?;";
		PreparedStatement pst = DBConnection.getConnection().prepareStatement(SQLQuery);
		pst.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
		pst.setString(2, id);
		
		pst.executeUpdate();
		pst.close();
	}
	
	/**
	 * Update the password of an user
	 * @param oldPassword
	 * @param newPassword
	 * @throws SQLException
	 */
	public static void changePassword(String oldPassword, String newPassword)
			throws SQLException
	{
		String SQLQuery = "Update \"user_table\" SET \"password\" = ? " +
				"WHERE \"password\" = ?";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, newPassword);
		pst.setString(2, oldPassword);
		pst.executeUpdate();
		pst.close();
	}
	
	/**
	 * Deletes a given Advert out of Database. Deletes by id.
	 * @param advert
	 * @throws SQLException
	 */
	public static void deleteAd(String adId) throws SQLException 
	{
		String SQLQuery = "DELETE FROM \"follower_table\" WHERE \"adId\" = ?";
		PreparedStatement pst = DBConnection.getConnection().prepareStatement(SQLQuery);
		pst.setString(1, adId);
		pst.executeUpdate();
		pst.close();
		
		SQLQuery = "DELETE FROM \"initiator_table\" WHERE \"adId\" = ?";
		pst = DBConnection.getConnection().prepareStatement(SQLQuery);
		pst.setString(1, adId);
		pst.executeUpdate();
		pst.close();

		SQLQuery = "DELETE FROM \"advertisement_table\" WHERE \"adId\" = ?";
		pst = DBConnection.getConnection().prepareStatement(SQLQuery);
		pst.setString(1, adId);
		pst.executeUpdate();
		pst.close();
	}
	
	/**
	 * Deletes a followerdata of the database
	 * @param userId
	 * @param adId
	 * @throws SQLException
	 */
	public static void deleteFollower(String userId, String adId) throws  SQLException 
	{

		String SQLQuery = "DELETE FROM \"follower_table\"" +
				"WHERE \"userId\" = ? AND \"adId\" = ?";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, userId);
		pst.setString(2, adId);
		pst.executeUpdate();
		pst.close();
	}
	
	/**
	 * Verifies the existence of a follower
	 * @param userId
	 * @param adId
	 * @return
	 * @throws SQLException
	 */
	public static boolean existsFollower(String userId, String adId) throws SQLException
	{
		ResultSet res = null;
		String SQLQuery = "SELECT COUNT(*) FROM \"follower_table\" " +
				"WHERE \"userId\" = ? AND \"adId\" = ?";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, userId);
		pst.setString(2, adId);
		res = pst.executeQuery();
		res.next();
		
		if (res.getInt(1) > 0)
		{
			res.close();
			pst.close();
			return true;
		}
		else 
		{
			res.close();
			pst.close();
			return false;
		}
	}
	
	public static boolean existsActivFollower (String userId, String adId) throws SQLException
	{
		ResultSet res = null;
		String SQLQuery = "SELECT COUNT(*) FROM \"follower_table\" " +
				"WHERE \"userId\" = ? AND \"adId\" = ? AND \"visible\" = True";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, userId);
		pst.setString(2, adId);
		res = pst.executeQuery();
		res.next();
		
		if (res.getInt(1) > 0)
		{
			res.close();
			pst.close();
			return true;
		}
		else 
		{
			res.close();
			pst.close();
			return false;
		}
	}
	
	/**
	 * Gets follower by an id
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public static Follower getFollower(String userId) throws SQLException
	{
		ResultSet res = null;

		String SQLQuery = "SELECT \"u\".\"academicTitle\", " +
				"\"u\".\"forename\", " +
				"\"u\".\"surname\", " +
				"\"u\".\"email\", " +
				"\"u\".\"userId\", " +
				"\"f\".\"description\", " +
				"\"f\".\"visible\" " +
				"FROM \"follower_table\" \"f\", " +
				"\"user_table\" \"u\"" +
				"WHERE \"u\".\"userId\"= ? " +
				"AND \"f\".\"userId\" = \"u\".\"userId\"";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, userId);
		res = pst.executeQuery();

		if (res.next()) 
		{
			Follower follower = new Follower(res.getString("academicTitle"),
					res.getString("forename"), res.getString("surname"),
					res.getString("email"), res.getString("description"),
					res.getBoolean("visible"), res.getString("userId"));
			res.close();
			pst.close();
			return follower;
		} 
		else
		{
			res.close();
			pst.close();
			return null;
		}

	}
	
	/*???  Funktioniert nicht. Warum??? */
	public static Follower getFollower (String adId, String userId) throws SQLException
	{
		ResultSet res = null;
		
		String SQLQuery = "SELECT \"u\".\"academicTitle\", " +
				"\"u\".\"forename\", " +
				"\"u\".\"surname\", " +
				"\"u\".\"email\", " +
				"\"u\".\"userId\", " +
				"\"f\".\"description\", " +
				"\"f\".\"visible\" " +
				"FROM \"follower_table\" \"f\", " +
				"\"user_table\" \"u\"" +
				"WHERE \"u\".\"userId\"= ? " +
				"AND \"f\".\"adId\"= ?" +
				"AND \"f\".\"userId\" = \"u\".\"userId\"";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, userId);
		pst.setString(2, adId);
		res = pst.executeQuery();
		//System.out.println("Ich war mal hier!");
		
		if (res.next()) 
		{
			//System.out.println("Ein Follower wurde in der Datenbank gefunden!");
			Follower follower = new Follower(res.getString("academicTitle"),
					res.getString("forename"), res.getString("surname"),
					res.getString("email"), res.getString("description"),
					res.getBoolean("visible"), res.getString("userId"));
			res.close();
			pst.close();
			return follower;
		} 
		else
		{
			res.close();
			pst.close();
			return null;
		}
	}
	
	/**
	 * Verifies the existence of a user based on the email
	 * @param eMail
	 * @return true, if user exists, else false
	 * @throws SQLException
	 */
	public static boolean existsUserByEmail(String eMail)
			throws SQLException
	{
		ResultSet res = null;
		String SQLQuery = "SELECT COUNT(*) FROM \"user_table\" " +
				"WHERE \"email\" = ?";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, eMail);
		res = pst.executeQuery();
		res.next();
		
		if (res.getInt(1) > 0) 
		{
			res.close();
			pst.close();
			return true;
		} 
		else 
		{
			res.close();
			pst.close();
			return false;
		}
	}
	
	/**
	 * Verifies the existence of a user based on the password
	 * @param password
	 * @return true, if user exist, else false
	 * @throws SQLException
	 */
	public static boolean existsUserByPassword(String password)
			throws SQLException 
	{
		ResultSet res = null;
		String SQLQuery = "SELECT COUNT(*) FROM \"user_table\" " +
				"WHERE \"password\" = ?";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, password);
		res = pst.executeQuery();
		res.next();
		
		if (res.getInt(1) > 0) 
		{
			res.close();
			pst.close();
			return true;
		} 
		else 
		{
			res.close();
			pst.close();
			return false;
		}
	}
	
	public static boolean existsUserByUserId(String userId)
			throws SQLException 
	{
		ResultSet res = null;
		String SQLQuery = "SELECT COUNT(*) FROM \"user_table\" " +
				"WHERE \"userId\" = ?";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, userId);
		res = pst.executeQuery();
		res.next();
		
		if (res.getInt(1) > 0) 
		{
			res.close();
			pst.close();
			return true;
		} 
		else 
		{
			res.close();
			pst.close();
			return false;
		}
	}
	
	
	/**
	 * Gets the Ad with a given id from Database and followers that are
	 * visible=true
	 * @param adId
	 * @return
	 * @throws SQLException
	 */
	public static Ad getActivAd(String adId) throws SQLException
	{
		ResultSet res = null;
		ResultSet res2 = null;
		Ad hilfsad = new Ad();

		String SQLQuery = "SELECT \"title\", " +
				"\"projectedStartDate\", " +
				"\"expiryDate\", " +
				"\"institute\", " +
				"\"projectType\", " +
				"\"targetedAudience\", " +
				"\"description\", " +
				"\"followersVisible\", " +
				"\"archivedDate\", " +
				"\"Search\", " +
				"\"visible\", " +
				"\"academicTitle\", " +
				"\"forename\", " +
				"\"surname\", " +
				"\"email\", " +
				"\"adCreated\", " +
				"\"adId\", " +
				"\"userId\" " +
				"FROM \"advertisement_table\" \"a\", " +
				"\"user_table\" \"u\", " +
				"\"initiator_table\" \"i\" " +
				"WHERE \"visible\" = True " +
				"AND \"u\".\"userId\" = \"i\".\"userId\"" +
				"AND \"a\".\"adId\" = \"i\".\"adId\" " +
				"AND \"a\".\"adId\" = ?;";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, adId);
		res = pst.executeQuery();

		if (res.next() == false) 
		{
			return null;
		} 
		else 
		{
			hilfsad = new Ad(res.getString("title"),
					res.getString("academicTitle"), res.getString("forename"),
					res.getString("surname"), res.getString("email"),
					res.getDate("projectedStartDate"), res.getDate("expiryDate"),
					res.getDate("adCreated"), res.getString("institute"),
					res.getString("projectType"), res.getString("targetedAudience"),
					res.getString("description"), res.getString("adId"),
					res.getString("userId"),
					res.getString("followersVisible"),
					res.getDate("archivedDate"), res.getBoolean("Search"),
					res.getBoolean("visible"),
					new LinkedList<model.Follower>());
			
			res.close();
			Follower hilfsfollower = new Follower();

			SQLQuery = "SELECT \"u\".\"userId\", " +
					"\"academicTitle\", " +
					"\"forename\", " +
					"\"surname\", " +
					"\"email\", " +
					"\"description\", " +
					"\"visible\" " +
					"FROM \"follower_table\" \"f\", " +
					"\"user_table\" \"u\" " +
					"WHERE \"adId\" = ? " +
					"AND \"visible\" = True " +
					"AND \"u\".\"userId\" = \"f\".\"userId\";";
			pst = DBConnection.getConnection().prepareStatement(SQLQuery);
			pst.setString(1, adId);

			res2 = pst.executeQuery();

			while (res2.next()) 
			{
				hilfsfollower = new Follower(res2.getString("academicTitle"),
						res2.getString("forename"),	res2.getString("surname"),
						res2.getString("email"), res2.getString("description"),
						res2.getBoolean("visible"), res2.getString("userId"));
				hilfsad.addFollower(hilfsfollower);
			}
		}
		res2.close();
		pst.close();
		return hilfsad;
	}
	
	public static Ad getAd(String adId) throws SQLException
	{
		ResultSet res = null;
		ResultSet res2 = null;
		Ad hilfsad = new Ad();

		String SQLQuery = "SELECT \"title\", " +
				"\"projectedStartDate\", " +
				"\"expiryDate\", " +
				"\"institute\", " +
				"\"projectType\", " +
				"\"targetedAudience\", " +
				"\"description\", " +
				"\"followersVisible\", " +
				"\"archivedDate\", " +
				"\"Search\", " +
				"\"visible\", " +
				"\"academicTitle\", " +
				"\"forename\", " +
				"\"surname\", " +
				"\"email\", " +
				"\"adCreated\", " +
				"\"adId\", " +
				"\"userId\" " +
				"FROM \"advertisement_table\" \"a\", " +
				"\"user_table\" \"u\", " +
				"\"initiator_table\" \"i\" " +
				"WHERE \"u\".\"userId\" = \"i\".\"userId\"" +
				"AND \"a\".\"adId\" = \"i\".\"adId\" " +
				"AND \"a\".\"adId\" = ?;";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, adId);
		res = pst.executeQuery();

		if (res.next() == false) 
		{
			return null;
		} 
		else 
		{
			hilfsad = new Ad(res.getString("title"),
							 res.getString("academicTitle"), 
							 res.getString("forename"),
							 res.getString("surname"), 
							 res.getString("email"),
					 		 res.getDate("projectedStartDate"), 
					 		 res.getDate("expiryDate"),
							 res.getDate("adCreated"), 
							 res.getString("institute"),
							 res.getString("projectType"), 
							 res.getString("targetedAudience"),
							 res.getString("description"), 
							 res.getString("adId"),
							 res.getString("userId"),
							 res.getString("followersVisible"),
							 res.getDate("archivedDate"), 
							 res.getBoolean("Search"),
							 res.getBoolean("visible"),
							 new LinkedList<model.Follower>());
			
			res.close();
			Follower hilfsfollower = new Follower();

			SQLQuery = "SELECT \"u\".\"userId\", " +
					"\"academicTitle\", " +
					"\"forename\", " +
					"\"surname\", " +
					"\"email\", " +
					"\"description\", " +
					"\"visible\" " +
					"FROM \"follower_table\" \"f\", " +
					"\"user_table\" \"u\" " +
					"WHERE \"adId\" = ? " +
					"AND \"visible\" = True " +
					"AND \"u\".\"userId\" = \"f\".\"userId\";";
			pst = DBConnection.getConnection().prepareStatement(SQLQuery);
			pst.setString(1, adId);

			res2 = pst.executeQuery();

			while (res2.next()) 
			{
				hilfsfollower = new Follower(res2.getString("academicTitle"),
						res2.getString("forename"),	res2.getString("surname"),
						res2.getString("email"), res2.getString("description"),
						res2.getBoolean("visible"), res2.getString("userId"));
				hilfsad.addFollower(hilfsfollower);
			}
		}
		res2.close();
		pst.close();
		return hilfsad;
	}
	
	public static Ad getOwnAd(String adId) throws SQLException
	{
		ResultSet res = null;
		ResultSet res2 = null;
		Ad hilfsad = new Ad();

		String SQLQuery = "SELECT \"title\", " +
				"\"projectedStartDate\", " +
				"\"expiryDate\", " +
				"\"institute\", " +
				"\"projectType\", " +
				"\"targetedAudience\", " +
				"\"description\", " +
				"\"followersVisible\", " +
				"\"archivedDate\", " +
				"\"Search\", " +
				"\"visible\", " +
				"\"academicTitle\", " +
				"\"forename\", " +
				"\"surname\", " +
				"\"email\", " +
				"\"adCreated\", " +
				"\"adId\", " +
				"\"userId\" " +
				"FROM \"advertisement_table\" \"a\", " +
				"\"user_table\" \"u\", " +
				"\"initiator_table\" \"i\" " +
				"WHERE  \"u\".\"userId\" = \"i\".\"userId\"" +
				"AND \"a\".\"adId\" = \"i\".\"adId\" " +
				"AND \"a\".\"adId\" = ?;";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, adId);
		res = pst.executeQuery();

		if (res.next() == false) 
		{
			return null;
		} 
		else 
		{
			hilfsad = new Ad(res.getString("title"),
					res.getString("academicTitle"), 
					res.getString("forename"),
					res.getString("surname"), 
					res.getString("email"),
					res.getDate("projectedStartDate"), 
					res.getDate("expiryDate"),
					res.getDate("adCreated"), 
					res.getString("institute"),
					res.getString("projectType"), 
					res.getString("targetedAudience"),
					res.getString("description"), 
					res.getString("adId"),
					res.getString("userId"),
					res.getString("followersVisible"),
					res.getDate("archivedDate"), 
					res.getBoolean("Search"),
					res.getBoolean("visible"),
					new LinkedList<model.Follower>());
			
			res.close();
			Follower hilfsfollower = new Follower();

			SQLQuery = "SELECT \"u\".\"userId\", " +
					"\"academicTitle\", " +
					"\"forename\", " +
					"\"surname\", " +
					"\"email\", " +
					"\"description\", " +
					"\"visible\" " +
					"FROM \"follower_table\" \"f\", " +
					"\"user_table\" \"u\" " +
					"WHERE \"adId\" = ? " +
					"AND \"visible\" = True " +
					"AND \"u\".\"userId\" = \"f\".\"userId\";";
			pst = DBConnection.getConnection().prepareStatement(SQLQuery);
			pst.setString(1, adId);

			res2 = pst.executeQuery();

			while (res2.next()) 
			{
				hilfsfollower = new Follower(res2.getString("academicTitle"),
						res2.getString("forename"),	res2.getString("surname"),
						res2.getString("email"), res2.getString("description"),
						res2.getBoolean("visible"), res2.getString("userId"));
				hilfsad.addFollower(hilfsfollower);
			}
		}
		res2.close();
		pst.close();
		return hilfsad;
	}
	/**
	 * Mostly copied from getAds ()
	 * Gets all Ads within range min..max
	 * @param min >= 1
	 * @param max >= min
	 * @return all ads within the range of min .. max
	 * @throws SQLException
	 */
	public static java.util.LinkedList<Ad> getAds(int min, int max) throws SQLException
	{
		ResultSet res = null;
//		ResultSet res2 = null;
		java.util.LinkedList<Ad> ads = new java.util.LinkedList<Ad>();
		Ad hilfsad = new Ad();

		//  Check range of min & max
		if (min < 1)
			min = 1;
		if (max < min)
			max = min;
		
		String SQLQuery = "SELECT \"title\", " +
				"\"projectedStartDate\", " +
				"\"expiryDate\", " +
				"\"institute\", " +
				"\"projectType\", " +
				"\"targetedAudience\", " +
				"\"description\", " +
				"\"followersVisible\", " +
				"\"archivedDate\", " +
				"\"Search\", " +
				"\"visible\", " +
				"\"academicTitle\", " +
				"\"forename\", " +
				"\"surname\", " +
				"\"email\", " +
				"\"adCreated\", " +
				"\"adId\", " +
				"\"userId\" " +
				"FROM \"advertisement_table\" \"a\", " +
				"\"user_table\" \"u\", " +
				"\"initiator_table\" \"i\" " +
				"WHERE \"visible\" = True " +
				"AND \"archivedDate\" IS NULL " +
				"AND \"u\".\"userId\" = \"i\".\"userId\"" +
				"AND \"a\".\"adId\" = \"i\".\"adId\" " +
				"ORDER BY \"adCreated\" DESC " +
				"LIMIT "+ (max - min + 1) + " " +
				"OFFSET " + (min-1) + ";";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		res = pst.executeQuery();
		
		while (res.next()) 
		{
			hilfsad = new Ad(res.getString("title"),
					res.getString("academictitle"), res.getString("forename"),
					res.getString("surname"), res.getString("email"),
					res.getDate("projectedStartDate"), res.getDate("expiryDate"),
					res.getDate("adCreated"), res.getString("institute"),
					res.getString("projectType"), res.getString("targetedAudience"),
					res.getString("description"), res.getString("adId"),
					res.getString("userId"),
					res.getString("followersVisible"),
					res.getDate("archivedDate"), res.getBoolean("Search"),
					res.getBoolean("visible"),
					new LinkedList<model.Follower>());
			ads.add(hilfsad);
		}
		
		res.close();
		pst.close();

		return ads;
	}
	
	/**
	 * Mostly copied from getAds ()
	 * 
	 * This function is able to filter the range and some more attributes (== constraints)
	 * Selecting multiple institutes or projectTypes are possible
	 * Structure: WHERE ... AND (institut1 or ... or institutN) AND (projectTyp1 or ... or projectTypM)
	 * 
	 * @param min >= 1
	 * @param max >= min
	 * @param constraints to filter more accurately
	 * @return
	 * @throws SQLException
	 */
	public static java.util.LinkedList<Ad> getAds(int min, int max, String[] constraints) throws SQLException
	{
		ResultSet res = null;
//		ResultSet res2 = null;
		java.util.LinkedList<Ad> ads = new java.util.LinkedList<Ad>();
		Ad hilfsad = new Ad();

		//  Check range of min & max
		if (min < 1)
			min = 1;
		if (max < min)
			max = min;
		
		
		// generate constraint instructions
		
		// this is standard
		String where = "WHERE (\"visible\" = True " +
				"AND \"archivedDate\" IS NULL " +
				"AND \"u\".\"userId\" = \"i\".\"userId\" " +
				"AND \"a\".\"adId\" = \"i\".\"adId\") ";
		
		//  get the rest
		String [] institutes = Settings.getProperty("institutes");
		String [] projectTypes = Settings.getProperty ("types");
		
		
		where += "inst_replacement proj_replacement";
		
		
		String instit = "";
		String projectT = "";
		
		for (int i = 0; i < constraints.length; i++)
		{
			boolean found = false;
			int j = 0;
			//  Check if current element is an institute
			while (j < institutes.length && !found)
			{
				if (institutes[j].equals(constraints[i]))
				{
					found = true;
					if (!instit.equals(""))
						instit += "OR ";
					else 
						instit += "AND (";
					instit += "\"a\".\"institute\" = '" + constraints[i] + "' ";
				}
				j++;
			}		
			
			//  check if current element is an projectTyp
			j = 0;
			while (j < projectTypes.length && !found)
			{
				if (projectTypes [j].equals(constraints[i]))
				{
					found = true;
					if (!projectT.equals(""))
						projectT += "OR ";
					else
						projectT += "AND (";
					
					projectT += "\"a\".\"projectType\" = '" + constraints[i] + "' ";
				}
				j++;
			}
		}
		if (!instit.equals(""))
			instit += ")";
		if (!projectT.equals(""))
			projectT += ")";
		
		where = where.replaceAll("(inst_replacement)", instit);
		where = where.replaceAll("(proj_replacement)", projectT);
		
		String SQLQuery = "SELECT \"title\", " +
				"\"projectedStartDate\", " +
				"\"expiryDate\", " +
				"\"institute\", " +
				"\"projectType\", " +
				"\"targetedAudience\", " +
				"\"description\", " +
				"\"followersVisible\", " +
				"\"archivedDate\", " +
				"\"Search\", " +
				"\"visible\", " +
				"\"academicTitle\", " +
				"\"forename\", " +
				"\"surname\", " +
				"\"email\", " +
				"\"adCreated\", " +
				"\"adId\", " +
				"\"userId\" " +
				"FROM \"advertisement_table\" \"a\", " +
				"\"user_table\" \"u\", " +
				"\"initiator_table\" \"i\" " +
				where	+
				"ORDER BY \"adCreated\" DESC " +
				"LIMIT "+ (max - min + 1) + " " +
				"OFFSET " + (min-1) + ";";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		res = pst.executeQuery();
		
		while (res.next()) 
		{
			hilfsad = new Ad(res.getString("title"),
					res.getString("academictitle"), res.getString("forename"),
					res.getString("surname"), res.getString("email"),
					res.getDate("projectedStartDate"), res.getDate("expiryDate"),
					res.getDate("adCreated"), res.getString("institute"),
					res.getString("projectType"), res.getString("targetedAudience"),
					res.getString("description"), res.getString("adId"),
					res.getString("userId"),
					res.getString("followersVisible"),
					res.getDate("archivedDate"), res.getBoolean("Search"),
					res.getBoolean("visible"),
					new LinkedList<model.Follower>());
			ads.add(hilfsad);
		}
		
		res.close();
		pst.close();

		return ads;
	}
	
	/**
	 * Gets List of all non archived Ads from Database
	 * @return
	 * @throws SQLException
	 */
	public static java.util.LinkedList<Ad> getAds() throws SQLException
	{
		ResultSet res = null;
//		ResultSet res2 = null;
		java.util.LinkedList<Ad> ads = new java.util.LinkedList<Ad>();
		Ad hilfsad = new Ad();

		String SQLQuery = "SELECT \"title\", " +
				"\"projectedStartDate\", " +
				"\"expiryDate\", " +
				"\"institute\", " +
				"\"projectType\", " +
				"\"targetedAudience\", " +
				"\"description\", " +
				"\"followersVisible\", " +
				"\"archivedDate\", " +
				"\"Search\", " +
				"\"visible\", " +
				"\"academicTitle\", " +
				"\"forename\", " +
				"\"surname\", " +
				"\"email\", " +
				"\"adCreated\", " +
				"\"adId\", " +
				"\"userId\" " +
				"FROM \"advertisement_table\" \"a\", " +
				"\"user_table\" \"u\", " +
				"\"initiator_table\" \"i\" " +
				"WHERE \"visible\" = True " +
				"AND \"archivedDate\" IS NULL " +
				"AND \"u\".\"userId\" = \"i\".\"userId\"" +
				"AND \"a\".\"adId\" = \"i\".\"adId\" " +
				"ORDER BY \"adCreated\" DESC;";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		res = pst.executeQuery();
		
		while (res.next()) 
		{
			hilfsad = new Ad(
					res.getString("title"),
					res.getString("academictitle"), 
					res.getString("forename"),
					res.getString("surname"), 
					res.getString("email"),
					res.getDate("projectedStartDate"), 
					res.getDate("expiryDate"),
					res.getDate("adCreated"), 
					res.getString("institute"),
					res.getString("projectType"), 
					res.getString("targetedAudience"),
					res.getString("description"), 
					res.getString("adId"),
					res.getString("userId"),
					res.getString("followersVisible"),
					res.getDate("archivedDate"), 
					res.getBoolean("Search"),
					res.getBoolean("visible"),
					new LinkedList<model.Follower>());
			ads.add(hilfsad);
		}
		
		res.close();
		pst.close();

		return ads;
	}
	
	public static java.util.LinkedList<Ad> getAds(String[] constraints) throws SQLException
	{
		ResultSet res = null;
//		ResultSet res2 = null;
		java.util.LinkedList<Ad> ads = new java.util.LinkedList<Ad>();
		Ad hilfsad = new Ad();
		
		// generate constraint instructions
		
		// this is standard
		String where = "WHERE (\"visible\" = True " +
				"AND \"archivedDate\" IS NULL " +
				"AND \"u\".\"userId\" = \"i\".\"userId\" " +
				"AND \"a\".\"adId\" = \"i\".\"adId\") ";
		
		//  get the rest
		String [] institutes = Settings.getProperty("institutes");
		String [] projectTypes = Settings.getProperty ("types");
		
		
		where += "inst_replacement proj_replacement";
		
		
		String instit = "";
		String projectT = "";
		
		for (int i = 0; i < constraints.length; i++)
		{
			boolean found = false;
			int j = 0;
			//  Check if current element is an institute
			while (j < institutes.length && !found)
			{
				if (institutes[j].equals(constraints[i]))
				{
					found = true;
					if (!instit.equals(""))
						instit += "OR ";
					else 
						instit += "AND (";
					instit += "\"a\".\"institute\" = '" + constraints[i] + "' ";
				}
				j++;
			}		
			
			//  check if current element is an projectTyp
			j = 0;
			while (j < projectTypes.length && !found)
			{
				if (projectTypes [j].equals(constraints[i]))
				{
					found = true;
					if (!projectT.equals(""))
						projectT += "OR ";
					else
						projectT += "AND (";
					
					projectT += "\"a\".\"projectType\" = '" + constraints[i] + "' ";
				}
				j++;
			}
		}
		if (!instit.equals(""))
			instit += ")";
		if (!projectT.equals(""))
			projectT += ")";
		
		where = where.replaceAll("(inst_replacement)", instit);
		where = where.replaceAll("(proj_replacement)", projectT);
		
		String SQLQuery = "SELECT \"title\", " +
				"\"projectedStartDate\", " +
				"\"expiryDate\", " +
				"\"institute\", " +
				"\"projectType\", " +
				"\"targetedAudience\", " +
				"\"description\", " +
				"\"followersVisible\", " +
				"\"archivedDate\", " +
				"\"Search\", " +
				"\"visible\", " +
				"\"academicTitle\", " +
				"\"forename\", " +
				"\"surname\", " +
				"\"email\", " +
				"\"adCreated\", " +
				"\"adId\", " +
				"\"userId\" " +
				"FROM \"advertisement_table\" \"a\", " +
				"\"user_table\" \"u\", " +
				"\"initiator_table\" \"i\" " +
				where	+
				"ORDER BY \"adCreated\" DESC " + ";";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		res = pst.executeQuery();
		
		while (res.next()) 
		{
			hilfsad = new Ad(res.getString("title"),
					res.getString("academictitle"), res.getString("forename"),
					res.getString("surname"), res.getString("email"),
					res.getDate("projectedStartDate"), res.getDate("expiryDate"),
					res.getDate("adCreated"), res.getString("institute"),
					res.getString("projectType"), res.getString("targetedAudience"),
					res.getString("description"), res.getString("adId"),
					res.getString("userId"),
					res.getString("followersVisible"),
					res.getDate("archivedDate"), res.getBoolean("Search"),
					res.getBoolean("visible"),
					new LinkedList<model.Follower>());
			ads.add(hilfsad);
		}
		
		res.close();
		pst.close();

		return ads;
	}
	
	/**
	 * Gets List of all non archived Ads from Database where the user follows
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static java.util.LinkedList<Ad> getFollows(String password) throws SQLException
	{
		ResultSet resAd = null;
		java.util.LinkedList<Ad> ads = new java.util.LinkedList<Ad>();
		Ad hilfsad = new Ad();
		
		String SQLQuery = "SELECT \"a\".\"title\", " +
				"\"a\".\"projectedStartDate\", " +
				"\"a\".\"expiryDate\", " +
				"\"a\".\"institute\", " +
				"\"a\".\"projectType\", " +
				"\"a\".\"targetedAudience\", " +
				"\"a\".\"description\", " +
				"\"a\".\"followersVisible\", " +
				"\"a\".\"archivedDate\", " +
				"\"a\".\"Search\", " +
				"\"a\".\"visible\", " +
				"\"u1\".\"academicTitle\", " +
				"\"u1\".\"forename\", " +
				"\"u1\".\"surname\", " +
				"\"u1\".\"email\", " +
				"\"i\".\"adCreated\", " +
				"\"i\".\"adId\", " +
				"\"i\".\"userId\" " +
				"FROM \"advertisement_table\" \"a\", " +
				"\"user_table\" \"u1\", " +
				"\"user_table\" \"u2\", " +
				"\"initiator_table\" \"i\", " +
				"\"follower_table\" \"f\" " +
				"WHERE \"visible\" = True " +
				"AND \"archivedDate\" IS NULL " +
				"AND \"u1\".\"userId\" = \"i\".\"userId\"" +
				"AND \"a\".\"adId\" = \"i\".\"adId\" " +
				"AND \"u2\".\"password\" = ? " +
				"AND \"u2\".\"userId\" = \"f\".\"userId\" " +
				"AND \"a\".\"adId\" = \"f\".\"adId\" " +
				"ORDER BY \"i\".\"adCreated\" DESC;";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, password);
		resAd = pst.executeQuery();

		while (resAd.next())
		{
			hilfsad = new Ad(resAd.getString("title"),
					resAd.getString("academictitle"), resAd.getString("forename"),
					resAd.getString("surname"), resAd.getString("email"),
					resAd.getDate("projectedStartDate"), resAd.getDate("expiryDate"),
					resAd.getDate("adCreated"), resAd.getString("institute"),
					resAd.getString("projectType"), resAd.getString("targetedAudience"),
					resAd.getString("description"), resAd.getString("adId"),
					resAd.getString("userId"),
					resAd.getString("followersVisible"),
					resAd.getDate("archivedDate"), resAd.getBoolean("Search"),
					resAd.getBoolean("visible"),
					new LinkedList<model.Follower>());
			
			SQLQuery = "SELECT \"u\".\"userId\", " +
					"\"u\".\"academicTitle\", " +
					"\"u\".\"forename\", " +
					"\"u\".\"surname\", " +
					"\"u\".\"email\", " +
					"\"f\".\"description\", " +
					"\"f\".\"visible\" " +
					"FROM \"follower_table\" \"f\", " +
					"\"user_table\" \"u\"" +
					"WHERE \"f\".\"adId\" = ? " +
					"AND \"u\".\"password\" = ? " +
					"AND \"u\".\"userId\" = \"f\".\"userId\";";
			
			PreparedStatement pstFollower = DBConnection.getConnection().prepareStatement(SQLQuery);
			pstFollower.setString(1, hilfsad.getAdId());
			pstFollower.setString(2, password);
			ResultSet resFollower = pstFollower.executeQuery();
			
			while (resFollower.next()) 
			{
				hilfsad.addFollower(new Follower(resFollower.getString("academicTitle"),
						resFollower.getString("forename"),	resFollower.getString("surname"),
						resFollower.getString("email"), resFollower.getString("description"),
						resFollower.getBoolean("visible"), resFollower.getString("userId")));
			}
			resFollower.close();
			pstFollower.close();
			ads.add(hilfsad);
		}
		resAd.close();
		pst.close();
		return ads;
	}
	
	
	/**
	 * Gets all followers of an ad
	 * @param adId
	 * @return the userIds of the followers of a given ad
	 * @throws SQLException 
	 */
	public static String [] getFollowersOfAd (String adId) throws SQLException
	{
		LinkedList <String> temp = new LinkedList<String> ();
		ResultSet result = null;
		
		String SQLQuery = 	"SELECT \"f\".\"userId\" " +
							"FROM \"follower_table\" \"f\" " +
							"WHERE \"f\".\"adId\" = ? " +
							"AND \"f\".\"visible\" = true;";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, adId);
		result = pst.executeQuery();
		
		while (result.next())
		{
			temp.add(result.getString ("userId"));
		}
		
		return temp.toArray(new String [0]);
	}
	
	
	public static java.util.LinkedList<Ad> getAllFollowers(String userId) throws SQLException
	{
		ResultSet resAd = null;
		java.util.LinkedList<Ad> ads = new java.util.LinkedList<Ad>();
		Ad hilfsad = new Ad();
		
		String SQLQuery = "SELECT \"a\".\"title\", " +
				"\"a\".\"projectedStartDate\", " +
				"\"a\".\"expiryDate\", " +
				"\"a\".\"institute\", " +
				"\"a\".\"projectType\", " +
				"\"a\".\"targetedAudience\", " +
				"\"a\".\"description\", " +
				"\"a\".\"followersVisible\", " +
				"\"a\".\"archivedDate\", " +
				"\"a\".\"Search\", " +
				"\"a\".\"visible\", " +
				"\"u1\".\"academicTitle\", " +
				"\"u1\".\"forename\", " +
				"\"u1\".\"surname\", " +
				"\"u1\".\"email\", " +
				"\"i\".\"adCreated\", " +
				"\"i\".\"adId\", " +
				"\"i\".\"userId\" " +
				"FROM \"advertisement_table\" \"a\", " +
				"\"user_table\" \"u1\", " +
				"\"user_table\" \"u2\", " +
				"\"initiator_table\" \"i\", " +
				"\"follower_table\" \"f\" " +
				"WHERE \"archivedDate\" IS NULL " +
				"AND \"u1\".\"userId\" = \"i\".\"userId\"" +
				"AND \"a\".\"adId\" = \"i\".\"adId\" " +
				"AND \"u2\".\"userId\" = ? " +
				"AND \"u2\".\"userId\" = \"f\".\"userId\" " +
				"AND \"a\".\"adId\" = \"f\".\"adId\" " +
				"ORDER BY \"i\".\"adCreated\" DESC;";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, userId);
		resAd = pst.executeQuery();

		while (resAd.next())
		{
			hilfsad = new Ad(resAd.getString("title"),
					resAd.getString("academictitle"), resAd.getString("forename"),
					resAd.getString("surname"), resAd.getString("email"),
					resAd.getDate("projectedStartDate"), resAd.getDate("expiryDate"),
					resAd.getDate("adCreated"), resAd.getString("institute"),
					resAd.getString("projectType"), resAd.getString("targetedAudience"),
					resAd.getString("description"), resAd.getString("adId"),
					resAd.getString("userId"),
					resAd.getString("followersVisible"),
					resAd.getDate("archivedDate"), resAd.getBoolean("Search"),
					resAd.getBoolean("visible"),
					new LinkedList<model.Follower>());
			
			SQLQuery = "SELECT \"u\".\"userId\", " +
					"\"u\".\"academicTitle\", " +
					"\"u\".\"forename\", " +
					"\"u\".\"surname\", " +
					"\"u\".\"email\", " +
					"\"f\".\"description\", " +
					"\"f\".\"visible\" " +
					"FROM \"follower_table\" \"f\", " +
					"\"user_table\" \"u\"" +
					"WHERE \"f\".\"adId\" = ? " +
					"AND \"u\".\"userId\" = ? " +
					"AND \"u\".\"userId\" = \"f\".\"userId\";";
			
			PreparedStatement pstFollower = DBConnection.getConnection().prepareStatement(SQLQuery);
			pstFollower.setString(1, hilfsad.getAdId());
			pstFollower.setString(2, userId);
			ResultSet resFollower = pstFollower.executeQuery();
			
			while (resFollower.next()) 
			{
				hilfsad.addFollower(new Follower(resFollower.getString("academicTitle"),
						resFollower.getString("forename"),	resFollower.getString("surname"),
						resFollower.getString("email"), resFollower.getString("description"),
						resFollower.getBoolean("visible"), resFollower.getString("userId")));
			}
			resFollower.close();
			pstFollower.close();
			ads.add(hilfsad);
		}
		resAd.close();
		pst.close();
		return ads;
	}
	
	
	/**
	 * Gets List of all non archived Ads from Database relates on a specified user
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static java.util.LinkedList<Ad> getPrivateAds(String password) throws SQLException
	{
		ResultSet res = null;
//		ResultSet res2 = null;
		java.util.LinkedList<Ad> ads = new java.util.LinkedList<Ad>();
		Ad hilfsad = new Ad();

		String SQLQuery = "SELECT \"title\", " +
				"\"projectedStartDate\", " +
				"\"expiryDate\", " +
				"\"institute\", " +
				"\"projectType\", " +
				"\"targetedAudience\", " +
				"\"description\", " +
				"\"followersVisible\", " +
				"\"archivedDate\", " +
				"\"Search\", " +
				"\"visible\", " +
				"\"academicTitle\", " +
				"\"forename\", " +
				"\"surname\", " +
				"\"email\", " +
				"\"adCreated\", " +
				"\"adId\", " +
				"\"userId\" " +
				"FROM \"advertisement_table\" \"a\", " +
				"\"user_table\" \"u\", " +
				"\"initiator_table\" \"i\" " +
				"WHERE \"archivedDate\" IS NULL " +
				"AND \"u\".\"userId\" = \"i\".\"userId\"" +
				"AND \"a\".\"adId\" = \"i\".\"adId\" " +
				"AND \"u\".\"password\" = ? " +
				"ORDER BY \"adCreated\" DESC;";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, password);
		res = pst.executeQuery();
		
		while (res.next())
		{
			hilfsad = new Ad(res.getString("title"),
					res.getString("academictitle"), res.getString("forename"),
					res.getString("surname"), res.getString("email"),
					res.getDate("projectedStartDate"), res.getDate("expiryDate"),
					res.getDate("adCreated"), res.getString("institute"),
					res.getString("projectType"), res.getString("targetedAudience"),
					res.getString("description"), res.getString("adId"),
					res.getString("userId"),
					res.getString("followersVisible"),
					res.getDate("archivedDate"), res.getBoolean("Search"),
					res.getBoolean("visible"),
					new LinkedList<model.Follower>());
			ads.add(hilfsad);
		}
		
		res.close();
		pst.close();

		return ads;
	}
	
	public static java.util.LinkedList<Ad> getOwnAds(String userId) throws SQLException
	{
		ResultSet res = null;
//		ResultSet res2 = null;
		java.util.LinkedList<Ad> ads = new java.util.LinkedList<Ad>();
		Ad hilfsad = new Ad();

		String SQLQuery = "SELECT \"title\", " +
				"\"projectedStartDate\", " +
				"\"expiryDate\", " +
				"\"institute\", " +
				"\"projectType\", " +
				"\"targetedAudience\", " +
				"\"description\", " +
				"\"followersVisible\", " +
				"\"archivedDate\", " +
				"\"Search\", " +
				"\"visible\", " +
				"\"academicTitle\", " +
				"\"forename\", " +
				"\"surname\", " +
				"\"email\", " +
				"\"adCreated\", " +
				"\"adId\", " +
				"\"userId\" " +
				"FROM \"advertisement_table\" \"a\", " +
				"\"user_table\" \"u\", " +
				"\"initiator_table\" \"i\" " +
				"WHERE \"archivedDate\" IS NULL " +
				"AND \"u\".\"userId\" = \"i\".\"userId\"" +
				"AND \"a\".\"adId\" = \"i\".\"adId\" " +
				"AND \"u\".\"userId\" = ? " +
				"ORDER BY \"adCreated\" DESC;";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, userId);
		res = pst.executeQuery();
		
		while (res.next())
		{
			hilfsad = new Ad(res.getString("title"),
					res.getString("academictitle"), res.getString("forename"),
					res.getString("surname"), res.getString("email"),
					res.getDate("projectedStartDate"), res.getDate("expiryDate"),
					res.getDate("adCreated"), res.getString("institute"),
					res.getString("projectType"), res.getString("targetedAudience"),
					res.getString("description"), res.getString("adId"),
					res.getString("userId"),
					res.getString("followersVisible"),
					res.getDate("archivedDate"), res.getBoolean("Search"),
					res.getBoolean("visible"),
					new LinkedList<model.Follower>());
			ads.add(hilfsad);
		}
		
		res.close();
		pst.close();

		return ads;
	}
	
	/**
	 * Gets the password of an user
	 * @param userId
	 * @return password
	 * @throws SQLException
	 */
	public static String getPassword(String userId)
			throws SQLException
	{
		ResultSet res = null;
		String SQLQuery = "SELECT \"password\" FROM \"user_table\" " +
				"WHERE \"userId\" = ?";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, userId);
		res = pst.executeQuery();
		
		if (res.next()) 
		{
			return res.getString("password");
		} 
		else 
		{
			return null;
		}
	}
	
	/**
	 * Gets the Ad with a given id from Database and followers that are
	 * visible=true
	 * @param adId
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static Ad getPrivateAd(String adId, String password) throws SQLException
	{
		ResultSet res = null;
		ResultSet res2 = null;
		Ad hilfsad = new Ad();

		String SQLQuery = "SELECT \"title\", " +
				"\"projectedStartDate\", " +
				"\"expiryDate\", " +
				"\"institute\", " +
				"\"projectType\", " +
				"\"targetedAudience\", " +
				"\"description\", " +
				"\"followersVisible\", " +
				"\"archivedDate\", " +
				"\"Search\", " +
				"\"visible\", " +
				"\"academicTitle\", " +
				"\"forename\", " +
				"\"surname\", " +
				"\"email\", " +
				"\"adCreated\", " +
				"\"adId\", " +
				"\"userId\" " +
				"FROM \"advertisement_table\" \"a\", " +
				"\"user_table\" \"u\", " +
				"\"initiator_table\" \"i\" " +
				"WHERE \"u\".\"userId\" = \"i\".\"userId\" " +
				"AND \"a\".\"adId\" = \"i\".\"adId\" " +
				"AND \"a\".\"adId\" = ? " +
				"AND \"u\".\"password\" = ? ;";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, adId);
		pst.setString(2, password);
		res = pst.executeQuery();

		if (res.next() == false) 
		{
			return null;
		}
		else 
		{
			hilfsad = new Ad(res.getString("title"),
					res.getString("academicTitle"), res.getString("forename"),
					res.getString("surname"), res.getString("email"),
					res.getDate("projectedStartDate"), res.getDate("expiryDate"),
					res.getDate("adCreated"), res.getString("institute"),
					res.getString("projectType"), res.getString("targetedAudience"),
					res.getString("description"), res.getString("adId"),
					res.getString("userId"),
					res.getString("followersVisible"),
					res.getDate("archivedDate"), res.getBoolean("Search"),
					res.getBoolean("visible"),
					new LinkedList<model.Follower>());
			res.close();
			Follower hilfsfollower = new Follower();

			SQLQuery = "SELECT \"u\".\"userId\", " +
					"\"academicTitle\", " +
					"\"forename\", " +
					"\"surname\", " +
					"\"email\", " +
					"\"description\", " +
					"\"visible\" " +
					"FROM \"follower_table\" \"f\", " +
					"\"user_table\" \"u\" " +
					"WHERE \"adId\" = ? " +
					"AND \"visible\" = True " +
					"AND \"u\".\"userId\" = \"f\".\"userId\";";
			pst = DBConnection.getConnection().prepareStatement(SQLQuery);
			pst.setString(1, adId);

			res2 = pst.executeQuery();

			while (res2.next()) 
			{
				hilfsfollower = new Follower(res2.getString("academicTitle"),
						res2.getString("forename"),	res2.getString("surname"),
						res2.getString("email"), res2.getString("description"),
						res2.getBoolean("visible"), res2.getString("userId"));
				hilfsad.addFollower(hilfsfollower);
			}
		}
		res2.close();
		pst.close();
		return hilfsad;
	}
	
	
	/**
	 * Gets user by an eMail
	 * @param eMail
	 * @return user
	 * @throws SQLException
	 */
	public static User getUserByEmail(String eMail) throws SQLException
	{
		ResultSet res = null;

		String SQLQuery = "SELECT * FROM \"user_table\" WHERE \"email\"= ?";
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, eMail);
		res = pst.executeQuery();
		pst.close();

		if (res.next()) 
		{
			return new User(res.getString("academicTitle"),
					res.getString("forename"), res.getString("surname"),
					res.getString("email"), res.getString("userId"));
		}
		else 
		{
			return null;
		}
	}
	
	public static User getUserByUserId (String userId) throws SQLException
	{
		ResultSet res = null;

		String SQLQuery = "SELECT * FROM \"user_table\" WHERE \"userId\"= ?";
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, userId);
		res = pst.executeQuery();
		pst.close();

		if (res.next()) 
		{
			return new User(res.getString("academicTitle"),
					res.getString("forename"), res.getString("surname"),
					res.getString("email"), res.getString("userId"));
		}
		else 
		{
			return null;
		}
	}
	
	/**
	 *  Gets user by an password
	 * @param password
	 * @return user
	 * @throws SQLException
	 */
	public static User getUserByPassword(String password) throws SQLException
	{
		ResultSet res = null;

		String SQLQuery = "SELECT * FROM \"user_table\" WHERE \"password\"= ?";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		pst.setString(1, password);
		res = pst.executeQuery();
		pst.close();

		if (res.next()) 
		{
			return new User(res.getString("academicTitle"),
					res.getString("forename"), res.getString("surname"),
					res.getString("email"), res.getString("userId"));
		} 
		else
		{
			return null;
		}
	}
	
	/**
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static synchronized void initDatabase() throws SQLException,
			IOException, URISyntaxException 
	{
		// In case this is the first connection, create the initial DB schema:
		loadProperties();
		URL pathurl = DBAdministration.class.getResource(props
				.getProperty("initscript"));
		URI uri = new URI(pathurl.toString());
		String pathtoscript = uri.getPath();
		String sql = FileUtils.readFileAsString(pathtoscript);
		
		Statement statement = DBConnection.getConnection().createStatement();
		statement.execute(sql);
	}
	
	/**
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void insertSampleData() throws SQLException,
			IOException, URISyntaxException
	{
		loadProperties();
		URL pathurl = DBAdministration.class.getResource(props
				.getProperty("sampledatascript"));
		URI uri = new URI(pathurl.toString());
		String pathtoscript = uri.getPath();
		String sql = FileUtils.readFileAsString(pathtoscript);
		
		Statement statement = DBConnection.getConnection().createStatement();
		statement.execute(sql);
	}
	
	/**
	 * insert a user
	 * @param user
	 * @throws SQLException
	 */
	public static void insertUser(User user)
			throws SQLException
	{
		String SQLQuery = "INSERT INTO \"user_table\" " +
				"(\"userId\", \"academicTitle\", \"forename\", \"surname\", " +
				"\"email\", \"password\", \"status\") " + 
				"VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement (SQLQuery);
		
		pst.setString(1, user.getUserId());
		pst.setString(2, user.getAcademicTitle());
		pst.setString(3, user.getForname());
		pst.setString(4, user.getSurname());
		pst.setString(5, user.getEmail());
		pst.setString(6, UUID.randomUUID().toString()); //FIXME test ob schon vorhanden
		pst.setString(7, "user"); //FIXME
		pst.executeUpdate();
	}
	
	/**
	 * @return true, if DB is empty
	 */
	public static boolean isEmpty() 
	{
		try 
		{
			String SQLQuery = "SELECT COUNT(*) FROM \"user_table\"";
			PreparedStatement pst;
			ResultSet res;
			pst = DBConnection.getConnection().prepareStatement(SQLQuery);
			res = pst.executeQuery();
			res.next();
			
			if (res.getInt(1) > 0)
			{
				res.close();
				pst.close();
				return false;
			} 
			else
			{
				res.close();
				pst.close();
				return true;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Checks if Schema is created
	 * @return true, if Schema exists
	 */
	public static boolean isSchemaCreated() 
	{
		try 
		{
			DatabaseMetaData dm = DBConnection.getConnection().getMetaData();
			
			return dm.getTables(null, null, "advertisement_table", null).first() &&
					dm.getTables(null, null, "user_table", null).first() &&
					dm.getTables(null, null, "initiator_table", null).first() &&
					dm.getTables(null, null, "follower_table", null).first();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	/**
	 * gets the properties required for the DB Administration out of the
	 * configuration file (settings.cfg)
	 */
	private static void loadProperties() 
	{
		props.setProperty("connectionstring",
				Settings.getProperty("connectionstring")[0]);
		props.setProperty("driver", Settings.getProperty("driver")[0]);
		props.setProperty("initscript", Settings.getProperty("initscript")[0]);
		props.setProperty("sampledatascript", Settings.getProperty("sampledatascript")[0]);
	}
	
	/**
	 * Updates the data of an Advertisement in the database
	 * @param advert
	 * @throws SQLException
	 */
	public static void updateAd(Ad advert) throws SQLException 
	{

		String SQLQuery = "UPDATE \"advertisement_table\" " +
				"SET \"title\" = ?, " +
				"\"projectedStartDate\" = ?, " +
				"\"expiryDate\" = ?, " +
				"\"institute\" = ?, " +
				"\"projectType\" = ?, " +
				"\"targetedAudience\" = ?, " +
				"\"description\" = ?, " +
				"\"followersVisible\" = ?, " +
				"\"Search\" = ?, " +
				"\"visible\" = ? " +
				"WHERE \"adId\" = ?;";

		PreparedStatement pst = DBConnection.getConnection().prepareStatement(SQLQuery);
		pst.setString(1, advert.getTitle());
		pst.setDate(2, new java.sql.Date(advert.getProjectStart().getTime()));
		pst.setDate(3, new java.sql.Date(advert.getExpiryDate().getTime()));
		pst.setString(4, advert.getInstitute());
		pst.setString(5, advert.getProjectType());
		pst.setString(6, advert.getTargetedToSaveinDB());
		pst.setString(7, advert.getDescription());
		pst.setString(8, advert.getFollowersVisible());
		pst.setBoolean(9, advert.isQuest());
		pst.setBoolean(10, advert.isVisible());
		pst.setString(11, advert.getAdId());

		pst.executeUpdate();
		pst.close();
	}
	
	/**
	 * Activates an Ad
	 * @param id
	 * @throws SQLException
	 */
	public static void updateAdExpiryDate(String adId, long expiryDate) throws SQLException 
	{

		String SQLQuery = "UPDATE \"advertisement_table\" SET \"expiryDate\" = ? WHERE \"adId\" = ?;";
		PreparedStatement pst = DBConnection.getConnection().prepareStatement(SQLQuery);
		pst.setDate(1, new java.sql.Date(expiryDate));
		pst.setString(2, adId);
		
		pst.executeUpdate();
		pst.close();
	}

	/**
	 * Updates the Followerdata of a single Follower at the Database
	 * @param follower
	 * @throws SQLException
	 */
	public static void updateFollower(Follower follower) throws SQLException 
	{
		
		String SQLQuery = "UPDATE \"follower_table\" SET \"description\" = ? " +
				"WHERE \"userId\" = ?;";
		PreparedStatement pst = DBConnection.getConnection().prepareStatement(SQLQuery);
		pst.setString(1, follower.getComment());
		pst.setString(2, follower.getUserId());
		
		pst.executeUpdate();
		pst.close();
	}
	//
	


	/**
	 * Updates the Userdata of a single User at the Database
	 * @param user
	 * @throws SQLException
	 */
	public static void updateUser(User user) throws SQLException 
	{
		String SQLQuery = "UPDATE \"user_table\" " +
				"SET \"academicTitle\" = ?, " +
				"\"forename\" = ?, " +
				"\"surname\" = ? " +
				"WHERE \"userId\" = ?;";
		
		PreparedStatement pst = DBConnection.getConnection().prepareStatement(SQLQuery);
		pst.setString(1, user.getAcademicTitle());
		pst.setString(2, user.getForname());
		pst.setString(3, user.getSurname());
		pst.setString(4, user.getUserId());

		pst.executeUpdate();
		pst.close();
	}

	
	
	
}
