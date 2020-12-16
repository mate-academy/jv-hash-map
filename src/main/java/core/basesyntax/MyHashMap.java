package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private static int capacity = 1;
    private int size = 0;
    private int hash;
    private Node<K, V>[] nodesArray;


    @Override
    public void put(K key, V value) {
        initializeArr();
        hash = setHash(key);
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (nodesArray[hash] != null) {
            Node<K, V> iterrarion = nodesArray[hash];
            if(Objects.equals(iterrarion.key, key)){
                iterrarion.value = newNode.value;
                return;
            }
            while (iterrarion.next != null){
                if(Objects.equals(iterrarion.key,key)) {
                    iterrarion.value = value;
                    return;
                }
                iterrarion = iterrarion.next;
            }
            iterrarion.next = newNode;
            size++;
        }
        if (nodesArray[hash] == null) {
            nodesArray[hash] = newNode;
            size++;
        }

    }


    @Override
    public V getValue(K key) {
        hash = setHash(key);
        if(nodesArray == null|| nodesArray[hash] == null) {
            return null;
        }
        Node<K, V> iterrarion = nodesArray[hash];
        while (iterrarion.next != null) {
            if(Objects.equals(iterrarion.key,key)) {
                return iterrarion.value;
            }
            iterrarion = iterrarion.next;
        }
        return iterrarion.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int setHash(K key) {
        return (key == null) ? 0 :  Math.abs(key.hashCode() % capacity);
    }

    private void initializeArr() {
        if (nodesArray == null) {
            nodesArray = new Node[INITIAL_CAPACITY];
            capacity = INITIAL_CAPACITY;
            return;
        }
            if(size > nodesArray.length*LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {

    }

    private class Node<K, V> {
        int hash;
        K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }


}
