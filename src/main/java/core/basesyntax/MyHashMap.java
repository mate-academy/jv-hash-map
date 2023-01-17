package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @Override
    public void put(K key, V value) {
        resize();
        putVal(hash(key), key, value);
    }

    private void putVal(int hash, K key, V value) {
        int bucket = arrayBucket(key);
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (table[bucket] == null) {
            table[bucket] = newNode;
            size++;
        } else if (table[bucket] != null) {
            Node<K, V> currentNode = table[bucket];
            while (currentNode != null) {
                if (currentNode.key == null && key == null
                        || currentNode.key != null && currentNode.key == key
                        || currentNode.key != null && currentNode.key.equals(key)) {
                    currentNode.value = value;
                    break;
                } else if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
    }

    private void resize() {
        if (table == null || table.length == 0) {
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            table = new Node[DEFAULT_INITIAL_CAPACITY];
        } else if (size == threshold && size <= MAXIMUM_CAPACITY) {
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length + DEFAULT_INITIAL_CAPACITY];
            threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
            transfer(oldTable);
        }
    }

    private void transfer(Node<K, V>[] table) {
        size = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                Node<K, V> kvNode = table[i];
                while (kvNode != null) {
                    putVal(kvNode.hash, kvNode.key, kvNode.value);
                    kvNode = kvNode.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        } else if (table[arrayBucket(key)] != null) {
            Node<K, V> kvNode = table[arrayBucket(key)];
            while (kvNode != null) {
                if (kvNode.key == null && key == null
                        || kvNode.key != null && kvNode.key == key
                        || kvNode.key != null && kvNode.key.equals(key)) {
                    return kvNode.value;
                }
                kvNode = kvNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int arrayBucket(K key) {
        return hash(key) < 0 ? (hash(key) * (-1)) % table.length : hash(key) % table.length;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
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
