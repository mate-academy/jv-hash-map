package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private Node<K, V>[] hashMapArray;
    private int size;

    private static class Node<K, V> {
        private final K nodeKey;
        private V nodeValue;
        private Node<K, V> nextNode;

        public Node(K key, V value, Node<K, V> next) {
            nodeKey = key;
            nodeValue = value;
            nextNode = next;
        }
    }

    public MyHashMap() {
        hashMapArray = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size == hashMapArray.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = findIndex(key);
        Node<K, V> newElement = findLastNode(key);
        if (newElement != null) {
            newElement.nodeValue = value;
        } else {
            hashMapArray[index] = new Node<K, V>(key, value, hashMapArray[index]);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = findIndex(key);
        Node<K, V> newElement = findLastNode(key);
        if (newElement != null) {
            return newElement.nodeValue;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int findIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % hashMapArray.length;
    }

    private Node findLastNode(K key) {
        Node<K, V> newNode = hashMapArray[findIndex(key)];
        while (newNode != null) {
            if (key == newNode.nodeKey || key != null && key.equals(newNode.nodeKey)) {
                return newNode;
            }
            newNode = newNode.nextNode;
        }
        return null;
    }

    private void resize() {
        Node<K, V>[] newHashMapArray = new Node[hashMapArray.length * 2];
        size = 0;
        Node<K, V>[] oldHashMapArray = hashMapArray;
        hashMapArray = newHashMapArray;
        for (int i = 0; i < oldHashMapArray.length; i++) {
            while (oldHashMapArray[i] != null) {
                put(oldHashMapArray[i].nodeKey, oldHashMapArray[i].nodeValue);
                oldHashMapArray[i] = oldHashMapArray[i].nextNode;
            }
        }
    }
}

