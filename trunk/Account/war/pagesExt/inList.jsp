<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ page import="com.scholers.account.util.ComUtil" %>
     <%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.scholers.account.business.impl.InService" %>
    <%
    java.util.Date date = new java.util.Date();
    String strCurDate =  ComUtil.getForDate(date);
    UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	String email = user.getEmail();

		  //月度总支出
    String sumPay = null;
    //if( request.getSession().getAttribute("sumIncome") != null){
    //	sumPay = (String) request.getSession().getAttribute("sumIncome");
    //}
    //if(sumPay == null) {
    	InService inService = new InService();
    	sumPay = ComUtil.getBigDecimal(inService.getInPay(user.getEmail()));
   // }
  //年度总支出
    String sumPayY = null;
    //if( request.getSession().getAttribute("sumIncomeY") != null){
    //	sumPayY = (String) request.getSession().getAttribute("sumIncomeY");
    //}
    if(sumPayY == null) {
    	//sumPayY = "0.00";
    	//InService inService = new InService();
    	sumPayY = ComUtil.getBigDecimal(inService.getSumInY(user.getEmail()));
    }
    String strFirstMonDate =  ComUtil.getForDate(ComUtil.getCurYearAndDate());
    %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="pagesExt/extjs3.3/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="pagesExt/css/style.css" />
