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
import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.scholers.account.bean.Book;
import com.scholers.account.bean.BookType;
import com.scholers.account.bean.PayType;
import com.scholers.account.bean.Users;
import com.scholers.account.business.InserviceIntf;
import com.scholers.account.business.impl.InService;
import com.scholers.account.util.ComUtil;
import com.scholers.account.util.ExtHelper;

/**
 * 
 * @author weique.lqf
 *
 */
public class InActionExt extends DispatchAction {

	/**
	 * 采用spring注入的方式
	 */
	@Autowired
	private InserviceIntf inService = null;

	/*
	 * 显示收入类型
	 */
	public ActionForward showBookList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		return mapping.findForward("bookList");
	}
	/*
	 * 显示收入类型列表
	 */
	public ActionForward showBookTypeList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		return mapping.findForward("bookTypeList");
	}
	
	/*
	 * 查询收入列表
	 */
	public ActionForward getBookList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String sumPay = ComUtil.getBigDecimal(inService.getInPay(user.getEmail()));
		request.getSession().setAttribute("sumIncome", sumPay);
		String sumPayY = ComUtil.getBigDecimal(inService.getSumInY(user.getEmail()));
		request.getSession().setAttribute("sumIncomeY", sumPayY);
		String start = request.getParameter("start");
		int page=Integer.valueOf(start);
		String times = request.getParameter("dd");
		//指定默认日期为本月初的日期
		if(times == null || times.equals("")) {
			times = ComUtil.getForDate(ComUtil.getCurYearAndDate());
		}
		//结束日期
		String endtime =request.getParameter("endtime");
	
		
		List<Book> payListRtn = null;
		int count = 15;
		 //总数
	    int totalSize = inService.getInSize(user.getEmail(), times, endtime);
	    int startNum = 0;
	    if(request.getParameter("start") != null){
	    	startNum = Integer.parseInt(request.getParameter("start"));
	    }
	    //当前页
	    int curPage = startNum;
	    //startNum *= count;
	    int endNum = startNum + count;
	    
	    if(endNum > totalSize){
	    	endNum = totalSize;
	    }
	    
		
		payListRtn = inService.getInByTime(startNum, endNum, user.getEmail(), times, endtime);
		
		String xml = ExtHelper.getJsonFromList(totalSize, payListRtn, "");
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(xml);   
		return null;
	}
	/*
	 * 得到类型列表
	 */
	public ActionForward getBookTypeList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		List<BookType> bookTypes = inService.getBookTypes(user.getEmail()); 
		
		List<BookType> bookTypesRtn = new ArrayList<BookType>();
		
		//分页
		int count = 10;
		 //总数
	    int totalSize = bookTypes.size();
	    int startNum = 0;
	    if(request.getParameter("start") != null){
	    	startNum = Integer.parseInt(request.getParameter("start"));
	    }
	    int endNum = startNum + count;
	    
	    if(endNum > totalSize){
	    	endNum = totalSize;
	    }
	    
	    bookTypesRtn.addAll(bookTypes.subList(startNum, endNum));
		
		String xml = ExtHelper.getJsonFromList(totalSize, bookTypesRtn, "");
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(xml);
		return null;
		
		
	}
	
	
	/*
	 * 得到所有类型列表
	 */
	public ActionForward getBookTypeListAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		List<BookType> bookTypes = inService.getBookTypes(user.getEmail()); 
		String xml = ExtHelper.getJsonFromList(bookTypes.size(), bookTypes, "");
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(xml);
		return null;
		
		
	}
	/*
	 * 增加收入
	 */
	public ActionForward addBook(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String sumPay = ComUtil.getBigDecimal(inService.getInPay(user.getEmail()));
		request.getSession().setAttribute("sumIncome", sumPay);
		String sumPayY = ComUtil.getBigDecimal(inService.getSumInY(user.getEmail()));
		request.getSession().setAttribute("sumIncomeY", sumPayY);
		String bookName = request.getParameter("bookName");
		String author = request.getParameter("author");
		String notes = request.getParameter("notes");
		Long bookTypeId = Long.parseLong(request.getParameter("bookTypeId"));
		float price = Float.parseFloat(request.getParameter("price"));
		String brief = request.getParameter("brief");
		// 消费日期
		Date date = ComUtil.getStrToDate(request.getParameter("useDate"));
		Date createDate = new Date();
		Book book = new Book(user, notes, date,
				createDate);
		book.setBookName(bookName);
		book.setAuthor(author);
		book.setBookTypeId(bookTypeId);
		book.setPrice(price);
		book.setBrief(brief);
		book.setEmail(user.getEmail());
		Long bookId = inService.addBook(book);
		boolean isSuccess = true;
		if(bookId == -1){  
			isSuccess = false;      
		}
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+",bookId:"+bookId+"}");
		return null;
	}
	/*
	 * 增加收入类型
	 */
	public ActionForward addBookType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String title = request.getParameter("title");
		String detail = request.getParameter("detail");
		BookType bookType = new BookType();
		bookType.setTitle(title);  
		bookType.setDetail(detail);
		bookType.setEmail(user.getEmail());
		Long iRtn = inService.addBookType(bookType);
		boolean isSuccess = true;
		if(iRtn == -1){
			isSuccess = false;
		}
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+",bookTypeId:"+  iRtn +"}");
		return null;
	}
	/*
	 * 修改收入
	 */
	public ActionForward modifyBook(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String sumPay = ComUtil.getBigDecimal(inService.getInPay(user.getEmail()));
		request.getSession().setAttribute("sumIncome", sumPay);
		String sumPayY = ComUtil.getBigDecimal(inService.getSumInY(user.getEmail()));
		request.getSession().setAttribute("sumIncomeY", sumPayY);
		
		Long bookId = Long.parseLong(request.getParameter("id"));
		String bookName = request.getParameter("bookName");
		String author = request.getParameter("author");
		Long bookTypeId = Long.parseLong(request.getParameter("bookTypeId"));
		float price = Float.parseFloat(request.getParameter("price"));
		String brief = request.getParameter("brief");
		String notes = request.getParameter("notes");
		// 消费日期
		Date date = ComUtil.getStrToDate(request.getParameter("useDate"));
		Date createDate = new Date();
		Book book = new Book(user, notes, date,
				createDate);
		book.setId(bookId);
		book.setBookName(bookName);
		book.setAuthor(author);
		book.setBookTypeId(bookTypeId);
		book.setPrice(price);
		book.setBrief(brief);
		boolean isSuccess = inService.updateBook(book);
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+",bookId:"+bookId+"}");
		return null;
	}
	/*
	 * 修改收入类型
	 */
	public ActionForward modifyBookType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		Long bookTypeId = Long.parseLong(request.getParameter("id"));
		String title = request.getParameter("title");
		String detail = request.getParameter("detail");
		BookType bookType = new BookType();
		bookType.setId(bookTypeId);
		bookType.setTitle(title);
		bookType.setDetail(detail);
		boolean isSuccess = inService.updateBookType(bookType);
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+",bookTypeId:"+bookTypeId+"}");
		return null;
	}
	/*
	 * 删除收入类型
	 */
	public ActionForward deleteBookType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Long bookTypeId = Long.parseLong(request.getParameter("bookTypeId"));
		//int num = inService.getBookNum(bookTypeId);
		response.setContentType("text/json;charset=UTF-8");
		//if(num == 0){
			boolean isSuccess = inService.deleteBookType(bookTypeId);
			response.getWriter().write("{success:"+isSuccess+",num:"+isSuccess+"}");
		//}else{
		//	response.getWriter().write("{success:false,num:"+num+"}");
		//}
		return null;
	}
	/*
	 * 鏍规嵁涔︾睄id鏌ヨ涔︾睄淇℃伅
	 */
	public ActionForward getBookById(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		
		//Long sumIncomeY =  Long.parseLong(SIncomeY.get(0).toString())+Long.parseLong(SIncomeY.get(1).toString())+Long.parseLong(SIncomeY.get(2).toString())+Long.parseLong(SIncomeY.get(3).toString())+Long.parseLong(SIncomeY.get(4).toString())+Long.parseLong(SIncomeY.get(5).toString())+Long.parseLong(SIncomeY.get(6).toString())+Long.parseLong(SIncomeY.get(7).toString())+Long.parseLong(SIncomeY.get(8).toString())+Long.parseLong(SIncomeY.get(9).toString())+Long.parseLong(SIncomeY.get(10).toString())+Long.parseLong(SIncomeY.get(11).toString());
		//re//quest.getSession().setAttribute("sumIncomeY", sumIncomeY);
		response.setContentType("text/json;charset=UTF-8");
		Long bookId = Long.parseLong(request.getParameter("bookId"));
		Book book = inService.getBook(bookId);
		String json = null;
		if(book != null){
			json = "{success:true,data:"+ExtHelper.getJsonFromBean(book)+"}";
		}else{
			json = "{success:false}";
		}
		response.getWriter().write(json);
		return null;
	}
	/*
	 * 根据收入编号查询收入类型
	 */
	public ActionForward getBookTypeById(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		response.setContentType("text/json;charset=UTF-8");
		Long bookTypeId = Long.parseLong(request.getParameter("bookTypeId"));
		BookType bookType = inService.getBookType(bookTypeId);
		String json = null;
		if(bookType != null){
			json = "{success:true,data:"+ExtHelper.getJsonFromBean(bookType)+"}";
		}else{
			json = "{success:false}";
		}
		response.getWriter().write(json);
		return null;
	}
	/*
	 * 删除收入
	 */
	public ActionForward deleteBooks(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String sumPay = ComUtil.getBigDecimal(inService.getInPay(user.getEmail()));
		request.getSession().setAttribute("sumIncome", sumPay);
		String sumPayY = ComUtil.getBigDecimal(inService.getSumInY(user.getEmail()));
		request.getSession().setAttribute("sumIncomeY", sumPayY);
		String bookIds = request.getParameter("bookIds");

		String[] ids =  bookIds.split("-");
		List<Long> idList = new ArrayList<Long>();
		for(int i = 0 ; i < ids.length ; i++){
			Long id = new Long(ids[i]);
			idList.add(id);
		}
		boolean isSuccess = inService.deleteBooks(idList);
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+"}");
		return null;
	}  
	
	
	public ActionForward login(ActionMapping mapping, ActionForm form,  
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String rand =  request.getSession().getAttribute("rand").toString();
			String randCode = request.getParameter("randCode");
			 Users user = new Users();
			 response.setContentType("text/json;charset=UTF-8");
			 if(rand.equals(randCode)){
				  user = null;//inService.getUsers(username);  
			 		if(user != null){
			 			if(user.getPassword().equals(password)){
					 		request.getSession().setAttribute("user", user);
					 		String userN =user.getUsername();
					 		String msg = "登录成功";
					 		response.getWriter().write("{success:true,msg:'"+msg+"',userN:'"+userN+"'}");
			 			}   
			 			else{
			 				String msg = "请确认密码，登录失败";
					 		response.getWriter().write("{success:false,msg:'"+msg+"'}");
			 			}
			 		}else{
			 			String msg = "对不起，没有此用户";
				 		response.getWriter().write("{success:false,msg:'"+msg+"'}");
			 		}
			 	}else{ 
			 		String msg = "对不起，验证码错误";  
			 		response.getWriter().write("{success:false,msg:'"+msg+"'}");
			 	}	 
		return null;
	}
	
	public ActionForward getUserName(ActionMapping mapping, ActionForm form,  
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		   Users user = new Users();
		   user = (Users) request.getSession().getAttribute("user");
			UserService userService = UserServiceFactory.getUserService();
			User user2 = userService.getCurrentUser();
		   String username = user.getUsername();
		   Long sumIncom = 0L;//inService.getIncome(user.getId());
			request.getSession().setAttribute("sumIncome", sumIncom);
			int sumPay = 0;//Pservice.getSumPay(user2.getEmail());
			request.getSession().setAttribute("sumPay", sumPay);
			List SPayY = inService.getBookByYP(user.getEmail());
			Long sumPayY = Long.parseLong(SPayY.get(0).toString())+Long.parseLong(SPayY.get(1).toString())+Long.parseLong(SPayY.get(2).toString())+Long.parseLong(SPayY.get(3).toString())+Long.parseLong(SPayY.get(4).toString())+Long.parseLong(SPayY.get(5).toString())+Long.parseLong(SPayY.get(6).toString())+Long.parseLong(SPayY.get(7).toString())+Long.parseLong(SPayY.get(8).toString())+Long.parseLong(SPayY.get(9).toString())+Long.parseLong(SPayY.get(10).toString())+Long.parseLong(SPayY.get(11).toString());
			request.getSession().setAttribute("sumPayY", sumPayY);
			List SIncomeY = inService.getBookByY(user.getEmail());
			Long sumIncomeY = Long.parseLong(SIncomeY.get(0).toString())+Long.parseLong(SIncomeY.get(1).toString())+Long.parseLong(SIncomeY.get(2).toString())+Long.parseLong(SIncomeY.get(3).toString())+Long.parseLong(SIncomeY.get(4).toString())+Long.parseLong(SIncomeY.get(5).toString())+Long.parseLong(SIncomeY.get(6).toString())+Long.parseLong(SIncomeY.get(7).toString())+Long.parseLong(SIncomeY.get(8).toString())+Long.parseLong(SIncomeY.get(9).toString())+Long.parseLong(SIncomeY.get(10).toString())+Long.parseLong(SIncomeY.get(11).toString());
			request.getSession().setAttribute("sumIncomeY", sumIncomeY);
			request.getSession().setAttribute("username",username);
		   response.setContentType("text/json;charset=UTF-8");
		   response.getWriter().write("{username:'"+username+"'}");
		   response.sendRedirect("indexExt.jsp");
		   return null;
	}
	
	
}
