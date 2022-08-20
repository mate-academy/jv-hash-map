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
        Node currentNode = null;
        if (buckets[bucketIndex] != null) {
            currentNode = getNodeByKey(bucketIndex, key);
        }
        if (currentNode != null) {
            currentNode.value = value;
            return;
        }
        currentNode = new Node<>(key, value);
        addNode(bucketIndex, currentNode);
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key, buckets.length);
        Node currentNode = getNodeByKey(index, key);
        if (currentNode != null) {
            return (V) currentNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addNode(int index, Node node) {
        if (buckets[index] != null) {
            Node currentNode = buckets[index];
            linkNodes(currentNode, node);
            size++;
            resize();
            return;
        }
        buckets[index] = node;
        size++;
        resize();
    }

    private Node getNodeByKey(int index, K key) {
        Node currentNode = buckets[index];
        while (currentNode != null) {
            if (currentNode.key == key
                    || currentNode.key != null && currentNode.key.equals(key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private int getBucketIndex(K key, int capacity) {
        if (key == null) {
            return 0;
        }
        return key.hashCode() & (capacity - 1);
    }

    private void resize() {
        if (size > (int) (buckets.length * LOAD_FACTOR)) {
            Node[] newBuckets = new Node[(int) (buckets.length * 2)];
            for (Node node : buckets) {
                if (node == null) {
                    continue;
                }
                Node currentNode = node;
                while (currentNode != null) {
                    int index = getBucketIndex((K) currentNode.key, newBuckets.length);
                    if (newBuckets[index] != null) {
                        linkNodes(newBuckets[index], currentNode);
                    } else {
                        newBuckets[index] = currentNode;
                    }
                    Node temp = currentNode;
                    currentNode = currentNode.next;
                    temp.next = null;
                }
            }
            this.buckets = newBuckets;
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

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
