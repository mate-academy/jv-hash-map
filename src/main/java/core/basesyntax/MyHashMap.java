package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_GROWN_FACTOR = 2;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size >= (table.length) * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = getHashCode(key);
        Node<K, V> newNode = new Node<>(key, value);

        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if ((key == null && currentNode.key == null)
                        || (key != null && key.equals(currentNode.key))) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    break;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getHashCode(key);
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if ((key == null && currentNode.key == null)
                    || (key != null && key.equals(currentNode.key))) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        int newCapacity = DEFAULT_CAPACITY * DEFAULT_GROWN_FACTOR;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (int i = 0; i < table.length; i++) {
            Node<K, V> currentNode = table[i];
            while (currentNode != null) {
                int newIndex = Math.abs(currentNode.key.hashCode() % newCapacity);
                Node<K, V> nextNode = currentNode.next;
                currentNode.next = newTable[newIndex];
                newTable[newIndex] = currentNode;
                currentNode = nextNode;
            }
        }
        table = newTable;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) obj;
            return (key == null && node.key == null) || (key != null && key.equals(node.key));
        }

        @Override
        public int hashCode() {
            int result = 17;
            return key == null ? 0 : (key.hashCode() * result + 31);
        }
    }
}
