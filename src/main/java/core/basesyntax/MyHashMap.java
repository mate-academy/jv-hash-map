package core.basesyntax;

import java.util.HashMap;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private static final int GROW_VALUE = 2;
    private int size;
    Node<K, V>[] table;

    class Node<K, V> {
        Node<K, V> next;
        final K key;
        V value;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = hash(key);
        Node<K, V> currentNode = table[hash];

        while(currentNode != null) {
            if (Objects.equals(key, currentNode.key)){
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        table[hash] = new Node<>(key, value, table[hash]);
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K, V> node = table[hash];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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
        int threshold = (int) (table.length * LOAD_FACTOR);
        if (getSize() >= threshold) {
            transferNodes(table.length * GROW_VALUE);
        }
    };

    private void transferNodes(int newCapacity) {
        Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = new Node[newCapacity];
        table = newTable;
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key,node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return  key == null ? 0 : (Math.abs(key.hashCode()) % table.length);
    }
}