<script type="text/javascript" src="pagesExt/extjs3.3/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="pagesExt/extjs3.3/ext-all.js"></script>
<script type="text/javascript" src="pagesExt/extjs3.3/source/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="pagesExt/extjs3.3/ext/themeChange.js"></script>
<title>收入列表</title>
</head>
<script type="text/javascript">
var author = '<%=email%>';
var dftDate = '<%=strCurDate%>';
	Ext.onReady(function(){
		//定义数据集对象
		var bookStore = new Ext.data.Store({
			//autoLoad :true,  
			reader: new Ext.data.JsonReader({
				totalProperty: "results",
				root: "items",
				id: "id"
			},
			Ext.data.Record.create([
				{name: 'id'},
				{name: 'useDate'},
				{name: 'price'},
				{name: 'bookName'},
				{name: 'author'},
				{name: 'typeName'},
				
				{name: 'brief'}
			])
			),
			proxy : new Ext.data.HttpProxy({
				url : 'inext.do?method=getBookList'
			})
		})
		//创建工具栏组件
		var toolbar = new Ext.Toolbar([
			{text : '新增收入',iconCls:'add',handler:showAddBook},
			 '-',
			{text : '修改收入',iconCls:'option',handler:showModifyBook},
			 '-',
			{text : '删除收入',iconCls:'remove',handler:showDeleteBooks},
			'-',
			{text : '本月总收入',iconCls:'tot',handler : showTotalPay},
			'-', 
			new Ext.Toolbar.TextItem('按时间查询：开始日期'),
			'-',   
			{
				xtype:'datefield',
				width : 150,
				allowBlank : true,
				blankText : '不能为空',
				id : 'times',
				name : 'times',
				value : '<%=strFirstMonDate%>',
				editable : false,//禁止编辑
				format:'Y-m-d'
             },
             new Ext.Toolbar.TextItem('结束日期：'),
 			'-',   
 			{
 				xtype:'datefield',
 				width : 150,
 				allowBlank : true,
 				blankText : '不能为空',
 				id : 'times2',
 				name : 'times2',
 				editable : false,//禁止编辑
 				format:'Y-m-d'
              },
             '-', 
			{text : '查询',iconCls:'find',handler:onItemCheck }
		]);  
		
		
		//创建Grid表格组件
		var cb = new Ext.grid.CheckboxSelectionModel()
		var bookGrid = new Ext.grid.GridPanel({
			applyTo : 'grid-div',
			//height:500,
		    bodyStyle:'height:100%',
			autoHeight:true,
			frame:true,
			tbar : toolbar,
			store: bookStore,
			stripeRows : true,
			autoScroll : true, 
			viewConfig : {
				autoFill : true
			},
			sm : cb,
			columns: [//配置表格列
				new Ext.grid.RowNumberer({
					header : '行号',
					width : 40
				}),//表格行号组件
				cb,
				{header: "收入编号", width: 1, dataIndex: 'id', sortable: true,hidden:true},
				{header: "收入日期", width: 60, dataIndex: 'useDate', sortable: true,align : 'center'},
				{header: "金额", width: 60, dataIndex: 'price', sortable: true, align : 'right'},
				{header: "收入名称", width: 60, dataIndex: 'bookName', sortable: true,hidden:true},
				{header: "作者", width: 80, dataIndex: 'author', sortable: true,align : 'center'},
				{header: "类型", width: 80, dataIndex: 'typeName', sortable: true,align : 'center'},
				
				{header: "备注", width: 80, dataIndex: 'brief', sortable: true}
			],
			 bbar: new Ext.PagingToolbar({
		        pageSize: 15,    
		        store: bookStore,
		        displayInfo: true,
		        displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
		        emptyMsg: "没有记录"
	    	})

		});
		bookStore.load({params:{start:0, limit:15}});
		
		//分页条件参数  
		bookStore.on('beforeload',function(){  
		 Ext.apply(  
		  this.baseParams,  
		  {  
			   dd:Ext.get('times').getValue(),
		       endtime:Ext.get('times2').getValue()
		     }  
		 );  
		});  
		//创建新增或修改收入信息的form表单
		Ext.QuickTips.init();
		Ext.form.Field.prototype.msgTarget = 'side';//统一指定错误信息提示方式
		var bookForm = new Ext.FormPanel({
			labelSeparator : "：",
			frame:true,
			border:false,
			items : [
				{
					xtype:'textfield',
					width : 200,
					name : 'price',
					fieldLabel:'金额'
				},{
					xtype:'hidden',
					width : 200,
					allowBlank : false,
					blankText : '收入名称不能为空',
					name : 'bookName',
					value:'pay',
					fieldLabel:'收入名称'
				},{
					xtype:'textfield',
					width : 200,
					allowBlank : false,
					blankText : '收入作者不能为空',
					name : 'author',
					value:'<%=email%>',
					readOnly:true,//禁止编辑
					fieldLabel:'作者'
				},{
					xtype:'combo',
					width : 200,
					allowBlank : false,
					blankText : '必须选择收入类型',
					hiddenName : 'bookTypeId',
					name : 'typeName',
					store : new Ext.data.Store({
						autoLoad :true,
						reader: new Ext.data.JsonReader({
							totalProperty: "results",
							root: "items"
						},
						Ext.data.Record.create([
							{name: 'id'},
							{name: 'title'},
							{name: 'detail'}
						])
						),
						proxy : new Ext.data.HttpProxy({
							url : 'inext.do?method=getBookTypeListAll'
						})
					}),//设置数据源
					allQuery:'allbook',//查询全部信息的查询字符串
					triggerAction: 'all',//单击触发按钮显示全部数据
					readOnly:false,//禁止编辑
					loadingText : '正在加载收入类型信息',//加载数据时显示的提示信息
					displayField:'title',//定义要显示的字段
					valueField : 'id',
					emptyText :'请选择收入类型',
					mode: 'remote',//远程模式
					fieldLabel:'类型'
				},{	

					xtype:'datefield',
					width : 150,
					allowBlank : false,
					editable : false,//禁止编辑
					blankText : '不能为空',
					name : 'useDate',
					emptyText:'请选择日期', 
					format:'Y-m-d',
				
					fieldLabel:'收入日期'
						

				},{
					xtype:'textarea',
					width : 200,
					name : 'brief',
					fieldLabel:'备注'
				},{
					xtype:'hidden',
					name : 'id'
				}
			],
			buttons:[
				{
					text : '关闭',
					handler : function(){
						win.hide();
					}
				},{
					text : '提交',
					handler : submitForm
				}
			]
		});
		//创建弹出窗口
		var win = new Ext.Window({
			layout:'fit',
		    width:380,
		    closeAction:'hide',
		    height:280,
			resizable : false,
			shadow : true,
			modal :true,
		    closable:true,
		    bodyStyle:'padding:5 5 5 5',
		    animCollapse:true,
			items:[bookForm]
		});
		//显示新建收入窗口
		function showAddBook(){
			bookForm.form.reset();
			//设置默认值
			var fields = bookForm.form.items;
			for(var i = 0 ; i < fields.length ; i++){
				var item = 	fields.itemAt(i);
				if(item.getName() == 'author'){
					item.setValue(author);
				} else if(item.getName() == 'useDate'){
					item.setValue(dftDate); //默认日期
				}
			}
			bookForm.isAdd = true;
			win.setTitle("新增收入信息");
			win.show();
		}
		//显示修改收入窗口
		function showModifyBook(){
			var bookList = getBookIdList();
			var num = bookList.length;
			if(num > 1){
				Ext.MessageBox.alert("提示","每次只能修改一条收入信息。")
			}else if(num == 1){
				bookForm.form.reset();
				bookForm.isAdd = false;
				win.setTitle("修改收入信息");
				win.show();
				var bookId = bookList[0];
				loadForm(bookId);
			}
		}
		//显示删除收入对话框
		function showDeleteBooks(){
			var bookList = getBookIdList();
			var num = bookList.length;
			if(num == 0){
				return;
			}
			Ext.MessageBox.confirm("提示","您确定要删除所选收入吗？",function(btnId){
				if(btnId == 'yes'){
					deleteBooks(bookList);
				}
			})
		}
		//删除收入
		function deleteBooks(bookList){
			var bookIds = bookList.join('-');
			var msgTip = Ext.MessageBox.show({
				title:'提示',
				width : 250,
				msg:'正在删除收入信息请稍后......'
			});
			Ext.Ajax.request({
				url : 'inext.do?method=deleteBooks',
				params : {bookIds : bookIds},
				method : 'POST',
				success : function(response,options){
					msgTip.hide();
					var result = Ext.util.JSON.decode(response.responseText);
					if(result.success){
						//服务器端数据成功删除后，同步删除客户端列表中的数据
						for(var i = 0 ; i < bookList.length ; i++){
							var index = bookStore.find('id',bookList[i]);
							if(index != -1){
								var rec = bookStore.getAt(index)
								bookStore.remove(rec);
							}
						}
						Ext.Msg.alert('提示','删除收入信息成功。');
					}else{
						Ext.Msg.alert('提示','删除收入信息失败！');
					}
				},
				failure : function(response,options){
					msgTip.hide();
					Ext.Msg.alert('提示','删除收入信息请求失败！');
				}
			});
		}
		//加载表单数据
		function loadForm(bookId){
			bookForm.form.load({
			   
				//waitMsg : '正在加载数据请稍后',//提示信息
				waitTitle : '提示',//标题
				url : 'inext.do?method=getBookById',//请求的url地址
				params : {bookId:bookId},
				method:'GET',//请求方式
				success:function(form,action){//加载成功的处理函数
					//Ext.Msg.alert('提示','数据加载成功');
				},
				failure:function(form,action){//加载失败的处理函数
					Ext.Msg.alert('提示','数据加载失败');
				}
			});
		}
		//提交表单数据
		function submitForm(){
			//判断当前执行的提交操作，isAdd为true表示执行收入新增操作，false表示执行收入修改操作
			if(bookForm.isAdd){
				//新增收入信息
				bookForm.form.submit({
					clientValidation:true,//进行客户端验证
					waitMsg : '正在提交数据请稍后',//提示信息
					waitTitle : '提示',//标题
					url : 'inext.do?method=addBook',//请求的url地址
					method:'POST',//请求方式
					success:function(form,action){//加载成功的处理函数
						win.hide();
						updateBookList(action.result.bookId);
						Ext.Msg.alert('提示','新增收入成功');
					},
					failure:function(form,action){//加载失败的处理函数
						Ext.Msg.alert('提示','新增收入失败');
					}
				});
			}else{
				//修改收入信息
				bookForm.form.submit({
					clientValidation:true,//进行客户端验证
					waitMsg : '正在提交数据请稍后',//提示信息
					waitTitle : '提示',//标题
					url : 'inext.do?method=modifyBook',//请求的url地址
					method:'POST',//请求方式
					success:function(form,action){//加载成功的处理函数
						win.hide();
						updateBookList(action.result.bookId);
						Ext.Msg.alert('提示','修改收入成功');
					},
					failure:function(form,action){//加载失败的处理函数
						Ext.Msg.alert('提示','修改收入失败');
					}
				});
			}
		}
		//明细数据修改后，同步更新收入列表信息
		function updateBookList(bookId){
			var fields = getFormFieldsObj(bookId);
			var index = bookStore.find('id',fields.id);
			if(index != -1){
				var item = bookStore.getAt(index);
				for(var fieldName in fields){
					item.set(fieldName,fields[fieldName]);
				}
				bookStore.commitChanges();
			}else{
				var rec = new Ext.data.Record(fields);
				bookStore.add(rec);
			}
		}
		//取得表单数据
		function getFormFieldsObj(bookId){
			var fields = bookForm.items;
			var obj = {};
			for(var i = 0 ; i < fields.length ; i++){
				var item = 	fields.itemAt(i);
				var value = item.getValue();
				if(item.getXType() == 'combo'){
					var index = item.store.find('id',value);
					if(index != -1){
						var selItem = item.store.getAt(index);
					}
					value = selItem.get('title');
				}
				//日期格式化
				else if(item.getXType() == 'datefield'){
					value = Ext.util.Format.date(value,'Y-m-d');
				}
				
				obj[item.name] = value;
			}
			if(Ext.isEmpty(obj['id'])){
				obj['id'] = bookId;
			}
			return obj;
		}
		//取得所选收入
		function getBookIdList(){
			var recs = bookGrid.getSelectionModel().getSelections();
			var list = [];
			if(recs.length == 0){
				Ext.MessageBox.alert('提示','请选择要进行操作的收入！');
			}else{
				for(var i = 0 ; i < recs.length ; i++){
					var rec = recs[i];
					if(rec.get('id') != 0){
						list.push(rec.get('id'))
					}
				}
				if(list.length == 0){
					Ext.MessageBox.alert('提示','总计行不能修改或者删除！');
				}
			}
			return list;
		}
		//按时间查询方法
         function onItemCheck(){
       		  var dd=Ext.get('times').getValue();  
		       var endtime=Ext.get('times2').getValue();     
              bookStore.reload({params:{start:0,limit:15,dd:dd, endtime:endtime}}); 
       };
		//查看总支出
		function showTotalPay(){
			winP.setTitle("您的总支出信息");
			winP.show();
		}
		
		
		//弹出总收入信息
		var sumIncome = document.getElementById("sumIncome").value; 
		var sumIncomeY = document.getElementById("sumIncomeY").value; 
		var winIn = new Ext.Window({
			layout:'fit',
		    width:370,
		    closeAction:'hide',
		    height:280,
			resizable : false,
			shadow : true,
			modal :true,
		    closable:true,
		    bodyStyle:'padding:5 5 5 5',
		    animCollapse:true,
			items:{  
					title:"总收入",  
					collapsible:true,
					html : '<br><center><font size = 6>本月总收入为：</font><font size = 5 color= red>'+sumIncome+'元</font></center><br/><center><font size = 6>本年总收入为：</font><font size = 5 color= red>'+sumIncomeY+'元</font></center><br/>'
				  }
		});

		//查看总收入
		function showTotalPay(){
			winIn.setTitle("您的总收入信息");
			winIn.show();
		}
	});
</script>
<body>
<div id='grid-div' style="width:100%; height:100%;"/>
<input type="hidden" name="sumIncome" id="sumIncome"  value="<%=sumPay %>"/>
<input type="hidden" name="sumIncomeY" id="sumIncomeY" value="<%=sumPayY%>"/>
</body>
</html>