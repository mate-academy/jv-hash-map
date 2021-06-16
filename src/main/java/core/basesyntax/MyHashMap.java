package core.basesyntax;


public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTORY = 0.75f;

    private Node<K, V>[] table;


    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
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



    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }
}
