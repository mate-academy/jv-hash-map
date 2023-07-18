package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_MULTIPLAYER = 2;
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
        int index = bucketIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> current = table[index];
        if (current == null) {
            table[index] = newNode;
            size++;
        }
        while (current != null) {
            if (current.key == key || current.key != null && current.key.equals(key)) {
                current.value = value;
               return;
            }
            if (current.next == null) {
                current.next = newNode;
                size++;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = bucketIndex(key);
        Node<K,V> current = table[index];
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
        return Math.abs(((key == null) ? 0 : key.hashCode())) % table.length;
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[table.length * GROW_MULTIPLAYER];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
