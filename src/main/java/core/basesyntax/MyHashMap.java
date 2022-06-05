package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static int DEFAULT_CAPACITY = 16;
    private static float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) LOAD_FACTOR * DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        validatedIndex(indexOf(key));
        if (size == threshold) {
            resize();
        }
        Node<K, V> node = new Node<>(key, value, null);
        if (table[indexOf(key)] == null) {
            table[indexOf(key)] = node;
            size++;
        } else {
            Node<K, V> currentNode = table[indexOf(key)];
            while (currentNode != null) {
                if (currentNode.key == key || currentNode.key != null
                        && currentNode.key.equals(key)) {
                    currentNode.value = value;
                    break;
                }
                if (currentNode.next == null) {
                    currentNode.next = node;
                    size++;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        validatedIndex(indexOf(key));
        Node<K, V> node = table[indexOf(key)];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int indexOf(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void validatedIndex(int index) {
        if (index < 0 || index >= table.length) {
            throw new ArrayIndexOutOfBoundsException("Index" + index + "is invalid");
        }
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length << 1;
        threshold = (int) LOAD_FACTOR * newCapacity;
        table = (Node<K, V>[]) new Node[newCapacity];
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
