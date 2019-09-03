package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
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

        public void setKey(K key) {
            this.key = key;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int capacity;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        size = 0;
        table = new Node[DEFAULT_CAPACITY];
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setTable(Node<K, V>[] table) {
        this.table = table;
    }

    @Override
    public void put(K key, V value) {
        resize();

        if (key == null && table[0] == null) {
            table[0] = new Node<K, V>(null, value, null);
            size++;
            return;
        }
        if (key == null && table[0] != null) {
            table[0].setValue(value);
            return;
        }

        int index = index();
        Node<K, V> current = table[index];
        if (current == null) {
            table[index] = new Node<K, V>(key, value, current);
            size++;
            return;
        }
        while (current != null) {
            if (key == current.getKey() || current.getKey().equals(key)) {
                current.setValue(value);
                return;
            }
            current = current.getNext();
        }
        current = table[index];
        table[index] = new Node<K, V>(key, value, current);
        size++;
        return;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return table[0].getValue();
        }
        int hash = key.hashCode();
        int index = index();
        Node<K, V> current = table[index];
        if (current == null) {
            return null;
        }
        Node<K, V> next = current.getNext();
        if (next == null) {
            return current.getValue();
        }
        while (current != null) {
            if (key == current.getKey() || key.equals(current.getKey())) {
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

    private int index() {
        return hashCode() % capacity;
    }

    private void resize() {
        if (size > LOAD_FACTOR * capacity) {
            size = 0;;
            int newCapacyty = capacity * 2;
            setCapacity(newCapacyty);
            Node<K, V>[] oldTable = table;
            setTable(new Node[capacity]);
            for (int i = 0; i < oldTable.length; i++) {
                if (oldTable[i] != null) {
                    K key = oldTable[i].getKey();
                    V value = oldTable[i].getValue();
                    put(key, value);
                    Node<K, V> node = oldTable[i].getNext();
                    while (node != null) {
                        key = node.getKey();
                        value = node.getValue();
                        put(key, value);
                        node = node.getNext();
                    }
                }
            }
        }
    }
}
