package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);

        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }

        Node<K, V> current = table[index];
        while (true) {
            if ((current.getKey() == null && key == null)
                    || (current.getKey() != null && current.getKey().equals(key))) {
                current.setValue(value);
                return;
            }
            if (current.getNext() == null) {
                current.setNext(newNode);
                size++;
                return;
            }
            current = current.getNext();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if ((current.getKey() == null && key == null)
                    || (current.getKey() != null && current.getKey().equals(key))) {
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

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @SuppressWarnings("unchecked")
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

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}




