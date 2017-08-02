package com.dewly.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.dewly.lang.Matcher;

/**
 * {@link CollectionUtil}
 * @author dewly
 *
 */
public class CollectionUtilTest {
	@Ignore
	@Test
	public void unionTest(){
		
		List<Integer> asList = Arrays.asList(1,3,4,3);
		List<Integer> asList2 = Arrays.asList(2,3,1,1);
		
		Collection<Integer> union = CollectionUtil.union(asList, asList2);
		Assert.assertArrayEquals(new Integer[]{1 }, union.toArray());
	}
	
	@Test
	public void countTest(){
		List<Integer> asList = Arrays.asList(1,3,4,3);
		int count = CollectionUtil.count(asList, null);
		Assert.assertEquals(count, 4);
		
		int count2 = CollectionUtil.count(asList, new Matcher<Integer>() {
			@Override
			public boolean match(Integer t) {
				if(t>1) return true;
				return false;
			}
		});
		Assert.assertEquals(3, count2);
		
	}
	
	
}
