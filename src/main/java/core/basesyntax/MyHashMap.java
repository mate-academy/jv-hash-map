package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private int arraySize = 16;
    private int size = 0;
    private Node<K, V>[] array;

    public MyHashMap() {
        array = new Node[arraySize];
    }

    @Override
    public void put(K key, V value) {
        if (size == (int) (arraySize * LOAD_FACTOR)) {
            resizeArray();
        }
        if (key == null) {
            putNullKey(value);
            return;
        }
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (array[index] == null) {
            array[index] = newNode;
        } else {
            Node<K, V> previousNode = null;
            Node<K, V> currentNode = array[index];
            while (currentNode != null) {
                if (currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            if (previousNode != null) {
                previousNode.next = newNode;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return array[0].value;
        }
        int index = getIndex(key);
        Node<K, V> newNode = array[index];
        while (newNode != null) {
            if (newNode.key.equals(key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return hashCode() % arraySize;
    }

    private void resizeArray() {
        arraySize = arraySize * 2;
        Node<K, V>[] newArray = new Node[arraySize];
        transfer(newArray);
        array = newArray;
    }

    private void transfer(Node<K, V>[] newArray) {
        for (int i = 0; i < array.length; i++) {
            Node<K, V> node = array[i];
            if (node != null) {
                array[i] = null;
                do {
                    Node<K, V> next = node.next;
                    int index = getIndex(node.key);
                    node.next = newArray[index];
                    newArray[index] = node;
                    node = next;
                } while (node != null);
            }
        }
    }

    private void putNullKey(V value) {
        if (array[0] == null) {
            array[0] = new Node<>(null, value, null);
            size++;
        } else {
            array[0].value = value;
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public int hashCode() {
            int prime = 17;
            if (key != null) {
                prime = prime * key.hashCode() * value.hashCode();
                return prime;
            }
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || !this.getClass().getName().equals(o.getClass().getName())) {
                return false;
            }
            Node<K, V> e = (Node<K, V>) o;
            return this.key == e.key;
        }
    }
}
