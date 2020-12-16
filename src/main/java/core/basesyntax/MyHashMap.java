package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        threshold = (int) (LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V>[] tab = table;
        int elementIndex;
        if (tab == null || tab.length == 0) {
            resize();
        }
        elementIndex = Math.abs(hash(key) % tab.length);
        if (tab[elementIndex] == null) {
            tab[elementIndex] = new Node<>(key, value, null);
            size++;
            return;
        }
        Node<K, V> head = tab[elementIndex];
        while (head != null) {
            if (Objects.equals(head.key, key)) {
                head.value = value;
                break;
            }
            if (head.next == null) {
                head.next = new Node<>(key, value, null);
                size++;
                break;
            }
            head = head.next;
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> head;
        Node<K, V> returnValue = null;
        Node<K, V>[] tab = table;
        head = tab[Math.abs(hash(key) % tab.length)];
        while (head != null) {
            if (Objects.equals(key, head.key)) {
                return head.value;
            }
            head = head.next;
        }
        if (returnValue != null) {
            return returnValue.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        final Node<K, V>[] oldTab = table;
        threshold = threshold * 2;
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[table.length * 2];
        size = 0;
        table = newTab;
        if (oldTab != null) {
            for (Node<K, V> node : oldTab) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
        Node<K, V> next;
        private K key;
        private V value;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key + " = " + value;
        }

    }
}
