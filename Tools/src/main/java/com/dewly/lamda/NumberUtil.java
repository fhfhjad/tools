package com.dewly.lamda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NumberUtil {
	
	public static int sum1(Integer... i ){
		return Stream.of(i).reduce(0,Integer::sum);
	}
	
	public static int sum2(Integer... i ){
		return Stream.of(i).reduce(0,(a,b)->a+b);
	}
	
	public static int sum3(Integer[] is){
		return Stream.of(is).reduce(0, (a,b)->a+b);
	}
	
	public static int sum4(Integer[] is){
		return Stream.of(is).reduce(Integer::sum).get();
	}
	
	public static int sum5(Integer[] is){
		return Stream.of(is).reduce((a,b)->a+b).get();
	}
	
	
	public static void main(String[] args) {
		System.out.println(sum1(1,5));
		System.out.println(sum5(new Integer[]{1,7}));
		
		
	}
	
	
}


