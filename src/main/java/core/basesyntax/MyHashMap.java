package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> newNode = new Node<>(getHash(key), key, value);
        Node<K, V> current = table[bucketIndex(key)];
        if (current == null) {
            table[bucketIndex(key)] = newNode;
            size++;
        }
        while (current != null) {
            if (current.key == key || current.key != null && current.key.equals(key)) {
                current.value = value;
                break;
            }
            if (current.next == null) {
                current.next = newNode;
                size++;
                break;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> current = table[bucketIndex(key)];
        while (current != null) {
            if (current.key == key || (current.key != null && current.key.equals(key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int bucketIndex(K key) {
        return Math.abs(getHash(key) % table.length);
    }

    private int getHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[table.length * 2];
        size = 0;
        for (Node<K, V> node : oldTable) {
            Node<K, V> newNode = node;
            while (newNode != null) {
                put(newNode.key, newNode.value);
                newNode = newNode.next;
            }
        }
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
