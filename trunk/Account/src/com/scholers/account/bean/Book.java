package com.scholers.account.bean;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Book {
	
	public Book() {
		
	}
	
	
	public Book(User author, String notes, Date useDate, Date createDate) {
		//this.author = author;
		this.notes = notes;
		this.useDate = useDate;
		this.createDate = createDate;
		this.email = author.getEmail();
	}
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private Long payId;
	
	public Long getPayId() {
		return payId;
	}
	public void setPayId(Long payId) {
		this.payId = payId;
	}
	@Persistent
	private String bookName;
	@Persistent
	private String author;
	@Persistent
	private Long bookTypeId;
	@Persistent
	private String typeName;
	@Persistent
	private Float price;
	@Persistent
	private String brief;
	
	@Persistent
	private String email;
	
	@Persistent
	private Date useDate;
	
	@Persistent
	private String notes;
	
	@Persistent
	private Date createDate;
	

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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Long getBookTypeId() {
		return bookTypeId;
	}
	public void setBookTypeId(Long bookTypeId) {
		this.bookTypeId = bookTypeId;
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
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
