package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity = 16;
    private int threshold;
    private int size;
    private Node<K, V>[] arrWithKeyValues;

    @Override
    public void put(K key, V value) {
        resizeIfNecessary();
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        int remainder = Math.abs(getHash(key)) % capacity;
        if (size == 0 || arrWithKeyValues[remainder] == null) {
            return null;
        }
        Node<K, V> temp = arrWithKeyValues[remainder];
        while (temp != null) {
            if ((temp.key == null && key == null)
                    || (temp.key != null && temp.key.hashCode() == getHash(key) && temp.key.equals(key))) {
                return temp.value;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfNecessary() {
        if (threshold == 0) {
            threshold = (int) (LOAD_FACTOR * capacity);
            arrWithKeyValues = new Node[capacity];
            return;
        }
        if (threshold == size) {
            Node<K, V>[] tempArr = new Node[capacity];
            capacity = capacity << 1;
            transformArray(tempArr);
            threshold = (int) (LOAD_FACTOR * capacity);
        }
    }

    private void putValue(K key, V value) {
        int remainder = Math.abs(getHash(key)) % capacity;
        if (arrWithKeyValues[remainder] == null) {
            arrWithKeyValues[remainder] = new Node<>(getHash(key), key, value, null);
            size++;
            return;
        }
        iterateAndPut(arrWithKeyValues[remainder], key, value);
    }

    private int getHash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private void iterateAndPut(Node<K, V> node, K key, V value) {
        Node<K, V> temp = node;
        while (temp != null) {
            if ((temp.key == null && key == null)
                    || (temp.key != null && temp.key.hashCode() == getHash(key) && temp.key.equals(key))) {
                temp.value = value;
                return;
            }
            if (temp.next == null) {
                temp.next = new Node<>(getHash(key), key, value, null);
                size++;
                return;
            }
            temp = temp.next;
        }
    }

    private void transformArray(Node<K, V>[] tempArr) {
        System.arraycopy(arrWithKeyValues, 0, tempArr, 0, arrWithKeyValues.length);
        arrWithKeyValues = new Node[capacity];
        size = 0;
        for (Node<K, V> node : tempArr) {
            if (node != null) {
                Node<K, V> tempNode = node;
                while (tempNode != null) {
                    putValue(tempNode.key, tempNode.value);
                    tempNode = tempNode.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
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
