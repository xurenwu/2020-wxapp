$(function() {

	var total = 0;
	var currentPageList = [];
	var len = 10;
	var currentPoint = 0;
	var currentPage = 1;
	var Data = {};
	

	var select_chapterId = 0; // 选中的book的dom对象
	var select_bookId_arr = new Array();
	
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
		"len" : len,
		"inital" : currentPoint
	};
	page_chapter_load("http://localhost:8080/2020_wxapp/getChapterList", Data);

	function page_chapter_load(url, Data) {
		
		$.ajax({
			url : url,
			type : "POST",
			dataType : "json",
			data : Data,
			async : false,
			success : function(data) {
				console.log(data)
				if (data.success) {
					total = data.data;
					currentPageList = data.list;
					console.log(currentPageList)
					str = "";
					if (currentPageList.length != 0) {
						for (var i = 0; i < currentPageList.length; i++) {
							str += "<tr>"
								+ "<td style=\"width: 50px;\">"
								+ "<label class=\"lyear-checkbox checkbox-primary\">"
								+ "<input type=\"checkbox\" name=\"ids[]\" value=\"" + currentPageList[i].chapterId +"\"><span></span>"
								+ "</label>"
								+ "</td>"
								+ "<td>" + currentPageList[i].chapterId + "</td>"
								+ "<td>" + currentPageList[i].bookId + "</td>"
								+ "<td>" + currentPageList[i].bookName + "</td>"
								+ "<td>" + currentPageList[i].chapterName + "</td>"
								+ " <td>" + currentPageList[i].chapterUrl + "</td>"
								+ "<td>"
								+ "<div class=\"btn-group\">"
								+ "<a id=\"\" class=\"btn btn-xs btn-default edit_chapterinfo\" href=\"#Modal\" data-toggle=\"modal\" ><i class=\"mdi mdi-pencil\"></i></a>"
								+ "<a id=\"\" class=\"btn btn-xs btn-default delete_chapter\" href=\"#!\" title=\"删除\" data-toggle=\"tooltip\"><i class=\"mdi mdi-window-close\"></i></a>"
								+ "</div>"
								+ "</td>"
								+ "</tr>"
						}
						$("#chapterlist").html(str);

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
						$("#chapterList").html("<p>暂无书籍信息</p>");
					}
				} else {
					$("#chapterList").html("<p>暂无书籍信息</p>");
				}
			}
		});
	}
	
	//页面跳转
	$(this).on('click','.pageIndex a',function(){
		var index = $(this).text();
		currentPage = index;
		currentPoint = (currentPage-1)*len
		var url = "http://localhost:8080/2020_wxapp/getChapterList"
		Data = {"len":len,"inital":currentPoint}
		page_chapter_load(url,Data);
	});
//	上一页
	$(this).on('click','#pre',function(){
		if(currentPage != 1){
			currentPage--;
			currentPoint = (currentPage-1)*len
			var url = "http://localhost:8080/2020_wxapp/getChapterList"
			Data = {"len":len,"inital":currentPoint}
			page_chapter_load(url,Data);
		}
	});
	
//	下一页
	$(this).on('click','#next',function(){
		if(currentPage != Math.ceil(total/len)){
			currentPage++;
			currentPoint = (currentPage-1)*len
			var url = "http://localhost:8080/2020_wxapp/getChapterList"
			Data = {"len":len,"inital":currentPoint}
			page_chapter_load(url,Data);
		}
	});
	
	//动态加载模态框
	$(this).on('click','.edit_chapterinfo',function(){
		console.log("动态夹加载");
		var select_chapter = $(this).parent().parent().siblings();
		select_chapterId = select_chapter.eq(1).text();
		$("#bookId").val(select_chapter.eq(2).text());
		$("#bookname").val(select_chapter.eq(3).text());
		$("#chapterName").val(select_chapter.eq(4).text());
		$("#chapterUrl").val(select_chapter.eq(5).text());
	});
	
	//提交修改模态框
	$(this).on('click','#chapter_submit',function(){
		console.log("修改模态框");
		//先检验模态框中的数据是否有空的
		if(checkInput()){
			var chapter_data={
				"chapterId":select_chapterId,
				"bookId":$("#bookId").val(),
				"bookName":$("#bookname").val(),
				"chapterName":$("#chapterName").val(),
				"chapterUrl":$("#chapterUrl").val(),
			}
			chapter_data = JSON.stringify(chapter_data);
			Data={
				"chapter":chapter_data,
				"len" : len,
				"inital" : currentPoint
			}
			var url="http://localhost:8080/2020_wxapp/updateChapterInfo";
			console.log(Data);
			page_chapter_load(url,Data);
			Dialog.success( "修改章节信息", "章节修改成功" );
		}
		
	})
	//检验模态框的输入框是否为空
	function checkInput(){
		var bookName = $("#bookname").val();
		var bookId = $("#bookId").val();
		var chapterName = $("#chapterName").val();
		var chapterUrl = $("#chapterUrl").val();
		console.log(bookName+bookId+chapterName+chapterUrl);
		if(bookName.length<=0 || chapterName.length<=0 || chapterName.length<=0 || chapterUrl.length<=0){
			console.log(bookName+bookId+chapterName+chapterUrl);
			return false;
		}else{
			return true;
		}
	}
	$("#form_modal input").blur(function(){
		var _this=$(this);
		var value =_this.val();
		var placehoder = _this.prev().text();
		placehoder = placehoder.substring(0,placehoder.length-1)
		console.log(placehoder)
		if(value.length<=0){
			_this.attr("placeholder",placehoder+"不能为空");
			_this.addClass('inputChange');
		}
	})
	//删除章节
	$(this).on('click','.delete_chapter',function(){
		var _this = $(this);
		Dialog({
			title: "章节删除",
			content: "是否删除"+_this.parent().parent().siblings().eq(4).text(),
			ok: {
				callback: function () {
					var delete_chapter_list=[];
					var delete_chapterId = _this.parent().parent().siblings().eq(1).prevObject[1].firstChild.data;
					delete_chapter_list.push(delete_chapterId);
					delete_chapter_list = JSON.stringify(delete_chapter_list);			//json序列化转化为json字符串
					console.log(delete_chapter_list.length==currentPageList.length);
					console.log(delete_chapter_list)
					if(select_bookId_arr.length==currentPageList.length&&currentPage>1){
						currentPage--;
					}else {
						currentPage = currentPage;
					}
					currentPoint = (currentPage-1)*len;
					var url = "http://localhost:8080/2020_wxapp/deleteChapterList";
					Data={"chapterIdList":delete_chapter_list,"inital":currentPoint,"len":len};
					console.log(Data);
					page_chapter_load(url,Data);
					Dialog.success( "删除章节信息", "章节删除成功" );
				}
			},
			cancel: {
				callback: function () {
					return;
				}
			}
		});
		
	})
	//批量删除
	$("#delete_chapter_list").click(function (){
		console.log("批量删除");
		var checkedObj = $("#chapterlist input:checkbox:checked");
		var delete_chapter_list = new Array();
		console.log(checkedObj)
		for(var i=0;i<checkedObj.length;i++){
			delete_chapter_list.push(checkedObj[i].value)
		}
		delete_chapter_list = JSON.stringify(delete_chapter_list);			//json序列化转化为json字符串
		console.log(delete_chapter_list.length==currentPageList.length);
		console.log(delete_chapter_list)
		if(select_bookId_arr.length==currentPageList.length&&currentPage>1){
			currentPage--;
		}else {
			currentPage = currentPage;
		}
		currentPoint = (currentPage-1)*len;
		var url = "http://localhost:8080/2020_wxapp/deleteChapterList";
		Data={"chapterIdList":delete_chapter_list,"inital":currentPoint,"len":len};
		console.log(Data);
		page_chapter_load(url,Data);
		Dialog.success( "删除章节信息", "章节删除成功" );
	})
	/*============================= 新增章节==========================================*/
	
	
})