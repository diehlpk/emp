<!-- 
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
 -->

<%@page import="controller.Settings"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<jsp:useBean id="ad" scope="request" type="model.Ad" class="model.Ad"/>
<%@ include file="include/Head.html" %>

<!-- Neuerstellen & Bearbeiten einer Anzeige -->


	<script type="text/javascript" src="./tinymce/jscripts/tiny_mce/tiny_mce.js"></script>

	<script type="text/javascript">
		tinyMCE.init({
			   mode : "textareas",
			    theme : "advanced",
			    theme_advanced_buttons1 : "bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright, justifyfull,bullist,numlist,undo,redo",
			    theme_advanced_buttons2 : "link,unlink,image",
			    theme_advanced_buttons3 : "",
			    theme_advanced_toolbar_location : "top",
			    theme_advanced_toolbar_align : "left",
			    theme_advanced_statusbar_location : "bottom",
			    plugins : 'inlinepopups,ccSimpleUploader',
			    relative_urls : false,
			    file_browser_callback: "ccSimpleUploader",
			    plugin_ccSimpleUploader_upload_path: '../../../../../../uploads',                 
			    plugin_ccSimpleUploader_upload_substitute_path: '/uploads/',
			    
				height : 350,
			
		});
	</script>
	
	<c:if test="${adId != null && adId != ''}">
   		<title>IMP - Anzeige bearbeiten</title>
   		<div id="breadcrumb">
      		<div class="tx-visrootline-pi1">
         		<a class="rootlink" href="http://www.change.me">Institution</a>&nbsp;&rArr;&nbsp;
         		<a href="http://www.change.me/home.html" target="_self" class="rootlink">Informatik</a>&nbsp;&rArr;&nbsp;
         		<a href="/IMP/index.jsp" target="_self" class="rootlink">Marktplatz</a>&nbsp;&rArr;&nbsp;Anzeige bearbeiten
      		</div>
   		</div>
	</c:if>
   
	<c:if test="${adId == null || adId == ''}"><title>Neue Anzeige erstellen</title>
   		<div id="breadcrumb">
      		<div class="tx-visrootline-pi1">
         		<a class="rootlink" href="http://www.change.me">Institution</a>&nbsp;&rArr;&nbsp;
         		<a href="http://www.change.me/home.html" target="_self" class="rootlink">Informatik</a>&nbsp;&rArr;&nbsp;
         		<a href="/IMP/index.jsp" target="_self" class="rootlink">Marktplatz</a>&nbsp;&rArr;&nbsp;Neue Anzeige erstellen
      		</div>
   		</div>
   	</c:if>
   		
   		
<%@ include file="include/Sidebars.html" %>
 
<!-- Hier geht es erst wirklich los -->

