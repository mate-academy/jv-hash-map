package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final float loadFactor = 0.75f;
    private int threshold;
    private int size;
    private int actualCapacity;
    private Node<K, V>[] table;

    public MyHashMap() {
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

    private void resize(){};

    /*
    returns hash value for rearrangement of the table after resize;
     */
    private int getHash(Node<K, V> node){
        return 0;
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;
        private int hash;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.next = next;
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
