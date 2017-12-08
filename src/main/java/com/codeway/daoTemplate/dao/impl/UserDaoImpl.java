package com.codeway.daoTemplate.dao.impl;

import java.util.List;

import com.codeway.daoTemplate.dao.IUserDao;
import com.codeway.daoTemplate.model.User;

/**
*
* @author Abhishek Pandey
*/
/*
 * Basic CRUD operation will be inherited from GenericDaoImpl class
 * 
 * This class will only have to implement additional functionalities
 */
public class UserDaoImpl extends GenericDaoImpl<Integer, User> implements IUserDao{

	private UserDaoImpl() {
		super(User.class);
	}

	@Override
	public List<User> searchUsersByName(String name) throws Exception {
		return getList("name like ?", "%"+name+"%");
	}

	@Override
	public User getUserByMobileNo(String mobile) throws Exception {
		 return getSingleEntity("mobile=?", mobile);
	}

	private static UserDaoImpl instance;
	
	public static UserDaoImpl getInstance() {
		if(instance ==null){
			synchronized (UserDaoImpl.class) {
	            if(instance == null){
	                instance = new UserDaoImpl();
	            }
	        }
		}
		return instance;
	}
}
