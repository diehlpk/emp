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

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;


public class Quartz {

    public static void main(String[] args) {

        try {
            // Grab the Scheduler instance from the Factory 
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            /*
    		 * Variablen initialisierung
    		 */
    		String deactivationAutomatic = null;
    		
    		/*
    		 * Belegen der Variablen
    		 */
    		deactivationAutomatic = controller.Settings.getProperty("deactivationAutomatic")[0];
    		
            // and start it off
            scheduler.start();
            // define the job and tie it to our HelloJob class
            JobDetail job = newJob(QuartzJob.class)
                .withIdentity("deactivateAdsAutomatically", "deactivationAds")
                .build();

            // Trigger the job to run now, and then repeat every 40 seconds
            SimpleTrigger trigger = newTrigger()
                .withIdentity("trigger", "deactivationAds")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(Integer.parseInt(controller.Settings.getProperty("deactivationIntervalTimeInSeconds")[0]))
                        .repeatForever())            
                .build();

    		/*
    		 * Falls automaticDeavtivationOn == yes dann starte den Job
    		 */
    		if (deactivationAutomatic.equals(new String("yes"))){
                // Tell quartz to schedule the job using our trigger
                scheduler.scheduleJob(job, trigger);
    		}
            

            //System.out.println("Quartz.java: Quartz wurde gestartet");
            //scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }
}
