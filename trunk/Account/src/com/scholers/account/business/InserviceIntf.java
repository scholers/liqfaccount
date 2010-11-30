package com.scholers.account.business;

import java.util.List;

import com.scholers.account.bean.Book;
import com.scholers.account.bean.BookType;

public interface InserviceIntf {
	/**
	 * ��ѯ���������б�
	 * @return ���������б�
	 */
	 List<BookType> getBookTypes(String email);
		
	/**
	 * ��ѯָ����������
	 * @param bookTypeId ��������id
	 * @return �������Ͷ���
	 */
	 BookType getBookType(Long bookTypeId);
	
	/**
	 * ������������
	 * @param bookType 
	 * @return  ���ӳɹ�����true�����򷵻�false
	 */
	 Long addBookType(BookType bookType);
	/**
	 * ������������
	 * @param bookType �������Ͷ���
	 * @return ���ӳɹ�����true�����򷵻�false
	 */
	 boolean updateBookType(BookType bookType);

	/**
	 * ɾ����������
	 * @param bookTypeId ��������id
	 * @return ɾ���ɹ�����true�����򷵻�false
	 */
	 boolean deleteBookType(Long bookTypeId);
	/**
	 * ��ѯ�����б�
	 * @return �����б�
	 */
	 List<Book> getBooks(String email);
	/**
	 * ��ѯ��������
	 * @param bookId id
	 * @return 
	 */
	 Book getBook(Long bookId);
	/**
	 * ��������
	 * @param book �������
	 * @return ���ӳɹ�����true�����򷵻�false
	 */
	 Long addBook(Book book);
	/**
	 * ����������Ϣ
	 * @param book �������
	 * @return ��������ɹ�����true�����򷵻�false
	 */
	 boolean updateBook(Book book);
	/**
	 * ɾ������
	 * @param bookId ����id
	 * @return ɾ������ɹ�����true�����򷵻�false
	 */
	 boolean deleteBook(Long bookId);
	/**
	 * ����ɾ��������Ϣ
	 * @param bookIds ����id�б�
	 * @return ����ɾ������ɹ�����true�����򷵻�false
	 */
	 boolean deleteBooks(List<Long> bookIds);

	
	/**
	 * 
	 * @param email
	 * @return
	 */
	 List<Book> getBookByY(String email);
	/**
	 * 
	 * @param UserId
	 * @return
	 */
	 List<Book> getBookByYP(String email);
	/**
	 * 
	 * @param email
	 * @return
	 */
	  List<Book> getInByM(String email);
	/**
	 * 
	 * @param email
	 * @return
	 */
	  List<Book> getBookByM(String email);
	
	/**
	 * 
	 * @return
	 */
	  int getDay();
	/**
	 * ����ʱ����Ϣ��ѯ����
	 * @param startNum
	 * @param endNum
	 * @param email
	 * @param times
	 * @param times2
	 * @return
	 */
	 List<Book> getInByTime(int startNum, int endNum, String email, String times, String times2);
	


	/**
	 * �õ��¶�������
	 * @param email
	 * @return
	 */
	 Float getInPay(String email);
	
	/**
	 * �����������
	 * @param email
	 * @return
	 */
	 Float getSumInY(String email);
	
	
	/**
	 * �õ���¼����
	 * @param email
	 * @param times
	 * @param times2
	 * @return
	 */
	 int getInSize(String email, String times, String times2) ;
}
