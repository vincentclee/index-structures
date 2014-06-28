package csx370.test;

import java.util.Map.Entry;
import java.util.Set;

import csx370.impl.ExtHashMap;
import junit.framework.TestCase;

/**
 * Basic testing framework for an ExtHashMap
 */
public class ExtHashMapTest extends TestCase {

	ExtHashMap<Integer, Integer> testMap;
	
	/**
	 * Sets up the testing state for the exthashmap
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setUp() {
		testMap = new ExtHashMap(Integer.class, Integer.class, 4);
	}
	
	/**
	 * Verifies basic get and put operations of the hash set.
	 */
	public void putAndGetTest() {
		testMap.put(5, 10);
		testMap.put(10, 20);
		testMap.put(1, 2);
		
		assertEquals(testMap.get(5), Integer.valueOf(10));
		assertEquals(testMap.get(10), Integer.valueOf(20));
		assertEquals(testMap.get(1), Integer.valueOf(2));
		
	}
	
	/**
	 * Basic verification that the set provided by the entrySet method is of the same size as the number of elements passed in.
	 */
	public void entrySetTest() {
		putAndGetTest();
		
		Set<Entry<Integer, Integer>> testSet = testMap.entrySet();
		assertEquals(testSet.size(), 3);
	}
 	
}
