package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public class Node<K , V>{
        private int hash;
        private K key;
        private V value;
        Node<K, V> next;


        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    private  Node<K, V> tail;

    public static final int DEFAULT_CAPACITY = 16;

    private Node<K, V>[] elements = new Node[DEFAULT_CAPACITY];

    private int count;

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key,value);
        if (count == 0){
            elements[0] = newNode;
            tail = newNode;
        } else if (count > DEFAULT_CAPACITY){
            elements = resize(elements);

        } else {
            int index = getIndex(key);
            elements[index] = newNode;
            tail.next = newNode;
        }
        count++;


    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> obj : elements) {
            if (obj.key.equals(key)){
                return obj.value;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return count;
    }

    private int getIndex(Object o){
        return o.hashCode() % DEFAULT_CAPACITY;
    }

    private Node<K, V>[] resize(Node<K, V>[] nodes){
        int newCapacity = DEFAULT_CAPACITY * 2;
        Node<K, V> [] newArray = new Node[newCapacity];
        int index = 0;
        for (int i = 0; i < elements.length; i++) {
            newArray[i % newCapacity] = elements[i];
        }
        return newArray;

    }

}
