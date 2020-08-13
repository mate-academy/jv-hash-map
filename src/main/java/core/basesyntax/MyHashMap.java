package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.table = (Node<K,V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (LOAD_FACTOR * table.length);
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value, null);
        int index = indexOf(key);

        if (size >= threshold) {
            resize();
        }

        if (table[index] == null) {
            table[index] = node;
        } else {
            Node<K, V> tmp = table[index];
            while (tmp != null) {
                if (Objects.equals(tmp.key, key)) {
                    tmp.value = value;
                    return;
                }
                if (tmp.next == null) {
                    tmp.next = node;
                    size++;
                    return;
                }
                tmp = tmp.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = indexOf(key);

        if (table[index] == null) {
            return null;
        }
        Node<K, V> tmp = table[index];
        while (tmp.next != null) {
            if (Objects.equals(tmp.key, key)) {
                return tmp.value;
            }
            tmp = tmp.next;
        }
        return tmp.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[oldTable.length * 2];
        size = 0;
        threshold = (int) (LOAD_FACTOR * table.length);

        for (Node<K, V> node : oldTable) {
            if (node != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int indexOf(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

}
