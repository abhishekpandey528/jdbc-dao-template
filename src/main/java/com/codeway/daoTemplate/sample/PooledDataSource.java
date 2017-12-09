/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeway.daoTemplate.sample;

import java.sql.Connection;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import com.codeway.daoTemplate.utils.TemplateConfiguration;
import com.codeway.daoTemplate.utils.TemplateDataSource;

/**
 *
 * @author Abhishek Pandey
 *          <br>
 *         Copyright (c) Abhishek Pandey
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br><br>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <br><br>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
public class PooledDataSource implements TemplateDataSource{

	DataSource source;

	public void initConnectionPool() throws Exception{
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
    public Connection getConnection()throws Exception
    {
    	if(source ==null)
    		initConnectionPool();
    	
    	Connection con =  source.getConnection();
//    	con.createStatement().executeQuery("SET NAMES utf8mb4");
    	return con;
        
    }
    
    public void closeConnection(Connection con)
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

    public void closePool() {
		if(source !=null)
			source.close(true);
	}
    
    private static PooledDataSource instance;
	
	public static PooledDataSource getInstance() {
		if(instance ==null){
			synchronized (PooledDataSource.class) {
	            if(instance == null){
	                instance = new PooledDataSource();
	            }
	        }
		}
		return instance;
	}

	private PooledDataSource() {}
}
