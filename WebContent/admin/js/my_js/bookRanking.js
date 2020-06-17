$(function(){
	
	
	var total = 0;
	var currentPageList = [];
	var len = 10;
	var currentPoint = 0;
	var currentPage = 1;
	var Data = {};
	var rank_key=$("#ranking_state").text();		//排行榜的排行方式
	var category = "全部";		//什么分类默认为全部
	var select_id = "all"
	
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
	
	Data = {
		"len":len,
		"currentPoint":currentPoint,
		"category":category,
		"rankingKey":rank_key
	}
	page_rank_load("http://localhost:8080/2020_wxapp/getRankingByCategory",Data);
	
	function page_rank_load(url,data){
		$.ajax({
			url : url,
			type : "POST",
			dataType : "json",
			data : data,
			async : true,
			success : function(data) {
				console.log(data);
				if(data.success){
					total = data.data;
					currentPageList = data.list;
					console.log(currentPageList)
					str = "";
					if (currentPageList.length != 0) {
						for (var i = 0; i < currentPageList.length; i++) {
							str += "<tr>"
								+"<th scope=\"row\">"+(i+1)+"</th>"
								+"<td>"+ currentPageList[i].bookId +"</td>"
								+"<td>"+ currentPageList[i].bookName +"</td>"
								+"<td>"+ currentPageList[i].category +"</td>"
								+"<td>"+ currentPageList[i].heat +"</td>"
								+"</tr>"
						}
						$("#"+select_id+" tbody").html(str)
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
						$("#chapterList").html("<p>暂无书籍信息</p>");
					}
				}else{
					console.log(data.msg)
				}
			}
		})
	}
	
	//页面跳转
	$(this).on('click','.pageIndex a',function(){
		var index = $(this).text();
		currentPage = index;
		currentPoint = (currentPage-1)*len
		var url = "http://localhost:8080/2020_wxapp/getRankingByCategory"
		Data = {
			"len":len,
			"currentPoint":currentPoint,
			"category":category,
			"rankingKey":rank_key
		}
		page_rank_load(url,Data);
	});
//	上一页
	$(this).on('click','#pre',function(){
		if(currentPage != 1){
			currentPage--;
			currentPoint = (currentPage-1)*len
			var url = "http://localhost:8080/2020_wxapp/getRankingByCategory"
			Data = {
				"len":len,
				"currentPoint":currentPoint,
				"category":category,
				"rankingKey":rank_key
			}
			page_rank_load(url,Data);
		}
	});
	
//	下一页
	$(this).on('click','#next',function(){
		if(currentPage != Math.ceil(total/len)){
			currentPage++;
			currentPoint = (currentPage-1)*len
			var url = "http://localhost:8080/2020_wxapp/getRankingByCategory"
			Data = {
				"len":len,
				"currentPoint":currentPoint,
				"category":category,
				"rankingKey":rank_key
			}
			page_rank_load(url,Data);
		}
	});
	
	//获取标签跳转
	$(this).on('click','#myTabs a',function(){
		var selectId = $(this).attr('href').substr(1);
		category = $(this).text();
		select_id = selectId;
		console.log(selectId+category);
		var url = "http://localhost:8080/2020_wxapp/getRankingByCategory"
		Data = {
			"len":len,
			"currentPoint":0,
			"category":category,
			"rankingKey":rank_key
		}
		page_rank_load(url,Data);
	})
	
})