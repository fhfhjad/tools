package com.dewly.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 反射工具类单元测试
 * @author Dewly
 *
 */
public class ReflectUtilTest {
	
	@Ignore
	@Test
	public void getConstructorTest(){
		Constructor<ArrayList> constructor = ReflectUtil.getConstructor(ArrayList.class,int.class);
		Assert.assertNotNull(constructor);
	}
	
	@Ignore
	@Test
	public void getConstructorsTest(){
		Constructor<ArrayList>[] constructors = ReflectUtil.getConstructors(ArrayList.class);
		Assert.assertEquals(3, constructors.length);
	}
	
	@Test
	public void getFiledValueTest(){
		Constructor<ArrayList> constructor = ReflectUtil.getConstructor(ArrayList.class,int.class);
		try {
			ArrayList newInstance = constructor.newInstance(20);
			Field field = ReflectUtil.getField(ArrayList.class, "DEFAULT_CAPACITY");
			Object fieldValue = ReflectUtil.getFieldValue(newInstance, field);
			System.out.println("fieldValue:" + fieldValue);
			Assert.assertEquals(10, fieldValue);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
}
