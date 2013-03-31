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


<%@page import="model.User"%>
<%@page import="model.Follower"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
<%@ page import="controller.DBAdministration" %>
<%@ page import="model.Ad" %>

<title>IMP - Anzeigendetails betrachten</title>
<%@ include file="include/Head.html" %>

<div id="breadcrumb">
    <div class="tx-visrootline-pi1">
        <a href="http://www.change.me">Institution</a>&nbsp;&rArr;&nbsp;
        <a href="http://www.change.me/home.html" >Informatik</a>&nbsp;&rArr;&nbsp;
        <a href="/IMP/index.jsp">Marktplatz</a>&nbsp;&rArr;&nbsp;Anzeigendetails betrachten
    </div>
</div>

<%@ include file="include/Sidebars.html" %>

	<script type="text/javascript" src="./tinymce/jscripts/tiny_mce/tiny_mce.js"></script>

	<script type="text/javascript">
		tinyMCE.init({
			   mode : "textareas",
			    theme : "advanced",
			    theme_advanced_buttons1 : "mybutton,bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright, justifyfull,bullist,numlist,undo,redo",
			    theme_advanced_buttons2 : "",
			    theme_advanced_buttons3 : "",
			    theme_advanced_toolbar_location : "top",
			    theme_advanced_toolbar_align : "left",
			    theme_advanced_statusbar_location : "bottom",
			    plugins : 'inlinepopups',
			
		});
	</script>


<!-- Bitte auf die Formatierung achten. Es hilft schon viel, wenn die Datei lesbar ist -->

<!-- Achtung: 
		Die Tabelle (<table>) hat GENAU 2 Spalten, d.h. in einem <tr> Tag müssen genau 2 <td> tags vorkommen.
		Sonst wird die Ansicht verändert
 -->
 
 	<%
 		String adId = request.getParameter("adId");
 		
 		if (adId == null || adId.equals(""))
 		{
 			%>
 				<img src="cross.png" alt="Fehler!" border="0" width="25" height="25" />
        		<b>Die von Ihnen aufgerufene Anzeige existiert nicht, oder nicht mehr!</b>
 			<%
 		}
 	%>
    
    <!-- Anzeige der Anzeige -->
    <!-- 
    	Zeigt an:
    		- Ist ein Gesuch (falls true)
    		- Titel
    		- Initiator
    		- Institut
    		- Projektart
    		- Zielgruppe
    		- Projektstart
    		- Beschreibung
    	Mögliche Aktionen
    		- Message an Initiator schicken
    -->
    
    <%
    	//boolean openForAll = false;
    	String title = (String) request.getAttribute("title");
		String nameInitiator = (String) request.getAttribute("nameInitiator");
		String emailInitiator = (String) request.getAttribute("emailInitiator");
		String institute = (String) request.getAttribute("institute");
		String projectType = (String) request.getAttribute("projectType");
		String targeted = (String) request.getAttribute("targeted");
		String projectStart = (String) request.getAttribute("projectStart");
		String description = (String) request.getAttribute ("description");
		
		String openForAll = (String) request.getAttribute("openForAll");
		String openForAllMessage = (String) request.getAttribute("openForAllMessage");
    %>
<div>
    <table>
    	<tr>
        	<td>Titel der Anzeige:</td>
        	<td><%=title%></td>
    	</tr>
    	
    	<tr>
        	<td>Initiator der Anzeige:</td>
        	<td>
        		<c:if test="${userId != null && userId != ''}">
        			<a href="./SendMessage?messageToInitiator=messageToInitiator&adId=${adId}&userId=${userId}">
        				<b><%=nameInitiator %></b>
        			</a>
        			&nbsp;
        			<a href="./SendMessage?messageToInitiator=messageToInitiator&adId=${adId}&userId=${userId}">
        				<img src="email-regular.png" alt="E-Mailicon" border="0" width="14" height="14" title="${toolTippEmailContact}" />
        			</a>
        		</c:if>
        		<c:if test="${userId == null || userId == ''}">
        			<a href="./SendMessage?messageToInitiator=messageToInitiator&adId=${adId}">
        				<b><%=nameInitiator %></b>
        			</a>
        			&nbsp;
        			<a href="./SendMessage?messageToInitiator=messageToInitiator&adId=${adId}">
        				<img src="email-regular.png" alt="E-Mailicon" border="0" width="14" height="14" title="${toolTippEmailContact}" />
        			</a>
        			
        		</c:if>
			</td>
    	</tr>
    
        <tr>
           	<td>Betreuendes Institut:</td>
           	<td><%=institute%></td>
        </tr>
    	
    
    	<tr>
        	<td>Art des Projekts:</td>
        	<td><%=projectType%></td>
    	</tr>
    
    	<tr>
        	<td>Zielgruppe:</td>
        	<td><%=targeted%></td>
   		</tr>
    
    	<tr>
        	<td>Projektbeginn:</td>
        	<td><%=projectStart%></td>
    	</tr>
    
    	<tr>
        	<td>Beschreibung:</td>
        	<td><%=description%></td>
    	</tr>
    
    	<tr>
    		<td colspan="2">           	
    		<%
    			if (openForAll.equals("Nur Initiator")) // nicht öffentlich
    			{
    				%>
    				<img src="warning.png" alt="Hinweis" border="0" width="20" height="20" />
    				<%
    				out.println ("Interessenten sind nicht &ouml;ffentlich sichtbar.");
    				%><br/><%
    			}    		
        		
				out.println (openForAllMessage);
            %>
        	</td>
    	</tr>
    </table>


