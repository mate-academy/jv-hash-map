
package core.basesyntax;

import static java.lang.System.arraycopy;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_VALUE = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_INDEX = 2;
    private Node [] hashMap = new Node[DEFAULT_VALUE];
    private int currentValue = DEFAULT_VALUE;
    private int size = 0;

    public class Node<K,V> {
        private Integer hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size > currentValue * LOAD_FACTOR) {
            resize();
        }
        int hash = hashCode(key);
        int index = Math.abs(hash) % currentValue;
        if (hashMap[index] != null) {
            Node currentNode = hashMap[index];
            do {
                if (currentNode.hash == hash && ((key == null && currentNode.key == null)
                        || (Objects.equals(key, currentNode.key)))) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next != null) {
                    currentNode = currentNode.next;
                }
            } while (currentNode.next != null);
            if (currentNode.hash == hash && ((key == null && currentNode.key == null)
                    || (Objects.equals(key, currentNode.key)))) {
                currentNode.value = value;
                return;
            }
            Node newNode = new Node<>(hash, key, value, null);
            currentNode.next = newNode;
            size++;
            return;
        }
        hashMap[index] = new Node(hash, key, value, null);
        size++;
    }

    public void resize() {
        Node [] copyHashMap = new Node[currentValue];
        arraycopy(hashMap, 0, copyHashMap, 0, hashMap.length);
        currentValue = currentValue * RESIZE_INDEX;
        Node [] hashMap = new Node[currentValue];
        this.hashMap = hashMap;
        size = 0;
        for (int i = 0; i < copyHashMap.length; i++) {
            if (copyHashMap[i] != null) {
                Node currentNode = copyHashMap[i];
                do {
                    put((K) currentNode.key, (V) currentNode.value);
                    if (currentNode.next != null) {
                        currentNode = currentNode.next;
                    } else {
                        break;
                    }
                } while (copyHashMap[i].next != null);
            }
        }
    }

    @Override
    public int hashCode(Object key) {
        int result;
        return (key == null) ? 0 : (result = key.hashCode()) ^ (result >>> 16);
    }

    @Override
    public V getValue(K key) {
        for (int i = 0; i < currentValue; i++) {
            if (hashMap[i] != null) {
                if (key == null) {
                    i = 0;
                }
                if (Objects.equals(key, hashMap[i].key)) {
                    return (V) hashMap[i].value;
                } else if (hashMap[i].next != null) {
                    Node currentNode = hashMap[i].next;
                    while (currentNode != null) {
                        if (Objects.equals(key, currentNode.key)) {
                            return (V) currentNode.value;
                        }
                        currentNode = currentNode.next;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
