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
    //月度总支出
    String sumPay = null;
    if( request.getSession().getAttribute("sumPay") != null){
    	sumPay = (String) request.getSession().getAttribute("sumPay");
    }
    if(sumPay == null) {
    	PayService payService = new PayService();
    	sumPay = ComUtil.getBigDecimal(payService.getSumPay(user.getEmail()));
    }
  //年度总支出
    String sumPayY = null;
    if( request.getSession().getAttribute("sumPayY") != null){
    	sumPayY = (String) request.getSession().getAttribute("sumPayY");
    }
    if(sumPayY == null) {
    	//sumPayY = "0.00";
    	PayService payService = new PayService();
    	sumPayY = ComUtil.getBigDecimal(payService.getSumPayY(user.getEmail()));
    }
    %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支出列表</title>
</head>
<script type="text/javascript">
	var author = '<%=email%>';
	var dftDate = '<%=strCurDate%>';
	Ext.onReady(function(){   
		//定义数据集对象
		var payStore = new Ext.data.Store({
			//autoLoad :true,
			reader: new Ext.data.JsonReader({
				totalProperty: "results",
				root: "items",
				id: "id",
				totalmon:"strTotalMoney"
			},
			Ext.data.Record.create([
				{name: 'id'},
				{name: 'useDate'},
				{name: 'price'},
				
				{name: 'author'},
				{name: 'typeName'},
				
				{name: 'notes'}
			])
			),
			proxy : new Ext.data.HttpProxy({
				url : 'payext.do?method=getPayList'
			})
		})
		//创建工具栏组件
		var toolbar = new Ext.Toolbar([
			{text : '新增支出',iconCls:'add',handler:showAddPay},
			 '-',
			{text : '修改支出',iconCls:'option',handler:showModifyPay},
			 '-',
			{text : '删除支出',iconCls:'remove',handler:showDeletePays},
			'-',
			{text : '本月总支出',iconCls:'tot',handler : showTotalPay},
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
 				
 				format:'Y-m-d'
              },
             '-', 
			{iconCls:'find',handler:onItemCheck }
		]);  
         	     	
		 
		//创建Grid表格组件
		var cb = new Ext.grid.CheckboxSelectionModel()
		var payGrid = new Ext.grid.GridPanel({
		    height:600,
			applyTo : 'grid-div',
			frame:true,
			tbar : toolbar,
			store: payStore,
			stripeRows : true,
			autoScroll : true,
			viewConfig : {
				autoFill : true
			},
			sm : cb,
			columns: [//配置表格列
				new Ext.grid.RowNumberer({
					header : '行号',
					width : 40,
					renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
			           if(parseInt(record.get("total"))<5){
			               return "<font style='color:blue'>"+(rowIndex+1)+"</font>"//也可以用下面的方法          
			           }
			          return (rowIndex+1)
			         }
				}),//表格行号组件
				cb,
				{header: "支出编号", width: 80, dataIndex: 'id', sortable: true,hidden:true},
				{header: "支出日期", width: 80, dataIndex: 'useDate', sortable: true,align : 'center'},
				{header: "金额", width: 80, dataIndex: 'price', sortable: true, align : 'right'},
				{header: "支出名称", width: 80, dataIndex: 'payName', sortable: true,hidden:true},
				{header: "作者", width: 80, dataIndex: 'author', sortable: true,align : 'center'},
				{header: "类型", width: 80, dataIndex: 'typeName', sortable: true,align : 'center'},
				
				{header: "备注", width: 80, dataIndex: 'notes', sortable: true}
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
				//alert(bbar2);
				 
				 //Ext.get('times2').setValue("消费10000元!");
				 var dd=Ext.get('times2').getValue();    
				//alert(dd);
				//Ext.get('times2').setValue("消费10000元!");
				
				
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
		Ext.QuickTips.init();
		Ext.form.Field.prototype.msgTarget = 'side';//统一指定错误信息提示方式
		var payForm = new Ext.FormPanel({
			labelSeparator : "：",
			frame:true,
			border:false,
			items : [
				{
					xtype:'hidden',
					width : 200,
					allowBlank : false,
					blankText : '支出名称不能为空',
					name : 'payName',
					value:'pay',
					fieldLabel:'支出名称'
				},{
					xtype:'textfield',
					width : 200,
					name : 'price',
					fieldLabel:'金额'
				},{
					xtype:'textfield',
					width : 200,
					allowBlank : false,
					blankText : '支出作者不能为空',
					name : 'author',
					value: author,
					readOnly:true,//禁止编辑
					fieldLabel:'作者'
				},{
					xtype:'combo',
					width : 200,
					allowBlank : false,
					blankText : '必须选择支出类型',
					hiddenName : 'payTypeId',
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
							url : 'payext.do?method=getPayTypeList'
						})
					}),//设置数据源
					allQuery:'allpay',//查询全部信息的查询字符串
					triggerAction: 'all',//单击触发按钮显示全部数据
					readOnly:false,//编辑
					loadingText : '正在加载支出类型信息',//加载数据时显示的提示信息
					displayField:'title',//定义要显示的字段
					valueField : 'id',
					emptyText :'请选择支出类型',
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
				
					fieldLabel:'支出日期'
				},{
					xtype:'textarea',
					width : 200,
					name : 'notes',
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
			items:[payForm]
		});
		//显示新建支出窗口
		function showAddPay(){
			payForm.form.reset();
			//设置默认值
			var fields = payForm.form.items;
			for(var i = 0 ; i < fields.length ; i++){
				var item = 	fields.itemAt(i);
				if(item.getName() == 'author'){
					item.setValue(author);
				} else if(item.getName() == 'useDate'){
					item.setValue(dftDate); //默认日期
				}
			}
			payForm.isAdd = true;
			win.setTitle("新增支出信息");
			win.show();
		}
		//显示修改支出窗口
		function showModifyPay(){
			var payList = getPayIdList();
			var num = payList.length;
			if(num > 1){
				Ext.MessageBox.alert("提示","每次只能修改一条支出信息。")
			}else if(num == 1){
				payForm.form.reset();
				payForm.isAdd = false;
				win.setTitle("修改支出信息");
				win.show();
				var payId = payList[0];
				loadForm(payId);
			}
		}
		//显示删除支出对话框
		function showDeletePays(){
			var payList = getPayIdList();
			var num = payList.length;
			if(num == 0){
				return;
			}
			Ext.MessageBox.confirm("提示","您确定要删除所选支出吗？",function(btnId){
				if(btnId == 'yes'){
					deletePays(payList);
				}
			})
		}
		//删除支出
		function deletePays(payList){
			var payIds = payList.join('-');
			var msgTip = Ext.MessageBox.show({
				title:'提示',
				width : 250,
				msg:'正在删除支出信息请稍后......'
			});
			Ext.Ajax.request({
				url : 'payext.do?method=deletePays',
				params : {payIds : payIds},
				method : 'POST',
				success : function(response,options){
					msgTip.hide();
					var result = Ext.util.JSON.decode(response.responseText);
					if(result.success){
						//服务器端数据成功删除后，同步删除客户端列表中的数据
						for(var i = 0 ; i < payList.length ; i++){
							var index = payStore.find('id',payList[i]);
							if(index != -1){
								var rec = payStore.getAt(index)
								payStore.remove(rec);
							}
						}
						Ext.Msg.alert('提示','删除支出信息成功。');
					}else{
						Ext.Msg.alert('提示','删除支出信息失败！');
					}
				},
				failure : function(response,options){
					msgTip.hide();
					Ext.Msg.alert('提示','删除支出信息请求失败！');
				}
			});
		}
		//加载表单数据
		function loadForm(payId){
			payForm.form.load({
				waitMsg : '正在加载数据请稍后',//提示信息
				waitTitle : '提示',//标题
				url : 'payext.do?method=getPayById',//请求的url地址
				params : {payId:payId},
				method:'GET',//请求方式
				success:function(form,action){//加载成功的处理函数
					Ext.Msg.alert('提示','数据加载成功');
				},
				failure:function(form,action){//加载失败的处理函数
					Ext.Msg.alert('提示','数据加载失败');
				}
			});
		}
		//提交表单数据
		function submitForm(){
			//判断当前执行的提交操作，isAdd为true表示执行支出新增操作，false表示执行支出修改操作
			if(payForm.isAdd){
				//新增支出信息
				payForm.form.submit({
					clientValidation:true,//进行客户端验证
					waitMsg : '正在提交数据请稍后',//提示信息
					waitTitle : '提示',//标题
					url : 'payext.do?method=addPay',//请求的url地址
					method:'POST',//请求方式
					success:function(form,action){//加载成功的处理函数
						win.hide();
						updatePayList(action.result.payId);
						Ext.Msg.alert('提示','新增支出成功');
					},
					failure:function(form,action){//加载失败的处理函数
						Ext.Msg.alert('提示','新增支出失败');
					}
				});
			}else{
				//修改支出信息
				payForm.form.submit({
					clientValidation:true,//进行客户端验证
					waitMsg : '正在提交数据请稍后',//提示信息
					waitTitle : '提示',//标题
					url : 'payext.do?method=modifyPay',//请求的url地址
					method:'POST',//请求方式
					success:function(form,action){//加载成功的处理函数
						win.hide();
						updatePayList(action.result.payId);
						Ext.Msg.alert('提示','修改支出成功');
					},
					failure:function(form,action){//加载失败的处理函数
						Ext.Msg.alert('提示','修改支出失败');
					}
				});
			}
		}
		//明细数据修改后，同步更新支出列表信息
		function updatePayList(payId){
			var fields = getFormFieldsObj(payId);
			var index = payStore.find('id',fields.id);
			if(index != -1){
				var item = payStore.getAt(index);
				for(var fieldName in fields){
					item.set(fieldName,fields[fieldName]);
				}
				payStore.commitChanges();
			}else{
				var rec = new Ext.data.Record(fields);
				payStore.add(rec);
			}
		}
		//取得表单数据
		function getFormFieldsObj(payId){
			var fields = payForm.items;
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
				
				obj[item.name] = value;
			}
			if(Ext.isEmpty(obj['id'])){
				obj['id'] = payId;
			}
			return obj;
		}
		//取得所选支出
		function getPayIdList(){
			var recs = payGrid.getSelectionModel().getSelections();
			var list = [];
			if(recs.length == 0){
				Ext.MessageBox.alert('提示','请选择要进行操作的支出！');
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
	});
	//弹出支出信息
		var sumPay = document.getElementById("sumPay").value;
		var sumPayY = document.getElementById("sumPayY").value;
		var winP = new Ext.Window({
			layout:'fit',
		    width:380,
		    closeAction:'hide',
		    height:300,
			resizable : false,
			shadow : true,
			modal :true,
		    closable:true,
		    bodyStyle:'padding:5 5 5 5',
		    animCollapse:true,
			items:{  title:"总支出",
					 collapsible:true,
					 	html : '<br><center><font size = 6>本月总支出为：</font><font size = 5 color= red>'+sumPay+'元</font></center><br><center><font size = 6>本年总支出为：</font><font size = 5 color= red>'+sumPayY+'元</font></center>'
				  }
			       

		});
		//查看总支出
		function showTotalPay(){
			winP.setTitle("您的总支出信息");
			winP.show();
		}
		
</script>
<body>
<div id='grid-div' style="width:100%; height:500"/>
<input type="hidden" name="sumPay" id="sumPay" value="<%=sumPay %>"/>
<input type="hidden" name="sumPayY" id="sumPayY" value="<%=sumPayY%>"/>
</body>
</html>