package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshole;
    private Node<K, V>[] buckets;


    @Override
    public void put(K key, V value) {
        int index  = Math.abs(key.hashCode() % buckets.length);
        Node<K, V> currNode = buckets[index];
        if (currNode == null) {
            buckets[index] = new Node<>(key, value, null);
        } else {
            while (currNode.next != null) {
                currNode = currNode.next;
            }
            currNode.next = new Node<>(key, value, null);
        }
        if (++size == threshole) {
            resize();
        }
    }

    private void resize() {

    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
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
