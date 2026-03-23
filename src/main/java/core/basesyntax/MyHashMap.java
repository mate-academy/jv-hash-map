package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * loadFactor);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        for (Node<K, V> curr = node; curr != null; curr = curr.next) {
            if (key == curr.key || (key != null && key.equals(curr.key))) {
                curr.value = value;
                return;
            }
        }
        table[index] = new Node<>(key, value, node);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        for (Node<K, V> curr = table[index]; curr != null; curr = curr.next) {
            if (key == curr.key || (key != null && key.equals(curr.key))) {
                return curr.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * CAPACITY_MULTIPLIER;
        table = new Node[newCapacity];
        threshold = (int) (newCapacity * loadFactor);
        size = 0;

        for (Node<K, V> headNode : oldTable) {
            Node<K, V> curr = headNode;
            while (curr != null) {
                put(curr.key, curr.value);
                curr = curr.next;
            }
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }


}
