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

<%@page import="controller.DBAdministration"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
<title>Informatik Marktplatz</title>
<jsp:useBean id="ad" scope="request" type="java.util.List<model.Ad>"
	class="java.util.LinkedList" />


<%@page import="model.Ad"%>
<%@page import="model.ElementOf"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<%@ include file="include/Head.html"%>

<div id="breadcrumb">
<div class="tx-visrootline-pi1"><a class="rootlink"
	href="http://www.change.me">Institution</a>&nbsp;&rArr;&nbsp;<a
	href="http://www.change.me/home.html" target="_self"
	class="rootlink">Informatik</a>&nbsp;&rArr;&nbsp;<a
	href="/IMP/index.jsp" target="_self" class="rootlink">Marktplatz</a>&nbsp;&rArr;&nbsp;Setup</div>
</div>

<%@ include file="include/Sidebars.html"%>

<!-- Setup -->
<!--
	Aktionen:
		- Schema initialisieren
		- Beispieldaten einfüllen 
		- Zur Hauptseite
 -->
	<%
		if (!DBAdministration.isSchemaCreated()) 
		{
			%>
				<div>
					Sie haben Imp zum ersten Mal ausgeführt. Bitte initialisieren Sie die Datenbank mit einem Klick auf folgenden Button:
				</div>
				<form action="./Setup" method="post">
					<input type="hidden" name="action" value="initializetables"/>
					<input type="submit" class="button" value="Datenbank initialisieren"/>
				</form>
			<%
		}
		
		if (DBAdministration.isSchemaCreated() && DBAdministration.isEmpty()) 
		{
			%>
				<div>
					Nun haben Sie die Möglichkeit automatisch Beispieldaten in die Datenbank eintragen zu lassen. Falls Sie dies nicht möchten klicken Sie bitte <a href="./Main">hier</a>. 
				</div>
				<form action="./Setup" method="post">
					<input type="hidden" name="action" value="insertsampledata"/>
					<input type="submit" class="button" value="Beispieldaten einfüllen"/>
				</form>
			<%
		}
	%>

	<br></br>
	<br></br>
	<!-- Link zur Hauptseite -->
	<a href="./Main">Klicken Sie hier, um zur Hauptseite zu gelangen.</a>

<%@ include file="include/Tail.html"%>