/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeway.daoTemplate.utils;

import java.sql.Connection;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 *
 * @author Abhishek Pandey
 */
public class TemplateDataSource {

	static DataSource source;

	public static void initConnectionPool() throws Exception{
		 PoolProperties p = new PoolProperties();
         p.setUrl(TemplateConfiguration.getString("db.connection.url"));
         p.setDriverClassName("com.mysql.jdbc.Driver");
         p.setUsername(TemplateConfiguration.getString("db.connection.username"));
         p.setPassword(TemplateConfiguration.getString("db.connection.checksum"));
         p.setTestWhileIdle(false);
         p.setTestOnBorrow(true);
         p.setValidationQuery("SELECT 1");
         p.setTestOnReturn(false);
         p.setValidationInterval(30000);
         p.setTimeBetweenEvictionRunsMillis(30000);
         p.setMaxActive(50);
         p.setInitialSize(5);
         p.setMaxWait(10000);
         p.setRemoveAbandonedTimeout(60);
         p.setMinEvictableIdleTimeMillis(30000);
         p.setMinIdle(10);
         p.setLogAbandoned(true);
         p.setRemoveAbandoned(true);
         
         p.setJdbcInterceptors(
                 "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
                 + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer;"
                 + "org.apache.tomcat.jdbc.pool.interceptor.ResetAbandonedTimer");
         source = new DataSource();
         source.setPoolProperties(p);
		
	}
    public static Connection getConnection()throws Exception
    {
    	if(source ==null)
    		initConnectionPool();
    	
    	Connection con =  source.getConnection();
    	con.createStatement().executeQuery("SET NAMES utf8mb4");
    	return con;
        
    }
    
    public static void closeConnection(Connection con)
    {
        try
        {
            if(con!=null)
                con.close();
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
    }

    public static void closePool() {
		if(source !=null)
			source.close(true);
	}
}
