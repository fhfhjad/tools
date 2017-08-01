package com.dewly.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {
	
	private final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";  
	
	/**
	 * 根据日期格式的字符串2017-07-27 12:00:00和默认的格式（yyyy-MM-dd HH:mm:ss）转换成LocalDateTime
	 * @param date
	 * @return
	 */
	public static LocalDateTime getLoalDateTimeFromStr(String date){
		DateTimeFormatter sf = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
		return LocalDateTime.parse(date, sf);
	}
	
	/**
	 * 根据日期格式的字符串2017-07-27 12:00:00和自定义格式转换成LocalDateTime
	 * @param date
	 * @param format
	 * @return
	 */
	public static LocalDateTime getLoalDateTimeFromStr(String date,String format){
		DateTimeFormatter sf = DateTimeFormatter.ofPattern(format);
		return LocalDateTime.parse(date, sf);
	}
	
	public static String getDateFromLocalDateTime(LocalDateTime date){
		DateTimeFormatter sf = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
		return date.format(sf);
	}
	
	
	
	public static void main(String[] args) {
		LocalDateTime d = getLoalDateTimeFromStr("2017-07-27 12:00:00");
		System.out.println(d);
		LocalDateTime d1 = getLoalDateTimeFromStr("2017-07-27 12:00:00","yyyy-MM-dd HH:mm:ss");
		System.out.println(d1);
	}
	
	
}
