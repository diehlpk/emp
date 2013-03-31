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

<%@page import="controller.InputSanitizer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Information</title>

<%@ include file="include/Head.html" %>

<div id="breadcrumb">
	<div class="tx-visrootline-pi1">
		<a class="rootlink" href="http://www.change.me">Institution</a>&nbsp;&rArr;&nbsp;
		<a href="http://www.change.me/home.html" target="_self" class="rootlink">Informatik</a>&nbsp;&rArr;&nbsp;
		<a href="/IMP/index.jsp" target="_self" class="rootlink">Marktplatz</a>&nbsp;&rArr;&nbsp;Information
	</div>
</div>

<%@ include file="include/Sidebars.html" %> 

<!-- Informationen: Hier werden die Fehlermeldungen ausgegeben -->
<!-- 
	Aktionen:
		- Zurück zur Anzeige (falls adId existiert)
		- Zur Hauptseite
		- Zum persönlichen Bereich (falls eingeloggt)
 -->

	<p>
	
	<%
			String source = (String) request.getAttribute("source");

			if (source == null) {
				request.setAttribute("source",
						"Ung&uuml;ltiger Zugriff! Es gibt keinen Benutzer mit dieser Emailadresse!");
			
			} else if ("advertisement".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Ihre Anzeige wurde erfolgreich erstellt, ist jedoch noch nicht im System sichtbar!</b> <br></br> Sie haben per E-Mail einen "
						+ "Link erhalten, mit dem Sie Ihre "
						+ "Anzeige freischalten und bearbeiten k&ouml;nnen.");
			
			} else if ("advertisement-fail".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Interner Fehler: Ihre Anzeige konnte aufgrund eines Datenbankfehlers nicht erstellt werden! Bitte wenden Sie sich an den Systemadministrator.</b>");
			
			} else if ("advertisement-edit-fail".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Interner Fehler: Ihre Anzeige konnte aufgrund eines Datenbankfehlers nicht aktualisiert werden! Bitte wenden Sie sich an den Systemadministrator.</b>");
			
			} else if ("advertisement-edit".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Ihre Anzeige wurde erfolgreich aktualisiert!</b>");
			
			} else if ("advertisement-deleted".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Sie haben Ihre Anzeige erfolgreich gel&ouml;scht!</b>");
			
			} else if ("advertisement-notDeleted".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Beim L&ouml;schen Ihrer Anzeige ist ein Fehler aufgetreten! M&ouml;glicherweise wurde die Anzeige bereits gel&ouml;scht und existiert nichtmehr oder die Verbindung zur Datenbank wurde unterbrochen.</b>");
			
			} else if ("advertisement-notFound".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Es konnte keine Anzeige mit dieser ID gefunden werden.</b>");
			
			} else if ("advertisement-auth".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Ihre Anzeige wurde erfolgreich freigeschaltet und ist nun im System sichtbar!</b>");
			
			} else if ("advertisement-activate".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Ihre Anzeige wurde erfolgreich verlängert und ist weiterhin im System sichtbar!</b>");
			
			} else if ("advertisement-reactivate".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Ihre Anzeige wurde erneut erfolgreich freigeschaltet und ist nun wieder im System sichtbar!</b>");
			
			} else if ("advertisement-deletion-aborted".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Sie haben den L&ouml;schvorgang erfolgreich abgebrochen!</b>");
			
			} else if ("advertisement-deleted-followerFail".equals(source)) {
				out.println("<img src=\"warning.png\" alt=\"Hinweis!\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Ihre Anzeige wurde gel&ouml;scht, allerdings konnten nicht alle Interessenten dar&uuml;ber informiert werden!</b>");
			
			} else if ("advertisement-moderated".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich!\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Das System befindet sich im moderierten Zustand! Das bedeutet, dass Ihre Anzeige von einen Moderator freigeschaltet werden muss!</b><br></br>Es wurde eine E-Mail an einen Moderator versandt, damit dieser Ihre Anzeige freischaltet.");
			
			} else if ("customerMessage".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Die E-Mail wurde erfolgreich versendet.</b>");
			
			} else if ("db".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Fehler!\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Interner Fehler: Der Server konnte keine Verbindung zur Datenbank aufbauen! Bitte wenden Sie sich an den Systemadministrator</b>");
			
			} else if ("follower".equals(source)) {
				out.println("<br></br><img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Vielen Dank für Ihre Interessensbekundung!</b>");
				out.println("Damit Sie als Interessent anerkannt werden, m&uuml;ssen Sie diesen Eintrag noch best&auml;tigen! Folgen Sie dazu dem Link, den Sie per E-Mail erhalten haben.");
			
			} else if ("follower-trusted".equals(source)) {
				out.println("<br></br><img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Vielen Dank für Ihre Interessensbekundung!</b>");
				out.println("Hiermit wurden Sie automatisch als aktiver Interessent dieser Anzeige hinzugefügt.");
			
			} else if ("follower-fail".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen!\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Interner Fehler: Aufgrund eines Datenbankfehlers konnten Sie nicht als Interessent eingetragen werden!</b>");
			
			} else if ("follower-notDeleted".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Beim L&ouml;schen Ihrer Daten ist ein Fehler aufgetreten! M&ouml;glicherweise wurden die Daten bereits gel&ouml;scht und existieren nichtmehr oder die Verbindung zur Datenbank wurde unterbrochen.</b>");
			
			} else if ("follower-notUpdated".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Beim Aktualisieren Ihrer Daten ist ein Fehler aufgetreten! M&ouml;glicherweise wurden die Daten bereits gel&ouml;scht und existieren nichtmehr oder die Verbindung zur Datenbank wurde unterbrochen.</b>");
			
			} else if ("follower-deleted".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Ihre Interessensbekundung wurde erfolgreich zur&uuml;ckgenommen. Sie sind nun nicht mehr Interessent dieser Anzeige!</b>");
			
			} else if ("follower-updated".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Ihre Daten wurden erfolgreich aktualisiert.</b>");
			
			} else if ("follower-authed".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Sie haben sich erfolgreich authentifiziert und sind nun als Interessent dieser Anzeige best&auml;tigt.</b>");
			
			} else if ("follower-authenticateMailFail".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Aufgrund eines Fehlers konnte der Anzeigenersteller nicht von Ihrer Anmeldung benachrichtigt werden. Sie wurden trotzdem freigeschalten.</b>");
			
			} else if ("No-follower".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Es konnte kein Interessent mit dieser ID gefunden werden.</b>");
			
			} else if ("follower-moderated".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich!\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Das System befindet sich im moderierten Zustand! Das bedeutet, dass Ihre Daten von einen Moderator freigeschaltet werden m&uuml;ssen!</b><br></br>Es wurde eine E-Mail an einen Moderator versand, damit dieser Ihre Daten freischaltet.</b>");
			
			} else if ("follower-twice".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen!\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Sie haben sich mit dieser E-Mail schon bei dieser Anzeige eingetragen.</b>");
			
			} else if ("id".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Fehler!\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Die von Ihnen aufgerufene Anzeige existiert nicht, oder nicht mehr!</b>");
			
			} else if ("link-changed".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Ihr Link wurde erfolgreich ge&auml;ndert.</b>");
			
			} else if ("link-sended".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Ihnen wurde ein neuer Link per E-Mail gesendet.</b>");
			
			} else if ("positiveAdvertDeactivation".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Die Anzeige wurde deaktiviert.</b>");
			
			} else if ("PositiveDeleteAdvert".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Die Anzeige wurde gelöscht.</b>");
			
			} else if ("sending-mail-failed".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Es konnte leider keine Email versendet werden.</b>");
			
			} else if ("setup-dbinitialization-successful".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Die Datenbank wurde erfolgreich angelegt.</b>");
				out.println("<br></br>");
				out.println("<a href=\"./setup.jsp\">Klicken Sie hier, um zur Setupseite zu gelangen.</a>");
			
			} else if ("setup-dbinitialization-failed".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen!\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Das Anlegen der Datenbank ist leider gescheitert.</b>");
				out.println("<br></br>");
				out.println("<a href=\"./setup.jsp\">Klicken Sie hier, um zur Setupseite zu gelangen.</a>");
			
			} else if ("setup-insertsampledata-successful".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Die Beispieldaten wurden erfolgreich hinzugefügt.</b>");
			
			} else if ("setup-insertsampledata-failed".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen!\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Das Hinzufügen der Beispieldaten ist leider gescheitert.</b>");
				out.println("<br></br>");
				out.println("<a href=\"./setup.jsp\">Klicken Sie hier, um zur Setupseite zu gelangen.</a>");
			
			} else if ("user-dont-exist".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Der angegebene Benutzer existiert nicht.</b>");
			
			} else if ("user-dont-exist-byEmail".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Es existiert kein Benutzer mit dieser Emailadresse.</b>");
			
			} else if ("user-data-changed".equals(source)) {
				out.println("<img src=\"OK.png\" alt=\"Aktion erfolgreich\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Die Daten wurden erfolgreich ge&auml;ndert.</b>");
			
			} else if ("user-data-fail".equals(source)) {
				out.println("<img src=\"cross.png\" alt=\"Aktion fehlgeschlagen\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>Die Daten konnten aufgrund eines Datenbankfehlers nicht ge&auml;ndert werden.</b>");
			
			} else {
				out.println("<img src=\"cross.png\" alt=\"Fehler!\" border=\"0\" width=\"25\" height=\"25\"></img>");
				out.println("<b>" + source + "</b>");
			}
		%>
	</p>
	
	<br></br>
	<br></br>
	
	<!-- Falls adId existiert  -->
	<!-- Definiert Button zurück zur Anzeige -->
	<c:if test="${adId != null && adId != ''}">
		<form action="./Main" method="post">
			<!-- Falls eingeloggt -->
        	<c:if test="${userId != null && userId != ''}">
				<input type="hidden" name="userId" value="${userId}"></input>
			</c:if>
			<input type="hidden" name="AdDetail" value="AdDetail"/>
    		<input type="hidden" name="adId" value="${adId}"></input>
    		<input type="submit" class="button" value="Anzeige betrachten"></input>
		</form>
	</c:if>
	
	<!-- Falls kein Datenbankfehler -->
	<!-- Definiert Button zur Hauptseite -->
	<c:if test="${source != 'db'}">
		<form action="./Main" method="post">
			<c:if test="${userId != null && userId != ''}">
				<input type="hidden" name="userId" value="${userId}"></input>
			</c:if>
    		<input type="submit" class="button" class="button"
    		 value="Zur&uuml;ck zur Hauptseite"></input>
		</form>
		
		<!-- Falls eingeloggt: Definiert Button zum persönlichen Bereich -->
		<c:if test="${userId != null && userId != ''}">
    		<form action="./PersonalSection" method="post">
    			<input type="hidden" name="userId" value="${userId}"></input>
    			<input type="submit" class="button" value="Zum pers&ouml;nlichen Bereich">
    		</form>
    	</c:if>
    </c:if>

<%@ include file="include/Tail.html" %>