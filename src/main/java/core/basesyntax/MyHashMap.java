package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private int size;
    private Node<K, V>[] array;

    public MyHashMap() {
        array = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= array.length * LOAD_FACTOR) {
            resize();
        }
        Node newNode = new Node(key, value, null);
        int index = findTheIndex(key);
        if (array[index] == null) {
            array[index] = newNode;
            size++;
            return;
        }
        makeLinks(newNode, index);
    }

    @Override
    public V getValue(K key) {
        int index = findTheIndex(key);
        Node<K, V> currentNode = array[index];
        while (currentNode != null) {
            if ((currentNode.key == key)
                    || (currentNode.key != null && currentNode.key.equals(key))) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int findTheIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % array.length;
    }

    private void makeLinks(Node newNode, int index) {
        Node currentNode = array[index];
        Node<K, V> tempNode = null;
        while (currentNode != null) {
            if ((currentNode.key == newNode.key)
                    || (currentNode.key != null && currentNode.key.equals(newNode.key))) {
                currentNode.value = newNode.value;
                return;
            }
            tempNode = currentNode;
            currentNode = currentNode.next;
        }
        tempNode.next = newNode;
        size++;
    }

    private void resize() {
        Node<K, V>[] oldArray = array;
        array = (Node<K, V>[]) new Node[oldArray.length * 2];
        size = 0;
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
