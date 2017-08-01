package com.dewly.lang;

/**
 * 过滤器接口
 * @author sunlulu
 *
 * @param <T>
 */
public interface MyFilter<T> {
	
	/**
	 * 是否接受对象
	 * @param t 检查的对象
	 * @return 是否接受的对象
	 */
	boolean accept(T t);
	
}
