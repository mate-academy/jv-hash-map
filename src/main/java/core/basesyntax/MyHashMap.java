package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] nodes;
    private int size;

    MyHashMap() {
        nodes = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index;
        if (size > 0.75 * nodes.length) {
            resize(nodes);
        }
        if (key == null) {
            index = 0;
        } else {
            index = Math.abs(key.hashCode()) % nodes.length;
        }
        if (nodes[index] == null) {
            nodes[index] = new Node<>(key, value);
        } else {
            Node<K, V> curNode = nodes[index];
            while (curNode.next != null) {
                if (Objects.equals(curNode.key, key)) {
                    curNode.value = value;
                    return;
                }
                curNode = curNode.next;
            }
            if (Objects.equals(curNode.key, key)) {
                curNode.value = value;
                return;
            } else {
                curNode.next = new Node<>(key, value);
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index;
        if (key == null) {
            index = 0;
        } else {
            index = Math.abs(key.hashCode()) % nodes.length;
        }
        if (nodes[index] == null) {
            return null;
        }
        if (nodes[index].next == null) {
            return nodes[index].value;
        }
        Node<K, V> curNode = nodes[index];
        while (curNode != null) {
            if (Objects.equals(curNode.key, key)) {
                return curNode.value;
            }
            curNode = curNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize(Node<K, V>[] oldNodes) {
        nodes = new Node[nodes.length * 2];
        size = 0;
        for (Node<K, V> node : oldNodes) {
            if (node != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