<!-- Interesse an Anzeige, Interessenbekundung möglich -->
<!-- 
	Erfrägt:
		- Vorname
		- Nachname
		- Email
		- Kommentar (optional)
		- Capatcha
	Mögliche Optionen:
		- Als Interessent eintragen
		- Zur Hauptseite zurück
		- Falls eingeloggt: Zum Persönlichen Bereich
 -->
 	<%
 		String isInitiator = (String) request.getAttribute("isInitiator");
 		String isFollower = (String) request.getAttribute("isFollower");
 		String userId = (String) request.getAttribute("userId");
 		
 		if ("false".equals(isInitiator) && "false".equals(isFollower))
 		{
 			%>
 				<form action="./AddFollower" method="post">
		<table>
			<%-- Falls die userId vorhanden ist => user ist schon eingeloggt, die Daten koennen also hier schon übernommen werden --%>	
		<tr>
			<td></td>
			<td>
				<h2>Sie interessieren sich für diese Anzeige?</h2>
    
				<p>Dann tragen Sie sich als Interessent ein:</p>
				<p>
					<input type="hidden" name="visiblityOfFollowers" value="${visiblityOfFollowers}"/>
					${visibiltyOfFollowers}
					<c:if test="${visiblityOfFollowers == Oeffentlich}">	
						Dies ist eine &ouml;ffentliche Anzeige. Die Namen der Interessenten werden nicht verborgen.
						<img src="warning.png" alt="Hinweis" border="0" width="20" height="20" />
					</c:if>
				</p>
			</td>
		</tr>
		
    	<tr>
        	<td>
            	<label for="forename"><span class="mandatoryinput">Vorname:</span></label>
            </td>
            <td>
            	<input type="text" id="forename" name="forename" size="20"  value="${forename}" ${readonly}></input>
	            	<c:if test="${forenameValidate != null && forenameValidate != ''}">
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
            	<c:if test="${surnameValidate != null && surnameValidate != ''}">
            		<a title="${surnameValidate}">
             			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" /> 
            		</a>
            	</c:if>
            </td>
        </tr>
        
        <tr>
            <td>
            	<label for="email"><span class="mandatoryinput">E-Mail-Adresse</span></label>
            	<br/>
            </td>
            <td>
            	<input type="text" id="email" name="email" size="40"  value="${email}" ${readonly} />
            		<a title="${toolTippEmail}">
             			<img src="querry.png" alt="?" border="0" width="20" height="20" /> 
            		</a>
            		<c:if test="${emailValidate != null && emailValidate != ''}">
            			<a title="${emailValidate}">
                    		<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
                   	 	</a>
            		</c:if>
            </td>
        </tr>

		<tr>
            <td>
            	Kommentar: <br/>
				<a title="${toolTippComment}">
             		<img src="querry.png" alt="?" border="0" width="20" height="20" /> 
            	</a>           
            </td>
            <td>
            	<textarea id="comment" name="comment" rows="10" cols="60">${comment}</textarea>
            	
            	<c:if test="${commentValidate != null && commentValidate != ''}">
            		<a title="${commentValidate}">
                		<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               	 	</a>
            	</c:if>
            </td>
        </tr>
		<% if (userId == null || userId == "") {%>
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
                    <c:if test="${captcha != null}">
                    	<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
                    </c:if>
                </a> 
            </td>
        </tr>
        <% } %>
		
		<tr>
			<td></td>
			<td>
				<c:if test="${userId != null}">
					<input type="hidden" name="userId" value="${userId}"/>
				</c:if>
    			<input type="hidden" name="adId" value="${adId}"/>
    			<input type="submit" class="button" value="Als Interessent eintragen" />
			</td>
		</tr>
		</table>
			<p>Alle mit <span class="mandatoryinput"> </span> gekennzeichneten Felder sind Pflichtfelder.</p>
	</form>
 						
 			<%
 		}
 	%>
	</div>

    <br />
    
    <!-- Definiert Button zur Hauptseite -->
	<form action="./Main" method="post">
		<c:if test="${userId != null && userId != ''}">
			<input type="hidden" name="userId" value="${userId}"/>
		</c:if>
    	<input type="submit" class="button" value="Zur&uuml;ck zur Hauptseite" />
	</form>

	<!-- Falls eingeloggt: definiert Button zum persönlichen Bereich -->
    <c:if test="${userId != null && userId != ''}">
    	<form action="./PersonalSection" method="post">
        	<input type="hidden" name="userId" value="${userId}"/>
        	<input type="submit" class="button" value="Zum pers&ouml;nlichen Bereich" />
    	</form>
    	
    	<form action="./Main" method="post">
    		<input type="hidden" name="adId" value="${adId}"/>
    		<input type="hidden" name="AdDetail" value="AdDetail"/>
    		<input type="submit" class="button" value="Ausloggen"/>
    	</form>
    </c:if>
    
    
<%@ include file="include/Tail.html" %>