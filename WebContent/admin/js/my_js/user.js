$(function(){
	
	var total=0;
	var currentPageList=[];
	var len=10;
	var currentPoint = 0;
	var currentPage = 1;
	var Data = {};
	var search_state = false; 		//是否在搜索页面
	var search_key = "";			//上一次的搜索记录
	/*初始页面加载*/
	Data={"len":len,"inital":currentPoint};
	page_user_load("http://localhost:8080/2020_wxapp/getUserList",Data);
	
	//加载页面头部
	var adminJsonStr = sessionStorage.getItem('adminInfo');
	
	var inital_adminInfo = JSON.parse(adminJsonStr);
	console.log(inital_adminInfo);
	
	inital_load();
	function inital_load(){
		$("#adminName").html(inital_adminInfo.admin_nickname);
		$("#nickname").val(inital_adminInfo.admin_nickname);
		$("#adminAvatar").attr('src',"http://qbhvuddzp.bkt.clouddn.com/"+inital_adminInfo.admin_avatar);
	} 
	
//	页面加载函数
	function page_user_load(url,Data){
		$.ajax({
	        url:url,
	        type:"POST",
	        dataType:"json",
	        data:Data,
	        success:function(data){
	        	if(data.success){
	        		total = data.data;
		            currentPageList = data.list;
		            str = "";
		            if(currentPageList.length != 0){
		            	for(var i=0;i<currentPageList.length;i++){
			            	str += "<tr>";
		            		str	+= "<th scope=\"row\">"+(i+1)+"</th>";
		            		str	+= "<td>"+currentPageList[i].userId+"</td>";
		            		str	+= "<td>"+currentPageList[i].name+"</td>";
		            		str	+= "<td>"+currentPageList[i].gender+"</td>";
		            		str	+= "<td>";
		            		str	+= "<div class='btn-group'>";
		            		str	+= "<a id=\"delete_bookinfo\" class=\"btn btn-xs btn-default\" href='#' title=\"删除\" data-toggle=\"tooltip\"><i class=\"mdi mdi-window-close\"></i></a>";
		            		str	+= "</div>";
		            		str	+= "</td>";
		            		str	+= "</tr>";
			            }
			            $("#userList").html(str);
			            
//			                                分页
			            var str1 = "";
			            if(currentPage == 1){
			            	str1 += "<li id='pre' class=\"disabled\">"
								 + "<a href=\"#\">"
								 + "<span><i class=\"mdi mdi-chevron-left\"></i></span>"
								 + "</a>"
								 + "</li>";
			            }
			            else{
			            	str1 += "<li id='pre'>"
								 + "<a href=\"#\">"
								 + "<span><i class=\"mdi mdi-chevron-left\"></i></span>"
								 + "</a>"
								 + "</li>";
			            }
			            for(var j=0; j<Math.ceil(total/len);j++){
			            	if(j+1 == currentPage)str1 += "<li class=\"pageIndex active\"><a href=\"#\">"+(j+1)+"</a></li>"
			            	else str1 += "<li class='pageIndex'><a href=\"#!\">"+(j+1)+"</a></li>"
			            }
			            if(currentPage == Math.ceil(total/len)){
			            	str1 += "<li id='next' class=\"disabled\">"
								 + "<a href=\"#!\">"
								 + "<span><i class=\"mdi mdi-chevron-right\"></i></span>"
								 + "</a>" + "</li>";
			            }
			            else{
			            	str1 += "<li id='next'>"
			            		+ "<a href=\"#!\">"
			            		+ "<span><i class=\"mdi mdi-chevron-right\"></i></span>"
			            		+ "</a>" + "</li>";
			            }
			            $("#pages").html(str1);
		            }else{
		            	$("#userList").html("<p>暂无用户信息</p>");
		            }
	        	}else{
	        		$("#userList").html("<p>暂无用户信息</p>");
	        	}
	        }
	   });
	}
	
//	页面跳转
	$(this).on('click','.pageIndex a',function(){
		var index = $(this).text();
		currentPage = index;
		currentPoint = (currentPage-1)*len
		var url = "http://localhost:8080/2020_wxapp/getUserList"
		Data = {"len":len,"inital":currentPoint}
		page_user_load(url,Data);
	});
//	上一页
	$(this).on('click','#pre',function(){
		if(currentPage != 1){
			currentPage--;
			currentPoint = (currentPage-1)*len
			var url = "http://localhost:8080/2020_wxapp/getUserList"
			Data = {"len":len,"inital":currentPoint}
			page_user_load(url,Data);
		}
	});
	
//	下一页
	$(this).on('click','#next',function(){
		if(currentPage != Math.ceil(total/len)){
			currentPage++;
			currentPoint = (currentPage-1)*len
			var url = "http://localhost:8080/2020_wxapp/getUserList"
			Data = {"len":len,"inital":currentPoint}
			page_user_load(url,Data);
		}
	});
//	删除
	$(this).on('click','#delete_bookinfo',function(){
		if(!search_state){
			var userId = $(this).parent().parent().siblings().eq(1).text();
			if(currentPageList.length == 1){
				currentPage--;
			}
			currentPoint = (currentPage-1)*len;
			var url = "http://localhost:8080/2020_wxapp/deleteUser";
			Data = {"userId":userId,"len":len,"inital":currentPoint}
			page_user_load(url,Data);
		}
		else{
			var userId = $(this).parent().parent().siblings().eq(1).text();
			var url = "http://localhost:8080/2020_wxapp/deleteUser";
			Data = {"userId":userId,"len": -1}
			$.ajax({
		        url:url,
		        type:"POST",
		        dataType:"json",
		        data:Data,
		        success:function(data){
		        	if(data.success){
		        		var url = "http://localhost:8080/2020_wxapp/searchUser";
		    		    Data = {"keyword":search_key};
		    		    search_ajax(url, Data);
		        	}
	        	}
		    })
		    
		}
	});
	
//	搜索
	$('#search').bind('keypress', function (event) { 
	   var keycode = (event.keyCode ? event.keyCode : event.which);
	   if(keycode == '13'){
		   var keyword = $("#search").val();
		   console.log(keyword)
		   search_key = keyword;
		   search_state = true;
		   var url = "http://localhost:8080/2020_wxapp/searchUser";
		   Data = {"keyword":keyword};
		   search_ajax(url, Data);  
	   }
	})
	
	//点击返回按钮返回原来的界面
	$("#backBtn").click(function(){
	   $("#backBtn").css("display","none");
	   $("#pages").css("display","block");
	   search_state = !search_state;
	   currentPoint = (currentPage-1)*len
	   var url = "http://localhost:8080/2020_wxapp/getUserList"
	   Data = {"len":len,"inital":currentPoint}
	   page_user_load(url,Data);
	})
	
	function search_ajax(url,data){
		 $.ajax({
		        url:url,
		        type:"POST",
		        dataType:"json",
		        data:data,
		        success:function(data){
		        	console.log(data)
		        	if(data.success){
			            var userlist = data.list;
			            console.log(userlist)
			            str = "";
			            if(userlist.length != 0){
			            	for(var i=0;i<userlist.length;i++){
				            	str += "<tr>";
			            		str	+= "<th scope=\"row\">"+(i+1)+"</th>";
			            		str	+= "<td>"+userlist[i].userId+"</td>";
			            		str	+= "<td>"+userlist[i].name+"</td>";
			            		str	+= "<td>"+userlist[i].gender+"</td>";
			            		str	+= "<td>";
			            		str	+= "<div class='btn-group'>";
			            		str	+= "<a id=\"delete_bookinfo\" class=\"btn btn-xs btn-default\" href='#' title=\"删除\" data-toggle=\"tooltip\"><i class=\"mdi mdi-window-close\"></i></a>";
			            		str	+= "</div>";
			            		str	+= "</td>";
			            		str	+= "</tr>";
				            }
				            $("#userList").html(str);
			            }else{
			            	$("#userList").html("<h3>暂无用户信息</h3>");
			            }
		        	}else{
		        		$("#userList").html("<h3>暂无用户信息</h3>");
		        	}
		        	
		        	//显示返回按钮
		        	$("#backBtn").css("display","block");
		        	$("#pages").css("display","none");
		        }
		   }); 
	}
})