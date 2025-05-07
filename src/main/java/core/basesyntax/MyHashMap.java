package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_FACTOR = 2;
    private int size;
    private int threshold;
    private int capacity;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int hash = getHashCode(key);
        int index = hash % table.length;
        Node<K, V> node = table[index];

        if (node == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
        } else {
            while (true) {
                if (node.hash == hash && (Objects.equals(key, node.key))) {
                    node.value = value;
                    break;
                } else if (node.next == null) {
                    node.next = new Node<>(hash, key, value, null);
                    size++;
                    break;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode;
        return (currentNode = getNode(key)) == null ? null : currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            int result = 17;
            result = 31 * result + (key == null ? 0 : key.hashCode());
            result = 31 * result + (value == null ? 0 : value.hashCode());
            return result;
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (o.getClass().equals(Node.class)) {
                Node<K, V> current = (Node<K, V>) o;
                return Objects.equals(this.key, current.key)
                        && Objects.equals(this.value, current.value);
            }
            return false;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        capacity = capacity * INCREASE_FACTOR;
        Node<K, V>[] newTable = new Node[capacity];
        threshold = (int) (capacity * LOAD_FACTOR);

        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            Node<K, V> nextNode;
            while (node != null) {
                int index = node.hash % capacity;
                if (newTable[index] == null) {
                    newTable[index] = new Node<>(node.hash, node.key, node.value, null);
                } else {
                    nextNode = newTable[index];
                    while (nextNode.next != null) {
                        nextNode = nextNode.next;
                    }
                    nextNode.next = new Node<>(node.hash, node.key, node.value, null);
                }
                node = node.next;
            }
        }
        table = newTable;
    }

    private Node<K, V> getNode(K key) {
        Node<K, V>[] currentTable;
        Node<K, V> firstNode;
        Node<K, V> nextNode;
        int hash = getHashCode(key);

        if ((currentTable = table) != null && currentTable.length > 0) {
            firstNode = currentTable[hash % currentTable.length];
            if (firstNode != null && firstNode.hash == hash
                    && (Objects.equals(key, firstNode.key))) {
                return firstNode;
            }
            if (firstNode != null && (nextNode = firstNode.next) != null) {
                do {
                    if (nextNode.hash == hash && (Objects.equals(key, nextNode.key))) {
                        return nextNode;
                    }
                } while ((nextNode = nextNode.next) != null);
            }
        }
        return null;
    }

    private int getHashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }
}
