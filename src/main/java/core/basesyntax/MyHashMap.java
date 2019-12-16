package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int size;
    private Node[] elements = new Node[DEFAULT_INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (size > elements.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = key == null ? 0 : convertToHash(key);
        if (elements[index] == null) {
            size++;
            elements[index] = new Node(key, value, null);
        } else {
            hashPut(index, key, value);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return (V) elements[0].value;
        }
        int index = convertToHash(key);
        Node<K, V> newNode = elements[index];
        while (newNode != null) {
            if (key.equals(newNode.key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node next;

        Node(K key, V value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        size = 0;
        Node[] oldMap = elements;
        elements = new Node[capacity * 2];
        for (Node<K, V> newMap : oldMap) {
            while (newMap != null) {
                put(newMap.key, newMap.value);
                newMap = newMap.next;
            }
        }
        capacity *= 2;
    }

    public void hashPut(int index, K key, V value) {
        Node<K, V> copyOfNode = elements[index];
        while (copyOfNode != null) {
            if (key == copyOfNode.key || key.equals(copyOfNode.key)) {
                copyOfNode.value = value;
                return;
            }
            copyOfNode = copyOfNode.next;
        }
        Node<K, V> newElementsArray = new Node<>(key, value, elements[index]);
        elements[index] = newElementsArray;
        size++;
    }

    private int convertToHash(K key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode()) % elements.length + 1;
        }
    }
}
