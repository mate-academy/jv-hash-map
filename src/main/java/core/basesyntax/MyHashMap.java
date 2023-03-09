package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTORY = 0.75f;
    private static final int MAX_CAPACITY = 1 << 30;

    private Node<K, V>[] mapTable;
    private int size;
    private final float loadFactor;
    private int threshold;

    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTORY;
    }

    @Override
    public void put(K key, V value) {
        putValue(hashCode(key), key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node;
        return (node = getNode(key)) == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hashCode(Object key) {
        int hash;
        return key == null ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    private void putValue(int hash, K key, V value) {
        Node<K, V>[] table;
        Node<K, V> specificNode;
        int resizeTableLength;
        int i;
        if ((table = mapTable) == null || (resizeTableLength = table.length) == 0) {
            resizeTableLength = (table = resize()).length;
        }
        if ((specificNode = table[i = (resizeTableLength - 1) & hash]) == null) {
            table[i] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> tempNode;
            K tempKey;
            if (specificNode.hash == hash
                    && ((tempKey = specificNode.key) == key
                    || (key != null && key.equals(tempKey)))) {
                tempNode = specificNode;
            } else {
                for (; ; ) {
                    if ((tempNode = specificNode.next) == null) {
                        specificNode.next = new Node<>(hash, key, value, null);
                        break;
                    }
                    if (tempNode.hash == hash
                            && ((tempKey = tempNode.key) == key
                            || (key != null && key.equals(tempKey)))) {
                        break;
                    }
                    specificNode = tempNode;
                }
            }
            if (tempNode != null) {
                tempNode.value = value;
                return;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] oldTable = mapTable;
        int oldLength = (oldTable == null) ? 0 : oldTable.length;
        int oldThreshold = threshold;
        int newLength;
        int newThreshold = 0;
        if (oldLength > 0) {
            if (oldLength >= MAX_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTable;
            } else if ((newLength = oldLength << 1) < MAX_CAPACITY
                    && oldLength >= DEFAULT_CAPACITY) {
                newThreshold = oldThreshold << 1;
            }
        } else if (oldThreshold > 0) {
            newLength = oldThreshold;
        } else {
            newLength = DEFAULT_CAPACITY;
            newThreshold = (int) (DEFAULT_LOAD_FACTORY * DEFAULT_CAPACITY);
        }
        if (newThreshold == 0) {
            float tempThreshold = newLength * loadFactor;
            newThreshold = (newLength < MAX_CAPACITY && tempThreshold < (float) MAX_CAPACITY
                    ? (int) tempThreshold : Integer.MAX_VALUE);
        }
        threshold = newThreshold;
        @SuppressWarnings({"unchecked"})
        Node<K,V>[] newTable = new Node[newLength];
        mapTable = newTable;
        if (oldTable != null) {
            for (int i = 0; i < oldLength; ++i) {
                Node<K,V> oldTabElement;
                if ((oldTabElement = oldTable[i]) != null) {
                    oldTable[i] = null;
                    if (oldTabElement.next == null) {
                        newTable[oldTabElement.hash & (newLength - 1)] = oldTabElement;
                    } else {
                        Node<K,V> lhead = null;
                        Node<K,V> ltail = null;
                        Node<K,V> hhead = null;
                        Node<K,V> htail = null;
                        Node<K,V> nextNode;
                        do {
                            nextNode = oldTabElement.next;
                            if ((oldTabElement.hash & oldLength) == 0) {
                                if (ltail == null) {
                                    lhead = oldTabElement;
                                } else {
                                    ltail.next = oldTabElement;
                                }
                                ltail = oldTabElement;
                            } else {
                                if (htail == null) {
                                    hhead = oldTabElement;
                                } else {
                                    htail.next = oldTabElement;
                                }
                                htail = oldTabElement;
                            }
                        } while ((oldTabElement = nextNode) != null);
                        if (ltail != null) {
                            ltail.next = null;
                            newTable[i] = lhead;
                        }
                        if (htail != null) {
                            htail.next = null;
                            newTable[i + oldLength] = hhead;
                        }
                    }
                }
            }
        }
        return newTable;
    }

    private Node<K, V> getNode(K key) {
        Node<K, V>[] newTable = mapTable;
        Node<K, V> firstNode;
        Node<K,V> firstNodeNext;
        int newTableLength;
        int hash = hashCode(key);
        K elementKey;
        if (newTable != null && (newTableLength = newTable.length) > 0
                && (firstNode = newTable[(newTableLength - 1) & hash]) != null) {
            if (firstNode.hash == hash
                    && ((elementKey = firstNode.key) == key
                            || (key != null && key.equals(elementKey)))) {
                return firstNode;
            }
            if ((firstNodeNext = firstNode.next) != null) {
                do {
                    if (firstNodeNext.hash == hash
                            && ((elementKey = firstNodeNext.key) == key
                                    || (key != null && key.equals(elementKey)))) {
                        return firstNodeNext;
                    }
                } while ((firstNodeNext = firstNodeNext.next) != null);
            }
        }
        return null;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
