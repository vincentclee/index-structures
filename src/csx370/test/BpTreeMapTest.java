/**
 * hi
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
//	Map<Integer,Integer> linkedHashMap;
	SortedMap<Integer,Integer> treeMap;
	
	/**
	 * hi
	 */
	@Before
	public void setUp() {
		bpt = new BpTreeMap<>(Integer.class, Integer.class);
		int toKeys = 20;
		
		Random random = new Random();
		
		
//		linkedHashMap = new LinkedHashMap<Integer,Integer>();
		treeMap = new TreeMap<Integer,Integer>();
		
		
		for (int i = 0; i < toKeys; i++) {
			int number = random.nextInt(20);
			
			//generate unique 5 digit number
//			while (linkedHashMap.containsKey(number = random.nextInt(20)));
			
//			linkedHashMap.put(number, number * number);
			treeMap.put(number, number * number);
			bpt.put(number, number * number);
		}
//		
//		System.out.println(linkedHashMap);
		
		
//		for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
//		    int key = entry.getKey();
//		    int value = entry.getValue();
//		    
//		    bpt.put(key, value);
//		}
//		for (int i = 1; i < toKeys; i += 2) {
//			bpt.put(i, i * i);
//			treeMap.put(i, i * i);
//		}
		
		System.out.println(treeMap);
		
//		bpt.print(bpt.getRoot(), 0);
		
//		for (int i = 0; i < toKeys; i++) {
//			out.println("key = " + i + " value = " + bpt.get(i));
//		} // for
	}
	
	/**
	 * the submap with keys in the range [firstKey, toKey)
	 */
	@Test
	public void headMapTest() {
		assertEquals("headMap", treeMap.headMap(treeMap.lastKey()).toString(), bpt.headMap(treeMap.lastKey()).toString());
	}
	
	/**
	 * the submap with keys in the range [fromKey, lastKey]
	 */
	@Test
	public void tailMapTest() {
		assertEquals("tailMap", treeMap.tailMap(treeMap.firstKey()).toString(), bpt.tailMap(treeMap.firstKey()).toString());
	}
	
	/**
	 * the submap with keys in the range [fromKey, toKey)
	 */
	@Test
	public void subMapTest() {
		assertEquals("subMap", treeMap.subMap(treeMap.firstKey(), treeMap.lastKey()).toString(), bpt.subMap(treeMap.firstKey(), treeMap.lastKey()).toString());
	}
}
