package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.hash = (key == null) ? 0 : key.hashCode();
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            table = resize();
        } else if (size >= threshold) {
            Node<K, V> [] oldTable = table;
            table = resize();
            table = transferBuckets(oldTable);
        }
        int bucket = hash(key);
        if (table[bucket] == null) {
            table[bucket] = new Node<>(key, value, null);
        } else {
            Node<K, V> node = table[bucket];
            while (node != null) {
                if (node.key == null && key == null) {
                    node.value = value;
                    break;
                }
                if (node.key != null && key != null) {
                    if ((node.key.hashCode() == key.hashCode()) && (node.key.equals(key))) {
                        node.value = value;
                        break;
                    }
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value, null);
                    break;
                } else {
                    node = node.next;
                }
            }
        }
        size = getSize();
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int index = hash(key);
        Node<K, V> bucket = table[index];
        if (bucket.next == null) {
            return bucket.value;
        }
        while (bucket != null) {
            if (bucket.key == null && key == null) {
                return bucket.value;
            }
            if (bucket.key != null && bucket.key.equals(key)) {
                return bucket.value;
            } else {
                bucket = bucket.next;
                if (bucket.next == null) {
                    return bucket.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        if (table == null) {
            return 0;
        }
        size = 0;
        for (Node<K, V> node : table) {
            if (node != null) {
                size++;
                node = node.next;
                while (node != null) {
                    size++;
                    node = node.next;
                }
            }
        }
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()) % capacity);
    }

    private Node<K, V>[] resize() {
        int oldCapacity = table == null ? 0 : table.length;
        if (table == null || table.length == 0) {
            capacity = INITIAL_CAPACITY;
            threshold = (int) (DEFAULT_LOAD_FACTOR * INITIAL_CAPACITY);
            return new Node[capacity];
        } else {
            capacity = oldCapacity * 2;
            threshold = threshold * 2;
            Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
            return newTable;
        }
    }

    private Node<K, V>[] transferBuckets(Node<K, V>[] oldTable) {
        Node<K, V> [] buffer = (Node<K, V> []) new Node[size];
        int bufferIndex = 0;
        Node<K, V>[] newTable = (Node<K, V> []) new Node[capacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                buffer[bufferIndex] = node;
                bufferIndex++;
                node = node.next;
                while (node != null) {
                    buffer[bufferIndex] = node;
                    node = node.next;
                    bufferIndex++;
                }
            }
        }
        for (int i = 0; i < buffer.length; i++) {
            buffer[i].next = null;
        }
        for (Node<K, V> node : buffer) {
            int index = hash(node.key);
            if (newTable[index] == null) {
                newTable[index] = node;
            } else {
                Node<K, V> newTableBucket = newTable[index];
                while (newTableBucket != null) {
                    if (newTableBucket.next == null) {
                        newTableBucket.next = node;
                        break;
                    }
                    newTableBucket = newTableBucket.next;
                }
            }
        }
        return newTable;
    }
}
