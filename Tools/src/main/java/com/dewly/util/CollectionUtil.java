package com.dewly.util;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;

import com.dewly.collection.EnumerationIterator;
import com.dewly.collection.IteratorEnumeration;
import com.dewly.convert.Convert;
import com.dewly.exceptions.UtilException;
import com.dewly.lang.BoundedPriorityQueue;
import com.dewly.lang.Editor;
import com.dewly.lang.Matcher;

/**
 * 集合相关工具类，包括数组
 * 
 * @author xiaoleilu
 * 
 */
public class CollectionUtil {

	private CollectionUtil() {
	}

	/**
	 * 两个集合的并集<br>
	 * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最多的个数<br>
	 * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
	 * 结果：[a, b, c, c, c]，此结果中只保留了三个c
	 * 
	 * @param <T> 集合元素类型
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @return 并集的集合，返回 {@link ArrayList}
	 */
	public static <T> Collection<T> union(final Collection<T> coll1, final Collection<T> coll2) {
		final ArrayList<T> list = new ArrayList<>();
		if (isEmpty(coll1)) {
			list.addAll(coll2);
		} else if (isEmpty(coll2)) {
			list.addAll(coll1);
		} else {
			final Map<T, Integer> map1 = countMap(coll1);
			final Map<T, Integer> map2 = countMap(coll2);
			final Set<T> elts = newHashSet(coll2);
			for (T t : elts) {
				for (int i = 0, m = Math.max(Integer.parseInt(map1.get(t).toString()), Integer.parseInt(map2.get(t).toString())); i < m; i++) {
					list.add(t);
				}
			}
		}
		return list;
	}

	/**
	 * 多个集合的并集<br>
	 * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最多的个数<br>
	 * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
	 * 结果：[a, b, c, c, c]，此结果中只保留了三个c
	 * 
	 * @param <T> 集合元素类型
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @param otherColls 其它集合
	 * @return 并集的集合，返回 {@link ArrayList}
	 */
	@SafeVarargs
	public static <T> Collection<T> union(final Collection<T> coll1, final Collection<T> coll2, final Collection<T>... otherColls) {
		Collection<T> union = union(coll1, coll2);
		for (Collection<T> coll : otherColls) {
			union = union(union, coll);
		}
		return union;
	}

	/**
	 * 两个集合的交集<br>
	 * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最少的个数<br>
	 * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
	 * 结果：[a, b, c, c]，此结果中只保留了两个c
	 * 
	 * @param <T> 集合元素类型
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @return 交集的集合，返回 {@link ArrayList}
	 */
	public static <T> Collection<T> intersection(final Collection<T> coll1, final Collection<T> coll2) {
		final ArrayList<T> list = new ArrayList<>();
		if (isNotEmpty(coll1) && isNotEmpty(coll2)) {
			final Map<T, Integer> map1 = countMap(coll1);
			final Map<T, Integer> map2 = countMap(coll2);
			final Set<T> elts = newHashSet(coll2);
			for (T t : elts) {
				for (int i = 0, m = Math.min(Integer.parseInt(map1.get(t).toString()), Integer.parseInt(map2.get(t).toString())); i < m; i++) {
					list.add(t);
				}
			}
		}
		return list;
	}

	/**
	 * 多个集合的交集<br>
	 * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最少的个数<br>
	 * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
	 * 结果：[a, b, c, c]，此结果中只保留了两个c
	 * 
	 * @param <T> 集合元素类型
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @param otherColls 其它集合
	 * @return 并集的集合，返回 {@link ArrayList}
	 */
	@SafeVarargs
	public static <T> Collection<T> intersection(final Collection<T> coll1, final Collection<T> coll2, final Collection<T>... otherColls) {
		Collection<T> intersection = intersection(coll1, coll2);
		if (isEmpty(intersection)) {
			return intersection;
		}
		for (Collection<T> coll : otherColls) {
			intersection = intersection(intersection, coll);
			if (isEmpty(intersection)) {
				return intersection;
			}
		}
		return intersection;
	}

	/**
	 * 两个集合的差集<br>
	 * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留两个集合中此元素个数差的个数<br>
	 * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
	 * 结果：[c]，此结果中只保留了一个
	 * 
	 * @param <T> 集合元素类型
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @return 差集的集合，返回 {@link ArrayList}
	 */
	public static <T> Collection<T> disjunction(final Collection<T> coll1, final Collection<T> coll2) {
		final ArrayList<T> list = new ArrayList<>();
		if (isNotEmpty(coll1) && isNotEmpty(coll2)) {
			final Map<T, Integer> map1 = countMap(coll1);
			final Map<T, Integer> map2 = countMap(coll2);
			final Set<T> elts = newHashSet(coll2);
			for (T t : elts) {
				for (int i = 0, m = Math.max(Integer.parseInt(map1.get(t).toString()), Integer.parseInt(map2.get(t).toString())) - Math.min(Integer.parseInt(map1.get(t).toString()), Integer.parseInt(map2.get(t).toString())); i < m; i++) {
					list.add(t);
				}
			}
		}
		return list;
	}

