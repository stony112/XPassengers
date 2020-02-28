<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@ tag import="java.sql.*" %>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.DatabaseAccess"/>
<%
	String path = utils.getXPlanePath();
%>
<form method="post" action="/SaveOptionsServlet">
<div class="xp_options">
	<label for="xp_options" class="xp_optionsLabel">X-Plane Path: </label>
	<input type="text" name="xp_options" class="xp_optionsInput" value="<%= path %>"/>
	<input type="submit"/>
</div>
</form>