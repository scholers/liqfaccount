<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.scholers.account.bean.*" %>
<%@ page import="com.scholers.account.dao.PMF" %>
<%@ page import="com.scholers.account.util.ComUtil" %>
<%@ page import="com.scholers.account.business.*" %>
<%@ page import="javax.jdo.Query" %>
<%@page import="java.math.BigDecimal"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>清风记账本</title>
<link rel="stylesheet" type="text/css" href="pagesExt/extjs3.3/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="pagesExt/css/style.css" />
<script type="text/javascript" src="pagesExt/extjs3.3/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="pagesExt/extjs3.3/ext-all.js"></script>
<script type="text/javascript" src="pagesExt/extjs3.3/source/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="pagesExt/extjs3.3/ext/themeChange.js"></script>
</head>

<%
	String userName = "";
	java.util.Date date = new java.util.Date();
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    //每页条数
    int count = 20;
    if (user != null) {
    	userName = user.getEmail();
    	//插入用户表
    	 com.scholers.account.util.ComUtil.addUser();
    }
%>
<script type="text/javascript">
	Ext.onReady(function(){
	   
		Ext.BLANK_IMAGE_URL = 'pagesExt/extjs3.3/resources/images/default/s.gif';
		//获取session用户的
		var uname = document.getElementById("uname").value;
		//判断添加用户的js
		
		var root = new Ext.tree.TreeNode({ 
			text : '系统说明',
			url : 'pagesExt/about.jsp',
			expanded : true//默认展开根节点
		});
		if(uname!=""){
		var root1 = new Ext.tree.TreeNode({
			text : '个人/家庭收入管理',
			url : 'pagesExt/about.jsp',
			expanded : true//默认展开根节点
		});
		
		var node1 = new Ext.tree.TreeNode({
			text : '收入类型管理',
			url : 'bookext.do?method=showBookTypeList'
		});
		var node2 = new Ext.tree.TreeNode({
			text : '收入管理',
			url : 'bookext.do?method=showBookList'       
		});
		
		
		root1.appendChild(node1);
		root1.appendChild(node2);
		
		var root2 = new Ext.tree.TreeNode({
			text : '个人/家庭支出管理',
			url : 'pagesExt/about.jsp',
			expanded : true//默认展开根节点
		});
		
		
		var node3 = new Ext.tree.TreeNode({
			text : '支出类型管理',
			url : 'payext.do?method=showPayTypeList'
		});
		var node4 = new Ext.tree.TreeNode({
			text : '支出管理',
			url : 'payext.do?method=showPayList'
		});
		root2.appendChild(node3);
		root2.appendChild(node4);
		
		var root5 = new Ext.tree.TreeNode({
			text : '数据分析管理',
			url : 'pagesExt/about.jsp'
		}); 
		
		var node7 = new Ext.tree.TreeNode({  
			text : '月收入数据分析',
			url : 'analysis.do?method=getInByM'
		});
		var node8 = new Ext.tree.TreeNode({
			text : '月支出数据分析',  
			url : 'analysis.do?method=getPayByM'
		});
		var node9 = new Ext.tree.TreeNode({
			text : '年收入数据分析',
			url : 'analysis.do?method=getBookByY'
		});
		var node10 = new Ext.tree.TreeNode({
			text : '年支出数据分析',
			url : 'analysis.do?method=getBookByYP'
		});
		root5.appendChild(node7);  
		root5.appendChild(node8);
		root5.appendChild(node9);
		root5.appendChild(node10);
		
		var node4 = new Ext.tree.TreeNode({
			text : '支出管理',
			url : 'account?operType=qry'
		});
		
		//判断是否为管理员
		if(uname=="scholers@gmail.com"){
			var root3 = new Ext.tree.TreeNode({
				text : '用户列表',
				url : 'pagesExt/about.jsp', 
				expanded : true//默认展开根节点
			})
			var node5 = new Ext.tree.TreeNode({
				text : '用户列表',
				url:'usersext.do?method=showUsersList'
			});
			root3.appendChild(node5);
			root.appendChild(root3);
		} 
		root.appendChild(root1);
		root.appendChild(root2);
		//root.appendChild(root5);
		}
		var menu = new Ext.tree.TreePanel({
			border : false,
			height:523,
			root : root,
			hrefTarget : 'mainContent',
			listeners : {
				click : function(node,e){
					mainPanel.load({  
						url:node.attributes.url,
						callback : function(){
							mainPanel.setTitle(node.text);
						},
						scripts: true
					});
				}
			}
			,
			tbar : [
				'皮肤选择：',
				{
					xtype : 'themeChange',
					width : 80,
					listWidth : 80
				},
				'->'
			],
			bbar : [
				{
					width : 80,
					listWidth : 80
				}
			]
		});
		if(uname==""){
		new Ext.Viewport({
			title : 'Ext.Viewport示例',
			layout:'border',//表格布局
			items: [{
				title : '清风记账本',
				collapsible: true,
				html : '<br><center><font size = 6>清风记账本</font></center>',
				region: 'north',//指定子面板所在区域为north
				height: 120,
				bbar :[
				'欢迎您：',
						{
						 id:'BA',text:uname+" 您好",
	                	 handler: function(){ Ext.Msg.alert("欢迎",uname+"：欢迎您使用清风记账本"); } 
						},
						'->',
						 {  
						 id:'CB',text:"请用给你的gmail用户登录!",iconCls:'login',
		                 handler: function(){  
		                 	 window.location.href="<%= userService.createLoginURL(request.getRequestURI()) %>";
		                 	 } 
						 }
					]
			},{
				title : '功能菜单',
				items : menu,
				split:true,
				collapsible: true,
				region:'west',//指定子面板所在区域为west
				width: 155
			},{
				title: '系统说明',
				contentEl : 'aboutDiv',
				collapsible: true,
				id : 'mainContent',
				region:'center'//指定子面板所在区域为center
			}]
		});
		}else{
		new Ext.Viewport({
			title : 'Ext.Viewport示例',
			layout:'border',//表格布局
			items: [{
				title : '清风记账本',
				collapsible: true,
				html : '<br><center><font size = 6>清风记账本</font></center>',
				region: 'north',//指定子面板所在区域为north
				height: 120,
				bbar :[
				'欢迎您：',
						{
						 id:'BA',text:uname+" 您好",
	                	 handler: function(){ Ext.Msg.alert("欢迎",uname+"：欢迎您使用清风记账本"); } 
						},
						'->',
						{  
						 id:'BB',text:"退出系统",iconCls:'logout',
		                 handler: function(){
							
		                 	 window.location.href="<%= userService.createLogoutURL(request.getRequestURI()) %>";
		                 	 } 
						 }
					]
			},{
				title : '功能菜单',
				items : menu,
				split:true,
				collapsible: true,
				region:'west',//指定子面板所在区域为west
				width: 155
			},{
				title: '系统说明',
				contentEl : 'aboutDiv',
				collapsible: true,
				id : 'mainContent',
				region:'center'//指定子面板所在区域为center
			}]
		});
		
		}
		var mainPanel = Ext.getCmp('mainContent');
	});
