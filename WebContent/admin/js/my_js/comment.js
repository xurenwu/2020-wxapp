$(function(){
	var total = 0;
	var currentPageList = [];
	var len = 10;
	var currentPoint = 0;
	var currentPage = 1;
	var Data = {};
	
	//加载页面头部
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

	var select_commentId_arr = new Array();
	Data = {
		"len" : len,
		"inital" : currentPoint
	};
	page_comment_load("http://localhost:8080/2020_wxapp/getCommentList", Data);
	
	
	function page_comment_load(url, Data) {
			
			$.ajax({
				url : url,
				type : "POST",
				dataType : "json",
				data : Data,
				async : true,
				success : function(data) {
					console.log(data)
					total = data.data;
					currentPageList = data.list;
					console.log(currentPageList)
					str = "";
					if (currentPageList.length != 0) {
						for (var i = 0; i < currentPageList.length; i++) {
							str += "<tr>"
								+ "<td style=\"width: 50px;\">"
								+ "<label class=\"lyear-checkbox checkbox-primary\">"
								+ "<input type=\"checkbox\" name=\"ids[]\" value=\"" + currentPageList[i].commentId +"\"><span></span>"
								+ "</label>"
								+ "</td>"
								+ "<td>" + currentPageList[i].commentId +"</td>"
								+ "<td>" + currentPageList[i].bookId +"</td>"
								+ "<td>" + currentPageList[i].userId +"</td>"
								+ "<td class=\"detail\"><div>" + currentPageList[i].Content +"</div></td>"
								+ "<td>" + currentPageList[i].commentTime +"</td>"
								+ "<td>"
								+ "<div class=\"btn-group\">"
								+ "<a id=\"delete_comment\" class=\"btn btn-xs btn-default\" href=\"#\" title=\"删除\" data-toggle=\"tooltip\"><i class=\"mdi mdi-window-close\"></i></a>"
								+ "</div>"
								+ "</td>"
								+ "</tr>"
						}
						$("#commentList").html(str);
						
						// 分页
						var str1 = "";
						if (currentPage == 1) {
							str1 += "<li id='pre' class=\"disabled\">"
									+ "<a href=\"#\">"
									+ "<span><i class=\"mdi mdi-chevron-left\"></i></span>"
									+ "</a>" + "</li>";
						} else {
							str1 += "<li id='pre'>"
									+ "<a href=\"#\">"
									+ "<span><i class=\"mdi mdi-chevron-left\"></i></span>"
									+ "</a>" + "</li>";
						}
						for (var j = 0; j < Math.ceil(total / len); j++) {
							if (j + 1 == currentPage)
								str1 += "<li class=\"pageIndex active\"><a href=\"#\">"
										+ (j + 1) + "</a></li>"
							else
								str1 += "<li class='pageIndex'><a href=\"#!\">"
										+ (j + 1) + "</a></li>"
						}
						if (currentPage == Math.ceil(total / len)) {
							str1 += "<li id='next' class=\"disabled\">"
									+ "<a href=\"#!\">"
									+ "<span><i class=\"mdi mdi-chevron-right\"></i></span>"
									+ "</a>" + "</li>";
						} else {
							str1 += "<li id='next'>"
									+ "<a href=\"#!\">"
									+ "<span><i class=\"mdi mdi-chevron-right\"></i></span>"
									+ "</a>" + "</li>";
						}
						$("#pages").html(str1);
					} else {
						$("#commentList").html("<p>暂无评论信息</p>");
					}
				} 
			})
	}
	
	//页面跳转
	$(this).on('click','.pageIndex a',function(){
		var index = $(this).text();
		currentPage = index;
		currentPoint = (currentPage-1)*len
		var url = "http://localhost:8080/2020_wxapp/getCommentList"
		Data = {"len":len,"inital":currentPoint}
		page_comment_load(url,Data);
	});
//	上一页
	$(this).on('click','#pre',function(){
		if(currentPage != 1){
			currentPage--;
			currentPoint = (currentPage-1)*len
			var url = "http://localhost:8080/2020_wxapp/getCommentList"
			Data = {"len":len,"inital":currentPoint}
			page_comment_load(url,Data);
		}
	});
	
//	下一页
	$(this).on('click','#next',function(){
		if(currentPage != Math.ceil(total/len)){
			currentPage++;
			currentPoint = (currentPage-1)*len
			var url = "http://localhost:8080/2020_wxapp/getCommentList"
			Data = {"len":len,"inital":currentPoint}
			page_comment_load(url,Data);
		}
	});
	
	
	//删除评论
	$(this).on('click',"#delete_comment",function(){
		console.log("删除评论");
		var _this = $(this);
		Dialog({
			title: "章节删除",
			content: "是否删除评论内容",
			ok: {
				callback: function () {
					var select_commentId = _this.parent().parent().siblings().eq(1).prevObject[1].firstChild.data
					select_commentId_arr.push(select_commentId);
					select_commentId_arr = JSON.stringify(select_commentId_arr);
					console.log(select_commentId_arr);
					if(select_commentId_arr.length==currentPageList.length&&currentPage>1){
						currentPage--;
					}else {
						currentPage = currentPage;
					}
					if(select_commentId_arr.length>0){
						var url = "http://localhost:8080/2020_wxapp/deleteCommentList";
						Data = {"len":len,"inital":currentPoint,"commentIdList":select_commentId_arr}
						console.log(Data);
						page_comment_load(url,Data);
						Dialog.success( "删除评论", "评论删除成功！");
					}else{
						Dialog.warn( "删除评论", "评论删除失败！");
					}
				}
			},
			cancel: {
				callback: function(){
					return;
				}
			}
		});
		
		
	})
	
	//删除评论
	$(this).on('click',"#delete_commentList",function(){
		var _this = $(this);
		var checkedObj = $("#commentList input:checkbox:checked");
		for(var i=0;i<checkedObj.length;i++){
			select_commentId_arr.push(checkedObj[i].value)
		}
		select_commentId_arr = JSON.stringify(select_commentId_arr);
		console.log(select_commentId_arr);
		if(select_commentId_arr.length==currentPageList.length&&currentPage>1){
			currentPage--;
		}else {
			currentPage = currentPage;
		}
		if(select_commentId_arr.length>0){
			var url = "http://localhost:8080/2020_wxapp/deleteCommentList";
			Data = {"len":len,"inital":currentPoint,"commentIdList":select_commentId_arr}
			console.log(Data);
			page_comment_load(url,Data);
			Dialog.success( "删除评论", "评论删除成功！");
		}else{
			Dialog.warn( "删除评论", "评论删除失败！");
		}
	})
})