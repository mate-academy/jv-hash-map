package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int size;
    private int threshold;
    private Node<K, V>[] table;
    private int tableCapacity = DEFAULT_CAPACITY;

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            ;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[tableCapacity];
        this.threshold = (int) (tableCapacity * DEFAULT_LOAD_FACTOR);
    }

    public Node<K, V> getNode(K key) {
        int position = getPositionByKey(key);
        Node<K, V> current = table[position];
        if (current == null) {
            return null;
        }
        if (key == null && key == current.key || current.key.equals(key)) {
            return current;
        }
        while (current.next != null) {
            current = current.next;
            if (current.key == key || key != null && key.equals(current.key)) {
                return current;
            }
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        int position = getPositionByKey(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> current = table[position];
        if (table[position] == null) {
            table[position] = newNode;
        } else if (current.key == key || key != null && key.equals(current.key)) {
            current.value = value;
            return;
        } else {
            while (current.next != null) {
                current = current.next;
                if (current.key == key || key != null && key.equals(current.key)) {
                    current.value = value;
                    return;
                }
            }
            current.next = newNode;
        }
        size++;
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> utility = getNode(key);
        return utility == null ? null : utility.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        tableCapacity = tableCapacity << 1;
        threshold = (int) (tableCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V> current;
        Node<K, V>[] oldArray = table;
        table = new Node[tableCapacity];
        size = 0;
        for (Node<K, V> node : oldArray) {
            current = node;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int getPositionByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % tableCapacity);
    }
}
