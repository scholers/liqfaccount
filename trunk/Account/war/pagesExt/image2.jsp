<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ page import="com.scholers.account.util.ComUtil" %>
<html>
  <%
    java.util.Date date = new java.util.Date();
    String strCurDate =  ComUtil.getForDate(date);
 %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
</head>
<script type="text/javascript">

Ext.chart.Chart.CHART_URL = '../pagesExt/extjs3.3/resources/charts.swf';

Ext.onReady(function(){
    var store = new Ext.data.JsonStore({
        fields: ['season', 'total'],
        data: [{
            season: 'Summer',
            total: 150
        },{
            season: 'Fall',
            total: 245
        },{
            season: 'Winter',
            total: 117
        },{
            season: 'Spring',
            total: 184
        }]
    });
    
    new Ext.Panel({
        width: 400,
        height: 400,
        title: 'Pie Chart with Legend - Favorite Season',
        renderTo: 'container',
        items: {
            store: store,
            xtype: 'piechart',
            dataField: 'total',
            categoryField: 'season',
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