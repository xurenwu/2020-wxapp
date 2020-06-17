$(function(){
	
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
	
	//加载页面头部
	var adminJsonStr = sessionStorage.getItem('adminInfo');
	
	var inital_adminInfo = JSON.parse(adminJsonStr);
	console.log(inital_adminInfo);
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
	
	//提交表单
	$(this).on('click','#add_bookInfo',function(){
		console.log("hello");
		console.log($("#category option:selected").text());
		console.log($("#status input:checked").val());
		var data = {};
		data={
			"bookName":$("#bookName").val(),
			"category":$("#category option:selected").text(),
			"state":$("#status input:checked").val(),
			"author":$("#author").val(),
			"desc":$("#desc").val(),
			"lastChapter":$("#lastChapter").val(),
			"lastChapterUrl":$("#lastChapterUrl").val(),
			"bookImage":$("#bookImage").val(),
			"heat":$("#heat").val(),
		}
		console.log(data);
		if(data.bookName.length>0 && data.category.length>0 
				&& data.state.length>0 && data.author.length>0 && data.desc.length>0)
		{
			key = $("#bookImage").val();
			data = JSON.stringify(data);
			var url = "http://localhost:8080/2020_wxapp/addBookInfo";
			submit_bookInfo(url,data);
		}else{
			Dialog.warn( "warn", "请输入完整书籍信息" );
		}
		
	})
	
	function submit_bookInfo(url,data){
		$.ajax({
	        url:url,
	        type:"POST",
	        dataType:"json",
	        data:{"bookInfo":data},
	        success:function(data){
	        	if(data.success){
	        		uplaod_qiniu();
	        		Dialog.success( "success", "书籍添加成功" );
	        	}else{
	        		Dialog.warn( "warn", "书籍上传失败" );
	        	}
	        }
		})
	}
	
	$(".form-group input").blur(function(){
		var _this=$(this);
		var value =_this.val();
		var placehoder = _this.prev().text();
		placehoder = placehoder.substring(0,placehoder.length)
		console.log(placehoder)
		if(value.length<=0){
			_this.attr("placeholder",placehoder+"不能为空");
			_this.addClass('inputChange');
		}
	})
	
	$(".form-group textarea").blur(function(){
		var _this=$(this);
		var value =_this.val();
		var placehoder = _this.prev().text();
		placehoder = placehoder.substring(0,placehoder.length)
		console.log(placehoder)
		if(value.length<=0){
			_this.attr("placeholder",placehoder+"不能为空");
			_this.addClass('inputChange');
		}
	});
	
	$("#bookImage").blur(function(){
		var _this=$(this);
		var value =_this.val();
		if(value.length>0){
			var suffix = value.substr(value.lastIndexOf(".")+1);
			if(suffix == 'jpg' || suffix == 'png' || suffix == 'jpeg' || suffix=='PNG'){
				console.log(key);
			}else{
				$("bookImage").val("");
				_this.attr("placeholder","请输入尾缀为jpg,png,PNG,jpeg格式的路径");
			}
		}
	})
	
	//图片预览，上传到七牛云
	$(this).on('click','#add-pic-btn',function(){
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
		    $("#book_image").attr("src",imgURL);
		    console.log(imgURL);
		}
	});
	
	//上传到七牛云
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
	
	
})