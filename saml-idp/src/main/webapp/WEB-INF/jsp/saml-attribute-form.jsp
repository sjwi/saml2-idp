<!DOCTYPE html>
<html>
	<%@ include file="partial/head.jsp" %>
	<body>
		<div class="row no-gutters container-row">
			<div class="col text-center">
				<div class="configuration config-content container">
					<form class="mt-2 pl-4 pr-4" method="POST" action="${pageContext.request.contextPath}/sso/saml2/respond" enctype="multipart/form-data" id="samlResponseForm">
						<div class="row mb-4 text-center">
							<div class="col">
								<%@ include file="partial/logo.jsp" %>
								<div class="row no-gutters mt-4">
									<div class="col">
										<h5 class="text-left">SAML Request parsed successfully. Fill out required response information below</h5>
									</div>
								</div>
								<div class="row no-gutters mt-4">
									<div class="col text-left">
										<div class="form-row">
											<label class="col-sm-4 col-form-label">Subject Name ID (Claim ID):</label>
											<div class="input-group col">
												<div class="input-group-prepend name-id-group">
													<input type="hidden" name="subjectNameIdFormat" class="name-id-format" value="nameid-format:unspecified">
													<button class="btn btn-outline-secondary dropdown-toggle format name-id-format" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">nameid-format:unspecified</button>
													<div class="dropdown-menu">
													  <a class="dropdown-item" href="javascript:void(0)">nameid-format:unspecified</a>
													  <a class="dropdown-item" href="javascript:void(0)">nameid-format:email</a>
													  <a class="dropdown-item" href="javascript:void(0)">nameid-format:x509SubjectName</a>
													  <a class="dropdown-item" href="javascript:void(0)">nameid-format:persistent</a>
													  <a class="dropdown-item" href="javascript:void(0)">nameid-format:transient</a>
													</div>
												</div>
												<input name="subjectNameId" class="form-control mono-space required" type="text" value="${samlAttributes.subjectNameId}">
											</div>
										</div>
									</div>
								</div>
								<div class="row no-gutters mt-4">
									<div class="col text-left">
										<div class="form-row">
											<label class="col-sm-4 col-form-label">Service Provider Issuer URI (Entity ID):</label>
											<div class="input-group col">
												<input class="form-control mono-space required" name="spEntityId" type="text" value="${samlAttributes.entityId}">
											</div>
										</div>
									</div>
								</div>
								<div class="row no-gutters mt-4">
									<div class="col text-left">
										<div class="form-row">
											<label class="col-sm-4 col-form-label">Assertion Consumer Service (ACS) URI:</label>
											<div class="input-group col">
												<input name="acsUrl" class="form-control mono-space required" type="text" value="${samlAttributes.acsUrl}">
											</div>
										</div>
									</div>
								</div>
								<div class="row no-gutters mt-4">
									<div class="col-sm-4 text-left">
										<label class="col-form-label">Additional Claim Attributes:</label>
									</div>
									<div class="col" id="additionalClaimAttributeAppend">
									</div>
								</div>
								<div class="row no-gutters">
									<div class="col w-100 text-right">
										<div class="btn btn-secondary btn-sm" id="addAttribute">+ Additional Claim Attribute</div>
									</div>
								</div>
								<div class="row no-gutters mt-4">
									<div class="col w-100 text-right">
										<div class="form-check form-check-inline">
										  <input class="form-check-input" type="radio" name="signatureOptions" id="signAssertion" value="assertion">
										  <label class="form-check-label" for="signAssertion">Sign Assertion</label>
										</div>
										<div class="form-check form-check-inline">
										  <input class="form-check-input" type="radio" name="signatureOptions" checked id="signAssertionAndResponse" value="response">
										  <label class="form-check-label" for="signAssertionAndResponse">Sign Assertion and Response</label>
										</div>
									</div>
								</div>
								<div class="row no-gutters mt-4">
									<div class="col text-left">
										<div class="form-row">
											<label class="col-sm-4 col-form-label">Relay State</label>
											<div class="input-group col">
												<input name="relayState" class="form-control mono-space required" disabled type="text" value="${samlAttributes.relayState}">
											</div>
										</div>
									</div>
								</div>
								<div class="row no-gutters mt-4">
									<div class="col text-left">
										<div class="form-row">
											<label class="col-sm-4 col-form-label">Request ID:</label>
											<div class="input-group col">
												<input name="requestId" disabled class="form-control mono-space" disabled type="text" value="${samlAttributes.requestId}">
											</div>
										</div>
									</div>
								</div>
								<div class="row no-gutters mt-4 mb-4">
									<div class="col w-100">
										<div class="form-row">
											<label class="col-sm-4 col-form-label text-left">SAML Request:</label>
											<div class="col">
												<textarea class="form-control saml-req mono-space" disabled rows=8>${samlRequest}</textarea>
											</div>
										</div>
									</div>
								</div>
								<div class="row no-gutters mt-5 mb-4">
									<div class="col text-right">
										<button class="btn btn-md btn-primary mt-2" type="submit">
											Submit SAML Response
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
					<div id="additionalClaimAttributeTemplate" class="collapse">
						<div class="form-row mb-3">
							<div class="col">
								<input type="text" class="form-control mono-space" name="additional-claim-name" placeholder="Additional claim name">
							</div>
							<div class="col-3">
								<button class="btn btn-outline-secondary dropdown-toggle format attrname-format w-100" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">unspecified</button>
								<div class="dropdown-menu">
								  <a class="dropdown-item" href="javascript:void(0)">unspecified</a>
								  <a class="dropdown-item" href="javascript:void(0)">uri</a>
								  <a class="dropdown-item" href="javascript:void(0)">basic</a>
								</div>
							</div>
							<div class="col">
								<input class="form-control mono-space" type="text" name="additional-claim-value" placeholder="Additional claim value">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
	<script>
		$('#additionalClaimAttributeAppend').append($('#additionalClaimAttributeTemplate').html());
		var format = require('xml-formatter');
		$('textarea').text(format($('textarea').text()));
		$(document).ready(function(){
			$('.name-id-group a.dropdown-item').on('click',function(){
				$('.name-id-format').val('urn:oasis:names:tc:SAML:1.1:' + $(this).html());
				$('.name-id-format').text($(this).html());
			});
			$("#addAttribute").click(function(){
				$('#additionalClaimAttributeAppend').append($('#additionalClaimAttributeTemplate').html());
			})
			$('#samlResponseForm').on('submit',function(e){
				if(!isFormFilledSilent(this)){
					e.preventDefault();
				}
			})
		})
	</script>
</html>