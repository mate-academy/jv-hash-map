package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> nodeFromBucket = table[index];
        while (nodeFromBucket != null) {
            if (key != null && key.equals(nodeFromBucket.getKey())
                    || key == nodeFromBucket.getKey()) {
                nodeFromBucket.setValue(value);
                return;
            } else {
                nodeFromBucket = nodeFromBucket.getNext();
            }
        }
        if (nodeFromBucket == null) {
            Node<K, V> newNode = new Node<>(key, value, table[index]);
            table[index] = newNode;
            size++;
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> nodeFromBucket = table[getIndex(key)];

        while (nodeFromBucket != null) {
            if (key != null && key.equals(nodeFromBucket.getKey())
                    || key == nodeFromBucket.getKey()) {
                return nodeFromBucket.getValue();
            } else {
                nodeFromBucket = nodeFromBucket.getNext();
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    final void resize() {
        size = 0;
        Node<K, V>[] oldTable;
        int capacityOld = table.length;
        int capacityNew = capacityOld << 1;
        int thresholdNew = threshold << 1;
        Node<K, V>[] newTable = (Node<K, V>[])new Node[capacityNew];
        oldTable = table;
        table = newTable;
        threshold = thresholdNew;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.getKey(), node.getValue());
                node = node.getNext();
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
