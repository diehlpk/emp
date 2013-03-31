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

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ page import="controller.DBAdministration" %>
<%@ page import="model.Ad" %>
<jsp:useBean id="ad" scope="request" type="model.Ad" class="model.Ad"/>
<title>IMP - E-Mail an den Initiator/ die Initiatorin schicken</title>
<%@ include file="include/Head.html" %>

<div id="breadcrumb">
   <div class="tx-visrootline-pi1">
      <a href="http://www.change.me">Institution</a>&nbsp;&rArr;&nbsp;
      <a href="http://www.change.me/home.html" >Informatik</a>&nbsp;&rArr;&nbsp;
      <a href="/IMP/index.jsp">Marktplatz</a>&nbsp;&rArr;&nbsp;Nachricht senden
   </div>
</div>

<%@ include file="include/Sidebars.html" %>
<!-- Send Customer Message -->
<!--
	Senden einer Nachricht an den Initator einer Ad
	Eingabe:
		- Email
		- Nachricht an Initiator bzw Interessenten
		- captcha
	Aktionen:
	 	- Nachricht senden
	 	- Zur Hauptseite
	 	- Zum persönlichen Bereich (falls eingeloggt)
 -->
 <%
 	// Variablendeklaration, die für diese Seite verwendet werden
 	String messageToFollower = request.getParameter("messageToFollower");
 %>
 <c:if test="${messageToAdmin == null || messageToAdmin == ''}">
 
	<form action="./SendCustomerMessage" method="post">
    	<table>
		
		<%-- Message to Initiator --%>
		<c:if test="${messageToInitiator != null && messageToInitiator != ''}">
			<tr>
            	<td>
					<label for="email"><span class="mandatoryinput">E-Mail-Adresse:</span></label>
               		<br />
               		<input type="text" id="email" name="email" size="40"  value="${email}" />
               		<a title="${toolTippEmail}">
                  		<img src="querry.png" alt="?" border="0" width="20" height="20" /> 
               		</a>
               		<c:if test="${emailValidate != null}">
                  		<a title="${emailValidate}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
                  	</c:if>
            	</td>
         	</tr>
         	
         	<tr>
            	<td>
            		<label for="emailReply"><span class="mandatoryinput">Wiederholung E-Mail-Adresse:</span></label>
               		<br />
               		<input type="text" id="emailReply" name="emailReply" size="40"  value="${emailReply}" />               		
               		<a title="Tragen Sie bitte Ihre E-Mail-Adresse zur Sicherheit ein weiteres Mal ein!">
                  		<img src="querry.png" alt="?" border="0" width="20" height="20" /> 
               		</a>
               		<c:if test="${emailReplyValidate != null}">
                  		<a title="${emailReplyValidate}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
                  	</c:if>
            	</td>
         	</tr>
         	
         	<tr>
            	<td>
            		<label for="customerMessage">
            			<span class="mandatoryinput">
            				Ihre Nachricht an den Initiator / die Initiatorin:
            			</span>
            		</label>
               	
               		<br />
               		<textarea id="customerMessage" name="customerMessage" rows="10" cols="60">${customerMessage}</textarea>
               		<br />
               		
               		<c:if test="${customerMessageValidate != null}">
               			<a title="${customerMessageValidate}">
               				<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
               		</c:if>
             	</td>
         	</tr>
         	
         	<tr>
            	<td>
               		<img src="./simpleImg" alt="Hier sollte das Captcha sein." />
               		<br />
               		<input type="text" name="captchaResponse" value="" />
               
               		<a title="Geben Sie den Inhalt des Captchas ein.">
                  		<img src="querry.png" alt="Information" border="0" width="20" height="20" />
               		</a>
               		<a title="${captcha}">
                  		<c:if test="${captcha != null}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
                  		</c:if>
               		</a> 
            	</td>
         	</tr>
          	
         	<input type="hidden" name="messageToInitiator" value="${messageToInitiator}" />
		</c:if>
			
		<%-- Message to Follower --%>
		<c:if test="${messageToFollower != null && messageToFollower != ''}">
		<% if (messageToFollower.equals("One")) {%>
				<tr>
	            	<td>
	            		<label for="customerMessage">
	            			<span class="mandatoryinput">
	            				Ihre Nachricht an den Interessenten / die Interessentin:
	            			</span>
	            		</label>
	               		
	               		<br />
	               		<textarea id="customerMessage" name="customerMessage" rows="10" cols="60">${customerMessage}</textarea>
	               		<br />
	               		
	               		<c:if test="${customerMessageValidate != null}">
	               			<a title="${customerMessageValidate}">
	               				<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
	               			</a>
	               		</c:if>
	             	</td>
	         	</tr>
		
	      		<input type="hidden" name="messageToFollower" value="One" />
	      		<input type="hidden" name="followerEmail" value="${followerEmail}" />
			<%} else if (messageToFollower.equals("All")) { %>
				<tr>
	            	<td>
	            		<label for="customerMessage">
	            			<span class="mandatoryinput">
	            				Ihre Nachricht alle Interessenten:
	            			</span>
	            		</label>
	               		
	               		<br />
	               		<textarea id="customerMessage" name="customerMessage" rows="10" cols="60">${customerMessage}</textarea>
	               		<br />
	               		
	               		<c:if test="${customerMessageValidate != null}">
	               			<a title="${customerMessageValidate}">
	               				<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
	               			</a>
	               		</c:if>
	             	</td>
	         	</tr>
		
	      		<input type="hidden" name="messageToFollower" value="All" />
			<%} %>
		</c:if>
         
      	</table>
      
      
      	<c:if test="${userId != null && userId != ''}">
      		<input type="hidden" name="userId" value="${userId}" />
      	</c:if>
      	      	
      	<input type="hidden" name="adId" value="${adId}" />
      	<input type="submit" class="button" name="confirm" value="Nachricht senden" />
      	
   	</form>
