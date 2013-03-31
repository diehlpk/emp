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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class GenerateSamplaData 
{
	
	/**
	 * @param args
	 * 
	 * Generates new "insert_sample_data.sql" file
	 */
	public static void main(String[] args) 
	{
		generateData (150);
	}
	
	
	//  Erzeugt nun Ads, 1 user und zugehörigen initiatortable
	private static void generateData (int numberOfSampleAds)
	{
		String source = "./src/insert_sample_data.sql";
		
		File f = new File (source);
		if (f.exists())
		{
			if (f.isFile())
			{
				PrintWriter out = null;
				try 
				{
					out = new PrintWriter (new FileOutputStream (f));
					//  Ein superuser der alle ads "erstellt" hat
					String superUser ="INSERT INTO \"user_table\" (\"userId\",\"academicTitle\",\"forename\",\"surname\",\"email\",\"password\",\"status\") VALUES \n" + 
					 "('e9e2d224-34f7-4288-8ac2-a4122e86906b','Wi. M.A.','Heinz','Gutfried','Gutfried@change.me','fixme1','user');\n";
					String userId = "e9e2d224-34f7-4288-8ac2-a4122e86906b";
					out.println (superUser);
					
					//  Create Ads && verbinde diese mit superUser
					//  set random generator with seed of actual time
					Random r = new Random (new Date ().getTime());
					String [] inst = {"INST1","INST2"};
					String [] projectType = {"Projekt-INF","Diplomarbeit","Bachelor-Arbeit","Master-Arbeit","Fachstudie","Studienarbeit","Studienprojekt","Prozessanalyse","Hilfskraft","Lerngruppe","Sonstige"};
					for (int i = 0; i < numberOfSampleAds; i++)
					{
						// create ad
						String ad = "INSERT INTO \"advertisement_table\" (\"adId\",\"title\",\"projectedStartDate\",\"expiryDate\",\"institute\",\"projectType\",\"targetedAudience\",\"description\",\"followersVisible\",\"archivedDate\",\"Search\",\"visible\") VALUES \n";
						ad += "(";
						String adId = UUID.randomUUID().toString();
						String title = "Testanzeige" + i;
						int month = r.nextInt(11)+1;
						int day = r.nextInt(28)+1;
						String startDate = "2012-" + (month)+"-"+ (day);
						String expiryDate = "2012-" + (month+1)+"-" + (day);
						String institute = inst [r.nextInt(9)];
						String pt = projectType [r.nextInt(11)];
						String targetAudiance ="Alle";
						String description ="Description " + i;
						String followersVis = "Oeffentlich";
						String rest ="null, 0,1";
						
						ad += "\'" +adId + "\',\'" + title + "\',\'";
						ad += startDate + "\',\'" + expiryDate + "\',\'" + institute + "\',\'";
						ad += pt + "\',\'" + targetAudiance + "\',\'" + description + "\',\'";
						ad += followersVis + "\'," + rest;
						ad += ");\n";
						
						out.println(ad);
						
						//  create initiator
						String initiator = "INSERT INTO \"initiator_table\" (\"userId\",\"adId\",\"adCreated\") VALUES \n" + 
						"(\'" + userId+ "\',\'" + adId+ "\',\'2012-09-05\'); \n";
						
						out.println (initiator);
					}
					
				}
				catch (FileNotFoundException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally
				{
					if (out != null)
						out.close ();
				}
			}
		}
	}

}
