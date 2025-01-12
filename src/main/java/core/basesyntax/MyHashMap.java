package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFOULT_START_CAPACITY = 16;
    static final double DEFOULT_LOAD_FACTOR = 0.75;
    private int size;
    private int capasity;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFOULT_START_CAPACITY];
        capasity = DEFOULT_START_CAPACITY;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (isFull()) {
            resize();
        }

        int hash = hash(key);
        if (hash == 0) {
            nullAdd(key, value);
            return;
        }

        if (table[hash] != null) {
            Node<K, V> currNode = table[hash];

            boolean isTail = false;
            while (!isTail) {
                if (key == null && currNode.getKey() == null) {
                    currNode.setValue(value);
                    return;
                }
                if (currNode.getKey() == null) {
                    currNode = currNode.next;
                    continue;
                }
                if (currNode.getKey().equals(key)) {
                    currNode.setValue(value);
                    return;
                }
                if (currNode.next == null) {
                    currNode.next = new Node<>(key, value, hash, null);
                    isTail = true;
                    size++;
                    return;
                }
                currNode = currNode.next;
            }
        }

        table[hash] = new Node<>(key, value, key.hashCode(), null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        if (table[hash] == null) {
            return null;
        }

        Node<K, V> currNode = table[hash];
        boolean isTail = false;

        while (!isTail) {
            if (currNode.getKey() == null && key == null) {
                return currNode.getValue();
            }
            if (currNode.getKey() != null) {
                if (currNode.getKey().equals(key)) {
                    return currNode.getValue();
                }
            }
            if (currNode.next == null) {
                isTail = true;
                return null;
            }
            currNode = currNode.next;
        }
        return currNode.getValue();
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean isFull() {
        return size + 1 > (int) capasity * DEFOULT_LOAD_FACTOR;
    }

    private void resize() {
        capasity *= 2;
        Node<K, V>[] newTable = new Node[capasity];

        for (Node<K, V> bucket : table) {
            while (bucket != null) {
                int newHash = (bucket.getKey() == null ? 0 : Math.abs(bucket.getKey().hashCode()))
                        % capasity;
                Node<K, V> nextNode = bucket.next;
                bucket.next = newTable[newHash];
                newTable[newHash] = bucket;
                bucket = nextNode;
            }
        }

        table = newTable;
    }

    private int hash(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode())) % capasity;
    }

    private void nullAdd(K key, V value) {
        Node<K, V> currNode = table[0];
        boolean isTail = false;
        while (!isTail) {
            if (currNode == null) {
                table[0] = new Node<>(key, value, 0, null);
                size++;
                return;
            }
            if (key == null && currNode.getKey() == null) {
                currNode.value = value;
                return;
            }
            if (currNode.next == null && key == null) {
                currNode.next = new Node<>(key, value, 0, null);
                isTail = true;
                size++;
                return;
            }
            if (currNode.next == null) {
                currNode.next = new Node<>(key, value, 0, null);
                isTail = true;
                size++;
                return;
            }
            currNode = currNode.next;
        }
    }

    static class Node<K, V> {
        private K key;
        private V value;
        private int hashCode;
        private Node<K, V> next;

        public Node(K key, V value, int hashCode, Node<K, V> next) {
            this.value = value;
            this.key = key;
            this.hashCode = hashCode;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public int getHashCode() {
            return hashCode;
        }

        public void setHashCode(int hashCode) {
            this.hashCode = hashCode;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}
