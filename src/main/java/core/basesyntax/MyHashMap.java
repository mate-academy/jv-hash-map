package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> listNode = table[index];
        while (true) {
            if (table[index] == null) {
                table[index] = newNode;
                size++;
                break;
            }
            if (Objects.equals(newNode.key, listNode.key)) {
                listNode.value = newNode.value;
                return;
            }
            if (listNode.next == null) {
                listNode.next = newNode;
                size++;
                break;
            }
            listNode = listNode.next;
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> resultNode = table[hash(key)];
        while (resultNode != null) {
            if (Objects.equals(resultNode.key, key)) {
                return resultNode.value;
            }
            resultNode = resultNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int h = key == null ? 0 : key.hashCode() % table.length;
        return h < 0 ? -h : h;
    }

    private void resize() {
        threshold = threshold * 2;
        size = 0;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length * 2];
        for (Node<K, V> oldNode : oldTable) {
            while (oldNode != null) {
                put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
            }
        }
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
