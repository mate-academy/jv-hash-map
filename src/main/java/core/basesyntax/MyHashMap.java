package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int capacity;
    private int size;

    private int threshold;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        if (table == null || size > threshold) {
            resize();
        }
        if (table[getBucketIndex(key)] == null ) {//when bucket index FREE
            table[getBucketIndex(key)] = new Node<>(key.hashCode(), key, value, null);
        }
        if (table[getBucketIndex(key)].key == null && key == null) {
                table[getBucketIndex(key)].value = value;
                return;
            }
        if (table[getBucketIndex(key)].key.equals(key)) {
                table[getBucketIndex(key)].value = value;
                return;
            }
        if (table[getBucketIndex(key)].next != null) {//backed with list
                Node<K,V> current = table[getBucketIndex(key)].next;//HEAD
                while (current != null) {//go while next will be NULL, when NULL make newNode
                    if (current.key.equals(key)) {
                        current.value = value;

                    }
                    if (current.next == null) {
                        current.next = new Node<>(key.hashCode(),key,value,null);
                        return;
                    }
                    current = current.next;
                }
            }
        }
    }



    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K,V>[] newTable;
        Node <K,V> currentNode;
        if (size > threshold && table != null) {
            capacity = capacity * 2;
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            newTable = (Node<K,V>[])new MyHashMap.Node[capacity];
            for (Node<K, V> kvNode : table) {
                if (kvNode != null) {
                    newTable[getBucketIndex(kvNode.key)] = kvNode;
                    if (kvNode.next != null) {
                        currentNode = kvNode.next;
                        while (currentNode != null) {
                            newTable[getBucketIndex(currentNode.key)] = currentNode;
                            currentNode = currentNode.next;
                        }
                    }
                }
            }
            table = newTable;
        }
        else {
            capacity = DEFAULT_INITIAL_CAPACITY;
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            table = (Node<K,V>[])new MyHashMap.Node[capacity];
        }
    }

    @Override
    public V getValue(K key) {
        if(size > 0) {
            Node<K,V> node = table[getBucketIndex(key)];
            if (node != null) {
                if ((node.key != null && (node.key.equals(key)))
                        || (node.key == null && key == null)) {//when Node.key == null
                    return node.value;
                } else {
                    return searchForCollision(node, key);
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int getBucketIndex(K key) {
        int hash = key == null ? 0 : key.hashCode() % capacity;
        return hash > 0 ? hash : -hash;
    }

    private Node<K,V> getNewNode(int hash, K key, V value) {
        return new Node<>(hash, key, value,null);
    }

    private V searchForCollision(Node<K,V> node, K key) {
        while (node != null) {
            if (node.key.equals(key)) {
                return node.value;
            } else {
                node = node.next;
            }
        }
        return null;
    }

    private static class Node<K,V> {
        private final int hashCode;
        private K key;
        private V value;
        private Node<K,V> next;

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (o.getClass() == this.getClass()) {
                Node<K,V> object = (Node<K,V>) o;
                return (this.hashCode == object.hashCode) && ((this.key != null && object.key != null)
                        ? (this.key.equals(object.key)) : (this.key == null & object.key == null))
                        && ((this.value != null && object.value != null)
                        ? (this.value.equals(object.value)) : (this.value == null && object.value == null))
                        && ((this.next != null && object.next != null)
                        ? this.next.equals(object.next) : (this.next == null && object.next == null));
            }
            return false;
        }

        @Override
        public int hashCode() {
            return 17 * (this.key == null ? 0 : key.hashCode()) + (this.value == null ? 0 : value.hashCode())
                    + (this.next == null ? 0 : this.next.hashCode()) + hashCode;
        }

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hashCode = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
