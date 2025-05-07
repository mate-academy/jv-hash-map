package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private int size;
    private MyNode<K,V>[] bucketArray;

    public MyHashMap() {
        bucketArray = new MyNode[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int arrayIndex = getIndexForKey(key);
        MyNode<K,V> tempNode = bucketArray[arrayIndex];
        MyNode<K,V> newNode = new MyNode<>(key, value, null);
        if (tempNode == null) {
            bucketArray[arrayIndex] = newNode;
            size++;
            resizeIfNeed();
            return;
        } else {
            while (tempNode != null) {
                if (isKeysEquals(tempNode.key, newNode.key)) {
                    tempNode.value = newNode.value;
                    break;
                } else if (tempNode.next == null) {
                    tempNode.next = newNode;
                    size++;
                    break;
                }
                tempNode = tempNode.next;
            }
        }
        resizeIfNeed();
    }

    @Override
    public V getValue(K key) {
        MyNode<K, V> tempNode;
        tempNode = bucketArray[getIndexForKey(key)];
        while (tempNode != null) {
            if (isKeysEquals(tempNode.key, key)) {
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
            if (isKeysEquals(tempNode.key, key)) {
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

    private void resizeIfNeed() {
        if (size == bucketArray.length * LOAD_FACTOR) {
            resize();
        }
    }

    private boolean isKeysEquals(K keyOne, K keyTwo) {
        return keyOne == keyTwo || keyOne != null && keyOne.equals(keyTwo);
    }
}
