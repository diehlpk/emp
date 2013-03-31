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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Informatik-Marktplatz - Pers&ouml;nlichen Link &auml;ndern</title>

<%@ include file="include/Head.html" %>

<div id="breadcrumb">
	<div class="tx-visrootline-pi1">
		<a class="rootlink" href="http://www.change.me">Institution</a>&nbsp;&rArr;&nbsp;
		<a href="http://www.change.me/home.html" target="_self" class="rootlink">Informatik</a>&nbsp;&rArr;&nbsp;
		<a href="/IMP/index.jsp" target="_self" class="rootlink">Marktplatz</a>&nbsp;&rArr;&nbsp;Pers&ouml;nlichen Link vergessen?
	</div>
</div>

<%@ include file="include/Sidebars.html" %>

<!-- 	<p>Durch die &Auml;nderung Ihres Links f&uuml;r den pers&ouml;nlichen Bereich wird Ihr alter Link automatisch ung&uuml;ltig und Sie erhalten per E-Mail einen neuen Link.</p> -->
	<p>
		<img src="warning.png" alt="Hinweis!" border="0" width="25" height="25" /img>
		Diese Aktion ist nur notwendig, wenn Sie Ihren bisherigen Link vergessen haben.
	</p>
	
<!--
	Edit Link:
	 	Eingabe:
	 		- Email
	 		- Captcha
		Aktionen:
			- Bestätigen: Ja (falls eingeloggt)
			- Bestätigen: Nein (falls eingeloggt)
			- Neuen Link senden (nicht eingeloggt)
			- Zur Hauptseite
 -->
    <form action="./EditLink" method="post">
    	
    	<!-- Falls eingeloggt -->
    	<!-- 
		<c:if test="${userId != null && userId != ''}">
    		<input type="hidden" name="userId" value="${userId}"/>
    		<table>
    			<tr>
    				<td>
    					M&ouml;chten Sie Ihren pers&ouml;nlichen Link wirklich &auml;ndern?
    				</td>
    			</tr>
		
				<tr>
					<td colspan="2">
						<input type="hidden" name="changeLink" value="changeLink"/>
						<input type="submit" class="button" name="confirm" value="Ja"></input>
					</td>
				</tr>
			</table>
		</c:if> -->

		<!-- Falls nicht eingeloggt -->
		<c:if test="${userId == null || userId == ''}">
			<table>
    			<tr>
    				<td>
						<label for="email">
							<span class="mandatoryinput">E-Mail-Adresse:</span>
						</label>
						<br />
    					<input type="text" id="email" name="email" size="40"  value="${email}"/>
						<a title="${toolTippEmail}">
							<img src="querry.png" alt="Information" border="0" width="20" height="20"></img>
						</a>
						<c:if test="${emailValidate != null}">
							<a title="${emailValidate}">
								<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20"></img>
							</a>
						</c:if>
    				</td>
    			</tr>
		
				<tr>
					<td>
						<img src="./simpleImg" alt="Hier sollte das Captcha sein."></img>
						<br />
    					<input type="text" name="captchaResponse" value=""></input>
    					<a title="Geben Sie den Inhalt des Captchas ein.">
							<img src="querry.png" alt="Information" border="0" width="20" height="20"></img>
						</a>
						<c:if test="${captcha != null}">
							<a title="${captcha}">
        						<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20"></img>
        					</a>
        				</c:if>
					</td>
				</tr>
			</table>
			
			<input type="hidden" name="newLink" value="newLink"/>
			<input type="submit" class="button" value="Persönlichen Link anfordern"></input>
		</c:if>
    </form>
	
	<br>
	<br>

	<p> Falls Ihnen ein Fehler aufgefallen ist oder Sie Grund zur Annahme haben, dass Ihr Link einem Dritten bekannt geworden ist, dann nehmen Sie bitte <a href="./SendMessage?messageToAdmin=messageToAdmin">Kontakt mit dem Administrator <img src="email-regular.png" alt="E-Mailicon" border="0" width="14" height="14" title="${toolTippEmailContact}" /></a> auf.<br>
	</p>
	
	<br>
	<br>

	<form action="./Main" method="post">
		<c:if test="${userId != null && userId != ''}">
			<input type="hidden" name="userId" value="${userId}"/>
		</c:if>
		<input type="submit" class="button" value="Zur&uuml;ck zur Hauptseite"/>
	</form>
	<!-- Falls nicht eingeloggt: Definiert Button zur Hauptseite -->
	<c:if test="${userId != null && userId == ''}">
		<form action="./PersonalSection" method="post">
			<input type="hidden" name="userId" value="${userId}"/>
    		<input type="submit" class="button" value="Zum Pers&ouml;nlichen Bereich"></input>
		</form>
	</c:if>


<%@ include file="include/Tail.html" %>