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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IMP - Pers&ouml;nliche Daten bearbeiten</title>

<%@ include file="include/Head.html" %>

<div id="breadcrumb">
    <div class="tx-visrootline-pi1">
        <a class="rootlink" href="http://www.change.me">Institution</a>&nbsp;&rArr;&nbsp;
        <a href="http://www.change.me/home.html" target="_self" class="rootlink">Informatik</a>&nbsp;&rArr;&nbsp;
        <a href="/IMP/index.jsp" target="_self" class="rootlink">Marktplatz</a>&nbsp;&rArr;&nbsp;
    </div>
</div>

<%@ include file="include/Sidebars.html" %>

<%-- !!!Soll und wird im Moment auch nicht genutzt!!! --%>

<!-- Edit User -->
<!-- 
	Eingabe:
		- akademischer Titel
		- Vorname
		- Nachname
		- Email
	Aktionen:
		- Änderung speichern
		- Zur Hauptseite
		- Zum persönlichen Bereich (falls eingeloggt)
 -->
 
	<form action="./EditUser" method="post">
    	<table>
        	<tr>
            	<td>
                	<label for="academicTitle">Akademischer Titel:</label>
                </td>
                <td>
                	<input type="text" id="academicTitle" name="academicTitle" size="20"  value="${user.academicTitle}" />
                	(optional)
                	<a title="${academicTitle}">
                    	<c:if test="${academicTitle != null}">
                    		<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
                    	</c:if>
                	</a>
                </td>
            </tr>
            
            <tr>
                <td>
                	<label for="forename"><span class="mandatoryinput">Vorname:</span></label>
                </td>
                <td>
                	<input type="text" id="forename" name="forename" size="20"  value="${user.forename}" />
                	<a title="${forename}">
                    	<c:if test="${forename != null}">
                    		<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
                    	</c:if>
                	</a>
                </td>
            </tr>
            
            <tr>
                <td>
                	<label for="surname">
                		<span class="mandatoryinput">Nachname:</span>
                	</label>
                </td>
                <td>
                	<input type="text" id="surname" name="surname" size="20"  value="${user.surname}" />
                	<a title="${surname}">
                    	<c:if test="${surname != null}">
                    	<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
                    	</c:if>
                	</a>
                </td>
            </tr>
            
            <tr>
                <td>
                	<label for="eMail">E-Mail-Adresse</label>
					<a title="Eine nachtr&auml;gliche &Auml;nderung Ihrer E-Mail-Adresse ist nicht m&ouml;glich. Bitte kontaktieren Sie in einem solchen Fall den Systemadministrator.">
		  				<img src="querry.png" alt="?" border="0" width="20" height="20" /> 
					</a>
                </td>
                <td>
                	<input type="text" id="eMail" name="eMail" size="40" value="${user.eMail}" disabled />
                </td>
            </tr>
        </table>
        
        <!-- Falls eingeloggt -->
        <c:if test="${sid != null && sid != ''}">
        	<input type="hidden" name="sid" value="${sid}" />
        </c:if>
        
        <!-- Änderung speichern -->
        <input type="hidden" name="userId" value="${user.id}" />
        <input type="submit" name="changeData" value="speichern" />
    </form>

	<br></br>
    <br></br>
    
    <!-- Definiert Button zur Hauptseite -->
    <form action="./Main" method="post">
        <c:if test="${sid != null && sid != ''}">
        	<input type="hidden" name="sid" value="${sid}" />
        </c:if>
        <input type="submit" value="Zur Hauptseite (Ihre &Auml;nderungen werden nicht gespeichert))" />
    </form>
    
    <!-- Falls eingeloggt: Definiert Button zum persönlichen Bereich -->
    <c:if test="${sid != null && sid != ''}">
    	<form action="./PersonalSection" method="post">
        	<input type="hidden" name="sid" value="${sid}"></input>
        	<input type="submit" value="Zum pers&ouml;nlichen Bereich (Ihre &Auml;nderungen werden nicht gespeichert)">
    	</form>
    </c:if>

<%@ include file="include/Tail.html" %>