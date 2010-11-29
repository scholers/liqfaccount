package com.scholers.account.business;

import java.util.List;

import com.scholers.account.bean.Book;
import com.scholers.account.bean.BookType;

public interface InserviceIntf {
	/**
	 * ��ѯ���������б�
	 * @return ���������б�
	 */
	public List<BookType> getBookTypes(String email);
		
	/**
	 * ��ѯָ����������
	 * @param bookTypeId ��������id
	 * @return �������Ͷ���
	 */
	public BookType getBookType(Long bookTypeId);
	
	/**
	 * ������������
	 * @param bookType 
	 * @return  ���ӳɹ�����true�����򷵻�false
	 */
	public Long addBookType(BookType bookType);
	/**
	 * ������������
	 * @param bookType �������Ͷ���
	 * @return ���ӳɹ�����true�����򷵻�false
	 */
	public boolean updateBookType(BookType bookType);

	/**
	 * ɾ����������
	 * @param bookTypeId ��������id
	 * @return ɾ���ɹ�����true�����򷵻�false
	 */
	public boolean deleteBookType(Long bookTypeId);
	/**
	 * ��ѯ�����б�
	 * @return �����б�
	 */
	public List<Book> getBooks(String email);
	/**
	 * ��ѯ��������
	 * @param bookId id
	 * @return 
	 */
	public Book getBook(Long bookId);
	/**
	 * ��������
	 * @param book �������
	 * @return ���ӳɹ�����true�����򷵻�false
	 */
	public Long addBook(Book book);
	/**
	 * ����������Ϣ
	 * @param book �������
	 * @return ��������ɹ�����true�����򷵻�false
	 */
	public boolean updateBook(Book book);
	/**
	 * ɾ������
	 * @param bookId ����id
	 * @return ɾ������ɹ�����true�����򷵻�false
	 */
	public boolean deleteBook(Long bookId);
	/**
	 * ����ɾ��������Ϣ
	 * @param bookIds ����id�б�
	 * @return ����ɾ������ɹ�����true�����򷵻�false
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
	 * ����ʱ����Ϣ��ѯ����
	 * @param startNum
	 * @param endNum
	 * @param email
	 * @param times
	 * @param times2
	 * @return
	 */
	public List<Book> getInByTime(int startNum, int endNum, String email, String times, String times2);
	


	/**
	 * �õ��¶�������
	 * @param email
	 * @return
	 */
	public Float getInPay(String email);
	
	/**
	 * �����������
	 * @param email
	 * @return
	 */
	public Float getSumInY(String email);
	
	
	/**
	 * �õ���¼����
	 * @param email
	 * @param times
	 * @param times2
	 * @return
	 */
	public int getInSize(String email, String times, String times2) ;
}
