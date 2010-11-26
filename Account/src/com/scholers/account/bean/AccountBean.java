package com.scholers.account.bean;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class AccountBean {
	
	public AccountBean() {
		
	}

	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long accountId;

	@Persistent
	private User author;
	
	@Persistent
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	@Persistent
	private String content;

	@Persistent
	private Date useDate;
	
	@Persistent
	private String useType;
	
	@Persistent
	private String useFang;
	
	@Persistent
	private Float sueAmt;
	
	@Persistent
	private String faxId;
	
	@Persistent
	private String orgId;
	
	@Persistent
	private String notes;
	
	@Persistent
	private Date createDate;

	public AccountBean(User author, String content, Date useDate, Date createDate) {
		this.author = author;
		this.content = content;
		this.useDate = useDate;
		this.createDate = createDate;
		this.notes = content;
		this.email = author.getEmail();
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getUseDate() {
		return useDate;
	}

	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}

	public String getUseType() {
		return useType;
	}

	public void setUseType(String useType) {
		this.useType = useType;
	}

	public String getUseFang() {
		return useFang;
	}

	public void setUseFang(String useFang) {
		this.useFang = useFang;
	}

	public Float getSueAmt() {
		return sueAmt;
	}

	public void setSueAmt(Float sueAmt) {
		this.sueAmt = sueAmt;
	}

	public String getFaxId() {
		return faxId;
	}

	public void setFaxId(String faxId) {
		this.faxId = faxId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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

	
}
