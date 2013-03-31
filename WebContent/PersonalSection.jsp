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

<%@page import="model.Follower"%>
<%@page import="model.User"%>
<%@page import="model.Ad"%>
<%@page import="controller.DBAdministration"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IMP - Pers&ouml;nlicher Bereich</title>

<%@ include file="include/Head.html" %>

<div id="breadcrumb">
   <div class="tx-visrootline-pi1">
      <a class="rootlink" href="http://www.change.me">Institution</a>&nbsp;&rArr;&nbsp;
      <a href="http://www.change.me/home.html" target="_self" class="rootlink">Informatik</a>&nbsp;&rArr;&nbsp;
      <a href="/IMP/index.jsp" target="_self" class="rootlink">Marktplatz</a>&nbsp;&rArr;&nbsp;Pers&ouml;nlicher Bereich
   </div>
</div>

<%@ include file="include/Sidebars.html" %>

<!-- Persönlicher Bereich -->
<!-- 
	Anzeige:
		- Eigene Ads
		- Ads an denen der User interessiert ist (interessante Ads)
	Aktionen:
		- Anzeige bearbeiten (für jede eigene Ad)
		- Anzeige aktivieren (für jede neue eigene Ad)
		- Interessenten anzeigen (für jede eigene Ad)
		- Interesse ändern  (für jede interessante Ad)
		- Interesse bestätigen (für jede interessante Ad)
		- Neue Anzeige erstellen
		- Eigene Daten ändern
		- Persönlichen Link ändern
		- Ausloggen
		- Zur Hauptseite
 -->


	Anzeigen die Sie erstellt haben:
	<br/>
	<%
		String userId = request.getParameter("userId");

		String [] titleArray = (String []) request.getAttribute("titleArray");
		String [] projectTypeArray = (String []) request.getAttribute("projectTypeArray");
		String [] targetedArray =(String []) request.getAttribute("targetedArray");
		String [] creationDateArray = (String []) request.getAttribute("creationDateArray");
		String [] projectStartArray = (String []) request.getAttribute("projectStartArray");
		String [] restDays = (String []) request.getAttribute("restDays");
		String [] adIdArray = (String []) request.getAttribute("adIdArray");
		boolean [] isActivArray = (boolean []) request.getAttribute("isActivArray");
		String [] followerCount = (String []) request.getAttribute("followerCountArray");
		
	%>
	<table class="impstyletable">
   		
      	<% 
      		if (titleArray.length > 0) 
      		{ %>
      		<tr>
      			<th>Titel</th>
      			<th>Projekttyp</th>
      			<th>Zielgruppe</th>
      			<th>Erstellungsdatum</th>
      			<th>Projektstart</th>
      			<th>Deaktivierung in</th>
      			<th>Interessenten</th>
      			<th></th>
      		</tr>
      		<% }
      		else
      		{
      			%>
      			<b>Derzeit haben sie keine Anzeigen erstellt.</b>
      			<br/>
      			<%
      		}
      	%>
   		
   		<%
   			for (int i = 0; i < titleArray.length; i++)
   			{   				
   			%>	
   				<tr>
      				<td><%= titleArray [i]%></td>
      				<td><%= projectTypeArray [i]%></td>
      				<td><%=targetedArray[i]%></td>
      				<td><%=creationDateArray [i]%></td>
      				<td><%=projectStartArray [i]%></td>
      				<td><%=restDays [i]%></td>
      				<td><a href="./PersonalSection?showFollowers=showFollowers&adId=<%=adIdArray [i] %>&userId=<%=userId%>">Sie haben <%=followerCount[i] %> Interessenten</a><br/></td>
      				<td>         			
         				<%if (isActivArray [i] == false){%>
         					<a href="./PersonalSection?activateAd=activateAd&adId=<%=adIdArray [i] %>&userId=<%=userId%>">Aktivieren</a><br/>
         					<a href="./Main?editAd=editAd&adId=<%=adIdArray [i]%>&userId=<%=userId%>">Bearbeiten</a><br/>
         					<a href="./PersonalSection?deleteAd=deleteAd&adId=<%=adIdArray [i]%>&userId=<%=userId %>">Löschen</a><br/>
         					<a href="./Main?AdDetail=AdDetail&adId=<%=adIdArray [i] %>&userId=<%=userId%>">Vorschau</a>
         				<%} %>
         
         				<%if (isActivArray [i] == true) {%>	
         					<a href="./Main?editAd=editAd&adId=<%= adIdArray [i]%>&userId=<%=userId%>">Bearbeiten</a><br/>
         					<a href="./PersonalSection?activateAd=activate&adId=<%=adIdArray [i] %>&userId=<%=userId%>">Ablaufdatum erneuern</a><br/>
         					<a href="./PersonalSection?deactivateAd=deactivateAd&adId=<%=adIdArray [i] %>&userId=<%=userId%>">Deaktivieren</a><br/>
         					<a href="./PersonalSection?deleteAd=deleteAd&adId=<%=adIdArray [i]%>&userId=<%=userId %>">Löschen</a><br/>
         					<a href="./Main?AdDetail=AdDetail&adId=<%=adIdArray [i] %>&userId=<%=userId%>">Vorschau</a>
         				<%} %>
      				</td>
      			</tr>
      		<%
   			}
   		%>
			</table>

	<table class="impstyletable">
   		
      	<%
      	
      	String [] titleFollow = (String[]) request.getAttribute("titleFollow");
      	String [] projectTypeFollow = (String[]) request.getAttribute("projectTypeFollow");
      	String [] targetedFollow = (String []) request.getAttribute("targetedFollow");
      	String [] instituteFollow = (String []) request.getAttribute("instituteFollow");
      	String [] creationDateFollow = (String []) request.getAttribute("creationDateFollow");
      	String [] adIdFollow = (String []) request.getAttribute("adIdFollow");
      	
      
      	if (titleFollow.length > 0) 
      	{ 
      	%>
      	<br></br>
		Angebote und Gesuche zu denen Sie Ihr Interesse bekundet haben:
		<br/>
      	<tr>
      		<th>Titel</th>
      		<th>Projekttyp</th>
      		<th>Zielgruppe</th>
      		<th>Institut</th>
      		<th>Erstellungsdatum</th>
      		<th></th>
      	</tr>
      	<% 
      	}
      	
      	for (int i = 0; i < titleFollow.length; i++)
      	{
      	%>
      	<tr>
      		<td><%= titleFollow [i]%></td>
      		<td><%= projectTypeFollow [i]%></td>
      		<td><%= targetedFollow [i]%></td>
      		<td><%= instituteFollow [i]%></td>
      		<td><%= creationDateFollow [i]%></td>
      		<td>
      			<a href="./Main?changeInterest=changeInterest&adId=<%=adIdFollow [i]%>&userId=<%=userId%>">Interesse zur&uuml;ckziehen</a><br/>		      				
      			<a href="./Main?AdDetail=AdDetail&adId=<%=adIdFollow [i] %>&userId=<%=userId%>">Anzeige betrachten</a><br/>
      		</td>
      	</tr>
      	<%
      	}
      	%>
     </table>
	
	

	<!-- Definiert Button Neue Ad erstellen -->
	<form action="./Main" method="post">
 		<input type="hidden" name="userId" value="${userId}" />
   		<input type="hidden" name="editAd" value="editAd"/>
   		<input type="submit" class="button" value="Neue Anzeige erstellen" />
	</form>

	<!-- Definiert Button Persönlichen Link ändern -->
<%-- 	<form action="./EditLink" method="post">
   		<c:if test="${userId != null && userId != ''}">
   			<input type="hidden" name="userId" value="${userId}"></input>
   		</c:if>
   		<input type="submit" value="Pers&ouml;nlichen Link &auml;ndern"></input>
	</form>
--%>   
   <!-- Definiert Button Ausloggen -->
   <form action="./Main" method="post">
      <input type="submit" class="button" value="Ausloggen"></input>
   </form>


	<!-- Definiert Button Zur Hauptseite -->
	<form action="./Main" method="post">
   		<c:if test="${userId != null && userId != ''}">
   			<input type="hidden" name="userId" value="${userId}"/>
   		</c:if>
   		<input type="submit" class="button" value="Zur&uuml;ck zur Hauptseite"></input>
	</form>

<%@ include file="include/Tail.html" %>