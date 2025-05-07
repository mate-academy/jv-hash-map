package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAX_CAPACITY = 1 << 30;
    private static int threshold;
    private Node<K, V>[] bucketsArray;
    private int size;

    @Override
    public void put(K key, V value) {
        putValue(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> correctNode = getNode(hash(key), key);
        return correctNode == null ? null : correctNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putValue(int hash, K key, V value) {
        int arrayLenght;
        Node<K, V> currentNode;
        if (bucketsArray == null || (arrayLenght = bucketsArray.length) == 0) {
            arrayLenght = (bucketsArray = resize()).length;
        }
        if ((currentNode = bucketsArray[hash & (arrayLenght - 1)]) == null) {
            bucketsArray[hash & (arrayLenght - 1)] = new Node<>(hash, key, value, null);
            size++;
        } else {
            if (currentNode.hash == hash || (key != null && key.equals(currentNode.key))) {
                currentNode.value = value;
            } else {
                Node<K, V> nextNode;
                while (true) {
                    nextNode = currentNode.next;
                    if (nextNode == null) {
                        currentNode.next = new Node<>(hash, key, value, null);
                        if (++size > threshold) {
                            bucketsArray = resize();
                        }
                        break;
                    }
                    if (nextNode.hash == hash || (key != null && key.equals(nextNode.key))) {
                        nextNode.value = value;
                        break;
                    }
                    currentNode = nextNode;
                }
            }
        }
    }

    private int hash(K key) {
        int hash;
        return key == null ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    private Node<K, V> getNode(int hash, K key) {
        int length;
        Node<K, V> firstOne;
        if (bucketsArray != null && (length = bucketsArray.length) > 0
                && (firstOne = bucketsArray[hash & (length - 1)]) != null) {
            if ((firstOne.hash == hash && key == firstOne.key)
                    || (key != null && key.equals(firstOne.key))) {
                return firstOne;
            }
            if (firstOne.next != null) {
                Node<K, V> nextValue = firstOne.next;
                do {
                    if ((nextValue.hash == hash && nextValue.key == key)
                            || (key != null && key.equals(nextValue.key))) {
                        return nextValue;
                    }
                    nextValue = nextValue.next;
                } while (nextValue != null);
            }
        }
        return null;
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] oldBuckets = bucketsArray;
        int oldCapacity = (oldBuckets == null) ? 0 : oldBuckets.length;
        int newCapacity = getNewCapacity(oldCapacity);
        Node<K, V>[] newBuckets = (Node<K, V>[]) new Node[newCapacity];
        bucketsArray = newBuckets;
        if (oldBuckets != null) {
            for (int i = 0; i < oldCapacity; i++) {
                Node<K, V> currentNode = oldBuckets[i];
                if (currentNode != null) {
                    oldBuckets[i] = null;
                    if (currentNode.next == null) {
                        newBuckets[currentNode.hash & (newCapacity - 1)] = currentNode;
                    } else {
                        Node<K, V> next;
                        do {
                            next = currentNode.next;
                            putValue(currentNode.hash, currentNode.key, currentNode.value);
                            size--;
                            currentNode = next;
                        } while (currentNode != null);
                    }
                }
            }
        }
        return newBuckets;
    }

    private int getNewCapacity(int oldCapacity) {
        int newCapacity = 0;
        if (oldCapacity > 0) {
            if (oldCapacity >= MAX_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                throw new RuntimeException("can't extend HashMap, current capacity: "
                        + oldCapacity + " is more than max capacity: " + MAX_CAPACITY);
            } else if (((oldCapacity * 2) < MAX_CAPACITY) && (oldCapacity >= DEFAULT_CAPACITY)) {
                newCapacity = oldCapacity * 2;
                threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
            }
        } else {
            newCapacity = DEFAULT_CAPACITY;
            threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
        return newCapacity;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }
}
