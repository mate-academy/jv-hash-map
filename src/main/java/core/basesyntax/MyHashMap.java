package core.basesyntax;

import java.util.NoSuchElementException;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity;
    private int threshold;
    private int size;
    private Node<K,V> [] table;

    public MyHashMap() {
        this.table = new Node[INITIAL_CAPACITY];
        this.capacity = INITIAL_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (o.getClass().equals(Node.class)) {
                Node<?, ?> that = (Node<?, ?>) o;
                return ((this.key == that.key) || (this.key != null && this.key.equals(that.key))
                        && (this.value == that.value) || (this.value != null && this.value.equals(that.value)));
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + getKey().hashCode();
            result = 31 * result + value.hashCode();
            result = 31 * result + next.hashCode();
            return result;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        if (table[hash(key) % capacity] != null) {
            Node<K,V> node = table[hash(key) % capacity];
            if (node.key == key || node.key.equals(key)) {
                node.value = value;
                return;
            }
            while (node.next != null) {
                node = node.next;
                if (node.key == key || node.key.equals(key)) {
                    node.value = value;
                    return;
                }
            }
            node.next = new Node<K,V>(hash(key), key, value, null);
            size++;
        }
        if (table[hash(key) % capacity] == null) {
            Node<K,V> newNode = new Node<>(hash(key), key, value, null);
            table[hash(key) % capacity] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[hash(key) % capacity];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        throw new NoSuchElementException();
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return Math.abs((key == null) ? 0 : key.hashCode());
    }

    private void resize() {
        final Node<K,V>[] oldTable = table;
        capacity *= 2;
        size = 0;
        threshold = (int) (capacity * LOAD_FACTOR);
        table = new Node[capacity];
        // для каждого елемента и старой таблицы вызываем put
    }
}
