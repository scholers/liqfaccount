package com.scholers.account.business.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.stereotype.Component;

import com.scholers.account.bean.Book;
import com.scholers.account.bean.BookType;
import com.scholers.account.bean.Users;
import com.scholers.account.business.InserviceIntf;
import com.scholers.account.dao.PMF;
import com.scholers.account.util.BookTypeSingle;
import com.scholers.account.util.ComUtil;
import com.scholers.account.util.DBUtil;



/**
 * 
 * @author weique.lqf
 *
 */
@Component("inService")
public class InService implements InserviceIntf{

	/**
	 * ��ѯ���������б�
	 * @return ���������б�
	 */
	public List<BookType> getBookTypes(String email){
		if (email == null) {
			return null;
		}
	
		return BookTypeSingle.getInstance().getBookTypeList(email);
	}
	/**
	 * ��ѯָ����������
	 * @param bookTypeId ��������id
	 * @return �������Ͷ���
	 */
	public BookType getBookType(Long bookTypeId){
		
		return BookTypeSingle.getInstance().getBookType(bookTypeId);
	}
	/**
	 * ������������
	 * @param bookType 
	 * @return  ���ӳɹ�����true�����򷵻�false
	 */
	public Long addBookType(BookType bookType){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Long iRtn = 0L;
		try {
			pm.makePersistent(bookType);
			iRtn = bookType.getId();
		} catch (Exception ex) {
			iRtn = -1L;
		} finally {
			pm.close();
		}
		if(iRtn > 0)
			BookTypeSingle.getInstance().reloadAllData();
		return iRtn;
	}  
	/**
	 * ������������
	 * @param bookType �������Ͷ���
	 * @return ���ӳɹ�����true�����򷵻�false
	 */
	public boolean updateBookType(BookType bookType){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		boolean isSuccess = true;
		try {
			BookType e = pm.getObjectById(BookType.class, bookType.getId());
			e.setDetail(bookType.getDetail());
			e.setTitle(bookType.getTitle());
		} catch (RuntimeException e) {
			isSuccess = false;
		} catch (Exception ex) {
			isSuccess = false;
		} finally {
			pm.close();
		}
		if(isSuccess)
			BookTypeSingle.getInstance().reloadAllData();
		return isSuccess;
	}

