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
<%@page import="model.Ad"%>
<%@page import="controller.DBAdministration"%>
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
<%@ include file="include/Head.html" %>

<!-- Anzeige löschen -->

   	<title>IMP - Anzeige löschen</title>
   	<div id="breadcrumb">
      	<div class="tx-visrootline-pi1">
         	<a class="rootlink" href="http://www.change.me">Institution</a>&nbsp;&rArr;&nbsp;
         	<a href="http://www.change.me/home.html" target="_self" class="rootlink">Informatik</a>&nbsp;&rArr;&nbsp;
         	<a href="/IMP/index.jsp" target="_self" class="rootlink">Marktplatz</a>&nbsp;&rArr;&nbsp;Anzeige l&ouml;schen
      	</div>
   	</div>	
   		
<%@ include file="include/Sidebars.html" %>
 

 	<%
 		String adId = request.getParameter ("adId");
 		String userId = request.getParameter ("userId");
 		
 		String title = (String) request.getAttribute("title");
 		String forename = (String) request.getAttribute ("forename");
 		String surname = (String) request.getAttribute ("surname");
 		String email =(String) request.getAttribute ("email");
 		String projectStart = (String) request.getAttribute ("projectStart");
 		String deactivationDate = (String) request.getAttribute ("deactivationDate");
 		String description = (String) request.getAttribute ("description");
 		String followerCount = (String) request.getAttribute ("followerCount");
  	%>
 
	<form action="./DeleteAdvertisment" method="post">

Sie sind im Begriff, die folgende Anzeige zu l&oml;schen:
      	<!-- Table mit Eingabeflächen -->
      	<!-- Table hat 2 Spalten, d.h. die Form <tr><td>...</td><td>...</td></tr> -->
      	<table bgcolor="#eeeeee">
         	<tr>
            	<td>
               		Titel der Anzeige:
            	</td>
            	<td>
            		<%=title %>
            	</td>
         	</tr>
         	
         	<tr>
            	<td>
               		Vorname:
            	</td>
            	<td>
               		<%=forename %>
            	</td>
         	</tr>
         	
         	<tr>
            	<td>
               		Nachname:
            	</td>
            	<td>
               		<%=surname %>
            	</td>
         	</tr>
         	
         	<tr>
            	<td>
               		E-Mail-Adresse:
            	</td>
            	<td>
               		<%=email %>
            	</td>
         	</tr>
         
         	<tr>
            	<td>
               		Projektstart:
            	</td>
            	<td>
               		<%=projectStart %>
            	</td>
         	</tr>
         	<%--
         	<tr>
         		<td>
         			Deaktivierungsdatum:
         		</td>
         		<td>
         			<%=deactivationDate%>
         		</td>
         	</tr>
         	 --%>	
         	<tr>
            	<td>
               		Beschreibung:
            	</td>
            	<td>
               		<%=description%>
            	</td>
         	</tr>  
         	<tr>
            	<td colspan="2">
            		Anzahl der Interessenten: <%= followerCount%>
            	</td>
         	</tr>
         	
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
         	<tr>
            	<td></td>
            	<td>
            		<input type="hidden" name="userId" value="${userId}"></input>
         			<input type="hidden" name="adId" value="${adId}"></input>
            		<input type="submit" class="button" value="Anzeige löschen"/>
            	</td>
         	</tr>
      	</table>
	</form>
   	
   	<br />
   	<br />
   
   <!-- Definiert Button zur Hauptseite -->
   	<form action="./Main" method="post">
		<c:if test="${userId != null && userId != ''}">
			<input type="hidden" name="userId" value="${userId}"/>
		</c:if>
		<input type="submit" class="button" value="Zur&uuml;ck zur Hauptseite" title="(Anzeige wird nicht gelöscht)"/>
   	</form>
   	
   	<!-- Falls eingeloggt: Definiert Button zum persönlichen Bereich -->
	<c:if test="${userId != null && userId != ''}">
   		<form action="./PersonalSection" method="post">
      		<input type="hidden" name="userId" value="${userId}"></input>
      		<input type="submit" class="button" value="Zum pers&ouml;nlichen Bereich" title="(Ihre Anzeige wird nicht gelöscht)" />
      	</form>
   	</c:if>
   	
<%@ include file="include/Tail.html" %>