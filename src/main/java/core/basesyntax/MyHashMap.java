package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;

    private int threshold;
    private int size;
    private Node<K,V>[] table;

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        size = getSize();
        table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K,V> currentNode = table[index];
        Node<K,V> nextNode = new Node<>(index, key, value, null);
        if (currentNode == null) {
            table[index] = nextNode;
        } else {
            while (true) {
                if (key == currentNode.key || (key != null && key.equals(currentNode.key))) {
                    currentNode.value = value;
                    return;
                } else if (currentNode.next == null) {
                    currentNode.next = nextNode;
                    break;
                }
                currentNode = currentNode.next;

            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        Node<K,V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (key == currentNode.key || (key != null && key.equals(currentNode.key))) {
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

    private int getIndex(K key) {
        return Math.abs((key == null) ? 0 : key.hashCode() % table.length);
    }

    private void resize() {
        size = 0;
        int resizedCapacity = table.length * 2;
        threshold = (int) (resizedCapacity * LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[resizedCapacity];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> currentNode = oldTable[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }
}
