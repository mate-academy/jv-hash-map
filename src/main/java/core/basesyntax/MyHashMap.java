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
    private int capacity;
    private int size = 0;
    private int index;
    private Node<K, V> curNode;

    MyHashMap() {
        nodes = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size > 0.75 * capacity) {
            resize(nodes);
        }
        if (key == null) {
            index = 0;
        } else {
            index = Math.abs(key.hashCode()) % capacity;
        }
        if (nodes[index] == null) {
            nodes[index] = new Node<>(key, value);
        } else {
            curNode = nodes[index];
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
        if (key == null) {
            index = 0;
        } else {
            index = Math.abs(key.hashCode()) % capacity;
        }
        if (nodes[index] == null) {
            return null;
        }
        if (nodes[index].next == null) {
            return nodes[index].value;
        }
        curNode = nodes[index];
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
        nodes = new Node[capacity * 2];
        capacity *= 2;
        int size = this.size;
        for (Node<K, V> node : oldNodes) {
            if (node != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                    this.size = size;
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
