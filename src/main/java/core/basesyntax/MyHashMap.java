package core.basesyntax;

import java.util.HashSet;

import static java.lang.Math.abs;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private HashObject<K, V>[] hashMap;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int size = 0;

    public MyHashMap() {
        hashMap = new HashObject[DEFAULT_INITIAL_CAPACITY];
    }

    public K[] keySet() {
        if (size == 0) {
            return null;
        } else {
            K[] keys = (K[]) new Object[size];
            int index = 0;
            for (HashObject<K, V> object : hashMap) {
                if (object != null) {
                    HashObject<K,V> currnetNode = object;
                    do {
                        keys[index] = (K) currnetNode.key;
                        currnetNode = currnetNode.next;
                        index++;
                    } while (currnetNode != null);
                }
            }
            return keys;
        }
    }

    public boolean containsKey(K key) {
        if (size == 0) {
            return false;
        }
        int ifItWasFound = 0;
        for (K currentKey : keySet()) {
            if ((key != null && currentKey != null && currentKey.equals(key))
                    || key == null && currentKey == null) {
                ifItWasFound++;
            }
        }
        return ifItWasFound != 0;
    }

    public V[] values() {
        V[] values = (V[]) new Object[size];
        return values;
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

    private HashObject getHashObject(K key) {
        HashObject currentNode = hashMap[keyIndex(key)];
        while (!((currentNode.key != null && key != null && currentNode.key.equals(key))
                || (currentNode.key == null && key == null))) {
            currentNode = currentNode.next;
        }
        return currentNode;
    }


    private void enlarge() {
        MyHashMap<K,V> temporaryNewHashMap = new MyHashMap<>();
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
            HashObject currentNode = hashMap[keyIndex(key)];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = new HashObject<>(key, value, null);
        }
        size++;
    }

    private void set(K key, V value) {
        getHashObject(key).value = value;
    }
}
