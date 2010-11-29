<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
</head>
<script type="text/javascript">

Ext.chart.Chart.CHART_URL = '../pagesExt/extjs3.3/resources/charts.swf';

Ext.onReady(function(){
	//定义数据集对象
	var typeStore = new Ext.data.JsonStore({
		 root: 'items', 
		 fields: ['notes', 'price'],
		 url:"analysis.do?method=getCountListChart"
	})
	typeStore.load({params:{start:0,limit:15}});
	
  new Ext.Panel({
        width: 400,
        height: 400,
        title: '财务分析饼状图',
        renderTo: 'container',
        items: {
            store: typeStore,
            xtype: 'piechart',
            dataField: 'price',
            categoryField: 'notes',
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