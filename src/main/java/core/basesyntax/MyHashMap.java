package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_INCREASE = 2;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int bucket = getBucket(key);
        Node<K, V> last = table[bucket];
        if (last == null) {
            table[bucket] = new Node<>(key, value, null);
            size++;
        } else {
            while (last != null) {
                if (Objects.equals(last.key, key)) {
                    last.value = value;
                    return;
                } else if (last.next == null) {
                    last.next = new Node<>(key, value, null);
                    size++;
                    break;
                } else {
                    last = last.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = getBucket(key);
        Node<K, V> node = table[bucket];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * CAPACITY_INCREASE];
        for (Node<K, V> old : oldTable) {
            while (old != null) {
                this.put(old.key, old.value);
                old = old.next;
            }
        }
    }

    private int getBucket(K key) {
        return Math.abs(getHash(key) % table.length);
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
