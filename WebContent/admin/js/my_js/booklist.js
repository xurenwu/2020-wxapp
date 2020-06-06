$(function(){
	
//	书籍列表js文件
	var total=0;
	var currentPageList=[];
	var len=10;
	var currentPoint = 0;
	var currentPage = 1;
	var Data = {};
	var search_state = false; 		//是否在搜索页面
	var search_key = "";			//上一次的搜索记录\
	var bookId=0;			//选中的book的dom对象
	var select_heat="";
	var select_enterTime="";
	var select_bookId_arr=new Array();
	
	Data={"len":len,"inital":currentPoint};
	page_book_load("http://localhost:8080/2020_wxapp/getBookList",Data);
	
	//页面初次加载
	function page_book_load(url,Data){
		$.ajax({
	        url:url,
	        type:"POST",
	        dataType:"json",
	        data:Data,
	        async: false,
	        success:function(data){
	        	if(data.success){
	        		total = data.data;
		            currentPageList = data.list;
		            console.log(currentPageList)
		            str = "";
		            if(currentPageList.length != 0){
		            	for(var i=0;i<currentPageList.length;i++){
			            	str += "<tr>"
			            		+ "<td><label class=\"lyear-checkbox checkbox-primary\">"
			            		+ "<input type=\"checkbox\" name=\"ids[]\" value=\""+ currentPageList[i].bookId +"\"><span></span>"
			            		+ "</label>"
			            		+ "</td>"
			            		+ "<td>" + currentPageList[i].bookId + "</td>"
			            		+ "<td>" + currentPageList[i].bookName + "</td>"
			            		+ "<td>" + currentPageList[i].author + "</td>"
			            		+ "<td>" + currentPageList[i].category + "</td>"
			            		+ "<td class='book_desc'><div>" + currentPageList[i].desc + "</div></td>"
			            		+ "<td>" + currentPageList[i].lastChapter + "</td>"
			            		+ "<td>" + currentPageList[i].heat + "</td>"
			            		+ "<td><font class=\"text-success\">" + currentPageList[i].state + "</font></td>"
			            		+ "<td>" + currentPageList[i].enterTime + "</td>"
			            		+ "<td><div class=\"btn-group\">"
			            		+ "<a class=\"btn btn-xs btn-default edit_bookinfo\" href=\"#!\" data-toggle=\"modal\" data-target=\"#Modal\"><i class=\"mdi mdi-pencil\"></i></a>"
			            		+ "<a class=\"btn btn-xs btn-default delete_bookinfo\" href=\"#!\" title=\"删除\" data-toggle=\"tooltip\"><i class=\"mdi mdi-window-close\"></i></a>"
			            		+ "</div>"
			            		+ "</td>"
			            		+ "</tr>"
		            		
			            }
			            $("#booklist").html(str);
			            
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
		            	$("#userList").html("<p>暂无书籍信息</p>");
		            }
	        	}else{
	        		$("#userList").html("<p>暂无书籍信息</p>");
	        	}
	        }
	   });
	}
	
	
	//页面跳转
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
	
	
	//动态加载模态框
	$(this).on('click','.edit_bookinfo',function(){
		console.log("动态夹加载");
		var select_book_info = $(this).parent().parent().siblings();
		bookId = select_book_info[1].innerText;
		select_enterTime = select_book_info[9].innerText;
		select_heat = select_book_info[7].innerText;
		
		var select_bookId = $(this).parent().parent().siblings().eq(1).text();
		for(var i=0;i<currentPageList.length;i++){
			console.log(currentPageList[i]);
			if(select_bookId == currentPageList[i].bookId){
				var select_book = currentPageList[i];
				$("#bookname").val(select_book.bookName)
				$("#author").val(select_book.author)
				var select = $('#category-select option')
				for(var j=0; j<select.length; j++){
					if(select[j].firstChild.data == select_book.category){  
						select[j].selected = true;    
	                }  
				}
				$("#desc").val(select_book.desc)
				$("#bookImage").val(select_book.bookImage)
				$("#lastChapter").val(select_book.lastChapter)
				$("#lastChapterUrl").val(select_book.lastChapterUrl)
				var book_state = $('#book_state input:radio');
				for(var i=0; i<book_state.length; i++){
					if(book_state.eq(i).val() == select_book.state){
						book_state.eq(i).prop('checked',true);  
						break;
					}
				}
			break;
				
			}
		}
	});
	
	//提交modal修改书籍信息
	$("#submit_modal").click(function(){
		var url = "http://localhost:8080/2020_wxapp/updateBookInfo";
		Data={
			"bookId":bookId,
			"bookName":$("#bookname").val(),
			"author":$("#author").val(),
			"category":$("#category-select option:selected").text(),
			"desc":$("#desc").val(),
			"bookImage":$("#bookImage").val(),
			"lastChapter":$("#lastChapter").val(),
			"lastChapterUrl":$("#lastChapterUrl").val(),
			"state":$("#book_state input[name='e']:checked").val(),
			"heat":select_heat,
			"enterTime":select_enterTime,
			"len":len,"inital":currentPoint
		}
		if(search_state){
			Data.Key=search_key;
			Data.len=-1;
			alert(Data.Key)
			search_ajax(url,Data)
		}else{
			page_book_load(url,Data);
		}
			
	})
	
	//检验输入框不能为空
	function checkInput(){
		var bookName = $("#bookname").val();
		var author = $("#author").val();
		var desc = $("#desc").val();
		var bookImage = $("#bookImage").val();
		var lastChapter = $("#lastChapter").val();
		var lastChapterUrl = $("#lastChapterUrl").val();
		
		if(bookName.length<=0 || author.length<=0 || desc.length<=0 || bookImage.length<=0 || lastChapter.length<=0 || lastChapterUrl.length<=0){
			console.log(bookName.length+" "+author.length+" "+desc.length);
			console.log(bookImage.length+" "+lastChapter.length+" "+lastChapterUrl.length);
			return false;
		}else{
			return true;
		}
	}
	$(".form-group input").blur(function(){
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
	
	$(".form-group textarea").blur(function(){
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
	
	//删除书籍
	$(this).on('click','.delete_bookinfo',function(){
		var _this = $(this);
		var delete_bookId = _this.parent().parent().siblings().eq(1).prevObject[1].firstChild.data;
		select_bookId_arr.push(delete_bookId);
		select_bookId_arr = JSON.stringify(select_bookId_arr);			//json序列化转化为json字符串
		console.log(select_bookId_arr.length==currentPageList.length);
		if(!search_state){
			//判段是不是再搜索页面，不在搜索页面不用加载分页符
			//判断是不再书籍首页，否则如果当前页面数据删除完后当前页面currentPage要减一
			if(select_bookId_arr.length==currentPageList.length&&currentPage>1){
				currentPage--;
			}else {
				currentPage = currentPage;
			}
			currentPoint = (currentPage-1)*len;
			var url = "http://localhost:8080/2020_wxapp/deleteBookInfo";
			Data={"bookIdArr":select_bookId_arr,"inital":currentPoint,"len":len};
			page_book_load(url,Data);
		}else{
			//如果是搜索页面直接设置当前页面为首页
			currentPage = 1;
			currentPoint = (currentPage-1)*len;
			var url = "http://localhost:8080/2020_wxapp/deleteBookInfo";
			Data={"Key":search_key,"bookIdArr":select_bookId_arr,"inital":currentPoint,"len":-1};
		    search_ajax(url, Data); 
		}
	});
	
	//批量删除
	$("#batch_delete").click(function(){
		var checkedObj = $("#booklist input:checkbox:checked");
		var booklist = new Array();
		for(var i=0;i<checkedObj.length;i++){
			booklist.push(checkedObj[i].value);
		}
		booklist = JSON.stringify(booklist);			//json序列化转化为json字符串
		console.log(booklist)
		if(!search_state){
			//判段是不是再搜索页面，不在搜索页面不用加载分页符
			//判断是不再书籍首页，否则如果当前页面数据删除完后当前页面currentPage要减一
			if(booklist.length==currentPageList.length&&currentPage>1){
				currentPage--;
			}else {
				currentPage = currentPage;
			}
			currentPoint = (currentPage-1)*len;
			var url = "http://localhost:8080/2020_wxapp/deleteBookInfo";
			Data={"bookIdArr":booklist,"inital":currentPoint,"len":len};
			page_book_load(url,Data);
		}else{
			//如果是搜索页面直接设置当前页面为首页
			currentPage = 1;
			currentPoint = (currentPage-1)*len;
			var url = "http://localhost:8080/2020_wxapp/deleteBookInfo";
			Data={"Key":search_key,"bookIdArr":booklist,"inital":currentPoint,"len":-1};
		    search_ajax(url, Data); 
		}
		
	})
	//批量删除
	
	//搜索书籍
	$('#search').bind('keypress', function (event) { 
	   var keycode = (event.keyCode ? event.keyCode : event.which);
	   if(keycode == '13'){
		   var keyword = $("#search").val();
		   //判断搜索关键字是否为空为空则提示输入关键字为空
		   if(keyword.length>0){
			   search_key = keyword;
			   search_state = true;
			   var url = "http://localhost:8080/2020_wxapp/search";
			   Data = {"Key":keyword};
			   search_ajax(url, Data);  
		   }else{
			   $("#search").attr('placeholder',"请输入关键字...");
		   }
	   }
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
			            var booklist = data.list;
			            console.log(booklist)
			            currentPageList = booklist;
			            str = "";
			            if(booklist.length != 0){
			            	for(var i=0;i<currentPageList.length;i++){
			            		str += "<tr>"
				            		+ "<td><label class=\"lyear-checkbox checkbox-primary\">"
				            		+ "<input type=\"checkbox\" name=\"ids[]\" value=\""+ currentPageList[i].bookId +"\"><span></span>"
				            		+ "</label>"
				            		+ "</td>"
				            		+ "<td>" + currentPageList[i].bookId + "</td>"
				            		+ "<td>" + currentPageList[i].bookName + "</td>"
				            		+ "<td>" + currentPageList[i].author + "</td>"
				            		+ "<td>" + currentPageList[i].category + "</td>"
				            		+ "<td class='book_desc'><div>" + currentPageList[i].desc + "</div></td>"
				            		+ "<td>" + currentPageList[i].lastChapter + "</td>"
				            		+ "<td>" + currentPageList[i].heat + "</td>"
				            		+ "<td><font class=\"text-success\">" + currentPageList[i].state + "</font></td>"
				            		+ "<td>" + currentPageList[i].enterTime + "</td>"
				            		+ "<td><div class=\"btn-group\">"
				            		+ "<a class=\"btn btn-xs btn-default edit_bookinfo\" href=\"#!\" data-toggle=\"modal\" data-target=\"#Modal\"><i class=\"mdi mdi-pencil\"></i></a>"
				            		+ "<a class=\"btn btn-xs btn-default delete_bookinfo\" href=\"#!\" title=\"删除\"  data-toggle=\"tooltip\"><i class=\"mdi mdi-window-close\"></i></a>"
				            		+ "</div>"
				            		+ "</td>"
				            		+ "</tr>"
				            }
			            	$("#booklist").html(str);
			            }else{
			            	$(".table-responsive").html("<h3>暂无书籍信息</h3>");
			            }
			            $(".pages").css("display","none");
			            $("#back_div").css("display","block");
			            $("#head_div").css("display","none");   
		        	} else{
		        		$(".table-responsive").html("<h3>暂无书籍信息</h3>");
		        	}
		        }
		 });
	}
	
	//点击返回按钮返回原来的界面
	$("#backBtn").click(function(){
	   $("#back_div").css("display","none");
	   $(".pages").css("display","block");
       $("#head_div").css("display","block"); 
	   search_state = !search_state;
	   currentPoint = (currentPage-1)*len
	   var url = "http://localhost:8080/2020_wxapp/getBookList"
	   Data = {"len":len,"inital":currentPoint}
	   page_book_load(url,Data);
	})
	
})