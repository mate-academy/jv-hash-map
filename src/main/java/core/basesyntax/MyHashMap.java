package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node[] buckets;
    private int size;

    public MyHashMap() {
        this.buckets = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key, buckets.length);
        if (buckets[bucketIndex] == null) {
            buckets[bucketIndex] = new Node(key, value);
            size++;
            resize();
            return;
        }
        Node currentNode = getNodeByKey(bucketIndex, key);
        if (keyMatch(currentNode, key)) {
            currentNode.value = value;
            return;
        }
        currentNode.next = new Node(key, value);
        size++;
        resize();
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key, buckets.length);
        if (buckets[index] == null) {
            return null;
        }
        Node currentNode = getNodeByKey(index, key);
        if (keyMatch(currentNode, key)) {
            return (V) currentNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node getNodeByKey(int index, K key) {
        Node currentNode = buckets[index];
        Node temp = null;
        while (currentNode != null) {
            if (keyMatch(currentNode, key)) {
                return currentNode;
            }
            temp = currentNode;
            currentNode = currentNode.next;
        }
        return temp;
    }

    private int getBucketIndex(K key, int capacity) {
        return key == null ? 0 : key.hashCode() & (capacity - 1);
    }

    private boolean keyMatch(Node node, K key) {
        return node.key == key
                || node.key != null && node.key.equals(key);
    }

    private void resize() {
        if (size > (int) (buckets.length * LOAD_FACTOR)) {
            Node[] previousBuckets = buckets;
            buckets = new Node[(int) (buckets.length * 2)];
            size = 0;
            for (Node node : previousBuckets) {
                if (node == null) {
                    continue;
                }
                Node currentNode = node;
                while (currentNode != null) {
                    put((K) currentNode.key, (V) currentNode.value);
                    Node temp = currentNode;
                    currentNode = currentNode.next;
                    temp.next = null;
                }
            }
        }
    }

    private void linkNodes(Node node, Node nextNode) {
        if (node.next == null) {
            node.next = nextNode;
            return;
        }
        Node currentNode = node;
        while (currentNode.next != null) {
            currentNode = currentNode.next;
        }
        currentNode.next = nextNode;
    }

    private static class Node<K extends Object,V extends Object> {
        private Object key;
        private Object value;

        private Node next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
