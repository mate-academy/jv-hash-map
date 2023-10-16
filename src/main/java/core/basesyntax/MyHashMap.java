package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DOUBLE_MULTIPLIER = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (size > 0 && replaceDuplicateKeyValue(key, value)) {
            return;
        }
        table = (size == table.length * DEFAULT_LOAD_FACTOR) ? resize() : table;
        Node<K, V> current = table[index];
        Node<K, V> newNode = new Node<>(key, value, null);;
        if (current != null) {
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        } else {
            table[index] = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (!checkElementIndexExistence(key)) {
            return null;
        }
        int index = hash(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if ((current.key == key)
                    || (current.key != null && current.key.equals(key))) {
                break;
            }
            current = current.next;
        }
        return current.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private Node<K,V>[] resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * DOUBLE_MULTIPLIER];
        Node<K, V> current;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                current = oldTable[i];
                put(current.key, current.value);
                size--;
                while (current.next != null) {
                    current = current.next;
                    put(current.key, current.value);
                    size--;
                }
            }
        }
        return table;
    }

    private boolean replaceDuplicateKeyValue(K key, V value) {
        int index = hash(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if ((current.key == key)
                    || (current.key != null && current.key.equals(key))) {
                current.value = value;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private boolean checkElementIndexExistence(K key) {
        if (size > 0) {
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null && hash(table[i].key) == hash(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
