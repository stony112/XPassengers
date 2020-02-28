<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@ tag import="java.sql.*" %>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.DatabaseAccess"/>
<a class="xp_createNew" href="newAirline.jsp">Create new Airline</a>
<form method="post" action="/ActivateServlet">
<%
	ResultSet airlines = dao.getResults("airlines");
	while (airlines.next()) {
		%> 
		<div class="xp_airlineSnippet">
			<span><%= airlines.getString("name") %></span><button type="submit" name="activate" value="activate<%= airlines.getInt("id") %>">activate</button>
		</div>
		<%
	}
%>
</form>