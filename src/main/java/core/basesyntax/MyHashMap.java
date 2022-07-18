package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int PLACE_FOR_NULL_KEY = 0;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        if (++size > threshold) {
            resize();
        }
        addValue(newNode);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> findNode = getNode(key);
        return findNode == null ? null : findNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addValue(Node<K, V> addNode) {
        Node<K, V> setNode;
        Node<K, V> nodePrev = null;
        int bucketIndex = getIndex(addNode.key);
        setNode = table[bucketIndex];
        if (setNode == null) {
            table[bucketIndex] = addNode;
            return;
        }
        while (setNode != null) {
            if (Objects.equals(addNode.key, setNode.key)) {
                setNode.value = addNode.value;
                size--;
                return;
            }
            nodePrev = setNode;
            setNode = setNode.next;
        }
        nodePrev.next = addNode;
    }

    private int getIndex(K key) {
        return key == null ? PLACE_FOR_NULL_KEY : Math.abs(key.hashCode()) % table.length;
    }


    private void resize() {
        threshold = threshold << 1;
        Node<K, V>[] newTable = new Node[table.length << 1];
        transfer(newTable);
    }

    private void transfer(Node<K, V>[] newTable) {
        Node<K, V>[] oldTable = table;
        table = newTable;
        Node<K, V> current;
        int element = size - 1;
        for (int bucketIndex = 0; bucketIndex < oldTable.length; bucketIndex++) {
            if (oldTable[bucketIndex] != null) {
                put(oldTable[bucketIndex].key, oldTable[bucketIndex].value);
                size--;
                current = oldTable[bucketIndex];
                if (current.next != null) {
                    oldTable[bucketIndex] = current.next;
                    bucketIndex--;
                }
                element--;
                if (element == 0) {
                    return;
                }
            }
        }
    }

    private Node<K, V> getNode(K key) {
        Node<K, V> findNode;
        findNode = table[getIndex(key)];
        if (findNode == null) {
            return null;
        }
        while (!Objects.equals(key, findNode.key)) {
            if (findNode.next == null) {
                return null;
            }
            findNode = findNode.next;
        }
        return findNode;
    }

}
