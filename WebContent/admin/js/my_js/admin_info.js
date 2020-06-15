$(function(){
	console.log("初次页面加载");
	
	var adminJsonStr = sessionStorage.getItem('adminInfo');
	
	var inital_adminInfo = JSON.parse(adminJsonStr);
	console.log(inital_adminInfo)
	var adminInfo = {};
	var file_path = null;		//临时文件
	var key = "";			//上传的文件名
	var token = "";				//服务端返回的token
	var config = {
		// 上传域名区域，z0指华东地区
		region: qiniu.region.z0
	};
	var putExtra = {
		// 限制上传文件类型
		mimeType: null
	};
	load_info_page();
	function load_info_page(){
		console.log(inital_adminInfo.admin_avatar);
		$("#adminName").html(inital_adminInfo.admin_nickname);
		$("#nickname").val(inital_adminInfo.admin_nickname);
		$("#adminAvatar").attr('src',"http://qbhvuddzp.bkt.clouddn.com/"+inital_adminInfo.admin_avatar);
		$("#username").val(inital_adminInfo.admin_name);
		$("#email").val(inital_adminInfo.admin_email);
		$("#remark").val(inital_adminInfo.admin_desc);
		$("#admin_image").attr('src',"http://qbhvuddzp.bkt.clouddn.com/"+inital_adminInfo.admin_avatar);
	}
	
	//修改头像
	$("#avatar").click(function(){
		document.getElementById("uploadImage").click();
	})
	
	$("#uploadImage").change(function(event){
		var files = event.target.files,file;
		if (files && files.length > 0) {
			file_path = files[0];
	        // 获取目前上传的文件
		    file = files[0];// 文件大小校验的动作
		    // 获取 window 的 URL 工具
		    var URL = window.URL || window.webkitURL;
		    // 通过 file 生成目标 url  获取真实的路径
		    var imgURL = URL.createObjectURL(file);
		    //用attr将img的src属性改成获得的url
		    $("#admin_image").attr("src",imgURL);
		    console.log(imgURL);
		}
	});
	
	function uplaod_qiniu(){
		//key有效
		console.log(key)
		if(key.length>0){
			var url = "http://localhost:8080/2020_wxapp/getQiNiuToken";
			$.ajax({
		        url:url,
		        type:"POST",
		        dataType:"json",
		        data:{"key":key},
		        async: false,
		        success:function(data){
		        	if(data.success){
		        		//获取token成功后
		        		var token = data.data;
		        		console.log(token);
		        		if(token.length>0){
		        			var observable = qiniu.upload(file_path, key, token, putExtra, config);
		        			var subscription = observable.subscribe(observable); 
		        			console.log(observable)
		        		}else{
		        			console.log("上传文件失败");
		        		}
		        	}
		        },
			});
		}
	}
	
	//修改管理员信息
	
	$("#save").click(function(){
		var name = $("#uploadImage").val();
		if(file_path == null)key=$("#admin_image").attr('src').subString(33,$("#admin_image").attr('src').length-1)
		key ="admin_"+(new Date()).getTime()+name.substring(name.lastIndexOf("."),name.length);		//获取当前是时间戳
		var data = {
			"admin_name":$("#username").val(),
			"admin_nickname":$("#nickname").val(),
			"admin_email":$("#email").val(),
			"admin_desc":$("#remark").val(),
			"admin_avatar":key
		}
		console.log(data);
		if(data.admin_name.length>0)
		{
			console.log(data);
			data = JSON.stringify(data);
			var url = "http://localhost:8080/2020_wxapp/adminModifyInfo";
			submit_adminInfo(url,data);
			console.log(inital_adminInfo);

			setTimeout(function(){ load_info_page(); }, 500);

			
		}
	})
	
	function submit_adminInfo(url,data){
		$.ajax({
	        url:url,
	        type:"POST",
	        dataType:"json",
	        data:{"adminInfo":data},
	        async : false,
	        success:function(data){
	        	if(data.success){
	        		if(file_path!=null){
	        			uplaod_qiniu();
	        		}
	        		inital_adminInfo.admin_name = data.data.admin_name;
	        		inital_adminInfo.admin_desc = data.data.admin_desc;
	        		inital_adminInfo.admin_nickname = data.data.admin_nickname;
	        		inital_adminInfo.admin_email = data.data.admin_email;
	        		inital_adminInfo.admin_avatar = data.data.admin_avatar;
	        		var adminStr = JSON.stringify(inital_adminInfo);
	        		sessionStorage.clear();
	    			console.log(adminStr);
	        		sessionStorage.setItem("adminInfo", adminStr);
	        	}else{
	        		console.log(data.msg)
	        	}
	        }
		})
	}
	
})