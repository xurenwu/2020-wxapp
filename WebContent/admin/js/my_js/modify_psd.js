$(function(){
	var adminJsonStr = sessionStorage.getItem('adminInfo');

	var inital_adminInfo = JSON.parse(adminJsonStr);
	if(inital_adminInfo==null){
		window.location.href = "http://localhost:8080/2020_wxapp/admin/pages_login.html";
	}else{
		inital_load();
	}
	function inital_load(){
		$("#adminName").html(inital_adminInfo.admin_nickname);
		$("#nickname").val(inital_adminInfo.admin_nickname);
		$("#adminAvatar").attr('src',"http://qbhvuddzp.bkt.clouddn.com/"+inital_adminInfo.admin_avatar);
	} 
	
	$("#old-password").blur(function(){
		var old_psd = $("#old-password").val();
		if(old_psd.length>0){
			if(inital_adminInfo.admin_password != old_psd){
				$("#old-password").val("");
				$("#old-password").attr("placeholder","请输入正确的密码");
				$("#old-password").addClass("inputChange");
			}
		}
	})
	
	$("#confirm-password").blur(function(){
		var confirm_psd = $("#confirm-password").val();
		var new_psd = $("#new-password").val();
		var old_psd = $("#old-password").val();
		if(confirm_psd.length>0&&confirm_psd!=new_psd){
			$("#confirm-password").val("");
			$("#confirm-password").attr("placeholder","两次密码不一致");
			$("#confirm-password").addClass("inputChange");
		}
	})
	
	$("#modify").click(function(){
		var confirm_psd = $("#confirm-password").val();
		var new_psd = $("#new-password").val();
		var old_psd = $("#old-password").val();
		if(old_psd.length>0&&new_psd.length>0&&confirm_psd.length>0){
			console.log(new_psd+confirm_psd+old_psd);
			var admin_name = inital_adminInfo.admin_name;
			var password = new_psd;
			$.ajax({
				url:"http://localhost:8080/2020_wxapp/modifyAdminPsd",
				type:"POST",
				dataType:"json",
				data:{"admin_name":admin_name,"password":password},
				async:false,
				success:function(data){
					if(data.success){
						console.log(data.data);
						var adminStr = JSON.stringify(data.data);
						sessionStorage.setItem("adminInfo", adminStr);
					}else{
						Dialog.warn( "warn", "修改失败" );
					}
				}
			})
		}
	})
})