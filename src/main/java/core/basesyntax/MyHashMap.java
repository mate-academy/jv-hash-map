package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_COEFFICIENT = 2;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value);
        Node currentNode = table[buckedIndex(key)];
        if (currentNode == null) {
            table[buckedIndex(key)] = newNode;
            size++;
        }
        while (currentNode != null) {
            if (currentNode.key == key || currentNode.key != null && currentNode.key.equals(key)) {
                currentNode.value = value;
                break;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                break;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[buckedIndex(key)];
        while (node != null) {
            if ((node.key == key) || (node.key != null && node.key.equals(key))) {
                return node.value;
            } else {
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[table.length * GROW_COEFFICIENT];
        size = 0;
        for (Node<K, V> node : oldTable) {
            Node<K, V> newNode = node;
            while (newNode != null) {
                put(newNode.key, newNode.value);
                newNode = newNode.next;
            }
        }
    }

    private int buckedIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
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
