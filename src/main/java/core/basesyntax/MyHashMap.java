package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int GROWTH_FACTOR = 2;
    private int size;
    private Node<K, V>[] mapArray;

    public MyHashMap() {
        mapArray = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node newNode = new Node<>(key, value, null);
        insert(index, newNode, key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> oldNode = mapArray[getIndex(key)];
        if (oldNode == null) {
            return null;
        }
        while (true) {
            if (areKeysEqual(oldNode.key, key)) {
                return oldNode.value;
            }
            if (oldNode.next == null) {
                return null;
            }
            oldNode = oldNode.next;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfFull() {
        if (mapArray.length * LOAD_FACTOR == size) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldMap = mapArray;
        mapArray = new Node[oldMap.length * GROWTH_FACTOR];
        size = 0;
        for (Node<K, V> element : oldMap) {
            while (element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }
    }

    private void insert(int index, Node<K, V> newNode, K key, V value) {
        if (mapArray[index] != null) {
            Node<K, V> oldNode = mapArray[index];
            while (true) {
                if (areKeysEqual(oldNode.key, key)) {
                    oldNode.value = value;
                    return;
                }
                if (oldNode.next == null) {
                    oldNode.next = newNode;
                    size++;
                    resizeIfFull();
                    return;
                }
                oldNode = oldNode.next;
            }
        } else {
            mapArray[index] = newNode;
            size++;
            resizeIfFull();
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % mapArray.length);
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
