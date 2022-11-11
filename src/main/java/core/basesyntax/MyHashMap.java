package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private int size;
    private MyNode<K,V>[] bucketArray;

    private class MyNode<K,V> {
        private K key;
        private V value;
        private MyNode<K, V> next;

        public MyNode(K key, V value, MyNode<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        bucketArray = new MyNode[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int arrayIndex = getIndexForKey(key);
        MyNode<K,V> tempNode = bucketArray[arrayIndex];
        MyNode<K,V> newNode = new MyNode<>(key, value, null);
        if (tempNode == null) {
            bucketArray[arrayIndex] = newNode;
            size++;
            checkSize();
            return;
        }
        while (tempNode != null) {
            if (newNode.key == null && tempNode.key == newNode.key
                    || newNode.key != null && isKeysEquals(tempNode, newNode.key)) {
                tempNode.value = newNode.value;
                break;
            } else if (tempNode.next == null) {
                tempNode.next = newNode;
                size++;
                break;
            }
            tempNode = tempNode.next;
        }
        checkSize();
    }

    @Override
    public V getValue(K key) {
        MyNode<K, V> tempNode;
        tempNode = bucketArray[getIndexForKey(key)];
        while (tempNode != null) {
            if ((tempNode.key == null && key == null)
                    || (key != null && isKeysEquals(tempNode, key))) {
                return tempNode.value;
            } else {
                tempNode = tempNode.next;
            }
        }
        return null;
    }

    public void clear() {
        new MyHashMap();
        size = 0;
    }

    public V remove(K key) {
        int arrayIndex = getIndexForKey(key);
        MyNode<K, V> tempNode;
        tempNode = bucketArray[arrayIndex];
        while (tempNode != null) {
            if ((tempNode.key == null && key == null)
                    || (key != null
                    && isKeysEquals(tempNode, key))) {
                tempNode.next = tempNode.next.next;
                size--;
                return tempNode.value;
            } else {
                tempNode = tempNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private MyNode<K, V>[] resize() {
        MyNode<K,V>[] oldBucketArray;
        oldBucketArray = bucketArray;
        int oldSize = size;
        size = 0;
        bucketArray = new MyNode[bucketArray.length << 1];
        for (MyNode<K,V> tempNode : oldBucketArray) {
            while (tempNode != null) {
                put(tempNode.key, tempNode.value);
                tempNode = tempNode.next;
            }
        }
        return bucketArray;
    }

    private int getIndexForKey(K key) {
        return key != null ? Math.abs(key.hashCode()) % bucketArray.length : 0;
    }

    private void checkSize() {
        if (size == bucketArray.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    private boolean isKeysEquals(MyNode node, K key) {
        return node.key != null && key.hashCode() != node.key.hashCode()
                ? false : key.equals(node.key);
    }
}
