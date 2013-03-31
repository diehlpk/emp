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

<%@page import="model.Ad"%>
<%@page import="controller.DBAdministration"%>
<%@page import="model.Follower"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Interessent bearbeiten</title>

<%@ include file="include/Head.html" %>

<div id="breadcrumb">
	<div class="tx-visrootline-pi1">
		<a class="rootlink" href="http://www.change.me">Institution</a>&nbsp;&rArr;&nbsp;
		<a href="http://www.change.me/home.html" target="_self" class="rootlink">Informatik</a>&nbsp;&rArr;&nbsp;
		<a href="/IMP/index.jsp" target="_self" class="rootlink">Marktplatz</a>&nbsp;&rArr;&nbsp;Interesse zurückziehen
	</div>
</div>

<%@ include file="include/Sidebars.html" %>

<!-- Follower bearbeiten -->
<!--
	Anzeige:
		- Fehlermeldung, falls keine follower.id existiert
		- Titel der Anzeige
		- Initiator (hier Ersteller)
		- Institut
		- Projektart
	Eingabe:
		- Vorname
		- Nachname
	Aktionen:
	 	- Interesse zurückziehen
	 	- Zur Hauptseite
	 	- zum persönlichen Bereich (falls eingeloggt)
 -->

	<%
		String userId = (String) request.getAttribute("userId");
		String adId = (String) request.getAttribute("adId");
	
		String titleAd = (String) request.getAttribute("titleAd");
		String nameInitiatorAd = (String) request.getAttribute("nameInitiatorAd");
		String instituteAd = (String) request.getAttribute("instituteAd");
		String projectTypeAd = (String) request.getAttribute("projectTypeAd");
	%>

	<c:if test="${userId == null || userId == ''}">
		<img src="cross.png" alt="Fehler!" border="0" width="25" height="25"/>
		<b>Es wurden keine passenden Daten gefunden!</b>
		<br></br>
	</c:if>
	
	<c:if test="${userId != null && userId != ''}">
		<p>Sie sind im Begriff, Ihr Interesse an folgender Anzeige zurückzuziehen:</p>
	
		<table>
			<tr>
				<td>Titel der Anzeige:</td>
				<td><b><%=titleAd %></b></td>
			</tr>
		
			<tr>
				<td>Ersteller der Anzeige:</td>
				<td><b><%=nameInitiatorAd %></b></td>
			</tr>
		
			<tr>
				<td>Betreuendes Institut:</td>
				<td><b><%=instituteAd %></b></td>
			</tr>
		
			<tr>
				<td>Art des Projekts:</td>
				<td><b><%=projectTypeAd %></b></td>
			</tr>
		</table>
	</c:if>
	
	<%
		String userForename = (String) request.getAttribute("userForename");
		String userSurname = (String) request.getAttribute("userSurname");
	%>
	<form action="./EditFollower" method="post">
		<table>
			<tr>
    			<td>
        			Vorname:
        		</td>
        		<td>
        			<b><%=userForename %></b>	
            	</td>       	
        	</tr>
        
        	<tr>
            	<td>
            		Nachname:
            	</td>
            	<td>
            		<b><%=userSurname %></b>
            	</td>
            </tr>       	
        </table>
      
        <input type="hidden" name="deleteFollower" value="deleteFollower" />
        <input type="hidden" name="userId" value="${userId}"/>
        <input type="hidden" name="adId" value="${adId }"/>
        
		<input type="submit" class="button" value="Interesse zur&uuml;ckziehen" name="delete"/>
	</form>
	
	<br></br>
	
	<!-- Definiert Button zur Hauptseite -->
	<form action="./Main" method="post">
        <c:if test="${userId != null && userId != ''}">
			<input type="hidden" name="userId" value="${userId}"/>
		</c:if>
    	<input type="submit" class="button" value="Zur Hauptseite" title="Ihre &Auml;nderungen werden nicht gespeichert."/>
	</form>
	
	<!-- Definiert Button zum persönlichen Bereich -->
	<c:if test="${userId != null && userId != ''}">
		<form action="./PersonalSection" method="post">
			<input type="hidden" name="userId" value="${userId}"/>
			<input type="submit" class="button" value="Zum pers&ouml;nlichen Bereich" title="Ihre &Auml;nderungen werden nicht gespeichert."/>
    	</form>
    </c:if>

<%@ include file="include/Tail.html" %>