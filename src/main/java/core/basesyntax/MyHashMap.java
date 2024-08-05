package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MAXIMAL_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size = 0;
    private Node[] table;
    private float threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);

        if (size >= threshold) {
            resize();
        }

        Node<K, V> current = table[index];
        if (current == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }

        while (current != null) {
            if ((current.getKey() == null && key == null)
                    || (current.getKey() != null && current.getKey().equals(key))) {
                current.setValue(value);
                return;
            }
            if (current.getNext() == null) {
                current.setNext(new Node<>(key, value, null));
                size++;
                return;
            }
            current = current.getNext();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> current = table[index];

        // Iterate the linked list at the correct index
        while (current != null) {
            if ((key == null && current.getKey() == null)
                    || (key != null && key.equals(current.getKey()))) {
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

    private int hash(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode()) % table.length);
    }

    private void resize() {
        if (table.length == MAXIMAL_CAPACITY) {
            return;
        }
        int oldCapacity = table.length;
        int newCapacity = oldCapacity * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = newCapacity * DEFAULT_LOAD_FACTOR;
        for (Node<K, V> bucket : table) {
            Node<K, V> current = bucket;
            while (current != null) {
                int newIndex = (current.getKey() == null ? 0
                        : Math.abs(current.getKey().hashCode()) % newCapacity);
                Node<K, V> next = current.getNext();
                current.setNext(newTable[newIndex]);
                newTable[newIndex] = current;
                current = next;
            }
        }
        table = newTable;
    }
}
