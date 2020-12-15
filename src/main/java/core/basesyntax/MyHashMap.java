package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float LOAD_FACTOR = 0.75f;
    Node<K, V>[] table;
    int threshold;
    private int size;

    public MyHashMap() {
    }

    @Override
    public void put(K key, V value) {
        putValue(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            Node<K, V> head;
            Node<K, V> returnValue = null;
            Node<K, V>[] tab = table;
            head = tab[Math.abs(hash(key) % tab.length)];
            while (head != null) {
                if ((key == head.key && key == null) || (key != null && key.equals(head.key))) {
                    returnValue = head;
                    break;
                }
                head = head.next;
            }
            return returnValue.value;
        } else {
            return null;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putValue(int hash, K key, V value) {
        Node<K, V>[] tab = table;
        int elementIndex;
        if (tab == null || tab.length == 0) {
            tab = resize();
        }
        elementIndex = Math.abs(hash(key) % tab.length);
        if (tab[elementIndex] == null) {
            tab[elementIndex] = new Node<>(hash, key, value, null);
            size++;
        } else {
            Node<K, V> head = tab[elementIndex];
            while (head != null) {
                if (Objects.equals(head.key, key)) {
                    head.value = value;
                    break;
                }
                if (head.next == null) {
                    head.next = new Node<>(hash, key, value, null);
                    size++;
                    break;
                }
                head = head.next;
            }
        }
        if (size > threshold) {
            resize();
        }
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;
        int oldCapacity = (oldTab == null) ? 0 : oldTab.length;
        int oldThreshold = threshold;
        int newCapacity;
        int newThreshold;
        if (oldCapacity != 0) {
            newCapacity = oldCapacity * 2;
            newThreshold = oldThreshold * 2;
        } else {
            newCapacity = DEFAULT_INITIAL_CAPACITY;
            newThreshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
        }
        threshold = newThreshold;
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        table = newTab;
        if (oldTab != null) {
            for (Node<K, V> node : oldTab) {
                while (node != null) {
                    putValue(node.hash, node.key, node.value);
                    node = node.next;
                }
            }
        }
        return newTab;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : (key.hashCode());
    }

    private static class Node<K, V> {
        Node<K, V> next;
        private int hash;
        private K key;
        private V value;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
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
