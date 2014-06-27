package csx370.test;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import csx370.impl.BpTreeMap;

/**
 * This class provides tests for the Index Structure: B+ Tree
 */
public class BpTreeMapTest {
	private BpTreeMap<Integer, Integer> bpt;
	private SortedMap<Integer,Integer> treeMap;
	
	/**
	 * Provides setup by generating random numbers for testing
	 */
	@Before
	public void setUp() {
		bpt = new BpTreeMap<>(Integer.class, Integer.class);
		treeMap = new TreeMap<Integer,Integer>();
		
		//random generator
		Random random = new Random();
		
		//number of non-unique items
		int toKeys = 100;
		
		//generate
		for (int i = 0; i < toKeys; i++) {
			int number = random.nextInt(1000);
			
			treeMap.put(number, number * number);
			bpt.put(number, number * number);
		}
	}
	
	/**
	 * the headmap with keys in the range [firstKey, toKey)
	 */
	@Test
	public void headMapTest() {
		assertEquals("headMap", treeMap.headMap(treeMap.lastKey()).toString(), bpt.headMap(treeMap.lastKey()).toString());
	}
	
	/**
	 * the tailmap with keys in the range [fromKey, lastKey]
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
	
	/**
	 * the size (number of keys) in the B+Tree.
	 */
	@Test
	public void sizeTest() {
		BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;
		
		for (int i = 1; i < totKeys; i += 2) {
			bptest.put(i, i * i);
		}
		int TestSize = bptest.size();
		assertEquals(" The size is ", TestSize, 6);
	}
	
	/**
	 * another submap with keys in the range [fromKey, toKey)
	 */
	@Test
	public void subMapTest1() {
		BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;
		
		for (int i = 1; i < totKeys; i += 2) {
			bptest.put(i, i * i);
		}
		
		SortedMap<Integer, Integer> SubMapTest = (SortedMap<Integer, Integer>) bptest.subMap(3, 9);
		String  st = SubMapTest.toString();
		
		assertEquals("SubMap Test is ", st, "{3=9, 5=25, 7=49}");
	}
	
	/**
	 * another headmap with keys in the range [firstKey, toKey)
	 */
	@Test
	public void headMapTest1() {
		BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;
		
		for (int i = 1; i < totKeys; i += 2) {
			bptest.put(i, i * i);
		}
		
		SortedMap<Integer, Integer> HeadTest = (SortedMap<Integer, Integer>) bptest.headMap(5);
		String  st =HeadTest.toString();
		
		org.junit.Assert.assertEquals("HeadMap Test is ", st, "{1=1, 3=9}");
	}
	
	/**
	 * another tailmap with keys in the range [fromKey, lastKey]
	 */
	@Test
	public void tailMapTest1() {
		BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;
		
		for (int i = 1; i < totKeys; i += 2) {
			bptest.put(i, i * i);
		}
		
		SortedMap<Integer, Integer> TailTest = (SortedMap<Integer, Integer>) bptest.tailMap(5);
		String  st =TailTest.toString();
		
		assertEquals("TailMap Test is ", st, "{5=25, 7=49, 9=81, 11=121}");
	}
	
	/**
	 * the first (smallest) key in the B+Tree map
	 */
	@Test
	public void firstKeyTest() {
		BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;
		
		for (int i = 1; i < totKeys; i += 2) {
			bptest.put(i, i * i);
		}
		int TestFirst = bptest.firstKey();
		assertEquals(" First Key is ", TestFirst, 1);
	}
	
	/**
	 * the last (largest) key in the B+Tree map
	 */
	@Test
	public void lastKeyTest() {
		BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;
		
		for (int i = 1; i < totKeys; i += 2) {
			bptest.put(i, i * i);
		}
		int TestLast = bptest.lastKey();
		assertEquals(" First Key is ", TestLast, 11);
	}
}
