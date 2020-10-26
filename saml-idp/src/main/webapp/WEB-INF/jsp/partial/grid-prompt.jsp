<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
	.btn-primary{
		background-image: linear-gradient(#007dc1,#0073b2);
	}
</style>
<script>
	var lock;
	function showSuccess(){
		$('g.lock-actives circle').each(function(index){
			$(this).css("fill","none");
			$(this).css("stroke-width","2");
			$(this).css("stroke","#00e600");
			$(this).css("opacity",".5");
		});
		$('g.lock-lines line').each(function(index){
			$(this).css("stroke","#00e600");
			$(this).css("opacity",".2");
		});
	}
	function showFailure(){
		$('g.lock-actives circle').each(function(index){
			$(this).css("fill","none");
			$(this).css("stroke-width","2");
			$(this).css("stroke","red");
			$(this).css("opacity",".5");
		});
		$('g.lock-lines line').each(function(index){
			$(this).css("stroke","red");
			$(this).css("opacity",".2");
		});
	}
	
	function clearKey(event){
		event.preventDefault();
	}

	document.addEventListener('contextmenu', event => clearKey(event));
	window.onload = function initializeGrid(){
		lock = new PatternLock("#lock", {
			  onPattern: function(pattern) {
				  submit(pattern);
			   }
			});
	}	

</script>
<div id="canvas" class="grid-canvas">
	<svg class="patternlock" id="lock" viewBox="0 0 130 130" xmlns="http://www.w3.org/2000/svg">
		<g class="lock-actives"></g>
		<g class="lock-lines"></g>
		<g class="lock-dots">
			<c:set var="y" value="0"/>
			<c:forEach var="row" items="${GRID}">
				<c:set var="x" value="0"/>
				<c:forEach var="idx" items="${row}">
					<circle id="${idx}" cx="${x + 20}" cy="${y + 20}" r="2" transform-origin="${x + 20} ${y + 20}"/>
					<c:set var="x" value="${x + 30}"/>
				</c:forEach>
				<c:set var="y" value="${y + 30}"/>
			</c:forEach>
		</g>
	</svg>
</div>
