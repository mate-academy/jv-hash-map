package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final static int DEFAULT_CAPACITY = 16;
    private final static float LOAD_FOLDER = 0.75f;
    private int size = 0;
    private Node<K,V>[] table;

    @Override
    public void put(K key, V value) {

    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    private void checkCapacity() {
        if (size >= table.length * LOAD_FOLDER) {
            Node<K,V>[] newTable = (Node<K, V>[]) new Node[table.length * 2];
            Node<K, V> currentNode;
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null) {
                    currentNode = table[i];
                    if (currentNode.next != null) {
                        while (currentNode != null) {
                            newTable = putValue(currentNode.key, currentNode.value, newTable);
                            currentNode = currentNode.next;
                        }
                    } else {
                        newTable = putValue(currentNode.key, currentNode.value, newTable);
                    }
                }
            }
            table = newTable;
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode() * 31;
    }

    private int index(int length, int hash) {
        return length - 1 & hash;
    }

    private static class Node<K, V> {
        private Node next;
        private final int hash;
        private V value;
        private final K key;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }
}
