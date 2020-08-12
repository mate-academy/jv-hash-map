package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] hashMap;

    public MyHashMap() {
        size = 0;
        hashMap = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if ((LOAD_FACTOR * hashMap.length) <= size) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndex(key);
        if (hashMap[index] == null) {
            hashMap[index] = newNode;
            size++;
            return;
        }
        Node<K, V> tempNode = hashMap[index];
        while (tempNode.next != null) {
            if (tempNode.key == key || tempNode.key != null && key.equals(tempNode.key)) {
                tempNode.value = value;
                return;
            }
            tempNode = tempNode.next;
        }
        if (tempNode.key == key || tempNode.key != null && tempNode.key.equals(key)) {
            tempNode.value = value;
            return;
        }
        tempNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return hashMap[0].value;
        }
        int index = getIndex(key);
        Node<K, V> tempNode = hashMap[index];
        while (tempNode != null) {
            if (tempNode.key.equals(key)) {
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
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % (hashMap.length) + 1;
    }

    private void resize() {
        size = 0;
        int newLength = hashMap.length * 2;
        Node<K, V>[] tempArray = hashMap;
        hashMap = new Node[newLength];
        for (Node<K, V> node : tempArray) {
            if (node == null) {
                continue;
            }
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
