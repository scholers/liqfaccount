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
import com.scholers.account.bean.Book;
import com.scholers.account.bean.BookType;
import com.scholers.account.bean.Users;
import com.scholers.account.business.InService;
import com.scholers.account.util.ComUtil;
import com.scholers.account.util.ExtHelper;


public class InActionExt extends DispatchAction {

	private InService service = new InService();

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
		String sumPay = ComUtil.getBigDecimal(service.getInPay(user.getEmail()));
		request.getSession().setAttribute("sumIncome", sumPay);
		String sumPayY = ComUtil.getBigDecimal(service.getSumInY(user.getEmail()));
		request.getSession().setAttribute("sumIncomeY", sumPayY);
		String start = request.getParameter("start");
		int page=Integer.valueOf(start);
		String times = request.getParameter("dd");
		/*if(times !=null){
			books = service.getInByTime(user.getEmail(), times);
		}else{
			books = service.getBooks(user.getEmail());
		}
		if(books == null) {
			books = new ArrayList<Book>();
		}
		//分页处理
		List<Book> book = new ArrayList<Book>();
		if(page == 0){
			if(books.size() > 15){
				book.addAll(books.subList(0, 15));
			}else{
				book.addAll(books);
			}   
		}else{   
			int num = page+15;
			if(books.size()>num){
				for(int i=page; i<num;i++){
					book.add(books.get(i));  
				}
			}else{
				for(int i=page; i<books.size();i++){
					book.add(books.get(i));
				}
			}
		}*/
		
		
		List<Book> payListRtn = null;
		int count = 15;
		 //总数
	    int totalSize = service.getInSize(user.getEmail(), times);
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
	    
		
		payListRtn = service.getInByTime(startNum, endNum, user.getEmail(), times);
		
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
		List<BookType> bookTypes = service.getBookTypes(user.getEmail());     
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
		String sumPay = ComUtil.getBigDecimal(service.getInPay(user.getEmail()));
		request.getSession().setAttribute("sumIncome", sumPay);
		String sumPayY = ComUtil.getBigDecimal(service.getSumInY(user.getEmail()));
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
		Long bookId = service.addBook(book);
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
		Long iRtn = service.addBookType(bookType);
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
		String sumPay = ComUtil.getBigDecimal(service.getInPay(user.getEmail()));
		request.getSession().setAttribute("sumIncome", sumPay);
		String sumPayY = ComUtil.getBigDecimal(service.getSumInY(user.getEmail()));
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
		boolean isSuccess = service.updateBook(book);
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
		boolean isSuccess = service.updateBookType(bookType);
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
		//int num = service.getBookNum(bookTypeId);
		response.setContentType("text/json;charset=UTF-8");
		//if(num == 0){
			boolean isSuccess = service.deleteBookType(bookTypeId);
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
		Book book = service.getBook(bookId);
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
		BookType bookType = service.getBookType(bookTypeId);
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
		String sumPay = ComUtil.getBigDecimal(service.getInPay(user.getEmail()));
		request.getSession().setAttribute("sumIncome", sumPay);
		String sumPayY = ComUtil.getBigDecimal(service.getSumInY(user.getEmail()));
		request.getSession().setAttribute("sumIncomeY", sumPayY);
		String bookIds = request.getParameter("bookIds");

		String[] ids =  bookIds.split("-");
		List<Long> idList = new ArrayList<Long>();
		for(int i = 0 ; i < ids.length ; i++){
			Long id = new Long(ids[i]);
			idList.add(id);
		}
		boolean isSuccess = service.deleteBooks(idList);
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
				  user = service.getUsers(username);  
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
		   Long sumIncom = service.getIncome(user.getId());
			request.getSession().setAttribute("sumIncome", sumIncom);
			int sumPay = 0;//Pservice.getSumPay(user2.getEmail());
			request.getSession().setAttribute("sumPay", sumPay);
			List SPayY = service.getBookByYP(user.getEmail());
			Long sumPayY = Long.parseLong(SPayY.get(0).toString())+Long.parseLong(SPayY.get(1).toString())+Long.parseLong(SPayY.get(2).toString())+Long.parseLong(SPayY.get(3).toString())+Long.parseLong(SPayY.get(4).toString())+Long.parseLong(SPayY.get(5).toString())+Long.parseLong(SPayY.get(6).toString())+Long.parseLong(SPayY.get(7).toString())+Long.parseLong(SPayY.get(8).toString())+Long.parseLong(SPayY.get(9).toString())+Long.parseLong(SPayY.get(10).toString())+Long.parseLong(SPayY.get(11).toString());
			request.getSession().setAttribute("sumPayY", sumPayY);
			List SIncomeY = service.getBookByY(user.getEmail());
			Long sumIncomeY = Long.parseLong(SIncomeY.get(0).toString())+Long.parseLong(SIncomeY.get(1).toString())+Long.parseLong(SIncomeY.get(2).toString())+Long.parseLong(SIncomeY.get(3).toString())+Long.parseLong(SIncomeY.get(4).toString())+Long.parseLong(SIncomeY.get(5).toString())+Long.parseLong(SIncomeY.get(6).toString())+Long.parseLong(SIncomeY.get(7).toString())+Long.parseLong(SIncomeY.get(8).toString())+Long.parseLong(SIncomeY.get(9).toString())+Long.parseLong(SIncomeY.get(10).toString())+Long.parseLong(SIncomeY.get(11).toString());
			request.getSession().setAttribute("sumIncomeY", sumIncomeY);
			request.getSession().setAttribute("username",username);
		   response.setContentType("text/json;charset=UTF-8");
		   response.getWriter().write("{username:'"+username+"'}");
		   response.sendRedirect("indexExt.jsp");
		   return null;
	}
	
	
}
