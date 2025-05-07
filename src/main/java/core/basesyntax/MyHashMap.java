package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private MyNode<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new MyNode[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (getNode(key) != null) {
            replaceValue(key, value);
        } else {
            addToBucket(key, value);
            size++;
        }
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        MyNode<K, V> currentNode = getNode(key);
        return currentNode == null ? null : currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "<empty map>";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(":");
        for (MyNode<K, V> myNode : table) {
            while (myNode != null) {
                sb.append(" ");
                sb.append(myNode.key).append("=").append(myNode.value);
                myNode = myNode.next;
            }
        }
        return sb.toString();
    }

    private void addToBucket(K key, V value) {
        addToBucket(new MyNode<>(keyHash(key), key, value));
    }

    private void addToBucket(MyNode<K, V> myNode) {
        MyNode<K, V> firstNode = table[bucketIndex(myNode.hashcode)];
        if (firstNode == null) {
            table[bucketIndex(myNode.hashcode)] = myNode;
        } else {
            getLastNode(firstNode).next = myNode;
        }
    }

    private MyNode<K, V> getLastNode(MyNode<K, V> myNode) {
        while (myNode.next != null) {
            myNode = myNode.next;
        }
        return myNode;
    }

    private int bucketIndex(int hashCode) {
        return hashCode & (table.length - 1);
    }

    private int keyHash(K key) {
        return Objects.hashCode(key);
    }

    private MyNode<K, V> getNode(K key) {
        MyNode<K, V> currentNode = table[bucketIndex(keyHash(key))];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    private void resize() {
        MyNode<K, V>[] copyTable = table;
        MyNode<K, V> nextNode;
        table = new MyNode[table.length << 1];
        for (MyNode<K, V> myNode : copyTable) {
            while (myNode != null) {
                nextNode = myNode.next;
                myNode.next = null;
                addToBucket(myNode);
                myNode = nextNode;
            }
        }
    }

    private void replaceValue(K key, V value) {
        getNode(key).value = value;
    }

    private class MyNode<K, V> {
        private int hashcode;
        private K key;
        private V value;
        private MyNode<K, V> next;

        public MyNode(int hashcode, K key, V value) {
            this.hashcode = hashcode;
            this.key = key;
            this.value = value;
        }
    }
}
