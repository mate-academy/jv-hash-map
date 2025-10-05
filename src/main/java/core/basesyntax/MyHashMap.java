package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() { return key; }

        public void setKey(K key) { this.key = key; }

        public V getValue() { return value; }

        public void setValue(V value) { this.value = value; }

        public Node<K, V> getNext() { return next; }

        public void setNext(Node<K, V> next) { this.next = next; }
    }

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = (key == null)
                ? 0
                : Math.abs(key.hashCode()) % table.length;

        Node<K, V> current = table[index];
        while (current != null) {
            if ((key == null
                    && current.getKey() == null)
                    || (key != null
                    && key.equals(current.getKey()))) {
                current.setValue(value);
                return;
            }
            current = current.getNext();
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.setNext(table[index]);
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = (key == null)
                ? 0
                : Math.abs(key.hashCode()) % table.length;
        Node<K, V> current = table[index];

        while (current != null) {
            if ((key == null
                    && current.getKey() == null)
                    || (key != null
                    && key.equals(current.getKey()))) {
                return current.getValue();
            }
            current = current.getNext();
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * 2];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.getKey(), node.getValue());
                node = node.getNext();
            }
        }
    }
}
