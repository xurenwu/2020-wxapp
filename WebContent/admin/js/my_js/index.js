$(function(){
	var adminJsonStr = sessionStorage.getItem('adminInfo');
	
	var inital_adminInfo = JSON.parse(adminJsonStr);
	
	if(inital_adminInfo==null){
		window.location.href = "http://localhost:8080/2020_wxapp/admin/pages_login.html";
	}else{
		
		inital_load();
		function inital_load(){
			$("#adminName").html(inital_adminInfo.admin_nickname);
			$("#nickname").val(inital_adminInfo.admin_nickname);
			$("#adminAvatar").attr('src',"http://qbhvuddzp.bkt.clouddn.com/"+inital_adminInfo.admin_avatar);
		} 
	}
	
})