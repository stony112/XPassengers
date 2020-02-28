<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.HashMap" %>
<jsp:useBean id="dao" class="XPassengersWeb.DatabaseAccess"/>
<%	
	ResultSet airlines = dao.getResults("airlines");
	HashMap<Integer, String> listAirline = new HashMap<Integer, String>();
	while (airlines.next()) {
		int id = airlines.getInt("id");
		String name = airlines.getString("name");
		listAirline.put(id,name);
	}
	
	pageContext.setAttribute("listAirline", listAirline);
%>
<t:generic>
	<jsp:body>
		<h3>new Pilot</h3>
		<form method="post" action="/CreatePilotServlet">
			<fieldset>
				Firstname<input type="text" name="pilotfirstname" />
				Lastname<input type="text" name="pilotlastname"/>
				Birthday<input type="date" name="pilotbirthday"/>
				<div class="xp_input_wrapper">
					<select name="pilotairline">
						<c:forEach items="${listAirline}" var="airline">
							<option value="${airline.key}">${airline.value}</option>
						</c:forEach>
					</select>
				</div>
			</fieldset>
			<input type="submit" value="Save"/>
		</form>
	</jsp:body>
</t:generic>