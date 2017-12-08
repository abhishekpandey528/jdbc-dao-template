package com.codeway.daoTemplate.dao;

import java.util.List;

import com.codeway.daoTemplate.model.User;

/**
*
* @author Abhishek Pandey
*/
public interface IUserDao extends IGenericDao<Integer, User>{
	
	/*
	 * Basic crud operation will be inherited from IGenericDao
	 */
	
	/*
	 * Additional functionalities
	 */
	List<User> searchUsersByName(String name) throws Exception;
	User getUserByMobileNo(String mobile)throws Exception;
}
