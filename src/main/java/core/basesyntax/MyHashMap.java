package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = (table.length - 1) & hash;
        Node<K, V> newNode = new Node(hash, key, value, null);

        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.hash == hash
                        && (current.key == key || (key != null && key.equals(current.key)))) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    break;
                }
                current = current.next;
            }
        }

        size++;
        resizeIfNeeded();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return (node == null) ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private Node<K, V> getNode(K key) {
        int hash = hash(key);
        int index = (table.length - 1) & hash;
        Node<K, V> node = table[index];

        while (node != null) {
            if (node.hash == hash
                    && (node.key == key || (key != null && key.equals(node.key)))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void resizeIfNeeded() {
        if (size > threshold) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                int newHash = (newCapacity - 1) & node.hash;
                Node<K, V> next = node.next;
                node.next = newTable[newHash];
                newTable[newHash] = node;
                node = next;
            }
        }
        table = newTable;
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
