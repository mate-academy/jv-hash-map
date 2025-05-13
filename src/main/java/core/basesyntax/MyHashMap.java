package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = Math.abs(key.hashCode() % table.length);
        Node<K, V> current = table[index];

        while (current != null) {
            if(current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;

    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(key.hashCode() % table.length);
        Node<K, V> current = table[index];
        while (current != null) {
            if(current.key.equals(key)) {
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

}
