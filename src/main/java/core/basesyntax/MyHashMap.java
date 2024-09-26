package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private static final int MULTIPLIER = 2;
    private Node<K, V>[] array;
    private int size;
    private int threshold;

    public MyHashMap() {
        array = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndexByHash(key);
        if (array[index] == null) {
            array[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> tempNode = array[index];
            while (tempNode.next != null || isEqual(key, tempNode.key)) {
                if (isEqual(key, tempNode.key)) {
                    tempNode.value = value;
                    return;
                }
                tempNode = tempNode.next;
            }
            tempNode.next = new Node<>(key, value, null);
        }
        if (++size > threshold) {
            resize();
        }
    }

    private boolean isEqual(K keyOne, K keyTwo) {
        return (keyOne == keyTwo) || (keyOne != null && keyOne.equals(keyTwo));
    }

    private void resize() {
        size = 0;
        Node<K, V>[] tempArray = array;
        array = (Node<K, V>[]) new Node[array.length * MULTIPLIER];
        threshold = (int) (array.length * LOAD_FACTOR);
        for (Node<K, V> tempNode : tempArray) {
            while (tempNode != null) {
                put(tempNode.key, tempNode.value);
                tempNode = tempNode.next;

            }
        }
    }

    private int getIndexByHash(K key) {
        return Math.abs(key == null ? 0 : key.hashCode() % array.length);
    }

    @Override
    public V getValue(K key) {
        if (mapIsEmpty()) {
            return null;
        }
        int index = getIndexByHash(key);
        Node<K, V> tempNode = array[index];
        while (tempNode != null) {
            if (isEqual(tempNode.key, key)) {
                return tempNode.value;
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
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
