<%
/**
 * Copyright (c) Pasturenzi Francesco
 * This is the form that you can see click on button "Preferences" of the Portlet
 */
%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<jsp:useBean id="saveStackOverflowParametersURL" class="java.lang.String" scope="request" />

<portlet:defineObjects />


<form id="<portlet:namespace/>accountForm" action="<%=saveStackOverflowParametersURL%>" method="POST">
	<span style="color:#000000;">URL Favorite Questions: <br>
		<input type="text" name="inUrlFavorite" size="50"><br>
	<span style="color:#000000;">URL StackOverflow Profile:</span><br>
		<input type="text" name="inUrlProfile" size="50"><br>
	
	<p style="text-align:right;">
	<input type="submit" id="inviaUrlForm" title="Save" value="Save">
	</p>
	<br>
</form>