</c:if>
<%-- Message to Administrator --%>
<%
 	// Variablendeklaration, die für diese Seite verwendet werden
 	String messageToAdmin = request.getParameter("messageToAdmin");
 %>
	<c:if test="${messageToAdmin != null && messageToAdmin != ''}">
	<p>
		<img src="warning.png" alt="Hinweis!" border="0" width="25" height="25" />
		Diese Aktion ist nur notwendig, wenn Ihnen ein Fehler aufgefallen ist oder Grund zur Annahme haben, dass Ihr Link einem Dritten bekannt geworden ist.
	</p>
	<form action="./SendCustomerMessage" method="post">
    	<table>
		
			<tr>
            	<td>
					<label for="email"><span class="mandatoryinput">Ihre E-Mail-Adresse:</span></label>
               		<br />
               		<input type="text" id="email" name="email" size="40"  value="${email}" />
               		<a title="${toolTippEmail}">
                  		<img src="querry.png" alt="?" border="0" width="20" height="20" /> 
               		</a>
               		<c:if test="${emailValidate != null}">
                  		<a title="${emailValidate}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
                  	</c:if>
            	</td>
         	</tr>
         	
         	<tr>
            	<td>
            		<label for="emailReply"><span class="mandatoryinput">Wiederholung Ihrer E-Mail-Adresse:</span></label>
               		<br />
               		<input type="text" id="emailReply" name="emailReply" size="40"  value="${emailReply}" />               		
               		<a title="Tragen Sie bitte Ihre E-Mail-Adresse zur Sicherheit ein weiteres Mal ein!">
                  		<img src="querry.png" alt="?" border="0" width="20" height="20" /> 
               		</a>
               		<c:if test="${emailReplyValidate != null}">
                  		<a title="${emailReplyValidate}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
                  	</c:if>
            	</td>
         	</tr>
         	
         	<tr>
            	<td>
            		<label for="customerMessage">
            			<span class="mandatoryinput">
            				Ihre Nachricht an den Administrator:
            			</span>
            		</label>
               	
               		<br />
               		<textarea id="customerMessage" name="customerMessage" rows="10" cols="60">${customerMessage}</textarea>
               		<br />
               		
               		<c:if test="${customerMessageValidate != null}">
               			<a title="${customerMessageValidate}">
               				<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
               			</a>
               		</c:if>
             	</td>
         	</tr>
         	
         	<tr>
            	<td>
               		<img src="./simpleImg" alt="Hier sollte das Captcha sein." />
               		<br />
               		<input type="text" name="captchaResponse" value="" />
               
               		<a title="Geben Sie den Inhalt des Captchas ein.">
                  		<img src="querry.png" alt="Information" border="0" width="20" height="20" />
               		</a>
               		<a title="${captcha}">
                  		<c:if test="${captcha != null}">
                  			<img src="cross.png" alt="Fehlerhafte Eingabe" border="0" width="20" height="20" />
                  		</c:if>
               		</a> 
            	</td>
         	</tr>
          	
		
		</table>
		
        <input type="hidden" name="messageToAdmin" value="${messageToInitiator}" />    	
      	<input type="submit" class="button" name="confirm" value="Nachricht senden" />
      	
   	</form>
</c:if>

   <p>Alle mit <span class="mandatoryinput"> </span> gekennzeichneten Felder sind Pflichtfelder.</p>

   <br />
   <br />
   
   <!-- Definiert Button zur Hauptseite -->
   <form action="./Main" method="post">
      <c:if test="${userId != null && userId != ''}">
      	<input type="hidden" name="userId" value="${userId}" />
      </c:if>
      <input type="submit" class="button" value="Zur&uuml;ck zur Hauptseite" title="(Nachricht wird nicht gesendet)" />
   </form>
   
   <%-- Button zurueck zur Anzeige --%>
	<c:if test="${adId != null && adId != ''}">
		<form action="./Main" method="post">
			<input type="hidden" name="AdDetail" value="AdDetail"/>
   			<input type="hidden" name="adId" value="${adId}"/>
   			<input type="submit" class="button" value="Zur&uuml;ck zur Anzeige" title="Nachricht wird nicht gesendet"/>
   		</form>
	</c:if>

	<!-- Falls eingeloggt: Definiert Button zum persönlichen Bereich -->
	<c:if test="${userId != null && userId != ''}">
   		<form action="./PersonalSection" method="post">
      		<input type="hidden" name="userId" value="${userId}" />
      		<input type="submit" class="button" value="Zum pers&ouml;nlichen Bereich" title="(Nachricht wird nicht gesendet)" />
   		</form>
   	</c:if>
<%@ include file="include/Tail.html" %>
