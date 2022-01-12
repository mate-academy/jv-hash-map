package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private final int defaultcapacity = 16;
    private final float defaultloadfactor = 0.75f;
    private int bucketsCapacity = defaultcapacity;
    private Node<K,V>[] buckets = new Node[bucketsCapacity];
    private int size = 0;

    private class Node<K,V> {
        private V value;
        private Node<K,V> next;
        private final int hash;
        private final K key;

        private Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = hash % bucketsCapacity;
        Node<K,V> currNode = getNode(key);
        if (currNode != null) {
            currNode.value = value;
        } else {
            buckets[index] = new Node<>(hash,key,value,buckets[index]);
            size++;
        }
        if (size > bucketsCapacity * defaultloadfactor) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> currNode = getNode(key);
        if (currNode != null) {
            return currNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {

        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode() >>> 1;
    }

    // return node by key or null if not found
    private Node<K,V> getNode(K key) {
        int hash = hash(key);
        Node<K,V> tmpnode = buckets[hash % bucketsCapacity];
        while (tmpnode != null) {
            if ((tmpnode.key == null && key == null)
                    || (tmpnode.key != null && tmpnode.key.equals(key))) {
                return tmpnode;
            }
            tmpnode = tmpnode.next;
        }
        return tmpnode;
    }

    private void resize() {

        int newCap = bucketsCapacity << 1;
        Node<K,V>[] newBuckets = new Node[newCap];
        for (int i = 0; i < bucketsCapacity; i++) {
            Node<K,V> currentBucket = buckets[i];
            while (currentBucket != null) {
                buckets[i] = buckets[i].next;
                currentBucket.next = newBuckets[currentBucket.hash % newCap ];
                newBuckets[currentBucket.hash % newCap ] = currentBucket;
                currentBucket = buckets[i];
            }
        }
        bucketsCapacity = newBuckets.length;
        buckets = newBuckets;
    }
}

