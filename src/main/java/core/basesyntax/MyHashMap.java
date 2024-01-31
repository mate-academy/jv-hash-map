package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int SCALE_FACTOR = 2;
    private Node<K, V>[] table;
    private int capacity;
    private int threshold;
    private int size;

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            resize();
        }
        int keyHash = generateHashCode(key);
        int tableIndex = Math.abs(keyHash) % capacity;
        if (table[tableIndex] == null) {
            table[tableIndex] = new Node<>(keyHash, key, value, null);
        } else {
            Node<K, V> node = table[tableIndex];
            Node<K, V> lastNode;
            do {
                if (node.hash == keyHash
                        && (node.key == key || (node.key != null && node.key.equals(key)))) {
                    node.value = value;
                    return;
                }
                lastNode = node;
                node = node.next;
            } while (node != null);
            lastNode.next = new Node<>(keyHash, key, value, null);
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null || table.length == 0) {
            return null;
        }
        int keyHash = generateHashCode(key);
        int tableIndex = Math.abs(keyHash) % capacity;
        if (table[tableIndex] == null) {
            return null;
        }
        Node<K, V> node = table[tableIndex];
        do {
            if (node.hash == keyHash
                    && (node.key == key || (node.key != null && node.key.equals(key)))) {
                return node.value;
            }
            node = node.next;
        } while (node != null);
        return null;
    }

    @Override
    public int getSize() {
        return size;
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

    private void resize() {
        int newCapacity;
        int newThreshold;
        if (table == null) {
            newCapacity = DEFAULT_CAPACITY;
            newThreshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
        } else {
            newCapacity = table.length * SCALE_FACTOR;
            newThreshold = (int) (DEFAULT_LOAD_FACTOR * newCapacity);
        }
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        if (table != null) {
            for (Node<K, V> node : table) {
                if (node != null) {
                    Node<K, V> tempNode = node;
                    while (tempNode != null) {
                        int newIndexPosition = Math.abs(tempNode.hash) % newCapacity;
                        if (newTable[newIndexPosition] == null) {
                            newTable[newIndexPosition] = new Node<>(tempNode.hash,
                                                                    tempNode.key,
                                                                    tempNode.value,
                                                               null);
                        } else {
                            Node<K, V> iterationNode = newTable[newIndexPosition];
                            while (iterationNode.next != null) {
                                iterationNode = iterationNode.next;
                            }
                            iterationNode.next = new Node<>(tempNode.hash,
                                                            tempNode.key,
                                                            tempNode.value,
                                                       null);
                        }
                        tempNode = tempNode.next;
                    }
                }
            }
        }
        table = newTable;
        capacity = newCapacity;
        threshold = newThreshold;
    }

    private int generateHashCode(K key) {
        return (key == null) ? 0 : key.hashCode();
    }
}
