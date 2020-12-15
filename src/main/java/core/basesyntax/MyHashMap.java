package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private static final int MULTIPLIER = 2;
    private Node<K, V>[] array;
    private int size;
    private int threshold;

    MyHashMap() {
    }

    @Override
    public void put(K key, V value) {
        if (mapIsEmpty()) {
            array = resize();
        }
        int tempHash = hash(key);
        if (array[tempHash] == null) {
            array[tempHash] = new Node<>(tempHash, key, value, null);
        } else {
            if (isEqual(key, array[tempHash].key)) {
                array[tempHash].value = value;
                return;
            }
            Node<K, V> tempNode = array[tempHash];
            while (tempNode.next != null) {
                if (isEqual(key, tempNode.next.key)) {
                    tempNode.next.value = value;
                    return;
                }
                tempNode = tempNode.next;
            }
            tempNode.next = new Node<>(tempHash, key, value, null);
        }
        if (++size > threshold) {
            resize();
        }
    }

    private boolean isEqual(K keyOne, K keyTwo) {
        return (keyOne == keyTwo) || (keyOne != null && keyOne.equals(keyTwo));
    }

    private Node<K, V>[] resize() {
        if (size == 0) {
            array = new Node[INITIAL_CAPACITY];
            threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
            return array;
        }
        Node<K, V> [] tempArray = array;
        array = (Node<K, V>[]) new Node[array.length * MULTIPLIER];
        threshold = (int) (array.length * LOAD_FACTOR);
        for (Node<K, V> tempNode : tempArray) {
            while (tempNode != null) {
                put(tempNode.key, tempNode.value);
                tempNode = tempNode.next;
                size--;
            }
        }
        return null;
    }

    private int hash(Object key) {
        int h;
        return Math.abs(((key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16)) % array.length);
    }

    @Override
    public V getValue(K key) {
        if (mapIsEmpty()) {
            return null;
        }
        int hashKey = hash(key);
        Node<K, V> tempNode = array[hashKey];
        if (isEqual(tempNode.key, key)) {
            return tempNode.value;
        }
        while (tempNode.next != null) {
            if (isEqual(tempNode.next.key, key)) {
                return tempNode.next.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    private boolean mapIsEmpty() {
        return array == null || array.length == 0;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private int hash;
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
