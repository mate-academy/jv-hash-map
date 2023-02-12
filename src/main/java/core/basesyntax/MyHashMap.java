package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_SIZE = 16;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int capacity;

    public MyHashMap() {
        capacity = INITIAL_SIZE;
        table = new Node[INITIAL_SIZE];
    }

    private int getIndexForBucket(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public V getValue(K key) {
        int index = getIndexForBucket(key);
        Node<K, V> searchNode = table[index];
        while (searchNode != null) {
            if (checkEquality(searchNode.key, key)) {
                return searchNode.value;
            }
            searchNode = searchNode.next;
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        int bucketIndex = getIndexForBucket(key);
        Node<K,V> currentNode = table[bucketIndex];
        if (currentNode == null) {
            table[bucketIndex] = new Node<>(key, value, null);
            size++;
        } else {
            while (currentNode.next != null) {
                if (checkEquality(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (checkEquality(currentNode.key, key)) {
                currentNode.value = value;
            } else {
                currentNode.next = new Node<>(key, value, null);
                size++;
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node[] oldTable = table;
        table = new Node[table.length * 2];
        size = 0;
        transfer(oldTable);
        int newCapacity = capacity * 2;
        threshold = (int) (capacity * 0.75f);
        threshold = threshold * 2;
        capacity = newCapacity;
    }

    private void transfer(Node<K,V> [] oldTable) {
        for (Node<K, V> transferNode : oldTable) {
            while (transferNode != null) {
                put(transferNode.key, transferNode.value);
                transferNode = transferNode.next;
            }
        }
    }

    private boolean checkEquality(K firstElement, K secondElement) {
        return firstElement == secondElement
                || firstElement != null && firstElement.equals(secondElement);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
