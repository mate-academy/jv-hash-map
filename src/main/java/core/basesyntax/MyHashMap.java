package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size = 0;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int hashCode = key == null ? 0 : key.hashCode();
        int index = (hashCode & Integer.MAX_VALUE) % table.length;
        if (table[index] == null) {
            table[index] = new Node<>(hashCode, key, value, null);
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.getKey() == null && key == null
                        || key != null && key.equals(current.getKey())) {
                    current.setValue(value);
                    return;
                }
                current = current.getNext();
            }
            table[index] = new Node<>(hashCode, key, value, table[index]);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int hashCode = key == null ? 0 : key.hashCode();
        int index = (hashCode & Integer.MAX_VALUE) % table.length;
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.getKey() == null && key == null
                    || key != null && key.equals(current.getKey())) {
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
        Node<K,V>[] oldTable = table;
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = (int) (newCapacity * LOAD_FACTOR);
        table = newTable;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                int index = (node.getHash() & Integer.MAX_VALUE) % newCapacity;
                Node<K, V> next = node.getNext();
                node.setNext(newTable[index]);
                newTable[index] = node;
                node = next;
            }
        }
    }
}
