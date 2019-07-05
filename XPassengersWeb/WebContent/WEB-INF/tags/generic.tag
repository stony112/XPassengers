<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.databaseAccess"/>
<html>
  <head>
		<meta charset="ISO-8859-1">
		<link href="/includes/css/style.css" rel="stylesheet" type="text/css"/>		
		<title>XPassengersWeb</title>
	</head>
	<body class="xp">
		<header class="xp_header">
			<a href="/" class="xp_home">XPassengers v2</a>
			<div class="xp_menu">
				<ul>
					<li class="xp_menuitem"><a href="/airlines/airlines.jsp">Airlines</a></li>
					<li class="xp_menuitem"><a href="/pilots/pilots.jsp">Pilots</a></li>
					<li class="xp_menuitem"><a href="/aircrafts/aircrafts.jsp">Aircrafts</a></li>
					<li class="xp_menuitem"><a href="/options.jsp">Options</a></li>
				</ul>
			</div>
		</header>
		<div class="xp_body">
			<jsp:doBody/>
		</div>
	</body>
</html>