package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;
    private int capacity;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        capacity = DEFAULT_INITIAL_CAPACITY;
    }

    static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] tempTable = table;
        capacity = capacity * 2;
        table = new Node[capacity];
        for (Node<K, V> e : tempTable) {
            while (e != null) {
                put(e.key, e.value);
                e = e.next;
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= (capacity * DEFAULT_LOAD_FACTOR)) {
            resize();
        }
        int index = hash(key);
        Node<K, V> nodeToPut = new Node<>(index, key,value, null);
        Node<K, V> current = table[index];
        if (current == null) {
            table[index] = nodeToPut;
            size++;
        } else {
            while (current != null) {
                if ((current.key == null && key == null)
                        || (current.hash == index && (Objects.equals(key, current.key)))) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            current.next = nodeToPut;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == index && (Objects.equals(key, current.key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