</script>
<%//} %>
<body>

<div id='aboutDiv' style='height:96%;width:100%'>
<td><table cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" style="width:290px;border: 1px solid #E6E6E6;"><tr><td rowspan="2" align="center"><div style="margin:5px auto; width: 80px;height:80px;"><a target="_blank" href="http://s.click.taobao.com/t_1?i=oBClBnddh2gw4Q%3D%3D&p=mm_24002873_0_0&n=12" style="width: 80px; margin:0px;padding:0px;height: 80px; overflow:hidden;"><img style="margin:0px;border:none;" src="http://image.taobao.com/bao/uploaded/http://img04.taobaocdn.com/bao/uploaded/i4/T1celLXkJKXXaC_XZ1_040126.jpg_sum.jpg"></a></div><div class="clearing"></div></td><td colspan="2" ><a target="_blank" href="http://s.click.taobao.com/t_1?i=oBClBnddh2gw4Q%3D%3D&p=mm_24002873_0_0&n=12" style="height:40px;width:180px;margin:5px;line-height:20px;color:#0000FF">SHOPZOOM black label品牌 秋冬新款 男士水洗褶皱牛仔裤[K2037]</a></td></tr><tr><td nowrap="nowrap" > <span style="font-weight:600;margin:5px;line-height:30px;color:#CC0000;">168.0元</span>&nbsp;</td><td nowrap="nowrap" width="100px" ><a target="_blank"href="http://s.click.taobao.com/t_1?i=oBClBnddh2gw4Q%3D%3D&p=mm_24002873_0_0&n=12"><img name="" style="margin:0px; pandding:0px;line-height:24px;vertical-align: text-bottom;border:none;" src="http://img.alimama.cn/images/tbk/cps/fgetccode_btn.gif"></a></td></tr></table>
    	</td>
    	<td><table cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" style="width:290px;border: 1px solid #E6E6E6;"><tr><td rowspan="2" align="center"><div style="margin:5px auto; width: 80px;height:80px;"><a target="_blank" href="http://s.click.taobao.com/t_1?i=oBClBnbYKi2OOA%3D%3D&p=mm_24002873_0_0&n=12" style="width: 80px; margin:0px;padding:0px;height: 80px; overflow:hidden;"><img style="margin:0px;border:none;" src="http://image.taobao.com/bao/uploaded/http://img01.taobaocdn.com/bao/uploaded/i1/T1A9lNXfdXXXXneM39_103518.jpg_sum.jpg"></a></div><div class="clearing"></div></td><td colspan="2" ><a target="_blank" href="http://s.click.taobao.com/t_1?i=oBClBnbYKi2OOA%3D%3D&p=mm_24002873_0_0&n=12" style="height:40px;width:180px;margin:5px;line-height:20px;color:#0000FF">SHOPZOOM2010秋冬新款 男装 活性水洗 纯棉 直筒牛仔裤子[K2050]</a></td></tr><tr><td nowrap="nowrap" > <span style="font-weight:600;margin:5px;line-height:30px;color:#CC0000;">198.0元</span>&nbsp;</td><td nowrap="nowrap" width="100px" ><a target="_blank"href="http://s.click.taobao.com/t_1?i=oBClBnbYKi2OOA%3D%3D&p=mm_24002873_0_0&n=12"><img name="" style="margin:0px; pandding:0px;line-height:24px;vertical-align: text-bottom;border:none;" src="http://img.alimama.cn/images/tbk/cps/fgetccode_btn.gif"></a></td></tr></table></td>
		<div style="width: 735px; height: 500px; overflow: auto;font-size:13px;" align=left>
		<p>这是一个个人/家庭财务B/S OA系统:</p>
		<p>　　此系统适合于个人和家庭的收入和支出的具体管理，能合理的利用系统来完成查找和了解个人和家庭的收支情况。从而使您的生活更加合理的花费和管理自己的财务！<input type="hidden"  name="uname" id="uname" value="<%=userName %>"/> </p>
		<p>功能介绍：</p>
		<p>此系统目前有:１,个人/家庭收入管理；２,个人/家庭支出管理,</p>
		<p><font color="red">　个人/家庭收入管理功能介绍:</font></p>
		<p>　　１）：收入类型管理：您可以根据自己的需要进行添加和修改删除收入类型，如：工资收入,奖金收入,其他收入...</p>
		<p>　　２）：收入管理：您可以根据自己的需要进行添加修改和删除收入,在收入中添加的时候可以根据时间情况选择您的收入类型。</p>
		<p>　　　１：您还可以进行查询本月的总收入情况</p>
		<p><font color="red">　个人/家庭支出管理功能介绍:</font></p>
		<p>　　１）：支出类型管理：您可以根据自己的需要进行添加和修改删除支出类型，如：吃饭,日常用品,小吃...</p>
		<p>　　２）：支出管理：您可以根据自己的需要进行添加修改和删除支出,在支出中添加的时候可以根据时间情况选择您的收入类型。</p>
		<p>　　　１：您还可以进行查询本月的总支出情况</p>
		<p><input type="hidden" name="uname" id="uname" value="<%=userName %>"/> </p>  
		
		</div>
  <script type="text/javascript" src="http://js.tongji.linezing.com/2145929/tongji.js"></script><noscript><a href="http://www.linezing.com"><img src="http://img.tongji.linezing.com/2145929/tongji.gif"/></a></noscript>
		
</div>

</body>
</html>