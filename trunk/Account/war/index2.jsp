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
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-10724778-2']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
</head>

<%
	String userName = "";
	java.util.Date date = new java.util.Date();
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    //每页条数
    int count = 20;
    String isAdmin = "false";
    if (user != null) {
    	userName = user.getEmail();
    	//插入用户表
    	 com.scholers.account.util.ComUtil.addUser();
    	//管理员
    	 if("scholers@gmail.com".equals(user.getEmail())) {
    			isAdmin = "true";
    	}
    }
    String conPath = request.getContextPath() + "/";
    
	
%>
<script type="text/javascript"><!--
	var isAdmin = '<%=isAdmin%>';
	var path = '<%=conPath%>';
	Ext.onReady(function(){

	 	
		var mainTab = new Ext.TabPanel({
			   id : 'mainTabPanel',
			  renderTo:'tabs',
			     region : "center",
			     enableTabScroll: true,
			     animate : true,
			     frame: true,
			     defaults : {
			         autoScroll : true,
			         height : 490
			       },
			     items : [{
			        xtype : "panel",
			        title : "欢迎",
			        html : "<iframe src=\"" + "/pagesExt/about.jsp" + "\" style='height:100%;width:100%;' frameborder=0></iframe>"
			       }]
			    }) //这里我定义一个欢迎的主页面，并且不能被关闭
						 
			/**
			 * 向tab中添加选项卡

			 * @params myId 被添加的组件id  myTitle 创建tabpanel时需要 myurl 将要加载数据的地址

			 */
			function addTab(id, name, link) {
			 	var tabId = "tab-" + id; 
			    var tabTitle = name;
			    var tabLink = link;
			    var centerpanel = Ext.getCmp('mainTabPanel'); 
			    var tab = centerpanel.getComponent(tabId);
			    if (!tab) {
	                tab = centerpanel.add({ 
	                    id       : tabId, 
	                    title    : tabTitle, 
	                    closable : true,                    
	                    autoLoad : {
	                        showMask : true,
	                        url      : tabLink,
	                        mode     : "iframe",
	                        maskMsg  : "Loading " + tabLink + "..."
	                    }                    
	                }).doLayout();

	            }
	            alert(tabLink);
			    centerpanel.setActiveTab(tab);
			    
			}

		
	     
		Ext.BLANK_IMAGE_URL = 'pagesExt/extjs3.3/resources/images/default/s.gif';
		//获取session用户的
		var uname = document.getElementById("uname").value;
		//判断添加用户的js
		
		var root = new Ext.tree.TreeNode({ 
			id :"work1",
			text : '系统说明',
			url : 'pagesExt/about.jsp',
			expanded : true//默认展开根节点
		});
		if(uname!=""){
		var root1 = new Ext.tree.TreeNode({
			id :"work1",
			text : '个人/家庭收入管理',
			url : 'pagesExt/about.jsp',
			expanded : true//默认展开根节点
		});
		
		var node1 = new Ext.tree.TreeNode({
			id :"work3",
			text : '收入类型管理',
			url :'inext.do?method=showBookTypeList'
		});
		var node2 = new Ext.tree.TreeNode({
			id :"work4",
			text : '收入管理',
			url : 'inext.do?method=showBookList'
		});
		
		
		root1.appendChild(node1);
		root1.appendChild(node2);
		
		var root2 = new Ext.tree.TreeNode({
			id :"work5",
			text : '个人/家庭支出管理',
			url : 'pagesExt/about.jsp',
			expanded : true//默认展开根节点
		});
		
		
		var node3 = new Ext.tree.TreeNode({
			id :"work6",
			text : '支出类型管理',
			url : 'payext.do?method=showPayTypeList'
		});
		var node4 = new Ext.tree.TreeNode({
			id :"work7",
			text : '支出管理',
			url : path  + 'payext.do?method=showPayList'
		});
		root2.appendChild(node3);
		root2.appendChild(node4);
		
		var root5 = new Ext.tree.TreeNode({
			id :"work1",
			text : '数据分析管理',
			url : 'pagesExt/about.jsp',
			expanded : true//默认展开根节点
		}); 
		
		var node7 = new Ext.tree.TreeNode({  
			id :"work9",
			text : '财务数据统计',
			url : 'analysis.do?method=showCountList'
			
		});
		var node8 = new Ext.tree.TreeNode({
			id :"work10",
			text : '图形分析',  
			url : 'analysis.do?method=showCountListChart'
		});
		var node9 = new Ext.tree.TreeNode({
			id :"work11",
			text : '年收入数据分析',
			url : 'analysis.do?method=getBookByY'
		});
		var node10 = new Ext.tree.TreeNode({
			id :"work12",
			text : '年支出数据分析',
			url : 'analysis.do?method=getBookByYP'
		});
		root5.appendChild(node7);  
		root5.appendChild(node8);
		//root5.appendChild(node9);
		//root5.appendChild(node10);
		
		var node4 = new Ext.tree.TreeNode({
			id :"work13",
			text : '支出管理',
			url : 'account?operType=qry'
		});
		
		//判断是否为管理员
		if(isAdmin == 'true'){
			var root3 = new Ext.tree.TreeNode({
				id :"work1",
				text : '用户列表',
				url : 'pagesExt/about.jsp', 
				expanded : true//默认展开根节点
			})
			var node5 = new Ext.tree.TreeNode({
				id :"work15",
				text : '用户列表',
				url:'usersext.do?method=showUsersList'
			});
			root3.appendChild(node5);
			root.appendChild(root3);
		} 
		root.appendChild(root1);
		root.appendChild(root2);
		root.appendChild(root5);
		}
		var menu = new Ext.tree.TreePanel({
			  id : 'tree',
			   animate : true,
			   autoScroll : true,
			   containerScroll : true,
			   lines : true,
			   //el : 'tree'
				border : false,
				height:523,
				root : root,
				//hrefTarget : 'mainContent',
				listeners:{
						click:function(_node){								
								var _url = _node.attributes.url;
								var _id = _node.id;//如果没有宝，则自动生成唯一的ID
								var _p = mainTab.getItem(id);//获取节点ID对应的标签面板
								if(_url){//具有URL值
									if(_p){
										//此面板已经存在，只需要激活就可以了。
										mainTab.setActiveTabl(_p);
									} else {
										//如果不存在，则创建新的面板，并激活
										_p = new Ext.Panel({
												title:_node.text,
												//autoLoad:{url:_url,scripts:true},
												html : "<iframe src=\"" + _url + "\" style='height:100%;width:100%;' frameborder=0></iframe>",
												closable:true,//标签上出现关闭按钮
												autoScroll:true,
												id:_id//这里一定设置
											});
										mainTab.add(_p);
										mainTab.setActiveTab(_p);
									}
								}
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
				title : '清风记账本',
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
							 id:'CB',text:"<center><font color=red size=2>点击这里用gmail用户登录</font></center>",iconCls:'login',
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
			   		items : mainTab, //将欢迎界面放入到ViewPort{
			       
					title: '系统说明',
					contentEl : 'tabs',
					collapsible: true,
					id : 'mainContent',
					region:'center'//指定子面板所在区域为center
				},{
					title: '右侧广告',
					contentEl : 'aboutDiv2',
					collapsible: true,
					width:150,
					region:'east'//指定子面板所在区域为center
				},{
					title: '底层广告',
					contentEl : 'aboutDiv3',
					collapsible: true,
					width:150,
					region:'south'//指定子面板所在区域为east
				}]
			});
		}else{
			new Ext.Viewport({
				title : '清风记账本',
				layout:'border',//表格布局
				items: [{
					title : '清风记账本',
					collapsible: true,
					html : '<br><center><font size = 6>清风记账本</font></center>',
					region: 'north',//指定子面板所在区域为north
					width:130,
					bbar :[
					'欢迎您：',
							{
							 id:'BA',text:uname+" 您好",
		                	 handler: function(){ Ext.Msg.alert("欢迎",uname+"：欢迎您使用清风记账本"); } 
							},
							'->',
							{  
							 id:'BB',text:"<center><font color=red size=2>退出系统</font></center>",iconCls:'logout',
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
					items : mainTab, 
					title: '清风记账本',
					contentEl : 'tabs',
					collapsible: true,
					//id : 'mainContent',
					region:'center'//指定子面板所在区域为center
				},{
					title: '右侧广告',
					contentEl : 'aboutDiv2',
					collapsible: true,
					width:150,
					region:'east'//指定子面板所在区域为east
				},{
					title: '底层广告',
					contentEl : 'aboutDiv3',
					collapsible: true,
					width:150,
					region:'south'//指定子面板所在区域为east
				
				}]
			});
		
		}
		//var mainPanel = Ext.getCmp('mainContent');
	});
--></script>
<body>
<div id="tabs22" style='height:96%;width:70%'></div>
<div id='tabs' style='height:96%;width:70%'>
<div style="width: 835px; height: 500px; overflow: auto;font-size:13px;" align=left>
		<img alt="" src="/pagesExt/img1.jpg">
		<p>这是一个个人/家庭财务在线记账系统:</p>
		<p>　　此系统适合于个人和家庭的收入和支出的具体管理，能合理的利用系统来完成查找和了解个人和家庭的收支情况。从而使您的生活更加合理的花费和管理自己的财务！</p>
		<p>功能介绍：此系统目前有:一,个人/家庭收入管理；二,个人/家庭支出管理,三,数据分析管理</p>
		<p><font color="red">　个人/家庭收入管理功能介绍:</font></p>
		<p>　　１）：收入类型管理：您可以根据自己的需要进行添加和修改删除收入类型，如：工资收入,奖金收入,其他收入...</p>
		<p>　　２）：收入管理：您可以根据自己的需要进行添加修改和删除收入,在收入中添加的时候可以根据时间情况选择您的收入类型。您还可以进行查询本月的总收入情况</p>
		<p><font color="red">　个人/家庭支出管理功能介绍:</font></p>
		<p>　　１）：支出类型管理：您可以根据自己的需要进行添加和修改删除支出类型，如：吃饭,日常用品,小吃...</p>
		<p>　　２）：支出管理：您可以根据自己的需要进行添加修改和删除支出,在支出中添加的时候可以根据时间情况选择您的收入类型。您还可以进行查询本月的总支出情况</p>
		<p><font color="red">　数据分析管理功能介绍:</font></p>
		<p>　　１）：财务状态数据分析：您可以选择日期段来统计您的收入，支出，以及结余（收入前去支出)三项记录</p>
		<p>　　２）：图形分析：你可以选择日期段来统计分析您的消费（支出）情况。</p>
		
		<p><input type="hidden" name="uname" id="uname" value="<%=userName %>"/> </p>  
		
		
		</div>
 
