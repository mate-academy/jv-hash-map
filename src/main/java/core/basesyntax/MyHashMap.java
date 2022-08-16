package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int START_CAPACITY = 16;
    private static final int DEFAULT_ARRAY_EXPANSION = 2;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] heads;
    private int capacity;
    private int size;

    public MyHashMap() {
        capacity = START_CAPACITY;
        heads = new Node[START_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (heads[index] == null) {
            heads[index] = newNode;
            size++;
            return;
        }
        Node<K, V> tmp = heads[index];
        if (Objects.equals(tmp.key, key)) {
            tmp.value = value;
            return;
        }
        Node prev = null;
        while (tmp != null) {
            if (Objects.equals(tmp.key, key)) {
                tmp.value = newNode.value;
                return;
            }
            prev = tmp;
            tmp = tmp.next;
        }
        prev.next = newNode;
        tmp = newNode;
        size++;

    }

    @Override
    public V getValue(K key) {
        Node<K, V> tmp = heads[getIndex(key)];
        while (tmp != null) {
            if (Objects.equals(key, tmp.key)) {
                return tmp.value;
            }
            tmp = tmp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (((double)size / (double)capacity) >= LOAD_FACTOR) {
            Node<K, V>[] tmp = heads;
            capacity *= DEFAULT_ARRAY_EXPANSION;
            size = 0;
            Node<K, V>[] newHeads = new Node[capacity];
            heads = newHeads;
            for (int i = 0; i < heads.length / DEFAULT_ARRAY_EXPANSION; i++) {
                while (tmp[i] != null) {
                    put(tmp[i].key, tmp[i].value);
                    tmp[i] = tmp[i].next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
