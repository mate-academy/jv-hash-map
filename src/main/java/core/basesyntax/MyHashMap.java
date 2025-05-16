package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
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
        while (current != null) {
            if (current.getKey() == key || key != null
                    && key.equals(current.getKey())) {
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
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.getKey() == key || key != null && key.equals(node.getKey())) {
                return node.getValue();
            }
            node = node.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getIndex(K key) {
        return key == null ? 0 :
                Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                int newIndex = node.key == null ? 0 :
                        Math.abs(node.key.hashCode()) % newCapacity;
                Node<K, V> next = node.getNext();

                node.setNext(newTable[newIndex]);
                newTable[newIndex] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
}
