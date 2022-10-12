package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int capacity = INITIAL_CAPACITY;
    private int size = 0;
    private int threshold = (int) (capacity * LOAD_FACTOR);

    MyHashMap() {
        table = (Node<K, V>[]) new Node[capacity];
    }

    private int getHash(Object key, int capacity) {
        return key == null ? 0 : key.hashCode() & (capacity - 1);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int bucket = getHash(key,capacity);
        Node<K,V> insertedElement = new Node<>(bucket, key, value, null);
        Node<K,V> currentElement = table[bucket];

        if (currentElement == null) {
            table[bucket] = insertedElement;
            size++;
        } else {
            while (currentElement.next != null) {
                if (currentElement.key == insertedElement.key
                        || currentElement.key != null
                        && currentElement.key.equals(insertedElement.key)) {
                    currentElement.value = insertedElement.value;
                    return;
                }
                currentElement = currentElement.next;
            }
            if ((currentElement.key == insertedElement.key
                    || currentElement.key != null
                    && currentElement.key.equals(insertedElement.key))) {
                currentElement.value = insertedElement.value;
            } else {
                currentElement.next = insertedElement;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key,capacity);
        Node<K, V> element = table[hash];
        if (element == null) {
            return null;
        }
        if (element.key == key || element.key != null && element.key.equals(key)) {
            return table[hash].getValue();
        } else {
            while (element != null) {
                if (element.key == key || element.key != null && element.key.equals(key)) {
                    return element.getValue();
                }
                element = element.next;
            }
            return null;
        }
    }

    private void resize() {
        int newCapacity = capacity << 1;
        threshold = threshold << 1;
        Node<K,V> [] newTable = (Node<K, V>[]) new Node [newCapacity];
        transfer(table, newTable);
        table = newTable;
        capacity = newCapacity;
    }

    private void transfer(Node<K,V> [] oldTable, Node<K,V> [] newTable) {
        int index = 0;
        while (index < oldTable.length) {
            Node<K,V> oldNode = oldTable[index];
            if (oldNode != null) {
                oldTable[index] = null;
                do {
                    Node<K,V> next = oldNode.next;
                    int hash = getHash(oldNode.key, newTable.length);
                    oldNode.next = newTable[hash];
                    newTable[hash] = oldNode;
                    oldNode = next;
                } while (oldNode != null);
            }
            index++;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K,V> {
        private final int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.next = next;
            this.value = value;
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash && key == node.key || key != null && key.equals(node.key)
                    && value == node.value || value != null && value.equals(node.value)
                    && next == node.next || next != null && next.equals(node.next);
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + (value == null ? 0 : value.hashCode());
            result = 31 * result + (key == null ? 0 : key.hashCode());
            result = 31 * result + (next == null ? 0 : next.hashCode());
            return result;
        }
    }
}

