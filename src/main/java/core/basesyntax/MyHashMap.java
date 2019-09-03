package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K, V>[] nodesArray;
    private int size = 0;
    private int defaultCapacity = 16;
    private double loadFactor = 0.75;

    public MyHashMap() {
        nodesArray = new Node[defaultCapacity];
    }

    public MyHashMap(int size) {
        if (size < defaultCapacity) {
            size = defaultCapacity;
        }
        nodesArray = new Node[size];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            Node<K, V> node;
            for (node = nodesArray[0]; node != null; node = node.next) {
                if (node.key == null) {
                    node.value = value;
                    return;
                }
            }
            nodesArray[0] = new Node<>(null, value, 0, node);
            size++;
        } else {
            if (size > nodesArray.length * loadFactor) {
                Node<K, V>[] newNodesArray = new Node[nodesArray.length * 3 / 2];
                Node<K, V> node;

                for (int i = 0; i < nodesArray.length; i++) {
                    node = nodesArray[i];
                    if (node == null) {
                        continue;
                    }

                    int index;
                    while (node != null) {
                        Node<K, V> next = node.next;
                        index = node.hash & (newNodesArray.length - 1);
                        node.next = newNodesArray[index];
                        newNodesArray[index] = node;
                        node = next;
                    }
                }
                this.nodesArray = newNodesArray;
            }

            int hash = key.hashCode();
            int index = hash & (nodesArray.length - 1);
            for (Node node = nodesArray[index]; node != null; node = node.next) {
                if (node.hash == hash
                        && (node.key == key || key.equals(node.key))) {
                    node.value = value;
                    return;
                }
            }
            Node<K, V> next = nodesArray[index];
            nodesArray[index] = new Node<>(key, value, hash, next);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            Node<K, V> node = nodesArray[0];
            while (node != null) {
                if (node.key == null) {
                    return node.value;
                }
                node = node.next;
            }
            throw new IndexOutOfBoundsException();
        }

        int hash = key.hashCode();
        int index = hash & (nodesArray.length - 1);
        Node<K, V> node = nodesArray[index];
        K tempKey;

        while (node != null) {
            tempKey = node.key;
            if (node.hash == hash && tempKey.equals(key)) {
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
