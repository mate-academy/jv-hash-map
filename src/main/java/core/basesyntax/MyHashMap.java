package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        size = 0;
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = hash(key, table.length);
        Node<K, V> lastNode = table[hash];
        while (lastNode != null) {
            if (Objects.equals(key, lastNode.key)) {
                lastNode.value = value;
                return;
            }
            if (lastNode.nextNode == null) {
                lastNode.nextNode = new Node<>(key, value, null);
                size++;
                return;
            }
            lastNode = lastNode.nextNode;
        }
        table[hash] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> nodeByKey = table[hash(key, table.length)];
        while (nodeByKey != null) {
            if (Objects.equals(key, nodeByKey.key)) {
                return nodeByKey.value;
            }
            nodeByKey = nodeByKey.nextNode;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size > table.length * LOAD_FACTOR) {
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * 2];
            size = 0;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.nextNode;
                }
            }
        }
    }

    private int hash(K key, int length) {
        return key != null ? Math.abs(key.hashCode() % length) : 0;
    }

    private static final class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }
}
