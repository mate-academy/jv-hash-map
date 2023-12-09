package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if(((int) (LOAD_FACTOR * table.length))==size){
            resize();
        }
        int index = indexByHash(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> entry = table[index];
        addNode(index, key, value,entry);
    }

    @Override
    public V getValue(K key) {
        int index = indexByHash(key);
        Node<K, V> node = table[index];
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

    private int indexByHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void addNode(int index, K key, V value, Node<K, V> node) {
        table[index] = new Node<>(key, value, node);
        size++;
    }

    private void resize() {
        Node<K, V>[] newTable = table;
        table = new Node[table.length << 1];
        size = 0;
        for (Node<K, V> node : newTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
