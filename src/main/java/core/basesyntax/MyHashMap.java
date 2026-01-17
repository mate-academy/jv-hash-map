package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            table = new Node[DEFAULT_INITIAL_CAPACITY];
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
        int hash = hash(key);
        int index = getIndex(hash);
        if (table[index] == null) {
            Node<K, V> newNode = new Node<>(hash, key, value, null);
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> kvNode = table[index];
            Node<K, V> last = null;
            while (kvNode != null) {
                if (Objects.equals(kvNode.key, key)) {
                    kvNode.value = value;
                    return;
                }
                last = kvNode;
                kvNode = kvNode.next;
            }
            Node<K, V> newNode = new Node<>(hash, key, value, null);
            last.next = newNode;
            size++;
        }
        if (size == threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null || table.length == 0) {
            return null;
        }
        int hash = hash(key);
        int index = getIndex(hash);
        Node<K,V> kvNode = table[index];
        while (kvNode != null) {
            if (Objects.equals(kvNode.key, key)) {
                return kvNode.value;
            }
            kvNode = kvNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(int hash) {
        return hash & (table.length - 1);
    }

    private static int hash(Object key) {
        int keysHash;
        return key == null ? 0 : (keysHash = key.hashCode()) ^ keysHash >>> 16;
    }

    private void resize() {
        int newLength = table.length * RESIZE_FACTOR;
        threshold = threshold * RESIZE_FACTOR;
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[newLength];
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                Node<K,V> kvNode = oldTable[i];
                while (kvNode != null) {
                    put(kvNode.key, kvNode.value);
                    kvNode = kvNode.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (o == null) {
                return false;
            }

            if (o.getClass().equals(Node.class)) {
                Node<?, ?> current = (Node<?, ?>) o;
                return Objects.equals(current.key, this.key)
                        && Objects.equals(current.value, this.value);
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}
