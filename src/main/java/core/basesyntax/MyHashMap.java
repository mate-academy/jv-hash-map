package core.basesyntax;

import java.util.Objects;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; //16
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putValue(hash(key), key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putValue(int hash, K key, V value) {
        if (table[hash] == null) {
            table[hash] = new Node<>(key, value, null);
        } else {
            Node<K, V> currentNode = table[hash];
            while (currentNode.next != null) {
                if (Objects.equals(key, currentNode.key)) {
                    break;
                }
                currentNode = currentNode.next;
            }
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                size--;
            } else {
                currentNode.next = new Node<>(key, value, null);
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % 16);
    }

    private void resize() {
        int newCapacity = table.length;
        newCapacity = newCapacity << 1;
        threshold = (int) (DEFAULT_LOAD_FACTOR * newCapacity);
        Node<K, V>[] oldTab = table;
        table = new Node[newCapacity];
        for (Node<K, V> node : oldTab) {
            Node<K, V> newNode = node;
            while (newNode != null) {
                putValue(hash(newNode.key), newNode.key, newNode.value);
                newNode = newNode.next;
            }
        }
    }

    public static class Node<K,V> {
        private final K key;
        private V value;
        private MyHashMap.Node<K,V> next;

        Node(K key, V value, MyHashMap.Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
