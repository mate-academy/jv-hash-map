package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_ARRAY_FACTOR = 2;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K,V>[])new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = getNode(key);
        if (node != null) {
            node.value = value;
            return;
        }
        node = new Node(key, value, null);
        setNewFirstNode(node);
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> e = getNode(key);
        return e == null ? null : e.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndexByKey(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode() % table.length));
    }

    private void resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = oldTab.length;
        threshold = threshold * DEFAULT_ARRAY_FACTOR;
        table = (Node<K,V>[])new Node[oldCap * DEFAULT_ARRAY_FACTOR];
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> node = oldTab[j];
            if (node != null) {
                oldTab[j] = null;
                while (node != null) {
                    setNewFirstNode(node);
                    node = node.next;
                }
            }
        }
    }

    private void setNewFirstNode(Node<K, V> node) {
        int index = getIndexByKey(node.key);
        Node<K, V> newNode = new Node<>(node.key, node.value, null);
        Node<K, V> first = table[index];
        if (first != null) {
            newNode.next = first;
        }
        table[index] = newNode;
    }

    private Node<K,V> getNode(K key) {
        int index = getIndexByKey(key);
        Node<K,V> node = table[index];
        if (node != null) {
            do {
                if (Objects.equals(key, node.key)) {
                    return node;
                }
            } while ((node = node.next) != null);
        }
        return null;
    }
}
