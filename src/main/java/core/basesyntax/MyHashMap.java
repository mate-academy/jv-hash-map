package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private Node<K, V> nullKeyNode;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }

    private int getBucketIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            if (nullKeyNode == null) {
                nullKeyNode = new Node<>(key, value);
                size++;
            } else {
                nullKeyNode.setValue(value);
            }
            return;
        }

        int bucketIndex = getBucketIndex(key);
        Node<K, V> head = table[bucketIndex];

        while (head != null) {
            if (head.getKey().equals(key)) {
                head.setValue(value);
                return;
            }
            head = head.getNext();
        }

        size++;
        head = table[bucketIndex];
        Node<K, V> newNode = new Node<>(key, value);
        newNode.setNext(head);
        table[bucketIndex] = newNode;

        if ((1.0 * size) / table.length >= DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return nullKeyNode == null ? null : nullKeyNode.getValue();
        }

        int bucketIndex = getBucketIndex(key);
        Node<K, V> head = table[bucketIndex];

        while (head != null) {
            if (head.getKey().equals(key)) {
                return head.getValue();
            }
            head = head.getNext();
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;

        for (Node<K, V> headNode : oldTable) {
            while (headNode != null) {
                put(headNode.getKey(), headNode.getValue());
                headNode = headNode.getNext();
            }
        }
    }
}