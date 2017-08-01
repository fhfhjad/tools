package com.dewly.test;

import java.util.List;

import com.dewly.file.FileUtil;
import com.dewly.lang.MyFilter;

public class TestMyFile {
	public static void main(String[] args) {
		
		List<String> filename = FileUtil.getFilename("/Users/xiaofengche/Documents", new MyFilter<String>() {
			
			@Override
			public boolean accept(String t) {
				System.out.println(t.length());
				System.out.println(t);
				System.out.println(t.lastIndexOf("."));
				if(t.lastIndexOf(".") != -1){
					System.out.println(t.substring(t.lastIndexOf(".")).toLowerCase());
					return ".jpg".equals(t.substring(t.lastIndexOf(".")).toLowerCase());
				}
				return false;
			}
		});
		
		filename.forEach(action -> {
			System.out.println(action);
		});
		
	}
}
