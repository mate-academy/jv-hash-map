package core.basesyntax;

import static java.awt.AWTEventMulticaster.add;
import static java.lang.Math.abs;
import static java.lang.reflect.Array.set;

import java.util.HashSet;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private HashObject<K, V>[] hashMap;
    private HashSet<K> keys;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int size = 0;

    public MyHashMap() {
        hashMap = new HashObject[DEFAULT_INITIAL_CAPACITY];
        keys = new HashSet<K>();
    }

    public HashSet<K> keySet() {
        return keys;
    }

    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    @Override
    public void put(K key, V value) {
        if (!containsKey(key)) {
            add(key, value);
        } else {
            set(key, value);
        }
    }

    @Override
    public V getValue(K key) {

        if (!containsKey(key)) {
            return null;
        } else {
            return (V) getHashObject(key).value;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class HashObject<K, V> {
        private K key;
        private V value;
        private HashObject<K, V> next;

        public HashObject(K key, V value, HashObject<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private HashObject<K, V> getHashObject(K key) {
        HashObject<K, V> currentNode = hashMap[keyIndex(key)];
        while (!((currentNode.key != null && key != null && currentNode.key.equals(key))
                || (currentNode.key == null && key == null))) {
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    private void enlarge() {
        MyHashMap<K, V> temporaryNewHashMap = new MyHashMap<>();
        temporaryNewHashMap.hashMap = new HashObject[capacity * 2];

        for (K key : keySet()) {
            temporaryNewHashMap.put(key, getValue(key));
        }

        hashMap = temporaryNewHashMap.hashMap;
    }

    private int keyIndex(K key) {
        if (key == null) {
            return 0;
        } else {
            return abs(key.hashCode()) % capacity;
        }
    }

    private void add(K key, V value) {
        if (size == (int) (capacity * DEFAULT_LOAD_FACTOR)) {
            enlarge();
        }
        if (hashMap[keyIndex(key)] == null) {
            hashMap[keyIndex(key)] = new HashObject<>(key, value, null);
        } else {
            HashObject<K, V> currentNode = hashMap[keyIndex(key)];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = new HashObject<K, V>(key, value, null);
        }
        size++;
        keys.add(key);
    }

    private void set(K key, V value) {
        getHashObject(key).value = value;

    }
}
