package com.codeway.daoTemplate.sample;

import com.codeway.daoTemplate.dao.GenericDaoImpl;
//import com.codeway.daoTemplate.utils.TemplateLogger;
//import com.codeway.daoTemplate.utils.PooledDataSource;

public class UserDaoImpl extends GenericDaoImpl<Integer, User> {

	public UserDaoImpl() {
		super(User.class, new MyDataSource());

//		super(User.class, PooledDataSource.getInstance());
//		TemplateLogger.shouldLog =false;
	}
	
	public User getUserByMobileNo(String mobile) throws Exception {
		 return getSingleEntity("mobile=?", mobile);
	}

//	public List<User> searchUsersByName(String name) throws Exception {
//		return getList("name like ?", "%"+name+"%");
//	}
//	private static UserDaoImpl instance;
//	
//	public static UserDaoImpl getInstance() {
//		if(instance ==null){
//			synchronized (UserDaoImpl.class) {
//	            if(instance == null){
//	                instance = new UserDaoImpl();
//	            }
//	        }
//		}
//		return instance;
//	}
	
	public static void main(String[] args) throws Exception{

		UserDaoImpl userDao = new UserDaoImpl();
		
		User user = new User();
		user.setEmail("test@abc1.com");
		user.setName("user 2 ");
		user.setMobile("999999998");
		
		userDao.save(user);
		
		User existing = userDao.getUserByMobileNo("999999998");
		
		System.out.println(existing.getName());
		
	}
}
