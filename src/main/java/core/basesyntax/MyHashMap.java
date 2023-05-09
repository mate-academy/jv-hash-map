package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K,V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int hash = hash(key);
        Node<K, V> temporaryNode = table[hash];
        Node<K, V> newNode = new Node<K, V>(key, value);
        if (temporaryNode == null) {
            table[hash] = newNode;
        } else {
            while (temporaryNode != null) {
                if (Objects.equals(key, temporaryNode.key)) {
                    temporaryNode.value = value;
                    return;
                }
                if (temporaryNode.next == null) {
                    temporaryNode.next = newNode;
                    break;
                }
                temporaryNode = temporaryNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> temporaryNode = table[hash(key)];
        if (temporaryNode == null) {
            return null;
        }
        while (temporaryNode != null) {
            if ((Objects.equals(key, temporaryNode.key))) {
                return temporaryNode.value;
            } else {
                temporaryNode = temporaryNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K,V>[] oldTab = table;
        table = new Node[table.length << 1];
        size = 0;
        for (Node<K,V> node : oldTab) {
            if (node != null) {
                put(node.key, node.value);
                while (node.next != null) {
                    put(node.next.key, node.next.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
