<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>支出类型列表</title>
</head>
<script type="text/javascript">
	Ext.onReady(function(){
		//定义数据集对象
		var typeStore = new Ext.data.Store({//配置分组数据集
			//autoLoad :true,
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
		})
		//创建工具栏组件
		var toolbar = new Ext.Toolbar([
			{text : '新增支出类型',iconCls:'add',handler : showAddPayType},
			'-',
			{text : '修改支出类型',iconCls:'option',handler : showModifyPayType},
			'-',
			{text : '删除支出类型',iconCls:'remove',handler : showDeletePayType}
			  
		]);
		//创建Grid表格组件
		var cb = new Ext.grid.CheckboxSelectionModel()
		var payTypeGrid = new Ext.grid.GridPanel({
			applyTo : 'grid-div',
			height:600,
			tbar : toolbar,
			frame:true,
			store: typeStore,
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
				{header: "类型编号", width: 80, dataIndex: 'id', sortable: true,hidden:true},
				{header: "类型名称", width: 180, dataIndex: 'title', sortable: true},
				{header: "类型说明", width: 280, dataIndex: 'detail', sortable: true}
			],
			 bbar: new Ext.PagingToolbar({
		        pageSize: 10,  
		        store: typeStore,
		        displayInfo: true,
		        displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
		        emptyMsg: "没有记录"
	    	})
		});
		typeStore.load({params:{start:0, limit:10}});
		//创建新增或修改支出类型信息的form表单
		Ext.QuickTips.init();
		Ext.form.Field.prototype.msgTarget = 'side';//统一指定错误信息提示方式
		var payTypeForm = new Ext.FormPanel({
			labelSeparator : "：",
			frame:true,
			border:false,
			items : [
				{
					xtype:'textfield',
					width : 200,
					allowBlank : false,
					blankText : '类型名称不能为空',
					name : 'title',
					fieldLabel:'类型名称'
				},{
					xtype:'textarea',
					width : 200,
					name : 'detail',
					fieldLabel:'类型说明'
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
		    height:200,
			resizable : false,
			shadow : true,
			modal :true,
		    closable:true,
		    bodyStyle:'padding:5 5 5 5',
		    animCollapse:true,
			items:[payTypeForm]
		});
		//显示新建支出类型窗口
		function showAddPayType(){
			payTypeForm.form.reset();
			payTypeForm.isAdd = true;
			win.setTitle("新增支出类型信息");
			win.show();
		}
		//显示修改支出类型窗口
		function showModifyPayType(){
			var payTypeList = getPayTypeIdList();
			var num = payTypeList.length;
			if(num > 1){
				Ext.MessageBox.alert("提示","每次只能修改一条支出信息。")
			}else if(num == 1){
				payTypeForm.isAdd = false;
				win.setTitle("修改支出信息");
				win.show();
				var payTypeId = payTypeList[0];
				loadForm(payTypeId);
			}
		}
		//显示删除支出对话框
		function showDeletePayType(){
			var payTypeList = getPayTypeIdList();
			var num = payTypeList.length;
			if(num > 1){
				Ext.MessageBox.alert("提示","每次只能删除一条支出信息。")
			}else if(num == 1){
				Ext.MessageBox.confirm("提示","您确定要删除所选支出类型吗？",function(btnId){
					if(btnId == 'yes'){
						var payTypeId = payTypeList[0];
						deletePayType(payTypeId);
					}
				})
			}
		}
		
		//删除支出类型
		function deletePayType(payTypeId){
			var msgTip = Ext.MessageBox.show({
				title:'提示',
				width : 250,
				msg:'正在删除支出类型信息请稍后......'
			});
			Ext.Ajax.request({
				url : 'payext.do?method=deletePayType',
				params : {payTypeId : payTypeId},
				method : 'POST',
				success : function(response,options){
					msgTip.hide();
					var result = Ext.util.JSON.decode(response.responseText);
					if(result.success){
						//服务器端数据成功删除后，同步删除客户端列表中的数据
						var index = typeStore.find('id',payTypeId);
						if(index != -1){
							var rec = typeStore.getAt(index)
							typeStore.remove(rec);
						}
						Ext.Msg.alert('提示','删除支出类型信息成功。');
					}else{
						Ext.Msg.alert('提示','该支出类型已包含'+result.num+'本支出信息不能删除！');
					}
				},
				failure : function(response,options){
					msgTip.hide();
					Ext.Msg.alert('提示','删除支出类型请求失败！');
				}
			});
		}
		
		//加载表单数据
		function loadForm(payTypeId){
			payTypeForm.form.load({
				waitMsg : '正在加载数据请稍后',//提示信息
				waitTitle : '提示',//标题
				url : 'payext.do?method=getPayTypeById',//请求的url地址
				params : {payTypeId:payTypeId},
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
			//判断当前执行的提交操作，isAdd为true表示执行支出类型新增操作，false表示执行支出类型修改操作
			if(payTypeForm.isAdd){
				//新增支出信息
				payTypeForm.form.submit({
					clientValidation:true,//进行客户端验证
					waitMsg : '正在提交数据请稍后',//提示信息
					waitTitle : '提示',//标题
					url : 'payext.do?method=addPayType',//请求的url地址
					method:'POST',//请求方式
					success:function(form,action){//加载成功的处理函数
						win.hide();
						updatePayList(action.result.payTypeId);
						Ext.Msg.alert('提示','新增支出类型成功');
					},
					failure:function(form,action){//加载失败的处理函数
						Ext.Msg.alert('提示','新增支出类型失败');
					}
				});
			}else{
				//修改支出信息
				payTypeForm.form.submit({
					clientValidation:true,//进行客户端验证
					waitMsg : '正在提交数据请稍后',//提示信息
					waitTitle : '提示',//标题
					url : 'payext.do?method=modifyPayType',//请求的url地址
					method:'POST',//请求方式
					success:function(form,action){//加载成功的处理函数
						win.hide();
						updatePayList(action.result.payTypeId);
						Ext.Msg.alert('提示','修改支出类型成功');
					},
					failure:function(form,action){//加载失败的处理函数
						Ext.Msg.alert('提示','修改支出类型失败');
					}
				});
			}
		}
		//明细数据修改后，同步更新支出列表信息
		function updatePayList(payTypeId){
			var fields = getFormFieldsObj(payTypeId);
			var index = typeStore.find('id',fields.id);
			if(index != -1){
				var item = typeStore.getAt(index);
				for(var fieldName in fields){
					item.set(fieldName,fields[fieldName]);
				}
				typeStore.commitChanges();
			}else{
				var rec = new Ext.data.Record(fields);
				typeStore.add(rec);
			}
		}
		//取得表单数据
		function getFormFieldsObj(payTypeId){
			var fields = payTypeForm.items;
			var obj = {};
			for(var i = 0 ; i < fields.length ; i++){
				var item = 	fields.itemAt(i);
				var value = item.getValue();
				obj[item.name] = value;
			}
			if(Ext.isEmpty(obj['id'])){
				obj['id'] = payTypeId;
			}
			return obj;
		}
		//取得所选支出
		function getPayTypeIdList(){
			var recs = payTypeGrid.getSelectionModel().getSelections();
			var list = [];
			if(recs.length == 0){
				Ext.MessageBox.alert('提示','请选择要进行操作的支出！');
			}else{
				for(var i = 0 ; i < recs.length ; i++){
					var rec = recs[i];
					list.push(rec.get('id'))
				}
			}
			return list;
		}
	});
</script>
<body>
<div id='grid-div' style="width:100%; height:100%;"/>
</body>
</html>