/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeway.daoTemplate.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
*
* @author Abhishek Pandey
*/
@Table(name="users")
public class User {
    
	@Id
	Integer id;
	
	String name;
	String email;
	
    String mobile;
    
    // this value will not be stored in table
	@Transient
    String tmpData;
    
    @Column(name="created_date")
    Timestamp created;
    
    @Column(name="updated_date")
    Timestamp modified;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTmpData() {
		return tmpData;
	}

	public void setTmpData(String tmpData) {
		this.tmpData = tmpData;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getModified() {
		return modified;
	}

	public void setModified(Timestamp modified) {
		this.modified = modified;
	}
  
}

