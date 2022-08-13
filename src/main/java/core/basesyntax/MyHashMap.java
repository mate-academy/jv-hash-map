package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private int size;

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

    static class Node<K, V> {
        int hash;
        K key;
        V Value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            Value = value;
            this.next = next;
        }

    }
}
