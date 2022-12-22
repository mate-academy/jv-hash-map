package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int ARRAY_GROW_COEFFICIENT = 2;
    private Node<K, V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = getHush(key) % table.length;
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.key == null && key == null || key != null && key.equals(node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
        }
    }
    @Override
    public V getValue(K key) {
        int index = getHush(key) % table.length;
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.key == null && key == null || key != null && key.equals(node.key)) {
                return node.value;
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
        table =  new Node[table.length * ARRAY_GROW_COEFFICIENT];
        size = 0;
        for(Node<K, V> node : oldTable) {
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

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    private int getHush(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() + 17);
    }
}
