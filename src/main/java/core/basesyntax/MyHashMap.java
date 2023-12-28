package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int currentCapacity;
    private int size;
    private Node<K, V>[] table;

    {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        currentCapacity = table.length;
    }

    @Override
    public void put(K key, V value) {
        int hashCode = key == null ? 0 : key.hashCode();
        int index = Math.abs(hashCode % currentCapacity);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> current = table[index];
            Node<K, V> nextNode = table[index];

            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;

                if (nextNode.next != null) {
                    nextNode = nextNode.next;
                }
            }

            nextNode.next = new Node<>(key, value, null);
            size++;
        }

        if (size >= currentCapacity * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hashCode = key == null ? 0 : key.hashCode();
        int index = Math.abs(hashCode % currentCapacity);
        Node<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newLength = currentCapacity << 1;
        currentCapacity = currentCapacity << 1;
        Node<K, V>[] oldTab = table;
        table = (Node<K, V>[]) new Node[newLength];

        for (Node<K, V> replaceNode : oldTab) {
            Node<K, V> current = replaceNode;
            while (current != null) {
                put(current.key, current.value);
                size--;

                current = current.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (obj == null) {
                return false;
            }

            if (obj.getClass().equals(getClass())) {
                Node<K, V> current = (Node<K, V>) obj;
                return Objects.equals(this.key, current.key)
                        && Objects.equals(this.value, current.value);
            }

            return false;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
