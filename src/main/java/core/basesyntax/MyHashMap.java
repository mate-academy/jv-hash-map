package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] node;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.node = (Node<K, V>[])new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int hashCode = hash(key);
        int index = toFindIndex(hashCode, node.length);

        Node<K, V> current = node[index];
        while (current != null) {
            if (key == null && current.key == null
                    || current.key != null && current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value, node[index]);
        node[index] = newNode;
        size++;
        checkNeedToResize();
    }

    @Override
    public V getValue(K key) {
        int hashCode = hash(key);
        int index = toFindIndex(hashCode, node.length);
        Node<K, V> current = node[index];
        while (current != null) {
            if (key == null && current.key == null
                    || current.key != null && current.key.equals(key)) {
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

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int toFindIndex(int hash, int length) {
        return Math.abs(hash) % length;
    }

    private void checkNeedToResize() {
        if ((double) size / node.length > LOAD_FACTOR) {
            resize();
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = node.length * 2;
        Node<K, V>[] oldNode = node;
        node = (Node<K, V>[])new Node[newCapacity];
        size = 0;
        for (Node<K, V> kvNode : oldNode) {
            Node<K, V> current = kvNode;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }
}
