package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private transient Node<K, V>[] table;

    static class Node<K, V> {
        final int hash;
        K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
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
        return 0;
    }
}
