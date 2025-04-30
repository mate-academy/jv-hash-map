package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int MULTIPLIER = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    public static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = hash(key, table.length);

        if (table[index] == null) {
            table[index] = new Node(key, value);
            size++;
        } else {
            Node<K, V> bucketEntry = table[index];

            while (bucketEntry != null) {
                if ((bucketEntry.key == key || key != null && key.equals(bucketEntry.key))) {
                    bucketEntry.value = value;
                    return;
                }
                if (bucketEntry.next == null) {
                    bucketEntry.next = new Node(key, value);
                    size++;
                    return;
                }
                bucketEntry = bucketEntry.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key, table.length)];

        while (node != null) {
            if (node.key == key || key != null && key.equals(node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int hash(K key, int tableCapacity) {
        return key == null ? 0 : Math.abs(key.hashCode()) % tableCapacity;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * MULTIPLIER];
        size = 0;
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }
}
