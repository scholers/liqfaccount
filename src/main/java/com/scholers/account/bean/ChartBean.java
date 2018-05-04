package com.scholers.account.bean;

/**
 * 统计图对象
 * @author weique.lqf
 *
 */
public class ChartBean {
	/**
	 * 
	 * @param typeName
	 * @param totalY
	 */
	public ChartBean(String typeName, Float totalY) {
		this.typeName = typeName;
		this.totalY = totalY;
	}
	//类型
	private String typeName;
	//总计
	private Float totalY;
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Float getTotalY() {
		return totalY;
	}
	public void setTotalY(Float totalY) {
		this.totalY = totalY;
	}
}
