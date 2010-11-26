package com.scholers.account.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.scholers.account.bean.PayType;
import com.scholers.account.dao.PMF;

/**
 * 实例化
 * @author jill
 *
 */
public class PayTypeSingle {

	private static PayTypeSingle payTypeInstance = new PayTypeSingle();
	private static Map<Long, PayType> rtnMap = null;
	
	private PayTypeSingle() {
		
	}
	
	public static PayTypeSingle getInstance() {
		return payTypeInstance;
	}
	
	/**
	 * 获取所有数据
	 * @return
	 */
	public Map<Long, PayType> reloadAllData () {
		rtnMap = new HashMap<Long, PayType>();
		
		List<PayType> accountList2 = new ArrayList<PayType>();
		List<PayType> accountList = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			pm.setDetachAllOnCommit(true);
			query = pm.newQuery(PayType.class);
			accountList = (List<PayType>) query.execute();
			accountList2.addAll(accountList);
			if (accountList2 == null) {
				return new HashMap<Long, PayType>();
			} else {
				for(PayType payType : accountList2){
					rtnMap.put(payType.getId(), payType);
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
	 * @param key
	 * @return
	 */
	public PayType getPayType (Long key) {
		if(rtnMap == null) {
			reloadAllData();
		}
		return rtnMap.get(key);
	}
	
	/**
	 * 获取支付列表
	 * @return
	 */
	public List<PayType> getPayTypeList(String email) {
		if(email == null || email.equals("")) {
			return new ArrayList<PayType>();
		}
		List<PayType> payTypeList = new ArrayList<PayType>();
		if(rtnMap == null) {
			reloadAllData();
		}
		Set<Map.Entry<Long, PayType>> set = rtnMap.entrySet();
		for(Map.Entry<Long, PayType> entry: set) {
			if(email.equals(entry.getValue().getEmail())) {
				payTypeList.add(entry.getValue());
			}
		}
		return payTypeList;
	}
	
	
	
}
