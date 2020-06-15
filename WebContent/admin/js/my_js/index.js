$(function(){
	var adminJsonStr = sessionStorage.getItem('adminInfo');
	
	var inital_adminInfo = JSON.parse(adminJsonStr);
	console.log(inital_adminInfo);
	
	inital_load();
	function inital_load(){
		$("#adminName").html(inital_adminInfo.admin_nickname);
		$("#nickname").val(inital_adminInfo.admin_nickname);
		$("#adminAvatar").attr('src',"http://qbhvuddzp.bkt.clouddn.com/"+inital_adminInfo.admin_avatar);
	} 
})