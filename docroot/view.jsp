<%
/**
 * Copyright (c) Pasturenzi Francesco
 * This is the VIEW of the Portlet.
 */
%>
<link type="text/css" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/styles-pajinate.css" />

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<jsp:useBean id="urlFavorite" class="java.lang.String" scope="request" />
<jsp:useBean id="listaFavorite" class="java.lang.String" scope="request" />
<jsp:useBean id="urlProfile" class="java.lang.String" scope="request" />

<portlet:defineObjects />
<div style="width:450px;height:640px;">
		<div id="wrapper" style="min-width:430px !important;padding:10px;margin:auto;position:relative;">
			
			<div style="text-align:center;width:340px;margin:auto;">
				<a href="#">
					<img src="<%=request.getContextPath() %>/images/stackoverflow-logo.png" style="width:300px;float:left;margin-top:-15px;" alt="StackOverflow logo" title="StackOverflow"/>
				</a>
				<a href="#">
					<img src="<%=request.getContextPath() %>/images/pastuweb.png" style="width:40px;float:left;margin-top:30px;" alt="Pastuweb logo" title="Pastuweb"/>
				</a>
				<span style="clear:left;"></span>
			</div>
			<div id="paging_container" class="container">
				<div class="page_navigation"></div>
				<br><br>
				<p style="text-align:center;position:relative;padding-left:5px;border-bottom:1px solid #EFEFEF;font-weight:bold;">
				Favorite Questions <span class="pastuweb_score">(Votes)</span>
				</p>
				<ul class="content">
					<%=listaFavorite %>
				</ul>
				
			</div>
			
		</div>
</div>		
		
<script type="text/javascript">		
		AUI().use('get', function(A){
			A.Get.script('http://code.jquery.com/jquery-1.9.1.js', {
		         onSuccess: function(A){
		        	 
		        	$("ul li").hover(
			     			  function () {
			     				  $(this).css("background","#EFEFEF");
			     			  }, 
			     			  function () {
			     				  $(this).css("background","");
			     			  }
			     	);
		        	 
		         }
			 });
			
		});
		
		
		
		AUI().use('get', function(A){
			A.Get.script('<%=request.getContextPath()%>/js/jquery.pajinate.js', {
				onSuccess: function(){
					$('#paging_container').pajinate({
		     			num_page_links_to_display : 5,
		     			items_per_page : 7	
		     		});
				}
			});
		});

</script>
