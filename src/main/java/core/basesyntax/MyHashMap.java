package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 1<<4;
    private static final float DEFAULT_LOAD_FACTOR=0.75f;
    private int threshold = (int)(INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private Node<K, V>[] table;
    int size;

private class Node<K,V>{
    private K key;
    private V value;
    private int hash;
    private Node<K,V> nextNode;


    public Node(K key,V value, int hash, Node<K,V> nextNode) {
        this.key=key;
        this.value=value;
        this.hash=hash;
        this.nextNode=nextNode;
    }
}
    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
     if(table==null){
         System.out.println("Table null");
         table=new Node[INITIAL_CAPACITY];
         table[0]=new Node<>(key,value,hashCode(),null);
         System.out.println("We put Node  first "+table[0].value+" and key: "+table[0].key);
         size++;
     }else{
         if(checkContainsKey(key)){
             System.out.println("The same key already  exist in table "+key);
             Node<K,V> currentNode=getNode(key);
             currentNode.value=value;
         }else{
             System.out.println("The same key dont exist "+key);
             int index=getHashCode(key)%table.length;
             if(table[index]==null){
                table[index]= new Node<>(key,value,hashCode(),null);
                size++;
             }else{
                    Node<K,V> nodeByIndex=table[index];
                    while ((nodeByIndex.nextNode!=null)){
                        nodeByIndex=nodeByIndex.nextNode;
                 }
                    nodeByIndex.nextNode=new Node<>(key,value,hashCode(),null);
             }
         }
     }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> currentNode=getNode(key);
        if(currentNode==null){
            return null;
        }
        return currentNode.value;
    }

    @Override
    public int getSize() {
    return size;
    }
    private int getHashCode(K key){
    return key==null?0:Math.abs(Objects.hash(key));
    }
    private boolean checkContainsKey(K key) {
    Node<K,V>currentNode =getNode(key);
     if(currentNode!=null){
         return true;
     }
     return false;
    }
    private Node<K,V> getNode(K key){
    if(table!=null){
        Node<K,V> selectedNode;
        for(int a=0;a<table.length;a++){
            selectedNode=table[a];
            while ((selectedNode!=null)){
                if((selectedNode.key==null&&key==null)){
                    return selectedNode;
                }
                if(selectedNode.key!=null&&selectedNode.key.equals(key)){
                    return selectedNode;
                }
                selectedNode=selectedNode.nextNode;
            }
        }
        }
    return null;
    }
    private void resize(){
    Node<K,V>[] oldTable=table;
    table=new Node[oldTable.length*2];
    for(Node<K,V> tbl:oldTable){
        while (tbl!=null){
            put(tbl.key,tbl.value);
            tbl=tbl.nextNode;
        }
    }
    }
}
