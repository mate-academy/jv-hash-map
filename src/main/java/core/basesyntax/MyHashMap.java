package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_RESIZE_MAP_FACTOR = 2;
    private int size;
    private Node<K, V>[] table;

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

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= (table.length * DEFAULT_LOAD_FACTOR)) {
            resize();
        }
        int hash = hash(key);
        int index = hash & (table.length - 1);
        if (table[index] == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
        } else {
            for (Node<K, V> node = table[index]; node != null; node = node.next) {
                if (node.hash == hash && Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(hash, key, value,null);
                    size++;
                    return;
                }
            }
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * DEFAULT_RESIZE_MAP_FACTOR];
        size = 0;
        for (Node<K, V> bucket : oldTable) {
            if (bucket != null) {
                for (Node<K, V> node = bucket; node != null; node = node.next) {
                    put(node.key, node.value);
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = (table.length - 1) & hash;
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