</div>
<div id='aboutDiv2' style='height:96%;width:30%'>

<table>
<tr>
<td><a href="http://s.click.taobao.com/t_9?p=mm_24002873_0_0&l=http%3A%2F%2Fmall.taobao.com%2F&eventid=101766"><img src="http://cb.alimama.cn/tbkunion/images/mall/images/120x240.gif" width="120px" height="240px" border="0" ></a></td></tr>
</table>	
</div>
<div id='aboutDiv3' style='height:96%;width:30%' align=right>
<script type='text/javascript'>
alimama_pid='mm_24002873_0_0';
alimama_type='g';
alimama_tks={};
alimama_tks.style_i=2;
alimama_tks.lg_i=1;
alimama_tks.w_i=572;
alimama_tks.h_i=69;
alimama_tks.btn_i=1;
alimama_tks.txt_s='';
alimama_tks.hot_i=1;
alimama_tks.hc_c='#0065FF';
alimama_tks.cid_i=0;
alimama_tks.t_i=1
</script>
<script type='text/javascript' src='http://a.alimama.cn/inf.js'></script>
</div>
<div id='aboutDiv3' style='height:96%;width:30%' align=center>
<script type="text/javascript" src="http://js.tongji.linezing.com/2145929/tongji.js"></script><noscript><a href="http://www.linezing.com"><img src="http://img.tongji.linezing.com/2145929/tongji.gif"/></a></noscript>
	
	</div>
</body>
</html>