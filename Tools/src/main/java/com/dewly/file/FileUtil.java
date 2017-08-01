package com.dewly.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.dewly.lang.MyFilter;

public class FileUtil {
	
	private FileUtil(){}
	
	public static List<String> getFilename(String path,MyFilter<String> pattenString){
		List<String> list = new ArrayList<>();
		File file = new File(path);
		if (file.exists()) {
			File[] listFiles = file.listFiles();
			for (File file2 : listFiles) {
				if(file2.isFile() && pattenString.accept(file2.getName())){
					list.add(file2.getName());
				}
			}
			return list;
		}else
			return null;
	}
	
}
