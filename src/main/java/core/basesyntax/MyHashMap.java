package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
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

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[4];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;

        Node<K, V> newNode = new Node<>(key, value);
        newNode.setNext(table[index]);
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
        Node<K, V> current = table[index];

        while (current != null) {
            if (current.getKey().equals(key)) {
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
}
