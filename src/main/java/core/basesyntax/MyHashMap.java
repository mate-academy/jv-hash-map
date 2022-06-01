package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int currentCapacity;
    private Node<K, V>[] values;
    private int size;

    public MyHashMap() {
        currentCapacity = DEFAULT_CAPACITY;
        values = new Node[currentCapacity];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(hash(key), key, value, null);
        checkLoading();
        if (addNodeToArray(node, values)) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                continue;
            }
            node = values[i];
            while (node != null) {
                if (isKeysEqual(node.key, key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int countIndex(Node<K, V> node) {
        return node.key == null ? 0 : node.hash % currentCapacity;
    }

    private void grow() {
        int newCapacity = currentCapacity * 2;
        Node<K, V>[] grown = new Node[newCapacity];
        currentCapacity = newCapacity;
        transitValues(grown);
        values = grown;
    }

    private void checkLoading() {
        if ((currentCapacity * LOAD_FACTOR) == size) {
            grow();
        }
    }

    private boolean addNodeToArray(Node<K, V> node, Node<K, V> [] array) {
        int index = countIndex(node);
        if (array[index] != null) {
            Node<K, V> temporary = array[index];
            while (temporary != null) {
                if (isKeysEqual(temporary.key, node.key)) {
                    temporary.value = node.value;
                    return false;
                }
                if (temporary.next == null) {
                    temporary.next = node;
                    break;
                }
                temporary = temporary.next;
            }
        } else {
            array[index] = node;
        }
        return true;
    }

    private void transitValues(Node[] grown) {
        for (Node<K, V> value : values) {
            if (value == null) {
                continue;
            }
            addNodeToArray(value, grown);
        }
    }

    private boolean isKeysEqual(K key1, K key2) {
        return Objects.equals(key1, key2);
    }

    private static int hash(Object key) {
        if (key == null) {
            return 0;
        }
        int h = 17;
        h = h * 31 + key.hashCode();
        return h > 0 ? h : -1 * h;
    }

    static class Node<K, V> {
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
    }
}
