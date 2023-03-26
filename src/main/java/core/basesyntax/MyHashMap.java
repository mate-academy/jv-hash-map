package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int newCapacity = DEFAULT_INITIAL_CAPACITY;
    private Node<K, V>[] table;
    private int trashold;
    private int size;

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value, null);
        int index = getIndex(node.key);
        
        if (size == 0 || size >= trashold) {
            resize();
        }
        
        if (table[index] == null) {
            table[index] = node;
            size++;
            return;
        }
        checkNodeToNext(table, node);
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
    
    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % newCapacity);
    }
    
    private void resize() {
        if (table == null) {
            table = new Node[DEFAULT_INITIAL_CAPACITY];
            trashold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
        if (size >= trashold) {
            newCapacity = newCapacity << 1;
            trashold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
            Node<K, V>[] newTable = new Node[newCapacity];
            table = addNodeToResizeTable(newTable);
        }
    }
    
    private void checkNodeToNext(Node<K, V>[] table, Node<K, V>  node) {
        int index = getIndex(node.key);
        if (table[index] != null && getIndex(table[index].key) == getIndex(node.key)) {
            if (table[index].next == null) {
                table[index].next = node;
                size++;
            } else {
                Node<K, V> newNode = table[index];
                while (newNode.next != null) {
                    if (Objects.equals(newNode.key, node.key)) {
                        newNode.value = node.value;
                        return;
                    }
                    newNode = newNode.next;
                }
                if (Objects.equals(newNode.key, node.key)) {
                    newNode.value = node.value;
                } else {
                    newNode.next = node;
                    size++;
                }
            }
        }
    }
    
    private Node<K, V>[] addNodeToResizeTable(Node<K, V>[] newTable) {
        Node<K, V>[] oldTable = table;
        
        for (int i = 0; i < table.length; i++) {
            if (oldTable[i] != null) {
                int index = getIndex(oldTable[i].key);
                Node<K ,V> newNode = oldTable[i];
                
                if (newTable[index] == null) {
                    newTable[index] = oldTable[i];
                }
                
                if (newNode.next != null) {
                    if (newTable[index] != null && getIndex(newTable[index].key) == getIndex(newNode.key)) {
                        if (newTable[index].next == null) {
                            while (newNode.next != null) {
                                if (Objects.equals(newTable[index].key, newNode.key)) {
                                    newTable[index].value = newNode.value;
                                } else {
                                    newTable[index].next = newNode;
                                }
                                newNode = newNode.next;
                            }
                        }
                    }
                }
            }
        }
        return newTable;
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
    }
    
    @Override
    public String toString() {
        return Arrays.toString(table) +
                ", size=" + size;
    }
}
