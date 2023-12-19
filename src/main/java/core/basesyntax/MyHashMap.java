package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K,V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];

    class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object == null || object.getClass() != this.getClass()) {
                return false;
            }

            Node<K, V> node = (Node<K, V>) object;
            return (value == node.value || (value != null && value.equals(node.getValue())))
                    && (key == node.key || (key != null && key.equals(node.getKey())));
        }
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value,null);
            size++;
        } else {
            Node<K, V> node = table[index];
            while (node.key == null || !node.key.equals(key)) {
                if (node.key == null && key == null) {
                    node.value = value;
                    break;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value, null);
                    size++;
                    break;
                }
                node = node.next;
            }
            if (node.key == null || node.key.equals(key)) {
                node.value = value;
            }
        }
    }

    public void resize() {
        size = 0;
        int newLength = table.length << 1;
        Node<K,V>[] oldTable = table;
        table = new Node[newLength];
        threshold = (int) (newLength * DEFAULT_LOAD_FACTOR);
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getIndex(K key) {
        return ((hash(key) % table.length) < 0)
                ? (hash(key) % table.length) + table.length : hash(key) % table.length;
    }
}
