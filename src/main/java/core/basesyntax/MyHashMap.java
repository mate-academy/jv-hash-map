package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V> [] table;
    private int threshold;
    private int size;

    @Override
    public void put(K key, V value) {
        if (size == threshold || table == null) {
            resize();
        }
        putValue(key, value);
        size++;
    }

    private void putValue(K key, V value) {
        int numberOfBucket = hash(key) % table.length;
        if (table[numberOfBucket] != null) {
            Node<K, V> currentElement = table[numberOfBucket];
            if (Objects.equals(currentElement.key, key)) {
                currentElement.value = value;
                size--;
            } else {
                while (currentElement.next != null) {
                    if (Objects.equals(currentElement.key, key)) {
                        currentElement.value = value;
                        size--;
                        return;
                    }
                    currentElement = currentElement.next;
                }
                currentElement.next = new Node<>(key, value);
            }
        } else {
            table[numberOfBucket] = new Node<>(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            int numberOfBucket = hash(key) % table.length;
            Node<K, V> element = table[numberOfBucket];
            if (Objects.equals(element.key, key)) {
                return element.value;
            } else {
                Node<K, V> currentElement = element;
                while (currentElement != null) {
                    if (Objects.equals(currentElement.key, key)) {
                        return currentElement.value;
                    }
                    currentElement = currentElement.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V> [] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : table.length;
        int oldThreshold = threshold;
        int newCapacity;
        int newThreshold;
        if (oldCapacity == 0) {
            newCapacity = DEFAULT_INITIAL_CAPACITY;
            newThreshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            table = (Node<K, V> [])new Node[newCapacity];
        } else {
            newCapacity = oldCapacity << 1;
            newThreshold = oldThreshold << 1;
            table = (Node<K, V> [])new Node[newCapacity];
            transfer(oldTable);
        }
        threshold = newThreshold;
    }

    private void transfer(Node<K, V> [] oldTable) {
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                putValue(oldTable[i].key, oldTable[i].value);
                if (oldTable[i].next != null) {
                    Node<K, V> currentElement = oldTable[i].next;
                    while (currentElement != null) {
                        putValue(currentElement.key, currentElement.value);
                        currentElement = currentElement.next;
                    }
                }
                oldTable[i] = null;
            }
        }
    }

    private int hash(Object key) {
        int result = 17;
        return (key == null) ? 0 : (Math.abs(31 * result + key.hashCode()));
    }

    private static class Node<K, V> {
        private int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
