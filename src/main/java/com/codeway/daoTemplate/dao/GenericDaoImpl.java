package com.codeway.daoTemplate.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.codeway.daoTemplate.utils.TemplateDataSource;
import com.codeway.daoTemplate.utils.TemplateLogger;

/**
* Template Class for Generic Dao Pattern implementation 
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
public abstract class GenericDaoImpl<Pk extends Serializable, Entity>{

//	Logger logger = Logger.getLogger(this.getClass());
	
	TemplateDataSource dataSource;
	Class<Entity> type;
	String tableName;
	String pkColumn;
	
	Map<String, Field> columnMap;
	
	public GenericDaoImpl(Class<Entity> type, TemplateDataSource dataSource) {
		
		this.type = type;
		this.dataSource = dataSource;
		
		if(type.isAnnotationPresent(Table.class)){
			tableName = type.getAnnotation(Table.class).name();
		}
		else tableName = type.getSimpleName();
		
		columnMap = new HashMap<>();
		for(Field f : type.getDeclaredFields()){
			
			if(f.isAnnotationPresent(Transient.class)) continue;
			
			String columnName = f.getName();
			if(f.isAnnotationPresent(Column.class))
				columnName = f.getAnnotation(Column.class).name();
			
			columnMap.put(columnName, f);
			
			if(f.isAnnotationPresent(Id.class)){
				pkColumn = columnName;
			}
		}
	}
	public Entity get(Pk id) throws Exception{
		
		if(pkColumn ==null)
			throw new Exception("Entity does\'t have @Id attribute");
		
		Connection con = dataSource.getConnection();
		
		PreparedStatement psmt = con.prepareStatement("select * from "+tableName+" where "+pkColumn+" =?");
		psmt.setObject(1, id);
		
		ResultSet rs = psmt.executeQuery();
		List<Entity> list = getEntitiesFromResultSet(rs);
		
		con.close();
		
		return list.isEmpty() ?null : list.get(0);
	}
	
	public List<Entity> getAll() throws Exception{
		
		return getList("", new Object[]{});
	}
	
	public Entity getSingleEntity(String whereCondition, Object... values) throws Exception{
		List<Entity> list = getList(whereCondition, values);
		if(!list.isEmpty()) return list.get(0);
		
		return null;
	}
	
	public List<Entity> getList(String whereCondition, Object... values) throws Exception{
		
		if(whereCondition ==null || values ==null) return new ArrayList<>();
		if(!whereCondition.isEmpty() && !whereCondition.startsWith("where")){
			whereCondition =" where "+whereCondition;
		}

		return getListFromQuery("select * from "+tableName+whereCondition, values);
	}
	
	public List<Entity> getListFromQuery(String query, Object... values) throws Exception{
		
		if(query ==null || query.isEmpty() || values ==null) return new ArrayList<>();

		Connection con = dataSource.getConnection();
		
		TemplateLogger.info("query : "+query);
		PreparedStatement psmt = con.prepareStatement(query);
		for(int i=0; i< values.length; i++){
			psmt.setObject(i+1, values[i]);
			TemplateLogger.info("values["+i+"] "+values[i]);
		}
		ResultSet rs = psmt.executeQuery();
		List<Entity> list = getEntitiesFromResultSet(rs);
		
		con.close();
		return list;
	}
	
	public Entity save(Entity entity) throws Exception
	{
		Connection conn = dataSource.getConnection();
		
		StringBuffer query =new StringBuffer("insert into "+tableName+" (");
		StringBuffer params = new StringBuffer(" values(");
		
		List<Object> paramVals = new LinkedList<>();
		
		for(String column : columnMap.keySet()){
			
			if(column.equals(pkColumn)) continue;
			
			query.append(column).append(",");
			params.append("?,");
			Field f = columnMap.get(column);
			
			paramVals.add(getFieldValue(entity, f));
		}
		query.deleteCharAt(query.length()-1).append(") ");
		params.deleteCharAt(params.length()-1).append(")");
		
		TemplateLogger.info(query.toString() + params.toString());
		PreparedStatement psmt = conn.prepareStatement(query.toString() + params.toString(), 
				Statement.RETURN_GENERATED_KEYS);
		
		for(int i=0; i< paramVals.size(); i++){
			psmt.setObject(i+1, paramVals.get(i));
			TemplateLogger.info("params value "+i+" = "+paramVals.get(i));
		}
		
		psmt.executeUpdate();
		
		if(pkColumn !=null){
			ResultSet generatedKeys = psmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				
				Field f = columnMap.get(pkColumn);
				setFieldValue(entity, f, generatedKeys.getObject(1)); // set generated primary key
	        }
	        else {
	            throw new SQLException("Creating Entity failed, no ID obtained.");
	        }
		}
		
		dataSource.closeConnection(conn);
		return entity;
	}

	public Entity update(Entity entity) throws Exception
	{
		if(pkColumn ==null)
			throw new Exception("Entity does\'t have @Id attribute");
		
		return update(entity, pkColumn+" = ?", new Object[]{getFieldValue(entity, columnMap.get(pkColumn))});
	}
	
	public Entity update(Entity entity, String whereCondition, Object... values) throws Exception
	{
		Connection conn = dataSource.getConnection();
		
		StringBuffer query =new StringBuffer("update "+tableName +" set ");
		
		List<Object> paramVals = new LinkedList<>();
		
		for(String column : columnMap.keySet()){
			
			query.append(column+" = ? ,");
			paramVals.add(getFieldValue(entity, columnMap.get(column)));
		}
		paramVals.addAll(Arrays.asList(values));
		
		query.deleteCharAt(query.length()-1).append(" where "+whereCondition);
		
//		paramVals.add(getFieldValue(entity, columnMap.get(pkColumn)));
		TemplateLogger.info(query.toString());
		PreparedStatement psmt = conn.prepareStatement(query.toString());
		for(int i=0; i< paramVals.size(); i++){
			psmt.setObject(i+1, paramVals.get(i));
		}
		
		psmt.executeUpdate();
		dataSource.closeConnection(conn);
		return entity;
	}

	public void remove(Pk id) throws Exception{
		if(pkColumn ==null)
			throw new Exception("Entity does\'t have @Id attribute");
		remove(pkColumn+" = ? ", new Object[]{id});
	}
	
	public void remove(String whereCondition, Object... values) throws Exception{
		
		if(values ==null || values.length ==0 || whereCondition==null || whereCondition.isEmpty()) 
			return;
		
		Connection conn = dataSource.getConnection();
		
		String query ="delete from "+tableName+" where "+whereCondition;
		PreparedStatement psmt = conn.prepareStatement(query);
		
		for(int i=0; i< values.length; i++){
			psmt.setObject(i+1, values[i]);
		}
		psmt.executeUpdate();
		dataSource.closeConnection(conn);
	}
	
	
	private List<Entity> getEntitiesFromResultSet(ResultSet rs) throws Exception{
		List<Entity> list = new LinkedList<>();
		
		while(rs.next()){
			
			Entity e = type.newInstance();
			for(String column : columnMap.keySet()){
				Field f = columnMap.get(column);
				Object val = rs.getObject(column);
				setFieldValue(e, f, val);
			}
			list.add(e);
		}
		return list;
	}
	
	private Object getFieldValue(Entity e, Field f) throws Exception{
		String fName  = f.getName(); 
		Method m = type.getMethod("get"+ fName.substring(0, 1).toUpperCase() + fName.substring(1));
		
		Object val = m.invoke(e);
		if(f.getType().isEnum() && val!=null)
			return val.toString();
		else return val;
	}
	private void setFieldValue(Entity e, Field f, Object val) throws Exception{
		String fName  = f.getName(); 
		if(val ==null) return;
		
//		TemplateLogger.info(fName+" type : "+f.getType()+" , db value type : "+val.getClass());
		
		Method m = type.getMethod("set"+ fName.substring(0, 1).toUpperCase() + fName.substring(1), f.getType());
		
		if(f.getType().isEnum() && val instanceof String){
			val = f.getType().getMethod("valueOf", String.class).invoke(null, val);
			TemplateLogger.debug("setting enum value "+val);
			m.invoke(e, val);
		}
		else if(f.getType().equals(Boolean.class) && val instanceof Number)
			m.invoke(e, ((int)val) >0 );
		else if(f.getType().equals(Integer.class) && val instanceof Long)
			m.invoke(e, ((Long)val).intValue());
		else if(f.getType().equals(Long.class) && val instanceof Integer){
			m.invoke(e, (int)val+0l);
		}
		else
			m.invoke(e, val);
	}
	
}
