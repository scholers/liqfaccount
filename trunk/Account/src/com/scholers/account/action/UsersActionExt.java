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
import com.scholers.account.bean.Users;
import com.scholers.account.business.UsersServicesIntf;
import com.scholers.account.util.ExtHelper;



/**
 * 
 * @author weique.lqf
 *
 */
public class UsersActionExt extends DispatchAction{ 
	@Autowired
	private UsersServicesIntf   usersService;

	public ActionForward showUsersList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		return mapping.findForward("usersList");
	}
	
	 /**
	  * 获取用户列表
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
		List<Users> payListRtn = null;
		int count = 15;
		 //总数
	    int totalSize = usersService.getUsersNum(user.getEmail());
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
	    
		
		payListRtn = usersService.getUsers(startNum, endNum);
		
		
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
		
		String strJson = ExtHelper.getJsonFromListTime(payListRtn.size(), userList, "");
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
        usersService.addUsers(users); 
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
		boolean isSuccess = usersService.updateUsers(users);
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
		String userIds = request.getParameter("userIds");
		
		List<Long> idList = new ArrayList<Long>();
		idList.add(Long.valueOf(userIds));

		boolean isSuccess = usersService.deleteUsers(idList);
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write("{success:" + isSuccess + "}");
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
	public ActionForward getUsersById(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		response.setContentType("text/json;charset=UTF-8");
		Long usersId = Long.parseLong(request.getParameter("usersId"));
		Users users = usersService.getUsers(usersId);
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
   