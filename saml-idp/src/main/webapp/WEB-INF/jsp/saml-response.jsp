<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
</head>
<body onload='document.forms["saml-form"].submit()'>
	<form name="saml-form" action="${ACS_URL}" method="POST">
		<input type="hidden" name="RelayState" value="${RELAY_STATE}"/>
		<input type="hidden" name="SAMLResponse" value="${SAML_RESPONSE}"/>
		<input type="hidden" value="submit"/>
	</form>

</body>
</html>