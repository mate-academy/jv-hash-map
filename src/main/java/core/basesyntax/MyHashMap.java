package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int index = indexFor(hash(key), table.length);
        for (Node<K,V> elem = table[index]; elem != null; elem = elem.next) {
            if (elem.hash == hash(key) && (key.equals(elem.key))) {
                elem.value = value;
                return;
            }
        }
        addEntry(hash(key), key, value, index);
    }

    @Override
    public V getValue(K key) {
        int index = indexFor(hash(key), table.length);
        for (Node<K,V> elem = table[index]; elem != null; elem = elem.next) {
            if (elem.hash == hash(key) && (Objects.equals(key, elem.key))) {
                return elem.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = 2 * table.length;
        Node<K,V>[] newTable = new Node[newCapacity];
        Node<K,V>[] source = table;
        int rehashCapacity = newTable.length;

        for (int newPos = 0; newPos < source.length; newPos++) {
            Node<K, V> element = source[newPos];
            while (element != null) {
                Node<K, V> next = element.next;
                int i = indexFor(element.hash, rehashCapacity);
                element.next = newTable[i];
                newTable[i] = element;
                element = next;
            }
        }
        table = newTable;
        threshold = (int)(newCapacity * DEFAULT_LOAD_FACTOR);
    }

    private void addEntry(int hash, K key, V value, int bucketIndex) {
        Node<K,V> elem = table[bucketIndex];
        table[bucketIndex] = new Node<>(hash, key, value, elem);
        if (size++ >= threshold) {
            resize();
        }

    }

    private void putForNullKey(V value) {
        for (Node<K,V> elem = table[0]; elem != null; elem = elem.next) {
            if (elem.key == null) {
                elem.value = value;
                return;
            }
        }
        addEntry(0,null, value,0);
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private static int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
