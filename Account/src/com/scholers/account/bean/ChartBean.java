package com.scholers.account.bean;

/**
 * 统计图对象
 * @author weique.lqf
 *
 */
public class ChartBean {
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
