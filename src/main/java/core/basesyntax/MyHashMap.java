package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = getNode(key);
        if (node != null) {
            node.value = value;
        } else {
            int hash = hash(key);
            int index = indexOf(hash);
            addNode(key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int indexOf(int hash) {
        return hash & (table.length - 1);
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = newNode;
        } else {
            while (node.next != null) {
                node = node.next;
            }
            node.next = newNode;
        }
        size++;
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    private Node<K, V> getNode(K key) {
        int hash = hash(key);
        int index = indexOf(hash);
        Node<K, V> node = table[index];
        while (node != null) {
            if (elementsAreEqual(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private boolean elementsAreEqual(K firstElement, K secondElement) {
        return firstElement == secondElement
                || firstElement != null && firstElement.equals(secondElement);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                size--;
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
