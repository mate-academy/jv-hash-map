package core.basesyntax;

import java.util.Objects;
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key, table.length);
        Node<K, V> current = table[index];
        if (current == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        Node<K, V> prev = null;
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            prev = current;
            current = current.next;
        }
        prev.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, table.length);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
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

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * RESIZE_MULTIPLIER;
        table = new Node[newCapacity];
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        size = 0;
        for (Node<K, V> headNode : oldTable) {
            while (headNode != null) {
                Node<K, V> nextNode = headNode.next;
                int index = getIndex(headNode.key, newCapacity);
                headNode.next = table[index];
                table[index] = headNode;
                size++;
                headNode = nextNode;
            }
        }
    }

    private int getIndex(K key, int length) {
        if (key == null) {
            return 0;
        }
        return (key.hashCode() & 0x7fffffff) % length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;
        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
