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

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>

<%@ page language="java" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="ad" scope="request" type="model.Ad" class="model.Ad"/>
<title>IMP - Anzeige l&ouml;schen</title>
<div id="breadcrumb">
 	<div class="tx-visrootline-pi1">
		<a class="rootlink" href="http://www.change.me">Institution</a>&nbsp;&rArr;&nbsp;
		<a href="http://www.change.me/home.html" target="_self" class="rootlink">Informatik</a>&nbsp;&rArr;&nbsp;
		<a href="/IMP/index.jsp" target="_self" class="rootlink">Anzeige l&ouml;schen</a>
	</div>
</div>

<%@ include file="include/Head.html" %>
<%@ include file="include/Sidebars.html" %> 

<!-- Löschen einer Anzeige -->
<!-- 
	Zeigt an:
		- Titel
		- Initiator (hier Ersteller)
		- Institut
		- Projektart
		
	Mögliche Aktionen:
		- Bestätigen
		- Abrechen		
 -->
	<img src="warning.png" alt="Hinweis" border="0" width="25" height="25"></img>
	<b>Sind Sie sicher, dass Sie die folgende Anzeige l&ouml;schen möchten?</b>
	<br></br>

	<%
		String title = (String) request.getAttribute ("title");
		String name = (String) request.getAttribute ("name");
		String institute = (String) request.getAttribute ("institute");
		String projectType = (String) request.getAttribute ("projectType");
		
	%>
	<table>
	<tr>
		<td>Titel der Anzeige:</td>
		<td><%=title %></td>
	</tr>
	
	<tr>
		<td>Ersteller der Anzeige:</td>
		<td><%=name %></td>
	</tr>
	
	<tr>
		<td>Betreuendes Institut:</td>
		<td><%=institute %></td>
	</tr>
	
	<tr>
		<td>Art des Projekts:</td>
		<td><%=projectType %></td>
	</tr>
	
	</table>

	<br></br>

	<form action="./Confirm" method="post">
		<c:if test="${userId != null && userId != ''}">
			<input type="hidden" name="userId" value="${userId}"></input>
		</c:if>
		<input type="hidden" name="adId" value="${adId}"></input>
		
		<table>
			<tr>
				<td colspan="2" align="center">
					<input type="submit" name="confirm" value="Ja, diese Anzeige l&ouml;schen!"></input>
				</td>
				<td>
					<input type="submit" name="confirm" value="Nein, diese Anzeige nicht l&ouml;schen!"></input>
				</td>
			</tr>
		</table>
	</form>
	

<%@ include file="include/Tail.html" %>