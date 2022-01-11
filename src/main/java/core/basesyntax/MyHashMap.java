package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int bucketsCapacity = 16;
    private float loadFactor = 0.75f;
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
        Node<K,V> currNode = getKey(key);
        if (currNode != null) {
            currNode.value = value;
        } else {
            buckets[index] = new Node<>(hash,key,value,buckets[index]);
            size++;
        }
        if (size > bucketsCapacity * loadFactor) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> currNode = getKey(key);
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
    private Node<K,V> getKey(K key) {
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
            Node<K,V> e = buckets[i];
            while (e != null) {
                buckets[i] = buckets[i].next;
                e.next = newBuckets[e.hash % newCap ];
                newBuckets[e.hash % newCap ] = e;
                e = buckets[i];
            }
        }
        bucketsCapacity = newBuckets.length;
        buckets = newBuckets;
    }
}

