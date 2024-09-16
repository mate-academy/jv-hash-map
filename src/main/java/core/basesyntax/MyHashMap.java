package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        int index = indexFor(hash(key));
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        while (true) {
            if (keysEqual(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = indexFor(hash(key));
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (keysEqual(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
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

    private int indexFor(int hash) {
        return hash % table.length;
    }

    private boolean keysEqual(K key1, K key2) {
        return key1 == key2 || (key1 != null && key1.equals(key2));
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
