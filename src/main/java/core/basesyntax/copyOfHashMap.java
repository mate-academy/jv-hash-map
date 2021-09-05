package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class copyOfHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private int threshold;
    private Node<K,V> [] table;
    private final float loadFactor;
    private int size;

    /*making constructor*/
    public copyOfHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }


    private class Node<K,V> implements Map.Entry<K,V> {
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
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
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
                    && Objects.equals(value, node.value)
                    && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            int result = 13;
            return result * ((key != null) ? key.hashCode() : 0);
            /*mb need to include value as in real hashMap*/
        }
    }

    public static final int hash(Object key) {
        int hash;
        return (key == null) ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    @Override
    public void put(K key, V value) {
        putValue(hash(key), key, value, false, true);
    }
    public final V putValue(int hash, K key, V value,
                            boolean onlyIfAbsent, boolean evict) {
        Node<K, V>[] tab;
        Node<K, V> previosNodeInBucket;
        int n;
        int i;
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;              /*making it bigger in first iteration*/
        }
        if ((previosNodeInBucket = tab[i = (n - 1) & hash]) == null) {      /*check wheather plase in array is empty or not*/
            tab[i] = newNode(hash, key, value, null);    /*this if empty*/
        } else {
            Node<K, V> storeValueToRevrite;
            K k;
            if (previosNodeInBucket.hash == hash                      /*colision, and in hm realisation we deside */
                    && ((k = previosNodeInBucket.key) == key          /*about trees and so on, but i dont need it*/
                    || (key != null && key.equals(k)))) {
                storeValueToRevrite = previosNodeInBucket;
            } else {
                for (int binCount = 0; ; ++binCount) {
                    if ((storeValueToRevrite = previosNodeInBucket.next) == null) {
                        previosNodeInBucket.next = newNode(hash, key, value, null);
                        break;
                    }
                    if (storeValueToRevrite.hash == hash &&
                            ((k = storeValueToRevrite.key) == key || (key != null && key.equals(k))))
                        break;
                    previosNodeInBucket = storeValueToRevrite;
                }
            }
            if (storeValueToRevrite != null) { // existing mapping for key
                V oldValue = storeValueToRevrite.value;
                if (!onlyIfAbsent || oldValue == null)
                    storeValueToRevrite.value = value;
                return oldValue;
            }
        }
        if (++size > threshold)
            resize();
        return null;
    }

    public Node<K,V>[] transfer(Node<K,V>[] oldTab, int oldCap, Node<K,V>[] newTab) {

        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {      /*enter in every full bucket*/
                oldTab[j] = null;               /*destroy old node*/
                if (e.next == null) {           /*check if node has next linked to it*/
                    newTab[e.hash & (newTab.length -1)] = e;   /*if no -put node into bucket in new array. need to change for % length*/
                }
                else { // preserve order
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;     /*pass next from our node into temporary next*/
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
        return newTab;

    }
    public Node<K,V> [] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap = 0;
        int newThr = 0;
        if (oldCap > 0) {
            if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY     /*the resize itself*/
                    && oldCap >= DEFAULT_INITIAL_CAPACITY) {
                newThr = oldCap << 1;
            }
        } else {
            newCap = DEFAULT_INITIAL_CAPACITY;                                 /*if its first iteration*/
            newThr = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
        threshold = newThr;
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];  /*new bigger array*/
        table = newTab;
        if (oldTab != null) {           /*if we have smth to replase - replace it*/
            transfer(oldTab, oldCap, newTab);
        }
        return newTab;
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
        if ((tab = table) != null && (arrayLength = tab.length) > 0 &&
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
