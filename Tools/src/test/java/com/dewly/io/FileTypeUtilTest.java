package com.dewly.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import com.dewly.io.resource.ResourceUtil;

public class FileTypeUtilTest {
	
	@Test
	public void testFileTypeUtilTest(){
		
		URL resource = ResourceUtil.getResource("ResourceUtil.class", ResourceUtil.class);
		try {
			System.out.println(resource.getFile());
			InputStream openStream = resource.openStream();
			
			String type = FileTypeUtil.getType(openStream);
			type = FileTypeUtil.getType(new File("/Users/xiaofengche/git/tools/Tools/target/classes/com/dewly/io/resource/ResourceUtil.class"));
			System.out.println(type);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
