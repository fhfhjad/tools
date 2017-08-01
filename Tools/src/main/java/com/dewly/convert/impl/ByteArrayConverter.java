package com.dewly.convert.impl;

import com.dewly.convert.AbstractConverter;
import com.dewly.convert.ConverterRegistry;
import com.dewly.util.ArrayUtil;

/**
 * byte 类型数组转换器
 * @author Looly
 *
 */
public class ByteArrayConverter extends AbstractConverter<byte[]>{
	
	@Override
	protected byte[] convertInternal(Object value) {
		final Byte[] result = ConverterRegistry.getInstance().convert(Byte[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
