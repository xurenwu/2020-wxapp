$(function() {

	var total = 0;
	var currentPageList = [];
	var len = 10;
	var currentPoint = 0;
	var currentPage = 1;
	var Data = {};

	var bookId = 0; // 选中的book的dom对象
	var select_heat = "";
	var select_enterTime = "";
	var select_bookId_arr = new Array();
	Data = {
		"len" : len,
		"inital" : currentPoint
	};
	page_chapter_load("http://localhost:8080/2020_wxapp/getBookList", Data);

	function page_chapter_load(url, Data) {
		
		$.ajax({
			url : url,
			type : "POST",
			dataType : "json",
			data : Data,
			async : false,
			success : function(data) {
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
								+ "<input type=\"checkbox\" name=\"ids[]\" value=\"1\"><span></span>"
								+ "</label>"
								+ "</td>"
								+ "<td>2050</td>"
								+ "<td>20201000</td>"
								+ "<td>诺威的森林</td>"
								+ "<td>编者语</td>"
								+ " <td>/foreign/nwdsl/1000.txt</td>"
								+ "<td>"
								+ "<div class=\"btn-group\">"
								+ "<a id=\"edit_bookinfo\" class=\"btn btn-xs btn-default\" href=\"#Modal\" data-toggle=\"modal\" ><i class=\"mdi mdi-pencil\"></i></a>"
								+ "<a id=\"delete_bookinfo\" class=\"btn btn-xs btn-default\" href=\"#\" title=\"删除\" data-toggle=\"tooltip\"><i class=\"mdi mdi-window-close\"></i></a>"
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
})