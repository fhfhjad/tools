package com.dewly.log;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang.exception.ExceptionUtils;

public class LogUtil {
	public static String getErrorInfoFromException(Exception e) {  
        try {  
            StringWriter sw = new StringWriter();  
            PrintWriter pw = new PrintWriter(sw);  
            e.printStackTrace(pw);  
            return "\r\n" + sw.toString() + "\r\n";  
        } catch (Exception e2) {  
            return "bad getErrorInfoFromException";  
        }  
    } 
	
	public static String getErrorInfo(Exception e){
		
		return ExceptionUtils.getFullStackTrace(e);
		
	}
	
	public static void main(String[] args) {
		try {
//			Exception e = new Exception("error");
//			System.out.println(getErrorInfo(e));
			int a = 1/0;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
			System.out.println(getErrorInfo(e));
			System.out.println(getErrorInfoFromException(e));
		}
		
	}
	
	
	
}
