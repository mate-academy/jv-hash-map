package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int buckets;
    private Node[] table;

    public MyHashMap() {
        this.table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        addNode(key, value);
    }

    @Override
    public V getValue(K key) {
        Node searchNode = table[getIndex(key, table.length)];
        while (true) {
            if (searchNode == null) {
                return null;
            }
            if (Objects.equals(key, searchNode.key)) {
                return (V) searchNode.value;
            }
            if (searchNode.next == null) {
                return null;
            }
            searchNode = searchNode.next;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addNode(K key, V value) {
        int index = getIndex(key, table.length);
        Node<K, V> node = new Node<>(getHash(key), key, value, null);
        if (table[index] == null) {
            table[index] = node;
            size++;
            buckets++;
        } else {
            Node searchNode = table[index];
            while (true) {
                if (Objects.equals(key, searchNode.key)) {
                    searchNode.value = value;
                    break;
                } else if (searchNode.next == null) {
                    searchNode.next = node;
                    size++;
                    break;
                }
                searchNode = searchNode.next;
            }
        }
    }

    private int getIndex(K key, int length) {
        return Math.abs(getHash(key) % length);
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode() / 13;
    }

    private void resize() {
        if (buckets >= table.length * LOAD_FACTOR) {
            Node[] oldTable = table;
            table = new Node[table.length * 2];
            size = 0;
            for (Node value : oldTable) {
                if (value != null) {
                    Node<K, V> node = value;
                    while (true) {
                        addNode(node.key, node.value);
                        if (node.next == null) {
                            break;
                        }
                        node = node.next;
                    }
                }
            }
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
