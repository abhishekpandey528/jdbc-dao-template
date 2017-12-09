# jdbc-dao-template

A light (only 17 KB Jar) JDBC Dao Template.
Without using Hibernate one can use Generic Dao Pattern with help of this project.

Developer can achieve jdbc level performance (much faster than Hibernate) with jdbc-dao-template.

JPA annotations are used (Currently only @Table , @Column, @Id, @Transient annotations are supported)

## Getting Started

Add ** build/daoTemplate.jar**  and ** mysql-connector-java**

Create modal class (e.g : User.java)

```
@Table(name="users")
public class User {
    
	@Id
	Integer id;
	
	String name;
    String mobile;
    
	@Transient
    String tmpData; // this value will not be stored in table
    
    @Column(name="created_date") // if variable name and column name are different
    Timestamp created;
    
    // getters & setters
}
```

Implement **TemplateDataSource** for providing sql connections  JDBC Data Source (You can use pooling as well , see **com.codeway.daoTemplate.sample.PooledDataSource** )

```
public class MyDataSource  implements TemplateDataSource{

	
	public Connection getConnection() throws Exception {

		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb","root","root");
	}

	public void closeConnection(Connection con) {
		if(con !=null)
			try { con.close();
			} catch (SQLException e) {e.printStackTrace();}
	}

}

``` 

Create Dao class (e.g : UserDaoImpl.java)

```
public class UserDaoImpl extends GenericDaoImpl<Integer, User> {

  /* all basic CRUD will be available from parent*/
	public UserDaoImpl() {
		super(User.class, new MyDataSource());
	}
	
	/* Define additional method */
	public User getUserByMobileNo(String mobile) throws Exception {
		 return getSingleEntity("mobile=?", mobile);
	}
	
	public static void main(String[] args) throws Exception{

		UserDaoImpl userDao = new UserDaoImpl();
		
		User user = new User();
		user.setName("user 2 ");
		
		userDao.save(user);
		
		User existing = userDao.getUserByMobileNo("999999998");
		
		System.out.println(existing.getName());
		
	}

```
That's it.

## Authors

 **Abhishek Pandey** - [abhishekpandey528](https://github.com/abhishekpandey528)

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE.md](LICENSE.md) file for details
