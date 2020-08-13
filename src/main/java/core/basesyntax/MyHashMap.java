package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node[] table;
    private int size = 0;
    private int threshold;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> temp = table[indexFor(key)];
        if (temp == null) {
            temp = new Node(key, value, null);
        } else {
            if (Objects.equals(temp.key, key)) {
                temp.value = value;
                return;
            }
            while (temp != null || Objects.equals(key, temp.key)) {
                if (key == null && temp.key == null || Objects.equals(key, temp.key)) {
                    temp.value = value;
                    return;
                }
                temp = temp.next;
            }
            temp.next = new Node<>(key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node = table[indexFor(key)]; node != null;
             node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size > threshold) {
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * 2];
            threshold = (int) (table.length * LOAD_FACTOR);
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int indexFor(K key) {
        return key == null ? 0 : (key.hashCode()) & (table.length - 1);
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
