package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int STOCK_ARR_LENGTH = 16;
    private static final float MAX_FILL = 0.75f;
    private static final int GROWTH_COEFICIENT = 2;
    private Node[] arr;
    private int size;

    public MyHashMap() {
        this.arr = new Node[STOCK_ARR_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        int pos = bucketFor(key);
        if (arr[pos] == null) {
            arr[pos] = new Node<>(key, value);
            increaseSize();
            return;
        }
        Node tempNode = findNode(key);
        if (keysAreEqual(key, (K)tempNode.key)) {
            tempNode.value = value;
            return;
        }
        tempNode.next = new Node<>(key, value);
        increaseSize();
    }

    @Override
    public V getValue(K key) {
        Node tempNode = findNode(key);
        return arr[bucketFor(key)] == null || !keysAreEqual(key, (K)findNode(key).key)
                ? null : (V)tempNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int bucketFor(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % arr.length;
    }

    private Node findNode(K key) {
        Node currNode = arr[bucketFor(key)];
        if (currNode == null) {
            return null;
        }
        while (currNode.next != null) {
            if (keysAreEqual(key, (K)currNode.key)) {
                return currNode;
            }
            currNode = currNode.next;
        }
        return currNode; //last node in bucket
    }

    private void increaseSize() {
        if (++size >= arr.length * MAX_FILL) {
            growArr();
        }
    }

    private void growArr() {
        Node[] oldArrCash = arr;
        arr = new Node[arr.length * GROWTH_COEFICIENT];
        size = 0;
        for (Node node : oldArrCash) {
            while (node != null) {
                this.put((K)node.key, (V)node.value);
                node = node.next;
            }
        }
    }

    private boolean keysAreEqual(K key1, K key2) {
        return key1 == key2 || key1 != null && key1.equals(key2);
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
