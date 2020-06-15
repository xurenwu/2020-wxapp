$(function(){
	
	/*登录***********************************/
	
	//登录数据
	var login_data={
		
	}
	
	
	//前端校验
	$("#name").blur(function(){
		var admin_name=$("#name").val();
		console.log(admin_name);
		if(admin_name.length>0){
			$.ajax({
				url:"http://localhost:8080/2020_wxapp/getAdminInfoByName",
				type:"POST",
				dataType:"json",
				data:{"admin_name":admin_name},
				success:function(data){
					if(!data.success){
						console.log(data.msg);
						Dialog.info("消息提示", data.msg);
					}
				}
			})
		}else{
			$("#name").attr('placeholder',"请输入用户名");
			$("#name").addClass("inputChange");
		}
	})
	
	$("#login").click(function(){
		login_data.admin_name=$("#name").val();
		login_data.password=$("#psd").val();
		console.log(login_data);
		if(login_data.password>0){
			$.ajax({
				url:"http://localhost:8080/2020_wxapp/adminLogin",
				type:"POST",
				dataType:"json",
				data:login_data,
				success:function(data){
					if(data.success){
						console.log(data);
						var adminStr = JSON.stringify(data.data);
						sessionStorage.setItem("adminInfo", adminStr);
						window.location.href = "http://localhost:8080/2020_wxapp/admin/pages_info.html";
					}
					else{
						console.log(data.msg);
						Dialog.info("消息提示", data.msg);
					}
				}
			})
		}else{
			$("#psd").attr('placeholder',"请输入用户名");
			$("#psd").addClass("inputChange");
		}
		
	})
	
		
	
	
	/*注册***********************************/
	
	//注册数据
	var regist_data={
			
	}
	
	$("#psd_r").blur(function(){
		var psd_r_v = $("#psd_r").val();
		
		if (psd_r_v == "") {
			$("#psd_r").parent().next().css("display", "none");
			$("#psd_r").parent().next().next().css("display", "block");
			return false;
		}else{
			$("#psd_r").parent().next().css("display", "block");
			$("#psd_r").parent().next().next().css("display", "none");
		}
	})
	
	$("#affirm_psd").blur(function(){
		var psd_r_v = $("#psd_r").val();
		var affirm_psd = $("#affirm_psd").val();
		console.log(psd_r_v+" "+affirm_psd);
		
		if(affirm_psd.length<0){
			$("#affirm_psd").parent().next().css("display", "none");
			$("#affirm_psd").parent().next().next().css("display", "block");
		}else if(psd_r_v != affirm_psd){
			$("#affirm_psd").parent().next().css("display", "none");
			$("#affirm_psd").parent().next().next().css("display", "block");
			$("#affirm_psd").val("");
			$("#affirm_psd").attr('placeholder',"请输入相同密码");
			$("#affirm_psd").addClass("inputChange");
		}else{
			$("#affirm_psd").parent().next().css("display", "block");
			$("#affirm_psd").parent().next().next().css("display", "none");
		}
	})
	
	$("#register").click(function(){
		var admin_name = $("#name_r").val();
		var admin_psd = $("#psd_r").val();
		var affirm_psd = $("#affirm_psd").val();
		console.log(admin_name+admin_psd+affirm_psd);
		if(admin_name.length<=0){
			console.log()
			ok_or_errorByRegister($("#name_r"));
			return;
		}
		if(admin_psd.length<=0||affirm_psd.length<=0){
			console.log(admin_psd.length+"  "+affirm_psd.length);
			Dialog.info("消息提示", "请输入密码");
			return;
		}
		$("#name_r").parent().next().css("display", "block");
		$("#psd_r").parent().next().css("display", "block");
		$("#affirm_psd").parent().next().css("display", "block");
		regist_data.admin_name=admin_name;
		regist_data.password=admin_psd;
		register("http://localhost:8080/2020_wxapp/adminRegister",regist_data);
	})
	
	function register(url,data){
		$.ajax({
			url:url,
			type:"POST",
			dataType:"json",
			data:data,
			async:false,
			success:function(data){
				if(data.success){
					console.log(data);
					Dialog.success( "success", data.msg );
					barter_btn($(".barter_registerBtn"));
				}
				else{
					console.log(data.msg);
					Dialog.info("消息提示", data.msg);
				}
			}
		})
	}
	
})