<!DOCTYPE html>
<html>
	<%@ page import="com.sjwi.saml.idp.config.ApplicationConstants" %>
	<%@ include file="partial/head.jsp" %>
<body>
	<div class="row container-row no-gutters">
		<div class="col text-center my-auto">
			<div class="login container mb-3">
				<%@ include file="partial/config-link.jsp" %>
				<%@ include file="partial/logo.jsp" %>
				<div class="pl-4 pr-4 row mb-4 text-center">
					<div class="col">
						<svg class="checkmark" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 52 52">
						  <circle class="checkmark__circle" cx="26" cy="26" r="25" fill="none"/>
						  <path class="checkmark__check" fill="none" d="M14.1 27.2l7.1 7.2 16.7-16.8"/>
						</svg>
					</div>
				</div>
				<h4 class="text-center text-secondary mb-3">Welcome ${USERNAME}</h4>
				<div class="row text-center mb-1">
					<div class="col">
						<h5>You have successfully been authenticated</h5>
					</div>
				</div>
				<div class="row text-center mb-4">
					<div class="col">
						<span>Logout to demo authenticating by another option.</span>
					</div>
				</div>
				<div class="row text-right mb-4">
					<div class="col">
						<a class="btn btn-secondary" href="${pageContext.request.contextPath}/destroy-user" onclick="return confirm('This will completely remove any user details and sign-on configuration that you have made. Continue?')">Delete Account</a>
						<a class="btn btn-info" href="${pageContext.request.contextPath}/logout">Logout</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

</html>