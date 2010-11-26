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
import com.scholers.account.bean.Pay;
import com.scholers.account.bean.Users;
import com.scholers.account.business.UsersService;
import com.scholers.account.util.ExtHelper;




public class UsersActionExt extends DispatchAction{ 
	   UsersService   service = new UsersService();
	
	 public ActionForward showUsersList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		return mapping.findForward("usersList");
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
	public ActionForward getUsersList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		String start = request.getParameter("start");
		int page =Integer.valueOf(start);  
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String times =request.getParameter("dd");
		List<Users> payListRtn = null;
		int count = 15;
		 //总数
	    int totalSize = service.getUsersNum(user.getEmail());
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
	    
		
		payListRtn = service.getUsers(startNum, endNum);
		
		
		//分页处理
		List<Users> userList = new ArrayList<Users>();
		if(page == 0){
			if(payListRtn.size() > 15){
				userList.addAll(payListRtn.subList(0, 15));
			}else{
				userList.addAll(payListRtn);
			}   
		}else{   
			int num = page+15;
			if(payListRtn.size()>num){
				for(int i=page; i<num;i++){
					userList.add(payListRtn.get(i));  
				}
			}else{
				for(int i=page; i<payListRtn.size();i++){
					userList.add(payListRtn.get(i));
				}
			}
		}
		
		String strJson = ExtHelper.getJsonFromList(payListRtn.size(), userList, "");
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(strJson);
		return null;
	}
	
	/**
	 * 增加用户信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addUsers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		Users users = new Users();   
        users.setPassword(password);
        users.setUsername(username);
        users.setEmail(user.getEmail());
        users.setUserType(0L);
        users.setCreateDate(new Date());
		service.addUsers(users); 
		boolean isSuccess = true;
		String usersId = "";
		//if(usersId == 0L){  
		//	isSuccess = false;        
		//}
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+",usersId:"+usersId+"}");
		return null;  
	}
	
	/**
	 * 修改用户信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modifyUsers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Long usersId = Long.parseLong(request.getParameter("id"));
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		 
		Users users = new Users();
		users.setId(usersId);
		users.setPassword(password);
		users.setUsername(username);
		boolean isSuccess = service.updateUsers(users);
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:"+isSuccess+",usersId:"+usersId+"}");
		return null;
	}
	
	/**
	 * 删除用户信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteUsers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Long usersId = Long.parseLong(request.getParameter("usersId"));
		int num = 0;//service.getUsersNum(usersId);
		response.setContentType("text/json;charset=UTF-8");
		if(num == 0){  
			boolean isSuccess = service.deleteUsers(usersId);
			response.getWriter().write("{success:"+isSuccess+",num:"+num+"}");
		}else{
			response.getWriter().write("{success:false,num:"+num+"}");
		}
		return null;
	}
	
	public ActionForward getUsersById(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		response.setContentType("text/json;charset=UTF-8");
		Long usersId = Long.parseLong(request.getParameter("usersId"));
		Users users = service.getUsers(usersId);
		String json = null;
		if(users != null){  
			json = "{success:true,data:"+ExtHelper.getJsonFromBean(users)+"}";
		}else{
			json = "{success:false}";
		}
		response.getWriter().write(json);
		return null;
	}
}
   