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
	
	/** Serialize */
	private static final long serialVersionUID = 1L;

	/** The maximum fanout for a B+Tree node. */
	private static final int ORDER = 5;
	
	/** The FLOOR(maximum fanout / 2) for a B+Tree node. */
	private static final int HALF = ORDER/2;
	
	/** The CEILING(maximum fanout / 2) for a B+Tree node. */
	private static final int MIDDLE = (int) Math.ceil(ORDER/2.0);
	
	/** The class for type K. */
	private final Class <K> classK;
	
	/** The class for type V. */
	@SuppressWarnings("unused")
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
	private Node root;
	
	/** The counter for the number nodes accessed (for performance testing). */
	private int count = 0;
	
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
	@SuppressWarnings("unchecked")
	public Set<Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> enSet = new LinkedHashSet<>();
		
		//set the starting node
		Node node = root;
		
		//go to first leaf
		while (!node.isLeaf) {
			node = (Node) node.ref[0];
		}
		
		//add entries until full
		while (node != null) {
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
		//correctly type key
		K keyy = (K) key;
		
		//set the starting node
		Node node = root;
		
		//find the value
		for (int i = 0; i < node.nKeys; i++) {
//			System.out.println(node.key[i]);
			
			//node is a leaf
			if (node.isLeaf) {
				//match
				if (keyy.compareTo(node.key[i]) == 0) {
					return (V) node.ref[i];
				} else if (keyy.compareTo(node.key[i]) < 0) { //key is less than node's current value
					return null;
				} continue;
			}
			
			//traverse non-leaf
			if (i == 0 && keyy.compareTo(node.key[i]) < 0) { //first reference
				node = (Node) node.ref[i];
				i = -1;
			} else if (i > 0 && keyy.compareTo(node.key[i-1]) >= 0 && keyy.compareTo(node.key[i]) < 0) { //go left
				node = (Node) node.ref[i];
				i = -1;
			} else if (i == node.nKeys-1) { //go right
				node = (Node) node.ref[i+1];
				i = -1;
			}
		}
		
		return null;
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
	public int size() {
		int sum = 0;
		
		//set the starting node
		Node node = root;
		
		//go to first leaf
		while (!node.isLeaf) {
			node = (Node) node.ref[0];
		}
		
		//loop through bottom row
		while (node != null) {
			sum += node.nKeys;
			
			//get next linked leaf
			node = (Node) node.ref[ORDER-1];
		}
		
		return sum;
	} // size
	
	private void debug() {
		System.out.println("first:" + firstKey());
		System.out.println("last:" + lastKey());
		System.out.println("size:" + size());
		System.out.println("entry:" + entrySet());
		
		System.out.println("ROOT" + Arrays.toString(root.key));
		System.out.println("ROOT" + Arrays.toString(root.ref));
	}
	
	/********************************************************************************
	 * Print the B+Tree using a pre-order traveral and indenting each level.
	 * @param n      the current node to print
	 * @param level  the current level of the B+Tree
	 */
	@SuppressWarnings("unchecked")
	private void print(Node n, int level) {
		if (level == 0) out.println("BpTreeMap");
		if (level == 0) out.println("-------------------------------------------");
		
		out.print(level);
		
		for (int j = 0; j < level; j++) {
			out.print("\t");
		}
		out.print("[.");
		for (int i = 0; i < n.nKeys; i++) {
			out.print(n.key [i] + ".");
		}
		
		out.println("]");
		
		if (!n.isLeaf) {
			for (int i = 0; i <= n.nKeys; i++) {
				print((Node) n.ref[i], level + 1);
			}
		} // if
		
		if (n.isLeaf && n.ref[ORDER - 1] == null) out.println ("-------------------------------------------");
	} // print
	
	/********************************************************************************
	 * Recursive helper function for finding a key in B+trees.
	 * @param key  the key to find
	 * @param ney  the current node
	 */
	@SuppressWarnings({ "unchecked", "unused" })
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
	 * Non-Recursive helper function for inserting a key in B+trees.
	 * @param key  the key to insert
	 * @param ref  the value/node to insert
	 * @param n    the current node
	 * @param p    the parent node
	 */
	private void insert(K key, V ref, Node n, Node p) {
		out.println("insert--" + key);
		
		//only one node
		if (root.isLeaf) {
			//empty or not full yet
			if (n.nKeys < ORDER - 1) {
				wedge(key, ref, n, 0);
			} else { //is full
				//key is a duplicate
				if (Arrays.stream(n.key).anyMatch(key::equals)) {
					wedge(key, ref, n, 0);
				} else { //not duplicate
					//split the node n
					Node right = split(key, ref, n);
					
					//push up first key of right to new root node
					root = new Node(false);
					root.key[0] = right.key[0];
					root.nKeys++;
					
					//link root to n and right
					root.ref[0] = n;
					root.ref[1] = right;
				}
			}
		} else { //more than one node
			//keep track of path through tree
			Stack<Node> stack = new Stack<Node>();
			
			//find leaf to insert on
			Node node = locate(key, stack);
			
			System.out.println("STACK: " + stack);
			
			//empty or not full yet
			if (node.nKeys < ORDER - 1) {
				wedge(key, ref, node, 0);
			} else { //is full
				//key is a duplicate
				if (Arrays.stream(node.key).anyMatch(key::equals)) {
					wedge(key, ref, node, 0);
				} else { //not duplicate
					//split the node n
					Node right = split(key, ref, node);
					
					//push up first key of right to parent node
					Node parent = null;
					
					//get parent
					parent = stack.pop();
					
					//not full parent add
					if (parent.nKeys < ORDER-1) {
						wedge(right.key[0], (V) right, parent, 1);
						return;
					}
					//full parent split
					else {
						System.out.println("Parent is full " + parent.nKeys);
						
						//split internal node
						Node rParent = new Node(false);
						K middleKey = iSplit(right.key[0], (V) right, parent, rParent);
						
						
						
						
						System.out.println("STACK CONTENTS: " + stack.size());
						
						//root
						if (parent == root) {
							Node newRoot = new Node(false);
							newRoot.key[0] = middleKey;
							newRoot.ref[0] = parent;
							newRoot.ref[1] = rParent;
							newRoot.nKeys++;
							
							//set new root
							root = newRoot;
							
							return;
						} else { // not root
							System.out.println("HI THERE");
							//TODO need to while loop untill stack is empty or root
							
//							//need to check
							parent = stack.pop();
//							
//							//link parent to right child
							wedge(middleKey, (V) rParent, parent, 1);
							return;
							
//							System.out.println(Arrays.toString(rParent.key));
//							System.out.println(Arrays.toString(rParent.ref));
//							System.out.println(middleKey);
						}
					}
				}
			}
		}
	} // insert
	
	/********************************************************************************
	 * Non-Recursive helper function for locating tuple where insertion will occur
	 * @param key  the key to insert
	 * @param ref  the value/node to insert
	 * @param n    the current node
	 * @param p    the parent node
	 */
	@SuppressWarnings("unchecked")
	private Node locate(K key, Stack<Node> stack) {
		//set the starting node
		Node node = root;
		
		//find the value
		for (int i = 0; i < node.nKeys; i++) {
			if (node.isLeaf) { //node is a leaf
				return node;
			} else if (i == 0 && key.compareTo(node.key[i]) < 0) { //first reference
				stack.push(node); //adds parent to stack
				node = (Node) node.ref[i];
				i = -1;
			} else if (i > 0 && key.compareTo(node.key[i-1]) >= 0 && key.compareTo(node.key[i]) < 0) { //go left
				stack.push(node);
				node = (Node) node.ref[i];
				i = -1;
			} else if (i == node.nKeys-1) { //go right
				stack.push(node);
				node = (Node) node.ref[i+1];
				i = -1;
			}
		}
		
		System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
		
		//not really possible
		return null;
	} // locate
	
	/********************************************************************************
	 * Wedge the key-ref pair into node n.
	 * @param key  the key to insert
	 * @param ref  the value/node to insert
	 * @param n    the current node
	 * @param i    the insertion position offset within node n
	 */
	@SuppressWarnings("unchecked")
	private void wedge(K key, V ref, Node n, int i) {
		//empty Node
		if (n.nKeys == 0) {
			n.key[n.nKeys] = key;
			n.ref[n.nKeys] = ref;
			
			//increment # of keys
			n.nKeys++;
		} 
		//not empty nodes
		else {
			//Map to store node key and ref pairs
			Map<K, V> sortedMap = new TreeMap<K, V>();
			
			//Add node current pairs to map
			for (int j = 0; j < n.nKeys; j++) {
				sortedMap.put(n.key[j], (V) n.ref[j+i]);
			}
			//Add key and ref to add
			sortedMap.put(key, ref);
			
			//set # of keys
			n.nKeys = sortedMap.size();
			
			/*
			 * Override values with sorted values
			 * src, srcPos, dest, destPos, length
			 */
			System.arraycopy(sortedMap.keySet().toArray(), 0, n.key, 0, n.nKeys);
			System.arraycopy(sortedMap.values().toArray(), 0, n.ref, i, n.nKeys);
		}
	} // wedge
	
	/********************************************************************************
	 * Split internal node n and return the key to be bubbled up the tree
	 * @param key  the key to insert
	 * @param ref  the value/node to insert
	 * @param left    the current node
	 * @param right    the newer node
	 */
	private K iSplit(K key, V ref, Node left, Node right) {
		//Map to store node key and ref pairs
		Map<K, V> sortedMap = new TreeMap<K, V>();
		
		//Add node current pairs to map
		for (int j = 0; j < left.nKeys; j++) {
			sortedMap.put(left.key[j], (V) left.ref[j+1]);
		}
		//Add key and ref to add
		sortedMap.put(key, ref);
		
		//clean array - a, fromIndex, toIndex, val
		Arrays.fill(left.key, 0, ORDER - 1, null);
		Arrays.fill(left.ref, 1, ORDER, null);
		
		//left - src, srcPos, dest, destPos, length
		System.arraycopy(sortedMap.keySet().toArray(), 0, left.key, 0, HALF);
		System.arraycopy(sortedMap.values().toArray(), 0, left.ref, 1, HALF);
		left.nKeys = HALF;
		
		//right - src, srcPos, dest, destPos, length
		System.arraycopy(sortedMap.keySet().toArray(), MIDDLE, right.key, 0, HALF);
		System.arraycopy(sortedMap.values().toArray(), MIDDLE-1, right.ref, 0, HALF+1);
		right.nKeys = HALF;
		
		System.out.println("left" + Arrays.toString(left.key));
		System.out.println("left" + Arrays.toString(left.ref));
		System.out.println(left.nKeys);
		System.out.println("right" + Arrays.toString(right.key));
		System.out.println("right" + Arrays.toString(right.ref));
		System.out.println(right.nKeys);
		
		System.out.println("MIDDLER: " + (K) sortedMap.keySet().toArray()[MIDDLE -1]);
		
		
		//DO NOT TOUCH
		return (K) sortedMap.keySet().toArray()[MIDDLE -1];
	} // split
	
	/********************************************************************************
	 * Split node n and return the newly created node.
	 * @param key  the key to insert
	 * @param ref  the value/node to insert
	 * @param n    the current node
	 */
	@SuppressWarnings("unchecked")
	private Node split(K key, V ref, Node n) {
		/*
		 * Node n START
		 * [3, 5, 7, 9]
		 * [9, 25, 49, 81, null]
		 * key = 1
		 * ref = 1
		 */
		
		//Creates a new right node
		Node right = new Node(n.isLeaf);
		
		//Map to store node key and ref pairs
		Map<K, V> sortedMap = new TreeMap<K, V>();
		
		//Add node current pairs to map
		for (int i = 0; i < n.nKeys; i++) {
			sortedMap.put(n.key[i], (V) n.ref[i]);
		}
		//Add key and ref to insert
		sortedMap.put(key, ref);
		
		/*
		 * Fill with null to 'n'
		 * a, fromIndex, toIndex, val
		 * [3, 5, null, null]
		 * [9, 25, null, null, null]
		 */
		Arrays.fill(n.key, HALF, ORDER - 1, null);
		Arrays.fill(n.ref, HALF, ORDER - 1, null);
		
		/*
		 * Copy over first half to 'n'
		 * src, srcPos, dest, destPos, length
		 * [1, 3, null, null]
		 * [1, 9, null, null, null]
		 */
		System.arraycopy(sortedMap.keySet().toArray(), 0, n.key, 0, HALF);
		System.arraycopy(sortedMap.values().toArray(), 0, n.ref, 0, HALF);
		n.nKeys = HALF; //set n.nKeys
		
		/*
		 * Copy over rest to 'right'
		 * src, srcPos, dest, destPos, length
		 * [5, 7, 9, null]
		 * [25, 49, 81, null, null]
		 */
		System.arraycopy(sortedMap.keySet().toArray(), HALF, right.key, 0, ORDER-HALF);
		System.arraycopy(sortedMap.values().toArray(), HALF, right.ref, 0, ORDER-HALF);
		right.nKeys = ORDER-HALF; //set right.nKeys
		
		//link n to right
		if (n.isLeaf && right.isLeaf) {
			System.out.println("both are leaf");
			//right -> (n.next)
			right.ref[ORDER - 1] = n.ref[ORDER - 1];
			
			//n -> right
			n.ref[ORDER - 1] = right;
			
		}
		
		return right;
	} // split
	
	/********************************************************************************
	 * The main method used for testing.
	 * @param  the command-line arguments (args [0] gives number of keys to insert)
	 */
	public static void main(String [] args) {
		BpTreeMap<Integer, Integer> bpt = new BpTreeMap<>(Integer.class, Integer.class);
		int totKeys = 37;
		if (args.length == 1) {
			totKeys = Integer.valueOf(args[0]);
		}
		
		for (int i = 1; i < totKeys; i++) {
			bpt.put(i, i * i);
		}
		
//		bpt.put(3, 9);
//		bpt.put(7, 49);
//		bpt.put(9, 81);
//		bpt.put(5, 25);
//		bpt.put(1, 1);
//		bpt.put(11, 121);
//		bpt.put(13, 169);
//		bpt.put(2, 4);
//		bpt.put(0, 0);
//		bpt.put(8, 64);
//		bpt.put(6, 36);
//		bpt.put(10, 100);
//		bpt.put(4, 16);
//		bpt.put(14, 1414);
//		bpt.put(12, 1212);
//		bpt.put(15, 1515);
//		bpt.put(16, 1616);
//		bpt.put(19, 1919);
//		bpt.put(21, 2121);
//		bpt.put(20, 2020);
//		bpt.put(20, 2021);
//		bpt.put(23, 2323);
//		bpt.put(22, 2222);
//		bpt.put(24, 2424);
//		bpt.put(25, 2525);
		
		bpt.debug();
		bpt.print(bpt.root, 0);
		
		for (int i = 0; i < 50; i++) {
			Integer value = bpt.get(i);
			if (value != null)
				out.println("key = " + i + " value = " + value);
		} // for
		
//		for (int i = 0; i < totKeys; i++) {
//			out.println("key = " + i + " value = " + bpt.get(i));
//		} // for
		
		out.println("-------------------------------------------");
		out.println("Average number of nodes accessed = " + bpt.count / (double) totKeys);
	} // main
} // BpTreeMap class
