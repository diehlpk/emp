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

<%@page import="java.util.UUID"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
<title>Informatik-Marktplatz</title>
<jsp:useBean id="ad" scope="request" type="java.util.List<model.Ad>"
	class="java.util.LinkedList" />


<%@ page import="model.Ad"%>
<%@ page import="model.ElementOf"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<%@ include file="include/Head.html"%>

<div id="breadcrumb">
	<div class="tx-visrootline-pi1">
		<a class="rootlink" href="http://www.change.me">Institution</a>&nbsp;&rArr;&nbsp; <a
			href="http://www.change.me/home.html"
			target="_self" class="rootlink">Informatik</a>&nbsp;&rArr;&nbsp; <a
			href="/IMP/index.jsp" target="_self" class="rootlink">Marktplatz</a>
	</div>
</div>

<%@ include file="include/Sidebars.html"%>

<!-- Main -->
<!-- 
	Eingabe:
		- Suchwort
		- Filter: Projekttyp, Institut, ...
	Anzeige:
		- Ads (in einer Tabelle)
	Aktionen:
		- Neue Anzeige erstellen
		- zum persönlichen Bereich (falls eingeloggt)
		- Ausloggen (falls eingeloggt)
		- Link vergessen (falls nicht eingeloggt)
 -->	
	<h1>Informatik-Marktplatz</h1>
	<br></br>
	<h2>Informatik-Marktplatz - Was ist das?</h2>
	
	<p>Hier können Studierende und Dozenten sich zu gemeinsamen
		Projekten zusammenfinden, wie zum Beispiel Diplomarbeiten, Fachstudien,
		Lerngruppen, Hilfskraftstellen oder Softwarepraktika. Dazu bietet das
		System allen Nutzern die Möglichkeit, Anzeigen mit Angeboten oder
		Gesuchen zu erstellen. Interessenten an einer Anzeige k&ouml;nnen sich
		auf dem Marktplatz eintragen oder sich direkt mit dem Initiator in
		Verbindung setzen.
	</p>
	
	<br></br>
