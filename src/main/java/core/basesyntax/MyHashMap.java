package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int capacity;
    private int threshold;
    private int size;

    @Override
    public void put(K key, V value) {
        if (table == null) {
            resize();
        }
        int bucket = getBucket(key);
        Node<K, V> current = table[bucket];
        if (current != null) {
            while (current.next != null) {
                if (isEqualsKeys(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (isEqualsKeys(current.key, key)) {
                current.value = value;
                return;
            }
            if (size == threshold) {
                resize();
                bucket = getBucket(key);
                current = table[bucket];
                if (current != null) {
                    while (current.next != null) {
                        current = current.next;
                    }
                    current.next = new Node<>(key, value, null);
                } else {
                    current.next = new Node<>(key, value, null);
                }
            } else {
                current.next = new Node<>(key, value, null);
            }
        } else {
            if (size == threshold) {
                resize();
                bucket = getBucket(key);
                table[bucket] = new Node<>(key, value, null);
            } else {
                table[bucket] = new Node<>(key, value, null);
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int bucket = getBucket(key);
        Node<K, V> current = table[bucket];
        while (current != null) {
            if (isEqualsKeys(current.key, key)) {
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

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getBucket(K key) {
        int hash = getHash(key);
        return Math.abs(hash % capacity);
    }

    private void resize() {
        if (table == null) {
            table = new Node[DEFAULT_CAPACITY];
            capacity = DEFAULT_CAPACITY;
            threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
        } else {
            capacity = capacity << 1;
            threshold = threshold << 1;
            Node<K,V>[] oldTable = table;
            table = new Node[capacity];
            size = 0;
            transfer(oldTable);
        }
    }

    private void transfer(Node<K,V>[] src) {
        for (Node<K, V> node : src) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean isEqualsKeys(K firstKey, K secondKey) {
        return firstKey == secondKey || (firstKey != null && firstKey.equals(secondKey));
    }
}
