package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float LOAD_FACTOR = 0.75F;

    private Node<K,V>[] buckets;
    private int size;
    private int capacity;

    MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        this.size = 0;
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        resize();
        Node<K, V> collusionNode = buckets[index];
        while (collusionNode != null) {
            if (key == collusionNode.key
                    || key != null && key.equals(collusionNode.key)) {
                collusionNode.value = value;
                return;
            }
            collusionNode = collusionNode.next;
        }
        collusionNode = new Node<>(key,value,buckets[index]);
        buckets[index] = collusionNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        resize();
        Node<K,V> collusionNode = buckets[index];
        if (collusionNode == null) {
            return null;
        }
        while (collusionNode != null) {
            K testKey = collusionNode.key;
            if ((testKey == key)
                    || (testKey != null && testKey.equals(key))) {
                return collusionNode.value;
            }
            collusionNode = collusionNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        if (size >= capacity * LOAD_FACTOR) {
            capacity = 2 * capacity;
            Node<K, V>[] newNode = new Node[capacity];
            Node<K, V>[] newBuckets = buckets;
            buckets = newNode;
            size = 0;
            for (Node<K, V> node : newBuckets) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private class Node<K,V> {
        private Node<K, V> next;
        private K key;
        private V value;

        private Node(K key, V value, Node<K,V> next) {
            this.next = next;
            this.value = value;
            this.key = key;
        }
    }
}
