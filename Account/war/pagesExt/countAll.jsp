<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ page import="com.scholers.account.util.ComUtil" %>
<%@ page import="com.scholers.account.business.impl.PayService" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
    <%
    java.util.Date date = new java.util.Date();
    String strCurDate =  ComUtil.getForDate(date);
    UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	String email = user.getEmail();
   
    String strFirstMonDate =  ComUtil.getForDate(ComUtil.getCurYearAndDate());
	
    %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支出列表</title>
</head>
<script type="text/javascript">

	Ext.onReady(function(){   
		//定义数据集对象
		var payStore = new Ext.data.Store({
			//autoLoad :true,
			reader: new Ext.data.JsonReader({
				totalProperty: "results",
				root: "items",
				id: "id"
			},
			Ext.data.Record.create([
				{name: 'id'},
				{name: 'author'},
				{name: 'notes'},
				
				{name: 'price'}
			])
			),
			proxy : new Ext.data.HttpProxy({
				url : 'analysis.do?method=getCountList'
			})
		})
		//创建工具栏组件
		var toolbar = new Ext.Toolbar([
			
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
		var payGrid = new Ext.grid.GridPanel({
		    //height:500,
		    bodyStyle:'height:100%',
			autoHeight:true,
			applyTo : 'grid-div',
			frame:true,
			tbar : toolbar,
			store: payStore,
			stripeRows : true,
			autoScroll : true,
			viewConfig : {
				autoFill : true
			},
			columns: [//配置表格列
				new Ext.grid.RowNumberer({
					header : '行号',
					width : 40
				}),//表格行号组件
				{header: "编号", width: 10, dataIndex: 'id', sortable: true,hidden:true},
				{header: "统计项目", width: 40, dataIndex: 'notes', sortable: true,align : 'right'},
				{header: "金额", width: 40, dataIndex: 'price', sortable: true, align : 'right'},
				{header: "作者", width: 40, dataIndex: 'author', sortable: true,align : 'center'}
				
			],
		
 			 bbar: new Ext.PagingToolbar({
 		        pageSize: 15,  
 		        store: payStore,
 		        displayInfo: true,
 		        displayMsg: "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
 		        emptyMsg: "没有记录"
 		   		
 	    	})
	    	 
		});

		payStore.load(({params:{start:0, limit:15},
			callback: function(records, options, success){
				//records是数据 options是遍历用的数据这些大家都可以自己去实验！
				//设置默认值
				var bbar2 = payGrid.bbar;
				 var dd=Ext.get('times2').getValue();    
				
			}}));

		//分页条件参数  
		payStore.on('beforeload',function(){  
		 Ext.apply(  
		  this.baseParams,  
		  {  
			   dd:Ext.get('times').getValue(),
		       endtime:Ext.get('times2').getValue()
		     }  
		 );  
		});  
		function onItemCheck(){
		        var dd=Ext.get('times').getValue(); 
		        var endtime=Ext.get('times2').getValue();     
		        payStore.reload({params:{start:0,limit:15,dd:dd,endtime:endtime}}); 
		};

		//创建新增或修改支出信息的form表单
		//Ext.QuickTips.init();
		//Ext.form.Field.prototype.msgTarget = 'side';//统一指定错误信息提示方式
	
	});
	
		
</script>
<body>
<div id='grid-div' style="width:100%; height:500"/>

</body>
</html>