<!-- Anzeige bearbeiten: -->
<!-- 
	Eingabe:
		- Art der Anzeige
		- Titel der Anzeige 
		- Akademischer Titel (Optional) (Voreingestellt)
		- Vorname (Voreingestellt)
		- Nachname (Voreingestellt)
		- Email (Voreingestellt)
		- Projektstart 
		- Projekttyp
		- Zielgrupe
		- Sichtbarkeit
		- Institut
		- Beschreibung
		- captacha
	Aktionen:
		- Anzeige erstellen (falls Neue Anzeige)
		- Anzeige bearbeiten (falls Anzeige bearbeiten)
		- Anzeige löschen (falls Anzeige bearbeiten)
		- Zur Hauptseite
		- Zum persönlichen Bereich (falls eingeloggt)
	
	Defintion:
		(Voreingestellt) == falls Eingeloggt, werden die Werte aus dem User Objekt übernommen
 -->
	<form action="./EditAdvertisement" method="post">      
      	<!-- Table mit Eingabeflächen -->
      	<!-- Table hat 2 Spalten, d.h. die Form <tr><td>...</td><td>...</td></tr> -->
      	<c:if test="${userId != null}">
    		<c:set var="readonly" value="readonly=\"readonly\"" />
    	</c:if>
    	
      	<table>        	
         	<tr>
            	<td>
               		<label for="title"><span class="mandatoryinput">Titel der Anzeige:</span></label>
            	</td>
            	<td>
               		<input type="text" id="title" name="title" size="20"  value="${title}" />
               		<c:if test="${titleValidate != null}">
						<a title="${titleValidate}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
					</c:if>               
            	</td>
         	</tr>
         	
         	<tr>
            	<td>
               		<label for="academicTitle">Akademischer Titel:</label>
           	 	</td>
            	<td>
               		<input type="text" id="academicTitle" name="academicTitle" size="20"  value="${academicTitle}" ${readonly} />
               		(optional)
               		<c:if test="${academicTitleValidate != null}">
						<a title="${academicTitleValidate}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
					</c:if>               
            	</td>
         	</tr>
         
         	<tr>
            	<td>
               		<label for="forename"><span class="mandatoryinput">Vorname:</span></label>
            	</td>
            	<td>
               		<input type="text" id="forename" name="forename" size="20"  value="${forename}" ${readonly} />
               		<c:if test="${forenameValidate != null}">
						<a title="${forenameValidate}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
					</c:if>               
            	</td>
         	</tr>
         	
         	<tr>
            	<td>
               		<label for="surname"><span class="mandatoryinput">Nachname:</span></label>
            	</td>
            	<td>
               		<input type="text" id="surname" name="surname" size="20"  value="${surname}" ${readonly} />
               		<c:if test="${surnameValidate != null}">
						<a title="${surnameValidate}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
					</c:if>               
            	</td>
         	</tr>
         	
         	<tr>
            	<td>
               		<label for="email"><span class="mandatoryinput">E-Mail-Adresse:</span></label>
            	</td>
            	<td>
               		<input type="text" id="email" name="email" size="40" value="${email}" ${readonly} />
               		<a title="${toolTippEmail}">
                  		<img src="querry.png" alt="?" border="0" width="20" height="20" />
               		</a>
 					<c:if test="${emailValidate != null}">
						<a title="${emailValidate}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
					</c:if>                          	
               	</td>
         	</tr>
         
         	<tr>
            	<td>
               		<label for="projectStart"><span class="mandatoryinput">Projektstart:</span></label>
            	</td>
            	<td>
               	<%
               		/*  Programmlogik; Dies sollte eigentlich in main.java stattfinden
               		 *  Zu ersetzen durch einen Datepicker 
               		 */
               		
               		// Berechne das aktuelle Datum und setze die Auswahl auf das berechnete Datum
               		Calendar cal = new GregorianCalendar ();
               
               		int projectStartDay = cal.get(Calendar.DAY_OF_MONTH);
               		int projectStartMonth = cal.get(Calendar.MONTH) + 1; //  cal kennt Monate 0 .. 11, daher die umwandlung
               		int projectStartYear = cal.get(Calendar.YEAR);
               		
               		try
           			{
               			String psd = (String) request.getAttribute("projectStartDay");
               			String psm = (String) request.getAttribute("projectStartMonth");
           				String psy = (String) request.getAttribute ("projectStartYear");
           				
               			projectStartDay = Integer.parseInt(psd);
                  		projectStartMonth = Integer.parseInt(psm);
                  		projectStartYear = Integer.parseInt(psy);
           			}
           			catch (NumberFormatException nfe)
           			{
           				projectStartDay = cal.get(Calendar.DAY_OF_MONTH);
                   		projectStartMonth = cal.get(Calendar.MONTH) + 1;
                   		projectStartYear = cal.get(Calendar.YEAR);
                   
           			} 
               	%>
               
               		<select name="projectStartDay" size="1">
                   	<% 
                   	for (int day = 1; day <= 31; day++) { %>
                  		<option <% if (day == projectStartDay) {out.print(" selected=\"selected\"");}  %>>
                   			<% out.print(day + ""); %>
                   		</option><% }
                   	%>
               		</select>
               
               		<select name="projectStartMonth" size="1">
                  	<% 
                  	for (int month = 1; month <= 12; month++) { %>
                  		<option<% if (month == projectStartMonth) {out.print(" selected=\"selected\"");}  %>>
                  			<% out.print(month); %>
                  		</option> <% }
                  	%>
               		</select>
               
               		<select name="projectStartYear" size="1">
                  	<% 	
                  		int yearNow = cal.get(Calendar.YEAR); // das Startjahr wird hier dynamisch festgelegt
                  		for (int year = yearNow; year <= (yearNow + 4); year++) { %>
                  		<option<% if (year == projectStartYear) {out.print(" selected=\"selected\"");} %>>                  
                  			<% out.print(year); %>
                  		</option><% } %>
               		</select>
					<c:if test="${projectStartValidate != null}">
						<a title="${projectStartValidate}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
					</c:if>               
            	</td>
         	</tr>
         	<tr>
         		<td>
         			<label><span class="mandatoryinput">Projekttyp:</span></label>
         		</td>
         		<td>
         			<select name ="projectType"  size="5">
						<%
						String projectType = (String) request.getAttribute("projectType");
						for (String projectT : controller.Settings
								.getProperty("types"))
						{
						%>
						<%
							if (projectT.equals(projectType)) 
							{
							%>
							<option selected="selected">
								<%out.println (projectT);%>
							</option>
							<%
							}
							else
							{
							%>
							<option>
								<%out.println (projectT);%>
							</option>
							<%
							}
						}
						%>
					</select>
					<c:if test="${projectTypeValidate != null}">
						<a title="${projectTypeValidate}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
					</c:if>
         		</td>
         	</tr>
         	<tr>
            	<td>
               		<label><span class="mandatoryinput">Zielgruppe:</span></label>
            	</td>
            	<td>
        			<% 
        				String [] targetGroups = (String []) request.getAttribute("targetGroups");
        				int i = 0;
        				for (String targetG : Settings.getProperty("targeted"))
        				{
        					boolean check = false;
        					for (String targetGroup : targetGroups)
        					{
        						if (targetG.equals(targetGroup))
        						{
        							check = true;
        						}
        					}
        					
        					String id = "targeted" + i;
        					i++;
        					if (check)
        					{
        						%>
    							<input type="checkbox" id=<%=id %> name="targeted" value=<%=targetG%> checked="checked"></input>
    							<label for=<%=id %>><%=targetG %></label>
    							<%
        					}
        					else
        					{
        						%>
    							<input type="checkbox" id=<%=id %>  name="targeted" value=<%=targetG%>></input>
    							<label for=<%=id %>><%=targetG %></label>
    							<%
        					}
        				}
        				
        			%>
					<c:if test="${targetedValidate != null}">
						<a title="${targetedValidate}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
					</c:if>
            	</td>
         	</tr>
         	<tr>
            	<td>
               		<label><span class="mandatoryinput">Für wen sollen Interessenten sichtbar sein?</span></label>
            	</td>
            	<td>
               		<p>
						<c:set var="disabled" value=""/>
          				<% 
          				String areFollowersVisible = (String) request.getAttribute("areFollowersVisible");
          				if ("Oeffentlich".equals(areFollowersVisible)) 
          				{ %>
                  			<input id="Oeffentlich" type="radio" name="followersVisible" value="Oeffentlich" checked="checked"></input> 
          					<label for="Oeffentlich">&Ouml;ffentlich</label>
          				<% } 
          				else 
          				{ %>
							<c:if test="${adId != null && adId != '' && userId != null && userId != ''}">
								<c:set var="disabled" value=" disabled=\"disabled\"" />
							</c:if>
                  			<input id="Oeffentlich" type="radio" name="followersVisible" value="Oeffentlich" ${disabled}></input> 
                  			<label for="Oeffentlich">&Ouml;ffentlich</label>
          				<% }
          
          				if ("Initiator und Follower".equals(areFollowersVisible)) 
          				{ %>
                  			<input id="Initiator und Follower" type="radio" name="followersVisible" value="Initiator und Follower" checked="checked"></input> 
                  			<label for="Initiator und Follower">Initiator und Interessenten</label>
          				<% } 
          				else 
          				{ %>
                  			<input id="Initiator und Follower" type="radio" name="followersVisible" value="Initiator und Follower" ${disabled}></input> 
                  			<label for="Initiator und Follower">Initiator und Interessenten</label>
          				<% }
          
          				if ("Nur Initiator".equals(areFollowersVisible)) 
          				{ %>
                  			<input id="Nur Initiator" type="radio" name="followersVisible" value="Nur Initiator" checked="checked" ${disabled}></input> 
                  			<label for="Nur Initiator">Nur Initiator</label>
                  			<input type="hidden" name="followersVisible" value="Nur Initiator"></input>
          				<% } 
          				else 
          				{ %>
                 			 <input id="Nur Initiator" type="radio" name="followersVisible" value="Nur Initiator"></input> 
                 			 <label for="Nur Initiator">Nur Initiator</label>
          				<% } %>
                  
                  		<a title="${toolTippVisibility}">
                     		<img src="querry.png" alt="?" border="0" width="20" height="20"></img> 
                  		</a>
               		</p>
            	</td>
         	</tr>
         	
         	<tr>
            	<td>
               		<label for="institute"><span class="mandatoryinput">Institut:</span></label>
            	</td>
             	<td>
               		<select id="institute" name="institute"> 
                  	<% 
                  	String checked = "";
                  	for (String institute : controller.Settings.getProperty("institutes"))
                  	{
                    	if (institute.equals((String) request.getAttribute("institute"))) 
                    	{
                        	checked = "selected=\"selected\"";
                      	}
                      	else
                      	{
                         	checked = "";
                      	}
                    	String query = "<option " + checked + "> " + institute + " </option>";
                      	out.println (new String(query.getBytes("ISO-8859-1"),"UTF-8"));
                   	} %>
                  	</select>
               		
