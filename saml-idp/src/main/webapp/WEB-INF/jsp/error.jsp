<!DOCTYPE html>
<html>
	<%@ include file="partial/head.jsp" %>
	<body>
		<div class="row container-row no-gutters">
			<div class="col text-center my-auto">
				<div class="login container">
					<div class="mt-3"></div>
					<%@ include file="partial/logo.jsp" %>
					<form class="mt-4 pl-4 pr-4" method="POST" action="login" enctype="multipart/form-data" id="mainForm" name="mainForm">
						<div class="row text-center mb-4">
							<div class="col">
								<span>Uh oh.. something went wrong.</span>
							</div>
						</div>
						<div class="row text-center mb-4">
							<div class="col">
								<span>${ERROR}</span>
							</div>
						</div>
					
					</form>
				</div>
			</div>
		</div>
	</body>
</html>