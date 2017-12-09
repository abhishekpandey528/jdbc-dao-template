package com.codeway.daoTemplate.sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.codeway.daoTemplate.utils.TemplateDataSource;

/*
*
* @Author Abhishek.Pandey
*/
public class MyDataSource  implements TemplateDataSource{

	
	@Override
	public Connection getConnection() throws Exception {

		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?zeroDateTimeBehavior=convertToNull",
    		  "root","root");
	}

	@Override
	public void closeConnection(Connection con) {
		
		if(con !=null)
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
	}

}
