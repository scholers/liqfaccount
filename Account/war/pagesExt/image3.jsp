<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.scholers.account.util.ComUtil" %>
<html>

  <%
    String strCurDate =  ComUtil.getForDate(ComUtil.getCurYearAndDate());
 %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
</head>
<script type="text/javascript">
var dftDate = '<%=strCurDate%>';
Ext.chart.Chart.CHART_URL = '../pagesExt/extjs3.3/resources/charts.swf';

Ext.onReady(function(){
	//定义数据集对象
	var typeStore = new Ext.data.JsonStore({
		 root: 'items', 
		 fields: ['typeName', 'price'],
		 url:"analysis.do?method=getCountListChart"
	})
	
	//创建工具栏组件
	var toolbar = new Ext.Toolbar([
		new Ext.Toolbar.TextItem('按时间查询：开始日期'),
		'-',   
		{
			xtype:'datefield',
			width : 120,
			allowBlank : true,
			blankText : '不能为空',
			id : 'times',
			name : 'times',
			value: '<%=strCurDate%>',
			editable : false,//禁止编辑
			format:'Y-m-d'
         },
         new Ext.Toolbar.TextItem('结束日期：'),
			'-',   
			{
				xtype:'datefield',
				width : 120,
				allowBlank : true,
				blankText : '不能为空',
				id : 'times2',
				name : 'times2',
				editable : false,//禁止编辑
				format:'Y-m-d'
          },
         '-', 
		{iconCls:'find',handler:onItemCheck }
	]);  
	
	typeStore.load({params:{start:0,limit:15}});
	//分页条件参数  
	typeStore.on('beforeload',function(){  
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
	        typeStore.reload({params:{start:0,limit:15,dd:dd,endtime:endtime}}); 
	};
	
  new Ext.Panel({
        width: 550,
        height: 400,
        tbar : toolbar,
        title: '消费分析饼状图',
        renderTo: 'container',
        items: {
            store: typeStore,
            xtype: 'piechart',
            dataField: 'price',
            categoryField: 'typeName',
            //extra styles get applied to the chart defaults
            extraStyle:
            {
                legend:
                {
                    display: 'bottom',
                    padding: 5,
                    font:
                    {
                        family: 'Tahoma',
                        size: 13
                    }
                }
            }
        }
    });
});
</script>
<body>
<div id="container"></div>
</body>
</html>