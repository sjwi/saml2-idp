<!DOCTYPE html>
<html>
	<%@ page import="com.sjwi.saml.idp.config.ApplicationConstants" %>
	<%@ include file="partial/head.jsp" %>
	<body>
		<div class="row no-gutters container-row">
			<div class="col text-center adjust-height">
				<div class="configuration config-content container mb-3">
					<div class="mt-3"></div>
					<%@ include file="partial/logo.jsp" %>
					<form class="mt-2 pl-4 pr-4" method="POST" action="login" enctype="multipart/form-data" id="mainForm" name="mainForm">
						<div class="row mb-4 text-center">
							<div class="col">
								<div class="row no-gutters">
									<div class="col">
										<h6 class="text-center text-danger">This identity provider is unconstrained, meaning it will successfully assert a response back to any Service Provider requesting authentication.</h6>
										<h6 class="text-center text-danger"> It is intended for demonstration purposes only.</h6>
									</div>
								</div>
								<div class="row no-gutters mt-4">
									<div class="col">
										<h5 class="text-left">Configuration Information</h5>
									</div>
								</div>
								<div class="row no-gutters mt-4">
									<div class="col text-left">
										<div><small>The Custom SAML Identity Provider is currently only configured to work with service provider initiated requests.</small></div>
									</div>
								</div>
								<div class="row no-gutters mt-4">
									<div class="col text-left">
										<div class="form-row">
											<label class="col-sm-4 col-form-label">Identity Provider Issuer URI (Entity ID):</label>
											<div class="input-group col">
												<input class="form-control mono-space" id="entityId" disabled type="text" value="<%=ApplicationConstants.ISSUER_URI%>">
												<div class="input-group-append copy-text copy-input" data-target="#entityId" title="Copy to clip-board">
													 <div class="input-group-text"><img width="24px" src="${pageContext.request.contextPath}/images/copy.png"><span class="collapse copy-checkmark">&#10003;</span></div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="row no-gutters mt-4">
									<div class="col text-left">
										<div class="form-row">
											<label class="col-sm-4 col-form-label">Single Sign-On Authorization End-point:</label>
											<div class="input-group col">
												<input id="ssoEndpoint" class="form-control mono-space" disabled type="text" value="<%=ApplicationConstants.AUTHORIZATION_ENDPOINT%>">
												<div class="input-group-append copy-text copy-input" data-target="#ssoEndpoint" title="Copy to clip-board">
													 <div class="input-group-text"><img width="24px" src="${pageContext.request.contextPath}/images/copy.png"><span class="collapse copy-checkmark">&#10003;</span></div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="row no-gutters mt-4">
									<div class="col text-left">
										<div class="form-row">
											<label class="col-sm-4 col-form-label">Identity Provider x509 Certificate:</label>
											<div class="input-group col">
												<textarea id="x509Cert" class="form-control mono-space" rows=23 disabled><%=ApplicationConstants.getPublicCertificateString()%></textarea>
												<div class="input-group-append copy-text copy-textarea" data-target="#x509Cert" title="Copy to clip-board">
													 <div class="input-group-text"><img width="24px" src="${pageContext.request.contextPath}/images/copy.png"><span class="collapse copy-checkmark">&#10003;</span></div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="row no-gutters mt-4 mb-4">
									<div class="col text-right">
										<a href="${pageContext.request.contextPath}/sso/saml2/metadata" class="btn btn-sm btn-dark mt-2" target="_blank">
											<img width="20px" class="copy-img-btn" src="${pageContext.request.contextPath}/images/download-inverse.png">
											Download Identity Provider Metadata
										</a>
									</div>
								</div>
								<div class="row no-gutters mt-4 mb-4">
									<div class="col text-right">
										<a href="${pageContext.request.contextPath}/login" class="btn btn-md btn-info mt-2" target="_blank">
											Test Sign-On-Options
										</a>
										<button type="submit" form="testSamlRequest" class="btn btn-md btn-primary mt-2">
											Test Sample SAML Request
										</button>
									</div>
								</div>
							</div>
						</div>
						<div class="row text-center">
							<div class="col">
								<span>
									
								</span>
							</div>
						</div>
					</form>
					<form action="${pageContext.request.contextPath}/sso/saml2" method="POST" id="testSamlRequest">
						<input type="hidden" name="RelayState" value="5C4DLtslXo2HHF5kYgIiWGrBoBmIPo">
						<input type="hidden" name="SAMLRequest" value="PHNhbWxwOkF1dGhuUmVxdWVzdCBWZXJzaW9uPSIyLjAiIElEPSJNVlNkQlZBZEM5RHpIazhJQ1lNZ0ZQMjlycHoiIElzc3VlSW5zdGFudD0iMjAyMC0xMC0yMVQyMDozNzoyMS43OTJaIiB4bWxuczpzYW1scD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOnByb3RvY29sIj4NCiAgICA8c2FtbDpJc3N1ZXIgeG1sbnM6c2FtbD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFzc2VydGlvbiI+DQogICAgICAgIHNhbXBsZV9zZXJ2aWNlUHJvdmlkZXJfaXNzdWVySUQNCiAgICA8L3NhbWw6SXNzdWVyPg0KICAgIDxzYW1scDpOYW1lSURQb2xpY3kgQWxsb3dDcmVhdGU9InRydWUiLz4NCjwvc2FtbHA6QXV0aG5SZXF1ZXN0Pg==">
					</form>
				</div>
			</div>
		</div>
	</body>
	<script>
		$(document).ready(function(){
			$('.copy-input').on('click',function(){
				copyTextToClipboard($($(this).data('target')).val());
				checked(this);
			});
			$('.copy-textarea').on('click',function(){
				copyTextToClipboard($($(this).data('target')).text());
				checked(this);
			});
		});
		function checked(elem){
			$(elem).find('div').addClass('bg-info');
			$(elem).find('div > img').hide('slow');
			$(elem).find('div > span').show('slow');
			setTimeout(function(){
				$(elem).find('div').removeClass('bg-info');
				$(elem).find('div > img').show('slow');
				$(elem).find('div > span').hide('slow');
			},1000);
		}
		function copyTextToClipboard(text){
			var targetId = "_hiddenCopyText_";
			var target = document.createElement("textarea");
			target.style.position = "absolute";
			target.style.left = "-9999px";
			target.style.top = "0";
			target.id = targetId;
			document.body.appendChild(target);
		    target.textContent = text;
		    var currentFocus = document.activeElement;
		    target.focus();
		    target.setSelectionRange(0, target.value.length);
		    document.execCommand("copy");
		    $(target).remove();
		    if (currentFocus && typeof currentFocus.focus === "function") {
		        currentFocus.focus();
		    }
		}
	</script>
</html>