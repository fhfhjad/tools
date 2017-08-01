package com.dewly.lamda;

import java.util.Optional;
import java.util.stream.Stream;

public class TextUtil {
	
	public static int getTextLength(String text){
		return Optional.ofNullable(text).map(String::length).orElse(-1);
	}
	
	/**
	 * 不定字段的拼接
	 * @param strings
	 * @return
	 */
	public static String concat(String... strings){
		return Stream.of(strings).reduce("", String::concat); 
	}
	
	/**
	 * 字符串数组的拼接
	 * @param strs
	 * @return
	 */
	public static String concatArr(String[] strs){
		return Stream.of(strs).reduce("", String::concat); 
	}
	
	public static void main(String[] args) {
		System.out.println(getTextLength(null));
		
		
		System.out.println(concat("a","1","c","d"));
	}
}
