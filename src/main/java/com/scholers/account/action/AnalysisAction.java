package com.scholers.account.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.scholers.account.bean.CountBean;
import com.scholers.account.business.AccountIntf;
import com.scholers.account.business.InserviceIntf;
import com.scholers.account.business.impl.AcountBusiness;
import com.scholers.account.business.impl.InService;
import com.scholers.account.util.ComUtil;
import com.scholers.account.util.ExtHelper;

/**
 * 统计分析
 * @author jill
 *
 */
public class AnalysisAction extends DispatchAction {
	@Autowired
	private AccountIntf acountBusiness = null;

	@Autowired     
	private  InserviceIntf inService = null; 

	// 放入数据
	private static CategoryDataset getDataSet(List a) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue((Number) a.get(0), "2009年", "一月");
		dataset.addValue((Number) a.get(1), "2009年", "二月");
		dataset.addValue((Number) a.get(2), "2009年", "三月");
		dataset.addValue((Number) a.get(3), "2009年", "四月");
		dataset.addValue((Number) a.get(4), "2009年", "五月");
		dataset.addValue((Number) a.get(5), "2009年", "六月");
		dataset.addValue((Number) a.get(6), "2009年", "七月");
		dataset.addValue((Number) a.get(7), "2009年", "八月");
		dataset.addValue((Number) a.get(8), "2009年", "九月");
		dataset.addValue((Number) a.get(9), "2009年", "十月");
		dataset.addValue((Number) a.get(10), "2009年", "十一月");
		dataset.addValue((Number) a.get(11), "2009年", "十二月");
		return dataset;
	}

	public ActionForward getBookByY(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String filename = null;
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		List a = inService.getBookByY(user.getEmail());
		CategoryDataset dataset = getDataSet(a);
		JFreeChart chart = ChartFactory.createLineChart("2009年全年收入走势图", // chart
																		// title
				"时间", // domain axis label
				"收入金额(元)", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls

				);
		CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot
				.getRenderer();
		lineandshaperenderer.setSeriesShapesVisible(0, true);
		lineandshaperenderer.setSeriesShapesVisible(1, true);
		lineandshaperenderer.setSeriesShapesVisible(2, true);
		lineandshaperenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		lineandshaperenderer.setBaseItemLabelsVisible(true);
		// lineandshaperenderer.setBaseFillPaint(Color.black);;
		// chart.setBackgroundPaint(Color.lightGray);
		chart.setTextAntiAlias(true);
		chart.setNotify(true);
		// categoryplot.setBackgroundPaint(Color.white);
		// categoryplot.setRangeGridlinePaint(Color.black);
		NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberaxis.setAutoRangeIncludesZero(true);
		try {
			/*------得到chart的保存路径----*/
			ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			filename = ServletUtilities.saveChartAsPNG(chart, 736, 400, info,
					request.getSession());
			/*------使用printWriter将文件写出----*/
			String grapIYURL = "/servlet/DisplayChart?filename=" + filename;
			request.getSession().setAttribute("grapIYURL", grapIYURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapping.findForward("incomyear");
	}

	public ActionForward getBookByYP(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String filename = null;
		// Users user = (Users) request.getSession().getAttribute("user");
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		List a = inService.getBookByYP(user.getEmail());
		CategoryDataset dataset = getDataSet(a);
		JFreeChart chart = ChartFactory.createLineChart("2009年全年支出走势图", // chart
																		// title
				"时间", // domain axis label
				"支出金额(元)", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls

				);
		CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot
				.getRenderer();
		lineandshaperenderer.setSeriesShapesVisible(0, true);
		lineandshaperenderer.setSeriesShapesVisible(1, true);
		lineandshaperenderer.setSeriesShapesVisible(2, true);
		lineandshaperenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		lineandshaperenderer.setBaseItemLabelsVisible(true);
		// lineandshaperenderer.setBaseFillPaint(Color.black);;
		// chart.setBackgroundPaint(Color.lightGray);
		chart.setTextAntiAlias(true);
		chart.setNotify(true);
		// categoryplot.setBackgroundPaint(Color.white);
		// categoryplot.setRangeGridlinePaint(Color.black);
		NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberaxis.setAutoRangeIncludesZero(true);
		try {
			/*------得到chart的保存路径----*/
			ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			filename = ServletUtilities.saveChartAsPNG(chart, 736, 400, info,
					request.getSession());
			/*------使用printWriter将文件写出----*/
			String grapIYURLP = "/servlet/DisplayChart?filename=" + filename;
			request.getSession().setAttribute("grapIYURLP", grapIYURLP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapping.findForward("payyear");
	}

	private CategoryDataset getDataSet1(List a) {
		DefaultCategoryDataset dataset = null;
		try {
			dataset = new DefaultCategoryDataset();
			Calendar calendar = Calendar.getInstance(TimeZone.getDefault(),
					Locale.CHINESE);
			calendar.setTime(new Date());

			int month = calendar.get(Calendar.MONTH) + 1;

			String m = String.valueOf(month + 1);
			int s = inService.getDay();
			for (int i = 0; i < s; i++) {
				dataset.addValue((Number) a.get(i), m + "月", i + 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataset;
	}

	public ActionForward getInByM(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String filename = null;
		// Users user = (Users) request.getSession().getAttribute("user");
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		List a = inService.getInByM(user.getEmail());
		CategoryDataset dataset = getDataSet1(a);
		JFreeChart chart = ChartFactory.createLineChart("本月收入走势图", // chart
																	// title
				"时间(号)", // domain axis label
				"支出金额(元)", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls

				);
		CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot
				.getRenderer();
		lineandshaperenderer.setSeriesShapesVisible(0, true);
		lineandshaperenderer.setSeriesShapesVisible(1, true);
		lineandshaperenderer.setSeriesShapesVisible(2, true);
		lineandshaperenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		lineandshaperenderer.setBaseItemLabelsVisible(true);
		// lineandshaperenderer.setBaseFillPaint(Color.black);;
		// chart.setBackgroundPaint(Color.lightGray);
		chart.setTextAntiAlias(true);
		chart.setNotify(true);
		// categoryplot.setBackgroundPaint(Color.white);
		// categoryplot.setRangeGridlinePaint(Color.black);
		NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberaxis.setAutoRangeIncludesZero(true);
		try {
			/*------得到chart的保存路径----*/
			ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			filename = ServletUtilities.saveChartAsPNG(chart, 736, 400, info,
					request.getSession());
			/*------使用printWriter将文件写出----*/
			String grapIMURL = "/servlet/DisplayChart?filename=" + filename;
			request.getSession().setAttribute("grapIMURL", grapIMURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapping.findForward("getInByM");
	}

	public ActionForward getPayByM(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String filename = null;
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		List a = inService.getBookByM(user.getEmail());
		CategoryDataset dataset = getDataSet1(a);
		JFreeChart chart = ChartFactory.createLineChart("本月支出走势图", // chart
																	// title
				"时间(号)", // domain axis label
				"支出金额(元)", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls

				);
		CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot
				.getRenderer();
		lineandshaperenderer.setSeriesShapesVisible(0, true);
		lineandshaperenderer.setSeriesShapesVisible(1, true);
		lineandshaperenderer.setSeriesShapesVisible(2, true);
		lineandshaperenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		lineandshaperenderer.setBaseItemLabelsVisible(true);
		// lineandshaperenderer.setBaseFillPaint(Color.black);;
		// chart.setBackgroundPaint(Color.lightGray);
		chart.setTextAntiAlias(true);
		chart.setNotify(true);
		// categoryplot.setBackgroundPaint(Color.white);
		// categoryplot.setRangeGridlinePaint(Color.black);
		NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberaxis.setAutoRangeIncludesZero(true);
		try {
			/*------得到chart的保存路径----*/
			ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			filename = ServletUtilities.saveChartAsPNG(chart, 736, 400, info,
					request.getSession());
			/*------使用printWriter将文件写出----*/
			String grapPMURL = "/servlet/DisplayChart?filename=" + filename;
			request.getSession().setAttribute("grapPMURL", grapPMURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapping.findForward("getPayByM");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	 public ActionForward showCountList(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			return mapping.findForward("getCountList");
		}
	 
	 /**
	  * 
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public ActionForward showCountListChart(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			return mapping.findForward("getCountListChart");
		}
	 
	 
	/**
	 * 查询统计结果
	 * 查询出来的结果形成三条记录
	 * 分别是总收入，总支出，总结余
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getCountList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		//开始日期
		String strStartTimes = request.getParameter("dd");
		//指定默认日期为本月初的日期
		if(strStartTimes == null || strStartTimes.equals("")) {
			strStartTimes = ComUtil.getForDate(ComUtil.getCurYearAndDate());
		}
		//结束日期
		String strEndTimes = request.getParameter("endtime");
		//总收入
		CountBean countBean = acountBusiness.getInByTimeAll(user.getEmail(), strStartTimes, strEndTimes, false).get(0);
		countBean.setId(1L);
		//总支出
		CountBean countBeanPay = acountBusiness.getPayByTimeAll(user.getEmail(), strStartTimes, strEndTimes, false).get(0);
		countBeanPay.setId(2L);
		//总结余
		CountBean countBeanEnd = new CountBean();
		countBeanEnd.setId(3L);
		countBeanEnd.setEmail(user.getEmail());
		countBeanEnd.setAuthor(user.getEmail());
		countBeanEnd.setNotes("总结余");
		countBeanEnd.setPrice(ComUtil.sub(countBean.getPrice(), countBeanPay.getPrice()));
		List<CountBean> payListRtn = new ArrayList<CountBean>();
		payListRtn.add(countBean);
		payListRtn.add(countBeanPay);
		payListRtn.add(countBeanEnd);
		String strJson = ExtHelper.getJsonFromList(payListRtn.size(), payListRtn, String.valueOf(countBeanEnd.getPrice()));
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(strJson);
		return null;
	}
	
	
	/**
	 * 查询统计结果--总消费情况
	 * 分别是总消费
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getCountListChart(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		//开始日期
		String times =request.getParameter("dd");
		//指定默认日期为本月初的日期
		if(times == null || times.equals("")) {
			times = ComUtil.getForDate(ComUtil.getCurYearAndDate());
		}
		//结束日期
		String endtime =request.getParameter("endtime");
		//总收入
		//List<CountBean>  = acountBusiness.getInByTimeAll(user.getEmail(), times, endtime, true);
		//countBean.setId(1L);
		//总消费
		List<CountBean> payList = acountBusiness.getPayByTimeAll(user.getEmail(), times, endtime, true);
		//countBeanPay.setId(2L);
		//总结余
		
		
		String strJson = ExtHelper.getJsonFromList(payList.size(), payList, "");
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(strJson);
		return null;
	}
	
	

}
