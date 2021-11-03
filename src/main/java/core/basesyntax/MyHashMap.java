package core.basesyntax;

public class MyHashMap<K,V> implements MyMap<K,V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node[] table;
    private int size;
    private int capacity;
    private int threshold;

    {
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = new Node[capacity];
    }

    private class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = calculateHashCode();
        }

        private int calculateHashCode() {
            if (key != null) {
                return Math.abs(key.hashCode() % capacity);
            }
            return 0;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K,V> puttedNode = new Node<>(key, value, null);
        if (table[puttedNode.hash] == null) {
            table[puttedNode.hash] = puttedNode;
        } else {
            Node oldNode = table[puttedNode.hash];
            while (oldNode != null) {
                if (oldNode.key == key || oldNode.key != null && oldNode.key.equals(key)) {
                    oldNode.value = value;
                    return;
                } else if (oldNode.next == null) {
                    oldNode.next = puttedNode;
                    size++;
                } else {
                    oldNode = oldNode.next;
                }
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[new Node<>(key, null, null).hash];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                return (V) node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        capacity = capacity << 1;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K,V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            while (oldTable[i] != null) {
                put(oldTable[i].key, oldTable[i].value);
                oldTable[i] = oldTable[i].next;
            }
        }
    }
}
