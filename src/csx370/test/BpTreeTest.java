
import  csx370.impl.BpTreeMap;
import static java.lang.System.out;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import static java.lang.System.out;
import org.junit.Assert.*;

@SuppressWarnings("unused")
public class BpTreeTest {


		// testing data
		
		private static final boolean DEBUG = false;
				
		//@Test
		/*public void entrySettest()
		{
                    BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;
                
		
		
                for (int i = 1; i < totKeys; i += 2)
                {
		bptest.put(i, i * i);
                }
             	
		}*/
           
               
                
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
                
               
                public void subMapTest()
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
                
                public void headMapTest()
                {
                BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;
                
                for (int i = 1; i < totKeys; i += 2)
                {
		bptest.put(i, i * i);
                }
                
                
                SortedMap<Integer, Integer> HeadTest = (SortedMap<Integer, Integer>) bptest.headMap(5);
                String  st =HeadTest.toString();
             
                org.junit.Assert.assertEquals("HeadMap Test is ", st, " {1=1, 3=9}");
                }
             
                public void tailMapTest()
                {
                BpTreeMap<Integer, Integer> bptest = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 12;
                
                for (int i = 1; i < totKeys; i += 2)
                {
		bptest.put(i, i * i);
                }
                
                
                SortedMap<Integer, Integer> TailTest = (SortedMap<Integer, Integer>) bptest.tailMap(5);
                String  st =TailTest.toString();
             
                org.junit.Assert.assertEquals("TailMap Test is ", st, " {5=25, 7=49, 9=81, 11=121}");
                }

               
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

