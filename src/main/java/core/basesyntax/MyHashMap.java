package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K,V> implements MyMap<K,V> {
    private final int defaultCapacity = 16;
    private final float loadFactor = 0.75f;
    private final int maxCapacity = Integer.MAX_VALUE;
    private Node<K,V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[defaultCapacity];
        threshold = (int) (defaultCapacity * loadFactor);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int indexNode = getIndex(key);
        Node<K,V> newNode = new Node<>(key, value, null);
        if (table[indexNode] == null) {
            table[indexNode] = newNode;
            size++;
        } else {
            Node<K,V> oldNode = table[indexNode];
            while (oldNode != null) {
                if (Objects.equals(key, oldNode.key)) {
                    oldNode.value = newNode.value;
                    return;
                }
                if (oldNode.next == null) {
                    oldNode.next = newNode;
                    size++;
                    return;
                }
                oldNode = oldNode.next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        threshold = threshold * 2;
        size = 0;
        Node<K,V>[] oldTable = table;
        table = new Node[table.length * 2];
        for (Node<K,V> node: oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        Node<K, V> node;
        for (node = table[getIndex(key)]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static final class Node<K,V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
