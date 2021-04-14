package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }

        if (key == null) {
            putForNullKey(value);
        } else if (table[hash(key)] == null) {
            putOnEmptyBucket(key, value);
        } else {
            putWithCollision(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return table[0].value;
        }

        int index = hash(key);
        Node<K, V> nodeInBucket = table[index];
        while (nodeInBucket != null) {
            if (key.equals(nodeInBucket.key)) {
                return nodeInBucket.value;
            }
            nodeInBucket = nodeInBucket.nextNode;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }

    public int hash(K key) {
        int index = key.hashCode() & (table.length - 1);
        return (index == 0) ? 1 : index;
    }

    public void putForNullKey(V value) {
        if (table[0] == null) {
            table[0] = new Node<>(null, value, null);
            size++;

        } else {
            table[0].value = value;
        }
    }

    public void putOnEmptyBucket(K key, V value) {
        Node<K,V> newNode = new Node<>(key, value, null);
        table[hash(key)] = newNode;
        size++;
    }

    public void putWithCollision(K key, V value) {
        int index = hash(key);
        Node<K,V> nodeInBucket = table[index];

        while (nodeInBucket != null) {
            if (key.equals(nodeInBucket.key)) {
                nodeInBucket.value = value;
                return;
            }
            if (nodeInBucket.nextNode == null) {
                nodeInBucket.nextNode = new Node<>(key, value, null);;
                size++;
                return;
            }
            nodeInBucket = nodeInBucket.nextNode;
        }
    }

    public void resize() {
        size = 0;
        Node<K, V>[] copyTable = table;
        table = new Node[table.length * 2];
        for (Node<K, V> nodeInBucket : copyTable) {
            while (nodeInBucket != null) {
                put(nodeInBucket.key, nodeInBucket.value);
                nodeInBucket = nodeInBucket.nextNode;
            }
        }
    }
}
