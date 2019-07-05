<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@ tag import="java.sql.*" %>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.databaseAccess"/>
<a class="xp_createNew" href="/pilots/newPilot.jsp">Create new Pilot</a>
<form method="post" action="/ActivateServlet">
<%
	ResultSet pilots = dao.getResults("pilots");
	while (pilots.next()) {
		%> 
		<div class="xp_airlineSnippet">
			<span><%= pilots.getString("firstname") %> <%= pilots.getString("lastname") %></span><button type="submit" name="activate" value="activate<%= pilots.getInt("id") %>">activate</button>
		</div>
		<%
	}
%>
</form>