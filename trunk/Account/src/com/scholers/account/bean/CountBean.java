package com.scholers.account.bean;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

/**
 * 统计使用的对象
 * @author weique.lqf
 *
 */
public class CountBean {
	public CountBean() {
		
	}
	public CountBean(User author, String notes, Date useDate, Date createDate) {
		this.user = author;
		this.notes = notes;
		this.useDate = useDate;
		this.createDate = createDate;
		this.email = author.getEmail();
	}
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private User user;
	
	@Persistent
	private String email;
	@Persistent
	private String accName;
	@Persistent
	private String accType;
	@Persistent
	private String author;
	@Persistent
	private Long accTypeId;
	@Persistent
	private String typeName;
	@Persistent
	private Float price;
	@Persistent
	private String brief;
	
	@Persistent
	private Date useDate;
	
	@Persistent
	private String notes;
	
	@Persistent
	private Date createDate;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getUseDate() {
		return useDate;
	}
	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Long getAccTypeId() {
		return accTypeId;
	}
	public void setAccTypeId(Long accTypeId) {
		this.accTypeId = accTypeId;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
}
