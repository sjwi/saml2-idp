<div class="row no-gutters">
	<div class="col text-center">
		<form  method="POST" action="login/key" id="keyForm" name="keyForm">
			<input type="hidden" id ="PASSWORD" name="PASSWORD"/>
			<div class="combo-container" >
				<c:forEach var="var" items="${PROMPT}" varStatus="theCount">
					<c:choose>
						<c:when test="${theCount.first}">
							<div class="pinbox">
								<input autocomplete="off" autofocus type="text" class="pinSub autofocus" name="sub${theCount.count}" id="sub${theCount.count}" value = "" maxlength="1" oninput="autotab(this, document.keyForm.sub${theCount.count + 1})">
								<span class="pin-index">${var}</span>
							</div>
						</c:when>
						<c:when test="${theCount.last}">
							<div class="pinbox">
								<input autocomplete="off" type="text" class="pinSub" name="sub${theCount.count}" id="sub${theCount.count}" value = "" maxlength="1" oninput="trySubmit(${theCount.count},this)">
								<span class="pin-index">${var}</span>
							</div>
						</c:when>
						<c:otherwise>
							<div class="pinbox">
								<input autocomplete="off" type="text" class="pinSub" name="sub${theCount.count}" id="sub${theCount.count}" value = "" maxlength="1" oninput="autotab(this, document.keyForm.sub${theCount.count + 1})">
								<span class="pin-index">${var}</span>
							</div>
						</c:otherwise>
					</c:choose>
				</c:forEach>	
			</div>
		</form>
	</div>
</div>
<script>

document.addEventListener("keydown", KeyCheck);  
function KeyCheck(event)
{
   var KeyID = event.keyCode;
   switch(KeyID)
   {
      case 8:
    	  var prev = $('#sub1');
    	  $('.pinSub').each(function(){

    		  if($(this).val() === ""){
    			  prev.value = "";
    			  prev.focus();
    			  return;
    		  }
    		  prev = $(this);
    		});
      break; 
      default:
      break;
   }
}

function autotab(current,to)
{
	
    if (current.getAttribute && current.value.length==current.getAttribute("maxlength")) 
    {
        to.focus(); 
    }
}

function trySubmit(count,element)
{
	element.blur();
	var fullPin = '';
	$('.pinSub').each(function(){

		fullPin = fullPin + $(this).val();
	});
	
	document.getElementById("PASSWORD").value = fullPin;
	
	$('.key-tab').css({"opacity":".5","pointer-events":"none"})
	$('.pinSub').each(function(){
		$(this).prop('disabled',true);
	});
	document.keyForm.submit();

	return true;
}

</script>