	/**
	 * ɾ����������
	 * @param bookTypeId ��������id
	 * @return ɾ���ɹ�����true�����򷵻�false
	 */
	public boolean deleteBookType(Long bookTypeId){
		boolean isSuccess = true;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			BookType bookType = pm
					.getObjectById(BookType.class, bookTypeId);
			pm.deletePersistent(bookType);
		} catch (RuntimeException e) {  
			isSuccess = false;
		} finally {
			pm.close();
		}
		if(isSuccess)
			BookTypeSingle.getInstance().reloadAllData();
		return isSuccess;
	}
	/**
	 * ��ѯ�����б�
	 * @return �����б�
	 */
	public List<Book> getBooks(String email){
		if (email == null || email.equals("")) {
			return null;
		}
		List<Book> accountList2 = new ArrayList<Book>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Book> accountList = null;
			pm.setDetachAllOnCommit(true);

			String queryTemplate = "", filter = "";

			queryTemplate = "email == \"%s\" ";
			filter = String.format(queryTemplate, email);
			query = pm.newQuery(Book.class, filter);
			//query.setOrdering("useDate desc");
			accountList = (List<Book>) query.execute();
			accountList2.addAll(accountList);
			
			
			if (accountList2 == null) {
				return new ArrayList<Book>();
			} else {
				List<Book> bookList = new ArrayList<Book>();
				for(Book book : accountList2) {
					String typeName = "";
					BookType bookType =  getBookType(book.getBookTypeId());
					if(bookType != null) {
						typeName = bookType.getTitle();
					}
					book.setTypeName(typeName);
					bookList.add(book);
				}
				return bookList;
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return accountList2;
	}
	/**
	 * ��ѯ��������
	 * @param bookId id
	 * @return 
	 */
	public Book getBook(Long bookId){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Book book = null;
		try {
			book = pm.getObjectById(Book.class, bookId);
		} finally {
			pm.close();
		}
		return book;
	}
	/**
	 * ��������
	 * @param book �������
	 * @return ���ӳɹ�����true�����򷵻�false
	 */
	public Long addBook(Book book){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Long bookId = 0L;
		try {
			pm.makePersistent(book);
			bookId = book.getId();
		} finally {
			pm.close();
		}
		return bookId;
	}
	/**
	 * ����������Ϣ
	 * @param book �������
	 * @return ��������ɹ�����true�����򷵻�false
	 */
	public boolean updateBook(Book book){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		boolean isSuccess = true;
		try {
			Book e = pm.getObjectById(Book.class, book.getId());
			e.setAuthor(book.getAuthor());
			e.setBookName(book.getBookName());
			e.setCreateDate(book.getCreateDate());
			e.setBookTypeId(book.getBookTypeId());
			e.setPrice(book.getPrice());
			e.setTypeName(book.getTypeName());
			e.setCreateDate(book.getCreateDate());
			e.setNotes(book.getNotes());
		
		} catch (RuntimeException e) {
			isSuccess = false;
		} catch (Exception ex) {
			isSuccess = false;
		} finally {
			pm.close();
		}

		return isSuccess;
	}
	/**
	 * ɾ������
	 * @param bookId ����id
	 * @return ɾ������ɹ�����true�����򷵻�false
	 */
	public boolean deleteBook(Long bookId){
		boolean isSuccess = true;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Book book = pm
					.getObjectById(Book.class, bookId);
			pm.deletePersistent(book);
		} catch (RuntimeException e) {  
			isSuccess = false;
		} finally {
			pm.close();
		}
		return isSuccess;
	}
	/**
	 * ����ɾ��������Ϣ
	 * @param bookIds ����id�б�
	 * @return ����ɾ������ɹ�����true�����򷵻�false
	 */
	public boolean deleteBooks(List<Long> bookIds){
		boolean isSuccess = true;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			
			for (int i = 0; i < bookIds.size(); i++) {
				Long bookId = (Long) bookIds.get(i);
				Book book = pm
				.getObjectById(Book.class, bookId);
				pm.deletePersistent(book);
			}
			
		} catch (Exception e) {
			
			isSuccess = false;
			e.printStackTrace();
		} finally {
			pm.close();
		}
		return isSuccess;
	}
	
	
	public Users getUsers(String username){
		Connection con = null;  
		Users user = null;
		try {
			con = DBUtil.getConnection();
			//user = dao.getUser(con,username);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(con != null){con.close();}
			} catch (SQLException e) {}
		}
		return user;
	}

	
	/**
	 * 
	 * @param email
	 * @return
	 */
	public List<Book> getBookByY(String email){
		if (email == null || email.equals("")) {
			return null;
		}
		List<Book> accountList2 = new ArrayList<Book>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Book> accountList = null;
			pm.setDetachAllOnCommit(true);

			String queryTemplate = "", filter = "";

			queryTemplate = "email == \"%s\" ";
			filter = String.format(queryTemplate, email);
			query = pm.newQuery(Book.class, filter);
			//query.setOrdering("useDate desc");
			accountList = (List<Book>) query.execute();
			accountList2.addAll(accountList);

			if (accountList2 == null) {
				return new ArrayList<Book>();
			}

			return accountList2;
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return accountList2;
		
	}
	
	/**
	 * 
	 * @param UserId
	 * @return
	 */
	public List<Book> getBookByYP(String email){
		if (email == null || email.equals("")) {
			return null;
		}
		List<Book> accountList2 = new ArrayList<Book>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Book> accountList = null;
			pm.setDetachAllOnCommit(true);

			String queryTemplate = "", filter = "";

			queryTemplate = "email == \"%s\" ";
			filter = String.format(queryTemplate, email);
			query = pm.newQuery(Book.class, filter);
			//query.setOrdering("useDate desc");
			accountList = (List<Book>) query.execute();
			accountList2.addAll(accountList);

			if (accountList2 == null) {
				return new ArrayList<Book>();
			}

			return accountList2;
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return accountList2;
		
	}
	/**
	 * 
	 * @param email
	 * @return
	 */
	public  List<Book> getInByM(String email){
		if (email == null || email.equals("")) {
			return null;
		}
		List<Book> accountList2 = new ArrayList<Book>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Book> accountList = null;
			pm.setDetachAllOnCommit(true);

			String queryTemplate = "", filter = "";

			queryTemplate = "email == \"%s\" ";
			filter = String.format(queryTemplate, email);
			query = pm.newQuery(Book.class, filter);
			//query.setOrdering("useDate desc");
			accountList = (List<Book>) query.execute();
			accountList2.addAll(accountList);

			if (accountList2 == null) {
				return new ArrayList<Book>();
			}

			return accountList2;
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return accountList2;
		
	}
	
	/**
	 * 
	 * @param email
	 * @return
	 */
	public  List<Book> getBookByM(String email){
		if (email == null || email.equals("")) {
			return null;
		}
		List<Book> accountList2 = new ArrayList<Book>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Book> accountList = null;
			pm.setDetachAllOnCommit(true);

			String queryTemplate = "", filter = "";

			queryTemplate = "email == \"%s\" ";
			filter = String.format(queryTemplate, email);
			query = pm.newQuery(Book.class, filter);
			//query.setOrdering("useDate desc");
			accountList = (List<Book>) query.execute();
			accountList2.addAll(accountList);

			if (accountList2 == null) {
				return new ArrayList<Book>();
			}

			return accountList2;
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return accountList2;
		
	}
	public  int getDay(){
		Connection con = null;
		int L = 10;
		/*try {
			con = DBUtil.getConnection();  
			con.setAutoCommit(false); 
			//L = dao.GetDay(con);
			con.commit();
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally{
			try {
				if(con != null){con.close();}
			} catch (SQLException e) {}
		}*/
		return L;  
		
	}
	/**
	 * ����ʱ����Ϣ��ѯ����
	 * @param startNum
	 * @param endNum
	 * @param email
	 * @param times
	 * @param times2
	 * @return
	 */
	public List<Book> getInByTime(int startNum, int endNum, String email, String times, String times2) {
		if (email == null || email.equals("")) {
			return null;
		}
		List<Book> accountList2 = new ArrayList<Book>();
		List<Book> accountList3 = new ArrayList<Book>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Book> accountList = null;
			pm.setDetachAllOnCommit(true);
			
			String queryTemplate = "", filter = "";
			queryTemplate = "email == \"%s\" ";
			//��ʼ����
			if(times != null && !times.equals("")) {
				queryTemplate += "&& useDate >= today";
			} 
			//��������
			if(times2 != null && !times2.equals("")) {
				queryTemplate += " && useDate <= today2";
			}
			filter = String.format(queryTemplate, email);
			query = pm.newQuery(Book.class, filter);
			if(times != null && !times.equals("")
					&& times2 != null && !times2.equals("")) {
				query.declareImports("import java.util.Date"); 
				//��ʼ����
				query.declareParameters("Date today, Date today2"); 
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
				Date today = null; 
				try {   
					today = format1.parse(times);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				} 
				//��������
				//query.declareParameters("Date today2"); 
				Date today2 = null; 
				try {   
					today2 = format1.parse(times2);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				} 
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Book>) query.executeWithArray(today, today2);
				
			} else if(times != null && !times.equals("")
					&& (times2 == null || times2.equals(""))) {
				query.declareImports("import java.util.Date"); 
				query.declareParameters("Date today"); 
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
				Date today = null; 
				try {   
					today = format1.parse(times);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				} 
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Book>) query.execute(today);
			}
			//��������
			else if(times2 != null && !times2.equals("")
					 && (times == null || times.equals(""))) {
				query.declareImports("import java.util.Date"); 
				query.declareParameters("Date today2"); 
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
				Date today2 = null; 
				try {   
					today2 = format1.parse(times2);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				}   
			
				
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Book>) query.execute(today2);
			} else {
				query.setOrdering("useDate desc,id desc");
				//query.setOrdering("useDate desc");
				accountList = (List<Book>) query.execute();
			}
			accountList2.addAll(accountList);
			if (accountList2 == null) {
				return new ArrayList<Book>();
			}
			
			if (endNum > accountList2.size()) {
				endNum = accountList2.size();
			}
			//����������
			Float tempFloat = Float.MIN_VALUE;
			for(Book book : accountList2) {
				tempFloat = ComUtil.add(tempFloat, book.getPrice());
			}
			accountList3.addAll(accountList2.subList(startNum, endNum));
			if (accountList3 == null) {
				accountList3 = new ArrayList<Book>();
			}
		
			//��ʾ���
			List<Book> payList2 = new ArrayList<Book>();
			for(Book book : accountList3) {
				String typeName = "";
				BookType bookType =  getBookType(book.getBookTypeId());
				if(bookType != null) {
					typeName = bookType.getTitle();
				}
				book.setTypeName(typeName);
				payList2.add(book);
			}
			
			Book tolPay = new Book();
			//Float price = Float.parseFloat(tempFloat);
			tolPay.setTypeName("�ܼ�");
			tolPay.setNotes("��ǰ��ѯ����������ܼƣ�");
			tolPay.setPrice(tempFloat);
			payList2.add(tolPay);
			
			return payList2;

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return accountList2;
	}
	


	/**
	 * �õ��¶�������
	 * @param email
	 * @return
	 */
	public Float getInPay(String email){
		if (email == null || email.equals("")) {
			return null;
		}
		Float totalSumPay = 0.0F;
		List<Book> accountList2 = new ArrayList<Book>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Book> accountList = null;
			pm.setDetachAllOnCommit(true);
			
			String queryTemplate = "", filter = "";
			Date date = ComUtil.getCurYearAndDate();
			if(date != null ) {
				queryTemplate = "email == \"%s\" && useDate >= start && useDate <= today";
			}
			filter = String.format(queryTemplate, email);
			
			query = pm.newQuery(Book.class, filter);
			if(date != null ) {
				//query.declareImports("import java.util.Date,java.util.Date"); 
				query.declareImports("import java.util.Date;" +"import java.util.Date" ); 
				query.declareParameters("Date start, Date today"); 
				query.setOrdering("useDate desc,id desc");
				//query.setOrdering("useDate desc");
				accountList = (List<Book>) query.execute(date, new Date());
			} else {
				query.setOrdering("useDate desc,id desc");
				//query.setOrdering("useDate desc");
				accountList = (List<Book>) query.execute();
			}
			accountList2.addAll(accountList);
			if (accountList2 == null) {
				accountList2 =  new ArrayList<Book>();
			}
		
			//��ʾ���
			
			for(Book pay : accountList2) {
				totalSumPay = ComUtil.add(pay.getPrice(), totalSumPay);
			}
			

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return totalSumPay;
	} 
	
	/**
	 * �����������
	 * @param email
	 * @return
	 */
	public Float getSumInY(String email){
		if (email == null || email.equals("")) {
			return null;
		}
		Float totalSumPay = 0.0F;
		List<Book> accountList2 = new ArrayList<Book>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Book> accountList = null;
			pm.setDetachAllOnCommit(true);
			
			String queryTemplate = "", filter = "";
			Date date = ComUtil.getCurYear();
			if(date != null ) {
				queryTemplate = "email == \"%s\" && useDate >= start && useDate <= today";
			}
			filter = String.format(queryTemplate, email);
			
			query = pm.newQuery(Book.class, filter);
			if(date != null ) {
				//query.declareImports("import java.util.Date,java.util.Date"); 
				query.declareImports("import java.util.Date;" +"import java.util.Date" ); 
				query.declareParameters("Date start, Date today"); 
				query.setOrdering("useDate desc,id desc");
				//query.setOrdering("useDate desc");
				accountList = (List<Book>) query.execute(date, new Date());
			} 
			accountList2.addAll(accountList);
			if (accountList2 == null) {
				accountList2 =  new ArrayList<Book>();
			}
		
			//��ʾ���
			
			for(Book in : accountList2) {
				totalSumPay = ComUtil.add(in.getPrice(), totalSumPay);
			}
			

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return totalSumPay;
	} 
	
	
	/**
	 * �õ���¼����
	 * @param email
	 * @param times
	 * @param times2
	 * @return
	 */
	public int getInSize(String email, String times, String times2) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {

			List<Book> accountList = null;
			pm.setDetachAllOnCommit(true);
			String queryTemplate = "", filter = "";
			queryTemplate = "email == \"%s\" ";
			//��ʼ����
			if(times != null && !times.equals("")) {
				queryTemplate += "&& useDate >= today";
			} 
			//��������
			if(times2 != null && !times2.equals("")) {
				queryTemplate += " && useDate <= today2";
			}
			filter = String.format(queryTemplate, email);
			query = pm.newQuery(Book.class, filter);
			if(times != null && !times.equals("")
					&& times2 != null && !times2.equals("")) {
				query.declareImports("import java.util.Date"); 
				//��ʼ����
				query.declareParameters("Date today, Date today2"); 
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
				Date today = null; 
				try {   
					today = format1.parse(times);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				} 
				//��������
				//query.declareParameters("Date today2"); 
				Date today2 = null; 
				try {   
					today2 = format1.parse(times2);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				} 
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Book>) query.executeWithArray(today, today2);
				
			} else if(times != null && !times.equals("")
					&& (times2 == null || times2.equals(""))) {
				query.declareImports("import java.util.Date"); 
				query.declareParameters("Date today"); 
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
				Date today = null; 
				try {   
					today = format1.parse(times);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				} 
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Book>) query.execute(today);
			}
			//��������
			else if(times2 != null && !times2.equals("")
					 && (times == null || times.equals(""))) {
				query.declareImports("import java.util.Date"); 
				query.declareParameters("Date today2"); 
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
				Date today2 = null; 
				try {   
					today2 = format1.parse(times2);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				}   
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Book>) query.execute(today2);
			} else {
				accountList = (List<Book>) pm.newQuery(query).execute();
			}
			
			if (accountList != null)
				return accountList.size();
			else
				return 0;
		} finally {
			pm.close();
		}
	}
	
}