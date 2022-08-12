package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_THRESHOLD = 12;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = DEFAULT_THRESHOLD;
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        if (table[hash % table.length] == null) {
            table[hash % table.length] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> currentNode = table[hash % table.length];
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                size--;
            } else if (currentNode.next != null) {
                while (currentNode.next != null) {
                    currentNode = currentNode.next;
                    if (Objects.equals(currentNode.key, key)) {
                        currentNode.value = value;
                        size--;
                        break;
                    }
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(hash, key, value, null);
                }
            } else {
                currentNode.next = new Node<>(hash, key, value, null);
            }
        }
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table[getBucket(key)] != null) {
            Node<K, V> element = table[getBucket(key)];
            while (element != null) {
                if (Objects.equals(element.key, key)) {
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

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
    }

    private void resize() {
        int newCapacity = table.length * 2;
        threshold = threshold * 2;
        Node<K, V>[] oldTab = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> element : oldTab) {
            while(element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }
    }

    private int getBucket(K key) {
        return Math.abs(hash(key) % table.length);
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
