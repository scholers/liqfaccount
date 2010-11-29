package com.scholers.account.business;

import java.util.List;

import com.scholers.account.bean.Book;
import com.scholers.account.bean.BookType;

public interface InserviceIntf {
	/**
	 * 查询收入类型列表
	 * @return 收入类型列表
	 */
	public List<BookType> getBookTypes(String email);
		
	/**
	 * 查询指定收入类型
	 * @param bookTypeId 收入类型id
	 * @return 收入类型对象
	 */
	public BookType getBookType(Long bookTypeId);
	
	/**
	 * 增加收入类型
	 * @param bookType 
	 * @return  增加成功返回true，否则返回false
	 */
	public Long addBookType(BookType bookType);
	/**
	 * 更新收入类型
	 * @param bookType 收入类型对象
	 * @return 增加成功返回true，否则返回false
	 */
	public boolean updateBookType(BookType bookType);

	/**
	 * 删除收入类型
	 * @param bookTypeId 收入类型id
	 * @return 删除成功返回true，否则返回false
	 */
	public boolean deleteBookType(Long bookTypeId);
	/**
	 * 查询收入列表
	 * @return 收入列表
	 */
	public List<Book> getBooks(String email);
	/**
	 * 查询收入数据
	 * @param bookId id
	 * @return 
	 */
	public Book getBook(Long bookId);
	/**
	 * 增加收入
	 * @param book 收入对象
	 * @return 增加成功返回true，否则返回false
	 */
	public Long addBook(Book book);
	/**
	 * 更新收入信息
	 * @param book 收入对象
	 * @return 更新收入成功返回true，否则返回false
	 */
	public boolean updateBook(Book book);
	/**
	 * 删除收入
	 * @param bookId 收入id
	 * @return 删除收入成功返回true，否则返回false
	 */
	public boolean deleteBook(Long bookId);
	/**
	 * 成批删除收入信息
	 * @param bookIds 收入id列表
	 * @return 成批删除收入成功返回true，否则返回false
	 */
	public boolean deleteBooks(List<Long> bookIds);

	
	/**
	 * 
	 * @param email
	 * @return
	 */
	public List<Book> getBookByY(String email);
	/**
	 * 
	 * @param UserId
	 * @return
	 */
	public List<Book> getBookByYP(String email);
	/**
	 * 
	 * @param email
	 * @return
	 */
	public  List<Book> getInByM(String email);
	/**
	 * 
	 * @param email
	 * @return
	 */
	public  List<Book> getBookByM(String email);
	
	/**
	 * 
	 * @return
	 */
	public  int getDay();
	/**
	 * 按照时间信息查询收入
	 * @param startNum
	 * @param endNum
	 * @param email
	 * @param times
	 * @param times2
	 * @return
	 */
	public List<Book> getInByTime(int startNum, int endNum, String email, String times, String times2);
	


	/**
	 * 得到月度总收入
	 * @param email
	 * @return
	 */
	public Float getInPay(String email);
	
	/**
	 * 本年度总收入
	 * @param email
	 * @return
	 */
	public Float getSumInY(String email);
	
	
	/**
	 * 得到记录总数
	 * @param email
	 * @param times
	 * @param times2
	 * @return
	 */
	public int getInSize(String email, String times, String times2) ;
}
