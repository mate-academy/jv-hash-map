package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int CAPACITY_GROW = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K,V>[] table;
    private int threshold;

    public MyHashMap() {
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private final int hash;
        private Node<K,V> next;

        private Node(K key, V value, int hash, Node<K, V> next) {
            this.value = value;
            this.key = key;
            this.hash = hash;
            this.next = next;
        }

    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        if (size == threshold) {
            resize();
        }
        Node<K, V> newNode = table[hash];
        if (newNode == null) {
            table[hash] = new Node<>(key, value, hash, null);
            size++;
        } else {
            while (newNode != null) {
                if (key == newNode.key || key != null && key.equals(newNode.key)) {
                    newNode.value = value;
                    return;
                }
                if (newNode.next == null) {
                    newNode.next = new Node<>(key, value, hash, null);
                    size++;
                    return;
                }
                newNode = newNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> newNode = table[hash(key)];
        while (newNode != null) {
            if (key == newNode.key || key != null && key.equals(newNode.key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length * CAPACITY_GROW];
        threshold = (int)(DEFAULT_LOAD_FACTOR * table.length);
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

}
