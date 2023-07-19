package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int GROWTH_FACTOR = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node newNode = new Node<>(key, value, null);
        insert(index, newNode, key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> oldNode = table[getIndex(key)];
        while (oldNode != null) {
            if (areKeysEqual(oldNode.key, key)) {
                return oldNode.value;
            }
            oldNode = oldNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfFull() {
        if (table.length * LOAD_FACTOR == size) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldMap = table;
        table = new Node[oldMap.length * GROWTH_FACTOR];
        size = 0;
        for (Node<K, V> element : oldMap) {
            while (element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }
    }

    private void insert(int index, Node<K, V> newNode, K key, V value) {
        if (table[index] != null) {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (areKeysEqual(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    break;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        } else {
            table[index] = newNode;
        }
        size++;
        resizeIfFull();
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    private boolean areKeysEqual(K firstKey, K secondKey) {
        return firstKey == secondKey || firstKey != null && firstKey.equals(secondKey);
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
