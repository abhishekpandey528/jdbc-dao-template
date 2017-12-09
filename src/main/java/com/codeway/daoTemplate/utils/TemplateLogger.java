package com.codeway.daoTemplate.utils;

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
public class TemplateLogger {

	public static boolean shouldLog =  true;//TemplateConfiguration.getBoolean("logging");
	
	public static void info(String msg){
		if(shouldLog) System.out.println(msg);
	}
	
	public static void debug(String msg){
		if(shouldLog) System.out.println(msg);
	}
	
	public static void error(String msg){
		if(shouldLog) System.err.println(msg);
	}
}
