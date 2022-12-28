package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * DEFAULT_LOAD_FACTOR) {
            sizeUp();
        }
        Node<K, V> newNode = new Node<>(Math.abs(Objects.hash(key)), key, value, null);
        int positionInArray = Math.abs(Objects.hash(key) % table.length);
        if (positionInArray == 0 && table[0] == null) {
            table[0] = newNode;
            size++;
        } else if (positionInArray == 0 && table[0] != null) {
            for (Node<K, V> nodeInZeroPosition = table[0]; nodeInZeroPosition != null; nodeInZeroPosition = nodeInZeroPosition.next) {
                if (Objects.equals(key,nodeInZeroPosition.key)) {
                    nodeInZeroPosition.value = value;
                    break;
                }
                if (nodeInZeroPosition.next == null) {
                    nodeInZeroPosition.next = newNode;
                    size++;
                    break;
                }
            }
        } else if (table[positionInArray] == null){
            table[positionInArray] = newNode;
            size++;
        } else {
            for (Node<K, V> nodeInZeroPosition = table[positionInArray]; nodeInZeroPosition != null; nodeInZeroPosition = nodeInZeroPosition.next) {
                if (Objects.equals(key,nodeInZeroPosition.key)) {
                    nodeInZeroPosition.value = value;
                    break;
                }
                if (nodeInZeroPosition.next == null) {
                    nodeInZeroPosition.next = newNode;
                    size++;
                    break;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int positionInArray = Math.abs(Objects.hash(key) % table.length);
        for(Node<K, V> node = table[positionInArray]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void sizeUp() {
        Node<K, V>[] tableUp = new Node[table.length * 2];
//        Node<K, V> nodeWithLink;
        for (Node<K, V> node : table) {
            for (Node<K, V> nodeCopy = node; nodeCopy != null; nodeCopy = nodeCopy.next) {
                Node<K, V> newNode = new Node<>(Math.abs(Objects.hash(nodeCopy.key)), nodeCopy.key, nodeCopy.value, null);
                Node<K, V> temporaryNode = tableUp[Math.abs(nodeCopy.hash % tableUp.length)];
                if (temporaryNode == null) {
                    tableUp[Math.abs(nodeCopy.hash % tableUp.length)] = newNode;
                    continue;
                }
                while (temporaryNode.next != null) {
                    temporaryNode = temporaryNode.next;
                }
                temporaryNode.next = newNode;
            }
//            tableUp[table[node.hash % table.length].hash % tableUp.length] = table[node.hash % table.length];
//            nodeWithLink = node;
//            while (nodeWithLink != null) {
//                if (nodeWithLink.hash == 0 && tableUp[0] == null || nodeWithLink.key == null && tableUp[0] == null) {
//                    tableUp[0] = nodeWithLink;
//                    nodeWithLink = nodeWithLink.next;
//                } else if (nodeWithLink.hash == 0 && tableUp[0] != null || nodeWithLink.key == null && tableUp[0] != null) {
//                    for (Node<K, V> nodeInZeroPosition = tableUp[0]; nodeInZeroPosition != null; nodeInZeroPosition = nodeInZeroPosition.next) {
//                        if (nodeInZeroPosition.next == null) {
//                            nodeInZeroPosition.next = nodeWithLink;
//                            nodeWithLink = nodeWithLink.next;
//                            break;
//                        }
//                    }
//                } else if (tableUp[Math.abs(table[Math.abs(nodeWithLink.hash % table.length)].hash % tableUp.length)] != null) {
//                    for (Node<K, V> nodeInZeroPosition = tableUp[Math.abs(table[Math.abs(nodeWithLink.hash % table.length)].hash % tableUp.length)]; nodeInZeroPosition != null; nodeInZeroPosition = nodeInZeroPosition.next) {
//                        if (nodeInZeroPosition.next == null) {
//                            nodeInZeroPosition.next = nodeWithLink;
//                            nodeWithLink = nodeWithLink.next;
//                            break;
//                        }
//                    }
//                } else {
//                    tableUp[Math.abs(table[Math.abs(nodeWithLink.hash % table.length)].hash % tableUp.length)] = table[Math.abs(nodeWithLink.hash % table.length)];
//                    nodeWithLink = nodeWithLink.next;
//                }
//            }
//            do {
//
//            } while (nodeWithLink != null);
        }
        table = tableUp;
    }
}
