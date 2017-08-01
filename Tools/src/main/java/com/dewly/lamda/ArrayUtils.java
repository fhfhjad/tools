package com.dewly.lamda;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArrayUtils {
	/**
	 * 不同类型的数组转出List
	 * @param t
	 * @return
	 */
	public static <T> List<T> toList(T[] t){
		return Stream.of(t).collect(Collectors.toList());
	}
	
	
	
	public static void main(String[] args) {
		
//		List<Person> list = testLimitAndSkip();
//		List<String> collect = list.stream().map(Person::getName).limit(10).skip(3).collect(Collectors.toList());
//		print(collect);
		
		Random seed = new Random();
		Supplier<Integer> random = seed::nextInt;
		Stream.generate(random).limit(10).forEach(System.out::println);
		//Another way
		IntStream.generate(() -> (int) (System.nanoTime() % 100)).
		limit(10).forEach(System.out::println);
	}
	
	private static void print(List<?> list){
		list.stream().forEach(
			System.out::println
		);
	}
	
	private static List<Person> testLimitAndSkip() {
		List<Person> persons = new ArrayList<>();
		for (int i = 1; i <= 10000; i++) {
			Person person = new Person(i, "name" + i);
			persons.add(person);
		}
		return persons;
	}
}

class Person {
	public int no;
	private String name;

	public Person(int no, String name) {
		this.no = no;
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
