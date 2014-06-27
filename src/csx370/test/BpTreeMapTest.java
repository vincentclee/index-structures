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
	
	
	
	
	
	
	
	
	
	
	@Test
	public void sizeTest()
	{
		BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;

		for (int i = 1; i < totKeys; i += 2)
		{
			bptest.put(i, i * i);
		}
		int TestSize = bptest.size();
		org.junit.Assert.assertEquals(" The size is ", TestSize, 6);
	}

	@Test
	public void subMapTest1()
	{
		BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;

		for (int i = 1; i < totKeys; i += 2)
		{
			bptest.put(i, i * i);
		}


		SortedMap<Integer, Integer> SubMapTest = (SortedMap<Integer, Integer>) bptest.subMap(3, 9);
		String  st = SubMapTest.toString();

		org.junit.Assert.assertEquals("SubMap Test is ", st, "{3=9, 5=25, 7=49}");

	}
	@Test
	public void headMapTest1()
	{
		BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;

		for (int i = 1; i < totKeys; i += 2)
		{
			bptest.put(i, i * i);
		}


		SortedMap<Integer, Integer> HeadTest = (SortedMap<Integer, Integer>) bptest.headMap(5);
		String  st =HeadTest.toString();

		org.junit.Assert.assertEquals("HeadMap Test is ", st, "{1=1, 3=9}");
	}
	@Test
	public void tailMapTest1()
	{
		BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;

		for (int i = 1; i < totKeys; i += 2)
		{
			bptest.put(i, i * i);
		}


		SortedMap<Integer, Integer> TailTest = (SortedMap<Integer, Integer>) bptest.tailMap(5);
		String  st =TailTest.toString();

		org.junit.Assert.assertEquals("TailMap Test is ", st, "{5=25, 7=49, 9=81, 11=121}");
	}
	@Test

	public void firstKeyTest()
	{
		BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;

		for (int i = 1; i < totKeys; i += 2)
		{
			bptest.put(i, i * i);
		}
		int TestFirst = bptest.firstKey();
		org.junit.Assert.assertEquals(" First Key is ", TestFirst, 1);

	}
	@Test
	public void lastKeyTest()
	{
		BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;

		for (int i = 1; i < totKeys; i += 2)
		{
			bptest.put(i, i * i);
		}
		int TestLast = bptest.lastKey();
		org.junit.Assert.assertEquals(" First Key is ", TestLast, 11);
	}
}
