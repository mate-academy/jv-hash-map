package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int INITIAL_CAPACITY = 16;
    static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int capacity;
    private Node<K, V>[] arrayValues;

    public MyHashMap() {
        arrayValues = new Node[INITIAL_CAPACITY];
        capacity = INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (arrayValues[getIndex(key)] == null) {
            arrayValues[getIndex(key)] = new Node<>(key, value, null);
        } else {
            Node<K, V> nodeFromList = arrayValues[getIndex(key)];
            while (checkKey(nodeFromList.getKey(), key) || nodeFromList.getNext() != null) {
                if (checkKey(nodeFromList.getKey(), key)) {
                    nodeFromList.setValue(value);
                    return;
                }
                nodeFromList = nodeFromList.getNext();
            }
            nodeFromList.next = new Node<>(key, value, null);
        }
        size++;
        if (checkSize()) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> tempNode = arrayValues[getIndex(key)];
        while (tempNode != null) {
            if (checkKey(tempNode.getKey(), key)) {
                return tempNode.getValue();
            }
            tempNode = tempNode.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return hash(key) % capacity;
    }

    private boolean checkKey(K keyInArray, K key) {
        return keyInArray != null && keyInArray.equals(key) || keyInArray == key;
    }

    private void resize() {
        size = 0;
        capacity = capacity * 2;
        Node<K, V>[] tempArray = arrayValues;
        arrayValues = new Node[capacity];
        for (int i = 0; i < tempArray.length; i++) {
            while (tempArray[i] != null) {
                put(tempArray[i].getKey(), tempArray[i].getValue());
                tempArray[i] = tempArray[i].next;
            }
        }
    }

    private boolean checkSize() {
        return (size > (arrayValues.length * LOAD_FACTOR));
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : Math.abs((h = 37 + key.hashCode()) ^ (h >>> 16));
    }

    public static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, MyHashMap.Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setValue(V newValue) {
            value = newValue;
        }
    }
}
