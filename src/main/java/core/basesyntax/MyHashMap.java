package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int ENCREASE_FACTOR = 2;
    private Node<K,V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.size = 0;
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        for (Node<K, V> element = table[getIndex(key)]; element != null; element = element.next) {
            if ((element.hash == hash(key)) && ((element.key == null && key == null)
                    || (element.key != null && element.key.equals(key)))) {
                element.value = value;
                return;
            }
        }

        Node<K, V> newNode = table[getIndex(key)];
        table[getIndex(key)] = new Node<>(hash(key), key, value, newNode);
        size++;
        if (size > threshold) {
            resize();
        }
    }

    public V getValue(K key) {
        int indexInTable = getIndex(key);

        if (table != null) {
            Node<K, V> element = table[indexInTable];

            while (element != null) {
                if (element.hash == hash(key) && Objects.equals(element.key, key)) {
                    return element.value;
                }
                element = element.next;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length * ENCREASE_FACTOR];
        threshold *= ENCREASE_FACTOR;
        size = 0;

        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(K key) {
        int hash = hash(key);
        return hash & (table.length - 1);
    }

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
}
