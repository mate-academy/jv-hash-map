package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int currentCapacity;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        currentCapacity = DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        putNode(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = getNumberCellByKey(key);
        Node<K, V> currentNode = table[hash];
        if (currentNode != null) {
            while (currentNode.next != null) {
                if (currentNode.key == key || key != null && key.equals(currentNode.key)) {
                    return currentNode.value;
                } else {
                    currentNode = currentNode.next;
                }
            }
            return currentNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        currentCapacity = currentCapacity * 2;
        threshold = (int) (currentCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[currentCapacity];
        copyElement(oldTable);
    }

    private void copyElement(Node<K, V>[] oldTable) {
        for (Node<K, V> element : oldTable) {
            if (element != null) {
                Node<K, V> currentNode = element;
                if (currentNode.next == null) {
                    putNode(currentNode.key, currentNode.value);
                } else {
                    while (currentNode != null) {
                        putNode(currentNode.key, currentNode.value);
                        currentNode = currentNode.next;
                    }
                }

            }
        }
    }

    private void putNode(K key, V value) {
        int hash = getNumberCellByKey(key);
        if (table[hash] == null) {
            table[hash] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> currentNode = table[hash];
            if (currentNode.key == key || key != null && key.equals(currentNode.key)) {
                currentNode.value = value;
                size--;
            } else {
                while (currentNode.next != null) {
                    if (currentNode.key == key || key != null && key.equals(currentNode.key)) {
                        break;
                    }
                    currentNode = currentNode.next;
                }
                if (currentNode.key == key || key != null && key.equals(currentNode.key)) {
                    currentNode.value = value;
                    size--;
                } else {
                    currentNode.next = new Node<>(hash, key, value, null);
                }
            }
        }
    }

    private int getNumberCellByKey(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % currentCapacity;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