<!--               		<a title="Falls Sie Student oder Externer sind, w&auml;hlen Sie bitte 'Keinem Institut zugeordnet'.">
                  		<img src="querry.png" alt="?" border="0" width="20" height="20" />
               		</a>
-->
           			<c:if test="${instituteValidate != null}">
						<a title="${instituteValidate}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
					</c:if>
            	</td>
         	</tr>
         	
         	<tr>
            	<td>
               		<label for="description"><span class="mandatoryinput">Beschreibung:</span></label>
            		<br/>
            		<c:if test="${descriptionValidate != null}">
						<a title="${descriptionValidate}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
					</c:if>
            	</td>
            	<td>
                  		<textarea id="description" name="description" rows="10" cols="60">${description}</textarea>
                  		<a title="${toolTippDescription}">
                     		<img src="querry.png" alt="?" border="0" width="20" height="20" />
                  		</a>
						<%
						String descriptionVal = (String) request.getAttribute ("descriptionValue");
						if (descriptionVal != null)
						{
							%>
							<a title=<%=descriptionVal %>>
                     			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
                  			</a>
							<%
						}
						%>                  
            	</td>
         	</tr>  
         	<c:if test="${adId == null || adId == '' }">
         	<tr>
            	<td></td>
            	<td>
               		<img src="./simpleImg" alt="Hier sollte das Captcha sein." />
               		<br />
               		<input type="text" name="captchaResponse" value="" />
               		<a title="Geben Sie den Inhalt des Captchas ein.">
                  		<img src="querry.png" alt="Information" border="0" width="20" height="20" />
               		</a>
               		<a title="${captcha}">
                  		<c:if test="${captcha != null}"><img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" /></c:if>
               		</a>
            	</td>
         	</tr>
         	</c:if>
         	<tr>
         		<td></td>
            	<td align="left">
            		<c:if test="${adCreated != null || adCreated != ''}">
            			<input type="hidden" name="adCreated" value="${adCreated}"/>
            		</c:if>
            		<c:if test="${userId != null && userId != ''}">
						<input type="hidden" name="userId" value="${userId }"/>
					</c:if>
               		<c:if test="${adId != null && adId != '' }">
               			<input type="hidden" name="adId" value="${adId}"/>
               			<input type="hidden" name="edit" value="editAd"/>
               			<input type="submit" class="button" name="editSubmit" value="Anzeige bearbeiten"/>
               		</c:if>
               		<c:if test="${adId == null || adId == '' }">
               			<input type="hidden" name="edit" value="createNewAd"/>
               			<input type="submit" class="button" value="Anzeige erstellen" name="editSubmit" />
               		</c:if>
            	</td>
         	</tr>
      	</table>
      		<p>Alle mit <span class="mandatoryinput"> </span> gekennzeichneten Felder sind Pflichtfelder.</p>
	</form>
   	
   	<br />
   	<br />
   
   <!-- Definiert Button zur Hauptseite -->
   	<form action="./Main" method="post">
		<c:if test="${userId != null && userId != ''}">
			<input type="hidden" name="userId" value="${userId}"/>
		</c:if>
      	<c:if test="${adId != null && adId != '' }" >
      		<input type="submit" class="button" value="Zur&uuml;ck zur Hauptseite" title="(Ihre Anzeige wird nicht ver&auml;ndert)" />
      	</c:if>
      	<c:if test="${adId == null || adId == '' }" >
      		<input type="submit" class="button" value="Zur&uuml;ck zur Hauptseite" title="(Ihre Anzeige wird nicht erstellt)" />
      	</c:if>
   	</form>
   	
   	<!-- Falls eingeloggt: Definiert Button zum persönlichen Bereich -->
	<c:if test="${userId != null && userId != ''}">
   		<form action="./PersonalSection" method="post">
      		<input type="hidden" name="userId" value ="${userId}"/>	
      		<c:if test="${adId != null && adId != '' }" >
      			<input type="submit" class="button" value="Zum pers&ouml;nlichen Bereich" title="(Ihre Anzeige wird nicht ver&auml;ndert)" />
      		</c:if>
      		<c:if test="${adId == null || adId == '' }" >
      			<input type="submit" class="button" value="Zum pers&ouml;nlichen Bereich" title="(Ihre Anzeige wird nicht erstellt)" />
      		</c:if>
   		</form>
   	</c:if>
   	
<%@ include file="include/Tail.html" %>