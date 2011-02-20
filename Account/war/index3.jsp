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
<script type="text/javascript">
	Ext.onReady(function(){	

		Ext.BLANK_IMAGE_URL = "pagesExt/extjs3.3/resources/images/default/s.gif";

		Ext.QuickTips.init();	
		
		var _top = new Ext.Panel({
						region:"north",
						//title:"标题",
						height:40,
						border:true,
						html:"LOGO",
						margins:'0 0 5 0'
					});
		var _left = new Ext.tree.TreePanel({
						region:"west",
						collapsible:true,
						title:"导航菜单",
						width:200,
						autoScroll:true,
						split:true,
						listeners:{
							click:function(_node){								
									var _url = _node.attributes.url;
									var _id = _node.id;//如果没有宝，则自动生成唯一的ID
									var _p = _center.getItem(id);//获取节点ID对应的标签面板
									if(_url){//具有URL值
										if(_p){
											//此面板已经存在，只需要激活就可以了。
											_center.setActiveTabl(_p);
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
											_center.add(_p);
											_center.setActiveTab(_p);
										}
									}
								}
						}
					});
		//定义根节点
		var _root = new Ext.tree.TreeNode({
			id :"work1",
			text : '系统说明',
			url : '/pagesExt/about.jsp',
			expanded : true//默认展开根节点
					});
		
		var _child1 = new Ext.tree.TreeNode({
			id :"work1",
			text : '个人/家庭收入管理',
			url : 'pagesExt/about.jsp',
			expanded : true//默认展开根节点
					});
		var _child2 = new Ext.tree.TreeNode({
			id :"work3",
			text : '收入类型管理',
			url : 'http://localhost:8888/inext.do?method=showBookTypeList'
					});
		var _child3 = new Ext.tree.TreeNode({
						text : "用户管理",
						url: "user_list.jsp"
					});
					
		_root.appendChild([_child1, _child2, _child3]);
		_left.setRootNode(_root);
		
		
		var _center = new Ext.TabPanel({
						region:"center",
						items:{
							id:"opt1",
							title:"默认页面",
							tabTip:"这是默认页面，不可以关闭",
							html : "<iframe src=\"" + "/pagesExt/about.jsp" + "\" style='height:100%;width:100%;' frameborder=0></iframe>",
						},
						enableTabScroll:true
						//activeItem:0
					});
		_center.setActiveTab("opt1");
		
		var _bottom = new Ext.Panel({
						region:"south",
						title:"Information",
						collapsible:true,
						html:"版权所有，翻版必究",
						split:true,
						height:100,
						bodyStyle : "padding: 10px; font-size: 12px; text-align:center;"
					});
		var _bottom2 = new Ext.Panel({
						region:"south",
						//height:50,
						frame:false,
						autoHeight:true,
						items:new Ext.Toolbar({
							height:20,
							items:[{
								xtype:'label',
								text:'wjt276'
							}]
						})
					});
					
		var _vp = new Ext.Viewport({
					layout:"border",
					items:[{
						xtype : 'box',
						region : 'north',
						applyTo : 'header',
						height : 50,
						split : false
					},
					//_top
					_left,_center
					,_bottom2
					]
				});
		
		_left.expandAll();
	});
			</script>
	
  </head>
  
  <body>
	<div id="header"><h1>清风记账本</h1></div>
  </body>
  </html>
  