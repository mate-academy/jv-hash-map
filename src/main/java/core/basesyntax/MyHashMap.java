package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int DEFAULT_CAPACITY = 16;
    private int size;

    public MyHashMap() {
        Node<K, V>[] table = (Node<K, V>[]) new Object[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
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
}
