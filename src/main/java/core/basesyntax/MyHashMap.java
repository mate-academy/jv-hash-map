package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPASITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] hashMap;

    public MyHashMap() {
        size = 0;
        hashMap = new Node[INITIAL_CAPASITY];
    }

    @Override
    public void put(K key, V value) {
        if ((LOAD_FACTOR * hashMap.length) <= size) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        if (key == null) {
            if (hashMap[0] == null) {
                size++;
            }
            hashMap[0] = newNode;
            return;
        }
        int index = getIndex(key);
        if (hashMap[index] == null) {
            hashMap[index] = newNode;
            size++;
            return;
        }
        Node<K, V> tempNode = hashMap[index];
        while (tempNode.next != null) {
            if (key.equals(tempNode.key)) {
                tempNode.value = value;
                return;
            }
            tempNode = tempNode.next;
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
        return Math.abs(key.hashCode()) % (hashMap.length) + 1;
    }

    private void resize() {
        Node<K, V>[] tempArray = hashMap;
        int newLength = hashMap.length * 2;
        hashMap = new Node[newLength];
        for (Node<K, V> node : tempArray) {
            while (node != null) {
                int index = getIndex(node.key);
                if (hashMap[index] == null) {
                    hashMap[index] = node;
                    return;
                }
                Node<K, V> tempNode = node;
                while (tempNode.next != null) {
                    tempNode = tempNode.next;
                }
                tempNode.next = new Node<>(node.key, node.value, null);
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
