package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int capacity;
    private int size;
    private int threshold;

    MyHashMap() {
        capacity = INITIAL_CAPACITY;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    private int getBucketIndex(Object key, int capacity) {
        return key == null ? 0 : key.hashCode() & (capacity - 1);
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    @Override
    public void put(K key, V value) {
        int bucket = getBucketIndex(key,capacity);
        Node<K,V> currentElement = table[bucket];

        if (currentElement == null) {
            table[bucket] = new Node<>(hash(key), key, value, null);
            size++;
        } else {
            while (currentElement.next != null) {
                if (areEqual(currentElement.key, key)) {
                    currentElement.value = value;
                    return;
                }
                currentElement = currentElement.next;
            }
            if (areEqual(currentElement.key, key)) {
                currentElement.value = value;
            } else {
                currentElement.next = new Node<>(hash(key), key, value, null);
                size++;
            }
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = getBucketIndex(key,capacity);
        Node<K, V> element = table[bucket];
        if (element == null) {
            return null;
        }
        if (areEqual(element.key, key)) {
            return table[bucket].getValue();
        } else {
            while (element != null) {
                if (areEqual(element.key, key)) {
                    return element.getValue();
                }
                element = element.next;
            }

            return null;
        }

    }

    private void resize() {
        int newCapacity = capacity << 1;
        Node<K,V> [] newTable = getTable(newCapacity);
        threshold = threshold << 1;
        transfer(table, newTable);
        table = newTable;
        capacity = newCapacity;
    }

    private void transfer(Node<K,V> [] oldTable, Node<K,V> [] newTable) {
        int index = 0;

        while (index < oldTable.length) {
            Node<K,V> currentNode = oldTable[index];
            if (currentNode != null) {
                do {
                    oldTable[index] = null;
                    int bucket = getBucketIndex(currentNode.key, newTable.length);
                    Node<K, V> next = currentNode.next;
                    currentNode.next = newTable[bucket];
                    newTable[bucket] = currentNode;
                    currentNode = next;
                } while (currentNode != null);
            }
            index++;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K,V>[] getTable(int capacity) {
        return (Node<K, V>[]) new Node[capacity];
    }

    private boolean areEqual(K firstElement, K secondElement) {
        return firstElement == secondElement
                || firstElement != null && firstElement.equals(secondElement);
    }

    private static final class Node<K,V> {
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

