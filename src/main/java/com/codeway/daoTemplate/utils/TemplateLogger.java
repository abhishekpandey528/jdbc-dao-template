package com.codeway.daoTemplate.utils;
/*
*
* @Author Abhishek.Pandey
*/
public class TemplateLogger {

	static boolean shouldLog =  TemplateConfiguration.getBoolean("logging");
	
	public static void info(String msg){
		System.out.println(msg);
	}
	
	public static void debug(String msg){
		System.out.println(msg);
	}
	
	public static void error(String msg){
		System.err.println(msg);
	}
}
