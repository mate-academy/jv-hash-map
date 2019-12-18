package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPASITY = 16;
    private static final float DEFAULT_LOAD_FACTORY = 0.75f;
    private int sizeCounter;
    private int capacity;
    private Node<K, V>[] elementsTable;

    public MyHashMap() {
        sizeCounter = 0;
        capacity = DEFAULT_CAPASITY;
        elementsTable = new Node[capacity];
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

    @Override
    public void put(K key, V value) {
        if (sizeCounter >= capacity * DEFAULT_LOAD_FACTORY) {
            resize();
        }

        int index = indexByKeyHash(key);
        Node<K, V> tempNode = elementsTable[index];
        while (tempNode != null) {
            if (key == tempNode.key || (key != null && key.equals(tempNode.key))) {
                tempNode.value = value;
                return;
            }
            tempNode = tempNode.next;
        }
        Node<K, V> newNode = new Node<>(key, value, elementsTable[index]);
        elementsTable[index] = newNode;
        sizeCounter++;
    }

    @Override
    public V getValue(K key) {
        int index = indexByKeyHash(key);
        Node<K, V> tempNode = elementsTable[index];
        if (tempNode == null) {
            return null;
        }

        while (tempNode != null) {
            if (tempNode.key == key || (key != null && key.equals(tempNode.key))) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return sizeCounter;
    }

    private int indexByKeyHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % elementsTable.length - 1);
    }

    private void resize() {
        sizeCounter = 0;
        Node<K, V>[] tempNodeArray = elementsTable;
        capacity *= 2;
        elementsTable = new Node[capacity];
        for (Node<K, V> node : tempNodeArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}

