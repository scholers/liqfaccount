package com.scholers.account.bean;

/**
 * ͳ��ͼ����
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
	//����
	private String typeName;
	//�ܼ�
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
