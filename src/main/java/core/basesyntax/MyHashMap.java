package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int STEP_INCREASE_CAPACITY = 2;
    private int threshold;
    private int size;
    private Node<K, V>[] map;

    public MyHashMap() {
        this.map = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int nodeIndex = getNodeIndex(key);
        Node<K, V> node = searchNode(key, nodeIndex);
        if (node != null) {
            node.value = value;
        } else {
            Node<K, V> newNode = new Node<>(key, value, map[nodeIndex]);
            map[nodeIndex] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = searchNode(key, getNodeIndex(key));
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldMap = map;
        map = new Node[DEFAULT_CAPACITY * STEP_INCREASE_CAPACITY];;
        for (Node<K, V> value : oldMap) {
            while (value != null) {
                put(value.key, value.value);
                value = value.next;
            }
        }
    }

    private int getNodeIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % map.length);
    }

    private Node<K, V> searchNode(K key, int nodeIndex) {
        Node<K, V> node = map[nodeIndex];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }
}
