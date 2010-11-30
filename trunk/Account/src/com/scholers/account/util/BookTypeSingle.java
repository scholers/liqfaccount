package com.scholers.account.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.scholers.account.bean.BookType;
import com.scholers.account.dao.PMF;

/**
 * 
 * @author jill
 *
 */
public class BookTypeSingle {
	private static BookTypeSingle bookTypeInstance = new BookTypeSingle();
	private static Map<Long, BookType> rtnMap = null;

	private BookTypeSingle() {

	}

	public static BookTypeSingle getInstance() {
		return bookTypeInstance;
	}

	/**
	 * 获取所有数据
	 * 
	 * @return
	 */
	public Map<Long, BookType> reloadAllData() {
		rtnMap = new HashMap<Long, BookType>();

		List<BookType> accountList2 = new ArrayList<BookType>();
		List<BookType> accountList = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			pm.setDetachAllOnCommit(true);
			query = pm.newQuery(BookType.class);
			accountList = (List<BookType>) query.execute();
			accountList2.addAll(accountList);
			if (accountList2 == null ||accountList2.isEmpty()) {
				return new HashMap<Long, BookType>();
			} else {
				for (BookType bookType : accountList2) {
					rtnMap.put(bookType.getId(), bookType);
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return rtnMap;
	}

	/**
	 * 根据KEY得到支出类型
	 * 
	 * @param key
	 * @return
	 */
	public BookType getBookType(Long key) {
		if (rtnMap == null) {
			reloadAllData();
		}
		return rtnMap.get(key);
	}

	/**
	 * 获取收入类型列表
	 * 
	 * @return
	 */
	public List<BookType> getBookTypeList(String email) {
		if (email == null || email.equals("")) {
			return new ArrayList<BookType>();
		}
		List<BookType> bookTypeList = new ArrayList<BookType>();
		if (rtnMap == null) {
			reloadAllData();
		}
		Set<Map.Entry<Long, BookType>> set = rtnMap.entrySet();
		for (Map.Entry<Long, BookType> entry : set) {
			if (email.equals(entry.getValue().getEmail())) {
				bookTypeList.add(entry.getValue());
			}
		}
		return bookTypeList;
	}

}