	/**
	 * 其中一个集合在另一个集合中是否至少包含一个元素，既是两个集合是否至少有一个共同的元素
	 * 
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @return 其中一个集合在另一个集合中是否至少包含一个元素
	 * @since 2.1
	 * @see #intersection
	 */
	public static boolean containsAny(final Collection<?> coll1, final Collection<?> coll2) {
		if (isEmpty(coll1) || isEmpty(coll2)) {
			return false;
		}
		if (coll1.size() < coll2.size()) {
			for (Object object : coll1) {
				if (coll2.contains(object)) {
					return true;
				}
			}
		} else {
			for (Object object : coll2) {
				if (coll1.contains(object)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 根据集合返回一个元素计数的 {@link Map}<br>
	 * 所谓元素计数就是假如这个集合中某个元素出现了n次，那将这个元素做为key，n做为value<br>
	 * 例如：[a,b,c,c,c] 得到：<br>
	 * a: 1<br>
	 * b: 1<br>
	 * c: 3<br>
	 * 
	 * @param <T> 集合元素类型
	 * @param collection 集合
	 * @return {@link Map}
	 */
	public static <T> Map<T, Integer> countMap(Collection<T> collection) {
		HashMap<T, Integer> countMap = new HashMap<>();
		Integer count;
		for (T t : collection) {
			count = countMap.get(t);
			if (null == count) {
				countMap.put(t, 1);
			} else {
				countMap.put(t, count + 1);
			}
		}
		return countMap;
	}

	/**
	 * 以 conjunction 为分隔符将集合转换为字符串
	 * 
	 * @param <T> 集合元素类型
	 * @param iterable {@link Iterable}
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterable<T> iterable, CharSequence conjunction) {
		if (null == iterable) {
			return null;
		}
		return join(iterable.iterator(), conjunction);
	}

	/**
	 * 以 conjunction 为分隔符将集合转换为字符串
	 * 
	 * @param <T> 集合元素类型
	 * @param iterator 集合
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterator<T> iterator, CharSequence conjunction) {
		if (null == iterator) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		T item;
		while (iterator.hasNext()) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}

			item = iterator.next();
			if (ArrayUtil.isArray(item)) {
				sb.append(ArrayUtil.join(ArrayUtil.wrap(item), conjunction));
			} else if (item instanceof Iterable<?>) {
				sb.append(join((Iterable<?>) item, conjunction));
			} else if (item instanceof Iterator<?>) {
				sb.append(join((Iterator<?>) item, conjunction));
			} else {
				sb.append(item);
			}
		}
		return sb.toString();
	}

	/**
	 * 切取部分数据
	 * 
	 * @param <T> 集合元素类型
	 * @param surplusAlaDatas 原数据
	 * @param partSize 每部分数据的长度
	 * @return 切取出的数据或null
	 */
	public static <T> List<T> popPart(Stack<T> surplusAlaDatas, int partSize) {
		if (isEmpty(surplusAlaDatas)) {
			return null;
		}

		final List<T> currentAlaDatas = new ArrayList<>();
		int size = surplusAlaDatas.size();
		// 切割
		if (size > partSize) {
			for (int i = 0; i < partSize; i++) {
				currentAlaDatas.add(surplusAlaDatas.pop());
			}
		} else {
			for (int i = 0; i < size; i++) {
				currentAlaDatas.add(surplusAlaDatas.pop());
			}
		}
		return currentAlaDatas;
	}

	/**
	 * 切取部分数据
	 * 
	 * @param <T> 集合元素类型
	 * @param surplusAlaDatas 原数据
	 * @param partSize 每部分数据的长度
	 * @return 切取出的数据或null
	 */
	public static <T> List<T> popPart(Deque<T> surplusAlaDatas, int partSize) {
		if (isEmpty(surplusAlaDatas)) {
			return null;
		}

		final List<T> currentAlaDatas = new ArrayList<>();
		int size = surplusAlaDatas.size();
		// 切割
		if (size > partSize) {
			for (int i = 0; i < partSize; i++) {
				currentAlaDatas.add(surplusAlaDatas.pop());
			}
		} else {
			for (int i = 0; i < size; i++) {
				currentAlaDatas.add(surplusAlaDatas.pop());
			}
		}
		return currentAlaDatas;
	}

	// ----------------------------------------------------------------------------------------------- new HashMap
	/**
	 * 新建一个HashMap
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	/**
	 * 新建一个HashMap
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75
	 * @param isOrder Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
	 * @return HashMap对象
	 * @since 3.0.4
	 */
	public static <K, V> HashMap<K, V> newHashMap(int size, boolean isOrder) {
		int initialCapacity = (int) (size / 0.75);
		return isOrder ? new LinkedHashMap<K, V>(initialCapacity) : new HashMap<K, V>(initialCapacity);
	}

	/**
	 * 新建一个HashMap
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(int size) {
		return newHashMap(size, false);
	}

	// ----------------------------------------------------------------------------------------------- new HashSet
	/**
	 * 新建一个HashSet
	 * 
	 * @param <T> 集合元素类型
	 * @param ts 元素数组
	 * @return HashSet对象
	 */
	@SafeVarargs
	public static <T> HashSet<T> newHashSet(T... ts) {
		return newHashSet(false, ts);
	}

	/**
	 * 新建一个HashSet
	 * 
	 * @param <T> 集合元素类型
	 * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回 {@link HashSet}
	 * @param ts 元素数组
	 * @return HashSet对象
	 */
	@SafeVarargs
	public static <T> HashSet<T> newHashSet(boolean isSorted, T... ts) {
		if (null == ts) {
			return isSorted ? new LinkedHashSet<T>() : new HashSet<T>();
		}
		int initialCapacity = Math.max((int) (ts.length / .75f) + 1, 16);
		HashSet<T> set = isSorted ? new LinkedHashSet<T>(initialCapacity) : new HashSet<T>(initialCapacity);
		for (T t : ts) {
			set.add(t);
		}
		return set;
	}

	/**
	 * 新建一个HashSet
	 * 
	 * @param <T> 集合元素类型
	 * @param collection 集合
	 * @return HashSet对象
	 */
	public static <T> HashSet<T> newHashSet(Collection<T> collection) {
		return newHashSet(false, collection);
	}

	/**
	 * 新建一个HashSet
	 * 
	 * @param <T> 集合元素类型
	 * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
	 * @param collection 集合，用于初始化Set
	 * @return HashSet对象
	 */
	public static <T> HashSet<T> newHashSet(boolean isSorted, Collection<T> collection) {
		return isSorted ? new LinkedHashSet<T>(collection) : new HashSet<T>(collection);
	}

	/**
	 * 新建一个HashSet
	 * 
	 * @param <T> 集合元素类型
	 * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
	 * @param iter {@link Iterator}
	 * @return HashSet对象
	 * @since 3.0.8
	 */
	public static <T> HashSet<T> newHashSet(boolean isSorted, Iterator<T> iter) {
		if (null == iter) {
			return newHashSet(isSorted, (T[]) null);
		}
		final HashSet<T> set = isSorted ? new LinkedHashSet<T>() : new HashSet<T>();
		while (iter.hasNext()) {
			set.add(iter.next());
		}
		return set;
	}

	/**
	 * 新建一个HashSet
	 * 
	 * @param <T> 集合元素类型
	 * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
	 * @param enumration {@link Enumeration}
	 * @return HashSet对象
	 * @since 3.0.8
	 */
	public static <T> HashSet<T> newHashSet(boolean isSorted, Enumeration<T> enumration) {
		if (null == enumration) {
			return newHashSet(isSorted, (T[]) null);
		}
		final HashSet<T> set = isSorted ? new LinkedHashSet<T>() : new HashSet<T>();
		while (enumration.hasMoreElements()) {
			set.add(enumration.nextElement());
		}
		return set;
	}

	// ----------------------------------------------------------------------------------------------- new ArrayList
	/**
	 * 新建一个ArrayList
	 * 
	 * @param <T> 集合元素类型
	 * @param values 数组
	 * @return ArrayList对象
	 */
	@SafeVarargs
	public static <T> ArrayList<T> newArrayList(T... values) {
		if (null == values) {
			return new ArrayList<>();
		}
		ArrayList<T> arrayList = new ArrayList<T>(values.length);
		for (T t : values) {
			arrayList.add(t);
		}
		return arrayList;
	}

	/**
	 * 新建一个ArrayList
	 * 
	 * @param <T> 集合元素类型
	 * @param collection 集合
	 * @return ArrayList对象
	 */
	public static <T> ArrayList<T> newArrayList(Collection<T> collection) {
		if (null == collection) {
			return new ArrayList<>();
		}
		return new ArrayList<T>(collection);
	}

	/**
	 * 新建一个ArrayList
	 * 
	 * @param <T> 集合元素类型
	 * @param iter {@link Iterator}
	 * @return ArrayList对象
	 * @since 3.0.8
	 */
	public static <T> ArrayList<T> newArrayList(Iterator<T> iter) {
		final ArrayList<T> list = new ArrayList<>();
		if (null == iter) {
			return list;
		}
		while (iter.hasNext()) {
			list.add(iter.next());
		}
		return list;
	}

	/**
	 * 新建一个ArrayList
	 * 
	 * @param <T> 集合元素类型
	 * @param enumration {@link Enumeration}
	 * @return ArrayList对象
	 * @since 3.0.8
	 */
	public static <T> ArrayList<T> newArrayList(Enumeration<T> enumration) {
		final ArrayList<T> list = new ArrayList<>();
		if (null == enumration) {
			return list;
		}
		while (enumration.hasMoreElements()) {
			list.add(enumration.nextElement());
		}
		return list;
	}

	/**
	 * 新建一个CopyOnWriteArrayList
	 * 
	 * @param <T> 集合元素类型
	 * @param collection 集合
	 * @return {@link CopyOnWriteArrayList}
	 */
	public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList(Collection<T> collection) {
		return (null == collection) ? (new CopyOnWriteArrayList<T>()) : (new CopyOnWriteArrayList<T>(collection));
	}

	/**
	 * 创建新的集合对象
	 * 
	 * @param <T> 集合类型
	 * @param collectionType 集合类型
	 * @return 集合类型对应的实例
	 * @since 3.0.8
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Collection<T> create(Class<?> collectionType) {
		Collection<T> list = null;
		if (collectionType.isAssignableFrom(AbstractCollection.class)) {
			// 抽象集合默认使用ArrayList
			list = new ArrayList<>();
		}

		// Set
		else if (collectionType.isAssignableFrom(HashSet.class)) {
			list = new HashSet<>();
		} else if (collectionType.isAssignableFrom(LinkedHashSet.class)) {
			list = new LinkedHashSet<>();
		} else if (collectionType.isAssignableFrom(TreeSet.class)) {
			list = new TreeSet<>();
		} else if (collectionType.isAssignableFrom(EnumSet.class)) {
			list = (Collection<T>) EnumSet.noneOf((Class<Enum>) ClassUtil.getTypeArgument(collectionType));
		}

		// List
		else if (collectionType.isAssignableFrom(ArrayList.class)) {
			list = new ArrayList<>();
		} else if (collectionType.isAssignableFrom(LinkedList.class)) {
			list = new LinkedList<>();
		}

		// Others，直接实例化
		else {
			try {
				list = (Collection<T>) ClassUtil.newInstance(collectionType);
			} catch (Exception e) {
				throw new UtilException(e);
			}
		}
		return list;
	}

	/**
	 * 创建Map<br>
	 * 传入抽象Map{@link AbstractMap}和{@link Map}类将默认创建{@link HashMap}
	 * 
	 * @param <K> map键类型
	 * @param <V> map值类型
	 * @param mapType map类型
	 * @return {@link Map}实例
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> createMap(Class<?> mapType) {
		if (mapType.isAssignableFrom(AbstractMap.class)) {
			return new HashMap<>();
		}

		else {
			try {
				return (Map<K, V>) ClassUtil.newInstance(mapType);
			} catch (Exception e) {
				throw new UtilException(e);
			}
		}
	}

	/**
	 * 去重集合
	 * 
	 * @param <T> 集合元素类型
	 * @param collection 集合
	 * @return {@link ArrayList}
	 */
	public static <T> ArrayList<T> distinct(Collection<T> collection) {
		if (isEmpty(collection)) {
			return new ArrayList<>();
		} else if (collection instanceof Set) {
			return new ArrayList<>(collection);
		} else {
			return new ArrayList<>(new LinkedHashSet<>(collection));
		}
	}

	/**
	 * 截取数组的部分
	 * 
	 * @param <T> 集合元素类型
	 * @param list 被截取的数组
	 * @param start 开始位置（包含）
	 * @param end 结束位置（不包含）
	 * @return 截取后的数组，当开始位置超过最大时，返回null
	 */
	public static <T> List<T> sub(List<T> list, int start, int end) {
		if (list == null || list.isEmpty()) {
			return null;
		}

		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}

		if (start > end) {
			int tmp = start;
			start = end;
			end = tmp;
		}

		final int size = list.size();
		if (end > size) {
			if (start >= size) {
				return null;
			}
			end = size;
		}

		return list.subList(start, end);
	}

	/**
	 * 截取集合的部分
	 * 
	 * @param <T> 集合元素类型
	 * @param list 被截取的数组
	 * @param start 开始位置（包含）
	 * @param end 结束位置（不包含）
	 * @return 截取后的数组，当开始位置超过最大时，返回null
	 */
	public static <T> List<T> sub(Collection<T> list, int start, int end) {
		if (list == null || list.isEmpty()) {
			return null;
		}

		return sub(new ArrayList<T>(list), start, end);
	}

	/**
	 * 对集合按照指定长度分段，每一个段为单独的集合，返回这个集合的列表
	 * 
	 * @param <T> 集合元素类型
	 * @param collection 集合
	 * @param size 每个段的长度
	 * @return 分段列表
	 */
	public static <T> List<List<T>> split(Collection<T> collection, int size) {
		final List<List<T>> result = new ArrayList<>();

		ArrayList<T> subList = new ArrayList<>(size);
		for (T t : collection) {
			if (subList.size() > size) {
				result.add(subList);
				subList = new ArrayList<>(size);
			}
			subList.add(t);
		}
		result.add(subList);
		return result;
	}

	/**
	 * 过滤<br>
	 * 过滤会改变原集合的内容
	 * 
	 * @param <T> 集合元素类型
	 * @param collection 集合
	 * @param editor 编辑器接口
	 * @return 过滤后的数组
	 */
	public static <T> Collection<T> filter(Collection<T> collection, Editor<T> editor) {
		Collection<T> collection2 = ObjectUtil.clone(collection);
		collection2.clear();

		T modified;
		for (T t : collection) {
			modified = editor.edit(t);
			if (null != modified) {
				collection2.add(t);
			}
		}
		return collection2;
	}

	/**
	 * 过滤
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param map Map
	 * @param editor 编辑器接口
	 * @return 过滤后的Map
	 */
	public static <K, V> Map<K, V> filter(Map<K, V> map, Editor<Entry<K, V>> editor) {
		Map<K, V> map2 = ObjectUtil.clone(map);
		map2.clear();

		Entry<K, V> modified;
		for (Entry<K, V> entry : map.entrySet()) {
			modified = editor.edit(entry);
			if (null != modified) {
				map2.put(entry.getKey(), entry.getValue());
			}
		}
		return map2;
	}

	/**
	 * 集合中匹配规则的数量
	 * 
	 * @param <T> 集合元素类型
	 * @param iterable {@link Iterable}
	 * @param matcher 匹配器，为空则全部匹配
	 * @return 匹配数量
	 */
	public static <T> int count(Iterable<T> iterable, Matcher<T> matcher) {
		int count = 0;
		for (T t : iterable) {
			if (null == matcher || matcher.match(t)) {
				count++;
			}
		}
		return count;
	}

	// ---------------------------------------------------------------------- isEmpty
	/**
	 * 集合是否为空
	 * 
	 * @param collection 集合
	 * @return 是否为空
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * Map是否为空
	 * 
	 * @param map 集合
	 * @return 是否为空
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * Iterable是否为空
	 * 
	 * @param iterable Iterable对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Iterable<?> iterable) {
		return null == iterable || isEmpty(iterable.iterator());
	}

	/**
	 * Iterator是否为空
	 * 
	 * @param Iterator Iterator对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Iterator<?> Iterator) {
		return null == Iterator || false == Iterator.hasNext();
	}

	/**
	 * Enumeration是否为空
	 * 
	 * @param enumeration {@link Enumeration}
	 * @return 是否为空
	 */
	public static boolean isEmpty(Enumeration<?> enumeration) {
		return null == enumeration || false == enumeration.hasMoreElements();
	}

	// ---------------------------------------------------------------------- isNotEmpty

	/**
	 * 集合是否为非空
	 * 
	 * @param collection 集合
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return false == isEmpty(collection);
	}

	/**
	 * Map是否为非空
	 * 
	 * @param map 集合
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return false == isEmpty(map);
	}

	/**
	 * Iterable是否为空
	 * 
	 * @param iterable Iterable对象
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Iterable<?> iterable) {
		return null != iterable && isNotEmpty(iterable.iterator());
	}

	/**
	 * Iterator是否为空
	 * 
	 * @param Iterator Iterator对象
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Iterator<?> Iterator) {
		return null != Iterator && Iterator.hasNext();
	}

	/**
	 * Enumeration是否为空
	 * 
	 * @param enumeration {@link Enumeration}
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Enumeration<?> enumeration) {
		return null != enumeration && enumeration.hasMoreElements();
	}

	/**
	 * 是否包含{@code null}元素
	 * 
	 * @param iterable 被检查的Iterable对象
	 * @return 是否包含{@code null}元素
	 * @since 3.0.7
	 */
	public static boolean hasNull(Iterable<?> iterable) {
		if (isNotEmpty(iterable)) {
			for (Object element : iterable) {
				if (null == element) {
					return true;
				}
			}
		}
		return false;
	}

	// ---------------------------------------------------------------------- zip

	/**
	 * 映射键值（参考Python的zip()函数）<br>
	 * 例如：<br>
	 * keys = a,b,c,d<br>
	 * values = 1,2,3,4<br>
	 * delimiter = , 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
	 * 如果两个数组长度不同，则只对应最短部分
	 * 
	 * @param keys 键列表
	 * @param values 值列表
	 * @param delimiter 分隔符
	 * @param isOrder 是否有序
	 * @return Map
	 * @since 3.0.4
	 */
	public static Map<String, String> zip(String keys, String values, String delimiter, boolean isOrder) {
		return ArrayUtil.zip(StrUtil.split(keys, delimiter), StrUtil.split(values, delimiter), isOrder);
	}

	/**
	 * 映射键值（参考Python的zip()函数），返回Map无序<br>
	 * 例如：<br>
	 * keys = a,b,c,d<br>
	 * values = 1,2,3,4<br>
	 * delimiter = , 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
	 * 如果两个数组长度不同，则只对应最短部分
	 * 
	 * @param keys 键列表
	 * @param values 值列表
	 * @param delimiter 分隔符
	 * @return Map
	 */
	public static Map<String, String> zip(String keys, String values, String delimiter) {
		return zip(keys, values, delimiter, false);
	}

	/**
	 * 映射键值（参考Python的zip()函数）<br>
	 * 例如：<br>
	 * keys = [a,b,c,d]<br>
	 * values = [1,2,3,4]<br>
	 * 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
	 * 如果两个数组长度不同，则只对应最短部分
	 * 
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param keys 键列表
	 * @param values 值列表
	 * @return Map
	 */
	public static <K, V> Map<K, V> zip(Collection<K> keys, Collection<V> values) {
		if (isEmpty(keys) || isEmpty(values)) {
			return null;
		}

		final List<K> keyList = new ArrayList<K>(keys);
		final List<V> valueList = new ArrayList<V>(values);

		final int size = Math.min(keys.size(), values.size());
		final Map<K, V> map = new HashMap<K, V>((int) (size / 0.75));
		for (int i = 0; i < size; i++) {
			map.put(keyList.get(i), valueList.get(i));
		}

		return map;
	}

	/**
	 * 将Entry集合转换为HashMap
	 * 
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param entryCollection entry集合
	 * @return Map
	 */
	public static <K, V> HashMap<K, V> toMap(Collection<Entry<K, V>> entryCollection) {
		final HashMap<K, V> map = new HashMap<K, V>();
		if (isNotEmpty(entryCollection)) {
			for (Entry<K, V> entry : entryCollection) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		return map;
	}

	/**
	 * 将数组转换为Map（HashMap），支持数组元素类型为：
	 * 
	 * <pre>
	 * Map.Entry
	 * 长度大于1的数组（取前两个值），如果不满足跳过此元素
	 * Iterable 长度也必须大于1（取前两个值），如果不满足跳过此元素
	 * Iterator 长度也必须大于1（取前两个值），如果不满足跳过此元素
	 * </pre>
	 * 
	 * <pre>
	 * Map&lt;Object, Object&gt; colorMap = CollectionUtil.toMap(new String[][] {{
	 *     {"RED", "#FF0000"},
	 *     {"GREEN", "#00FF00"},
	 *     {"BLUE", "#0000FF"}});
	 * </pre>
	 * 
	 * 参考：commons-lang
	 * 
	 * @param array 数组。元素类型为Map.Entry、数组、Iterable、Iterator
	 * @return {@link HashMap}
	 * @since 3.0.8
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap<Object, Object> toMap(Object[] array) {
		if (array == null) {
			return null;
		}
		final HashMap<Object, Object> map = new HashMap<>((int) (array.length * 1.5));
		for (int i = 0; i < array.length; i++) {
			Object object = array[i];
			if (object instanceof Map.Entry) {
				Map.Entry entry = (Map.Entry) object;
				map.put(entry.getKey(), entry.getValue());
			} else if (object instanceof Object[]) {
				final Object[] entry = (Object[]) object;
				if (entry.length > 1) {
					map.put(entry[0], entry[1]);
				}
			} else if (object instanceof Iterable) {
				Iterator iter = ((Iterable) object).iterator();
				if (iter.hasNext()) {
					final Object key = iter.next();
					if (iter.hasNext()) {
						final Object value = iter.next();
						map.put(key, value);
					}
				}
			} else if (object instanceof Iterator) {
				Iterator iter = ((Iterator) object);
				if (iter.hasNext()) {
					final Object key = iter.next();
					if (iter.hasNext()) {
						final Object value = iter.next();
						map.put(key, value);
					}
				}
			} else {
				throw new IllegalArgumentException(StrUtil.format("Array element {}, '{}', is not type of Map.Entry or Array or Iterable or Iterator", i, object));
			}
		}
		return map;
	}

	/**
	 * 将集合转换为排序后的TreeSet
	 * 
	 * @param <T> 集合元素类型
	 * @param collection 集合
	 * @param comparator 比较器
	 * @return treeSet
	 */
	public static <T> TreeSet<T> toTreeSet(Collection<T> collection, Comparator<T> comparator) {
		final TreeSet<T> treeSet = new TreeSet<T>(comparator);
		for (T t : collection) {
			treeSet.add(t);
		}
		return treeSet;
	}

	/**
	 * Iterator转换为Enumeration
	 * <p>
	 * Adapt the specified <code>Iterator</code> to the <code>Enumeration</code> interface.
	 * 
	 * @param <E> 集合元素类型
	 * @param iter {@link Iterator}
	 * @return {@link Enumeration}
	 */
	public static <E> Enumeration<E> asEnumeration(Iterator<E> iter) {
		return new IteratorEnumeration<E>(iter);
	}

	/**
	 * Enumeration转换为Iterator
	 * <p>
	 * Adapt the specified <code>Enumeration</code> to the <code>Iterator</code> interface
	 * 
	 * @param <E> 集合元素类型
	 * @param e {@link Enumeration}
	 * @return {@link Iterator}
	 */
	public static <E> Iterator<E> asIterator(Enumeration<E> e) {
		return new EnumerationIterator<E>(e);
	}

	/**
	 * {@link Iterator} 转为 {@link Iterable}
	 * 
	 * @param <E> 元素类型
	 * @param iter {@link Iterator}
	 * @return {@link Iterable}
	 */
	public static <E> Iterable<E> asIterable(final Iterator<E> iter) {
		return new Iterable<E>(){
			@Override
			public Iterator<E> iterator() {
				return iter;
			}
		};
	}

	/**
	 * {@link Iterable}转为{@link Collection}<br>
	 * 首先尝试强转，强转失败则构建一个新的{@link ArrayList}
	 * 
	 * @param iterable {@link Iterable}
	 * @return {@link Collection} 或者 {@link ArrayList}
	 * @since 3.0.9
	 */
	public static <E> Collection<E> toCollection(Iterable<E> iterable) {
		return (iterable instanceof Collection) ? (Collection<E>) iterable : newArrayList(iterable.iterator());
	}

	/**
	 * 将指定对象全部加入到集合中<br>
	 * 提供的对象如果为集合类型，会自动转换为目标元素类型<br>
	 * 
	 * @param <T> 元素类型
	 * @param collection 被加入的集合
	 * @param value 对象，可能为Iterator、Iterable、Enumeration、Array
	 * @return 被加入集合
	 */
//	public static <T> Collection<T> addAll(Collection<T> collection, Object value) {
//		return addAll(collection, value, ClassUtil.getTypeArgument(collection.getClass()));
//	}

	/**
	 * 将指定对象全部加入到集合中<br>
	 * 提供的对象如果为集合类型，会自动转换为目标元素类型<br>
	 * 
	 * @param <T> 元素类型
	 * @param collection 被加入的集合
	 * @param value 对象，可能为Iterator、Iterable、Enumeration、Array
	 * @param elementType 元素类型，为空时，使用Object类型来接纳所有类型
	 * @return 被加入集合
	 */
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static <T> Collection<T> addAll(Collection<T> collection, Object value, Class<?> elementType) {
//		if (null == collection || null == value) {
//			return collection;
//		}
//		if (null == elementType) {// 元素类型为空时，使用Object类型来接纳所有类型
//			elementType = Object.class;
//		}
//
//		return collection;
//	}

	/**
	 * 加入全部
	 * 
	 * @param <T> 集合元素类型
	 * @param collection 被加入的集合 {@link Collection}
	 * @param iterator 要加入的{@link Iterator}
	 * @return 原集合
	 */
	public static <T> Collection<T> addAll(Collection<T> collection, Iterator<T> iterator) {
		if (null != collection && null != iterator) {
			while (iterator.hasNext()) {
				collection.add(iterator.next());
			}
		}
		return collection;
	}

	/**
	 * 加入全部
	 * 
	 * @param <T> 集合元素类型
	 * @param collection 被加入的集合 {@link Collection}
	 * @param iterable 要加入的内容{@link Iterable}
	 * @return 原集合
	 */
	public static <T> Collection<T> addAll(Collection<T> collection, Iterable<T> iterable) {
		return addAll(collection, iterable.iterator());
	}

	/**
	 * 加入全部
	 * 
	 * @param <T> 集合元素类型
	 * @param collection 被加入的集合 {@link Collection}
	 * @param enumeration 要加入的内容{@link Enumeration}
	 * @return 原集合
	 */
	public static <T> Collection<T> addAll(Collection<T> collection, Enumeration<T> enumeration) {
		if (null != collection && null != enumeration) {
			while (enumeration.hasMoreElements()) {
				collection.add(enumeration.nextElement());
			}
		}
		return collection;
	}

	/**
	 * 加入全部
	 * 
	 * @param <T> 集合元素类型
	 * @param collection 被加入的集合 {@link Collection}
	 * @param values 要加入的内容数组
	 * @return 原集合
	 * @since 3.0.8
	 */
	public static <T> Collection<T> addAll(Collection<T> collection, T[] values) {
		if (null != collection && null != values) {
			for (T value : values) {
				collection.add(value);
			}
		}
		return collection;
	}

	/**
	 * 将另一个列表中的元素加入到列表中，如果列表中已经存在此元素则忽略之
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param otherList 其它列表
	 * @return 此列表
	 */
	public static <T> List<T> addAllIfNotContains(List<T> list, List<T> otherList) {
		for (T t : otherList) {
			if (false == list.contains(t)) {
				list.add(t);
			}
		}
		return list;
	}

	/**
	 * 获取集合的第一个元素
	 * 
	 * @param <T> 集合元素类型
	 * @param iterable {@link Iterable}
	 * @return 第一个元素
	 * @since 3.0.1
	 */
	public static <T> T getFirst(Iterable<T> iterable) {
		if (null != iterable) {
			return getFirst(iterable.iterator());
		}
		return null;
	}

	/**
	 * 获取集合的第一个元素
	 * 
	 * @param <T> 集合元素类型
	 * @param iterator {@link Iterator}
	 * @return 第一个元素
	 * @since 3.0.1
	 */
	public static <T> T getFirst(Iterator<T> iterator) {
		if (null != iterator && iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}

	/**
	 * 获得{@link Iterable}对象的元素类型（通过第一个非空元素判断）
	 * 
	 * @param iterable {@link Iterable}
	 * @return 元素类型，当列表为空或元素全部为null时，返回null
	 * @since 3.0.8
	 */
	public static Class<?> getElementType(Iterable<?> iterable) {
		if (null != iterable) {
			Iterator<?> iterator = iterable.iterator();
			return getElementType(iterator);
		}
		return null;
	}

	/**
	 * 获得{@link Iterator}对象的元素类型（通过第一个非空元素判断）
	 * 
	 * @param iterator {@link Iterator}
	 * @return 元素类型，当列表为空或元素全部为null时，返回null
	 * @since 3.0.8
	 */
	public static Class<?> getElementType(Iterator<?> iterator) {
		if (null != iterator) {
			Object t;
			while (iterator.hasNext()) {
				t = iterator.next();
				if (null != t) {
					return t.getClass();
				}
			}
		}
		return null;
	}

	/**
	 * 从Map中获取指定键列表对应的值列表<br>
	 * 如果key在map中不存在或key对应值为null，则返回值列表对应位置的值也为null
	 * 
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param map {@link Map}
	 * @param keys 键列表
	 * @return 值列表
	 * @since 3.0.8
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> ArrayList<V> valuesOfKeys(Map<K, V> map, K... keys) {
		final ArrayList<V> values = new ArrayList<>();
		for (K k : keys) {
			values.add(map.get(k));
		}
		return values;
	}

	/**
	 * 从Map中获取指定键列表对应的值列表<br>
	 * 如果key在map中不存在或key对应值为null，则返回值列表对应位置的值也为null
	 * 
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param map {@link Map}
	 * @param keys 键列表
	 * @return 值列表
	 * @since 3.0.9
	 */
	public static <K, V> ArrayList<V> valuesOfKeys(Map<K, V> map, Iterable<K> keys) {
		return valuesOfKeys(map, keys.iterator());
	}

	/**
	 * 从Map中获取指定键列表对应的值列表<br>
	 * 如果key在map中不存在或key对应值为null，则返回值列表对应位置的值也为null
	 * 
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param map {@link Map}
	 * @param keys 键列表
	 * @return 值列表
	 * @since 3.0.9
	 */
	public static <K, V> ArrayList<V> valuesOfKeys(Map<K, V> map, Iterator<K> keys) {
		final ArrayList<V> values = new ArrayList<>();
		while (keys.hasNext()) {
			values.add(map.get(keys.next()));
		}
		return values;
	}

	// ------------------------------------------------------------------------------------------------- sort
	/**
	 * 将多个集合排序并显示不同的段落（分页）<br>
	 * 采用先排序，后截断的方式取分页的部分
	 * 
	 * @param <T> 集合元素类型
	 * @param pageNo 页码，从1开始
	 * @param numPerPage 每页的条目数
	 * @param comparator 比较器
	 * @param colls 集合数组
	 * @return 分页后的段落内容
	 */
	@SafeVarargs
	public static <T> List<T> sortPageAll(int pageNo, int numPerPage, Comparator<T> comparator, Collection<T>... colls) {
		final List<T> result = new ArrayList<>();
		for (Collection<T> coll : colls) {
			result.addAll(coll);
		}

		Collections.sort(result, comparator);

		int resultSize = result.size();
		// 每页条目数大于总数直接返回所有
		if (resultSize <= numPerPage) {
			return result;
		}
		final int[] startEnd = PageUtil.transToStartEnd(pageNo, numPerPage);
		if (startEnd[1] > resultSize) {
			// 越界直接返回空
			return new ArrayList<>();
		}

		return result.subList(startEnd[0], startEnd[1]);
	}

	/**
	 * 将多个集合排序并显示不同的段落（分页）<br>
	 * 采用{@link BoundedPriorityQueue}实现分页取局部
	 * 
	 * @param <T> 集合元素类型
	 * @param pageNo 页码
	 * @param numPerPage 每页的条目数
	 * @param comparator 比较器
	 * @param colls 集合数组
	 * @return 分业后的段落内容
	 */
	@SafeVarargs
	public static <T> List<T> sortPageAll2(int pageNo, int numPerPage, Comparator<T> comparator, Collection<T>... colls) {
		BoundedPriorityQueue<T> queue = new BoundedPriorityQueue<>(pageNo * numPerPage, comparator);
		for (Collection<T> coll : colls) {
			queue.addAll(coll);
		}

		int resultSize = queue.size();
		// 每页条目数大于总数直接返回所有
		if (resultSize <= numPerPage) {
			return queue.toList();
		}
		final int[] startEnd = PageUtil.transToStartEnd(pageNo, numPerPage);
		if (startEnd[1] > resultSize) {
			// 越界直接返回空
			return new ArrayList<>();
		}

		return queue.toList().subList(startEnd[0], startEnd[1]);
	}

	/**
	 * 排序集合，排序不会修改原集合
	 * 
	 * @param <T> 集合元素类型
	 * @param collection 集合
	 * @param comparator 比较器
	 * @return treeSet
	 */
	public static <T> List<T> sort(Collection<T> collection, Comparator<? super T> comparator) {
		List<T> list = new ArrayList<T>(collection);
		Collections.sort(list, comparator);
		return list;
	}
	
	/**
	 * 针对List排序，排序会修改原List
	 * 
	 * @param <T> 元素类型
	 * @param list 被排序的List
	 * @param c {@link Comparator}
	 * @return 原list
	 * @see Collections#sort(List, Comparator)
	 */
	public static <T> List<T> sort(List<T> list, Comparator<? super T> c) {
		Collections.sort(list, c);
		return list;
	}

	/**
	 * 排序Map
	 * 
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param map Map
	 * @param comparator Entry比较器
	 * @return {@link TreeMap}
	 * @since 3.0.9
	 */
	public static <K, V> TreeMap<K, V> sort(Map<K, V> map, Comparator<? super K> comparator) {
		TreeMap<K, V> result = new TreeMap<K, V>(comparator);
		result.putAll(map);
		return result;
	}

	/**
	 * 通过Entry排序，可以按照键排序，也可以按照值排序，亦或者两者综合排序
	 * 
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param entryCollection Entry集合
	 * @param comparator {@link Comparator}
	 * @return {@link LinkedList}
	 * @since 3.0.9
	 */
	public static <K, V> LinkedHashMap<K, V> sortToMap(Collection<Map.Entry<K, V>> entryCollection, Comparator<Map.Entry<K, V>> comparator) {
		List<Map.Entry<K, V>> list = new LinkedList<>(entryCollection);
		Collections.sort(list, comparator);
		
		LinkedHashMap<K, V> result = new LinkedHashMap<>();
		for(Map.Entry<K, V> entry : list){
				result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	/**
	 * 通过Entry排序，可以按照键排序，也可以按照值排序，亦或者两者综合排序
	 * 
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param map 被排序的Map
	 * @param comparator {@link Comparator}
	 * @return {@link LinkedList}
	 * @since 3.0.9
	 */
	public static <K, V> LinkedHashMap<K, V> sortByEntry(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator){
		return sortToMap(map.entrySet(), comparator);
	}
	
	/**
	 * 将Set排序（根据Entry的值）
	 * 
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param collection 被排序的{@link Collection}
	 * @return 排序后的Set
	 */
	public static <K, V> List<Entry<K, V>> sortEntryToList(Collection<Entry<K, V>> collection) {
		List<Entry<K, V>> list = new LinkedList<>(collection);
		Collections.sort(list, new Comparator<Entry<K, V>>(){

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				V v1 = o1.getValue();
				V v2 = o2.getValue();
				
				if(v1 instanceof Comparable) {
					return ((Comparable)v1).compareTo(v2);
				}else {
					return v1.toString().compareTo(v2.toString());
				}
			}
		});
		return list;
	}

	// ------------------------------------------------------------------------------------------------- forEach

	/**
	 * 循环遍历 {@link Iterator}，使用{@link Consumer} 接受遍历的每条数据，并针对每条数据做处理
	 * 
	 * @param <T> 集合元素类型
	 * @param iterator {@link Iterator}
	 * @param consumer {@link Consumer} 遍历的每条数据处理器
	 */
	public static <T> void forEach(Iterator<T> iterator, Consumer<T> consumer) {
		int index = 0;
		while (iterator.hasNext()) {
			consumer.accept(iterator.next(), index);
			index++;
		}
	}

	/**
	 * 循环遍历 {@link Enumeration}，使用{@link Consumer} 接受遍历的每条数据，并针对每条数据做处理
	 * 
	 * @param <T> 集合元素类型
	 * @param enumeration {@link Enumeration}
	 * @param consumer {@link Consumer} 遍历的每条数据处理器
	 */
	public static <T> void forEach(Enumeration<T> enumeration, Consumer<T> consumer) {
		int index = 0;
		while (enumeration.hasMoreElements()) {
			consumer.accept(enumeration.nextElement(), index);
			index++;
		}
	}

	/**
	 * 循环遍历Map，使用{@link KVConsumer} 接受遍历的每条数据，并针对每条数据做处理
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param map {@link Map}
	 * @param kvConsumer {@link KVConsumer} 遍历的每条数据处理器
	 */
	public static <K, V> void forEach(Map<K, V> map, KVConsumer<K, V> kvConsumer) {
		int index = 0;
		for (Entry<K, V> entry : map.entrySet()) {
			kvConsumer.accept(entry.getKey(), entry.getValue(), index);
			index++;
		}
	}

	/**
	 * 针对一个参数做相应的操作
	 * 
	 * @author Looly
	 *
	 * @param <T> 处理参数类型
	 */
	public static interface Consumer<T> {
		/**
		 * 接受并处理一个参数
		 * 
		 * @param value 参数值
		 * @param index 参数在集合中的索引
		 */
		void accept(T value, int index);
	}

	/**
	 * 针对两个参数做相应的操作，例如Map中的KEY和VALUE
	 * 
	 * @author Looly
	 *
	 * @param <K> KEY类型
	 * @param <V> VALUE类型
	 */
	public static interface KVConsumer<K, V> {
		/**
		 * 接受并处理一对参数
		 * 
		 * @param key 键
		 * @param value 值
		 * @param index 参数在集合中的索引
		 */
		void accept(K key, V value, int index);
	}
}
