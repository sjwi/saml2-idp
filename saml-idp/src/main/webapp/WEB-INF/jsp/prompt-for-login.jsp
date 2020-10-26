<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<%@ include file="partial/head.jsp" %>
	<body>
		<div class="row no-gutters container-row mb-3">
			<div class="col text-center my-auto">
				<div class="login container">
					<%@ include file="partial/config-link.jsp" %>
					<%@ include file="partial/logo.jsp" %>
					<div class="row text-center">
						<div class="col">
							<h6 class="text-secondary">${USERNAME}'s Authentication Options:</h6>
						</div>
					</div>
					<div class="row no-gutters tab mt-2 mb-4">
						<div class="col-4 text-center pw tab-cell active pt-3 pb-3" data-target="password-tab">
							Password
						</div>
						<div class="col-4 text-center key tab-cell pt-3 pb-3" data-target="key-tab">
							Key Combo
						</div>
						<div class="col-4 text-center grid tab-cell pt-3 pb-3" data-target="grid-tab">
							Pattern
						</div>
					</div>
					<h5 class="instruction-header text-center mt-5">Enter your username and password</h5>
					<div class="row sign-on-section">
						<div class="col my-auto">
							<div class="password-tab tab-page">
								<c:if test="${PW_ENROLLED}">
									<div class="error-row row text-center mb-3 <% if(request.getSession().getAttribute("PW_ERROR") == null){ %> collapse <% }%>">
										<div class="col error">
										<%=request.getSession().getAttribute("PW_ERROR") %>
										<% request.getSession().removeAttribute("PW_ERROR"); %>
										</div>
									</div>
									<form action="login/password" method="POST">
										<div class="row text-left">
												<div class="col-4">
													<label class="col-form-label">Username</label>
												</div>
												<div class="col-8">
													<input type="text" class="form-control" value="${USERNAME}">
												</div>
										</div>
										<div class="row mt-4 text-left">
												<div class="col-4">
													<label class="col-form-label">Password</label>
												</div>
												<div class="col-8">
													<input type="password" autofocus name="password" class="form-control">
												</div>
										</div>
										<div class="row mt-4 text-right pb-3">
											<div class="col">
												<button class="btn btn-primary" type="submit">Login</button>
											</div>
										</div>
									</form>
								</c:if>
								<c:if test="${!PW_ENROLLED}">
									<div class="row no-gutters enroll">
										<div class="col text-center">
											<h6 class="mb-4 text-secondary">You have not enrolled in this authentication method yet</h6>
											<div class="btn btn-lg btn-success" onclick="openPasswordEnrollModal()">Click Here to Enroll</div>
										</div>
									</div>
								</c:if>
							</div>
							<div class="key-tab tab-page collapse">
								<c:if test="${KEY_ENROLLED}">
									<div class="error-row row text-center mb-3 <% if(request.getSession().getAttribute("KEY_ERROR") == null){ %> collapse <% }%>">
										<div class="col error">
										<%=request.getSession().getAttribute("KEY_ERROR") %>
										<% request.getSession().removeAttribute("KEY_ERROR"); %>
										</div>
									</div>
									<%@ include file="partial/key-prompt.jsp" %>
								</c:if>
								<c:if test="${!KEY_ENROLLED}">
									<div class="row no-gutters enroll">
										<div class="col text-center">
											<h6 class="mb-4 text-secondary">You have not enrolled in this authentication method yet</h6>
											<div class="btn btn-lg btn-success" id="keyEnrollBtn">Click Here to Enroll</div>
										</div>
									</div>
								</c:if>
							</div>
							<div class="grid-tab tab-page collapse mt-3 mb-4-5">
								<c:if test="${GRID_ENROLLED}">
									<div class="error-row row text-center mb-2 <% if(request.getSession().getAttribute("GRID_ERROR") == null){ %> collapse <% }%>">
										<div class="col error">
										<%=request.getSession().getAttribute("GRID_ERROR") %>
										<% request.getSession().removeAttribute("GRID_ERROR"); %>
										</div>
									</div>
									<div class="row no-gutters">
										<div class="col text-center">
											<form id="gridLogin" action="login/grid" method="POST">
												<input type="hidden" name="pattern" id="pattern">
												<%@ include file="partial/grid-prompt.jsp" %>
											</form>
										</div>
										<script>
											var attempt = 1;
											var pattern;
											function submit(key){
												if(key.length > 3){
													$('#pattern').val(key);
													$('#gridLogin').submit();
												} else {
													showFailure();
												}
											}
											function arraysEqual(a1,a2) {
												return JSON.stringify(a1)==JSON.stringify(a2);
											}
										</script>
									</div>
								</c:if>
								<c:if test="${!GRID_ENROLLED}">
									<div class="row no-gutters enroll">
										<div class="col text-center">
											<h6 class="mb-4 text-secondary">You have not enrolled in this authentication method yet</h6>
											<div class="btn btn-lg btn-success" onclick="openGridEnrollModal()">Click Here to Enroll</div>
										</div>
									</div>
								</c:if>
							</div>
						</div>
					</div>
					<div class="text-center mb-4 reset-btn">
						<a href="javascript:void(0)" onclick="return confirmReset()" class="error"><small>Reset</small></a>
					</div>
				</div>
			</div>
		</div>
		<c:if test="${!PW_ENROLLED}">
			<div class="modal fade" id="passwordEnrollModal">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class = "modal-header">
							<h5 class="modal-title">Create Password</h5>
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							  <span aria-hidden="true">&times;</span>
							</button>
						</div>
						<form method="POST" action="enroll/password" id="passwordEnrollForm">
							<div class="modal-body">
								<div class="form-group row">
									<div class="col mx-auto col-md-6">
										<input placeholder="Create a password" class="form-control password required autofocus" type="password" name="password"/>
									</div>
								</div>
								<div class="form-group row">
									<div class="col mx-auto col-md-6">
										<input placeholder="Confirm password" class="form-control confirm-password required" type="password" name="confirmPassword"/>
									</div>
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
								<button type="submit" class="btn btn-info" id="createPasswordBtn">Create Password</button>
							</div>
						</form>	
					</div>
				</div>
			</div>
		</c:if>
		<c:if test="${!GRID_ENROLLED}">
			<div class="modal fade" id="gridEnrollModal">
				<div class="modal-dialog modal-lg" role="document">
					<div class="modal-content">
						<div class = "modal-header">
							<h5 class="modal-title">Create a Pattern</h5>
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							  <span aria-hidden="true">&times;</span>
							</button>
						</div>
						<form method="POST" action="enroll/grid" id="gridEnrollForm">
							<input id="pattern" type="hidden" name="pattern">
							<div class="modal-body">
								<div class="form-group row">
									<div class="col text-center mb-3">
										<h5>Draw an authorization pattern by connecting at least 4 dots</h5>
										<h6>(You can right click to cancel your pattern at any time)</h6>
										<h6 class="mt-3 mb-3 text-center text-primary confirm-pattern" style="opacity:0">Draw your authorization pattern a second time to confirm</h6>
										<%@ include file="partial/grid-prompt.jsp" %>
									</div>
								</div>
								<div class="form-group row">
									<div class="col text-right">
										<button id="enroll" type="submit" class="btn btn-success collapse">Enroll</button>
									</div>
								</div>
							</div>
						</form>	
					</div>
				</div>
			</div>
			<script>
				var attempt = 1;
				var pattern;
				function submit(key){
					if (key.length > 3 && attempt == 1){
						showSuccess();
						pattern = key;
						$('#pattern').val(key);
						$('.confirm-pattern').css('opacity','1');
						setTimeout(function(){
							lock.clear();
						},500);
						attempt = 2;
					} else if (arraysEqual(key,pattern)){
						showSuccess();
						$('#gridEnrollForm').submit();
					} else {
						showFailure();
					}
				}
				function arraysEqual(a1,a2) {
					return JSON.stringify(a1)==JSON.stringify(a2);
				}
			</script>
		</c:if>
		
	</body>
	<script language="javascript">
		var user = '<%=request.getAttribute("USERNAME")%>';
		var focusedTab = 'password';
		var passwordInstructions = 'Enter your username and password';
		var keyInstructions = 'Enter the key combination using your <a href="' + '${pageContext.request.contextPath}' + '/download-key" target="_blank">Authorization Key</a>';
		var gridInstructions = 'Draw your authorization pattern';
		$(document).ready(function(){
			if ($('.enroll.row').is(':visible')){
				$('.reset-btn,.instruction-header').css('opacity','0');
			}
			$('.tab-cell').on('click',function(){
				$('.tab-cell').removeClass('active');
				$(this).addClass('active');
				$('.tab-page').hide();
				$('.' + $(this).data('target')).show();
				$('.' + $(this).data('target')).find('.autofocus').focus();
				window.history.replaceState($(this).data('target'), $(this).data('target'), location.protocol + '//' + location.host + location.pathname + '#' + $(this).data('target'));
				if ($('.enroll.row').is(':visible')){
					$('.reset-btn,.instruction-header').css('opacity','0');
				} else {
					$('.reset-btn,.instruction-header').css('opacity','1');
				}
				focusedTab = $(this).data('target').replace('-tab','');
				updateFocusedAttributes();
			});
			$('#keyEnrollBtn').on('click',function(){
				$.ajax({
					url: "enroll/key", 
					success: function(result){
						var myWindow = window.open("pdf/SecurityKey_" + user + '.pdf', "_blank", "width=1000,height=1000");
						var url = location.pathname + '#key-tab';
						location.href = url;
						location.reload();
					}
				});
			});
			$(document).on('shown.bs.modal','.modal', function() {
				$(this).find('.autofocus').focus();
			});
			if (location.href.includes('#key-tab')){
				$('.key.tab-cell').click();
				focusedTab = 'key';
			}
			if (location.href.includes('#password-tab')){
				$('.pw.tab-cell').click();
				focusedTab = 'password';
			}
			if (location.href.includes('#grid-tab')){
				$('.grid.tab-cell').click();
				focusedTab = 'grid';
			}
			updateFocusedAttributes();
		});
		function updateFocusedAttributes(){
			switch(focusedTab) {
			case 'key':
				$('.instruction-header').html(keyInstructions);
				break;
			case 'grid':
				$('.instruction-header').html(gridInstructions);
				break;
			default:
				$('.instruction-header').html(passwordInstructions);
			}
			$('.reset-btn > a').attr('href', '${pageContext.request.contextPath}' + '/reset?type=' + focusedTab);
		}
		function confirmReset(element){
			return confirm('Are you sure you want to remove this authentication option? This cannot be undone');
		}
		function openPasswordEnrollModal(){
			$('#passwordEnrollModal').modal('show');
		}
		function openGridEnrollModal(){
			$('#gridEnrollModal').modal('show');
		}
	</script>

</html>