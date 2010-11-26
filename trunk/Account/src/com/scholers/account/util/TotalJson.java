package com.scholers.account.util;

import java.util.List;

/**
 * 
 * @author jill
 *
 */
public class TotalJson {
	private long results;
	private List<Object> items;
	/**
	 * 收入/支出总数
	 */
	private String strTotalMoney;
	public String getStrTotalMoney() {
		return strTotalMoney;
	}

	public void setStrTotalMoney(String strTotalMoney) {
		this.strTotalMoney = strTotalMoney;
	}

	public List<Object> getItems() {
		return items;
	}

	public void setItems(List<Object> items) {
		this.items = items;
	}

	public long getResults() {
		return results;
	}

	public void setResults(long results) {
		this.results = results;
	}
}
