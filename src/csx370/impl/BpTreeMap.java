package csx370.impl;

/************************************************************************************
 * @file BpTreeMap.java
 *
 * @author  John Miller
 */

import java.io.*;
import java.lang.reflect.Array;
import static java.lang.System.out;
import java.util.*;

/************************************************************************************
 * This class provides B+Tree maps.  B+Trees are used as multi-level index structures
 * that provide efficient access for both point queries and range queries.
 */
public class BpTreeMap<K extends Comparable<K>, V> extends AbstractMap<K, V> 
	implements Serializable, Cloneable, SortedMap<K, V> {
	
	/** The maximum fanout for a B+Tree node. */
	private static final int ORDER = 5;
	
	/** The class for type K. */
	private final Class <K> classK;
	
	/** The class for type V. */
	private final Class <V> classV;
	
	/********************************************************************************
	 * This inner class defines nodes that are stored in the B+tree map.
	 */
	private class Node {
		boolean isLeaf;
		int nKeys;
		K[] key;
		Object[] ref;
		@SuppressWarnings("unchecked")
		Node(boolean _isLeaf) {
			isLeaf = _isLeaf;
			nKeys = 0;
			key = (K[]) Array.newInstance(classK, ORDER - 1);
			if (isLeaf) {
				//ref = (V[]) Array.newInstance(classV, ORDER);
				ref = new Object[ORDER];
			} else {
				ref = (Node[]) Array.newInstance(Node.class, ORDER);
			} // if
		} // constructor
	} // Node inner class
	
	/** The root of the B+Tree */
	private final Node root;
	
	/** The counter for the number nodes accessed (for performance testing). */
	private int count = 0;
	
	/** The size (number of keys) in the B+Tree. */
	private int sum = 0;
	
	/********************************************************************************
	 * Construct an empty B+Tree map.
	 * @param _classK  the class for keys (K)
	 * @param _classV  the class for values (V)
	 */
	public BpTreeMap(Class<K> _classK, Class<V> _classV) {
		classK = _classK;
		classV = _classV;
		root = new Node(true);
	} // constructor
	
	/********************************************************************************
	 * Return null to use the natural order based on the key type.  This requires the
	 * key type to implement Comparable.
	 */
	public Comparator<? super K> comparator() {
		return null;
	} // comparator
	
	/********************************************************************************
	 * Return a set containing all the entries as pairs of keys and values.
	 * @return  the set view of the map
	 */
	public Set<Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> enSet = new HashSet<>();
		
		//set the starting node
		Node node = root;
		
		//go to first leaf
		while (!node.isLeaf) {
			node = (Node) node.ref[0];
		}
		
		//add entries until full
		while (enSet.size() != sum) {
			//don't need this if correctly implemented
			//if node is null, exit
			if (node == null) {
				break;
			}
			
			//iterate through current node
			for (int i = 0; i < node.nKeys; i++) {
				enSet.add(new SimpleEntry<K, V>(node.key[i], (V) node.ref[i]));
			}
			
			//get next linked leaf
			node = (Node) node.ref[ORDER-1];
		}
		
		return enSet;
	} // entrySet
	
	/********************************************************************************
	 * Given the key, look up the value in the B+Tree map.
	 * @param key  the key used for look up
	 * @return  the value associated with the key
	 */
	@SuppressWarnings("unchecked")
	public V get(Object key) {
		return find((K) key, root);
	} // get
	
	/********************************************************************************
	 * Put the key-value pair in the B+Tree map.
	 * @param key    the key to insert
	 * @param value  the value to insert
	 * @return  null (not the previous value)
	 */
	public V put(K key, V value) {
		insert(key, value, root, null);
		return null;
	} // put
	
	/********************************************************************************
	 * Return the first (smallest) key in the B+Tree map.
	 * @return  the first key in the B+Tree map.
	 */
	public K firstKey() {
		//checks for first time
		boolean first = true;
		
		//contains smallest value
		K smallest = null;
		
		//loop through whole map
		for (Map.Entry<K, V> item : entrySet()) {
			//get key from set
			K key = item.getKey();
			
			//checks if smallest is initialized
			if (first) {
				smallest = key;
				first = false;
			}
			//checks whether current key is less than 'smallest'
			else {
				if (key.compareTo(smallest) < 0) {
					smallest = key;
				}
			}
		}
		
		//return smallest item
		return smallest;
	} // firstKey
	
	/********************************************************************************
	 * Return the last (largest) key in the B+Tree map.
	 * @return  the last key in the B+Tree map.
	 */
	public K lastKey() {
		//checks for first time
		boolean first = true;
		
		//contains largest value
		K largest = null;
		
		//loop through whole map
		for (Map.Entry<K, V> item : entrySet()) {
			//get key from set
			K key = item.getKey();
			
			//checks if largest is initialized
			if (first) {
				largest = key;
				first = false;
			}
			//checks whether current key is less than 'largest'
			else {
				if (key.compareTo(largest) > 0) {
					largest = key;
				}
			}
		}
		
		//return largest item
		return largest;
	} // lastKey
	
	/********************************************************************************
	 * Return the portion of the B+Tree map where key < toKey.
	 * @return  the submap with keys in the range [firstKey, toKey)
	 */
	public SortedMap<K,V> headMap(K toKey) {
		return subMap(firstKey(), toKey);
	} // headMap
	
	/********************************************************************************
	 * Return the portion of the B+Tree map where fromKey <= key.
	 * @return  the submap with keys in the range [fromKey, lastKey]
	 */
	public SortedMap<K,V> tailMap(K fromKey) {
		SortedMap<K,V> mapper = new TreeMap<>();
		
		//set the starting node
		Node node = root;
		
		//go to first leaf
		while (!node.isLeaf) {
			node = (Node) node.ref[0];
		}
		
		//add entries
		while (node != null) {
			//iterate through current node
			for (int i = 0; i < node.nKeys; i++) {
				if (fromKey.compareTo(node.key[i]) <= 0) {
					mapper.put(node.key[i], (V) node.ref[i]);
				}
			}
			
			//get next linked leaf
			node = (Node) node.ref[ORDER-1];
		}
		
		return mapper;
	} // tailMap
	
	/********************************************************************************
	 * Return the portion of the B+Tree map whose keys are between fromKey and toKey,
	 * i.e., fromKey <= key < toKey.
	 * @return  the submap with keys in the range [fromKey, toKey)
	 */
	public SortedMap<K,V> subMap(K fromKey, K toKey) {
		SortedMap<K,V> mapper = new TreeMap<>();
		
		//set the starting node
		Node node = root;
		
		//go to first leaf
		while (!node.isLeaf) {
			node = (Node) node.ref[0];
		}
		
		//add entries
		while (node != null) {
			//iterate through current node
			for (int i = 0; i < node.nKeys; i++) {
				if (fromKey.compareTo(node.key[i]) <= 0 && toKey.compareTo(node.key[i]) > 0) {
					mapper.put(node.key[i], (V) node.ref[i]);
				}
			}
			
			//get next linked leaf
			node = (Node) node.ref[ORDER-1];
		}
		
		return mapper;
	} // subMap
	
	/********************************************************************************
	 * Return the size (number of keys) in the B+Tree.
	 * @return  the size of the B+Tree
	 */
	public int size() {
		return sum;
	} // size
	
	/********************************************************************************
	 * Print the B+Tree using a pre-order traveral and indenting each level.
	 * @param n      the current node to print
	 * @param level  the current level of the B+Tree
	 */
	@SuppressWarnings("unchecked")
	private void print(Node n, int level) {
		System.out.println("first:" + firstKey()); //TODO
		System.out.println("last:" + lastKey()); //TODO
		System.out.println("size:" + size()); //TODO
		System.out.println(headMap((K) new Integer(7)));
		out.println("BpTreeMap");
		out.println("-------------------------------------------");
		
		for (int j = 0; j < level; j++) {
			out.print ("\t");
		}
		out.print ("[ . ");
		for (int i = 0; i < n.nKeys; i++) {
			out.print(n.key [i] + " . ");
		}
		out.println ("]");
		if (!n.isLeaf) {
			for (int i = 0; i <= n.nKeys; i++) {
				print((Node) n.ref[i], level + 1);
			}
		} // if
		
		out.println ("-------------------------------------------");
	} // print
	
	/********************************************************************************
	 * Recursive helper function for finding a key in B+trees.
	 * @param key  the key to find
	 * @param ney  the current node
	 */
	@SuppressWarnings("unchecked")
	private V find(K key, Node n) {
		count++;
		for (int i = 0; i < n.nKeys; i++) {
			K k_i = n.key[i];
			if (key.compareTo(k_i) <= 0) {
				if (n.isLeaf) {
					return (key.equals(k_i)) ? (V) n.ref[i] : null;
				} else {
					return find(key, (Node) n.ref[i]);
				} // if
			} // if
		} // for
		return (n.isLeaf) ? null : find (key, (Node) n.ref[n.nKeys]);
	} // find
	
	/********************************************************************************
	 * Recursive helper function for inserting a key in B+trees.
	 * @param key  the key to insert
	 * @param ref  the value/node to insert
	 * @param n    the current node
	 * @param p    the parent node
	 */
	private void insert(K key, V ref, Node n, Node p) {
		//Increment sum
		sum++;
		
		//Do insert
		if (n.nKeys < ORDER - 1) {
			for (int i = 0; i < n.nKeys; i++) {
				K k_i = n.key[i];
				if (key.compareTo(k_i) < 0) {
					wedge(key, ref, n, i);
				} else if (key.equals (k_i)) {
					out.println("BpTreeMap:insert: attempt to insert duplicate key = " + key);
				} // if
			} // for
			wedge(key, ref, n, n.nKeys);
		} else {
			Node sib = split(key, ref, n);
			//somenthing with the nKeys
			System.out.println("nKeys" + sib.nKeys);
			wedge(key, ref, sib,sib.nKeys);

		} // if
	} // insert
	
	/********************************************************************************
	 * Wedge the key-ref pair into node n.
	 * @param key  the key to insert
	 * @param ref  the value/node to insert
	 * @param n    the current node
	 * @param i    the insertion position within node n
	 */
	private void wedge(K key, V ref, Node n, int i) {
		for (int j = n.nKeys; j > i; j--) {
			n.key[j] = n.key[j - 1];
			n.ref[j] = n.ref[j - 1];
		} // for
		n.key[i] = key;
		n.ref[i] = ref;
		n.nKeys++;
	} // wedge
	
	/********************************************************************************
	 * Split node n and return the newly created node.
	 * @param key  the key to insert
	 * @param ref  the value/node to insert
	 * @param n    the current node
	 */
	private Node split(K key, V ref, Node n) {
		// Creates a Node for Childe node
		Node child = new Node(n.isLeaf);
		// the sibling node should have the same status leaf - non-leaf
		child.isLeaf = n.isLeaf;
		// The child has the same number of elements
		child.nKeys = 1;
		// The upper half of the elements goes to the child
		if (child.isLeaf) {
			int Half = n.nKeys / 2;
			out.print("Half is : " + Half);
			for (int i = 0; i < Half; i++) { 
				wedge(n.key[i+ Half], (V) n.ref[i + Half], child, i);
				n.nKeys--;
			}
			wedge(key, ref, child, Half);
			
		}
		else if (!child.isLeaf) {
			int Half = n.nKeys / 2;
			//out.print(" Half is : " + Half);
			for (int i = 0; i < Half; i++) { 
				wedge(n.key[i+ Half], null, child, i);
				n.nKeys--;
			}
			wedge(key, null, child, Half);
			
		}
		return child;
	} // split
	
	/********************************************************************************
	 * The main method used for testing.
	 * @param  the command-line arguments (args [0] gives number of keys to insert)
	 */
	public static void main(String [] args) {
		BpTreeMap<Integer, Integer> bpt = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 10;
		if (args.length == 1) {
			totKeys = Integer.valueOf(args[0]);
		}
		
		for (int i = 1; i < totKeys; i += 2) {
			bpt.put(i, i * i);
		}
		
		bpt.print(bpt.root, 0);
		
		for (int i = 0; i < totKeys; i++) {
			out.println("key = " + i + " value = " + bpt.get(i));
		} // for
		
		out.println("-------------------------------------------");
		out.println("Average number of nodes accessed = " + bpt.count / (double) totKeys);
	} // main
} // BpTreeMap class
