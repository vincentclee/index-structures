/**
 * 
 */
package csx370.test;

import static java.lang.System.out;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import csx370.impl.BpTreeMap;

/**
 * hi
 *
 */
public class BpTreeMapTest {
	BpTreeMap<Integer, Integer> bpt;
	Map<Integer,Integer> linkedHashMap;
	SortedMap<Integer,Integer> treeMap;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() {
		bpt = new BpTreeMap<>(Integer.class, Integer.class);
		int toKeys = 8;
		
		Random random = new Random();
		
		
		linkedHashMap = new LinkedHashMap<Integer,Integer>();
		treeMap = new TreeMap<Integer,Integer>();
		
		
		for (int i = 0; i < toKeys; i++) {
			int number = random.nextInt(20);
			
			//generate unique 5 digit number
			while (linkedHashMap.containsKey(number = random.nextInt(20)));
			
			linkedHashMap.put(number, number * number);
			treeMap.put(number, number * number);
		}
		
		System.out.println(linkedHashMap);
		System.out.println(treeMap);
		
		
		
//		bpt.print(bpt.getRoot(), 0);
		
		for (int i = 0; i < toKeys; i++) {
			out.println("key = " + i + " value = " + bpt.get(i));
		} // for
	}

	@Test
	public void headMapTest() {
		bpt.headMap(treeMap.lastKey());
	}
	
	@Test
	public void tailMapTest() {
		bpt.tailMap(treeMap.lastKey());
	}
	
	@Test
	public void subMapTest() {
		bpt.subMap(treeMap.firstKey(), treeMap.lastKey());
	}
}
