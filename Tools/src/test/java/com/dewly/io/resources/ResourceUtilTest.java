package com.dewly.io.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.dewly.io.resource.ResourceUtil;

/**
 * classpath资源文件测试 {@link ResourceUtil}
 * 
 * @author sunlulu
 *
 */
public class ResourceUtilTest {
	@Ignore
	@Test
	public void testResourceUtil() {
		URL resource = ResourceUtil.getResource("com/dewly/io/resource/test.xml");
		try {
			InputStream openStream = resource.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(openStream));
			br.lines().forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void testResource(){
		URL resource = ResourceUtil.getResource("test.xml", ResourceUtil.class);
		try {
			InputStream openStream = resource.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(openStream));
			br.lines().forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void testGetResources(){
		List<URL> resources = ResourceUtil.getResources("com/dewly/util/StrUtil.class");
		resources.stream().forEach(action -> {
			String file = action.getFile();
			System.out.println(file);
		});
	}
	
}
