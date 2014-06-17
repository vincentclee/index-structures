#CSCI 4370/6370 Database Management

##Task List

###BpTreeMap.java
- [ ] public Set \<Map.Entry \<K,V>> entrySet()
- [ ] public K firstKey()
- [ ] public K lastKey()
- [ ] public SortedMap \<K,V> headMap(K toKey)
- [ ] public SortedMap \<K,V> tailMap(K fromKey)
- [ ] public SortedMap \<K,V> subMap(K fromKey, K toKey)
- [ ] public int size()
- [ ] private void insert(K key, V ref, Node n, Node p)
- [ ] private Node split(K key, V ref, Node n)

###ExtHashMap.java
- [ ] public Set \<Map.Entry \<K, V>> entrySet()
- [ ] public V get(Object key)
- [ ] public V put(K key, V value)
- [ ] private void print()

###LinHashMap.java
- [ ] public Set \<Map.Entry \<K, V>> entrySet()
- [ ] public V get(Object key)
- [ ] public V put(K key, V value)
- [ ] private void print()


##Project Information

###No.
2

###Description
Implement the following three Index Structures:

###Starter Code (must be used)
BpTreeMap.java, LinHashMap.java and ExtHashMap.java

###Comment
Use the indices to speed up the processing of Select and Join.

###Due
6/27
