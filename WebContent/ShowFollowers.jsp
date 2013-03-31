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
<%@page import="controller.DBAdministration"%>
<%@page import="java.util.LinkedList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ page import="model.Ad" %>
<jsp:useBean id="ad" scope="request" type="model.Ad" class="model.Ad"/>
<title>IMP - Interessenten anzeigen</title>

<%@ include file="include/Head.html" %>

<div id="breadcrumb">
    <div class="tx-visrootline-pi1">
        <a class="rootlink" href="http://www.change.me">Institution</a>&nbsp;&rArr;&nbsp;
        <a href="http://www.change.me/home.html" target="_self" class="rootlink">Informatik</a>&nbsp;&rArr;&nbsp;
        <a href="/IMP/index.jsp" target="_self" class="rootlink">Marktplatz</a>&nbsp;&rArr;&nbsp;Interessenten anzeigen
    </div>
</div>

<%@ include file="include/Sidebars.html" %>


<!-- ShowFollowers -->
<!-- 
	Anzeige:
		- Ad + Beschreibung
		- Bisherige Interessenten + Kommentar
	Aktionen:
		- 
		- zur Hauptseite
		- zum persönlichen Bereich (falls eingeloggt)
 -->
   <%
   		String adId = request.getParameter("adId");
   		String userId = request.getParameter("userId");
   		
   		String title = (String) request.getAttribute("title");
    	String description = (String) request.getAttribute ("description");
    	String followerCountMessage =(String) request.getAttribute ("followerCountMessage");    	

    	// Prüfe ob wirklich eine LinkedList<Follower> über den Request gekommen ist
		LinkedList<Follower> followerList = (LinkedList<Follower>) request.getAttribute ("followerList");
		
    %>
    <table> 
    <tr>
        <td>Titel der Anzeige:</td>
        <td colspan="2" align="left"><%=title %></td>
    </tr>
    <tr>
        <td>Beschreibung:</td>
        <td colspan="2" align="left"><%=description%></td>
    </tr>
    <% if (followerList.size() > 1) { %>
    <tr>
        <td></td>
        <td colspan="2" align="left">
        	<br />
			<a href="./SendMessage?messageToFollower=All&adId=${adId}&userId=${userId}">
    				Nachricht an alle Interessenten schicken
    		</a>
    		<a href="./SendMessage?messageToFollower=All&adId=${adId}&userId=${userId}">
    			<img src="email-regular.png" alt="E-Mailicon" border="0" width="14" height="14" title="${toolTippEmailContact}" />
    		</a>
         </td>
     </tr>
	<% } %>
     <tr>
        <td></td>
        <td colspan="2" align="left">
        	<br />
        		<u>
        			<%out.println (followerCountMessage); %>
            	</u>
         </td>
     </tr>

   <%
    for (Follower item: followerList)
    {
    	%>
    	<tr>
    		<td></td>
    		<td>
    			<a href="./SendMessage?messageToFollower=One&adId=${adId}&userId=${userId}&followerEmail=<%=item.getEmail()%>">
    				<b><%= item.getForname() + " " + item.getSurname()%></b>
    			</a>
    			<a href="./SendMessage?messageToFollower=One&adId=${adId}&userId=${userId}&followerEmail=<%=item.getEmail()%>">
    			<img src="email-regular.png" alt="E-Mailicon" border="0" width="14" height="14" title="${toolTippEmailContact}" />
    			</a>
    		</td>
    		<td><%out.println ("Kommentar: " + item.getComment()); %></td>
    	</tr>
    	<%	
    }
    
    %>
    </table>
    
    <br></br>
    <br></br>
    
    <!-- Definiert Button zur Hauptseite -->
    <form action="./Main" method="post">
        <c:if test="${userId != null && userId != ''}">
        	<input type="hidden" name="userId" value="${userId}"></input>
        </c:if>
        <input type="submit" class="button" value="Zur&uuml;ck zur Hauptseite"></input>
    </form>
    
    <!-- Falls eingeloggt: Definiert Button zum persönlichen Bereich -->
    <c:if test="${userId != null && userId != ''}">
    <form action="./PersonalSection" method="post">
        <input type="hidden" name="userId" value="${userId}"></input>
        <input type="submit" class="button" value="Zum pers&ouml;nlichen Bereich">
    </form>
    </c:if>

<%@ include file="include/Tail.html" %>