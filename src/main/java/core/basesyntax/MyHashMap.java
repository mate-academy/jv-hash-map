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
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
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
        int index = findIndex(key, hashMapArray.length);
        Node<K, V> newElement = findLastNode(key, index);
        if (newElement != null) {
            newElement.value = value;
        } else {
            hashMapArray[index] = new Node<K, V>(key, value, hashMapArray[index]);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = findIndex(key, hashMapArray.length);
        Node<K, V> newElement = findLastNode(key, index);
        if (newElement != null) {
            return newElement.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int findIndex(K key, int length) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % length;
    }

    private Node findLastNode(K key, int index) {
        Node<K, V> newNode = hashMapArray[index];
        while (newNode != null) {
            if (key == newNode.key || key != null && key.equals(newNode.key)) {
                return newNode;
            }
            newNode = newNode.next;
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
                put(oldHashMapArray[i].key, oldHashMapArray[i].value);
                oldHashMapArray[i] = oldHashMapArray[i].next;
            }
        }
    }
}

