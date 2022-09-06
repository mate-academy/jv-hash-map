package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K,V> node = new Node<>(key, value,null);
        int index = hash(key);
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    break;
                }
                if (currentNode.next == null) {
                    currentNode.next = node;
                    size++;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
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

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length << 1;
        threshold = (int) (LOAD_FACTOR * newCapacity);
        table = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    class Node<K,V> {
    final K key;
    V value;
    Node<K,V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
