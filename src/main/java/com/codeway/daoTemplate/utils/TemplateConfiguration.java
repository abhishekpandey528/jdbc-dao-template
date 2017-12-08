package com.codeway.daoTemplate.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
*
* @author Abhishek Pandey
*/
public class TemplateConfiguration{

	private static Map<String, String> map;
	

	public static String getString(String key) throws Exception{
		if(map ==null) loadProperties();
		return map.get(key);
	}
	
	public static boolean getBoolean(String key) {
		try{
			String val = getString(key); 
			return val !=null && val.equalsIgnoreCase("true");
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static void loadProperties() throws Exception{

		map = new HashMap<>();
		InputStream is = TemplateConfiguration.class.getClassLoader().getResourceAsStream("config.properties");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line =null;
		while( (line=br.readLine()) !=null){
//			System.out.println(line);
			if(line.trim().isEmpty() || !line.contains("=")) continue;
			
			String[] parts = line.split("=");
			map.put(parts[0].trim(), parts[1].trim());
		}
	}
	
}
