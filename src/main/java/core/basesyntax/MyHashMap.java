package core.basesyntax;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private Node<K,V>[] hashTable;
    private static final int DEFAULT_CAPACITY = 1 << 4;
    public static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final double LOAD_FACTOR = 0.75f;
    private int size;

    public MyHashMap() {
        size = 0;
        hashTable = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {

    }

    static class Node<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        private Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash &&
                    Objects.equals(nodes, node.nodes) &&
                    Objects.equals(key, node.key) &&
                    Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            hash = 31;
            hash *= 17 + key.hashCode();
            hash *= 17 + value.hashCode();
            hash += nodes.hashCode();
            return hash;
        }
        private int hash() {
            return hashCode() % hashTable.length;
        }
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }


    public static void main(String[] args) {
        System.out.println(2000 >>> 16);
    }
}
