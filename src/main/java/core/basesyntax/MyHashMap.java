package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int arrayCapacity;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        arrayCapacity = DEFAULT_CAPACITY;
        table = new Node[arrayCapacity];
        threshold = (int) (arrayCapacity * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        add(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (table[index] == null) {
            return null;
        }
        if (equalsKey(table[index].key, key)) {
            return table[index].value;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (equalsKey(currentNode.key, key)) {
                    return currentNode.value;
                } else {
                    currentNode = currentNode.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }

    private void resize() {
        size = 0;
        arrayCapacity <<= 1;
        threshold = (int) (arrayCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldArray = table;
        table = new Node[arrayCapacity];
        for (Node<K, V> element : oldArray) {
            if (element != null) {
                if (element.next == null) {
                    add(element.key, element.value);
                } else {
                    while (element != null) {
                        add(element.key, element.value);
                        element = element.next;
                    }
                }
            }
        }
    }

    private void add(K key, V value) {
        int index = getIndex(key);
        int hash = key == null ? 0 : key.hashCode();
        if (table[index] == null) {
            table[index] = new Node<>(hash, key, value);
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            if (equalsKey(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            while (currentNode.next != null) {
                if (equalsKey(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (equalsKey(currentNode.key, key)) {
                currentNode.value = value;
                return;
            } else {
                currentNode.next = new Node<>(hash, key, value);
                size++;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % arrayCapacity;
    }

    private boolean equalsKey(K key1, K key2) {
        if (key1 == key2) {
            return true;
        }
        if (key1 == null && key2 == null) {
            return true;
        }
        if (key1 == null || key2 == null) {
            return false;
        }
        return key1.equals(key2);
    }
}
