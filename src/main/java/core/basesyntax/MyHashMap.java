package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private int size = 0;
    private MyNode<K,V>[] bucketArray = new MyNode[DEFAULT_INITIAL_CAPACITY];

    class MyNode<K,V> {
        private int hash;
        private K key;
        private V value;
        private MyNode<K, V> next;

        public MyNode(int hash, K key, V value, MyNode<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public MyNode() {

        }
    }

    public MyHashMap() {

    }

    @Override
    public void put(K key, V value) {
        int arrayIndex = arrayIndexCalculation(key);
        MyNode<K,V> tempNode = new MyNode<>();
        MyNode<K,V> newNode = new MyNode<>(key != null ? key.hashCode() : 0, key, value, null);
        tempNode = bucketArray[arrayIndex];

        do {
            if (tempNode == null) {
                bucketArray[arrayIndex] = newNode;
                size++;
                break;
            } else if (newNode.key == null && tempNode.key == newNode.key) {
                tempNode.value = newNode.value;
                break;
            } else if (newNode.key == null && tempNode.next == null) {
                tempNode.next = newNode;
                size++;
                break;
            } else if (tempNode.key != null && tempNode.key.equals(newNode.key)) {
                tempNode.value = newNode.value;
                break;
            } else if (tempNode.next == null) {
                tempNode.next = newNode;
                size++;
                break;
            }
            tempNode = tempNode.next;
        } while (tempNode != null);
        if (size == bucketArray.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        return getRemove(key, true);
    }

    public void clear() {
        bucketArray = new MyNode[DEFAULT_INITIAL_CAPACITY];
    }

    public V remove(K key) {
        return getRemove(key, false);
    }

    @Override
    public int getSize() {
        return size;
    }

    private V getRemove(K key, boolean getTrue) {
        int arrayIndex = arrayIndexCalculation(key);
        MyNode<K, V> tempNode = new MyNode<>();
        tempNode = bucketArray[arrayIndex];
        if (tempNode != null) {
            do {
                if ((tempNode.key == null && key == null)
                        || (key != null && key.equals(tempNode.key))) {
                    if (!getTrue) {
                        tempNode.next = tempNode.next.next;
                        size--;
                    }
                    return tempNode.value;
                } else {
                    tempNode = tempNode.next;
                }
            } while (tempNode != null);
        }

        return null;
    }

    private MyNode<K, V>[] resize() {
        MyNode<K,V>[] oldBucketArray = new MyNode[bucketArray.length];

        oldBucketArray = bucketArray;
        int oldSize = size;
        size = 0;
        bucketArray = new MyNode[bucketArray.length << 1];
        for (MyNode<K,V> tempNode : oldBucketArray) {
            do {
                if (tempNode != null) {
                    put(tempNode.key, tempNode.value);
                    tempNode = tempNode.next;
                }
            } while (tempNode != null);
        }

        return bucketArray;
    }

    private int arrayIndexCalculation(K key) {
        if (key != null) {
            return Math.abs(key.hashCode()) % DEFAULT_INITIAL_CAPACITY;
        } else {
            return 0;
        }
    }
}
