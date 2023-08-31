package core.basesyntax;


public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int threshold;
    private int size;


    public MyHashMap() {
        table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> node = table[index];
            while (node != null) {
                if (key == node.key || key != null && key.equals(node.key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            Node<K, V> newNode = new Node<>(key, value, table[index]);
            table[index] = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
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


    private void resize() {
        final Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length << 1];
        size = 0;
        threshold = (int) (table.length * LOAD_FACTOR);
        for (Node<K, V> oldNode : oldTable) {
            Node<K, V> node = oldNode;
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
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
}