package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K,V>[] table;
    private int size;

    @Override
    public void put(K key, V value) {
        putValue(hash(key),key, value);
    }

    private V putValue(int hash, K key, V value) {
        Node<K,V> node = new Node<>(hash, key, value, null);
        if (table == null || table.length == 0) {
            table = (Node<K,V>[])new Node[DEFAULT_INITIAL_CAPACITY];
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
        if (table[hash] == null) {
            table[hash] = node;
            size++;
        } else {
            addIfBucketIsNotEmpty(hash, node);
        }
        if (size > threshold) {
            resize();
        }
        return value;
    }

    @Override
    public V getValue(K key) {
        if (!checkIfKeyExists(key)) {
            return null;
        }
        Node<K, V> node = table[hash(key)];
        while (key == null && node.key != null || node.key == null
                && key != null || node.key != null && !node.key.equals(key)) {
            node = node.next;
        }
        return node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public boolean equals(Node<K, V> obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            return this.key.equals(obj.key) && this.hash == obj.hash
                    && this.value.equals(obj.value) && this.next.equals(obj.next);
        }

        public final int hashCode() {
            int result = 17;
            result = 31 * result + key.hashCode();
            result = 31 * result + value.hashCode();
            result = 31 * result + next.hashCode();
            return result;
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        size = 0;
        Node<K, V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[newCapacity];
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                put(node.key, node.value);
                while (node.next != null) {
                    node = node.next;
                    put(node.key, node.value);
                }
            }
        }
    }

    private boolean checkIfKeyExists(K key) {
        return (table != null && table.length != 0) && (table[hash(key)] != null || key == null);
    }

    private int hash(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()) % 16);
    }

    private V addIfBucketIsNotEmpty(int hash, Node<K, V> node) {
        Node<K, V> lastNode = table[hash];
        while (lastNode != null) {
            if ((node.key == lastNode.key)
                    || (node.key != null && node.key.equals(lastNode.key))) {
                lastNode.value = node.value;
                return node.value;
            }
            if (lastNode.next == null) {
                break;
            } else {
                lastNode = lastNode.next;
            }
        }
        lastNode.next = node;
        size++;
        return node.value;
    }
}
