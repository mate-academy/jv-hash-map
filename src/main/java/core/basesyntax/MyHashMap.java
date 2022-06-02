package core.basesyntax;

public class MyHashMap<K,V> implements MyMap<K,V> {
    private static final int DEFAULTCAPACITY = 16;
    private static final float DEFAULTLOADFACTOR = 0.75f;
    private int bucketsCapacity = DEFAULTCAPACITY;
    private Node<K,V>[] buckets = new Node[bucketsCapacity];
    private int size = 0;

    @Override
    public void put(K key, V value) {
        int hash = getHashCode(key);
        int index = hash % bucketsCapacity;
        Node<K,V> currentNode = getNode(key);
        if (currentNode != null) {
            currentNode.value = value;
        } else {
            buckets[index] = new Node<>(key, value, buckets[index], hash);
            size++;
        }
        if (size > bucketsCapacity * DEFAULTLOADFACTOR) {
            resize();
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newSize = bucketsCapacity * 2;
        Node<K,V>[] newNodeArray = new Node[newSize];
        for (int i = 0; i < bucketsCapacity; i++) {
            Node<K,V> currentNode = buckets[i];
            while (currentNode != null) {
                buckets[i] = buckets[i].next;
                currentNode.next = newNodeArray[currentNode.hashCode % newSize];
                newNodeArray[currentNode.hashCode % newSize] = currentNode;
                currentNode = buckets[i];
            }
        }
        bucketsCapacity = newNodeArray.length;
        buckets = newNodeArray;
    }

    private Node<K,V> getNode(K key) {
        int hash = getHashCode(key);
        Node<K,V> testNode = buckets[hash % bucketsCapacity];
        while (testNode != null) {
            if ((testNode.key == null && key == null)
                    || (testNode.key != null && testNode.key.equals(key))) {
                return testNode;
            }
            testNode = testNode.next;
        }
        return testNode;
    }

    private int getHashCode(K key) {
        return (key == null) ? 0 : (17 * 31 + key.hashCode() >>> 2);
    }

    @Override
    public V getValue(K key) {
        Node<K,V> currentNode = getNode(key);
        if (currentNode != null) {
            return currentNode.value;
        }
        return null;
    }

    private class Node<K,V> {
        private V value;
        private final K key;
        private Node<K, V> next;
        private final int hashCode;

        private Node(K key, V value, Node<K,V> next, int hashCode) {
            this.value = value;
            this.key = key;
            this.next = next;
            this.hashCode = hashCode;
        }
    }
}
