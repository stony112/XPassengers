<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@ tag import="java.sql.*" %>
<%@ tag import="java.util.HashMap" %>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.DatabaseAccess"/>
<a class="xp_createNew" href="/pilots/newPilot.jsp">Create new Pilot</a>
<form method="post" action="/ActivateServlet">
<%
	HashMap<String,Object> wheres = new HashMap<String,Object>();
	wheres.put("airlineID", utils.getActiveAirline());
	ResultSet pilots = dao.select("pilots", "*", wheres);
	if (pilots != null) {
		while (pilots.next()) {
			%> 
			<div class="xp_airlineSnippet">
				<span><%= pilots.getString("firstname") %> <%= pilots.getString("lastname") %></span><button type="submit" name="activate" value="activate<%= pilots.getInt("id") %>">activate</button>
			</div>
			<%
		}
	}
%>
</form>