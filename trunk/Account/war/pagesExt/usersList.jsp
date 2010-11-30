<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
</head>
<script type="text/javascript">
	Ext.onReady(function(){
		//定义数据集对象
		var typeStore = new Ext.data.Store({//配置分组数据集
			//autoLoad :true,
			reader: new Ext.data.JsonReader({
				totalProperty: "results",
				root: "items",
				id: "id"
			},   
			Ext.data.Record.create([
				{name: 'id'},
				{name: 'email'},      
				{name:'loginNum'},
				{name: 'createDate'},
				{name: 'lastLoginTime'}   
			])
			),
			proxy : new Ext.data.HttpProxy({
				url : 'usersext.do?method=getUsersList'
			})
		})
		//创建工具栏组件
		var toolbar = new Ext.Toolbar([
			//{text : '新增用户',iconCls:'add',handler : showAddUsersType},
			//'-',
			//{text : '修改用户',iconCls:'option',handler : showModifyUsersType},
			//'-',
			{text : '删除用户',iconCls:'remove',handler : showDeleteUsersType}
			
		]);
		//创建Grid表格组件
		var cb = new Ext.grid.CheckboxSelectionModel()
		var usersGrid = new Ext.grid.GridPanel({
		    height:600,
			applyTo : 'grid-div',
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
				{header: "编号", width: 80, dataIndex: 'id', sortable: true},
				{header: "邮箱", width: 80, dataIndex: 'email', sortable: true},
				{header: "登录次数", width: 80, dataIndex: 'loginNum', sortable: true},
				{header: "创建时间", width: 180, dataIndex: 'createDate', sortable: true},
				{header: "最后一次登录时间", width: 180, dataIndex: 'lastLoginTime', sortable: true}
			],
			 bbar: new Ext.PagingToolbar({
		        pageSize: 15,  
		        store: typeStore,
		        displayInfo: true,
		        displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
		        emptyMsg: "没有记录"
	    	})
	    });
			typeStore.load({params:{start:0, limit:15}});
			
		function onItemCheck(){
		        var dd=Ext.get('times').getValue(); 
		        var endtime=Ext.get('times2').getValue();     
		        payStore.reload({params:{start:0,limit:15,dd:dd,endtime:endtime}}); 
		};
		//创建新增或修改用户类型信息的form表单
		Ext.QuickTips.init();
		Ext.form.Field.prototype.msgTarget = 'side';//统一指定错误信息提示方式
		var usersForm = new Ext.FormPanel({
			labelSeparator : "：",
			frame:true,
			border:false,
			items : [
				{
					xtype:'textfield',
					width : 200,
					allowBlank : false,
					blankText : '类型名称不能为空',
					name : 'username',
					fieldLabel:'姓名'
				},{
					xtype:'textfield',
					width : 200,
					name : 'password',
					fieldLabel:'密码'
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
			items:[usersForm]
		});
		//显示新建用户类型窗口
		function showAddUsersType(){
			usersForm.form.reset();
			usersForm.isAdd = true;
			win.setTitle("新增用户信息");
			win.show();
		}
		//显示修改用户类型窗口
		function showModifyUsersType(){
			var usersList = getUsersIdList();
			var num = usersList.length;
			if(num > 1){
				Ext.MessageBox.alert("提示","每次只能修改一条用户信息。")
			}else if(num == 1){
				usersForm.isAdd = false;
				win.setTitle("修改用户信息");
				win.show();
				var usersId = usersList[0];
				loadForm(usersId);
			}
		}
		//显示删除用户对话框
		function showDeleteUsersType(){
			var usersList = getUsersIdList();
			var num = usersList.length;
			if(num > 1){
				Ext.MessageBox.alert("提示","每次只能删除一条用户信息。")
			}else if(num == 1){
				Ext.MessageBox.confirm("提示","您确定要删除所选用户吗？",function(btnId){
					if(btnId == 'yes'){
						var usersId = usersList[0];
						deleteUsers(usersId);
					}
				})
			}
		}
		//删除用户类型
		function deleteUsers(usersId){
			//var usersIds = usersId.join('-');
			var msgTip = Ext.MessageBox.show({
				title:'提示',
				width : 250,
				msg:'正在删除用户信息请稍后......'
			});
			Ext.Ajax.request({
				url : 'usersext.do?method=deleteUsers',
				params : {userIds : usersId},
				method : 'POST',
				success : function(response,options){
					msgTip.hide();
					var result = Ext.util.JSON.decode(response.responseText);
					if(result.success){
						//服务器端数据成功删除后，同步删除客户端列表中的数据
						var index = typeStore.find('id',usersId);
						if(index != -1){
							var rec = typeStore.getAt(index)
							typeStore.remove(rec);
						}
						Ext.Msg.alert('提示','删除用户信息成功。');
					}else{
						Ext.Msg.alert('提示','该用户已包含'+result.num+'本用户信息不能删除！');
					}
				},
				failure : function(response,options){
					msgTip.hide();
					Ext.Msg.alert('提示','删除用户类型请求失败！');
				}
			});
		}
		//加载表单数据
		function loadForm(usersId){
			usersForm.form.load({
				waitMsg : '正在加载数据请稍后',//提示信息
				waitTitle : '提示',//标题
				url : 'usersext.do?method=getUsersById',//请求的url地址
				params : {usersId:usersId},
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
			//判断当前执行的提交操作，isAdd为true表示执行用户类型新增操作，false表示执行用户类型修改操作
			if(usersForm.isAdd){
				//新增用户信息
				usersForm.form.submit({
					clientValidation:true,//进行客户端验证
					waitMsg : '正在提交数据请稍后',//提示信息
					waitTitle : '提示',//标题
					url : 'usersext.do?method=addUsers',//请求的url地址
					method:'POST',//请求方式
					success:function(form,action){//加载成功的处理函数
						win.hide();
						updateUsersList(action.result.usersId);
						Ext.Msg.alert('提示','新增用户成功');
					},
					failure:function(form,action){//加载失败的处理函数
						Ext.Msg.alert('提示','新增用户失败');
					}
				});
			}else{
				//修改用户信息
				usersForm.form.submit({
					clientValidation:true,//进行客户端验证
					waitMsg : '正在提交数据请稍后',//提示信息
					waitTitle : '提示',//标题
					url : 'usersext.do?method=modifyUsers',//请求的url地址
					method:'POST',//请求方式
					success:function(form,action){//加载成功的处理函数
						win.hide();
						updateUsersList(action.result.UsersId);
						Ext.Msg.alert('提示','修改用户成功');
					},
					failure:function(form,action){//加载失败的处理函数
						Ext.Msg.alert('提示','修改用户失败');
					}
				});
			}
		}
		//明细数据修改后，同步更新用户列表信息
		function updateUsersList(usersId){
			var fields = getFormFieldsObj(usersId);
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
		function getFormFieldsObj(usersId){
			var fields = usersForm.items;
			var obj = {};
			for(var i = 0 ; i < fields.length ; i++){
				var item = 	fields.itemAt(i);
				var value = item.getValue();
				obj[item.name] = value;
			}
			if(Ext.isEmpty(obj['id'])){
				obj['id'] = usersId;
			}
			return obj;
		}
		//取得所选用户
		function getUsersIdList(){
			var recs = usersGrid.getSelectionModel().getSelections();
			var list = [];
			if(recs.length == 0){
				Ext.MessageBox.alert('提示','请选择要进行操作的用户！');
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