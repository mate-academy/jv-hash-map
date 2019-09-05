package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K, V>[] nodes;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    public MyHashMap() {
        nodes = new Node[DEFAULT_CAPACITY];
    }

    private void putNullKey(V value) {
        while (nodes[0] != null) {
            if (nodes[0].key == null) {
                nodes[0].value = value;
                return;
            }
        }
        nodes[0] = new Node<>(null, value, 0, nodes[0]);
        size++;
    }

    private void reHash() {
        if (size > nodes.length * LOAD_FACTOR) {
            Node<K, V>[] newNodesArray = new Node[nodes.length * 2];
            Node<K, V> node;
            for (Node<K, V> i : nodes) {
                node = i;
                if (node == null) {
                    continue;
                }

                while (node != null) {
                    Node<K, V> next = node.next;
                    int index = node.hash & (newNodesArray.length - 1);
                    node.next = newNodesArray[index];
                    newNodesArray[index] = node;
                    node = next;
                }
            }
            this.nodes = newNodesArray;
        }
    }

    @Override
    public void put(K key, V value) {
        reHash();
        if (key == null) {
            putNullKey(value);
        } else {
            int hash = key.hashCode();
            int index = hash & (nodes.length - 1);
            for (Node node = nodes[index]; node != null; node = node.next) {
                if (node.hash == hash
                        && (node.key == key || key.equals(node.key))) {
                    node.value = value;
                    return;
                }
            }
            Node<K, V> next = nodes[index];
            nodes[index] = new Node<>(key, value, hash, next);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            Node<K, V> node = nodes[0];
            while (node != null) {
                if (node.key == null) {
                    return node.value;
                }
                node = node.next;
            }
            throw new IndexOutOfBoundsException();
        }

        int index = key.hashCode() & (nodes.length - 1);
        Node<K, V> node = nodes[index];
        while (node != null) {
            if (node.key.equals(key)) {
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

    private static class Node<K,V> {
        private final K key;
        private final int hash;
        private V value;
        Node<K,V> next;

        public Node(K key, V value, int hash, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        public final K getKey() {
            return this.key;
        }

        public final V getValue() {
            return this.value;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            Node<?, ?> node = (Node<?, ?>) object;
            return hash == node.hash
                    && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value)
                    && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, hash, value, next);
        }
    }
}
