package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int INITIAL_CAPACITY = 16;
    private final float LOAD_FACTOR = 0.75f;
    private final int toResize = 2;
    private int size;
    private Node<K, V>[] myHashMapArray;

    private class Node<K,V> {
        final K key;
        V value;
        Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final V getValue()      { return value; }

        public final String toString() { return key + "=" + value; }
    }

    private int getBucketInHashMap(K key) {
        return key == null ? 0 : Math.abs(hashOfKey(key)) % myHashMapArray.length;
    }

    private int hashOfKey(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    @Override
    public V getValue(K key) {
        if (isEmpty()) {
            return null;
        }
        int position = getBucketInHashMap(key);
        Node<K, V> maybe = myHashMapArray[position];
        while(maybe != null){
            if ((key == null && maybe.key == null)
                || (key != null
                && hashOfKey(maybe.key) == hashOfKey(key)
                && key.equals(maybe.key))){
                    return maybe.value;
            }
            maybe = maybe.next;
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        checkBeforePut();
        Node newNode = new Node(key, value, null);
        int position = getBucketInHashMap(key);
        if (myHashMapArray[position] == null) {
            myHashMapArray[position] = newNode;
            size++;
        } else {
            Node bucketToPutIn = myHashMapArray[position];
            while (bucketToPutIn != null) {
                if ((bucketToPutIn.key == null && newNode.key == null)
                    || (newNode.key != null
                        && hashOfKey(newNode.key) == hashOfKey(bucketToPutIn.key)
                        && newNode.key.equals(bucketToPutIn.key))) {
                    bucketToPutIn.value = newNode.value;
                    return;
                }
                if (bucketToPutIn.next == null) {
                    bucketToPutIn.next = newNode;
                    size++;
                    return;
                }
                bucketToPutIn = bucketToPutIn.next;
            }
        }
    }

    private void checkBeforePut() {
        if (myHashMapArray == null) {
            myHashMapArray = new Node[INITIAL_CAPACITY];
        }
        if (size == (int) myHashMapArray.length * LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        Node<K,V>[] oldHashMap = myHashMapArray;
        myHashMapArray = new Node[oldHashMap.length * toResize];
        size = 0;
        for (Node<K,V> node : oldHashMap) {
            while(node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int getSize() {
        return size;
    }
}
