package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private static final int BITMASK = 1;
    private static final int HASH_NULL = 0;

    private MyNode<K, V>[] table = (MyNode<K, V>[]) new MyNode[DEFAULT_INITIAL_CAPACITY];
    private int size;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int index;
    private int hash;

    @Override
    public void put(K key, V value) {
        hash = hashCheck(key);
        index = getIndex(hash);
        if (table[index] == null) {
            MyNode<K, V> newNode = new MyNode<>(key, value, hash);
            table[index] = newNode;
            size++;
            if (size > capacity * DEFAULT_LOAD_FACTOR) {
                resize();
            }
        } else {
            MyNode<K, V> current = table[index];
            while (current != null) {
                if (Objects.equals(current.key, key) && current.hash == hash) {
                    current.value = value;
                    break;
                } else {
                    if (current.next == null) {
                        current.next = new MyNode<>(key, value, hash);
                        size++;
                        if (size > capacity * DEFAULT_LOAD_FACTOR) {
                            resize();
                        }
                        break;
                    }
                    current = current.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        hash = hashCheck(key);
        index = getIndex(hash);
        MyNode<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key) && current.hash == hash) {
                return (V) current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        MyNode<K, V>[] newTable = (MyNode<K, V>[]) new MyNode[capacity * RESIZE_FACTOR];
        capacity = capacity * RESIZE_FACTOR;
        for (int i = 0; i < table.length; i++) {
            MyNode<K, V> current = table[i];
            while (current != null) {
                int index = current.hash & (capacity - BITMASK);
                MyNode<K, V> newCurrent = current.next;
                current.next = null;
                if (newTable[index] == null) {
                    newTable[index] = current;
                } else {
                    MyNode<K, V> old = newTable[index];
                    while (old.next != null) {
                        old = old.next;
                    }
                    old.next = current;
                }
                current = newCurrent;
            }
        }
        table = newTable;
    }

    private int getIndex(int hash) {
        int index = hash & (capacity - BITMASK);
        return index;
    }

    private int hashCheck(K key) {
        int hash;
        if (key == null) {
            hash = HASH_NULL;
        } else {
            hash = key.hashCode();
        }
        return hash;
    }

    private static class MyNode<K, V> {
        private K key;
        private V value;
        private MyNode<K, V> next;
        private int hash;

        public MyNode(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
