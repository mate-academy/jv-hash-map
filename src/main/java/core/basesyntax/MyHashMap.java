package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    final static int INITIAL_CAPACITY = 16;
    final static float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int capacity;
    private Node<K, V>[] table;
    private int size;

    private int hash(K key) {
        return Math.abs((key == null) ? 0 : key.hashCode());
    }

    private int getBucketIndex(int hash) {
        return hash % capacity;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = hash(key);
        int index = getBucketIndex(hash);
        Node currentNode = table[index];
        if (bucketConditionIsNull(currentNode)) {
            table[index] = newNode(key, value);
            size++;
        } else {
            // якщо хеши ключів однакові, колізія
            if (hash((K) currentNode.key) == (hash(key))) {
                // перевіряємо за equals
                while (currentNode != null) {
                    if (currentNode.key == key || currentNode.key.equals(key)) {
                        // співпали - перезаписуєм ноду
                        currentNode.value = value;
                    } else {
                        // не співпали за equals, переходимо до наступної ноди (якщо вона є)
                        if (currentNode.next != null) {
                            currentNode = currentNode.next;
                        } else {
                            currentNode.next = newNode(key, value);
                            size++;
                        }
                    }
                    currentNode = currentNode.next;
                }
            }
            // якщо хеши ключів різні
            else {
                while (currentNode.next != null) {
                    currentNode = currentNode.next;
                }
                currentNode.next = newNode(key, value);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        return table[hash(key)].value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        if (table == null) {
            capacity = INITIAL_CAPACITY;
            threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
            table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        }
        if (size + 1 > threshold) {
            capacity = capacity * 2;
            setThreshold(threshold * 2);
            table = (Node<K, V>[]) new Node[capacity];
            // transfer(newTable);
        }
        return table;
    }

    private Node<K, V> newNode(K key, V value) {
        return new Node<>(hash(key), key, value, null);
    }


    private boolean bucketConditionIsNull(Node currentNode) {
        return currentNode == null ? true : false;
    }

    private Node<K, V>[] transfer(Node<K, V>[] newTable) {
        for (Node<K, V> node : table) {
            if (node != null) {
                hash(node.key);
            }

        }
        table = newTable;
        return table;
    }

    public int setThreshold(int threshold) {
        this.threshold = threshold;
        return threshold;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
