package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private int size = 0;

    private Node[] nodes;

    public MyHashMap() {
        this.nodes = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    public boolean timeToResize() {
        return size >= nodes.length * LOAD_FACTOR;
    }

    private void resize() {
        Node[] oldNodes = nodes;
        nodes = new Node[nodes.length * 2];
        size = 0;
        for (Node<K, V> node : oldNodes) {
            Node<K, V> item = node;
            while (item != null) {
                put(item.key, item.value);
                item = item.next;
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (timeToResize()) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = nodes[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value, nodes[index]);
        nodes[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = nodes[getIndex(key)];
        if (currentNode == null) {
            return null;
        }
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private int getIndex(K key) {
        return key == null ? 0 : key.hashCode() & (nodes.length - 1);
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {

        final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() {
            return key + "=" + value;
        }
    }
}
