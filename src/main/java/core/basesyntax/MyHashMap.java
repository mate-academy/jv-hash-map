package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    private static final float LOAD_FACTOR = 0.75f;
    private static final int COEFFICIENT_GROW = 2;
    private Node<K, V>[] map;
    private int capacity;
    private int size;

    public MyHashMap() {
        capacity = 16;
        map = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        putItem(map, capacity, key, value);
        if (size > capacity * LOAD_FACTOR) {
            resizeMap();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key) % capacity;
        Node<K, V> node = map[index];
        while (node != null) {
            if (node.key == key || (key != null && key.equals(node.key))) {
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

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void putItem(Node<K, V>[] map, int capacity, K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, hash(key), null);
        int index = newNode.hash % capacity;
        if (map[index] == null) {
            map[index] = newNode;
            size++;
            return;
        }
        Node<K, V> node = map[index];
        Node<K, V> oldNode = null;
        do {
            if (key == node.key || (key != null && key.equals(node.key))) {
                node.value = value;
                return;
            }
            oldNode = node;
            node = node.next;
        } while (node != null);

        size++;
        oldNode.next = newNode;
    }

    private void resizeMap() {
        size = 0;
        int oldCapacity = capacity;
        capacity *= COEFFICIENT_GROW;
        Node<K, V>[] newMap = (Node<K, V>[]) new Node[capacity];

        for (int i = 0; i < oldCapacity; i++) {
            if (map[i] != null) {
                Node<K, V> node = map[i];
                do {
                    putItem(newMap, capacity, node.key, node.value);
                    node = node.next;
                } while (node != null);
            }
        }
        map = newMap;
    }
}