<%--
Dieser Teil ist in Zukunft in der Erweiterten Suche zu finden

	<form action="./FilterAdvertisement" method="post">			
		<table>
			<tr>
				<td>
					<label for="projectTypes">Projekt Typ:</label>
				</td>
				<td>
					<label for="institute">Institute:</label>
				</td>
			</tr>
			<tr>
				<td>
					<select name ="projectTypes" multiple="multiple" size="5">
						<option
						<%if (ElementOf.isElementOf("Alle", type)) 
						{
							out.println("selected=\"selected\"");
						}%>>
						Alle
					</option>
					<%
						for (String projectT : controller.Settings
								.getProperty("types")) {
					%>
					<option
						<%if (ElementOf.isElementOf(projectT, type)) {
						out.println("selected=\"selected\"");
						}%>>
						<%
							out.println(projectT);
						%>
					</option>
					<%
						}
					%>
					
					</select> 
					<a title="W&auml;hlen Sie die Projekttypen aus, die angezeigt werden sollen. Sie k&ouml;nnen mehrere Projekttypen ausw&auml;hlen, indem Sie [Strg] gedr&uuml;ckt halten.">
						<img src="querry.png" alt="Information" border="0" width="20"
							height="20"></img> 
					</a>
					
				</td>
				<td>
				<select name='institute' multiple="multiple" size="5">
					<option
						<%if (ElementOf.isElementOf("Alle", institutes)) 
						{
							out.println("selected=\"selected\"");
						}%>>
						Alle
					</option>
					<%
						for (String institute : controller.Settings
								.getProperty("institutes")) {
					%>
					<option
						<%if (ElementOf.isElementOf(institute, institutes)) {
						out.println("selected=\"selected\"");
						}%>>
						<%
							out.println(institute);
						%>
					</option>
					<%
						}
					%>
					<option
						<%if (ElementOf.isElementOf("Keinem Institut zugeordnet", institutes)) {
						out.println("selected=\"selected\"");
						}%>>
						Keinem Institut zugeordnet
					</option>
					</select> 
					<a title="W&auml;hlen Sie die Institute aus, die angezeigt werden sollen. Sie k&ouml;nnen mehrere Institute ausw&auml;hlen, indem Sie [Strg] gedr&uuml;ckt halten.">
						<img src="querry.png" alt="Information" border="0" width="20"
							height="20"></img> 
					</a>
				</td>
			</tr>
			<tr>
				<td><input type="submit" name="search" value="Suchen"></input></td>
				<td></td>
			</tr>		
		</table>
	</form>
 --%>
 	<table  cellpadding="0" cellspacing="0" border="0" class="display" id="AnzeigenTable">
		<thead>
			<tr>
			<%
				String isEmpty = (String) request.getAttribute("isEmpty");
				if (isEmpty.equals("false"))
				{
			%>
					<th id="theadBorderBottomRight">Titel</th>
					<th id="theadBorderBottomRight">Projekttyp</th>
					<th id="theadBorderBottomRight">Zielgruppe</th>
					<th id="theadBorderBottomRight">Institut</th>
					<th id="theadBorderBottomRight">Projektbeginn</th>
					<th id="theadBorderBottom">Erstellt</th>
			<%
				}
			%>
			</tr>
		</thead>
		<tbody>
			<%
				String userId = (String) request.getAttribute("userId");
				// Laenge der Arrays alle gleich
				String [] title = (String []) request.getAttribute("titleArray");
				String [] projectType = (String []) request.getAttribute("projectTypeArray");
				String [] targeted = (String []) request.getAttribute("targetedArray");
				String [] institute = (String []) request.getAttribute("instituteArray");
				String [] projectStart = (String []) request.getAttribute("projectStartArray");
				String [] adIdArray = (String []) request.getAttribute("adIdArray");
				String [] projectCreate = (String []) request.getAttribute("projectCreateArray");
				
				for (int i = 0; i < title.length; i++)
				{
					%>
						<tr class="even gradeU">
							<td>
							<%
							if (userId == null || userId.equals(""))
							{
								%><a href="./Main?AdDetail=AdDetail&adId=<%=adIdArray [i] %>"><%=title[i] %></a><%
							}
							else
							{
								%><a href="./Main?AdDetail=AdDetail&adId=<%=adIdArray [i] %>&userId=${userId}"><%=title[i] %></a><%
							}
							%>
            				</td>
							<td><%=projectType[i] %></td>
							<td><%=targeted[i] %></td>
							<td><%=institute [i] %></td>
							<td><%=projectStart [i] %></td>
							<td><%=projectCreate [i] %></td>
						</tr>
									
					<%
				}
			%>
		</tbody>
	</table>
 	<br/> <br/>

	<!-- Defininiert Button Neue Anzeige erstellen -->
	<form action="./Main" method="post">
		<c:if test="${userId != null && userId != ''}">
			<input type="hidden" name="userId" value="${userId}"/>
		</c:if>
		<input type="hidden" name="editAd" value="editAd"/>
		<input type="submit" class="button" value="Neue Anzeige erstellen" />
	</form>

	<!-- Falls Eingeloogt: -->
	<c:if test="${userId != null && userId != ''}">
		<!-- Definiert Button zum persönlichen Bereich -->
		<form action="./PersonalSection" method="post">
			<input type="hidden" name="userId" value="${userId}"/>
			<input type="submit" class="button" value="Zum pers&ouml;nlichen Bereich" />
		</form>
		
		<!-- Definiert Button ausloggen -->
		<form action="./Main" method="post">
			<input type="submit" class="button" value="Ausloggen" />
		</form>
	</c:if>

	<!-- falls nicht eingeloggt: Definiert Button persönlichen Link vergessen -->
	<c:if test="${userId == null || userId == ''}">
		<form action="./EditLink" method="post">
			<input type="hidden" name="requestLink" value="requestLink"/>
			<input type="submit" class="button" value="Pers&ouml;nlichen Link vergessen?" />
		</form>
	</c:if>

<%@ include file="include/Tail.html"%>