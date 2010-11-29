package com.scholers.account.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.scholers.account.bean.Pay;
import com.scholers.account.bean.PayType;
import com.scholers.account.business.impl.PayService;
import com.scholers.account.util.ComUtil;
import com.scholers.account.util.ExtHelper;

public class PayActionExt extends DispatchAction {
	private PayService payService ;



	public void setPayService(PayService payService) {
		this.payService = payService;
	}

	/*
	 * 跳转支出页面
	 */
	public ActionForward showPayList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("payList");
	}

	/*
	 * 跳转支出类型页面
	 */
	public ActionForward showPayTypeList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("payTypeList");
	}

	/**
	 * 得到支出列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getPayList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String sumPay = ComUtil.getBigDecimal(payService.getSumPay(user.getEmail()));
		request.getSession().setAttribute("sumPay", sumPay);
		String sumPayY = ComUtil.getBigDecimal(payService.getSumPayY(user.getEmail()));
		request.getSession().setAttribute("sumPayY", sumPayY);
		//开始日期
		String times =request.getParameter("dd");
		//结束日期
		String endtime =request.getParameter("endtime");
		List<Pay> payListRtn = null;
		int count = 15;
		 //总数
	    int totalSize = payService.qryAccCount(user.getEmail(), times, endtime);
	    int startNum = 0;
	    if(request.getParameter("start") != null){
	    	startNum = Integer.parseInt(request.getParameter("start"));
	    }
	    //当前页
	    int curPage = startNum;
	    //startNum *= count;
	    int endNum = startNum + count;
	    
	    if(endNum > totalSize && totalSize != 0){
	    	endNum = totalSize;
	    }
		payListRtn = payService.getPayByTime(startNum, endNum, user.getEmail(), times, endtime);
		String strJson = ExtHelper.getJsonFromList(totalSize, payListRtn, "111.00");
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(strJson);
		return null;
	}

	/**
	 * 查询支付类型信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getPayTypeList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		List<PayType> payTypes = payService.getPayTypes(user.getEmail());
		String strJson = ExtHelper.getJsonFromList(payTypes.size(), payTypes, "");
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(strJson);
		return null;
	}

	/**
	 * 增加支付
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addPay(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String sumPay = ComUtil.getBigDecimal(payService.getSumPay(user.getEmail()));
		request.getSession().setAttribute("sumPay", sumPay);
		String notes = request.getParameter("notes");
		String payName = request.getParameter("payName");
		String typeName = request.getParameter("typeName");
		String author = request.getParameter("author");
		Long payTypeId = Long.parseLong(request.getParameter("payTypeId"));
		// 消费金额
		Float temAmt = Float.valueOf(request.getParameter("price"));
		// 消费日期
		Date date = ComUtil.getStrToDate(request.getParameter("useDate"));
		Date createDate = new Date();
		Pay accBean = new Pay(user, notes, date,
				createDate);
		accBean.setPrice(temAmt);
		accBean.setAuthor(author);
		accBean.setPayName(payName);
		accBean.setPayTypeId(payTypeId);
		accBean.setTypeName(typeName);
		if (user != null) {
			/*log.info("Greeting posted by user " + user.getNickname() + ": "
					+ content);*/
		} else {
			//log.info("Greeting posted anonymously: " + content);
		}
		Long payId = payService.addPay(accBean);
		boolean isSuccess = true;
		if(payId > 0 ){
			isSuccess = true;
		} else {
			isSuccess = false;
		}
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(
				"{success:" + isSuccess + ",payId:" + payId + "}");
		return null;
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
	public ActionForward addPayType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String title = request.getParameter("title");
		String detail = request.getParameter("detail");
		PayType payType = new PayType();
		payType.setTitle(title);
		payType.setDetail(detail);
		payType.setEmail(user.getEmail());
		Long payTypeId = payService.addPayType(payType);
		boolean isSuccess = true;
		if (payTypeId == -1) {
			isSuccess = false;
		}
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(
				"{success:" + isSuccess + ",payTypeId:" + payTypeId + "}");
		return null;
	}

	/**
	 * 修改支出
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modifyPay(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		Long payId = Long.parseLong(request.getParameter("id"));
		String notes = request.getParameter("notes");
		String payName = request.getParameter("payName");
		String typeName = request.getParameter("typeName");
		String author = request.getParameter("author");
		Long payTypeId = Long.parseLong(request.getParameter("payTypeId"));
		// 消费金额
		Float temAmt = Float.valueOf(request.getParameter("price"));
		// 消费日期
		Date date = ComUtil.getStrToDate(request.getParameter("useDate"));
		Date createDate = new Date();
		Pay accBean = new Pay(user, notes, date,
				createDate);
		accBean.setPrice(temAmt);
		accBean.setAuthor(author);
		accBean.setPayName(payName);
		accBean.setPayTypeId(payTypeId);
		accBean.setTypeName(typeName);
		accBean.setId(payId);
		
		boolean isSuccess = payService.updatePay(accBean);
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(
				"{success:" + isSuccess + ",payId:" + payId + "}");
		return null;
	}

	public ActionForward modifyPayType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long payTypeId = Long.parseLong(request.getParameter("id"));
		String title = request.getParameter("title");
		String detail = request.getParameter("detail");
		PayType payType = new PayType();
		payType.setId(payTypeId);
		payType.setTitle(title);
		payType.setDetail(detail);
		boolean isSuccess = payService.updatePayType(payType);
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(
				"{success:" + isSuccess + ",payTypeId:" + payTypeId + "}");
		return null;
	}

	/**
	 * 删除支付类型
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deletePayType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long payTypeId = Long.parseLong(request.getParameter("payTypeId"));
		int num = payService.getPayNum(payTypeId);
		boolean isSuccess = true;
		response.setContentType("text/json;charset=UTF-8");
		if (num == 0) {
			if (payTypeId <= 0) {
				isSuccess = false;
			} else {
				isSuccess = payService.deletePayType(payTypeId);
			}
			
			response.getWriter().write(
					"{success:" + isSuccess + ",num:" + num + "}");
		} else {
			response.getWriter().write("{success:false,num:" + num + "}");
		}
		return null;
	}

	/**
	 */
	public ActionForward getPayTypeById(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setContentType("text/json;charset=UTF-8");
		Long payTypeId = Long.parseLong(request.getParameter("payTypeId"));
		PayType payType = payService.getPayType(payTypeId);
		String json = null;
		if (payType != null) {
			json = "{success:true,data:" + ExtHelper.getJsonFromBean(payType)
					+ "}";
		} else {
			json = "{success:false}";
		}
		response.getWriter().write(json);
		return null;
	}
	
	/**
	 * 根据支付编号获取支付数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getPayById(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setContentType("text/json;charset=UTF-8");
		Long payId = Long.parseLong(request.getParameter("payId"));
		Pay pay = payService.getPay(payId);
		String json = null;
		if (pay != null) {
			json = "{success:true,data:" + ExtHelper.getJsonFromBean(pay) + "}";
		} else {
			json = "{success:false}";
		}
		System.out.println(json);
		response.getWriter().write(json);
		return null;
		
		
		
	}


/**
 * 删除支付数据--批量
 * @param mapping
 * @param form
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
	public ActionForward deletePays(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//Users users = (Users) request.getSession().getAttribute("user");
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String sumPay = ComUtil.getBigDecimal(payService.getSumPay(user.getEmail()));
		request.getSession().setAttribute("sumPay", sumPay);
		String sumPayY = ComUtil.getBigDecimal(payService.getSumPayY(user.getEmail()));
		request.getSession().setAttribute("sumPayY", sumPayY);
		
		String payIds = request.getParameter("payIds");
		String[] ids = payIds.split("-");
		List<Long> idList = new ArrayList<Long>();
		for (int i = 0; i < ids.length; i++) {
			Long id = new Long(ids[i]);
			idList.add(id);
		}
		boolean isSuccess = payService.deletePays(idList);
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:" + isSuccess + "}");
		return null;
	}
}
