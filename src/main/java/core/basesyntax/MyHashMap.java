package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float LOAD_FACTOR = 0.75f;
    private transient Node<K, V>[] table;
    private int size;
    private int capacity;
    private double threshhold;

    MyHashMap() {
        table = new Node[16];
        size = 0;
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshhold = capacity * LOAD_FACTOR;
    }

    static final int hash(Object key) {
        int h;
        return key == null ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    static class Node<K,V> implements Map.Entry<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public int getHash() {
            return hash;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public void setNext(Node<K, V> next) {

            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshhold) {
            resize();
        }
        int index = hash(key);
        index = Math.abs(index) % capacity;
        if (table[index] == null) {
            table[index] = new Node<>(hash(key), key, value, null);
        } else {
            Node<K, V> temp = table[index];
            if (Objects.equals(temp.key,key)) {
                temp.value = value;
                return;
            }
            while (temp.next != null) {
                if (Objects.equals(temp.next.key,key)) {
                    temp.next.value = value;
                    return;
                }
                temp = temp.next;
            }
            temp.next = new Node<>(hash(key), key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(hash(key)) % capacity;
        if (table[index] == null) {
            return null;
        }
        Node<K,V> temp = table[index];
        if (Objects.equals(temp.key,key)) {
            return temp.value;
        }
        while (temp.next != null) {
            if (Objects.equals(temp.next.key, key)) {
                return temp.next.value;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] temp = table;
        table = new Node[capacity = capacity << 1];
        threshhold = capacity * LOAD_FACTOR;
        int tempSize = size;
        transfer(temp);
        size = tempSize;
    }

    private void transfer(Node<K, V>[] temp) {
        for (Node<K, V> node : temp) {
            if (node != null) {
                put(node.key,node.value);
                while (node.next != null) {
                    put(node.next.key, node.next.value);
                    node.next = node.next.next;
                }
            }
        }
    }
}
