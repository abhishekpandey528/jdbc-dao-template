package com.codeway.daoTemplate;

import com.codeway.daoTemplate.dao.IUserDao;
import com.codeway.daoTemplate.dao.impl.UserDaoImpl;
import com.codeway.daoTemplate.model.User;

public class Test {

	public static void main(String[] args) throws Exception{

		IUserDao userDao = UserDaoImpl.getInstance();
		
		User user = new User();
		user.setEmail("test@abc.com");
		user.setName("abhishek pandey");
		user.setMobile("999999999");
		
		userDao.save(user);
		
		User existing = userDao.getUserByMobileNo("999999999");
		
		System.out.println(existing.getName());
		
	}
}
