<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:generic>
	<jsp:body>
		<h3>new Airline</h3>
		<form method="post" action="/createAirlineServlet">
			<fieldset>
				Name <input type="text" name="airlinename" /><br />
				Homebase (ICAO) <input type="text" name="homebase"/><br />
				IATA <input type="text" name="iata"/><br />
			</fieldset>
			<input type="submit" value="Save"/>
		</form>
	</jsp:body>
</t:generic>