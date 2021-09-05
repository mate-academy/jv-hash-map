package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_INDEX = 2;

    private int threshold;
    private Node<K,V> [] initialStorage;
    private float loadFactor;
    private int size;

    /*making constructor*/
    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        this.initialStorage = (Node<K, V>[]) new Node [DEFAULT_INITIAL_CAPACITY];
    }
    

    private class Node<K,V>  {
        private int hash;
        private final K key;
        private V value;
        Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key + " = " + value;
        }


        @Override
        public boolean equals(Object o) {                /*mb need to rewrite it somehow*/
            if (this == o) {
                return true;
            }
            if (o.getClass().equals(Map.Entry.class)) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash &&
                    Objects.equals(key, node.key)
                    && Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            int result = 13;
            return result * ((key != null) ? key.hashCode() : 0);
        }
    }

   public static final int hash(Object key) {
        int hash;
        return (key == null) ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> previousNodeInBucket;
        int bucketNumber = key.hashCode() % initialStorage.length;
      if (initialStorage[bucketNumber] == null ) {
          initialStorage[bucketNumber] = newNode(key.hashCode(), key, value, null);
      } else {
          previousNodeInBucket = initialStorage[bucketNumber];
          while(previousNodeInBucket.next != null) {
              if (Objects.equals(previousNodeInBucket.key, key)) {
                  previousNodeInBucket.value = value;
              }
              previousNodeInBucket = previousNodeInBucket.next;
          }
          previousNodeInBucket.next = newNode(key.hashCode(), key, value, null);
      }
      if (++size > threshold) {
          resize();
      }
    }

    public Node<K,V>[] transfer(Node<K,V>[] oldTab, int oldCap, Node<K,V>[] newTab) {

        for (int i = 0; i < oldCap; i++) {
            if (oldTab[i] != null) {
                if(oldTab[i].next == null) {
                    newTab[oldTab[i].hashCode() % newTab.length] = oldTab[i];
                }
                else {
                    while (oldTab[i] == null) {
                        newTab[oldTab[i].hashCode() % newTab.length] = oldTab[i];
                        oldTab[i] = oldTab[i].next;
                    }
                }
            }
        }
        return newTab;
    }
    public Node<K,V> [] resize() {
        Node<K,V>[] oldStorage = initialStorage;
        int oldCapacity = oldStorage.length;
        int oldThreshold = threshold;
        int newCapacity = oldCapacity * RESIZE_INDEX;
        int newThreshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        threshold = newThreshold;
        Node<K, V>[] newStorage = (Node<K, V>[]) new Node[newCapacity];
        initialStorage = newStorage;
        if (oldStorage != null) {
            transfer(oldStorage, oldCapacity, newStorage);
        }
        return newStorage;
    }

    @Override
    public V getValue(K key) {
    Node<K,V> nodeToGetFrom;
        return (nodeToGetFrom = getNode(key)) == null ? null : nodeToGetFrom.value;
    }

    public Node<K,V> getNode(Object key) {
        Node<K,V>[] tab;
        Node<K,V> first, e;
        int arrayLength, hash;
        K k;
        if ((tab = initialStorage) != null && (arrayLength = tab.length) > 0 &&
                (first = tab[(arrayLength - 1) & (hash = hash(key))]) != null) {             /*some condition*/
            if (first.hash == hash && // always check first node
                    ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<> (hash, key, value,next);
    }


}
