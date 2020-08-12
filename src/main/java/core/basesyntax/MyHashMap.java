package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
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
            arrayValues[getIndex(key)] = new Node<>(key, value);
        } else {
            Node<K, V> nodeFromList = arrayValues[getIndex(key)];
            while (checkKey(nodeFromList.key, key) || nodeFromList.next != null) {
                if (checkKey(nodeFromList.key, key)) {
                    nodeFromList.value = value;
                    return;
                }
                nodeFromList = nodeFromList.next;
            }
            nodeFromList.next = new Node<>(key, value);
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
            if (checkKey(tempNode.key, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private boolean checkKey(K keyInArray, K key) {
        return keyInArray != null && keyInArray.equals(key) || keyInArray == key;
    }

    private void resize() {
        size = 0;
        capacity *= 2;
        Node<K, V>[] tempArray = arrayValues;
        arrayValues = new Node[capacity];
        for (int i = 0; i < tempArray.length; i++) {
            while (tempArray[i] != null) {
                put(tempArray[i].key, tempArray[i].value);
                tempArray[i] = tempArray[i].next;
            }
        }
    }

    private boolean checkSize() {
        return (size > (arrayValues.length * LOAD_FACTOR));
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
