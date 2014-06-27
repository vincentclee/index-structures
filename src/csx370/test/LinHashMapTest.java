package csx370.test;

import junit.framework.TestCase;
import csx370.impl.LinHashMap;

/**
 * This class provides tests for the Index Structure: Linear Hashing
 */
public class LinHashMapTest extends TestCase 
{
  LinHashMap<Integer, Integer> lhm;

  // test get and put methods
  public void testGetAndPut()
  {
    lhm = new LinHashMap<Integer, Integer>(Integer.class, Integer.class, 2);
    
    lhm.put(123, 456);
    assertEquals("get and put test 1", new Integer(456), lhm.get(123));
  }// testGetAndPut

  // test entrySet method
  public void testEntrySet()
  {
    lhm = new LinHashMap<Integer, Integer>(Integer.class, Integer.class, 2);
    assertEquals("entry set test 1", 0, lhm.entrySet().size());
    assertTrue("entry set test 1a", lhm.entrySet().isEmpty());

    lhm = new LinHashMap<Integer, Integer>(Integer.class, Integer.class, 2);
    lhm.put(0, 0);
    assertEquals("entry set test 2", 1, lhm.entrySet().size());
    assertFalse("entry set test 3", lhm.entrySet().isEmpty());

    lhm = new LinHashMap<Integer, Integer>(Integer.class, Integer.class, 2);
    for(int i = 0; i < 100; i++)
    {
      lhm.put(i, i);
    }// for
    assertEquals("entry set test 4", 100, lhm.entrySet().size());
  }// testEntrySet

  // checks if the map handle a large number of entries well
  public void testVolume()
  {
    lhm = new LinHashMap<Integer, Integer>(Integer.class, Integer.class, 2);

    for(int i = 0; i < 100000; i++)
    {
      lhm.put(i, i * 2);
    }// for

    assertEquals("volume test size", 100000, lhm.entrySet().size());

    for(int i = 0; i < 100000; i++)
    {
      assertEquals("volume test iteration #" + i, new Integer(i * 2), lhm.get(i));
    }// for
  }// testVolume

  // makes sure map returns null for a nonpresent value
  public void testReturnNull()
  {
    lhm = new LinHashMap<Integer, Integer>(Integer.class, Integer.class, 2);

    assertNull("return null test 1", lhm.get(2));

    lhm.put(1, 1);

    assertNull("return null test 3", lhm.get(0));
    assertNotNull("return null test 2", lhm.get(1));
    assertNull("return null test 3", lhm.get(2));
  }// testReturnNull

  // tests if the map can handle other data types
  public void testOtherClasses()
  {
    // char -> string
    LinHashMap<Character, String> lhm = new LinHashMap<Character, String>(Character.class, String.class, 2);
    lhm.put('f', "friends who do stuff together");
    lhm.put('u', "you and me");
    lhm.put('n', "anywhere, anytime at all down here in the deep blue sea");

    assertEquals("other classes test 1", "friends who do stuff together", lhm.get('f'));
    assertEquals("other classes test 2", "you and me", lhm.get('u'));
    assertEquals("other classes test 3", "anywhere, anytime at all down here in the deep blue sea", lhm.get('n'));

    // bool -> float
    LinHashMap<Boolean, Float> lhm2 = new LinHashMap<Boolean, Float>(Boolean.class, Float.class, 2);
    lhm2.put(true, 1.0f);
    lhm2.put(false, 0.0f);
    
    assertEquals("other classes test 4", 1.0, lhm2.get(true), .001);
    assertEquals("other classes test 5", 0.0, lhm2.get(false), .001);
  }// testOtherClasses

  // make sure it won't blow up or anything when dealing with dupes
  public void testDuplicates()
  {
    lhm = new LinHashMap<Integer, Integer>(Integer.class, Integer.class, 2);

    lhm.put(0, 0);
    lhm.put(1, 1);
    lhm.put(2, 2);
    lhm.put(3, 3);

    for(int i = 0; i < 100; i++)
    {
      lhm.put(1, 1);
      lhm.put(3, 3);
    }// for

    for(int i = 0; i < 4; i++)
    {
      assertEquals("duplicates test #" + i, new Integer(i), lhm.get(i));
    }// for

    assertEquals("duplicates size test", 4, lhm.entrySet().size());
  }// testDuplicates
}// AllTests